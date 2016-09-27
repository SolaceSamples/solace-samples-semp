/**
 * SEMP (Solace Element Management Protocol)
 *  SEMP (starting in `v2`, see [note 1](#notes)) is a RESTful API for configuring a Solace router.  SEMP uses URIs to address manageble **resources** of the Solace router. Resources are either individual **objects**, or **collections** of objects. The following APIs are provided:   API|Base Path|Purpose|Comments :---|:---|:---|:--- Configuration|/SEMP/v2/config|Reading and writing config state|See [note 2](#notes)    Resources are always nouns, with individual objects being singular and collections being plural. Objects within a collection are identified by an `obj-id`, which follows the collection name with the form `collection-name/obj-id`. Some examples:  <pre> /SEMP/v2/config/msgVpns                       ; MsgVpn collection /SEMP/v2/config/msgVpns/finance               ; MsgVpn object named \"finance\" /SEMP/v2/config/msgVpns/finance/queues        ; Queue collection within MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ ; Queue object named \"orderQ\" within MsgVpn \"finance\" </pre>  ## Collection Resources  Collections are unordered lists of objects (unless described as otherwise), and are described by JSON arrays. Each item in the array represents an object in the same manner as the individual object would normally be represented. The creation of a new object is done through its collection resource.  ## Object Resources  Objects are composed of attributes and collections, and are described by JSON content as name/value pairs. The collections of an object are not contained directly in the object's JSON content, rather the content includes a URI attribute which points to the collection. This contained collection resource must be managed as a separate resource through this URI.  At a minimum, every object has 1 or more identifying attributes, and its own `uri` attribute which contains the URI to itself. Attributes may have any (non-exclusively) of the following properties:   Property|Meaning|Comments :---|:---|:--- Identifying|Attribute is involved in unique identification of the object, and appears in its URI| Required|Attribute must be provided in the request| Read-Only|Attribute can only be read, not written|See [note 3](#notes) Write-Only|Attribute can only be written, not read| Requires-Disable|Attribute can only be changed when object is disabled| Deprecated|Attribute is deprecated, and will disappear in the next SEMP version|    In some requests, certain attributes may only be provided in certain combinations with other attributes:   Relationship|Meaning :---|:--- Requires|Attribute may only be changed by a request if a particular attribute or combination of attributes is also provided in the request Conflicts|Attribute may only be provided in a request if a particular attribute or combination of attributes is not also provided in the request    ## HTTP Methods  The HTTP methods of POST, PUT, PATCH, DELETE, and GET manipulate resources following these general principles:   Method|Resource|Meaning|Request Body|Response Body|Missing Request Attributes :---|:---|:---|:---|:---|:--- POST|Collection|Create object|Initial attribute values|Object attributes and metadata|Set to default PUT|Object|Replace object|New attribute values|Object attributes and metadata|Set to default (but see [note 4](#notes)) PATCH|Object|Update object|New attribute values|Object attributes and metadata | Left unchanged| DELETE|Object|Delete object|Empty|Object metadata|N/A GET|Object|Get object|Empty|Object attributes and metadata|N/A GET|Collection|Get collection|Empty|Object attributes and collection metadata|N/A    ## Common Query Parameters  The following are some common query parameters that are supported by many method/URI combinations. Individual URIs may document additional parameters. Note that multiple query parameters can be used together in a single URI, separated by the ampersand character. For example:  <pre> ; Request for the MsgVpns collection using two hypothetical query parameters ; \"q1\" and \"q2\" with values \"val1\" and \"val2\" respectively /SEMP/v2/config/msgVpns?q1=val1&q2=val2 </pre>  ### select  Include in the response only selected attributes of the object. Use this query parameter to limit the size of the returned data for each returned object, or return only those fields that are desired.  The value of `select` is a comma-separated list of attribute names. Names may include the `*` wildcard. Nested attribute names are supported using periods (e.g. `parentName.childName`). If the list is empty (i.e. `select=`) no attributes are returned; otherwise the list must match at least one attribute name of the object. Some examples:  <pre> ; List of all MsgVpn names /SEMP/v2/config/msgVpns?select=msgVpnName  ; Authentication attributes of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance?select=authentication*  ; Access related attributes of Queue \"orderQ\" of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ?select=owner,permission </pre>  ### where  Include in the response only objects where certain conditions are true. Use this query parameter to limit which objects are returned to those whose attribute values meet the given conditions.  The value of `where` is a comma-separated list of expressions. All expressions must be true for the object to be included in the response. Each expression takes the form:  <pre> expression  = attribute-name OP value OP          = '==' | '!=' | '<' | '>' | '<=' | '>=' </pre>  `value` may be a number, string, `true`, or `false`, as appropriate for the type of `attribute-name`. Greater-than and less-than comparisons only work for numbers. A `*` in a string `value` is interpreted as a wildcard. Some examples:  <pre> ; Only enabled MsgVpns /SEMP/v2/config/msgVpns?where=enabled==true  ; Only MsgVpns using basic non-LDAP authentication /SEMP/v2/config/msgVpns?where=authenticationBasicEnabled==true,authenticationBasicType!=ldap  ; Only MsgVpns that allow more than 100 client connections /SEMP/v2/config/msgVpns?where=maxConnectionCount>100 </pre>  ### count  Limit the count of objects in the response. This can be useful to limit the size of the response for large collections. The minimum value for `count` is `1` and the default is `10`. For example:  <pre> ; Up to 25 MsgVpns /SEMP/v2/config/msgVpns?count=25 </pre>  ### cursor  The cursor, or position, for the next page of objects. Cursors are opaque data that should not be created or interpreted by SEMP clients, and should only be used as described below.  When a request is made for a collection and there may be additional objects available for retrieval that are not included in the initial response, the response will include a `cursorQuery` field containing a cursor. The value of this field can be specified in the `cursor` query parameter of a subsequent request to retrieve the next page of objects. For convenience, an appropriate URI is constructed automatically by the router and included in the `nextPageUri` field of the response. This URI can be used directly to retrieve the next page of objects.  ## Notes  1. This specification defines SEMP starting in `v2`, and not the original SEMP `v1` interface. Request and response formats between `v1` and `v2` are entirely incompatible, although both protocols share a common port configuration on the Solace router. They are differentiated by the initial portion of the URI path, one of either `/SEMP/` or `/SEMP/v2/`. 2. The config API is partially implemented. Only a subset of all configurable objects are available. 3. Read-only attributes may appear in POST and PUT/PATCH requests. However, if a read-only attribute is not marked as identifying, it will be ignored during a PUT/PATCH. 4. For PUT, if the SEMP user is not authorized to modify the attribute, its value is left unchanged rather than set to default. In addition, the values of write-only attributes are not set to their defaults on a PUT. 5. For DELETE, the body of the request currently serves no purpose and will cause an error if not empty. 
 *
 * OpenAPI spec version: 2.7.2.2.212
 * Contact: support_request@solacesystems.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.swagger.client.api;

import io.swagger.client.ApiCallback;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;
import io.swagger.client.ProgressRequestBody;
import io.swagger.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import io.swagger.client.model.SempMetaOnlyResponse;
import io.swagger.client.model.MsgVpn;
import io.swagger.client.model.MsgVpnResponse;
import io.swagger.client.model.MsgVpnAclProfile;
import io.swagger.client.model.MsgVpnAclProfileResponse;
import io.swagger.client.model.MsgVpnAclProfileClientConnectExceptionResponse;
import io.swagger.client.model.MsgVpnAclProfileClientConnectException;
import io.swagger.client.model.MsgVpnAclProfilePublishExceptionResponse;
import io.swagger.client.model.MsgVpnAclProfilePublishException;
import io.swagger.client.model.MsgVpnAclProfileSubscribeException;
import io.swagger.client.model.MsgVpnAclProfileSubscribeExceptionResponse;
import io.swagger.client.model.MsgVpnAuthorizationGroupResponse;
import io.swagger.client.model.MsgVpnAuthorizationGroup;
import io.swagger.client.model.MsgVpnBridge;
import io.swagger.client.model.MsgVpnBridgeResponse;
import io.swagger.client.model.MsgVpnBridgeRemoteMsgVpn;
import io.swagger.client.model.MsgVpnBridgeRemoteMsgVpnResponse;
import io.swagger.client.model.MsgVpnBridgeRemoteSubscription;
import io.swagger.client.model.MsgVpnBridgeRemoteSubscriptionResponse;
import io.swagger.client.model.MsgVpnBridgeTlsTrustedCommonNameResponse;
import io.swagger.client.model.MsgVpnBridgeTlsTrustedCommonName;
import io.swagger.client.model.MsgVpnClientProfileResponse;
import io.swagger.client.model.MsgVpnClientProfile;
import io.swagger.client.model.MsgVpnClientUsernameResponse;
import io.swagger.client.model.MsgVpnClientUsername;
import io.swagger.client.model.MsgVpnQueueResponse;
import io.swagger.client.model.MsgVpnQueue;
import io.swagger.client.model.MsgVpnQueueSubscription;
import io.swagger.client.model.MsgVpnQueueSubscriptionResponse;
import io.swagger.client.model.MsgVpnRestDeliveryPoint;
import io.swagger.client.model.MsgVpnRestDeliveryPointResponse;
import io.swagger.client.model.MsgVpnRestDeliveryPointQueueBinding;
import io.swagger.client.model.MsgVpnRestDeliveryPointQueueBindingResponse;
import io.swagger.client.model.MsgVpnRestDeliveryPointRestConsumer;
import io.swagger.client.model.MsgVpnRestDeliveryPointRestConsumerResponse;
import io.swagger.client.model.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName;
import io.swagger.client.model.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse;
import io.swagger.client.model.MsgVpnSequencedTopic;
import io.swagger.client.model.MsgVpnSequencedTopicResponse;
import io.swagger.client.model.MsgVpnAclProfileClientConnectExceptionsResponse;
import io.swagger.client.model.MsgVpnAclProfilePublishExceptionsResponse;
import io.swagger.client.model.MsgVpnAclProfileSubscribeExceptionsResponse;
import io.swagger.client.model.MsgVpnAclProfilesResponse;
import io.swagger.client.model.MsgVpnAuthorizationGroupsResponse;
import io.swagger.client.model.MsgVpnBridgeRemoteMsgVpnsResponse;
import io.swagger.client.model.MsgVpnBridgeRemoteSubscriptionsResponse;
import io.swagger.client.model.MsgVpnBridgeTlsTrustedCommonNamesResponse;
import io.swagger.client.model.MsgVpnBridgesResponse;
import io.swagger.client.model.MsgVpnClientProfilesResponse;
import io.swagger.client.model.MsgVpnClientUsernamesResponse;
import io.swagger.client.model.MsgVpnQueueSubscriptionsResponse;
import io.swagger.client.model.MsgVpnQueuesResponse;
import io.swagger.client.model.MsgVpnRestDeliveryPointQueueBindingsResponse;
import io.swagger.client.model.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse;
import io.swagger.client.model.MsgVpnRestDeliveryPointRestConsumersResponse;
import io.swagger.client.model.MsgVpnRestDeliveryPointsResponse;
import io.swagger.client.model.MsgVpnSequencedTopicsResponse;
import io.swagger.client.model.MsgVpnsResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgVpnApi {
    private ApiClient apiClient;

    public MsgVpnApi() {
        this(Configuration.getDefaultApiClient());
    }

    public MsgVpnApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /* Build call for createMsgVpn */
    private com.squareup.okhttp.Call createMsgVpnCall(MsgVpn body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpn(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns".replaceAll("\\{format\\}","json");

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Message VPN object.
     * Creates a Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword||||x| replicationEnabledQueueBehavior||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param body The Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnResponse createMsgVpn(MsgVpn body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnResponse> resp = createMsgVpnWithHttpInfo(body, select);
        return resp.getData();
    }

    /**
     * Creates a Message VPN object.
     * Creates a Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword||||x| replicationEnabledQueueBehavior||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param body The Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnResponse> createMsgVpnWithHttpInfo(MsgVpn body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnCall(body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Message VPN object. (asynchronously)
     * Creates a Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword||||x| replicationEnabledQueueBehavior||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param body The Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnAsync(MsgVpn body, List<String> select, final ApiCallback<MsgVpnResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnCall(body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnAclProfile */
    private com.squareup.okhttp.Call createMsgVpnAclProfileCall(String msgVpnName, MsgVpnAclProfile body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnAclProfile(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnAclProfile(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates an ACL Profile object.
     * Creates an ACL Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The ACL Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfileResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfileResponse createMsgVpnAclProfile(String msgVpnName, MsgVpnAclProfile body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfileResponse> resp = createMsgVpnAclProfileWithHttpInfo(msgVpnName, body, select);
        return resp.getData();
    }

    /**
     * Creates an ACL Profile object.
     * Creates an ACL Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The ACL Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfileResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfileResponse> createMsgVpnAclProfileWithHttpInfo(String msgVpnName, MsgVpnAclProfile body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnAclProfileCall(msgVpnName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates an ACL Profile object. (asynchronously)
     * Creates an ACL Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The ACL Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnAclProfileAsync(String msgVpnName, MsgVpnAclProfile body, List<String> select, final ApiCallback<MsgVpnAclProfileResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnAclProfileCall(msgVpnName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnAclProfileClientConnectException */
    private com.squareup.okhttp.Call createMsgVpnAclProfileClientConnectExceptionCall(String msgVpnName, String aclProfileName, MsgVpnAclProfileClientConnectException body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnAclProfileClientConnectException(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling createMsgVpnAclProfileClientConnectException(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnAclProfileClientConnectException(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Client Connect Exception object.
     * Creates a Client Connect Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| clientConnectExceptionAddress|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The Client Connect Exception object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfileClientConnectExceptionResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfileClientConnectExceptionResponse createMsgVpnAclProfileClientConnectException(String msgVpnName, String aclProfileName, MsgVpnAclProfileClientConnectException body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfileClientConnectExceptionResponse> resp = createMsgVpnAclProfileClientConnectExceptionWithHttpInfo(msgVpnName, aclProfileName, body, select);
        return resp.getData();
    }

    /**
     * Creates a Client Connect Exception object.
     * Creates a Client Connect Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| clientConnectExceptionAddress|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The Client Connect Exception object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfileClientConnectExceptionResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfileClientConnectExceptionResponse> createMsgVpnAclProfileClientConnectExceptionWithHttpInfo(String msgVpnName, String aclProfileName, MsgVpnAclProfileClientConnectException body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnAclProfileClientConnectExceptionCall(msgVpnName, aclProfileName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileClientConnectExceptionResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Client Connect Exception object. (asynchronously)
     * Creates a Client Connect Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| clientConnectExceptionAddress|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The Client Connect Exception object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnAclProfileClientConnectExceptionAsync(String msgVpnName, String aclProfileName, MsgVpnAclProfileClientConnectException body, List<String> select, final ApiCallback<MsgVpnAclProfileClientConnectExceptionResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnAclProfileClientConnectExceptionCall(msgVpnName, aclProfileName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileClientConnectExceptionResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnAclProfilePublishException */
    private com.squareup.okhttp.Call createMsgVpnAclProfilePublishExceptionCall(String msgVpnName, String aclProfileName, MsgVpnAclProfilePublishException body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnAclProfilePublishException(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling createMsgVpnAclProfilePublishException(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnAclProfilePublishException(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Publish Topic Exception object.
     * Creates a Publish Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| publishExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The Publish Topic Exception object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfilePublishExceptionResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfilePublishExceptionResponse createMsgVpnAclProfilePublishException(String msgVpnName, String aclProfileName, MsgVpnAclProfilePublishException body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfilePublishExceptionResponse> resp = createMsgVpnAclProfilePublishExceptionWithHttpInfo(msgVpnName, aclProfileName, body, select);
        return resp.getData();
    }

    /**
     * Creates a Publish Topic Exception object.
     * Creates a Publish Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| publishExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The Publish Topic Exception object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfilePublishExceptionResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfilePublishExceptionResponse> createMsgVpnAclProfilePublishExceptionWithHttpInfo(String msgVpnName, String aclProfileName, MsgVpnAclProfilePublishException body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnAclProfilePublishExceptionCall(msgVpnName, aclProfileName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfilePublishExceptionResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Publish Topic Exception object. (asynchronously)
     * Creates a Publish Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| publishExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The Publish Topic Exception object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnAclProfilePublishExceptionAsync(String msgVpnName, String aclProfileName, MsgVpnAclProfilePublishException body, List<String> select, final ApiCallback<MsgVpnAclProfilePublishExceptionResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnAclProfilePublishExceptionCall(msgVpnName, aclProfileName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfilePublishExceptionResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnAclProfileSubscribeException */
    private com.squareup.okhttp.Call createMsgVpnAclProfileSubscribeExceptionCall(String msgVpnName, String aclProfileName, MsgVpnAclProfileSubscribeException body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnAclProfileSubscribeException(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling createMsgVpnAclProfileSubscribeException(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnAclProfileSubscribeException(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Subscribe Topic Exception object.
     * Creates a Subscribe Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| subscribeExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The Subscribe Topic Exception object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfileSubscribeExceptionResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfileSubscribeExceptionResponse createMsgVpnAclProfileSubscribeException(String msgVpnName, String aclProfileName, MsgVpnAclProfileSubscribeException body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfileSubscribeExceptionResponse> resp = createMsgVpnAclProfileSubscribeExceptionWithHttpInfo(msgVpnName, aclProfileName, body, select);
        return resp.getData();
    }

    /**
     * Creates a Subscribe Topic Exception object.
     * Creates a Subscribe Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| subscribeExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The Subscribe Topic Exception object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfileSubscribeExceptionResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfileSubscribeExceptionResponse> createMsgVpnAclProfileSubscribeExceptionWithHttpInfo(String msgVpnName, String aclProfileName, MsgVpnAclProfileSubscribeException body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnAclProfileSubscribeExceptionCall(msgVpnName, aclProfileName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileSubscribeExceptionResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Subscribe Topic Exception object. (asynchronously)
     * Creates a Subscribe Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| subscribeExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The Subscribe Topic Exception object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnAclProfileSubscribeExceptionAsync(String msgVpnName, String aclProfileName, MsgVpnAclProfileSubscribeException body, List<String> select, final ApiCallback<MsgVpnAclProfileSubscribeExceptionResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnAclProfileSubscribeExceptionCall(msgVpnName, aclProfileName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileSubscribeExceptionResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnAuthorizationGroup */
    private com.squareup.okhttp.Call createMsgVpnAuthorizationGroupCall(String msgVpnName, MsgVpnAuthorizationGroup body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnAuthorizationGroup(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnAuthorizationGroup(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/authorizationGroups".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a LDAP Authorization Group object.
     * Creates a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authorizationGroupName|x|x||| msgVpnName|x||x|| orderAfterAuthorizationGroupName||||x| orderBeforeAuthorizationGroupName||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The LDAP Authorization Group object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAuthorizationGroupResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAuthorizationGroupResponse createMsgVpnAuthorizationGroup(String msgVpnName, MsgVpnAuthorizationGroup body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAuthorizationGroupResponse> resp = createMsgVpnAuthorizationGroupWithHttpInfo(msgVpnName, body, select);
        return resp.getData();
    }

    /**
     * Creates a LDAP Authorization Group object.
     * Creates a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authorizationGroupName|x|x||| msgVpnName|x||x|| orderAfterAuthorizationGroupName||||x| orderBeforeAuthorizationGroupName||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The LDAP Authorization Group object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAuthorizationGroupResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAuthorizationGroupResponse> createMsgVpnAuthorizationGroupWithHttpInfo(String msgVpnName, MsgVpnAuthorizationGroup body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnAuthorizationGroupCall(msgVpnName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAuthorizationGroupResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a LDAP Authorization Group object. (asynchronously)
     * Creates a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authorizationGroupName|x|x||| msgVpnName|x||x|| orderAfterAuthorizationGroupName||||x| orderBeforeAuthorizationGroupName||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The LDAP Authorization Group object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnAuthorizationGroupAsync(String msgVpnName, MsgVpnAuthorizationGroup body, List<String> select, final ApiCallback<MsgVpnAuthorizationGroupResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnAuthorizationGroupCall(msgVpnName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAuthorizationGroupResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnBridge */
    private com.squareup.okhttp.Call createMsgVpnBridgeCall(String msgVpnName, MsgVpnBridge body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnBridge(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnBridge(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Bridge object.
     * Creates a Bridge object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| msgVpnName|x||x|| remoteAuthenticationBasicPassword||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeResponse createMsgVpnBridge(String msgVpnName, MsgVpnBridge body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeResponse> resp = createMsgVpnBridgeWithHttpInfo(msgVpnName, body, select);
        return resp.getData();
    }

    /**
     * Creates a Bridge object.
     * Creates a Bridge object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| msgVpnName|x||x|| remoteAuthenticationBasicPassword||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeResponse> createMsgVpnBridgeWithHttpInfo(String msgVpnName, MsgVpnBridge body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnBridgeCall(msgVpnName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Bridge object. (asynchronously)
     * Creates a Bridge object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| msgVpnName|x||x|| remoteAuthenticationBasicPassword||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnBridgeAsync(String msgVpnName, MsgVpnBridge body, List<String> select, final ApiCallback<MsgVpnBridgeResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnBridgeCall(msgVpnName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnBridgeRemoteMsgVpn */
    private com.squareup.okhttp.Call createMsgVpnBridgeRemoteMsgVpnCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteMsgVpn body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling createMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling createMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Remote Message VPN object.
     * Creates a Remote Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| password||||x| remoteMsgVpnInterface|x|||| remoteMsgVpnLocation|x|||| remoteMsgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeRemoteMsgVpnResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeRemoteMsgVpnResponse createMsgVpnBridgeRemoteMsgVpn(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteMsgVpn body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeRemoteMsgVpnResponse> resp = createMsgVpnBridgeRemoteMsgVpnWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, body, select);
        return resp.getData();
    }

    /**
     * Creates a Remote Message VPN object.
     * Creates a Remote Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| password||||x| remoteMsgVpnInterface|x|||| remoteMsgVpnLocation|x|||| remoteMsgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeRemoteMsgVpnResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeRemoteMsgVpnResponse> createMsgVpnBridgeRemoteMsgVpnWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteMsgVpn body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnBridgeRemoteMsgVpnCall(msgVpnName, bridgeName, bridgeVirtualRouter, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteMsgVpnResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Remote Message VPN object. (asynchronously)
     * Creates a Remote Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| password||||x| remoteMsgVpnInterface|x|||| remoteMsgVpnLocation|x|||| remoteMsgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnBridgeRemoteMsgVpnAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteMsgVpn body, List<String> select, final ApiCallback<MsgVpnBridgeRemoteMsgVpnResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnBridgeRemoteMsgVpnCall(msgVpnName, bridgeName, bridgeVirtualRouter, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteMsgVpnResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnBridgeRemoteSubscription */
    private com.squareup.okhttp.Call createMsgVpnBridgeRemoteSubscriptionCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteSubscription body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnBridgeRemoteSubscription(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling createMsgVpnBridgeRemoteSubscription(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling createMsgVpnBridgeRemoteSubscription(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnBridgeRemoteSubscription(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Remote Subscription object.
     * Creates a Remote Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| deliverAlwaysEnabled||x||| msgVpnName|x||x|| remoteSubscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Remote Subscription object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeRemoteSubscriptionResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeRemoteSubscriptionResponse createMsgVpnBridgeRemoteSubscription(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteSubscription body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeRemoteSubscriptionResponse> resp = createMsgVpnBridgeRemoteSubscriptionWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, body, select);
        return resp.getData();
    }

    /**
     * Creates a Remote Subscription object.
     * Creates a Remote Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| deliverAlwaysEnabled||x||| msgVpnName|x||x|| remoteSubscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Remote Subscription object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeRemoteSubscriptionResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeRemoteSubscriptionResponse> createMsgVpnBridgeRemoteSubscriptionWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteSubscription body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnBridgeRemoteSubscriptionCall(msgVpnName, bridgeName, bridgeVirtualRouter, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteSubscriptionResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Remote Subscription object. (asynchronously)
     * Creates a Remote Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| deliverAlwaysEnabled||x||| msgVpnName|x||x|| remoteSubscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Remote Subscription object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnBridgeRemoteSubscriptionAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeRemoteSubscription body, List<String> select, final ApiCallback<MsgVpnBridgeRemoteSubscriptionResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnBridgeRemoteSubscriptionCall(msgVpnName, bridgeName, bridgeVirtualRouter, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteSubscriptionResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnBridgeTlsTrustedCommonName */
    private com.squareup.okhttp.Call createMsgVpnBridgeTlsTrustedCommonNameCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeTlsTrustedCommonName body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling createMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling createMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Trusted Common Name object.
     * Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Trusted Common Name object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeTlsTrustedCommonNameResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeTlsTrustedCommonNameResponse createMsgVpnBridgeTlsTrustedCommonName(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeTlsTrustedCommonName body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeTlsTrustedCommonNameResponse> resp = createMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, body, select);
        return resp.getData();
    }

    /**
     * Creates a Trusted Common Name object.
     * Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Trusted Common Name object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeTlsTrustedCommonNameResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeTlsTrustedCommonNameResponse> createMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeTlsTrustedCommonName body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnBridgeTlsTrustedCommonNameCall(msgVpnName, bridgeName, bridgeVirtualRouter, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeTlsTrustedCommonNameResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Trusted Common Name object. (asynchronously)
     * Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Trusted Common Name object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnBridgeTlsTrustedCommonNameAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridgeTlsTrustedCommonName body, List<String> select, final ApiCallback<MsgVpnBridgeTlsTrustedCommonNameResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnBridgeTlsTrustedCommonNameCall(msgVpnName, bridgeName, bridgeVirtualRouter, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeTlsTrustedCommonNameResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnClientProfile */
    private com.squareup.okhttp.Call createMsgVpnClientProfileCall(String msgVpnName, MsgVpnClientProfile body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnClientProfile(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnClientProfile(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientProfiles".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Client Profile object.
     * Creates a Client Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Client Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnClientProfileResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnClientProfileResponse createMsgVpnClientProfile(String msgVpnName, MsgVpnClientProfile body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnClientProfileResponse> resp = createMsgVpnClientProfileWithHttpInfo(msgVpnName, body, select);
        return resp.getData();
    }

    /**
     * Creates a Client Profile object.
     * Creates a Client Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Client Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnClientProfileResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnClientProfileResponse> createMsgVpnClientProfileWithHttpInfo(String msgVpnName, MsgVpnClientProfile body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnClientProfileCall(msgVpnName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnClientProfileResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Client Profile object. (asynchronously)
     * Creates a Client Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Client Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnClientProfileAsync(String msgVpnName, MsgVpnClientProfile body, List<String> select, final ApiCallback<MsgVpnClientProfileResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnClientProfileCall(msgVpnName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnClientProfileResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnClientUsername */
    private com.squareup.okhttp.Call createMsgVpnClientUsernameCall(String msgVpnName, MsgVpnClientUsername body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnClientUsername(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnClientUsername(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientUsernames".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Client Username object.
     * Creates a Client Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientUsername|x|x||| msgVpnName|x||x|| password||||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Client Username object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnClientUsernameResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnClientUsernameResponse createMsgVpnClientUsername(String msgVpnName, MsgVpnClientUsername body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnClientUsernameResponse> resp = createMsgVpnClientUsernameWithHttpInfo(msgVpnName, body, select);
        return resp.getData();
    }

    /**
     * Creates a Client Username object.
     * Creates a Client Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientUsername|x|x||| msgVpnName|x||x|| password||||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Client Username object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnClientUsernameResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnClientUsernameResponse> createMsgVpnClientUsernameWithHttpInfo(String msgVpnName, MsgVpnClientUsername body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnClientUsernameCall(msgVpnName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnClientUsernameResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Client Username object. (asynchronously)
     * Creates a Client Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientUsername|x|x||| msgVpnName|x||x|| password||||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Client Username object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnClientUsernameAsync(String msgVpnName, MsgVpnClientUsername body, List<String> select, final ApiCallback<MsgVpnClientUsernameResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnClientUsernameCall(msgVpnName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnClientUsernameResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnQueue */
    private com.squareup.okhttp.Call createMsgVpnQueueCall(String msgVpnName, MsgVpnQueue body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnQueue(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnQueue(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/queues".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Queue object.
     * Creates a Queue object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Queue object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnQueueResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnQueueResponse createMsgVpnQueue(String msgVpnName, MsgVpnQueue body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnQueueResponse> resp = createMsgVpnQueueWithHttpInfo(msgVpnName, body, select);
        return resp.getData();
    }

    /**
     * Creates a Queue object.
     * Creates a Queue object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Queue object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnQueueResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnQueueResponse> createMsgVpnQueueWithHttpInfo(String msgVpnName, MsgVpnQueue body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnQueueCall(msgVpnName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnQueueResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Queue object. (asynchronously)
     * Creates a Queue object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Queue object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnQueueAsync(String msgVpnName, MsgVpnQueue body, List<String> select, final ApiCallback<MsgVpnQueueResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnQueueCall(msgVpnName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnQueueResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnQueueSubscription */
    private com.squareup.okhttp.Call createMsgVpnQueueSubscriptionCall(String msgVpnName, String queueName, MsgVpnQueueSubscription body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnQueueSubscription(Async)");
        }
        
        // verify the required parameter 'queueName' is set
        if (queueName == null) {
            throw new ApiException("Missing the required parameter 'queueName' when calling createMsgVpnQueueSubscription(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnQueueSubscription(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "queueName" + "\\}", apiClient.escapeString(queueName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Queue Subscription object.
     * Creates a Queue Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param body The Queue Subscription object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnQueueSubscriptionResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnQueueSubscriptionResponse createMsgVpnQueueSubscription(String msgVpnName, String queueName, MsgVpnQueueSubscription body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnQueueSubscriptionResponse> resp = createMsgVpnQueueSubscriptionWithHttpInfo(msgVpnName, queueName, body, select);
        return resp.getData();
    }

    /**
     * Creates a Queue Subscription object.
     * Creates a Queue Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param body The Queue Subscription object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnQueueSubscriptionResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnQueueSubscriptionResponse> createMsgVpnQueueSubscriptionWithHttpInfo(String msgVpnName, String queueName, MsgVpnQueueSubscription body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnQueueSubscriptionCall(msgVpnName, queueName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnQueueSubscriptionResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Queue Subscription object. (asynchronously)
     * Creates a Queue Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param body The Queue Subscription object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnQueueSubscriptionAsync(String msgVpnName, String queueName, MsgVpnQueueSubscription body, List<String> select, final ApiCallback<MsgVpnQueueSubscriptionResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnQueueSubscriptionCall(msgVpnName, queueName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnQueueSubscriptionResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnRestDeliveryPoint */
    private com.squareup.okhttp.Call createMsgVpnRestDeliveryPointCall(String msgVpnName, MsgVpnRestDeliveryPoint body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnRestDeliveryPoint(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnRestDeliveryPoint(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a REST Delivery Point object.
     * Creates a REST Delivery Point object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointResponse createMsgVpnRestDeliveryPoint(String msgVpnName, MsgVpnRestDeliveryPoint body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointResponse> resp = createMsgVpnRestDeliveryPointWithHttpInfo(msgVpnName, body, select);
        return resp.getData();
    }

    /**
     * Creates a REST Delivery Point object.
     * Creates a REST Delivery Point object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointResponse> createMsgVpnRestDeliveryPointWithHttpInfo(String msgVpnName, MsgVpnRestDeliveryPoint body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnRestDeliveryPointCall(msgVpnName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a REST Delivery Point object. (asynchronously)
     * Creates a REST Delivery Point object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnRestDeliveryPointAsync(String msgVpnName, MsgVpnRestDeliveryPoint body, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnRestDeliveryPointCall(msgVpnName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnRestDeliveryPointQueueBinding */
    private com.squareup.okhttp.Call createMsgVpnRestDeliveryPointQueueBindingCall(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling createMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Queue Binding object.
     * Creates a Queue Binding object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueBindingName|x|x||| restDeliveryPointName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointQueueBindingResponse createMsgVpnRestDeliveryPointQueueBinding(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointQueueBindingResponse> resp = createMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(msgVpnName, restDeliveryPointName, body, select);
        return resp.getData();
    }

    /**
     * Creates a Queue Binding object.
     * Creates a Queue Binding object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueBindingName|x|x||| restDeliveryPointName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointQueueBindingResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointQueueBindingResponse> createMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnRestDeliveryPointQueueBindingCall(msgVpnName, restDeliveryPointName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointQueueBindingResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Queue Binding object. (asynchronously)
     * Creates a Queue Binding object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueBindingName|x|x||| restDeliveryPointName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnRestDeliveryPointQueueBindingAsync(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointQueueBindingResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnRestDeliveryPointQueueBindingCall(msgVpnName, restDeliveryPointName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointQueueBindingResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnRestDeliveryPointRestConsumer */
    private com.squareup.okhttp.Call createMsgVpnRestDeliveryPointRestConsumerCall(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling createMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a REST Consumer object.
     * Creates a REST Consumer object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword||||x| msgVpnName|x||x|| restConsumerName|x|x||| restDeliveryPointName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointRestConsumerResponse createMsgVpnRestDeliveryPointRestConsumer(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointRestConsumerResponse> resp = createMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(msgVpnName, restDeliveryPointName, body, select);
        return resp.getData();
    }

    /**
     * Creates a REST Consumer object.
     * Creates a REST Consumer object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword||||x| msgVpnName|x||x|| restConsumerName|x|x||| restDeliveryPointName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointRestConsumerResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointRestConsumerResponse> createMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnRestDeliveryPointRestConsumerCall(msgVpnName, restDeliveryPointName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a REST Consumer object. (asynchronously)
     * Creates a REST Consumer object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword||||x| msgVpnName|x||x|| restConsumerName|x|x||| restDeliveryPointName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnRestDeliveryPointRestConsumerAsync(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointRestConsumerResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnRestDeliveryPointRestConsumerCall(msgVpnName, restDeliveryPointName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName */
    private com.squareup.okhttp.Call createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameCall(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new ApiException("Missing the required parameter 'restConsumerName' when calling createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "restConsumerName" + "\\}", apiClient.escapeString(restConsumerName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Trusted Common Name object.
     * Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restConsumerName|x||x|| restDeliveryPointName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param body The Trusted Common Name object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse> resp = createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, body, select);
        return resp.getData();
    }

    /**
     * Creates a Trusted Common Name object.
     * Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restConsumerName|x||x|| restDeliveryPointName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param body The Trusted Common Name object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse> createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameCall(msgVpnName, restDeliveryPointName, restConsumerName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Trusted Common Name object. (asynchronously)
     * Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restConsumerName|x||x|| restDeliveryPointName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param body The Trusted Common Name object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameAsync(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName body, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameCall(msgVpnName, restDeliveryPointName, restConsumerName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for createMsgVpnSequencedTopic */
    private com.squareup.okhttp.Call createMsgVpnSequencedTopicCall(String msgVpnName, MsgVpnSequencedTopic body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling createMsgVpnSequencedTopic(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling createMsgVpnSequencedTopic(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/sequencedTopics".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Creates a Sequenced Topic object.
     * Creates a Sequenced Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| sequencedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Sequenced Topic object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnSequencedTopicResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnSequencedTopicResponse createMsgVpnSequencedTopic(String msgVpnName, MsgVpnSequencedTopic body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnSequencedTopicResponse> resp = createMsgVpnSequencedTopicWithHttpInfo(msgVpnName, body, select);
        return resp.getData();
    }

    /**
     * Creates a Sequenced Topic object.
     * Creates a Sequenced Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| sequencedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Sequenced Topic object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnSequencedTopicResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnSequencedTopicResponse> createMsgVpnSequencedTopicWithHttpInfo(String msgVpnName, MsgVpnSequencedTopic body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = createMsgVpnSequencedTopicCall(msgVpnName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnSequencedTopicResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Creates a Sequenced Topic object. (asynchronously)
     * Creates a Sequenced Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| sequencedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Sequenced Topic object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call createMsgVpnSequencedTopicAsync(String msgVpnName, MsgVpnSequencedTopic body, List<String> select, final ApiCallback<MsgVpnSequencedTopicResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = createMsgVpnSequencedTopicCall(msgVpnName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnSequencedTopicResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpn */
    private com.squareup.okhttp.Call deleteMsgVpnCall(String msgVpnName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpn(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Message VPN object.
     * Deletes a Message VPN object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpn(String msgVpnName) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnWithHttpInfo(msgVpnName);
        return resp.getData();
    }

    /**
     * Deletes a Message VPN object.
     * Deletes a Message VPN object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnWithHttpInfo(String msgVpnName) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnCall(msgVpnName, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Message VPN object. (asynchronously)
     * Deletes a Message VPN object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnAsync(String msgVpnName, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnCall(msgVpnName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnAclProfile */
    private com.squareup.okhttp.Call deleteMsgVpnAclProfileCall(String msgVpnName, String aclProfileName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnAclProfile(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling deleteMsgVpnAclProfile(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes an ACL Profile object.
     * Deletes an ACL Profile object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnAclProfile(String msgVpnName, String aclProfileName) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnAclProfileWithHttpInfo(msgVpnName, aclProfileName);
        return resp.getData();
    }

    /**
     * Deletes an ACL Profile object.
     * Deletes an ACL Profile object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnAclProfileWithHttpInfo(String msgVpnName, String aclProfileName) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnAclProfileCall(msgVpnName, aclProfileName, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes an ACL Profile object. (asynchronously)
     * Deletes an ACL Profile object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnAclProfileAsync(String msgVpnName, String aclProfileName, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnAclProfileCall(msgVpnName, aclProfileName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnAclProfileClientConnectException */
    private com.squareup.okhttp.Call deleteMsgVpnAclProfileClientConnectExceptionCall(String msgVpnName, String aclProfileName, String clientConnectExceptionAddress, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnAclProfileClientConnectException(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling deleteMsgVpnAclProfileClientConnectException(Async)");
        }
        
        // verify the required parameter 'clientConnectExceptionAddress' is set
        if (clientConnectExceptionAddress == null) {
            throw new ApiException("Missing the required parameter 'clientConnectExceptionAddress' when calling deleteMsgVpnAclProfileClientConnectException(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()))
        .replaceAll("\\{" + "clientConnectExceptionAddress" + "\\}", apiClient.escapeString(clientConnectExceptionAddress.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Client Connect Exception object.
     * Deletes a Client Connect Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param clientConnectExceptionAddress The clientConnectExceptionAddress of the Client Connect Exception. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnAclProfileClientConnectException(String msgVpnName, String aclProfileName, String clientConnectExceptionAddress) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnAclProfileClientConnectExceptionWithHttpInfo(msgVpnName, aclProfileName, clientConnectExceptionAddress);
        return resp.getData();
    }

    /**
     * Deletes a Client Connect Exception object.
     * Deletes a Client Connect Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param clientConnectExceptionAddress The clientConnectExceptionAddress of the Client Connect Exception. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnAclProfileClientConnectExceptionWithHttpInfo(String msgVpnName, String aclProfileName, String clientConnectExceptionAddress) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnAclProfileClientConnectExceptionCall(msgVpnName, aclProfileName, clientConnectExceptionAddress, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Client Connect Exception object. (asynchronously)
     * Deletes a Client Connect Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param clientConnectExceptionAddress The clientConnectExceptionAddress of the Client Connect Exception. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnAclProfileClientConnectExceptionAsync(String msgVpnName, String aclProfileName, String clientConnectExceptionAddress, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnAclProfileClientConnectExceptionCall(msgVpnName, aclProfileName, clientConnectExceptionAddress, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnAclProfilePublishException */
    private com.squareup.okhttp.Call deleteMsgVpnAclProfilePublishExceptionCall(String msgVpnName, String aclProfileName, String topicSyntax, String publishExceptionTopic, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnAclProfilePublishException(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling deleteMsgVpnAclProfilePublishException(Async)");
        }
        
        // verify the required parameter 'topicSyntax' is set
        if (topicSyntax == null) {
            throw new ApiException("Missing the required parameter 'topicSyntax' when calling deleteMsgVpnAclProfilePublishException(Async)");
        }
        
        // verify the required parameter 'publishExceptionTopic' is set
        if (publishExceptionTopic == null) {
            throw new ApiException("Missing the required parameter 'publishExceptionTopic' when calling deleteMsgVpnAclProfilePublishException(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()))
        .replaceAll("\\{" + "topicSyntax" + "\\}", apiClient.escapeString(topicSyntax.toString()))
        .replaceAll("\\{" + "publishExceptionTopic" + "\\}", apiClient.escapeString(publishExceptionTopic.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Publish Topic Exception object.
     * Deletes a Publish Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Publish Topic Exception. (required)
     * @param publishExceptionTopic The publishExceptionTopic of the Publish Topic Exception. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnAclProfilePublishException(String msgVpnName, String aclProfileName, String topicSyntax, String publishExceptionTopic) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnAclProfilePublishExceptionWithHttpInfo(msgVpnName, aclProfileName, topicSyntax, publishExceptionTopic);
        return resp.getData();
    }

    /**
     * Deletes a Publish Topic Exception object.
     * Deletes a Publish Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Publish Topic Exception. (required)
     * @param publishExceptionTopic The publishExceptionTopic of the Publish Topic Exception. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnAclProfilePublishExceptionWithHttpInfo(String msgVpnName, String aclProfileName, String topicSyntax, String publishExceptionTopic) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnAclProfilePublishExceptionCall(msgVpnName, aclProfileName, topicSyntax, publishExceptionTopic, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Publish Topic Exception object. (asynchronously)
     * Deletes a Publish Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Publish Topic Exception. (required)
     * @param publishExceptionTopic The publishExceptionTopic of the Publish Topic Exception. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnAclProfilePublishExceptionAsync(String msgVpnName, String aclProfileName, String topicSyntax, String publishExceptionTopic, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnAclProfilePublishExceptionCall(msgVpnName, aclProfileName, topicSyntax, publishExceptionTopic, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnAclProfileSubscribeException */
    private com.squareup.okhttp.Call deleteMsgVpnAclProfileSubscribeExceptionCall(String msgVpnName, String aclProfileName, String topicSyntax, String subscribeExceptionTopic, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnAclProfileSubscribeException(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling deleteMsgVpnAclProfileSubscribeException(Async)");
        }
        
        // verify the required parameter 'topicSyntax' is set
        if (topicSyntax == null) {
            throw new ApiException("Missing the required parameter 'topicSyntax' when calling deleteMsgVpnAclProfileSubscribeException(Async)");
        }
        
        // verify the required parameter 'subscribeExceptionTopic' is set
        if (subscribeExceptionTopic == null) {
            throw new ApiException("Missing the required parameter 'subscribeExceptionTopic' when calling deleteMsgVpnAclProfileSubscribeException(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()))
        .replaceAll("\\{" + "topicSyntax" + "\\}", apiClient.escapeString(topicSyntax.toString()))
        .replaceAll("\\{" + "subscribeExceptionTopic" + "\\}", apiClient.escapeString(subscribeExceptionTopic.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Subscribe Topic Exception object.
     * Deletes a Subscribe Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Subscribe Topic Exception. (required)
     * @param subscribeExceptionTopic The subscribeExceptionTopic of the Subscribe Topic Exception. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnAclProfileSubscribeException(String msgVpnName, String aclProfileName, String topicSyntax, String subscribeExceptionTopic) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnAclProfileSubscribeExceptionWithHttpInfo(msgVpnName, aclProfileName, topicSyntax, subscribeExceptionTopic);
        return resp.getData();
    }

    /**
     * Deletes a Subscribe Topic Exception object.
     * Deletes a Subscribe Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Subscribe Topic Exception. (required)
     * @param subscribeExceptionTopic The subscribeExceptionTopic of the Subscribe Topic Exception. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnAclProfileSubscribeExceptionWithHttpInfo(String msgVpnName, String aclProfileName, String topicSyntax, String subscribeExceptionTopic) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnAclProfileSubscribeExceptionCall(msgVpnName, aclProfileName, topicSyntax, subscribeExceptionTopic, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Subscribe Topic Exception object. (asynchronously)
     * Deletes a Subscribe Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Subscribe Topic Exception. (required)
     * @param subscribeExceptionTopic The subscribeExceptionTopic of the Subscribe Topic Exception. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnAclProfileSubscribeExceptionAsync(String msgVpnName, String aclProfileName, String topicSyntax, String subscribeExceptionTopic, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnAclProfileSubscribeExceptionCall(msgVpnName, aclProfileName, topicSyntax, subscribeExceptionTopic, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnAuthorizationGroup */
    private com.squareup.okhttp.Call deleteMsgVpnAuthorizationGroupCall(String msgVpnName, String authorizationGroupName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnAuthorizationGroup(Async)");
        }
        
        // verify the required parameter 'authorizationGroupName' is set
        if (authorizationGroupName == null) {
            throw new ApiException("Missing the required parameter 'authorizationGroupName' when calling deleteMsgVpnAuthorizationGroup(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "authorizationGroupName" + "\\}", apiClient.escapeString(authorizationGroupName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a LDAP Authorization Group object.
     * Deletes a LDAP Authorization Group object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnAuthorizationGroup(String msgVpnName, String authorizationGroupName) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnAuthorizationGroupWithHttpInfo(msgVpnName, authorizationGroupName);
        return resp.getData();
    }

    /**
     * Deletes a LDAP Authorization Group object.
     * Deletes a LDAP Authorization Group object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnAuthorizationGroupWithHttpInfo(String msgVpnName, String authorizationGroupName) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnAuthorizationGroupCall(msgVpnName, authorizationGroupName, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a LDAP Authorization Group object. (asynchronously)
     * Deletes a LDAP Authorization Group object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnAuthorizationGroupAsync(String msgVpnName, String authorizationGroupName, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnAuthorizationGroupCall(msgVpnName, authorizationGroupName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnBridge */
    private com.squareup.okhttp.Call deleteMsgVpnBridgeCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnBridge(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling deleteMsgVpnBridge(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling deleteMsgVpnBridge(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Bridge object.
     * Deletes a Bridge object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnBridge(String msgVpnName, String bridgeName, String bridgeVirtualRouter) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnBridgeWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter);
        return resp.getData();
    }

    /**
     * Deletes a Bridge object.
     * Deletes a Bridge object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnBridgeWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnBridgeCall(msgVpnName, bridgeName, bridgeVirtualRouter, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Bridge object. (asynchronously)
     * Deletes a Bridge object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnBridgeAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnBridgeCall(msgVpnName, bridgeName, bridgeVirtualRouter, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnBridgeRemoteMsgVpn */
    private com.squareup.okhttp.Call deleteMsgVpnBridgeRemoteMsgVpnCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling deleteMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling deleteMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnName' is set
        if (remoteMsgVpnName == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnName' when calling deleteMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnLocation' is set
        if (remoteMsgVpnLocation == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnLocation' when calling deleteMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnInterface' is set
        if (remoteMsgVpnInterface == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnInterface' when calling deleteMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()))
        .replaceAll("\\{" + "remoteMsgVpnName" + "\\}", apiClient.escapeString(remoteMsgVpnName.toString()))
        .replaceAll("\\{" + "remoteMsgVpnLocation" + "\\}", apiClient.escapeString(remoteMsgVpnLocation.toString()))
        .replaceAll("\\{" + "remoteMsgVpnInterface" + "\\}", apiClient.escapeString(remoteMsgVpnInterface.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Remote Message VPN object.
     * Deletes a Remote Message VPN object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnBridgeRemoteMsgVpn(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnBridgeRemoteMsgVpnWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface);
        return resp.getData();
    }

    /**
     * Deletes a Remote Message VPN object.
     * Deletes a Remote Message VPN object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnBridgeRemoteMsgVpnWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnBridgeRemoteMsgVpnCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Remote Message VPN object. (asynchronously)
     * Deletes a Remote Message VPN object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnBridgeRemoteMsgVpnAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnBridgeRemoteMsgVpnCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnBridgeRemoteSubscription */
    private com.squareup.okhttp.Call deleteMsgVpnBridgeRemoteSubscriptionCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnBridgeRemoteSubscription(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling deleteMsgVpnBridgeRemoteSubscription(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling deleteMsgVpnBridgeRemoteSubscription(Async)");
        }
        
        // verify the required parameter 'remoteSubscriptionTopic' is set
        if (remoteSubscriptionTopic == null) {
            throw new ApiException("Missing the required parameter 'remoteSubscriptionTopic' when calling deleteMsgVpnBridgeRemoteSubscription(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()))
        .replaceAll("\\{" + "remoteSubscriptionTopic" + "\\}", apiClient.escapeString(remoteSubscriptionTopic.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Remote Subscription object.
     * Deletes a Remote Subscription object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteSubscriptionTopic The remoteSubscriptionTopic of the Remote Subscription. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnBridgeRemoteSubscription(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnBridgeRemoteSubscriptionWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic);
        return resp.getData();
    }

    /**
     * Deletes a Remote Subscription object.
     * Deletes a Remote Subscription object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteSubscriptionTopic The remoteSubscriptionTopic of the Remote Subscription. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnBridgeRemoteSubscriptionWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnBridgeRemoteSubscriptionCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Remote Subscription object. (asynchronously)
     * Deletes a Remote Subscription object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteSubscriptionTopic The remoteSubscriptionTopic of the Remote Subscription. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnBridgeRemoteSubscriptionAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnBridgeRemoteSubscriptionCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnBridgeTlsTrustedCommonName */
    private com.squareup.okhttp.Call deleteMsgVpnBridgeTlsTrustedCommonNameCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling deleteMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling deleteMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'tlsTrustedCommonName' is set
        if (tlsTrustedCommonName == null) {
            throw new ApiException("Missing the required parameter 'tlsTrustedCommonName' when calling deleteMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()))
        .replaceAll("\\{" + "tlsTrustedCommonName" + "\\}", apiClient.escapeString(tlsTrustedCommonName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Trusted Common Name object.
     * Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnBridgeTlsTrustedCommonName(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName);
        return resp.getData();
    }

    /**
     * Deletes a Trusted Common Name object.
     * Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnBridgeTlsTrustedCommonNameCall(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Trusted Common Name object. (asynchronously)
     * Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnBridgeTlsTrustedCommonNameAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnBridgeTlsTrustedCommonNameCall(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnClientProfile */
    private com.squareup.okhttp.Call deleteMsgVpnClientProfileCall(String msgVpnName, String clientProfileName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnClientProfile(Async)");
        }
        
        // verify the required parameter 'clientProfileName' is set
        if (clientProfileName == null) {
            throw new ApiException("Missing the required parameter 'clientProfileName' when calling deleteMsgVpnClientProfile(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "clientProfileName" + "\\}", apiClient.escapeString(clientProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Client Profile object.
     * Deletes a Client Profile object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnClientProfile(String msgVpnName, String clientProfileName) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnClientProfileWithHttpInfo(msgVpnName, clientProfileName);
        return resp.getData();
    }

    /**
     * Deletes a Client Profile object.
     * Deletes a Client Profile object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnClientProfileWithHttpInfo(String msgVpnName, String clientProfileName) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnClientProfileCall(msgVpnName, clientProfileName, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Client Profile object. (asynchronously)
     * Deletes a Client Profile object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnClientProfileAsync(String msgVpnName, String clientProfileName, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnClientProfileCall(msgVpnName, clientProfileName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnClientUsername */
    private com.squareup.okhttp.Call deleteMsgVpnClientUsernameCall(String msgVpnName, String clientUsername, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnClientUsername(Async)");
        }
        
        // verify the required parameter 'clientUsername' is set
        if (clientUsername == null) {
            throw new ApiException("Missing the required parameter 'clientUsername' when calling deleteMsgVpnClientUsername(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "clientUsername" + "\\}", apiClient.escapeString(clientUsername.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Client Username object.
     * Deletes a Client Username object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnClientUsername(String msgVpnName, String clientUsername) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnClientUsernameWithHttpInfo(msgVpnName, clientUsername);
        return resp.getData();
    }

    /**
     * Deletes a Client Username object.
     * Deletes a Client Username object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnClientUsernameWithHttpInfo(String msgVpnName, String clientUsername) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnClientUsernameCall(msgVpnName, clientUsername, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Client Username object. (asynchronously)
     * Deletes a Client Username object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnClientUsernameAsync(String msgVpnName, String clientUsername, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnClientUsernameCall(msgVpnName, clientUsername, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnQueue */
    private com.squareup.okhttp.Call deleteMsgVpnQueueCall(String msgVpnName, String queueName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnQueue(Async)");
        }
        
        // verify the required parameter 'queueName' is set
        if (queueName == null) {
            throw new ApiException("Missing the required parameter 'queueName' when calling deleteMsgVpnQueue(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/queues/{queueName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "queueName" + "\\}", apiClient.escapeString(queueName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Queue object.
     * Deletes a Queue object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnQueue(String msgVpnName, String queueName) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnQueueWithHttpInfo(msgVpnName, queueName);
        return resp.getData();
    }

    /**
     * Deletes a Queue object.
     * Deletes a Queue object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnQueueWithHttpInfo(String msgVpnName, String queueName) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnQueueCall(msgVpnName, queueName, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Queue object. (asynchronously)
     * Deletes a Queue object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnQueueAsync(String msgVpnName, String queueName, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnQueueCall(msgVpnName, queueName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnQueueSubscription */
    private com.squareup.okhttp.Call deleteMsgVpnQueueSubscriptionCall(String msgVpnName, String queueName, String subscriptionTopic, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnQueueSubscription(Async)");
        }
        
        // verify the required parameter 'queueName' is set
        if (queueName == null) {
            throw new ApiException("Missing the required parameter 'queueName' when calling deleteMsgVpnQueueSubscription(Async)");
        }
        
        // verify the required parameter 'subscriptionTopic' is set
        if (subscriptionTopic == null) {
            throw new ApiException("Missing the required parameter 'subscriptionTopic' when calling deleteMsgVpnQueueSubscription(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "queueName" + "\\}", apiClient.escapeString(queueName.toString()))
        .replaceAll("\\{" + "subscriptionTopic" + "\\}", apiClient.escapeString(subscriptionTopic.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Queue Subscription object.
     * Deletes a Queue Subscription object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param subscriptionTopic The subscriptionTopic of the Queue Subscription. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnQueueSubscription(String msgVpnName, String queueName, String subscriptionTopic) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnQueueSubscriptionWithHttpInfo(msgVpnName, queueName, subscriptionTopic);
        return resp.getData();
    }

    /**
     * Deletes a Queue Subscription object.
     * Deletes a Queue Subscription object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param subscriptionTopic The subscriptionTopic of the Queue Subscription. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnQueueSubscriptionWithHttpInfo(String msgVpnName, String queueName, String subscriptionTopic) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnQueueSubscriptionCall(msgVpnName, queueName, subscriptionTopic, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Queue Subscription object. (asynchronously)
     * Deletes a Queue Subscription object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param subscriptionTopic The subscriptionTopic of the Queue Subscription. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnQueueSubscriptionAsync(String msgVpnName, String queueName, String subscriptionTopic, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnQueueSubscriptionCall(msgVpnName, queueName, subscriptionTopic, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnRestDeliveryPoint */
    private com.squareup.okhttp.Call deleteMsgVpnRestDeliveryPointCall(String msgVpnName, String restDeliveryPointName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnRestDeliveryPoint(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling deleteMsgVpnRestDeliveryPoint(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a REST Delivery Point object.
     * Deletes a REST Delivery Point object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnRestDeliveryPoint(String msgVpnName, String restDeliveryPointName) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnRestDeliveryPointWithHttpInfo(msgVpnName, restDeliveryPointName);
        return resp.getData();
    }

    /**
     * Deletes a REST Delivery Point object.
     * Deletes a REST Delivery Point object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnRestDeliveryPointWithHttpInfo(String msgVpnName, String restDeliveryPointName) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnRestDeliveryPointCall(msgVpnName, restDeliveryPointName, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a REST Delivery Point object. (asynchronously)
     * Deletes a REST Delivery Point object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnRestDeliveryPointAsync(String msgVpnName, String restDeliveryPointName, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnRestDeliveryPointCall(msgVpnName, restDeliveryPointName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnRestDeliveryPointQueueBinding */
    private com.squareup.okhttp.Call deleteMsgVpnRestDeliveryPointQueueBindingCall(String msgVpnName, String restDeliveryPointName, String queueBindingName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling deleteMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new ApiException("Missing the required parameter 'queueBindingName' when calling deleteMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "queueBindingName" + "\\}", apiClient.escapeString(queueBindingName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Queue Binding object.
     * Deletes a Queue Binding object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnRestDeliveryPointQueueBinding(String msgVpnName, String restDeliveryPointName, String queueBindingName) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName);
        return resp.getData();
    }

    /**
     * Deletes a Queue Binding object.
     * Deletes a Queue Binding object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnRestDeliveryPointQueueBindingCall(msgVpnName, restDeliveryPointName, queueBindingName, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Queue Binding object. (asynchronously)
     * Deletes a Queue Binding object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnRestDeliveryPointQueueBindingAsync(String msgVpnName, String restDeliveryPointName, String queueBindingName, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnRestDeliveryPointQueueBindingCall(msgVpnName, restDeliveryPointName, queueBindingName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnRestDeliveryPointRestConsumer */
    private com.squareup.okhttp.Call deleteMsgVpnRestDeliveryPointRestConsumerCall(String msgVpnName, String restDeliveryPointName, String restConsumerName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling deleteMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new ApiException("Missing the required parameter 'restConsumerName' when calling deleteMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "restConsumerName" + "\\}", apiClient.escapeString(restConsumerName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a REST Consumer object.
     * Deletes a REST Consumer object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnRestDeliveryPointRestConsumer(String msgVpnName, String restDeliveryPointName, String restConsumerName) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName);
        return resp.getData();
    }

    /**
     * Deletes a REST Consumer object.
     * Deletes a REST Consumer object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnRestDeliveryPointRestConsumerCall(msgVpnName, restDeliveryPointName, restConsumerName, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a REST Consumer object. (asynchronously)
     * Deletes a REST Consumer object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnRestDeliveryPointRestConsumerAsync(String msgVpnName, String restDeliveryPointName, String restConsumerName, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnRestDeliveryPointRestConsumerCall(msgVpnName, restDeliveryPointName, restConsumerName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName */
    private com.squareup.okhttp.Call deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameCall(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new ApiException("Missing the required parameter 'restConsumerName' when calling deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'tlsTrustedCommonName' is set
        if (tlsTrustedCommonName == null) {
            throw new ApiException("Missing the required parameter 'tlsTrustedCommonName' when calling deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "restConsumerName" + "\\}", apiClient.escapeString(restConsumerName.toString()))
        .replaceAll("\\{" + "tlsTrustedCommonName" + "\\}", apiClient.escapeString(tlsTrustedCommonName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Trusted Common Name object.
     * Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, tlsTrustedCommonName);
        return resp.getData();
    }

    /**
     * Deletes a Trusted Common Name object.
     * Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameCall(msgVpnName, restDeliveryPointName, restConsumerName, tlsTrustedCommonName, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Trusted Common Name object. (asynchronously)
     * Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameAsync(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameCall(msgVpnName, restDeliveryPointName, restConsumerName, tlsTrustedCommonName, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for deleteMsgVpnSequencedTopic */
    private com.squareup.okhttp.Call deleteMsgVpnSequencedTopicCall(String msgVpnName, String sequencedTopic, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling deleteMsgVpnSequencedTopic(Async)");
        }
        
        // verify the required parameter 'sequencedTopic' is set
        if (sequencedTopic == null) {
            throw new ApiException("Missing the required parameter 'sequencedTopic' when calling deleteMsgVpnSequencedTopic(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "sequencedTopic" + "\\}", apiClient.escapeString(sequencedTopic.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Deletes a Sequenced Topic object.
     * Deletes a Sequenced Topic object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param sequencedTopic The sequencedTopic of the Sequenced Topic. (required)
     * @return SempMetaOnlyResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public SempMetaOnlyResponse deleteMsgVpnSequencedTopic(String msgVpnName, String sequencedTopic) throws ApiException {
        ApiResponse<SempMetaOnlyResponse> resp = deleteMsgVpnSequencedTopicWithHttpInfo(msgVpnName, sequencedTopic);
        return resp.getData();
    }

    /**
     * Deletes a Sequenced Topic object.
     * Deletes a Sequenced Topic object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param sequencedTopic The sequencedTopic of the Sequenced Topic. (required)
     * @return ApiResponse&lt;SempMetaOnlyResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<SempMetaOnlyResponse> deleteMsgVpnSequencedTopicWithHttpInfo(String msgVpnName, String sequencedTopic) throws ApiException {
        com.squareup.okhttp.Call call = deleteMsgVpnSequencedTopicCall(msgVpnName, sequencedTopic, null, null);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Deletes a Sequenced Topic object. (asynchronously)
     * Deletes a Sequenced Topic object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param sequencedTopic The sequencedTopic of the Sequenced Topic. (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteMsgVpnSequencedTopicAsync(String msgVpnName, String sequencedTopic, final ApiCallback<SempMetaOnlyResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteMsgVpnSequencedTopicCall(msgVpnName, sequencedTopic, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<SempMetaOnlyResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpn */
    private com.squareup.okhttp.Call getMsgVpnCall(String msgVpnName, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpn(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Message VPN object.
     * Gets a Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnResponse getMsgVpn(String msgVpnName, List<String> select) throws ApiException {
        ApiResponse<MsgVpnResponse> resp = getMsgVpnWithHttpInfo(msgVpnName, select);
        return resp.getData();
    }

    /**
     * Gets a Message VPN object.
     * Gets a Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnResponse> getMsgVpnWithHttpInfo(String msgVpnName, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnCall(msgVpnName, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Message VPN object. (asynchronously)
     * Gets a Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnAsync(String msgVpnName, List<String> select, final ApiCallback<MsgVpnResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnCall(msgVpnName, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnAclProfile */
    private com.squareup.okhttp.Call getMsgVpnAclProfileCall(String msgVpnName, String aclProfileName, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnAclProfile(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling getMsgVpnAclProfile(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets an ACL Profile object.
     * Gets an ACL Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfileResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfileResponse getMsgVpnAclProfile(String msgVpnName, String aclProfileName, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfileResponse> resp = getMsgVpnAclProfileWithHttpInfo(msgVpnName, aclProfileName, select);
        return resp.getData();
    }

    /**
     * Gets an ACL Profile object.
     * Gets an ACL Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfileResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfileResponse> getMsgVpnAclProfileWithHttpInfo(String msgVpnName, String aclProfileName, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnAclProfileCall(msgVpnName, aclProfileName, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets an ACL Profile object. (asynchronously)
     * Gets an ACL Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnAclProfileAsync(String msgVpnName, String aclProfileName, List<String> select, final ApiCallback<MsgVpnAclProfileResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnAclProfileCall(msgVpnName, aclProfileName, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnAclProfileClientConnectException */
    private com.squareup.okhttp.Call getMsgVpnAclProfileClientConnectExceptionCall(String msgVpnName, String aclProfileName, String clientConnectExceptionAddress, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnAclProfileClientConnectException(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling getMsgVpnAclProfileClientConnectException(Async)");
        }
        
        // verify the required parameter 'clientConnectExceptionAddress' is set
        if (clientConnectExceptionAddress == null) {
            throw new ApiException("Missing the required parameter 'clientConnectExceptionAddress' when calling getMsgVpnAclProfileClientConnectException(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()))
        .replaceAll("\\{" + "clientConnectExceptionAddress" + "\\}", apiClient.escapeString(clientConnectExceptionAddress.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Client Connect Exception object.
     * Gets a Client Connect Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param clientConnectExceptionAddress The clientConnectExceptionAddress of the Client Connect Exception. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfileClientConnectExceptionResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfileClientConnectExceptionResponse getMsgVpnAclProfileClientConnectException(String msgVpnName, String aclProfileName, String clientConnectExceptionAddress, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfileClientConnectExceptionResponse> resp = getMsgVpnAclProfileClientConnectExceptionWithHttpInfo(msgVpnName, aclProfileName, clientConnectExceptionAddress, select);
        return resp.getData();
    }

    /**
     * Gets a Client Connect Exception object.
     * Gets a Client Connect Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param clientConnectExceptionAddress The clientConnectExceptionAddress of the Client Connect Exception. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfileClientConnectExceptionResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfileClientConnectExceptionResponse> getMsgVpnAclProfileClientConnectExceptionWithHttpInfo(String msgVpnName, String aclProfileName, String clientConnectExceptionAddress, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnAclProfileClientConnectExceptionCall(msgVpnName, aclProfileName, clientConnectExceptionAddress, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileClientConnectExceptionResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Client Connect Exception object. (asynchronously)
     * Gets a Client Connect Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param clientConnectExceptionAddress The clientConnectExceptionAddress of the Client Connect Exception. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnAclProfileClientConnectExceptionAsync(String msgVpnName, String aclProfileName, String clientConnectExceptionAddress, List<String> select, final ApiCallback<MsgVpnAclProfileClientConnectExceptionResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnAclProfileClientConnectExceptionCall(msgVpnName, aclProfileName, clientConnectExceptionAddress, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileClientConnectExceptionResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnAclProfileClientConnectExceptions */
    private com.squareup.okhttp.Call getMsgVpnAclProfileClientConnectExceptionsCall(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnAclProfileClientConnectExceptions(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling getMsgVpnAclProfileClientConnectExceptions(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Client Connect Exception objects.
     * Gets a list of Client Connect Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfileClientConnectExceptionsResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfileClientConnectExceptionsResponse getMsgVpnAclProfileClientConnectExceptions(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfileClientConnectExceptionsResponse> resp = getMsgVpnAclProfileClientConnectExceptionsWithHttpInfo(msgVpnName, aclProfileName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Client Connect Exception objects.
     * Gets a list of Client Connect Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfileClientConnectExceptionsResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfileClientConnectExceptionsResponse> getMsgVpnAclProfileClientConnectExceptionsWithHttpInfo(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnAclProfileClientConnectExceptionsCall(msgVpnName, aclProfileName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileClientConnectExceptionsResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Client Connect Exception objects. (asynchronously)
     * Gets a list of Client Connect Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnAclProfileClientConnectExceptionsAsync(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnAclProfileClientConnectExceptionsResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnAclProfileClientConnectExceptionsCall(msgVpnName, aclProfileName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileClientConnectExceptionsResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnAclProfilePublishException */
    private com.squareup.okhttp.Call getMsgVpnAclProfilePublishExceptionCall(String msgVpnName, String aclProfileName, String topicSyntax, String publishExceptionTopic, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnAclProfilePublishException(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling getMsgVpnAclProfilePublishException(Async)");
        }
        
        // verify the required parameter 'topicSyntax' is set
        if (topicSyntax == null) {
            throw new ApiException("Missing the required parameter 'topicSyntax' when calling getMsgVpnAclProfilePublishException(Async)");
        }
        
        // verify the required parameter 'publishExceptionTopic' is set
        if (publishExceptionTopic == null) {
            throw new ApiException("Missing the required parameter 'publishExceptionTopic' when calling getMsgVpnAclProfilePublishException(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()))
        .replaceAll("\\{" + "topicSyntax" + "\\}", apiClient.escapeString(topicSyntax.toString()))
        .replaceAll("\\{" + "publishExceptionTopic" + "\\}", apiClient.escapeString(publishExceptionTopic.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Publish Topic Exception object.
     * Gets a Publish Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Publish Topic Exception. (required)
     * @param publishExceptionTopic The publishExceptionTopic of the Publish Topic Exception. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfilePublishExceptionResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfilePublishExceptionResponse getMsgVpnAclProfilePublishException(String msgVpnName, String aclProfileName, String topicSyntax, String publishExceptionTopic, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfilePublishExceptionResponse> resp = getMsgVpnAclProfilePublishExceptionWithHttpInfo(msgVpnName, aclProfileName, topicSyntax, publishExceptionTopic, select);
        return resp.getData();
    }

    /**
     * Gets a Publish Topic Exception object.
     * Gets a Publish Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Publish Topic Exception. (required)
     * @param publishExceptionTopic The publishExceptionTopic of the Publish Topic Exception. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfilePublishExceptionResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfilePublishExceptionResponse> getMsgVpnAclProfilePublishExceptionWithHttpInfo(String msgVpnName, String aclProfileName, String topicSyntax, String publishExceptionTopic, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnAclProfilePublishExceptionCall(msgVpnName, aclProfileName, topicSyntax, publishExceptionTopic, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfilePublishExceptionResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Publish Topic Exception object. (asynchronously)
     * Gets a Publish Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Publish Topic Exception. (required)
     * @param publishExceptionTopic The publishExceptionTopic of the Publish Topic Exception. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnAclProfilePublishExceptionAsync(String msgVpnName, String aclProfileName, String topicSyntax, String publishExceptionTopic, List<String> select, final ApiCallback<MsgVpnAclProfilePublishExceptionResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnAclProfilePublishExceptionCall(msgVpnName, aclProfileName, topicSyntax, publishExceptionTopic, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfilePublishExceptionResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnAclProfilePublishExceptions */
    private com.squareup.okhttp.Call getMsgVpnAclProfilePublishExceptionsCall(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnAclProfilePublishExceptions(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling getMsgVpnAclProfilePublishExceptions(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Publish Topic Exception objects.
     * Gets a list of Publish Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfilePublishExceptionsResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfilePublishExceptionsResponse getMsgVpnAclProfilePublishExceptions(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfilePublishExceptionsResponse> resp = getMsgVpnAclProfilePublishExceptionsWithHttpInfo(msgVpnName, aclProfileName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Publish Topic Exception objects.
     * Gets a list of Publish Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfilePublishExceptionsResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfilePublishExceptionsResponse> getMsgVpnAclProfilePublishExceptionsWithHttpInfo(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnAclProfilePublishExceptionsCall(msgVpnName, aclProfileName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfilePublishExceptionsResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Publish Topic Exception objects. (asynchronously)
     * Gets a list of Publish Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnAclProfilePublishExceptionsAsync(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnAclProfilePublishExceptionsResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnAclProfilePublishExceptionsCall(msgVpnName, aclProfileName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfilePublishExceptionsResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnAclProfileSubscribeException */
    private com.squareup.okhttp.Call getMsgVpnAclProfileSubscribeExceptionCall(String msgVpnName, String aclProfileName, String topicSyntax, String subscribeExceptionTopic, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnAclProfileSubscribeException(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling getMsgVpnAclProfileSubscribeException(Async)");
        }
        
        // verify the required parameter 'topicSyntax' is set
        if (topicSyntax == null) {
            throw new ApiException("Missing the required parameter 'topicSyntax' when calling getMsgVpnAclProfileSubscribeException(Async)");
        }
        
        // verify the required parameter 'subscribeExceptionTopic' is set
        if (subscribeExceptionTopic == null) {
            throw new ApiException("Missing the required parameter 'subscribeExceptionTopic' when calling getMsgVpnAclProfileSubscribeException(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()))
        .replaceAll("\\{" + "topicSyntax" + "\\}", apiClient.escapeString(topicSyntax.toString()))
        .replaceAll("\\{" + "subscribeExceptionTopic" + "\\}", apiClient.escapeString(subscribeExceptionTopic.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Subscribe Topic Exception object.
     * Gets a Subscribe Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Subscribe Topic Exception. (required)
     * @param subscribeExceptionTopic The subscribeExceptionTopic of the Subscribe Topic Exception. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfileSubscribeExceptionResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfileSubscribeExceptionResponse getMsgVpnAclProfileSubscribeException(String msgVpnName, String aclProfileName, String topicSyntax, String subscribeExceptionTopic, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfileSubscribeExceptionResponse> resp = getMsgVpnAclProfileSubscribeExceptionWithHttpInfo(msgVpnName, aclProfileName, topicSyntax, subscribeExceptionTopic, select);
        return resp.getData();
    }

    /**
     * Gets a Subscribe Topic Exception object.
     * Gets a Subscribe Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Subscribe Topic Exception. (required)
     * @param subscribeExceptionTopic The subscribeExceptionTopic of the Subscribe Topic Exception. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfileSubscribeExceptionResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfileSubscribeExceptionResponse> getMsgVpnAclProfileSubscribeExceptionWithHttpInfo(String msgVpnName, String aclProfileName, String topicSyntax, String subscribeExceptionTopic, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnAclProfileSubscribeExceptionCall(msgVpnName, aclProfileName, topicSyntax, subscribeExceptionTopic, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileSubscribeExceptionResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Subscribe Topic Exception object. (asynchronously)
     * Gets a Subscribe Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param topicSyntax The topicSyntax of the Subscribe Topic Exception. (required)
     * @param subscribeExceptionTopic The subscribeExceptionTopic of the Subscribe Topic Exception. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnAclProfileSubscribeExceptionAsync(String msgVpnName, String aclProfileName, String topicSyntax, String subscribeExceptionTopic, List<String> select, final ApiCallback<MsgVpnAclProfileSubscribeExceptionResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnAclProfileSubscribeExceptionCall(msgVpnName, aclProfileName, topicSyntax, subscribeExceptionTopic, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileSubscribeExceptionResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnAclProfileSubscribeExceptions */
    private com.squareup.okhttp.Call getMsgVpnAclProfileSubscribeExceptionsCall(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnAclProfileSubscribeExceptions(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling getMsgVpnAclProfileSubscribeExceptions(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Subscribe Topic Exception objects.
     * Gets a list of Subscribe Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfileSubscribeExceptionsResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfileSubscribeExceptionsResponse getMsgVpnAclProfileSubscribeExceptions(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfileSubscribeExceptionsResponse> resp = getMsgVpnAclProfileSubscribeExceptionsWithHttpInfo(msgVpnName, aclProfileName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Subscribe Topic Exception objects.
     * Gets a list of Subscribe Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfileSubscribeExceptionsResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfileSubscribeExceptionsResponse> getMsgVpnAclProfileSubscribeExceptionsWithHttpInfo(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnAclProfileSubscribeExceptionsCall(msgVpnName, aclProfileName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileSubscribeExceptionsResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Subscribe Topic Exception objects. (asynchronously)
     * Gets a list of Subscribe Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnAclProfileSubscribeExceptionsAsync(String msgVpnName, String aclProfileName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnAclProfileSubscribeExceptionsResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnAclProfileSubscribeExceptionsCall(msgVpnName, aclProfileName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileSubscribeExceptionsResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnAclProfiles */
    private com.squareup.okhttp.Call getMsgVpnAclProfilesCall(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnAclProfiles(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of ACL Profile objects.
     * Gets a list of ACL Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfilesResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfilesResponse getMsgVpnAclProfiles(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfilesResponse> resp = getMsgVpnAclProfilesWithHttpInfo(msgVpnName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of ACL Profile objects.
     * Gets a list of ACL Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfilesResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfilesResponse> getMsgVpnAclProfilesWithHttpInfo(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnAclProfilesCall(msgVpnName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfilesResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of ACL Profile objects. (asynchronously)
     * Gets a list of ACL Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnAclProfilesAsync(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnAclProfilesResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnAclProfilesCall(msgVpnName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfilesResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnAuthorizationGroup */
    private com.squareup.okhttp.Call getMsgVpnAuthorizationGroupCall(String msgVpnName, String authorizationGroupName, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnAuthorizationGroup(Async)");
        }
        
        // verify the required parameter 'authorizationGroupName' is set
        if (authorizationGroupName == null) {
            throw new ApiException("Missing the required parameter 'authorizationGroupName' when calling getMsgVpnAuthorizationGroup(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "authorizationGroupName" + "\\}", apiClient.escapeString(authorizationGroupName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a LDAP Authorization Group object.
     * Gets a LDAP Authorization Group object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAuthorizationGroupResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAuthorizationGroupResponse getMsgVpnAuthorizationGroup(String msgVpnName, String authorizationGroupName, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAuthorizationGroupResponse> resp = getMsgVpnAuthorizationGroupWithHttpInfo(msgVpnName, authorizationGroupName, select);
        return resp.getData();
    }

    /**
     * Gets a LDAP Authorization Group object.
     * Gets a LDAP Authorization Group object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAuthorizationGroupResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAuthorizationGroupResponse> getMsgVpnAuthorizationGroupWithHttpInfo(String msgVpnName, String authorizationGroupName, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnAuthorizationGroupCall(msgVpnName, authorizationGroupName, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAuthorizationGroupResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a LDAP Authorization Group object. (asynchronously)
     * Gets a LDAP Authorization Group object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnAuthorizationGroupAsync(String msgVpnName, String authorizationGroupName, List<String> select, final ApiCallback<MsgVpnAuthorizationGroupResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnAuthorizationGroupCall(msgVpnName, authorizationGroupName, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAuthorizationGroupResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnAuthorizationGroups */
    private com.squareup.okhttp.Call getMsgVpnAuthorizationGroupsCall(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnAuthorizationGroups(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/authorizationGroups".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of LDAP Authorization Group objects.
     * Gets a list of LDAP Authorization Group objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAuthorizationGroupsResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAuthorizationGroupsResponse getMsgVpnAuthorizationGroups(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAuthorizationGroupsResponse> resp = getMsgVpnAuthorizationGroupsWithHttpInfo(msgVpnName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of LDAP Authorization Group objects.
     * Gets a list of LDAP Authorization Group objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAuthorizationGroupsResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAuthorizationGroupsResponse> getMsgVpnAuthorizationGroupsWithHttpInfo(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnAuthorizationGroupsCall(msgVpnName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAuthorizationGroupsResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of LDAP Authorization Group objects. (asynchronously)
     * Gets a list of LDAP Authorization Group objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnAuthorizationGroupsAsync(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnAuthorizationGroupsResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnAuthorizationGroupsCall(msgVpnName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAuthorizationGroupsResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnBridge */
    private com.squareup.okhttp.Call getMsgVpnBridgeCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnBridge(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling getMsgVpnBridge(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridge(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Bridge object.
     * Gets a Bridge object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeResponse getMsgVpnBridge(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeResponse> resp = getMsgVpnBridgeWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, select);
        return resp.getData();
    }

    /**
     * Gets a Bridge object.
     * Gets a Bridge object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeResponse> getMsgVpnBridgeWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnBridgeCall(msgVpnName, bridgeName, bridgeVirtualRouter, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Bridge object. (asynchronously)
     * Gets a Bridge object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnBridgeAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> select, final ApiCallback<MsgVpnBridgeResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnBridgeCall(msgVpnName, bridgeName, bridgeVirtualRouter, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnBridgeRemoteMsgVpn */
    private com.squareup.okhttp.Call getMsgVpnBridgeRemoteMsgVpnCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnName' is set
        if (remoteMsgVpnName == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnName' when calling getMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnLocation' is set
        if (remoteMsgVpnLocation == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnLocation' when calling getMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnInterface' is set
        if (remoteMsgVpnInterface == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnInterface' when calling getMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()))
        .replaceAll("\\{" + "remoteMsgVpnName" + "\\}", apiClient.escapeString(remoteMsgVpnName.toString()))
        .replaceAll("\\{" + "remoteMsgVpnLocation" + "\\}", apiClient.escapeString(remoteMsgVpnLocation.toString()))
        .replaceAll("\\{" + "remoteMsgVpnInterface" + "\\}", apiClient.escapeString(remoteMsgVpnInterface.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Remote Message VPN object.
     * Gets a Remote Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeRemoteMsgVpnResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeRemoteMsgVpnResponse getMsgVpnBridgeRemoteMsgVpn(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeRemoteMsgVpnResponse> resp = getMsgVpnBridgeRemoteMsgVpnWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, select);
        return resp.getData();
    }

    /**
     * Gets a Remote Message VPN object.
     * Gets a Remote Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeRemoteMsgVpnResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeRemoteMsgVpnResponse> getMsgVpnBridgeRemoteMsgVpnWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnBridgeRemoteMsgVpnCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteMsgVpnResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Remote Message VPN object. (asynchronously)
     * Gets a Remote Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnBridgeRemoteMsgVpnAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, List<String> select, final ApiCallback<MsgVpnBridgeRemoteMsgVpnResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnBridgeRemoteMsgVpnCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteMsgVpnResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnBridgeRemoteMsgVpns */
    private com.squareup.okhttp.Call getMsgVpnBridgeRemoteMsgVpnsCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeRemoteMsgVpns(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeRemoteMsgVpns(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeRemoteMsgVpns(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Remote Message VPN objects.
     * Gets a list of Remote Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeRemoteMsgVpnsResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeRemoteMsgVpnsResponse getMsgVpnBridgeRemoteMsgVpns(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeRemoteMsgVpnsResponse> resp = getMsgVpnBridgeRemoteMsgVpnsWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Remote Message VPN objects.
     * Gets a list of Remote Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeRemoteMsgVpnsResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeRemoteMsgVpnsResponse> getMsgVpnBridgeRemoteMsgVpnsWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnBridgeRemoteMsgVpnsCall(msgVpnName, bridgeName, bridgeVirtualRouter, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteMsgVpnsResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Remote Message VPN objects. (asynchronously)
     * Gets a list of Remote Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnBridgeRemoteMsgVpnsAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> where, List<String> select, final ApiCallback<MsgVpnBridgeRemoteMsgVpnsResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnBridgeRemoteMsgVpnsCall(msgVpnName, bridgeName, bridgeVirtualRouter, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteMsgVpnsResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnBridgeRemoteSubscription */
    private com.squareup.okhttp.Call getMsgVpnBridgeRemoteSubscriptionCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeRemoteSubscription(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeRemoteSubscription(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeRemoteSubscription(Async)");
        }
        
        // verify the required parameter 'remoteSubscriptionTopic' is set
        if (remoteSubscriptionTopic == null) {
            throw new ApiException("Missing the required parameter 'remoteSubscriptionTopic' when calling getMsgVpnBridgeRemoteSubscription(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()))
        .replaceAll("\\{" + "remoteSubscriptionTopic" + "\\}", apiClient.escapeString(remoteSubscriptionTopic.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Remote Subscription object.
     * Gets a Remote Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteSubscriptionTopic The remoteSubscriptionTopic of the Remote Subscription. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeRemoteSubscriptionResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeRemoteSubscriptionResponse getMsgVpnBridgeRemoteSubscription(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeRemoteSubscriptionResponse> resp = getMsgVpnBridgeRemoteSubscriptionWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic, select);
        return resp.getData();
    }

    /**
     * Gets a Remote Subscription object.
     * Gets a Remote Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteSubscriptionTopic The remoteSubscriptionTopic of the Remote Subscription. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeRemoteSubscriptionResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeRemoteSubscriptionResponse> getMsgVpnBridgeRemoteSubscriptionWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnBridgeRemoteSubscriptionCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteSubscriptionResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Remote Subscription object. (asynchronously)
     * Gets a Remote Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteSubscriptionTopic The remoteSubscriptionTopic of the Remote Subscription. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnBridgeRemoteSubscriptionAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteSubscriptionTopic, List<String> select, final ApiCallback<MsgVpnBridgeRemoteSubscriptionResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnBridgeRemoteSubscriptionCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteSubscriptionTopic, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteSubscriptionResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnBridgeRemoteSubscriptions */
    private com.squareup.okhttp.Call getMsgVpnBridgeRemoteSubscriptionsCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeRemoteSubscriptions(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeRemoteSubscriptions(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeRemoteSubscriptions(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Remote Subscription objects.
     * Gets a list of Remote Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeRemoteSubscriptionsResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeRemoteSubscriptionsResponse getMsgVpnBridgeRemoteSubscriptions(String msgVpnName, String bridgeName, String bridgeVirtualRouter, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeRemoteSubscriptionsResponse> resp = getMsgVpnBridgeRemoteSubscriptionsWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Remote Subscription objects.
     * Gets a list of Remote Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeRemoteSubscriptionsResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeRemoteSubscriptionsResponse> getMsgVpnBridgeRemoteSubscriptionsWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnBridgeRemoteSubscriptionsCall(msgVpnName, bridgeName, bridgeVirtualRouter, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteSubscriptionsResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Remote Subscription objects. (asynchronously)
     * Gets a list of Remote Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnBridgeRemoteSubscriptionsAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnBridgeRemoteSubscriptionsResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnBridgeRemoteSubscriptionsCall(msgVpnName, bridgeName, bridgeVirtualRouter, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteSubscriptionsResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnBridgeTlsTrustedCommonName */
    private com.squareup.okhttp.Call getMsgVpnBridgeTlsTrustedCommonNameCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'tlsTrustedCommonName' is set
        if (tlsTrustedCommonName == null) {
            throw new ApiException("Missing the required parameter 'tlsTrustedCommonName' when calling getMsgVpnBridgeTlsTrustedCommonName(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()))
        .replaceAll("\\{" + "tlsTrustedCommonName" + "\\}", apiClient.escapeString(tlsTrustedCommonName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Trusted Common Name object.
     * Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeTlsTrustedCommonNameResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeTlsTrustedCommonNameResponse getMsgVpnBridgeTlsTrustedCommonName(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeTlsTrustedCommonNameResponse> resp = getMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName, select);
        return resp.getData();
    }

    /**
     * Gets a Trusted Common Name object.
     * Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeTlsTrustedCommonNameResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeTlsTrustedCommonNameResponse> getMsgVpnBridgeTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnBridgeTlsTrustedCommonNameCall(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeTlsTrustedCommonNameResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Trusted Common Name object. (asynchronously)
     * Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnBridgeTlsTrustedCommonNameAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String tlsTrustedCommonName, List<String> select, final ApiCallback<MsgVpnBridgeTlsTrustedCommonNameResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnBridgeTlsTrustedCommonNameCall(msgVpnName, bridgeName, bridgeVirtualRouter, tlsTrustedCommonName, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeTlsTrustedCommonNameResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnBridgeTlsTrustedCommonNames */
    private com.squareup.okhttp.Call getMsgVpnBridgeTlsTrustedCommonNamesCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnBridgeTlsTrustedCommonNames(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling getMsgVpnBridgeTlsTrustedCommonNames(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling getMsgVpnBridgeTlsTrustedCommonNames(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Trusted Common Name objects.
     * Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeTlsTrustedCommonNamesResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeTlsTrustedCommonNamesResponse getMsgVpnBridgeTlsTrustedCommonNames(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeTlsTrustedCommonNamesResponse> resp = getMsgVpnBridgeTlsTrustedCommonNamesWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Trusted Common Name objects.
     * Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeTlsTrustedCommonNamesResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeTlsTrustedCommonNamesResponse> getMsgVpnBridgeTlsTrustedCommonNamesWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnBridgeTlsTrustedCommonNamesCall(msgVpnName, bridgeName, bridgeVirtualRouter, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeTlsTrustedCommonNamesResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Trusted Common Name objects. (asynchronously)
     * Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnBridgeTlsTrustedCommonNamesAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, List<String> where, List<String> select, final ApiCallback<MsgVpnBridgeTlsTrustedCommonNamesResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnBridgeTlsTrustedCommonNamesCall(msgVpnName, bridgeName, bridgeVirtualRouter, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeTlsTrustedCommonNamesResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnBridges */
    private com.squareup.okhttp.Call getMsgVpnBridgesCall(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnBridges(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Bridge objects.
     * Gets a list of Bridge objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgesResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgesResponse getMsgVpnBridges(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgesResponse> resp = getMsgVpnBridgesWithHttpInfo(msgVpnName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Bridge objects.
     * Gets a list of Bridge objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgesResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgesResponse> getMsgVpnBridgesWithHttpInfo(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnBridgesCall(msgVpnName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgesResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Bridge objects. (asynchronously)
     * Gets a list of Bridge objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnBridgesAsync(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnBridgesResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnBridgesCall(msgVpnName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgesResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnClientProfile */
    private com.squareup.okhttp.Call getMsgVpnClientProfileCall(String msgVpnName, String clientProfileName, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnClientProfile(Async)");
        }
        
        // verify the required parameter 'clientProfileName' is set
        if (clientProfileName == null) {
            throw new ApiException("Missing the required parameter 'clientProfileName' when calling getMsgVpnClientProfile(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "clientProfileName" + "\\}", apiClient.escapeString(clientProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Client Profile object.
     * Gets a Client Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnClientProfileResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnClientProfileResponse getMsgVpnClientProfile(String msgVpnName, String clientProfileName, List<String> select) throws ApiException {
        ApiResponse<MsgVpnClientProfileResponse> resp = getMsgVpnClientProfileWithHttpInfo(msgVpnName, clientProfileName, select);
        return resp.getData();
    }

    /**
     * Gets a Client Profile object.
     * Gets a Client Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnClientProfileResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnClientProfileResponse> getMsgVpnClientProfileWithHttpInfo(String msgVpnName, String clientProfileName, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnClientProfileCall(msgVpnName, clientProfileName, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnClientProfileResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Client Profile object. (asynchronously)
     * Gets a Client Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnClientProfileAsync(String msgVpnName, String clientProfileName, List<String> select, final ApiCallback<MsgVpnClientProfileResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnClientProfileCall(msgVpnName, clientProfileName, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnClientProfileResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnClientProfiles */
    private com.squareup.okhttp.Call getMsgVpnClientProfilesCall(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnClientProfiles(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientProfiles".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Client Profile objects.
     * Gets a list of Client Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnClientProfilesResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnClientProfilesResponse getMsgVpnClientProfiles(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnClientProfilesResponse> resp = getMsgVpnClientProfilesWithHttpInfo(msgVpnName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Client Profile objects.
     * Gets a list of Client Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnClientProfilesResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnClientProfilesResponse> getMsgVpnClientProfilesWithHttpInfo(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnClientProfilesCall(msgVpnName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnClientProfilesResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Client Profile objects. (asynchronously)
     * Gets a list of Client Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnClientProfilesAsync(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnClientProfilesResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnClientProfilesCall(msgVpnName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnClientProfilesResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnClientUsername */
    private com.squareup.okhttp.Call getMsgVpnClientUsernameCall(String msgVpnName, String clientUsername, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnClientUsername(Async)");
        }
        
        // verify the required parameter 'clientUsername' is set
        if (clientUsername == null) {
            throw new ApiException("Missing the required parameter 'clientUsername' when calling getMsgVpnClientUsername(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "clientUsername" + "\\}", apiClient.escapeString(clientUsername.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Client Username object.
     * Gets a Client Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnClientUsernameResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnClientUsernameResponse getMsgVpnClientUsername(String msgVpnName, String clientUsername, List<String> select) throws ApiException {
        ApiResponse<MsgVpnClientUsernameResponse> resp = getMsgVpnClientUsernameWithHttpInfo(msgVpnName, clientUsername, select);
        return resp.getData();
    }

    /**
     * Gets a Client Username object.
     * Gets a Client Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnClientUsernameResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnClientUsernameResponse> getMsgVpnClientUsernameWithHttpInfo(String msgVpnName, String clientUsername, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnClientUsernameCall(msgVpnName, clientUsername, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnClientUsernameResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Client Username object. (asynchronously)
     * Gets a Client Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnClientUsernameAsync(String msgVpnName, String clientUsername, List<String> select, final ApiCallback<MsgVpnClientUsernameResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnClientUsernameCall(msgVpnName, clientUsername, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnClientUsernameResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnClientUsernames */
    private com.squareup.okhttp.Call getMsgVpnClientUsernamesCall(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnClientUsernames(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientUsernames".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Client Username objects.
     * Gets a list of Client Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnClientUsernamesResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnClientUsernamesResponse getMsgVpnClientUsernames(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnClientUsernamesResponse> resp = getMsgVpnClientUsernamesWithHttpInfo(msgVpnName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Client Username objects.
     * Gets a list of Client Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnClientUsernamesResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnClientUsernamesResponse> getMsgVpnClientUsernamesWithHttpInfo(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnClientUsernamesCall(msgVpnName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnClientUsernamesResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Client Username objects. (asynchronously)
     * Gets a list of Client Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnClientUsernamesAsync(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnClientUsernamesResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnClientUsernamesCall(msgVpnName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnClientUsernamesResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnQueue */
    private com.squareup.okhttp.Call getMsgVpnQueueCall(String msgVpnName, String queueName, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnQueue(Async)");
        }
        
        // verify the required parameter 'queueName' is set
        if (queueName == null) {
            throw new ApiException("Missing the required parameter 'queueName' when calling getMsgVpnQueue(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/queues/{queueName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "queueName" + "\\}", apiClient.escapeString(queueName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Queue object.
     * Gets a Queue object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnQueueResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnQueueResponse getMsgVpnQueue(String msgVpnName, String queueName, List<String> select) throws ApiException {
        ApiResponse<MsgVpnQueueResponse> resp = getMsgVpnQueueWithHttpInfo(msgVpnName, queueName, select);
        return resp.getData();
    }

    /**
     * Gets a Queue object.
     * Gets a Queue object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnQueueResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnQueueResponse> getMsgVpnQueueWithHttpInfo(String msgVpnName, String queueName, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnQueueCall(msgVpnName, queueName, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnQueueResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Queue object. (asynchronously)
     * Gets a Queue object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnQueueAsync(String msgVpnName, String queueName, List<String> select, final ApiCallback<MsgVpnQueueResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnQueueCall(msgVpnName, queueName, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnQueueResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnQueueSubscription */
    private com.squareup.okhttp.Call getMsgVpnQueueSubscriptionCall(String msgVpnName, String queueName, String subscriptionTopic, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnQueueSubscription(Async)");
        }
        
        // verify the required parameter 'queueName' is set
        if (queueName == null) {
            throw new ApiException("Missing the required parameter 'queueName' when calling getMsgVpnQueueSubscription(Async)");
        }
        
        // verify the required parameter 'subscriptionTopic' is set
        if (subscriptionTopic == null) {
            throw new ApiException("Missing the required parameter 'subscriptionTopic' when calling getMsgVpnQueueSubscription(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "queueName" + "\\}", apiClient.escapeString(queueName.toString()))
        .replaceAll("\\{" + "subscriptionTopic" + "\\}", apiClient.escapeString(subscriptionTopic.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Queue Subscription object.
     * Gets a Queue Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param subscriptionTopic The subscriptionTopic of the Queue Subscription. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnQueueSubscriptionResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnQueueSubscriptionResponse getMsgVpnQueueSubscription(String msgVpnName, String queueName, String subscriptionTopic, List<String> select) throws ApiException {
        ApiResponse<MsgVpnQueueSubscriptionResponse> resp = getMsgVpnQueueSubscriptionWithHttpInfo(msgVpnName, queueName, subscriptionTopic, select);
        return resp.getData();
    }

    /**
     * Gets a Queue Subscription object.
     * Gets a Queue Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param subscriptionTopic The subscriptionTopic of the Queue Subscription. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnQueueSubscriptionResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnQueueSubscriptionResponse> getMsgVpnQueueSubscriptionWithHttpInfo(String msgVpnName, String queueName, String subscriptionTopic, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnQueueSubscriptionCall(msgVpnName, queueName, subscriptionTopic, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnQueueSubscriptionResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Queue Subscription object. (asynchronously)
     * Gets a Queue Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param subscriptionTopic The subscriptionTopic of the Queue Subscription. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnQueueSubscriptionAsync(String msgVpnName, String queueName, String subscriptionTopic, List<String> select, final ApiCallback<MsgVpnQueueSubscriptionResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnQueueSubscriptionCall(msgVpnName, queueName, subscriptionTopic, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnQueueSubscriptionResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnQueueSubscriptions */
    private com.squareup.okhttp.Call getMsgVpnQueueSubscriptionsCall(String msgVpnName, String queueName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnQueueSubscriptions(Async)");
        }
        
        // verify the required parameter 'queueName' is set
        if (queueName == null) {
            throw new ApiException("Missing the required parameter 'queueName' when calling getMsgVpnQueueSubscriptions(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "queueName" + "\\}", apiClient.escapeString(queueName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Queue Subscription objects.
     * Gets a list of Queue Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnQueueSubscriptionsResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnQueueSubscriptionsResponse getMsgVpnQueueSubscriptions(String msgVpnName, String queueName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnQueueSubscriptionsResponse> resp = getMsgVpnQueueSubscriptionsWithHttpInfo(msgVpnName, queueName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Queue Subscription objects.
     * Gets a list of Queue Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnQueueSubscriptionsResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnQueueSubscriptionsResponse> getMsgVpnQueueSubscriptionsWithHttpInfo(String msgVpnName, String queueName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnQueueSubscriptionsCall(msgVpnName, queueName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnQueueSubscriptionsResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Queue Subscription objects. (asynchronously)
     * Gets a list of Queue Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnQueueSubscriptionsAsync(String msgVpnName, String queueName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnQueueSubscriptionsResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnQueueSubscriptionsCall(msgVpnName, queueName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnQueueSubscriptionsResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnQueues */
    private com.squareup.okhttp.Call getMsgVpnQueuesCall(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnQueues(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/queues".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Queue objects.
     * Gets a list of Queue objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnQueuesResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnQueuesResponse getMsgVpnQueues(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnQueuesResponse> resp = getMsgVpnQueuesWithHttpInfo(msgVpnName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Queue objects.
     * Gets a list of Queue objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnQueuesResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnQueuesResponse> getMsgVpnQueuesWithHttpInfo(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnQueuesCall(msgVpnName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnQueuesResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Queue objects. (asynchronously)
     * Gets a list of Queue objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnQueuesAsync(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnQueuesResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnQueuesCall(msgVpnName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnQueuesResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnRestDeliveryPoint */
    private com.squareup.okhttp.Call getMsgVpnRestDeliveryPointCall(String msgVpnName, String restDeliveryPointName, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPoint(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPoint(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a REST Delivery Point object.
     * Gets a REST Delivery Point object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointResponse getMsgVpnRestDeliveryPoint(String msgVpnName, String restDeliveryPointName, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointResponse> resp = getMsgVpnRestDeliveryPointWithHttpInfo(msgVpnName, restDeliveryPointName, select);
        return resp.getData();
    }

    /**
     * Gets a REST Delivery Point object.
     * Gets a REST Delivery Point object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointResponse> getMsgVpnRestDeliveryPointWithHttpInfo(String msgVpnName, String restDeliveryPointName, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointCall(msgVpnName, restDeliveryPointName, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a REST Delivery Point object. (asynchronously)
     * Gets a REST Delivery Point object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnRestDeliveryPointAsync(String msgVpnName, String restDeliveryPointName, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointCall(msgVpnName, restDeliveryPointName, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnRestDeliveryPointQueueBinding */
    private com.squareup.okhttp.Call getMsgVpnRestDeliveryPointQueueBindingCall(String msgVpnName, String restDeliveryPointName, String queueBindingName, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new ApiException("Missing the required parameter 'queueBindingName' when calling getMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "queueBindingName" + "\\}", apiClient.escapeString(queueBindingName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Queue Binding object.
     * Gets a Queue Binding object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointQueueBindingResponse getMsgVpnRestDeliveryPointQueueBinding(String msgVpnName, String restDeliveryPointName, String queueBindingName, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointQueueBindingResponse> resp = getMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, select);
        return resp.getData();
    }

    /**
     * Gets a Queue Binding object.
     * Gets a Queue Binding object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointQueueBindingResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointQueueBindingResponse> getMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointQueueBindingCall(msgVpnName, restDeliveryPointName, queueBindingName, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointQueueBindingResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Queue Binding object. (asynchronously)
     * Gets a Queue Binding object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnRestDeliveryPointQueueBindingAsync(String msgVpnName, String restDeliveryPointName, String queueBindingName, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointQueueBindingResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointQueueBindingCall(msgVpnName, restDeliveryPointName, queueBindingName, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointQueueBindingResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnRestDeliveryPointQueueBindings */
    private com.squareup.okhttp.Call getMsgVpnRestDeliveryPointQueueBindingsCall(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointQueueBindings(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointQueueBindings(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Queue Binding objects.
     * Gets a list of Queue Binding objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingsResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointQueueBindingsResponse getMsgVpnRestDeliveryPointQueueBindings(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointQueueBindingsResponse> resp = getMsgVpnRestDeliveryPointQueueBindingsWithHttpInfo(msgVpnName, restDeliveryPointName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Queue Binding objects.
     * Gets a list of Queue Binding objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointQueueBindingsResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointQueueBindingsResponse> getMsgVpnRestDeliveryPointQueueBindingsWithHttpInfo(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointQueueBindingsCall(msgVpnName, restDeliveryPointName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointQueueBindingsResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Queue Binding objects. (asynchronously)
     * Gets a list of Queue Binding objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnRestDeliveryPointQueueBindingsAsync(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointQueueBindingsResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointQueueBindingsCall(msgVpnName, restDeliveryPointName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointQueueBindingsResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnRestDeliveryPointRestConsumer */
    private com.squareup.okhttp.Call getMsgVpnRestDeliveryPointRestConsumerCall(String msgVpnName, String restDeliveryPointName, String restConsumerName, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new ApiException("Missing the required parameter 'restConsumerName' when calling getMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "restConsumerName" + "\\}", apiClient.escapeString(restConsumerName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a REST Consumer object.
     * Gets a REST Consumer object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointRestConsumerResponse getMsgVpnRestDeliveryPointRestConsumer(String msgVpnName, String restDeliveryPointName, String restConsumerName, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointRestConsumerResponse> resp = getMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, select);
        return resp.getData();
    }

    /**
     * Gets a REST Consumer object.
     * Gets a REST Consumer object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointRestConsumerResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointRestConsumerResponse> getMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointRestConsumerCall(msgVpnName, restDeliveryPointName, restConsumerName, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a REST Consumer object. (asynchronously)
     * Gets a REST Consumer object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnRestDeliveryPointRestConsumerAsync(String msgVpnName, String restDeliveryPointName, String restConsumerName, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointRestConsumerResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointRestConsumerCall(msgVpnName, restDeliveryPointName, restConsumerName, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName */
    private com.squareup.okhttp.Call getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameCall(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new ApiException("Missing the required parameter 'restConsumerName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        
        // verify the required parameter 'tlsTrustedCommonName' is set
        if (tlsTrustedCommonName == null) {
            throw new ApiException("Missing the required parameter 'tlsTrustedCommonName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "restConsumerName" + "\\}", apiClient.escapeString(restConsumerName.toString()))
        .replaceAll("\\{" + "tlsTrustedCommonName" + "\\}", apiClient.escapeString(tlsTrustedCommonName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Trusted Common Name object.
     * Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse> resp = getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, tlsTrustedCommonName, select);
        return resp.getData();
    }

    /**
     * Gets a Trusted Common Name object.
     * Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse> getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameCall(msgVpnName, restDeliveryPointName, restConsumerName, tlsTrustedCommonName, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Trusted Common Name object. (asynchronously)
     * Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param tlsTrustedCommonName The tlsTrustedCommonName of the Trusted Common Name. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameAsync(String msgVpnName, String restDeliveryPointName, String restConsumerName, String tlsTrustedCommonName, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameCall(msgVpnName, restDeliveryPointName, restConsumerName, tlsTrustedCommonName, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames */
    private com.squareup.okhttp.Call getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesCall(String msgVpnName, String restDeliveryPointName, String restConsumerName, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames(Async)");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new ApiException("Missing the required parameter 'restConsumerName' when calling getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "restConsumerName" + "\\}", apiClient.escapeString(restConsumerName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Trusted Common Name objects.
     * Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNames(String msgVpnName, String restDeliveryPointName, String restConsumerName, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse> resp = getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Trusted Common Name objects.
     * Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse> getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesCall(msgVpnName, restDeliveryPointName, restConsumerName, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Trusted Common Name objects. (asynchronously)
     * Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesAsync(String msgVpnName, String restDeliveryPointName, String restConsumerName, List<String> where, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesCall(msgVpnName, restDeliveryPointName, restConsumerName, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnRestDeliveryPointRestConsumers */
    private com.squareup.okhttp.Call getMsgVpnRestDeliveryPointRestConsumersCall(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPointRestConsumers(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling getMsgVpnRestDeliveryPointRestConsumers(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of REST Consumer objects.
     * Gets a list of REST Consumer objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointRestConsumersResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointRestConsumersResponse getMsgVpnRestDeliveryPointRestConsumers(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointRestConsumersResponse> resp = getMsgVpnRestDeliveryPointRestConsumersWithHttpInfo(msgVpnName, restDeliveryPointName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of REST Consumer objects.
     * Gets a list of REST Consumer objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointRestConsumersResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointRestConsumersResponse> getMsgVpnRestDeliveryPointRestConsumersWithHttpInfo(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointRestConsumersCall(msgVpnName, restDeliveryPointName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumersResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of REST Consumer objects. (asynchronously)
     * Gets a list of REST Consumer objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnRestDeliveryPointRestConsumersAsync(String msgVpnName, String restDeliveryPointName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointRestConsumersResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointRestConsumersCall(msgVpnName, restDeliveryPointName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumersResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnRestDeliveryPoints */
    private com.squareup.okhttp.Call getMsgVpnRestDeliveryPointsCall(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnRestDeliveryPoints(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of REST Delivery Point objects.
     * Gets a list of REST Delivery Point objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointsResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointsResponse getMsgVpnRestDeliveryPoints(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointsResponse> resp = getMsgVpnRestDeliveryPointsWithHttpInfo(msgVpnName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of REST Delivery Point objects.
     * Gets a list of REST Delivery Point objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointsResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointsResponse> getMsgVpnRestDeliveryPointsWithHttpInfo(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointsCall(msgVpnName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointsResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of REST Delivery Point objects. (asynchronously)
     * Gets a list of REST Delivery Point objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnRestDeliveryPointsAsync(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointsResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnRestDeliveryPointsCall(msgVpnName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointsResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnSequencedTopic */
    private com.squareup.okhttp.Call getMsgVpnSequencedTopicCall(String msgVpnName, String sequencedTopic, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnSequencedTopic(Async)");
        }
        
        // verify the required parameter 'sequencedTopic' is set
        if (sequencedTopic == null) {
            throw new ApiException("Missing the required parameter 'sequencedTopic' when calling getMsgVpnSequencedTopic(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "sequencedTopic" + "\\}", apiClient.escapeString(sequencedTopic.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a Sequenced Topic object.
     * Gets a Sequenced Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param sequencedTopic The sequencedTopic of the Sequenced Topic. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnSequencedTopicResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnSequencedTopicResponse getMsgVpnSequencedTopic(String msgVpnName, String sequencedTopic, List<String> select) throws ApiException {
        ApiResponse<MsgVpnSequencedTopicResponse> resp = getMsgVpnSequencedTopicWithHttpInfo(msgVpnName, sequencedTopic, select);
        return resp.getData();
    }

    /**
     * Gets a Sequenced Topic object.
     * Gets a Sequenced Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param sequencedTopic The sequencedTopic of the Sequenced Topic. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnSequencedTopicResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnSequencedTopicResponse> getMsgVpnSequencedTopicWithHttpInfo(String msgVpnName, String sequencedTopic, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnSequencedTopicCall(msgVpnName, sequencedTopic, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnSequencedTopicResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a Sequenced Topic object. (asynchronously)
     * Gets a Sequenced Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param sequencedTopic The sequencedTopic of the Sequenced Topic. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnSequencedTopicAsync(String msgVpnName, String sequencedTopic, List<String> select, final ApiCallback<MsgVpnSequencedTopicResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnSequencedTopicCall(msgVpnName, sequencedTopic, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnSequencedTopicResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpnSequencedTopics */
    private com.squareup.okhttp.Call getMsgVpnSequencedTopicsCall(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling getMsgVpnSequencedTopics(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/sequencedTopics".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Sequenced Topic objects.
     * Gets a list of Sequenced Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnSequencedTopicsResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnSequencedTopicsResponse getMsgVpnSequencedTopics(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnSequencedTopicsResponse> resp = getMsgVpnSequencedTopicsWithHttpInfo(msgVpnName, count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Sequenced Topic objects.
     * Gets a list of Sequenced Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnSequencedTopicsResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnSequencedTopicsResponse> getMsgVpnSequencedTopicsWithHttpInfo(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnSequencedTopicsCall(msgVpnName, count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnSequencedTopicsResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Sequenced Topic objects. (asynchronously)
     * Gets a list of Sequenced Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnSequencedTopicsAsync(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnSequencedTopicsResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnSequencedTopicsCall(msgVpnName, count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnSequencedTopicsResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for getMsgVpns */
    private com.squareup.okhttp.Call getMsgVpnsCall(Integer count, String cursor, List<String> where, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        

        // create path and map variables
        String localVarPath = "/msgVpns".replaceAll("\\{format\\}","json");

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (count != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
        if (cursor != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
        if (where != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "where", where));
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Gets a list of Message VPN objects.
     * Gets a list of Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnsResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnsResponse getMsgVpns(Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        ApiResponse<MsgVpnsResponse> resp = getMsgVpnsWithHttpInfo(count, cursor, where, select);
        return resp.getData();
    }

    /**
     * Gets a list of Message VPN objects.
     * Gets a list of Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnsResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnsResponse> getMsgVpnsWithHttpInfo(Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = getMsgVpnsCall(count, cursor, where, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnsResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Gets a list of Message VPN objects. (asynchronously)
     * Gets a list of Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
     * @param count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (optional, default to 10)
     * @param cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). (optional)
     * @param where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). (optional)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call getMsgVpnsAsync(Integer count, String cursor, List<String> where, List<String> select, final ApiCallback<MsgVpnsResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = getMsgVpnsCall(count, cursor, where, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnsResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for replaceMsgVpn */
    private com.squareup.okhttp.Call replaceMsgVpnCall(String msgVpnName, MsgVpn body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling replaceMsgVpn(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceMsgVpn(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Replaces a Message VPN object.
     * Replaces a Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnResponse replaceMsgVpn(String msgVpnName, MsgVpn body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnResponse> resp = replaceMsgVpnWithHttpInfo(msgVpnName, body, select);
        return resp.getData();
    }

    /**
     * Replaces a Message VPN object.
     * Replaces a Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnResponse> replaceMsgVpnWithHttpInfo(String msgVpnName, MsgVpn body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = replaceMsgVpnCall(msgVpnName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Replaces a Message VPN object. (asynchronously)
     * Replaces a Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call replaceMsgVpnAsync(String msgVpnName, MsgVpn body, List<String> select, final ApiCallback<MsgVpnResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = replaceMsgVpnCall(msgVpnName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for replaceMsgVpnAclProfile */
    private com.squareup.okhttp.Call replaceMsgVpnAclProfileCall(String msgVpnName, String aclProfileName, MsgVpnAclProfile body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling replaceMsgVpnAclProfile(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling replaceMsgVpnAclProfile(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceMsgVpnAclProfile(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Replaces an ACL Profile object.
     * Replaces an ACL Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The ACL Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfileResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfileResponse replaceMsgVpnAclProfile(String msgVpnName, String aclProfileName, MsgVpnAclProfile body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfileResponse> resp = replaceMsgVpnAclProfileWithHttpInfo(msgVpnName, aclProfileName, body, select);
        return resp.getData();
    }

    /**
     * Replaces an ACL Profile object.
     * Replaces an ACL Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The ACL Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfileResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfileResponse> replaceMsgVpnAclProfileWithHttpInfo(String msgVpnName, String aclProfileName, MsgVpnAclProfile body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = replaceMsgVpnAclProfileCall(msgVpnName, aclProfileName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Replaces an ACL Profile object. (asynchronously)
     * Replaces an ACL Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The ACL Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call replaceMsgVpnAclProfileAsync(String msgVpnName, String aclProfileName, MsgVpnAclProfile body, List<String> select, final ApiCallback<MsgVpnAclProfileResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = replaceMsgVpnAclProfileCall(msgVpnName, aclProfileName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for replaceMsgVpnAuthorizationGroup */
    private com.squareup.okhttp.Call replaceMsgVpnAuthorizationGroupCall(String msgVpnName, String authorizationGroupName, MsgVpnAuthorizationGroup body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling replaceMsgVpnAuthorizationGroup(Async)");
        }
        
        // verify the required parameter 'authorizationGroupName' is set
        if (authorizationGroupName == null) {
            throw new ApiException("Missing the required parameter 'authorizationGroupName' when calling replaceMsgVpnAuthorizationGroup(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceMsgVpnAuthorizationGroup(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "authorizationGroupName" + "\\}", apiClient.escapeString(authorizationGroupName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Replaces a LDAP Authorization Group object.
     * Replaces a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @param body The LDAP Authorization Group object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAuthorizationGroupResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAuthorizationGroupResponse replaceMsgVpnAuthorizationGroup(String msgVpnName, String authorizationGroupName, MsgVpnAuthorizationGroup body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAuthorizationGroupResponse> resp = replaceMsgVpnAuthorizationGroupWithHttpInfo(msgVpnName, authorizationGroupName, body, select);
        return resp.getData();
    }

    /**
     * Replaces a LDAP Authorization Group object.
     * Replaces a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @param body The LDAP Authorization Group object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAuthorizationGroupResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAuthorizationGroupResponse> replaceMsgVpnAuthorizationGroupWithHttpInfo(String msgVpnName, String authorizationGroupName, MsgVpnAuthorizationGroup body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = replaceMsgVpnAuthorizationGroupCall(msgVpnName, authorizationGroupName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAuthorizationGroupResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Replaces a LDAP Authorization Group object. (asynchronously)
     * Replaces a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @param body The LDAP Authorization Group object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call replaceMsgVpnAuthorizationGroupAsync(String msgVpnName, String authorizationGroupName, MsgVpnAuthorizationGroup body, List<String> select, final ApiCallback<MsgVpnAuthorizationGroupResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = replaceMsgVpnAuthorizationGroupCall(msgVpnName, authorizationGroupName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAuthorizationGroupResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for replaceMsgVpnBridge */
    private com.squareup.okhttp.Call replaceMsgVpnBridgeCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling replaceMsgVpnBridge(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling replaceMsgVpnBridge(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling replaceMsgVpnBridge(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceMsgVpnBridge(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Replaces a Bridge object.
     * Replaces a Bridge object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeResponse replaceMsgVpnBridge(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeResponse> resp = replaceMsgVpnBridgeWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, body, select);
        return resp.getData();
    }

    /**
     * Replaces a Bridge object.
     * Replaces a Bridge object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeResponse> replaceMsgVpnBridgeWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = replaceMsgVpnBridgeCall(msgVpnName, bridgeName, bridgeVirtualRouter, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Replaces a Bridge object. (asynchronously)
     * Replaces a Bridge object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call replaceMsgVpnBridgeAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, List<String> select, final ApiCallback<MsgVpnBridgeResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = replaceMsgVpnBridgeCall(msgVpnName, bridgeName, bridgeVirtualRouter, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for replaceMsgVpnBridgeRemoteMsgVpn */
    private com.squareup.okhttp.Call replaceMsgVpnBridgeRemoteMsgVpnCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling replaceMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling replaceMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling replaceMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnName' is set
        if (remoteMsgVpnName == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnName' when calling replaceMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnLocation' is set
        if (remoteMsgVpnLocation == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnLocation' when calling replaceMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnInterface' is set
        if (remoteMsgVpnInterface == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnInterface' when calling replaceMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()))
        .replaceAll("\\{" + "remoteMsgVpnName" + "\\}", apiClient.escapeString(remoteMsgVpnName.toString()))
        .replaceAll("\\{" + "remoteMsgVpnLocation" + "\\}", apiClient.escapeString(remoteMsgVpnLocation.toString()))
        .replaceAll("\\{" + "remoteMsgVpnInterface" + "\\}", apiClient.escapeString(remoteMsgVpnInterface.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Replaces a Remote Message VPN object.
     * Replaces a Remote Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeRemoteMsgVpnResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeRemoteMsgVpnResponse replaceMsgVpnBridgeRemoteMsgVpn(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeRemoteMsgVpnResponse> resp = replaceMsgVpnBridgeRemoteMsgVpnWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, select);
        return resp.getData();
    }

    /**
     * Replaces a Remote Message VPN object.
     * Replaces a Remote Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeRemoteMsgVpnResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeRemoteMsgVpnResponse> replaceMsgVpnBridgeRemoteMsgVpnWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = replaceMsgVpnBridgeRemoteMsgVpnCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteMsgVpnResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Replaces a Remote Message VPN object. (asynchronously)
     * Replaces a Remote Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call replaceMsgVpnBridgeRemoteMsgVpnAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, List<String> select, final ApiCallback<MsgVpnBridgeRemoteMsgVpnResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = replaceMsgVpnBridgeRemoteMsgVpnCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteMsgVpnResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for replaceMsgVpnClientProfile */
    private com.squareup.okhttp.Call replaceMsgVpnClientProfileCall(String msgVpnName, String clientProfileName, MsgVpnClientProfile body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling replaceMsgVpnClientProfile(Async)");
        }
        
        // verify the required parameter 'clientProfileName' is set
        if (clientProfileName == null) {
            throw new ApiException("Missing the required parameter 'clientProfileName' when calling replaceMsgVpnClientProfile(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceMsgVpnClientProfile(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "clientProfileName" + "\\}", apiClient.escapeString(clientProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Replaces a Client Profile object.
     * Replaces a Client Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @param body The Client Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnClientProfileResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnClientProfileResponse replaceMsgVpnClientProfile(String msgVpnName, String clientProfileName, MsgVpnClientProfile body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnClientProfileResponse> resp = replaceMsgVpnClientProfileWithHttpInfo(msgVpnName, clientProfileName, body, select);
        return resp.getData();
    }

    /**
     * Replaces a Client Profile object.
     * Replaces a Client Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @param body The Client Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnClientProfileResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnClientProfileResponse> replaceMsgVpnClientProfileWithHttpInfo(String msgVpnName, String clientProfileName, MsgVpnClientProfile body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = replaceMsgVpnClientProfileCall(msgVpnName, clientProfileName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnClientProfileResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Replaces a Client Profile object. (asynchronously)
     * Replaces a Client Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @param body The Client Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call replaceMsgVpnClientProfileAsync(String msgVpnName, String clientProfileName, MsgVpnClientProfile body, List<String> select, final ApiCallback<MsgVpnClientProfileResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = replaceMsgVpnClientProfileCall(msgVpnName, clientProfileName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnClientProfileResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for replaceMsgVpnClientUsername */
    private com.squareup.okhttp.Call replaceMsgVpnClientUsernameCall(String msgVpnName, String clientUsername, MsgVpnClientUsername body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling replaceMsgVpnClientUsername(Async)");
        }
        
        // verify the required parameter 'clientUsername' is set
        if (clientUsername == null) {
            throw new ApiException("Missing the required parameter 'clientUsername' when calling replaceMsgVpnClientUsername(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceMsgVpnClientUsername(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "clientUsername" + "\\}", apiClient.escapeString(clientUsername.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Replaces a Client Username object.
     * Replaces a Client Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @param body The Client Username object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnClientUsernameResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnClientUsernameResponse replaceMsgVpnClientUsername(String msgVpnName, String clientUsername, MsgVpnClientUsername body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnClientUsernameResponse> resp = replaceMsgVpnClientUsernameWithHttpInfo(msgVpnName, clientUsername, body, select);
        return resp.getData();
    }

    /**
     * Replaces a Client Username object.
     * Replaces a Client Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @param body The Client Username object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnClientUsernameResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnClientUsernameResponse> replaceMsgVpnClientUsernameWithHttpInfo(String msgVpnName, String clientUsername, MsgVpnClientUsername body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = replaceMsgVpnClientUsernameCall(msgVpnName, clientUsername, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnClientUsernameResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Replaces a Client Username object. (asynchronously)
     * Replaces a Client Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @param body The Client Username object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call replaceMsgVpnClientUsernameAsync(String msgVpnName, String clientUsername, MsgVpnClientUsername body, List<String> select, final ApiCallback<MsgVpnClientUsernameResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = replaceMsgVpnClientUsernameCall(msgVpnName, clientUsername, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnClientUsernameResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for replaceMsgVpnQueue */
    private com.squareup.okhttp.Call replaceMsgVpnQueueCall(String msgVpnName, String queueName, MsgVpnQueue body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling replaceMsgVpnQueue(Async)");
        }
        
        // verify the required parameter 'queueName' is set
        if (queueName == null) {
            throw new ApiException("Missing the required parameter 'queueName' when calling replaceMsgVpnQueue(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceMsgVpnQueue(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/queues/{queueName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "queueName" + "\\}", apiClient.escapeString(queueName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Replaces a Queue object.
     * Replaces a Queue object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param body The Queue object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnQueueResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnQueueResponse replaceMsgVpnQueue(String msgVpnName, String queueName, MsgVpnQueue body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnQueueResponse> resp = replaceMsgVpnQueueWithHttpInfo(msgVpnName, queueName, body, select);
        return resp.getData();
    }

    /**
     * Replaces a Queue object.
     * Replaces a Queue object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param body The Queue object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnQueueResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnQueueResponse> replaceMsgVpnQueueWithHttpInfo(String msgVpnName, String queueName, MsgVpnQueue body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = replaceMsgVpnQueueCall(msgVpnName, queueName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnQueueResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Replaces a Queue object. (asynchronously)
     * Replaces a Queue object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param body The Queue object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call replaceMsgVpnQueueAsync(String msgVpnName, String queueName, MsgVpnQueue body, List<String> select, final ApiCallback<MsgVpnQueueResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = replaceMsgVpnQueueCall(msgVpnName, queueName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnQueueResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for replaceMsgVpnRestDeliveryPoint */
    private com.squareup.okhttp.Call replaceMsgVpnRestDeliveryPointCall(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling replaceMsgVpnRestDeliveryPoint(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling replaceMsgVpnRestDeliveryPoint(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceMsgVpnRestDeliveryPoint(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Replaces a REST Delivery Point object.
     * Replaces a REST Delivery Point object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointResponse replaceMsgVpnRestDeliveryPoint(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointResponse> resp = replaceMsgVpnRestDeliveryPointWithHttpInfo(msgVpnName, restDeliveryPointName, body, select);
        return resp.getData();
    }

    /**
     * Replaces a REST Delivery Point object.
     * Replaces a REST Delivery Point object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointResponse> replaceMsgVpnRestDeliveryPointWithHttpInfo(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = replaceMsgVpnRestDeliveryPointCall(msgVpnName, restDeliveryPointName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Replaces a REST Delivery Point object. (asynchronously)
     * Replaces a REST Delivery Point object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call replaceMsgVpnRestDeliveryPointAsync(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = replaceMsgVpnRestDeliveryPointCall(msgVpnName, restDeliveryPointName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for replaceMsgVpnRestDeliveryPointQueueBinding */
    private com.squareup.okhttp.Call replaceMsgVpnRestDeliveryPointQueueBindingCall(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling replaceMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling replaceMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new ApiException("Missing the required parameter 'queueBindingName' when calling replaceMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "queueBindingName" + "\\}", apiClient.escapeString(queueBindingName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Replaces a Queue Binding object.
     * Replaces a Queue Binding object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointQueueBindingResponse replaceMsgVpnRestDeliveryPointQueueBinding(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointQueueBindingResponse> resp = replaceMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, body, select);
        return resp.getData();
    }

    /**
     * Replaces a Queue Binding object.
     * Replaces a Queue Binding object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointQueueBindingResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointQueueBindingResponse> replaceMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = replaceMsgVpnRestDeliveryPointQueueBindingCall(msgVpnName, restDeliveryPointName, queueBindingName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointQueueBindingResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Replaces a Queue Binding object. (asynchronously)
     * Replaces a Queue Binding object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call replaceMsgVpnRestDeliveryPointQueueBindingAsync(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointQueueBindingResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = replaceMsgVpnRestDeliveryPointQueueBindingCall(msgVpnName, restDeliveryPointName, queueBindingName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointQueueBindingResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for replaceMsgVpnRestDeliveryPointRestConsumer */
    private com.squareup.okhttp.Call replaceMsgVpnRestDeliveryPointRestConsumerCall(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling replaceMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling replaceMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new ApiException("Missing the required parameter 'restConsumerName' when calling replaceMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling replaceMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "restConsumerName" + "\\}", apiClient.escapeString(restConsumerName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Replaces a REST Consumer object.
     * Replaces a REST Consumer object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointRestConsumerResponse replaceMsgVpnRestDeliveryPointRestConsumer(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointRestConsumerResponse> resp = replaceMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, body, select);
        return resp.getData();
    }

    /**
     * Replaces a REST Consumer object.
     * Replaces a REST Consumer object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointRestConsumerResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointRestConsumerResponse> replaceMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = replaceMsgVpnRestDeliveryPointRestConsumerCall(msgVpnName, restDeliveryPointName, restConsumerName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Replaces a REST Consumer object. (asynchronously)
     * Replaces a REST Consumer object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call replaceMsgVpnRestDeliveryPointRestConsumerAsync(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointRestConsumerResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = replaceMsgVpnRestDeliveryPointRestConsumerCall(msgVpnName, restDeliveryPointName, restConsumerName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for updateMsgVpn */
    private com.squareup.okhttp.Call updateMsgVpnCall(String msgVpnName, MsgVpn body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling updateMsgVpn(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMsgVpn(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Updates a Message VPN object.
     * Updates a Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnResponse updateMsgVpn(String msgVpnName, MsgVpn body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnResponse> resp = updateMsgVpnWithHttpInfo(msgVpnName, body, select);
        return resp.getData();
    }

    /**
     * Updates a Message VPN object.
     * Updates a Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnResponse> updateMsgVpnWithHttpInfo(String msgVpnName, MsgVpn body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = updateMsgVpnCall(msgVpnName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates a Message VPN object. (asynchronously)
     * Updates a Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param body The Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMsgVpnAsync(String msgVpnName, MsgVpn body, List<String> select, final ApiCallback<MsgVpnResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateMsgVpnCall(msgVpnName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for updateMsgVpnAclProfile */
    private com.squareup.okhttp.Call updateMsgVpnAclProfileCall(String msgVpnName, String aclProfileName, MsgVpnAclProfile body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling updateMsgVpnAclProfile(Async)");
        }
        
        // verify the required parameter 'aclProfileName' is set
        if (aclProfileName == null) {
            throw new ApiException("Missing the required parameter 'aclProfileName' when calling updateMsgVpnAclProfile(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMsgVpnAclProfile(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "aclProfileName" + "\\}", apiClient.escapeString(aclProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Updates an ACL Profile object.
     * Updates an ACL Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The ACL Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAclProfileResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAclProfileResponse updateMsgVpnAclProfile(String msgVpnName, String aclProfileName, MsgVpnAclProfile body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAclProfileResponse> resp = updateMsgVpnAclProfileWithHttpInfo(msgVpnName, aclProfileName, body, select);
        return resp.getData();
    }

    /**
     * Updates an ACL Profile object.
     * Updates an ACL Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The ACL Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAclProfileResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAclProfileResponse> updateMsgVpnAclProfileWithHttpInfo(String msgVpnName, String aclProfileName, MsgVpnAclProfile body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = updateMsgVpnAclProfileCall(msgVpnName, aclProfileName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates an ACL Profile object. (asynchronously)
     * Updates an ACL Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param aclProfileName The aclProfileName of the ACL Profile. (required)
     * @param body The ACL Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMsgVpnAclProfileAsync(String msgVpnName, String aclProfileName, MsgVpnAclProfile body, List<String> select, final ApiCallback<MsgVpnAclProfileResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateMsgVpnAclProfileCall(msgVpnName, aclProfileName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAclProfileResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for updateMsgVpnAuthorizationGroup */
    private com.squareup.okhttp.Call updateMsgVpnAuthorizationGroupCall(String msgVpnName, String authorizationGroupName, MsgVpnAuthorizationGroup body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling updateMsgVpnAuthorizationGroup(Async)");
        }
        
        // verify the required parameter 'authorizationGroupName' is set
        if (authorizationGroupName == null) {
            throw new ApiException("Missing the required parameter 'authorizationGroupName' when calling updateMsgVpnAuthorizationGroup(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMsgVpnAuthorizationGroup(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "authorizationGroupName" + "\\}", apiClient.escapeString(authorizationGroupName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Updates a LDAP Authorization Group object.
     * Updates a LDAP Authorization Group object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @param body The LDAP Authorization Group object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnAuthorizationGroupResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnAuthorizationGroupResponse updateMsgVpnAuthorizationGroup(String msgVpnName, String authorizationGroupName, MsgVpnAuthorizationGroup body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnAuthorizationGroupResponse> resp = updateMsgVpnAuthorizationGroupWithHttpInfo(msgVpnName, authorizationGroupName, body, select);
        return resp.getData();
    }

    /**
     * Updates a LDAP Authorization Group object.
     * Updates a LDAP Authorization Group object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @param body The LDAP Authorization Group object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnAuthorizationGroupResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnAuthorizationGroupResponse> updateMsgVpnAuthorizationGroupWithHttpInfo(String msgVpnName, String authorizationGroupName, MsgVpnAuthorizationGroup body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = updateMsgVpnAuthorizationGroupCall(msgVpnName, authorizationGroupName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnAuthorizationGroupResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates a LDAP Authorization Group object. (asynchronously)
     * Updates a LDAP Authorization Group object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param authorizationGroupName The authorizationGroupName of the LDAP Authorization Group. (required)
     * @param body The LDAP Authorization Group object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMsgVpnAuthorizationGroupAsync(String msgVpnName, String authorizationGroupName, MsgVpnAuthorizationGroup body, List<String> select, final ApiCallback<MsgVpnAuthorizationGroupResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateMsgVpnAuthorizationGroupCall(msgVpnName, authorizationGroupName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnAuthorizationGroupResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for updateMsgVpnBridge */
    private com.squareup.okhttp.Call updateMsgVpnBridgeCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling updateMsgVpnBridge(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling updateMsgVpnBridge(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling updateMsgVpnBridge(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMsgVpnBridge(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Updates a Bridge object.
     * Updates a Bridge object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeResponse updateMsgVpnBridge(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeResponse> resp = updateMsgVpnBridgeWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, body, select);
        return resp.getData();
    }

    /**
     * Updates a Bridge object.
     * Updates a Bridge object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeResponse> updateMsgVpnBridgeWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = updateMsgVpnBridgeCall(msgVpnName, bridgeName, bridgeVirtualRouter, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates a Bridge object. (asynchronously)
     * Updates a Bridge object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param body The Bridge object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMsgVpnBridgeAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, MsgVpnBridge body, List<String> select, final ApiCallback<MsgVpnBridgeResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateMsgVpnBridgeCall(msgVpnName, bridgeName, bridgeVirtualRouter, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for updateMsgVpnBridgeRemoteMsgVpn */
    private com.squareup.okhttp.Call updateMsgVpnBridgeRemoteMsgVpnCall(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling updateMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'bridgeName' is set
        if (bridgeName == null) {
            throw new ApiException("Missing the required parameter 'bridgeName' when calling updateMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'bridgeVirtualRouter' is set
        if (bridgeVirtualRouter == null) {
            throw new ApiException("Missing the required parameter 'bridgeVirtualRouter' when calling updateMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnName' is set
        if (remoteMsgVpnName == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnName' when calling updateMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnLocation' is set
        if (remoteMsgVpnLocation == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnLocation' when calling updateMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'remoteMsgVpnInterface' is set
        if (remoteMsgVpnInterface == null) {
            throw new ApiException("Missing the required parameter 'remoteMsgVpnInterface' when calling updateMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMsgVpnBridgeRemoteMsgVpn(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "bridgeName" + "\\}", apiClient.escapeString(bridgeName.toString()))
        .replaceAll("\\{" + "bridgeVirtualRouter" + "\\}", apiClient.escapeString(bridgeVirtualRouter.toString()))
        .replaceAll("\\{" + "remoteMsgVpnName" + "\\}", apiClient.escapeString(remoteMsgVpnName.toString()))
        .replaceAll("\\{" + "remoteMsgVpnLocation" + "\\}", apiClient.escapeString(remoteMsgVpnLocation.toString()))
        .replaceAll("\\{" + "remoteMsgVpnInterface" + "\\}", apiClient.escapeString(remoteMsgVpnInterface.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Updates a Remote Message VPN object.
     * Updates a Remote Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnBridgeRemoteMsgVpnResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnBridgeRemoteMsgVpnResponse updateMsgVpnBridgeRemoteMsgVpn(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnBridgeRemoteMsgVpnResponse> resp = updateMsgVpnBridgeRemoteMsgVpnWithHttpInfo(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, select);
        return resp.getData();
    }

    /**
     * Updates a Remote Message VPN object.
     * Updates a Remote Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnBridgeRemoteMsgVpnResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnBridgeRemoteMsgVpnResponse> updateMsgVpnBridgeRemoteMsgVpnWithHttpInfo(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = updateMsgVpnBridgeRemoteMsgVpnCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteMsgVpnResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates a Remote Message VPN object. (asynchronously)
     * Updates a Remote Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param bridgeName The bridgeName of the Bridge. (required)
     * @param bridgeVirtualRouter The bridgeVirtualRouter of the Bridge. (required)
     * @param remoteMsgVpnName The remoteMsgVpnName of the Remote Message VPN. (required)
     * @param remoteMsgVpnLocation The remoteMsgVpnLocation of the Remote Message VPN. (required)
     * @param remoteMsgVpnInterface The remoteMsgVpnInterface of the Remote Message VPN. (required)
     * @param body The Remote Message VPN object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMsgVpnBridgeRemoteMsgVpnAsync(String msgVpnName, String bridgeName, String bridgeVirtualRouter, String remoteMsgVpnName, String remoteMsgVpnLocation, String remoteMsgVpnInterface, MsgVpnBridgeRemoteMsgVpn body, List<String> select, final ApiCallback<MsgVpnBridgeRemoteMsgVpnResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateMsgVpnBridgeRemoteMsgVpnCall(msgVpnName, bridgeName, bridgeVirtualRouter, remoteMsgVpnName, remoteMsgVpnLocation, remoteMsgVpnInterface, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnBridgeRemoteMsgVpnResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for updateMsgVpnClientProfile */
    private com.squareup.okhttp.Call updateMsgVpnClientProfileCall(String msgVpnName, String clientProfileName, MsgVpnClientProfile body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling updateMsgVpnClientProfile(Async)");
        }
        
        // verify the required parameter 'clientProfileName' is set
        if (clientProfileName == null) {
            throw new ApiException("Missing the required parameter 'clientProfileName' when calling updateMsgVpnClientProfile(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMsgVpnClientProfile(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "clientProfileName" + "\\}", apiClient.escapeString(clientProfileName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Updates a Client Profile object.
     * Updates a Client Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @param body The Client Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnClientProfileResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnClientProfileResponse updateMsgVpnClientProfile(String msgVpnName, String clientProfileName, MsgVpnClientProfile body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnClientProfileResponse> resp = updateMsgVpnClientProfileWithHttpInfo(msgVpnName, clientProfileName, body, select);
        return resp.getData();
    }

    /**
     * Updates a Client Profile object.
     * Updates a Client Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @param body The Client Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnClientProfileResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnClientProfileResponse> updateMsgVpnClientProfileWithHttpInfo(String msgVpnName, String clientProfileName, MsgVpnClientProfile body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = updateMsgVpnClientProfileCall(msgVpnName, clientProfileName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnClientProfileResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates a Client Profile object. (asynchronously)
     * Updates a Client Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientProfileName The clientProfileName of the Client Profile. (required)
     * @param body The Client Profile object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMsgVpnClientProfileAsync(String msgVpnName, String clientProfileName, MsgVpnClientProfile body, List<String> select, final ApiCallback<MsgVpnClientProfileResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateMsgVpnClientProfileCall(msgVpnName, clientProfileName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnClientProfileResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for updateMsgVpnClientUsername */
    private com.squareup.okhttp.Call updateMsgVpnClientUsernameCall(String msgVpnName, String clientUsername, MsgVpnClientUsername body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling updateMsgVpnClientUsername(Async)");
        }
        
        // verify the required parameter 'clientUsername' is set
        if (clientUsername == null) {
            throw new ApiException("Missing the required parameter 'clientUsername' when calling updateMsgVpnClientUsername(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMsgVpnClientUsername(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "clientUsername" + "\\}", apiClient.escapeString(clientUsername.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Updates a Client Username object.
     * Updates a Client Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @param body The Client Username object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnClientUsernameResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnClientUsernameResponse updateMsgVpnClientUsername(String msgVpnName, String clientUsername, MsgVpnClientUsername body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnClientUsernameResponse> resp = updateMsgVpnClientUsernameWithHttpInfo(msgVpnName, clientUsername, body, select);
        return resp.getData();
    }

    /**
     * Updates a Client Username object.
     * Updates a Client Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @param body The Client Username object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnClientUsernameResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnClientUsernameResponse> updateMsgVpnClientUsernameWithHttpInfo(String msgVpnName, String clientUsername, MsgVpnClientUsername body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = updateMsgVpnClientUsernameCall(msgVpnName, clientUsername, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnClientUsernameResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates a Client Username object. (asynchronously)
     * Updates a Client Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param clientUsername The clientUsername of the Client Username. (required)
     * @param body The Client Username object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMsgVpnClientUsernameAsync(String msgVpnName, String clientUsername, MsgVpnClientUsername body, List<String> select, final ApiCallback<MsgVpnClientUsernameResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateMsgVpnClientUsernameCall(msgVpnName, clientUsername, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnClientUsernameResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for updateMsgVpnQueue */
    private com.squareup.okhttp.Call updateMsgVpnQueueCall(String msgVpnName, String queueName, MsgVpnQueue body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling updateMsgVpnQueue(Async)");
        }
        
        // verify the required parameter 'queueName' is set
        if (queueName == null) {
            throw new ApiException("Missing the required parameter 'queueName' when calling updateMsgVpnQueue(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMsgVpnQueue(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/queues/{queueName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "queueName" + "\\}", apiClient.escapeString(queueName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Updates a Queue object.
     * Updates a Queue object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param body The Queue object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnQueueResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnQueueResponse updateMsgVpnQueue(String msgVpnName, String queueName, MsgVpnQueue body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnQueueResponse> resp = updateMsgVpnQueueWithHttpInfo(msgVpnName, queueName, body, select);
        return resp.getData();
    }

    /**
     * Updates a Queue object.
     * Updates a Queue object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param body The Queue object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnQueueResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnQueueResponse> updateMsgVpnQueueWithHttpInfo(String msgVpnName, String queueName, MsgVpnQueue body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = updateMsgVpnQueueCall(msgVpnName, queueName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnQueueResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates a Queue object. (asynchronously)
     * Updates a Queue object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param queueName The queueName of the Queue. (required)
     * @param body The Queue object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMsgVpnQueueAsync(String msgVpnName, String queueName, MsgVpnQueue body, List<String> select, final ApiCallback<MsgVpnQueueResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateMsgVpnQueueCall(msgVpnName, queueName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnQueueResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for updateMsgVpnRestDeliveryPoint */
    private com.squareup.okhttp.Call updateMsgVpnRestDeliveryPointCall(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling updateMsgVpnRestDeliveryPoint(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling updateMsgVpnRestDeliveryPoint(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMsgVpnRestDeliveryPoint(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Updates a REST Delivery Point object.
     * Updates a REST Delivery Point object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointResponse updateMsgVpnRestDeliveryPoint(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointResponse> resp = updateMsgVpnRestDeliveryPointWithHttpInfo(msgVpnName, restDeliveryPointName, body, select);
        return resp.getData();
    }

    /**
     * Updates a REST Delivery Point object.
     * Updates a REST Delivery Point object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointResponse> updateMsgVpnRestDeliveryPointWithHttpInfo(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = updateMsgVpnRestDeliveryPointCall(msgVpnName, restDeliveryPointName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates a REST Delivery Point object. (asynchronously)
     * Updates a REST Delivery Point object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param body The REST Delivery Point object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMsgVpnRestDeliveryPointAsync(String msgVpnName, String restDeliveryPointName, MsgVpnRestDeliveryPoint body, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateMsgVpnRestDeliveryPointCall(msgVpnName, restDeliveryPointName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for updateMsgVpnRestDeliveryPointQueueBinding */
    private com.squareup.okhttp.Call updateMsgVpnRestDeliveryPointQueueBindingCall(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling updateMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling updateMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'queueBindingName' is set
        if (queueBindingName == null) {
            throw new ApiException("Missing the required parameter 'queueBindingName' when calling updateMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMsgVpnRestDeliveryPointQueueBinding(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "queueBindingName" + "\\}", apiClient.escapeString(queueBindingName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Updates a Queue Binding object.
     * Updates a Queue Binding object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointQueueBindingResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointQueueBindingResponse updateMsgVpnRestDeliveryPointQueueBinding(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointQueueBindingResponse> resp = updateMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(msgVpnName, restDeliveryPointName, queueBindingName, body, select);
        return resp.getData();
    }

    /**
     * Updates a Queue Binding object.
     * Updates a Queue Binding object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointQueueBindingResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointQueueBindingResponse> updateMsgVpnRestDeliveryPointQueueBindingWithHttpInfo(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = updateMsgVpnRestDeliveryPointQueueBindingCall(msgVpnName, restDeliveryPointName, queueBindingName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointQueueBindingResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates a Queue Binding object. (asynchronously)
     * Updates a Queue Binding object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param queueBindingName The queueBindingName of the Queue Binding. (required)
     * @param body The Queue Binding object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMsgVpnRestDeliveryPointQueueBindingAsync(String msgVpnName, String restDeliveryPointName, String queueBindingName, MsgVpnRestDeliveryPointQueueBinding body, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointQueueBindingResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateMsgVpnRestDeliveryPointQueueBindingCall(msgVpnName, restDeliveryPointName, queueBindingName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointQueueBindingResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /* Build call for updateMsgVpnRestDeliveryPointRestConsumer */
    private com.squareup.okhttp.Call updateMsgVpnRestDeliveryPointRestConsumerCall(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = body;
        
        // verify the required parameter 'msgVpnName' is set
        if (msgVpnName == null) {
            throw new ApiException("Missing the required parameter 'msgVpnName' when calling updateMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'restDeliveryPointName' is set
        if (restDeliveryPointName == null) {
            throw new ApiException("Missing the required parameter 'restDeliveryPointName' when calling updateMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'restConsumerName' is set
        if (restConsumerName == null) {
            throw new ApiException("Missing the required parameter 'restConsumerName' when calling updateMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling updateMsgVpnRestDeliveryPointRestConsumer(Async)");
        }
        

        // create path and map variables
        String localVarPath = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}".replaceAll("\\{format\\}","json")
        .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
        .replaceAll("\\{" + "restDeliveryPointName" + "\\}", apiClient.escapeString(restDeliveryPointName.toString()))
        .replaceAll("\\{" + "restConsumerName" + "\\}", apiClient.escapeString(restConsumerName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        if (select != null)
        localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "select", select));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "basicAuth" };
        return apiClient.buildCall(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    /**
     * Updates a REST Consumer object.
     * Updates a REST Consumer object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return MsgVpnRestDeliveryPointRestConsumerResponse
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public MsgVpnRestDeliveryPointRestConsumerResponse updateMsgVpnRestDeliveryPointRestConsumer(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select) throws ApiException {
        ApiResponse<MsgVpnRestDeliveryPointRestConsumerResponse> resp = updateMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(msgVpnName, restDeliveryPointName, restConsumerName, body, select);
        return resp.getData();
    }

    /**
     * Updates a REST Consumer object.
     * Updates a REST Consumer object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @return ApiResponse&lt;MsgVpnRestDeliveryPointRestConsumerResponse&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<MsgVpnRestDeliveryPointRestConsumerResponse> updateMsgVpnRestDeliveryPointRestConsumerWithHttpInfo(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select) throws ApiException {
        com.squareup.okhttp.Call call = updateMsgVpnRestDeliveryPointRestConsumerCall(msgVpnName, restDeliveryPointName, restConsumerName, body, select, null, null);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerResponse>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Updates a REST Consumer object. (asynchronously)
     * Updates a REST Consumer object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
     * @param msgVpnName The msgVpnName of the Message VPN. (required)
     * @param restDeliveryPointName The restDeliveryPointName of the REST Delivery Point. (required)
     * @param restConsumerName The restConsumerName of the REST Consumer. (required)
     * @param body The REST Consumer object&#39;s attributes. (required)
     * @param select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateMsgVpnRestDeliveryPointRestConsumerAsync(String msgVpnName, String restDeliveryPointName, String restConsumerName, MsgVpnRestDeliveryPointRestConsumer body, List<String> select, final ApiCallback<MsgVpnRestDeliveryPointRestConsumerResponse> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateMsgVpnRestDeliveryPointRestConsumerCall(msgVpnName, restDeliveryPointName, restConsumerName, body, select, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<MsgVpnRestDeliveryPointRestConsumerResponse>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
