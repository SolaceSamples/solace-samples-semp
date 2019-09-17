
# MsgVpnJndiConnectionFactory

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**allowDuplicateClientIdEnabled** | **Boolean** | Enable or disable whether new JMS connections can use the same Client identifier (ID) as an existing connection. The default value is &#x60;false&#x60;. Available since 2.3. |  [optional]
**clientDescription** | **String** | The description of the Client. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**clientId** | **String** | The Client identifier (ID). If not specified, a unique value for it will be generated. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**connectionFactoryName** | **String** | The name of the JMS Connection Factory. |  [optional]
**dtoReceiveOverrideEnabled** | **Boolean** | Enable or disable overriding by the Subscriber (Consumer) of the deliver-to-one (DTO) property on messages. When enabled, the Subscriber can receive all DTO tagged messages. The default value is &#x60;true&#x60;. |  [optional]
**dtoReceiveSubscriberLocalPriority** | **Integer** | The priority for receiving deliver-to-one (DTO) messages by the Subscriber (Consumer) if the messages are published on the local broker that the Subscriber is directly connected to. The default value is &#x60;1&#x60;. |  [optional]
**dtoReceiveSubscriberNetworkPriority** | **Integer** | The priority for receiving deliver-to-one (DTO) messages by the Subscriber (Consumer) if the messages are published on a remote broker. The default value is &#x60;1&#x60;. |  [optional]
**dtoSendEnabled** | **Boolean** | Enable or disable the deliver-to-one (DTO) property on messages sent by the Publisher (Producer). The default value is &#x60;false&#x60;. |  [optional]
**dynamicEndpointCreateDurableEnabled** | **Boolean** | Enable or disable whether a durable endpoint will be dynamically created on the broker when the client calls \&quot;Session.createDurableSubscriber()\&quot; or \&quot;Session.createQueue()\&quot;. The created endpoint respects the message time-to-live (TTL) according to the \&quot;dynamicEndpointRespectTtlEnabled\&quot; property. The default value is &#x60;false&#x60;. |  [optional]
**dynamicEndpointRespectTtlEnabled** | **Boolean** | Enable or disable whether dynamically created durable and non-durable endpoints respect the message time-to-live (TTL) property. The default value is &#x60;true&#x60;. |  [optional]
**guaranteedReceiveAckTimeout** | **Integer** | The timeout for sending the acknowledgement (ACK) for guaranteed messages received by the Subscriber (Consumer), in milliseconds. The default value is &#x60;1000&#x60;. |  [optional]
**guaranteedReceiveWindowSize** | **Integer** | The size of the window for guaranteed messages received by the Subscriber (Consumer), in messages. The default value is &#x60;18&#x60;. |  [optional]
**guaranteedReceiveWindowSizeAckThreshold** | **Integer** | The threshold for sending the acknowledgement (ACK) for guaranteed messages received by the Subscriber (Consumer) as a percentage of the \&quot;guaranteedReceiveWindowSize\&quot; value. The default value is &#x60;60&#x60;. |  [optional]
**guaranteedSendAckTimeout** | **Integer** | The timeout for receiving the acknowledgement (ACK) for guaranteed messages sent by the Publisher (Producer), in milliseconds. The default value is &#x60;2000&#x60;. |  [optional]
**guaranteedSendWindowSize** | **Integer** | The size of the window for non-persistent guaranteed messages sent by the Publisher (Producer), in messages. For persistent messages the window size is fixed at 1. The default value is &#x60;255&#x60;. |  [optional]
**messagingDefaultDeliveryMode** | [**MessagingDefaultDeliveryModeEnum**](#MessagingDefaultDeliveryModeEnum) | The default delivery mode for messages sent by the Publisher (Producer). The default value is &#x60;\&quot;persistent\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;persistent\&quot; - The broker spools messages (persists in the Message Spool) as part of the send operation. \&quot;non-persistent\&quot; - The broker does not spool messages (does not persist in the Message Spool) as part of the send operation. &lt;/pre&gt;  |  [optional]
**messagingDefaultDmqEligibleEnabled** | **Boolean** | Enable or disable whether messages sent by the Publisher (Producer) are Dead Message Queue (DMQ) eligible by default. The default value is &#x60;false&#x60;. |  [optional]
**messagingDefaultElidingEligibleEnabled** | **Boolean** | Enable or disable whether messages sent by the Publisher (Producer) are Eliding eligible by default. The default value is &#x60;false&#x60;. |  [optional]
**messagingJmsxUserIdEnabled** | **Boolean** | Enable or disable inclusion (adding or replacing) of the JMSXUserID property in messages sent by the Publisher (Producer). The default value is &#x60;false&#x60;. |  [optional]
**messagingTextInXmlPayloadEnabled** | **Boolean** | Enable or disable encoding of JMS text messages in Publisher (Producer) messages as XML payload. When disabled, JMS text messages are encoded as a binary attachment. The default value is &#x60;true&#x60;. |  [optional]
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]
**transportCompressionLevel** | **Integer** | The ZLIB compression level for the connection to the broker. The value \&quot;0\&quot; means no compression, and the value \&quot;-1\&quot; means the compression level is specified in the JNDI Properties file. The default value is &#x60;-1&#x60;. |  [optional]
**transportConnectRetryCount** | **Integer** | The maximum number of retry attempts to establish an initial connection to the host or list of hosts. The value \&quot;0\&quot; means a single attempt (no retries), and the value \&quot;-1\&quot; means to retry forever. The default value is &#x60;0&#x60;. |  [optional]
**transportConnectRetryPerHostCount** | **Integer** | The maximum number of retry attempts to establish an initial connection to each host on the list of hosts. The value \&quot;0\&quot; means a single attempt (no retries), and the value \&quot;-1\&quot; means to retry forever. The default value is &#x60;0&#x60;. |  [optional]
**transportConnectTimeout** | **Integer** | The timeout for establishing an initial connection to the broker, in milliseconds. The default value is &#x60;30000&#x60;. |  [optional]
**transportDirectTransportEnabled** | **Boolean** | Enable or disable usage of the Direct Transport mode for sending non-persistent messages. When disabled, the Guaranteed Transport mode is used. The default value is &#x60;true&#x60;. |  [optional]
**transportKeepaliveCount** | **Integer** | The maximum number of consecutive application-level keepalive messages sent without the broker response before the connection to the broker is closed. The default value is &#x60;3&#x60;. |  [optional]
**transportKeepaliveEnabled** | **Boolean** | Enable or disable usage of application-level keepalive messages to maintain a connection with the broker. The default value is &#x60;true&#x60;. |  [optional]
**transportKeepaliveInterval** | **Integer** | The interval between application-level keepalive messages, in milliseconds. The default value is &#x60;3000&#x60;. |  [optional]
**transportMsgCallbackOnIoThreadEnabled** | **Boolean** | Enable or disable delivery of asynchronous messages directly from the I/O thread. Contact Solace Support before enabling this property. The default value is &#x60;false&#x60;. |  [optional]
**transportOptimizeDirectEnabled** | **Boolean** | Enable or disable optimization for the Direct Transport delivery mode. If enabled, the client application is limited to one Publisher (Producer) and one non-durable Subscriber (Consumer). The default value is &#x60;false&#x60;. |  [optional]
**transportPort** | **Integer** | The connection port number on the broker for SMF clients. The value \&quot;-1\&quot; means the port is specified in the JNDI Properties file. The default value is &#x60;-1&#x60;. |  [optional]
**transportReadTimeout** | **Integer** | The timeout for reading a reply from the broker, in milliseconds. The default value is &#x60;10000&#x60;. |  [optional]
**transportReceiveBufferSize** | **Integer** | The size of the receive socket buffer, in bytes. It corresponds to the SO_RCVBUF socket option. The default value is &#x60;65536&#x60;. |  [optional]
**transportReconnectRetryCount** | **Integer** | The maximum number of attempts to reconnect to the host or list of hosts after the connection has been lost. The value \&quot;-1\&quot; means to retry forever. The default value is &#x60;3&#x60;. |  [optional]
**transportReconnectRetryWait** | **Integer** | The amount of time before making another attempt to connect or reconnect to the host after the connection has been lost, in milliseconds. The default value is &#x60;3000&#x60;. |  [optional]
**transportSendBufferSize** | **Integer** | The size of the send socket buffer, in bytes. It corresponds to the SO_SNDBUF socket option. The default value is &#x60;65536&#x60;. |  [optional]
**transportTcpNoDelayEnabled** | **Boolean** | Enable or disable the TCP_NODELAY option. When enabled, Nagle&#39;s algorithm for TCP/IP congestion control (RFC 896) is disabled. The default value is &#x60;true&#x60;. |  [optional]
**xaEnabled** | **Boolean** | Enable or disable this as an XA Connection Factory. When enabled, the Connection Factory can be cast to \&quot;XAConnectionFactory\&quot;, \&quot;XAQueueConnectionFactory\&quot; or \&quot;XATopicConnectionFactory\&quot;. The default value is &#x60;false&#x60;. |  [optional]


<a name="MessagingDefaultDeliveryModeEnum"></a>
## Enum: MessagingDefaultDeliveryModeEnum
Name | Value
---- | -----
PERSISTENT | &quot;persistent&quot;
NON_PERSISTENT | &quot;non-persistent&quot;



