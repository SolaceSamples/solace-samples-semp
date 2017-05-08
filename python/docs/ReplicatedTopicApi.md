# semp_client.ReplicatedTopicApi

All URIs are relative to *http://localhost/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn_replicated_topic**](ReplicatedTopicApi.md#create_msg_vpn_replicated_topic) | **POST** /msgVpns/{msgVpnName}/replicatedTopics | Creates a Replicated Topic object.
[**delete_msg_vpn_replicated_topic**](ReplicatedTopicApi.md#delete_msg_vpn_replicated_topic) | **DELETE** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Deletes a Replicated Topic object.
[**get_msg_vpn_replicated_topic**](ReplicatedTopicApi.md#get_msg_vpn_replicated_topic) | **GET** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Gets a Replicated Topic object.
[**get_msg_vpn_replicated_topics**](ReplicatedTopicApi.md#get_msg_vpn_replicated_topics) | **GET** /msgVpns/{msgVpnName}/replicatedTopics | Gets a list of Replicated Topic objects.
[**replace_msg_vpn_replicated_topic**](ReplicatedTopicApi.md#replace_msg_vpn_replicated_topic) | **PUT** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Replaces a Replicated Topic object.
[**update_msg_vpn_replicated_topic**](ReplicatedTopicApi.md#update_msg_vpn_replicated_topic) | **PATCH** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Updates a Replicated Topic object.


# **create_msg_vpn_replicated_topic**
> MsgVpnReplicatedTopicResponse create_msg_vpn_replicated_topic(msg_vpn_name, body, select=select)

Creates a Replicated Topic object.

Creates a Replicated Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.ReplicatedTopicApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnReplicatedTopic() # MsgVpnReplicatedTopic | The Replicated Topic object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Replicated Topic object.
    api_response = api_instance.create_msg_vpn_replicated_topic(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ReplicatedTopicApi->create_msg_vpn_replicated_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnReplicatedTopic**](MsgVpnReplicatedTopic.md)| The Replicated Topic object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_replicated_topic**
> SempMetaOnlyResponse delete_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic)

Deletes a Replicated Topic object.

Deletes a Replicated Topic object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.ReplicatedTopicApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
replicated_topic = 'replicated_topic_example' # str | The replicatedTopic of the Replicated Topic.

try: 
    # Deletes a Replicated Topic object.
    api_response = api_instance.delete_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ReplicatedTopicApi->delete_msg_vpn_replicated_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **replicated_topic** | **str**| The replicatedTopic of the Replicated Topic. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_replicated_topic**
> MsgVpnReplicatedTopicResponse get_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, select=select)

Gets a Replicated Topic object.

Gets a Replicated Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicatedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.ReplicatedTopicApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
replicated_topic = 'replicated_topic_example' # str | The replicatedTopic of the Replicated Topic.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Replicated Topic object.
    api_response = api_instance.get_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ReplicatedTopicApi->get_msg_vpn_replicated_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **replicated_topic** | **str**| The replicatedTopic of the Replicated Topic. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_replicated_topics**
> MsgVpnReplicatedTopicsResponse get_msg_vpn_replicated_topics(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Replicated Topic objects.

Gets a list of Replicated Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicatedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.ReplicatedTopicApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Replicated Topic objects.
    api_response = api_instance.get_msg_vpn_replicated_topics(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ReplicatedTopicApi->get_msg_vpn_replicated_topics: %s\n" % e
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

[**MsgVpnReplicatedTopicsResponse**](MsgVpnReplicatedTopicsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_replicated_topic**
> MsgVpnReplicatedTopicResponse replace_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, body, select=select)

Replaces a Replicated Topic object.

Replaces a Replicated Topic object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.ReplicatedTopicApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
replicated_topic = 'replicated_topic_example' # str | The replicatedTopic of the Replicated Topic.
body = semp_client.MsgVpnReplicatedTopic() # MsgVpnReplicatedTopic | The Replicated Topic object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Replicated Topic object.
    api_response = api_instance.replace_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ReplicatedTopicApi->replace_msg_vpn_replicated_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **replicated_topic** | **str**| The replicatedTopic of the Replicated Topic. | 
 **body** | [**MsgVpnReplicatedTopic**](MsgVpnReplicatedTopic.md)| The Replicated Topic object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_replicated_topic**
> MsgVpnReplicatedTopicResponse update_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, body, select=select)

Updates a Replicated Topic object.

Updates a Replicated Topic object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.ReplicatedTopicApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
replicated_topic = 'replicated_topic_example' # str | The replicatedTopic of the Replicated Topic.
body = semp_client.MsgVpnReplicatedTopic() # MsgVpnReplicatedTopic | The Replicated Topic object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Replicated Topic object.
    api_response = api_instance.update_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling ReplicatedTopicApi->update_msg_vpn_replicated_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **replicated_topic** | **str**| The replicatedTopic of the Replicated Topic. | 
 **body** | [**MsgVpnReplicatedTopic**](MsgVpnReplicatedTopic.md)| The Replicated Topic object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

