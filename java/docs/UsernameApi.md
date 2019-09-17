# UsernameApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createUsername**](UsernameApi.md#createUsername) | **POST** /usernames | Create a Username object.
[**createUsernameMsgVpnAccessLevelException**](UsernameApi.md#createUsernameMsgVpnAccessLevelException) | **POST** /usernames/{userName}/msgVpnAccessLevelExceptions | Create a Message VPN Access Level Exception object.
[**deleteUsername**](UsernameApi.md#deleteUsername) | **DELETE** /usernames/{userName} | Delete a Username object.
[**deleteUsernameMsgVpnAccessLevelException**](UsernameApi.md#deleteUsernameMsgVpnAccessLevelException) | **DELETE** /usernames/{userName}/msgVpnAccessLevelExceptions/{msgVpnName} | Delete a Message VPN Access Level Exception object.
[**getUsername**](UsernameApi.md#getUsername) | **GET** /usernames/{userName} | Get a Username object.
[**getUsernameMsgVpnAccessLevelException**](UsernameApi.md#getUsernameMsgVpnAccessLevelException) | **GET** /usernames/{userName}/msgVpnAccessLevelExceptions/{msgVpnName} | Get a Message VPN Access Level Exception object.
[**getUsernameMsgVpnAccessLevelExceptions**](UsernameApi.md#getUsernameMsgVpnAccessLevelExceptions) | **GET** /usernames/{userName}/msgVpnAccessLevelExceptions | Get a list of Message VPN Access Level Exception objects.
[**getUsernames**](UsernameApi.md#getUsernames) | **GET** /usernames | Get a list of Username objects.
[**replaceUsername**](UsernameApi.md#replaceUsername) | **PUT** /usernames/{userName} | Replace a Username object.
[**replaceUsernameMsgVpnAccessLevelException**](UsernameApi.md#replaceUsernameMsgVpnAccessLevelException) | **PUT** /usernames/{userName}/msgVpnAccessLevelExceptions/{msgVpnName} | Replace a Message VPN Access Level Exception object.
[**updateUsername**](UsernameApi.md#updateUsername) | **PATCH** /usernames/{userName} | Update a Username object.
[**updateUsernameMsgVpnAccessLevelException**](UsernameApi.md#updateUsernameMsgVpnAccessLevelException) | **PATCH** /usernames/{userName}/msgVpnAccessLevelExceptions/{msgVpnName} | Update a Message VPN Access Level Exception object.


<a name="createUsername"></a>
# **createUsername**
> UsernameResponse createUsername(body, select)

Create a Username object.

Create a Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: password||||x| userName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/none\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin msgVpnDefaultAccessLevel|global/read-write    This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
Username body = new Username(); // Username | The Username object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    UsernameResponse result = apiInstance.createUsername(body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#createUsername");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**Username**](Username.md)| The Username object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**UsernameResponse**](UsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createUsernameMsgVpnAccessLevelException"></a>
# **createUsernameMsgVpnAccessLevelException**
> UsernameMsgVpnAccessLevelExceptionResponse createUsernameMsgVpnAccessLevelException(userName, body, select)

Create a Message VPN Access Level Exception object.

Create a Message VPN Access Level Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| userName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
String userName = "userName_example"; // String | Username.
UsernameMsgVpnAccessLevelException body = new UsernameMsgVpnAccessLevelException(); // UsernameMsgVpnAccessLevelException | The Message VPN Access Level Exception object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    UsernameMsgVpnAccessLevelExceptionResponse result = apiInstance.createUsernameMsgVpnAccessLevelException(userName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#createUsernameMsgVpnAccessLevelException");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userName** | **String**| Username. |
 **body** | [**UsernameMsgVpnAccessLevelException**](UsernameMsgVpnAccessLevelException.md)| The Message VPN Access Level Exception object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**UsernameMsgVpnAccessLevelExceptionResponse**](UsernameMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteUsername"></a>
# **deleteUsername**
> SempMetaOnlyResponse deleteUsername(userName)

Delete a Username object.

Delete a Username object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/none\&quot; is required to perform this operation.  This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
String userName = "userName_example"; // String | Username.
try {
    SempMetaOnlyResponse result = apiInstance.deleteUsername(userName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#deleteUsername");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userName** | **String**| Username. |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteUsernameMsgVpnAccessLevelException"></a>
# **deleteUsernameMsgVpnAccessLevelException**
> SempMetaOnlyResponse deleteUsernameMsgVpnAccessLevelException(userName, msgVpnName)

Delete a Message VPN Access Level Exception object.

Delete a Message VPN Access Level Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
String userName = "userName_example"; // String | Username.
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN for which an access level exception may be configured.
try {
    SempMetaOnlyResponse result = apiInstance.deleteUsernameMsgVpnAccessLevelException(userName, msgVpnName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#deleteUsernameMsgVpnAccessLevelException");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userName** | **String**| Username. |
 **msgVpnName** | **String**| The name of the Message VPN for which an access level exception may be configured. |

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getUsername"></a>
# **getUsername**
> UsernameResponse getUsername(userName, select)

Get a Username object.

Get a Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: password||x| userName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
String userName = "userName_example"; // String | Username.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    UsernameResponse result = apiInstance.getUsername(userName, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#getUsername");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userName** | **String**| Username. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**UsernameResponse**](UsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getUsernameMsgVpnAccessLevelException"></a>
# **getUsernameMsgVpnAccessLevelException**
> UsernameMsgVpnAccessLevelExceptionResponse getUsernameMsgVpnAccessLevelException(userName, msgVpnName, select)

Get a Message VPN Access Level Exception object.

Get a Message VPN Access Level Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| userName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
String userName = "userName_example"; // String | Username.
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN for which an access level exception may be configured.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    UsernameMsgVpnAccessLevelExceptionResponse result = apiInstance.getUsernameMsgVpnAccessLevelException(userName, msgVpnName, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#getUsernameMsgVpnAccessLevelException");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userName** | **String**| Username. |
 **msgVpnName** | **String**| The name of the Message VPN for which an access level exception may be configured. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**UsernameMsgVpnAccessLevelExceptionResponse**](UsernameMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getUsernameMsgVpnAccessLevelExceptions"></a>
# **getUsernameMsgVpnAccessLevelExceptions**
> UsernameMsgVpnAccessLevelExceptionsResponse getUsernameMsgVpnAccessLevelExceptions(userName, count, cursor, where, select)

Get a list of Message VPN Access Level Exception objects.

Get a list of Message VPN Access Level Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| userName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
String userName = "userName_example"; // String | Username.
Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
List<String> where = Arrays.asList("where_example"); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    UsernameMsgVpnAccessLevelExceptionsResponse result = apiInstance.getUsernameMsgVpnAccessLevelExceptions(userName, count, cursor, where, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#getUsernameMsgVpnAccessLevelExceptions");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userName** | **String**| Username. |
 **count** | **Integer**| Limit the count of objects in the response. See the documentation for the &#x60;count&#x60; parameter. | [optional] [default to 10]
 **cursor** | **String**| The cursor, or position, for the next page of objects. See the documentation for the &#x60;cursor&#x60; parameter. | [optional]
 **where** | [**List&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See the the documentation for the &#x60;where&#x60; parameter. | [optional]
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**UsernameMsgVpnAccessLevelExceptionsResponse**](UsernameMsgVpnAccessLevelExceptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getUsernames"></a>
# **getUsernames**
> UsernamesResponse getUsernames(count, cursor, where, select)

Get a list of Username objects.

Get a list of Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: password||x| userName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-only\&quot; is required to perform this operation.  This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
Integer count = 10; // Integer | Limit the count of objects in the response. See the documentation for the `count` parameter.
String cursor = "cursor_example"; // String | The cursor, or position, for the next page of objects. See the documentation for the `cursor` parameter.
List<String> where = Arrays.asList("where_example"); // List<String> | Include in the response only objects where certain conditions are true. See the the documentation for the `where` parameter.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    UsernamesResponse result = apiInstance.getUsernames(count, cursor, where, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#getUsernames");
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

[**UsernamesResponse**](UsernamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="replaceUsername"></a>
# **replaceUsername**
> UsernameResponse replaceUsername(userName, body, select)

Replace a Username object.

Replace a Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: password|||x|| userName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/none\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin msgVpnDefaultAccessLevel|global/read-write    This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
String userName = "userName_example"; // String | Username.
Username body = new Username(); // Username | The Username object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    UsernameResponse result = apiInstance.replaceUsername(userName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#replaceUsername");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userName** | **String**| Username. |
 **body** | [**Username**](Username.md)| The Username object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**UsernameResponse**](UsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="replaceUsernameMsgVpnAccessLevelException"></a>
# **replaceUsernameMsgVpnAccessLevelException**
> UsernameMsgVpnAccessLevelExceptionResponse replaceUsernameMsgVpnAccessLevelException(userName, msgVpnName, body, select)

Replace a Message VPN Access Level Exception object.

Replace a Message VPN Access Level Exception object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| userName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
String userName = "userName_example"; // String | Username.
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN for which an access level exception may be configured.
UsernameMsgVpnAccessLevelException body = new UsernameMsgVpnAccessLevelException(); // UsernameMsgVpnAccessLevelException | The Message VPN Access Level Exception object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    UsernameMsgVpnAccessLevelExceptionResponse result = apiInstance.replaceUsernameMsgVpnAccessLevelException(userName, msgVpnName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#replaceUsernameMsgVpnAccessLevelException");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userName** | **String**| Username. |
 **msgVpnName** | **String**| The name of the Message VPN for which an access level exception may be configured. |
 **body** | [**UsernameMsgVpnAccessLevelException**](UsernameMsgVpnAccessLevelException.md)| The Message VPN Access Level Exception object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**UsernameMsgVpnAccessLevelExceptionResponse**](UsernameMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateUsername"></a>
# **updateUsername**
> UsernameResponse updateUsername(userName, body, select)

Update a Username object.

Update a Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: password|||x|| userName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/none\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: globalAccessLevel|global/admin msgVpnDefaultAccessLevel|global/read-write    This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
String userName = "userName_example"; // String | Username.
Username body = new Username(); // Username | The Username object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    UsernameResponse result = apiInstance.updateUsername(userName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#updateUsername");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userName** | **String**| Username. |
 **body** | [**Username**](Username.md)| The Username object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**UsernameResponse**](UsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateUsernameMsgVpnAccessLevelException"></a>
# **updateUsernameMsgVpnAccessLevelException**
> UsernameMsgVpnAccessLevelExceptionResponse updateUsernameMsgVpnAccessLevelException(userName, msgVpnName, body, select)

Update a Message VPN Access Level Exception object.

Update a Message VPN Access Level Exception object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| userName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/read-write\&quot; is required to perform this operation.  This has been available since 2.12.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.UsernameApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

UsernameApi apiInstance = new UsernameApi();
String userName = "userName_example"; // String | Username.
String msgVpnName = "msgVpnName_example"; // String | The name of the Message VPN for which an access level exception may be configured.
UsernameMsgVpnAccessLevelException body = new UsernameMsgVpnAccessLevelException(); // UsernameMsgVpnAccessLevelException | The Message VPN Access Level Exception object's attributes.
List<String> select = Arrays.asList("select_example"); // List<String> | Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the `select` parameter.
try {
    UsernameMsgVpnAccessLevelExceptionResponse result = apiInstance.updateUsernameMsgVpnAccessLevelException(userName, msgVpnName, body, select);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UsernameApi#updateUsernameMsgVpnAccessLevelException");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userName** | **String**| Username. |
 **msgVpnName** | **String**| The name of the Message VPN for which an access level exception may be configured. |
 **body** | [**UsernameMsgVpnAccessLevelException**](UsernameMsgVpnAccessLevelException.md)| The Message VPN Access Level Exception object&#39;s attributes. |
 **select** | [**List&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. See the documentation for the &#x60;select&#x60; parameter. | [optional]

### Return type

[**UsernameMsgVpnAccessLevelExceptionResponse**](UsernameMsgVpnAccessLevelExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

