/*
 * SEMP (Solace Element Management Protocol)
 * SEMP (starting in `v2`, see note 1) is a RESTful API for configuring, monitoring, and administering a Solace PubSub+ broker.  SEMP uses URIs to address manageable **resources** of the Solace PubSub+ broker. Resources are individual **objects**, **collections** of objects, or (exclusively in the action API) **actions**. This document applies to the following API:   API|Base Path|Purpose|Comments :---|:---|:---|:--- Configuration|/SEMP/v2/config|Reading and writing config state|See note 2    The following APIs are also available:   API|Base Path|Purpose|Comments :---|:---|:---|:--- Action|/SEMP/v2/action|Performing actions|See note 2 Monitoring|/SEMP/v2/monitor|Querying operational parameters|See note 2    Resources are always nouns, with individual objects being singular and collections being plural.  Objects within a collection are identified by an `obj-id`, which follows the collection name with the form `collection-name/obj-id`.  Actions within an object are identified by an `action-id`, which follows the object name with the form `obj-id/action-id`.  Some examples:  ``` /SEMP/v2/config/msgVpns                        ; MsgVpn collection /SEMP/v2/config/msgVpns/a                      ; MsgVpn object named \"a\" /SEMP/v2/config/msgVpns/a/queues               ; Queue collection in MsgVpn \"a\" /SEMP/v2/config/msgVpns/a/queues/b             ; Queue object named \"b\" in MsgVpn \"a\" /SEMP/v2/action/msgVpns/a/queues/b/startReplay ; Action that starts a replay on Queue \"b\" in MsgVpn \"a\" /SEMP/v2/monitor/msgVpns/a/clients             ; Client collection in MsgVpn \"a\" /SEMP/v2/monitor/msgVpns/a/clients/c           ; Client object named \"c\" in MsgVpn \"a\" ```  ## Collection Resources  Collections are unordered lists of objects (unless described as otherwise), and are described by JSON arrays. Each item in the array represents an object in the same manner as the individual object would normally be represented. In the configuration API, the creation of a new object is done through its collection resource.  ## Object and Action Resources  Objects are composed of attributes, actions, collections, and other objects. They are described by JSON objects as name/value pairs. The collections and actions of an object are not contained directly in the object's JSON content; rather the content includes an attribute containing a URI which points to the collections and actions. These contained resources must be managed through this URI. At a minimum, every object has one or more identifying attributes, and its own `uri` attribute which contains the URI pointing to itself.  Actions are also composed of attributes, and are described by JSON objects as name/value pairs. Unlike objects, however, they are not members of a collection and cannot be retrieved, only performed. Actions only exist in the action API.  Attributes in an object or action may have any (non-exclusively) of the following properties:   Property|Meaning|Comments :---|:---|:--- Identifying|Attribute is involved in unique identification of the object, and appears in its URI| Required|Attribute must be provided in the request| Read-Only|Attribute can only be read, not written|See note 3 Write-Only|Attribute can only be written, not read| Requires-Disable|Attribute can only be changed when object is disabled| Deprecated|Attribute is deprecated, and will disappear in the next SEMP version|    In some requests, certain attributes may only be provided in certain combinations with other attributes:   Relationship|Meaning :---|:--- Requires|Attribute may only be changed by a request if a particular attribute or combination of attributes is also provided in the request Conflicts|Attribute may only be provided in a request if a particular attribute or combination of attributes is not also provided in the request    ## HTTP Methods  The following HTTP methods manipulate resources in accordance with these general principles. Note that some methods are only used in certain APIs:   Method|Resource|Meaning|Request Body|Response Body|Missing Request Attributes :---|:---|:---|:---|:---|:--- POST|Collection|Create object|Initial attribute values|Object attributes and metadata|Set to default PUT|Object|Create or replace object|New attribute values|Object attributes and metadata|Set to default (but see note 4) PUT|Action|Performs action|Action arguments|Action metadata|N/A PATCH|Object|Update object|New attribute values|Object attributes and metadata|unchanged DELETE|Object|Delete object|Empty|Object metadata|N/A GET|Object|Get object|Empty|Object attributes and metadata|N/A GET|Collection|Get collection|Empty|Object attributes and collection metadata|N/A    ## Common Query Parameters  The following are some common query parameters that are supported by many method/URI combinations. Individual URIs may document additional parameters. Note that multiple query parameters can be used together in a single URI, separated by the ampersand character. For example:  ``` ; Request for the MsgVpns collection using two hypothetical query parameters \"q1\" and \"q2\" ; with values \"val1\" and \"val2\" respectively /SEMP/v2/config/msgVpns?q1=val1&q2=val2 ```  ### select  Include in the response only selected attributes of the object, or exclude from the response selected attributes of the object. Use this query parameter to limit the size of the returned data for each returned object, return only those fields that are desired, or exclude fields that are not desired.  The value of `select` is a comma-separated list of attribute names. If the list contains attribute names that are not prefaced by `-`, only those attributes are included in the response. If the list contains attribute names that are prefaced by `-`, those attributes are excluded from the response. If the list contains both types, then the difference of the first set of attributes and the second set of attributes is returned. If the list is empty (i.e. `select=`), no attributes are returned.  All attributes that are prefaced by `-` must follow all attributes that are not prefaced by `-`. In addition, each attribute name in the list must match at least one attribute in the object.  Names may include the `*` wildcard (zero or more characters). Nested attribute names are supported using periods (e.g. `parentName.childName`).  Some examples:  ``` ; List of all MsgVpn names /SEMP/v2/config/msgVpns?select=msgVpnName ; List of all MsgVpn and their attributes except for their names /SEMP/v2/config/msgVpns?select=-msgVpnName ; Authentication attributes of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance?select=authentication* ; All attributes of MsgVpn \"finance\" except for authentication attributes /SEMP/v2/config/msgVpns/finance?select=-authentication* ; Access related attributes of Queue \"orderQ\" of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ?select=owner,permission ```  ### where  Include in the response only objects where certain conditions are true. Use this query parameter to limit which objects are returned to those whose attribute values meet the given conditions.  The value of `where` is a comma-separated list of expressions. All expressions must be true for the object to be included in the response. Each expression takes the form:  ``` expression  = attribute-name OP value OP          = '==' | '!=' | '&lt;' | '&gt;' | '&lt;=' | '&gt;=' ```  `value` may be a number, string, `true`, or `false`, as appropriate for the type of `attribute-name`. Greater-than and less-than comparisons only work for numbers. A `*` in a string `value` is interpreted as a wildcard (zero or more characters). Some examples:  ``` ; Only enabled MsgVpns /SEMP/v2/config/msgVpns?where=enabled==true ; Only MsgVpns using basic non-LDAP authentication /SEMP/v2/config/msgVpns?where=authenticationBasicEnabled==true,authenticationBasicType!=ldap ; Only MsgVpns that allow more than 100 client connections /SEMP/v2/config/msgVpns?where=maxConnectionCount>100 ; Only MsgVpns with msgVpnName starting with \"B\": /SEMP/v2/config/msgVpns?where=msgVpnName==B* ```  ### count  Limit the count of objects in the response. This can be useful to limit the size of the response for large collections. The minimum value for `count` is `1` and the default is `10`. There is also a per-collection maximum value to limit request handling time. For example:  ``` ; Up to 25 MsgVpns /SEMP/v2/config/msgVpns?count=25 ```  ### cursor  The cursor, or position, for the next page of objects. Cursors are opaque data that should not be created or interpreted by SEMP clients, and should only be used as described below.  When a request is made for a collection and there may be additional objects available for retrieval that are not included in the initial response, the response will include a `cursorQuery` field containing a cursor. The value of this field can be specified in the `cursor` query parameter of a subsequent request to retrieve the next page of objects. For convenience, an appropriate URI is constructed automatically by the broker and included in the `nextPageUri` field of the response. This URI can be used directly to retrieve the next page of objects.  ## Notes  Note|Description :---:|:--- 1|This specification defines SEMP starting in \"v2\", and not the original SEMP \"v1\" interface. Request and response formats between \"v1\" and \"v2\" are entirely incompatible, although both protocols share a common port configuration on the Solace PubSub+ broker. They are differentiated by the initial portion of the URI path, one of either \"/SEMP/\" or \"/SEMP/v2/\" 2|This API is partially implemented. Only a subset of all objects are available. 3|Read-only attributes may appear in POST and PUT/PATCH requests. However, if a read-only attribute is not marked as identifying, it will be ignored during a PUT/PATCH. 4|For PUT, if the SEMP user is not authorized to modify the attribute, its value is left unchanged rather than set to default. In addition, the values of write-only attributes are not set to their defaults on a PUT. If the object does not exist, it is created first.    
 *
 * OpenAPI spec version: 2.12.00902000014
 * Contact: support@solace.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.solace.labs.sempclient.samplelib.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * MsgVpnBridgeRemoteMsgVpn
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-11T16:20:54.920-04:00")
public class MsgVpnBridgeRemoteMsgVpn {
  @JsonProperty("bridgeName")
  private String bridgeName = null;

  /**
   * The virtual router of the Bridge. The allowed values and their meaning are:  <pre> \"primary\" - The Bridge is used for the primary virtual router. \"backup\" - The Bridge is used for the backup virtual router. \"auto\" - The Bridge is automatically assigned a router. </pre> 
   */
  public enum BridgeVirtualRouterEnum {
    PRIMARY("primary"),
    
    BACKUP("backup"),
    
    AUTO("auto");

    private String value;

    BridgeVirtualRouterEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static BridgeVirtualRouterEnum fromValue(String text) {
      for (BridgeVirtualRouterEnum b : BridgeVirtualRouterEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("bridgeVirtualRouter")
  private BridgeVirtualRouterEnum bridgeVirtualRouter = null;

  @JsonProperty("clientUsername")
  private String clientUsername = null;

  @JsonProperty("compressedDataEnabled")
  private Boolean compressedDataEnabled = null;

  @JsonProperty("connectOrder")
  private Integer connectOrder = null;

  @JsonProperty("egressFlowWindowSize")
  private Long egressFlowWindowSize = null;

  @JsonProperty("enabled")
  private Boolean enabled = null;

  @JsonProperty("msgVpnName")
  private String msgVpnName = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("queueBinding")
  private String queueBinding = null;

  @JsonProperty("remoteMsgVpnInterface")
  private String remoteMsgVpnInterface = null;

  @JsonProperty("remoteMsgVpnLocation")
  private String remoteMsgVpnLocation = null;

  @JsonProperty("remoteMsgVpnName")
  private String remoteMsgVpnName = null;

  @JsonProperty("tlsEnabled")
  private Boolean tlsEnabled = null;

  @JsonProperty("unidirectionalClientProfile")
  private String unidirectionalClientProfile = null;

  public MsgVpnBridgeRemoteMsgVpn bridgeName(String bridgeName) {
    this.bridgeName = bridgeName;
    return this;
  }

   /**
   * The name of the Bridge.
   * @return bridgeName
  **/
  @ApiModelProperty(example = "null", value = "The name of the Bridge.")
  public String getBridgeName() {
    return bridgeName;
  }

  public void setBridgeName(String bridgeName) {
    this.bridgeName = bridgeName;
  }

  public MsgVpnBridgeRemoteMsgVpn bridgeVirtualRouter(BridgeVirtualRouterEnum bridgeVirtualRouter) {
    this.bridgeVirtualRouter = bridgeVirtualRouter;
    return this;
  }

   /**
   * The virtual router of the Bridge. The allowed values and their meaning are:  <pre> \"primary\" - The Bridge is used for the primary virtual router. \"backup\" - The Bridge is used for the backup virtual router. \"auto\" - The Bridge is automatically assigned a router. </pre> 
   * @return bridgeVirtualRouter
  **/
  @ApiModelProperty(example = "null", value = "The virtual router of the Bridge. The allowed values and their meaning are:  <pre> \"primary\" - The Bridge is used for the primary virtual router. \"backup\" - The Bridge is used for the backup virtual router. \"auto\" - The Bridge is automatically assigned a router. </pre> ")
  public BridgeVirtualRouterEnum getBridgeVirtualRouter() {
    return bridgeVirtualRouter;
  }

  public void setBridgeVirtualRouter(BridgeVirtualRouterEnum bridgeVirtualRouter) {
    this.bridgeVirtualRouter = bridgeVirtualRouter;
  }

  public MsgVpnBridgeRemoteMsgVpn clientUsername(String clientUsername) {
    this.clientUsername = clientUsername;
    return this;
  }

   /**
   * The Client Username the Bridge uses to login to the remote Message VPN. This per remote Message VPN value overrides the value provided for the Bridge overall. The default value is `\"\"`.
   * @return clientUsername
  **/
  @ApiModelProperty(example = "null", value = "The Client Username the Bridge uses to login to the remote Message VPN. This per remote Message VPN value overrides the value provided for the Bridge overall. The default value is `\"\"`.")
  public String getClientUsername() {
    return clientUsername;
  }

  public void setClientUsername(String clientUsername) {
    this.clientUsername = clientUsername;
  }

  public MsgVpnBridgeRemoteMsgVpn compressedDataEnabled(Boolean compressedDataEnabled) {
    this.compressedDataEnabled = compressedDataEnabled;
    return this;
  }

   /**
   * Enable or disable data compression for the remote Message VPN connection. The default value is `false`.
   * @return compressedDataEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable data compression for the remote Message VPN connection. The default value is `false`.")
  public Boolean getCompressedDataEnabled() {
    return compressedDataEnabled;
  }

  public void setCompressedDataEnabled(Boolean compressedDataEnabled) {
    this.compressedDataEnabled = compressedDataEnabled;
  }

  public MsgVpnBridgeRemoteMsgVpn connectOrder(Integer connectOrder) {
    this.connectOrder = connectOrder;
    return this;
  }

   /**
   * The preference given to incoming connections from remote Message VPN hosts, from 1 (highest priority) to 4 (lowest priority). The default value is `4`.
   * @return connectOrder
  **/
  @ApiModelProperty(example = "null", value = "The preference given to incoming connections from remote Message VPN hosts, from 1 (highest priority) to 4 (lowest priority). The default value is `4`.")
  public Integer getConnectOrder() {
    return connectOrder;
  }

  public void setConnectOrder(Integer connectOrder) {
    this.connectOrder = connectOrder;
  }

  public MsgVpnBridgeRemoteMsgVpn egressFlowWindowSize(Long egressFlowWindowSize) {
    this.egressFlowWindowSize = egressFlowWindowSize;
    return this;
  }

   /**
   * The number of outstanding guaranteed messages that can be transmitted over the remote Message VPN connection before an acknowledgement is received. The default value is `255`.
   * @return egressFlowWindowSize
  **/
  @ApiModelProperty(example = "null", value = "The number of outstanding guaranteed messages that can be transmitted over the remote Message VPN connection before an acknowledgement is received. The default value is `255`.")
  public Long getEgressFlowWindowSize() {
    return egressFlowWindowSize;
  }

  public void setEgressFlowWindowSize(Long egressFlowWindowSize) {
    this.egressFlowWindowSize = egressFlowWindowSize;
  }

  public MsgVpnBridgeRemoteMsgVpn enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the remote Message VPN. The default value is `false`.
   * @return enabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the remote Message VPN. The default value is `false`.")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public MsgVpnBridgeRemoteMsgVpn msgVpnName(String msgVpnName) {
    this.msgVpnName = msgVpnName;
    return this;
  }

   /**
   * The name of the Message VPN.
   * @return msgVpnName
  **/
  @ApiModelProperty(example = "null", value = "The name of the Message VPN.")
  public String getMsgVpnName() {
    return msgVpnName;
  }

  public void setMsgVpnName(String msgVpnName) {
    this.msgVpnName = msgVpnName;
  }

  public MsgVpnBridgeRemoteMsgVpn password(String password) {
    this.password = password;
    return this;
  }

   /**
   * The password for the Client Username. The default is to have no `password`.
   * @return password
  **/
  @ApiModelProperty(example = "null", value = "The password for the Client Username. The default is to have no `password`.")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public MsgVpnBridgeRemoteMsgVpn queueBinding(String queueBinding) {
    this.queueBinding = queueBinding;
    return this;
  }

   /**
   * The queue binding of the Bridge in the remote Message VPN. The default value is `\"\"`.
   * @return queueBinding
  **/
  @ApiModelProperty(example = "null", value = "The queue binding of the Bridge in the remote Message VPN. The default value is `\"\"`.")
  public String getQueueBinding() {
    return queueBinding;
  }

  public void setQueueBinding(String queueBinding) {
    this.queueBinding = queueBinding;
  }

  public MsgVpnBridgeRemoteMsgVpn remoteMsgVpnInterface(String remoteMsgVpnInterface) {
    this.remoteMsgVpnInterface = remoteMsgVpnInterface;
    return this;
  }

   /**
   * The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, `remoteMsgVpnLocation` must not be a virtual router name.
   * @return remoteMsgVpnInterface
  **/
  @ApiModelProperty(example = "null", value = "The physical interface on the local Message VPN host for connecting to the remote Message VPN. By default, an interface is chosen automatically (recommended), but if specified, `remoteMsgVpnLocation` must not be a virtual router name.")
  public String getRemoteMsgVpnInterface() {
    return remoteMsgVpnInterface;
  }

  public void setRemoteMsgVpnInterface(String remoteMsgVpnInterface) {
    this.remoteMsgVpnInterface = remoteMsgVpnInterface;
  }

  public MsgVpnBridgeRemoteMsgVpn remoteMsgVpnLocation(String remoteMsgVpnLocation) {
    this.remoteMsgVpnLocation = remoteMsgVpnLocation;
    return this;
  }

   /**
   * The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \"v:\").
   * @return remoteMsgVpnLocation
  **/
  @ApiModelProperty(example = "null", value = "The location of the remote Message VPN as either an FQDN with port, IP address with port, or virtual router name (starting with \"v:\").")
  public String getRemoteMsgVpnLocation() {
    return remoteMsgVpnLocation;
  }

  public void setRemoteMsgVpnLocation(String remoteMsgVpnLocation) {
    this.remoteMsgVpnLocation = remoteMsgVpnLocation;
  }

  public MsgVpnBridgeRemoteMsgVpn remoteMsgVpnName(String remoteMsgVpnName) {
    this.remoteMsgVpnName = remoteMsgVpnName;
    return this;
  }

   /**
   * The name of the remote Message VPN.
   * @return remoteMsgVpnName
  **/
  @ApiModelProperty(example = "null", value = "The name of the remote Message VPN.")
  public String getRemoteMsgVpnName() {
    return remoteMsgVpnName;
  }

  public void setRemoteMsgVpnName(String remoteMsgVpnName) {
    this.remoteMsgVpnName = remoteMsgVpnName;
  }

  public MsgVpnBridgeRemoteMsgVpn tlsEnabled(Boolean tlsEnabled) {
    this.tlsEnabled = tlsEnabled;
    return this;
  }

   /**
   * Enable or disable TLS encryption for the remote Message VPN connection. The default value is `false`.
   * @return tlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable TLS encryption for the remote Message VPN connection. The default value is `false`.")
  public Boolean getTlsEnabled() {
    return tlsEnabled;
  }

  public void setTlsEnabled(Boolean tlsEnabled) {
    this.tlsEnabled = tlsEnabled;
  }

  public MsgVpnBridgeRemoteMsgVpn unidirectionalClientProfile(String unidirectionalClientProfile) {
    this.unidirectionalClientProfile = unidirectionalClientProfile;
    return this;
  }

   /**
   * The Client Profile for the unidirectional Bridge of the remote Message VPN. The Client Profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is `\"#client-profile\"`.
   * @return unidirectionalClientProfile
  **/
  @ApiModelProperty(example = "null", value = "The Client Profile for the unidirectional Bridge of the remote Message VPN. The Client Profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is `\"#client-profile\"`.")
  public String getUnidirectionalClientProfile() {
    return unidirectionalClientProfile;
  }

  public void setUnidirectionalClientProfile(String unidirectionalClientProfile) {
    this.unidirectionalClientProfile = unidirectionalClientProfile;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnBridgeRemoteMsgVpn msgVpnBridgeRemoteMsgVpn = (MsgVpnBridgeRemoteMsgVpn) o;
    return Objects.equals(this.bridgeName, msgVpnBridgeRemoteMsgVpn.bridgeName) &&
        Objects.equals(this.bridgeVirtualRouter, msgVpnBridgeRemoteMsgVpn.bridgeVirtualRouter) &&
        Objects.equals(this.clientUsername, msgVpnBridgeRemoteMsgVpn.clientUsername) &&
        Objects.equals(this.compressedDataEnabled, msgVpnBridgeRemoteMsgVpn.compressedDataEnabled) &&
        Objects.equals(this.connectOrder, msgVpnBridgeRemoteMsgVpn.connectOrder) &&
        Objects.equals(this.egressFlowWindowSize, msgVpnBridgeRemoteMsgVpn.egressFlowWindowSize) &&
        Objects.equals(this.enabled, msgVpnBridgeRemoteMsgVpn.enabled) &&
        Objects.equals(this.msgVpnName, msgVpnBridgeRemoteMsgVpn.msgVpnName) &&
        Objects.equals(this.password, msgVpnBridgeRemoteMsgVpn.password) &&
        Objects.equals(this.queueBinding, msgVpnBridgeRemoteMsgVpn.queueBinding) &&
        Objects.equals(this.remoteMsgVpnInterface, msgVpnBridgeRemoteMsgVpn.remoteMsgVpnInterface) &&
        Objects.equals(this.remoteMsgVpnLocation, msgVpnBridgeRemoteMsgVpn.remoteMsgVpnLocation) &&
        Objects.equals(this.remoteMsgVpnName, msgVpnBridgeRemoteMsgVpn.remoteMsgVpnName) &&
        Objects.equals(this.tlsEnabled, msgVpnBridgeRemoteMsgVpn.tlsEnabled) &&
        Objects.equals(this.unidirectionalClientProfile, msgVpnBridgeRemoteMsgVpn.unidirectionalClientProfile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bridgeName, bridgeVirtualRouter, clientUsername, compressedDataEnabled, connectOrder, egressFlowWindowSize, enabled, msgVpnName, password, queueBinding, remoteMsgVpnInterface, remoteMsgVpnLocation, remoteMsgVpnName, tlsEnabled, unidirectionalClientProfile);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnBridgeRemoteMsgVpn {\n");
    
    sb.append("    bridgeName: ").append(toIndentedString(bridgeName)).append("\n");
    sb.append("    bridgeVirtualRouter: ").append(toIndentedString(bridgeVirtualRouter)).append("\n");
    sb.append("    clientUsername: ").append(toIndentedString(clientUsername)).append("\n");
    sb.append("    compressedDataEnabled: ").append(toIndentedString(compressedDataEnabled)).append("\n");
    sb.append("    connectOrder: ").append(toIndentedString(connectOrder)).append("\n");
    sb.append("    egressFlowWindowSize: ").append(toIndentedString(egressFlowWindowSize)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    queueBinding: ").append(toIndentedString(queueBinding)).append("\n");
    sb.append("    remoteMsgVpnInterface: ").append(toIndentedString(remoteMsgVpnInterface)).append("\n");
    sb.append("    remoteMsgVpnLocation: ").append(toIndentedString(remoteMsgVpnLocation)).append("\n");
    sb.append("    remoteMsgVpnName: ").append(toIndentedString(remoteMsgVpnName)).append("\n");
    sb.append("    tlsEnabled: ").append(toIndentedString(tlsEnabled)).append("\n");
    sb.append("    unidirectionalClientProfile: ").append(toIndentedString(unidirectionalClientProfile)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

