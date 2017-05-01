# SempClient::MsgVpnBridgeRemoteMsgVpn

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**bridge_name** | **String** | The name of the Bridge. | [optional] 
**bridge_virtual_router** | **String** | The virtual-router of the Bridge. The allowed values and their meaning are:      \&quot;primary\&quot; - Bridge belongs to the primary virtual-router.     \&quot;backup\&quot; - Bridge belongs to the backup virtual-router.  | [optional] 
**client_username** | **String** | The client username the bridge uses to login to the Remote Message VPN. This per Remote Message VPN value overrides the value provided for the bridge overall. The default is to have no &#x60;clientUsername&#x60;. | [optional] 
**compressed_data_enabled** | **BOOLEAN** | Enable or disable data compression for the Remote Message VPN. The default value is &#x60;false&#x60;. | [optional] 
**connect_order** | **Integer** | The order in which attempts to connect to different Message VPN hosts are attempted, or the preference given to incoming connections from remote routers, from &#x60;1&#x60; (highest priority) to &#x60;4&#x60; (lowest priority). The default value is &#x60;4&#x60;. | [optional] 
**egress_flow_window_size** | **Integer** | The window size indicates how many outstanding guaranteed messages can be sent over the Remote Message VPN connection before acknowledgement is received by the sender. The default value is &#x60;255&#x60;. | [optional] 
**enabled** | **BOOLEAN** | Enable or disable the Remote Message VPN. The default value is &#x60;false&#x60;. | [optional] 
**msg_vpn_name** | **String** | The name of the Message VPN. | [optional] 
**password** | **String** | The password for the client username the bridge uses to login to the Remote Message VPN. The default is to have no &#x60;password&#x60;. | [optional] 
**queue_binding** | **String** | The queue binding of the bridge for this Remote Message VPN. The bridge attempts to bind to that queue over the bridge link once the link has been established, or immediately if it already is established. The queue must be configured on the remote router when the bridge connection is established. If the bind fails an event log is generated which includes the reason for the failure. The default is to have no &#x60;queueBinding&#x60;. | [optional] 
**remote_msg_vpn_interface** | **String** | The interface on the local router through which to access the Remote Message VPN. If not provided (recommended) then an interface will be chosen automatically based on routing tables. If an interface is provided, &#x60;remoteMsgVpnLocation&#x60; must be either a hostname or IP address, not a virtual router-name. | [optional] 
**remote_msg_vpn_location** | **String** | The location of the Remote Message VPN. This may be given as either a hostname (resolvable via DNS), IP Address, or virtual router-name (starts with &#39;v:&#39;). If specified as a hostname or IP Address, a port must be specified as well. | [optional] 
**remote_msg_vpn_name** | **String** | The name of the Remote Message VPN. | [optional] 
**tls_enabled** | **BOOLEAN** | Enable or disable TLS for the Remote Message VPN. The default value is &#x60;false&#x60;. | [optional] 
**unidirectional_client_profile** | **String** | The client-profile for the unidirectional bridge for the Remote Message VPN. The client-profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is &#x60;\&quot;#client-profile\&quot;&#x60;. | [optional] 


