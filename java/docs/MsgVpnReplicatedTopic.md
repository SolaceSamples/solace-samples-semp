
# MsgVpnReplicatedTopic

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]
**replicatedTopic** | **String** | The topic for applying replication. Published messages matching this topic will be replicated to the standby site. |  [optional]
**replicationMode** | [**ReplicationModeEnum**](#ReplicationModeEnum) | The replication mode for the Replicated Topic. The default value is &#x60;\&quot;async\&quot;&#x60;. The allowed values and their meaning are:  &lt;pre&gt; \&quot;sync\&quot; - Messages are acknowledged when replicated (spooled remotely). \&quot;async\&quot; - Messages are acknowledged when pending replication (spooled locally). &lt;/pre&gt;  Available since 2.1. |  [optional]


<a name="ReplicationModeEnum"></a>
## Enum: ReplicationModeEnum
Name | Value
---- | -----
SYNC | &quot;sync&quot;
ASYNC | &quot;async&quot;



