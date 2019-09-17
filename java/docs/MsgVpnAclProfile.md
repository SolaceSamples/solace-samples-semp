
# MsgVpnAclProfile

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**aclProfileName** | **String** | The name of the ACL Profile. |  [optional]
**clientConnectDefaultAction** | [**ClientConnectDefaultActionEnum**](#ClientConnectDefaultActionEnum) | The default action to take when a client using the ACL Profile connects to the Message VPN. The default value is &#x60;\&quot;disallow\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;allow\&quot; - Allow client connection unless an exception is found for it. \&quot;disallow\&quot; - Disallow client connection unless an exception is found for it. &lt;/pre&gt;  |  [optional]
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]
**publishTopicDefaultAction** | [**PublishTopicDefaultActionEnum**](#PublishTopicDefaultActionEnum) | The default action to take when a client using the ACL Profile publishes to a topic in the Message VPN. The default value is &#x60;\&quot;disallow\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;allow\&quot; - Allow topic unless an exception is found for it. \&quot;disallow\&quot; - Disallow topic unless an exception is found for it. &lt;/pre&gt;  |  [optional]
**subscribeTopicDefaultAction** | [**SubscribeTopicDefaultActionEnum**](#SubscribeTopicDefaultActionEnum) | The default action to take when a client using the ACL Profile subscribes to a topic in the Message VPN. The default value is &#x60;\&quot;disallow\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;allow\&quot; - Allow topic unless an exception is found for it. \&quot;disallow\&quot; - Disallow topic unless an exception is found for it. &lt;/pre&gt;  |  [optional]


<a name="ClientConnectDefaultActionEnum"></a>
## Enum: ClientConnectDefaultActionEnum
Name | Value
---- | -----
ALLOW | &quot;allow&quot;
DISALLOW | &quot;disallow&quot;


<a name="PublishTopicDefaultActionEnum"></a>
## Enum: PublishTopicDefaultActionEnum
Name | Value
---- | -----
ALLOW | &quot;allow&quot;
DISALLOW | &quot;disallow&quot;


<a name="SubscribeTopicDefaultActionEnum"></a>
## Enum: SubscribeTopicDefaultActionEnum
Name | Value
---- | -----
ALLOW | &quot;allow&quot;
DISALLOW | &quot;disallow&quot;



