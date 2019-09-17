package com.solace.labs.sempclient.samplelib.api;

import com.solace.labs.sempclient.samplelib.ApiException;
import com.solace.labs.sempclient.samplelib.ApiClient;
import com.solace.labs.sempclient.samplelib.Configuration;
import com.solace.labs.sempclient.samplelib.Pair;

import javax.ws.rs.core.GenericType;

import com.solace.labs.sempclient.samplelib.model.DmrCluster;
import com.solace.labs.sempclient.samplelib.model.DmrClusterLink;
import com.solace.labs.sempclient.samplelib.model.DmrClusterLinkRemoteAddress;
import com.solace.labs.sempclient.samplelib.model.DmrClusterLinkRemoteAddressResponse;
import com.solace.labs.sempclient.samplelib.model.DmrClusterLinkRemoteAddressesResponse;
import com.solace.labs.sempclient.samplelib.model.DmrClusterLinkResponse;
import com.solace.labs.sempclient.samplelib.model.DmrClusterLinkTlsTrustedCommonName;
import com.solace.labs.sempclient.samplelib.model.DmrClusterLinkTlsTrustedCommonNameResponse;
import com.solace.labs.sempclient.samplelib.model.DmrClusterLinkTlsTrustedCommonNamesResponse;
import com.solace.labs.sempclient.samplelib.model.DmrClusterLinksResponse;
import com.solace.labs.sempclient.samplelib.model.DmrClusterResponse;
import com.solace.labs.sempclient.samplelib.model.DmrClustersResponse;
import com.solace.labs.sempclient.samplelib.model.SempMetaOnlyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-11T16:20:54.920-04:00")
public class DmrClusterApi {
  private ApiClient apiClient;

  public DmrClusterApi() {
    this(Configuration.getDefaultApiClient());
  }

