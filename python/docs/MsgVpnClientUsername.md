# MsgVpnClientUsername

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**acl_profile_name** | **str** | The acl-profile of this client-username. The default value is &#x60;\&quot;default\&quot;&#x60;. | [optional] 
**client_profile_name** | **str** | The client-profile of this client-username. The default value is &#x60;\&quot;default\&quot;&#x60;. | [optional] 
**client_username** | **str** | The name of the Client Username. | [optional] 
**enabled** | **bool** | Enables or disables a client-username. When disabled all clients currently connected referencing this client username are disconnected. The default value is &#x60;false&#x60;. | [optional] 
**guaranteed_endpoint_permission_override_enabled** | **bool** | Enables or disables guaranteed endpoint permission override for a client-username. When enabled all guaranteed endpoints may be accessed, modified or deleted with the same permission as the owner. The default value is &#x60;false&#x60;. | [optional] 
**msg_vpn_name** | **str** | The name of the Message VPN. | [optional] 
**password** | **str** | The password of this client-username for internal authentication. The default is to have no &#x60;password&#x60;. | [optional] 
**subscription_manager_enabled** | **bool** | Enables or disables subscription management capability. This is the ability to manage subscriptions on behalf of other client names. The default value is &#x60;false&#x60;. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


