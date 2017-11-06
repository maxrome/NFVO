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

package org.openbaton.tosca.templates.TopologyTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.openbaton.tosca.exceptions.NotFoundException;
import org.openbaton.tosca.exceptions.ParseException;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.CP.CPNodeTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.FP.FPTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.NodeTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.VDU.VDUNodeTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.VL.VLNodeTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.VNF.VNFNodeTemplate;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.VNFFG.VNFFGTemplate;

/** Created by rvl on 17.08.16. */
public class TopologyTemplate {

  private Object inputs = null;
  private Map<String, NodeTemplate> node_templates;

  public Object getInputs() {
    return inputs;
  }

  public void setInputs(Object inputs) {
    this.inputs = inputs;
  }

  public Map<String, NodeTemplate> getNode_templates() {
    return node_templates;
  }

  public void setNode_templates(Map<String, NodeTemplate> node_templates) {
    this.node_templates = node_templates;
  }

  public List<CPNodeTemplate> getCPNodes() {

    List<CPNodeTemplate> cpNodes = new ArrayList<>();

    for (String nodeName : node_templates.keySet()) {

      NodeTemplate n = node_templates.get(nodeName);
      if (Objects.equals(n.getType().toLowerCase(), "tosca.nodes.nfv.cp")) {

        CPNodeTemplate cpNode = new CPNodeTemplate(n, nodeName);
        cpNodes.add(cpNode);
      }
    }
    return cpNodes;
  }

  public List<VDUNodeTemplate> getVDUNodes() {

    List<VDUNodeTemplate> vduNodes = new ArrayList<>();

    for (String nodeName : node_templates.keySet()) {

      NodeTemplate n = node_templates.get(nodeName);
      if (Objects.equals(n.getType().toLowerCase(), "tosca.nodes.nfv.vdu")) {

        VDUNodeTemplate vduNode = new VDUNodeTemplate(n, nodeName);
        vduNodes.add(vduNode);
      }
    }

    return vduNodes;
  }

  public List<VLNodeTemplate> getVLNodes() {

    List<VLNodeTemplate> vlNodes = new ArrayList<>();

    for (String nodeName : node_templates.keySet()) {

      NodeTemplate n = node_templates.get(nodeName);
      if (Objects.equals(n.getType().toLowerCase(), "tosca.nodes.nfv.vl")) {
        VLNodeTemplate vduNode = new VLNodeTemplate(n, nodeName);
        vlNodes.add(vduNode);
      }
    }

    return vlNodes;
  }

  public List<VNFNodeTemplate> getVNFNodes() throws NotFoundException {

    List<VNFNodeTemplate> vnfNodes = new ArrayList<>();

    for (String nodeName : node_templates.keySet()) {

      NodeTemplate n = node_templates.get(nodeName);
      if (Objects.equals(n.getType().toLowerCase(), "openbaton.type.vnf")) {

        VNFNodeTemplate vnfNode = new VNFNodeTemplate(n, nodeName);
        vnfNodes.add(vnfNode);
      }
    }

    return vnfNodes;
  }

  public List<VNFFGTemplate> getVNFFGNodes() throws NotFoundException {
    List<VNFFGTemplate> vnffgNodes = new ArrayList<VNFFGTemplate>();
    for (String nodeName : node_templates.keySet()) {
      NodeTemplate n = node_templates.get(nodeName);
      if (Objects.equals(n.getType(), "tosca.groups.nfv.VNFFG")) {

        VNFFGTemplate vnfFFGNode = new VNFFGTemplate(n, nodeName);
        vnffgNodes.add(vnfFFGNode);
      }
    }
    return vnffgNodes;
  }

  public List<FPTemplate> getFPNodes() throws NotFoundException, ParseException {
    List<FPTemplate> fpNodes = new ArrayList<FPTemplate>();
    for (String nodeName : node_templates.keySet()) {
      NodeTemplate n = node_templates.get(nodeName);
      if (Objects.equals(n.getType(), "tosca.nodes.nfv.FP")) {

        FPTemplate fpNode = new FPTemplate(n, nodeName);
        fpNodes.add(fpNode);
      }
    }
    return fpNodes;
  }

  @Override
  public String toString() {
    return "Topology: \n" + "inuts: " + inputs + "\n" + "Nodes: \n" + node_templates;
  }
}
