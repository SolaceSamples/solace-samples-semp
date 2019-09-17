
# MsgVpnBridge

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**bridgeName** | **String** | The name of the Bridge. |  [optional]
**bridgeVirtualRouter** | [**BridgeVirtualRouterEnum**](#BridgeVirtualRouterEnum) | The virtual router of the Bridge. The allowed values and their meaning are:  &lt;pre&gt; \&quot;primary\&quot; - The Bridge is used for the primary virtual router. \&quot;backup\&quot; - The Bridge is used for the backup virtual router. \&quot;auto\&quot; - The Bridge is automatically assigned a router. &lt;/pre&gt;  |  [optional]
**enabled** | **Boolean** | Enable or disable the Bridge. The default value is &#x60;false&#x60;. |  [optional]
**maxTtl** | **Long** | The maximum time-to-live (TTL) in hops. Messages are discarded if their TTL exceeds this value. The default value is &#x60;8&#x60;. |  [optional]
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]
**remoteAuthenticationBasicClientUsername** | **String** | The Client Username the Bridge uses to login to the remote Message VPN. The default value is &#x60;\&quot;\&quot;&#x60;. |  [optional]
**remoteAuthenticationBasicPassword** | **String** | The password for the Client Username. The default is to have no &#x60;remoteAuthenticationBasicPassword&#x60;. |  [optional]
**remoteAuthenticationClientCertContent** | **String** | The PEM formatted content for the client certificate used by the Bridge to login to the remote Message VPN. It must consist of a private key and between one and three certificates comprising the certificate trust chain. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9. |  [optional]
**remoteAuthenticationClientCertPassword** | **String** | The password for the client certificate. Changing this attribute requires an HTTPS connection. The default value is &#x60;\&quot;\&quot;&#x60;. Available since 2.9. |  [optional]
**remoteAuthenticationScheme** | [**RemoteAuthenticationSchemeEnum**](#RemoteAuthenticationSchemeEnum) | The authentication scheme for the remote Message VPN. The default value is &#x60;\&quot;basic\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;basic\&quot; - Basic Authentication Scheme (via username and password). \&quot;client-certificate\&quot; - Client Certificate Authentication Scheme (via certificate file or content). &lt;/pre&gt;  |  [optional]
**remoteConnectionRetryCount** | **Long** | The maximum number of retry attempts to establish a connection to the remote Message VPN. A value of 0 means to retry forever. The default value is &#x60;0&#x60;. |  [optional]
**remoteConnectionRetryDelay** | **Long** | The number of seconds to delay before retrying to connect to the remote Message VPN. The default value is &#x60;3&#x60;. |  [optional]
**remoteDeliverToOnePriority** | [**RemoteDeliverToOnePriorityEnum**](#RemoteDeliverToOnePriorityEnum) | The priority for deliver-to-one (DTO) messages transmitted from the remote Message VPN. The default value is &#x60;\&quot;p1\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;p1\&quot; - The 1st or highest priority. \&quot;p2\&quot; - The 2nd highest priority. \&quot;p3\&quot; - The 3rd highest priority. \&quot;p4\&quot; - The 4th highest priority. \&quot;da\&quot; - Ignore priority and deliver always. &lt;/pre&gt;  |  [optional]
**tlsCipherSuiteList** | **String** | The colon-separated list of cipher-suites supported for TLS connections to the remote Message VPN. The value \&quot;default\&quot; implies all supported suites ordered from most secure to least secure. The default value is &#x60;\&quot;default\&quot;&#x60;. |  [optional]


<a name="BridgeVirtualRouterEnum"></a>
## Enum: BridgeVirtualRouterEnum
Name | Value
---- | -----
PRIMARY | &quot;primary&quot;
BACKUP | &quot;backup&quot;
AUTO | &quot;auto&quot;


<a name="RemoteAuthenticationSchemeEnum"></a>
## Enum: RemoteAuthenticationSchemeEnum
Name | Value
---- | -----
BASIC | &quot;basic&quot;
CLIENT_CERTIFICATE | &quot;client-certificate&quot;


<a name="RemoteDeliverToOnePriorityEnum"></a>
## Enum: RemoteDeliverToOnePriorityEnum
Name | Value
---- | -----
P1 | &quot;p1&quot;
P2 | &quot;p2&quot;
P3 | &quot;p3&quot;
P4 | &quot;p4&quot;
DA | &quot;da&quot;



