package org.openbaton.catalogue.mano.descriptor;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import org.openbaton.catalogue.util.IdGenerator;

/** Created by mah on 4/28/16. */
@Entity
public class ACLMatchingCriteria implements Serializable {
  @Id private String id;
  @Version private int version = 0;

  //should be a CIDR (eg. 192.168.1.11/32)
  private String source_ip_prefix;

  //should be a CIDR (eg. 192.168.1.11/32)
  private String destination_ip_prefix;

  private int source_port_min;
  private int source_port_max;

  private int destination_port_min;
  private int destination_port_max;

  private int protocol;

  public ACLMatchingCriteria() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSourceIPPrefix() {
    return source_ip_prefix;
  }

  public void setSourceIPPrefix(String src_ip_prefix) {
    this.source_ip_prefix = src_ip_prefix;
  }

  public String getDestinationIPPrefix() {
    return destination_ip_prefix;
  }

  public void setDestinationIPPrefix(String dest_ip_prefix) {
    this.destination_ip_prefix = dest_ip_prefix;
  }

  public int getSourcePortMin() {
    return source_port_min;
  }

  public void setSourcePortMin(int source_port_min) {
    this.source_port_min = source_port_min;
  }

  public int getSourcePortMax() {
    return source_port_max;
  }

  public void setSourcePortMax(int source_port_max) {
    this.source_port_max = source_port_max;
  }

  public int getDestinationPortMin() {
    return destination_port_min;
  }

  public void setDestinationPortMin(int destination_port_min) {
    this.destination_port_min = destination_port_min;
  }

  public int getDestinationPortMax() {
    return destination_port_max;
  }

  public void setDestinationPortMax(int destination_port_max) {
    this.destination_port_max = destination_port_max;
  }

  public int getProtocol() {
    return protocol;
  }

  public void setProtocol(int protocol) {
    this.protocol = protocol;
  }

  @PrePersist
  public void ensureId() {
    id = IdGenerator.createUUID();
  }
}
