# DmrBridgeApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createMsgVpnDmrBridge**](DmrBridgeApi.md#createMsgVpnDmrBridge) | **POST** /msgVpns/{msgVpnName}/dmrBridges | Create a DMR Bridge object.
[**deleteMsgVpnDmrBridge**](DmrBridgeApi.md#deleteMsgVpnDmrBridge) | **DELETE** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Delete a DMR Bridge object.
[**getMsgVpnDmrBridge**](DmrBridgeApi.md#getMsgVpnDmrBridge) | **GET** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Get a DMR Bridge object.
[**getMsgVpnDmrBridges**](DmrBridgeApi.md#getMsgVpnDmrBridges) | **GET** /msgVpns/{msgVpnName}/dmrBridges | Get a list of DMR Bridge objects.
[**replaceMsgVpnDmrBridge**](DmrBridgeApi.md#replaceMsgVpnDmrBridge) | **PUT** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Replace a DMR Bridge object.
[**updateMsgVpnDmrBridge**](DmrBridgeApi.md#updateMsgVpnDmrBridge) | **PATCH** /msgVpns/{msgVpnName}/dmrBridges/{remoteNodeName} | Update a DMR Bridge object.


<a name="createMsgVpnDmrBridge"></a>
# **createMsgVpnDmrBridge**
> MsgVpnDmrBridgeResponse createMsgVpnDmrBridge(msgVpnName, body, select)

Create a DMR Bridge object.

Create a DMR Bridge object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| remoteNodeName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrBridgeApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrBridgeApi apiInstance = new DmrBridgeApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
MsgVpnDmrBridge body = new MsgVpnDmrBridge(); // MsgVpnDmrBridge | The DMR Bridge object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnDmrBridgeResponse result = apiInstance.createMsgVpnDmrBridge(msgVpnName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrBridgeApi#createMsgVpnDmrBridge");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **body** | [**MsgVpnDmrBridge**](MsgVpnDmrBridge.md)| The DMR Bridge object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnDmrBridgeResponse**](MsgVpnDmrBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteMsgVpnDmrBridge"></a>
# **deleteMsgVpnDmrBridge**
> SempMetaOnlyResponse deleteMsgVpnDmrBridge(msgVpnName, remoteNodeName)

Delete a DMR Bridge object.

Delete a DMR Bridge object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrBridgeApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrBridgeApi apiInstance = new DmrBridgeApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the DMR Bridge.
try {
    SempMetaOnlyResponse result = apiInstance.deleteMsgVpnDmrBridge(msgVpnName, remoteNodeName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrBridgeApi#deleteMsgVpnDmrBridge");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the DMR Bridge. |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMsgVpnDmrBridge"></a>
# **getMsgVpnDmrBridge**
> MsgVpnDmrBridgeResponse getMsgVpnDmrBridge(msgVpnName, remoteNodeName, select)

Get a DMR Bridge object.

Get a DMR Bridge object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| remoteNodeName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrBridgeApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrBridgeApi apiInstance = new DmrBridgeApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the DMR Bridge.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnDmrBridgeResponse result = apiInstance.getMsgVpnDmrBridge(msgVpnName, remoteNodeName, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrBridgeApi#getMsgVpnDmrBridge");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the DMR Bridge. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnDmrBridgeResponse**](MsgVpnDmrBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMsgVpnDmrBridges"></a>
# **getMsgVpnDmrBridges**
> MsgVpnDmrBridgesResponse getMsgVpnDmrBridges(msgVpnName, count, cursor, where, select)

Get a list of DMR Bridge objects.

Get a list of DMR Bridge objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| remoteNodeName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrBridgeApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrBridgeApi apiInstance = new DmrBridgeApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
List<String> where = Arrays.asList("where_example"); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnDmrBridgesResponse result = apiInstance.getMsgVpnDmrBridges(msgVpnName, count, cursor, where, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrBridgeApi#getMsgVpnDmrBridges");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10]
 **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional]
 **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional]
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnDmrBridgesResponse**](MsgVpnDmrBridgesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="replaceMsgVpnDmrBridge"></a>
# **replaceMsgVpnDmrBridge**
> MsgVpnDmrBridgeResponse replaceMsgVpnDmrBridge(msgVpnName, remoteNodeName, body, select)

Replace a DMR Bridge object.

Replace a DMR Bridge object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| remoteNodeName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrBridgeApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrBridgeApi apiInstance = new DmrBridgeApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the DMR Bridge.
MsgVpnDmrBridge body = new MsgVpnDmrBridge(); // MsgVpnDmrBridge | The DMR Bridge object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnDmrBridgeResponse result = apiInstance.replaceMsgVpnDmrBridge(msgVpnName, remoteNodeName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrBridgeApi#replaceMsgVpnDmrBridge");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the DMR Bridge. |
 **body** | [**MsgVpnDmrBridge**](MsgVpnDmrBridge.md)| The DMR Bridge object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnDmrBridgeResponse**](MsgVpnDmrBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateMsgVpnDmrBridge"></a>
# **updateMsgVpnDmrBridge**
> MsgVpnDmrBridgeResponse updateMsgVpnDmrBridge(msgVpnName, remoteNodeName, body, select)

Update a DMR Bridge object.

Update a DMR Bridge object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| remoteNodeName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.DmrBridgeApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

DmrBridgeApi apiInstance = new DmrBridgeApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String remoteNodeName = "remoteNodeName_example"; // String | The name of the node at the remote end of the DMR Bridge.
MsgVpnDmrBridge body = new MsgVpnDmrBridge(); // MsgVpnDmrBridge | The DMR Bridge object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnDmrBridgeResponse result = apiInstance.updateMsgVpnDmrBridge(msgVpnName, remoteNodeName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DmrBridgeApi#updateMsgVpnDmrBridge");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **remoteNodeName** | **String**| The name of the node at the remote end of the DMR Bridge. |
 **body** | [**MsgVpnDmrBridge**](MsgVpnDmrBridge.md)| The DMR Bridge object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnDmrBridgeResponse**](MsgVpnDmrBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

