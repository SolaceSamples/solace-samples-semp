
# UsernameMsgVpnAccessLevelException

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**accessLevel** | [**AccessLevelEnum**](#AccessLevelEnum) | vpn-scope access-level to assign to CLI users. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - User has no access to a Message VPN. \&quot;read-only\&quot; - User has read-only access to a Message VPN. \&quot;read-write\&quot; - User has read-write access to most Message VPN settings. &lt;/pre&gt;  |  [optional]
**msgVpnName** | **String** | The name of the Message VPN for which an access level exception may be configured. |  [optional]
**userName** | **String** | Username. |  [optional]


<a name="AccessLevelEnum"></a>
## Enum: AccessLevelEnum
Name | Value
---- | -----
NONE | &quot;none&quot;
READ_ONLY | &quot;read-only&quot;
READ_WRITE | &quot;read-write&quot;



