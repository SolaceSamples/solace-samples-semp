# SempClient::QueueApi

All URIs are relative to *http://www.solacesystems.com/SEMP/v2/config*

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
> MsgVpnQueueResponse create_msg_vpn_queue(msg_vpn_name, body, opts)

Creates a Queue object.

Creates a Queue object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

### Example
```ruby
# load the gem
require 'semp_client'
# setup authorization
SempClient.configure do |config|
  # Configure HTTP basic authorization: basicAuth
  config.username = 'YOUR USERNAME'
  config.password = 'YOUR PASSWORD'
end

api_instance = SempClient::QueueApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

body = SempClient::MsgVpnQueue.new # MsgVpnQueue | The Queue object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Creates a Queue object.
  result = api_instance.create_msg_vpn_queue(msg_vpn_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling QueueApi->create_msg_vpn_queue: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnQueue**](MsgVpnQueue.md)| The Queue object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **create_msg_vpn_queue_subscription**
> MsgVpnQueueSubscriptionResponse create_msg_vpn_queue_subscription(msg_vpn_name, queue_name, body, opts)

Creates a Queue Subscription object.

Creates a Queue Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

### Example
```ruby
# load the gem
require 'semp_client'
# setup authorization
SempClient.configure do |config|
  # Configure HTTP basic authorization: basicAuth
  config.username = 'YOUR USERNAME'
  config.password = 'YOUR PASSWORD'
end

api_instance = SempClient::QueueApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

queue_name = "queue_name_example" # String | The queueName of the Queue.

