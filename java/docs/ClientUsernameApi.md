# ClientUsernameApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createMsgVpnClientUsername**](ClientUsernameApi.md#createMsgVpnClientUsername) | **POST** /msgVpns/{msgVpnName}/clientUsernames | Create a Client Username object.
[**deleteMsgVpnClientUsername**](ClientUsernameApi.md#deleteMsgVpnClientUsername) | **DELETE** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Delete a Client Username object.
[**getMsgVpnClientUsername**](ClientUsernameApi.md#getMsgVpnClientUsername) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Get a Client Username object.
[**getMsgVpnClientUsernames**](ClientUsernameApi.md#getMsgVpnClientUsernames) | **GET** /msgVpns/{msgVpnName}/clientUsernames | Get a list of Client Username objects.
[**replaceMsgVpnClientUsername**](ClientUsernameApi.md#replaceMsgVpnClientUsername) | **PUT** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Replace a Client Username object.
[**updateMsgVpnClientUsername**](ClientUsernameApi.md#updateMsgVpnClientUsername) | **PATCH** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Update a Client Username object.


<a name="createMsgVpnClientUsername"></a>
# **createMsgVpnClientUsername**
> MsgVpnClientUsernameResponse createMsgVpnClientUsername(msgVpnName, body, select)

Create a Client Username object.

Create a Client Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientUsername|x|x||| msgVpnName|x||x|| password||||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ClientUsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ClientUsernameApi apiInstance = new ClientUsernameApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
MsgVpnClientUsername body = new MsgVpnClientUsername(); // MsgVpnClientUsername | The Client Username object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnClientUsernameResponse result = apiInstance.createMsgVpnClientUsername(msgVpnName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClientUsernameApi#createMsgVpnClientUsername");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteMsgVpnClientUsername"></a>
# **deleteMsgVpnClientUsername**
> SempMetaOnlyResponse deleteMsgVpnClientUsername(msgVpnName, clientUsername)

Delete a Client Username object.

Delete a Client Username object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ClientUsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ClientUsernameApi apiInstance = new ClientUsernameApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String clientUsername = "clientUsername_example"; // String | The value of the Client Username.
try {
    SempMetaOnlyResponse result = apiInstance.deleteMsgVpnClientUsername(msgVpnName, clientUsername);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClientUsernameApi#deleteMsgVpnClientUsername");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **clientUsername** | **String**| The value of the Client Username. |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMsgVpnClientUsername"></a>
# **getMsgVpnClientUsername**
> MsgVpnClientUsernameResponse getMsgVpnClientUsername(msgVpnName, clientUsername, select)

Get a Client Username object.

Get a Client Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ClientUsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ClientUsernameApi apiInstance = new ClientUsernameApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String clientUsername = "clientUsername_example"; // String | The value of the Client Username.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnClientUsernameResponse result = apiInstance.getMsgVpnClientUsername(msgVpnName, clientUsername, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClientUsernameApi#getMsgVpnClientUsername");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **clientUsername** | **String**| The value of the Client Username. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getMsgVpnClientUsernames"></a>
# **getMsgVpnClientUsernames**
> MsgVpnClientUsernamesResponse getMsgVpnClientUsernames(msgVpnName, count, cursor, where, select)

Get a list of Client Username objects.

Get a list of Client Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-only\&quot; is required to perform this operation.  This has been available since 2.0.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ClientUsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ClientUsernameApi apiInstance = new ClientUsernameApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
List<String> where = Arrays.asList("where_example"); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnClientUsernamesResponse result = apiInstance.getMsgVpnClientUsernames(msgVpnName, count, cursor, where, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClientUsernameApi#getMsgVpnClientUsernames");
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

[**MsgVpnClientUsernamesResponse**](MsgVpnClientUsernamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="replaceMsgVpnClientUsername"></a>
# **replaceMsgVpnClientUsername**
> MsgVpnClientUsernameResponse replaceMsgVpnClientUsername(msgVpnName, clientUsername, body, select)

Replace a Client Username object.

Replace a Client Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ClientUsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ClientUsernameApi apiInstance = new ClientUsernameApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String clientUsername = "clientUsername_example"; // String | The value of the Client Username.
MsgVpnClientUsername body = new MsgVpnClientUsername(); // MsgVpnClientUsername | The Client Username object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnClientUsernameResponse result = apiInstance.replaceMsgVpnClientUsername(msgVpnName, clientUsername, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClientUsernameApi#replaceMsgVpnClientUsername");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **clientUsername** | **String**| The value of the Client Username. |
 **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateMsgVpnClientUsername"></a>
# **updateMsgVpnClientUsername**
> MsgVpnClientUsernameResponse updateMsgVpnClientUsername(msgVpnName, clientUsername, body, select)

Update a Client Username object.

Update a Client Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/read-write\&quot; is required to perform this operation.  This has been available since 2.0.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.ClientUsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ClientUsernameApi apiInstance = new ClientUsernameApi();
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN.
String clientUsername = "clientUsername_example"; // String | The value of the Client Username.
MsgVpnClientUsername body = new MsgVpnClientUsername(); // MsgVpnClientUsername | The Client Username object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    MsgVpnClientUsernameResponse result = apiInstance.updateMsgVpnClientUsername(msgVpnName, clientUsername, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClientUsernameApi#updateMsgVpnClientUsername");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msgVpnName** | **String**| The name of the Message VPN. |
 **clientUsername** | **String**| The value of the Client Username. |
 **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

