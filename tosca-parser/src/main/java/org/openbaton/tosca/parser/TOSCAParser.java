/*
 * Copyright (c) 2016 Open Baton (http://www.openbaton.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.openbaton.tosca.parser;

import java.util.*;
import org.openbaton.catalogue.mano.descriptor.*;
import org.openbaton.catalogue.nfvo.Configuration;
import org.openbaton.catalogue.nfvo.ConfigurationParameter;
import org.openbaton.tosca.exceptions.NotFoundException;
import org.openbaton.tosca.exceptions.ParseException;
import org.openbaton.tosca.templates.NSDTemplate;
import org.openbaton.tosca.templates.RelationshipsTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.CP.CPNodeTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.FP.FPACLCriteria;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.FP.FPPolicy;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.FP.FPTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.VDU.VDUNodeTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.VL.VLNodeTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.VNF.VNFConfigurations;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.VNF.VNFNodeTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.VNFFG.VNFFGTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.TopologyTemplate;
import org.openbaton.tosca.templates.VNFDTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** Created by rvl on 17.08.16. */
@Service
public class TOSCAParser {

  public TOSCAParser() {}

  private Logger log = LoggerFactory.getLogger(TOSCAParser.class);

  /**
   * Parser of the Virtual Link
   *
   * @param vlNodeTemplate
   * @return
   */
  private InternalVirtualLink parseVL(VLNodeTemplate vlNodeTemplate) {

    InternalVirtualLink vl = new InternalVirtualLink();
    vl.setName(vlNodeTemplate.getName());

    return vl;
  }

  /**
   * Parser of the Connection Point
   *
   * @param cpTemplate
   * @return
   */
  private VNFDConnectionPoint parseCPTemplate(CPNodeTemplate cpTemplate) {

    VNFDConnectionPoint cp = new VNFDConnectionPoint();
    cp.setVirtual_link_reference(cpTemplate.getRequirements().getVirtualLink().get(0));

    if (cpTemplate.getProperties() != null) {
      if (cpTemplate.getProperties().getFloatingIP() != null) {
        cp.setFloatingIp(cpTemplate.getProperties().getFloatingIP());
      }
      cp.setInterfaceId(cpTemplate.getProperties().getInterfaceId());
    }

    return cp;
  }

  /**
   * Parser of the Virtual Deployment Unit
   *
   * @param vduTemplate
   * @param cps
   * @return
   */
  private VirtualDeploymentUnit parseVDUTemplate(
      VDUNodeTemplate vduTemplate, List<CPNodeTemplate> cps) {

    VirtualDeploymentUnit vdu = new VirtualDeploymentUnit();
    vdu.setName(vduTemplate.getName());

    // ADD Settings
    vdu.setScale_in_out(vduTemplate.getProperties().getScale_in_out());
    vdu.setVm_image(vduTemplate.getArtifacts());
    if (vduTemplate.getProperties().getFault_management_policy() != null) {
      vdu.setFault_management_policy(
          vduTemplate.getProperties().getFault_management_policy().getFaultManagementPolicies());
    }

    vdu.setVimInstanceName(vduTemplate.getProperties().getVim_instance_name());

    // ADD VNF Connection Points
    Set<VNFComponent> vnfComponents = new HashSet<>();
    VNFComponent vnfc = new VNFComponent();
    Set<VNFDConnectionPoint> connectionPoints = new HashSet<>();

    for (CPNodeTemplate cp : cps) {
      if (cp.getRequirements().getVirtualBinding().contains(vduTemplate.getName())) {
        connectionPoints.add(parseCPTemplate(cp));
      }
    }

    vnfc.setConnection_point(connectionPoints);
    vnfComponents.add(vnfc);
    vdu.setVnfc(vnfComponents);

    return vdu;
  }

