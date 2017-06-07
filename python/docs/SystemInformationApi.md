# semp_client.SystemInformationApi

All URIs are relative to *http://localhost/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get_system_information**](SystemInformationApi.md#get_system_information) | **GET** /systemInformation | Gets SEMP API version and platform information.


# **get_system_information**
> SystemInformationResponse get_system_information()

Gets SEMP API version and platform information.

Gets SEMP API version and platform information.  A SEMP client authorized with a minimum access scope/level of \"global/none\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.SystemInformationApi()

try: 
    # Gets SEMP API version and platform information.
    api_response = api_instance.get_system_information()
    pprint(api_response)
except ApiException as e:
    print "Exception when calling SystemInformationApi->get_system_information: %s\n" % e
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

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

