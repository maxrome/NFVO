package org.openbaton.tosca.templates.TopologyTemplate.Nodes.FP;

import java.util.List;
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
public class FPACLCriteria {

  private String ipProto;
  private String ipDest;
  private Integer destPortMin;
  private Integer destPortMax;
  private Integer srcPortMin;
  private Integer srcPortMax;
  private String ipSrcPrefix;
  private String ipDstPrefix;

  public FPACLCriteria(List<Map<String, Object>> criteriaList) throws ParseException {
    for (Map<String, Object> criteria : criteriaList) {
      parseCriteria(criteria);
    }
  }

  public FPACLCriteria(Map<String, Object> criteriaMap) throws ParseException {
    parseCriteria(criteriaMap);
  }

  private void parseCriteria(Map<String, Object> criteria) throws ParseException {

    for (String key : criteria.keySet()) {
      if (key.equals("ip_proto")) {
        ipProto = (String) criteria.get(key);
      } else if (key.equals("ip_dest")) {
        ipDest = (String) criteria.get(key);
      } else if (key.equals("dest_port_min")) {
        destPortMin = (Integer) criteria.get(key);
      } else if (key.equals("dest_port_max")) {
        destPortMax = (Integer) criteria.get(key);
      } else if (key.equals("src_port_min")) {
        srcPortMin = (Integer) criteria.get(key);
      } else if (key.equals("src_port_max")) {
        srcPortMax = (Integer) criteria.get(key);
      } else if (key.equals("ip_src_prefix")) {
        ipSrcPrefix = (String) criteria.get(key);
      } else if (key.equals("ip_dst_prefix")) {
        ipDstPrefix = (String) criteria.get(key);
      } else {
        throw new ParseException("Error parsing ACL criteria, key '" + key + " not valid");
      }
    }
  }

  public String getIpProto() {
    return ipProto;
  }

  public void setIpProto(String ipProto) {
    this.ipProto = ipProto;
  }

  public String getIpDest() {
    return ipDest;
  }

  public void setIpDest(String ipDest) {
    this.ipDest = ipDest;
  }

  public String getIpSrcPrefix() {
    return ipSrcPrefix;
  }

  public void setIpSrcPrefix(String ipSrcPrefix) {
    this.ipSrcPrefix = ipSrcPrefix;
  }

  public String getIpDstPrefix() {
    return ipDstPrefix;
  }

  public void setIpDstPrefix(String ipDstPrefix) {
    this.ipDstPrefix = ipDstPrefix;
  }

  public Integer getDestPortMin() {
    return destPortMin;
  }

  public void setDestPortMin(Integer destPortMin) {
    this.destPortMin = destPortMin;
  }

  public Integer getDestPortMax() {
    return destPortMax;
  }

  public void setDestPortMax(Integer destPortMax) {
    this.destPortMax = destPortMax;
  }

  public Integer getSrcPortMin() {
    return srcPortMin;
  }

  public void setSrcPortMin(Integer srcPortMin) {
    this.srcPortMin = srcPortMin;
  }

  public Integer getSrcPortMax() {
    return srcPortMax;
  }

  public void setSrcPortMax(Integer srcPortMax) {
    this.srcPortMax = srcPortMax;
  }
}
