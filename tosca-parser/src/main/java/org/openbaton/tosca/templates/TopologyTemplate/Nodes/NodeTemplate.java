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

package org.openbaton.tosca.templates.TopologyTemplate.Nodes;

import java.util.List;

/** Created by rvl on 17.08.16. */
public class NodeTemplate {

  private String type;
  private Object capabilities = null;
  private Object properties = null;
  private Object requirements = null;
  private Object attributes = null;
  private Object artifacts = null;
  private Object interfaces = null;

  //added for VNFFG
  private String description = null;
  private List<String> members = null;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setProperties(Object properties) {
    this.properties = properties;
  }

  public Object getProperties() {
    return this.properties;
  }

  public void setRequirements(Object requirements) {
    this.requirements = requirements;
  }

  public Object getRequirements() {
    return this.requirements;
  }

  public void setAttributes(Object attributes) {
    this.attributes = attributes;
  }

  public Object getAttributes() {
    return this.attributes;
  }

  public Object getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(Object capabilities) {
    this.capabilities = capabilities;
  }

  public Object getArtifacts() {
    return artifacts;
  }

  public void setArtifacts(Object artifacts) {
    this.artifacts = artifacts;
  }

  public Object getInterfaces() {
    return interfaces;
  }

  public void setInterfaces(Object interfaces) {
    this.interfaces = interfaces;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getMembers() {
    return members;
  }

  public void setMembers(List<String> members) {
    this.members = members;
  }

  @Override
  public String toString() {
    return "NodeTemplate{"
        + "type='"
        + type
        + '\''
        + ", capabilities="
        + capabilities
        + ", properties="
        + properties
        + ", requirements="
        + requirements
        + ", attributes="
        + attributes
        + ", artifacts="
        + artifacts
        + ", interfaces="
        + interfaces
        + ", description='"
        + description
        + '\''
        + ", members="
        + members
        + '}';
  }
}
