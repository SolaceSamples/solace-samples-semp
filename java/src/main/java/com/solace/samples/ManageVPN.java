/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.solace.samples;

import java.util.List;

import com.google.gson.Gson;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.MsgVpnApi;
import io.swagger.client.model.MsgVpn;
import io.swagger.client.model.MsgVpn.AuthenticationBasicTypeEnum;
import io.swagger.client.model.MsgVpnClientProfile;
import io.swagger.client.model.MsgVpnClientProfileResponse;
import io.swagger.client.model.MsgVpnClientUsername;
import io.swagger.client.model.MsgVpnClientUsernameResponse;
import io.swagger.client.model.MsgVpnQueue;
import io.swagger.client.model.MsgVpnQueue.AccessTypeEnum;
import io.swagger.client.model.MsgVpnQueue.PermissionEnum;
import io.swagger.client.model.MsgVpnQueueResponse;
import io.swagger.client.model.MsgVpnQueuesResponse;
import io.swagger.client.model.MsgVpnResponse;
import io.swagger.client.model.SempError;
import io.swagger.client.model.SempMetaOnlyResponse;

@SuppressWarnings("unused")
public class ManageVPN {

    String DEFAULT_CLIENTPROFILE = "default";
    String DEFAULT_CLIENTUSERNAME = "default";
    
    MsgVpnApi sempApiInstance;
    
    private void handleError(ApiException ae) {
        Gson gson = new Gson();
        String responseString = ae.getResponseBody();
        SempMetaOnlyResponse respObj = gson.fromJson(responseString, SempMetaOnlyResponse.class);
        SempError errorInfo = respObj.getMeta().getError();
        System.out.println("Error during operation. Details:" + 
                "\nHTTP Status Code: " + ae.getCode() + 
                "\nSEMP Error Code: " + errorInfo.getCode() + 
                "\nSEMP Error Status: " + errorInfo.getStatus() + 
                "\nSEMP Error Descriptions: " + errorInfo.getDescription());
    }
    
    public void initialize(String basePath, String user, String password) throws Exception {

        System.out.format("SEMP initializing: %s, %s \n", basePath, user);
       
        ApiClient thisClient = new ApiClient();
        thisClient.setBasePath(basePath);
        thisClient.setUsername(user);
        thisClient.setPassword(password);
        sempApiInstance = new MsgVpnApi(thisClient);
    }
    
	public void createMessageVpn(String messageVpnName) throws ApiException {

        System.out.format("Creating Message-VPN: %s...\n", messageVpnName);
       
        // Create message-vpn
        MsgVpn msgVpn = new MsgVpn();
        msgVpn.setMsgVpnName(messageVpnName);
        msgVpn.setAuthenticationBasicType(AuthenticationBasicTypeEnum.INTERNAL);
        msgVpn.setMaxMsgSpoolUsage(1500);
        msgVpn.setEnabled(true);
        MsgVpnResponse resp = sempApiInstance.createMsgVpn(msgVpn, null);        
    }
    
    public void updateDefaultClientProfileForPersistentMessaging(String messageVpnName) throws ApiException {

        System.out.format("Modifying Client-Profile for persistent messaging...\n");
        
        // Modify existing default client-profile
        MsgVpnClientProfile clientProfile = new MsgVpnClientProfile();
        clientProfile.setAllowGuaranteedMsgSendEnabled(true);
        clientProfile.setAllowGuaranteedMsgReceiveEnabled(true);
        clientProfile.setAllowGuaranteedEndpointCreateEnabled(true);
        MsgVpnClientProfileResponse resp = sempApiInstance.updateMsgVpnClientProfile(messageVpnName, DEFAULT_CLIENTPROFILE, clientProfile, null);
    }
    
