package com.solace.labs.sempclient.samplelib.api;

import com.solace.labs.sempclient.samplelib.ApiException;
import com.solace.labs.sempclient.samplelib.ApiClient;
import com.solace.labs.sempclient.samplelib.Configuration;
import com.solace.labs.sempclient.samplelib.Pair;

import javax.ws.rs.core.GenericType;

import com.solace.labs.sempclient.samplelib.model.MsgVpnMqttSession;
import com.solace.labs.sempclient.samplelib.model.MsgVpnMqttSessionResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnMqttSessionSubscription;
import com.solace.labs.sempclient.samplelib.model.MsgVpnMqttSessionSubscriptionResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnMqttSessionSubscriptionsResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnMqttSessionsResponse;
import com.solace.labs.sempclient.samplelib.model.SempMetaOnlyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-11T16:20:54.920-04:00")
public class MqttSessionApi {
  private ApiClient apiClient;

  public MqttSessionApi() {
    this(Configuration.getDefaultApiClient());
  }

  public MqttSessionApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Create an MQTT Session object.
   * Create an MQTT Session object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param body The MQTT Session object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnMqttSessionResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnMqttSessionResponse createMsgVpnMqttSession(String msgVpnName, MsgVpnMqttSession body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling createMsgVpnMqttSession");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createMsgVpnMqttSession");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions".replaceAll("\\{format\\}","json")
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

