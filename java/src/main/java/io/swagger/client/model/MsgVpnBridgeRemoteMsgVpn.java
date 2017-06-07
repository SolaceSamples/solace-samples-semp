/**
 * SEMP (Solace Element Management Protocol)
 *  SEMP (starting in `v2`, see [note 1](#notes)) is a RESTful API for configuring a Solace router.  SEMP uses URIs to address manageble **resources** of the Solace router. Resources are either individual **objects**, or **collections** of objects. The following APIs are provided:   API|Base Path|Purpose|Comments :---|:---|:---|:--- Configuration|/SEMP/v2/config|Reading and writing config state|See [note 2](#notes)    Resources are always nouns, with individual objects being singular and collections being plural. Objects within a collection are identified by an `obj-id`, which follows the collection name with the form `collection-name/obj-id`. Some examples:  <pre> /SEMP/v2/config/msgVpns                       ; MsgVpn collection /SEMP/v2/config/msgVpns/finance               ; MsgVpn object named \"finance\" /SEMP/v2/config/msgVpns/finance/queues        ; Queue collection within MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ ; Queue object named \"orderQ\" within MsgVpn \"finance\" </pre>  ## Collection Resources  Collections are unordered lists of objects (unless described as otherwise), and are described by JSON arrays. Each item in the array represents an object in the same manner as the individual object would normally be represented. The creation of a new object is done through its collection resource.  ## Object Resources  Objects are composed of attributes and collections, and are described by JSON content as name/value pairs. The collections of an object are not contained directly in the object's JSON content, rather the content includes a URI attribute which points to the collection. This contained collection resource must be managed as a separate resource through this URI.  At a minimum, every object has 1 or more identifying attributes, and its own `uri` attribute which contains the URI to itself. Attributes may have any (non-exclusively) of the following properties:   Property|Meaning|Comments :---|:---|:--- Identifying|Attribute is involved in unique identification of the object, and appears in its URI| Required|Attribute must be provided in the request| Read-Only|Attribute can only be read, not written|See [note 3](#notes) Write-Only|Attribute can only be written, not read| Requires-Disable|Attribute can only be changed when object is disabled| Deprecated|Attribute is deprecated, and will disappear in the next SEMP version|    In some requests, certain attributes may only be provided in certain combinations with other attributes:   Relationship|Meaning :---|:--- Requires|Attribute may only be changed by a request if a particular attribute or combination of attributes is also provided in the request Conflicts|Attribute may only be provided in a request if a particular attribute or combination of attributes is not also provided in the request    ## HTTP Methods  The HTTP methods of POST, PUT, PATCH, DELETE, and GET manipulate resources following these general principles:   Method|Resource|Meaning|Request Body|Response Body|Missing Request Attributes :---|:---|:---|:---|:---|:--- POST|Collection|Create object|Initial attribute values|Object attributes and metadata|Set to default PUT|Object|Replace object|New attribute values|Object attributes and metadata|Set to default (but see [note 4](#notes)) PATCH|Object|Update object|New attribute values|Object attributes and metadata | Left unchanged| DELETE|Object|Delete object|Empty|Object metadata|N/A GET|Object|Get object|Empty|Object attributes and metadata|N/A GET|Collection|Get collection|Empty|Object attributes and collection metadata|N/A    ## Common Query Parameters  The following are some common query parameters that are supported by many method/URI combinations. Individual URIs may document additional parameters. Note that multiple query parameters can be used together in a single URI, separated by the ampersand character. For example:  <pre> ; Request for the MsgVpns collection using two hypothetical query parameters ; \"q1\" and \"q2\" with values \"val1\" and \"val2\" respectively /SEMP/v2/config/msgVpns?q1=val1&q2=val2 </pre>  ### select  Include in the response only selected attributes of the object. Use this query parameter to limit the size of the returned data for each returned object, or return only those fields that are desired.  The value of `select` is a comma-separated list of attribute names. Names may include the `*` wildcard. Nested attribute names are supported using periods (e.g. `parentName.childName`). If the list is empty (i.e. `select=`) no attributes are returned; otherwise the list must match at least one attribute name of the object. Some examples:  <pre> ; List of all MsgVpn names /SEMP/v2/config/msgVpns?select=msgVpnName  ; Authentication attributes of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance?select=authentication*  ; Access related attributes of Queue \"orderQ\" of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ?select=owner,permission </pre>  ### where  Include in the response only objects where certain conditions are true. Use this query parameter to limit which objects are returned to those whose attribute values meet the given conditions.  The value of `where` is a comma-separated list of expressions. All expressions must be true for the object to be included in the response. Each expression takes the form:  <pre> expression  = attribute-name OP value OP          = '==' | '!=' | '<' | '>' | '<=' | '>=' </pre>  `value` may be a number, string, `true`, or `false`, as appropriate for the type of `attribute-name`. Greater-than and less-than comparisons only work for numbers. A `*` in a string `value` is interpreted as a wildcard. Some examples:  <pre> ; Only enabled MsgVpns /SEMP/v2/config/msgVpns?where=enabled==true  ; Only MsgVpns using basic non-LDAP authentication /SEMP/v2/config/msgVpns?where=authenticationBasicEnabled==true,authenticationBasicType!=ldap  ; Only MsgVpns that allow more than 100 client connections /SEMP/v2/config/msgVpns?where=maxConnectionCount>100 </pre>  ### count  Limit the count of objects in the response. This can be useful to limit the size of the response for large collections. The minimum value for `count` is `1` and the default is `10`. For example:  <pre> ; Up to 25 MsgVpns /SEMP/v2/config/msgVpns?count=25 </pre>  ### cursor  The cursor, or position, for the next page of objects. Cursors are opaque data that should not be created or interpreted by SEMP clients, and should only be used as described below.  When a request is made for a collection and there may be additional objects available for retrieval that are not included in the initial response, the response will include a `cursorQuery` field containing a cursor. The value of this field can be specified in the `cursor` query parameter of a subsequent request to retrieve the next page of objects. For convenience, an appropriate URI is constructed automatically by the router and included in the `nextPageUri` field of the response. This URI can be used directly to retrieve the next page of objects.  ## Notes  1. This specification defines SEMP starting in `v2`, and not the original SEMP `v1` interface. Request and response formats between `v1` and `v2` are entirely incompatible, although both protocols share a common port configuration on the Solace router. They are differentiated by the initial portion of the URI path, one of either `/SEMP/` or `/SEMP/v2/`. 2. The config API is partially implemented. Only a subset of all configurable objects are available. 3. Read-only attributes may appear in POST and PUT/PATCH requests. However, if a read-only attribute is not marked as identifying, it will be ignored during a PUT/PATCH. 4. For PUT, if the SEMP user is not authorized to modify the attribute, its value is left unchanged rather than set to default. In addition, the values of write-only attributes are not set to their defaults on a PUT. 5. For DELETE, the body of the request currently serves no purpose and will cause an error if not empty. 
 *
 * OpenAPI spec version: 2.7.2.2.212
 * Contact: support_request@solacesystems.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.swagger.client.model;

import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * MsgVpnBridgeRemoteMsgVpn
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-09-13T14:36:02.848Z")
public class MsgVpnBridgeRemoteMsgVpn   {
  @SerializedName("bridgeName")
  private String bridgeName = null;

  /**
   * The virtual-router of the Bridge. The allowed values and their meaning are:      \"primary\" - Bridge belongs to the primary virtual-router.     \"backup\" - Bridge belongs to the backup virtual-router. 
   */
  public enum BridgeVirtualRouterEnum {
    @SerializedName("primary")
    PRIMARY("primary"),
    
    @SerializedName("backup")
    BACKUP("backup");

    private String value;

    BridgeVirtualRouterEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("bridgeVirtualRouter")
  private BridgeVirtualRouterEnum bridgeVirtualRouter = null;

  @SerializedName("clientUsername")
  private String clientUsername = null;

  @SerializedName("compressedDataEnabled")
  private Boolean compressedDataEnabled = null;

  @SerializedName("connectOrder")
  private Integer connectOrder = null;

  @SerializedName("egressFlowWindowSize")
  private Integer egressFlowWindowSize = null;

  @SerializedName("enabled")
  private Boolean enabled = null;

  @SerializedName("msgVpnName")
  private String msgVpnName = null;

  @SerializedName("password")
  private String password = null;

  @SerializedName("queueBinding")
  private String queueBinding = null;

  @SerializedName("remoteMsgVpnInterface")
  private String remoteMsgVpnInterface = null;

  @SerializedName("remoteMsgVpnLocation")
  private String remoteMsgVpnLocation = null;

  @SerializedName("remoteMsgVpnName")
  private String remoteMsgVpnName = null;

  @SerializedName("tlsEnabled")
  private Boolean tlsEnabled = null;

  @SerializedName("unidirectionalClientProfile")
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
   * The virtual-router of the Bridge. The allowed values and their meaning are:      \"primary\" - Bridge belongs to the primary virtual-router.     \"backup\" - Bridge belongs to the backup virtual-router. 
   * @return bridgeVirtualRouter
  **/
  @ApiModelProperty(example = "null", value = "The virtual-router of the Bridge. The allowed values and their meaning are:      \"primary\" - Bridge belongs to the primary virtual-router.     \"backup\" - Bridge belongs to the backup virtual-router. ")
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
   * The client username the bridge uses to login to the Remote Message VPN. This per Remote Message VPN value overrides the value provided for the bridge overall. The default is to have no `clientUsername`.
   * @return clientUsername
  **/
  @ApiModelProperty(example = "null", value = "The client username the bridge uses to login to the Remote Message VPN. This per Remote Message VPN value overrides the value provided for the bridge overall. The default is to have no `clientUsername`.")
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
   * Enable or disable data compression for the Remote Message VPN. The default value is `false`.
   * @return compressedDataEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable data compression for the Remote Message VPN. The default value is `false`.")
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
   * The order in which attempts to connect to different Message VPN hosts are attempted, or the preference given to incoming connections from remote routers, from `1` (highest priority) to `4` (lowest priority). The default value is `4`.
   * @return connectOrder
  **/
  @ApiModelProperty(example = "null", value = "The order in which attempts to connect to different Message VPN hosts are attempted, or the preference given to incoming connections from remote routers, from `1` (highest priority) to `4` (lowest priority). The default value is `4`.")
  public Integer getConnectOrder() {
    return connectOrder;
  }

  public void setConnectOrder(Integer connectOrder) {
    this.connectOrder = connectOrder;
  }

  public MsgVpnBridgeRemoteMsgVpn egressFlowWindowSize(Integer egressFlowWindowSize) {
    this.egressFlowWindowSize = egressFlowWindowSize;
    return this;
  }

   /**
   * The window size indicates how many outstanding guaranteed messages can be sent over the Remote Message VPN connection before acknowledgement is received by the sender. The default value is `255`.
   * @return egressFlowWindowSize
  **/
  @ApiModelProperty(example = "null", value = "The window size indicates how many outstanding guaranteed messages can be sent over the Remote Message VPN connection before acknowledgement is received by the sender. The default value is `255`.")
  public Integer getEgressFlowWindowSize() {
    return egressFlowWindowSize;
  }

  public void setEgressFlowWindowSize(Integer egressFlowWindowSize) {
    this.egressFlowWindowSize = egressFlowWindowSize;
  }

  public MsgVpnBridgeRemoteMsgVpn enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the Remote Message VPN. The default value is `false`.
   * @return enabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the Remote Message VPN. The default value is `false`.")
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
   * The password for the client username the bridge uses to login to the Remote Message VPN. The default is to have no `password`.
   * @return password
  **/
  @ApiModelProperty(example = "null", value = "The password for the client username the bridge uses to login to the Remote Message VPN. The default is to have no `password`.")
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
   * The queue binding of the bridge for this Remote Message VPN. The bridge attempts to bind to that queue over the bridge link once the link has been established, or immediately if it already is established. The queue must be configured on the remote router when the bridge connection is established. If the bind fails an event log is generated which includes the reason for the failure. The default is to have no `queueBinding`.
   * @return queueBinding
  **/
  @ApiModelProperty(example = "null", value = "The queue binding of the bridge for this Remote Message VPN. The bridge attempts to bind to that queue over the bridge link once the link has been established, or immediately if it already is established. The queue must be configured on the remote router when the bridge connection is established. If the bind fails an event log is generated which includes the reason for the failure. The default is to have no `queueBinding`.")
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
   * The interface on the local router through which to access the Remote Message VPN. If not provided (recommended) then an interface will be chosen automatically based on routing tables. If an interface is provided, `remoteMsgVpnLocation` must be either a hostname or IP address, not a virtual router-name.
   * @return remoteMsgVpnInterface
  **/
  @ApiModelProperty(example = "null", value = "The interface on the local router through which to access the Remote Message VPN. If not provided (recommended) then an interface will be chosen automatically based on routing tables. If an interface is provided, `remoteMsgVpnLocation` must be either a hostname or IP address, not a virtual router-name.")
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
   * The location of the Remote Message VPN. This may be given as either a hostname (resolvable via DNS), ip-address, or virtual router-name (starts with 'v:').
   * @return remoteMsgVpnLocation
  **/
  @ApiModelProperty(example = "null", value = "The location of the Remote Message VPN. This may be given as either a hostname (resolvable via DNS), ip-address, or virtual router-name (starts with 'v:').")
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
   * The name of the Remote Message VPN.
   * @return remoteMsgVpnName
  **/
  @ApiModelProperty(example = "null", value = "The name of the Remote Message VPN.")
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
   * Enable or disable TLS for the Remote Message VPN. The default value is `false`.
   * @return tlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable TLS for the Remote Message VPN. The default value is `false`.")
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
   * The client-profile for the unidirectional bridge for the Remote Message VPN. The client-profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is `\"#client-profile\"`.
   * @return unidirectionalClientProfile
  **/
  @ApiModelProperty(example = "null", value = "The client-profile for the unidirectional bridge for the Remote Message VPN. The client-profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is `\"#client-profile\"`.")
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

