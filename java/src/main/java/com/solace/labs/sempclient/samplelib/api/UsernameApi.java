package com.solace.labs.sempclient.samplelib.api;

import com.solace.labs.sempclient.samplelib.ApiException;
import com.solace.labs.sempclient.samplelib.ApiClient;
import com.solace.labs.sempclient.samplelib.Configuration;
import com.solace.labs.sempclient.samplelib.Pair;

import javax.ws.rs.core.GenericType;

import com.solace.labs.sempclient.samplelib.model.SempMetaOnlyResponse;
import com.solace.labs.sempclient.samplelib.model.Username;
import com.solace.labs.sempclient.samplelib.model.UsernameMsgVpnAccessLevelException;
import com.solace.labs.sempclient.samplelib.model.UsernameMsgVpnAccessLevelExceptionResponse;
import com.solace.labs.sempclient.samplelib.model.UsernameMsgVpnAccessLevelExceptionsResponse;
import com.solace.labs.sempclient.samplelib.model.UsernameResponse;
import com.solace.labs.sempclient.samplelib.model.UsernamesResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-11T16:20:54.920-04:00")
public class UsernameApi {
  private ApiClient apiClient;

  public UsernameApi() {
    this(Configuration.getDefaultApiClient());
  }

  public UsernameApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Create a Username object.
   * Create a Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: password||||x| userName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/none\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin msgVpnDefaultAccessLevel|global/read-write    This has been available since 2.12.
   * @param body The Username object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return UsernameResponse
   * @throws ApiException if fails to make API call
   */
  public UsernameResponse createUsername(Username body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createUsername");
    }
    
