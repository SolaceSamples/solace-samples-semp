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
import com.solace.labs.sempclient.samplelib.model.EventThreshold;
import com.solace.labs.sempclient.samplelib.model.EventThresholdByValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * MsgVpn
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-11T16:20:54.920-04:00")
public class MsgVpn {
  @JsonProperty("authenticationBasicEnabled")
  private Boolean authenticationBasicEnabled = null;

  @JsonProperty("authenticationBasicProfileName")
  private String authenticationBasicProfileName = null;

  @JsonProperty("authenticationBasicRadiusDomain")
  private String authenticationBasicRadiusDomain = null;

  /**
   * The type of basic authentication to use for clients connecting to the Message VPN. The default value is `\"radius\"`. The allowed values and their meaning are:  <pre> \"internal\" - Internal database. Authentication is against Client Usernames. \"ldap\" - LDAP authentication. An LDAP profile name must be provided. \"radius\" - RADIUS authentication. A RADIUS profile name must be provided. \"none\" - No authentication. Anonymous login allowed. </pre> 
   */
  public enum AuthenticationBasicTypeEnum {
    INTERNAL("internal"),
    
    LDAP("ldap"),
    
    RADIUS("radius"),
    
    NONE("none");

    private String value;

    AuthenticationBasicTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AuthenticationBasicTypeEnum fromValue(String text) {
      for (AuthenticationBasicTypeEnum b : AuthenticationBasicTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("authenticationBasicType")
  private AuthenticationBasicTypeEnum authenticationBasicType = null;

  @JsonProperty("authenticationClientCertAllowApiProvidedUsernameEnabled")
  private Boolean authenticationClientCertAllowApiProvidedUsernameEnabled = null;

  @JsonProperty("authenticationClientCertEnabled")
  private Boolean authenticationClientCertEnabled = null;

  @JsonProperty("authenticationClientCertMaxChainDepth")
  private Long authenticationClientCertMaxChainDepth = null;

  /**
   * The desired behavior for client certificate revocation checking. The default value is `\"allow-valid\"`. The allowed values and their meaning are:  <pre> \"allow-all\" - Allow the client to authenticate, the result of client certificate revocation check is ignored. \"allow-unknown\" - Allow the client to authenticate even if the revocation status of his certificate cannot be determined. \"allow-valid\" - Allow the client to authenticate only when the revocation check returned an explicit positive response. </pre>  Available since 2.6.
   */
  public enum AuthenticationClientCertRevocationCheckModeEnum {
    ALL("allow-all"),
    
    UNKNOWN("allow-unknown"),
    
    VALID("allow-valid");

    private String value;

    AuthenticationClientCertRevocationCheckModeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AuthenticationClientCertRevocationCheckModeEnum fromValue(String text) {
      for (AuthenticationClientCertRevocationCheckModeEnum b : AuthenticationClientCertRevocationCheckModeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("authenticationClientCertRevocationCheckMode")
  private AuthenticationClientCertRevocationCheckModeEnum authenticationClientCertRevocationCheckMode = null;

  /**
   * The field from the client certificate to use as the client username. The default value is `\"common-name\"`. The allowed values and their meaning are:  <pre> \"common-name\" - The username is extracted from the certificate's Common Name. \"subject-alternate-name-msupn\" - The username is extracted from the certificate's Other Name type of the Subject Alternative Name and must have the msUPN signature. </pre>  Available since 2.5.
   */
  public enum AuthenticationClientCertUsernameSourceEnum {
    COMMON_NAME("common-name"),
    
    SUBJECT_ALTERNATE_NAME_MSUPN("subject-alternate-name-msupn");

    private String value;

    AuthenticationClientCertUsernameSourceEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AuthenticationClientCertUsernameSourceEnum fromValue(String text) {
      for (AuthenticationClientCertUsernameSourceEnum b : AuthenticationClientCertUsernameSourceEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("authenticationClientCertUsernameSource")
  private AuthenticationClientCertUsernameSourceEnum authenticationClientCertUsernameSource = null;

  @JsonProperty("authenticationClientCertValidateDateEnabled")
  private Boolean authenticationClientCertValidateDateEnabled = null;

  @JsonProperty("authenticationKerberosAllowApiProvidedUsernameEnabled")
  private Boolean authenticationKerberosAllowApiProvidedUsernameEnabled = null;

  @JsonProperty("authenticationKerberosEnabled")
  private Boolean authenticationKerberosEnabled = null;

  @JsonProperty("authorizationLdapGroupMembershipAttributeName")
  private String authorizationLdapGroupMembershipAttributeName = null;

  @JsonProperty("authorizationProfileName")
  private String authorizationProfileName = null;

  /**
   * The type of authorization to use for clients connecting to the Message VPN. The default value is `\"internal\"`. The allowed values and their meaning are:  <pre> \"ldap\" - LDAP authorization. \"internal\" - Internal authorization. </pre> 
   */
  public enum AuthorizationTypeEnum {
    LDAP("ldap"),
    
    INTERNAL("internal");

    private String value;

    AuthorizationTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AuthorizationTypeEnum fromValue(String text) {
      for (AuthorizationTypeEnum b : AuthorizationTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("authorizationType")
  private AuthorizationTypeEnum authorizationType = null;

  @JsonProperty("bridgingTlsServerCertEnforceTrustedCommonNameEnabled")
  private Boolean bridgingTlsServerCertEnforceTrustedCommonNameEnabled = null;

  @JsonProperty("bridgingTlsServerCertMaxChainDepth")
  private Long bridgingTlsServerCertMaxChainDepth = null;

  @JsonProperty("bridgingTlsServerCertValidateDateEnabled")
  private Boolean bridgingTlsServerCertValidateDateEnabled = null;

  @JsonProperty("distributedCacheManagementEnabled")
  private Boolean distributedCacheManagementEnabled = null;

  @JsonProperty("dmrEnabled")
  private Boolean dmrEnabled = null;

  @JsonProperty("enabled")
  private Boolean enabled = null;

  @JsonProperty("eventConnectionCountThreshold")
  private EventThreshold eventConnectionCountThreshold = null;

  @JsonProperty("eventEgressFlowCountThreshold")
  private EventThreshold eventEgressFlowCountThreshold = null;

  @JsonProperty("eventEgressMsgRateThreshold")
  private EventThresholdByValue eventEgressMsgRateThreshold = null;

  @JsonProperty("eventEndpointCountThreshold")
  private EventThreshold eventEndpointCountThreshold = null;

  @JsonProperty("eventIngressFlowCountThreshold")
  private EventThreshold eventIngressFlowCountThreshold = null;

  @JsonProperty("eventIngressMsgRateThreshold")
  private EventThresholdByValue eventIngressMsgRateThreshold = null;

  @JsonProperty("eventLargeMsgThreshold")
  private Long eventLargeMsgThreshold = null;

  @JsonProperty("eventLogTag")
  private String eventLogTag = null;

  @JsonProperty("eventMsgSpoolUsageThreshold")
  private EventThreshold eventMsgSpoolUsageThreshold = null;

  @JsonProperty("eventPublishClientEnabled")
  private Boolean eventPublishClientEnabled = null;

  @JsonProperty("eventPublishMsgVpnEnabled")
  private Boolean eventPublishMsgVpnEnabled = null;

  /**
   * Subscription level Event message publishing mode. The default value is `\"off\"`. The allowed values and their meaning are:  <pre> \"off\" - Disable client level event message publishing. \"on-with-format-v1\" - Enable client level event message publishing with format v1. \"on-with-no-unsubscribe-events-on-disconnect-format-v1\" - As \"on-with-format-v1\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. \"on-with-format-v2\" - Enable client level event message publishing with format v2. \"on-with-no-unsubscribe-events-on-disconnect-format-v2\" - As \"on-with-format-v2\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. </pre> 
   */
  public enum EventPublishSubscriptionModeEnum {
    OFF("off"),
    
    ON_WITH_FORMAT_V1("on-with-format-v1"),
    
    ON_WITH_NO_UNSUBSCRIBE_EVENTS_ON_DISCONNECT_FORMAT_V1("on-with-no-unsubscribe-events-on-disconnect-format-v1"),
    
    ON_WITH_FORMAT_V2("on-with-format-v2"),
    
    ON_WITH_NO_UNSUBSCRIBE_EVENTS_ON_DISCONNECT_FORMAT_V2("on-with-no-unsubscribe-events-on-disconnect-format-v2");

    private String value;

    EventPublishSubscriptionModeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static EventPublishSubscriptionModeEnum fromValue(String text) {
      for (EventPublishSubscriptionModeEnum b : EventPublishSubscriptionModeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("eventPublishSubscriptionMode")
  private EventPublishSubscriptionModeEnum eventPublishSubscriptionMode = null;

  @JsonProperty("eventPublishTopicFormatMqttEnabled")
  private Boolean eventPublishTopicFormatMqttEnabled = null;

  @JsonProperty("eventPublishTopicFormatSmfEnabled")
  private Boolean eventPublishTopicFormatSmfEnabled = null;

  @JsonProperty("eventServiceAmqpConnectionCountThreshold")
  private EventThreshold eventServiceAmqpConnectionCountThreshold = null;

  @JsonProperty("eventServiceMqttConnectionCountThreshold")
  private EventThreshold eventServiceMqttConnectionCountThreshold = null;

  @JsonProperty("eventServiceRestIncomingConnectionCountThreshold")
  private EventThreshold eventServiceRestIncomingConnectionCountThreshold = null;

  @JsonProperty("eventServiceSmfConnectionCountThreshold")
  private EventThreshold eventServiceSmfConnectionCountThreshold = null;

  @JsonProperty("eventServiceWebConnectionCountThreshold")
  private EventThreshold eventServiceWebConnectionCountThreshold = null;

  @JsonProperty("eventSubscriptionCountThreshold")
  private EventThreshold eventSubscriptionCountThreshold = null;

  @JsonProperty("eventTransactedSessionCountThreshold")
  private EventThreshold eventTransactedSessionCountThreshold = null;

  @JsonProperty("eventTransactionCountThreshold")
  private EventThreshold eventTransactionCountThreshold = null;

  @JsonProperty("exportSubscriptionsEnabled")
  private Boolean exportSubscriptionsEnabled = null;

  @JsonProperty("jndiEnabled")
  private Boolean jndiEnabled = null;

  @JsonProperty("maxConnectionCount")
  private Long maxConnectionCount = null;

  @JsonProperty("maxEgressFlowCount")
  private Long maxEgressFlowCount = null;

  @JsonProperty("maxEndpointCount")
  private Long maxEndpointCount = null;

  @JsonProperty("maxIngressFlowCount")
  private Long maxIngressFlowCount = null;

  @JsonProperty("maxMsgSpoolUsage")
  private Long maxMsgSpoolUsage = null;

  @JsonProperty("maxSubscriptionCount")
  private Long maxSubscriptionCount = null;

  @JsonProperty("maxTransactedSessionCount")
  private Long maxTransactedSessionCount = null;

  @JsonProperty("maxTransactionCount")
  private Long maxTransactionCount = null;

  @JsonProperty("mqttRetainMaxMemory")
  private Integer mqttRetainMaxMemory = null;

  @JsonProperty("msgVpnName")
  private String msgVpnName = null;

  @JsonProperty("replicationAckPropagationIntervalMsgCount")
  private Long replicationAckPropagationIntervalMsgCount = null;

  @JsonProperty("replicationBridgeAuthenticationBasicClientUsername")
  private String replicationBridgeAuthenticationBasicClientUsername = null;

  @JsonProperty("replicationBridgeAuthenticationBasicPassword")
  private String replicationBridgeAuthenticationBasicPassword = null;

  @JsonProperty("replicationBridgeAuthenticationClientCertContent")
  private String replicationBridgeAuthenticationClientCertContent = null;

  @JsonProperty("replicationBridgeAuthenticationClientCertPassword")
  private String replicationBridgeAuthenticationClientCertPassword = null;

  /**
   * The authentication scheme for the replication Bridge in the Message VPN. The default value is `\"basic\"`. The allowed values and their meaning are:  <pre> \"basic\" - Basic Authentication Scheme (via username and password). \"client-certificate\" - Client Certificate Authentication Scheme (via certificate file or content). </pre> 
   */
  public enum ReplicationBridgeAuthenticationSchemeEnum {
    BASIC("basic"),
    
    CLIENT_CERTIFICATE("client-certificate");

    private String value;

    ReplicationBridgeAuthenticationSchemeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ReplicationBridgeAuthenticationSchemeEnum fromValue(String text) {
      for (ReplicationBridgeAuthenticationSchemeEnum b : ReplicationBridgeAuthenticationSchemeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("replicationBridgeAuthenticationScheme")
  private ReplicationBridgeAuthenticationSchemeEnum replicationBridgeAuthenticationScheme = null;

  @JsonProperty("replicationBridgeCompressedDataEnabled")
  private Boolean replicationBridgeCompressedDataEnabled = null;

  @JsonProperty("replicationBridgeEgressFlowWindowSize")
  private Long replicationBridgeEgressFlowWindowSize = null;

  @JsonProperty("replicationBridgeRetryDelay")
  private Long replicationBridgeRetryDelay = null;

  @JsonProperty("replicationBridgeTlsEnabled")
  private Boolean replicationBridgeTlsEnabled = null;

  @JsonProperty("replicationBridgeUnidirectionalClientProfileName")
  private String replicationBridgeUnidirectionalClientProfileName = null;

  @JsonProperty("replicationEnabled")
  private Boolean replicationEnabled = null;

  /**
   * The behavior to take when enabling replication for the Message VPN, depending on the existence of the replication Queue. The default value is `\"fail-on-existing-queue\"`. The allowed values and their meaning are:  <pre> \"fail-on-existing-queue\" - The data replication queue must not already exist. \"force-use-existing-queue\" - The data replication queue must already exist. Any data messages on the Queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail. \"force-recreate-queue\" - The data replication queue must already exist. Any data messages on the Queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. </pre> 
   */
  public enum ReplicationEnabledQueueBehaviorEnum {
    FAIL_ON_EXISTING_QUEUE("fail-on-existing-queue"),
    
    FORCE_USE_EXISTING_QUEUE("force-use-existing-queue"),
    
    FORCE_RECREATE_QUEUE("force-recreate-queue");

    private String value;

    ReplicationEnabledQueueBehaviorEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ReplicationEnabledQueueBehaviorEnum fromValue(String text) {
      for (ReplicationEnabledQueueBehaviorEnum b : ReplicationEnabledQueueBehaviorEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("replicationEnabledQueueBehavior")
  private ReplicationEnabledQueueBehaviorEnum replicationEnabledQueueBehavior = null;

  @JsonProperty("replicationQueueMaxMsgSpoolUsage")
  private Long replicationQueueMaxMsgSpoolUsage = null;

  @JsonProperty("replicationQueueRejectMsgToSenderOnDiscardEnabled")
  private Boolean replicationQueueRejectMsgToSenderOnDiscardEnabled = null;

  @JsonProperty("replicationRejectMsgWhenSyncIneligibleEnabled")
  private Boolean replicationRejectMsgWhenSyncIneligibleEnabled = null;

  /**
   * The replication role for the Message VPN. The default value is `\"standby\"`. The allowed values and their meaning are:  <pre> \"active\" - Assume the Active role in replication for the Message VPN. \"standby\" - Assume the Standby role in replication for the Message VPN. </pre> 
   */
  public enum ReplicationRoleEnum {
    ACTIVE("active"),
    
    STANDBY("standby");

    private String value;

    ReplicationRoleEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ReplicationRoleEnum fromValue(String text) {
      for (ReplicationRoleEnum b : ReplicationRoleEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("replicationRole")
  private ReplicationRoleEnum replicationRole = null;

  /**
   * The transaction replication mode for all transactions within the Message VPN. Changing this value during operation will not affect existing transactions; it is only used upon starting a transaction. The default value is `\"async\"`. The allowed values and their meaning are:  <pre> \"sync\" - Messages are acknowledged when replicated (spooled remotely). \"async\" - Messages are acknowledged when pending replication (spooled locally). </pre> 
   */
  public enum ReplicationTransactionModeEnum {
    SYNC("sync"),
    
    ASYNC("async");

    private String value;

    ReplicationTransactionModeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ReplicationTransactionModeEnum fromValue(String text) {
      for (ReplicationTransactionModeEnum b : ReplicationTransactionModeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("replicationTransactionMode")
  private ReplicationTransactionModeEnum replicationTransactionMode = null;

  @JsonProperty("restTlsServerCertEnforceTrustedCommonNameEnabled")
  private Boolean restTlsServerCertEnforceTrustedCommonNameEnabled = null;

  @JsonProperty("restTlsServerCertMaxChainDepth")
  private Long restTlsServerCertMaxChainDepth = null;

  @JsonProperty("restTlsServerCertValidateDateEnabled")
  private Boolean restTlsServerCertValidateDateEnabled = null;

  @JsonProperty("sempOverMsgBusAdminClientEnabled")
  private Boolean sempOverMsgBusAdminClientEnabled = null;

  @JsonProperty("sempOverMsgBusAdminDistributedCacheEnabled")
  private Boolean sempOverMsgBusAdminDistributedCacheEnabled = null;

  @JsonProperty("sempOverMsgBusAdminEnabled")
  private Boolean sempOverMsgBusAdminEnabled = null;

  @JsonProperty("sempOverMsgBusEnabled")
  private Boolean sempOverMsgBusEnabled = null;

  @JsonProperty("sempOverMsgBusShowEnabled")
  private Boolean sempOverMsgBusShowEnabled = null;

  @JsonProperty("serviceAmqpMaxConnectionCount")
  private Long serviceAmqpMaxConnectionCount = null;

  @JsonProperty("serviceAmqpPlainTextEnabled")
  private Boolean serviceAmqpPlainTextEnabled = null;

  @JsonProperty("serviceAmqpPlainTextListenPort")
  private Long serviceAmqpPlainTextListenPort = null;

  @JsonProperty("serviceAmqpTlsEnabled")
  private Boolean serviceAmqpTlsEnabled = null;

  @JsonProperty("serviceAmqpTlsListenPort")
  private Long serviceAmqpTlsListenPort = null;

  @JsonProperty("serviceMqttMaxConnectionCount")
  private Long serviceMqttMaxConnectionCount = null;

  @JsonProperty("serviceMqttPlainTextEnabled")
  private Boolean serviceMqttPlainTextEnabled = null;

  @JsonProperty("serviceMqttPlainTextListenPort")
  private Long serviceMqttPlainTextListenPort = null;

  @JsonProperty("serviceMqttTlsEnabled")
  private Boolean serviceMqttTlsEnabled = null;

  @JsonProperty("serviceMqttTlsListenPort")
  private Long serviceMqttTlsListenPort = null;

  @JsonProperty("serviceMqttTlsWebSocketEnabled")
  private Boolean serviceMqttTlsWebSocketEnabled = null;

  @JsonProperty("serviceMqttTlsWebSocketListenPort")
  private Long serviceMqttTlsWebSocketListenPort = null;

  @JsonProperty("serviceMqttWebSocketEnabled")
  private Boolean serviceMqttWebSocketEnabled = null;

  @JsonProperty("serviceMqttWebSocketListenPort")
  private Long serviceMqttWebSocketListenPort = null;

  @JsonProperty("serviceRestIncomingMaxConnectionCount")
  private Long serviceRestIncomingMaxConnectionCount = null;

  @JsonProperty("serviceRestIncomingPlainTextEnabled")
  private Boolean serviceRestIncomingPlainTextEnabled = null;

  @JsonProperty("serviceRestIncomingPlainTextListenPort")
  private Long serviceRestIncomingPlainTextListenPort = null;

  @JsonProperty("serviceRestIncomingTlsEnabled")
  private Boolean serviceRestIncomingTlsEnabled = null;

  @JsonProperty("serviceRestIncomingTlsListenPort")
  private Long serviceRestIncomingTlsListenPort = null;

  /**
   * The REST service mode for incoming REST clients that connect to the Message VPN. The default value is `\"messaging\"`. The allowed values and their meaning are:  <pre> \"gateway\" - Act as a message gateway through which REST messages are propagated. \"messaging\" - Act as a message broker on which REST messages are queued. </pre>  Available since 2.6.
   */
  public enum ServiceRestModeEnum {
    GATEWAY("gateway"),
    
    MESSAGING("messaging");

    private String value;

    ServiceRestModeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ServiceRestModeEnum fromValue(String text) {
      for (ServiceRestModeEnum b : ServiceRestModeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("serviceRestMode")
  private ServiceRestModeEnum serviceRestMode = null;

  @JsonProperty("serviceRestOutgoingMaxConnectionCount")
  private Long serviceRestOutgoingMaxConnectionCount = null;

  @JsonProperty("serviceSmfMaxConnectionCount")
  private Long serviceSmfMaxConnectionCount = null;

  @JsonProperty("serviceSmfPlainTextEnabled")
  private Boolean serviceSmfPlainTextEnabled = null;

  @JsonProperty("serviceSmfTlsEnabled")
  private Boolean serviceSmfTlsEnabled = null;

  @JsonProperty("serviceWebMaxConnectionCount")
  private Long serviceWebMaxConnectionCount = null;

  @JsonProperty("serviceWebPlainTextEnabled")
  private Boolean serviceWebPlainTextEnabled = null;

  @JsonProperty("serviceWebTlsEnabled")
  private Boolean serviceWebTlsEnabled = null;

  @JsonProperty("tlsAllowDowngradeToPlainTextEnabled")
  private Boolean tlsAllowDowngradeToPlainTextEnabled = null;

  public MsgVpn authenticationBasicEnabled(Boolean authenticationBasicEnabled) {
    this.authenticationBasicEnabled = authenticationBasicEnabled;
    return this;
  }

   /**
   * Enable or disable basic authentication for clients connecting to the Message VPN. The default value is `true`.
   * @return authenticationBasicEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable basic authentication for clients connecting to the Message VPN. The default value is `true`.")
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
   * The name of the RADIUS or LDAP Profile to use for basic authentication. The default value is `\"default\"`.
   * @return authenticationBasicProfileName
  **/
  @ApiModelProperty(example = "null", value = "The name of the RADIUS or LDAP Profile to use for basic authentication. The default value is `\"default\"`.")
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
   * The RADIUS domain to use for basic authentication. The default value is `\"\"`.
   * @return authenticationBasicRadiusDomain
  **/
  @ApiModelProperty(example = "null", value = "The RADIUS domain to use for basic authentication. The default value is `\"\"`.")
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
   * The type of basic authentication to use for clients connecting to the Message VPN. The default value is `\"radius\"`. The allowed values and their meaning are:  <pre> \"internal\" - Internal database. Authentication is against Client Usernames. \"ldap\" - LDAP authentication. An LDAP profile name must be provided. \"radius\" - RADIUS authentication. A RADIUS profile name must be provided. \"none\" - No authentication. Anonymous login allowed. </pre> 
   * @return authenticationBasicType
  **/
  @ApiModelProperty(example = "null", value = "The type of basic authentication to use for clients connecting to the Message VPN. The default value is `\"radius\"`. The allowed values and their meaning are:  <pre> \"internal\" - Internal database. Authentication is against Client Usernames. \"ldap\" - LDAP authentication. An LDAP profile name must be provided. \"radius\" - RADIUS authentication. A RADIUS profile name must be provided. \"none\" - No authentication. Anonymous login allowed. </pre> ")
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
   * Enable or disable allowing a client to specify a Client Username via the API connect method. When disabled, the certificate CN (Common Name) is always used. The default value is `false`.
   * @return authenticationClientCertAllowApiProvidedUsernameEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing a client to specify a Client Username via the API connect method. When disabled, the certificate CN (Common Name) is always used. The default value is `false`.")
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
   * Enable or disable client certificate authentication in the Message VPN. The default value is `false`.
   * @return authenticationClientCertEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable client certificate authentication in the Message VPN. The default value is `false`.")
  public Boolean getAuthenticationClientCertEnabled() {
    return authenticationClientCertEnabled;
  }

  public void setAuthenticationClientCertEnabled(Boolean authenticationClientCertEnabled) {
    this.authenticationClientCertEnabled = authenticationClientCertEnabled;
  }

  public MsgVpn authenticationClientCertMaxChainDepth(Long authenticationClientCertMaxChainDepth) {
    this.authenticationClientCertMaxChainDepth = authenticationClientCertMaxChainDepth;
    return this;
  }

   /**
   * The maximum depth for a client certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.
   * @return authenticationClientCertMaxChainDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth for a client certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.")
  public Long getAuthenticationClientCertMaxChainDepth() {
    return authenticationClientCertMaxChainDepth;
  }

  public void setAuthenticationClientCertMaxChainDepth(Long authenticationClientCertMaxChainDepth) {
    this.authenticationClientCertMaxChainDepth = authenticationClientCertMaxChainDepth;
  }

  public MsgVpn authenticationClientCertRevocationCheckMode(AuthenticationClientCertRevocationCheckModeEnum authenticationClientCertRevocationCheckMode) {
    this.authenticationClientCertRevocationCheckMode = authenticationClientCertRevocationCheckMode;
    return this;
  }

   /**
   * The desired behavior for client certificate revocation checking. The default value is `\"allow-valid\"`. The allowed values and their meaning are:  <pre> \"allow-all\" - Allow the client to authenticate, the result of client certificate revocation check is ignored. \"allow-unknown\" - Allow the client to authenticate even if the revocation status of his certificate cannot be determined. \"allow-valid\" - Allow the client to authenticate only when the revocation check returned an explicit positive response. </pre>  Available since 2.6.
   * @return authenticationClientCertRevocationCheckMode
  **/
  @ApiModelProperty(example = "null", value = "The desired behavior for client certificate revocation checking. The default value is `\"allow-valid\"`. The allowed values and their meaning are:  <pre> \"allow-all\" - Allow the client to authenticate, the result of client certificate revocation check is ignored. \"allow-unknown\" - Allow the client to authenticate even if the revocation status of his certificate cannot be determined. \"allow-valid\" - Allow the client to authenticate only when the revocation check returned an explicit positive response. </pre>  Available since 2.6.")
  public AuthenticationClientCertRevocationCheckModeEnum getAuthenticationClientCertRevocationCheckMode() {
    return authenticationClientCertRevocationCheckMode;
  }

  public void setAuthenticationClientCertRevocationCheckMode(AuthenticationClientCertRevocationCheckModeEnum authenticationClientCertRevocationCheckMode) {
    this.authenticationClientCertRevocationCheckMode = authenticationClientCertRevocationCheckMode;
  }

  public MsgVpn authenticationClientCertUsernameSource(AuthenticationClientCertUsernameSourceEnum authenticationClientCertUsernameSource) {
    this.authenticationClientCertUsernameSource = authenticationClientCertUsernameSource;
    return this;
  }

   /**
   * The field from the client certificate to use as the client username. The default value is `\"common-name\"`. The allowed values and their meaning are:  <pre> \"common-name\" - The username is extracted from the certificate's Common Name. \"subject-alternate-name-msupn\" - The username is extracted from the certificate's Other Name type of the Subject Alternative Name and must have the msUPN signature. </pre>  Available since 2.5.
   * @return authenticationClientCertUsernameSource
  **/
  @ApiModelProperty(example = "null", value = "The field from the client certificate to use as the client username. The default value is `\"common-name\"`. The allowed values and their meaning are:  <pre> \"common-name\" - The username is extracted from the certificate's Common Name. \"subject-alternate-name-msupn\" - The username is extracted from the certificate's Other Name type of the Subject Alternative Name and must have the msUPN signature. </pre>  Available since 2.5.")
  public AuthenticationClientCertUsernameSourceEnum getAuthenticationClientCertUsernameSource() {
    return authenticationClientCertUsernameSource;
  }

  public void setAuthenticationClientCertUsernameSource(AuthenticationClientCertUsernameSourceEnum authenticationClientCertUsernameSource) {
    this.authenticationClientCertUsernameSource = authenticationClientCertUsernameSource;
  }

  public MsgVpn authenticationClientCertValidateDateEnabled(Boolean authenticationClientCertValidateDateEnabled) {
    this.authenticationClientCertValidateDateEnabled = authenticationClientCertValidateDateEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the client certificate. The default value is `true`.
   * @return authenticationClientCertValidateDateEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the client certificate. The default value is `true`.")
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
   * Enable or disable allowing a client to specify a Client Username via the API connect method. When disabled, the Kerberos Principal name is always used. The default value is `false`.
   * @return authenticationKerberosAllowApiProvidedUsernameEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing a client to specify a Client Username via the API connect method. When disabled, the Kerberos Principal name is always used. The default value is `false`.")
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
   * Enable or disable Kerberos authentication in the Message VPN. The default value is `false`.
   * @return authenticationKerberosEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable Kerberos authentication in the Message VPN. The default value is `false`.")
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
   * The name of the attribute that is retrieved from the LDAP server as part of the LDAP search when authorizing a client connecting to the Message VPN. The default value is `\"memberOf\"`.
   * @return authorizationLdapGroupMembershipAttributeName
  **/
  @ApiModelProperty(example = "null", value = "The name of the attribute that is retrieved from the LDAP server as part of the LDAP search when authorizing a client connecting to the Message VPN. The default value is `\"memberOf\"`.")
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
   * The name of the LDAP Profile to use for client authorization. The default value is `\"\"`.
   * @return authorizationProfileName
  **/
  @ApiModelProperty(example = "null", value = "The name of the LDAP Profile to use for client authorization. The default value is `\"\"`.")
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
   * The type of authorization to use for clients connecting to the Message VPN. The default value is `\"internal\"`. The allowed values and their meaning are:  <pre> \"ldap\" - LDAP authorization. \"internal\" - Internal authorization. </pre> 
   * @return authorizationType
  **/
  @ApiModelProperty(example = "null", value = "The type of authorization to use for clients connecting to the Message VPN. The default value is `\"internal\"`. The allowed values and their meaning are:  <pre> \"ldap\" - LDAP authorization. \"internal\" - Internal authorization. </pre> ")
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
   * Enable or disable validation of the Common Name (CN) in the server certificate from the remote broker. If enabled, the Common Name is checked against the list of Trusted Common Names configured for the Bridge. The default value is `true`.
   * @return bridgingTlsServerCertEnforceTrustedCommonNameEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable validation of the Common Name (CN) in the server certificate from the remote broker. If enabled, the Common Name is checked against the list of Trusted Common Names configured for the Bridge. The default value is `true`.")
  public Boolean getBridgingTlsServerCertEnforceTrustedCommonNameEnabled() {
    return bridgingTlsServerCertEnforceTrustedCommonNameEnabled;
  }

  public void setBridgingTlsServerCertEnforceTrustedCommonNameEnabled(Boolean bridgingTlsServerCertEnforceTrustedCommonNameEnabled) {
    this.bridgingTlsServerCertEnforceTrustedCommonNameEnabled = bridgingTlsServerCertEnforceTrustedCommonNameEnabled;
  }

  public MsgVpn bridgingTlsServerCertMaxChainDepth(Long bridgingTlsServerCertMaxChainDepth) {
    this.bridgingTlsServerCertMaxChainDepth = bridgingTlsServerCertMaxChainDepth;
    return this;
  }

   /**
   * The maximum depth for a server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.
   * @return bridgingTlsServerCertMaxChainDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth for a server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.")
  public Long getBridgingTlsServerCertMaxChainDepth() {
    return bridgingTlsServerCertMaxChainDepth;
  }

  public void setBridgingTlsServerCertMaxChainDepth(Long bridgingTlsServerCertMaxChainDepth) {
    this.bridgingTlsServerCertMaxChainDepth = bridgingTlsServerCertMaxChainDepth;
  }

  public MsgVpn bridgingTlsServerCertValidateDateEnabled(Boolean bridgingTlsServerCertValidateDateEnabled) {
    this.bridgingTlsServerCertValidateDateEnabled = bridgingTlsServerCertValidateDateEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid based on these dates. The default value is `true`.
   * @return bridgingTlsServerCertValidateDateEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid based on these dates. The default value is `true`.")
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
   * Enable or disable managing of cache instances over the message bus. The default value is `true`.
   * @return distributedCacheManagementEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable managing of cache instances over the message bus. The default value is `true`.")
  public Boolean getDistributedCacheManagementEnabled() {
    return distributedCacheManagementEnabled;
  }

  public void setDistributedCacheManagementEnabled(Boolean distributedCacheManagementEnabled) {
    this.distributedCacheManagementEnabled = distributedCacheManagementEnabled;
  }

  public MsgVpn dmrEnabled(Boolean dmrEnabled) {
    this.dmrEnabled = dmrEnabled;
    return this;
  }

   /**
   * Enable or disable Dynamic Message Routing (DMR) for the Message VPN. The default value is `false`. Available since 2.11.
   * @return dmrEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable Dynamic Message Routing (DMR) for the Message VPN. The default value is `false`. Available since 2.11.")
  public Boolean getDmrEnabled() {
    return dmrEnabled;
  }

  public void setDmrEnabled(Boolean dmrEnabled) {
    this.dmrEnabled = dmrEnabled;
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
   * Get eventConnectionCountThreshold
   * @return eventConnectionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Get eventEgressFlowCountThreshold
   * @return eventEgressFlowCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Get eventEgressMsgRateThreshold
   * @return eventEgressMsgRateThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Get eventEndpointCountThreshold
   * @return eventEndpointCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Get eventIngressFlowCountThreshold
   * @return eventIngressFlowCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Get eventIngressMsgRateThreshold
   * @return eventIngressMsgRateThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThresholdByValue getEventIngressMsgRateThreshold() {
    return eventIngressMsgRateThreshold;
  }

  public void setEventIngressMsgRateThreshold(EventThresholdByValue eventIngressMsgRateThreshold) {
    this.eventIngressMsgRateThreshold = eventIngressMsgRateThreshold;
  }

  public MsgVpn eventLargeMsgThreshold(Long eventLargeMsgThreshold) {
    this.eventLargeMsgThreshold = eventLargeMsgThreshold;
    return this;
  }

   /**
   * The threshold, in kilobytes, after which a message is considered to be large for the Message VPN. The default value is `1024`.
   * @return eventLargeMsgThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold, in kilobytes, after which a message is considered to be large for the Message VPN. The default value is `1024`.")
  public Long getEventLargeMsgThreshold() {
    return eventLargeMsgThreshold;
  }

  public void setEventLargeMsgThreshold(Long eventLargeMsgThreshold) {
    this.eventLargeMsgThreshold = eventLargeMsgThreshold;
  }

  public MsgVpn eventLogTag(String eventLogTag) {
    this.eventLogTag = eventLogTag;
    return this;
  }

   /**
   * A prefix applied to all published Events in the Message VPN. The default value is `\"\"`.
   * @return eventLogTag
  **/
  @ApiModelProperty(example = "null", value = "A prefix applied to all published Events in the Message VPN. The default value is `\"\"`.")
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
   * Get eventMsgSpoolUsageThreshold
   * @return eventMsgSpoolUsageThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Enable or disable Client level Event message publishing. The default value is `false`.
   * @return eventPublishClientEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable Client level Event message publishing. The default value is `false`.")
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
   * Enable or disable Message VPN level Event message publishing. The default value is `false`.
   * @return eventPublishMsgVpnEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable Message VPN level Event message publishing. The default value is `false`.")
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
   * Subscription level Event message publishing mode. The default value is `\"off\"`. The allowed values and their meaning are:  <pre> \"off\" - Disable client level event message publishing. \"on-with-format-v1\" - Enable client level event message publishing with format v1. \"on-with-no-unsubscribe-events-on-disconnect-format-v1\" - As \"on-with-format-v1\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. \"on-with-format-v2\" - Enable client level event message publishing with format v2. \"on-with-no-unsubscribe-events-on-disconnect-format-v2\" - As \"on-with-format-v2\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. </pre> 
   * @return eventPublishSubscriptionMode
  **/
  @ApiModelProperty(example = "null", value = "Subscription level Event message publishing mode. The default value is `\"off\"`. The allowed values and their meaning are:  <pre> \"off\" - Disable client level event message publishing. \"on-with-format-v1\" - Enable client level event message publishing with format v1. \"on-with-no-unsubscribe-events-on-disconnect-format-v1\" - As \"on-with-format-v1\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. \"on-with-format-v2\" - Enable client level event message publishing with format v2. \"on-with-no-unsubscribe-events-on-disconnect-format-v2\" - As \"on-with-format-v2\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. </pre> ")
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
   * Enable or disable Event publish topics in MQTT format. The default value is `false`.
   * @return eventPublishTopicFormatMqttEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable Event publish topics in MQTT format. The default value is `false`.")
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
   * Enable or disable Event publish topics in SMF format. The default value is `true`.
   * @return eventPublishTopicFormatSmfEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable Event publish topics in SMF format. The default value is `true`.")
  public Boolean getEventPublishTopicFormatSmfEnabled() {
    return eventPublishTopicFormatSmfEnabled;
  }

  public void setEventPublishTopicFormatSmfEnabled(Boolean eventPublishTopicFormatSmfEnabled) {
    this.eventPublishTopicFormatSmfEnabled = eventPublishTopicFormatSmfEnabled;
  }

  public MsgVpn eventServiceAmqpConnectionCountThreshold(EventThreshold eventServiceAmqpConnectionCountThreshold) {
    this.eventServiceAmqpConnectionCountThreshold = eventServiceAmqpConnectionCountThreshold;
    return this;
  }

   /**
   * Get eventServiceAmqpConnectionCountThreshold
   * @return eventServiceAmqpConnectionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThreshold getEventServiceAmqpConnectionCountThreshold() {
    return eventServiceAmqpConnectionCountThreshold;
  }

  public void setEventServiceAmqpConnectionCountThreshold(EventThreshold eventServiceAmqpConnectionCountThreshold) {
    this.eventServiceAmqpConnectionCountThreshold = eventServiceAmqpConnectionCountThreshold;
  }

  public MsgVpn eventServiceMqttConnectionCountThreshold(EventThreshold eventServiceMqttConnectionCountThreshold) {
    this.eventServiceMqttConnectionCountThreshold = eventServiceMqttConnectionCountThreshold;
    return this;
  }

   /**
   * Get eventServiceMqttConnectionCountThreshold
   * @return eventServiceMqttConnectionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThreshold getEventServiceMqttConnectionCountThreshold() {
    return eventServiceMqttConnectionCountThreshold;
  }

  public void setEventServiceMqttConnectionCountThreshold(EventThreshold eventServiceMqttConnectionCountThreshold) {
    this.eventServiceMqttConnectionCountThreshold = eventServiceMqttConnectionCountThreshold;
  }

  public MsgVpn eventServiceRestIncomingConnectionCountThreshold(EventThreshold eventServiceRestIncomingConnectionCountThreshold) {
    this.eventServiceRestIncomingConnectionCountThreshold = eventServiceRestIncomingConnectionCountThreshold;
    return this;
  }

   /**
   * Get eventServiceRestIncomingConnectionCountThreshold
   * @return eventServiceRestIncomingConnectionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Get eventServiceSmfConnectionCountThreshold
   * @return eventServiceSmfConnectionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Get eventServiceWebConnectionCountThreshold
   * @return eventServiceWebConnectionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Get eventSubscriptionCountThreshold
   * @return eventSubscriptionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Get eventTransactedSessionCountThreshold
   * @return eventTransactedSessionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Get eventTransactionCountThreshold
   * @return eventTransactionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
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
   * Enable or disable the export of subscriptions in the Message VPN to other routers in the network over Neighbor links. The default value is `false`.
   * @return exportSubscriptionsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the export of subscriptions in the Message VPN to other routers in the network over Neighbor links. The default value is `false`.")
  public Boolean getExportSubscriptionsEnabled() {
    return exportSubscriptionsEnabled;
  }

  public void setExportSubscriptionsEnabled(Boolean exportSubscriptionsEnabled) {
    this.exportSubscriptionsEnabled = exportSubscriptionsEnabled;
  }

  public MsgVpn jndiEnabled(Boolean jndiEnabled) {
    this.jndiEnabled = jndiEnabled;
    return this;
  }

   /**
   * Enable or disable JNDI access for clients in the Message VPN. The default value is `false`. Available since 2.2.
   * @return jndiEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable JNDI access for clients in the Message VPN. The default value is `false`. Available since 2.2.")
  public Boolean getJndiEnabled() {
    return jndiEnabled;
  }

  public void setJndiEnabled(Boolean jndiEnabled) {
    this.jndiEnabled = jndiEnabled;
  }

  public MsgVpn maxConnectionCount(Long maxConnectionCount) {
    this.maxConnectionCount = maxConnectionCount;
    return this;
  }

   /**
   * The maximum number of client connections to the Message VPN. The default is the max value supported by the platform.
   * @return maxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of client connections to the Message VPN. The default is the max value supported by the platform.")
  public Long getMaxConnectionCount() {
    return maxConnectionCount;
  }

  public void setMaxConnectionCount(Long maxConnectionCount) {
    this.maxConnectionCount = maxConnectionCount;
  }

  public MsgVpn maxEgressFlowCount(Long maxEgressFlowCount) {
    this.maxEgressFlowCount = maxEgressFlowCount;
    return this;
  }

   /**
   * The maximum number of transmit flows that can be created in the Message VPN. The default value is `1000`.
   * @return maxEgressFlowCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of transmit flows that can be created in the Message VPN. The default value is `1000`.")
  public Long getMaxEgressFlowCount() {
    return maxEgressFlowCount;
  }

  public void setMaxEgressFlowCount(Long maxEgressFlowCount) {
    this.maxEgressFlowCount = maxEgressFlowCount;
  }

  public MsgVpn maxEndpointCount(Long maxEndpointCount) {
    this.maxEndpointCount = maxEndpointCount;
    return this;
  }

   /**
   * The maximum number of Queues and Topic Endpoints that can be created in the Message VPN. The default value is `1000`.
   * @return maxEndpointCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of Queues and Topic Endpoints that can be created in the Message VPN. The default value is `1000`.")
  public Long getMaxEndpointCount() {
    return maxEndpointCount;
  }

  public void setMaxEndpointCount(Long maxEndpointCount) {
    this.maxEndpointCount = maxEndpointCount;
  }

  public MsgVpn maxIngressFlowCount(Long maxIngressFlowCount) {
    this.maxIngressFlowCount = maxIngressFlowCount;
    return this;
  }

   /**
   * The maximum number of receive flows that can be created in the Message VPN. The default value is `1000`.
   * @return maxIngressFlowCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of receive flows that can be created in the Message VPN. The default value is `1000`.")
  public Long getMaxIngressFlowCount() {
    return maxIngressFlowCount;
  }

  public void setMaxIngressFlowCount(Long maxIngressFlowCount) {
    this.maxIngressFlowCount = maxIngressFlowCount;
  }

  public MsgVpn maxMsgSpoolUsage(Long maxMsgSpoolUsage) {
    this.maxMsgSpoolUsage = maxMsgSpoolUsage;
    return this;
  }

   /**
   * The maximum message spool usage by the Message VPN, in megabytes. The default value is `0`.
   * @return maxMsgSpoolUsage
  **/
  @ApiModelProperty(example = "null", value = "The maximum message spool usage by the Message VPN, in megabytes. The default value is `0`.")
  public Long getMaxMsgSpoolUsage() {
    return maxMsgSpoolUsage;
  }

  public void setMaxMsgSpoolUsage(Long maxMsgSpoolUsage) {
    this.maxMsgSpoolUsage = maxMsgSpoolUsage;
  }

  public MsgVpn maxSubscriptionCount(Long maxSubscriptionCount) {
    this.maxSubscriptionCount = maxSubscriptionCount;
    return this;
  }

   /**
   * The maximum number of local client subscriptions (both primary and backup) that can be added to the Message VPN. The default varies by platform.
   * @return maxSubscriptionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of local client subscriptions (both primary and backup) that can be added to the Message VPN. The default varies by platform.")
  public Long getMaxSubscriptionCount() {
    return maxSubscriptionCount;
  }

  public void setMaxSubscriptionCount(Long maxSubscriptionCount) {
    this.maxSubscriptionCount = maxSubscriptionCount;
  }

  public MsgVpn maxTransactedSessionCount(Long maxTransactedSessionCount) {
    this.maxTransactedSessionCount = maxTransactedSessionCount;
    return this;
  }

   /**
   * The maximum number of transacted sessions that can be created in the Message VPN. The default varies by platform.
   * @return maxTransactedSessionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of transacted sessions that can be created in the Message VPN. The default varies by platform.")
  public Long getMaxTransactedSessionCount() {
    return maxTransactedSessionCount;
  }

  public void setMaxTransactedSessionCount(Long maxTransactedSessionCount) {
    this.maxTransactedSessionCount = maxTransactedSessionCount;
  }

  public MsgVpn maxTransactionCount(Long maxTransactionCount) {
    this.maxTransactionCount = maxTransactionCount;
    return this;
  }

   /**
   * The maximum number of transactions that can be created in the Message VPN. The default varies by platform.
   * @return maxTransactionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of transactions that can be created in the Message VPN. The default varies by platform.")
  public Long getMaxTransactionCount() {
    return maxTransactionCount;
  }

  public void setMaxTransactionCount(Long maxTransactionCount) {
    this.maxTransactionCount = maxTransactionCount;
  }

  public MsgVpn mqttRetainMaxMemory(Integer mqttRetainMaxMemory) {
    this.mqttRetainMaxMemory = mqttRetainMaxMemory;
    return this;
  }

   /**
   * The maximum total memory usage of the MQTT Retain feature for this Message VPN, in MB. If the maximum memory is reached, any arriving retain messages that require more memory are discarded.  A value of -1 indicates that the memory is bounded only by the global max memory limit. A value of 0 prevents MQTT Retain from becoming operational. The default value is `-1`. Available since 2.11.
   * @return mqttRetainMaxMemory
  **/
  @ApiModelProperty(example = "null", value = "The maximum total memory usage of the MQTT Retain feature for this Message VPN, in MB. If the maximum memory is reached, any arriving retain messages that require more memory are discarded.  A value of -1 indicates that the memory is bounded only by the global max memory limit. A value of 0 prevents MQTT Retain from becoming operational. The default value is `-1`. Available since 2.11.")
  public Integer getMqttRetainMaxMemory() {
    return mqttRetainMaxMemory;
  }

  public void setMqttRetainMaxMemory(Integer mqttRetainMaxMemory) {
    this.mqttRetainMaxMemory = mqttRetainMaxMemory;
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

  public MsgVpn replicationAckPropagationIntervalMsgCount(Long replicationAckPropagationIntervalMsgCount) {
    this.replicationAckPropagationIntervalMsgCount = replicationAckPropagationIntervalMsgCount;
    return this;
  }

   /**
   * The acknowledgement (ACK) propagation interval for the replication Bridge, in number of replicated messages. The default value is `20`.
   * @return replicationAckPropagationIntervalMsgCount
  **/
  @ApiModelProperty(example = "null", value = "The acknowledgement (ACK) propagation interval for the replication Bridge, in number of replicated messages. The default value is `20`.")
  public Long getReplicationAckPropagationIntervalMsgCount() {
    return replicationAckPropagationIntervalMsgCount;
  }

  public void setReplicationAckPropagationIntervalMsgCount(Long replicationAckPropagationIntervalMsgCount) {
    this.replicationAckPropagationIntervalMsgCount = replicationAckPropagationIntervalMsgCount;
  }

  public MsgVpn replicationBridgeAuthenticationBasicClientUsername(String replicationBridgeAuthenticationBasicClientUsername) {
    this.replicationBridgeAuthenticationBasicClientUsername = replicationBridgeAuthenticationBasicClientUsername;
    return this;
  }

   /**
   * The Client Username the replication Bridge uses to login to the remote Message VPN. The default value is `\"\"`.
   * @return replicationBridgeAuthenticationBasicClientUsername
  **/
  @ApiModelProperty(example = "null", value = "The Client Username the replication Bridge uses to login to the remote Message VPN. The default value is `\"\"`.")
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
   * The password for the Client Username. The default is to have no `replicationBridgeAuthenticationBasicPassword`.
   * @return replicationBridgeAuthenticationBasicPassword
  **/
  @ApiModelProperty(example = "null", value = "The password for the Client Username. The default is to have no `replicationBridgeAuthenticationBasicPassword`.")
  public String getReplicationBridgeAuthenticationBasicPassword() {
    return replicationBridgeAuthenticationBasicPassword;
  }

  public void setReplicationBridgeAuthenticationBasicPassword(String replicationBridgeAuthenticationBasicPassword) {
    this.replicationBridgeAuthenticationBasicPassword = replicationBridgeAuthenticationBasicPassword;
  }

  public MsgVpn replicationBridgeAuthenticationClientCertContent(String replicationBridgeAuthenticationClientCertContent) {
    this.replicationBridgeAuthenticationClientCertContent = replicationBridgeAuthenticationClientCertContent;
    return this;
  }

   /**
   * The PEM formatted content for the client certificate used by this bridge to login to the Remote Message VPN. It must consist of a private key and between one and three certificates comprising the certificate trust chain. Changing this attribute requires an HTTPS connection. The default value is `\"\"`. Available since 2.9.
   * @return replicationBridgeAuthenticationClientCertContent
  **/
  @ApiModelProperty(example = "null", value = "The PEM formatted content for the client certificate used by this bridge to login to the Remote Message VPN. It must consist of a private key and between one and three certificates comprising the certificate trust chain. Changing this attribute requires an HTTPS connection. The default value is `\"\"`. Available since 2.9.")
  public String getReplicationBridgeAuthenticationClientCertContent() {
    return replicationBridgeAuthenticationClientCertContent;
  }

  public void setReplicationBridgeAuthenticationClientCertContent(String replicationBridgeAuthenticationClientCertContent) {
    this.replicationBridgeAuthenticationClientCertContent = replicationBridgeAuthenticationClientCertContent;
  }

  public MsgVpn replicationBridgeAuthenticationClientCertPassword(String replicationBridgeAuthenticationClientCertPassword) {
    this.replicationBridgeAuthenticationClientCertPassword = replicationBridgeAuthenticationClientCertPassword;
    return this;
  }

   /**
   * The password for the client certificate. Changing this attribute requires an HTTPS connection. The default value is `\"\"`. Available since 2.9.
   * @return replicationBridgeAuthenticationClientCertPassword
  **/
  @ApiModelProperty(example = "null", value = "The password for the client certificate. Changing this attribute requires an HTTPS connection. The default value is `\"\"`. Available since 2.9.")
  public String getReplicationBridgeAuthenticationClientCertPassword() {
    return replicationBridgeAuthenticationClientCertPassword;
  }

  public void setReplicationBridgeAuthenticationClientCertPassword(String replicationBridgeAuthenticationClientCertPassword) {
    this.replicationBridgeAuthenticationClientCertPassword = replicationBridgeAuthenticationClientCertPassword;
  }

  public MsgVpn replicationBridgeAuthenticationScheme(ReplicationBridgeAuthenticationSchemeEnum replicationBridgeAuthenticationScheme) {
    this.replicationBridgeAuthenticationScheme = replicationBridgeAuthenticationScheme;
    return this;
  }

   /**
   * The authentication scheme for the replication Bridge in the Message VPN. The default value is `\"basic\"`. The allowed values and their meaning are:  <pre> \"basic\" - Basic Authentication Scheme (via username and password). \"client-certificate\" - Client Certificate Authentication Scheme (via certificate file or content). </pre> 
   * @return replicationBridgeAuthenticationScheme
  **/
  @ApiModelProperty(example = "null", value = "The authentication scheme for the replication Bridge in the Message VPN. The default value is `\"basic\"`. The allowed values and their meaning are:  <pre> \"basic\" - Basic Authentication Scheme (via username and password). \"client-certificate\" - Client Certificate Authentication Scheme (via certificate file or content). </pre> ")
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
   * Enable or disable use of compression for the replication Bridge. The default value is `false`.
   * @return replicationBridgeCompressedDataEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable use of compression for the replication Bridge. The default value is `false`.")
  public Boolean getReplicationBridgeCompressedDataEnabled() {
    return replicationBridgeCompressedDataEnabled;
  }

  public void setReplicationBridgeCompressedDataEnabled(Boolean replicationBridgeCompressedDataEnabled) {
    this.replicationBridgeCompressedDataEnabled = replicationBridgeCompressedDataEnabled;
  }

  public MsgVpn replicationBridgeEgressFlowWindowSize(Long replicationBridgeEgressFlowWindowSize) {
    this.replicationBridgeEgressFlowWindowSize = replicationBridgeEgressFlowWindowSize;
    return this;
  }

   /**
   * The size of the window used for guaranteed messages published to the replication Bridge, in messages. The default value is `255`.
   * @return replicationBridgeEgressFlowWindowSize
  **/
  @ApiModelProperty(example = "null", value = "The size of the window used for guaranteed messages published to the replication Bridge, in messages. The default value is `255`.")
  public Long getReplicationBridgeEgressFlowWindowSize() {
    return replicationBridgeEgressFlowWindowSize;
  }

  public void setReplicationBridgeEgressFlowWindowSize(Long replicationBridgeEgressFlowWindowSize) {
    this.replicationBridgeEgressFlowWindowSize = replicationBridgeEgressFlowWindowSize;
  }

  public MsgVpn replicationBridgeRetryDelay(Long replicationBridgeRetryDelay) {
    this.replicationBridgeRetryDelay = replicationBridgeRetryDelay;
    return this;
  }

   /**
   * The number of seconds that must pass before retrying the replication Bridge connection. The default value is `3`.
   * @return replicationBridgeRetryDelay
  **/
  @ApiModelProperty(example = "null", value = "The number of seconds that must pass before retrying the replication Bridge connection. The default value is `3`.")
  public Long getReplicationBridgeRetryDelay() {
    return replicationBridgeRetryDelay;
  }

  public void setReplicationBridgeRetryDelay(Long replicationBridgeRetryDelay) {
    this.replicationBridgeRetryDelay = replicationBridgeRetryDelay;
  }

  public MsgVpn replicationBridgeTlsEnabled(Boolean replicationBridgeTlsEnabled) {
    this.replicationBridgeTlsEnabled = replicationBridgeTlsEnabled;
    return this;
  }

   /**
   * Enable or disable use of TLS for the replication Bridge connection. The default value is `false`.
   * @return replicationBridgeTlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable use of TLS for the replication Bridge connection. The default value is `false`.")
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
   * The Client Profile for the unidirectional replication Bridge in the Message VPN. It is used only for the TCP parameters. The default value is `\"#client-profile\"`.
   * @return replicationBridgeUnidirectionalClientProfileName
  **/
  @ApiModelProperty(example = "null", value = "The Client Profile for the unidirectional replication Bridge in the Message VPN. It is used only for the TCP parameters. The default value is `\"#client-profile\"`.")
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
   * Enable or disable replication for the Message VPN. The default value is `false`.
   * @return replicationEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable replication for the Message VPN. The default value is `false`.")
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
   * The behavior to take when enabling replication for the Message VPN, depending on the existence of the replication Queue. The default value is `\"fail-on-existing-queue\"`. The allowed values and their meaning are:  <pre> \"fail-on-existing-queue\" - The data replication queue must not already exist. \"force-use-existing-queue\" - The data replication queue must already exist. Any data messages on the Queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail. \"force-recreate-queue\" - The data replication queue must already exist. Any data messages on the Queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. </pre> 
   * @return replicationEnabledQueueBehavior
  **/
  @ApiModelProperty(example = "null", value = "The behavior to take when enabling replication for the Message VPN, depending on the existence of the replication Queue. The default value is `\"fail-on-existing-queue\"`. The allowed values and their meaning are:  <pre> \"fail-on-existing-queue\" - The data replication queue must not already exist. \"force-use-existing-queue\" - The data replication queue must already exist. Any data messages on the Queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail. \"force-recreate-queue\" - The data replication queue must already exist. Any data messages on the Queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. </pre> ")
  public ReplicationEnabledQueueBehaviorEnum getReplicationEnabledQueueBehavior() {
    return replicationEnabledQueueBehavior;
  }

  public void setReplicationEnabledQueueBehavior(ReplicationEnabledQueueBehaviorEnum replicationEnabledQueueBehavior) {
    this.replicationEnabledQueueBehavior = replicationEnabledQueueBehavior;
  }

  public MsgVpn replicationQueueMaxMsgSpoolUsage(Long replicationQueueMaxMsgSpoolUsage) {
    this.replicationQueueMaxMsgSpoolUsage = replicationQueueMaxMsgSpoolUsage;
    return this;
  }

   /**
   * The maximum message spool usage by the replication Bridge local Queue (quota), in megabytes. The default value is `60000`.
   * @return replicationQueueMaxMsgSpoolUsage
  **/
  @ApiModelProperty(example = "null", value = "The maximum message spool usage by the replication Bridge local Queue (quota), in megabytes. The default value is `60000`.")
  public Long getReplicationQueueMaxMsgSpoolUsage() {
    return replicationQueueMaxMsgSpoolUsage;
  }

  public void setReplicationQueueMaxMsgSpoolUsage(Long replicationQueueMaxMsgSpoolUsage) {
    this.replicationQueueMaxMsgSpoolUsage = replicationQueueMaxMsgSpoolUsage;
  }

  public MsgVpn replicationQueueRejectMsgToSenderOnDiscardEnabled(Boolean replicationQueueRejectMsgToSenderOnDiscardEnabled) {
    this.replicationQueueRejectMsgToSenderOnDiscardEnabled = replicationQueueRejectMsgToSenderOnDiscardEnabled;
    return this;
  }

   /**
   * Enable or disable whether messages discarded on the replication Bridge local Queue are rejected back to the sender. The default value is `true`.
   * @return replicationQueueRejectMsgToSenderOnDiscardEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable whether messages discarded on the replication Bridge local Queue are rejected back to the sender. The default value is `true`.")
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
   * Enable or disable whether guaranteed messages published to synchronously replicated Topics are rejected back to the sender when synchronous replication becomes ineligible. The default value is `false`.
   * @return replicationRejectMsgWhenSyncIneligibleEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable whether guaranteed messages published to synchronously replicated Topics are rejected back to the sender when synchronous replication becomes ineligible. The default value is `false`.")
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
   * The replication role for the Message VPN. The default value is `\"standby\"`. The allowed values and their meaning are:  <pre> \"active\" - Assume the Active role in replication for the Message VPN. \"standby\" - Assume the Standby role in replication for the Message VPN. </pre> 
   * @return replicationRole
  **/
  @ApiModelProperty(example = "null", value = "The replication role for the Message VPN. The default value is `\"standby\"`. The allowed values and their meaning are:  <pre> \"active\" - Assume the Active role in replication for the Message VPN. \"standby\" - Assume the Standby role in replication for the Message VPN. </pre> ")
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
   * The transaction replication mode for all transactions within the Message VPN. Changing this value during operation will not affect existing transactions; it is only used upon starting a transaction. The default value is `\"async\"`. The allowed values and their meaning are:  <pre> \"sync\" - Messages are acknowledged when replicated (spooled remotely). \"async\" - Messages are acknowledged when pending replication (spooled locally). </pre> 
   * @return replicationTransactionMode
  **/
  @ApiModelProperty(example = "null", value = "The transaction replication mode for all transactions within the Message VPN. Changing this value during operation will not affect existing transactions; it is only used upon starting a transaction. The default value is `\"async\"`. The allowed values and their meaning are:  <pre> \"sync\" - Messages are acknowledged when replicated (spooled remotely). \"async\" - Messages are acknowledged when pending replication (spooled locally). </pre> ")
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
   * Enable or disable validation of the Common Name (CN) in the server certificate from the remote REST Consumer. If enabled, the Common Name is checked against the list of Trusted Common Names configured for the REST Consumer. The default value is `true`.
   * @return restTlsServerCertEnforceTrustedCommonNameEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable validation of the Common Name (CN) in the server certificate from the remote REST Consumer. If enabled, the Common Name is checked against the list of Trusted Common Names configured for the REST Consumer. The default value is `true`.")
  public Boolean getRestTlsServerCertEnforceTrustedCommonNameEnabled() {
    return restTlsServerCertEnforceTrustedCommonNameEnabled;
  }

  public void setRestTlsServerCertEnforceTrustedCommonNameEnabled(Boolean restTlsServerCertEnforceTrustedCommonNameEnabled) {
    this.restTlsServerCertEnforceTrustedCommonNameEnabled = restTlsServerCertEnforceTrustedCommonNameEnabled;
  }

  public MsgVpn restTlsServerCertMaxChainDepth(Long restTlsServerCertMaxChainDepth) {
    this.restTlsServerCertMaxChainDepth = restTlsServerCertMaxChainDepth;
    return this;
  }

   /**
   * The maximum depth for a REST Consumer server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.
   * @return restTlsServerCertMaxChainDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth for a REST Consumer server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.")
  public Long getRestTlsServerCertMaxChainDepth() {
    return restTlsServerCertMaxChainDepth;
  }

  public void setRestTlsServerCertMaxChainDepth(Long restTlsServerCertMaxChainDepth) {
    this.restTlsServerCertMaxChainDepth = restTlsServerCertMaxChainDepth;
  }

  public MsgVpn restTlsServerCertValidateDateEnabled(Boolean restTlsServerCertValidateDateEnabled) {
    this.restTlsServerCertValidateDateEnabled = restTlsServerCertValidateDateEnabled;
    return this;
  }

   /**
   * Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the REST Consumer server certificate. The default value is `true`.
   * @return restTlsServerCertValidateDateEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the REST Consumer server certificate. The default value is `true`.")
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
   * Enable or disable \"admin client\" SEMP over the message bus commands for the current Message VPN. The default value is `false`.
   * @return sempOverMsgBusAdminClientEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable \"admin client\" SEMP over the message bus commands for the current Message VPN. The default value is `false`.")
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
   * Enable or disable \"admin distributed-cache\" SEMP over the message bus commands for the current Message VPN. The default value is `false`.
   * @return sempOverMsgBusAdminDistributedCacheEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable \"admin distributed-cache\" SEMP over the message bus commands for the current Message VPN. The default value is `false`.")
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
   * Enable or disable \"admin\" SEMP over the message bus commands for the current Message VPN. The default value is `false`.
   * @return sempOverMsgBusAdminEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable \"admin\" SEMP over the message bus commands for the current Message VPN. The default value is `false`.")
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
   * Enable or disable SEMP over the message bus for the current Message VPN. The default value is `true`.
   * @return sempOverMsgBusEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable SEMP over the message bus for the current Message VPN. The default value is `true`.")
  public Boolean getSempOverMsgBusEnabled() {
    return sempOverMsgBusEnabled;
  }

  public void setSempOverMsgBusEnabled(Boolean sempOverMsgBusEnabled) {
    this.sempOverMsgBusEnabled = sempOverMsgBusEnabled;
  }

  public MsgVpn sempOverMsgBusShowEnabled(Boolean sempOverMsgBusShowEnabled) {
    this.sempOverMsgBusShowEnabled = sempOverMsgBusShowEnabled;
    return this;
  }

   /**
   * Enable or disable \"show\" SEMP over the message bus commands for the current Message VPN. The default value is `false`.
   * @return sempOverMsgBusShowEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable \"show\" SEMP over the message bus commands for the current Message VPN. The default value is `false`.")
  public Boolean getSempOverMsgBusShowEnabled() {
    return sempOverMsgBusShowEnabled;
  }

  public void setSempOverMsgBusShowEnabled(Boolean sempOverMsgBusShowEnabled) {
    this.sempOverMsgBusShowEnabled = sempOverMsgBusShowEnabled;
  }

  public MsgVpn serviceAmqpMaxConnectionCount(Long serviceAmqpMaxConnectionCount) {
    this.serviceAmqpMaxConnectionCount = serviceAmqpMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of AMQP client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform. Available since 2.2.
   * @return serviceAmqpMaxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of AMQP client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform. Available since 2.2.")
  public Long getServiceAmqpMaxConnectionCount() {
    return serviceAmqpMaxConnectionCount;
  }

  public void setServiceAmqpMaxConnectionCount(Long serviceAmqpMaxConnectionCount) {
    this.serviceAmqpMaxConnectionCount = serviceAmqpMaxConnectionCount;
  }

  public MsgVpn serviceAmqpPlainTextEnabled(Boolean serviceAmqpPlainTextEnabled) {
    this.serviceAmqpPlainTextEnabled = serviceAmqpPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the plain-text AMQP service in the Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. The default value is `false`. Available since 2.2.
   * @return serviceAmqpPlainTextEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the plain-text AMQP service in the Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. The default value is `false`. Available since 2.2.")
  public Boolean getServiceAmqpPlainTextEnabled() {
    return serviceAmqpPlainTextEnabled;
  }

  public void setServiceAmqpPlainTextEnabled(Boolean serviceAmqpPlainTextEnabled) {
    this.serviceAmqpPlainTextEnabled = serviceAmqpPlainTextEnabled;
  }

  public MsgVpn serviceAmqpPlainTextListenPort(Long serviceAmqpPlainTextListenPort) {
    this.serviceAmqpPlainTextListenPort = serviceAmqpPlainTextListenPort;
    return this;
  }

   /**
   * The port number for plain-text AMQP clients that connect to the Message VPN. The default is to have no `serviceAmqpPlainTextListenPort`. Available since 2.2.
   * @return serviceAmqpPlainTextListenPort
  **/
  @ApiModelProperty(example = "null", value = "The port number for plain-text AMQP clients that connect to the Message VPN. The default is to have no `serviceAmqpPlainTextListenPort`. Available since 2.2.")
  public Long getServiceAmqpPlainTextListenPort() {
    return serviceAmqpPlainTextListenPort;
  }

  public void setServiceAmqpPlainTextListenPort(Long serviceAmqpPlainTextListenPort) {
    this.serviceAmqpPlainTextListenPort = serviceAmqpPlainTextListenPort;
  }

  public MsgVpn serviceAmqpTlsEnabled(Boolean serviceAmqpTlsEnabled) {
    this.serviceAmqpTlsEnabled = serviceAmqpTlsEnabled;
    return this;
  }

   /**
   * Enable or disable the use of TLS for the AMQP service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is `false`. Available since 2.2.
   * @return serviceAmqpTlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the use of TLS for the AMQP service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is `false`. Available since 2.2.")
  public Boolean getServiceAmqpTlsEnabled() {
    return serviceAmqpTlsEnabled;
  }

  public void setServiceAmqpTlsEnabled(Boolean serviceAmqpTlsEnabled) {
    this.serviceAmqpTlsEnabled = serviceAmqpTlsEnabled;
  }

  public MsgVpn serviceAmqpTlsListenPort(Long serviceAmqpTlsListenPort) {
    this.serviceAmqpTlsListenPort = serviceAmqpTlsListenPort;
    return this;
  }

   /**
   * The port number for AMQP clients that connect to the Message VPN over TLS. The default is to have no `serviceAmqpTlsListenPort`. Available since 2.2.
   * @return serviceAmqpTlsListenPort
  **/
  @ApiModelProperty(example = "null", value = "The port number for AMQP clients that connect to the Message VPN over TLS. The default is to have no `serviceAmqpTlsListenPort`. Available since 2.2.")
  public Long getServiceAmqpTlsListenPort() {
    return serviceAmqpTlsListenPort;
  }

  public void setServiceAmqpTlsListenPort(Long serviceAmqpTlsListenPort) {
    this.serviceAmqpTlsListenPort = serviceAmqpTlsListenPort;
  }

  public MsgVpn serviceMqttMaxConnectionCount(Long serviceMqttMaxConnectionCount) {
    this.serviceMqttMaxConnectionCount = serviceMqttMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of MQTT client connections that can be simultaneously connected to the Message VPN. The default is the max value supported by the platform. Available since 2.1.
   * @return serviceMqttMaxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of MQTT client connections that can be simultaneously connected to the Message VPN. The default is the max value supported by the platform. Available since 2.1.")
  public Long getServiceMqttMaxConnectionCount() {
    return serviceMqttMaxConnectionCount;
  }

  public void setServiceMqttMaxConnectionCount(Long serviceMqttMaxConnectionCount) {
    this.serviceMqttMaxConnectionCount = serviceMqttMaxConnectionCount;
  }

  public MsgVpn serviceMqttPlainTextEnabled(Boolean serviceMqttPlainTextEnabled) {
    this.serviceMqttPlainTextEnabled = serviceMqttPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the plain-text MQTT service in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is `false`. Available since 2.1.
   * @return serviceMqttPlainTextEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the plain-text MQTT service in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is `false`. Available since 2.1.")
  public Boolean getServiceMqttPlainTextEnabled() {
    return serviceMqttPlainTextEnabled;
  }

  public void setServiceMqttPlainTextEnabled(Boolean serviceMqttPlainTextEnabled) {
    this.serviceMqttPlainTextEnabled = serviceMqttPlainTextEnabled;
  }

  public MsgVpn serviceMqttPlainTextListenPort(Long serviceMqttPlainTextListenPort) {
    this.serviceMqttPlainTextListenPort = serviceMqttPlainTextListenPort;
    return this;
  }

   /**
   * The port number for plain-text MQTT clients that connect to the Message VPN. The default value is `0`. Available since 2.1.
   * @return serviceMqttPlainTextListenPort
  **/
  @ApiModelProperty(example = "null", value = "The port number for plain-text MQTT clients that connect to the Message VPN. The default value is `0`. Available since 2.1.")
  public Long getServiceMqttPlainTextListenPort() {
    return serviceMqttPlainTextListenPort;
  }

  public void setServiceMqttPlainTextListenPort(Long serviceMqttPlainTextListenPort) {
    this.serviceMqttPlainTextListenPort = serviceMqttPlainTextListenPort;
  }

  public MsgVpn serviceMqttTlsEnabled(Boolean serviceMqttTlsEnabled) {
    this.serviceMqttTlsEnabled = serviceMqttTlsEnabled;
    return this;
  }

   /**
   * Enable or disable the use of TLS for the MQTT service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is `false`. Available since 2.1.
   * @return serviceMqttTlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the use of TLS for the MQTT service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is `false`. Available since 2.1.")
  public Boolean getServiceMqttTlsEnabled() {
    return serviceMqttTlsEnabled;
  }

  public void setServiceMqttTlsEnabled(Boolean serviceMqttTlsEnabled) {
    this.serviceMqttTlsEnabled = serviceMqttTlsEnabled;
  }

  public MsgVpn serviceMqttTlsListenPort(Long serviceMqttTlsListenPort) {
    this.serviceMqttTlsListenPort = serviceMqttTlsListenPort;
    return this;
  }

   /**
   * The port number for MQTT clients that connect to the Message VPN over TLS. The default value is `0`. Available since 2.1.
   * @return serviceMqttTlsListenPort
  **/
  @ApiModelProperty(example = "null", value = "The port number for MQTT clients that connect to the Message VPN over TLS. The default value is `0`. Available since 2.1.")
  public Long getServiceMqttTlsListenPort() {
    return serviceMqttTlsListenPort;
  }

  public void setServiceMqttTlsListenPort(Long serviceMqttTlsListenPort) {
    this.serviceMqttTlsListenPort = serviceMqttTlsListenPort;
  }

  public MsgVpn serviceMqttTlsWebSocketEnabled(Boolean serviceMqttTlsWebSocketEnabled) {
    this.serviceMqttTlsWebSocketEnabled = serviceMqttTlsWebSocketEnabled;
    return this;
  }

   /**
   * Enable or disable the use of WebSocket over TLS for the MQTT service in the Message VPN. Disabling causes clients currently connected by WebSocket over TLS to be disconnected. The default value is `false`. Available since 2.1.
   * @return serviceMqttTlsWebSocketEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the use of WebSocket over TLS for the MQTT service in the Message VPN. Disabling causes clients currently connected by WebSocket over TLS to be disconnected. The default value is `false`. Available since 2.1.")
  public Boolean getServiceMqttTlsWebSocketEnabled() {
    return serviceMqttTlsWebSocketEnabled;
  }

  public void setServiceMqttTlsWebSocketEnabled(Boolean serviceMqttTlsWebSocketEnabled) {
    this.serviceMqttTlsWebSocketEnabled = serviceMqttTlsWebSocketEnabled;
  }

  public MsgVpn serviceMqttTlsWebSocketListenPort(Long serviceMqttTlsWebSocketListenPort) {
    this.serviceMqttTlsWebSocketListenPort = serviceMqttTlsWebSocketListenPort;
    return this;
  }

   /**
   * The port number for MQTT clients that connect to the Message VPN using WebSocket over TLS. The default value is `0`. Available since 2.1.
   * @return serviceMqttTlsWebSocketListenPort
  **/
  @ApiModelProperty(example = "null", value = "The port number for MQTT clients that connect to the Message VPN using WebSocket over TLS. The default value is `0`. Available since 2.1.")
  public Long getServiceMqttTlsWebSocketListenPort() {
    return serviceMqttTlsWebSocketListenPort;
  }

  public void setServiceMqttTlsWebSocketListenPort(Long serviceMqttTlsWebSocketListenPort) {
    this.serviceMqttTlsWebSocketListenPort = serviceMqttTlsWebSocketListenPort;
  }

  public MsgVpn serviceMqttWebSocketEnabled(Boolean serviceMqttWebSocketEnabled) {
    this.serviceMqttWebSocketEnabled = serviceMqttWebSocketEnabled;
    return this;
  }

   /**
   * Enable or disable the use of WebSocket for the MQTT service in the Message VPN. Disabling causes clients currently connected by WebSocket to be disconnected. The default value is `false`. Available since 2.1.
   * @return serviceMqttWebSocketEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the use of WebSocket for the MQTT service in the Message VPN. Disabling causes clients currently connected by WebSocket to be disconnected. The default value is `false`. Available since 2.1.")
  public Boolean getServiceMqttWebSocketEnabled() {
    return serviceMqttWebSocketEnabled;
  }

  public void setServiceMqttWebSocketEnabled(Boolean serviceMqttWebSocketEnabled) {
    this.serviceMqttWebSocketEnabled = serviceMqttWebSocketEnabled;
  }

  public MsgVpn serviceMqttWebSocketListenPort(Long serviceMqttWebSocketListenPort) {
    this.serviceMqttWebSocketListenPort = serviceMqttWebSocketListenPort;
    return this;
  }

   /**
   * The port number for plain-text MQTT clients that connect to the Message VPN using WebSocket. The default value is `0`. Available since 2.1.
   * @return serviceMqttWebSocketListenPort
  **/
  @ApiModelProperty(example = "null", value = "The port number for plain-text MQTT clients that connect to the Message VPN using WebSocket. The default value is `0`. Available since 2.1.")
  public Long getServiceMqttWebSocketListenPort() {
    return serviceMqttWebSocketListenPort;
  }

  public void setServiceMqttWebSocketListenPort(Long serviceMqttWebSocketListenPort) {
    this.serviceMqttWebSocketListenPort = serviceMqttWebSocketListenPort;
  }

  public MsgVpn serviceRestIncomingMaxConnectionCount(Long serviceRestIncomingMaxConnectionCount) {
    this.serviceRestIncomingMaxConnectionCount = serviceRestIncomingMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of REST incoming client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform.
   * @return serviceRestIncomingMaxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of REST incoming client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform.")
  public Long getServiceRestIncomingMaxConnectionCount() {
    return serviceRestIncomingMaxConnectionCount;
  }

  public void setServiceRestIncomingMaxConnectionCount(Long serviceRestIncomingMaxConnectionCount) {
    this.serviceRestIncomingMaxConnectionCount = serviceRestIncomingMaxConnectionCount;
  }

  public MsgVpn serviceRestIncomingPlainTextEnabled(Boolean serviceRestIncomingPlainTextEnabled) {
    this.serviceRestIncomingPlainTextEnabled = serviceRestIncomingPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the plain-text REST service for incoming clients in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is `false`.
   * @return serviceRestIncomingPlainTextEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the plain-text REST service for incoming clients in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is `false`.")
  public Boolean getServiceRestIncomingPlainTextEnabled() {
    return serviceRestIncomingPlainTextEnabled;
  }

  public void setServiceRestIncomingPlainTextEnabled(Boolean serviceRestIncomingPlainTextEnabled) {
    this.serviceRestIncomingPlainTextEnabled = serviceRestIncomingPlainTextEnabled;
  }

  public MsgVpn serviceRestIncomingPlainTextListenPort(Long serviceRestIncomingPlainTextListenPort) {
    this.serviceRestIncomingPlainTextListenPort = serviceRestIncomingPlainTextListenPort;
    return this;
  }

   /**
   * The port number for incoming plain-text REST clients that connect to the Message VPN. The default value is `0`.
   * @return serviceRestIncomingPlainTextListenPort
  **/
  @ApiModelProperty(example = "null", value = "The port number for incoming plain-text REST clients that connect to the Message VPN. The default value is `0`.")
  public Long getServiceRestIncomingPlainTextListenPort() {
    return serviceRestIncomingPlainTextListenPort;
  }

  public void setServiceRestIncomingPlainTextListenPort(Long serviceRestIncomingPlainTextListenPort) {
    this.serviceRestIncomingPlainTextListenPort = serviceRestIncomingPlainTextListenPort;
  }

  public MsgVpn serviceRestIncomingTlsEnabled(Boolean serviceRestIncomingTlsEnabled) {
    this.serviceRestIncomingTlsEnabled = serviceRestIncomingTlsEnabled;
    return this;
  }

   /**
   * Enable or disable the use of TLS for the REST service for incoming clients in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is `false`.
   * @return serviceRestIncomingTlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the use of TLS for the REST service for incoming clients in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is `false`.")
  public Boolean getServiceRestIncomingTlsEnabled() {
    return serviceRestIncomingTlsEnabled;
  }

  public void setServiceRestIncomingTlsEnabled(Boolean serviceRestIncomingTlsEnabled) {
    this.serviceRestIncomingTlsEnabled = serviceRestIncomingTlsEnabled;
  }

  public MsgVpn serviceRestIncomingTlsListenPort(Long serviceRestIncomingTlsListenPort) {
    this.serviceRestIncomingTlsListenPort = serviceRestIncomingTlsListenPort;
    return this;
  }

   /**
   * The port number for incoming REST clients that connect to the Message VPN over TLS. The default value is `0`.
   * @return serviceRestIncomingTlsListenPort
  **/
  @ApiModelProperty(example = "null", value = "The port number for incoming REST clients that connect to the Message VPN over TLS. The default value is `0`.")
  public Long getServiceRestIncomingTlsListenPort() {
    return serviceRestIncomingTlsListenPort;
  }

  public void setServiceRestIncomingTlsListenPort(Long serviceRestIncomingTlsListenPort) {
    this.serviceRestIncomingTlsListenPort = serviceRestIncomingTlsListenPort;
  }

  public MsgVpn serviceRestMode(ServiceRestModeEnum serviceRestMode) {
    this.serviceRestMode = serviceRestMode;
    return this;
  }

   /**
   * The REST service mode for incoming REST clients that connect to the Message VPN. The default value is `\"messaging\"`. The allowed values and their meaning are:  <pre> \"gateway\" - Act as a message gateway through which REST messages are propagated. \"messaging\" - Act as a message broker on which REST messages are queued. </pre>  Available since 2.6.
   * @return serviceRestMode
  **/
  @ApiModelProperty(example = "null", value = "The REST service mode for incoming REST clients that connect to the Message VPN. The default value is `\"messaging\"`. The allowed values and their meaning are:  <pre> \"gateway\" - Act as a message gateway through which REST messages are propagated. \"messaging\" - Act as a message broker on which REST messages are queued. </pre>  Available since 2.6.")
  public ServiceRestModeEnum getServiceRestMode() {
    return serviceRestMode;
  }

  public void setServiceRestMode(ServiceRestModeEnum serviceRestMode) {
    this.serviceRestMode = serviceRestMode;
  }

  public MsgVpn serviceRestOutgoingMaxConnectionCount(Long serviceRestOutgoingMaxConnectionCount) {
    this.serviceRestOutgoingMaxConnectionCount = serviceRestOutgoingMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of REST Consumer (outgoing) client connections that can be simultaneously connected to the Message VPN. The default varies by platform.
   * @return serviceRestOutgoingMaxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of REST Consumer (outgoing) client connections that can be simultaneously connected to the Message VPN. The default varies by platform.")
  public Long getServiceRestOutgoingMaxConnectionCount() {
    return serviceRestOutgoingMaxConnectionCount;
  }

  public void setServiceRestOutgoingMaxConnectionCount(Long serviceRestOutgoingMaxConnectionCount) {
    this.serviceRestOutgoingMaxConnectionCount = serviceRestOutgoingMaxConnectionCount;
  }

  public MsgVpn serviceSmfMaxConnectionCount(Long serviceSmfMaxConnectionCount) {
    this.serviceSmfMaxConnectionCount = serviceSmfMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of SMF client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform.
   * @return serviceSmfMaxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of SMF client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform.")
  public Long getServiceSmfMaxConnectionCount() {
    return serviceSmfMaxConnectionCount;
  }

  public void setServiceSmfMaxConnectionCount(Long serviceSmfMaxConnectionCount) {
    this.serviceSmfMaxConnectionCount = serviceSmfMaxConnectionCount;
  }

  public MsgVpn serviceSmfPlainTextEnabled(Boolean serviceSmfPlainTextEnabled) {
    this.serviceSmfPlainTextEnabled = serviceSmfPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the plain-text SMF service in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is `true`.
   * @return serviceSmfPlainTextEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the plain-text SMF service in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is `true`.")
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
   * Enable or disable the use of TLS for the SMF service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is `true`.
   * @return serviceSmfTlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the use of TLS for the SMF service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is `true`.")
  public Boolean getServiceSmfTlsEnabled() {
    return serviceSmfTlsEnabled;
  }

  public void setServiceSmfTlsEnabled(Boolean serviceSmfTlsEnabled) {
    this.serviceSmfTlsEnabled = serviceSmfTlsEnabled;
  }

  public MsgVpn serviceWebMaxConnectionCount(Long serviceWebMaxConnectionCount) {
    this.serviceWebMaxConnectionCount = serviceWebMaxConnectionCount;
    return this;
  }

   /**
   * The maximum number of Web Transport client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform.
   * @return serviceWebMaxConnectionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of Web Transport client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the platform. The default is the max value supported by the platform.")
  public Long getServiceWebMaxConnectionCount() {
    return serviceWebMaxConnectionCount;
  }

  public void setServiceWebMaxConnectionCount(Long serviceWebMaxConnectionCount) {
    this.serviceWebMaxConnectionCount = serviceWebMaxConnectionCount;
  }

  public MsgVpn serviceWebPlainTextEnabled(Boolean serviceWebPlainTextEnabled) {
    this.serviceWebPlainTextEnabled = serviceWebPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable the plain-text Web Transport service in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is `true`.
   * @return serviceWebPlainTextEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the plain-text Web Transport service in the Message VPN. Disabling causes clients currently connected to be disconnected. The default value is `true`.")
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
   * Enable or disable the use of TLS for the Web Transport service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is `true`.
   * @return serviceWebTlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the use of TLS for the Web Transport service in the Message VPN. Disabling causes clients currently connected over TLS to be disconnected. The default value is `true`.")
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
        Objects.equals(this.authenticationClientCertRevocationCheckMode, msgVpn.authenticationClientCertRevocationCheckMode) &&
        Objects.equals(this.authenticationClientCertUsernameSource, msgVpn.authenticationClientCertUsernameSource) &&
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
        Objects.equals(this.dmrEnabled, msgVpn.dmrEnabled) &&
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
        Objects.equals(this.eventServiceAmqpConnectionCountThreshold, msgVpn.eventServiceAmqpConnectionCountThreshold) &&
        Objects.equals(this.eventServiceMqttConnectionCountThreshold, msgVpn.eventServiceMqttConnectionCountThreshold) &&
        Objects.equals(this.eventServiceRestIncomingConnectionCountThreshold, msgVpn.eventServiceRestIncomingConnectionCountThreshold) &&
        Objects.equals(this.eventServiceSmfConnectionCountThreshold, msgVpn.eventServiceSmfConnectionCountThreshold) &&
        Objects.equals(this.eventServiceWebConnectionCountThreshold, msgVpn.eventServiceWebConnectionCountThreshold) &&
        Objects.equals(this.eventSubscriptionCountThreshold, msgVpn.eventSubscriptionCountThreshold) &&
        Objects.equals(this.eventTransactedSessionCountThreshold, msgVpn.eventTransactedSessionCountThreshold) &&
        Objects.equals(this.eventTransactionCountThreshold, msgVpn.eventTransactionCountThreshold) &&
        Objects.equals(this.exportSubscriptionsEnabled, msgVpn.exportSubscriptionsEnabled) &&
        Objects.equals(this.jndiEnabled, msgVpn.jndiEnabled) &&
        Objects.equals(this.maxConnectionCount, msgVpn.maxConnectionCount) &&
        Objects.equals(this.maxEgressFlowCount, msgVpn.maxEgressFlowCount) &&
        Objects.equals(this.maxEndpointCount, msgVpn.maxEndpointCount) &&
        Objects.equals(this.maxIngressFlowCount, msgVpn.maxIngressFlowCount) &&
        Objects.equals(this.maxMsgSpoolUsage, msgVpn.maxMsgSpoolUsage) &&
        Objects.equals(this.maxSubscriptionCount, msgVpn.maxSubscriptionCount) &&
        Objects.equals(this.maxTransactedSessionCount, msgVpn.maxTransactedSessionCount) &&
        Objects.equals(this.maxTransactionCount, msgVpn.maxTransactionCount) &&
        Objects.equals(this.mqttRetainMaxMemory, msgVpn.mqttRetainMaxMemory) &&
        Objects.equals(this.msgVpnName, msgVpn.msgVpnName) &&
        Objects.equals(this.replicationAckPropagationIntervalMsgCount, msgVpn.replicationAckPropagationIntervalMsgCount) &&
        Objects.equals(this.replicationBridgeAuthenticationBasicClientUsername, msgVpn.replicationBridgeAuthenticationBasicClientUsername) &&
        Objects.equals(this.replicationBridgeAuthenticationBasicPassword, msgVpn.replicationBridgeAuthenticationBasicPassword) &&
        Objects.equals(this.replicationBridgeAuthenticationClientCertContent, msgVpn.replicationBridgeAuthenticationClientCertContent) &&
        Objects.equals(this.replicationBridgeAuthenticationClientCertPassword, msgVpn.replicationBridgeAuthenticationClientCertPassword) &&
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
        Objects.equals(this.sempOverMsgBusShowEnabled, msgVpn.sempOverMsgBusShowEnabled) &&
        Objects.equals(this.serviceAmqpMaxConnectionCount, msgVpn.serviceAmqpMaxConnectionCount) &&
        Objects.equals(this.serviceAmqpPlainTextEnabled, msgVpn.serviceAmqpPlainTextEnabled) &&
        Objects.equals(this.serviceAmqpPlainTextListenPort, msgVpn.serviceAmqpPlainTextListenPort) &&
        Objects.equals(this.serviceAmqpTlsEnabled, msgVpn.serviceAmqpTlsEnabled) &&
        Objects.equals(this.serviceAmqpTlsListenPort, msgVpn.serviceAmqpTlsListenPort) &&
        Objects.equals(this.serviceMqttMaxConnectionCount, msgVpn.serviceMqttMaxConnectionCount) &&
        Objects.equals(this.serviceMqttPlainTextEnabled, msgVpn.serviceMqttPlainTextEnabled) &&
        Objects.equals(this.serviceMqttPlainTextListenPort, msgVpn.serviceMqttPlainTextListenPort) &&
        Objects.equals(this.serviceMqttTlsEnabled, msgVpn.serviceMqttTlsEnabled) &&
        Objects.equals(this.serviceMqttTlsListenPort, msgVpn.serviceMqttTlsListenPort) &&
        Objects.equals(this.serviceMqttTlsWebSocketEnabled, msgVpn.serviceMqttTlsWebSocketEnabled) &&
        Objects.equals(this.serviceMqttTlsWebSocketListenPort, msgVpn.serviceMqttTlsWebSocketListenPort) &&
        Objects.equals(this.serviceMqttWebSocketEnabled, msgVpn.serviceMqttWebSocketEnabled) &&
        Objects.equals(this.serviceMqttWebSocketListenPort, msgVpn.serviceMqttWebSocketListenPort) &&
        Objects.equals(this.serviceRestIncomingMaxConnectionCount, msgVpn.serviceRestIncomingMaxConnectionCount) &&
        Objects.equals(this.serviceRestIncomingPlainTextEnabled, msgVpn.serviceRestIncomingPlainTextEnabled) &&
        Objects.equals(this.serviceRestIncomingPlainTextListenPort, msgVpn.serviceRestIncomingPlainTextListenPort) &&
        Objects.equals(this.serviceRestIncomingTlsEnabled, msgVpn.serviceRestIncomingTlsEnabled) &&
        Objects.equals(this.serviceRestIncomingTlsListenPort, msgVpn.serviceRestIncomingTlsListenPort) &&
        Objects.equals(this.serviceRestMode, msgVpn.serviceRestMode) &&
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
    return Objects.hash(authenticationBasicEnabled, authenticationBasicProfileName, authenticationBasicRadiusDomain, authenticationBasicType, authenticationClientCertAllowApiProvidedUsernameEnabled, authenticationClientCertEnabled, authenticationClientCertMaxChainDepth, authenticationClientCertRevocationCheckMode, authenticationClientCertUsernameSource, authenticationClientCertValidateDateEnabled, authenticationKerberosAllowApiProvidedUsernameEnabled, authenticationKerberosEnabled, authorizationLdapGroupMembershipAttributeName, authorizationProfileName, authorizationType, bridgingTlsServerCertEnforceTrustedCommonNameEnabled, bridgingTlsServerCertMaxChainDepth, bridgingTlsServerCertValidateDateEnabled, distributedCacheManagementEnabled, dmrEnabled, enabled, eventConnectionCountThreshold, eventEgressFlowCountThreshold, eventEgressMsgRateThreshold, eventEndpointCountThreshold, eventIngressFlowCountThreshold, eventIngressMsgRateThreshold, eventLargeMsgThreshold, eventLogTag, eventMsgSpoolUsageThreshold, eventPublishClientEnabled, eventPublishMsgVpnEnabled, eventPublishSubscriptionMode, eventPublishTopicFormatMqttEnabled, eventPublishTopicFormatSmfEnabled, eventServiceAmqpConnectionCountThreshold, eventServiceMqttConnectionCountThreshold, eventServiceRestIncomingConnectionCountThreshold, eventServiceSmfConnectionCountThreshold, eventServiceWebConnectionCountThreshold, eventSubscriptionCountThreshold, eventTransactedSessionCountThreshold, eventTransactionCountThreshold, exportSubscriptionsEnabled, jndiEnabled, maxConnectionCount, maxEgressFlowCount, maxEndpointCount, maxIngressFlowCount, maxMsgSpoolUsage, maxSubscriptionCount, maxTransactedSessionCount, maxTransactionCount, mqttRetainMaxMemory, msgVpnName, replicationAckPropagationIntervalMsgCount, replicationBridgeAuthenticationBasicClientUsername, replicationBridgeAuthenticationBasicPassword, replicationBridgeAuthenticationClientCertContent, replicationBridgeAuthenticationClientCertPassword, replicationBridgeAuthenticationScheme, replicationBridgeCompressedDataEnabled, replicationBridgeEgressFlowWindowSize, replicationBridgeRetryDelay, replicationBridgeTlsEnabled, replicationBridgeUnidirectionalClientProfileName, replicationEnabled, replicationEnabledQueueBehavior, replicationQueueMaxMsgSpoolUsage, replicationQueueRejectMsgToSenderOnDiscardEnabled, replicationRejectMsgWhenSyncIneligibleEnabled, replicationRole, replicationTransactionMode, restTlsServerCertEnforceTrustedCommonNameEnabled, restTlsServerCertMaxChainDepth, restTlsServerCertValidateDateEnabled, sempOverMsgBusAdminClientEnabled, sempOverMsgBusAdminDistributedCacheEnabled, sempOverMsgBusAdminEnabled, sempOverMsgBusEnabled, sempOverMsgBusShowEnabled, serviceAmqpMaxConnectionCount, serviceAmqpPlainTextEnabled, serviceAmqpPlainTextListenPort, serviceAmqpTlsEnabled, serviceAmqpTlsListenPort, serviceMqttMaxConnectionCount, serviceMqttPlainTextEnabled, serviceMqttPlainTextListenPort, serviceMqttTlsEnabled, serviceMqttTlsListenPort, serviceMqttTlsWebSocketEnabled, serviceMqttTlsWebSocketListenPort, serviceMqttWebSocketEnabled, serviceMqttWebSocketListenPort, serviceRestIncomingMaxConnectionCount, serviceRestIncomingPlainTextEnabled, serviceRestIncomingPlainTextListenPort, serviceRestIncomingTlsEnabled, serviceRestIncomingTlsListenPort, serviceRestMode, serviceRestOutgoingMaxConnectionCount, serviceSmfMaxConnectionCount, serviceSmfPlainTextEnabled, serviceSmfTlsEnabled, serviceWebMaxConnectionCount, serviceWebPlainTextEnabled, serviceWebTlsEnabled, tlsAllowDowngradeToPlainTextEnabled);
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
    sb.append("    authenticationClientCertRevocationCheckMode: ").append(toIndentedString(authenticationClientCertRevocationCheckMode)).append("\n");
    sb.append("    authenticationClientCertUsernameSource: ").append(toIndentedString(authenticationClientCertUsernameSource)).append("\n");
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
    sb.append("    dmrEnabled: ").append(toIndentedString(dmrEnabled)).append("\n");
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
    sb.append("    eventServiceAmqpConnectionCountThreshold: ").append(toIndentedString(eventServiceAmqpConnectionCountThreshold)).append("\n");
    sb.append("    eventServiceMqttConnectionCountThreshold: ").append(toIndentedString(eventServiceMqttConnectionCountThreshold)).append("\n");
    sb.append("    eventServiceRestIncomingConnectionCountThreshold: ").append(toIndentedString(eventServiceRestIncomingConnectionCountThreshold)).append("\n");
    sb.append("    eventServiceSmfConnectionCountThreshold: ").append(toIndentedString(eventServiceSmfConnectionCountThreshold)).append("\n");
    sb.append("    eventServiceWebConnectionCountThreshold: ").append(toIndentedString(eventServiceWebConnectionCountThreshold)).append("\n");
    sb.append("    eventSubscriptionCountThreshold: ").append(toIndentedString(eventSubscriptionCountThreshold)).append("\n");
    sb.append("    eventTransactedSessionCountThreshold: ").append(toIndentedString(eventTransactedSessionCountThreshold)).append("\n");
    sb.append("    eventTransactionCountThreshold: ").append(toIndentedString(eventTransactionCountThreshold)).append("\n");
    sb.append("    exportSubscriptionsEnabled: ").append(toIndentedString(exportSubscriptionsEnabled)).append("\n");
    sb.append("    jndiEnabled: ").append(toIndentedString(jndiEnabled)).append("\n");
    sb.append("    maxConnectionCount: ").append(toIndentedString(maxConnectionCount)).append("\n");
    sb.append("    maxEgressFlowCount: ").append(toIndentedString(maxEgressFlowCount)).append("\n");
    sb.append("    maxEndpointCount: ").append(toIndentedString(maxEndpointCount)).append("\n");
    sb.append("    maxIngressFlowCount: ").append(toIndentedString(maxIngressFlowCount)).append("\n");
    sb.append("    maxMsgSpoolUsage: ").append(toIndentedString(maxMsgSpoolUsage)).append("\n");
    sb.append("    maxSubscriptionCount: ").append(toIndentedString(maxSubscriptionCount)).append("\n");
    sb.append("    maxTransactedSessionCount: ").append(toIndentedString(maxTransactedSessionCount)).append("\n");
    sb.append("    maxTransactionCount: ").append(toIndentedString(maxTransactionCount)).append("\n");
    sb.append("    mqttRetainMaxMemory: ").append(toIndentedString(mqttRetainMaxMemory)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    replicationAckPropagationIntervalMsgCount: ").append(toIndentedString(replicationAckPropagationIntervalMsgCount)).append("\n");
    sb.append("    replicationBridgeAuthenticationBasicClientUsername: ").append(toIndentedString(replicationBridgeAuthenticationBasicClientUsername)).append("\n");
    sb.append("    replicationBridgeAuthenticationBasicPassword: ").append(toIndentedString(replicationBridgeAuthenticationBasicPassword)).append("\n");
    sb.append("    replicationBridgeAuthenticationClientCertContent: ").append(toIndentedString(replicationBridgeAuthenticationClientCertContent)).append("\n");
    sb.append("    replicationBridgeAuthenticationClientCertPassword: ").append(toIndentedString(replicationBridgeAuthenticationClientCertPassword)).append("\n");
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
    sb.append("    sempOverMsgBusShowEnabled: ").append(toIndentedString(sempOverMsgBusShowEnabled)).append("\n");
    sb.append("    serviceAmqpMaxConnectionCount: ").append(toIndentedString(serviceAmqpMaxConnectionCount)).append("\n");
    sb.append("    serviceAmqpPlainTextEnabled: ").append(toIndentedString(serviceAmqpPlainTextEnabled)).append("\n");
    sb.append("    serviceAmqpPlainTextListenPort: ").append(toIndentedString(serviceAmqpPlainTextListenPort)).append("\n");
    sb.append("    serviceAmqpTlsEnabled: ").append(toIndentedString(serviceAmqpTlsEnabled)).append("\n");
    sb.append("    serviceAmqpTlsListenPort: ").append(toIndentedString(serviceAmqpTlsListenPort)).append("\n");
    sb.append("    serviceMqttMaxConnectionCount: ").append(toIndentedString(serviceMqttMaxConnectionCount)).append("\n");
    sb.append("    serviceMqttPlainTextEnabled: ").append(toIndentedString(serviceMqttPlainTextEnabled)).append("\n");
    sb.append("    serviceMqttPlainTextListenPort: ").append(toIndentedString(serviceMqttPlainTextListenPort)).append("\n");
    sb.append("    serviceMqttTlsEnabled: ").append(toIndentedString(serviceMqttTlsEnabled)).append("\n");
    sb.append("    serviceMqttTlsListenPort: ").append(toIndentedString(serviceMqttTlsListenPort)).append("\n");
    sb.append("    serviceMqttTlsWebSocketEnabled: ").append(toIndentedString(serviceMqttTlsWebSocketEnabled)).append("\n");
    sb.append("    serviceMqttTlsWebSocketListenPort: ").append(toIndentedString(serviceMqttTlsWebSocketListenPort)).append("\n");
    sb.append("    serviceMqttWebSocketEnabled: ").append(toIndentedString(serviceMqttWebSocketEnabled)).append("\n");
    sb.append("    serviceMqttWebSocketListenPort: ").append(toIndentedString(serviceMqttWebSocketListenPort)).append("\n");
    sb.append("    serviceRestIncomingMaxConnectionCount: ").append(toIndentedString(serviceRestIncomingMaxConnectionCount)).append("\n");
    sb.append("    serviceRestIncomingPlainTextEnabled: ").append(toIndentedString(serviceRestIncomingPlainTextEnabled)).append("\n");
    sb.append("    serviceRestIncomingPlainTextListenPort: ").append(toIndentedString(serviceRestIncomingPlainTextListenPort)).append("\n");
    sb.append("    serviceRestIncomingTlsEnabled: ").append(toIndentedString(serviceRestIncomingTlsEnabled)).append("\n");
    sb.append("    serviceRestIncomingTlsListenPort: ").append(toIndentedString(serviceRestIncomingTlsListenPort)).append("\n");
    sb.append("    serviceRestMode: ").append(toIndentedString(serviceRestMode)).append("\n");
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

