# SempClient::ClientProfileApi

All URIs are relative to *http://www.solacesystems.com/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn_client_profile**](ClientProfileApi.md#create_msg_vpn_client_profile) | **POST** /msgVpns/{msgVpnName}/clientProfiles | Creates a Client Profile object.
[**delete_msg_vpn_client_profile**](ClientProfileApi.md#delete_msg_vpn_client_profile) | **DELETE** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Deletes a Client Profile object.
[**get_msg_vpn_client_profile**](ClientProfileApi.md#get_msg_vpn_client_profile) | **GET** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Gets a Client Profile object.
[**get_msg_vpn_client_profiles**](ClientProfileApi.md#get_msg_vpn_client_profiles) | **GET** /msgVpns/{msgVpnName}/clientProfiles | Gets a list of Client Profile objects.
[**replace_msg_vpn_client_profile**](ClientProfileApi.md#replace_msg_vpn_client_profile) | **PUT** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Replaces a Client Profile object.
[**update_msg_vpn_client_profile**](ClientProfileApi.md#update_msg_vpn_client_profile) | **PATCH** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Updates a Client Profile object.


# **create_msg_vpn_client_profile**
> MsgVpnClientProfileResponse create_msg_vpn_client_profile(msg_vpn_name, body, opts)

Creates a Client Profile object.

Creates a Client Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.

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

api_instance = SempClient::ClientProfileApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

body = SempClient::MsgVpnClientProfile.new # MsgVpnClientProfile | The Client Profile object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Creates a Client Profile object.
  result = api_instance.create_msg_vpn_client_profile(msg_vpn_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientProfileApi->create_msg_vpn_client_profile: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnClientProfile**](MsgVpnClientProfile.md)| The Client Profile object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **delete_msg_vpn_client_profile**
> SempMetaOnlyResponse delete_msg_vpn_client_profile(msg_vpn_name, client_profile_name)

Deletes a Client Profile object.

Deletes a Client Profile object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.

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

api_instance = SempClient::ClientProfileApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

client_profile_name = "client_profile_name_example" # String | The clientProfileName of the Client Profile.


begin
  #Deletes a Client Profile object.
  result = api_instance.delete_msg_vpn_client_profile(msg_vpn_name, client_profile_name)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientProfileApi->delete_msg_vpn_client_profile: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **String**| The clientProfileName of the Client Profile. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_client_profile**
> MsgVpnClientProfileResponse get_msg_vpn_client_profile(msg_vpn_name, client_profile_name, opts)

Gets a Client Profile object.

Gets a Client Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::ClientProfileApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

client_profile_name = "client_profile_name_example" # String | The clientProfileName of the Client Profile.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a Client Profile object.
  result = api_instance.get_msg_vpn_client_profile(msg_vpn_name, client_profile_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientProfileApi->get_msg_vpn_client_profile: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **String**| The clientProfileName of the Client Profile. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **get_msg_vpn_client_profiles**
> MsgVpnClientProfilesResponse get_msg_vpn_client_profiles(msg_vpn_name, opts)

Gets a list of Client Profile objects.

Gets a list of Client Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.

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

api_instance = SempClient::ClientProfileApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

opts = { 
  count: 10, # Integer | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
  cursor: "cursor_example", # String | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
  where: ["where_example"], # Array<String> | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Gets a list of Client Profile objects.
  result = api_instance.get_msg_vpn_client_profiles(msg_vpn_name, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientProfileApi->get_msg_vpn_client_profiles: #{e}"
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

[**MsgVpnClientProfilesResponse**](MsgVpnClientProfilesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **replace_msg_vpn_client_profile**
> MsgVpnClientProfileResponse replace_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, opts)

Replaces a Client Profile object.

Replaces a Client Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.

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

api_instance = SempClient::ClientProfileApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

client_profile_name = "client_profile_name_example" # String | The clientProfileName of the Client Profile.

body = SempClient::MsgVpnClientProfile.new # MsgVpnClientProfile | The Client Profile object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Replaces a Client Profile object.
  result = api_instance.replace_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientProfileApi->replace_msg_vpn_client_profile: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **String**| The clientProfileName of the Client Profile. | 
 **body** | [**MsgVpnClientProfile**](MsgVpnClientProfile.md)| The Client Profile object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



# **update_msg_vpn_client_profile**
> MsgVpnClientProfileResponse update_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, opts)

Updates a Client Profile object.

Updates a Client Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.

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

api_instance = SempClient::ClientProfileApi.new

msg_vpn_name = "msg_vpn_name_example" # String | The msgVpnName of the Message VPN.

client_profile_name = "client_profile_name_example" # String | The clientProfileName of the Client Profile.

body = SempClient::MsgVpnClientProfile.new # MsgVpnClientProfile | The Client Profile object's attributes.

opts = { 
  select: ["select_example"] # Array<String> | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
}

begin
  #Updates a Client Profile object.
  result = api_instance.update_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, opts)
  p result
rescue SempClient::ApiError => e
  puts "Exception when calling ClientProfileApi->update_msg_vpn_client_profile: #{e}"
end
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **String**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **String**| The clientProfileName of the Client Profile. | 
 **body** | [**MsgVpnClientProfile**](MsgVpnClientProfile.md)| The Client Profile object&#39;s attributes. | 
 **select** | [**Array&lt;String&gt;**](String.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json



