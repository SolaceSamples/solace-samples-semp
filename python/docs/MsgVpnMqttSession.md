# MsgVpnMqttSession

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**enabled** | **bool** | Enable or disable the MQTT Session. When disabled a client attempting to connect to this session will be denied, and an existing connection will be closed. QoS 1 subscriptions of an MQTT Session will continue to attract messages while disabled. The default value is &#x60;false&#x60;. | [optional] 
**mqtt_session_client_id** | **str** | The client-id of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. | [optional] 
**mqtt_session_virtual_router** | **str** | The virtual-router of the MQTT Session. The allowed values and their meaning are:      \&quot;primary\&quot; - MQTT Session belongs to the primary virtual-router.     \&quot;backup\&quot; - MQTT Session belongs to the backup virtual-router.  | [optional] 
**msg_vpn_name** | **str** | The name of the Message VPN. | [optional] 
**owner** | **str** | The owner of the MQTT Session. For externally-created sessions this will be the Client Username of the connecting client. For management-created sessions this will be empty by default. In either case the owner can be changed by the administrator. The MQTT Session must be disabled to change its owner. The default is to have no &#x60;owner&#x60;. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


