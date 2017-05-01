# semp_client.AuthorizationGroupApi

All URIs are relative to *http://localhost/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn_authorization_group**](AuthorizationGroupApi.md#create_msg_vpn_authorization_group) | **POST** /msgVpns/{msgVpnName}/authorizationGroups | Creates an LDAP Authorization Group object.
[**delete_msg_vpn_authorization_group**](AuthorizationGroupApi.md#delete_msg_vpn_authorization_group) | **DELETE** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Deletes an LDAP Authorization Group object.
[**get_msg_vpn_authorization_group**](AuthorizationGroupApi.md#get_msg_vpn_authorization_group) | **GET** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Gets an LDAP Authorization Group object.
[**get_msg_vpn_authorization_groups**](AuthorizationGroupApi.md#get_msg_vpn_authorization_groups) | **GET** /msgVpns/{msgVpnName}/authorizationGroups | Gets a list of LDAP Authorization Group objects.
[**replace_msg_vpn_authorization_group**](AuthorizationGroupApi.md#replace_msg_vpn_authorization_group) | **PUT** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Replaces an LDAP Authorization Group object.
[**update_msg_vpn_authorization_group**](AuthorizationGroupApi.md#update_msg_vpn_authorization_group) | **PATCH** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Updates an LDAP Authorization Group object.


# **create_msg_vpn_authorization_group**
> MsgVpnAuthorizationGroupResponse create_msg_vpn_authorization_group(msg_vpn_name, body, select=select)

Creates an LDAP Authorization Group object.

Creates an LDAP Authorization Group object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authorizationGroupName|x|x||| msgVpnName|x||x|| orderAfterAuthorizationGroupName||||x| orderBeforeAuthorizationGroupName||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AuthorizationGroupApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnAuthorizationGroup() # MsgVpnAuthorizationGroup | The LDAP Authorization Group object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates an LDAP Authorization Group object.
    api_response = api_instance.create_msg_vpn_authorization_group(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AuthorizationGroupApi->create_msg_vpn_authorization_group: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnAuthorizationGroup**](MsgVpnAuthorizationGroup.md)| The LDAP Authorization Group object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAuthorizationGroupResponse**](MsgVpnAuthorizationGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_authorization_group**
> SempMetaOnlyResponse delete_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name)

Deletes an LDAP Authorization Group object.

Deletes an LDAP Authorization Group object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AuthorizationGroupApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
authorization_group_name = 'authorization_group_name_example' # str | The authorizationGroupName of the LDAP Authorization Group.

try: 
    # Deletes an LDAP Authorization Group object.
    api_response = api_instance.delete_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AuthorizationGroupApi->delete_msg_vpn_authorization_group: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **authorization_group_name** | **str**| The authorizationGroupName of the LDAP Authorization Group. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_authorization_group**
> MsgVpnAuthorizationGroupResponse get_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, select=select)

Gets an LDAP Authorization Group object.

Gets an LDAP Authorization Group object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AuthorizationGroupApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
authorization_group_name = 'authorization_group_name_example' # str | The authorizationGroupName of the LDAP Authorization Group.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets an LDAP Authorization Group object.
    api_response = api_instance.get_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AuthorizationGroupApi->get_msg_vpn_authorization_group: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **authorization_group_name** | **str**| The authorizationGroupName of the LDAP Authorization Group. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAuthorizationGroupResponse**](MsgVpnAuthorizationGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_authorization_groups**
> MsgVpnAuthorizationGroupsResponse get_msg_vpn_authorization_groups(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of LDAP Authorization Group objects.

Gets a list of LDAP Authorization Group objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AuthorizationGroupApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of LDAP Authorization Group objects.
    api_response = api_instance.get_msg_vpn_authorization_groups(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AuthorizationGroupApi->get_msg_vpn_authorization_groups: %s\n" % e
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

[**MsgVpnAuthorizationGroupsResponse**](MsgVpnAuthorizationGroupsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_authorization_group**
> MsgVpnAuthorizationGroupResponse replace_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, select=select)

Replaces an LDAP Authorization Group object.

Replaces an LDAP Authorization Group object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AuthorizationGroupApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
authorization_group_name = 'authorization_group_name_example' # str | The authorizationGroupName of the LDAP Authorization Group.
body = semp_client.MsgVpnAuthorizationGroup() # MsgVpnAuthorizationGroup | The LDAP Authorization Group object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces an LDAP Authorization Group object.
    api_response = api_instance.replace_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AuthorizationGroupApi->replace_msg_vpn_authorization_group: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **authorization_group_name** | **str**| The authorizationGroupName of the LDAP Authorization Group. | 
 **body** | [**MsgVpnAuthorizationGroup**](MsgVpnAuthorizationGroup.md)| The LDAP Authorization Group object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAuthorizationGroupResponse**](MsgVpnAuthorizationGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_authorization_group**
> MsgVpnAuthorizationGroupResponse update_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, select=select)

Updates an LDAP Authorization Group object.

Updates an LDAP Authorization Group object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.AuthorizationGroupApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
authorization_group_name = 'authorization_group_name_example' # str | The authorizationGroupName of the LDAP Authorization Group.
body = semp_client.MsgVpnAuthorizationGroup() # MsgVpnAuthorizationGroup | The LDAP Authorization Group object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates an LDAP Authorization Group object.
    api_response = api_instance.update_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling AuthorizationGroupApi->update_msg_vpn_authorization_group: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **authorization_group_name** | **str**| The authorizationGroupName of the LDAP Authorization Group. | 
 **body** | [**MsgVpnAuthorizationGroup**](MsgVpnAuthorizationGroup.md)| The LDAP Authorization Group object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAuthorizationGroupResponse**](MsgVpnAuthorizationGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