    // create path and map variables
    String localVarPath = "/usernames".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "select", select));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<UsernameResponse> localVarReturnType = new GenericType<UsernameResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Create a Message VPN Access Level Exception object.
   * Create a Message VPN Access Level Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| userName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.12.
   * @param userName Username. (required)
   * @param body The Message VPN Access Level Exception object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return UsernameMsgVpnAccessLevelExceptionResponse
   * @throws ApiException if fails to make API call
   */
  public UsernameMsgVpnAccessLevelExceptionResponse createUsernameMsgVpnAccessLevelException(String userName, UsernameMsgVpnAccessLevelException body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'userName' is set
    if (userName == null) {
      throw new ApiException(400, "Missing the required parameter 'userName' when calling createUsernameMsgVpnAccessLevelException");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createUsernameMsgVpnAccessLevelException");
    }
    
    // create path and map variables
    String localVarPath = "/usernames/{userName}/msgVpnAccessLevelExceptions".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "userName" + "\\}", apiClient.escapeString(userName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "select", select));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<UsernameMsgVpnAccessLevelExceptionResponse> localVarReturnType = new GenericType<UsernameMsgVpnAccessLevelExceptionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Delete a Username object.
   * Delete a Username object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/none\&quot; is required to perform this operation.  This has been available since 2.12.
   * @param userName Username. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteUsername(String userName) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'userName' is set
    if (userName == null) {
      throw new ApiException(400, "Missing the required parameter 'userName' when calling deleteUsername");
    }
    
    // create path and map variables
    String localVarPath = "/usernames/{userName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "userName" + "\\}", apiClient.escapeString(userName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<SempMetaOnlyResponse> localVarReturnType = new GenericType<SempMetaOnlyResponse>() {};
    return apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Delete a Message VPN Access Level Exception object.
   * Delete a Message VPN Access Level Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.12.
   * @param userName Username. (required)
   * @param msgVpnName The name of the Message VPN for which an access level exception may be configured. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteUsernameMsgVpnAccessLevelException(String userName, String msgVpnName) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'userName' is set
    if (userName == null) {
      throw new ApiException(400, "Missing the required parameter 'userName' when calling deleteUsernameMsgVpnAccessLevelException");
    }
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling deleteUsernameMsgVpnAccessLevelException");
    }
    
    // create path and map variables
    String localVarPath = "/usernames/{userName}/msgVpnAccessLevelExceptions/{msgVpnName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "userName" + "\\}", apiClient.escapeString(userName.toString()))
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();


    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<SempMetaOnlyResponse> localVarReturnType = new GenericType<SempMetaOnlyResponse>() {};
    return apiClient.invokeAPI(localVarPath, "DELETE", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a Username object.
   * Get a Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: password||x| userName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.12.
   * @param userName Username. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return UsernameResponse
   * @throws ApiException if fails to make API call
   */
  public UsernameResponse getUsername(String userName, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'userName' is set
    if (userName == null) {
      throw new ApiException(400, "Missing the required parameter 'userName' when calling getUsername");
    }
    
    // create path and map variables
    String localVarPath = "/usernames/{userName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "userName" + "\\}", apiClient.escapeString(userName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "select", select));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<UsernameResponse> localVarReturnType = new GenericType<UsernameResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a Message VPN Access Level Exception object.
   * Get a Message VPN Access Level Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| userName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.12.
   * @param userName Username. (required)
   * @param msgVpnName The name of the Message VPN for which an access level exception may be configured. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return UsernameMsgVpnAccessLevelExceptionResponse
   * @throws ApiException if fails to make API call
   */
  public UsernameMsgVpnAccessLevelExceptionResponse getUsernameMsgVpnAccessLevelException(String userName, String msgVpnName, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'userName' is set
    if (userName == null) {
      throw new ApiException(400, "Missing the required parameter 'userName' when calling getUsernameMsgVpnAccessLevelException");
    }
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getUsernameMsgVpnAccessLevelException");
    }
    
    // create path and map variables
    String localVarPath = "/usernames/{userName}/msgVpnAccessLevelExceptions/{msgVpnName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "userName" + "\\}", apiClient.escapeString(userName.toString()))
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "select", select));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<UsernameMsgVpnAccessLevelExceptionResponse> localVarReturnType = new GenericType<UsernameMsgVpnAccessLevelExceptionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Message VPN Access Level Exception objects.
   * Get a list of Message VPN Access Level Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| userName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.12.
   * @param userName Username. (required)
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return UsernameMsgVpnAccessLevelExceptionsResponse
   * @throws ApiException if fails to make API call
   */
  public UsernameMsgVpnAccessLevelExceptionsResponse getUsernameMsgVpnAccessLevelExceptions(String userName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'userName' is set
    if (userName == null) {
      throw new ApiException(400, "Missing the required parameter 'userName' when calling getUsernameMsgVpnAccessLevelExceptions");
    }
    
    // create path and map variables
    String localVarPath = "/usernames/{userName}/msgVpnAccessLevelExceptions".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "userName" + "\\}", apiClient.escapeString(userName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "where", where));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "select", select));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<UsernameMsgVpnAccessLevelExceptionsResponse> localVarReturnType = new GenericType<UsernameMsgVpnAccessLevelExceptionsResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Username objects.
   * Get a list of Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: password||x| userName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.12.
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return UsernamesResponse
   * @throws ApiException if fails to make API call
   */
  public UsernamesResponse getUsernames(Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/usernames".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "count", count));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "cursor", cursor));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "where", where));
    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "select", select));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<UsernamesResponse> localVarReturnType = new GenericType<UsernamesResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Replace a Username object.
   * Replace a Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: password|||x|| userName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/none\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin msgVpnDefaultAccessLevel|global/read-write    This has been available since 2.12.
   * @param userName Username. (required)
   * @param body The Username object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return UsernameResponse
   * @throws ApiException if fails to make API call
   */
  public UsernameResponse replaceUsername(String userName, Username body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'userName' is set
    if (userName == null) {
      throw new ApiException(400, "Missing the required parameter 'userName' when calling replaceUsername");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling replaceUsername");
    }
    
    // create path and map variables
    String localVarPath = "/usernames/{userName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "userName" + "\\}", apiClient.escapeString(userName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "select", select));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<UsernameResponse> localVarReturnType = new GenericType<UsernameResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Replace a Message VPN Access Level Exception object.
   * Replace a Message VPN Access Level Exception object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| userName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.12.
   * @param userName Username. (required)
   * @param msgVpnName The name of the Message VPN for which an access level exception may be configured. (required)
   * @param body The Message VPN Access Level Exception object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return UsernameMsgVpnAccessLevelExceptionResponse
   * @throws ApiException if fails to make API call
   */
  public UsernameMsgVpnAccessLevelExceptionResponse replaceUsernameMsgVpnAccessLevelException(String userName, String msgVpnName, UsernameMsgVpnAccessLevelException body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'userName' is set
    if (userName == null) {
      throw new ApiException(400, "Missing the required parameter 'userName' when calling replaceUsernameMsgVpnAccessLevelException");
    }
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling replaceUsernameMsgVpnAccessLevelException");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling replaceUsernameMsgVpnAccessLevelException");
    }
    
    // create path and map variables
    String localVarPath = "/usernames/{userName}/msgVpnAccessLevelExceptions/{msgVpnName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "userName" + "\\}", apiClient.escapeString(userName.toString()))
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "select", select));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<UsernameMsgVpnAccessLevelExceptionResponse> localVarReturnType = new GenericType<UsernameMsgVpnAccessLevelExceptionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Update a Username object.
   * Update a Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: password|||x|| userName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/none\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin msgVpnDefaultAccessLevel|global/read-write    This has been available since 2.12.
   * @param userName Username. (required)
   * @param body The Username object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return UsernameResponse
   * @throws ApiException if fails to make API call
   */
  public UsernameResponse updateUsername(String userName, Username body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'userName' is set
    if (userName == null) {
      throw new ApiException(400, "Missing the required parameter 'userName' when calling updateUsername");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling updateUsername");
    }
    
    // create path and map variables
    String localVarPath = "/usernames/{userName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "userName" + "\\}", apiClient.escapeString(userName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "select", select));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<UsernameResponse> localVarReturnType = new GenericType<UsernameResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Update a Message VPN Access Level Exception object.
   * Update a Message VPN Access Level Exception object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| userName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.12.
   * @param userName Username. (required)
   * @param msgVpnName The name of the Message VPN for which an access level exception may be configured. (required)
   * @param body The Message VPN Access Level Exception object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return UsernameMsgVpnAccessLevelExceptionResponse
   * @throws ApiException if fails to make API call
   */
  public UsernameMsgVpnAccessLevelExceptionResponse updateUsernameMsgVpnAccessLevelException(String userName, String msgVpnName, UsernameMsgVpnAccessLevelException body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'userName' is set
    if (userName == null) {
      throw new ApiException(400, "Missing the required parameter 'userName' when calling updateUsernameMsgVpnAccessLevelException");
    }
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling updateUsernameMsgVpnAccessLevelException");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling updateUsernameMsgVpnAccessLevelException");
    }
    
    // create path and map variables
    String localVarPath = "/usernames/{userName}/msgVpnAccessLevelExceptions/{msgVpnName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "userName" + "\\}", apiClient.escapeString(userName.toString()))
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("csv", "select", select));

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "basicAuth" };

    GenericType<UsernameMsgVpnAccessLevelExceptionResponse> localVarReturnType = new GenericType<UsernameMsgVpnAccessLevelExceptionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
}
