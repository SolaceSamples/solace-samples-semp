
# MsgVpnRestDeliveryPoint

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**clientProfileName** | **String** | The Client Profile of the REST Delivery Point. It must exist in the local Message VPN. Its TCP parameters are used for all REST Consumers in this RDP. Its queue properties are used by the RDP client. The Client Profile is used inside the auto-generated Client Username for this RDP. The default value is &#x60;\&quot;default\&quot;&#x60;. |  [optional]
**enabled** | **Boolean** | Enable or disable the REST Delivery Point. When disabled, no connections are initiated or messages delivered to any of the contained REST Consumers. The default value is &#x60;false&#x60;. |  [optional]
**msgVpnName** | **String** | The name of the Message VPN. |  [optional]
**restDeliveryPointName** | **String** | The name of the REST Delivery Point. |  [optional]



