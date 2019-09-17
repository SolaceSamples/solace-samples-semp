
# MsgVpnDistributedCacheClusterInstance

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**autoStartEnabled** | **Boolean** | Enable or disable auto-start for the Cache Instance. When enabled, the Cache Instance will automatically attempt to transition from the Stopped operational state to Up whenever it restarts or reconnects to the message broker. The default value is &#x60;false&#x60;. |  [optional]
**cacheName** | **String** | The name of the Distributed Cache. |  [optional]
**clusterName** | **String** | The name of the Cache Cluster. |  [optional]
**enabled** | **Boolean** | Enable or disable the Cache Instance. The default value is &#x60;false&#x60;. |  [optional]
**instanceName** | **String** | The name of the Cache Instance. |  [optional]
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]
**stopOnLostMsgEnabled** | **Boolean** | Enable or disable stop-on-lost-message for the Cache Instance. When enabled, the Cache Instance will transition to the stopped operational state upon losing a message. When stopped, it cannot accept or respond to cache requests, but continues to cache messages. The default value is &#x60;true&#x60;. |  [optional]



