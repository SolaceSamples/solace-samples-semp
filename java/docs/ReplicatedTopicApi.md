# ReplicatedTopicApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createMsgVpnReplicatedTopic**](ReplicatedTopicApi.md#createMsgVpnReplicatedTopic) | **POST** /msgVpns/{msgVpnName}/replicatedTopics | Create a Replicated Topic object.
[**deleteMsgVpnReplicatedTopic**](ReplicatedTopicApi.md#deleteMsgVpnReplicatedTopic) | **DELETE** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Delete a Replicated Topic object.
[**getMsgVpnReplicatedTopic**](ReplicatedTopicApi.md#getMsgVpnReplicatedTopic) | **GET** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Get a Replicated Topic object.
[**getMsgVpnReplicatedTopics**](ReplicatedTopicApi.md#getMsgVpnReplicatedTopics) | **GET** /msgVpns/{msgVpnName}/replicatedTopics | Get a list of Replicated Topic objects.
[**replaceMsgVpnReplicatedTopic**](ReplicatedTopicApi.md#replaceMsgVpnReplicatedTopic) | **PUT** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Replace a Replicated Topic object.
[**updateMsgVpnReplicatedTopic**](ReplicatedTopicApi.md#updateMsgVpnReplicatedTopic) | **PATCH** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Update a Replicated Topic object.


<a name="createMsgVpnReplicatedTopic"></a>
# **createMsgVpnReplicatedTopic**
> MsgVpnReplicatedTopicResponse createMsgVpnReplicatedTopic(msgVpnName, body, select)

Create a Replicated Topic object.

Create a Replicated Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.9.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ReplicatedTopicApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ReplicatedTopicApi apiInstance = new ReplicatedTopicApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
MsgVpnReplicatedTopic body = new MsgVpnReplicatedTopic(); // MsgVpnReplicatedTopic | The Replicated Topic object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnReplicatedTopicResponse result = apiInstance.createMsgVpnReplicatedTopic(msgVpnName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReplicatedTopicApi#createMsgVpnReplicatedTopic");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **body** | [**MsgVpnReplicatedTopic**](MsgVpnReplicatedTopic.md)| The Replicated Topic object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteMsgVpnReplicatedTopic"></a>
# **deleteMsgVpnReplicatedTopic**
> SempMetaOnlyResponse deleteMsgVpnReplicatedTopic(msgVpnName, replicatedTopic)

Delete a Replicated Topic object.

Delete a Replicated Topic object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.9.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ReplicatedTopicApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ReplicatedTopicApi apiInstance = new ReplicatedTopicApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String replicatedTopic = "replicatedTopic_example"; // String | The topic for applying replication. Published messages matching this topic will be replicated to the standby site.
try {
    SempMetaOnlyResponse result = apiInstance.deleteMsgVpnReplicatedTopic(msgVpnName, replicatedTopic);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReplicatedTopicApi#deleteMsgVpnReplicatedTopic");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **replicatedTopic** | **String**| The topic for applying replication. Published messages matching this topic will be replicated to the standby site. |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMsgVpnReplicatedTopic"></a>
# **getMsgVpnReplicatedTopic**
> MsgVpnReplicatedTopicResponse getMsgVpnReplicatedTopic(msgVpnName, replicatedTopic, select)

Get a Replicated Topic object.

Get a Replicated Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicatedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.9.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ReplicatedTopicApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ReplicatedTopicApi apiInstance = new ReplicatedTopicApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String replicatedTopic = "replicatedTopic_example"; // String | The topic for applying replication. Published messages matching this topic will be replicated to the standby site.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnReplicatedTopicResponse result = apiInstance.getMsgVpnReplicatedTopic(msgVpnName, replicatedTopic, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReplicatedTopicApi#getMsgVpnReplicatedTopic");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **replicatedTopic** | **String**| The topic for applying replication. Published messages matching this topic will be replicated to the standby site. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMsgVpnReplicatedTopics"></a>
# **getMsgVpnReplicatedTopics**
> MsgVpnReplicatedTopicsResponse getMsgVpnReplicatedTopics(msgVpnName, count, cursor, where, select)

Get a list of Replicated Topic objects.

Get a list of Replicated Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicatedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.9.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ReplicatedTopicApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ReplicatedTopicApi apiInstance = new ReplicatedTopicApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
List<String> where = Arrays.asList("where_example"); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnReplicatedTopicsResponse result = apiInstance.getMsgVpnReplicatedTopics(msgVpnName, count, cursor, where, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReplicatedTopicApi#getMsgVpnReplicatedTopics");
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

[**MsgVpnReplicatedTopicsResponse**](MsgVpnReplicatedTopicsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="replaceMsgVpnReplicatedTopic"></a>
# **replaceMsgVpnReplicatedTopic**
> MsgVpnReplicatedTopicResponse replaceMsgVpnReplicatedTopic(msgVpnName, replicatedTopic, body, select)

Replace a Replicated Topic object.

Replace a Replicated Topic object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.9.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ReplicatedTopicApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ReplicatedTopicApi apiInstance = new ReplicatedTopicApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String replicatedTopic = "replicatedTopic_example"; // String | The topic for applying replication. Published messages matching this topic will be replicated to the standby site.
MsgVpnReplicatedTopic body = new MsgVpnReplicatedTopic(); // MsgVpnReplicatedTopic | The Replicated Topic object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnReplicatedTopicResponse result = apiInstance.replaceMsgVpnReplicatedTopic(msgVpnName, replicatedTopic, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReplicatedTopicApi#replaceMsgVpnReplicatedTopic");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **replicatedTopic** | **String**| The topic for applying replication. Published messages matching this topic will be replicated to the standby site. |
 **body** | [**MsgVpnReplicatedTopic**](MsgVpnReplicatedTopic.md)| The Replicated Topic object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateMsgVpnReplicatedTopic"></a>
# **updateMsgVpnReplicatedTopic**
> MsgVpnReplicatedTopicResponse updateMsgVpnReplicatedTopic(msgVpnName, replicatedTopic, body, select)

Update a Replicated Topic object.

Update a Replicated Topic object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.9.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ReplicatedTopicApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ReplicatedTopicApi apiInstance = new ReplicatedTopicApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String replicatedTopic = "replicatedTopic_example"; // String | The topic for applying replication. Published messages matching this topic will be replicated to the standby site.
MsgVpnReplicatedTopic body = new MsgVpnReplicatedTopic(); // MsgVpnReplicatedTopic | The Replicated Topic object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnReplicatedTopicResponse result = apiInstance.updateMsgVpnReplicatedTopic(msgVpnName, replicatedTopic, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ReplicatedTopicApi#updateMsgVpnReplicatedTopic");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **replicatedTopic** | **String**| The topic for applying replication. Published messages matching this topic will be replicated to the standby site. |
 **body** | [**MsgVpnReplicatedTopic**](MsgVpnReplicatedTopic.md)| The Replicated Topic object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