  /**
   * Parser of the relationship template
   *
   * @param nsd
   * @param relationshipsTemplates
   */
  private void parseRelationships(
      NetworkServiceDescriptor nsd, Map<String, RelationshipsTemplate> relationshipsTemplates) {
    if (relationshipsTemplates == null) return;
    for (String key : relationshipsTemplates.keySet()) {
      VNFDependency vnfDependency = new VNFDependency();

      RelationshipsTemplate relationshipsTemplate = relationshipsTemplates.get(key);

      vnfDependency.setSource(relationshipsTemplate.getSource());
      vnfDependency.setTarget(relationshipsTemplate.getTarget());
      vnfDependency.setParameters(new HashSet<>(relationshipsTemplate.getParameters()));

      nsd.getVnf_dependency().add(vnfDependency);
    }
  }

  /**
   * Parser of the VNF Node
   *
   * @param vnf
   * @param topologyTemplate
   * @return
   */
  private VirtualNetworkFunctionDescriptor parseVNFNode(
      VNFNodeTemplate vnf, TopologyTemplate topologyTemplate) throws NotFoundException {

    VirtualNetworkFunctionDescriptor vnfd = new VirtualNetworkFunctionDescriptor();

    vnfd.setName(vnf.getName());
    vnfd.setVendor(vnf.getProperties().getVendor());
    vnfd.setVersion(vnf.getProperties().getVersion());

    vnfd.setDeployment_flavour(vnf.getProperties().getDeploymentFlavourConverted());
    vnfd.setVnfPackageLocation(vnf.getProperties().getVnfPackageLocation());
    if (vnf.getProperties().getEndpoint() == null)
      throw new NotFoundException("No endpoint specified in properties for VNF: " + vnfd.getName());
    vnfd.setEndpoint(vnf.getProperties().getEndpoint());
    if (vnf.getProperties().getType() == null)
      throw new NotFoundException("No type specified in properties for VNF: " + vnfd.getName());
    vnfd.setType(vnf.getProperties().getType());

    if (vnf.getProperties().getAuto_scale_policy() != null) {
      vnfd.setAuto_scale_policy(vnf.getProperties().getAuto_scale_policy().getAutoScalePolicySet());
    }

    ArrayList<String> vduList = vnf.getRequirements().getVDUS();

    // ADD VDUs
    Set<VirtualDeploymentUnit> vdus = new HashSet<>();

    for (VDUNodeTemplate vdu : topologyTemplate.getVDUNodes()) {
      if (vduList.contains(vdu.getName())) {
        vdus.add(parseVDUTemplate(vdu, topologyTemplate.getCPNodes()));
      }
    }
    vnfd.setVdu(vdus);

    Set<String> virtualLinkReferences = new HashSet<>();

    for (VirtualDeploymentUnit vdu : vdus) {
      for (VNFComponent vnfComponent : vdu.getVnfc()) {
        for (VNFDConnectionPoint cp : vnfComponent.getConnection_point()) {
          if (cp.getVirtual_link_reference() != null)
            virtualLinkReferences.add(cp.getVirtual_link_reference());
        }
      }
    }

    // ADD VLs
    //ArrayList<String> vl_list = vnf.getRequirements().getVirtualLinks();
    Set<InternalVirtualLink> vls = new HashSet<>();

    for (VLNodeTemplate vl : topologyTemplate.getVLNodes()) {

      if (virtualLinkReferences.contains(vl.getName())) {
        vls.add(parseVL(vl));
      }
    }
    vnfd.setVirtual_link(vls);
    vnfd.setLifecycle_event(vnf.getInterfaces().getOpLifecycle());

    //ADD CONFIGURATIONS
    if (vnf.getProperties().getConfigurations() != null) {
      Configuration configuration = new Configuration();
      configuration.setName(vnf.getProperties().getConfigurations().getName());

      Set<ConfigurationParameter> configurationParameters = new HashSet<>();

      for (HashMap<String, String> pair :
          vnf.getProperties().getConfigurations().getConfigurationParameters()) {

        ConfigurationParameter configurationParameter = new ConfigurationParameter();
        configurationParameter.setConfKey((String) pair.keySet().toArray()[0]);
        configurationParameter.setValue((String) pair.values().toArray()[0]);
        configurationParameters.add(configurationParameter);
      }

      configuration.setConfigurationParameters(configurationParameters);
      vnfd.setConfigurations(configuration);
    }

    return vnfd;
  }