  public DmrClusterApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Create a Cluster object.
   * Create a Cluster object. Any attribute missing from the request will be set to its default value.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword||||x| authenticationClientCertContent||||x| authenticationClientCertPassword||||x| dmrClusterName|x|x||| nodeName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param body The Cluster object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterResponse createDmrCluster(DmrCluster body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createDmrCluster");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters".replaceAll("\\{format\\}","json");

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

    GenericType<DmrClusterResponse> localVarReturnType = new GenericType<DmrClusterResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Create a Link object.
   * Create a Link object. Any attribute missing from the request will be set to its default value.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword||||x| dmrClusterName|x||x|| remoteNodeName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param body The Link object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterLinkResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterLinkResponse createDmrClusterLink(String dmrClusterName, DmrClusterLink body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling createDmrClusterLink");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createDmrClusterLink");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()));

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

    GenericType<DmrClusterLinkResponse> localVarReturnType = new GenericType<DmrClusterLinkResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Create a Remote Address object.
   * Create a Remote Address object. Any attribute missing from the request will be set to its default value.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x|| remoteAddress|x|x||| remoteNodeName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @param body The Remote Address object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterLinkRemoteAddressResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterLinkRemoteAddressResponse createDmrClusterLinkRemoteAddress(String dmrClusterName, String remoteNodeName, DmrClusterLinkRemoteAddress body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling createDmrClusterLinkRemoteAddress");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling createDmrClusterLinkRemoteAddress");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createDmrClusterLinkRemoteAddress");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()));

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

    GenericType<DmrClusterLinkRemoteAddressResponse> localVarReturnType = new GenericType<DmrClusterLinkRemoteAddressResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Create a Trusted Common Name object.
   * Create a Trusted Common Name object. Any attribute missing from the request will be set to its default value.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x|| remoteNodeName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @param body The Trusted Common Name object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterLinkTlsTrustedCommonNameResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterLinkTlsTrustedCommonNameResponse createDmrClusterLinkTlsTrustedCommonName(String dmrClusterName, String remoteNodeName, DmrClusterLinkTlsTrustedCommonName body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling createDmrClusterLinkTlsTrustedCommonName");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling createDmrClusterLinkTlsTrustedCommonName");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling createDmrClusterLinkTlsTrustedCommonName");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()));

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

    GenericType<DmrClusterLinkTlsTrustedCommonNameResponse> localVarReturnType = new GenericType<DmrClusterLinkTlsTrustedCommonNameResponse>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Delete a Cluster object.
   * Delete a Cluster object.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteDmrCluster(String dmrClusterName) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling deleteDmrCluster");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()));

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
   * Delete a Link object.
   * Delete a Link object.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteDmrClusterLink(String dmrClusterName, String remoteNodeName) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling deleteDmrClusterLink");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling deleteDmrClusterLink");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()));

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
   * Delete a Remote Address object.
   * Delete a Remote Address object.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @param remoteAddress The FQDN or IP address (and optional port) of the remote node. If a port is not provided, it will vary based on the transport encoding: 55555 (plain-text), 55443 (encrypted), or 55003 (compressed). (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteDmrClusterLinkRemoteAddress(String dmrClusterName, String remoteNodeName, String remoteAddress) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling deleteDmrClusterLinkRemoteAddress");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling deleteDmrClusterLinkRemoteAddress");
    }
    
    // verify the required parameter 'remoteAddress' is set
    if (remoteAddress == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteAddress' when calling deleteDmrClusterLinkRemoteAddress");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses/{remoteAddress}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()))
      .replaceAll("\\{" + "remoteAddress" + "\\}", apiClient.escapeString(remoteAddress.toString()));

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
   * Delete a Trusted Common Name object.
   * Delete a Trusted Common Name object.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @param tlsTrustedCommonName The expected trusted common name of the remote certificate. (required)
   * @return SempMetaOnlyResponse
   * @throws ApiException if fails to make API call
   */
  public SempMetaOnlyResponse deleteDmrClusterLinkTlsTrustedCommonName(String dmrClusterName, String remoteNodeName, String tlsTrustedCommonName) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling deleteDmrClusterLinkTlsTrustedCommonName");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling deleteDmrClusterLinkTlsTrustedCommonName");
    }
    
    // verify the required parameter 'tlsTrustedCommonName' is set
    if (tlsTrustedCommonName == null) {
      throw new ApiException(400, "Missing the required parameter 'tlsTrustedCommonName' when calling deleteDmrClusterLinkTlsTrustedCommonName");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames/{tlsTrustedCommonName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()))
      .replaceAll("\\{" + "tlsTrustedCommonName" + "\\}", apiClient.escapeString(tlsTrustedCommonName.toString()));

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
   * Get a Cluster object.
   * Get a Cluster object.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationBasicPassword||x| authenticationClientCertContent||x| authenticationClientCertPassword||x| dmrClusterName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterResponse getDmrCluster(String dmrClusterName, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling getDmrCluster");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()));

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

    GenericType<DmrClusterResponse> localVarReturnType = new GenericType<DmrClusterResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a Link object.
   * Get a Link object.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationBasicPassword||x| dmrClusterName|x|| remoteNodeName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterLinkResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterLinkResponse getDmrClusterLink(String dmrClusterName, String remoteNodeName, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling getDmrClusterLink");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling getDmrClusterLink");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()));

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

    GenericType<DmrClusterLinkResponse> localVarReturnType = new GenericType<DmrClusterLinkResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a Remote Address object.
   * Get a Remote Address object.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: dmrClusterName|x|| remoteAddress|x|| remoteNodeName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @param remoteAddress The FQDN or IP address (and optional port) of the remote node. If a port is not provided, it will vary based on the transport encoding: 55555 (plain-text), 55443 (encrypted), or 55003 (compressed). (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterLinkRemoteAddressResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterLinkRemoteAddressResponse getDmrClusterLinkRemoteAddress(String dmrClusterName, String remoteNodeName, String remoteAddress, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling getDmrClusterLinkRemoteAddress");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling getDmrClusterLinkRemoteAddress");
    }
    
    // verify the required parameter 'remoteAddress' is set
    if (remoteAddress == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteAddress' when calling getDmrClusterLinkRemoteAddress");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses/{remoteAddress}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()))
      .replaceAll("\\{" + "remoteAddress" + "\\}", apiClient.escapeString(remoteAddress.toString()));

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

    GenericType<DmrClusterLinkRemoteAddressResponse> localVarReturnType = new GenericType<DmrClusterLinkRemoteAddressResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Remote Address objects.
   * Get a list of Remote Address objects.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: dmrClusterName|x|| remoteAddress|x|| remoteNodeName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterLinkRemoteAddressesResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterLinkRemoteAddressesResponse getDmrClusterLinkRemoteAddresses(String dmrClusterName, String remoteNodeName, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling getDmrClusterLinkRemoteAddresses");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling getDmrClusterLinkRemoteAddresses");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

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

    GenericType<DmrClusterLinkRemoteAddressesResponse> localVarReturnType = new GenericType<DmrClusterLinkRemoteAddressesResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a Trusted Common Name object.
   * Get a Trusted Common Name object.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: dmrClusterName|x|| remoteNodeName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @param tlsTrustedCommonName The expected trusted common name of the remote certificate. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterLinkTlsTrustedCommonNameResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterLinkTlsTrustedCommonNameResponse getDmrClusterLinkTlsTrustedCommonName(String dmrClusterName, String remoteNodeName, String tlsTrustedCommonName, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling getDmrClusterLinkTlsTrustedCommonName");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling getDmrClusterLinkTlsTrustedCommonName");
    }
    
    // verify the required parameter 'tlsTrustedCommonName' is set
    if (tlsTrustedCommonName == null) {
      throw new ApiException(400, "Missing the required parameter 'tlsTrustedCommonName' when calling getDmrClusterLinkTlsTrustedCommonName");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames/{tlsTrustedCommonName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()))
      .replaceAll("\\{" + "tlsTrustedCommonName" + "\\}", apiClient.escapeString(tlsTrustedCommonName.toString()));

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

    GenericType<DmrClusterLinkTlsTrustedCommonNameResponse> localVarReturnType = new GenericType<DmrClusterLinkTlsTrustedCommonNameResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Trusted Common Name objects.
   * Get a list of Trusted Common Name objects.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: dmrClusterName|x|| remoteNodeName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterLinkTlsTrustedCommonNamesResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterLinkTlsTrustedCommonNamesResponse getDmrClusterLinkTlsTrustedCommonNames(String dmrClusterName, String remoteNodeName, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling getDmrClusterLinkTlsTrustedCommonNames");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling getDmrClusterLinkTlsTrustedCommonNames");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

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

    GenericType<DmrClusterLinkTlsTrustedCommonNamesResponse> localVarReturnType = new GenericType<DmrClusterLinkTlsTrustedCommonNamesResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Link objects.
   * Get a list of Link objects.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationBasicPassword||x| dmrClusterName|x|| remoteNodeName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterLinksResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterLinksResponse getDmrClusterLinks(String dmrClusterName, Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling getDmrClusterLinks");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()));

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

    GenericType<DmrClusterLinksResponse> localVarReturnType = new GenericType<DmrClusterLinksResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Get a list of Cluster objects.
   * Get a list of Cluster objects.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationBasicPassword||x| authenticationClientCertContent||x| authenticationClientCertPassword||x| dmrClusterName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param count Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. (optional, default to 10)
   * @param cursor The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. (optional)
   * @param where Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. (optional)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClustersResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClustersResponse getDmrClusters(Integer count, String cursor, List<String> where, List<String> select) throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/dmrClusters".replaceAll("\\{format\\}","json");

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

    GenericType<DmrClustersResponse> localVarReturnType = new GenericType<DmrClustersResponse>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Replace a Cluster object.
   * Replace a Cluster object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword|||x|x| authenticationClientCertContent|||x|x| authenticationClientCertPassword|||x|x| directOnlyEnabled||x||| dmrClusterName|x|x||| nodeName||x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param body The Cluster object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterResponse replaceDmrCluster(String dmrClusterName, DmrCluster body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling replaceDmrCluster");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling replaceDmrCluster");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()));

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

    GenericType<DmrClusterResponse> localVarReturnType = new GenericType<DmrClusterResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Replace a Link object.
   * Replace a Link object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword|||x|x| authenticationScheme||||x| dmrClusterName|x|x||| egressFlowWindowSize||||x| initiator||||x| remoteNodeName|x|x||| span||||x| transportCompressedEnabled||||x| transportTlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @param body The Link object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterLinkResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterLinkResponse replaceDmrClusterLink(String dmrClusterName, String remoteNodeName, DmrClusterLink body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling replaceDmrClusterLink");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling replaceDmrClusterLink");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling replaceDmrClusterLink");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()));

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

    GenericType<DmrClusterLinkResponse> localVarReturnType = new GenericType<DmrClusterLinkResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Update a Cluster object.
   * Update a Cluster object. Any attribute missing from the request will be left unchanged.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword|||x|x| authenticationClientCertContent|||x|x| authenticationClientCertPassword|||x|x| directOnlyEnabled||x||| dmrClusterName|x|x||| nodeName||x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param body The Cluster object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterResponse updateDmrCluster(String dmrClusterName, DmrCluster body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling updateDmrCluster");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling updateDmrCluster");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()));

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

    GenericType<DmrClusterResponse> localVarReturnType = new GenericType<DmrClusterResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Update a Link object.
   * Update a Link object. Any attribute missing from the request will be left unchanged.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword|||x|x| authenticationScheme||||x| dmrClusterName|x|x||| egressFlowWindowSize||||x| initiator||||x| remoteNodeName|x|x||| span||||x| transportCompressedEnabled||||x| transportTlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.
   * @param dmrClusterName The name of the Cluster. (required)
   * @param remoteNodeName The name of the node at the remote end of the Link. (required)
   * @param body The Link object&#39;s attributes. (required)
   * @param select Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. (optional)
   * @return DmrClusterLinkResponse
   * @throws ApiException if fails to make API call
   */
  public DmrClusterLinkResponse updateDmrClusterLink(String dmrClusterName, String remoteNodeName, DmrClusterLink body, List<String> select) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'dmrClusterName' is set
    if (dmrClusterName == null) {
      throw new ApiException(400, "Missing the required parameter 'dmrClusterName' when calling updateDmrClusterLink");
    }
    
    // verify the required parameter 'remoteNodeName' is set
    if (remoteNodeName == null) {
      throw new ApiException(400, "Missing the required parameter 'remoteNodeName' when calling updateDmrClusterLink");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling updateDmrClusterLink");
    }
    
    // create path and map variables
    String localVarPath = "/dmrClusters/{dmrClusterName}/links/{remoteNodeName}".replaceAll("\\{format\\}","json")
      .replaceAll("\\{" + "dmrClusterName" + "\\}", apiClient.escapeString(dmrClusterName.toString()))
      .replaceAll("\\{" + "remoteNodeName" + "\\}", apiClient.escapeString(remoteNodeName.toString()));

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

    GenericType<DmrClusterLinkResponse> localVarReturnType = new GenericType<DmrClusterLinkResponse>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
}
