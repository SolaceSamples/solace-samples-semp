# MsgVpnRestDeliveryPointQueueBinding

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**msg_vpn_name** | **str** | The name of the Message VPN. | [optional] 
**post_request_target** | **str** | The POST request target string to send for messages attracted to the associated queue-binding. It identifies the target resource on the far-end REST consumer upon which to apply the POST request.  There are generally two common forms for the request target. The origin-form is most often used in practice and contains the absolute path and query components of the target URI. If the path component is empty then the client must generally send a \&quot;/\&quot; as the path.  For example if trying to send a message to a resource identified as:    http://www.example.org/where?q&#x3D;now  The value \&quot;post-request-target\&quot; would be \&quot;/where?q&#x3D;now\&quot;.  When making a request to a proxy, most often it is required to use the absolute form. In the preceding example this would mean that the \&quot;post-request-target\&quot; would have a value of \&quot;http://www.example.org/where?q&#x3D;now\&quot;.  The POST request target string is not validated. Its value is used as is in the HTTP POST request target field when sending HTTP POSTs to REST Consumers. The default is to have no &#x60;postRequestTarget&#x60;. | [optional] 
**queue_binding_name** | **str** | The name of a queue within this Message VPN. | [optional] 
**rest_delivery_point_name** | **str** | A Message VPN-wide unique name for the REST Delivery Point. This name is used to auto-generate a client-username in this Message VPN, which is used by the client for this RDP. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


