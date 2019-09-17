
# MsgVpnMqttRetainCache

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**cacheName** | **String** | The name of the MQTT Retain Cache. |  [optional]
**enabled** | **Boolean** | Enable or disable this MQTT Retain Cache. When the cache is disabled, neither retain messages nor retain requests will be delivered by the cache. However, live retain messages will continue to be delivered to currently connected MQTT clients. The default value is &#x60;false&#x60;. |  [optional]
**msgLifetime** | **Long** | The message lifetime, in seconds. If a message remains cached for the duration of its lifetime, the cache will remove the message. A lifetime of 0 results in the message being retained indefinitely. The default value is &#x60;0&#x60;. |  [optional]
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]



