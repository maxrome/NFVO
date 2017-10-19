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

package org.openbaton.catalogue.mano.descriptor;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import org.openbaton.catalogue.util.IdGenerator;

/**
 * Created by lto on 06/02/15.
 *
 * <p>Based on ETSI GS NFV-MAN 001 V1.1.1 (2014-12)
 */
@Entity
public class NetworkForwardingPath extends BaseEntity {

  /** A policy or rule to apply to the NFP */
  @OneToOne(cascade = CascadeType.ALL)
  private Policy policy;
  /**
   * A tuple containing a reference to a Connection Point in the NFP and the position in the path
   */
  //@ElementCollection(fetch = FetchType.EAGER)
  //private Map<String, String> connection;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "NetworkForwardingPathId")
  private List<Connection> connections;

  public NetworkForwardingPath() {}

  @Override
  public String toString() {
    return "NetworkForwardingPath{"
        + "policy="
        + policy
        + ", connection="
        + connection
        + "} "
        + super.toString();
  }

  public Policy getPolicy() {
    return policy;
  }

  public void setPolicy(Policy policy) {
    this.policy = policy;
  }

  public List<Connection> getConnections() {
    return connections;
  }

  public void setConnections(List<Connection> connections) {
    this.connections = connections;
  }

  @java.lang.Override
  public java.lang.String toString() {
    return "NetworkForwardingPath{"
        + "id='"
        + id
        + '\''
        + ", version="
        + version
        + ", policy="
        + policy
        + ", connections="
        + connections
        + '}';
  }
}
