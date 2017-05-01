# SempClient::ClientUsernameApi

All URIs are relative to *http://www.solacesystems.com/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn_client_username**](ClientUsernameApi.md#create_msg_vpn_client_username) | **POST** /msgVpns/{msgVpnName}/clientUsernames | Creates a Client Username object.
[**delete_msg_vpn_client_username**](ClientUsernameApi.md#delete_msg_vpn_client_username) | **DELETE** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Deletes a Client Username object.
[**get_msg_vpn_client_username**](ClientUsernameApi.md#get_msg_vpn_client_username) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Gets a Client Username object.
[**get_msg_vpn_client_usernames**](ClientUsernameApi.md#get_msg_vpn_client_usernames) | **GET** /msgVpns/{msgVpnName}/clientUsernames | Gets a list of Client Username objects.
[**replace_msg_vpn_client_username**](ClientUsernameApi.md#replace_msg_vpn_client_username) | **PUT** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Replaces a Client Username object.
[**update_msg_vpn_client_username**](ClientUsernameApi.md#update_msg_vpn_client_username) | **PATCH** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Updates a Client Username object.


# **create_msg_vpn_client_username**
> MsgVpnClientUsernameResponse create_msg_vpn_client_username(msg_vpn_name, body, opts)

Creates a Client Username object.

Creates a Client Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientUsername|x|x||| msgVpnName|x||x|| password||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::ClientUsernameApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

body = SempClient::MsgVpnClientUsername.new # MsgVpnClientUsername | The Client Username object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Creates a Client Username object.
  result = api_instance.create_msg_vpn_client_username(msg_vpn_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientUsernameApi->create_msg_vpn_client_username: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **delete_msg_vpn_client_username**
> SempMetaOnlyResponse delete_msg_vpn_client_username(msg_vpn_name, client_username)

Deletes a Client Username object.

Deletes a Client Username object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::ClientUsernameApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

client_username = "client_username_example" # String | The clientUsername of the Client Username.


begin
  #Deletes a Client Username object.
  result = api_instance.delete_msg_vpn_client_username(msg_vpn_name, client_username)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientUsernameApi->delete_msg_vpn_client_username: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **client_username** | **String**| The clientUsername of the Client Username. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_client_username**
> MsgVpnClientUsernameResponse get_msg_vpn_client_username(msg_vpn_name, client_username, opts)

Gets a Client Username object.

Gets a Client Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::ClientUsernameApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

client_username = "client_username_example" # String | The clientUsername of the Client Username.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a Client Username object.
  result = api_instance.get_msg_vpn_client_username(msg_vpn_name, client_username, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientUsernameApi->get_msg_vpn_client_username: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **client_username** | **String**| The clientUsername of the Client Username. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_client_usernames**
> MsgVpnClientUsernamesResponse get_msg_vpn_client_usernames(msg_vpn_name, opts)

Gets a list of Client Username objects.

Gets a list of Client Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::ClientUsernameApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

opts = { 
  count: 10, # Integer | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
  cursor: "cursor_example", # String | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
  where: ["where_example"], # Array<String> | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a list of Client Username objects.
  result = api_instance.get_msg_vpn_client_usernames(msg_vpn_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientUsernameApi->get_msg_vpn_client_usernames: #{e}"
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

[**MsgVpnClientUsernamesResponse**](MsgVpnClientUsernamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **replace_msg_vpn_client_username**
> MsgVpnClientUsernameResponse replace_msg_vpn_client_username(msg_vpn_name, client_username, body, opts)

Replaces a Client Username object.

Replaces a Client Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::ClientUsernameApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

client_username = "client_username_example" # String | The clientUsername of the Client Username.

body = SempClient::MsgVpnClientUsername.new # MsgVpnClientUsername | The Client Username object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Replaces a Client Username object.
  result = api_instance.replace_msg_vpn_client_username(msg_vpn_name, client_username, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientUsernameApi->replace_msg_vpn_client_username: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **client_username** | **String**| The clientUsername of the Client Username. | 
 **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **update_msg_vpn_client_username**
> MsgVpnClientUsernameResponse update_msg_vpn_client_username(msg_vpn_name, client_username, body, opts)

Updates a Client Username object.

Updates a Client Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.

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

api_instance = SempClient::ClientUsernameApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

client_username = "client_username_example" # String | The clientUsername of the Client Username.

body = SempClient::MsgVpnClientUsername.new # MsgVpnClientUsername | The Client Username object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Updates a Client Username object.
  result = api_instance.update_msg_vpn_client_username(msg_vpn_name, client_username, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientUsernameApi->update_msg_vpn_client_username: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **client_username** | **String**| The clientUsername of the Client Username. | 
 **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



