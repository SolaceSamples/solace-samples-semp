
# MsgVpnRestDeliveryPointRestConsumer

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**authenticationClientCertContent** | **String** | The PEM formatted content for the client certificate that the REST Consumer will present to the REST host. It must consist of a private key and between one and three certificates comprising the certificate trust chain. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9. |  [optional]
**authenticationClientCertPassword** | **String** | The password for the client certificate. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9. |  [optional]
**authenticationHttpBasicPassword** | **String** | The password for the username. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**authenticationHttpBasicUsername** | **String** | The username that the REST Consumer will use to login to the REST host. Normally a username is only configured when basic authentication is selected for the REST Consumer. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**authenticationScheme** | [**AuthenticationSchemeEnum**](#AuthenticationSchemeEnum) | The authentication scheme used by the REST Consumer to login to the REST host. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - Login with no authentication. This may be useful for anonymous connections or when a REST Consumer does not require authentication. \&quot;http-basic\&quot; - Login with a username and optional password according to HTTP Basic authentication as per RFC2616. \&quot;client-certificate\&quot; - Login with a client TLS certificate as per RFC5246. Client certificate authentication is only available on TLS connections. &lt;/pre&gt;  |  [optional]
**enabled** | **Boolean** | Enable or disable the REST Consumer. When disabled, no connections are initiated or messages delivered to this particular REST Consumer. The default value is &#x60;false&#x60;. |  [optional]
**localInterface** | **String** | The interface that will be used for all outgoing connections associated with the REST Consumer. When unspecified, an interface is automatically chosen. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**maxPostWaitTime** | **Integer** | The maximum amount of time (in seconds) to wait for an HTTP POST response from the REST Consumer. Once this time is exceeded, the TCP connection is reset. The default value is &#x60;30&#x60;. |  [optional]
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]
**outgoingConnectionCount** | **Integer** | The number of concurrent TCP connections open to the REST Consumer. The default value is &#x60;3&#x60;. |  [optional]
**remoteHost** | **String** | The IP address or DNS name to which the broker is to connect to deliver messages for the REST Consumer. A host value must be configured for the REST Consumer to be operationally up. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**remotePort** | **Long** | The port associated with the host of the REST Consumer. The default value is &#x60;8080&#x60;. |  [optional]
**restConsumerName** | **String** | The name of the REST Consumer. |  [optional]
**restDeliveryPointName** | **String** | The name of the REST Delivery Point. |  [optional]
**retryDelay** | **Integer** | The number of seconds that must pass before retrying the remote REST Consumer connection. The default value is &#x60;3&#x60;. |  [optional]
**tlsCipherSuiteList** | **String** | The colon-separated list of cipher-suites the REST Consumer uses in its encrypted connection. All supported suites are included by default, from most-secure to least-secure. The REST Consumer should choose the first suite from this list that it supports. The default value is &#x60;\&quot;default\&quot;&#x60;. |  [optional]
**tlsEnabled** | **Boolean** | Indicates whether TLS for the REST Consumer is enabled. The default value is &#x60;false&#x60;. |  [optional]


<a name="AuthenticationSchemeEnum"></a>
## Enum: AuthenticationSchemeEnum
Name | Value
---- | -----
NONE | &quot;none&quot;
HTTP_BASIC | &quot;http-basic&quot;
CLIENT_CERTIFICATE | &quot;client-certificate&quot;



