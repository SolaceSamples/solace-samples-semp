
# Username

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**globalAccessLevel** | [**GlobalAccessLevelEnum**](#GlobalAccessLevelEnum) | The global-scope access-level of a CLI username. The default value is &#x60;\&quot;read-only\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - User has no access to global data. \&quot;read-only\&quot; - User has read-only access to global data. \&quot;read-write\&quot; - User has read-write access to most global data. \&quot;admin\&quot; - User has read-write access to all global data. &lt;/pre&gt;  |  [optional]
**msgVpnDefaultAccessLevel** | [**MsgVpnDefaultAccessLevelEnum**](#MsgVpnDefaultAccessLevelEnum) | The vpn-scope access-level that gets assigned by default to CLI users on each Message VPN unless there is an access-level exception configured for it. In that case the exception takes precedence. The default value is &#x60;\&quot;none\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;none\&quot; - User has no access to a Message VPN. \&quot;read-only\&quot; - User has read-only access to a Message VPN. \&quot;read-write\&quot; - User has read-write access to most Message VPN settings. &lt;/pre&gt;  |  [optional]
**password** | **String** | Change the password of the user. The default is to have no &#x60;password&#x60;. |  [optional]
**userName** | **String** | Username. |  [optional]


<a name="GlobalAccessLevelEnum"></a>
## Enum: GlobalAccessLevelEnum
Name | Value
---- | -----
NONE | &quot;none&quot;
READ_ONLY | &quot;read-only&quot;
READ_WRITE | &quot;read-write&quot;
ADMIN | &quot;admin&quot;


<a name="MsgVpnDefaultAccessLevelEnum"></a>
## Enum: MsgVpnDefaultAccessLevelEnum
Name | Value
---- | -----
NONE | &quot;none&quot;
READ_ONLY | &quot;read-only&quot;
READ_WRITE | &quot;read-write&quot;