body = SempClient::MsgVpnQueueSubscription.new # MsgVpnQueueSubscription | The Queue Subscription object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Creates a Queue Subscription object.
  result = api_instance.create_msg_vpn_queue_subscription(msg_vpn_name, queue_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling QueueApi->create_msg_vpn_queue_subscription: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **queue_name** | **String**| The queueName of the Queue. | 
 **body** | [**MsgVpnQueueSubscription**](MsgVpnQueueSubscription.md)| The Queue Subscription object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueSubscriptionResponse**](MsgVpnQueueSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **delete_msg_vpn_queue**
> SempMetaOnlyResponse delete_msg_vpn_queue(msg_vpn_name, queue_name)

Deletes a Queue object.

Deletes a Queue object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

### Example
```ruby
# load the gem
require 'semp_client'
# setup authorization
SempClient.configure do |config|
  # Configure HTTP basic authorization: basicAuth
  config.username = 'YOUR USERNAME'
  config.password = 'YOUR PASSWORD'
end

api_instance = SempClient::QueueApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

queue_name = "queue_name_example" # String | The queueName of the Queue.


begin
  #Deletes a Queue object.
  result = api_instance.delete_msg_vpn_queue(msg_vpn_name, queue_name)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling QueueApi->delete_msg_vpn_queue: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **queue_name** | **String**| The queueName of the Queue. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **delete_msg_vpn_queue_subscription**
> SempMetaOnlyResponse delete_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic)

Deletes a Queue Subscription object.

Deletes a Queue Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

### Example
```ruby
# load the gem
require 'semp_client'
# setup authorization
SempClient.configure do |config|
  # Configure HTTP basic authorization: basicAuth
  config.username = 'YOUR USERNAME'
  config.password = 'YOUR PASSWORD'
end

api_instance = SempClient::QueueApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

queue_name = "queue_name_example" # String | The queueName of the Queue.

subscription_topic = "subscription_topic_example" # String | The subscriptionTopic of the Queue Subscription.


begin
  #Deletes a Queue Subscription object.
  result = api_instance.delete_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling QueueApi->delete_msg_vpn_queue_subscription: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **queue_name** | **String**| The queueName of the Queue. | 
 **subscription_topic** | **String**| The subscriptionTopic of the Queue Subscription. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_queue**
> MsgVpnQueueResponse get_msg_vpn_queue(msg_vpn_name, queue_name, opts)

Gets a Queue object.

Gets a Queue object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

### Example
```ruby
# load the gem
require 'semp_client'
# setup authorization
SempClient.configure do |config|
  # Configure HTTP basic authorization: basicAuth
  config.username = 'YOUR USERNAME'
  config.password = 'YOUR PASSWORD'
end

api_instance = SempClient::QueueApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

queue_name = "queue_name_example" # String | The queueName of the Queue.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a Queue object.
  result = api_instance.get_msg_vpn_queue(msg_vpn_name, queue_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling QueueApi->get_msg_vpn_queue: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **queue_name** | **String**| The queueName of the Queue. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_queue_subscription**
> MsgVpnQueueSubscriptionResponse get_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic, opts)

Gets a Queue Subscription object.

Gets a Queue Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

### Example
```ruby
# load the gem
require 'semp_client'
# setup authorization
SempClient.configure do |config|
  # Configure HTTP basic authorization: basicAuth
  config.username = 'YOUR USERNAME'
  config.password = 'YOUR PASSWORD'
end

api_instance = SempClient::QueueApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

queue_name = "queue_name_example" # String | The queueName of the Queue.

subscription_topic = "subscription_topic_example" # String | The subscriptionTopic of the Queue Subscription.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a Queue Subscription object.
  result = api_instance.get_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling QueueApi->get_msg_vpn_queue_subscription: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **queue_name** | **String**| The queueName of the Queue. | 
 **subscription_topic** | **String**| The subscriptionTopic of the Queue Subscription. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueSubscriptionResponse**](MsgVpnQueueSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_queue_subscriptions**
> MsgVpnQueueSubscriptionsResponse get_msg_vpn_queue_subscriptions(msg_vpn_name, queue_name, opts)

Gets a list of Queue Subscription objects.

Gets a list of Queue Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

### Example
```ruby
# load the gem
require 'semp_client'
# setup authorization
SempClient.configure do |config|
  # Configure HTTP basic authorization: basicAuth
  config.username = 'YOUR USERNAME'
  config.password = 'YOUR PASSWORD'
end

api_instance = SempClient::QueueApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

queue_name = "queue_name_example" # String | The queueName of the Queue.

opts = { 
  count: 10, # Integer | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
  cursor: "cursor_example", # String | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
  where: ["where_example"], # Array<String> | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a list of Queue Subscription objects.
  result = api_instance.get_msg_vpn_queue_subscriptions(msg_vpn_name, queue_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling QueueApi->get_msg_vpn_queue_subscriptions: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **queue_name** | **String**| The queueName of the Queue. | 
 **count** | **Integer**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **String**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**Array&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueSubscriptionsResponse**](MsgVpnQueueSubscriptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_queues**
> MsgVpnQueuesResponse get_msg_vpn_queues(msg_vpn_name, opts)

Gets a list of Queue objects.

Gets a list of Queue objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

### Example
```ruby
# load the gem
require 'semp_client'
# setup authorization
SempClient.configure do |config|
  # Configure HTTP basic authorization: basicAuth
  config.username = 'YOUR USERNAME'
  config.password = 'YOUR PASSWORD'
end

api_instance = SempClient::QueueApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

opts = { 
  count: 10, # Integer | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
  cursor: "cursor_example", # String | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
  where: ["where_example"], # Array<String> | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a list of Queue objects.
  result = api_instance.get_msg_vpn_queues(msg_vpn_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling QueueApi->get_msg_vpn_queues: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **count** | **Integer**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **String**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**Array&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueuesResponse**](MsgVpnQueuesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **replace_msg_vpn_queue**
> MsgVpnQueueResponse replace_msg_vpn_queue(msg_vpn_name, queue_name, body, opts)

Replaces a Queue object.

Replaces a Queue object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

### Example
```ruby
# load the gem
require 'semp_client'
# setup authorization
SempClient.configure do |config|
  # Configure HTTP basic authorization: basicAuth
  config.username = 'YOUR USERNAME'
  config.password = 'YOUR PASSWORD'
end

api_instance = SempClient::QueueApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

queue_name = "queue_name_example" # String | The queueName of the Queue.

body = SempClient::MsgVpnQueue.new # MsgVpnQueue | The Queue object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Replaces a Queue object.
  result = api_instance.replace_msg_vpn_queue(msg_vpn_name, queue_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling QueueApi->replace_msg_vpn_queue: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **queue_name** | **String**| The queueName of the Queue. | 
 **body** | [**MsgVpnQueue**](MsgVpnQueue.md)| The Queue object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **update_msg_vpn_queue**
> MsgVpnQueueResponse update_msg_vpn_queue(msg_vpn_name, queue_name, body, opts)

Updates a Queue object.

Updates a Queue object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

### Example
```ruby
# load the gem
require 'semp_client'
# setup authorization
SempClient.configure do |config|
  # Configure HTTP basic authorization: basicAuth
  config.username = 'YOUR USERNAME'
  config.password = 'YOUR PASSWORD'
end

api_instance = SempClient::QueueApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

queue_name = "queue_name_example" # String | The queueName of the Queue.

body = SempClient::MsgVpnQueue.new # MsgVpnQueue | The Queue object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Updates a Queue object.
  result = api_instance.update_msg_vpn_queue(msg_vpn_name, queue_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling QueueApi->update_msg_vpn_queue: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **queue_name** | **String**| The queueName of the Queue. | 
 **body** | [**MsgVpnQueue**](MsgVpnQueue.md)| The Queue object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



