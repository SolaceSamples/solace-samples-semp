# SempClient::MsgVpnBridgeRemoteSubscription

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**bridge_name** | **String** | The name of the Bridge. | [optional] 
**bridge_virtual_router** | **String** | The virtual-router of the Bridge. The allowed values and their meaning are:      \&quot;primary\&quot; - Bridge belongs to the primary virtual-router.     \&quot;backup\&quot; - Bridge belongs to the backup virtual-router.  | [optional] 
**deliver_always_enabled** | **BOOLEAN** | Flag the topic as deliver-always instead of with the configured deliver-to-one remote-priority value for the bridge. A given topic may be deliver-to-one or deliver-always but not both. | [optional] 
**msg_vpn_name** | **String** | The name of the Message VPN. | [optional] 
**remote_subscription_topic** | **String** | The topic of the Remote Subscription. | [optional] 


