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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Created by rvl on 19.08.16. */
public class FPForwarderList {

  private List<String> forwarderList = null;

  public FPForwarderList() {}

  @SuppressWarnings({"unsafe", "unchecked"})
  public FPForwarderList(Object o) {

    forwarderList = new ArrayList<String>();

    List<Map<String, Object>> requirements = (List<Map<String, Object>>) o;

    for (Map<String, Object> entry : requirements) {
      if (entry.containsKey("forwarder")) forwarderList.add((String) entry.get("forwarder"));
    }
  }

  public List<String> getForwarderList() {
    return forwarderList;
  }

  public void setForwarderList(List<String> forwarderList) {
    this.forwarderList = forwarderList;
  }

  @Override
  public String toString() {
    return "FPForwarderList{" + "forwarderList=" + forwarderList + '}';
  }
}
