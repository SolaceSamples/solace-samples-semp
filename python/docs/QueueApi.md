# semp_client.QueueApi

All URIs are relative to *http://localhost/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn_queue**](QueueApi.md#create_msg_vpn_queue) | **POST** /msgVpns/{msgVpnName}/queues | Creates a Queue object.
[**create_msg_vpn_queue_subscription**](QueueApi.md#create_msg_vpn_queue_subscription) | **POST** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions | Creates a Queue Subscription object.
[**delete_msg_vpn_queue**](QueueApi.md#delete_msg_vpn_queue) | **DELETE** /msgVpns/{msgVpnName}/queues/{queueName} | Deletes a Queue object.
[**delete_msg_vpn_queue_subscription**](QueueApi.md#delete_msg_vpn_queue_subscription) | **DELETE** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic} | Deletes a Queue Subscription object.
[**get_msg_vpn_queue**](QueueApi.md#get_msg_vpn_queue) | **GET** /msgVpns/{msgVpnName}/queues/{queueName} | Gets a Queue object.
[**get_msg_vpn_queue_subscription**](QueueApi.md#get_msg_vpn_queue_subscription) | **GET** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic} | Gets a Queue Subscription object.
[**get_msg_vpn_queue_subscriptions**](QueueApi.md#get_msg_vpn_queue_subscriptions) | **GET** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions | Gets a list of Queue Subscription objects.
[**get_msg_vpn_queues**](QueueApi.md#get_msg_vpn_queues) | **GET** /msgVpns/{msgVpnName}/queues | Gets a list of Queue objects.
[**replace_msg_vpn_queue**](QueueApi.md#replace_msg_vpn_queue) | **PUT** /msgVpns/{msgVpnName}/queues/{queueName} | Replaces a Queue object.
[**update_msg_vpn_queue**](QueueApi.md#update_msg_vpn_queue) | **PATCH** /msgVpns/{msgVpnName}/queues/{queueName} | Updates a Queue object.


# **create_msg_vpn_queue**
> MsgVpnQueueResponse create_msg_vpn_queue(msg_vpn_name, body, select=select)

Creates a Queue object.

Creates a Queue object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.QueueApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnQueue() # MsgVpnQueue | The Queue object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Queue object.
    api_response = api_instance.create_msg_vpn_queue(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling QueueApi->create_msg_vpn_queue: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnQueue**](MsgVpnQueue.md)| The Queue object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_queue_subscription**
> MsgVpnQueueSubscriptionResponse create_msg_vpn_queue_subscription(msg_vpn_name, queue_name, body, select=select)

Creates a Queue Subscription object.

Creates a Queue Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.QueueApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
body = semp_client.MsgVpnQueueSubscription() # MsgVpnQueueSubscription | The Queue Subscription object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Queue Subscription object.
    api_response = api_instance.create_msg_vpn_queue_subscription(msg_vpn_name, queue_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling QueueApi->create_msg_vpn_queue_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **body** | [**MsgVpnQueueSubscription**](MsgVpnQueueSubscription.md)| The Queue Subscription object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueSubscriptionResponse**](MsgVpnQueueSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_queue**
> SempMetaOnlyResponse delete_msg_vpn_queue(msg_vpn_name, queue_name)

Deletes a Queue object.

Deletes a Queue object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.QueueApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.

try: 
    # Deletes a Queue object.
    api_response = api_instance.delete_msg_vpn_queue(msg_vpn_name, queue_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling QueueApi->delete_msg_vpn_queue: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_queue_subscription**
> SempMetaOnlyResponse delete_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic)

Deletes a Queue Subscription object.

Deletes a Queue Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.QueueApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the Queue Subscription.

try: 
    # Deletes a Queue Subscription object.
    api_response = api_instance.delete_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling QueueApi->delete_msg_vpn_queue_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **subscription_topic** | **str**| The subscriptionTopic of the Queue Subscription. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_queue**
> MsgVpnQueueResponse get_msg_vpn_queue(msg_vpn_name, queue_name, select=select)

Gets a Queue object.

Gets a Queue object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.QueueApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Queue object.
    api_response = api_instance.get_msg_vpn_queue(msg_vpn_name, queue_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling QueueApi->get_msg_vpn_queue: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_queue_subscription**
> MsgVpnQueueSubscriptionResponse get_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic, select=select)

Gets a Queue Subscription object.

Gets a Queue Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.QueueApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the Queue Subscription.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Queue Subscription object.
    api_response = api_instance.get_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling QueueApi->get_msg_vpn_queue_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **subscription_topic** | **str**| The subscriptionTopic of the Queue Subscription. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueSubscriptionResponse**](MsgVpnQueueSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_queue_subscriptions**
> MsgVpnQueueSubscriptionsResponse get_msg_vpn_queue_subscriptions(msg_vpn_name, queue_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Queue Subscription objects.

Gets a list of Queue Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.QueueApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Queue Subscription objects.
    api_response = api_instance.get_msg_vpn_queue_subscriptions(msg_vpn_name, queue_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling QueueApi->get_msg_vpn_queue_subscriptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueSubscriptionsResponse**](MsgVpnQueueSubscriptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_queues**
> MsgVpnQueuesResponse get_msg_vpn_queues(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Queue objects.

Gets a list of Queue objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.QueueApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Queue objects.
    api_response = api_instance.get_msg_vpn_queues(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling QueueApi->get_msg_vpn_queues: %s\n" % e
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

[**MsgVpnQueuesResponse**](MsgVpnQueuesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_queue**
> MsgVpnQueueResponse replace_msg_vpn_queue(msg_vpn_name, queue_name, body, select=select)

Replaces a Queue object.

Replaces a Queue object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.QueueApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
body = semp_client.MsgVpnQueue() # MsgVpnQueue | The Queue object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Queue object.
    api_response = api_instance.replace_msg_vpn_queue(msg_vpn_name, queue_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling QueueApi->replace_msg_vpn_queue: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **body** | [**MsgVpnQueue**](MsgVpnQueue.md)| The Queue object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_queue**
> MsgVpnQueueResponse update_msg_vpn_queue(msg_vpn_name, queue_name, body, select=select)

Updates a Queue object.

Updates a Queue object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

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
api_instance = semp_client.QueueApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
body = semp_client.MsgVpnQueue() # MsgVpnQueue | The Queue object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Queue object.
    api_response = api_instance.update_msg_vpn_queue(msg_vpn_name, queue_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling QueueApi->update_msg_vpn_queue: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **body** | [**MsgVpnQueue**](MsgVpnQueue.md)| The Queue object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

