# MsgVpnRestDeliveryPoint

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**client_profile_name** | **str** | The client-profile of this REST Delivery Point. The client-profile must exist in the local Message VPN. Its TCP parameters are used for all REST Consumers in this RDP. Additionally, the RDP client uses the associated queue properties, such as the queue max-depth and min-msg-burst. The client-profile is used inside the auto-generated client-username for this RDP. The default value is &#x60;\&quot;default\&quot;&#x60;. | [optional] 
**enabled** | **bool** | Enable or disable the REST Delivery Point. When disabled, no connections are initiated or messages delivered to any of the contained REST Consumers. The default value is &#x60;false&#x60;. | [optional] 
**msg_vpn_name** | **str** | The name of the Message VPN. | [optional] 
**rest_delivery_point_name** | **str** | A Message VPN-wide unique name for the REST Delivery Point. This name is used to auto-generate a client-username in this Message VPN, which is used by the client for this RDP. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


