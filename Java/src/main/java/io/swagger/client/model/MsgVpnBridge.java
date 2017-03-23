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
 * MsgVpnBridge
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-09-13T14:36:02.848Z")
public class MsgVpnBridge   {
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

  @SerializedName("enabled")
  private Boolean enabled = null;

  @SerializedName("maxTtl")
  private Integer maxTtl = null;

  @SerializedName("msgVpnName")
  private String msgVpnName = null;

  @SerializedName("remoteAuthenticationBasicClientUsername")
  private String remoteAuthenticationBasicClientUsername = null;

  @SerializedName("remoteAuthenticationBasicPassword")
  private String remoteAuthenticationBasicPassword = null;

  /**
   * The authentication scheme for the Remote Message VPN. The default value is `\"basic\"`. The allowed values and their meaning are:      \"basic\" - Basic Authentication Scheme (via username and password).     \"client-certificate\" - Client Certificate Authentication Scheme (via certificate-file). 
   */
  public enum RemoteAuthenticationSchemeEnum {
    @SerializedName("basic")
    BASIC("basic"),
    
    @SerializedName("client-certificate")
    CLIENT_CERTIFICATE("client-certificate");

    private String value;

    RemoteAuthenticationSchemeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("remoteAuthenticationScheme")
  private RemoteAuthenticationSchemeEnum remoteAuthenticationScheme = null;

  @SerializedName("remoteConnectionRetryCount")
  private Integer remoteConnectionRetryCount = null;

  @SerializedName("remoteConnectionRetryDelay")
  private Integer remoteConnectionRetryDelay = null;

  /**
   * The deliver-to-one priority for the bridge used on the remote router. The default value is `\"p1\"`. The allowed values and their meaning are:      \"p1\" - Priority 1 (highest).     \"p2\" - Priority 2.     \"p3\" - Priority 3.     \"p4\" - Priority 4 (lowest).     \"da\" - Deliver Always. 
   */
  public enum RemoteDeliverToOnePriorityEnum {
    @SerializedName("p1")
    P1("p1"),
    
    @SerializedName("p2")
    P2("p2"),
    
    @SerializedName("p3")
    P3("p3"),
    
    @SerializedName("p4")
    P4("p4"),
    
    @SerializedName("da")
    DA("da");

    private String value;

    RemoteDeliverToOnePriorityEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("remoteDeliverToOnePriority")
  private RemoteDeliverToOnePriorityEnum remoteDeliverToOnePriority = null;

  @SerializedName("tlsCipherSuiteList")
  private String tlsCipherSuiteList = null;

