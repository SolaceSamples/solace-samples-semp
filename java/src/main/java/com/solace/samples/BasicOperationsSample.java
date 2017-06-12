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

import com.solace.labs.sempclient.samplelib.ApiClient;
import com.solace.labs.sempclient.samplelib.ApiException;
import com.solace.labs.sempclient.samplelib.api.MsgVpnApi;
import com.solace.labs.sempclient.samplelib.model.MsgVpnClientUsername;
import com.solace.labs.sempclient.samplelib.model.MsgVpnClientUsernameResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnClientUsernamesResponse;
import com.solace.labs.sempclient.samplelib.model.SempError;
import com.solace.labs.sempclient.samplelib.model.SempMetaOnlyResponse;

public class BasicOperationsSample {

    MsgVpnApi sempApiInstance;
    
    private void handleError(ApiException ae) {
        Gson gson = new Gson();
        String responseString = ae.getResponseBody();
        SempMetaOnlyResponse respObj = gson.fromJson(responseString, SempMetaOnlyResponse.class);
        SempError errorInfo = respObj.getMeta().getError();
        System.out.println("Error during operation. Details..." + 
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
    
    public void createObjectUsingPost() {

        String msgVpn = "default";
        String clientUsername = "tutorialUser";
        System.out.format("Creating Object: %s \n", clientUsername);
       
        MsgVpnClientUsername newClientUsername = new MsgVpnClientUsername();
        newClientUsername.setClientUsername(clientUsername);
        newClientUsername.setEnabled(true);
        MsgVpnClientUsernameResponse resp = null;
        try {
             resp = sempApiInstance.createMsgVpnClientUsername(msgVpn, newClientUsername, null);
        } catch (ApiException e) {
            handleError(e);
            return;
        }
        
        MsgVpnClientUsername createdClientUsername = resp.getData();
        System.out.println("Created Client Username: " + createdClientUsername);
    }
    
    public void retrievingObjectUsingGet() {
        try {
            String msgVpn = "default";
            String clientUsername = "tutorialUser";
            MsgVpnClientUsernameResponse resp = sempApiInstance.getMsgVpnClientUsername(msgVpn, clientUsername, null);
            System.out.println("Retrieved Client Username: " + resp.getData());
        } catch (ApiException e) {
            handleError(e);
        }
    }
    
    public void retrievingCollectionUsingGet() {
        try {
            String msgVpn = "default";
            // Ignore paging and selectors in this example. So set to null.
            MsgVpnClientUsernamesResponse resp = sempApiInstance.getMsgVpnClientUsernames(msgVpn, null, null, null, null);
            List<MsgVpnClientUsername> clientUsernamesList = resp.getData();
            System.out.println("Retrieved " + clientUsernamesList.size() + " Client Usernames.");
        } catch (ApiException e) {
            handleError(e);
        }
    }
    
    public void partialObjectUpdateUsingPatch() {
        try {
            String msgVpn = "default";
            String clientUsername = "tutorialUser";
            MsgVpnClientUsername updatedClientUsername = new MsgVpnClientUsername();
            updatedClientUsername.setEnabled(false);
            MsgVpnClientUsernameResponse resp = sempApiInstance.updateMsgVpnClientUsername(
                    msgVpn, clientUsername, updatedClientUsername, null);
            System.out.println("Updated: " + resp.getData());
        } catch (ApiException e) {
            handleError(e);
        }
    }
    
    public void replaceObjectUpdateUsingPut() {
        try {
            String msgVpn = "default";
            String clientUsername = "tutorialUser";
            MsgVpnClientUsername updatedClientUsername = new MsgVpnClientUsername();
            updatedClientUsername.setEnabled(true);
            MsgVpnClientUsernameResponse resp = sempApiInstance.replaceMsgVpnClientUsername(
                            msgVpn, clientUsername, updatedClientUsername, null);
            System.out.println("Updated: " + resp.getData());
            
        } catch (ApiException e) {
            handleError(e);
        }
    }
    
    public void removingObjectUsingDelete() {

        try {
            String msgVpn = "default";
            String clientUsername = "tutorialUser";
            SempMetaOnlyResponse resp = sempApiInstance.deleteMsgVpnClientUsername(msgVpn, clientUsername);
            System.out.println("Client Username delete. Resp: " + resp.getMeta().getResponseCode());
            
        } catch (ApiException e) {
            handleError(e);
        }
    }

    public static void main(String... args) throws Exception {

        // Check command line arguments
        if (args.length < 1) {
            System.out.println("Usage: javaClientSample <SEMP_BASE_PATH> <SEMP_USER> <SEMP_PASSWORD>");
            System.out.println("SEMP_BASE_PATH Ex: http://solacevmr:8080/SEMP/v2/config");
            System.exit(-1);
        }
        System.out.println("JavaClientSample initializing...");
        
        
        String vmrBasePath = args[0];
        String vmrUser = args[1];
        String vmrPassword = args[2];

        BasicOperationsSample app = new BasicOperationsSample();
        
        app.initialize(vmrBasePath, vmrUser, vmrPassword);
        app.createObjectUsingPost();
        app.retrievingObjectUsingGet();
        app.retrievingCollectionUsingGet();
        app.partialObjectUpdateUsingPatch();
        app.replaceObjectUpdateUsingPut();
        app.removingObjectUsingDelete();
        
    }
}
