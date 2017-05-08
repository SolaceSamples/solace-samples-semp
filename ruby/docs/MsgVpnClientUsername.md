# SempClient::MsgVpnClientUsername

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**acl_profile_name** | **String** | The acl-profile of this client-username. The default value is &#x60;\&quot;default\&quot;&#x60;. | [optional] 
**client_profile_name** | **String** | The client-profile of this client-username. The default value is &#x60;\&quot;default\&quot;&#x60;. | [optional] 
**client_username** | **String** | The name of the Client Username. | [optional] 
**enabled** | **BOOLEAN** | Enables or disables a client-username. When disabled all clients currently connected referencing this client username are disconnected. The default value is &#x60;false&#x60;. | [optional] 
**guaranteed_endpoint_permission_override_enabled** | **BOOLEAN** | Enables or disables guaranteed endpoint permission override for a client-username. When enabled all guaranteed endpoints may be accessed, modified or deleted with the same permission as the owner. The default value is &#x60;false&#x60;. | [optional] 
**msg_vpn_name** | **String** | The name of the Message VPN. | [optional] 
**password** | **String** | The password of this client-username for internal authentication. The default is to have no &#x60;password&#x60;. | [optional] 
**subscription_manager_enabled** | **BOOLEAN** | Enables or disables subscription management capability. This is the ability to manage subscriptions on behalf of other client names. The default value is &#x60;false&#x60;. | [optional] 


