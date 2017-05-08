# SempClient::MsgVpnBridge

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**bridge_name** | **String** | The name of the Bridge. | [optional] 
**bridge_virtual_router** | **String** | The virtual-router of the Bridge. The allowed values and their meaning are:      \&quot;primary\&quot; - Bridge belongs to the primary virtual-router.     \&quot;backup\&quot; - Bridge belongs to the backup virtual-router.  | [optional] 
**enabled** | **BOOLEAN** | Enable or disable the bridge. The default value is &#x60;false&#x60;. | [optional] 
**max_ttl** | **Integer** | The max-ttl value for the bridge, in hops. When a bridge is sending a message to the remote router, the TTL value for the message becomes the lower of its current TTL value or this value. The default value is &#x60;8&#x60;. | [optional] 
**msg_vpn_name** | **String** | The name of the Message VPN. | [optional] 
**remote_authentication_basic_client_username** | **String** | The client username the bridge uses to login to the Remote Message VPN. The default is to have no &#x60;remoteAuthenticationBasicClientUsername&#x60;. | [optional] 
**remote_authentication_basic_password** | **String** | The password for the client username the bridge uses to login to the Remote Message VPN. The default is to have no &#x60;remoteAuthenticationBasicPassword&#x60;. | [optional] 
**remote_authentication_scheme** | **String** | The authentication scheme for the Remote Message VPN. The default value is &#x60;\&quot;basic\&quot;&#x60;. The allowed values and their meaning are:      \&quot;basic\&quot; - Basic Authentication Scheme (via username and password).     \&quot;client-certificate\&quot; - Client Certificate Authentication Scheme (via certificate-file).  | [optional] 
**remote_connection_retry_count** | **Integer** | The number of retries that are attempted for a router name before the next remote router alternative is attempted. The default value is &#x60;0&#x60;. | [optional] 
**remote_connection_retry_delay** | **Integer** | The number of seconds that must pass before retrying a connection. The default value is &#x60;3&#x60;. | [optional] 
**remote_deliver_to_one_priority** | **String** | The deliver-to-one priority for the bridge used on the remote router. The default value is &#x60;\&quot;p1\&quot;&#x60;. The allowed values and their meaning are:      \&quot;p1\&quot; - Priority 1 (highest).     \&quot;p2\&quot; - Priority 2.     \&quot;p3\&quot; - Priority 3.     \&quot;p4\&quot; - Priority 4 (lowest).     \&quot;da\&quot; - Deliver Always.  | [optional] 
**tls_cipher_suite_list** | **String** | The colon-separated list of of cipher suites for the TLS authentication mechanism. The suite selected will be the first suite in the list that is supported by the remote router. The default value is &#x60;\&quot;ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:AES256-GCM-SHA384:AES256-SHA256:AES256-SHA:ECDHE-RSA-DES-CBC3-SHA:DES-CBC3-SHA:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:AES128-GCM-SHA256:AES128-SHA256:AES128-SHA\&quot;&#x60;. | [optional] 


