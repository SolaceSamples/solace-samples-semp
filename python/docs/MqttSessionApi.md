# semp_client.MqttSessionApi

All URIs are relative to *http://localhost/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn_mqtt_session**](MqttSessionApi.md#create_msg_vpn_mqtt_session) | **POST** /msgVpns/{msgVpnName}/mqttSessions | Creates an MQTT Session object.
[**create_msg_vpn_mqtt_session_subscription**](MqttSessionApi.md#create_msg_vpn_mqtt_session_subscription) | **POST** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions | Creates an MQTT Session Subscription object.
[**delete_msg_vpn_mqtt_session**](MqttSessionApi.md#delete_msg_vpn_mqtt_session) | **DELETE** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Deletes an MQTT Session object.
[**delete_msg_vpn_mqtt_session_subscription**](MqttSessionApi.md#delete_msg_vpn_mqtt_session_subscription) | **DELETE** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Deletes an MQTT Session Subscription object.
[**get_msg_vpn_mqtt_session**](MqttSessionApi.md#get_msg_vpn_mqtt_session) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Gets an MQTT Session object.
[**get_msg_vpn_mqtt_session_subscription**](MqttSessionApi.md#get_msg_vpn_mqtt_session_subscription) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Gets an MQTT Session Subscription object.
[**get_msg_vpn_mqtt_session_subscriptions**](MqttSessionApi.md#get_msg_vpn_mqtt_session_subscriptions) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions | Gets a list of MQTT Session Subscription objects.
[**get_msg_vpn_mqtt_sessions**](MqttSessionApi.md#get_msg_vpn_mqtt_sessions) | **GET** /msgVpns/{msgVpnName}/mqttSessions | Gets a list of MQTT Session objects.
[**replace_msg_vpn_mqtt_session**](MqttSessionApi.md#replace_msg_vpn_mqtt_session) | **PUT** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Replaces an MQTT Session object.
[**replace_msg_vpn_mqtt_session_subscription**](MqttSessionApi.md#replace_msg_vpn_mqtt_session_subscription) | **PUT** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Replaces an MQTT Session Subscription object.
[**update_msg_vpn_mqtt_session**](MqttSessionApi.md#update_msg_vpn_mqtt_session) | **PATCH** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Updates an MQTT Session object.
[**update_msg_vpn_mqtt_session_subscription**](MqttSessionApi.md#update_msg_vpn_mqtt_session_subscription) | **PATCH** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Updates an MQTT Session Subscription object.


# **create_msg_vpn_mqtt_session**
> MsgVpnMqttSessionResponse create_msg_vpn_mqtt_session(msg_vpn_name, body, select=select)

Creates an MQTT Session object.

Creates an MQTT Session object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnMqttSession() # MsgVpnMqttSession | The MQTT Session object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates an MQTT Session object.
    api_response = api_instance.create_msg_vpn_mqtt_session(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->create_msg_vpn_mqtt_session: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnMqttSession**](MsgVpnMqttSession.md)| The MQTT Session object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionResponse**](MsgVpnMqttSessionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_mqtt_session_subscription**
> MsgVpnMqttSessionSubscriptionResponse create_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)

Creates an MQTT Session Subscription object.

Creates an MQTT Session Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x||x|| mqttSessionVirtualRouter|x||x|| msgVpnName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
body = semp_client.MsgVpnMqttSessionSubscription() # MsgVpnMqttSessionSubscription | The MQTT Session Subscription object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates an MQTT Session Subscription object.
    api_response = api_instance.create_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->create_msg_vpn_mqtt_session_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **body** | [**MsgVpnMqttSessionSubscription**](MsgVpnMqttSessionSubscription.md)| The MQTT Session Subscription object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionSubscriptionResponse**](MsgVpnMqttSessionSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_mqtt_session**
> SempMetaOnlyResponse delete_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router)

Deletes an MQTT Session object.

Deletes an MQTT Session object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.

try: 
    # Deletes an MQTT Session object.
    api_response = api_instance.delete_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->delete_msg_vpn_mqtt_session: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_mqtt_session_subscription**
> SempMetaOnlyResponse delete_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic)

Deletes an MQTT Session Subscription object.

Deletes an MQTT Session Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the MQTT Session Subscription.

try: 
    # Deletes an MQTT Session Subscription object.
    api_response = api_instance.delete_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->delete_msg_vpn_mqtt_session_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **subscription_topic** | **str**| The subscriptionTopic of the MQTT Session Subscription. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_mqtt_session**
> MsgVpnMqttSessionResponse get_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, select=select)

Gets an MQTT Session object.

Gets an MQTT Session object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets an MQTT Session object.
    api_response = api_instance.get_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->get_msg_vpn_mqtt_session: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionResponse**](MsgVpnMqttSessionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_mqtt_session_subscription**
> MsgVpnMqttSessionSubscriptionResponse get_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, select=select)

Gets an MQTT Session Subscription object.

Gets an MQTT Session Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the MQTT Session Subscription.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets an MQTT Session Subscription object.
    api_response = api_instance.get_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->get_msg_vpn_mqtt_session_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **subscription_topic** | **str**| The subscriptionTopic of the MQTT Session Subscription. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionSubscriptionResponse**](MsgVpnMqttSessionSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_mqtt_session_subscriptions**
> MsgVpnMqttSessionSubscriptionsResponse get_msg_vpn_mqtt_session_subscriptions(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, count=count, cursor=cursor, where=where, select=select)

Gets a list of MQTT Session Subscription objects.

Gets a list of MQTT Session Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of MQTT Session Subscription objects.
    api_response = api_instance.get_msg_vpn_mqtt_session_subscriptions(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->get_msg_vpn_mqtt_session_subscriptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionSubscriptionsResponse**](MsgVpnMqttSessionSubscriptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_mqtt_sessions**
> MsgVpnMqttSessionsResponse get_msg_vpn_mqtt_sessions(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of MQTT Session objects.

Gets a list of MQTT Session objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of MQTT Session objects.
    api_response = api_instance.get_msg_vpn_mqtt_sessions(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->get_msg_vpn_mqtt_sessions: %s\n" % e
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

[**MsgVpnMqttSessionsResponse**](MsgVpnMqttSessionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_mqtt_session**
> MsgVpnMqttSessionResponse replace_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)

Replaces an MQTT Session object.

Replaces an MQTT Session object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| owner||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
body = semp_client.MsgVpnMqttSession() # MsgVpnMqttSession | The MQTT Session object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces an MQTT Session object.
    api_response = api_instance.replace_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->replace_msg_vpn_mqtt_session: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **body** | [**MsgVpnMqttSession**](MsgVpnMqttSession.md)| The MQTT Session object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionResponse**](MsgVpnMqttSessionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_mqtt_session_subscription**
> MsgVpnMqttSessionSubscriptionResponse replace_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, select=select)

Replaces an MQTT Session Subscription object.

Replaces an MQTT Session Subscription object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the MQTT Session Subscription.
body = semp_client.MsgVpnMqttSessionSubscription() # MsgVpnMqttSessionSubscription | The MQTT Session Subscription object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces an MQTT Session Subscription object.
    api_response = api_instance.replace_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->replace_msg_vpn_mqtt_session_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **subscription_topic** | **str**| The subscriptionTopic of the MQTT Session Subscription. | 
 **body** | [**MsgVpnMqttSessionSubscription**](MsgVpnMqttSessionSubscription.md)| The MQTT Session Subscription object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionSubscriptionResponse**](MsgVpnMqttSessionSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_mqtt_session**
> MsgVpnMqttSessionResponse update_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)

Updates an MQTT Session object.

Updates an MQTT Session object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| owner||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
body = semp_client.MsgVpnMqttSession() # MsgVpnMqttSession | The MQTT Session object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates an MQTT Session object.
    api_response = api_instance.update_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->update_msg_vpn_mqtt_session: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **body** | [**MsgVpnMqttSession**](MsgVpnMqttSession.md)| The MQTT Session object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionResponse**](MsgVpnMqttSessionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_mqtt_session_subscription**
> MsgVpnMqttSessionSubscriptionResponse update_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, select=select)

Updates an MQTT Session Subscription object.

Updates an MQTT Session Subscription object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.MqttSessionApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the MQTT Session Subscription.
body = semp_client.MsgVpnMqttSessionSubscription() # MsgVpnMqttSessionSubscription | The MQTT Session Subscription object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates an MQTT Session Subscription object.
    api_response = api_instance.update_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MqttSessionApi->update_msg_vpn_mqtt_session_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **subscription_topic** | **str**| The subscriptionTopic of the MQTT Session Subscription. | 
 **body** | [**MsgVpnMqttSessionSubscription**](MsgVpnMqttSessionSubscription.md)| The MQTT Session Subscription object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionSubscriptionResponse**](MsgVpnMqttSessionSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

