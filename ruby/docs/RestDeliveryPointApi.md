# SempClient::RestDeliveryPointApi

All URIs are relative to *http://www.solacesystems.com/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn_rest_delivery_point**](RestDeliveryPointApi.md#create_msg_vpn_rest_delivery_point) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints | Creates a REST Delivery Point object.
[**create_msg_vpn_rest_delivery_point_queue_binding**](RestDeliveryPointApi.md#create_msg_vpn_rest_delivery_point_queue_binding) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings | Creates a Queue Binding object.
[**create_msg_vpn_rest_delivery_point_rest_consumer**](RestDeliveryPointApi.md#create_msg_vpn_rest_delivery_point_rest_consumer) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers | Creates a REST Consumer object.
[**create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**](RestDeliveryPointApi.md#create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames | Creates a Trusted Common Name object.
[**delete_msg_vpn_rest_delivery_point**](RestDeliveryPointApi.md#delete_msg_vpn_rest_delivery_point) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Deletes a REST Delivery Point object.
[**delete_msg_vpn_rest_delivery_point_queue_binding**](RestDeliveryPointApi.md#delete_msg_vpn_rest_delivery_point_queue_binding) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Deletes a Queue Binding object.
[**delete_msg_vpn_rest_delivery_point_rest_consumer**](RestDeliveryPointApi.md#delete_msg_vpn_rest_delivery_point_rest_consumer) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Deletes a REST Consumer object.
[**delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**](RestDeliveryPointApi.md#delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Deletes a Trusted Common Name object.
[**get_msg_vpn_rest_delivery_point**](RestDeliveryPointApi.md#get_msg_vpn_rest_delivery_point) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Gets a REST Delivery Point object.
[**get_msg_vpn_rest_delivery_point_queue_binding**](RestDeliveryPointApi.md#get_msg_vpn_rest_delivery_point_queue_binding) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Gets a Queue Binding object.
[**get_msg_vpn_rest_delivery_point_queue_bindings**](RestDeliveryPointApi.md#get_msg_vpn_rest_delivery_point_queue_bindings) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings | Gets a list of Queue Binding objects.
[**get_msg_vpn_rest_delivery_point_rest_consumer**](RestDeliveryPointApi.md#get_msg_vpn_rest_delivery_point_rest_consumer) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Gets a REST Consumer object.
[**get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**](RestDeliveryPointApi.md#get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Gets a Trusted Common Name object.
[**get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names**](RestDeliveryPointApi.md#get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames | Gets a list of Trusted Common Name objects.
[**get_msg_vpn_rest_delivery_point_rest_consumers**](RestDeliveryPointApi.md#get_msg_vpn_rest_delivery_point_rest_consumers) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers | Gets a list of REST Consumer objects.
[**get_msg_vpn_rest_delivery_points**](RestDeliveryPointApi.md#get_msg_vpn_rest_delivery_points) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints | Gets a list of REST Delivery Point objects.
[**replace_msg_vpn_rest_delivery_point**](RestDeliveryPointApi.md#replace_msg_vpn_rest_delivery_point) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Replaces a REST Delivery Point object.
[**replace_msg_vpn_rest_delivery_point_queue_binding**](RestDeliveryPointApi.md#replace_msg_vpn_rest_delivery_point_queue_binding) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Replaces a Queue Binding object.
[**replace_msg_vpn_rest_delivery_point_rest_consumer**](RestDeliveryPointApi.md#replace_msg_vpn_rest_delivery_point_rest_consumer) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Replaces a REST Consumer object.
[**update_msg_vpn_rest_delivery_point**](RestDeliveryPointApi.md#update_msg_vpn_rest_delivery_point) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Updates a REST Delivery Point object.
[**update_msg_vpn_rest_delivery_point_queue_binding**](RestDeliveryPointApi.md#update_msg_vpn_rest_delivery_point_queue_binding) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Updates a Queue Binding object.
[**update_msg_vpn_rest_delivery_point_rest_consumer**](RestDeliveryPointApi.md#update_msg_vpn_rest_delivery_point_rest_consumer) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Updates a REST Consumer object.


# **create_msg_vpn_rest_delivery_point**
> MsgVpnRestDeliveryPointResponse create_msg_vpn_rest_delivery_point(msg_vpn_name, body, opts)

Creates a REST Delivery Point object.

Creates a REST Delivery Point object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

body = SempClient::MsgVpnRestDeliveryPoint.new # MsgVpnRestDeliveryPoint | The REST Delivery Point object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Creates a REST Delivery Point object.
  result = api_instance.create_msg_vpn_rest_delivery_point(msg_vpn_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->create_msg_vpn_rest_delivery_point: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnRestDeliveryPoint**](MsgVpnRestDeliveryPoint.md)| The REST Delivery Point object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointResponse**](MsgVpnRestDeliveryPointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **create_msg_vpn_rest_delivery_point_queue_binding**
> MsgVpnRestDeliveryPointQueueBindingResponse create_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, body, opts)

Creates a Queue Binding object.

Creates a Queue Binding object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueBindingName|x|x||| restDeliveryPointName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

body = SempClient::MsgVpnRestDeliveryPointQueueBinding.new # MsgVpnRestDeliveryPointQueueBinding | The Queue Binding object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Creates a Queue Binding object.
  result = api_instance.create_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->create_msg_vpn_rest_delivery_point_queue_binding: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **body** | [**MsgVpnRestDeliveryPointQueueBinding**](MsgVpnRestDeliveryPointQueueBinding.md)| The Queue Binding object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointQueueBindingResponse**](MsgVpnRestDeliveryPointQueueBindingResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **create_msg_vpn_rest_delivery_point_rest_consumer**
> MsgVpnRestDeliveryPointRestConsumerResponse create_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, body, opts)

Creates a REST Consumer object.

Creates a REST Consumer object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword||||x| msgVpnName|x||x|| restConsumerName|x|x||| restDeliveryPointName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

body = SempClient::MsgVpnRestDeliveryPointRestConsumer.new # MsgVpnRestDeliveryPointRestConsumer | The REST Consumer object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Creates a REST Consumer object.
  result = api_instance.create_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->create_msg_vpn_rest_delivery_point_rest_consumer: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **body** | [**MsgVpnRestDeliveryPointRestConsumer**](MsgVpnRestDeliveryPointRestConsumer.md)| The REST Consumer object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerResponse**](MsgVpnRestDeliveryPointRestConsumerResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**
> MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts)

Creates a Trusted Common Name object.

Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restConsumerName|x||x|| restDeliveryPointName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

rest_consumer_name = "rest_consumer_name_example" # String | The restConsumerName of the REST Consumer.

body = SempClient::MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName.new # MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName | The Trusted Common Name object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Creates a Trusted Common Name object.
  result = api_instance.create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **String**| The restConsumerName of the REST Consumer. | 
 **body** | [**MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName**](MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName.md)| The Trusted Common Name object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse**](MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **delete_msg_vpn_rest_delivery_point**
> SempMetaOnlyResponse delete_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name)

Deletes a REST Delivery Point object.

Deletes a REST Delivery Point object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.


begin
  #Deletes a REST Delivery Point object.
  result = api_instance.delete_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->delete_msg_vpn_rest_delivery_point: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **delete_msg_vpn_rest_delivery_point_queue_binding**
> SempMetaOnlyResponse delete_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name)

Deletes a Queue Binding object.

Deletes a Queue Binding object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

queue_binding_name = "queue_binding_name_example" # String | The queueBindingName of the Queue Binding.


begin
  #Deletes a Queue Binding object.
  result = api_instance.delete_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->delete_msg_vpn_rest_delivery_point_queue_binding: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **queue_binding_name** | **String**| The queueBindingName of the Queue Binding. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **delete_msg_vpn_rest_delivery_point_rest_consumer**
> SempMetaOnlyResponse delete_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name)

Deletes a REST Consumer object.

Deletes a REST Consumer object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

rest_consumer_name = "rest_consumer_name_example" # String | The restConsumerName of the REST Consumer.


begin
  #Deletes a REST Consumer object.
  result = api_instance.delete_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->delete_msg_vpn_rest_delivery_point_rest_consumer: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **String**| The restConsumerName of the REST Consumer. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**
> SempMetaOnlyResponse delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name)

Deletes a Trusted Common Name object.

Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

rest_consumer_name = "rest_consumer_name_example" # String | The restConsumerName of the REST Consumer.

tls_trusted_common_name = "tls_trusted_common_name_example" # String | The tlsTrustedCommonName of the Trusted Common Name.


begin
  #Deletes a Trusted Common Name object.
  result = api_instance.delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **String**| The restConsumerName of the REST Consumer. | 
 **tls_trusted_common_name** | **String**| The tlsTrustedCommonName of the Trusted Common Name. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_rest_delivery_point**
> MsgVpnRestDeliveryPointResponse get_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, opts)

Gets a REST Delivery Point object.

Gets a REST Delivery Point object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a REST Delivery Point object.
  result = api_instance.get_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->get_msg_vpn_rest_delivery_point: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointResponse**](MsgVpnRestDeliveryPointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_rest_delivery_point_queue_binding**
> MsgVpnRestDeliveryPointQueueBindingResponse get_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, opts)

Gets a Queue Binding object.

Gets a Queue Binding object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

queue_binding_name = "queue_binding_name_example" # String | The queueBindingName of the Queue Binding.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a Queue Binding object.
  result = api_instance.get_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->get_msg_vpn_rest_delivery_point_queue_binding: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **queue_binding_name** | **String**| The queueBindingName of the Queue Binding. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointQueueBindingResponse**](MsgVpnRestDeliveryPointQueueBindingResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_rest_delivery_point_queue_bindings**
> MsgVpnRestDeliveryPointQueueBindingsResponse get_msg_vpn_rest_delivery_point_queue_bindings(msg_vpn_name, rest_delivery_point_name, opts)

Gets a list of Queue Binding objects.

Gets a list of Queue Binding objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

opts = { 
  count: 10, # Integer | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
  cursor: "cursor_example", # String | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
  where: ["where_example"], # Array<String> | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a list of Queue Binding objects.
  result = api_instance.get_msg_vpn_rest_delivery_point_queue_bindings(msg_vpn_name, rest_delivery_point_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->get_msg_vpn_rest_delivery_point_queue_bindings: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **count** | **Integer**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **String**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**Array&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointQueueBindingsResponse**](MsgVpnRestDeliveryPointQueueBindingsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_rest_delivery_point_rest_consumer**
> MsgVpnRestDeliveryPointRestConsumerResponse get_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts)

Gets a REST Consumer object.

Gets a REST Consumer object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

rest_consumer_name = "rest_consumer_name_example" # String | The restConsumerName of the REST Consumer.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a REST Consumer object.
  result = api_instance.get_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->get_msg_vpn_rest_delivery_point_rest_consumer: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **String**| The restConsumerName of the REST Consumer. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerResponse**](MsgVpnRestDeliveryPointRestConsumerResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**
> MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, opts)

Gets a Trusted Common Name object.

Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

rest_consumer_name = "rest_consumer_name_example" # String | The restConsumerName of the REST Consumer.

tls_trusted_common_name = "tls_trusted_common_name_example" # String | The tlsTrustedCommonName of the Trusted Common Name.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a Trusted Common Name object.
  result = api_instance.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **String**| The restConsumerName of the REST Consumer. | 
 **tls_trusted_common_name** | **String**| The tlsTrustedCommonName of the Trusted Common Name. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse**](MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names**
> MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts)

Gets a list of Trusted Common Name objects.

Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

rest_consumer_name = "rest_consumer_name_example" # String | The restConsumerName of the REST Consumer.

opts = { 
  where: ["where_example"], # Array<String> | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a list of Trusted Common Name objects.
  result = api_instance.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **String**| The restConsumerName of the REST Consumer. | 
 **where** | [**Array&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse**](MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_rest_delivery_point_rest_consumers**
> MsgVpnRestDeliveryPointRestConsumersResponse get_msg_vpn_rest_delivery_point_rest_consumers(msg_vpn_name, rest_delivery_point_name, opts)

Gets a list of REST Consumer objects.

Gets a list of REST Consumer objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

opts = { 
  count: 10, # Integer | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
  cursor: "cursor_example", # String | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
  where: ["where_example"], # Array<String> | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a list of REST Consumer objects.
  result = api_instance.get_msg_vpn_rest_delivery_point_rest_consumers(msg_vpn_name, rest_delivery_point_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->get_msg_vpn_rest_delivery_point_rest_consumers: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **count** | **Integer**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **String**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**Array&lt;String&gt;**](String.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumersResponse**](MsgVpnRestDeliveryPointRestConsumersResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_rest_delivery_points**
> MsgVpnRestDeliveryPointsResponse get_msg_vpn_rest_delivery_points(msg_vpn_name, opts)

Gets a list of REST Delivery Point objects.

Gets a list of REST Delivery Point objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

opts = { 
  count: 10, # Integer | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
  cursor: "cursor_example", # String | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
  where: ["where_example"], # Array<String> | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a list of REST Delivery Point objects.
  result = api_instance.get_msg_vpn_rest_delivery_points(msg_vpn_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->get_msg_vpn_rest_delivery_points: #{e}"
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

[**MsgVpnRestDeliveryPointsResponse**](MsgVpnRestDeliveryPointsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **replace_msg_vpn_rest_delivery_point**
> MsgVpnRestDeliveryPointResponse replace_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, opts)

Replaces a REST Delivery Point object.

Replaces a REST Delivery Point object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

body = SempClient::MsgVpnRestDeliveryPoint.new # MsgVpnRestDeliveryPoint | The REST Delivery Point object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Replaces a REST Delivery Point object.
  result = api_instance.replace_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->replace_msg_vpn_rest_delivery_point: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **body** | [**MsgVpnRestDeliveryPoint**](MsgVpnRestDeliveryPoint.md)| The REST Delivery Point object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointResponse**](MsgVpnRestDeliveryPointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **replace_msg_vpn_rest_delivery_point_queue_binding**
> MsgVpnRestDeliveryPointQueueBindingResponse replace_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, opts)

Replaces a Queue Binding object.

Replaces a Queue Binding object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

queue_binding_name = "queue_binding_name_example" # String | The queueBindingName of the Queue Binding.

body = SempClient::MsgVpnRestDeliveryPointQueueBinding.new # MsgVpnRestDeliveryPointQueueBinding | The Queue Binding object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Replaces a Queue Binding object.
  result = api_instance.replace_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->replace_msg_vpn_rest_delivery_point_queue_binding: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **queue_binding_name** | **String**| The queueBindingName of the Queue Binding. | 
 **body** | [**MsgVpnRestDeliveryPointQueueBinding**](MsgVpnRestDeliveryPointQueueBinding.md)| The Queue Binding object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointQueueBindingResponse**](MsgVpnRestDeliveryPointQueueBindingResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **replace_msg_vpn_rest_delivery_point_rest_consumer**
> MsgVpnRestDeliveryPointRestConsumerResponse replace_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts)

Replaces a REST Consumer object.

Replaces a REST Consumer object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

rest_consumer_name = "rest_consumer_name_example" # String | The restConsumerName of the REST Consumer.

body = SempClient::MsgVpnRestDeliveryPointRestConsumer.new # MsgVpnRestDeliveryPointRestConsumer | The REST Consumer object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Replaces a REST Consumer object.
  result = api_instance.replace_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->replace_msg_vpn_rest_delivery_point_rest_consumer: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **String**| The restConsumerName of the REST Consumer. | 
 **body** | [**MsgVpnRestDeliveryPointRestConsumer**](MsgVpnRestDeliveryPointRestConsumer.md)| The REST Consumer object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerResponse**](MsgVpnRestDeliveryPointRestConsumerResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **update_msg_vpn_rest_delivery_point**
> MsgVpnRestDeliveryPointResponse update_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, opts)

Updates a REST Delivery Point object.

Updates a REST Delivery Point object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

body = SempClient::MsgVpnRestDeliveryPoint.new # MsgVpnRestDeliveryPoint | The REST Delivery Point object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Updates a REST Delivery Point object.
  result = api_instance.update_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->update_msg_vpn_rest_delivery_point: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **body** | [**MsgVpnRestDeliveryPoint**](MsgVpnRestDeliveryPoint.md)| The REST Delivery Point object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointResponse**](MsgVpnRestDeliveryPointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **update_msg_vpn_rest_delivery_point_queue_binding**
> MsgVpnRestDeliveryPointQueueBindingResponse update_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, opts)

Updates a Queue Binding object.

Updates a Queue Binding object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

queue_binding_name = "queue_binding_name_example" # String | The queueBindingName of the Queue Binding.

body = SempClient::MsgVpnRestDeliveryPointQueueBinding.new # MsgVpnRestDeliveryPointQueueBinding | The Queue Binding object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Updates a Queue Binding object.
  result = api_instance.update_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->update_msg_vpn_rest_delivery_point_queue_binding: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **queue_binding_name** | **String**| The queueBindingName of the Queue Binding. | 
 **body** | [**MsgVpnRestDeliveryPointQueueBinding**](MsgVpnRestDeliveryPointQueueBinding.md)| The Queue Binding object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointQueueBindingResponse**](MsgVpnRestDeliveryPointQueueBindingResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **update_msg_vpn_rest_delivery_point_rest_consumer**
> MsgVpnRestDeliveryPointRestConsumerResponse update_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts)

Updates a REST Consumer object.

Updates a REST Consumer object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::RestDeliveryPointApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

rest_delivery_point_name = "rest_delivery_point_name_example" # String | The restDeliveryPointName of the REST Delivery Point.

rest_consumer_name = "rest_consumer_name_example" # String | The restConsumerName of the REST Consumer.

body = SempClient::MsgVpnRestDeliveryPointRestConsumer.new # MsgVpnRestDeliveryPointRestConsumer | The REST Consumer object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Updates a REST Consumer object.
  result = api_instance.update_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling RestDeliveryPointApi->update_msg_vpn_rest_delivery_point_rest_consumer: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **String**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **String**| The restConsumerName of the REST Consumer. | 
 **body** | [**MsgVpnRestDeliveryPointRestConsumer**](MsgVpnRestDeliveryPointRestConsumer.md)| The REST Consumer object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerResponse**](MsgVpnRestDeliveryPointRestConsumerResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



