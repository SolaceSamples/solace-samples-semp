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

import io.swagger.client.model.MsgVpnBridge;
import io.swagger.client.model.MsgVpnBridgeResponse;
import io.swagger.client.model.SempMetaOnlyResponse;
import io.swagger.client.model.MsgVpnBridgeRemoteMsgVpn;
import io.swagger.client.model.MsgVpnBridgeRemoteMsgVpnResponse;
import io.swagger.client.model.MsgVpnBridgeRemoteSubscription;
import io.swagger.client.model.MsgVpnBridgeRemoteSubscriptionResponse;
import io.swagger.client.model.MsgVpnBridgeTlsTrustedCommonNameResponse;
import io.swagger.client.model.MsgVpnBridgeTlsTrustedCommonName;
import io.swagger.client.model.MsgVpnBridgeRemoteMsgVpnsResponse;
import io.swagger.client.model.MsgVpnBridgeRemoteSubscriptionsResponse;
import io.swagger.client.model.MsgVpnBridgeTlsTrustedCommonNamesResponse;
import io.swagger.client.model.MsgVpnBridgesResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BridgeApi {
    private ApiClient apiClient;

    public BridgeApi() {
        this(Configuration.getDefaultApiClient());
    }

    public BridgeApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
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
}
