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

package org.openbaton.vnfm.interfaces.manager;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.openbaton.catalogue.api.DeployNSRBody;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VNFComponent;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VNFRecordDependency;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Script;
import org.openbaton.catalogue.nfvo.messages.Interfaces.NFVMessage;
import org.openbaton.exceptions.BadFormatException;
import org.openbaton.exceptions.NotFoundException;
import org.springframework.scheduling.annotation.Async;

/** Created by lto on 26/05/15. */
public interface VnfmManager {
  Map<String, Map<String, Integer>> getVnfrNames();

  void deploy(
      NetworkServiceDescriptor networkServiceDescriptor,
      NetworkServiceRecord networkServiceRecord,
      DeployNSRBody body,
      Map<String, Set<String>> vduVimInstances,
      String monitoringIp)
      throws NotFoundException, BadFormatException, ExecutionException, InterruptedException;

  Future<Void> release(VirtualNetworkFunctionRecord virtualNetworkFunctionRecord)
      throws NotFoundException, BadFormatException, ExecutionException, InterruptedException;

  @Async
  Future<NFVMessage> requestLog(VirtualNetworkFunctionRecord vnfr, String hostname)
      throws NotFoundException, BadFormatException, ExecutionException, InterruptedException;

  @Async
  Future<Void> addVnfc(
      VirtualNetworkFunctionRecord virtualNetworkFunctionRecord,
      VNFComponent component,
      VNFRecordDependency dependency,
      String mode,
      List<String> vimInstanceNames)
      throws NotFoundException, BadFormatException, ExecutionException, InterruptedException;

  Future<Void> removeVnfcDependency(
      VirtualNetworkFunctionRecord virtualNetworkFunctionRecord, VNFCInstance vnfcInstance)
      throws NotFoundException, BadFormatException, ExecutionException, InterruptedException;

  void findAndSetNSRStatus(VirtualNetworkFunctionRecord virtualNetworkFunctionRecord);

  void removeVnfrName(String nsdId, String vnfrName);

  void updateScript(Script script, String vnfPackageId)
      throws NotFoundException, BadFormatException, ExecutionException, InterruptedException;
}