  /**
   * Parser of the VNF template
   *
   * @param VNFDTemplate
   * @return
   */
  public VirtualNetworkFunctionDescriptor parseVNFDTemplate(VNFDTemplate VNFDTemplate)
      throws NotFoundException {

    VirtualNetworkFunctionDescriptor vnfd = new VirtualNetworkFunctionDescriptor();

    // ADD SETTINGS
    if (VNFDTemplate.getMetadata() == null)
      throw new NotFoundException("The VNFD Template must have contain metadata child!");
    vnfd.setName(VNFDTemplate.getMetadata().getID());
    vnfd.setVendor(VNFDTemplate.getMetadata().getVendor());
    vnfd.setVersion(VNFDTemplate.getMetadata().getVersion());

    if (VNFDTemplate.getInputs() == null)
      throw new NotFoundException(
          "You should specify at least endpoint, deployment_flavour and type in inputs");
    vnfd.setDeployment_flavour(VNFDTemplate.getInputs().getDeploymentFlavourConverted());
    vnfd.setVnfPackageLocation(VNFDTemplate.getInputs().getVnfPackageLocation());
    if (VNFDTemplate.getInputs().getEndpoint() == null)
      throw new NotFoundException("No endpoint specified in inputs!");
    vnfd.setEndpoint(VNFDTemplate.getInputs().getEndpoint());
    if (VNFDTemplate.getInputs().getType() == null)
      throw new NotFoundException("No type specified in inputs!");
    vnfd.setType(VNFDTemplate.getInputs().getType());

    // ADD VDUs
    Set<VirtualDeploymentUnit> vdus = new HashSet<>();
    for (VDUNodeTemplate vdu : VNFDTemplate.getTopology_template().getVDUNodes()) {
      vdus.add(parseVDUTemplate(vdu, VNFDTemplate.getTopology_template().getCPNodes()));
    }
    vnfd.setVdu(vdus);

    // ADD VLs
    Set<InternalVirtualLink> vls = new HashSet<>();

    for (VLNodeTemplate vl : VNFDTemplate.getTopology_template().getVLNodes()) {

      vls.add(parseVL(vl));
    }

    vnfd.setVirtual_link(vls);
    vnfd.setLifecycle_event(VNFDTemplate.getInputs().getInterfaces().getOpLifecycle());

    //ADD CONFIGURATIONS
    if (VNFDTemplate.getInputs().getConfigurations() != null) {

      VNFConfigurations configurations = VNFDTemplate.getInputs().getConfigurations();

      Configuration configuration = new Configuration();
      configuration.setName(configurations.getName());

      Set<ConfigurationParameter> configurationParameters = new HashSet<>();

      for (HashMap<String, String> pair : configurations.getConfigurationParameters()) {

        ConfigurationParameter configurationParameter = new ConfigurationParameter();
        configurationParameter.setConfKey((String) pair.keySet().toArray()[0]);
        configurationParameter.setValue((String) pair.values().toArray()[0]);
        configurationParameters.add(configurationParameter);
      }

      configuration.setConfigurationParameters(configurationParameters);
      vnfd.setConfigurations(configuration);
    }

    return vnfd;
  }

  /**
   * Parser of the NSD template
   *
   * @param nsdTemplate
   * @return
   */
  public NetworkServiceDescriptor parseNSDTemplate(NSDTemplate nsdTemplate)
      throws ParseException, NotFoundException {

    NetworkServiceDescriptor nsd = new NetworkServiceDescriptor();

    if (nsdTemplate.getMetadata() == null)
      throw new NotFoundException("The NSD Template must have a metadata child!");
    nsd.setVersion(nsdTemplate.getMetadata().getVersion());
    nsd.setVendor(nsdTemplate.getMetadata().getVendor());
    nsd.setName(nsdTemplate.getMetadata().getID());

    // ADD VNFDS

    for (VNFNodeTemplate vnfNodeTemplate : nsdTemplate.getTopology_template().getVNFNodes()) {

      VirtualNetworkFunctionDescriptor vnf =
          parseVNFNode(vnfNodeTemplate, nsdTemplate.getTopology_template());

      nsd.getVnfd().add(vnf);
    }

    // ADD VLS
    nsd.setVld(new HashSet<VirtualLinkDescriptor>());

    for (VLNodeTemplate vlNode : nsdTemplate.getTopology_template().getVLNodes()) {

      VirtualLinkDescriptor vld = new VirtualLinkDescriptor();
      vld.setName(vlNode.getName());
      if (vlNode.getQos() != null) vld.setQos(vlNode.getQos());
      nsd.getVld().add(vld);
    }

    // ADD VNFFG
    nsd.setVnffgd(parseVNFFG(nsdTemplate));

    // ADD DEPENDENCIES
    parseRelationships(nsd, nsdTemplate.getRelationships_template());

    return nsd;
  }

