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

import io.swagger.client.model.MsgVpnAclProfile;
import io.swagger.client.model.MsgVpnAclProfileResponse;
import io.swagger.client.model.SempMetaOnlyResponse;
import io.swagger.client.model.MsgVpnAclProfileClientConnectExceptionResponse;
import io.swagger.client.model.MsgVpnAclProfileClientConnectException;
import io.swagger.client.model.MsgVpnAclProfilePublishExceptionResponse;
import io.swagger.client.model.MsgVpnAclProfilePublishException;
import io.swagger.client.model.MsgVpnAclProfileSubscribeException;
import io.swagger.client.model.MsgVpnAclProfileSubscribeExceptionResponse;
import io.swagger.client.model.MsgVpnAclProfileClientConnectExceptionsResponse;
import io.swagger.client.model.MsgVpnAclProfilePublishExceptionsResponse;
import io.swagger.client.model.MsgVpnAclProfileSubscribeExceptionsResponse;
import io.swagger.client.model.MsgVpnAclProfilesResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AclProfileApi {
    private ApiClient apiClient;

    public AclProfileApi() {
        this(Configuration.getDefaultApiClient());
    }

    public AclProfileApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
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
}
