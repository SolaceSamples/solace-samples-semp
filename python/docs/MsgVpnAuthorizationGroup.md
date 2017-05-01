# MsgVpnAuthorizationGroup

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**acl_profile_name** | **str** | The ACL Profile of the LDAP Authorization Group. The default value is &#x60;\&quot;default\&quot;&#x60;. | [optional] 
**authorization_group_name** | **str** | The name of the LDAP Authorization Group. | [optional] 
**client_profile_name** | **str** | The Client Profile of the LDAP Authorization Group. The default value is &#x60;\&quot;default\&quot;&#x60;. | [optional] 
**enabled** | **bool** | Enable or disable the authorization feature for this group for the Message VPN. The default value is &#x60;false&#x60;. | [optional] 
**msg_vpn_name** | **str** | The name of the Message VPN. | [optional] 
**order_after_authorization_group_name** | **str** | Lower the priority to be less than this group. The default is not applicable. | [optional] 
**order_before_authorization_group_name** | **str** | Raise the priority to be greater than this group. The default is not applicable. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