  public MsgVpnBridge bridgeName(String bridgeName) {
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

  public MsgVpnBridge bridgeVirtualRouter(BridgeVirtualRouterEnum bridgeVirtualRouter) {
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

  public MsgVpnBridge enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the bridge. The default value is `false`.
   * @return enabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the bridge. The default value is `false`.")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public MsgVpnBridge maxTtl(Integer maxTtl) {
    this.maxTtl = maxTtl;
    return this;
  }

   /**
   * The max-ttl value for the bridge, in hops. When a bridge is sending a message to the remote router, the TTL value for the message becomes the lower of its current TTL value or this value. The default value is `8`.
   * @return maxTtl
  **/
  @ApiModelProperty(example = "null", value = "The max-ttl value for the bridge, in hops. When a bridge is sending a message to the remote router, the TTL value for the message becomes the lower of its current TTL value or this value. The default value is `8`.")
  public Integer getMaxTtl() {
    return maxTtl;
  }

  public void setMaxTtl(Integer maxTtl) {
    this.maxTtl = maxTtl;
  }

  public MsgVpnBridge msgVpnName(String msgVpnName) {
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

  public MsgVpnBridge remoteAuthenticationBasicClientUsername(String remoteAuthenticationBasicClientUsername) {
    this.remoteAuthenticationBasicClientUsername = remoteAuthenticationBasicClientUsername;
    return this;
  }

   /**
   * The client username the bridge uses to login to the Remote Message VPN. The default is to have no `remoteAuthenticationBasicClientUsername`.
   * @return remoteAuthenticationBasicClientUsername
  **/
  @ApiModelProperty(example = "null", value = "The client username the bridge uses to login to the Remote Message VPN. The default is to have no `remoteAuthenticationBasicClientUsername`.")
  public String getRemoteAuthenticationBasicClientUsername() {
    return remoteAuthenticationBasicClientUsername;
  }

  public void setRemoteAuthenticationBasicClientUsername(String remoteAuthenticationBasicClientUsername) {
    this.remoteAuthenticationBasicClientUsername = remoteAuthenticationBasicClientUsername;
  }

  public MsgVpnBridge remoteAuthenticationBasicPassword(String remoteAuthenticationBasicPassword) {
    this.remoteAuthenticationBasicPassword = remoteAuthenticationBasicPassword;
    return this;
  }

   /**
   * The password for the client username the bridge uses to login to the Remote Message VPN. The default is to have no `remoteAuthenticationBasicPassword`.
   * @return remoteAuthenticationBasicPassword
  **/
  @ApiModelProperty(example = "null", value = "The password for the client username the bridge uses to login to the Remote Message VPN. The default is to have no `remoteAuthenticationBasicPassword`.")
  public String getRemoteAuthenticationBasicPassword() {
    return remoteAuthenticationBasicPassword;
  }

  public void setRemoteAuthenticationBasicPassword(String remoteAuthenticationBasicPassword) {
    this.remoteAuthenticationBasicPassword = remoteAuthenticationBasicPassword;
  }

  public MsgVpnBridge remoteAuthenticationScheme(RemoteAuthenticationSchemeEnum remoteAuthenticationScheme) {
    this.remoteAuthenticationScheme = remoteAuthenticationScheme;
    return this;
  }

   /**
   * The authentication scheme for the Remote Message VPN. The default value is `\"basic\"`. The allowed values and their meaning are:      \"basic\" - Basic Authentication Scheme (via username and password).     \"client-certificate\" - Client Certificate Authentication Scheme (via certificate-file). 
   * @return remoteAuthenticationScheme
  **/
  @ApiModelProperty(example = "null", value = "The authentication scheme for the Remote Message VPN. The default value is `\"basic\"`. The allowed values and their meaning are:      \"basic\" - Basic Authentication Scheme (via username and password).     \"client-certificate\" - Client Certificate Authentication Scheme (via certificate-file). ")
  public RemoteAuthenticationSchemeEnum getRemoteAuthenticationScheme() {
    return remoteAuthenticationScheme;
  }

  public void setRemoteAuthenticationScheme(RemoteAuthenticationSchemeEnum remoteAuthenticationScheme) {
    this.remoteAuthenticationScheme = remoteAuthenticationScheme;
  }

  public MsgVpnBridge remoteConnectionRetryCount(Integer remoteConnectionRetryCount) {
    this.remoteConnectionRetryCount = remoteConnectionRetryCount;
    return this;
  }

   /**
   * The number of retries that are attempted for a router name before the next remote router alternative is attempted. The default value is `0`.
   * @return remoteConnectionRetryCount
  **/
  @ApiModelProperty(example = "null", value = "The number of retries that are attempted for a router name before the next remote router alternative is attempted. The default value is `0`.")
  public Integer getRemoteConnectionRetryCount() {
    return remoteConnectionRetryCount;
  }

  public void setRemoteConnectionRetryCount(Integer remoteConnectionRetryCount) {
    this.remoteConnectionRetryCount = remoteConnectionRetryCount;
  }

  public MsgVpnBridge remoteConnectionRetryDelay(Integer remoteConnectionRetryDelay) {
    this.remoteConnectionRetryDelay = remoteConnectionRetryDelay;
    return this;
  }

   /**
   * The number of seconds that must pass before retrying a connection. The default value is `3`.
   * @return remoteConnectionRetryDelay
  **/
  @ApiModelProperty(example = "null", value = "The number of seconds that must pass before retrying a connection. The default value is `3`.")
  public Integer getRemoteConnectionRetryDelay() {
    return remoteConnectionRetryDelay;
  }

  public void setRemoteConnectionRetryDelay(Integer remoteConnectionRetryDelay) {
    this.remoteConnectionRetryDelay = remoteConnectionRetryDelay;
  }

  public MsgVpnBridge remoteDeliverToOnePriority(RemoteDeliverToOnePriorityEnum remoteDeliverToOnePriority) {
    this.remoteDeliverToOnePriority = remoteDeliverToOnePriority;
    return this;
  }

   /**
   * The deliver-to-one priority for the bridge used on the remote router. The default value is `\"p1\"`. The allowed values and their meaning are:      \"p1\" - Priority 1 (highest).     \"p2\" - Priority 2.     \"p3\" - Priority 3.     \"p4\" - Priority 4 (lowest).     \"da\" - Deliver Always. 
   * @return remoteDeliverToOnePriority
  **/
  @ApiModelProperty(example = "null", value = "The deliver-to-one priority for the bridge used on the remote router. The default value is `\"p1\"`. The allowed values and their meaning are:      \"p1\" - Priority 1 (highest).     \"p2\" - Priority 2.     \"p3\" - Priority 3.     \"p4\" - Priority 4 (lowest).     \"da\" - Deliver Always. ")
  public RemoteDeliverToOnePriorityEnum getRemoteDeliverToOnePriority() {
    return remoteDeliverToOnePriority;
  }

  public void setRemoteDeliverToOnePriority(RemoteDeliverToOnePriorityEnum remoteDeliverToOnePriority) {
    this.remoteDeliverToOnePriority = remoteDeliverToOnePriority;
  }

  public MsgVpnBridge tlsCipherSuiteList(String tlsCipherSuiteList) {
    this.tlsCipherSuiteList = tlsCipherSuiteList;
    return this;
  }

   /**
   * The colon-separated list of of cipher suites for the TLS authentication mechanism. The suite selected will be the first suite in the list that is supported by the remote router. The default value is `\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:AES256-GCM-SHA384:AES256-SHA256:AES256-SHA:ECDHE-RSA-DES-CBC3-SHA:DES-CBC3-SHA:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:AES128-GCM-SHA256:AES128-SHA256:AES128-SHA\"`.
   * @return tlsCipherSuiteList
  **/
  @ApiModelProperty(example = "null", value = "The colon-separated list of of cipher suites for the TLS authentication mechanism. The suite selected will be the first suite in the list that is supported by the remote router. The default value is `\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:AES256-GCM-SHA384:AES256-SHA256:AES256-SHA:ECDHE-RSA-DES-CBC3-SHA:DES-CBC3-SHA:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:AES128-GCM-SHA256:AES128-SHA256:AES128-SHA\"`.")
  public String getTlsCipherSuiteList() {
    return tlsCipherSuiteList;
  }

  public void setTlsCipherSuiteList(String tlsCipherSuiteList) {
    this.tlsCipherSuiteList = tlsCipherSuiteList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnBridge msgVpnBridge = (MsgVpnBridge) o;
    return Objects.equals(this.bridgeName, msgVpnBridge.bridgeName) &&
        Objects.equals(this.bridgeVirtualRouter, msgVpnBridge.bridgeVirtualRouter) &&
        Objects.equals(this.enabled, msgVpnBridge.enabled) &&
        Objects.equals(this.maxTtl, msgVpnBridge.maxTtl) &&
        Objects.equals(this.msgVpnName, msgVpnBridge.msgVpnName) &&
        Objects.equals(this.remoteAuthenticationBasicClientUsername, msgVpnBridge.remoteAuthenticationBasicClientUsername) &&
        Objects.equals(this.remoteAuthenticationBasicPassword, msgVpnBridge.remoteAuthenticationBasicPassword) &&
        Objects.equals(this.remoteAuthenticationScheme, msgVpnBridge.remoteAuthenticationScheme) &&
        Objects.equals(this.remoteConnectionRetryCount, msgVpnBridge.remoteConnectionRetryCount) &&
        Objects.equals(this.remoteConnectionRetryDelay, msgVpnBridge.remoteConnectionRetryDelay) &&
        Objects.equals(this.remoteDeliverToOnePriority, msgVpnBridge.remoteDeliverToOnePriority) &&
        Objects.equals(this.tlsCipherSuiteList, msgVpnBridge.tlsCipherSuiteList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bridgeName, bridgeVirtualRouter, enabled, maxTtl, msgVpnName, remoteAuthenticationBasicClientUsername, remoteAuthenticationBasicPassword, remoteAuthenticationScheme, remoteConnectionRetryCount, remoteConnectionRetryDelay, remoteDeliverToOnePriority, tlsCipherSuiteList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnBridge {\n");
    
    sb.append("    bridgeName: ").append(toIndentedString(bridgeName)).append("\n");
    sb.append("    bridgeVirtualRouter: ").append(toIndentedString(bridgeVirtualRouter)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    maxTtl: ").append(toIndentedString(maxTtl)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    remoteAuthenticationBasicClientUsername: ").append(toIndentedString(remoteAuthenticationBasicClientUsername)).append("\n");
    sb.append("    remoteAuthenticationBasicPassword: ").append(toIndentedString(remoteAuthenticationBasicPassword)).append("\n");
    sb.append("    remoteAuthenticationScheme: ").append(toIndentedString(remoteAuthenticationScheme)).append("\n");
    sb.append("    remoteConnectionRetryCount: ").append(toIndentedString(remoteConnectionRetryCount)).append("\n");
    sb.append("    remoteConnectionRetryDelay: ").append(toIndentedString(remoteConnectionRetryDelay)).append("\n");
    sb.append("    remoteDeliverToOnePriority: ").append(toIndentedString(remoteDeliverToOnePriority)).append("\n");
    sb.append("    tlsCipherSuiteList: ").append(toIndentedString(tlsCipherSuiteList)).append("\n");
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

