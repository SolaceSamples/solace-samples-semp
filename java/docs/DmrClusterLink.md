
# DmrClusterLink

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**authenticationBasicPassword** | **String** | The password used to authenticate with the remote node when using basic internal authentication. If this per-Link password is not configured, the Cluster&#39;s password is used instead. The default is to have no &#x60;authenticationBasicPassword&#x60;. |  [optional]
**authenticationScheme** | [**AuthenticationSchemeEnum**](#AuthenticationSchemeEnum) | The authentication scheme to be used by the Link which initiates connections to the remote node. The default value is &#x60;\&quot;basic\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;basic\&quot; - Basic Authentication Scheme (via username and password). \&quot;client-certificate\&quot; - Client Certificate Authentication Scheme (via certificate file or content). &lt;/pre&gt;  |  [optional]
**clientProfileQueueControl1MaxDepth** | **Integer** | The maximum depth of the \&quot;Control 1\&quot; (C-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is &#x60;20000&#x60;. |  [optional]
**clientProfileQueueControl1MinMsgBurst** | **Integer** | The number of messages that are always allowed entry into the \&quot;Control 1\&quot; (C-1) priority queue, regardless of the &#x60;clientProfileQueueControl1MaxDepth&#x60; value. The default value is &#x60;4&#x60;. |  [optional]
**clientProfileQueueDirect1MaxDepth** | **Integer** | The maximum depth of the \&quot;Direct 1\&quot; (D-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is &#x60;20000&#x60;. |  [optional]
**clientProfileQueueDirect1MinMsgBurst** | **Integer** | The number of messages that are always allowed entry into the \&quot;Direct 1\&quot; (D-1) priority queue, regardless of the &#x60;clientProfileQueueDirect1MaxDepth&#x60; value. The default value is &#x60;4&#x60;. |  [optional]
**clientProfileQueueDirect2MaxDepth** | **Integer** | The maximum depth of the \&quot;Direct 2\&quot; (D-2) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is &#x60;20000&#x60;. |  [optional]
**clientProfileQueueDirect2MinMsgBurst** | **Integer** | The number of messages that are always allowed entry into the \&quot;Direct 2\&quot; (D-2) priority queue, regardless of the &#x60;clientProfileQueueDirect2MaxDepth&#x60; value. The default value is &#x60;4&#x60;. |  [optional]
**clientProfileQueueDirect3MaxDepth** | **Integer** | The maximum depth of the \&quot;Direct 3\&quot; (D-3) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is &#x60;20000&#x60;. |  [optional]
**clientProfileQueueDirect3MinMsgBurst** | **Integer** | The number of messages that are always allowed entry into the \&quot;Direct 3\&quot; (D-3) priority queue, regardless of the &#x60;clientProfileQueueDirect3MaxDepth&#x60; value. The default value is &#x60;4&#x60;. |  [optional]
**clientProfileQueueGuaranteed1MaxDepth** | **Integer** | The maximum depth of the \&quot;Guaranteed 1\&quot; (G-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is &#x60;20000&#x60;. |  [optional]
**clientProfileQueueGuaranteed1MinMsgBurst** | **Integer** | The number of messages that are always allowed entry into the \&quot;Guaranteed 1\&quot; (G-3) priority queue, regardless of the &#x60;clientProfileQueueGuaranteed1MaxDepth&#x60; value. The default value is &#x60;255&#x60;. |  [optional]
**clientProfileTcpCongestionWindowSize** | **Long** | The TCP initial congestion window size, in multiples of the TCP Maximum Segment Size (MSS). Changing the value from its default of 2 results in non-compliance with RFC 2581. Contact Solace Support before changing this value. The default value is &#x60;2&#x60;. |  [optional]
**clientProfileTcpKeepaliveCount** | **Long** | The number of TCP keepalive retransmissions to be carried out before declaring that the remote end is not available. The default value is &#x60;5&#x60;. |  [optional]
**clientProfileTcpKeepaliveIdleTime** | **Long** | The amount of time a connection must remain idle before TCP begins sending keepalive probes, in seconds. The default value is &#x60;3&#x60;. |  [optional]
**clientProfileTcpKeepaliveInterval** | **Long** | The amount of time between TCP keepalive retransmissions when no acknowledgement is received, in seconds. The default value is &#x60;1&#x60;. |  [optional]
**clientProfileTcpMaxSegmentSize** | **Long** | The TCP maximum segment size, in kilobytes. Changes are applied to all existing connections. The default value is &#x60;1460&#x60;. |  [optional]
**clientProfileTcpMaxWindowSize** | **Long** | The TCP maximum window size, in kilobytes. Changes are applied to all existing connections. The default value is &#x60;256&#x60;. |  [optional]
**dmrClusterName** | **String** | The name of the Cluster. |  [optional]
**egressFlowWindowSize** | **Long** | The number of outstanding guaranteed messages that can be sent over the Link before acknowledgement is received by the sender. The default value is &#x60;255&#x60;. |  [optional]
**enabled** | **Boolean** | Enable or disable the Link. When disabled, subscription sets of this and the remote node are not kept up-to-date, and messages are not exchanged with the remote node. Published guaranteed messages will be queued up for future delivery based on current subscription sets. The default value is &#x60;false&#x60;. |  [optional]
**initiator** | [**InitiatorEnum**](#InitiatorEnum) | The initiator of the Link&#39;s TCP connections. The default value is &#x60;\&quot;lexical\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;lexical\&quot; - The \&quot;higher\&quot; node-name initiates. \&quot;local\&quot; - The local node initiates. \&quot;remote\&quot; - The remote node initiates. &lt;/pre&gt;  |  [optional]
**queueDeadMsgQueue** | **String** | The name of the Dead Message Queue (DMQ) used by the Queue for discarded messages. The default value is &#x60;\&quot;#DEAD_MSG_QUEUE\&quot;&#x60;. |  [optional]
**queueEventSpoolUsageThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**queueMaxDeliveredUnackedMsgsPerFlow** | **Long** | The maximum number of messages delivered but not acknowledged per flow for the Queue. The default is the max value supported by the platform. |  [optional]
**queueMaxMsgSpoolUsage** | **Long** | The maximum message spool usage by the Queue (quota), in megabytes (MB). The default varies by platform. |  [optional]
**queueMaxRedeliveryCount** | **Long** | The maximum number of times the Queue will attempt redelivery of a message prior to it being discarded or moved to the DMQ. A value of 0 means to retry forever. The default value is &#x60;0&#x60;. |  [optional]
**queueMaxTtl** | **Long** | The maximum time in seconds a message can stay in the Queue when &#x60;queueRespectTtlEnabled&#x60; is &#x60;true&#x60;. A message expires when the lesser of the sender assigned time-to-live (TTL) in the message and the &#x60;queueMaxTtl&#x60; configured for the Queue, is exceeded. A value of 0 disables expiry. The default value is &#x60;0&#x60;. |  [optional]
**queueRejectMsgToSenderOnDiscardBehavior** | [**QueueRejectMsgToSenderOnDiscardBehaviorEnum**](#QueueRejectMsgToSenderOnDiscardBehaviorEnum) | Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. The default value is &#x60;\&quot;always\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;always\&quot; - Always return a negative acknowledgment (NACK) to the sending client on message discard. \&quot;when-queue-enabled\&quot; - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Queue is enabled. \&quot;never\&quot; - Never return a negative acknowledgment (NACK) to the sending client on message discard. &lt;/pre&gt;  |  [optional]
**queueRespectTtlEnabled** | **Boolean** | Enable or disable the respecting of the time-to-live (TTL) for messages in the Queue. When enabled, expired messages are discarded or moved to the DMQ. The default value is &#x60;false&#x60;. |  [optional]
**remoteNodeName** | **String** | The name of the node at the remote end of the Link. |  [optional]
**span** | [**SpanEnum**](#SpanEnum) | The span of the Link, either internal or external. Internal Links connect nodes within the same Cluster. External Links connect nodes within different Clusters. The default value is &#x60;\&quot;external\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;internal\&quot; - Link to same cluster. \&quot;external\&quot; - Link to other cluster. &lt;/pre&gt;  |  [optional]
**transportCompressedEnabled** | **Boolean** | Enable or disable compression on the Link. The default value is &#x60;false&#x60;. |  [optional]
**transportTlsEnabled** | **Boolean** | Enable or disable encryption on the Link. The default value is &#x60;false&#x60;. |  [optional]


<a name="AuthenticationSchemeEnum"></a>
## Enum: AuthenticationSchemeEnum
Name | Value
---- | -----
BASIC | &quot;basic&quot;
CLIENT_CERTIFICATE | &quot;client-certificate&quot;


<a name="InitiatorEnum"></a>
## Enum: InitiatorEnum
Name | Value
---- | -----
LEXICAL | &quot;lexical&quot;
LOCAL | &quot;local&quot;
REMOTE | &quot;remote&quot;


<a name="QueueRejectMsgToSenderOnDiscardBehaviorEnum"></a>
## Enum: QueueRejectMsgToSenderOnDiscardBehaviorEnum
Name | Value
---- | -----
ALWAYS | &quot;always&quot;
WHEN_QUEUE_ENABLED | &quot;when-queue-enabled&quot;
NEVER | &quot;never&quot;


<a name="SpanEnum"></a>
## Enum: SpanEnum
Name | Value
---- | -----
INTERNAL | &quot;internal&quot;
EXTERNAL | &quot;external&quot;



