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
 * MsgVpnRestDeliveryPointRestConsumer
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-09-13T14:36:02.848Z")
public class MsgVpnRestDeliveryPointRestConsumer   {
  @SerializedName("authenticationHttpBasicPassword")
  private String authenticationHttpBasicPassword = null;

  @SerializedName("authenticationHttpBasicUsername")
  private String authenticationHttpBasicUsername = null;

  /**
   * The authentication scheme used by the REST Consumer to login to the rest-host. The client-certificate auth-scheme is only compatible with connections using TLS. The default value is `\"none\"`. The allowed values and their meaning are:      \"none\" - Login with no authentication (login anonymously). This means no authentication is provided to the REST consumer when connecting to it. This is useful for anonymous connections or when no authentication is required by the REST consumer.     \"http-basic\" - Login with simple username / password credentials according to HTTP Basic authentication as per RFC2616.     \"client-certificate\" - Login with client TLS certificate as per RFC5246. 
   */
  public enum AuthenticationSchemeEnum {
    @SerializedName("none")
    NONE("none"),
    
    @SerializedName("http-basic")
    HTTP_BASIC("http-basic"),
    
    @SerializedName("client-certificate")
    CLIENT_CERTIFICATE("client-certificate");

    private String value;

    AuthenticationSchemeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("authenticationScheme")
  private AuthenticationSchemeEnum authenticationScheme = null;

  @SerializedName("enabled")
  private Boolean enabled = null;

  @SerializedName("localInterface")
  private String localInterface = null;

  @SerializedName("maxPostWaitTime")
  private Integer maxPostWaitTime = null;

  @SerializedName("msgVpnName")
  private String msgVpnName = null;

  @SerializedName("outgoingConnectionCount")
  private Integer outgoingConnectionCount = null;

  @SerializedName("remoteHost")
  private String remoteHost = null;

  @SerializedName("remotePort")
  private Integer remotePort = null;

  @SerializedName("restConsumerName")
  private String restConsumerName = null;

  @SerializedName("restDeliveryPointName")
  private String restDeliveryPointName = null;

  @SerializedName("retryDelay")
  private Integer retryDelay = null;

  @SerializedName("tlsCipherSuiteList")
  private String tlsCipherSuiteList = null;

  @SerializedName("tlsEnabled")
  private Boolean tlsEnabled = null;

  public MsgVpnRestDeliveryPointRestConsumer authenticationHttpBasicPassword(String authenticationHttpBasicPassword) {
    this.authenticationHttpBasicPassword = authenticationHttpBasicPassword;
    return this;
  }

   /**
   * The password that the REST Consumer will use to login to the rest-host. The default is to have no `authenticationHttpBasicPassword`.
   * @return authenticationHttpBasicPassword
  **/
  @ApiModelProperty(example = "null", value = "The password that the REST Consumer will use to login to the rest-host. The default is to have no `authenticationHttpBasicPassword`.")
  public String getAuthenticationHttpBasicPassword() {
    return authenticationHttpBasicPassword;
  }

  public void setAuthenticationHttpBasicPassword(String authenticationHttpBasicPassword) {
    this.authenticationHttpBasicPassword = authenticationHttpBasicPassword;
  }

  public MsgVpnRestDeliveryPointRestConsumer authenticationHttpBasicUsername(String authenticationHttpBasicUsername) {
    this.authenticationHttpBasicUsername = authenticationHttpBasicUsername;
    return this;
  }

   /**
   * The username that the REST Consumer will use to login to the rest-host. Normally a username is only configured when basic authentication is selected for the REST Consumer. The default is to have no `authenticationHttpBasicUsername`.
   * @return authenticationHttpBasicUsername
  **/
  @ApiModelProperty(example = "null", value = "The username that the REST Consumer will use to login to the rest-host. Normally a username is only configured when basic authentication is selected for the REST Consumer. The default is to have no `authenticationHttpBasicUsername`.")
  public String getAuthenticationHttpBasicUsername() {
    return authenticationHttpBasicUsername;
  }

  public void setAuthenticationHttpBasicUsername(String authenticationHttpBasicUsername) {
    this.authenticationHttpBasicUsername = authenticationHttpBasicUsername;
  }

  public MsgVpnRestDeliveryPointRestConsumer authenticationScheme(AuthenticationSchemeEnum authenticationScheme) {
    this.authenticationScheme = authenticationScheme;
    return this;
  }

