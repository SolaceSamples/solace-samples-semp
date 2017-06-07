# MsgVpnQueue

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**access_type** | **str** | The queue access type of either exclusive or non-exclusive. The default value is &#x60;\&quot;exclusive\&quot;&#x60;. The allowed values and their meaning are:      \&quot;exclusive\&quot; - Exclusive delivery of messages to first bound client.     \&quot;non-exclusive\&quot; - Non-exclusive delivery of messages to all bound clients.  | [optional] 
**consumer_ack_propagation_enabled** | **bool** | Enable or disable the propagation of consumer acks received on the active replication Message VPN to the standby replication Message VPN. The default value is &#x60;true&#x60;. | [optional] 
**egress_enabled** | **bool** | Enable or disable the flow of messages from a Queue. The default value is &#x60;false&#x60;. | [optional] 
**event_bind_count_threshold** | [**EventThreshold**](EventThreshold.md) |  | [optional] 
**event_msg_spool_usage_threshold** | [**EventThreshold**](EventThreshold.md) |  | [optional] 
**event_reject_low_priority_msg_limit_threshold** | [**EventThreshold**](EventThreshold.md) |  | [optional] 
**ingress_enabled** | **bool** | Enable or disable the flow of messages to a Queue. The default value is &#x60;false&#x60;. | [optional] 
**max_bind_count** | **int** | The maximum number of times clients can bind to a given Queue. The default value is &#x60;1000&#x60;. | [optional] 
**max_delivered_unacked_msgs_per_flow** | **int** | The max messages delivered but not acknowledged per flow for this Queue. The default is the max value supported by the hardware. | [optional] 
**max_msg_size** | **int** | The max message size (in bytes) allowed in this Queue. The default value is &#x60;10000000&#x60;. | [optional] 
**max_msg_spool_usage** | **int** | The max spool usage (in MB) of this Queue. Setting the value to 0 enables the last-value-queue feature and disables quota checking. The default varies by platform. | [optional] 
**max_redelivery_count** | **int** | The maximum number of times the Queue will attempt redelivery of a given message prior to it being discarded or moved to the #DEAD_MSG_QUEUE. A value of 0 means to retry forever. The default value is &#x60;0&#x60;. | [optional] 
**max_ttl** | **int** | The maximum number of seconds that a message can stay in the Queue when &#x60;respectTtlEnabled&#x60; is &#x60;true&#x60;. A message will expire according to the lesser of the TTL in the message (assigned by the publisher) and the &#x60;maxTtl&#x60; configured on the Queue. &#x60;maxTtl&#x60; is a 32-bit integer value from 1 to 4294967295 representing the expiry time in seconds. A &#x60;maxTtl&#x60; of &#x60;0&#x60; disables this feature. The default value is &#x60;0&#x60;. | [optional] 
**msg_vpn_name** | **str** | The name of the Message VPN. | [optional] 
**owner** | **str** | The Client Username which owns the Queue. The default is to have no &#x60;owner&#x60;. | [optional] 
**permission** | **str** | Permission level for users of the Queue, excluding the owner. The default value is &#x60;\&quot;no-access\&quot;&#x60;. The allowed values and their meaning are:      \&quot;no-access\&quot; - Disallows all access.     \&quot;read-only\&quot; - Read-only access to the messages in the Queue.     \&quot;consume\&quot; - Consume (read and remove) messages in the Queue.     \&quot;modify-topic\&quot; - Consume messages or modify the topic/selector of the Queue.     \&quot;delete\&quot; - Consume messages, modify the topic/selector or delete the Queue altogether.  | [optional] 
**queue_name** | **str** | The name of the Queue. | [optional] 
**reject_low_priority_msg_enabled** | **bool** | Enable or disable if low priority messages are subject to &#x60;rejectLowPriorityMsgLimit&#x60; checking. This may only be enabled if &#x60;rejectMsgToSenderOnDiscardBehavior&#x60; does not have a value of &#x60;\&quot;never\&quot;&#x60;. The default value is &#x60;false&#x60;. | [optional] 
**reject_low_priority_msg_limit** | **int** | The number of messages of any priority in the Queue above which low priority messages are not admitted but higher priority messages are allowed. The default value is &#x60;0&#x60;. | [optional] 
**reject_msg_to_sender_on_discard_behavior** | **str** | The circumstances under which a nack is sent to the client on discards. Note that nacks cause the message to not be delivered to any destination and transacted-session commits to fail. This attribute may only have a value of &#x60;\&quot;never\&quot;&#x60; if &#x60;rejectLowPriorityMsgEnabled&#x60; is disabled. The default value is &#x60;\&quot;when-queue-enabled\&quot;&#x60;. The allowed values and their meaning are:      \&quot;when-queue-enabled\&quot; - Message discards result in nacks being returned to the sending client, except if the discard reason is that the queue is disabled.     \&quot;never\&quot; - Message discards never result in nacks being returned to the sending client.  Available since 2.1.0. | [optional] 
**respect_ttl_enabled** | **bool** | Enable or disable the respecting of TTL. If enabled, then messages contained in the Queue are checked for expiry. If expired, the message is removed from the Queue and either discarded or a copy of the message placed in the #DEAD_MSG_QUEUE. The default value is &#x60;false&#x60;. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


