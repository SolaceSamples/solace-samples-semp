package com.solace.labs.sempclient.samplelib.api;

import com.solace.labs.sempclient.samplelib.ApiException;
import com.solace.labs.sempclient.samplelib.ApiClient;
import com.solace.labs.sempclient.samplelib.Configuration;
import com.solace.labs.sempclient.samplelib.Pair;

import javax.ws.rs.core.GenericType;

import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCache;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheCluster;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterGlobalCachingHomeCluster;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterGlobalCachingHomeClusterResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixesResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterGlobalCachingHomeClustersResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterInstance;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterInstanceResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterInstancesResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterTopic;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterTopicResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClusterTopicsResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheClustersResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCacheResponse;
import com.solace.labs.sempclient.samplelib.model.MsgVpnDistributedCachesResponse;
import com.solace.labs.sempclient.samplelib.model.SempMetaOnlyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-11T16:20:54.920-04:00")
public class DistributedCacheApi {
  private ApiClient apiClient;

  public DistributedCacheApi() {
    this(Configuration.getDefaultApiClient());
  }

  public DistributedCacheApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Create a Distributed Cache object.
   * Create a Distributed Cache object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnDistributedCache|scheduledDeleteMsgDayList|scheduledDeleteMsgTimeList| MsgVpnDistributedCache|scheduledDeleteMsgTimeList|scheduledDeleteMsgDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param body The Distributed Cache object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheResponse createMsgVpnDistributedCache(String msgVpnName, MsgVpnDistributedCache body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling createMsgVpnDistributedCache");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createMsgVpnDistributedCache");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches".replaceAll("\\{format\\}","json")
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

    GenericType<MsgVpnDistributedCacheResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Create a Cache Cluster object.
   * Create a Cache Cluster object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x||x|| clusterName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent| EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param body The Cache Cluster object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterResponse createMsgVpnDistributedCacheCluster(String msgVpnName, String cacheName, MsgVpnDistributedCacheCluster body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling createMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling createMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createMsgVpnDistributedCacheCluster");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Create a Home Cache Cluster object.
   * Create a Home Cache Cluster object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x||x|| clusterName|x||x|| homeClusterName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param body The Home Cache Cluster object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterGlobalCachingHomeClusterResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterGlobalCachingHomeClusterResponse createMsgVpnDistributedCacheClusterGlobalCachingHomeCluster(String msgVpnName, String cacheName, String clusterName, MsgVpnDistributedCacheClusterGlobalCachingHomeCluster body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling createMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling createMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling createMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClusterResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClusterResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Create a Topic Prefix object.
   * Create a Topic Prefix object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x||x|| clusterName|x||x|| homeClusterName|x||x|| msgVpnName|x||x|| topicPrefix|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param homeClusterName The name of the remote Home Cache Cluster. (required)
   * @param body The Topic Prefix object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixResponse createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix(String msgVpnName, String cacheName, String clusterName, String homeClusterName, MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'homeClusterName' is set
    if (homeClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'homeClusterName' when calling createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "homeClusterName" + "\\}", apiClient.escapeString(homeClusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Create a Cache Instance object.
   * Create a Cache Instance object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x||x|| clusterName|x||x|| instanceName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param body The Cache Instance object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterInstanceResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterInstanceResponse createMsgVpnDistributedCacheClusterInstance(String msgVpnName, String cacheName, String clusterName, MsgVpnDistributedCacheClusterInstance body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling createMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling createMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling createMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createMsgVpnDistributedCacheClusterInstance");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterInstanceResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterInstanceResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Create a Topic object.
   * Create a Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x||x|| clusterName|x||x|| msgVpnName|x||x|| topic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param body The Topic object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterTopicResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterTopicResponse createMsgVpnDistributedCacheClusterTopic(String msgVpnName, String cacheName, String clusterName, MsgVpnDistributedCacheClusterTopic body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling createMsgVpnDistributedCacheClusterTopic");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling createMsgVpnDistributedCacheClusterTopic");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling createMsgVpnDistributedCacheClusterTopic");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createMsgVpnDistributedCacheClusterTopic");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterTopicResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterTopicResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Delete a Distributed Cache object.
   * Delete a Distributed Cache object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteMsgVpnDistributedCache(String msgVpnName, String cacheName) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnDistributedCache");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling deleteMsgVpnDistributedCache");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()));

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
   * Delete a Cache Cluster object.
   * Delete a Cache Cluster object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteMsgVpnDistributedCacheCluster(String msgVpnName, String cacheName, String clusterName) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling deleteMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling deleteMsgVpnDistributedCacheCluster");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()));

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
   * Delete a Home Cache Cluster object.
   * Delete a Home Cache Cluster object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param homeClusterName The name of the remote Home Cache Cluster. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteMsgVpnDistributedCacheClusterGlobalCachingHomeCluster(String msgVpnName, String cacheName, String clusterName, String homeClusterName) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling deleteMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling deleteMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // verify the required parameter 'homeClusterName' is set
    if (homeClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'homeClusterName' when calling deleteMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "homeClusterName" + "\\}", apiClient.escapeString(homeClusterName.toString()));

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
   * Delete a Topic Prefix object.
   * Delete a Topic Prefix object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param homeClusterName The name of the remote Home Cache Cluster. (required)
   * @param topicPrefix A topic prefix for global topics available from the remote Home Cache Cluster. A wildcard (/&gt;) is implied at the end of the prefix. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix(String msgVpnName, String cacheName, String clusterName, String homeClusterName, String topicPrefix) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'homeClusterName' is set
    if (homeClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'homeClusterName' when calling deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'topicPrefix' is set
    if (topicPrefix == null) {
      throw new ApiException(400, "Missing the required parameter 'topicPrefix' when calling deleteMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes/{topicPrefix}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "homeClusterName" + "\\}", apiClient.escapeString(homeClusterName.toString()))
      .replaceAll("\\{" + "topicPrefix" + "\\}", apiClient.escapeString(topicPrefix.toString()));

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
   * Delete a Cache Instance object.
   * Delete a Cache Instance object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param instanceName The name of the Cache Instance. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteMsgVpnDistributedCacheClusterInstance(String msgVpnName, String cacheName, String clusterName, String instanceName) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling deleteMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling deleteMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'instanceName' is set
    if (instanceName == null) {
      throw new ApiException(400, "Missing the required parameter 'instanceName' when calling deleteMsgVpnDistributedCacheClusterInstance");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "instanceName" + "\\}", apiClient.escapeString(instanceName.toString()));

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
   * Delete a Topic object.
   * Delete a Topic object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param topic The value of the Topic in the form a/b/c. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteMsgVpnDistributedCacheClusterTopic(String msgVpnName, String cacheName, String clusterName, String topic) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling deleteMsgVpnDistributedCacheClusterTopic");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling deleteMsgVpnDistributedCacheClusterTopic");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling deleteMsgVpnDistributedCacheClusterTopic");
    }
    
    // verify the required parameter 'topic' is set
    if (topic == null) {
      throw new ApiException(400, "Missing the required parameter 'topic' when calling deleteMsgVpnDistributedCacheClusterTopic");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics/{topic}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "topic" + "\\}", apiClient.escapeString(topic.toString()));

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
   * Get a Distributed Cache object.
   * Get a Distributed Cache object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheResponse getMsgVpnDistributedCache(String msgVpnName, String cacheName, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCache");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling getMsgVpnDistributedCache");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()));

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

    GenericType<MsgVpnDistributedCacheResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a Cache Cluster object.
   * Get a Cache Cluster object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| clusterName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterResponse getMsgVpnDistributedCacheCluster(String msgVpnName, String cacheName, String clusterName, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling getMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling getMsgVpnDistributedCacheCluster");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a Home Cache Cluster object.
   * Get a Home Cache Cluster object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| clusterName|x|| homeClusterName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param homeClusterName The name of the remote Home Cache Cluster. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterGlobalCachingHomeClusterResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterGlobalCachingHomeClusterResponse getMsgVpnDistributedCacheClusterGlobalCachingHomeCluster(String msgVpnName, String cacheName, String clusterName, String homeClusterName, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // verify the required parameter 'homeClusterName' is set
    if (homeClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'homeClusterName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeCluster");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "homeClusterName" + "\\}", apiClient.escapeString(homeClusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClusterResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClusterResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a Topic Prefix object.
   * Get a Topic Prefix object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| clusterName|x|| homeClusterName|x|| msgVpnName|x|| topicPrefix|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param homeClusterName The name of the remote Home Cache Cluster. (required)
   * @param topicPrefix A topic prefix for global topics available from the remote Home Cache Cluster. A wildcard (/&gt;) is implied at the end of the prefix. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixResponse getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix(String msgVpnName, String cacheName, String clusterName, String homeClusterName, String topicPrefix, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'homeClusterName' is set
    if (homeClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'homeClusterName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // verify the required parameter 'topicPrefix' is set
    if (topicPrefix == null) {
      throw new ApiException(400, "Missing the required parameter 'topicPrefix' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefix");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes/{topicPrefix}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "homeClusterName" + "\\}", apiClient.escapeString(homeClusterName.toString()))
      .replaceAll("\\{" + "topicPrefix" + "\\}", apiClient.escapeString(topicPrefix.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Topic Prefix objects.
   * Get a list of Topic Prefix objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| clusterName|x|| homeClusterName|x|| msgVpnName|x|| topicPrefix|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param homeClusterName The name of the remote Home Cache Cluster. (required)
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixesResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixesResponse getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixes(String msgVpnName, String cacheName, String clusterName, String homeClusterName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixes");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixes");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixes");
    }
    
    // verify the required parameter 'homeClusterName' is set
    if (homeClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'homeClusterName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixes");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters/{homeClusterName}/topicPrefixes".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "homeClusterName" + "\\}", apiClient.escapeString(homeClusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixesResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClusterTopicPrefixesResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Home Cache Cluster objects.
   * Get a list of Home Cache Cluster objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| clusterName|x|| homeClusterName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterGlobalCachingHomeClustersResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterGlobalCachingHomeClustersResponse getMsgVpnDistributedCacheClusterGlobalCachingHomeClusters(String msgVpnName, String cacheName, String clusterName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusters");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusters");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling getMsgVpnDistributedCacheClusterGlobalCachingHomeClusters");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/globalCachingHomeClusters".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClustersResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterGlobalCachingHomeClustersResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a Cache Instance object.
   * Get a Cache Instance object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| clusterName|x|| instanceName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param instanceName The name of the Cache Instance. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterInstanceResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterInstanceResponse getMsgVpnDistributedCacheClusterInstance(String msgVpnName, String cacheName, String clusterName, String instanceName, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling getMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling getMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'instanceName' is set
    if (instanceName == null) {
      throw new ApiException(400, "Missing the required parameter 'instanceName' when calling getMsgVpnDistributedCacheClusterInstance");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "instanceName" + "\\}", apiClient.escapeString(instanceName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterInstanceResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterInstanceResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Cache Instance objects.
   * Get a list of Cache Instance objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| clusterName|x|| instanceName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterInstancesResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterInstancesResponse getMsgVpnDistributedCacheClusterInstances(String msgVpnName, String cacheName, String clusterName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCacheClusterInstances");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling getMsgVpnDistributedCacheClusterInstances");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling getMsgVpnDistributedCacheClusterInstances");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterInstancesResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterInstancesResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a Topic object.
   * Get a Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| clusterName|x|| msgVpnName|x|| topic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param topic The value of the Topic in the form a/b/c. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterTopicResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterTopicResponse getMsgVpnDistributedCacheClusterTopic(String msgVpnName, String cacheName, String clusterName, String topic, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCacheClusterTopic");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling getMsgVpnDistributedCacheClusterTopic");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling getMsgVpnDistributedCacheClusterTopic");
    }
    
    // verify the required parameter 'topic' is set
    if (topic == null) {
      throw new ApiException(400, "Missing the required parameter 'topic' when calling getMsgVpnDistributedCacheClusterTopic");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics/{topic}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "topic" + "\\}", apiClient.escapeString(topic.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterTopicResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterTopicResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Topic objects.
   * Get a list of Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| clusterName|x|| msgVpnName|x|| topic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterTopicsResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterTopicsResponse getMsgVpnDistributedCacheClusterTopics(String msgVpnName, String cacheName, String clusterName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCacheClusterTopics");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling getMsgVpnDistributedCacheClusterTopics");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling getMsgVpnDistributedCacheClusterTopics");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/topics".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterTopicsResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterTopicsResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Cache Cluster objects.
   * Get a list of Cache Cluster objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| clusterName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClustersResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClustersResponse getMsgVpnDistributedCacheClusters(String msgVpnName, String cacheName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCacheClusters");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling getMsgVpnDistributedCacheClusters");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()));

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

    GenericType<MsgVpnDistributedCacheClustersResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClustersResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Distributed Cache objects.
   * Get a list of Distributed Cache objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCachesResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCachesResponse getMsgVpnDistributedCaches(String msgVpnName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling getMsgVpnDistributedCaches");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches".replaceAll("\\{format\\}","json")
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

    GenericType<MsgVpnDistributedCachesResponse> localVarReturnType = new GenericType<MsgVpnDistributedCachesResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Replace a Distributed Cache object.
   * Replace a Distributed Cache object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnDistributedCache|scheduledDeleteMsgDayList|scheduledDeleteMsgTimeList| MsgVpnDistributedCache|scheduledDeleteMsgTimeList|scheduledDeleteMsgDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param body The Distributed Cache object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheResponse replaceMsgVpnDistributedCache(String msgVpnName, String cacheName, MsgVpnDistributedCache body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnDistributedCache");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling replaceMsgVpnDistributedCache");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling replaceMsgVpnDistributedCache");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()));

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

    GenericType<MsgVpnDistributedCacheResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Replace a Cache Cluster object.
   * Replace a Cache Cluster object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x|x||| clusterName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent| EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param body The Cache Cluster object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterResponse replaceMsgVpnDistributedCacheCluster(String msgVpnName, String cacheName, String clusterName, MsgVpnDistributedCacheCluster body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling replaceMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling replaceMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling replaceMsgVpnDistributedCacheCluster");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Replace a Cache Instance object.
   * Replace a Cache Instance object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x|x||| clusterName|x|x||| instanceName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param instanceName The name of the Cache Instance. (required)
   * @param body The Cache Instance object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterInstanceResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterInstanceResponse replaceMsgVpnDistributedCacheClusterInstance(String msgVpnName, String cacheName, String clusterName, String instanceName, MsgVpnDistributedCacheClusterInstance body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling replaceMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling replaceMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling replaceMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'instanceName' is set
    if (instanceName == null) {
      throw new ApiException(400, "Missing the required parameter 'instanceName' when calling replaceMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling replaceMsgVpnDistributedCacheClusterInstance");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "instanceName" + "\\}", apiClient.escapeString(instanceName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterInstanceResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterInstanceResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Update a Distributed Cache object.
   * Update a Distributed Cache object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnDistributedCache|scheduledDeleteMsgDayList|scheduledDeleteMsgTimeList| MsgVpnDistributedCache|scheduledDeleteMsgTimeList|scheduledDeleteMsgDayList|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param body The Distributed Cache object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheResponse updateMsgVpnDistributedCache(String msgVpnName, String cacheName, MsgVpnDistributedCache body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnDistributedCache");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling updateMsgVpnDistributedCache");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling updateMsgVpnDistributedCache");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()));

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

    GenericType<MsgVpnDistributedCacheResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Update a Cache Cluster object.
   * Update a Cache Cluster object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x|x||| clusterName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent| EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param body The Cache Cluster object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterResponse updateMsgVpnDistributedCacheCluster(String msgVpnName, String cacheName, String clusterName, MsgVpnDistributedCacheCluster body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling updateMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling updateMsgVpnDistributedCacheCluster");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling updateMsgVpnDistributedCacheCluster");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Update a Cache Instance object.
   * Update a Cache Instance object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x|x||| clusterName|x|x||| instanceName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param msgVpnName The name of the Message VPN. (required)
   * @param cacheName The name of the Distributed Cache. (required)
   * @param clusterName The name of the Cache Cluster. (required)
   * @param instanceName The name of the Cache Instance. (required)
   * @param body The Cache Instance object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return MsgVpnDistributedCacheClusterInstanceResponse
   * @throws ApiException if fails to make API call
   */
  public MsgVpnDistributedCacheClusterInstanceResponse updateMsgVpnDistributedCacheClusterInstance(String msgVpnName, String cacheName, String clusterName, String instanceName, MsgVpnDistributedCacheClusterInstance body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'msgVpnName' is set
    if (msgVpnName == null) {
      throw new ApiException(400, "Missing the required parameter 'msgVpnName' when calling updateMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'cacheName' is set
    if (cacheName == null) {
      throw new ApiException(400, "Missing the required parameter 'cacheName' when calling updateMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'clusterName' is set
    if (clusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'clusterName' when calling updateMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'instanceName' is set
    if (instanceName == null) {
      throw new ApiException(400, "Missing the required parameter 'instanceName' when calling updateMsgVpnDistributedCacheClusterInstance");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling updateMsgVpnDistributedCacheClusterInstance");
    }
    
    // create path and map variables
    String localVarPath = "/msgVpns/{msgVpnName}/distributedCaches/{cacheName}/clusters/{clusterName}/instances/{instanceName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "msgVpnName" + "\\}", apiClient.escapeString(msgVpnName.toString()))
      .replaceAll("\\{" + "cacheName" + "\\}", apiClient.escapeString(cacheName.toString()))
      .replaceAll("\\{" + "clusterName" + "\\}", apiClient.escapeString(clusterName.toString()))
      .replaceAll("\\{" + "instanceName" + "\\}", apiClient.escapeString(instanceName.toString()));

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

    GenericType<MsgVpnDistributedCacheClusterInstanceResponse> localVarReturnType = new GenericType<MsgVpnDistributedCacheClusterInstanceResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
}
