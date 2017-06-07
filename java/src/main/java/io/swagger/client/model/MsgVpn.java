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
import io.swagger.client.model.EventThreshold;
import io.swagger.client.model.EventThresholdByValue;


/**
 * MsgVpn
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-09-13T14:36:02.848Z")
public class MsgVpn   {
  @SerializedName("authenticationBasicEnabled")
  private Boolean authenticationBasicEnabled = null;

  @SerializedName("authenticationBasicProfileName")
  private String authenticationBasicProfileName = null;

  @SerializedName("authenticationBasicRadiusDomain")
  private String authenticationBasicRadiusDomain = null;

  /**
   * Authentication mechanism to be used for basic authentication of clients connecting to this Message VPN. The default value is `\"radius\"`. The allowed values and their meaning are:      \"radius\" - Radius authentication. A radius profile must be provided.     \"ldap\" - LDAP authentication. An LDAP profile must be provided.     \"internal\" - Internal database. Authentication is against Client Usernames.     \"none\" - No authentication. Anonymous login allowed. 
   */
  public enum AuthenticationBasicTypeEnum {
    @SerializedName("radius")
    RADIUS("radius"),
    
    @SerializedName("ldap")
    LDAP("ldap"),
    
    @SerializedName("internal")
    INTERNAL("internal"),
    
    @SerializedName("none")
    NONE("none");

    private String value;

    AuthenticationBasicTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("authenticationBasicType")
  private AuthenticationBasicTypeEnum authenticationBasicType = null;

  @SerializedName("authenticationClientCertAllowApiProvidedUsernameEnabled")
  private Boolean authenticationClientCertAllowApiProvidedUsernameEnabled = null;

  @SerializedName("authenticationClientCertEnabled")
  private Boolean authenticationClientCertEnabled = null;

  @SerializedName("authenticationClientCertMaxChainDepth")
  private Integer authenticationClientCertMaxChainDepth = null;

  @SerializedName("authenticationClientCertValidateDateEnabled")
  private Boolean authenticationClientCertValidateDateEnabled = null;

  @SerializedName("authenticationKerberosAllowApiProvidedUsernameEnabled")
  private Boolean authenticationKerberosAllowApiProvidedUsernameEnabled = null;

  @SerializedName("authenticationKerberosEnabled")
  private Boolean authenticationKerberosEnabled = null;

  @SerializedName("authorizationLdapGroupMembershipAttributeName")
  private String authorizationLdapGroupMembershipAttributeName = null;

  @SerializedName("authorizationProfileName")
  private String authorizationProfileName = null;

  /**
   * Authorization mechanism to be used for clients connecting to this Message VPN. The default value is `\"internal\"`. The allowed values and their meaning are:      \"ldap\" - LDAP authorization.     \"internal\" - Internal authorization. 
   */
  public enum AuthorizationTypeEnum {
    @SerializedName("ldap")
    LDAP("ldap"),
    
    @SerializedName("internal")
    INTERNAL("internal");

    private String value;

    AuthorizationTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("authorizationType")
  private AuthorizationTypeEnum authorizationType = null;

  @SerializedName("bridgingTlsServerCertEnforceTrustedCommonNameEnabled")
  private Boolean bridgingTlsServerCertEnforceTrustedCommonNameEnabled = null;

  @SerializedName("bridgingTlsServerCertMaxChainDepth")
  private Integer bridgingTlsServerCertMaxChainDepth = null;

  @SerializedName("bridgingTlsServerCertValidateDateEnabled")
  private Boolean bridgingTlsServerCertValidateDateEnabled = null;

  @SerializedName("distributedCacheManagementEnabled")
  private Boolean distributedCacheManagementEnabled = null;

  @SerializedName("enabled")
  private Boolean enabled = null;

  @SerializedName("eventConnectionCountThreshold")
  private EventThreshold eventConnectionCountThreshold = null;

  @SerializedName("eventEgressFlowCountThreshold")
  private EventThreshold eventEgressFlowCountThreshold = null;

  @SerializedName("eventEgressMsgRateThreshold")
  private EventThresholdByValue eventEgressMsgRateThreshold = null;

  @SerializedName("eventEndpointCountThreshold")
  private EventThreshold eventEndpointCountThreshold = null;

  @SerializedName("eventIngressFlowCountThreshold")
  private EventThreshold eventIngressFlowCountThreshold = null;

  @SerializedName("eventIngressMsgRateThreshold")
  private EventThresholdByValue eventIngressMsgRateThreshold = null;

  @SerializedName("eventLargeMsgThreshold")
  private Integer eventLargeMsgThreshold = null;

  @SerializedName("eventLogTag")
  private String eventLogTag = null;

  @SerializedName("eventMsgSpoolUsageThreshold")
  private EventThreshold eventMsgSpoolUsageThreshold = null;

  @SerializedName("eventPublishClientEnabled")
  private Boolean eventPublishClientEnabled = null;

  @SerializedName("eventPublishMsgVpnEnabled")
  private Boolean eventPublishMsgVpnEnabled = null;

  /**
   *  Subscription level event message publishing mode. Format v1 modes use a publish topic of the form:      #LOG/INFO/SUB_ADD/subscribed-topic     #LOG/INFO/SUB_DEL/subscribed-topic  Format v2 modes use a publish topic of the form:      #LOG/INFO/SUB/router-name/ADD/vpn-name/client-name/subscribed-topic     #LOG/INFO/SUB/router-name/DEL/vpn-name/client-name/subscribed-topic  Format v2 is recommended. . The default value is `\"off\"`. The allowed values and their meaning are:      \"off\" - Disable client level event message publishing.     \"on-with-format-v1\" - Enable client level event message publishing with format v1.     \"on-with-no-unsubscribe-events-on-disconnect-format-v1\" - As \"on-with-format-v1\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions.     \"on-with-format-v2\" - Enable client level event message publishing with format v2.     \"on-with-no-unsubscribe-events-on-disconnect-format-v2\" - As \"on-with-format-v2\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. 
   */
  public enum EventPublishSubscriptionModeEnum {
    @SerializedName("off")
    OFF("off"),
    
    @SerializedName("on-with-format-v1")
    ON_WITH_FORMAT_V1("on-with-format-v1"),
    
    @SerializedName("on-with-no-unsubscribe-events-on-disconnect-format-v1")
    ON_WITH_NO_UNSUBSCRIBE_EVENTS_ON_DISCONNECT_FORMAT_V1("on-with-no-unsubscribe-events-on-disconnect-format-v1"),
    
    @SerializedName("on-with-format-v2")
    ON_WITH_FORMAT_V2("on-with-format-v2"),
    
    @SerializedName("on-with-no-unsubscribe-events-on-disconnect-format-v2")
    ON_WITH_NO_UNSUBSCRIBE_EVENTS_ON_DISCONNECT_FORMAT_V2("on-with-no-unsubscribe-events-on-disconnect-format-v2");

    private String value;

    EventPublishSubscriptionModeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("eventPublishSubscriptionMode")
  private EventPublishSubscriptionModeEnum eventPublishSubscriptionMode = null;

  @SerializedName("eventPublishTopicFormatMqttEnabled")
  private Boolean eventPublishTopicFormatMqttEnabled = null;

  @SerializedName("eventPublishTopicFormatSmfEnabled")
  private Boolean eventPublishTopicFormatSmfEnabled = null;

  @SerializedName("eventServiceRestIncomingConnectionCountThreshold")
  private EventThreshold eventServiceRestIncomingConnectionCountThreshold = null;

  @SerializedName("eventServiceSmfConnectionCountThreshold")
  private EventThreshold eventServiceSmfConnectionCountThreshold = null;

  @SerializedName("eventServiceWebConnectionCountThreshold")
  private EventThreshold eventServiceWebConnectionCountThreshold = null;

  @SerializedName("eventSubscriptionCountThreshold")
  private EventThreshold eventSubscriptionCountThreshold = null;

  @SerializedName("eventTransactedSessionCountThreshold")
  private EventThreshold eventTransactedSessionCountThreshold = null;

  @SerializedName("eventTransactionCountThreshold")
  private EventThreshold eventTransactionCountThreshold = null;

  @SerializedName("exportSubscriptionsEnabled")
  private Boolean exportSubscriptionsEnabled = null;

  @SerializedName("maxConnectionCount")
  private Integer maxConnectionCount = null;

  @SerializedName("maxEgressFlowCount")
  private Integer maxEgressFlowCount = null;

  @SerializedName("maxEndpointCount")
  private Integer maxEndpointCount = null;

  @SerializedName("maxIngressFlowCount")
  private Integer maxIngressFlowCount = null;

  @SerializedName("maxMsgSpoolUsage")
  private Integer maxMsgSpoolUsage = null;

  @SerializedName("maxSubscriptionCount")
  private Integer maxSubscriptionCount = null;

  @SerializedName("maxTransactedSessionCount")
  private Integer maxTransactedSessionCount = null;

  @SerializedName("maxTransactionCount")
  private Integer maxTransactionCount = null;

  @SerializedName("msgVpnName")
  private String msgVpnName = null;

  @SerializedName("replicationAckPropagationIntervalMsgCount")
  private Integer replicationAckPropagationIntervalMsgCount = null;

  @SerializedName("replicationBridgeAuthenticationBasicClientUsername")
  private String replicationBridgeAuthenticationBasicClientUsername = null;

  @SerializedName("replicationBridgeAuthenticationBasicPassword")
  private String replicationBridgeAuthenticationBasicPassword = null;

  /**
   * The authentication scheme for the replication bridge. The default value is `\"basic\"`. The allowed values and their meaning are:      \"basic\" - Basic Authentication Scheme (via username and password).     \"client-certificate\" - Client Certificate Authentication Scheme (via certificate-file). 
   */
  public enum ReplicationBridgeAuthenticationSchemeEnum {
    @SerializedName("basic")
    BASIC("basic"),
    
    @SerializedName("client-certificate")
    CLIENT_CERTIFICATE("client-certificate");

    private String value;

    ReplicationBridgeAuthenticationSchemeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("replicationBridgeAuthenticationScheme")
  private ReplicationBridgeAuthenticationSchemeEnum replicationBridgeAuthenticationScheme = null;

  @SerializedName("replicationBridgeCompressedDataEnabled")
  private Boolean replicationBridgeCompressedDataEnabled = null;

  @SerializedName("replicationBridgeEgressFlowWindowSize")
  private Integer replicationBridgeEgressFlowWindowSize = null;

  @SerializedName("replicationBridgeRetryDelay")
  private Integer replicationBridgeRetryDelay = null;

  @SerializedName("replicationBridgeTlsEnabled")
  private Boolean replicationBridgeTlsEnabled = null;

  @SerializedName("replicationBridgeUnidirectionalClientProfileName")
  private String replicationBridgeUnidirectionalClientProfileName = null;

  @SerializedName("replicationEnabled")
  private Boolean replicationEnabled = null;

  /**
   * The behavior to take when enabling replication, depending on the existance of the replication queue. This only has meaning in a request which enables replication. The default value is `\"fail-on-existing-queue\"`. The allowed values and their meaning are:      \"fail-on-existing-queue\" - The data replication queue must not already exist.     \"force-use-existing-queue\" - The data replication queue must already exist. Any data messages on the queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail.     \"force-recreate-queue\" - The data replication queue must already exist. Any data messages on the queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. 
   */
  public enum ReplicationEnabledQueueBehaviorEnum {
    @SerializedName("fail-on-existing-queue")
    FAIL_ON_EXISTING_QUEUE("fail-on-existing-queue"),
    
    @SerializedName("force-use-existing-queue")
    FORCE_USE_EXISTING_QUEUE("force-use-existing-queue"),
    
    @SerializedName("force-recreate-queue")
    FORCE_RECREATE_QUEUE("force-recreate-queue");

    private String value;

    ReplicationEnabledQueueBehaviorEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("replicationEnabledQueueBehavior")
  private ReplicationEnabledQueueBehaviorEnum replicationEnabledQueueBehavior = null;

  @SerializedName("replicationQueueMaxMsgSpoolUsage")
  private Integer replicationQueueMaxMsgSpoolUsage = null;

  @SerializedName("replicationQueueRejectMsgToSenderOnDiscardEnabled")
  private Boolean replicationQueueRejectMsgToSenderOnDiscardEnabled = null;

  @SerializedName("replicationRejectMsgWhenSyncIneligibleEnabled")
  private Boolean replicationRejectMsgWhenSyncIneligibleEnabled = null;

  /**
   * The replication role for this Message VPN. The default value is `\"standby\"`. The allowed values and their meaning are:      \"active\" - Assume the active role in replication for the Message VPN.     \"standby\" - Assume the standby role in replication for the Message VPN. 
   */
  public enum ReplicationRoleEnum {
    @SerializedName("active")
    ACTIVE("active"),
    
    @SerializedName("standby")
    STANDBY("standby");

    private String value;

    ReplicationRoleEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("replicationRole")
  private ReplicationRoleEnum replicationRole = null;

  /**
   * The transaction replication mode for all transactions within a Message VPN. When mode is async, all transactions originated by clients are replicated to the standby site using async-replication. When mode is sync, all transactions originated by clients are replicated to the standby site using sync-replication. Changing this value during operation will not affect existing transactions. It is only validated upon starting a transaction. The default value is `\"async\"`. The allowed values and their meaning are:      \"sync\" - Synchronous replication-mode.     \"async\" - Asynchronous replication-mode. 
   */
  public enum ReplicationTransactionModeEnum {
    @SerializedName("sync")
    SYNC("sync"),
    
    @SerializedName("async")
    ASYNC("async");

    private String value;

    ReplicationTransactionModeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("replicationTransactionMode")
  private ReplicationTransactionModeEnum replicationTransactionMode = null;

  @SerializedName("restTlsServerCertEnforceTrustedCommonNameEnabled")
  private Boolean restTlsServerCertEnforceTrustedCommonNameEnabled = null;

  @SerializedName("restTlsServerCertMaxChainDepth")
  private Integer restTlsServerCertMaxChainDepth = null;

  @SerializedName("restTlsServerCertValidateDateEnabled")
  private Boolean restTlsServerCertValidateDateEnabled = null;

  @SerializedName("sempOverMsgBusAdminClientEnabled")
  private Boolean sempOverMsgBusAdminClientEnabled = null;

  @SerializedName("sempOverMsgBusAdminDistributedCacheEnabled")
  private Boolean sempOverMsgBusAdminDistributedCacheEnabled = null;

  @SerializedName("sempOverMsgBusAdminEnabled")
  private Boolean sempOverMsgBusAdminEnabled = null;

  @SerializedName("sempOverMsgBusEnabled")
  private Boolean sempOverMsgBusEnabled = null;

  @SerializedName("sempOverMsgBusLegacyShowClearEnabled")
  private Boolean sempOverMsgBusLegacyShowClearEnabled = null;

  @SerializedName("sempOverMsgBusShowEnabled")
  private Boolean sempOverMsgBusShowEnabled = null;

  @SerializedName("serviceRestIncomingMaxConnectionCount")
  private Integer serviceRestIncomingMaxConnectionCount = null;

  @SerializedName("serviceRestIncomingPlainTextEnabled")
  private Boolean serviceRestIncomingPlainTextEnabled = null;

  @SerializedName("serviceRestIncomingPlainTextListenPort")
  private Integer serviceRestIncomingPlainTextListenPort = null;

  @SerializedName("serviceRestIncomingTlsEnabled")
  private Boolean serviceRestIncomingTlsEnabled = null;

  @SerializedName("serviceRestIncomingTlsListenPort")
  private Integer serviceRestIncomingTlsListenPort = null;

  @SerializedName("serviceRestOutgoingMaxConnectionCount")
  private Integer serviceRestOutgoingMaxConnectionCount = null;

  @SerializedName("serviceSmfMaxConnectionCount")
  private Integer serviceSmfMaxConnectionCount = null;

  @SerializedName("serviceSmfPlainTextEnabled")
  private Boolean serviceSmfPlainTextEnabled = null;

  @SerializedName("serviceSmfTlsEnabled")
  private Boolean serviceSmfTlsEnabled = null;

  @SerializedName("serviceWebMaxConnectionCount")
  private Integer serviceWebMaxConnectionCount = null;

  @SerializedName("serviceWebPlainTextEnabled")
  private Boolean serviceWebPlainTextEnabled = null;

  @SerializedName("serviceWebTlsEnabled")
  private Boolean serviceWebTlsEnabled = null;

  @SerializedName("tlsAllowDowngradeToPlainTextEnabled")
  private Boolean tlsAllowDowngradeToPlainTextEnabled = null;

  public MsgVpn authenticationBasicEnabled(Boolean authenticationBasicEnabled) {
    this.authenticationBasicEnabled = authenticationBasicEnabled;
    return this;
  }

   /**
   * Enable or disable basic authentication for clients within the Message VPN. Basic authentication is authentication that involves the use of a username and password to prove identity. When enabled, the currently selected authentication type is used for authentication of users that provide basic authentication credentials. If a user provides credentials for a different authentication scheme this setting is not applicable. The default value is `true`.
   * @return authenticationBasicEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable basic authentication for clients within the Message VPN. Basic authentication is authentication that involves the use of a username and password to prove identity. When enabled, the currently selected authentication type is used for authentication of users that provide basic authentication credentials. If a user provides credentials for a different authentication scheme this setting is not applicable. The default value is `true`.")
  public Boolean getAuthenticationBasicEnabled() {
    return authenticationBasicEnabled;
  }

  public void setAuthenticationBasicEnabled(Boolean authenticationBasicEnabled) {
    this.authenticationBasicEnabled = authenticationBasicEnabled;
  }

  public MsgVpn authenticationBasicProfileName(String authenticationBasicProfileName) {
    this.authenticationBasicProfileName = authenticationBasicProfileName;
    return this;
  }

   /**
   * The name of the RADIUS or LDAP profile to use when `authenticationBasicType` is `\"radius\"` or `\"ldap\"` respectively. The default value is `\"default\"`.
   * @return authenticationBasicProfileName
  **/
  @ApiModelProperty(example = "null", value = "The name of the RADIUS or LDAP profile to use when `authenticationBasicType` is `\"radius\"` or `\"ldap\"` respectively. The default value is `\"default\"`.")
  public String getAuthenticationBasicProfileName() {
    return authenticationBasicProfileName;
  }

  public void setAuthenticationBasicProfileName(String authenticationBasicProfileName) {
    this.authenticationBasicProfileName = authenticationBasicProfileName;
  }

  public MsgVpn authenticationBasicRadiusDomain(String authenticationBasicRadiusDomain) {
    this.authenticationBasicRadiusDomain = authenticationBasicRadiusDomain;
    return this;
  }

   /**
   * The RADIUS domain string to use when `authenticationBasicType` is `\"radius\"`. The default value is `\"\"`.
   * @return authenticationBasicRadiusDomain
  **/
  @ApiModelProperty(example = "null", value = "The RADIUS domain string to use when `authenticationBasicType` is `\"radius\"`. The default value is `\"\"`.")
  public String getAuthenticationBasicRadiusDomain() {
    return authenticationBasicRadiusDomain;
  }

  public void setAuthenticationBasicRadiusDomain(String authenticationBasicRadiusDomain) {
    this.authenticationBasicRadiusDomain = authenticationBasicRadiusDomain;
  }

  public MsgVpn authenticationBasicType(AuthenticationBasicTypeEnum authenticationBasicType) {
    this.authenticationBasicType = authenticationBasicType;
    return this;
  }

   /**
   * Authentication mechanism to be used for basic authentication of clients connecting to this Message VPN. The default value is `\"radius\"`. The allowed values and their meaning are:      \"radius\" - Radius authentication. A radius profile must be provided.     \"ldap\" - LDAP authentication. An LDAP profile must be provided.     \"internal\" - Internal database. Authentication is against Client Usernames.     \"none\" - No authentication. Anonymous login allowed. 
   * @return authenticationBasicType
  **/
  @ApiModelProperty(example = "null", value = "Authentication mechanism to be used for basic authentication of clients connecting to this Message VPN. The default value is `\"radius\"`. The allowed values and their meaning are:      \"radius\" - Radius authentication. A radius profile must be provided.     \"ldap\" - LDAP authentication. An LDAP profile must be provided.     \"internal\" - Internal database. Authentication is against Client Usernames.     \"none\" - No authentication. Anonymous login allowed. ")
  public AuthenticationBasicTypeEnum getAuthenticationBasicType() {
    return authenticationBasicType;
  }

  public void setAuthenticationBasicType(AuthenticationBasicTypeEnum authenticationBasicType) {
    this.authenticationBasicType = authenticationBasicType;
  }

  public MsgVpn authenticationClientCertAllowApiProvidedUsernameEnabled(Boolean authenticationClientCertAllowApiProvidedUsernameEnabled) {
    this.authenticationClientCertAllowApiProvidedUsernameEnabled = authenticationClientCertAllowApiProvidedUsernameEnabled;
    return this;
  }

   /**
   * When enabled, if the client specifies a client-username via the API connect method, the client provided username is used instead of the CN (Common Name) field of the certificate's subject. When disabled, the certificate CN is always used as the client-username. The default value is `false`.
   * @return authenticationClientCertAllowApiProvidedUsernameEnabled
  **/
  @ApiModelProperty(example = "null", value = "When enabled, if the client specifies a client-username via the API connect method, the client provided username is used instead of the CN (Common Name) field of the certificate's subject. When disabled, the certificate CN is always used as the client-username. The default value is `false`.")
  public Boolean getAuthenticationClientCertAllowApiProvidedUsernameEnabled() {
    return authenticationClientCertAllowApiProvidedUsernameEnabled;
  }

  public void setAuthenticationClientCertAllowApiProvidedUsernameEnabled(Boolean authenticationClientCertAllowApiProvidedUsernameEnabled) {
    this.authenticationClientCertAllowApiProvidedUsernameEnabled = authenticationClientCertAllowApiProvidedUsernameEnabled;
  }

  public MsgVpn authenticationClientCertEnabled(Boolean authenticationClientCertEnabled) {
    this.authenticationClientCertEnabled = authenticationClientCertEnabled;
    return this;
  }

   /**
   * Enable or disable client certificate client authentication in the Message VPN. The default value is `false`.
   * @return authenticationClientCertEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable client certificate client authentication in the Message VPN. The default value is `false`.")
  public Boolean getAuthenticationClientCertEnabled() {
    return authenticationClientCertEnabled;
  }

  public void setAuthenticationClientCertEnabled(Boolean authenticationClientCertEnabled) {
    this.authenticationClientCertEnabled = authenticationClientCertEnabled;
  }

  public MsgVpn authenticationClientCertMaxChainDepth(Integer authenticationClientCertMaxChainDepth) {
    this.authenticationClientCertMaxChainDepth = authenticationClientCertMaxChainDepth;
    return this;
  }

   /**
   * The maximum depth for a client certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.
   * @return authenticationClientCertMaxChainDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth for a client certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.")
  public Integer getAuthenticationClientCertMaxChainDepth() {
    return authenticationClientCertMaxChainDepth;
  }

  public void setAuthenticationClientCertMaxChainDepth(Integer authenticationClientCertMaxChainDepth) {
    this.authenticationClientCertMaxChainDepth = authenticationClientCertMaxChainDepth;
  }

  public MsgVpn authenticationClientCertValidateDateEnabled(Boolean authenticationClientCertValidateDateEnabled) {
    this.authenticationClientCertValidateDateEnabled = authenticationClientCertValidateDateEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.
   * @return authenticationClientCertValidateDateEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.")
  public Boolean getAuthenticationClientCertValidateDateEnabled() {
    return authenticationClientCertValidateDateEnabled;
  }

  public void setAuthenticationClientCertValidateDateEnabled(Boolean authenticationClientCertValidateDateEnabled) {
    this.authenticationClientCertValidateDateEnabled = authenticationClientCertValidateDateEnabled;
  }

  public MsgVpn authenticationKerberosAllowApiProvidedUsernameEnabled(Boolean authenticationKerberosAllowApiProvidedUsernameEnabled) {
    this.authenticationKerberosAllowApiProvidedUsernameEnabled = authenticationKerberosAllowApiProvidedUsernameEnabled;
    return this;
  }

   /**
   * When enabled, if the client specifies a client-username via the API connect method, the client provided username is used instead of the Kerberos Principal name in Kerberos token. When disabled, the Kerberos Principal name is always used as the client-username. The default value is `false`.
   * @return authenticationKerberosAllowApiProvidedUsernameEnabled
  **/
  @ApiModelProperty(example = "null", value = "When enabled, if the client specifies a client-username via the API connect method, the client provided username is used instead of the Kerberos Principal name in Kerberos token. When disabled, the Kerberos Principal name is always used as the client-username. The default value is `false`.")
  public Boolean getAuthenticationKerberosAllowApiProvidedUsernameEnabled() {
    return authenticationKerberosAllowApiProvidedUsernameEnabled;
  }

  public void setAuthenticationKerberosAllowApiProvidedUsernameEnabled(Boolean authenticationKerberosAllowApiProvidedUsernameEnabled) {
    this.authenticationKerberosAllowApiProvidedUsernameEnabled = authenticationKerberosAllowApiProvidedUsernameEnabled;
  }

  public MsgVpn authenticationKerberosEnabled(Boolean authenticationKerberosEnabled) {
    this.authenticationKerberosEnabled = authenticationKerberosEnabled;
    return this;
  }

   /**
   * Enable or disable Kerberos authentication for clients in the Message VPN. If a user provides credentials for a different authentication scheme, this setting is not applicable. The default value is `false`.
   * @return authenticationKerberosEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable Kerberos authentication for clients in the Message VPN. If a user provides credentials for a different authentication scheme, this setting is not applicable. The default value is `false`.")
  public Boolean getAuthenticationKerberosEnabled() {
    return authenticationKerberosEnabled;
  }

  public void setAuthenticationKerberosEnabled(Boolean authenticationKerberosEnabled) {
    this.authenticationKerberosEnabled = authenticationKerberosEnabled;
  }

  public MsgVpn authorizationLdapGroupMembershipAttributeName(String authorizationLdapGroupMembershipAttributeName) {
    this.authorizationLdapGroupMembershipAttributeName = authorizationLdapGroupMembershipAttributeName;
    return this;
  }

   /**
   * The name of the attribute that should be retrieved from the LDAP server as part of the LDAP search when authorizing a client. It indicates that the client belongs to a particular group (i.e. the value associated with this attribute). The default value is `\"memberOf\"`.
   * @return authorizationLdapGroupMembershipAttributeName
  **/
  @ApiModelProperty(example = "null", value = "The name of the attribute that should be retrieved from the LDAP server as part of the LDAP search when authorizing a client. It indicates that the client belongs to a particular group (i.e. the value associated with this attribute). The default value is `\"memberOf\"`.")
  public String getAuthorizationLdapGroupMembershipAttributeName() {
    return authorizationLdapGroupMembershipAttributeName;
  }

  public void setAuthorizationLdapGroupMembershipAttributeName(String authorizationLdapGroupMembershipAttributeName) {
    this.authorizationLdapGroupMembershipAttributeName = authorizationLdapGroupMembershipAttributeName;
  }

  public MsgVpn authorizationProfileName(String authorizationProfileName) {
    this.authorizationProfileName = authorizationProfileName;
    return this;
  }

   /**
   * The LDAP profile name to be used when `authorizationType` is `\"ldap\"`. The default value is `\"\"`.
   * @return authorizationProfileName
  **/
  @ApiModelProperty(example = "null", value = "The LDAP profile name to be used when `authorizationType` is `\"ldap\"`. The default value is `\"\"`.")
  public String getAuthorizationProfileName() {
    return authorizationProfileName;
  }

  public void setAuthorizationProfileName(String authorizationProfileName) {
    this.authorizationProfileName = authorizationProfileName;
  }

  public MsgVpn authorizationType(AuthorizationTypeEnum authorizationType) {
    this.authorizationType = authorizationType;
    return this;
  }

   /**
   * Authorization mechanism to be used for clients connecting to this Message VPN. The default value is `\"internal\"`. The allowed values and their meaning are:      \"ldap\" - LDAP authorization.     \"internal\" - Internal authorization. 
   * @return authorizationType
  **/
  @ApiModelProperty(example = "null", value = "Authorization mechanism to be used for clients connecting to this Message VPN. The default value is `\"internal\"`. The allowed values and their meaning are:      \"ldap\" - LDAP authorization.     \"internal\" - Internal authorization. ")
  public AuthorizationTypeEnum getAuthorizationType() {
    return authorizationType;
  }

  public void setAuthorizationType(AuthorizationTypeEnum authorizationType) {
    this.authorizationType = authorizationType;
  }

  public MsgVpn bridgingTlsServerCertEnforceTrustedCommonNameEnabled(Boolean bridgingTlsServerCertEnforceTrustedCommonNameEnabled) {
    this.bridgingTlsServerCertEnforceTrustedCommonNameEnabled = bridgingTlsServerCertEnforceTrustedCommonNameEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the common name in the server certificate on the remote router. If enabled, the common name is checked against the list of trusted common names configured for the bridge. The default value is `true`.
   * @return bridgingTlsServerCertEnforceTrustedCommonNameEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable validation of the common name in the server certificate on the remote router. If enabled, the common name is checked against the list of trusted common names configured for the bridge. The default value is `true`.")
  public Boolean getBridgingTlsServerCertEnforceTrustedCommonNameEnabled() {
    return bridgingTlsServerCertEnforceTrustedCommonNameEnabled;
  }

  public void setBridgingTlsServerCertEnforceTrustedCommonNameEnabled(Boolean bridgingTlsServerCertEnforceTrustedCommonNameEnabled) {
    this.bridgingTlsServerCertEnforceTrustedCommonNameEnabled = bridgingTlsServerCertEnforceTrustedCommonNameEnabled;
  }

  public MsgVpn bridgingTlsServerCertMaxChainDepth(Integer bridgingTlsServerCertMaxChainDepth) {
    this.bridgingTlsServerCertMaxChainDepth = bridgingTlsServerCertMaxChainDepth;
    return this;
  }

   /**
   * The maximum depth for a server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.
   * @return bridgingTlsServerCertMaxChainDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth for a server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.")
  public Integer getBridgingTlsServerCertMaxChainDepth() {
    return bridgingTlsServerCertMaxChainDepth;
  }

  public void setBridgingTlsServerCertMaxChainDepth(Integer bridgingTlsServerCertMaxChainDepth) {
    this.bridgingTlsServerCertMaxChainDepth = bridgingTlsServerCertMaxChainDepth;
  }

  public MsgVpn bridgingTlsServerCertValidateDateEnabled(Boolean bridgingTlsServerCertValidateDateEnabled) {
    this.bridgingTlsServerCertValidateDateEnabled = bridgingTlsServerCertValidateDateEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.
   * @return bridgingTlsServerCertValidateDateEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.")
  public Boolean getBridgingTlsServerCertValidateDateEnabled() {
    return bridgingTlsServerCertValidateDateEnabled;
  }

  public void setBridgingTlsServerCertValidateDateEnabled(Boolean bridgingTlsServerCertValidateDateEnabled) {
    this.bridgingTlsServerCertValidateDateEnabled = bridgingTlsServerCertValidateDateEnabled;
  }

  public MsgVpn distributedCacheManagementEnabled(Boolean distributedCacheManagementEnabled) {
    this.distributedCacheManagementEnabled = distributedCacheManagementEnabled;
    return this;
  }

   /**
   * Enable or disable managing of cache instances over the message bus. For a given Message VPN only one router in the network should have this attribute enabled. The default value is `true`.
   * @return distributedCacheManagementEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable managing of cache instances over the message bus. For a given Message VPN only one router in the network should have this attribute enabled. The default value is `true`.")
  public Boolean getDistributedCacheManagementEnabled() {
    return distributedCacheManagementEnabled;
  }

  public void setDistributedCacheManagementEnabled(Boolean distributedCacheManagementEnabled) {
    this.distributedCacheManagementEnabled = distributedCacheManagementEnabled;
  }

  public MsgVpn enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the Message VPN. The default value is `false`.
   * @return enabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the Message VPN. The default value is `false`.")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public MsgVpn eventConnectionCountThreshold(EventThreshold eventConnectionCountThreshold) {
    this.eventConnectionCountThreshold = eventConnectionCountThreshold;
    return this;
  }

   /**
   * The threshold for the client connection count event of the Message VPN, relative to `maxConnectionCount`.
   * @return eventConnectionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the client connection count event of the Message VPN, relative to `maxConnectionCount`.")
  public EventThreshold getEventConnectionCountThreshold() {
    return eventConnectionCountThreshold;
  }

  public void setEventConnectionCountThreshold(EventThreshold eventConnectionCountThreshold) {
    this.eventConnectionCountThreshold = eventConnectionCountThreshold;
  }

  public MsgVpn eventEgressFlowCountThreshold(EventThreshold eventEgressFlowCountThreshold) {
    this.eventEgressFlowCountThreshold = eventEgressFlowCountThreshold;
    return this;
  }

   /**
   * The threshold for the egress flow count event of the Message VPN, relative to `maxEgressFlowCount`.
   * @return eventEgressFlowCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the egress flow count event of the Message VPN, relative to `maxEgressFlowCount`.")
  public EventThreshold getEventEgressFlowCountThreshold() {
    return eventEgressFlowCountThreshold;
  }

  public void setEventEgressFlowCountThreshold(EventThreshold eventEgressFlowCountThreshold) {
    this.eventEgressFlowCountThreshold = eventEgressFlowCountThreshold;
  }

  public MsgVpn eventEgressMsgRateThreshold(EventThresholdByValue eventEgressMsgRateThreshold) {
    this.eventEgressMsgRateThreshold = eventEgressMsgRateThreshold;
    return this;
  }

   /**
   * The threshold for the egress message rate event of the Message VPN.
   * @return eventEgressMsgRateThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the egress message rate event of the Message VPN.")
  public EventThresholdByValue getEventEgressMsgRateThreshold() {
    return eventEgressMsgRateThreshold;
  }

  public void setEventEgressMsgRateThreshold(EventThresholdByValue eventEgressMsgRateThreshold) {
    this.eventEgressMsgRateThreshold = eventEgressMsgRateThreshold;
  }

  public MsgVpn eventEndpointCountThreshold(EventThreshold eventEndpointCountThreshold) {
    this.eventEndpointCountThreshold = eventEndpointCountThreshold;
    return this;
  }

   /**
   * The threshold for the endpoint count event of the Message VPN, relative to `maxEndpointCount`.
   * @return eventEndpointCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the endpoint count event of the Message VPN, relative to `maxEndpointCount`.")
  public EventThreshold getEventEndpointCountThreshold() {
    return eventEndpointCountThreshold;
  }

  public void setEventEndpointCountThreshold(EventThreshold eventEndpointCountThreshold) {
    this.eventEndpointCountThreshold = eventEndpointCountThreshold;
  }

  public MsgVpn eventIngressFlowCountThreshold(EventThreshold eventIngressFlowCountThreshold) {
    this.eventIngressFlowCountThreshold = eventIngressFlowCountThreshold;
    return this;
  }

   /**
   * The threshold for the ingress flow count event of the Message VPN, relative to `maxIngressFlowCount`.
   * @return eventIngressFlowCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the ingress flow count event of the Message VPN, relative to `maxIngressFlowCount`.")
  public EventThreshold getEventIngressFlowCountThreshold() {
    return eventIngressFlowCountThreshold;
  }

  public void setEventIngressFlowCountThreshold(EventThreshold eventIngressFlowCountThreshold) {
    this.eventIngressFlowCountThreshold = eventIngressFlowCountThreshold;
  }

  public MsgVpn eventIngressMsgRateThreshold(EventThresholdByValue eventIngressMsgRateThreshold) {
    this.eventIngressMsgRateThreshold = eventIngressMsgRateThreshold;
    return this;
  }

   /**
   * The threshold for the ingress message rate event of the Message VPN.
   * @return eventIngressMsgRateThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the ingress message rate event of the Message VPN.")
  public EventThresholdByValue getEventIngressMsgRateThreshold() {
    return eventIngressMsgRateThreshold;
  }

  public void setEventIngressMsgRateThreshold(EventThresholdByValue eventIngressMsgRateThreshold) {
    this.eventIngressMsgRateThreshold = eventIngressMsgRateThreshold;
  }

  public MsgVpn eventLargeMsgThreshold(Integer eventLargeMsgThreshold) {
    this.eventLargeMsgThreshold = eventLargeMsgThreshold;
    return this;
  }

   /**
   * Size in KB for what is being considered a large message for the Message VPN. The default value is `1024`.
   * @return eventLargeMsgThreshold
  **/
  @ApiModelProperty(example = "null", value = "Size in KB for what is being considered a large message for the Message VPN. The default value is `1024`.")
  public Integer getEventLargeMsgThreshold() {
    return eventLargeMsgThreshold;
  }

  public void setEventLargeMsgThreshold(Integer eventLargeMsgThreshold) {
    this.eventLargeMsgThreshold = eventLargeMsgThreshold;
  }

  public MsgVpn eventLogTag(String eventLogTag) {
    this.eventLogTag = eventLogTag;
    return this;
  }

   /**
   * A prefix applied to all publish events in this Message VPN. The default is to have no `eventLogTag`.
   * @return eventLogTag
  **/
  @ApiModelProperty(example = "null", value = "A prefix applied to all publish events in this Message VPN. The default is to have no `eventLogTag`.")
  public String getEventLogTag() {
    return eventLogTag;
  }

  public void setEventLogTag(String eventLogTag) {
    this.eventLogTag = eventLogTag;
  }

  public MsgVpn eventMsgSpoolUsageThreshold(EventThreshold eventMsgSpoolUsageThreshold) {
    this.eventMsgSpoolUsageThreshold = eventMsgSpoolUsageThreshold;
    return this;
  }

   /**
   * The threshold for the message spool usage event of the Message VPN, relative to `maxMsgSpoolUsage`.
   * @return eventMsgSpoolUsageThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the message spool usage event of the Message VPN, relative to `maxMsgSpoolUsage`.")
  public EventThreshold getEventMsgSpoolUsageThreshold() {
    return eventMsgSpoolUsageThreshold;
  }

  public void setEventMsgSpoolUsageThreshold(EventThreshold eventMsgSpoolUsageThreshold) {
    this.eventMsgSpoolUsageThreshold = eventMsgSpoolUsageThreshold;
  }

  public MsgVpn eventPublishClientEnabled(Boolean eventPublishClientEnabled) {
    this.eventPublishClientEnabled = eventPublishClientEnabled;
    return this;
  }

   /**
   * Enable or disable client level event message publishing. The default value is `false`.
   * @return eventPublishClientEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable client level event message publishing. The default value is `false`.")
  public Boolean getEventPublishClientEnabled() {
    return eventPublishClientEnabled;
  }

  public void setEventPublishClientEnabled(Boolean eventPublishClientEnabled) {
    this.eventPublishClientEnabled = eventPublishClientEnabled;
  }

  public MsgVpn eventPublishMsgVpnEnabled(Boolean eventPublishMsgVpnEnabled) {
    this.eventPublishMsgVpnEnabled = eventPublishMsgVpnEnabled;
    return this;
  }

   /**
   * Enable or disable Message VPN level event message publishing. The default value is `false`.
   * @return eventPublishMsgVpnEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable Message VPN level event message publishing. The default value is `false`.")
  public Boolean getEventPublishMsgVpnEnabled() {
    return eventPublishMsgVpnEnabled;
  }

  public void setEventPublishMsgVpnEnabled(Boolean eventPublishMsgVpnEnabled) {
    this.eventPublishMsgVpnEnabled = eventPublishMsgVpnEnabled;
  }

  public MsgVpn eventPublishSubscriptionMode(EventPublishSubscriptionModeEnum eventPublishSubscriptionMode) {
    this.eventPublishSubscriptionMode = eventPublishSubscriptionMode;
    return this;
  }

   /**
   *  Subscription level event message publishing mode. Format v1 modes use a publish topic of the form:      #LOG/INFO/SUB_ADD/subscribed-topic     #LOG/INFO/SUB_DEL/subscribed-topic  Format v2 modes use a publish topic of the form:      #LOG/INFO/SUB/router-name/ADD/vpn-name/client-name/subscribed-topic     #LOG/INFO/SUB/router-name/DEL/vpn-name/client-name/subscribed-topic  Format v2 is recommended. . The default value is `\"off\"`. The allowed values and their meaning are:      \"off\" - Disable client level event message publishing.     \"on-with-format-v1\" - Enable client level event message publishing with format v1.     \"on-with-no-unsubscribe-events-on-disconnect-format-v1\" - As \"on-with-format-v1\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions.     \"on-with-format-v2\" - Enable client level event message publishing with format v2.     \"on-with-no-unsubscribe-events-on-disconnect-format-v2\" - As \"on-with-format-v2\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. 
   * @return eventPublishSubscriptionMode
  **/
  @ApiModelProperty(example = "null", value = " Subscription level event message publishing mode. Format v1 modes use a publish topic of the form:      #LOG/INFO/SUB_ADD/subscribed-topic     #LOG/INFO/SUB_DEL/subscribed-topic  Format v2 modes use a publish topic of the form:      #LOG/INFO/SUB/router-name/ADD/vpn-name/client-name/subscribed-topic     #LOG/INFO/SUB/router-name/DEL/vpn-name/client-name/subscribed-topic  Format v2 is recommended. . The default value is `\"off\"`. The allowed values and their meaning are:      \"off\" - Disable client level event message publishing.     \"on-with-format-v1\" - Enable client level event message publishing with format v1.     \"on-with-no-unsubscribe-events-on-disconnect-format-v1\" - As \"on-with-format-v1\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions.     \"on-with-format-v2\" - Enable client level event message publishing with format v2.     \"on-with-no-unsubscribe-events-on-disconnect-format-v2\" - As \"on-with-format-v2\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. ")
  public EventPublishSubscriptionModeEnum getEventPublishSubscriptionMode() {
    return eventPublishSubscriptionMode;
  }

  public void setEventPublishSubscriptionMode(EventPublishSubscriptionModeEnum eventPublishSubscriptionMode) {
    this.eventPublishSubscriptionMode = eventPublishSubscriptionMode;
  }

  public MsgVpn eventPublishTopicFormatMqttEnabled(Boolean eventPublishTopicFormatMqttEnabled) {
    this.eventPublishTopicFormatMqttEnabled = eventPublishTopicFormatMqttEnabled;
    return this;
  }

   /**
   * Enable or disable event publish topics in MQTT format. The default value is `false`.
   * @return eventPublishTopicFormatMqttEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable event publish topics in MQTT format. The default value is `false`.")
  public Boolean getEventPublishTopicFormatMqttEnabled() {
    return eventPublishTopicFormatMqttEnabled;
  }

  public void setEventPublishTopicFormatMqttEnabled(Boolean eventPublishTopicFormatMqttEnabled) {
    this.eventPublishTopicFormatMqttEnabled = eventPublishTopicFormatMqttEnabled;
  }

  public MsgVpn eventPublishTopicFormatSmfEnabled(Boolean eventPublishTopicFormatSmfEnabled) {
    this.eventPublishTopicFormatSmfEnabled = eventPublishTopicFormatSmfEnabled;
    return this;
  }

   /**
   * Enable or disable event publish topics in SMF format. The default value is `true`.
   * @return eventPublishTopicFormatSmfEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable event publish topics in SMF format. The default value is `true`.")
  public Boolean getEventPublishTopicFormatSmfEnabled() {
    return eventPublishTopicFormatSmfEnabled;
  }

  public void setEventPublishTopicFormatSmfEnabled(Boolean eventPublishTopicFormatSmfEnabled) {
    this.eventPublishTopicFormatSmfEnabled = eventPublishTopicFormatSmfEnabled;
  }

  public MsgVpn eventServiceRestIncomingConnectionCountThreshold(EventThreshold eventServiceRestIncomingConnectionCountThreshold) {
    this.eventServiceRestIncomingConnectionCountThreshold = eventServiceRestIncomingConnectionCountThreshold;
    return this;
  }

   /**
   * The threshold for the incoming REST client connection count event of the Message VPN, relative to `serviceRestIncomingMaxConnectionCount`.
   * @return eventServiceRestIncomingConnectionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the incoming REST client connection count event of the Message VPN, relative to `serviceRestIncomingMaxConnectionCount`.")
  public EventThreshold getEventServiceRestIncomingConnectionCountThreshold() {
    return eventServiceRestIncomingConnectionCountThreshold;
  }

  public void setEventServiceRestIncomingConnectionCountThreshold(EventThreshold eventServiceRestIncomingConnectionCountThreshold) {
    this.eventServiceRestIncomingConnectionCountThreshold = eventServiceRestIncomingConnectionCountThreshold;
  }

  public MsgVpn eventServiceSmfConnectionCountThreshold(EventThreshold eventServiceSmfConnectionCountThreshold) {
    this.eventServiceSmfConnectionCountThreshold = eventServiceSmfConnectionCountThreshold;
    return this;
  }

   /**
   * The threshold for the SMF client connection count event of the Message VPN, relative to `serviceSmfMaxConnectionCount`.
   * @return eventServiceSmfConnectionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the SMF client connection count event of the Message VPN, relative to `serviceSmfMaxConnectionCount`.")
  public EventThreshold getEventServiceSmfConnectionCountThreshold() {
    return eventServiceSmfConnectionCountThreshold;
  }

  public void setEventServiceSmfConnectionCountThreshold(EventThreshold eventServiceSmfConnectionCountThreshold) {
    this.eventServiceSmfConnectionCountThreshold = eventServiceSmfConnectionCountThreshold;
  }

  public MsgVpn eventServiceWebConnectionCountThreshold(EventThreshold eventServiceWebConnectionCountThreshold) {
    this.eventServiceWebConnectionCountThreshold = eventServiceWebConnectionCountThreshold;
    return this;
  }

   /**
   * The threshold for the web-transport client connection count event of the Message VPN, relative to `serviceWebMaxConnectionCount`.
   * @return eventServiceWebConnectionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the web-transport client connection count event of the Message VPN, relative to `serviceWebMaxConnectionCount`.")
  public EventThreshold getEventServiceWebConnectionCountThreshold() {
    return eventServiceWebConnectionCountThreshold;
  }

  public void setEventServiceWebConnectionCountThreshold(EventThreshold eventServiceWebConnectionCountThreshold) {
    this.eventServiceWebConnectionCountThreshold = eventServiceWebConnectionCountThreshold;
  }

  public MsgVpn eventSubscriptionCountThreshold(EventThreshold eventSubscriptionCountThreshold) {
    this.eventSubscriptionCountThreshold = eventSubscriptionCountThreshold;
    return this;
  }

   /**
   * The threshold for the subscription count event of the Message VPN, relative to `maxSubscriptionCount`.
   * @return eventSubscriptionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the subscription count event of the Message VPN, relative to `maxSubscriptionCount`.")
  public EventThreshold getEventSubscriptionCountThreshold() {
    return eventSubscriptionCountThreshold;
  }

  public void setEventSubscriptionCountThreshold(EventThreshold eventSubscriptionCountThreshold) {
    this.eventSubscriptionCountThreshold = eventSubscriptionCountThreshold;
  }

  public MsgVpn eventTransactedSessionCountThreshold(EventThreshold eventTransactedSessionCountThreshold) {
    this.eventTransactedSessionCountThreshold = eventTransactedSessionCountThreshold;
    return this;
  }

   /**
   * The threshold for the transacted session count event of the Message VPN, relative to `maxTransactedSessionCount`.
   * @return eventTransactedSessionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the transacted session count event of the Message VPN, relative to `maxTransactedSessionCount`.")
  public EventThreshold getEventTransactedSessionCountThreshold() {
    return eventTransactedSessionCountThreshold;
  }

  public void setEventTransactedSessionCountThreshold(EventThreshold eventTransactedSessionCountThreshold) {
    this.eventTransactedSessionCountThreshold = eventTransactedSessionCountThreshold;
  }

  public MsgVpn eventTransactionCountThreshold(EventThreshold eventTransactionCountThreshold) {
    this.eventTransactionCountThreshold = eventTransactionCountThreshold;
    return this;
  }

   /**
   * The threshold for the transaction count event of the Message VPN, relative to `maxTransactionsCount`.
   * @return eventTransactionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the transaction count event of the Message VPN, relative to `maxTransactionsCount`.")
  public EventThreshold getEventTransactionCountThreshold() {
    return eventTransactionCountThreshold;
  }

  public void setEventTransactionCountThreshold(EventThreshold eventTransactionCountThreshold) {
    this.eventTransactionCountThreshold = eventTransactionCountThreshold;
  }

  public MsgVpn exportSubscriptionsEnabled(Boolean exportSubscriptionsEnabled) {
    this.exportSubscriptionsEnabled = exportSubscriptionsEnabled;
    return this;
  }

   /**
   * Enable or disable the export of subscriptions in this Message VPN to other routers in the network over neighbor links. The default value is `false`.
   * @return exportSubscriptionsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the export of subscriptions in this Message VPN to other routers in the network over neighbor links. The default value is `false`.")
  public Boolean getExportSubscriptionsEnabled() {
    return exportSubscriptionsEnabled;
  }

  public void setExportSubscriptionsEnabled(Boolean exportSubscriptionsEnabled) {
    this.exportSubscriptionsEnabled = exportSubscriptionsEnabled;
  }

  public MsgVpn maxConnectionCount(Integer maxConnectionCount) {
    this.maxConnectionCount = maxConnectionCount;
    return this;
  }

   /**
   * The maximum number of client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
   * @return maxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.")
  public Integer getMaxConnectionCount() {
    return maxConnectionCount;
  }

  public void setMaxConnectionCount(Integer maxConnectionCount) {
    this.maxConnectionCount = maxConnectionCount;
  }

  public MsgVpn maxEgressFlowCount(Integer maxEgressFlowCount) {
    this.maxEgressFlowCount = maxEgressFlowCount;
    return this;
  }

   /**
   * The maximum number of egress flows that can be created on this Message VPN. The default value is `16000`.
   * @return maxEgressFlowCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of egress flows that can be created on this Message VPN. The default value is `16000`.")
  public Integer getMaxEgressFlowCount() {
    return maxEgressFlowCount;
  }

  public void setMaxEgressFlowCount(Integer maxEgressFlowCount) {
    this.maxEgressFlowCount = maxEgressFlowCount;
  }

  public MsgVpn maxEndpointCount(Integer maxEndpointCount) {
    this.maxEndpointCount = maxEndpointCount;
    return this;
  }

   /**
   * The maximum number of queues and topic endpoints that can be created on this Message VPN. The default value is `16000`.
   * @return maxEndpointCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of queues and topic endpoints that can be created on this Message VPN. The default value is `16000`.")
  public Integer getMaxEndpointCount() {
    return maxEndpointCount;
  }

  public void setMaxEndpointCount(Integer maxEndpointCount) {
    this.maxEndpointCount = maxEndpointCount;
  }

  public MsgVpn maxIngressFlowCount(Integer maxIngressFlowCount) {
    this.maxIngressFlowCount = maxIngressFlowCount;
    return this;
  }

   /**
   * The maximum number of ingress flows that can be created on this Message VPN. The default value is `16000`.
   * @return maxIngressFlowCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of ingress flows that can be created on this Message VPN. The default value is `16000`.")
  public Integer getMaxIngressFlowCount() {
    return maxIngressFlowCount;
  }

  public void setMaxIngressFlowCount(Integer maxIngressFlowCount) {
    this.maxIngressFlowCount = maxIngressFlowCount;
  }

  public MsgVpn maxMsgSpoolUsage(Integer maxMsgSpoolUsage) {
    this.maxMsgSpoolUsage = maxMsgSpoolUsage;
    return this;
  }

   /**
   * Max spool usage (in MB) allowed for the Message VPN. The default value is `0`.
   * @return maxMsgSpoolUsage
  **/
  @ApiModelProperty(example = "null", value = "Max spool usage (in MB) allowed for the Message VPN. The default value is `0`.")
  public Integer getMaxMsgSpoolUsage() {
    return maxMsgSpoolUsage;
  }

  public void setMaxMsgSpoolUsage(Integer maxMsgSpoolUsage) {
    this.maxMsgSpoolUsage = maxMsgSpoolUsage;
  }

  public MsgVpn maxSubscriptionCount(Integer maxSubscriptionCount) {
    this.maxSubscriptionCount = maxSubscriptionCount;
    return this;
  }

   /**
   * The maximum number of local client subscriptions (both primary and backup) that can be added to this Message VPN. The default value is `5000000`.
   * @return maxSubscriptionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of local client subscriptions (both primary and backup) that can be added to this Message VPN. The default value is `5000000`.")
  public Integer getMaxSubscriptionCount() {
    return maxSubscriptionCount;
  }

  public void setMaxSubscriptionCount(Integer maxSubscriptionCount) {
    this.maxSubscriptionCount = maxSubscriptionCount;
  }

  public MsgVpn maxTransactedSessionCount(Integer maxTransactedSessionCount) {
    this.maxTransactedSessionCount = maxTransactedSessionCount;
    return this;
  }

   /**
   * The maximum number of transacted sessions for this Message VPN. The default value is `16000`.
   * @return maxTransactedSessionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of transacted sessions for this Message VPN. The default value is `16000`.")
  public Integer getMaxTransactedSessionCount() {
    return maxTransactedSessionCount;
  }

  public void setMaxTransactedSessionCount(Integer maxTransactedSessionCount) {
    this.maxTransactedSessionCount = maxTransactedSessionCount;
  }

  public MsgVpn maxTransactionCount(Integer maxTransactionCount) {
    this.maxTransactionCount = maxTransactionCount;
    return this;
  }

   /**
   * The maximum number of transactions for this Message VPN. The default value is `50000`.
   * @return maxTransactionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of transactions for this Message VPN. The default value is `50000`.")
  public Integer getMaxTransactionCount() {
    return maxTransactionCount;
  }

  public void setMaxTransactionCount(Integer maxTransactionCount) {
    this.maxTransactionCount = maxTransactionCount;
  }

  public MsgVpn msgVpnName(String msgVpnName) {
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

  public MsgVpn replicationAckPropagationIntervalMsgCount(Integer replicationAckPropagationIntervalMsgCount) {
    this.replicationAckPropagationIntervalMsgCount = replicationAckPropagationIntervalMsgCount;
    return this;
  }

   /**
   * The ack-propagation interval, in number of replicated messages. The default value is `20`.
   * @return replicationAckPropagationIntervalMsgCount
  **/
  @ApiModelProperty(example = "null", value = "The ack-propagation interval, in number of replicated messages. The default value is `20`.")
  public Integer getReplicationAckPropagationIntervalMsgCount() {
    return replicationAckPropagationIntervalMsgCount;
  }

  public void setReplicationAckPropagationIntervalMsgCount(Integer replicationAckPropagationIntervalMsgCount) {
    this.replicationAckPropagationIntervalMsgCount = replicationAckPropagationIntervalMsgCount;
  }

  public MsgVpn replicationBridgeAuthenticationBasicClientUsername(String replicationBridgeAuthenticationBasicClientUsername) {
    this.replicationBridgeAuthenticationBasicClientUsername = replicationBridgeAuthenticationBasicClientUsername;
    return this;
  }

   /**
   * The client username the replication bridge uses to login to the Remote Message VPN on the replication mate. The default is to have no `replicationBridgeAuthenticationBasicClientUsername`.
   * @return replicationBridgeAuthenticationBasicClientUsername
  **/
  @ApiModelProperty(example = "null", value = "The client username the replication bridge uses to login to the Remote Message VPN on the replication mate. The default is to have no `replicationBridgeAuthenticationBasicClientUsername`.")
  public String getReplicationBridgeAuthenticationBasicClientUsername() {
    return replicationBridgeAuthenticationBasicClientUsername;
  }

  public void setReplicationBridgeAuthenticationBasicClientUsername(String replicationBridgeAuthenticationBasicClientUsername) {
    this.replicationBridgeAuthenticationBasicClientUsername = replicationBridgeAuthenticationBasicClientUsername;
  }

  public MsgVpn replicationBridgeAuthenticationBasicPassword(String replicationBridgeAuthenticationBasicPassword) {
    this.replicationBridgeAuthenticationBasicPassword = replicationBridgeAuthenticationBasicPassword;
    return this;
  }

   /**
   * The password that the bridge uses to login to the Remote Message VPN. The default is to have no `replicationBridgeAuthenticationBasicPassword`.
   * @return replicationBridgeAuthenticationBasicPassword
  **/
  @ApiModelProperty(example = "null", value = "The password that the bridge uses to login to the Remote Message VPN. The default is to have no `replicationBridgeAuthenticationBasicPassword`.")
  public String getReplicationBridgeAuthenticationBasicPassword() {
    return replicationBridgeAuthenticationBasicPassword;
  }

  public void setReplicationBridgeAuthenticationBasicPassword(String replicationBridgeAuthenticationBasicPassword) {
    this.replicationBridgeAuthenticationBasicPassword = replicationBridgeAuthenticationBasicPassword;
  }

  public MsgVpn replicationBridgeAuthenticationScheme(ReplicationBridgeAuthenticationSchemeEnum replicationBridgeAuthenticationScheme) {
    this.replicationBridgeAuthenticationScheme = replicationBridgeAuthenticationScheme;
    return this;
  }

   /**
   * The authentication scheme for the replication bridge. The default value is `\"basic\"`. The allowed values and their meaning are:      \"basic\" - Basic Authentication Scheme (via username and password).     \"client-certificate\" - Client Certificate Authentication Scheme (via certificate-file). 
   * @return replicationBridgeAuthenticationScheme
  **/
  @ApiModelProperty(example = "null", value = "The authentication scheme for the replication bridge. The default value is `\"basic\"`. The allowed values and their meaning are:      \"basic\" - Basic Authentication Scheme (via username and password).     \"client-certificate\" - Client Certificate Authentication Scheme (via certificate-file). ")
  public ReplicationBridgeAuthenticationSchemeEnum getReplicationBridgeAuthenticationScheme() {
    return replicationBridgeAuthenticationScheme;
  }

  public void setReplicationBridgeAuthenticationScheme(ReplicationBridgeAuthenticationSchemeEnum replicationBridgeAuthenticationScheme) {
    this.replicationBridgeAuthenticationScheme = replicationBridgeAuthenticationScheme;
  }

  public MsgVpn replicationBridgeCompressedDataEnabled(Boolean replicationBridgeCompressedDataEnabled) {
    this.replicationBridgeCompressedDataEnabled = replicationBridgeCompressedDataEnabled;
    return this;
  }

   /**
   * Whether compression is used for the bridge. The default value is `false`.
   * @return replicationBridgeCompressedDataEnabled
  **/
  @ApiModelProperty(example = "null", value = "Whether compression is used for the bridge. The default value is `false`.")
  public Boolean getReplicationBridgeCompressedDataEnabled() {
    return replicationBridgeCompressedDataEnabled;
  }

  public void setReplicationBridgeCompressedDataEnabled(Boolean replicationBridgeCompressedDataEnabled) {
    this.replicationBridgeCompressedDataEnabled = replicationBridgeCompressedDataEnabled;
  }

  public MsgVpn replicationBridgeEgressFlowWindowSize(Integer replicationBridgeEgressFlowWindowSize) {
    this.replicationBridgeEgressFlowWindowSize = replicationBridgeEgressFlowWindowSize;
    return this;
  }

   /**
   * The window size of outstanding guaranteed messages. The default value is `255`.
   * @return replicationBridgeEgressFlowWindowSize
  **/
  @ApiModelProperty(example = "null", value = "The window size of outstanding guaranteed messages. The default value is `255`.")
  public Integer getReplicationBridgeEgressFlowWindowSize() {
    return replicationBridgeEgressFlowWindowSize;
  }

  public void setReplicationBridgeEgressFlowWindowSize(Integer replicationBridgeEgressFlowWindowSize) {
    this.replicationBridgeEgressFlowWindowSize = replicationBridgeEgressFlowWindowSize;
  }

  public MsgVpn replicationBridgeRetryDelay(Integer replicationBridgeRetryDelay) {
    this.replicationBridgeRetryDelay = replicationBridgeRetryDelay;
    return this;
  }

   /**
   * Number of seconds that must pass before retrying a connection. The default value is `3`.
   * @return replicationBridgeRetryDelay
  **/
  @ApiModelProperty(example = "null", value = "Number of seconds that must pass before retrying a connection. The default value is `3`.")
  public Integer getReplicationBridgeRetryDelay() {
    return replicationBridgeRetryDelay;
  }

  public void setReplicationBridgeRetryDelay(Integer replicationBridgeRetryDelay) {
    this.replicationBridgeRetryDelay = replicationBridgeRetryDelay;
  }

  public MsgVpn replicationBridgeTlsEnabled(Boolean replicationBridgeTlsEnabled) {
    this.replicationBridgeTlsEnabled = replicationBridgeTlsEnabled;
    return this;
  }

   /**
   * Enable or disable use of TLS for the bridge connection. The default value is `false`.
   * @return replicationBridgeTlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable use of TLS for the bridge connection. The default value is `false`.")
  public Boolean getReplicationBridgeTlsEnabled() {
    return replicationBridgeTlsEnabled;
  }

  public void setReplicationBridgeTlsEnabled(Boolean replicationBridgeTlsEnabled) {
    this.replicationBridgeTlsEnabled = replicationBridgeTlsEnabled;
  }

  public MsgVpn replicationBridgeUnidirectionalClientProfileName(String replicationBridgeUnidirectionalClientProfileName) {
    this.replicationBridgeUnidirectionalClientProfileName = replicationBridgeUnidirectionalClientProfileName;
    return this;
  }

   /**
   * The client-profile for the unidirectional replication bridge for the Message VPN. The client-profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is `\"#client-profile\"`.
   * @return replicationBridgeUnidirectionalClientProfileName
  **/
  @ApiModelProperty(example = "null", value = "The client-profile for the unidirectional replication bridge for the Message VPN. The client-profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is `\"#client-profile\"`.")
  public String getReplicationBridgeUnidirectionalClientProfileName() {
    return replicationBridgeUnidirectionalClientProfileName;
  }

  public void setReplicationBridgeUnidirectionalClientProfileName(String replicationBridgeUnidirectionalClientProfileName) {
    this.replicationBridgeUnidirectionalClientProfileName = replicationBridgeUnidirectionalClientProfileName;
  }

  public MsgVpn replicationEnabled(Boolean replicationEnabled) {
    this.replicationEnabled = replicationEnabled;
    return this;
  }

   /**
   * Enable or disable the replication feature for the Message VPN. The default value is `false`.
   * @return replicationEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the replication feature for the Message VPN. The default value is `false`.")
  public Boolean getReplicationEnabled() {
    return replicationEnabled;
  }

  public void setReplicationEnabled(Boolean replicationEnabled) {
    this.replicationEnabled = replicationEnabled;
  }

  public MsgVpn replicationEnabledQueueBehavior(ReplicationEnabledQueueBehaviorEnum replicationEnabledQueueBehavior) {
    this.replicationEnabledQueueBehavior = replicationEnabledQueueBehavior;
    return this;
  }

   /**
   * The behavior to take when enabling replication, depending on the existance of the replication queue. This only has meaning in a request which enables replication. The default value is `\"fail-on-existing-queue\"`. The allowed values and their meaning are:      \"fail-on-existing-queue\" - The data replication queue must not already exist.     \"force-use-existing-queue\" - The data replication queue must already exist. Any data messages on the queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail.     \"force-recreate-queue\" - The data replication queue must already exist. Any data messages on the queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. 
   * @return replicationEnabledQueueBehavior
  **/
  @ApiModelProperty(example = "null", value = "The behavior to take when enabling replication, depending on the existance of the replication queue. This only has meaning in a request which enables replication. The default value is `\"fail-on-existing-queue\"`. The allowed values and their meaning are:      \"fail-on-existing-queue\" - The data replication queue must not already exist.     \"force-use-existing-queue\" - The data replication queue must already exist. Any data messages on the queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail.     \"force-recreate-queue\" - The data replication queue must already exist. Any data messages on the queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. ")
  public ReplicationEnabledQueueBehaviorEnum getReplicationEnabledQueueBehavior() {
    return replicationEnabledQueueBehavior;
  }

  public void setReplicationEnabledQueueBehavior(ReplicationEnabledQueueBehaviorEnum replicationEnabledQueueBehavior) {
    this.replicationEnabledQueueBehavior = replicationEnabledQueueBehavior;
  }

  public MsgVpn replicationQueueMaxMsgSpoolUsage(Integer replicationQueueMaxMsgSpoolUsage) {
    this.replicationQueueMaxMsgSpoolUsage = replicationQueueMaxMsgSpoolUsage;
    return this;
  }

   /**
   * The max spool usage (in MB) of the replication queue. The default value is `60000`.
   * @return replicationQueueMaxMsgSpoolUsage
  **/
  @ApiModelProperty(example = "null", value = "The max spool usage (in MB) of the replication queue. The default value is `60000`.")
  public Integer getReplicationQueueMaxMsgSpoolUsage() {
    return replicationQueueMaxMsgSpoolUsage;
  }

  public void setReplicationQueueMaxMsgSpoolUsage(Integer replicationQueueMaxMsgSpoolUsage) {
    this.replicationQueueMaxMsgSpoolUsage = replicationQueueMaxMsgSpoolUsage;
  }

  public MsgVpn replicationQueueRejectMsgToSenderOnDiscardEnabled(Boolean replicationQueueRejectMsgToSenderOnDiscardEnabled) {
    this.replicationQueueRejectMsgToSenderOnDiscardEnabled = replicationQueueRejectMsgToSenderOnDiscardEnabled;
    return this;
  }

   /**
   * The message discard behavior. The default value is `true`.
   * @return replicationQueueRejectMsgToSenderOnDiscardEnabled
  **/
  @ApiModelProperty(example = "null", value = "The message discard behavior. The default value is `true`.")
  public Boolean getReplicationQueueRejectMsgToSenderOnDiscardEnabled() {
    return replicationQueueRejectMsgToSenderOnDiscardEnabled;
  }

  public void setReplicationQueueRejectMsgToSenderOnDiscardEnabled(Boolean replicationQueueRejectMsgToSenderOnDiscardEnabled) {
    this.replicationQueueRejectMsgToSenderOnDiscardEnabled = replicationQueueRejectMsgToSenderOnDiscardEnabled;
  }

  public MsgVpn replicationRejectMsgWhenSyncIneligibleEnabled(Boolean replicationRejectMsgWhenSyncIneligibleEnabled) {
    this.replicationRejectMsgWhenSyncIneligibleEnabled = replicationRejectMsgWhenSyncIneligibleEnabled;
    return this;
  }

   /**
   * Enable or disable sync mode ineligible behavior. If enabled and sync replication becomes ineligible, guaranteed messages published to sync replicated topics will be rejected to the sender. If disabled, sync replication will revert to async mode. The default value is `false`.
   * @return replicationRejectMsgWhenSyncIneligibleEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable sync mode ineligible behavior. If enabled and sync replication becomes ineligible, guaranteed messages published to sync replicated topics will be rejected to the sender. If disabled, sync replication will revert to async mode. The default value is `false`.")
  public Boolean getReplicationRejectMsgWhenSyncIneligibleEnabled() {
    return replicationRejectMsgWhenSyncIneligibleEnabled;
  }

  public void setReplicationRejectMsgWhenSyncIneligibleEnabled(Boolean replicationRejectMsgWhenSyncIneligibleEnabled) {
    this.replicationRejectMsgWhenSyncIneligibleEnabled = replicationRejectMsgWhenSyncIneligibleEnabled;
  }

  public MsgVpn replicationRole(ReplicationRoleEnum replicationRole) {
    this.replicationRole = replicationRole;
    return this;
  }

   /**
   * The replication role for this Message VPN. The default value is `\"standby\"`. The allowed values and their meaning are:      \"active\" - Assume the active role in replication for the Message VPN.     \"standby\" - Assume the standby role in replication for the Message VPN. 
   * @return replicationRole
  **/
  @ApiModelProperty(example = "null", value = "The replication role for this Message VPN. The default value is `\"standby\"`. The allowed values and their meaning are:      \"active\" - Assume the active role in replication for the Message VPN.     \"standby\" - Assume the standby role in replication for the Message VPN. ")
  public ReplicationRoleEnum getReplicationRole() {
    return replicationRole;
  }

  public void setReplicationRole(ReplicationRoleEnum replicationRole) {
    this.replicationRole = replicationRole;
  }

  public MsgVpn replicationTransactionMode(ReplicationTransactionModeEnum replicationTransactionMode) {
    this.replicationTransactionMode = replicationTransactionMode;
    return this;
  }

   /**
   * The transaction replication mode for all transactions within a Message VPN. When mode is async, all transactions originated by clients are replicated to the standby site using async-replication. When mode is sync, all transactions originated by clients are replicated to the standby site using sync-replication. Changing this value during operation will not affect existing transactions. It is only validated upon starting a transaction. The default value is `\"async\"`. The allowed values and their meaning are:      \"sync\" - Synchronous replication-mode.     \"async\" - Asynchronous replication-mode. 
   * @return replicationTransactionMode
  **/
  @ApiModelProperty(example = "null", value = "The transaction replication mode for all transactions within a Message VPN. When mode is async, all transactions originated by clients are replicated to the standby site using async-replication. When mode is sync, all transactions originated by clients are replicated to the standby site using sync-replication. Changing this value during operation will not affect existing transactions. It is only validated upon starting a transaction. The default value is `\"async\"`. The allowed values and their meaning are:      \"sync\" - Synchronous replication-mode.     \"async\" - Asynchronous replication-mode. ")
  public ReplicationTransactionModeEnum getReplicationTransactionMode() {
    return replicationTransactionMode;
  }

  public void setReplicationTransactionMode(ReplicationTransactionModeEnum replicationTransactionMode) {
    this.replicationTransactionMode = replicationTransactionMode;
  }

  public MsgVpn restTlsServerCertEnforceTrustedCommonNameEnabled(Boolean restTlsServerCertEnforceTrustedCommonNameEnabled) {
    this.restTlsServerCertEnforceTrustedCommonNameEnabled = restTlsServerCertEnforceTrustedCommonNameEnabled;
    return this;
  }

   /**
   * Enable or disable whether or not the trusted-common-name attribute of a REST Consumer is enforced or not. Each REST Consumer has a list of common-names which it expects to be returned in the server-certificate from the remote REST Consumer. If enforce-trusted-common-name is enabled, but the list of common-names has not been configured, the REST Consumer will not be allowed to be brought into service. An appropriate error message is provided in the REST Consumer operational display. The default value is `true`.
   * @return restTlsServerCertEnforceTrustedCommonNameEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable whether or not the trusted-common-name attribute of a REST Consumer is enforced or not. Each REST Consumer has a list of common-names which it expects to be returned in the server-certificate from the remote REST Consumer. If enforce-trusted-common-name is enabled, but the list of common-names has not been configured, the REST Consumer will not be allowed to be brought into service. An appropriate error message is provided in the REST Consumer operational display. The default value is `true`.")
  public Boolean getRestTlsServerCertEnforceTrustedCommonNameEnabled() {
    return restTlsServerCertEnforceTrustedCommonNameEnabled;
  }

  public void setRestTlsServerCertEnforceTrustedCommonNameEnabled(Boolean restTlsServerCertEnforceTrustedCommonNameEnabled) {
    this.restTlsServerCertEnforceTrustedCommonNameEnabled = restTlsServerCertEnforceTrustedCommonNameEnabled;
  }

  public MsgVpn restTlsServerCertMaxChainDepth(Integer restTlsServerCertMaxChainDepth) {
    this.restTlsServerCertMaxChainDepth = restTlsServerCertMaxChainDepth;
    return this;
  }

   /**
   * The maximum depth for the certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.
   * @return restTlsServerCertMaxChainDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth for the certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.")
  public Integer getRestTlsServerCertMaxChainDepth() {
    return restTlsServerCertMaxChainDepth;
  }

  public void setRestTlsServerCertMaxChainDepth(Integer restTlsServerCertMaxChainDepth) {
    this.restTlsServerCertMaxChainDepth = restTlsServerCertMaxChainDepth;
  }

  public MsgVpn restTlsServerCertValidateDateEnabled(Boolean restTlsServerCertValidateDateEnabled) {
    this.restTlsServerCertValidateDateEnabled = restTlsServerCertValidateDateEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.
   * @return restTlsServerCertValidateDateEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.")
  public Boolean getRestTlsServerCertValidateDateEnabled() {
    return restTlsServerCertValidateDateEnabled;
  }

  public void setRestTlsServerCertValidateDateEnabled(Boolean restTlsServerCertValidateDateEnabled) {
    this.restTlsServerCertValidateDateEnabled = restTlsServerCertValidateDateEnabled;
  }

  public MsgVpn sempOverMsgBusAdminClientEnabled(Boolean sempOverMsgBusAdminClientEnabled) {
    this.sempOverMsgBusAdminClientEnabled = sempOverMsgBusAdminClientEnabled;
    return this;
  }

   /**
   * Enable or disable \"admin client\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.
   * @return sempOverMsgBusAdminClientEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable \"admin client\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.")
  public Boolean getSempOverMsgBusAdminClientEnabled() {
    return sempOverMsgBusAdminClientEnabled;
  }

  public void setSempOverMsgBusAdminClientEnabled(Boolean sempOverMsgBusAdminClientEnabled) {
    this.sempOverMsgBusAdminClientEnabled = sempOverMsgBusAdminClientEnabled;
  }

  public MsgVpn sempOverMsgBusAdminDistributedCacheEnabled(Boolean sempOverMsgBusAdminDistributedCacheEnabled) {
    this.sempOverMsgBusAdminDistributedCacheEnabled = sempOverMsgBusAdminDistributedCacheEnabled;
    return this;
  }

   /**
   * Enable or disable \"admin distributed-cache\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.
   * @return sempOverMsgBusAdminDistributedCacheEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable \"admin distributed-cache\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.")
  public Boolean getSempOverMsgBusAdminDistributedCacheEnabled() {
    return sempOverMsgBusAdminDistributedCacheEnabled;
  }

  public void setSempOverMsgBusAdminDistributedCacheEnabled(Boolean sempOverMsgBusAdminDistributedCacheEnabled) {
    this.sempOverMsgBusAdminDistributedCacheEnabled = sempOverMsgBusAdminDistributedCacheEnabled;
  }

  public MsgVpn sempOverMsgBusAdminEnabled(Boolean sempOverMsgBusAdminEnabled) {
    this.sempOverMsgBusAdminEnabled = sempOverMsgBusAdminEnabled;
    return this;
  }

   /**
   * Enable or disable \"admin\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.
   * @return sempOverMsgBusAdminEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable \"admin\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.")
  public Boolean getSempOverMsgBusAdminEnabled() {
    return sempOverMsgBusAdminEnabled;
  }

  public void setSempOverMsgBusAdminEnabled(Boolean sempOverMsgBusAdminEnabled) {
    this.sempOverMsgBusAdminEnabled = sempOverMsgBusAdminEnabled;
  }

  public MsgVpn sempOverMsgBusEnabled(Boolean sempOverMsgBusEnabled) {
    this.sempOverMsgBusEnabled = sempOverMsgBusEnabled;
    return this;
  }

   /**
   * Enable or disable SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `true`.
   * @return sempOverMsgBusEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `true`.")
  public Boolean getSempOverMsgBusEnabled() {
    return sempOverMsgBusEnabled;
  }

  public void setSempOverMsgBusEnabled(Boolean sempOverMsgBusEnabled) {
    this.sempOverMsgBusEnabled = sempOverMsgBusEnabled;
  }

  public MsgVpn sempOverMsgBusLegacyShowClearEnabled(Boolean sempOverMsgBusLegacyShowClearEnabled) {
    this.sempOverMsgBusLegacyShowClearEnabled = sempOverMsgBusLegacyShowClearEnabled;
    return this;
  }

   /**
   * Enable or disable \"legacy-show-clear\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `true`.
   * @return sempOverMsgBusLegacyShowClearEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable \"legacy-show-clear\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `true`.")
  public Boolean getSempOverMsgBusLegacyShowClearEnabled() {
    return sempOverMsgBusLegacyShowClearEnabled;
  }

  public void setSempOverMsgBusLegacyShowClearEnabled(Boolean sempOverMsgBusLegacyShowClearEnabled) {
    this.sempOverMsgBusLegacyShowClearEnabled = sempOverMsgBusLegacyShowClearEnabled;
  }

  public MsgVpn sempOverMsgBusShowEnabled(Boolean sempOverMsgBusShowEnabled) {
    this.sempOverMsgBusShowEnabled = sempOverMsgBusShowEnabled;
    return this;
  }

   /**
   * Enable or disable \"show\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.
   * @return sempOverMsgBusShowEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable \"show\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.")
  public Boolean getSempOverMsgBusShowEnabled() {
    return sempOverMsgBusShowEnabled;
  }

  public void setSempOverMsgBusShowEnabled(Boolean sempOverMsgBusShowEnabled) {
    this.sempOverMsgBusShowEnabled = sempOverMsgBusShowEnabled;
  }

  public MsgVpn serviceRestIncomingMaxConnectionCount(Integer serviceRestIncomingMaxConnectionCount) {
    this.serviceRestIncomingMaxConnectionCount = serviceRestIncomingMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of REST incoming client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
   * @return serviceRestIncomingMaxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of REST incoming client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.")
  public Integer getServiceRestIncomingMaxConnectionCount() {
    return serviceRestIncomingMaxConnectionCount;
  }

  public void setServiceRestIncomingMaxConnectionCount(Integer serviceRestIncomingMaxConnectionCount) {
    this.serviceRestIncomingMaxConnectionCount = serviceRestIncomingMaxConnectionCount;
  }

  public MsgVpn serviceRestIncomingPlainTextEnabled(Boolean serviceRestIncomingPlainTextEnabled) {
    this.serviceRestIncomingPlainTextEnabled = serviceRestIncomingPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable plain-text REST for this Message VPN. The default value is `false`.
   * @return serviceRestIncomingPlainTextEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable plain-text REST for this Message VPN. The default value is `false`.")
  public Boolean getServiceRestIncomingPlainTextEnabled() {
    return serviceRestIncomingPlainTextEnabled;
  }

  public void setServiceRestIncomingPlainTextEnabled(Boolean serviceRestIncomingPlainTextEnabled) {
    this.serviceRestIncomingPlainTextEnabled = serviceRestIncomingPlainTextEnabled;
  }

  public MsgVpn serviceRestIncomingPlainTextListenPort(Integer serviceRestIncomingPlainTextListenPort) {
    this.serviceRestIncomingPlainTextListenPort = serviceRestIncomingPlainTextListenPort;
    return this;
  }

   /**
   * The TCP port on the NAB for incoming plain-text REST client connections for the Message VPN. The TCP port must not be in use by another service in any Message VPN in the msg-backbone VRF. Enabling plain-text REST is not allowed without a listen-port. The default is to have no `serviceRestIncomingPlainTextListenPort`.
   * @return serviceRestIncomingPlainTextListenPort
  **/
  @ApiModelProperty(example = "null", value = "The TCP port on the NAB for incoming plain-text REST client connections for the Message VPN. The TCP port must not be in use by another service in any Message VPN in the msg-backbone VRF. Enabling plain-text REST is not allowed without a listen-port. The default is to have no `serviceRestIncomingPlainTextListenPort`.")
  public Integer getServiceRestIncomingPlainTextListenPort() {
    return serviceRestIncomingPlainTextListenPort;
  }

  public void setServiceRestIncomingPlainTextListenPort(Integer serviceRestIncomingPlainTextListenPort) {
    this.serviceRestIncomingPlainTextListenPort = serviceRestIncomingPlainTextListenPort;
  }

  public MsgVpn serviceRestIncomingTlsEnabled(Boolean serviceRestIncomingTlsEnabled) {
    this.serviceRestIncomingTlsEnabled = serviceRestIncomingTlsEnabled;
    return this;
  }

   /**
   * Enable or disable incoming TLS REST service for this Message VPN. The default value is `false`.
   * @return serviceRestIncomingTlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable incoming TLS REST service for this Message VPN. The default value is `false`.")
  public Boolean getServiceRestIncomingTlsEnabled() {
    return serviceRestIncomingTlsEnabled;
  }

  public void setServiceRestIncomingTlsEnabled(Boolean serviceRestIncomingTlsEnabled) {
    this.serviceRestIncomingTlsEnabled = serviceRestIncomingTlsEnabled;
  }

  public MsgVpn serviceRestIncomingTlsListenPort(Integer serviceRestIncomingTlsListenPort) {
    this.serviceRestIncomingTlsListenPort = serviceRestIncomingTlsListenPort;
    return this;
  }

   /**
   * The TCP port on the NAB for incoming TLS REST client connections for the Message VPN. The TCP port must not be in use by another service in any Message VPN in the msg-backbone VRF. Enabling plain-text REST is not allowed without a listen-port. The default is to have no `serviceRestIncomingTlsListenPort`.
   * @return serviceRestIncomingTlsListenPort
  **/
  @ApiModelProperty(example = "null", value = "The TCP port on the NAB for incoming TLS REST client connections for the Message VPN. The TCP port must not be in use by another service in any Message VPN in the msg-backbone VRF. Enabling plain-text REST is not allowed without a listen-port. The default is to have no `serviceRestIncomingTlsListenPort`.")
  public Integer getServiceRestIncomingTlsListenPort() {
    return serviceRestIncomingTlsListenPort;
  }

  public void setServiceRestIncomingTlsListenPort(Integer serviceRestIncomingTlsListenPort) {
    this.serviceRestIncomingTlsListenPort = serviceRestIncomingTlsListenPort;
  }

  public MsgVpn serviceRestOutgoingMaxConnectionCount(Integer serviceRestOutgoingMaxConnectionCount) {
    this.serviceRestOutgoingMaxConnectionCount = serviceRestOutgoingMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of REST consumer connections that can be simultaneously established from the Message VPN. The default value is `6000`.
   * @return serviceRestOutgoingMaxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of REST consumer connections that can be simultaneously established from the Message VPN. The default value is `6000`.")
  public Integer getServiceRestOutgoingMaxConnectionCount() {
    return serviceRestOutgoingMaxConnectionCount;
  }

  public void setServiceRestOutgoingMaxConnectionCount(Integer serviceRestOutgoingMaxConnectionCount) {
    this.serviceRestOutgoingMaxConnectionCount = serviceRestOutgoingMaxConnectionCount;
  }

  public MsgVpn serviceSmfMaxConnectionCount(Integer serviceSmfMaxConnectionCount) {
    this.serviceSmfMaxConnectionCount = serviceSmfMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of SMF client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
   * @return serviceSmfMaxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of SMF client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.")
  public Integer getServiceSmfMaxConnectionCount() {
    return serviceSmfMaxConnectionCount;
  }

  public void setServiceSmfMaxConnectionCount(Integer serviceSmfMaxConnectionCount) {
    this.serviceSmfMaxConnectionCount = serviceSmfMaxConnectionCount;
  }

  public MsgVpn serviceSmfPlainTextEnabled(Boolean serviceSmfPlainTextEnabled) {
    this.serviceSmfPlainTextEnabled = serviceSmfPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable plain-text SMF service in the Message VPN. The default value is `true`.
   * @return serviceSmfPlainTextEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable plain-text SMF service in the Message VPN. The default value is `true`.")
  public Boolean getServiceSmfPlainTextEnabled() {
    return serviceSmfPlainTextEnabled;
  }

  public void setServiceSmfPlainTextEnabled(Boolean serviceSmfPlainTextEnabled) {
    this.serviceSmfPlainTextEnabled = serviceSmfPlainTextEnabled;
  }

  public MsgVpn serviceSmfTlsEnabled(Boolean serviceSmfTlsEnabled) {
    this.serviceSmfTlsEnabled = serviceSmfTlsEnabled;
    return this;
  }

   /**
   * Enable or disable TLS SMF service for this Message VPN. The default value is `true`.
   * @return serviceSmfTlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable TLS SMF service for this Message VPN. The default value is `true`.")
  public Boolean getServiceSmfTlsEnabled() {
    return serviceSmfTlsEnabled;
  }

  public void setServiceSmfTlsEnabled(Boolean serviceSmfTlsEnabled) {
    this.serviceSmfTlsEnabled = serviceSmfTlsEnabled;
  }

  public MsgVpn serviceWebMaxConnectionCount(Integer serviceWebMaxConnectionCount) {
    this.serviceWebMaxConnectionCount = serviceWebMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of web-transport client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
   * @return serviceWebMaxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of web-transport client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.")
  public Integer getServiceWebMaxConnectionCount() {
    return serviceWebMaxConnectionCount;
  }

  public void setServiceWebMaxConnectionCount(Integer serviceWebMaxConnectionCount) {
    this.serviceWebMaxConnectionCount = serviceWebMaxConnectionCount;
  }

  public MsgVpn serviceWebPlainTextEnabled(Boolean serviceWebPlainTextEnabled) {
    this.serviceWebPlainTextEnabled = serviceWebPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable plain-text Web Transport service in the Message VPN. The default value is `true`.
   * @return serviceWebPlainTextEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable plain-text Web Transport service in the Message VPN. The default value is `true`.")
  public Boolean getServiceWebPlainTextEnabled() {
    return serviceWebPlainTextEnabled;
  }

  public void setServiceWebPlainTextEnabled(Boolean serviceWebPlainTextEnabled) {
    this.serviceWebPlainTextEnabled = serviceWebPlainTextEnabled;
  }

  public MsgVpn serviceWebTlsEnabled(Boolean serviceWebTlsEnabled) {
    this.serviceWebTlsEnabled = serviceWebTlsEnabled;
    return this;
  }

   /**
   * Enable or disable TLS Web Transport service in the Message VPN. The default value is `true`.
   * @return serviceWebTlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable TLS Web Transport service in the Message VPN. The default value is `true`.")
  public Boolean getServiceWebTlsEnabled() {
    return serviceWebTlsEnabled;
  }

  public void setServiceWebTlsEnabled(Boolean serviceWebTlsEnabled) {
    this.serviceWebTlsEnabled = serviceWebTlsEnabled;
  }

  public MsgVpn tlsAllowDowngradeToPlainTextEnabled(Boolean tlsAllowDowngradeToPlainTextEnabled) {
    this.tlsAllowDowngradeToPlainTextEnabled = tlsAllowDowngradeToPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the allowing of TLS SMF clients to downgrade their connections to plain-text connections. Changing this will not affect existing connections. The default value is `false`.
   * @return tlsAllowDowngradeToPlainTextEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the allowing of TLS SMF clients to downgrade their connections to plain-text connections. Changing this will not affect existing connections. The default value is `false`.")
  public Boolean getTlsAllowDowngradeToPlainTextEnabled() {
    return tlsAllowDowngradeToPlainTextEnabled;
  }

  public void setTlsAllowDowngradeToPlainTextEnabled(Boolean tlsAllowDowngradeToPlainTextEnabled) {
    this.tlsAllowDowngradeToPlainTextEnabled = tlsAllowDowngradeToPlainTextEnabled;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpn msgVpn = (MsgVpn) o;
    return Objects.equals(this.authenticationBasicEnabled, msgVpn.authenticationBasicEnabled) &&
        Objects.equals(this.authenticationBasicProfileName, msgVpn.authenticationBasicProfileName) &&
        Objects.equals(this.authenticationBasicRadiusDomain, msgVpn.authenticationBasicRadiusDomain) &&
        Objects.equals(this.authenticationBasicType, msgVpn.authenticationBasicType) &&
        Objects.equals(this.authenticationClientCertAllowApiProvidedUsernameEnabled, msgVpn.authenticationClientCertAllowApiProvidedUsernameEnabled) &&
        Objects.equals(this.authenticationClientCertEnabled, msgVpn.authenticationClientCertEnabled) &&
        Objects.equals(this.authenticationClientCertMaxChainDepth, msgVpn.authenticationClientCertMaxChainDepth) &&
        Objects.equals(this.authenticationClientCertValidateDateEnabled, msgVpn.authenticationClientCertValidateDateEnabled) &&
        Objects.equals(this.authenticationKerberosAllowApiProvidedUsernameEnabled, msgVpn.authenticationKerberosAllowApiProvidedUsernameEnabled) &&
        Objects.equals(this.authenticationKerberosEnabled, msgVpn.authenticationKerberosEnabled) &&
        Objects.equals(this.authorizationLdapGroupMembershipAttributeName, msgVpn.authorizationLdapGroupMembershipAttributeName) &&
        Objects.equals(this.authorizationProfileName, msgVpn.authorizationProfileName) &&
        Objects.equals(this.authorizationType, msgVpn.authorizationType) &&
        Objects.equals(this.bridgingTlsServerCertEnforceTrustedCommonNameEnabled, msgVpn.bridgingTlsServerCertEnforceTrustedCommonNameEnabled) &&
        Objects.equals(this.bridgingTlsServerCertMaxChainDepth, msgVpn.bridgingTlsServerCertMaxChainDepth) &&
        Objects.equals(this.bridgingTlsServerCertValidateDateEnabled, msgVpn.bridgingTlsServerCertValidateDateEnabled) &&
        Objects.equals(this.distributedCacheManagementEnabled, msgVpn.distributedCacheManagementEnabled) &&
        Objects.equals(this.enabled, msgVpn.enabled) &&
        Objects.equals(this.eventConnectionCountThreshold, msgVpn.eventConnectionCountThreshold) &&
        Objects.equals(this.eventEgressFlowCountThreshold, msgVpn.eventEgressFlowCountThreshold) &&
        Objects.equals(this.eventEgressMsgRateThreshold, msgVpn.eventEgressMsgRateThreshold) &&
        Objects.equals(this.eventEndpointCountThreshold, msgVpn.eventEndpointCountThreshold) &&
        Objects.equals(this.eventIngressFlowCountThreshold, msgVpn.eventIngressFlowCountThreshold) &&
        Objects.equals(this.eventIngressMsgRateThreshold, msgVpn.eventIngressMsgRateThreshold) &&
        Objects.equals(this.eventLargeMsgThreshold, msgVpn.eventLargeMsgThreshold) &&
        Objects.equals(this.eventLogTag, msgVpn.eventLogTag) &&
        Objects.equals(this.eventMsgSpoolUsageThreshold, msgVpn.eventMsgSpoolUsageThreshold) &&
        Objects.equals(this.eventPublishClientEnabled, msgVpn.eventPublishClientEnabled) &&
        Objects.equals(this.eventPublishMsgVpnEnabled, msgVpn.eventPublishMsgVpnEnabled) &&
        Objects.equals(this.eventPublishSubscriptionMode, msgVpn.eventPublishSubscriptionMode) &&
        Objects.equals(this.eventPublishTopicFormatMqttEnabled, msgVpn.eventPublishTopicFormatMqttEnabled) &&
        Objects.equals(this.eventPublishTopicFormatSmfEnabled, msgVpn.eventPublishTopicFormatSmfEnabled) &&
        Objects.equals(this.eventServiceRestIncomingConnectionCountThreshold, msgVpn.eventServiceRestIncomingConnectionCountThreshold) &&
        Objects.equals(this.eventServiceSmfConnectionCountThreshold, msgVpn.eventServiceSmfConnectionCountThreshold) &&
        Objects.equals(this.eventServiceWebConnectionCountThreshold, msgVpn.eventServiceWebConnectionCountThreshold) &&
        Objects.equals(this.eventSubscriptionCountThreshold, msgVpn.eventSubscriptionCountThreshold) &&
        Objects.equals(this.eventTransactedSessionCountThreshold, msgVpn.eventTransactedSessionCountThreshold) &&
        Objects.equals(this.eventTransactionCountThreshold, msgVpn.eventTransactionCountThreshold) &&
        Objects.equals(this.exportSubscriptionsEnabled, msgVpn.exportSubscriptionsEnabled) &&
        Objects.equals(this.maxConnectionCount, msgVpn.maxConnectionCount) &&
        Objects.equals(this.maxEgressFlowCount, msgVpn.maxEgressFlowCount) &&
        Objects.equals(this.maxEndpointCount, msgVpn.maxEndpointCount) &&
        Objects.equals(this.maxIngressFlowCount, msgVpn.maxIngressFlowCount) &&
        Objects.equals(this.maxMsgSpoolUsage, msgVpn.maxMsgSpoolUsage) &&
        Objects.equals(this.maxSubscriptionCount, msgVpn.maxSubscriptionCount) &&
        Objects.equals(this.maxTransactedSessionCount, msgVpn.maxTransactedSessionCount) &&
        Objects.equals(this.maxTransactionCount, msgVpn.maxTransactionCount) &&
        Objects.equals(this.msgVpnName, msgVpn.msgVpnName) &&
        Objects.equals(this.replicationAckPropagationIntervalMsgCount, msgVpn.replicationAckPropagationIntervalMsgCount) &&
        Objects.equals(this.replicationBridgeAuthenticationBasicClientUsername, msgVpn.replicationBridgeAuthenticationBasicClientUsername) &&
        Objects.equals(this.replicationBridgeAuthenticationBasicPassword, msgVpn.replicationBridgeAuthenticationBasicPassword) &&
        Objects.equals(this.replicationBridgeAuthenticationScheme, msgVpn.replicationBridgeAuthenticationScheme) &&
        Objects.equals(this.replicationBridgeCompressedDataEnabled, msgVpn.replicationBridgeCompressedDataEnabled) &&
        Objects.equals(this.replicationBridgeEgressFlowWindowSize, msgVpn.replicationBridgeEgressFlowWindowSize) &&
        Objects.equals(this.replicationBridgeRetryDelay, msgVpn.replicationBridgeRetryDelay) &&
        Objects.equals(this.replicationBridgeTlsEnabled, msgVpn.replicationBridgeTlsEnabled) &&
        Objects.equals(this.replicationBridgeUnidirectionalClientProfileName, msgVpn.replicationBridgeUnidirectionalClientProfileName) &&
        Objects.equals(this.replicationEnabled, msgVpn.replicationEnabled) &&
        Objects.equals(this.replicationEnabledQueueBehavior, msgVpn.replicationEnabledQueueBehavior) &&
        Objects.equals(this.replicationQueueMaxMsgSpoolUsage, msgVpn.replicationQueueMaxMsgSpoolUsage) &&
        Objects.equals(this.replicationQueueRejectMsgToSenderOnDiscardEnabled, msgVpn.replicationQueueRejectMsgToSenderOnDiscardEnabled) &&
        Objects.equals(this.replicationRejectMsgWhenSyncIneligibleEnabled, msgVpn.replicationRejectMsgWhenSyncIneligibleEnabled) &&
        Objects.equals(this.replicationRole, msgVpn.replicationRole) &&
        Objects.equals(this.replicationTransactionMode, msgVpn.replicationTransactionMode) &&
        Objects.equals(this.restTlsServerCertEnforceTrustedCommonNameEnabled, msgVpn.restTlsServerCertEnforceTrustedCommonNameEnabled) &&
        Objects.equals(this.restTlsServerCertMaxChainDepth, msgVpn.restTlsServerCertMaxChainDepth) &&
        Objects.equals(this.restTlsServerCertValidateDateEnabled, msgVpn.restTlsServerCertValidateDateEnabled) &&
        Objects.equals(this.sempOverMsgBusAdminClientEnabled, msgVpn.sempOverMsgBusAdminClientEnabled) &&
        Objects.equals(this.sempOverMsgBusAdminDistributedCacheEnabled, msgVpn.sempOverMsgBusAdminDistributedCacheEnabled) &&
        Objects.equals(this.sempOverMsgBusAdminEnabled, msgVpn.sempOverMsgBusAdminEnabled) &&
        Objects.equals(this.sempOverMsgBusEnabled, msgVpn.sempOverMsgBusEnabled) &&
        Objects.equals(this.sempOverMsgBusLegacyShowClearEnabled, msgVpn.sempOverMsgBusLegacyShowClearEnabled) &&
        Objects.equals(this.sempOverMsgBusShowEnabled, msgVpn.sempOverMsgBusShowEnabled) &&
        Objects.equals(this.serviceRestIncomingMaxConnectionCount, msgVpn.serviceRestIncomingMaxConnectionCount) &&
        Objects.equals(this.serviceRestIncomingPlainTextEnabled, msgVpn.serviceRestIncomingPlainTextEnabled) &&
        Objects.equals(this.serviceRestIncomingPlainTextListenPort, msgVpn.serviceRestIncomingPlainTextListenPort) &&
        Objects.equals(this.serviceRestIncomingTlsEnabled, msgVpn.serviceRestIncomingTlsEnabled) &&
        Objects.equals(this.serviceRestIncomingTlsListenPort, msgVpn.serviceRestIncomingTlsListenPort) &&
        Objects.equals(this.serviceRestOutgoingMaxConnectionCount, msgVpn.serviceRestOutgoingMaxConnectionCount) &&
        Objects.equals(this.serviceSmfMaxConnectionCount, msgVpn.serviceSmfMaxConnectionCount) &&
        Objects.equals(this.serviceSmfPlainTextEnabled, msgVpn.serviceSmfPlainTextEnabled) &&
        Objects.equals(this.serviceSmfTlsEnabled, msgVpn.serviceSmfTlsEnabled) &&
        Objects.equals(this.serviceWebMaxConnectionCount, msgVpn.serviceWebMaxConnectionCount) &&
        Objects.equals(this.serviceWebPlainTextEnabled, msgVpn.serviceWebPlainTextEnabled) &&
        Objects.equals(this.serviceWebTlsEnabled, msgVpn.serviceWebTlsEnabled) &&
        Objects.equals(this.tlsAllowDowngradeToPlainTextEnabled, msgVpn.tlsAllowDowngradeToPlainTextEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticationBasicEnabled, authenticationBasicProfileName, authenticationBasicRadiusDomain, authenticationBasicType, authenticationClientCertAllowApiProvidedUsernameEnabled, authenticationClientCertEnabled, authenticationClientCertMaxChainDepth, authenticationClientCertValidateDateEnabled, authenticationKerberosAllowApiProvidedUsernameEnabled, authenticationKerberosEnabled, authorizationLdapGroupMembershipAttributeName, authorizationProfileName, authorizationType, bridgingTlsServerCertEnforceTrustedCommonNameEnabled, bridgingTlsServerCertMaxChainDepth, bridgingTlsServerCertValidateDateEnabled, distributedCacheManagementEnabled, enabled, eventConnectionCountThreshold, eventEgressFlowCountThreshold, eventEgressMsgRateThreshold, eventEndpointCountThreshold, eventIngressFlowCountThreshold, eventIngressMsgRateThreshold, eventLargeMsgThreshold, eventLogTag, eventMsgSpoolUsageThreshold, eventPublishClientEnabled, eventPublishMsgVpnEnabled, eventPublishSubscriptionMode, eventPublishTopicFormatMqttEnabled, eventPublishTopicFormatSmfEnabled, eventServiceRestIncomingConnectionCountThreshold, eventServiceSmfConnectionCountThreshold, eventServiceWebConnectionCountThreshold, eventSubscriptionCountThreshold, eventTransactedSessionCountThreshold, eventTransactionCountThreshold, exportSubscriptionsEnabled, maxConnectionCount, maxEgressFlowCount, maxEndpointCount, maxIngressFlowCount, maxMsgSpoolUsage, maxSubscriptionCount, maxTransactedSessionCount, maxTransactionCount, msgVpnName, replicationAckPropagationIntervalMsgCount, replicationBridgeAuthenticationBasicClientUsername, replicationBridgeAuthenticationBasicPassword, replicationBridgeAuthenticationScheme, replicationBridgeCompressedDataEnabled, replicationBridgeEgressFlowWindowSize, replicationBridgeRetryDelay, replicationBridgeTlsEnabled, replicationBridgeUnidirectionalClientProfileName, replicationEnabled, replicationEnabledQueueBehavior, replicationQueueMaxMsgSpoolUsage, replicationQueueRejectMsgToSenderOnDiscardEnabled, replicationRejectMsgWhenSyncIneligibleEnabled, replicationRole, replicationTransactionMode, restTlsServerCertEnforceTrustedCommonNameEnabled, restTlsServerCertMaxChainDepth, restTlsServerCertValidateDateEnabled, sempOverMsgBusAdminClientEnabled, sempOverMsgBusAdminDistributedCacheEnabled, sempOverMsgBusAdminEnabled, sempOverMsgBusEnabled, sempOverMsgBusLegacyShowClearEnabled, sempOverMsgBusShowEnabled, serviceRestIncomingMaxConnectionCount, serviceRestIncomingPlainTextEnabled, serviceRestIncomingPlainTextListenPort, serviceRestIncomingTlsEnabled, serviceRestIncomingTlsListenPort, serviceRestOutgoingMaxConnectionCount, serviceSmfMaxConnectionCount, serviceSmfPlainTextEnabled, serviceSmfTlsEnabled, serviceWebMaxConnectionCount, serviceWebPlainTextEnabled, serviceWebTlsEnabled, tlsAllowDowngradeToPlainTextEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpn {\n");
    
    sb.append("    authenticationBasicEnabled: ").append(toIndentedString(authenticationBasicEnabled)).append("\n");
    sb.append("    authenticationBasicProfileName: ").append(toIndentedString(authenticationBasicProfileName)).append("\n");
    sb.append("    authenticationBasicRadiusDomain: ").append(toIndentedString(authenticationBasicRadiusDomain)).append("\n");
    sb.append("    authenticationBasicType: ").append(toIndentedString(authenticationBasicType)).append("\n");
    sb.append("    authenticationClientCertAllowApiProvidedUsernameEnabled: ").append(toIndentedString(authenticationClientCertAllowApiProvidedUsernameEnabled)).append("\n");
    sb.append("    authenticationClientCertEnabled: ").append(toIndentedString(authenticationClientCertEnabled)).append("\n");
    sb.append("    authenticationClientCertMaxChainDepth: ").append(toIndentedString(authenticationClientCertMaxChainDepth)).append("\n");
    sb.append("    authenticationClientCertValidateDateEnabled: ").append(toIndentedString(authenticationClientCertValidateDateEnabled)).append("\n");
    sb.append("    authenticationKerberosAllowApiProvidedUsernameEnabled: ").append(toIndentedString(authenticationKerberosAllowApiProvidedUsernameEnabled)).append("\n");
    sb.append("    authenticationKerberosEnabled: ").append(toIndentedString(authenticationKerberosEnabled)).append("\n");
    sb.append("    authorizationLdapGroupMembershipAttributeName: ").append(toIndentedString(authorizationLdapGroupMembershipAttributeName)).append("\n");
    sb.append("    authorizationProfileName: ").append(toIndentedString(authorizationProfileName)).append("\n");
    sb.append("    authorizationType: ").append(toIndentedString(authorizationType)).append("\n");
    sb.append("    bridgingTlsServerCertEnforceTrustedCommonNameEnabled: ").append(toIndentedString(bridgingTlsServerCertEnforceTrustedCommonNameEnabled)).append("\n");
    sb.append("    bridgingTlsServerCertMaxChainDepth: ").append(toIndentedString(bridgingTlsServerCertMaxChainDepth)).append("\n");
    sb.append("    bridgingTlsServerCertValidateDateEnabled: ").append(toIndentedString(bridgingTlsServerCertValidateDateEnabled)).append("\n");
    sb.append("    distributedCacheManagementEnabled: ").append(toIndentedString(distributedCacheManagementEnabled)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    eventConnectionCountThreshold: ").append(toIndentedString(eventConnectionCountThreshold)).append("\n");
    sb.append("    eventEgressFlowCountThreshold: ").append(toIndentedString(eventEgressFlowCountThreshold)).append("\n");
    sb.append("    eventEgressMsgRateThreshold: ").append(toIndentedString(eventEgressMsgRateThreshold)).append("\n");
    sb.append("    eventEndpointCountThreshold: ").append(toIndentedString(eventEndpointCountThreshold)).append("\n");
    sb.append("    eventIngressFlowCountThreshold: ").append(toIndentedString(eventIngressFlowCountThreshold)).append("\n");
    sb.append("    eventIngressMsgRateThreshold: ").append(toIndentedString(eventIngressMsgRateThreshold)).append("\n");
    sb.append("    eventLargeMsgThreshold: ").append(toIndentedString(eventLargeMsgThreshold)).append("\n");
    sb.append("    eventLogTag: ").append(toIndentedString(eventLogTag)).append("\n");
    sb.append("    eventMsgSpoolUsageThreshold: ").append(toIndentedString(eventMsgSpoolUsageThreshold)).append("\n");
    sb.append("    eventPublishClientEnabled: ").append(toIndentedString(eventPublishClientEnabled)).append("\n");
    sb.append("    eventPublishMsgVpnEnabled: ").append(toIndentedString(eventPublishMsgVpnEnabled)).append("\n");
    sb.append("    eventPublishSubscriptionMode: ").append(toIndentedString(eventPublishSubscriptionMode)).append("\n");
    sb.append("    eventPublishTopicFormatMqttEnabled: ").append(toIndentedString(eventPublishTopicFormatMqttEnabled)).append("\n");
    sb.append("    eventPublishTopicFormatSmfEnabled: ").append(toIndentedString(eventPublishTopicFormatSmfEnabled)).append("\n");
    sb.append("    eventServiceRestIncomingConnectionCountThreshold: ").append(toIndentedString(eventServiceRestIncomingConnectionCountThreshold)).append("\n");
    sb.append("    eventServiceSmfConnectionCountThreshold: ").append(toIndentedString(eventServiceSmfConnectionCountThreshold)).append("\n");
    sb.append("    eventServiceWebConnectionCountThreshold: ").append(toIndentedString(eventServiceWebConnectionCountThreshold)).append("\n");
    sb.append("    eventSubscriptionCountThreshold: ").append(toIndentedString(eventSubscriptionCountThreshold)).append("\n");
    sb.append("    eventTransactedSessionCountThreshold: ").append(toIndentedString(eventTransactedSessionCountThreshold)).append("\n");
    sb.append("    eventTransactionCountThreshold: ").append(toIndentedString(eventTransactionCountThreshold)).append("\n");
    sb.append("    exportSubscriptionsEnabled: ").append(toIndentedString(exportSubscriptionsEnabled)).append("\n");
    sb.append("    maxConnectionCount: ").append(toIndentedString(maxConnectionCount)).append("\n");
    sb.append("    maxEgressFlowCount: ").append(toIndentedString(maxEgressFlowCount)).append("\n");
    sb.append("    maxEndpointCount: ").append(toIndentedString(maxEndpointCount)).append("\n");
    sb.append("    maxIngressFlowCount: ").append(toIndentedString(maxIngressFlowCount)).append("\n");
    sb.append("    maxMsgSpoolUsage: ").append(toIndentedString(maxMsgSpoolUsage)).append("\n");
    sb.append("    maxSubscriptionCount: ").append(toIndentedString(maxSubscriptionCount)).append("\n");
    sb.append("    maxTransactedSessionCount: ").append(toIndentedString(maxTransactedSessionCount)).append("\n");
    sb.append("    maxTransactionCount: ").append(toIndentedString(maxTransactionCount)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    replicationAckPropagationIntervalMsgCount: ").append(toIndentedString(replicationAckPropagationIntervalMsgCount)).append("\n");
    sb.append("    replicationBridgeAuthenticationBasicClientUsername: ").append(toIndentedString(replicationBridgeAuthenticationBasicClientUsername)).append("\n");
    sb.append("    replicationBridgeAuthenticationBasicPassword: ").append(toIndentedString(replicationBridgeAuthenticationBasicPassword)).append("\n");
    sb.append("    replicationBridgeAuthenticationScheme: ").append(toIndentedString(replicationBridgeAuthenticationScheme)).append("\n");
    sb.append("    replicationBridgeCompressedDataEnabled: ").append(toIndentedString(replicationBridgeCompressedDataEnabled)).append("\n");
    sb.append("    replicationBridgeEgressFlowWindowSize: ").append(toIndentedString(replicationBridgeEgressFlowWindowSize)).append("\n");
    sb.append("    replicationBridgeRetryDelay: ").append(toIndentedString(replicationBridgeRetryDelay)).append("\n");
    sb.append("    replicationBridgeTlsEnabled: ").append(toIndentedString(replicationBridgeTlsEnabled)).append("\n");
    sb.append("    replicationBridgeUnidirectionalClientProfileName: ").append(toIndentedString(replicationBridgeUnidirectionalClientProfileName)).append("\n");
    sb.append("    replicationEnabled: ").append(toIndentedString(replicationEnabled)).append("\n");
    sb.append("    replicationEnabledQueueBehavior: ").append(toIndentedString(replicationEnabledQueueBehavior)).append("\n");
    sb.append("    replicationQueueMaxMsgSpoolUsage: ").append(toIndentedString(replicationQueueMaxMsgSpoolUsage)).append("\n");
    sb.append("    replicationQueueRejectMsgToSenderOnDiscardEnabled: ").append(toIndentedString(replicationQueueRejectMsgToSenderOnDiscardEnabled)).append("\n");
    sb.append("    replicationRejectMsgWhenSyncIneligibleEnabled: ").append(toIndentedString(replicationRejectMsgWhenSyncIneligibleEnabled)).append("\n");
    sb.append("    replicationRole: ").append(toIndentedString(replicationRole)).append("\n");
    sb.append("    replicationTransactionMode: ").append(toIndentedString(replicationTransactionMode)).append("\n");
    sb.append("    restTlsServerCertEnforceTrustedCommonNameEnabled: ").append(toIndentedString(restTlsServerCertEnforceTrustedCommonNameEnabled)).append("\n");
    sb.append("    restTlsServerCertMaxChainDepth: ").append(toIndentedString(restTlsServerCertMaxChainDepth)).append("\n");
    sb.append("    restTlsServerCertValidateDateEnabled: ").append(toIndentedString(restTlsServerCertValidateDateEnabled)).append("\n");
    sb.append("    sempOverMsgBusAdminClientEnabled: ").append(toIndentedString(sempOverMsgBusAdminClientEnabled)).append("\n");
    sb.append("    sempOverMsgBusAdminDistributedCacheEnabled: ").append(toIndentedString(sempOverMsgBusAdminDistributedCacheEnabled)).append("\n");
    sb.append("    sempOverMsgBusAdminEnabled: ").append(toIndentedString(sempOverMsgBusAdminEnabled)).append("\n");
    sb.append("    sempOverMsgBusEnabled: ").append(toIndentedString(sempOverMsgBusEnabled)).append("\n");
    sb.append("    sempOverMsgBusLegacyShowClearEnabled: ").append(toIndentedString(sempOverMsgBusLegacyShowClearEnabled)).append("\n");
    sb.append("    sempOverMsgBusShowEnabled: ").append(toIndentedString(sempOverMsgBusShowEnabled)).append("\n");
    sb.append("    serviceRestIncomingMaxConnectionCount: ").append(toIndentedString(serviceRestIncomingMaxConnectionCount)).append("\n");
    sb.append("    serviceRestIncomingPlainTextEnabled: ").append(toIndentedString(serviceRestIncomingPlainTextEnabled)).append("\n");
    sb.append("    serviceRestIncomingPlainTextListenPort: ").append(toIndentedString(serviceRestIncomingPlainTextListenPort)).append("\n");
    sb.append("    serviceRestIncomingTlsEnabled: ").append(toIndentedString(serviceRestIncomingTlsEnabled)).append("\n");
    sb.append("    serviceRestIncomingTlsListenPort: ").append(toIndentedString(serviceRestIncomingTlsListenPort)).append("\n");
    sb.append("    serviceRestOutgoingMaxConnectionCount: ").append(toIndentedString(serviceRestOutgoingMaxConnectionCount)).append("\n");
    sb.append("    serviceSmfMaxConnectionCount: ").append(toIndentedString(serviceSmfMaxConnectionCount)).append("\n");
    sb.append("    serviceSmfPlainTextEnabled: ").append(toIndentedString(serviceSmfPlainTextEnabled)).append("\n");
    sb.append("    serviceSmfTlsEnabled: ").append(toIndentedString(serviceSmfTlsEnabled)).append("\n");
    sb.append("    serviceWebMaxConnectionCount: ").append(toIndentedString(serviceWebMaxConnectionCount)).append("\n");
    sb.append("    serviceWebPlainTextEnabled: ").append(toIndentedString(serviceWebPlainTextEnabled)).append("\n");
    sb.append("    serviceWebTlsEnabled: ").append(toIndentedString(serviceWebTlsEnabled)).append("\n");
    sb.append("    tlsAllowDowngradeToPlainTextEnabled: ").append(toIndentedString(tlsAllowDowngradeToPlainTextEnabled)).append("\n");
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

