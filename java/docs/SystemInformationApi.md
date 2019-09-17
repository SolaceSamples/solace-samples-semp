# SystemInformationApi

All URIs are relative to *http://www.solace.com/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getSystemInformation**](SystemInformationApi.md#getSystemInformation) | **GET** /systemInformation | Get a System Information object.


<a name="getSystemInformation"></a>
# **getSystemInformation**
> SystemInformationResponse getSystemInformation()

Get a System Information object.

Get a System Information object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/none\&quot; is required to perform this operation.  This has been available since 2.1.0.  This has been deprecated since 2.2.0.

### Example
```java
// Import classes:
//import com.solace.labs.sempclient.samplelib.ApiClient;
//import com.solace.labs.sempclient.samplelib.ApiException;
//import com.solace.labs.sempclient.samplelib.Configuration;
//import com.solace.labs.sempclient.samplelib.auth.*;
//import com.solace.labs.sempclient.samplelib.api.SystemInformationApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

SystemInformationApi apiInstance = new SystemInformationApi();
try {
    SystemInformationResponse result = apiInstance.getSystemInformation();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SystemInformationApi#getSystemInformation");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SystemInformationResponse**](SystemInformationResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

