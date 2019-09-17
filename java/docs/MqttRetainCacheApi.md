# MqttRetainCacheApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createMsgVpnMqttRetainCache**](MqttRetainCacheApi.md#createMsgVpnMqttRetainCache) | **POST** /msgVpns/{msgVpnName}/mqttRetainCaches | Create an MQTT Retain Cache object.
[**deleteMsgVpnMqttRetainCache**](MqttRetainCacheApi.md#deleteMsgVpnMqttRetainCache) | **DELETE** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Delete an MQTT Retain Cache object.
[**getMsgVpnMqttRetainCache**](MqttRetainCacheApi.md#getMsgVpnMqttRetainCache) | **GET** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Get an MQTT Retain Cache object.
[**getMsgVpnMqttRetainCaches**](MqttRetainCacheApi.md#getMsgVpnMqttRetainCaches) | **GET** /msgVpns/{msgVpnName}/mqttRetainCaches | Get a list of MQTT Retain Cache objects.
[**replaceMsgVpnMqttRetainCache**](MqttRetainCacheApi.md#replaceMsgVpnMqttRetainCache) | **PUT** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Replace an MQTT Retain Cache object.
[**updateMsgVpnMqttRetainCache**](MqttRetainCacheApi.md#updateMsgVpnMqttRetainCache) | **PATCH** /msgVpns/{msgVpnName}/mqttRetainCaches/{cacheName} | Update an MQTT Retain Cache object.


<a name="createMsgVpnMqttRetainCache"></a>
# **createMsgVpnMqttRetainCache**
> MsgVpnMqttRetainCacheResponse createMsgVpnMqttRetainCache(msgVpnName, body, select)

Create an MQTT Retain Cache object.

Create an MQTT Retain Cache object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.MqttRetainCacheApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

MqttRetainCacheApi apiInstance = new MqttRetainCacheApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
MsgVpnMqttRetainCache body = new MsgVpnMqttRetainCache(); // MsgVpnMqttRetainCache | The MQTT Retain Cache object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnMqttRetainCacheResponse result = apiInstance.createMsgVpnMqttRetainCache(msgVpnName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MqttRetainCacheApi#createMsgVpnMqttRetainCache");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **body** | [**MsgVpnMqttRetainCache**](MsgVpnMqttRetainCache.md)| The MQTT Retain Cache object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnMqttRetainCacheResponse**](MsgVpnMqttRetainCacheResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteMsgVpnMqttRetainCache"></a>
# **deleteMsgVpnMqttRetainCache**
> SempMetaOnlyResponse deleteMsgVpnMqttRetainCache(msgVpnName, cacheName)

Delete an MQTT Retain Cache object.

Delete an MQTT Retain Cache object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.MqttRetainCacheApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

MqttRetainCacheApi apiInstance = new MqttRetainCacheApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String cacheName = "cacheName_example"; // String | The name of the MQTT Retain Cache.
try {
    SempMetaOnlyResponse result = apiInstance.deleteMsgVpnMqttRetainCache(msgVpnName, cacheName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MqttRetainCacheApi#deleteMsgVpnMqttRetainCache");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **cacheName** | **String**| The name of the MQTT Retain Cache. |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMsgVpnMqttRetainCache"></a>
# **getMsgVpnMqttRetainCache**
> MsgVpnMqttRetainCacheResponse getMsgVpnMqttRetainCache(msgVpnName, cacheName, select)

Get an MQTT Retain Cache object.

Get an MQTT Retain Cache object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.MqttRetainCacheApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

MqttRetainCacheApi apiInstance = new MqttRetainCacheApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String cacheName = "cacheName_example"; // String | The name of the MQTT Retain Cache.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnMqttRetainCacheResponse result = apiInstance.getMsgVpnMqttRetainCache(msgVpnName, cacheName, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MqttRetainCacheApi#getMsgVpnMqttRetainCache");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **cacheName** | **String**| The name of the MQTT Retain Cache. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnMqttRetainCacheResponse**](MsgVpnMqttRetainCacheResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMsgVpnMqttRetainCaches"></a>
# **getMsgVpnMqttRetainCaches**
> MsgVpnMqttRetainCachesResponse getMsgVpnMqttRetainCaches(msgVpnName, count, cursor, where, select)

Get a list of MQTT Retain Cache objects.

Get a list of MQTT Retain Cache objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: cacheName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.MqttRetainCacheApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

MqttRetainCacheApi apiInstance = new MqttRetainCacheApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
List<String> where = Arrays.asList("where_example"); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnMqttRetainCachesResponse result = apiInstance.getMsgVpnMqttRetainCaches(msgVpnName, count, cursor, where, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MqttRetainCacheApi#getMsgVpnMqttRetainCaches");
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

[**MsgVpnMqttRetainCachesResponse**](MsgVpnMqttRetainCachesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="replaceMsgVpnMqttRetainCache"></a>
# **replaceMsgVpnMqttRetainCache**
> MsgVpnMqttRetainCacheResponse replaceMsgVpnMqttRetainCache(msgVpnName, cacheName, body, select)

Replace an MQTT Retain Cache object.

Replace an MQTT Retain Cache object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.MqttRetainCacheApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

MqttRetainCacheApi apiInstance = new MqttRetainCacheApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String cacheName = "cacheName_example"; // String | The name of the MQTT Retain Cache.
MsgVpnMqttRetainCache body = new MsgVpnMqttRetainCache(); // MsgVpnMqttRetainCache | The MQTT Retain Cache object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnMqttRetainCacheResponse result = apiInstance.replaceMsgVpnMqttRetainCache(msgVpnName, cacheName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MqttRetainCacheApi#replaceMsgVpnMqttRetainCache");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **cacheName** | **String**| The name of the MQTT Retain Cache. |
 **body** | [**MsgVpnMqttRetainCache**](MsgVpnMqttRetainCache.md)| The MQTT Retain Cache object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnMqttRetainCacheResponse**](MsgVpnMqttRetainCacheResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateMsgVpnMqttRetainCache"></a>
# **updateMsgVpnMqttRetainCache**
> MsgVpnMqttRetainCacheResponse updateMsgVpnMqttRetainCache(msgVpnName, cacheName, body, select)

Update an MQTT Retain Cache object.

Update an MQTT Retain Cache object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: cacheName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.11.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.MqttRetainCacheApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

MqttRetainCacheApi apiInstance = new MqttRetainCacheApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String cacheName = "cacheName_example"; // String | The name of the MQTT Retain Cache.
MsgVpnMqttRetainCache body = new MsgVpnMqttRetainCache(); // MsgVpnMqttRetainCache | The MQTT Retain Cache object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnMqttRetainCacheResponse result = apiInstance.updateMsgVpnMqttRetainCache(msgVpnName, cacheName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MqttRetainCacheApi#updateMsgVpnMqttRetainCache");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **cacheName** | **String**| The name of the MQTT Retain Cache. |
 **body** | [**MsgVpnMqttRetainCache**](MsgVpnMqttRetainCache.md)| The MQTT Retain Cache object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnMqttRetainCacheResponse**](MsgVpnMqttRetainCacheResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

