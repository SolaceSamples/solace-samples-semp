# semp_client.ClientProfileApi

All URIs are relative to *http://localhost/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn_client_profile**](ClientProfileApi.md#create_msg_vpn_client_profile) | **POST** /msgVpns/{msgVpnName}/clientProfiles | Creates a Client Profile object.
[**delete_msg_vpn_client_profile**](ClientProfileApi.md#delete_msg_vpn_client_profile) | **DELETE** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Deletes a Client Profile object.
[**get_msg_vpn_client_profile**](ClientProfileApi.md#get_msg_vpn_client_profile) | **GET** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Gets a Client Profile object.
[**get_msg_vpn_client_profiles**](ClientProfileApi.md#get_msg_vpn_client_profiles) | **GET** /msgVpns/{msgVpnName}/clientProfiles | Gets a list of Client Profile objects.
[**replace_msg_vpn_client_profile**](ClientProfileApi.md#replace_msg_vpn_client_profile) | **PUT** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Replaces a Client Profile object.
[**update_msg_vpn_client_profile**](ClientProfileApi.md#update_msg_vpn_client_profile) | **PATCH** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Updates a Client Profile object.


# **create_msg_vpn_client_profile**
> MsgVpnClientProfileResponse create_msg_vpn_client_profile(msg_vpn_name, body, select=select)

Creates a Client Profile object.

Creates a Client Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.ClientProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnClientProfile() # MsgVpnClientProfile | The Client Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Client Profile object.
    api_response = api_instance.create_msg_vpn_client_profile(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ClientProfileApi->create_msg_vpn_client_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnClientProfile**](MsgVpnClientProfile.md)| The Client Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_client_profile**
> SempMetaOnlyResponse delete_msg_vpn_client_profile(msg_vpn_name, client_profile_name)

Deletes a Client Profile object.

Deletes a Client Profile object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.ClientProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_profile_name = 'client_profile_name_example' # str | The clientProfileName of the Client Profile.

try: 
    # Deletes a Client Profile object.
    api_response = api_instance.delete_msg_vpn_client_profile(msg_vpn_name, client_profile_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ClientProfileApi->delete_msg_vpn_client_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **str**| The clientProfileName of the Client Profile. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_client_profile**
> MsgVpnClientProfileResponse get_msg_vpn_client_profile(msg_vpn_name, client_profile_name, select=select)

Gets a Client Profile object.

Gets a Client Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.ClientProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_profile_name = 'client_profile_name_example' # str | The clientProfileName of the Client Profile.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Client Profile object.
    api_response = api_instance.get_msg_vpn_client_profile(msg_vpn_name, client_profile_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ClientProfileApi->get_msg_vpn_client_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **str**| The clientProfileName of the Client Profile. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_client_profiles**
> MsgVpnClientProfilesResponse get_msg_vpn_client_profiles(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Client Profile objects.

Gets a list of Client Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.ClientProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Client Profile objects.
    api_response = api_instance.get_msg_vpn_client_profiles(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ClientProfileApi->get_msg_vpn_client_profiles: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfilesResponse**](MsgVpnClientProfilesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_client_profile**
> MsgVpnClientProfileResponse replace_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, select=select)

Replaces a Client Profile object.

Replaces a Client Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.ClientProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_profile_name = 'client_profile_name_example' # str | The clientProfileName of the Client Profile.
body = semp_client.MsgVpnClientProfile() # MsgVpnClientProfile | The Client Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Client Profile object.
    api_response = api_instance.replace_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ClientProfileApi->replace_msg_vpn_client_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **str**| The clientProfileName of the Client Profile. | 
 **body** | [**MsgVpnClientProfile**](MsgVpnClientProfile.md)| The Client Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_client_profile**
> MsgVpnClientProfileResponse update_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, select=select)

Updates a Client Profile object.

Updates a Client Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.ClientProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_profile_name = 'client_profile_name_example' # str | The clientProfileName of the Client Profile.
body = semp_client.MsgVpnClientProfile() # MsgVpnClientProfile | The Client Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Client Profile object.
    api_response = api_instance.update_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ClientProfileApi->update_msg_vpn_client_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **str**| The clientProfileName of the Client Profile. | 
 **body** | [**MsgVpnClientProfile**](MsgVpnClientProfile.md)| The Client Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

