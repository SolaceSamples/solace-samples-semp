# SempClient::MsgVpnAuthorizationGroup

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**acl_profile_name** | **String** | The ACL Profile of the LDAP Authorization Group. The default value is &#x60;\&quot;default\&quot;&#x60;. | [optional] 
**authorization_group_name** | **String** | The name of the LDAP Authorization Group. | [optional] 
**client_profile_name** | **String** | The Client Profile of the LDAP Authorization Group. The default value is &#x60;\&quot;default\&quot;&#x60;. | [optional] 
**enabled** | **BOOLEAN** | Enable or disable the authorization feature for this group for the Message VPN. The default value is &#x60;false&#x60;. | [optional] 
**msg_vpn_name** | **String** | The name of the Message VPN. | [optional] 
**order_after_authorization_group_name** | **String** | Lower the priority to be less than this group. The default is not applicable. | [optional] 
**order_before_authorization_group_name** | **String** | Raise the priority to be greater than this group. The default is not applicable. | [optional] 


