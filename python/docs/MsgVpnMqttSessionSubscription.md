# MsgVpnMqttSessionSubscription

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**mqtt_session_client_id** | **str** | The client-id of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. | [optional] 
**mqtt_session_virtual_router** | **str** | The virtual-router of the MQTT Session. The allowed values and their meaning are:      \&quot;primary\&quot; - MQTT Session belongs to the primary virtual-router.     \&quot;backup\&quot; - MQTT Session belongs to the backup virtual-router.  | [optional] 
**msg_vpn_name** | **str** | The name of the Message VPN. | [optional] 
**subscription_qos** | **int** | The quality of service for the subscription. The value is either &#x60;0&#x60; (deliver at most once) or &#x60;1&#x60; (deliver at least once). The default value is &#x60;0&#x60;. | [optional] 
**subscription_topic** | **str** | An MQTT topic string. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


