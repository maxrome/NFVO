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

import java.util.Map;
import org.openbaton.tosca.exceptions.ParseException;

/** Created by rvl on 19.08.16. */
public class FPProperties {

  private FPPolicy policy = null;

  public FPProperties() {}

  @SuppressWarnings({"unsafe", "unchecked"})
  public FPProperties(Object properties) throws ParseException {

    Map<String, Object> propertiesMap = (Map<String, Object>) properties;

    if (propertiesMap.containsKey("policy")) {
      policy = new FPPolicy(propertiesMap.get("policy"));
    }
  }

  public FPPolicy getPolicy() {
    return policy;
  }

  public void setPolicy(FPPolicy policy) {
    this.policy = policy;
  }

  @Override
  public String toString() {
    return "FPProperties{" + "policy=" + policy + '}';
  }
}
