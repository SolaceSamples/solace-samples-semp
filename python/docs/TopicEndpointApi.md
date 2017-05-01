# semp_client.TopicEndpointApi

All URIs are relative to *http://localhost/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn_topic_endpoint**](TopicEndpointApi.md#create_msg_vpn_topic_endpoint) | **POST** /msgVpns/{msgVpnName}/topicEndpoints | Creates a Topic Endpoint object.
[**delete_msg_vpn_topic_endpoint**](TopicEndpointApi.md#delete_msg_vpn_topic_endpoint) | **DELETE** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Deletes a Topic Endpoint object.
[**get_msg_vpn_topic_endpoint**](TopicEndpointApi.md#get_msg_vpn_topic_endpoint) | **GET** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Gets a Topic Endpoint object.
[**get_msg_vpn_topic_endpoints**](TopicEndpointApi.md#get_msg_vpn_topic_endpoints) | **GET** /msgVpns/{msgVpnName}/topicEndpoints | Gets a list of Topic Endpoint objects.
[**replace_msg_vpn_topic_endpoint**](TopicEndpointApi.md#replace_msg_vpn_topic_endpoint) | **PUT** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Replaces a Topic Endpoint object.
[**update_msg_vpn_topic_endpoint**](TopicEndpointApi.md#update_msg_vpn_topic_endpoint) | **PATCH** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Updates a Topic Endpoint object.


# **create_msg_vpn_topic_endpoint**
> MsgVpnTopicEndpointResponse create_msg_vpn_topic_endpoint(msg_vpn_name, body, select=select)

Creates a Topic Endpoint object.

Creates a Topic Endpoint object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.TopicEndpointApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnTopicEndpoint() # MsgVpnTopicEndpoint | The Topic Endpoint object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Topic Endpoint object.
    api_response = api_instance.create_msg_vpn_topic_endpoint(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling TopicEndpointApi->create_msg_vpn_topic_endpoint: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnTopicEndpoint**](MsgVpnTopicEndpoint.md)| The Topic Endpoint object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_topic_endpoint**
> SempMetaOnlyResponse delete_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name)

Deletes a Topic Endpoint object.

Deletes a Topic Endpoint object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.TopicEndpointApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
topic_endpoint_name = 'topic_endpoint_name_example' # str | The topicEndpointName of the Topic Endpoint.

try: 
    # Deletes a Topic Endpoint object.
    api_response = api_instance.delete_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling TopicEndpointApi->delete_msg_vpn_topic_endpoint: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **topic_endpoint_name** | **str**| The topicEndpointName of the Topic Endpoint. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_topic_endpoint**
> MsgVpnTopicEndpointResponse get_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, select=select)

Gets a Topic Endpoint object.

Gets a Topic Endpoint object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| topicEndpointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.TopicEndpointApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
topic_endpoint_name = 'topic_endpoint_name_example' # str | The topicEndpointName of the Topic Endpoint.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Topic Endpoint object.
    api_response = api_instance.get_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling TopicEndpointApi->get_msg_vpn_topic_endpoint: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **topic_endpoint_name** | **str**| The topicEndpointName of the Topic Endpoint. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_topic_endpoints**
> MsgVpnTopicEndpointsResponse get_msg_vpn_topic_endpoints(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Topic Endpoint objects.

Gets a list of Topic Endpoint objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| topicEndpointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.TopicEndpointApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Topic Endpoint objects.
    api_response = api_instance.get_msg_vpn_topic_endpoints(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling TopicEndpointApi->get_msg_vpn_topic_endpoints: %s\n" % e
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

[**MsgVpnTopicEndpointsResponse**](MsgVpnTopicEndpointsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_topic_endpoint**
> MsgVpnTopicEndpointResponse replace_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, body, select=select)

Replaces a Topic Endpoint object.

Replaces a Topic Endpoint object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| owner||||x| permission||||x| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.TopicEndpointApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
topic_endpoint_name = 'topic_endpoint_name_example' # str | The topicEndpointName of the Topic Endpoint.
body = semp_client.MsgVpnTopicEndpoint() # MsgVpnTopicEndpoint | The Topic Endpoint object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Topic Endpoint object.
    api_response = api_instance.replace_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling TopicEndpointApi->replace_msg_vpn_topic_endpoint: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **topic_endpoint_name** | **str**| The topicEndpointName of the Topic Endpoint. | 
 **body** | [**MsgVpnTopicEndpoint**](MsgVpnTopicEndpoint.md)| The Topic Endpoint object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_topic_endpoint**
> MsgVpnTopicEndpointResponse update_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, body, select=select)

Updates a Topic Endpoint object.

Updates a Topic Endpoint object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| owner||||x| permission||||x| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

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
api_instance = semp_client.TopicEndpointApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
topic_endpoint_name = 'topic_endpoint_name_example' # str | The topicEndpointName of the Topic Endpoint.
body = semp_client.MsgVpnTopicEndpoint() # MsgVpnTopicEndpoint | The Topic Endpoint object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Topic Endpoint object.
    api_response = api_instance.update_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling TopicEndpointApi->update_msg_vpn_topic_endpoint: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **topic_endpoint_name** | **str**| The topicEndpointName of the Topic Endpoint. | 
 **body** | [**MsgVpnTopicEndpoint**](MsgVpnTopicEndpoint.md)| The Topic Endpoint object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