    GenericType<MsgVpnMqttSessionResponse> localVarReturnType = new GenericType<MsgVpnMqttSessionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Create a Subscription object.
   * Create a Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x||x|| mqttSessionVirtualRouter|x||x|| msgVpnName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
   * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
   * @param body The Subscription object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnMqttSessionSubscriptionResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnMqttSessionSubscriptionResponse createMsgVpnMqttSessionSubscription(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, MsgVpnMqttSessionSubscription body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling createMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'mqttSessionClientId' is set
    if (mqttSessionClientId == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionClientId' when calling createMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'mqttSessionVirtualRouter' is set
    if (mqttSessionVirtualRouter == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionVirtualRouter' when calling createMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createMsgVpnMqttSessionSubscription");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "mqttSessionClientId" + "\\}", apiClient.escapeString(mqttSessionClientId.toString()))
      .replaceAll("\\{" + "mqttSessionVirtualRouter" + "\\}", apiClient.escapeString(mqttSessionVirtualRouter.toString()));

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

    GenericType<MsgVpnMqttSessionSubscriptionResponse> localVarReturnType = new GenericType<MsgVpnMqttSessionSubscriptionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Delete an MQTT Session object.
   * Delete an MQTT Session object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
   * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteMsgVpnMqttSession(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnMqttSession");
    }
    
    // verify the required parameter 'mqttSessionClientId' is set
    if (mqttSessionClientId == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionClientId' when calling deleteMsgVpnMqttSession");
    }
    
    // verify the required parameter 'mqttSessionVirtualRouter' is set
    if (mqttSessionVirtualRouter == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionVirtualRouter' when calling deleteMsgVpnMqttSession");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "mqttSessionClientId" + "\\}", apiClient.escapeString(mqttSessionClientId.toString()))
      .replaceAll("\\{" + "mqttSessionVirtualRouter" + "\\}", apiClient.escapeString(mqttSessionVirtualRouter.toString()));

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
   * Delete a Subscription object.
   * Delete a Subscription object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
   * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
   * @param subscriptionTopic The MQTT subscription topic. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteMsgVpnMqttSessionSubscription(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'mqttSessionClientId' is set
    if (mqttSessionClientId == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionClientId' when calling deleteMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'mqttSessionVirtualRouter' is set
    if (mqttSessionVirtualRouter == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionVirtualRouter' when calling deleteMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'subscriptionTopic' is set
    if (subscriptionTopic == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriptionTopic' when calling deleteMsgVpnMqttSessionSubscription");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "mqttSessionClientId" + "\\}", apiClient.escapeString(mqttSessionClientId.toString()))
      .replaceAll("\\{" + "mqttSessionVirtualRouter" + "\\}", apiClient.escapeString(mqttSessionVirtualRouter.toString()))
      .replaceAll("\\{" + "subscriptionTopic" + "\\}", apiClient.escapeString(subscriptionTopic.toString()));

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
   * Get an MQTT Session object.
   * Get an MQTT Session object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
   * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnMqttSessionResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnMqttSessionResponse getMsgVpnMqttSession(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnMqttSession");
    }
    
    // verify the required parameter 'mqttSessionClientId' is set
    if (mqttSessionClientId == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionClientId' when calling getMsgVpnMqttSession");
    }
    
    // verify the required parameter 'mqttSessionVirtualRouter' is set
    if (mqttSessionVirtualRouter == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionVirtualRouter' when calling getMsgVpnMqttSession");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "mqttSessionClientId" + "\\}", apiClient.escapeString(mqttSessionClientId.toString()))
      .replaceAll("\\{" + "mqttSessionVirtualRouter" + "\\}", apiClient.escapeString(mqttSessionVirtualRouter.toString()));

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

    GenericType<MsgVpnMqttSessionResponse> localVarReturnType = new GenericType<MsgVpnMqttSessionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a Subscription object.
   * Get a Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
   * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
   * @param subscriptionTopic The MQTT subscription topic. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnMqttSessionSubscriptionResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnMqttSessionSubscriptionResponse getMsgVpnMqttSessionSubscription(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'mqttSessionClientId' is set
    if (mqttSessionClientId == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionClientId' when calling getMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'mqttSessionVirtualRouter' is set
    if (mqttSessionVirtualRouter == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionVirtualRouter' when calling getMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'subscriptionTopic' is set
    if (subscriptionTopic == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriptionTopic' when calling getMsgVpnMqttSessionSubscription");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "mqttSessionClientId" + "\\}", apiClient.escapeString(mqttSessionClientId.toString()))
      .replaceAll("\\{" + "mqttSessionVirtualRouter" + "\\}", apiClient.escapeString(mqttSessionVirtualRouter.toString()))
      .replaceAll("\\{" + "subscriptionTopic" + "\\}", apiClient.escapeString(subscriptionTopic.toString()));

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

    GenericType<MsgVpnMqttSessionSubscriptionResponse> localVarReturnType = new GenericType<MsgVpnMqttSessionSubscriptionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Subscription objects.
   * Get a list of Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
   * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnMqttSessionSubscriptionsResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnMqttSessionSubscriptionsResponse getMsgVpnMqttSessionSubscriptions(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnMqttSessionSubscriptions");
    }
    
    // verify the required parameter 'mqttSessionClientId' is set
    if (mqttSessionClientId == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionClientId' when calling getMsgVpnMqttSessionSubscriptions");
    }
    
    // verify the required parameter 'mqttSessionVirtualRouter' is set
    if (mqttSessionVirtualRouter == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionVirtualRouter' when calling getMsgVpnMqttSessionSubscriptions");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "mqttSessionClientId" + "\\}", apiClient.escapeString(mqttSessionClientId.toString()))
      .replaceAll("\\{" + "mqttSessionVirtualRouter" + "\\}", apiClient.escapeString(mqttSessionVirtualRouter.toString()));

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

    GenericType<MsgVpnMqttSessionSubscriptionsResponse> localVarReturnType = new GenericType<MsgVpnMqttSessionSubscriptionsResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of MQTT Session objects.
   * Get a list of MQTT Session objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnMqttSessionsResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnMqttSessionsResponse getMsgVpnMqttSessions(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnMqttSessions");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()));

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

    GenericType<MsgVpnMqttSessionsResponse> localVarReturnType = new GenericType<MsgVpnMqttSessionsResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Replace an MQTT Session object.
   * Replace an MQTT Session object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| owner||||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
   * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
   * @param body The MQTT Session object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnMqttSessionResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnMqttSessionResponse replaceMsgVpnMqttSession(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, MsgVpnMqttSession body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnMqttSession");
    }
    
    // verify the required parameter 'mqttSessionClientId' is set
    if (mqttSessionClientId == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionClientId' when calling replaceMsgVpnMqttSession");
    }
    
    // verify the required parameter 'mqttSessionVirtualRouter' is set
    if (mqttSessionVirtualRouter == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionVirtualRouter' when calling replaceMsgVpnMqttSession");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling replaceMsgVpnMqttSession");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "mqttSessionClientId" + "\\}", apiClient.escapeString(mqttSessionClientId.toString()))
      .replaceAll("\\{" + "mqttSessionVirtualRouter" + "\\}", apiClient.escapeString(mqttSessionVirtualRouter.toString()));

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

    GenericType<MsgVpnMqttSessionResponse> localVarReturnType = new GenericType<MsgVpnMqttSessionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Replace a Subscription object.
   * Replace a Subscription object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
   * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
   * @param subscriptionTopic The MQTT subscription topic. (required)
   * @param body The Subscription object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnMqttSessionSubscriptionResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnMqttSessionSubscriptionResponse replaceMsgVpnMqttSessionSubscription(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic, MsgVpnMqttSessionSubscription body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'mqttSessionClientId' is set
    if (mqttSessionClientId == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionClientId' when calling replaceMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'mqttSessionVirtualRouter' is set
    if (mqttSessionVirtualRouter == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionVirtualRouter' when calling replaceMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'subscriptionTopic' is set
    if (subscriptionTopic == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriptionTopic' when calling replaceMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling replaceMsgVpnMqttSessionSubscription");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "mqttSessionClientId" + "\\}", apiClient.escapeString(mqttSessionClientId.toString()))
      .replaceAll("\\{" + "mqttSessionVirtualRouter" + "\\}", apiClient.escapeString(mqttSessionVirtualRouter.toString()))
      .replaceAll("\\{" + "subscriptionTopic" + "\\}", apiClient.escapeString(subscriptionTopic.toString()));

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

    GenericType<MsgVpnMqttSessionSubscriptionResponse> localVarReturnType = new GenericType<MsgVpnMqttSessionSubscriptionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Update an MQTT Session object.
   * Update an MQTT Session object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| owner||||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
   * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
   * @param body The MQTT Session object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnMqttSessionResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnMqttSessionResponse updateMsgVpnMqttSession(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, MsgVpnMqttSession body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnMqttSession");
    }
    
    // verify the required parameter 'mqttSessionClientId' is set
    if (mqttSessionClientId == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionClientId' when calling updateMsgVpnMqttSession");
    }
    
    // verify the required parameter 'mqttSessionVirtualRouter' is set
    if (mqttSessionVirtualRouter == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionVirtualRouter' when calling updateMsgVpnMqttSession");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling updateMsgVpnMqttSession");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "mqttSessionClientId" + "\\}", apiClient.escapeString(mqttSessionClientId.toString()))
      .replaceAll("\\{" + "mqttSessionVirtualRouter" + "\\}", apiClient.escapeString(mqttSessionVirtualRouter.toString()));

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

    GenericType<MsgVpnMqttSessionResponse> localVarReturnType = new GenericType<MsgVpnMqttSessionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Update a Subscription object.
   * Update a Subscription object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param mqttSessionClientId The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. (required)
   * @param mqttSessionVirtualRouter The virtual router of the MQTT Session. (required)
   * @param subscriptionTopic The MQTT subscription topic. (required)
   * @param body The Subscription object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnMqttSessionSubscriptionResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnMqttSessionSubscriptionResponse updateMsgVpnMqttSessionSubscription(String msgVpnName, String mqttSessionClientId, String mqttSessionVirtualRouter, String subscriptionTopic, MsgVpnMqttSessionSubscription body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'mqttSessionClientId' is set
    if (mqttSessionClientId == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionClientId' when calling updateMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'mqttSessionVirtualRouter' is set
    if (mqttSessionVirtualRouter == null) {
      throw new ApiException(400, "Missing the required parameter 'mqttSessionVirtualRouter' when calling updateMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'subscriptionTopic' is set
    if (subscriptionTopic == null) {
      throw new ApiException(400, "Missing the required parameter 'subscriptionTopic' when calling updateMsgVpnMqttSessionSubscription");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling updateMsgVpnMqttSessionSubscription");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "mqttSessionClientId" + "\\}", apiClient.escapeString(mqttSessionClientId.toString()))
      .replaceAll("\\{" + "mqttSessionVirtualRouter" + "\\}", apiClient.escapeString(mqttSessionVirtualRouter.toString()))
      .replaceAll("\\{" + "subscriptionTopic" + "\\}", apiClient.escapeString(subscriptionTopic.toString()));

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

    GenericType<MsgVpnMqttSessionSubscriptionResponse> localVarReturnType = new GenericType<MsgVpnMqttSessionSubscriptionResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
}