    public void setupClientUsername(String messageVpnName, String clientName, String clientPassword) throws ApiException {

        System.out.format("Setting up Client-Username: %s with password...\n", clientName);

        // Create or modify client-username
        MsgVpnClientUsername clientUsername = new MsgVpnClientUsername();
        clientUsername.setPassword(clientPassword);
        clientUsername.setEnabled(true);
        if (!clientName.equals(DEFAULT_CLIENTUSERNAME)) {
            // Create client-username if not default (which was already created automatically with the VPN)
            clientUsername.setClientUsername(clientName);
            MsgVpnClientUsernameResponse resp = sempApiInstance.createMsgVpnClientUsername(
                    messageVpnName, clientUsername, null);
        } else {
            // Modify existing default client-username
            MsgVpnClientUsernameResponse resp = sempApiInstance.updateMsgVpnClientUsername(
                    messageVpnName, DEFAULT_CLIENTUSERNAME, clientUsername, null);
        }
    }
    
    public void createQueue(String messageVpnName, String queueName ) throws ApiException {

        System.out.format("Creating persistent Queue: %s...\n", queueName);
           
        // Create a queue
        MsgVpnQueue queue = new MsgVpnQueue();
        queue.setQueueName(queueName);
        queue.setPermission(PermissionEnum.DELETE);
        queue.setIngressEnabled(true);
        queue.setEgressEnabled(true);
        queue.setAccessType(AccessTypeEnum.NON_EXCLUSIVE);
        MsgVpnQueueResponse resp = sempApiInstance.createMsgVpnQueue(messageVpnName, queue, null);
    }

    public void deleteMessageVpn(String messageVPNName) throws ApiException {

        System.out.format("Deleting Message-VPN: %s...\n", messageVPNName);

        // Prerequisite for delete VPN is to remove all queues
        MsgVpnQueuesResponse queryResp = sempApiInstance.getMsgVpnQueues(messageVPNName, null, null, null, null);
        List<MsgVpnQueue> queuesList = queryResp.getData();
        if (queuesList.size() > 0) {
            System.out.format("Message-VPN contains one or more queues, deleting them first:\n");
            for (MsgVpnQueue queue: queuesList){
                deleteQueue(messageVPNName, queue.getQueueName());
            }
        }
    
        // Delete message-vpn
        SempMetaOnlyResponse resp = sempApiInstance.deleteMsgVpn(messageVPNName);
    }

    public void deleteQueue(String messageVpnName, String queueName) throws ApiException {

        System.out.format("Deleting Queue: %s...\n", queueName);

        // Delete queue
        SempMetaOnlyResponse resp = sempApiInstance.deleteMsgVpnQueue(messageVpnName, queueName);
    }

    public static void main(String... args) throws Exception {

        // Modify these params as needed
        final String vmrUser = "admin";
        final String vmrPassword = "admin";
        final String vpnUserName = "default";
        final String vpnUserPassword = "password";
        final String testQueueName = "testQueue";

        final String usage = "\nUsage: manageVPN [create | delete] <host:port> <vpnname>" +
                "\nEx: manageVPN create <host:port> <vpnname>" +
                "\n        Create a message-vpn and add a sample queue: testQueue" +
                "\n    manageVPN delete <host:port> <vpnname>" +
                "\n        Delete the message-vpn";
        
        // Check command line arguments
        if (args.length < 3) {
            System.out.println(usage);
            System.exit(-1);
        }
        
        String command = args[0];
        String vmrBasePath = "http://" + args[1] + "/SEMP/v2/config";
        String messageVpnName = args[2];

        ManageVPN app = new ManageVPN();
        
        app.initialize(vmrBasePath, vmrUser, vmrPassword);
        try {
            switch (command.toLowerCase())  {
                case "create":
                    app.createMessageVpn(messageVpnName);
                    app.updateDefaultClientProfileForPersistentMessaging(messageVpnName);
                    app.setupClientUsername(messageVpnName, vpnUserName, vpnUserPassword);
                    // Additionally create a queue
                    app.createQueue(messageVpnName, testQueueName);
                    break;
                case "delete":
                    app.deleteMessageVpn(messageVpnName);
                    break;
                default:
                    System.out.println("Invalid command: " + command + usage);
                    System.exit(-1);
            }
        } catch (ApiException e) {
            app.handleError(e);
            System.exit(-1);
        }
    }
}
