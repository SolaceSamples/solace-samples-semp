
# MsgVpnQueue

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**accessType** | [**AccessTypeEnum**](#AccessTypeEnum) | The access type for delivering messages to consumer flows bound to the Queue. The default value is &#x60;\&quot;exclusive\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;exclusive\&quot; - Exclusive delivery of messages to the first bound consumer flow. \&quot;non-exclusive\&quot; - Non-exclusive delivery of messages to all bound consumer flows in a round-robin fashion. &lt;/pre&gt;  |  [optional]
**consumerAckPropagationEnabled** | **Boolean** | Enable or disable the propagation of consumer acknowledgements (ACKs) received on the active replication Message VPN to the standby replication Message VPN. The default value is &#x60;true&#x60;. |  [optional]
**deadMsgQueue** | **String** | The name of the Dead Message Queue (DMQ) used by the Queue. The default value is &#x60;\&quot;#DEAD_MSG_QUEUE\&quot;&#x60;. Available since 2.2. |  [optional]
**egressEnabled** | **Boolean** | Enable or disable the transmission of messages from the Queue. The default value is &#x60;false&#x60;. |  [optional]
**eventBindCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventMsgSpoolUsageThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventRejectLowPriorityMsgLimitThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**ingressEnabled** | **Boolean** | Enable or disable the reception of messages to the Queue. The default value is &#x60;false&#x60;. |  [optional]
**maxBindCount** | **Long** | The maximum number of consumer flows that can bind to the Queue. The default value is &#x60;1000&#x60;. |  [optional]
**maxDeliveredUnackedMsgsPerFlow** | **Long** | The maximum number of messages delivered but not acknowledged per flow for the Queue. The default is the max value supported by the platform. |  [optional]
**maxMsgSize** | **Integer** | The maximum message size allowed in the Queue, in bytes (B). The default value is &#x60;10000000&#x60;. |  [optional]
**maxMsgSpoolUsage** | **Long** | The maximum message spool usage allowed by the Queue, in megabytes (MB). A value of 0 only allows spooling of the last message received and disables quota checking. The default varies by platform. |  [optional]
**maxRedeliveryCount** | **Long** | The maximum number of times the Queue will attempt redelivery of a message prior to it being discarded or moved to the DMQ. A value of 0 means to retry forever. The default value is &#x60;0&#x60;. |  [optional]
**maxTtl** | **Long** | The maximum time in seconds a message can stay in the Queue when &#x60;respectTtlEnabled&#x60; is &#x60;\&quot;true\&quot;&#x60;. A message expires when the lesser of the sender assigned time-to-live (TTL) in the message and the &#x60;maxTtl&#x60; configured for the Queue, is exceeded. A value of 0 disables expiry. The default value is &#x60;0&#x60;. |  [optional]
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]
**owner** | **String** | The Client Username that owns the Queue and has permission equivalent to &#x60;\&quot;delete\&quot;&#x60;. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**permission** | [**PermissionEnum**](#PermissionEnum) | The permission level for all consumers of the Queue, excluding the owner. The default value is &#x60;\&quot;no-access\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;no-access\&quot; - Disallows all access. \&quot;read-only\&quot; - Read-only access to the messages. \&quot;consume\&quot; - Consume (read and remove) messages. \&quot;modify-topic\&quot; - Consume messages or modify the topic/selector. \&quot;delete\&quot; - Consume messages, modify the topic/selector or delete the Client created endpoint altogether. &lt;/pre&gt;  |  [optional]
**queueName** | **String** | The name of the Queue. |  [optional]
**rejectLowPriorityMsgEnabled** | **Boolean** | Enable or disable the checking of low priority messages against the &#x60;rejectLowPriorityMsgLimit&#x60;. This may only be enabled if &#x60;rejectMsgToSenderOnDiscardBehavior&#x60; does not have a value of &#x60;\&quot;never\&quot;&#x60;. The default value is &#x60;false&#x60;. |  [optional]
**rejectLowPriorityMsgLimit** | **Long** | The number of messages of any priority in the Queue above which low priority messages are not admitted but higher priority messages are allowed. The default value is &#x60;0&#x60;. |  [optional]
**rejectMsgToSenderOnDiscardBehavior** | [**RejectMsgToSenderOnDiscardBehaviorEnum**](#RejectMsgToSenderOnDiscardBehaviorEnum) | Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. The default value is &#x60;\&quot;when-queue-enabled\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always return a negative acknowledgment (NACK) to the sending client on message discard. \&quot;when-queue-enabled\&quot; - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Queue is enabled. \&quot;never\&quot; - Never return a negative acknowledgment (NACK) to the sending client on message discard. &lt;/pre&gt;  Available since 2.1. |  [optional]
**respectMsgPriorityEnabled** | **Boolean** | Enable or disable the respecting of message priority. When enabled, messages contained in the Queue are delivered in priority order, from 9 (highest) to 0 (lowest). MQTT queues do not support enabling message priority. The default value is &#x60;false&#x60;. Available since 2.8. |  [optional]
**respectTtlEnabled** | **Boolean** | Enable or disable the respecting of the time-to-live (TTL) for messages in the Queue. When enabled, expired messages are discarded or moved to the DMQ. The default value is &#x60;false&#x60;. |  [optional]


<a name="AccessTypeEnum"></a>
## Enum: AccessTypeEnum
Name | Value
---- | -----
EXCLUSIVE | &quot;exclusive&quot;
NON_EXCLUSIVE | &quot;non-exclusive&quot;


<a name="PermissionEnum"></a>
## Enum: PermissionEnum
Name | Value
---- | -----
NO_ACCESS | &quot;no-access&quot;
READ_ONLY | &quot;read-only&quot;
CONSUME | &quot;consume&quot;
MODIFY_TOPIC | &quot;modify-topic&quot;
DELETE | &quot;delete&quot;


<a name="RejectMsgToSenderOnDiscardBehaviorEnum"></a>
## Enum: RejectMsgToSenderOnDiscardBehaviorEnum
Name | Value
---- | -----
ALWAYS | &quot;always&quot;
WHEN_QUEUE_ENABLED | &quot;when-queue-enabled&quot;
NEVER | &quot;never&quot;



