# DmrClusterApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createDmrCluster**](DmrClusterApi.md#createDmrCluster) | **POST** /dmrClusters | Create a Cluster object.
[**createDmrClusterLink**](DmrClusterApi.md#createDmrClusterLink) | **POST** /dmrClusters/{dmrClusterName}/links | Create a Link object.
[**createDmrClusterLinkRemoteAddress**](DmrClusterApi.md#createDmrClusterLinkRemoteAddress) | **POST** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses | Create a Remote Address object.
[**createDmrClusterLinkTlsTrustedCommonName**](DmrClusterApi.md#createDmrClusterLinkTlsTrustedCommonName) | **POST** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames | Create a Trusted Common Name object.
[**deleteDmrCluster**](DmrClusterApi.md#deleteDmrCluster) | **DELETE** /dmrClusters/{dmrClusterName} | Delete a Cluster object.
[**deleteDmrClusterLink**](DmrClusterApi.md#deleteDmrClusterLink) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Delete a Link object.
[**deleteDmrClusterLinkRemoteAddress**](DmrClusterApi.md#deleteDmrClusterLinkRemoteAddress) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses/{remoteAddress} | Delete a Remote Address object.
[**deleteDmrClusterLinkTlsTrustedCommonName**](DmrClusterApi.md#deleteDmrClusterLinkTlsTrustedCommonName) | **DELETE** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Delete a Trusted Common Name object.
[**getDmrCluster**](DmrClusterApi.md#getDmrCluster) | **GET** /dmrClusters/{dmrClusterName} | Get a Cluster object.
[**getDmrClusterLink**](DmrClusterApi.md#getDmrClusterLink) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Get a Link object.
[**getDmrClusterLinkRemoteAddress**](DmrClusterApi.md#getDmrClusterLinkRemoteAddress) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses/{remoteAddress} | Get a Remote Address object.
[**getDmrClusterLinkRemoteAddresses**](DmrClusterApi.md#getDmrClusterLinkRemoteAddresses) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/remoteAddresses | Get a list of Remote Address objects.
[**getDmrClusterLinkTlsTrustedCommonName**](DmrClusterApi.md#getDmrClusterLinkTlsTrustedCommonName) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Get a Trusted Common Name object.
[**getDmrClusterLinkTlsTrustedCommonNames**](DmrClusterApi.md#getDmrClusterLinkTlsTrustedCommonNames) | **GET** /dmrClusters/{dmrClusterName}/links/{remoteNodeName}/tlsTrustedCommonNames | Get a list of Trusted Common Name objects.
[**getDmrClusterLinks**](DmrClusterApi.md#getDmrClusterLinks) | **GET** /dmrClusters/{dmrClusterName}/links | Get a list of Link objects.
[**getDmrClusters**](DmrClusterApi.md#getDmrClusters) | **GET** /dmrClusters | Get a list of Cluster objects.
[**replaceDmrCluster**](DmrClusterApi.md#replaceDmrCluster) | **PUT** /dmrClusters/{dmrClusterName} | Replace a Cluster object.
[**replaceDmrClusterLink**](DmrClusterApi.md#replaceDmrClusterLink) | **PUT** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Replace a Link object.
[**updateDmrCluster**](DmrClusterApi.md#updateDmrCluster) | **PATCH** /dmrClusters/{dmrClusterName} | Update a Cluster object.
[**updateDmrClusterLink**](DmrClusterApi.md#updateDmrClusterLink) | **PATCH** /dmrClusters/{dmrClusterName}/links/{remoteNodeName} | Update a Link object.


<a name="createDmrCluster"></a>
# **createDmrCluster**
> DmrClusterResponse createDmrCluster(body, select)

Create a Cluster object.

Create a Cluster object. Any attribute missing from the request will be set to its default value.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword||||x| authenticationClientCertContent||||x| authenticationClientCertPassword||||x| dmrClusterName|x|x||| nodeName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
DmrCluster body = new DmrCluster(); // DmrCluster | The Cluster object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterResponse result = apiInstance.createDmrCluster(body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#createDmrCluster");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**DmrCluster**](DmrCluster.md)| The Cluster object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterResponse**](DmrClusterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createDmrClusterLink"></a>
# **createDmrClusterLink**
> DmrClusterLinkResponse createDmrClusterLink(dmrClusterName, body, select)

Create a Link object.

Create a Link object. Any attribute missing from the request will be set to its default value.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword||||x| dmrClusterName|x||x|| remoteNodeName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
DmrClusterLink body = new DmrClusterLink(); // DmrClusterLink | The Link object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterLinkResponse result = apiInstance.createDmrClusterLink(dmrClusterName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#createDmrClusterLink");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **body** | [**DmrClusterLink**](DmrClusterLink.md)| The Link object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterLinkResponse**](DmrClusterLinkResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createDmrClusterLinkRemoteAddress"></a>
# **createDmrClusterLinkRemoteAddress**
> DmrClusterLinkRemoteAddressResponse createDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, body, select)

Create a Remote Address object.

Create a Remote Address object. Any attribute missing from the request will be set to its default value.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x|| remoteAddress|x|x||| remoteNodeName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
DmrClusterLinkRemoteAddress body = new DmrClusterLinkRemoteAddress(); // DmrClusterLinkRemoteAddress | The Remote Address object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterLinkRemoteAddressResponse result = apiInstance.createDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#createDmrClusterLinkRemoteAddress");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |
 **body** | [**DmrClusterLinkRemoteAddress**](DmrClusterLinkRemoteAddress.md)| The Remote Address object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterLinkRemoteAddressResponse**](DmrClusterLinkRemoteAddressResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createDmrClusterLinkTlsTrustedCommonName"></a>
# **createDmrClusterLinkTlsTrustedCommonName**
> DmrClusterLinkTlsTrustedCommonNameResponse createDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, body, select)

Create a Trusted Common Name object.

Create a Trusted Common Name object. Any attribute missing from the request will be set to its default value.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: dmrClusterName|x||x|| remoteNodeName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
DmrClusterLinkTlsTrustedCommonName body = new DmrClusterLinkTlsTrustedCommonName(); // DmrClusterLinkTlsTrustedCommonName | The Trusted Common Name object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterLinkTlsTrustedCommonNameResponse result = apiInstance.createDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#createDmrClusterLinkTlsTrustedCommonName");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |
 **body** | [**DmrClusterLinkTlsTrustedCommonName**](DmrClusterLinkTlsTrustedCommonName.md)| The Trusted Common Name object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterLinkTlsTrustedCommonNameResponse**](DmrClusterLinkTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteDmrCluster"></a>
# **deleteDmrCluster**
> SempMetaOnlyResponse deleteDmrCluster(dmrClusterName)

Delete a Cluster object.

Delete a Cluster object.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
try {
    SempMetaOnlyResponse result = apiInstance.deleteDmrCluster(dmrClusterName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#deleteDmrCluster");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteDmrClusterLink"></a>
# **deleteDmrClusterLink**
> SempMetaOnlyResponse deleteDmrClusterLink(dmrClusterName, remoteNodeName)

Delete a Link object.

Delete a Link object.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
try {
    SempMetaOnlyResponse result = apiInstance.deleteDmrClusterLink(dmrClusterName, remoteNodeName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#deleteDmrClusterLink");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteDmrClusterLinkRemoteAddress"></a>
# **deleteDmrClusterLinkRemoteAddress**
> SempMetaOnlyResponse deleteDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, remoteAddress)

Delete a Remote Address object.

Delete a Remote Address object.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
String remoteAddress = "remoteAddress_example"; // String | The FQDN or IP address (and optional port) of the remote node. If a port is not provided, it will vary based on the transport encoding: 55555 (plain-text), 55443 (encrypted), or 55003 (compressed).
try {
    SempMetaOnlyResponse result = apiInstance.deleteDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, remoteAddress);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#deleteDmrClusterLinkRemoteAddress");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |
 **remoteAddress** | **String**| The FQDN or IP address (and optional port) of the remote node. If a port is not provided, it will vary based on the transport encoding: 55555 (plain-text), 55443 (encrypted), or 55003 (compressed). |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteDmrClusterLinkTlsTrustedCommonName"></a>
# **deleteDmrClusterLinkTlsTrustedCommonName**
> SempMetaOnlyResponse deleteDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, tlsTrustedCommonName)

Delete a Trusted Common Name object.

Delete a Trusted Common Name object.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
String tlsTrustedCommonName = "tlsTrustedCommonName_example"; // String | The expected trusted common name of the remote certificate.
try {
    SempMetaOnlyResponse result = apiInstance.deleteDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, tlsTrustedCommonName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#deleteDmrClusterLinkTlsTrustedCommonName");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |
 **tlsTrustedCommonName** | **String**| The expected trusted common name of the remote certificate. |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getDmrCluster"></a>
# **getDmrCluster**
> DmrClusterResponse getDmrCluster(dmrClusterName, select)

Get a Cluster object.

Get a Cluster object.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationBasicPassword||x| authenticationClientCertContent||x| authenticationClientCertPassword||x| dmrClusterName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterResponse result = apiInstance.getDmrCluster(dmrClusterName, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#getDmrCluster");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterResponse**](DmrClusterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getDmrClusterLink"></a>
# **getDmrClusterLink**
> DmrClusterLinkResponse getDmrClusterLink(dmrClusterName, remoteNodeName, select)

Get a Link object.

Get a Link object.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationBasicPassword||x| dmrClusterName|x|| remoteNodeName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterLinkResponse result = apiInstance.getDmrClusterLink(dmrClusterName, remoteNodeName, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#getDmrClusterLink");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterLinkResponse**](DmrClusterLinkResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getDmrClusterLinkRemoteAddress"></a>
# **getDmrClusterLinkRemoteAddress**
> DmrClusterLinkRemoteAddressResponse getDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, remoteAddress, select)

Get a Remote Address object.

Get a Remote Address object.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: dmrClusterName|x|| remoteAddress|x|| remoteNodeName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
String remoteAddress = "remoteAddress_example"; // String | The FQDN or IP address (and optional port) of the remote node. If a port is not provided, it will vary based on the transport encoding: 55555 (plain-text), 55443 (encrypted), or 55003 (compressed).
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterLinkRemoteAddressResponse result = apiInstance.getDmrClusterLinkRemoteAddress(dmrClusterName, remoteNodeName, remoteAddress, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinkRemoteAddress");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |
 **remoteAddress** | **String**| The FQDN or IP address (and optional port) of the remote node. If a port is not provided, it will vary based on the transport encoding: 55555 (plain-text), 55443 (encrypted), or 55003 (compressed). |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterLinkRemoteAddressResponse**](DmrClusterLinkRemoteAddressResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getDmrClusterLinkRemoteAddresses"></a>
# **getDmrClusterLinkRemoteAddresses**
> DmrClusterLinkRemoteAddressesResponse getDmrClusterLinkRemoteAddresses(dmrClusterName, remoteNodeName, where, select)

Get a list of Remote Address objects.

Get a list of Remote Address objects.  Each Remote Address, consisting of a FQDN or IP address and optional port, is used to connect to the remote node for this Link. Up to 4 addresses may be provided for each Link, and will be tried on a round-robin basis.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: dmrClusterName|x|| remoteAddress|x|| remoteNodeName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
List<String> where = Arrays.asList("where_example"); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterLinkRemoteAddressesResponse result = apiInstance.getDmrClusterLinkRemoteAddresses(dmrClusterName, remoteNodeName, where, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinkRemoteAddresses");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |
 **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional]
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterLinkRemoteAddressesResponse**](DmrClusterLinkRemoteAddressesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getDmrClusterLinkTlsTrustedCommonName"></a>
# **getDmrClusterLinkTlsTrustedCommonName**
> DmrClusterLinkTlsTrustedCommonNameResponse getDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, tlsTrustedCommonName, select)

Get a Trusted Common Name object.

Get a Trusted Common Name object.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: dmrClusterName|x|| remoteNodeName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
String tlsTrustedCommonName = "tlsTrustedCommonName_example"; // String | The expected trusted common name of the remote certificate.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterLinkTlsTrustedCommonNameResponse result = apiInstance.getDmrClusterLinkTlsTrustedCommonName(dmrClusterName, remoteNodeName, tlsTrustedCommonName, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinkTlsTrustedCommonName");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |
 **tlsTrustedCommonName** | **String**| The expected trusted common name of the remote certificate. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterLinkTlsTrustedCommonNameResponse**](DmrClusterLinkTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getDmrClusterLinkTlsTrustedCommonNames"></a>
# **getDmrClusterLinkTlsTrustedCommonNames**
> DmrClusterLinkTlsTrustedCommonNamesResponse getDmrClusterLinkTlsTrustedCommonNames(dmrClusterName, remoteNodeName, where, select)

Get a list of Trusted Common Name objects.

Get a list of Trusted Common Name objects.  The Trusted Common Names for the Link are used by encrypted transports to verify the name in the certificate presented by the remote node. They must include the common name of the remote node&#39;s server certificate or client certificate, depending upon the initiator of the connection.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: dmrClusterName|x|| remoteNodeName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
List<String> where = Arrays.asList("where_example"); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterLinkTlsTrustedCommonNamesResponse result = apiInstance.getDmrClusterLinkTlsTrustedCommonNames(dmrClusterName, remoteNodeName, where, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinkTlsTrustedCommonNames");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |
 **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional]
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterLinkTlsTrustedCommonNamesResponse**](DmrClusterLinkTlsTrustedCommonNamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getDmrClusterLinks"></a>
# **getDmrClusterLinks**
> DmrClusterLinksResponse getDmrClusterLinks(dmrClusterName, count, cursor, where, select)

Get a list of Link objects.

Get a list of Link objects.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationBasicPassword||x| dmrClusterName|x|| remoteNodeName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
List<String> where = Arrays.asList("where_example"); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterLinksResponse result = apiInstance.getDmrClusterLinks(dmrClusterName, count, cursor, where, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#getDmrClusterLinks");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10]
 **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional]
 **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional]
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterLinksResponse**](DmrClusterLinksResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getDmrClusters"></a>
# **getDmrClusters**
> DmrClustersResponse getDmrClusters(count, cursor, where, select)

Get a list of Cluster objects.

Get a list of Cluster objects.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationBasicPassword||x| authenticationClientCertContent||x| authenticationClientCertPassword||x| dmrClusterName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
List<String> where = Arrays.asList("where_example"); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClustersResponse result = apiInstance.getDmrClusters(count, cursor, where, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#getDmrClusters");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10]
 **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional]
 **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional]
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClustersResponse**](DmrClustersResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="replaceDmrCluster"></a>
# **replaceDmrCluster**
> DmrClusterResponse replaceDmrCluster(dmrClusterName, body, select)

Replace a Cluster object.

Replace a Cluster object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword|||x|x| authenticationClientCertContent|||x|x| authenticationClientCertPassword|||x|x| directOnlyEnabled||x||| dmrClusterName|x|x||| nodeName||x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
DmrCluster body = new DmrCluster(); // DmrCluster | The Cluster object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterResponse result = apiInstance.replaceDmrCluster(dmrClusterName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#replaceDmrCluster");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **body** | [**DmrCluster**](DmrCluster.md)| The Cluster object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterResponse**](DmrClusterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="replaceDmrClusterLink"></a>
# **replaceDmrClusterLink**
> DmrClusterLinkResponse replaceDmrClusterLink(dmrClusterName, remoteNodeName, body, select)

Replace a Link object.

Replace a Link object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword|||x|x| authenticationScheme||||x| dmrClusterName|x|x||| egressFlowWindowSize||||x| initiator||||x| remoteNodeName|x|x||| span||||x| transportCompressedEnabled||||x| transportTlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
DmrClusterLink body = new DmrClusterLink(); // DmrClusterLink | The Link object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterLinkResponse result = apiInstance.replaceDmrClusterLink(dmrClusterName, remoteNodeName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#replaceDmrClusterLink");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |
 **body** | [**DmrClusterLink**](DmrClusterLink.md)| The Link object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterLinkResponse**](DmrClusterLinkResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateDmrCluster"></a>
# **updateDmrCluster**
> DmrClusterResponse updateDmrCluster(dmrClusterName, body, select)

Update a Cluster object.

Update a Cluster object. Any attribute missing from the request will be left unchanged.  A Cluster is a provisioned object on a message broker that contains global DMR configuration parameters.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword|||x|x| authenticationClientCertContent|||x|x| authenticationClientCertPassword|||x|x| directOnlyEnabled||x||| dmrClusterName|x|x||| nodeName||x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- DmrCluster|authenticationClientCertPassword|authenticationClientCertContent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
DmrCluster body = new DmrCluster(); // DmrCluster | The Cluster object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterResponse result = apiInstance.updateDmrCluster(dmrClusterName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#updateDmrCluster");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **body** | [**DmrCluster**](DmrCluster.md)| The Cluster object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterResponse**](DmrClusterResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateDmrClusterLink"></a>
# **updateDmrClusterLink**
> DmrClusterLinkResponse updateDmrClusterLink(dmrClusterName, remoteNodeName, body, select)

Update a Link object.

Update a Link object. Any attribute missing from the request will be left unchanged.  A Link connects nodes (either within a Cluster or between two different Clusters) and allows them to exchange topology information, subscriptions and data.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationBasicPassword|||x|x| authenticationScheme||||x| dmrClusterName|x|x||| egressFlowWindowSize||||x| initiator||||x| remoteNodeName|x|x||| span||||x| transportCompressedEnabled||||x| transportTlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrClusterApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrClusterApi apiInstance = new DmrClusterApi();
String dmrClusterName = "dmrClusterName_example"; // String | The name of the Cluster.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the Link.
DmrClusterLink body = new DmrClusterLink(); // DmrClusterLink | The Link object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    DmrClusterLinkResponse result = apiInstance.updateDmrClusterLink(dmrClusterName, remoteNodeName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrClusterApi#updateDmrClusterLink");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **dmrClusterName** | **String**| The name of the Cluster. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the Link. |
 **body** | [**DmrClusterLink**](DmrClusterLink.md)| The Link object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**DmrClusterLinkResponse**](DmrClusterLinkResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