   /**
   * The authentication scheme used by the REST Consumer to login to the rest-host. The client-certificate auth-scheme is only compatible with connections using TLS. The default value is `\"none\"`. The allowed values and their meaning are:      \"none\" - Login with no authentication (login anonymously). This means no authentication is provided to the REST consumer when connecting to it. This is useful for anonymous connections or when no authentication is required by the REST consumer.     \"http-basic\" - Login with simple username / password credentials according to HTTP Basic authentication as per RFC2616.     \"client-certificate\" - Login with client TLS certificate as per RFC5246. 
   * @return authenticationScheme
  **/
  @ApiModelProperty(example = "null", value = "The authentication scheme used by the REST Consumer to login to the rest-host. The client-certificate auth-scheme is only compatible with connections using TLS. The default value is `\"none\"`. The allowed values and their meaning are:      \"none\" - Login with no authentication (login anonymously). This means no authentication is provided to the REST consumer when connecting to it. This is useful for anonymous connections or when no authentication is required by the REST consumer.     \"http-basic\" - Login with simple username / password credentials according to HTTP Basic authentication as per RFC2616.     \"client-certificate\" - Login with client TLS certificate as per RFC5246. ")
  public AuthenticationSchemeEnum getAuthenticationScheme() {
    return authenticationScheme;
  }

  public void setAuthenticationScheme(AuthenticationSchemeEnum authenticationScheme) {
    this.authenticationScheme = authenticationScheme;
  }

  public MsgVpnRestDeliveryPointRestConsumer enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable this REST Consumer. When disabled, no connections are initiated or messages delivered to this particular REST Consumer. The default value is `false`.
   * @return enabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable this REST Consumer. When disabled, no connections are initiated or messages delivered to this particular REST Consumer. The default value is `false`.")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public MsgVpnRestDeliveryPointRestConsumer localInterface(String localInterface) {
    this.localInterface = localInterface;
    return this;
  }

   /**
   * The interface that will be used for all outgoing connections associated with the given REST consumer. The source IP address used for these connections will always be the IP address associated with the AD-enabled virtual-router for the specified interface. When unspecified the router will automatically choose an interface through which the REST consumer is reachable. The default is to have no `localInterface`.
   * @return localInterface
  **/
  @ApiModelProperty(example = "null", value = "The interface that will be used for all outgoing connections associated with the given REST consumer. The source IP address used for these connections will always be the IP address associated with the AD-enabled virtual-router for the specified interface. When unspecified the router will automatically choose an interface through which the REST consumer is reachable. The default is to have no `localInterface`.")
  public String getLocalInterface() {
    return localInterface;
  }

  public void setLocalInterface(String localInterface) {
    this.localInterface = localInterface;
  }

  public MsgVpnRestDeliveryPointRestConsumer maxPostWaitTime(Integer maxPostWaitTime) {
    this.maxPostWaitTime = maxPostWaitTime;
    return this;
  }

   /**
   * The maximum amount of time (in seconds) that the router will wait for a POST response from the REST Consumer. Once a POST operation has been outstanding for this period of time, the request is considered hung and the TCP connection is reset. If this POST is for a non-persistent message, the message is discarded. If this POST is for a persistent message, then message delivery is re-attempted via another available outgoing connection on any available outgoing connection for that RDP, up to the Max-Delivery-Count on the queue. If this count is exceeded, and the message is DMQ-eligible, then this message is moved to the DMQ, otherwise it is discarded. The default value is `30`.
   * @return maxPostWaitTime
  **/
  @ApiModelProperty(example = "null", value = "The maximum amount of time (in seconds) that the router will wait for a POST response from the REST Consumer. Once a POST operation has been outstanding for this period of time, the request is considered hung and the TCP connection is reset. If this POST is for a non-persistent message, the message is discarded. If this POST is for a persistent message, then message delivery is re-attempted via another available outgoing connection on any available outgoing connection for that RDP, up to the Max-Delivery-Count on the queue. If this count is exceeded, and the message is DMQ-eligible, then this message is moved to the DMQ, otherwise it is discarded. The default value is `30`.")
  public Integer getMaxPostWaitTime() {
    return maxPostWaitTime;
  }

  public void setMaxPostWaitTime(Integer maxPostWaitTime) {
    this.maxPostWaitTime = maxPostWaitTime;
  }

