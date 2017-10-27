package org.openbaton.tosca.templates.TopologyTemplate.Nodes.FP;

import java.util.Map;
import org.openbaton.tosca.exceptions.ParseException;

/**
 * /* Copyright (c) 2016 Assembly Data System SpA
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>Created by Massimiliano Romano on 17/10/2017.
 */
public class FPPolicy {

  private FPACLCriteria aclCriteria;

  @SuppressWarnings({"unsafe", "unchecked"})
  public FPPolicy(Object o) throws ParseException {

    Map<String, Object> policyMap = (Map<String, Object>) o;
    //List<Map<String,Object>> criteriaList = (List<Map<String,Object>> ) policyMap.get("criteria");
    Map<String, Object> criteriaMap = (Map<String, Object>) policyMap.get("criteria");

    //aclCriteria = new FPACLCriteria(criteriaList);
    aclCriteria = new FPACLCriteria(criteriaMap);
  }

  public FPACLCriteria getAclCriteria() {
    return aclCriteria;
  }

  public void setAclCriteria(FPACLCriteria aclCriteria) {
    this.aclCriteria = aclCriteria;
  }

  @Override
  public String toString() {
    return "FPPolicy{" + "aclCriteria=" + aclCriteria + '}';
  }
}
