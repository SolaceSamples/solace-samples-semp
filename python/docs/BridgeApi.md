# semp_client.BridgeApi

All URIs are relative to *http://localhost/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn_bridge**](BridgeApi.md#create_msg_vpn_bridge) | **POST** /msgVpns/{msgVpnName}/bridges | Creates a Bridge object.
[**create_msg_vpn_bridge_remote_msg_vpn**](BridgeApi.md#create_msg_vpn_bridge_remote_msg_vpn) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Creates a Remote Message VPN object.
[**create_msg_vpn_bridge_remote_subscription**](BridgeApi.md#create_msg_vpn_bridge_remote_subscription) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Creates a Remote Subscription object.
[**create_msg_vpn_bridge_tls_trusted_common_name**](BridgeApi.md#create_msg_vpn_bridge_tls_trusted_common_name) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Creates a Trusted Common Name object.
[**delete_msg_vpn_bridge**](BridgeApi.md#delete_msg_vpn_bridge) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Deletes a Bridge object.
[**delete_msg_vpn_bridge_remote_msg_vpn**](BridgeApi.md#delete_msg_vpn_bridge_remote_msg_vpn) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Deletes a Remote Message VPN object.
[**delete_msg_vpn_bridge_remote_subscription**](BridgeApi.md#delete_msg_vpn_bridge_remote_subscription) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Deletes a Remote Subscription object.
[**delete_msg_vpn_bridge_tls_trusted_common_name**](BridgeApi.md#delete_msg_vpn_bridge_tls_trusted_common_name) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Deletes a Trusted Common Name object.
[**get_msg_vpn_bridge**](BridgeApi.md#get_msg_vpn_bridge) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Gets a Bridge object.
[**get_msg_vpn_bridge_remote_msg_vpn**](BridgeApi.md#get_msg_vpn_bridge_remote_msg_vpn) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Gets a Remote Message VPN object.
[**get_msg_vpn_bridge_remote_msg_vpns**](BridgeApi.md#get_msg_vpn_bridge_remote_msg_vpns) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Gets a list of Remote Message VPN objects.
[**get_msg_vpn_bridge_remote_subscription**](BridgeApi.md#get_msg_vpn_bridge_remote_subscription) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Gets a Remote Subscription object.
[**get_msg_vpn_bridge_remote_subscriptions**](BridgeApi.md#get_msg_vpn_bridge_remote_subscriptions) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Gets a list of Remote Subscription objects.
[**get_msg_vpn_bridge_tls_trusted_common_name**](BridgeApi.md#get_msg_vpn_bridge_tls_trusted_common_name) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Gets a Trusted Common Name object.
[**get_msg_vpn_bridge_tls_trusted_common_names**](BridgeApi.md#get_msg_vpn_bridge_tls_trusted_common_names) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Gets a list of Trusted Common Name objects.
[**get_msg_vpn_bridges**](BridgeApi.md#get_msg_vpn_bridges) | **GET** /msgVpns/{msgVpnName}/bridges | Gets a list of Bridge objects.
[**replace_msg_vpn_bridge**](BridgeApi.md#replace_msg_vpn_bridge) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Replaces a Bridge object.
[**replace_msg_vpn_bridge_remote_msg_vpn**](BridgeApi.md#replace_msg_vpn_bridge_remote_msg_vpn) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Replaces a Remote Message VPN object.
[**update_msg_vpn_bridge**](BridgeApi.md#update_msg_vpn_bridge) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Updates a Bridge object.
[**update_msg_vpn_bridge_remote_msg_vpn**](BridgeApi.md#update_msg_vpn_bridge_remote_msg_vpn) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Updates a Remote Message VPN object.


# **create_msg_vpn_bridge**
> MsgVpnBridgeResponse create_msg_vpn_bridge(msg_vpn_name, body, select=select)

Creates a Bridge object.

Creates a Bridge object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| msgVpnName|x||x|| remoteAuthenticationBasicPassword||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnBridge() # MsgVpnBridge | The Bridge object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Bridge object.
    api_response = api_instance.create_msg_vpn_bridge(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->create_msg_vpn_bridge: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnBridge**](MsgVpnBridge.md)| The Bridge object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_bridge_remote_msg_vpn**
> MsgVpnBridgeRemoteMsgVpnResponse create_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)

Creates a Remote Message VPN object.

Creates a Remote Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| password||||x| remoteMsgVpnInterface|x|||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
body = semp_client.MsgVpnBridgeRemoteMsgVpn() # MsgVpnBridgeRemoteMsgVpn | The Remote Message VPN object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Remote Message VPN object.
    api_response = api_instance.create_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->create_msg_vpn_bridge_remote_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **body** | [**MsgVpnBridgeRemoteMsgVpn**](MsgVpnBridgeRemoteMsgVpn.md)| The Remote Message VPN object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_bridge_remote_subscription**
> MsgVpnBridgeRemoteSubscriptionResponse create_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)

Creates a Remote Subscription object.

Creates a Remote Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| deliverAlwaysEnabled||x||| msgVpnName|x||x|| remoteSubscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
body = semp_client.MsgVpnBridgeRemoteSubscription() # MsgVpnBridgeRemoteSubscription | The Remote Subscription object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Remote Subscription object.
    api_response = api_instance.create_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->create_msg_vpn_bridge_remote_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **body** | [**MsgVpnBridgeRemoteSubscription**](MsgVpnBridgeRemoteSubscription.md)| The Remote Subscription object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteSubscriptionResponse**](MsgVpnBridgeRemoteSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_bridge_tls_trusted_common_name**
> MsgVpnBridgeTlsTrustedCommonNameResponse create_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)

Creates a Trusted Common Name object.

Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
body = semp_client.MsgVpnBridgeTlsTrustedCommonName() # MsgVpnBridgeTlsTrustedCommonName | The Trusted Common Name object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Trusted Common Name object.
    api_response = api_instance.create_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->create_msg_vpn_bridge_tls_trusted_common_name: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **body** | [**MsgVpnBridgeTlsTrustedCommonName**](MsgVpnBridgeTlsTrustedCommonName.md)| The Trusted Common Name object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeTlsTrustedCommonNameResponse**](MsgVpnBridgeTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_bridge**
> SempMetaOnlyResponse delete_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router)

Deletes a Bridge object.

Deletes a Bridge object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.

try: 
    # Deletes a Bridge object.
    api_response = api_instance.delete_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->delete_msg_vpn_bridge: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_bridge_remote_msg_vpn**
> SempMetaOnlyResponse delete_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface)

Deletes a Remote Message VPN object.

Deletes a Remote Message VPN object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_msg_vpn_name = 'remote_msg_vpn_name_example' # str | The remoteMsgVpnName of the Remote Message VPN.
remote_msg_vpn_location = 'remote_msg_vpn_location_example' # str | The remoteMsgVpnLocation of the Remote Message VPN.
remote_msg_vpn_interface = 'remote_msg_vpn_interface_example' # str | The remoteMsgVpnInterface of the Remote Message VPN.

try: 
    # Deletes a Remote Message VPN object.
    api_response = api_instance.delete_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->delete_msg_vpn_bridge_remote_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_msg_vpn_name** | **str**| The remoteMsgVpnName of the Remote Message VPN. | 
 **remote_msg_vpn_location** | **str**| The remoteMsgVpnLocation of the Remote Message VPN. | 
 **remote_msg_vpn_interface** | **str**| The remoteMsgVpnInterface of the Remote Message VPN. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_bridge_remote_subscription**
> SempMetaOnlyResponse delete_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic)

Deletes a Remote Subscription object.

Deletes a Remote Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_subscription_topic = 'remote_subscription_topic_example' # str | The remoteSubscriptionTopic of the Remote Subscription.

try: 
    # Deletes a Remote Subscription object.
    api_response = api_instance.delete_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->delete_msg_vpn_bridge_remote_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_subscription_topic** | **str**| The remoteSubscriptionTopic of the Remote Subscription. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_bridge_tls_trusted_common_name**
> SempMetaOnlyResponse delete_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name)

Deletes a Trusted Common Name object.

Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
tls_trusted_common_name = 'tls_trusted_common_name_example' # str | The tlsTrustedCommonName of the Trusted Common Name.

try: 
    # Deletes a Trusted Common Name object.
    api_response = api_instance.delete_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->delete_msg_vpn_bridge_tls_trusted_common_name: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **tls_trusted_common_name** | **str**| The tlsTrustedCommonName of the Trusted Common Name. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge**
> MsgVpnBridgeResponse get_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, select=select)

Gets a Bridge object.

Gets a Bridge object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Bridge object.
    api_response = api_instance.get_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->get_msg_vpn_bridge: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_remote_msg_vpn**
> MsgVpnBridgeRemoteMsgVpnResponse get_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, select=select)

Gets a Remote Message VPN object.

Gets a Remote Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_msg_vpn_name = 'remote_msg_vpn_name_example' # str | The remoteMsgVpnName of the Remote Message VPN.
remote_msg_vpn_location = 'remote_msg_vpn_location_example' # str | The remoteMsgVpnLocation of the Remote Message VPN.
remote_msg_vpn_interface = 'remote_msg_vpn_interface_example' # str | The remoteMsgVpnInterface of the Remote Message VPN.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Remote Message VPN object.
    api_response = api_instance.get_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->get_msg_vpn_bridge_remote_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_msg_vpn_name** | **str**| The remoteMsgVpnName of the Remote Message VPN. | 
 **remote_msg_vpn_location** | **str**| The remoteMsgVpnLocation of the Remote Message VPN. | 
 **remote_msg_vpn_interface** | **str**| The remoteMsgVpnInterface of the Remote Message VPN. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_remote_msg_vpns**
> MsgVpnBridgeRemoteMsgVpnsResponse get_msg_vpn_bridge_remote_msg_vpns(msg_vpn_name, bridge_name, bridge_virtual_router, where=where, select=select)

Gets a list of Remote Message VPN objects.

Gets a list of Remote Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Remote Message VPN objects.
    api_response = api_instance.get_msg_vpn_bridge_remote_msg_vpns(msg_vpn_name, bridge_name, bridge_virtual_router, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->get_msg_vpn_bridge_remote_msg_vpns: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteMsgVpnsResponse**](MsgVpnBridgeRemoteMsgVpnsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_remote_subscription**
> MsgVpnBridgeRemoteSubscriptionResponse get_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, select=select)

Gets a Remote Subscription object.

Gets a Remote Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_subscription_topic = 'remote_subscription_topic_example' # str | The remoteSubscriptionTopic of the Remote Subscription.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Remote Subscription object.
    api_response = api_instance.get_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->get_msg_vpn_bridge_remote_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_subscription_topic** | **str**| The remoteSubscriptionTopic of the Remote Subscription. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteSubscriptionResponse**](MsgVpnBridgeRemoteSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_remote_subscriptions**
> MsgVpnBridgeRemoteSubscriptionsResponse get_msg_vpn_bridge_remote_subscriptions(msg_vpn_name, bridge_name, bridge_virtual_router, count=count, cursor=cursor, where=where, select=select)

Gets a list of Remote Subscription objects.

Gets a list of Remote Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Remote Subscription objects.
    api_response = api_instance.get_msg_vpn_bridge_remote_subscriptions(msg_vpn_name, bridge_name, bridge_virtual_router, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->get_msg_vpn_bridge_remote_subscriptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteSubscriptionsResponse**](MsgVpnBridgeRemoteSubscriptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_tls_trusted_common_name**
> MsgVpnBridgeTlsTrustedCommonNameResponse get_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, select=select)

Gets a Trusted Common Name object.

Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
tls_trusted_common_name = 'tls_trusted_common_name_example' # str | The tlsTrustedCommonName of the Trusted Common Name.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Trusted Common Name object.
    api_response = api_instance.get_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->get_msg_vpn_bridge_tls_trusted_common_name: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **tls_trusted_common_name** | **str**| The tlsTrustedCommonName of the Trusted Common Name. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeTlsTrustedCommonNameResponse**](MsgVpnBridgeTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_tls_trusted_common_names**
> MsgVpnBridgeTlsTrustedCommonNamesResponse get_msg_vpn_bridge_tls_trusted_common_names(msg_vpn_name, bridge_name, bridge_virtual_router, where=where, select=select)

Gets a list of Trusted Common Name objects.

Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Trusted Common Name objects.
    api_response = api_instance.get_msg_vpn_bridge_tls_trusted_common_names(msg_vpn_name, bridge_name, bridge_virtual_router, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->get_msg_vpn_bridge_tls_trusted_common_names: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeTlsTrustedCommonNamesResponse**](MsgVpnBridgeTlsTrustedCommonNamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridges**
> MsgVpnBridgesResponse get_msg_vpn_bridges(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Bridge objects.

Gets a list of Bridge objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Bridge objects.
    api_response = api_instance.get_msg_vpn_bridges(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->get_msg_vpn_bridges: %s\n" % e
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

[**MsgVpnBridgesResponse**](MsgVpnBridgesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_bridge**
> MsgVpnBridgeResponse replace_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)

Replaces a Bridge object.

Replaces a Bridge object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
body = semp_client.MsgVpnBridge() # MsgVpnBridge | The Bridge object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Bridge object.
    api_response = api_instance.replace_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->replace_msg_vpn_bridge: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **body** | [**MsgVpnBridge**](MsgVpnBridge.md)| The Bridge object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_bridge_remote_msg_vpn**
> MsgVpnBridgeRemoteMsgVpnResponse replace_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, select=select)

Replaces a Remote Message VPN object.

Replaces a Remote Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_msg_vpn_name = 'remote_msg_vpn_name_example' # str | The remoteMsgVpnName of the Remote Message VPN.
remote_msg_vpn_location = 'remote_msg_vpn_location_example' # str | The remoteMsgVpnLocation of the Remote Message VPN.
remote_msg_vpn_interface = 'remote_msg_vpn_interface_example' # str | The remoteMsgVpnInterface of the Remote Message VPN.
body = semp_client.MsgVpnBridgeRemoteMsgVpn() # MsgVpnBridgeRemoteMsgVpn | The Remote Message VPN object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Remote Message VPN object.
    api_response = api_instance.replace_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->replace_msg_vpn_bridge_remote_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_msg_vpn_name** | **str**| The remoteMsgVpnName of the Remote Message VPN. | 
 **remote_msg_vpn_location** | **str**| The remoteMsgVpnLocation of the Remote Message VPN. | 
 **remote_msg_vpn_interface** | **str**| The remoteMsgVpnInterface of the Remote Message VPN. | 
 **body** | [**MsgVpnBridgeRemoteMsgVpn**](MsgVpnBridgeRemoteMsgVpn.md)| The Remote Message VPN object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_bridge**
> MsgVpnBridgeResponse update_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)

Updates a Bridge object.

Updates a Bridge object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
body = semp_client.MsgVpnBridge() # MsgVpnBridge | The Bridge object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Bridge object.
    api_response = api_instance.update_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->update_msg_vpn_bridge: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **body** | [**MsgVpnBridge**](MsgVpnBridge.md)| The Bridge object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_bridge_remote_msg_vpn**
> MsgVpnBridgeRemoteMsgVpnResponse update_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, select=select)

Updates a Remote Message VPN object.

Updates a Remote Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.BridgeApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_msg_vpn_name = 'remote_msg_vpn_name_example' # str | The remoteMsgVpnName of the Remote Message VPN.
remote_msg_vpn_location = 'remote_msg_vpn_location_example' # str | The remoteMsgVpnLocation of the Remote Message VPN.
remote_msg_vpn_interface = 'remote_msg_vpn_interface_example' # str | The remoteMsgVpnInterface of the Remote Message VPN.
body = semp_client.MsgVpnBridgeRemoteMsgVpn() # MsgVpnBridgeRemoteMsgVpn | The Remote Message VPN object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Remote Message VPN object.
    api_response = api_instance.update_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling BridgeApi->update_msg_vpn_bridge_remote_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_msg_vpn_name** | **str**| The remoteMsgVpnName of the Remote Message VPN. | 
 **remote_msg_vpn_location** | **str**| The remoteMsgVpnLocation of the Remote Message VPN. | 
 **remote_msg_vpn_interface** | **str**| The remoteMsgVpnInterface of the Remote Message VPN. | 
 **body** | [**MsgVpnBridgeRemoteMsgVpn**](MsgVpnBridgeRemoteMsgVpn.md)| The Remote Message VPN object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