  public MsgVpnRestDeliveryPointRestConsumer msgVpnName(String msgVpnName) {
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

  public MsgVpnRestDeliveryPointRestConsumer outgoingConnectionCount(Integer outgoingConnectionCount) {
    this.outgoingConnectionCount = outgoingConnectionCount;
    return this;
  }

   /**
   * The total number of concurrent TCP connections open to this REST Consumer initiated by the router. Multiple connections to a single REST Consumer are typically desirable to increase throughput via concurrency. The more connections, the higher the potential throughput. The default value is `3`.
   * @return outgoingConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The total number of concurrent TCP connections open to this REST Consumer initiated by the router. Multiple connections to a single REST Consumer are typically desirable to increase throughput via concurrency. The more connections, the higher the potential throughput. The default value is `3`.")
  public Integer getOutgoingConnectionCount() {
    return outgoingConnectionCount;
  }

  public void setOutgoingConnectionCount(Integer outgoingConnectionCount) {
    this.outgoingConnectionCount = outgoingConnectionCount;
  }

  public MsgVpnRestDeliveryPointRestConsumer remoteHost(String remoteHost) {
    this.remoteHost = remoteHost;
    return this;
  }

   /**
   * The IPv4 address or DNS name to which the router is to connect to deliver messages for this REST Consumer. If the REST Consumer is enabled while the host value is not configured then the REST Consumer has an operational Down state due to the empty host configuration until a usable host value is configured. The default is to have no `remoteHost`.
   * @return remoteHost
  **/
  @ApiModelProperty(example = "null", value = "The IPv4 address or DNS name to which the router is to connect to deliver messages for this REST Consumer. If the REST Consumer is enabled while the host value is not configured then the REST Consumer has an operational Down state due to the empty host configuration until a usable host value is configured. The default is to have no `remoteHost`.")
  public String getRemoteHost() {
    return remoteHost;
  }

  public void setRemoteHost(String remoteHost) {
    this.remoteHost = remoteHost;
  }

  public MsgVpnRestDeliveryPointRestConsumer remotePort(Integer remotePort) {
    this.remotePort = remotePort;
    return this;
  }

   /**
   * The port associated with the host of the current REST Consumer. The default value is `8080`.
   * @return remotePort
  **/
  @ApiModelProperty(example = "null", value = "The port associated with the host of the current REST Consumer. The default value is `8080`.")
  public Integer getRemotePort() {
    return remotePort;
  }

  public void setRemotePort(Integer remotePort) {
    this.remotePort = remotePort;
  }

  public MsgVpnRestDeliveryPointRestConsumer restConsumerName(String restConsumerName) {
    this.restConsumerName = restConsumerName;
    return this;
  }

   /**
   * An RDP-wide unique name for the REST consumer.
   * @return restConsumerName
  **/
  @ApiModelProperty(example = "null", value = "An RDP-wide unique name for the REST consumer.")
  public String getRestConsumerName() {
    return restConsumerName;
  }

  public void setRestConsumerName(String restConsumerName) {
    this.restConsumerName = restConsumerName;
  }

  public MsgVpnRestDeliveryPointRestConsumer restDeliveryPointName(String restDeliveryPointName) {
    this.restDeliveryPointName = restDeliveryPointName;
    return this;
  }

   /**
   * A Message VPN-wide unique name for the REST Delivery Point. This name is used to auto-generate a client-username in this Message VPN, which is used by the client for this RDP.
   * @return restDeliveryPointName
  **/
  @ApiModelProperty(example = "null", value = "A Message VPN-wide unique name for the REST Delivery Point. This name is used to auto-generate a client-username in this Message VPN, which is used by the client for this RDP.")
  public String getRestDeliveryPointName() {
    return restDeliveryPointName;
  }

  public void setRestDeliveryPointName(String restDeliveryPointName) {
    this.restDeliveryPointName = restDeliveryPointName;
  }

  public MsgVpnRestDeliveryPointRestConsumer retryDelay(Integer retryDelay) {
    this.retryDelay = retryDelay;
    return this;
  }

   /**
   * The number of seconds that must pass before retrying a connection. The default value is `3`.
   * @return retryDelay
  **/
  @ApiModelProperty(example = "null", value = "The number of seconds that must pass before retrying a connection. The default value is `3`.")
  public Integer getRetryDelay() {
    return retryDelay;
  }

  public void setRetryDelay(Integer retryDelay) {
    this.retryDelay = retryDelay;
  }

  public MsgVpnRestDeliveryPointRestConsumer tlsCipherSuiteList(String tlsCipherSuiteList) {
    this.tlsCipherSuiteList = tlsCipherSuiteList;
    return this;
  }

   /**
   * The colon-separated list of cipher-suites the REST Consumer uses in its encrypted connection. All supported suites are included by default, from most-secure to least-secure. The remote server (REST Consumer) should choose the first suite from this list that it supports. The cipher-suite list can only be changed when the REST Consumer is disabled. The default value is `\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:AES256-GCM-SHA384:AES256-SHA256:AES256-SHA:ECDHE-RSA-DES-CBC3-SHA:DES-CBC3-SHA:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:AES128-GCM-SHA256:AES128-SHA256:AES128-SHA\"`.
   * @return tlsCipherSuiteList
  **/
  @ApiModelProperty(example = "null", value = "The colon-separated list of cipher-suites the REST Consumer uses in its encrypted connection. All supported suites are included by default, from most-secure to least-secure. The remote server (REST Consumer) should choose the first suite from this list that it supports. The cipher-suite list can only be changed when the REST Consumer is disabled. The default value is `\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:AES256-GCM-SHA384:AES256-SHA256:AES256-SHA:ECDHE-RSA-DES-CBC3-SHA:DES-CBC3-SHA:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:AES128-GCM-SHA256:AES128-SHA256:AES128-SHA\"`.")
  public String getTlsCipherSuiteList() {
    return tlsCipherSuiteList;
  }

  public void setTlsCipherSuiteList(String tlsCipherSuiteList) {
    this.tlsCipherSuiteList = tlsCipherSuiteList;
  }

  public MsgVpnRestDeliveryPointRestConsumer tlsEnabled(Boolean tlsEnabled) {
    this.tlsEnabled = tlsEnabled;
    return this;
  }

   /**
   * Enable or disable TLS for the REST Consumer. This may only be done when the REST Consumer is disabled. The default value is `false`.
   * @return tlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable TLS for the REST Consumer. This may only be done when the REST Consumer is disabled. The default value is `false`.")
  public Boolean getTlsEnabled() {
    return tlsEnabled;
  }

  public void setTlsEnabled(Boolean tlsEnabled) {
    this.tlsEnabled = tlsEnabled;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnRestDeliveryPointRestConsumer msgVpnRestDeliveryPointRestConsumer = (MsgVpnRestDeliveryPointRestConsumer) o;
    return Objects.equals(this.authenticationHttpBasicPassword, msgVpnRestDeliveryPointRestConsumer.authenticationHttpBasicPassword) &&
        Objects.equals(this.authenticationHttpBasicUsername, msgVpnRestDeliveryPointRestConsumer.authenticationHttpBasicUsername) &&
        Objects.equals(this.authenticationScheme, msgVpnRestDeliveryPointRestConsumer.authenticationScheme) &&
        Objects.equals(this.enabled, msgVpnRestDeliveryPointRestConsumer.enabled) &&
        Objects.equals(this.localInterface, msgVpnRestDeliveryPointRestConsumer.localInterface) &&
        Objects.equals(this.maxPostWaitTime, msgVpnRestDeliveryPointRestConsumer.maxPostWaitTime) &&
        Objects.equals(this.msgVpnName, msgVpnRestDeliveryPointRestConsumer.msgVpnName) &&
        Objects.equals(this.outgoingConnectionCount, msgVpnRestDeliveryPointRestConsumer.outgoingConnectionCount) &&
        Objects.equals(this.remoteHost, msgVpnRestDeliveryPointRestConsumer.remoteHost) &&
        Objects.equals(this.remotePort, msgVpnRestDeliveryPointRestConsumer.remotePort) &&
        Objects.equals(this.restConsumerName, msgVpnRestDeliveryPointRestConsumer.restConsumerName) &&
        Objects.equals(this.restDeliveryPointName, msgVpnRestDeliveryPointRestConsumer.restDeliveryPointName) &&
        Objects.equals(this.retryDelay, msgVpnRestDeliveryPointRestConsumer.retryDelay) &&
        Objects.equals(this.tlsCipherSuiteList, msgVpnRestDeliveryPointRestConsumer.tlsCipherSuiteList) &&
        Objects.equals(this.tlsEnabled, msgVpnRestDeliveryPointRestConsumer.tlsEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticationHttpBasicPassword, authenticationHttpBasicUsername, authenticationScheme, enabled, localInterface, maxPostWaitTime, msgVpnName, outgoingConnectionCount, remoteHost, remotePort, restConsumerName, restDeliveryPointName, retryDelay, tlsCipherSuiteList, tlsEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnRestDeliveryPointRestConsumer {\n");
    
    sb.append("    authenticationHttpBasicPassword: ").append(toIndentedString(authenticationHttpBasicPassword)).append("\n");
    sb.append("    authenticationHttpBasicUsername: ").append(toIndentedString(authenticationHttpBasicUsername)).append("\n");
    sb.append("    authenticationScheme: ").append(toIndentedString(authenticationScheme)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    localInterface: ").append(toIndentedString(localInterface)).append("\n");
    sb.append("    maxPostWaitTime: ").append(toIndentedString(maxPostWaitTime)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    outgoingConnectionCount: ").append(toIndentedString(outgoingConnectionCount)).append("\n");
    sb.append("    remoteHost: ").append(toIndentedString(remoteHost)).append("\n");
    sb.append("    remotePort: ").append(toIndentedString(remotePort)).append("\n");
    sb.append("    restConsumerName: ").append(toIndentedString(restConsumerName)).append("\n");
    sb.append("    restDeliveryPointName: ").append(toIndentedString(restDeliveryPointName)).append("\n");
    sb.append("    retryDelay: ").append(toIndentedString(retryDelay)).append("\n");
    sb.append("    tlsCipherSuiteList: ").append(toIndentedString(tlsCipherSuiteList)).append("\n");
    sb.append("    tlsEnabled: ").append(toIndentedString(tlsEnabled)).append("\n");
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

