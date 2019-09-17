
# MsgVpnMqttSessionSubscription

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**mqttSessionClientId** | **String** | The Client ID of the MQTT Session, which corresponds to the ClientId provided in the MQTT CONNECT packet. |  [optional]
**mqttSessionVirtualRouter** | [**MqttSessionVirtualRouterEnum**](#MqttSessionVirtualRouterEnum) | The virtual router of the MQTT Session. The allowed values and their meaning are:  &lt;pre&gt; \&quot;primary\&quot; - The MQTT Session belongs to the primary virtual router. \&quot;backup\&quot; - The MQTT Session belongs to the backup virtual router. &lt;/pre&gt;  |  [optional]
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]
**subscriptionQos** | **Long** | The quality of service (QoS) for the subscription as either 0 (deliver at most once) or 1 (deliver at least once). QoS 2 is not supported, but QoS 2 messages attracted by QoS 0 or QoS 1 subscriptions are accepted and delivered accordingly. The default value is &#x60;0&#x60;. |  [optional]
**subscriptionTopic** | **String** | The MQTT subscription topic. |  [optional]


<a name="MqttSessionVirtualRouterEnum"></a>
## Enum: MqttSessionVirtualRouterEnum
Name | Value
---- | -----
PRIMARY | &quot;primary&quot;
BACKUP | &quot;backup&quot;



