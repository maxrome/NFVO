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

  private Integer source_port_min;
  private Integer source_port_max;

  private Integer destination_port_min;
  private Integer destination_port_max;

  private Integer protocol;

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

  public Integer getSourcePortMin() {
    return source_port_min;
  }

  public void setSourcePortMin(Integer source_port_min) {
    this.source_port_min = source_port_min;
  }

  public Integer getSourcePortMax() {
    return source_port_max;
  }

  public void setSourcePortMax(Integer source_port_max) {
    this.source_port_max = source_port_max;
  }

  public Integer getDestinationPortMin() {
    return destination_port_min;
  }

  public void setDestinationPortMin(Integer destination_port_min) {
    this.destination_port_min = destination_port_min;
  }

  public Integer getDestinationPortMax() {
    return destination_port_max;
  }

  public void setDestinationPortMax(Integer destination_port_max) {
    this.destination_port_max = destination_port_max;
  }

  public Integer getProtocol() {
    return protocol;
  }

  public void setProtocol(Integer protocol) {
    this.protocol = protocol;
  }

  @PrePersist
  public void ensureId() {
    id = IdGenerator.createUUID();
  }
}
