# semp_client.AclProfileApi

All URIs are relative to *http://localhost/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn_acl_profile**](AclProfileApi.md#create_msg_vpn_acl_profile) | **POST** /msgVpns/{msgVpnName}/aclProfiles | Creates an ACL Profile object.
[**create_msg_vpn_acl_profile_client_connect_exception**](AclProfileApi.md#create_msg_vpn_acl_profile_client_connect_exception) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions | Creates a Client Connect Exception object.
[**create_msg_vpn_acl_profile_publish_exception**](AclProfileApi.md#create_msg_vpn_acl_profile_publish_exception) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions | Creates a Publish Topic Exception object.
[**create_msg_vpn_acl_profile_subscribe_exception**](AclProfileApi.md#create_msg_vpn_acl_profile_subscribe_exception) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions | Creates a Subscribe Topic Exception object.
[**delete_msg_vpn_acl_profile**](AclProfileApi.md#delete_msg_vpn_acl_profile) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Deletes an ACL Profile object.
[**delete_msg_vpn_acl_profile_client_connect_exception**](AclProfileApi.md#delete_msg_vpn_acl_profile_client_connect_exception) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress} | Deletes a Client Connect Exception object.
[**delete_msg_vpn_acl_profile_publish_exception**](AclProfileApi.md#delete_msg_vpn_acl_profile_publish_exception) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic} | Deletes a Publish Topic Exception object.
[**delete_msg_vpn_acl_profile_subscribe_exception**](AclProfileApi.md#delete_msg_vpn_acl_profile_subscribe_exception) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic} | Deletes a Subscribe Topic Exception object.
[**get_msg_vpn_acl_profile**](AclProfileApi.md#get_msg_vpn_acl_profile) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Gets an ACL Profile object.
[**get_msg_vpn_acl_profile_client_connect_exception**](AclProfileApi.md#get_msg_vpn_acl_profile_client_connect_exception) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress} | Gets a Client Connect Exception object.
[**get_msg_vpn_acl_profile_client_connect_exceptions**](AclProfileApi.md#get_msg_vpn_acl_profile_client_connect_exceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions | Gets a list of Client Connect Exception objects.
[**get_msg_vpn_acl_profile_publish_exception**](AclProfileApi.md#get_msg_vpn_acl_profile_publish_exception) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic} | Gets a Publish Topic Exception object.
[**get_msg_vpn_acl_profile_publish_exceptions**](AclProfileApi.md#get_msg_vpn_acl_profile_publish_exceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions | Gets a list of Publish Topic Exception objects.
[**get_msg_vpn_acl_profile_subscribe_exception**](AclProfileApi.md#get_msg_vpn_acl_profile_subscribe_exception) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic} | Gets a Subscribe Topic Exception object.
[**get_msg_vpn_acl_profile_subscribe_exceptions**](AclProfileApi.md#get_msg_vpn_acl_profile_subscribe_exceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions | Gets a list of Subscribe Topic Exception objects.
[**get_msg_vpn_acl_profiles**](AclProfileApi.md#get_msg_vpn_acl_profiles) | **GET** /msgVpns/{msgVpnName}/aclProfiles | Gets a list of ACL Profile objects.
[**replace_msg_vpn_acl_profile**](AclProfileApi.md#replace_msg_vpn_acl_profile) | **PUT** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Replaces an ACL Profile object.
[**update_msg_vpn_acl_profile**](AclProfileApi.md#update_msg_vpn_acl_profile) | **PATCH** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Updates an ACL Profile object.


# **create_msg_vpn_acl_profile**
> MsgVpnAclProfileResponse create_msg_vpn_acl_profile(msg_vpn_name, body, select=select)

Creates an ACL Profile object.

Creates an ACL Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnAclProfile() # MsgVpnAclProfile | The ACL Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates an ACL Profile object.
    api_response = api_instance.create_msg_vpn_acl_profile(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->create_msg_vpn_acl_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnAclProfile**](MsgVpnAclProfile.md)| The ACL Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileResponse**](MsgVpnAclProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_acl_profile_client_connect_exception**
> MsgVpnAclProfileClientConnectExceptionResponse create_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, body, select=select)

Creates a Client Connect Exception object.

Creates a Client Connect Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| clientConnectExceptionAddress|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
body = semp_client.MsgVpnAclProfileClientConnectException() # MsgVpnAclProfileClientConnectException | The Client Connect Exception object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Client Connect Exception object.
    api_response = api_instance.create_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->create_msg_vpn_acl_profile_client_connect_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **body** | [**MsgVpnAclProfileClientConnectException**](MsgVpnAclProfileClientConnectException.md)| The Client Connect Exception object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileClientConnectExceptionResponse**](MsgVpnAclProfileClientConnectExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_acl_profile_publish_exception**
> MsgVpnAclProfilePublishExceptionResponse create_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, body, select=select)

Creates a Publish Topic Exception object.

Creates a Publish Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| publishExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
body = semp_client.MsgVpnAclProfilePublishException() # MsgVpnAclProfilePublishException | The Publish Topic Exception object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Publish Topic Exception object.
    api_response = api_instance.create_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->create_msg_vpn_acl_profile_publish_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **body** | [**MsgVpnAclProfilePublishException**](MsgVpnAclProfilePublishException.md)| The Publish Topic Exception object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfilePublishExceptionResponse**](MsgVpnAclProfilePublishExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_acl_profile_subscribe_exception**
> MsgVpnAclProfileSubscribeExceptionResponse create_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, body, select=select)

Creates a Subscribe Topic Exception object.

Creates a Subscribe Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| subscribeExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
body = semp_client.MsgVpnAclProfileSubscribeException() # MsgVpnAclProfileSubscribeException | The Subscribe Topic Exception object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Subscribe Topic Exception object.
    api_response = api_instance.create_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->create_msg_vpn_acl_profile_subscribe_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **body** | [**MsgVpnAclProfileSubscribeException**](MsgVpnAclProfileSubscribeException.md)| The Subscribe Topic Exception object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileSubscribeExceptionResponse**](MsgVpnAclProfileSubscribeExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_acl_profile**
> SempMetaOnlyResponse delete_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name)

Deletes an ACL Profile object.

Deletes an ACL Profile object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.

try: 
    # Deletes an ACL Profile object.
    api_response = api_instance.delete_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->delete_msg_vpn_acl_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_acl_profile_client_connect_exception**
> SempMetaOnlyResponse delete_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address)

Deletes a Client Connect Exception object.

Deletes a Client Connect Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
client_connect_exception_address = 'client_connect_exception_address_example' # str | The clientConnectExceptionAddress of the Client Connect Exception.

try: 
    # Deletes a Client Connect Exception object.
    api_response = api_instance.delete_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->delete_msg_vpn_acl_profile_client_connect_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **client_connect_exception_address** | **str**| The clientConnectExceptionAddress of the Client Connect Exception. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_acl_profile_publish_exception**
> SempMetaOnlyResponse delete_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic)

Deletes a Publish Topic Exception object.

Deletes a Publish Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
topic_syntax = 'topic_syntax_example' # str | The topicSyntax of the Publish Topic Exception.
publish_exception_topic = 'publish_exception_topic_example' # str | The publishExceptionTopic of the Publish Topic Exception.

try: 
    # Deletes a Publish Topic Exception object.
    api_response = api_instance.delete_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->delete_msg_vpn_acl_profile_publish_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **topic_syntax** | **str**| The topicSyntax of the Publish Topic Exception. | 
 **publish_exception_topic** | **str**| The publishExceptionTopic of the Publish Topic Exception. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_acl_profile_subscribe_exception**
> SempMetaOnlyResponse delete_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic)

Deletes a Subscribe Topic Exception object.

Deletes a Subscribe Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
topic_syntax = 'topic_syntax_example' # str | The topicSyntax of the Subscribe Topic Exception.
subscribe_exception_topic = 'subscribe_exception_topic_example' # str | The subscribeExceptionTopic of the Subscribe Topic Exception.

try: 
    # Deletes a Subscribe Topic Exception object.
    api_response = api_instance.delete_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->delete_msg_vpn_acl_profile_subscribe_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **topic_syntax** | **str**| The topicSyntax of the Subscribe Topic Exception. | 
 **subscribe_exception_topic** | **str**| The subscribeExceptionTopic of the Subscribe Topic Exception. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile**
> MsgVpnAclProfileResponse get_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, select=select)

Gets an ACL Profile object.

Gets an ACL Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets an ACL Profile object.
    api_response = api_instance.get_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->get_msg_vpn_acl_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileResponse**](MsgVpnAclProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_client_connect_exception**
> MsgVpnAclProfileClientConnectExceptionResponse get_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address, select=select)

Gets a Client Connect Exception object.

Gets a Client Connect Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
client_connect_exception_address = 'client_connect_exception_address_example' # str | The clientConnectExceptionAddress of the Client Connect Exception.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Client Connect Exception object.
    api_response = api_instance.get_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->get_msg_vpn_acl_profile_client_connect_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **client_connect_exception_address** | **str**| The clientConnectExceptionAddress of the Client Connect Exception. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileClientConnectExceptionResponse**](MsgVpnAclProfileClientConnectExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_client_connect_exceptions**
> MsgVpnAclProfileClientConnectExceptionsResponse get_msg_vpn_acl_profile_client_connect_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Client Connect Exception objects.

Gets a list of Client Connect Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Client Connect Exception objects.
    api_response = api_instance.get_msg_vpn_acl_profile_client_connect_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->get_msg_vpn_acl_profile_client_connect_exceptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileClientConnectExceptionsResponse**](MsgVpnAclProfileClientConnectExceptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_publish_exception**
> MsgVpnAclProfilePublishExceptionResponse get_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, select=select)

Gets a Publish Topic Exception object.

Gets a Publish Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
topic_syntax = 'topic_syntax_example' # str | The topicSyntax of the Publish Topic Exception.
publish_exception_topic = 'publish_exception_topic_example' # str | The publishExceptionTopic of the Publish Topic Exception.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Publish Topic Exception object.
    api_response = api_instance.get_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->get_msg_vpn_acl_profile_publish_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **topic_syntax** | **str**| The topicSyntax of the Publish Topic Exception. | 
 **publish_exception_topic** | **str**| The publishExceptionTopic of the Publish Topic Exception. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfilePublishExceptionResponse**](MsgVpnAclProfilePublishExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_publish_exceptions**
> MsgVpnAclProfilePublishExceptionsResponse get_msg_vpn_acl_profile_publish_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Publish Topic Exception objects.

Gets a list of Publish Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Publish Topic Exception objects.
    api_response = api_instance.get_msg_vpn_acl_profile_publish_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->get_msg_vpn_acl_profile_publish_exceptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfilePublishExceptionsResponse**](MsgVpnAclProfilePublishExceptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_subscribe_exception**
> MsgVpnAclProfileSubscribeExceptionResponse get_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, select=select)

Gets a Subscribe Topic Exception object.

Gets a Subscribe Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
topic_syntax = 'topic_syntax_example' # str | The topicSyntax of the Subscribe Topic Exception.
subscribe_exception_topic = 'subscribe_exception_topic_example' # str | The subscribeExceptionTopic of the Subscribe Topic Exception.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Subscribe Topic Exception object.
    api_response = api_instance.get_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->get_msg_vpn_acl_profile_subscribe_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **topic_syntax** | **str**| The topicSyntax of the Subscribe Topic Exception. | 
 **subscribe_exception_topic** | **str**| The subscribeExceptionTopic of the Subscribe Topic Exception. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileSubscribeExceptionResponse**](MsgVpnAclProfileSubscribeExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_subscribe_exceptions**
> MsgVpnAclProfileSubscribeExceptionsResponse get_msg_vpn_acl_profile_subscribe_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Subscribe Topic Exception objects.

Gets a list of Subscribe Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Subscribe Topic Exception objects.
    api_response = api_instance.get_msg_vpn_acl_profile_subscribe_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->get_msg_vpn_acl_profile_subscribe_exceptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileSubscribeExceptionsResponse**](MsgVpnAclProfileSubscribeExceptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profiles**
> MsgVpnAclProfilesResponse get_msg_vpn_acl_profiles(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of ACL Profile objects.

Gets a list of ACL Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of ACL Profile objects.
    api_response = api_instance.get_msg_vpn_acl_profiles(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->get_msg_vpn_acl_profiles: %s\n" % e
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

[**MsgVpnAclProfilesResponse**](MsgVpnAclProfilesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_acl_profile**
> MsgVpnAclProfileResponse replace_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, select=select)

Replaces an ACL Profile object.

Replaces an ACL Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
body = semp_client.MsgVpnAclProfile() # MsgVpnAclProfile | The ACL Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces an ACL Profile object.
    api_response = api_instance.replace_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->replace_msg_vpn_acl_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **body** | [**MsgVpnAclProfile**](MsgVpnAclProfile.md)| The ACL Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileResponse**](MsgVpnAclProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_acl_profile**
> MsgVpnAclProfileResponse update_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, select=select)

Updates an ACL Profile object.

Updates an ACL Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AclProfileApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
body = semp_client.MsgVpnAclProfile() # MsgVpnAclProfile | The ACL Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates an ACL Profile object.
    api_response = api_instance.update_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AclProfileApi->update_msg_vpn_acl_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **body** | [**MsgVpnAclProfile**](MsgVpnAclProfile.md)| The ACL Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileResponse**](MsgVpnAclProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

