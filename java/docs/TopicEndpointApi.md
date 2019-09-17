# TopicEndpointApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createMsgVpnTopicEndpoint**](TopicEndpointApi.md#createMsgVpnTopicEndpoint) | **POST** /msgVpns/{msgVpnName}/topicEndpoints | Create a Topic Endpoint object.
[**deleteMsgVpnTopicEndpoint**](TopicEndpointApi.md#deleteMsgVpnTopicEndpoint) | **DELETE** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Delete a Topic Endpoint object.
[**getMsgVpnTopicEndpoint**](TopicEndpointApi.md#getMsgVpnTopicEndpoint) | **GET** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Get a Topic Endpoint object.
[**getMsgVpnTopicEndpoints**](TopicEndpointApi.md#getMsgVpnTopicEndpoints) | **GET** /msgVpns/{msgVpnName}/topicEndpoints | Get a list of Topic Endpoint objects.
[**replaceMsgVpnTopicEndpoint**](TopicEndpointApi.md#replaceMsgVpnTopicEndpoint) | **PUT** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Replace a Topic Endpoint object.
[**updateMsgVpnTopicEndpoint**](TopicEndpointApi.md#updateMsgVpnTopicEndpoint) | **PATCH** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Update a Topic Endpoint object.


<a name="createMsgVpnTopicEndpoint"></a>
# **createMsgVpnTopicEndpoint**
> MsgVpnTopicEndpointResponse createMsgVpnTopicEndpoint(msgVpnName, body, select)

Create a Topic Endpoint object.

Create a Topic Endpoint object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.TopicEndpointApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

TopicEndpointApi apiInstance = new TopicEndpointApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
MsgVpnTopicEndpoint body = new MsgVpnTopicEndpoint(); // MsgVpnTopicEndpoint | The Topic Endpoint object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnTopicEndpointResponse result = apiInstance.createMsgVpnTopicEndpoint(msgVpnName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TopicEndpointApi#createMsgVpnTopicEndpoint");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **body** | [**MsgVpnTopicEndpoint**](MsgVpnTopicEndpoint.md)| The Topic Endpoint object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteMsgVpnTopicEndpoint"></a>
# **deleteMsgVpnTopicEndpoint**
> SempMetaOnlyResponse deleteMsgVpnTopicEndpoint(msgVpnName, topicEndpointName)

Delete a Topic Endpoint object.

Delete a Topic Endpoint object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.TopicEndpointApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

TopicEndpointApi apiInstance = new TopicEndpointApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String topicEndpointName = "topicEndpointName_example"; // String | The name of the Topic Endpoint.
try {
    SempMetaOnlyResponse result = apiInstance.deleteMsgVpnTopicEndpoint(msgVpnName, topicEndpointName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TopicEndpointApi#deleteMsgVpnTopicEndpoint");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **topicEndpointName** | **String**| The name of the Topic Endpoint. |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMsgVpnTopicEndpoint"></a>
# **getMsgVpnTopicEndpoint**
> MsgVpnTopicEndpointResponse getMsgVpnTopicEndpoint(msgVpnName, topicEndpointName, select)

Get a Topic Endpoint object.

Get a Topic Endpoint object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| topicEndpointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.TopicEndpointApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

TopicEndpointApi apiInstance = new TopicEndpointApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String topicEndpointName = "topicEndpointName_example"; // String | The name of the Topic Endpoint.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnTopicEndpointResponse result = apiInstance.getMsgVpnTopicEndpoint(msgVpnName, topicEndpointName, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TopicEndpointApi#getMsgVpnTopicEndpoint");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **topicEndpointName** | **String**| The name of the Topic Endpoint. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMsgVpnTopicEndpoints"></a>
# **getMsgVpnTopicEndpoints**
> MsgVpnTopicEndpointsResponse getMsgVpnTopicEndpoints(msgVpnName, count, cursor, where, select)

Get a list of Topic Endpoint objects.

Get a list of Topic Endpoint objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| topicEndpointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.1.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.TopicEndpointApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

TopicEndpointApi apiInstance = new TopicEndpointApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
List<String> where = Arrays.asList("where_example"); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnTopicEndpointsResponse result = apiInstance.getMsgVpnTopicEndpoints(msgVpnName, count, cursor, where, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TopicEndpointApi#getMsgVpnTopicEndpoints");
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

[**MsgVpnTopicEndpointsResponse**](MsgVpnTopicEndpointsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="replaceMsgVpnTopicEndpoint"></a>
# **replaceMsgVpnTopicEndpoint**
> MsgVpnTopicEndpointResponse replaceMsgVpnTopicEndpoint(msgVpnName, topicEndpointName, body, select)

Replace a Topic Endpoint object.

Replace a Topic Endpoint object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| respectMsgPriorityEnabled||||x| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.TopicEndpointApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

TopicEndpointApi apiInstance = new TopicEndpointApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String topicEndpointName = "topicEndpointName_example"; // String | The name of the Topic Endpoint.
MsgVpnTopicEndpoint body = new MsgVpnTopicEndpoint(); // MsgVpnTopicEndpoint | The Topic Endpoint object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnTopicEndpointResponse result = apiInstance.replaceMsgVpnTopicEndpoint(msgVpnName, topicEndpointName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TopicEndpointApi#replaceMsgVpnTopicEndpoint");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **topicEndpointName** | **String**| The name of the Topic Endpoint. |
 **body** | [**MsgVpnTopicEndpoint**](MsgVpnTopicEndpoint.md)| The Topic Endpoint object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateMsgVpnTopicEndpoint"></a>
# **updateMsgVpnTopicEndpoint**
> MsgVpnTopicEndpointResponse updateMsgVpnTopicEndpoint(msgVpnName, topicEndpointName, body, select)

Update a Topic Endpoint object.

Update a Topic Endpoint object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| respectMsgPriorityEnabled||||x| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.1.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.TopicEndpointApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

TopicEndpointApi apiInstance = new TopicEndpointApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String topicEndpointName = "topicEndpointName_example"; // String | The name of the Topic Endpoint.
MsgVpnTopicEndpoint body = new MsgVpnTopicEndpoint(); // MsgVpnTopicEndpoint | The Topic Endpoint object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnTopicEndpointResponse result = apiInstance.updateMsgVpnTopicEndpoint(msgVpnName, topicEndpointName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TopicEndpointApi#updateMsgVpnTopicEndpoint");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **topicEndpointName** | **String**| The name of the Topic Endpoint. |
 **body** | [**MsgVpnTopicEndpoint**](MsgVpnTopicEndpoint.md)| The Topic Endpoint object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

