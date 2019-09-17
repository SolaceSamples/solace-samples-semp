
# MsgVpn

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**authenticationBasicEnabled** | **Boolean** | Enable or disable basic authentication for clients connecting to the Message VPN. The default value is &#x60;true&#x60;. |  [optional]
**authenticationBasicProfileName** | **String** | The name of the RADIUS or LDAP Profile to use for basic authentication. The default value is &#x60;\&quot;default\&quot;&#x60;. |  [optional]
**authenticationBasicRadiusDomain** | **String** | The RADIUS domain to use for basic authentication. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**authenticationBasicType** | [**AuthenticationBasicTypeEnum**](#AuthenticationBasicTypeEnum) | The type of basic authentication to use for clients connecting to the Message VPN. The default value is &#x60;\&quot;radius\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;internal\&quot; - Internal database. Authentication is against Client Usernames. \&quot;ldap\&quot; - LDAP authentication. An LDAP profile name must be provided. \&quot;radius\&quot; - RADIUS authentication. A RADIUS profile name must be provided. \&quot;none\&quot; - No authentication. Anonymous login allowed. &lt;/pre&gt;  |  [optional]
**authenticationClientCertAllowApiProvidedUsernameEnabled** | **Boolean** | Enable or disable allowing a client to specify a Client Username via the API connect method. When disabled, the certificate CN (Common Name) is always used. The default value is &#x60;false&#x60;. |  [optional]
**authenticationClientCertEnabled** | **Boolean** | Enable or disable client certificate authentication in the Message VPN. The default value is &#x60;false&#x60;. |  [optional]
**authenticationClientCertMaxChainDepth** | **Long** | The maximum depth for a client certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is &#x60;3&#x60;. |  [optional]
**authenticationClientCertRevocationCheckMode** | [**AuthenticationClientCertRevocationCheckModeEnum**](#AuthenticationClientCertRevocationCheckModeEnum) | The desired behavior for client certificate revocation checking. The default value is &#x60;\&quot;allow-valid\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;allow-all\&quot; - Allow the client to authenticate, the result of client certificate revocation check is ignored. \&quot;allow-unknown\&quot; - Allow the client to authenticate even if the revocation status of his certificate cannot be determined. \&quot;allow-valid\&quot; - Allow the client to authenticate only when the revocation check returned an explicit positive response. &lt;/pre&gt;  Available since 2.6. |  [optional]
**authenticationClientCertUsernameSource** | [**AuthenticationClientCertUsernameSourceEnum**](#AuthenticationClientCertUsernameSourceEnum) | The field from the client certificate to use as the client username. The default value is &#x60;\&quot;common-name\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;common-name\&quot; - The username is extracted from the certificate&#39;s Common Name. \&quot;subject-alternate-name-msupn\&quot; - The username is extracted from the certificate&#39;s Other Name type of the Subject Alternative Name and must have the msUPN signature. &lt;/pre&gt;  Available since 2.5. |  [optional]
**authenticationClientCertValidateDateEnabled** | **Boolean** | Enable or disable validation of the \&quot;Not Before\&quot; and \&quot;Not After\&quot; validity dates in the client certificate. The default value is &#x60;true&#x60;. |  [optional]
**authenticationKerberosAllowApiProvidedUsernameEnabled** | **Boolean** | Enable or disable allowing a client to specify a Client Username via the API connect method. When disabled, the Kerberos Principal name is always used. The default value is &#x60;false&#x60;. |  [optional]
**authenticationKerberosEnabled** | **Boolean** | Enable or disable Kerberos authentication in the Message VPN. The default value is &#x60;false&#x60;. |  [optional]
**authorizationLdapGroupMembershipAttributeName** | **String** | The name of the attribute that is retrieved from the LDAP server as part of the LDAP search when authorizing a client connecting to the Message VPN. The default value is &#x60;\&quot;memberOf\&quot;&#x60;. |  [optional]
**authorizationProfileName** | **String** | The name of the LDAP Profile to use for client authorization. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**authorizationType** | [**AuthorizationTypeEnum**](#AuthorizationTypeEnum) | The type of authorization to use for clients connecting to the Message VPN. The default value is &#x60;\&quot;internal\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;ldap\&quot; - LDAP authorization. \&quot;internal\&quot; - Internal authorization. &lt;/pre&gt;  |  [optional]
**bridgingTlsServerCertEnforceTrustedCommonNameEnabled** | **Boolean** | Enable or disable validation of the Common Name (CN) in the server certificate from the remote broker. If enabled, the Common Name is checked against the list of Trusted Common Names configured for the Bridge. The default value is &#x60;true&#x60;. |  [optional]
**bridgingTlsServerCertMaxChainDepth** | **Long** | The maximum depth for a server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is &#x60;3&#x60;. |  [optional]
**bridgingTlsServerCertValidateDateEnabled** | **Boolean** | Enable or disable validation of the \&quot;Not Before\&quot; and \&quot;Not After\&quot; validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid based on these dates. The default value is &#x60;true&#x60;. |  [optional]
**distributedCacheManagementEnabled** | **Boolean** | Enable or disable managing of cache instances over the message bus. The default value is &#x60;true&#x60;. |  [optional]
**dmrEnabled** | **Boolean** | Enable or disable Dynamic Message Routing (DMR) for the Message VPN. The default value is &#x60;false&#x60;. Available since 2.11. |  [optional]
**enabled** | **Boolean** | Enable or disable the Message VPN. The default value is &#x60;false&#x60;. |  [optional]
**eventConnectionCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventEgressFlowCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventEgressMsgRateThreshold** | [**EventThresholdByValue**](EventThresholdByValue.md) |  |  [optional]
**eventEndpointCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventIngressFlowCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventIngressMsgRateThreshold** | [**EventThresholdByValue**](EventThresholdByValue.md) |  |  [optional]
**eventLargeMsgThreshold** | **Long** | The threshold, in kilobytes, after which a message is considered to be large for the Message VPN. The default value is &#x60;1024&#x60;. |  [optional]
**eventLogTag** | **String** | A prefix applied to all published Events in the Message VPN. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**eventMsgSpoolUsageThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventPublishClientEnabled** | **Boolean** | Enable or disable Client level Event message publishing. The default value is &#x60;false&#x60;. |  [optional]
**eventPublishMsgVpnEnabled** | **Boolean** | Enable or disable Message VPN level Event message publishing. The default value is &#x60;false&#x60;. |  [optional]
**eventPublishSubscriptionMode** | [**EventPublishSubscriptionModeEnum**](#EventPublishSubscriptionModeEnum) | Subscription level Event message publishing mode. The default value is &#x60;\&quot;off\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;off\&quot; - Disable client level event message publishing. \&quot;on-with-format-v1\&quot; - Enable client level event message publishing with format v1. \&quot;on-with-no-unsubscribe-events-on-disconnect-format-v1\&quot; - As \&quot;on-with-format-v1\&quot;, but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. \&quot;on-with-format-v2\&quot; - Enable client level event message publishing with format v2. \&quot;on-with-no-unsubscribe-events-on-disconnect-format-v2\&quot; - As \&quot;on-with-format-v2\&quot;, but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. &lt;/pre&gt;  |  [optional]
**eventPublishTopicFormatMqttEnabled** | **Boolean** | Enable or disable Event publish topics in MQTT format. The default value is &#x60;false&#x60;. |  [optional]
**eventPublishTopicFormatSmfEnabled** | **Boolean** | Enable or disable Event publish topics in SMF format. The default value is &#x60;true&#x60;. |  [optional]
**eventServiceAmqpConnectionCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventServiceMqttConnectionCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventServiceRestIncomingConnectionCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventServiceSmfConnectionCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventServiceWebConnectionCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventSubscriptionCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventTransactedSessionCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**eventTransactionCountThreshold** | [**EventThreshold**](EventThreshold.md) |  |  [optional]
**exportSubscriptionsEnabled** | **Boolean** | Enable or disable the export of subscriptions in the Message VPN to other routers in the network over Neighbor links. The default value is &#x60;false&#x60;. |  [optional]
**jndiEnabled** | **Boolean** | Enable or disable JNDI access for clients in the Message VPN. The default value is &#x60;false&#x60;. Available since 2.2. |  [optional]
**maxConnectionCount** | **Long** | The maximum number of client connections to the Message VPN. The default is the max value supported by the platform. |  [optional]
**maxEgressFlowCount** | **Long** | The maximum number of transmit flows that can be created in the Message VPN. The default value is &#x60;1000&#x60;. |  [optional]
**maxEndpointCount** | **Long** | The maximum number of Queues and Topic Endpoints that can be created in the Message VPN. The default value is &#x60;1000&#x60;. |  [optional]
**maxIngressFlowCount** | **Long** | The maximum number of receive flows that can be created in the Message VPN. The default value is &#x60;1000&#x60;. |  [optional]
**maxMsgSpoolUsage** | **Long** | The maximum message spool usage by the Message VPN, in megabytes. The default value is &#x60;0&#x60;. |  [optional]
**maxSubscriptionCount** | **Long** | The maximum number of local client subscriptions (both primary and backup) that can be added to the Message VPN. The default varies by platform. |  [optional]
**maxTransactedSessionCount** | **Long** | The maximum number of transacted sessions that can be created in the Message VPN. The default varies by platform. |  [optional]
**maxTransactionCount** | **Long** | The maximum number of transactions that can be created in the Message VPN. The default varies by platform. |  [optional]
**mqttRetainMaxMemory** | **Integer** | The maximum total memory usage of the MQTT Retain feature for this Message VPN, in MB. If the maximum memory is reached, any arriving retain messages that require more memory are discarded.  A value of -1 indicates that the memory is bounded only by the global max memory limit. A value of 0 prevents MQTT Retain from becoming operational. The default value is &#x60;-1&#x60;. Available since 2.11. |  [optional]
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]
**replicationAckPropagationIntervalMsgCount** | **Long** | The acknowledgement (ACK) propagation interval for the replication Bridge, in number of replicated messages. The default value is &#x60;20&#x60;. |  [optional]
**replicationBridgeAuthenticationBasicClientUsername** | **String** | The Client Username the replication Bridge uses to login to the remote Message VPN. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**replicationBridgeAuthenticationBasicPassword** | **String** | The password for the Client Username. The default is to have no &#x60;replicationBridgeAuthenticationBasicPassword&#x60;. |  [optional]
**replicationBridgeAuthenticationClientCertContent** | **String** | The PEM formatted content for the client certificate used by this bridge to login to the Remote Message VPN. It must consist of a private key and between one and three certificates comprising the certificate trust chain. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9. |  [optional]
**replicationBridgeAuthenticationClientCertPassword** | **String** | The password for the client certificate. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9. |  [optional]
**replicationBridgeAuthenticationScheme** | [**ReplicationBridgeAuthenticationSchemeEnum**](#ReplicationBridgeAuthenticationSchemeEnum) | The authentication scheme for the replication Bridge in the Message VPN. The default value is &#x60;\&quot;basic\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;basic\&quot; - Basic Authentication Scheme (via username and password). \&quot;client-certificate\&quot; - Client Certificate Authentication Scheme (via certificate file or content). &lt;/pre&gt;  |  [optional]
**replicationBridgeCompressedDataEnabled** | **Boolean** | Enable or disable use of compression for the replication Bridge. The default value is &#x60;false&#x60;. |  [optional]
**replicationBridgeEgressFlowWindowSize** | **Long** | The size of the window used for guaranteed messages published to the replication Bridge, in messages. The default value is &#x60;255&#x60;. |  [optional]
**replicationBridgeRetryDelay** | **Long** | The number of seconds that must pass before retrying the replication Bridge connection. The default value is &#x60;3&#x60;. |  [optional]
**replicationBridgeTlsEnabled** | **Boolean** | Enable or disable use of TLS for the replication Bridge connection. The default value is &#x60;false&#x60;. |  [optional]
**replicationBridgeUnidirectionalClientProfileName** | **String** | The Client Profile for the unidirectional replication Bridge in the Message VPN. It is used only for the TCP parameters. The default value is &#x60;\&quot;#client-profile\&quot;&#x60;. |  [optional]
**replicationEnabled** | **Boolean** | Enable or disable replication for the Message VPN. The default value is &#x60;false&#x60;. |  [optional]
**replicationEnabledQueueBehavior** | [**ReplicationEnabledQueueBehaviorEnum**](#ReplicationEnabledQueueBehaviorEnum) | The behavior to take when enabling replication for the Message VPN, depending on the existence of the replication Queue. The default value is &#x60;\&quot;fail-on-existing-queue\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;fail-on-existing-queue\&quot; - The data replication queue must not already exist. \&quot;force-use-existing-queue\&quot; - The data replication queue must already exist. Any data messages on the Queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail. \&quot;force-recreate-queue\&quot; - The data replication queue must already exist. Any data messages on the Queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. &lt;/pre&gt;  |  [optional]
**replicationQueueMaxMsgSpoolUsage** | **Long** | The maximum message spool usage by the replication Bridge local Queue (quota), in megabytes. The default value is &#x60;60000&#x60;. |  [optional]
**replicationQueueRejectMsgToSenderOnDiscardEnabled** | **Boolean** | Enable or disable whether messages discarded on the replication Bridge local Queue are rejected back to the sender. The default value is &#x60;true&#x60;. |  [optional]
**replicationRejectMsgWhenSyncIneligibleEnabled** | **Boolean** | Enable or disable whether guaranteed messages published to synchronously replicated Topics are rejected back to the sender when synchronous replication becomes ineligible. The default value is &#x60;false&#x60;. |  [optional]
**replicationRole** | [**ReplicationRoleEnum**](#ReplicationRoleEnum) | The replication role for the Message VPN. The default value is &#x60;\&quot;standby\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;active\&quot; - Assume the Active role in replication for the Message VPN. \&quot;standby\&quot; - Assume the Standby role in replication for the Message VPN. &lt;/pre&gt;  |  [optional]
**replicationTransactionMode** | [**ReplicationTransactionModeEnum**](#ReplicationTransactionModeEnum) | The transaction replication mode for all transactions within the Message VPN. Changing this value during operation will not affect existing transactions; it is only used upon starting a transaction. The default value is &#x60;\&quot;async\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;sync\&quot; - Messages are acknowledged when replicated (spooled remotely). \&quot;async\&quot; - Messages are acknowledged when pending replication (spooled locally). &lt;/pre&gt;  |  [optional]
**restTlsServerCertEnforceTrustedCommonNameEnabled** | **Boolean** | Enable or disable validation of the Common Name (CN) in the server certificate from the remote REST Consumer. If enabled, the Common Name is checked against the list of Trusted Common Names configured for the REST Consumer. The default value is &#x60;true&#x60;. |  [optional]
**restTlsServerCertMaxChainDepth** | **Long** | The maximum depth for a REST Consumer server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is &#x60;3&#x60;. |  [optional]
**restTlsServerCertValidateDateEnabled** | **Boolean** | Enable or disable validation of the \&quot;Not Before\&quot; and \&quot;Not After\&quot; validity dates in the REST Consumer server certificate. The default value is &#x60;true&#x60;. |  [optional]
**sempOverMsgBusAdminClientEnabled** | **Boolean** | Enable or disable \&quot;admin client\&quot; SEMP over the message bus commands for the current Message VPN. The default value is &#x60;false&#x60;. |  [optional]
**sempOverMsgBusAdminDistributedCacheEnabled** | **Boolean** | Enable or disable \&quot;admin distributed-cache\&quot; SEMP over the message bus commands for the current Message VPN. The default value is &#x60;false&#x60;. |  [optional]
**sempOverMsgBusAdminEnabled** | **Boolean** | Enable or disable \&quot;admin\&quot; SEMP over the message bus commands for the current Message VPN. The default value is &#x60;false&#x60;. |  [optional]
**sempOverMsgBusEnabled** | **Boolean** | Enable or disable SEMP over the message bus for the current Message VPN. The default value is &#x60;true&#x60;. |  [optional]
**sempOverMsgBusShowEnabled** | **Boolean** | Enable or disable \&quot;show\&quot; SEMP over the message bus commands for the current Message VPN. The default value is &#x60;false&#x60;. |  [optional]
**serviceAmqpMaxConnectionCount** | **Long** | The maximum number of AMQP client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform. Available since 2.2. |  [optional]
**serviceAmqpPlainTextEnabled** | **Boolean** | Enable or disable the plain-text AMQP service in the Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. The default value is &#x60;false&#x60;. Available since 2.2. |  [optional]
**serviceAmqpPlainTextListenPort** | **Long** | The port number for plain-text AMQP clients that connect to the Message VPN. The default is to have no &#x60;serviceAmqpPlainTextListenPort&#x60;. Available since 2.2. |  [optional]
**serviceAmqpTlsEnabled** | **Boolean** | Enable or disable the use of TLS for the AMQP service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is &#x60;false&#x60;. Available since 2.2. |  [optional]
**serviceAmqpTlsListenPort** | **Long** | The port number for AMQP clients that connect to the Message VPN over TLS. The default is to have no &#x60;serviceAmqpTlsListenPort&#x60;. Available since 2.2. |  [optional]
**serviceMqttMaxConnectionCount** | **Long** | The maximum number of MQTT client connections that can be simultaneously connected to the Message VPN. The default is the max value supported by the platform. Available since 2.1. |  [optional]
**serviceMqttPlainTextEnabled** | **Boolean** | Enable or disable the plain-text MQTT service in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is &#x60;false&#x60;. Available since 2.1. |  [optional]
**serviceMqttPlainTextListenPort** | **Long** | The port number for plain-text MQTT clients that connect to the Message VPN. The default value is &#x60;0&#x60;. Available since 2.1. |  [optional]
**serviceMqttTlsEnabled** | **Boolean** | Enable or disable the use of TLS for the MQTT service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is &#x60;false&#x60;. Available since 2.1. |  [optional]
**serviceMqttTlsListenPort** | **Long** | The port number for MQTT clients that connect to the Message VPN over TLS. The default value is &#x60;0&#x60;. Available since 2.1. |  [optional]
**serviceMqttTlsWebSocketEnabled** | **Boolean** | Enable or disable the use of WebSocket over TLS for the MQTT service in the Message VPN. Disabling causes clients currently connected by WebSocket over TLS to be disconnected. The default value is &#x60;false&#x60;. Available since 2.1. |  [optional]
**serviceMqttTlsWebSocketListenPort** | **Long** | The port number for MQTT clients that connect to the Message VPN using WebSocket over TLS. The default value is &#x60;0&#x60;. Available since 2.1. |  [optional]
**serviceMqttWebSocketEnabled** | **Boolean** | Enable or disable the use of WebSocket for the MQTT service in the Message VPN. Disabling causes clients currently connected by WebSocket to be disconnected. The default value is &#x60;false&#x60;. Available since 2.1. |  [optional]
**serviceMqttWebSocketListenPort** | **Long** | The port number for plain-text MQTT clients that connect to the Message VPN using WebSocket. The default value is &#x60;0&#x60;. Available since 2.1. |  [optional]
**serviceRestIncomingMaxConnectionCount** | **Long** | The maximum number of REST incoming client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform. |  [optional]
**serviceRestIncomingPlainTextEnabled** | **Boolean** | Enable or disable the plain-text REST service for incoming clients in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is &#x60;false&#x60;. |  [optional]
**serviceRestIncomingPlainTextListenPort** | **Long** | The port number for incoming plain-text REST clients that connect to the Message VPN. The default value is &#x60;0&#x60;. |  [optional]
**serviceRestIncomingTlsEnabled** | **Boolean** | Enable or disable the use of TLS for the REST service for incoming clients in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is &#x60;false&#x60;. |  [optional]
**serviceRestIncomingTlsListenPort** | **Long** | The port number for incoming REST clients that connect to the Message VPN over TLS. The default value is &#x60;0&#x60;. |  [optional]
**serviceRestMode** | [**ServiceRestModeEnum**](#ServiceRestModeEnum) | The REST service mode for incoming REST clients that connect to the Message VPN. The default value is &#x60;\&quot;messaging\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;gateway\&quot; - Act as a message gateway through which REST messages are propagated. \&quot;messaging\&quot; - Act as a message broker on which REST messages are queued. &lt;/pre&gt;  Available since 2.6. |  [optional]
**serviceRestOutgoingMaxConnectionCount** | **Long** | The maximum number of REST Consumer (outgoing) client connections that can be simultaneously connected to the Message VPN. The default varies by platform. |  [optional]
**serviceSmfMaxConnectionCount** | **Long** | The maximum number of SMF client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform. |  [optional]
**serviceSmfPlainTextEnabled** | **Boolean** | Enable or disable the plain-text SMF service in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is &#x60;true&#x60;. |  [optional]
**serviceSmfTlsEnabled** | **Boolean** | Enable or disable the use of TLS for the SMF service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is &#x60;true&#x60;. |  [optional]
**serviceWebMaxConnectionCount** | **Long** | The maximum number of Web Transport client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform. |  [optional]
**serviceWebPlainTextEnabled** | **Boolean** | Enable or disable the plain-text Web Transport service in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is &#x60;true&#x60;. |  [optional]
**serviceWebTlsEnabled** | **Boolean** | Enable or disable the use of TLS for the Web Transport service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is &#x60;true&#x60;. |  [optional]
**tlsAllowDowngradeToPlainTextEnabled** | **Boolean** | Enable or disable the allowing of TLS SMF clients to downgrade their connections to plain-text connections. Changing this will not affect existing connections. The default value is &#x60;false&#x60;. |  [optional]


<a name="AuthenticationBasicTypeEnum"></a>
## Enum: AuthenticationBasicTypeEnum
Name | Value
---- | -----
INTERNAL | &quot;internal&quot;
LDAP | &quot;ldap&quot;
RADIUS | &quot;radius&quot;
NONE | &quot;none&quot;


<a name="AuthenticationClientCertRevocationCheckModeEnum"></a>
## Enum: AuthenticationClientCertRevocationCheckModeEnum
Name | Value
---- | -----
ALL | &quot;allow-all&quot;
UNKNOWN | &quot;allow-unknown&quot;
VALID | &quot;allow-valid&quot;


<a name="AuthenticationClientCertUsernameSourceEnum"></a>
## Enum: AuthenticationClientCertUsernameSourceEnum
Name | Value
---- | -----
COMMON_NAME | &quot;common-name&quot;
SUBJECT_ALTERNATE_NAME_MSUPN | &quot;subject-alternate-name-msupn&quot;


<a name="AuthorizationTypeEnum"></a>
## Enum: AuthorizationTypeEnum
Name | Value
---- | -----
LDAP | &quot;ldap&quot;
INTERNAL | &quot;internal&quot;


<a name="EventPublishSubscriptionModeEnum"></a>
## Enum: EventPublishSubscriptionModeEnum
Name | Value
---- | -----
OFF | &quot;off&quot;
ON_WITH_FORMAT_V1 | &quot;on-with-format-v1&quot;
ON_WITH_NO_UNSUBSCRIBE_EVENTS_ON_DISCONNECT_FORMAT_V1 | &quot;on-with-no-unsubscribe-events-on-disconnect-format-v1&quot;
ON_WITH_FORMAT_V2 | &quot;on-with-format-v2&quot;
ON_WITH_NO_UNSUBSCRIBE_EVENTS_ON_DISCONNECT_FORMAT_V2 | &quot;on-with-no-unsubscribe-events-on-disconnect-format-v2&quot;


<a name="ReplicationBridgeAuthenticationSchemeEnum"></a>
## Enum: ReplicationBridgeAuthenticationSchemeEnum
Name | Value
---- | -----
BASIC | &quot;basic&quot;
CLIENT_CERTIFICATE | &quot;client-certificate&quot;


<a name="ReplicationEnabledQueueBehaviorEnum"></a>
## Enum: ReplicationEnabledQueueBehaviorEnum
Name | Value
---- | -----
FAIL_ON_EXISTING_QUEUE | &quot;fail-on-existing-queue&quot;
FORCE_USE_EXISTING_QUEUE | &quot;force-use-existing-queue&quot;
FORCE_RECREATE_QUEUE | &quot;force-recreate-queue&quot;


<a name="ReplicationRoleEnum"></a>
## Enum: ReplicationRoleEnum
Name | Value
---- | -----
ACTIVE | &quot;active&quot;
STANDBY | &quot;standby&quot;


<a name="ReplicationTransactionModeEnum"></a>
## Enum: ReplicationTransactionModeEnum
Name | Value
---- | -----
SYNC | &quot;sync&quot;
ASYNC | &quot;async&quot;


<a name="ServiceRestModeEnum"></a>
## Enum: ServiceRestModeEnum
Name | Value
---- | -----
GATEWAY | &quot;gateway&quot;
MESSAGING | &quot;messaging&quot;



