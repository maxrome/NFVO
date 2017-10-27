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

import java.util.List;
import org.openbaton.tosca.exceptions.NotFoundException;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.NodeTemplate;

/** Created by rvl on 19.08.16. */
public class VNFFGTemplate {

  private String type = "";
  private String name = "";
  private String description = "";
  private VNFFGProperties properties = null;
  private List<String> members;

  @SuppressWarnings({"unsafe", "unchecked"})
  public VNFFGTemplate(NodeTemplate nodeTemplate, String nodeName) throws NotFoundException {

    this.name = nodeName;
    this.type = nodeTemplate.getType();
    this.members = nodeTemplate.getMembers();

    if (nodeTemplate.getProperties() == null)
      throw new NotFoundException(
          "You should specify at least 'cp', 'vl', 'vnf' in properties for VNFFG: ");
    properties = new VNFFGProperties(nodeTemplate.getProperties());
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public VNFFGProperties getProperties() {
    return properties;
  }

  public void setProperties(VNFFGProperties properties) {
    this.properties = properties;
  }

  public List<String> getMembers() {
    return members;
  }

  public void setMembers(List<String> members) {
    this.members = members;
  }

  @Override
  public String toString() {
    return "VNFFGTemplate{"
        + "type='"
        + type
        + '\''
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", properties="
        + properties
        + ", members="
        + members
        + '}';
  }
}
