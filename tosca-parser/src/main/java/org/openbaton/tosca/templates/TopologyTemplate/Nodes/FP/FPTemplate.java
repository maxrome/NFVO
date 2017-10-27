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

package org.openbaton.tosca.templates.TopologyTemplate.Nodes.FP;

import org.openbaton.tosca.exceptions.NotFoundException;
import org.openbaton.tosca.exceptions.ParseException;
import org.openbaton.tosca.templates.TopologyTemplate.Nodes.NodeTemplate;

/*FORWARDING PATH */

public class FPTemplate {

  private String type = "";
  private String name = "";
  private String description = "";
  private FPProperties properties = null;
  private FPForwarderList requirements;

  @SuppressWarnings({"unsafe", "unchecked"})
  public FPTemplate(NodeTemplate nodeTemplate, String nodeName)
      throws NotFoundException, ParseException {

    this.name = nodeName;
    this.type = nodeTemplate.getType();

    if (nodeTemplate.getProperties() == null)
      throw new NotFoundException("You should specify at least a 'policy'");
    properties = new FPProperties(nodeTemplate.getProperties());

    if (nodeTemplate.getRequirements() == null)
      throw new NotFoundException("you should specify at least 2 'forwarder' in requirements");
    requirements = new FPForwarderList(nodeTemplate.getRequirements());
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public FPProperties getProperties() {
    return properties;
  }

  public void setProperties(FPProperties properties) {
    this.properties = properties;
  }

  public FPForwarderList getRequirements() {
    return requirements;
  }

  public void setRequirements(FPForwarderList requirements) {
    this.requirements = requirements;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "FPTemplate{"
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
        + ", requirements="
        + requirements
        + '}';
  }
}