  private Set<VNFForwardingGraphDescriptor> parseVNFFG(NSDTemplate nsdTosca)
      throws NotFoundException, ParseException {
    List<VNFFGTemplate> vnffgList = nsdTosca.getTopology_template().getVNFFGNodes();
    if (vnffgList.size() == 0) return null;

    //  Map < VDU , VNF >
    Map<String, String> vduVnfMap = new HashMap<String, String>();
    List<VNFNodeTemplate> vnfList = nsdTosca.getTopology_template().getVNFNodes();
    for (VNFNodeTemplate vnf : vnfList) {
      for (String vduName : vnf.getRequirements().getVDUS()) {
        if (vduVnfMap.get(vduName) != null) {
          throw new ParseException(
              String.format(
                  "VDU '%s' is associated with more than one VNF, but to use VNFFG you should associate each VDU to only one VNF",
                  vduName));
        }
        vduVnfMap.put(vduName, vnf.getName());
      }
    }

    //Map creation for fast fetching
    Map<String, VNFFGTemplate> vnffgMap = new HashMap<String, VNFFGTemplate>();
    Map<String, CPNodeTemplate> cpMap = new HashMap<String, CPNodeTemplate>();
    Map<String, FPTemplate> fpMap = new HashMap<String, FPTemplate>();

    List<FPTemplate> fpList = nsdTosca.getTopology_template().getFPNodes();
    List<CPNodeTemplate> cpList = nsdTosca.getTopology_template().getCPNodes();

    for (VNFFGTemplate vnffg : vnffgList) {
      vnffgMap.put(vnffg.getName(), vnffg);
    }

    for (CPNodeTemplate cp : cpList) {
      cpMap.put(cp.getName(), cp);
    }

    for (FPTemplate fp : fpList) {
      fpMap.put(fp.getName(), fp);
    }

    // START PARSING VNFFG

    Set<VNFForwardingGraphDescriptor> vnffgSet = new HashSet<VNFForwardingGraphDescriptor>();

    for (VNFFGTemplate vnffgTemplate : vnffgList) {
      log.debug("creating VNFFGD");

      VNFForwardingGraphDescriptor vnffgd = new VNFForwardingGraphDescriptor();
      vnffgd.setVendor(vnffgTemplate.getProperties().getVendor());
      vnffgd.setVersion(vnffgTemplate.getProperties().getVersion());
      if (vnffgTemplate.getProperties().getSymmetrical() != null) {
        vnffgd.setSymmetricity(vnffgTemplate.getProperties().getSymmetrical());
      } else {
        vnffgd.setSymmetricity(false);
      }

      //TODO set number of endpoints and symmetrical

      if (vnffgTemplate.getMembers().size() == 0)
        throw new ParseException(
            String.format(
                "VNFFG with name'%s' should have at least one forwarding path in members list",
                vnffgTemplate.getName()));

      for (String fpName : vnffgTemplate.getMembers()) {
        FPTemplate fpTemplate = fpMap.get(fpName);
        if (fpTemplate == null) {
          throw new NotFoundException(
              String.format(
                  "Cannot find FP with name '%s' listed in 'members' of VNFFG with name '%s'",
                  fpName, vnffgTemplate.getName()));
        }

        //building the NetworkForwardingPath
        NetworkForwardingPath nfp = new NetworkForwardingPath();

        //building the 'connections' array
        for (int pathIndex = 0;
            pathIndex < fpTemplate.getRequirements().getForwarderList().size();
            pathIndex++) {
          //for (String cpName : fpTemplate.getRequirements().getForwarderList()){
          String cpName = fpTemplate.getRequirements().getForwarderList().get(pathIndex);
          CPNodeTemplate cp = cpMap.get(cpName);
          if (cp == null) {
            throw new NotFoundException(
                String.format(
                    "Cannot find CP with name '%s' listed in requirements of '%s'",
                    cpName, fpTemplate.getName()));
          }
          if (cp.getRequirements().getVirtualBinding() == null
              || cp.getRequirements().getVirtualBinding().size() == 0) {
            throw new NotFoundException(
                String.format(
                    "CP '%s' must have a 'virtualBinding' in requirements to use VNFFG",
                    cp.getName()));
          }
          if (cp.getRequirements().getVirtualBinding().size() > 1) {
            throw new NotFoundException(
                String.format(
                    "CP '%s' should have only 1 'virtualBinding' in requirements to use VNFFG",
                    cp.getName()));
          }

          //get the first CP virtualBinding
          String vduName = cp.getRequirements().getVirtualBinding().get(0);

          String vnfName = vduVnfMap.get(vduName);
          if (vnfName == null) {
            throw new NotFoundException(
                String.format("VNF '%s' referenced in CP '' not found in descriptor"));
          }

          if (cp.getRequirements().getVirtualLink() == null
              || cp.getRequirements().getVirtualLink().size() == 0) {
            throw new NotFoundException(
                String.format(
                    "CP '%s' must have a 'virtualLink' in requirements to use VNFFG",
                    cp.getName()));
          }
          if (cp.getRequirements().getVirtualLink().size() > 1) {
            throw new NotFoundException(
                String.format(
                    "CP '%s' should have only 1 'virtualLink' in requirements to use VNFFG",
                    cp.getName()));
          }

          //get the first virtual link
          String virtualLinkName = cp.getRequirements().getVirtualLink().get(0);

          //put vnfName and virtualLinkName in the network forwarding path
          Connection conn = new Connection();
          conn.setVnf_name(vnfName);
          conn.setVirtual_link(virtualLinkName);
          conn.setPathIndex(pathIndex);
          nfp.getConnections().add(conn);
        }

        //building the policy
        FPPolicy policy = fpTemplate.getProperties().getPolicy();
        if (policy == null)
          throw new NotFoundException(
              String.format("Cannot find any 'policy' in 'properties' of FP '%s'", fpName));

        FPACLCriteria toscaACL = policy.getAclCriteria();
        if (toscaACL == null) {
          throw new NotFoundException(
              String.format("Cannot find any 'criteria' in 'policy' of FP '%s'", fpName));
        }

        Policy p = new Policy();
        ACLMatchingCriteria catalogueACL = new ACLMatchingCriteria();

        if (toscaACL.getIpProto() == null || toscaACL.getIpProto().isEmpty()) {
          throw new ParseException(
              String.format("Cannot find 'ip_proto' in 'policy' of FP '%s'", fpName));
        }
        catalogueACL.setProtocol(protocolString2Int(toscaACL.getIpProto()));

        catalogueACL.setDestinationIPPrefix(toscaACL.getIpDstPrefix());
        catalogueACL.setDestinationPortMin(toscaACL.getDestPortMin());
        catalogueACL.setDestinationPortMax(toscaACL.getDestPortMax());
        catalogueACL.setSourceIPPrefix(toscaACL.getIpSrcPrefix());
        catalogueACL.setSourcePortMin(toscaACL.getSrcPortMin());
        catalogueACL.setSourcePortMax(toscaACL.getSrcPortMax());

        p.setAcl_matching_criteria(catalogueACL);

        nfp.setPolicy(p);

        vnffgd.getNetwork_forwarding_path().add(nfp);
      }

      vnffgSet.add(vnffgd);
    }

    return vnffgSet;
  }

  private int protocolString2Int(String protocol) throws ParseException {

    String proto = protocol.toLowerCase();

    if (proto.equals("icmp")) {
      return 1;
    } else if (proto.equals("tcp")) {
      return 6;
    } else if (proto.equals("udp")) {
      return 17;
    } else {
      throw new ParseException(
          String.format(
              "'%s' is not a valid protocol, allowed values are 'icmp','tcp','udp'", protocol));
    }
  }
}
