
# MsgVpnDistributedCacheCluster

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**cacheName** | **String** | The name of the Distributed Cache. |  [optional]
**clusterName** | **String** | The name of the Cache Cluster. |  [optional]
**deliverToOneOverrideEnabled** | **Boolean** | Enable or disable deliver-to-one override for the Cache Cluster. The default value is &#x60;true&#x60;. |  [optional]
**enabled** | **Boolean** | Enable or disable the Cache Cluster. The default value is &#x60;false&#x60;. |  [optional]
**eventDataByteRateThreshold** | [**EventThresholdByValue**](EventThresholdByValue.md) |  |  [optional]
**eventDataMsgRateThreshold** | [**EventThresholdByValue**](EventThresholdByValue.md) |  |  [optional]
**eventMaxMemoryThreshold** | [**EventThresholdByPercent**](EventThresholdByPercent.md) |  |  [optional]
**eventMaxTopicsThreshold** | [**EventThresholdByPercent**](EventThresholdByPercent.md) |  |  [optional]
**eventRequestQueueDepthThreshold** | [**EventThresholdByPercent**](EventThresholdByPercent.md) |  |  [optional]
**eventRequestRateThreshold** | [**EventThresholdByValue**](EventThresholdByValue.md) |  |  [optional]
**eventResponseRateThreshold** | [**EventThresholdByValue**](EventThresholdByValue.md) |  |  [optional]
**globalCachingEnabled** | **Boolean** | Enable or disable global caching for the Cache Cluster. When enabled, the Cache Instances will fetch topics from remote Home Cache Clusters when requested, and subscribe to those topics to cache them locally. When disabled, the Cache Instances will remove all subscriptions and cached messages for topics from remote Home Cache Clusters. The default value is &#x60;false&#x60;. |  [optional]
**globalCachingHeartbeat** | **Long** | The heartbeat interval, in seconds, used by the Cache Instances to monitor connectivity with the remote Home Cache Clusters. The default value is &#x60;3&#x60;. |  [optional]
**globalCachingTopicLifetime** | **Long** | The topic lifetime, in seconds. If no client requests are received for a given global topic over the duration of the topic lifetime, then the Cache Instance will remove the subscription and cached messages for that topic. A value of 0 disables aging. The default value is &#x60;3600&#x60;. |  [optional]
**maxMemory** | **Long** | The maximum memory usage, in megabytes (MB), for each Cache Instance in the Cache Cluster. The default value is &#x60;2048&#x60;. |  [optional]
**maxMsgsPerTopic** | **Long** | The maximum number of messages per topic for each Cache Instance in the Cache Cluster. When at the maximum, old messages are removed as new messages arrive. The default value is &#x60;1&#x60;. |  [optional]
**maxRequestQueueDepth** | **Long** | The maximum queue depth for cache requests received by the Cache Cluster. The default value is &#x60;100000&#x60;. |  [optional]
**maxTopicCount** | **Long** | The maximum number of topics for each Cache Instance in the Cache Cluster. The default value is &#x60;2000000&#x60;. |  [optional]
**msgLifetime** | **Long** | The message lifetime, in seconds. If a message remains cached for the duration of its lifetime, the Cache Instance will remove the message. A lifetime of 0 results in the message being retained indefinitely. The default is to have no &#x60;msgLifetime&#x60;. |  [optional]
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]
**newTopicAdvertisementEnabled** | **Boolean** | Enable or disable the advertising, onto the message bus, of new topics learned by each Cache Instance in the Cache Cluster. The default value is &#x60;false&#x60;. |  [optional]



