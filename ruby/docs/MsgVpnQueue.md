# SempClient::MsgVpnQueue

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**access_type** | **String** | The queue access type of either exclusive or non-exclusive. The default value is &#x60;\&quot;exclusive\&quot;&#x60;. The allowed values and their meaning are:      \&quot;exclusive\&quot; - Exclusive delivery of messages to first bound client.     \&quot;non-exclusive\&quot; - Non-exclusive delivery of messages to all bound clients.  | [optional] 
**consumer_ack_propagation_enabled** | **BOOLEAN** | Enable or disable the propagation of consumer acks received on the active replication Message VPN to the standby replication Message VPN. The default value is &#x60;true&#x60;. | [optional] 
**dead_msg_queue** | **String** | Identifies the name of the queue which should be used as the queue&#39;s dead message queue. The default value is &#x60;\&quot;#DEAD_MSG_QUEUE\&quot;&#x60;. | [optional] 
**egress_enabled** | **BOOLEAN** | Enable or disable the flow of messages from a queue. The default value is &#x60;false&#x60;. | [optional] 
**event_bind_count_threshold** | [**EventThreshold**](EventThreshold.md) |  | [optional] 
**event_msg_spool_usage_threshold** | [**EventThreshold**](EventThreshold.md) |  | [optional] 
**event_reject_low_priority_msg_limit_threshold** | [**EventThreshold**](EventThreshold.md) |  | [optional] 
**ingress_enabled** | **BOOLEAN** | Enable or disable the flow of messages to a queue. The default value is &#x60;false&#x60;. | [optional] 
**max_bind_count** | **Integer** | The maximum number of times a client(s) can bind to a given queue. The default value is &#x60;1000&#x60;. | [optional] 
**max_delivered_unacked_msgs_per_flow** | **Integer** | The max messages delivered but not acknowledged per flow for this queue. The default is the max value supported by the hardware. | [optional] 
**max_msg_size** | **Integer** | The max message size (in bytes) allowed in this queue. The default value is &#x60;10000000&#x60;. | [optional] 
**max_msg_spool_usage** | **Integer** | The max spool usage (in MB) of this queue. Setting the value to 0 enables the last-value-queue feature and disables quota checking. The default value is &#x60;4000&#x60;. | [optional] 
**max_redelivery_count** | **Integer** | The maximum number of times the queue will attempt redelivery of a given message prior to it being discarded or moved to the #DEAD_MSG_QUEUE. A value of 0 means to retry forever. The default value is &#x60;0&#x60;. | [optional] 
**max_ttl** | **Integer** | The maximum number of seconds that a message can stay in a queue or topic-endpoint when respect-ttl is enabled. A message will expire according to the lesser of the TTL in the message (assigned by the publisher) and the max-ttl configured on the endpoint. The max-ttl is a 32 bit integer value from 1 to 4294967295 representing the expiry time in seconds. A max-ttl of 0 disables this feature. The default value is &#x60;0&#x60;. | [optional] 
**msg_vpn_name** | **String** | The name of the Message VPN. | [optional] 
**owner** | **String** | The client-username owner of the queue. The default is to have no &#x60;owner&#x60;. | [optional] 
**permission** | **String** | Permission level for users of the queue, excluding the owner. The default value is &#x60;\&quot;no-access\&quot;&#x60;. The allowed values and their meaning are:      \&quot;no-access\&quot; - Disallows all access.     \&quot;read-only\&quot; - Read-only access to the messages in the queue.     \&quot;consume\&quot; - Consume (read and remove) messages in the queue.     \&quot;modify-topic\&quot; - Consume messages or modify the topic/selector of the queue.     \&quot;delete\&quot; - Consume messages or delete the queue altogether.  | [optional] 
**queue_name** | **String** | The name of the Queue. | [optional] 
**reject_low_priority_msg_enabled** | **BOOLEAN** | Enable or disable if low priority messages are subject to &#x60;rejectLowPriorityMsgLimit&#x60; checking. This may only be enabled if &#x60;rejectMsgToSenderOnDiscardBehavior&#x60; does not have a value of &#x60;\&quot;never\&quot;&#x60;. The default value is &#x60;false&#x60;. | [optional] 
**reject_low_priority_msg_limit** | **Integer** | The number of messages of any priority queued to an endpoint above which low priority messages are not admitted but higher priority messages are allowed into the endpoint. The default value is &#x60;0&#x60;. | [optional] 
**reject_msg_to_sender_on_discard_behavior** | **String** | The circumstances under which a nack is sent to the client on discards. Note that nacks cause the message to not be delivered to any destination and transacted-session commits to fail. This attribute may only have a value of &#x60;\&quot;never\&quot;&#x60; if &#x60;rejectLowPriorityMsgEnabled&#x60; is disabled. The default value is &#x60;\&quot;when-queue-enabled\&quot;&#x60;. The allowed values and their meaning are:      \&quot;always\&quot; - Message discards always result in nacks being returned to the sending client, even if the discard reason is that the queue is disabled.     \&quot;when-queue-enabled\&quot; - Message discards result in nacks being returned to the sending client, except if the discard reason is that the queue is disabled.     \&quot;never\&quot; - Message discards never result in nacks being returned to the sending client.  | [optional] 
**respect_ttl_enabled** | **BOOLEAN** | Enable or disable the respecting of TTL. If enabled, then messages contained in the queue are checked for expiry. If expired, the message is removed from the queue and either discarded or a copy of the message placed in the #DEAD_MSG_QUEUE. The default value is &#x60;false&#x60;. | [optional] 


