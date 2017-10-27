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

package org.openbaton.tosca.templates.TopologyTemplate.Nodes.VNFFG;

import java.util.*;

/** Created by rvl on 19.08.16. */
public class VNFFGProperties {

  private String vendor = null;
  private String ID = null;
  private String version = "";
  private Boolean symmetrical = null;

  private List<String> cp;
  private List<String> vl;
  private List<String> vnf;

  public VNFFGProperties() {}

  @SuppressWarnings({"unsafe", "unchecked"})
  public VNFFGProperties(Object properties) {

    Map<String, Object> propertiesMap = (Map<String, Object>) properties;

    if (propertiesMap.containsKey("vendor")) {
      vendor = (String) propertiesMap.get("vendor");
    }
    if (propertiesMap.containsKey("version")) {
      version = String.valueOf(propertiesMap.get("version"));
    }

    if (propertiesMap.containsKey("symmetrical")) {
      this.symmetrical = (Boolean) propertiesMap.get("symmetrical");
    }

    if (propertiesMap.containsKey("cp")) {
      //set cp
      cp = (List<String>) propertiesMap.get("cp");
    }
    if (propertiesMap.containsKey("vl")) {
      //set vl
      vl = (List<String>) propertiesMap.get("vl");
    }
    if (propertiesMap.containsKey("vnf")) {
      //set vnf
      vnf = (List<String>) propertiesMap.get("vnf");
    }

    if (propertiesMap.containsKey("ID")) {
      ID = (String) propertiesMap.get("ID");
    }
  }

  public Boolean getSymmetrical() {
    return symmetrical;
  }

  public void setSymmetrical(Boolean symmetrical) {
    this.symmetrical = symmetrical;
  }

  public String getVendor() {
    return vendor;
  }

  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public List<String> getCp() {
    return cp;
  }

  public void setCp(List<String> cp) {
    this.cp = cp;
  }

  public List<String> getVl() {
    return vl;
  }

  public void setVl(List<String> vl) {
    this.vl = vl;
  }

  public List<String> getVnf() {
    return vnf;
  }

  public void setVnf(List<String> vnf) {
    this.vnf = vnf;
  }
}
