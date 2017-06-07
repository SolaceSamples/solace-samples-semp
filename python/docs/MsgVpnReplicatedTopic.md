# MsgVpnReplicatedTopic

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**msg_vpn_name** | **str** | The name of the Message VPN. | [optional] 
**replicated_topic** | **str** | Topic for applying replication. Published messages matching this topic will be replicated to the standby site. | [optional] 
**replication_mode** | **str** | Choose the replication-mode for the Replicated Topic. The default value is &#x60;\&quot;async\&quot;&#x60;. The allowed values and their meaning are:      \&quot;sync\&quot; - Synchronous replication-mode. Published messages are acknowledged when they are spooled on the standby site.     \&quot;async\&quot; - Asynchronous replication-mode. Published messages are acknowledged when they are spooled locally.  | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


