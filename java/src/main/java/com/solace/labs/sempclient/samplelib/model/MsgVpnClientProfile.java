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
import com.solace.labs.sempclient.samplelib.model.EventThresholdByPercent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * MsgVpnClientProfile
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-11T16:20:54.920-04:00")
public class MsgVpnClientProfile {
  @JsonProperty("allowBridgeConnectionsEnabled")
  private Boolean allowBridgeConnectionsEnabled = null;

  @JsonProperty("allowCutThroughForwardingEnabled")
  private Boolean allowCutThroughForwardingEnabled = null;

  @JsonProperty("allowGuaranteedEndpointCreateEnabled")
  private Boolean allowGuaranteedEndpointCreateEnabled = null;

  @JsonProperty("allowGuaranteedMsgReceiveEnabled")
  private Boolean allowGuaranteedMsgReceiveEnabled = null;

  @JsonProperty("allowGuaranteedMsgSendEnabled")
  private Boolean allowGuaranteedMsgSendEnabled = null;

  @JsonProperty("allowSharedSubscriptionsEnabled")
  private Boolean allowSharedSubscriptionsEnabled = null;

  @JsonProperty("allowTransactedSessionsEnabled")
  private Boolean allowTransactedSessionsEnabled = null;

  @JsonProperty("apiQueueManagementCopyFromOnCreateName")
  private String apiQueueManagementCopyFromOnCreateName = null;

  @JsonProperty("apiTopicEndpointManagementCopyFromOnCreateName")
  private String apiTopicEndpointManagementCopyFromOnCreateName = null;

  @JsonProperty("clientProfileName")
  private String clientProfileName = null;

  @JsonProperty("compressionEnabled")
  private Boolean compressionEnabled = null;

  @JsonProperty("elidingDelay")
  private Long elidingDelay = null;

  @JsonProperty("elidingEnabled")
  private Boolean elidingEnabled = null;

  @JsonProperty("elidingMaxTopicCount")
  private Long elidingMaxTopicCount = null;

  @JsonProperty("eventClientProvisionedEndpointSpoolUsageThreshold")
  private EventThresholdByPercent eventClientProvisionedEndpointSpoolUsageThreshold = null;

  @JsonProperty("eventConnectionCountPerClientUsernameThreshold")
  private EventThreshold eventConnectionCountPerClientUsernameThreshold = null;

  @JsonProperty("eventEgressFlowCountThreshold")
  private EventThreshold eventEgressFlowCountThreshold = null;

  @JsonProperty("eventEndpointCountPerClientUsernameThreshold")
  private EventThreshold eventEndpointCountPerClientUsernameThreshold = null;

  @JsonProperty("eventIngressFlowCountThreshold")
  private EventThreshold eventIngressFlowCountThreshold = null;

  @JsonProperty("eventServiceSmfConnectionCountPerClientUsernameThreshold")
  private EventThreshold eventServiceSmfConnectionCountPerClientUsernameThreshold = null;

  @JsonProperty("eventServiceWebConnectionCountPerClientUsernameThreshold")
  private EventThreshold eventServiceWebConnectionCountPerClientUsernameThreshold = null;

  @JsonProperty("eventSubscriptionCountThreshold")
  private EventThreshold eventSubscriptionCountThreshold = null;

  @JsonProperty("eventTransactedSessionCountThreshold")
  private EventThreshold eventTransactedSessionCountThreshold = null;

  @JsonProperty("eventTransactionCountThreshold")
  private EventThreshold eventTransactionCountThreshold = null;

  @JsonProperty("maxConnectionCountPerClientUsername")
  private Long maxConnectionCountPerClientUsername = null;

  @JsonProperty("maxEgressFlowCount")
  private Long maxEgressFlowCount = null;

  @JsonProperty("maxEndpointCountPerClientUsername")
  private Long maxEndpointCountPerClientUsername = null;

  @JsonProperty("maxIngressFlowCount")
  private Long maxIngressFlowCount = null;

  @JsonProperty("maxSubscriptionCount")
  private Long maxSubscriptionCount = null;

  @JsonProperty("maxTransactedSessionCount")
  private Long maxTransactedSessionCount = null;

  @JsonProperty("maxTransactionCount")
  private Long maxTransactionCount = null;

  @JsonProperty("msgVpnName")
  private String msgVpnName = null;

  @JsonProperty("queueControl1MaxDepth")
  private Integer queueControl1MaxDepth = null;

  @JsonProperty("queueControl1MinMsgBurst")
  private Integer queueControl1MinMsgBurst = null;

  @JsonProperty("queueDirect1MaxDepth")
  private Integer queueDirect1MaxDepth = null;

  @JsonProperty("queueDirect1MinMsgBurst")
  private Integer queueDirect1MinMsgBurst = null;

  @JsonProperty("queueDirect2MaxDepth")
  private Integer queueDirect2MaxDepth = null;

  @JsonProperty("queueDirect2MinMsgBurst")
  private Integer queueDirect2MinMsgBurst = null;

  @JsonProperty("queueDirect3MaxDepth")
  private Integer queueDirect3MaxDepth = null;

  @JsonProperty("queueDirect3MinMsgBurst")
  private Integer queueDirect3MinMsgBurst = null;

  @JsonProperty("queueGuaranteed1MaxDepth")
  private Integer queueGuaranteed1MaxDepth = null;

  @JsonProperty("queueGuaranteed1MinMsgBurst")
  private Integer queueGuaranteed1MinMsgBurst = null;

  @JsonProperty("rejectMsgToSenderOnNoSubscriptionMatchEnabled")
  private Boolean rejectMsgToSenderOnNoSubscriptionMatchEnabled = null;

  @JsonProperty("replicationAllowClientConnectWhenStandbyEnabled")
  private Boolean replicationAllowClientConnectWhenStandbyEnabled = null;

  @JsonProperty("serviceSmfMaxConnectionCountPerClientUsername")
  private Long serviceSmfMaxConnectionCountPerClientUsername = null;

  @JsonProperty("serviceWebInactiveTimeout")
  private Long serviceWebInactiveTimeout = null;

  @JsonProperty("serviceWebMaxConnectionCountPerClientUsername")
  private Long serviceWebMaxConnectionCountPerClientUsername = null;

  @JsonProperty("serviceWebMaxPayload")
  private Long serviceWebMaxPayload = null;

  @JsonProperty("tcpCongestionWindowSize")
  private Long tcpCongestionWindowSize = null;

  @JsonProperty("tcpKeepaliveCount")
  private Long tcpKeepaliveCount = null;

  @JsonProperty("tcpKeepaliveIdleTime")
  private Long tcpKeepaliveIdleTime = null;

  @JsonProperty("tcpKeepaliveInterval")
  private Long tcpKeepaliveInterval = null;

  @JsonProperty("tcpMaxSegmentSize")
  private Long tcpMaxSegmentSize = null;

  @JsonProperty("tcpMaxWindowSize")
  private Long tcpMaxWindowSize = null;

  @JsonProperty("tlsAllowDowngradeToPlainTextEnabled")
  private Boolean tlsAllowDowngradeToPlainTextEnabled = null;

  public MsgVpnClientProfile allowBridgeConnectionsEnabled(Boolean allowBridgeConnectionsEnabled) {
    this.allowBridgeConnectionsEnabled = allowBridgeConnectionsEnabled;
    return this;
  }

   /**
   * Enable or disable allowing Bridge clients using the Client Profile to connect. Changing this setting does not affect existing Bridge client connections. The default value is `false`.
   * @return allowBridgeConnectionsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing Bridge clients using the Client Profile to connect. Changing this setting does not affect existing Bridge client connections. The default value is `false`.")
  public Boolean getAllowBridgeConnectionsEnabled() {
    return allowBridgeConnectionsEnabled;
  }

  public void setAllowBridgeConnectionsEnabled(Boolean allowBridgeConnectionsEnabled) {
    this.allowBridgeConnectionsEnabled = allowBridgeConnectionsEnabled;
  }

  public MsgVpnClientProfile allowCutThroughForwardingEnabled(Boolean allowCutThroughForwardingEnabled) {
    this.allowCutThroughForwardingEnabled = allowCutThroughForwardingEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to bind to endpoints with the cut-through forwarding delivery mode. Changing this value does not affect existing client connections. The default value is `false`.
   * @return allowCutThroughForwardingEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing clients using the Client Profile to bind to endpoints with the cut-through forwarding delivery mode. Changing this value does not affect existing client connections. The default value is `false`.")
  public Boolean getAllowCutThroughForwardingEnabled() {
    return allowCutThroughForwardingEnabled;
  }

  public void setAllowCutThroughForwardingEnabled(Boolean allowCutThroughForwardingEnabled) {
    this.allowCutThroughForwardingEnabled = allowCutThroughForwardingEnabled;
  }

  public MsgVpnClientProfile allowGuaranteedEndpointCreateEnabled(Boolean allowGuaranteedEndpointCreateEnabled) {
    this.allowGuaranteedEndpointCreateEnabled = allowGuaranteedEndpointCreateEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to create topic endponts or queues. Changing this value does not affect existing client connections. The default value is `false`.
   * @return allowGuaranteedEndpointCreateEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing clients using the Client Profile to create topic endponts or queues. Changing this value does not affect existing client connections. The default value is `false`.")
  public Boolean getAllowGuaranteedEndpointCreateEnabled() {
    return allowGuaranteedEndpointCreateEnabled;
  }

  public void setAllowGuaranteedEndpointCreateEnabled(Boolean allowGuaranteedEndpointCreateEnabled) {
    this.allowGuaranteedEndpointCreateEnabled = allowGuaranteedEndpointCreateEnabled;
  }

  public MsgVpnClientProfile allowGuaranteedMsgReceiveEnabled(Boolean allowGuaranteedMsgReceiveEnabled) {
    this.allowGuaranteedMsgReceiveEnabled = allowGuaranteedMsgReceiveEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to receive guaranteed messages. Changing this setting does not affect existing client connections. The default value is `false`.
   * @return allowGuaranteedMsgReceiveEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing clients using the Client Profile to receive guaranteed messages. Changing this setting does not affect existing client connections. The default value is `false`.")
  public Boolean getAllowGuaranteedMsgReceiveEnabled() {
    return allowGuaranteedMsgReceiveEnabled;
  }

  public void setAllowGuaranteedMsgReceiveEnabled(Boolean allowGuaranteedMsgReceiveEnabled) {
    this.allowGuaranteedMsgReceiveEnabled = allowGuaranteedMsgReceiveEnabled;
  }

  public MsgVpnClientProfile allowGuaranteedMsgSendEnabled(Boolean allowGuaranteedMsgSendEnabled) {
    this.allowGuaranteedMsgSendEnabled = allowGuaranteedMsgSendEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to send guaranteed messages. Changing this setting does not affect existing client connections. The default value is `false`.
   * @return allowGuaranteedMsgSendEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing clients using the Client Profile to send guaranteed messages. Changing this setting does not affect existing client connections. The default value is `false`.")
  public Boolean getAllowGuaranteedMsgSendEnabled() {
    return allowGuaranteedMsgSendEnabled;
  }

  public void setAllowGuaranteedMsgSendEnabled(Boolean allowGuaranteedMsgSendEnabled) {
    this.allowGuaranteedMsgSendEnabled = allowGuaranteedMsgSendEnabled;
  }

  public MsgVpnClientProfile allowSharedSubscriptionsEnabled(Boolean allowSharedSubscriptionsEnabled) {
    this.allowSharedSubscriptionsEnabled = allowSharedSubscriptionsEnabled;
    return this;
  }

   /**
   * Enable or disable allowing shared subscriptions. Changing this setting does not affect existing subscriptions. The default value is `false`. Available since 2.11.
   * @return allowSharedSubscriptionsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing shared subscriptions. Changing this setting does not affect existing subscriptions. The default value is `false`. Available since 2.11.")
  public Boolean getAllowSharedSubscriptionsEnabled() {
    return allowSharedSubscriptionsEnabled;
  }

  public void setAllowSharedSubscriptionsEnabled(Boolean allowSharedSubscriptionsEnabled) {
    this.allowSharedSubscriptionsEnabled = allowSharedSubscriptionsEnabled;
  }

  public MsgVpnClientProfile allowTransactedSessionsEnabled(Boolean allowTransactedSessionsEnabled) {
    this.allowTransactedSessionsEnabled = allowTransactedSessionsEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to establish transacted sessions. Changing this setting does not affect existing client connections. The default value is `false`.
   * @return allowTransactedSessionsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing clients using the Client Profile to establish transacted sessions. Changing this setting does not affect existing client connections. The default value is `false`.")
  public Boolean getAllowTransactedSessionsEnabled() {
    return allowTransactedSessionsEnabled;
  }

  public void setAllowTransactedSessionsEnabled(Boolean allowTransactedSessionsEnabled) {
    this.allowTransactedSessionsEnabled = allowTransactedSessionsEnabled;
  }

  public MsgVpnClientProfile apiQueueManagementCopyFromOnCreateName(String apiQueueManagementCopyFromOnCreateName) {
    this.apiQueueManagementCopyFromOnCreateName = apiQueueManagementCopyFromOnCreateName;
    return this;
  }

   /**
   * The name of a queue to copy settings from when a new queue is created by a client using the Client Profile. The referenced queue must exist in the Message VPN. The default value is `\"\"`.
   * @return apiQueueManagementCopyFromOnCreateName
  **/
  @ApiModelProperty(example = "null", value = "The name of a queue to copy settings from when a new queue is created by a client using the Client Profile. The referenced queue must exist in the Message VPN. The default value is `\"\"`.")
  public String getApiQueueManagementCopyFromOnCreateName() {
    return apiQueueManagementCopyFromOnCreateName;
  }

  public void setApiQueueManagementCopyFromOnCreateName(String apiQueueManagementCopyFromOnCreateName) {
    this.apiQueueManagementCopyFromOnCreateName = apiQueueManagementCopyFromOnCreateName;
  }

  public MsgVpnClientProfile apiTopicEndpointManagementCopyFromOnCreateName(String apiTopicEndpointManagementCopyFromOnCreateName) {
    this.apiTopicEndpointManagementCopyFromOnCreateName = apiTopicEndpointManagementCopyFromOnCreateName;
    return this;
  }

   /**
   * The name of a topic endpoint to copy settings from when a new topic endpoint is created by a client using the Client Profile. The referenced topic endpoint must exist in the Message VPN. The default value is `\"\"`.
   * @return apiTopicEndpointManagementCopyFromOnCreateName
  **/
  @ApiModelProperty(example = "null", value = "The name of a topic endpoint to copy settings from when a new topic endpoint is created by a client using the Client Profile. The referenced topic endpoint must exist in the Message VPN. The default value is `\"\"`.")
  public String getApiTopicEndpointManagementCopyFromOnCreateName() {
    return apiTopicEndpointManagementCopyFromOnCreateName;
  }

  public void setApiTopicEndpointManagementCopyFromOnCreateName(String apiTopicEndpointManagementCopyFromOnCreateName) {
    this.apiTopicEndpointManagementCopyFromOnCreateName = apiTopicEndpointManagementCopyFromOnCreateName;
  }

  public MsgVpnClientProfile clientProfileName(String clientProfileName) {
    this.clientProfileName = clientProfileName;
    return this;
  }

   /**
   * The name of the Client Profile.
   * @return clientProfileName
  **/
  @ApiModelProperty(example = "null", value = "The name of the Client Profile.")
  public String getClientProfileName() {
    return clientProfileName;
  }

  public void setClientProfileName(String clientProfileName) {
    this.clientProfileName = clientProfileName;
  }

  public MsgVpnClientProfile compressionEnabled(Boolean compressionEnabled) {
    this.compressionEnabled = compressionEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to use compression. The default value is `true`. Available since 2.10.
   * @return compressionEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing clients using the Client Profile to use compression. The default value is `true`. Available since 2.10.")
  public Boolean getCompressionEnabled() {
    return compressionEnabled;
  }

  public void setCompressionEnabled(Boolean compressionEnabled) {
    this.compressionEnabled = compressionEnabled;
  }

  public MsgVpnClientProfile elidingDelay(Long elidingDelay) {
    this.elidingDelay = elidingDelay;
    return this;
  }

   /**
   * The amount of time to delay the delivery of messages to clients using the Client Profile after the initial message has been delivered (the eliding delay interval), in milliseconds. A value of 0 means there is no delay in delivering messages to clients. The default value is `0`.
   * @return elidingDelay
  **/
  @ApiModelProperty(example = "null", value = "The amount of time to delay the delivery of messages to clients using the Client Profile after the initial message has been delivered (the eliding delay interval), in milliseconds. A value of 0 means there is no delay in delivering messages to clients. The default value is `0`.")
  public Long getElidingDelay() {
    return elidingDelay;
  }

  public void setElidingDelay(Long elidingDelay) {
    this.elidingDelay = elidingDelay;
  }

  public MsgVpnClientProfile elidingEnabled(Boolean elidingEnabled) {
    this.elidingEnabled = elidingEnabled;
    return this;
  }

   /**
   * Enable or disable message eliding for clients using the Client Profile. The default value is `false`.
   * @return elidingEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable message eliding for clients using the Client Profile. The default value is `false`.")
  public Boolean getElidingEnabled() {
    return elidingEnabled;
  }

  public void setElidingEnabled(Boolean elidingEnabled) {
    this.elidingEnabled = elidingEnabled;
  }

  public MsgVpnClientProfile elidingMaxTopicCount(Long elidingMaxTopicCount) {
    this.elidingMaxTopicCount = elidingMaxTopicCount;
    return this;
  }

   /**
   * The maximum number of topics tracked for message eliding per client connection using the Client Profile. The default value is `256`.
   * @return elidingMaxTopicCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of topics tracked for message eliding per client connection using the Client Profile. The default value is `256`.")
  public Long getElidingMaxTopicCount() {
    return elidingMaxTopicCount;
  }

  public void setElidingMaxTopicCount(Long elidingMaxTopicCount) {
    this.elidingMaxTopicCount = elidingMaxTopicCount;
  }

  public MsgVpnClientProfile eventClientProvisionedEndpointSpoolUsageThreshold(EventThresholdByPercent eventClientProvisionedEndpointSpoolUsageThreshold) {
    this.eventClientProvisionedEndpointSpoolUsageThreshold = eventClientProvisionedEndpointSpoolUsageThreshold;
    return this;
  }

   /**
   * Get eventClientProvisionedEndpointSpoolUsageThreshold
   * @return eventClientProvisionedEndpointSpoolUsageThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThresholdByPercent getEventClientProvisionedEndpointSpoolUsageThreshold() {
    return eventClientProvisionedEndpointSpoolUsageThreshold;
  }

  public void setEventClientProvisionedEndpointSpoolUsageThreshold(EventThresholdByPercent eventClientProvisionedEndpointSpoolUsageThreshold) {
    this.eventClientProvisionedEndpointSpoolUsageThreshold = eventClientProvisionedEndpointSpoolUsageThreshold;
  }

  public MsgVpnClientProfile eventConnectionCountPerClientUsernameThreshold(EventThreshold eventConnectionCountPerClientUsernameThreshold) {
    this.eventConnectionCountPerClientUsernameThreshold = eventConnectionCountPerClientUsernameThreshold;
    return this;
  }

   /**
   * Get eventConnectionCountPerClientUsernameThreshold
   * @return eventConnectionCountPerClientUsernameThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThreshold getEventConnectionCountPerClientUsernameThreshold() {
    return eventConnectionCountPerClientUsernameThreshold;
  }

  public void setEventConnectionCountPerClientUsernameThreshold(EventThreshold eventConnectionCountPerClientUsernameThreshold) {
    this.eventConnectionCountPerClientUsernameThreshold = eventConnectionCountPerClientUsernameThreshold;
  }

  public MsgVpnClientProfile eventEgressFlowCountThreshold(EventThreshold eventEgressFlowCountThreshold) {
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

  public MsgVpnClientProfile eventEndpointCountPerClientUsernameThreshold(EventThreshold eventEndpointCountPerClientUsernameThreshold) {
    this.eventEndpointCountPerClientUsernameThreshold = eventEndpointCountPerClientUsernameThreshold;
    return this;
  }

   /**
   * Get eventEndpointCountPerClientUsernameThreshold
   * @return eventEndpointCountPerClientUsernameThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThreshold getEventEndpointCountPerClientUsernameThreshold() {
    return eventEndpointCountPerClientUsernameThreshold;
  }

  public void setEventEndpointCountPerClientUsernameThreshold(EventThreshold eventEndpointCountPerClientUsernameThreshold) {
    this.eventEndpointCountPerClientUsernameThreshold = eventEndpointCountPerClientUsernameThreshold;
  }

  public MsgVpnClientProfile eventIngressFlowCountThreshold(EventThreshold eventIngressFlowCountThreshold) {
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

  public MsgVpnClientProfile eventServiceSmfConnectionCountPerClientUsernameThreshold(EventThreshold eventServiceSmfConnectionCountPerClientUsernameThreshold) {
    this.eventServiceSmfConnectionCountPerClientUsernameThreshold = eventServiceSmfConnectionCountPerClientUsernameThreshold;
    return this;
  }

   /**
   * Get eventServiceSmfConnectionCountPerClientUsernameThreshold
   * @return eventServiceSmfConnectionCountPerClientUsernameThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThreshold getEventServiceSmfConnectionCountPerClientUsernameThreshold() {
    return eventServiceSmfConnectionCountPerClientUsernameThreshold;
  }

  public void setEventServiceSmfConnectionCountPerClientUsernameThreshold(EventThreshold eventServiceSmfConnectionCountPerClientUsernameThreshold) {
    this.eventServiceSmfConnectionCountPerClientUsernameThreshold = eventServiceSmfConnectionCountPerClientUsernameThreshold;
  }

  public MsgVpnClientProfile eventServiceWebConnectionCountPerClientUsernameThreshold(EventThreshold eventServiceWebConnectionCountPerClientUsernameThreshold) {
    this.eventServiceWebConnectionCountPerClientUsernameThreshold = eventServiceWebConnectionCountPerClientUsernameThreshold;
    return this;
  }

   /**
   * Get eventServiceWebConnectionCountPerClientUsernameThreshold
   * @return eventServiceWebConnectionCountPerClientUsernameThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThreshold getEventServiceWebConnectionCountPerClientUsernameThreshold() {
    return eventServiceWebConnectionCountPerClientUsernameThreshold;
  }

  public void setEventServiceWebConnectionCountPerClientUsernameThreshold(EventThreshold eventServiceWebConnectionCountPerClientUsernameThreshold) {
    this.eventServiceWebConnectionCountPerClientUsernameThreshold = eventServiceWebConnectionCountPerClientUsernameThreshold;
  }

  public MsgVpnClientProfile eventSubscriptionCountThreshold(EventThreshold eventSubscriptionCountThreshold) {
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

  public MsgVpnClientProfile eventTransactedSessionCountThreshold(EventThreshold eventTransactedSessionCountThreshold) {
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

  public MsgVpnClientProfile eventTransactionCountThreshold(EventThreshold eventTransactionCountThreshold) {
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

  public MsgVpnClientProfile maxConnectionCountPerClientUsername(Long maxConnectionCountPerClientUsername) {
    this.maxConnectionCountPerClientUsername = maxConnectionCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of client connections per Client Username using the Client Profile. The default is the max value supported by the platform.
   * @return maxConnectionCountPerClientUsername
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of client connections per Client Username using the Client Profile. The default is the max value supported by the platform.")
  public Long getMaxConnectionCountPerClientUsername() {
    return maxConnectionCountPerClientUsername;
  }

  public void setMaxConnectionCountPerClientUsername(Long maxConnectionCountPerClientUsername) {
    this.maxConnectionCountPerClientUsername = maxConnectionCountPerClientUsername;
  }

  public MsgVpnClientProfile maxEgressFlowCount(Long maxEgressFlowCount) {
    this.maxEgressFlowCount = maxEgressFlowCount;
    return this;
  }

   /**
   * The maximum number of transmit flows that can be created by one client using the Client Profile. The default value is `1000`.
   * @return maxEgressFlowCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of transmit flows that can be created by one client using the Client Profile. The default value is `1000`.")
  public Long getMaxEgressFlowCount() {
    return maxEgressFlowCount;
  }

  public void setMaxEgressFlowCount(Long maxEgressFlowCount) {
    this.maxEgressFlowCount = maxEgressFlowCount;
  }

  public MsgVpnClientProfile maxEndpointCountPerClientUsername(Long maxEndpointCountPerClientUsername) {
    this.maxEndpointCountPerClientUsername = maxEndpointCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of queues and topic endpoints that can be created by clients with the same Client Username using the Client Profile. The default value is `1000`.
   * @return maxEndpointCountPerClientUsername
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of queues and topic endpoints that can be created by clients with the same Client Username using the Client Profile. The default value is `1000`.")
  public Long getMaxEndpointCountPerClientUsername() {
    return maxEndpointCountPerClientUsername;
  }

  public void setMaxEndpointCountPerClientUsername(Long maxEndpointCountPerClientUsername) {
    this.maxEndpointCountPerClientUsername = maxEndpointCountPerClientUsername;
  }

  public MsgVpnClientProfile maxIngressFlowCount(Long maxIngressFlowCount) {
    this.maxIngressFlowCount = maxIngressFlowCount;
    return this;
  }

   /**
   * The maximum number of receive flows that can be created by one client using the Client Profile. The default value is `1000`.
   * @return maxIngressFlowCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of receive flows that can be created by one client using the Client Profile. The default value is `1000`.")
  public Long getMaxIngressFlowCount() {
    return maxIngressFlowCount;
  }

  public void setMaxIngressFlowCount(Long maxIngressFlowCount) {
    this.maxIngressFlowCount = maxIngressFlowCount;
  }

  public MsgVpnClientProfile maxSubscriptionCount(Long maxSubscriptionCount) {
    this.maxSubscriptionCount = maxSubscriptionCount;
    return this;
  }

   /**
   * The maximum number of subscriptions per client using the Client Profile. The default varies by platform.
   * @return maxSubscriptionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of subscriptions per client using the Client Profile. The default varies by platform.")
  public Long getMaxSubscriptionCount() {
    return maxSubscriptionCount;
  }

  public void setMaxSubscriptionCount(Long maxSubscriptionCount) {
    this.maxSubscriptionCount = maxSubscriptionCount;
  }

  public MsgVpnClientProfile maxTransactedSessionCount(Long maxTransactedSessionCount) {
    this.maxTransactedSessionCount = maxTransactedSessionCount;
    return this;
  }

   /**
   * The maximum number of transacted sessions that can be created by one client using the Client Profile. The default value is `10`.
   * @return maxTransactedSessionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of transacted sessions that can be created by one client using the Client Profile. The default value is `10`.")
  public Long getMaxTransactedSessionCount() {
    return maxTransactedSessionCount;
  }

  public void setMaxTransactedSessionCount(Long maxTransactedSessionCount) {
    this.maxTransactedSessionCount = maxTransactedSessionCount;
  }

  public MsgVpnClientProfile maxTransactionCount(Long maxTransactionCount) {
    this.maxTransactionCount = maxTransactionCount;
    return this;
  }

   /**
   * The maximum number of transactions that can be created by one client using the Client Profile. The default varies by platform.
   * @return maxTransactionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of transactions that can be created by one client using the Client Profile. The default varies by platform.")
  public Long getMaxTransactionCount() {
    return maxTransactionCount;
  }

  public void setMaxTransactionCount(Long maxTransactionCount) {
    this.maxTransactionCount = maxTransactionCount;
  }

  public MsgVpnClientProfile msgVpnName(String msgVpnName) {
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

  public MsgVpnClientProfile queueControl1MaxDepth(Integer queueControl1MaxDepth) {
    this.queueControl1MaxDepth = queueControl1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \"Control 1\" (C-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.
   * @return queueControl1MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the \"Control 1\" (C-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.")
  public Integer getQueueControl1MaxDepth() {
    return queueControl1MaxDepth;
  }

  public void setQueueControl1MaxDepth(Integer queueControl1MaxDepth) {
    this.queueControl1MaxDepth = queueControl1MaxDepth;
  }

  public MsgVpnClientProfile queueControl1MinMsgBurst(Integer queueControl1MinMsgBurst) {
    this.queueControl1MinMsgBurst = queueControl1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \"Control 1\" (C-1) priority queue, regardless of the `queueControl1MaxDepth` value. The default value is `4`.
   * @return queueControl1MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The number of messages that are always allowed entry into the \"Control 1\" (C-1) priority queue, regardless of the `queueControl1MaxDepth` value. The default value is `4`.")
  public Integer getQueueControl1MinMsgBurst() {
    return queueControl1MinMsgBurst;
  }

  public void setQueueControl1MinMsgBurst(Integer queueControl1MinMsgBurst) {
    this.queueControl1MinMsgBurst = queueControl1MinMsgBurst;
  }

  public MsgVpnClientProfile queueDirect1MaxDepth(Integer queueDirect1MaxDepth) {
    this.queueDirect1MaxDepth = queueDirect1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \"Direct 1\" (D-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.
   * @return queueDirect1MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the \"Direct 1\" (D-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.")
  public Integer getQueueDirect1MaxDepth() {
    return queueDirect1MaxDepth;
  }

  public void setQueueDirect1MaxDepth(Integer queueDirect1MaxDepth) {
    this.queueDirect1MaxDepth = queueDirect1MaxDepth;
  }

  public MsgVpnClientProfile queueDirect1MinMsgBurst(Integer queueDirect1MinMsgBurst) {
    this.queueDirect1MinMsgBurst = queueDirect1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \"Direct 1\" (D-1) priority queue, regardless of the `queueDirect1MaxDepth` value. The default value is `4`.
   * @return queueDirect1MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The number of messages that are always allowed entry into the \"Direct 1\" (D-1) priority queue, regardless of the `queueDirect1MaxDepth` value. The default value is `4`.")
  public Integer getQueueDirect1MinMsgBurst() {
    return queueDirect1MinMsgBurst;
  }

  public void setQueueDirect1MinMsgBurst(Integer queueDirect1MinMsgBurst) {
    this.queueDirect1MinMsgBurst = queueDirect1MinMsgBurst;
  }

  public MsgVpnClientProfile queueDirect2MaxDepth(Integer queueDirect2MaxDepth) {
    this.queueDirect2MaxDepth = queueDirect2MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \"Direct 2\" (D-2) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.
   * @return queueDirect2MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the \"Direct 2\" (D-2) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.")
  public Integer getQueueDirect2MaxDepth() {
    return queueDirect2MaxDepth;
  }

  public void setQueueDirect2MaxDepth(Integer queueDirect2MaxDepth) {
    this.queueDirect2MaxDepth = queueDirect2MaxDepth;
  }

  public MsgVpnClientProfile queueDirect2MinMsgBurst(Integer queueDirect2MinMsgBurst) {
    this.queueDirect2MinMsgBurst = queueDirect2MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \"Direct 2\" (D-2) priority queue, regardless of the `queueDirect2MaxDepth` value. The default value is `4`.
   * @return queueDirect2MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The number of messages that are always allowed entry into the \"Direct 2\" (D-2) priority queue, regardless of the `queueDirect2MaxDepth` value. The default value is `4`.")
  public Integer getQueueDirect2MinMsgBurst() {
    return queueDirect2MinMsgBurst;
  }

  public void setQueueDirect2MinMsgBurst(Integer queueDirect2MinMsgBurst) {
    this.queueDirect2MinMsgBurst = queueDirect2MinMsgBurst;
  }

  public MsgVpnClientProfile queueDirect3MaxDepth(Integer queueDirect3MaxDepth) {
    this.queueDirect3MaxDepth = queueDirect3MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \"Direct 3\" (D-3) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.
   * @return queueDirect3MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the \"Direct 3\" (D-3) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.")
  public Integer getQueueDirect3MaxDepth() {
    return queueDirect3MaxDepth;
  }

  public void setQueueDirect3MaxDepth(Integer queueDirect3MaxDepth) {
    this.queueDirect3MaxDepth = queueDirect3MaxDepth;
  }

  public MsgVpnClientProfile queueDirect3MinMsgBurst(Integer queueDirect3MinMsgBurst) {
    this.queueDirect3MinMsgBurst = queueDirect3MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \"Direct 3\" (D-3) priority queue, regardless of the `queueDirect3MaxDepth` value. The default value is `4`.
   * @return queueDirect3MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The number of messages that are always allowed entry into the \"Direct 3\" (D-3) priority queue, regardless of the `queueDirect3MaxDepth` value. The default value is `4`.")
  public Integer getQueueDirect3MinMsgBurst() {
    return queueDirect3MinMsgBurst;
  }

  public void setQueueDirect3MinMsgBurst(Integer queueDirect3MinMsgBurst) {
    this.queueDirect3MinMsgBurst = queueDirect3MinMsgBurst;
  }

  public MsgVpnClientProfile queueGuaranteed1MaxDepth(Integer queueGuaranteed1MaxDepth) {
    this.queueGuaranteed1MaxDepth = queueGuaranteed1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \"Guaranteed 1\" (G-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.
   * @return queueGuaranteed1MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the \"Guaranteed 1\" (G-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.")
  public Integer getQueueGuaranteed1MaxDepth() {
    return queueGuaranteed1MaxDepth;
  }

  public void setQueueGuaranteed1MaxDepth(Integer queueGuaranteed1MaxDepth) {
    this.queueGuaranteed1MaxDepth = queueGuaranteed1MaxDepth;
  }

  public MsgVpnClientProfile queueGuaranteed1MinMsgBurst(Integer queueGuaranteed1MinMsgBurst) {
    this.queueGuaranteed1MinMsgBurst = queueGuaranteed1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \"Guaranteed 1\" (G-3) priority queue, regardless of the `queueGuaranteed1MaxDepth` value. The default value is `255`.
   * @return queueGuaranteed1MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The number of messages that are always allowed entry into the \"Guaranteed 1\" (G-3) priority queue, regardless of the `queueGuaranteed1MaxDepth` value. The default value is `255`.")
  public Integer getQueueGuaranteed1MinMsgBurst() {
    return queueGuaranteed1MinMsgBurst;
  }

  public void setQueueGuaranteed1MinMsgBurst(Integer queueGuaranteed1MinMsgBurst) {
    this.queueGuaranteed1MinMsgBurst = queueGuaranteed1MinMsgBurst;
  }

  public MsgVpnClientProfile rejectMsgToSenderOnNoSubscriptionMatchEnabled(Boolean rejectMsgToSenderOnNoSubscriptionMatchEnabled) {
    this.rejectMsgToSenderOnNoSubscriptionMatchEnabled = rejectMsgToSenderOnNoSubscriptionMatchEnabled;
    return this;
  }

   /**
   * Enable or disable the sending of a negative acknowledgement (NACK) to a client using the Client Profile when discarding a guaranteed message due to no matching subscription found. The default value is `false`. Available since 2.2.
   * @return rejectMsgToSenderOnNoSubscriptionMatchEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the sending of a negative acknowledgement (NACK) to a client using the Client Profile when discarding a guaranteed message due to no matching subscription found. The default value is `false`. Available since 2.2.")
  public Boolean getRejectMsgToSenderOnNoSubscriptionMatchEnabled() {
    return rejectMsgToSenderOnNoSubscriptionMatchEnabled;
  }

  public void setRejectMsgToSenderOnNoSubscriptionMatchEnabled(Boolean rejectMsgToSenderOnNoSubscriptionMatchEnabled) {
    this.rejectMsgToSenderOnNoSubscriptionMatchEnabled = rejectMsgToSenderOnNoSubscriptionMatchEnabled;
  }

  public MsgVpnClientProfile replicationAllowClientConnectWhenStandbyEnabled(Boolean replicationAllowClientConnectWhenStandbyEnabled) {
    this.replicationAllowClientConnectWhenStandbyEnabled = replicationAllowClientConnectWhenStandbyEnabled;
    return this;
  }

   /**
   * Enable or disable allowing clients using the Client Profile to connect to the Message VPN when its replication state is standby. The default value is `false`.
   * @return replicationAllowClientConnectWhenStandbyEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing clients using the Client Profile to connect to the Message VPN when its replication state is standby. The default value is `false`.")
  public Boolean getReplicationAllowClientConnectWhenStandbyEnabled() {
    return replicationAllowClientConnectWhenStandbyEnabled;
  }

  public void setReplicationAllowClientConnectWhenStandbyEnabled(Boolean replicationAllowClientConnectWhenStandbyEnabled) {
    this.replicationAllowClientConnectWhenStandbyEnabled = replicationAllowClientConnectWhenStandbyEnabled;
  }

  public MsgVpnClientProfile serviceSmfMaxConnectionCountPerClientUsername(Long serviceSmfMaxConnectionCountPerClientUsername) {
    this.serviceSmfMaxConnectionCountPerClientUsername = serviceSmfMaxConnectionCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of SMF client connections per Client Username using the Client Profile. The default is the max value supported by the platform.
   * @return serviceSmfMaxConnectionCountPerClientUsername
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of SMF client connections per Client Username using the Client Profile. The default is the max value supported by the platform.")
  public Long getServiceSmfMaxConnectionCountPerClientUsername() {
    return serviceSmfMaxConnectionCountPerClientUsername;
  }

  public void setServiceSmfMaxConnectionCountPerClientUsername(Long serviceSmfMaxConnectionCountPerClientUsername) {
    this.serviceSmfMaxConnectionCountPerClientUsername = serviceSmfMaxConnectionCountPerClientUsername;
  }

  public MsgVpnClientProfile serviceWebInactiveTimeout(Long serviceWebInactiveTimeout) {
    this.serviceWebInactiveTimeout = serviceWebInactiveTimeout;
    return this;
  }

   /**
   * The timeout for inactive Web Transport client sessions using the Client Profile, in seconds. The default value is `30`.
   * @return serviceWebInactiveTimeout
  **/
  @ApiModelProperty(example = "null", value = "The timeout for inactive Web Transport client sessions using the Client Profile, in seconds. The default value is `30`.")
  public Long getServiceWebInactiveTimeout() {
    return serviceWebInactiveTimeout;
  }

  public void setServiceWebInactiveTimeout(Long serviceWebInactiveTimeout) {
    this.serviceWebInactiveTimeout = serviceWebInactiveTimeout;
  }

  public MsgVpnClientProfile serviceWebMaxConnectionCountPerClientUsername(Long serviceWebMaxConnectionCountPerClientUsername) {
    this.serviceWebMaxConnectionCountPerClientUsername = serviceWebMaxConnectionCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of Web Transport client connections per Client Username using the Client Profile. The default is the max value supported by the platform.
   * @return serviceWebMaxConnectionCountPerClientUsername
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of Web Transport client connections per Client Username using the Client Profile. The default is the max value supported by the platform.")
  public Long getServiceWebMaxConnectionCountPerClientUsername() {
    return serviceWebMaxConnectionCountPerClientUsername;
  }

  public void setServiceWebMaxConnectionCountPerClientUsername(Long serviceWebMaxConnectionCountPerClientUsername) {
    this.serviceWebMaxConnectionCountPerClientUsername = serviceWebMaxConnectionCountPerClientUsername;
  }

  public MsgVpnClientProfile serviceWebMaxPayload(Long serviceWebMaxPayload) {
    this.serviceWebMaxPayload = serviceWebMaxPayload;
    return this;
  }

   /**
   * The maximum Web Transport payload size before fragmentation occurs for clients using the Client Profile, in bytes. The size of the header is not included. The default value is `1000000`.
   * @return serviceWebMaxPayload
  **/
  @ApiModelProperty(example = "null", value = "The maximum Web Transport payload size before fragmentation occurs for clients using the Client Profile, in bytes. The size of the header is not included. The default value is `1000000`.")
  public Long getServiceWebMaxPayload() {
    return serviceWebMaxPayload;
  }

  public void setServiceWebMaxPayload(Long serviceWebMaxPayload) {
    this.serviceWebMaxPayload = serviceWebMaxPayload;
  }

  public MsgVpnClientProfile tcpCongestionWindowSize(Long tcpCongestionWindowSize) {
    this.tcpCongestionWindowSize = tcpCongestionWindowSize;
    return this;
  }

   /**
   * The TCP initial congestion window size for clients using the Client Profile, in multiples of the TCP Maximum Segment Size (MSS). Changing the value from its default of 2 results in non-compliance with RFC 2581. Contact Solace Support before changing this value. The default value is `2`.
   * @return tcpCongestionWindowSize
  **/
  @ApiModelProperty(example = "null", value = "The TCP initial congestion window size for clients using the Client Profile, in multiples of the TCP Maximum Segment Size (MSS). Changing the value from its default of 2 results in non-compliance with RFC 2581. Contact Solace Support before changing this value. The default value is `2`.")
  public Long getTcpCongestionWindowSize() {
    return tcpCongestionWindowSize;
  }

  public void setTcpCongestionWindowSize(Long tcpCongestionWindowSize) {
    this.tcpCongestionWindowSize = tcpCongestionWindowSize;
  }

  public MsgVpnClientProfile tcpKeepaliveCount(Long tcpKeepaliveCount) {
    this.tcpKeepaliveCount = tcpKeepaliveCount;
    return this;
  }

   /**
   * The number of TCP keepalive retransmissions to a client using the Client Profile before declaring that it is not available. The default value is `5`.
   * @return tcpKeepaliveCount
  **/
  @ApiModelProperty(example = "null", value = "The number of TCP keepalive retransmissions to a client using the Client Profile before declaring that it is not available. The default value is `5`.")
  public Long getTcpKeepaliveCount() {
    return tcpKeepaliveCount;
  }

  public void setTcpKeepaliveCount(Long tcpKeepaliveCount) {
    this.tcpKeepaliveCount = tcpKeepaliveCount;
  }

  public MsgVpnClientProfile tcpKeepaliveIdleTime(Long tcpKeepaliveIdleTime) {
    this.tcpKeepaliveIdleTime = tcpKeepaliveIdleTime;
    return this;
  }

   /**
   * The amount of time a client connection using the Client Profile must remain idle before TCP begins sending keepalive probes, in seconds. The default value is `3`.
   * @return tcpKeepaliveIdleTime
  **/
  @ApiModelProperty(example = "null", value = "The amount of time a client connection using the Client Profile must remain idle before TCP begins sending keepalive probes, in seconds. The default value is `3`.")
  public Long getTcpKeepaliveIdleTime() {
    return tcpKeepaliveIdleTime;
  }

  public void setTcpKeepaliveIdleTime(Long tcpKeepaliveIdleTime) {
    this.tcpKeepaliveIdleTime = tcpKeepaliveIdleTime;
  }

  public MsgVpnClientProfile tcpKeepaliveInterval(Long tcpKeepaliveInterval) {
    this.tcpKeepaliveInterval = tcpKeepaliveInterval;
    return this;
  }

   /**
   * The amount of time between TCP keepalive retransmissions to a client using the Client Profile when no acknowledgement is received, in seconds. The default value is `1`.
   * @return tcpKeepaliveInterval
  **/
  @ApiModelProperty(example = "null", value = "The amount of time between TCP keepalive retransmissions to a client using the Client Profile when no acknowledgement is received, in seconds. The default value is `1`.")
  public Long getTcpKeepaliveInterval() {
    return tcpKeepaliveInterval;
  }

  public void setTcpKeepaliveInterval(Long tcpKeepaliveInterval) {
    this.tcpKeepaliveInterval = tcpKeepaliveInterval;
  }

  public MsgVpnClientProfile tcpMaxSegmentSize(Long tcpMaxSegmentSize) {
    this.tcpMaxSegmentSize = tcpMaxSegmentSize;
    return this;
  }

   /**
   * The TCP maximum segment size for clients using the Client Profile, in kilobytes. Changes are applied to all existing connections. The default value is `1460`.
   * @return tcpMaxSegmentSize
  **/
  @ApiModelProperty(example = "null", value = "The TCP maximum segment size for clients using the Client Profile, in kilobytes. Changes are applied to all existing connections. The default value is `1460`.")
  public Long getTcpMaxSegmentSize() {
    return tcpMaxSegmentSize;
  }

  public void setTcpMaxSegmentSize(Long tcpMaxSegmentSize) {
    this.tcpMaxSegmentSize = tcpMaxSegmentSize;
  }

  public MsgVpnClientProfile tcpMaxWindowSize(Long tcpMaxWindowSize) {
    this.tcpMaxWindowSize = tcpMaxWindowSize;
    return this;
  }

   /**
   * The TCP maximum window size for clients using the Client Profile, in kilobytes. Changes are applied to all existing connections. The default value is `256`.
   * @return tcpMaxWindowSize
  **/
  @ApiModelProperty(example = "null", value = "The TCP maximum window size for clients using the Client Profile, in kilobytes. Changes are applied to all existing connections. The default value is `256`.")
  public Long getTcpMaxWindowSize() {
    return tcpMaxWindowSize;
  }

  public void setTcpMaxWindowSize(Long tcpMaxWindowSize) {
    this.tcpMaxWindowSize = tcpMaxWindowSize;
  }

  public MsgVpnClientProfile tlsAllowDowngradeToPlainTextEnabled(Boolean tlsAllowDowngradeToPlainTextEnabled) {
    this.tlsAllowDowngradeToPlainTextEnabled = tlsAllowDowngradeToPlainTextEnabled;
    return this;
  }

   /**
   * Enable or disable allowing a client using the Client Profile to downgrade an encrypted connection to plain text. The default value is `true`. Available since 2.8.
   * @return tlsAllowDowngradeToPlainTextEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing a client using the Client Profile to downgrade an encrypted connection to plain text. The default value is `true`. Available since 2.8.")
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
    MsgVpnClientProfile msgVpnClientProfile = (MsgVpnClientProfile) o;
    return Objects.equals(this.allowBridgeConnectionsEnabled, msgVpnClientProfile.allowBridgeConnectionsEnabled) &&
        Objects.equals(this.allowCutThroughForwardingEnabled, msgVpnClientProfile.allowCutThroughForwardingEnabled) &&
        Objects.equals(this.allowGuaranteedEndpointCreateEnabled, msgVpnClientProfile.allowGuaranteedEndpointCreateEnabled) &&
        Objects.equals(this.allowGuaranteedMsgReceiveEnabled, msgVpnClientProfile.allowGuaranteedMsgReceiveEnabled) &&
        Objects.equals(this.allowGuaranteedMsgSendEnabled, msgVpnClientProfile.allowGuaranteedMsgSendEnabled) &&
        Objects.equals(this.allowSharedSubscriptionsEnabled, msgVpnClientProfile.allowSharedSubscriptionsEnabled) &&
        Objects.equals(this.allowTransactedSessionsEnabled, msgVpnClientProfile.allowTransactedSessionsEnabled) &&
        Objects.equals(this.apiQueueManagementCopyFromOnCreateName, msgVpnClientProfile.apiQueueManagementCopyFromOnCreateName) &&
        Objects.equals(this.apiTopicEndpointManagementCopyFromOnCreateName, msgVpnClientProfile.apiTopicEndpointManagementCopyFromOnCreateName) &&
        Objects.equals(this.clientProfileName, msgVpnClientProfile.clientProfileName) &&
        Objects.equals(this.compressionEnabled, msgVpnClientProfile.compressionEnabled) &&
        Objects.equals(this.elidingDelay, msgVpnClientProfile.elidingDelay) &&
        Objects.equals(this.elidingEnabled, msgVpnClientProfile.elidingEnabled) &&
        Objects.equals(this.elidingMaxTopicCount, msgVpnClientProfile.elidingMaxTopicCount) &&
        Objects.equals(this.eventClientProvisionedEndpointSpoolUsageThreshold, msgVpnClientProfile.eventClientProvisionedEndpointSpoolUsageThreshold) &&
        Objects.equals(this.eventConnectionCountPerClientUsernameThreshold, msgVpnClientProfile.eventConnectionCountPerClientUsernameThreshold) &&
        Objects.equals(this.eventEgressFlowCountThreshold, msgVpnClientProfile.eventEgressFlowCountThreshold) &&
        Objects.equals(this.eventEndpointCountPerClientUsernameThreshold, msgVpnClientProfile.eventEndpointCountPerClientUsernameThreshold) &&
        Objects.equals(this.eventIngressFlowCountThreshold, msgVpnClientProfile.eventIngressFlowCountThreshold) &&
        Objects.equals(this.eventServiceSmfConnectionCountPerClientUsernameThreshold, msgVpnClientProfile.eventServiceSmfConnectionCountPerClientUsernameThreshold) &&
        Objects.equals(this.eventServiceWebConnectionCountPerClientUsernameThreshold, msgVpnClientProfile.eventServiceWebConnectionCountPerClientUsernameThreshold) &&
        Objects.equals(this.eventSubscriptionCountThreshold, msgVpnClientProfile.eventSubscriptionCountThreshold) &&
        Objects.equals(this.eventTransactedSessionCountThreshold, msgVpnClientProfile.eventTransactedSessionCountThreshold) &&
        Objects.equals(this.eventTransactionCountThreshold, msgVpnClientProfile.eventTransactionCountThreshold) &&
        Objects.equals(this.maxConnectionCountPerClientUsername, msgVpnClientProfile.maxConnectionCountPerClientUsername) &&
        Objects.equals(this.maxEgressFlowCount, msgVpnClientProfile.maxEgressFlowCount) &&
        Objects.equals(this.maxEndpointCountPerClientUsername, msgVpnClientProfile.maxEndpointCountPerClientUsername) &&
        Objects.equals(this.maxIngressFlowCount, msgVpnClientProfile.maxIngressFlowCount) &&
        Objects.equals(this.maxSubscriptionCount, msgVpnClientProfile.maxSubscriptionCount) &&
        Objects.equals(this.maxTransactedSessionCount, msgVpnClientProfile.maxTransactedSessionCount) &&
        Objects.equals(this.maxTransactionCount, msgVpnClientProfile.maxTransactionCount) &&
        Objects.equals(this.msgVpnName, msgVpnClientProfile.msgVpnName) &&
        Objects.equals(this.queueControl1MaxDepth, msgVpnClientProfile.queueControl1MaxDepth) &&
        Objects.equals(this.queueControl1MinMsgBurst, msgVpnClientProfile.queueControl1MinMsgBurst) &&
        Objects.equals(this.queueDirect1MaxDepth, msgVpnClientProfile.queueDirect1MaxDepth) &&
        Objects.equals(this.queueDirect1MinMsgBurst, msgVpnClientProfile.queueDirect1MinMsgBurst) &&
        Objects.equals(this.queueDirect2MaxDepth, msgVpnClientProfile.queueDirect2MaxDepth) &&
        Objects.equals(this.queueDirect2MinMsgBurst, msgVpnClientProfile.queueDirect2MinMsgBurst) &&
        Objects.equals(this.queueDirect3MaxDepth, msgVpnClientProfile.queueDirect3MaxDepth) &&
        Objects.equals(this.queueDirect3MinMsgBurst, msgVpnClientProfile.queueDirect3MinMsgBurst) &&
        Objects.equals(this.queueGuaranteed1MaxDepth, msgVpnClientProfile.queueGuaranteed1MaxDepth) &&
        Objects.equals(this.queueGuaranteed1MinMsgBurst, msgVpnClientProfile.queueGuaranteed1MinMsgBurst) &&
        Objects.equals(this.rejectMsgToSenderOnNoSubscriptionMatchEnabled, msgVpnClientProfile.rejectMsgToSenderOnNoSubscriptionMatchEnabled) &&
        Objects.equals(this.replicationAllowClientConnectWhenStandbyEnabled, msgVpnClientProfile.replicationAllowClientConnectWhenStandbyEnabled) &&
        Objects.equals(this.serviceSmfMaxConnectionCountPerClientUsername, msgVpnClientProfile.serviceSmfMaxConnectionCountPerClientUsername) &&
        Objects.equals(this.serviceWebInactiveTimeout, msgVpnClientProfile.serviceWebInactiveTimeout) &&
        Objects.equals(this.serviceWebMaxConnectionCountPerClientUsername, msgVpnClientProfile.serviceWebMaxConnectionCountPerClientUsername) &&
        Objects.equals(this.serviceWebMaxPayload, msgVpnClientProfile.serviceWebMaxPayload) &&
        Objects.equals(this.tcpCongestionWindowSize, msgVpnClientProfile.tcpCongestionWindowSize) &&
        Objects.equals(this.tcpKeepaliveCount, msgVpnClientProfile.tcpKeepaliveCount) &&
        Objects.equals(this.tcpKeepaliveIdleTime, msgVpnClientProfile.tcpKeepaliveIdleTime) &&
        Objects.equals(this.tcpKeepaliveInterval, msgVpnClientProfile.tcpKeepaliveInterval) &&
        Objects.equals(this.tcpMaxSegmentSize, msgVpnClientProfile.tcpMaxSegmentSize) &&
        Objects.equals(this.tcpMaxWindowSize, msgVpnClientProfile.tcpMaxWindowSize) &&
        Objects.equals(this.tlsAllowDowngradeToPlainTextEnabled, msgVpnClientProfile.tlsAllowDowngradeToPlainTextEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowBridgeConnectionsEnabled, allowCutThroughForwardingEnabled, allowGuaranteedEndpointCreateEnabled, allowGuaranteedMsgReceiveEnabled, allowGuaranteedMsgSendEnabled, allowSharedSubscriptionsEnabled, allowTransactedSessionsEnabled, apiQueueManagementCopyFromOnCreateName, apiTopicEndpointManagementCopyFromOnCreateName, clientProfileName, compressionEnabled, elidingDelay, elidingEnabled, elidingMaxTopicCount, eventClientProvisionedEndpointSpoolUsageThreshold, eventConnectionCountPerClientUsernameThreshold, eventEgressFlowCountThreshold, eventEndpointCountPerClientUsernameThreshold, eventIngressFlowCountThreshold, eventServiceSmfConnectionCountPerClientUsernameThreshold, eventServiceWebConnectionCountPerClientUsernameThreshold, eventSubscriptionCountThreshold, eventTransactedSessionCountThreshold, eventTransactionCountThreshold, maxConnectionCountPerClientUsername, maxEgressFlowCount, maxEndpointCountPerClientUsername, maxIngressFlowCount, maxSubscriptionCount, maxTransactedSessionCount, maxTransactionCount, msgVpnName, queueControl1MaxDepth, queueControl1MinMsgBurst, queueDirect1MaxDepth, queueDirect1MinMsgBurst, queueDirect2MaxDepth, queueDirect2MinMsgBurst, queueDirect3MaxDepth, queueDirect3MinMsgBurst, queueGuaranteed1MaxDepth, queueGuaranteed1MinMsgBurst, rejectMsgToSenderOnNoSubscriptionMatchEnabled, replicationAllowClientConnectWhenStandbyEnabled, serviceSmfMaxConnectionCountPerClientUsername, serviceWebInactiveTimeout, serviceWebMaxConnectionCountPerClientUsername, serviceWebMaxPayload, tcpCongestionWindowSize, tcpKeepaliveCount, tcpKeepaliveIdleTime, tcpKeepaliveInterval, tcpMaxSegmentSize, tcpMaxWindowSize, tlsAllowDowngradeToPlainTextEnabled);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnClientProfile {\n");
    
    sb.append("    allowBridgeConnectionsEnabled: ").append(toIndentedString(allowBridgeConnectionsEnabled)).append("\n");
    sb.append("    allowCutThroughForwardingEnabled: ").append(toIndentedString(allowCutThroughForwardingEnabled)).append("\n");
    sb.append("    allowGuaranteedEndpointCreateEnabled: ").append(toIndentedString(allowGuaranteedEndpointCreateEnabled)).append("\n");
    sb.append("    allowGuaranteedMsgReceiveEnabled: ").append(toIndentedString(allowGuaranteedMsgReceiveEnabled)).append("\n");
    sb.append("    allowGuaranteedMsgSendEnabled: ").append(toIndentedString(allowGuaranteedMsgSendEnabled)).append("\n");
    sb.append("    allowSharedSubscriptionsEnabled: ").append(toIndentedString(allowSharedSubscriptionsEnabled)).append("\n");
    sb.append("    allowTransactedSessionsEnabled: ").append(toIndentedString(allowTransactedSessionsEnabled)).append("\n");
    sb.append("    apiQueueManagementCopyFromOnCreateName: ").append(toIndentedString(apiQueueManagementCopyFromOnCreateName)).append("\n");
    sb.append("    apiTopicEndpointManagementCopyFromOnCreateName: ").append(toIndentedString(apiTopicEndpointManagementCopyFromOnCreateName)).append("\n");
    sb.append("    clientProfileName: ").append(toIndentedString(clientProfileName)).append("\n");
    sb.append("    compressionEnabled: ").append(toIndentedString(compressionEnabled)).append("\n");
    sb.append("    elidingDelay: ").append(toIndentedString(elidingDelay)).append("\n");
    sb.append("    elidingEnabled: ").append(toIndentedString(elidingEnabled)).append("\n");
    sb.append("    elidingMaxTopicCount: ").append(toIndentedString(elidingMaxTopicCount)).append("\n");
    sb.append("    eventClientProvisionedEndpointSpoolUsageThreshold: ").append(toIndentedString(eventClientProvisionedEndpointSpoolUsageThreshold)).append("\n");
    sb.append("    eventConnectionCountPerClientUsernameThreshold: ").append(toIndentedString(eventConnectionCountPerClientUsernameThreshold)).append("\n");
    sb.append("    eventEgressFlowCountThreshold: ").append(toIndentedString(eventEgressFlowCountThreshold)).append("\n");
    sb.append("    eventEndpointCountPerClientUsernameThreshold: ").append(toIndentedString(eventEndpointCountPerClientUsernameThreshold)).append("\n");
    sb.append("    eventIngressFlowCountThreshold: ").append(toIndentedString(eventIngressFlowCountThreshold)).append("\n");
    sb.append("    eventServiceSmfConnectionCountPerClientUsernameThreshold: ").append(toIndentedString(eventServiceSmfConnectionCountPerClientUsernameThreshold)).append("\n");
    sb.append("    eventServiceWebConnectionCountPerClientUsernameThreshold: ").append(toIndentedString(eventServiceWebConnectionCountPerClientUsernameThreshold)).append("\n");
    sb.append("    eventSubscriptionCountThreshold: ").append(toIndentedString(eventSubscriptionCountThreshold)).append("\n");
    sb.append("    eventTransactedSessionCountThreshold: ").append(toIndentedString(eventTransactedSessionCountThreshold)).append("\n");
    sb.append("    eventTransactionCountThreshold: ").append(toIndentedString(eventTransactionCountThreshold)).append("\n");
    sb.append("    maxConnectionCountPerClientUsername: ").append(toIndentedString(maxConnectionCountPerClientUsername)).append("\n");
    sb.append("    maxEgressFlowCount: ").append(toIndentedString(maxEgressFlowCount)).append("\n");
    sb.append("    maxEndpointCountPerClientUsername: ").append(toIndentedString(maxEndpointCountPerClientUsername)).append("\n");
    sb.append("    maxIngressFlowCount: ").append(toIndentedString(maxIngressFlowCount)).append("\n");
    sb.append("    maxSubscriptionCount: ").append(toIndentedString(maxSubscriptionCount)).append("\n");
    sb.append("    maxTransactedSessionCount: ").append(toIndentedString(maxTransactedSessionCount)).append("\n");
    sb.append("    maxTransactionCount: ").append(toIndentedString(maxTransactionCount)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    queueControl1MaxDepth: ").append(toIndentedString(queueControl1MaxDepth)).append("\n");
    sb.append("    queueControl1MinMsgBurst: ").append(toIndentedString(queueControl1MinMsgBurst)).append("\n");
    sb.append("    queueDirect1MaxDepth: ").append(toIndentedString(queueDirect1MaxDepth)).append("\n");
    sb.append("    queueDirect1MinMsgBurst: ").append(toIndentedString(queueDirect1MinMsgBurst)).append("\n");
    sb.append("    queueDirect2MaxDepth: ").append(toIndentedString(queueDirect2MaxDepth)).append("\n");
    sb.append("    queueDirect2MinMsgBurst: ").append(toIndentedString(queueDirect2MinMsgBurst)).append("\n");
    sb.append("    queueDirect3MaxDepth: ").append(toIndentedString(queueDirect3MaxDepth)).append("\n");
    sb.append("    queueDirect3MinMsgBurst: ").append(toIndentedString(queueDirect3MinMsgBurst)).append("\n");
    sb.append("    queueGuaranteed1MaxDepth: ").append(toIndentedString(queueGuaranteed1MaxDepth)).append("\n");
    sb.append("    queueGuaranteed1MinMsgBurst: ").append(toIndentedString(queueGuaranteed1MinMsgBurst)).append("\n");
    sb.append("    rejectMsgToSenderOnNoSubscriptionMatchEnabled: ").append(toIndentedString(rejectMsgToSenderOnNoSubscriptionMatchEnabled)).append("\n");
    sb.append("    replicationAllowClientConnectWhenStandbyEnabled: ").append(toIndentedString(replicationAllowClientConnectWhenStandbyEnabled)).append("\n");
    sb.append("    serviceSmfMaxConnectionCountPerClientUsername: ").append(toIndentedString(serviceSmfMaxConnectionCountPerClientUsername)).append("\n");
    sb.append("    serviceWebInactiveTimeout: ").append(toIndentedString(serviceWebInactiveTimeout)).append("\n");
    sb.append("    serviceWebMaxConnectionCountPerClientUsername: ").append(toIndentedString(serviceWebMaxConnectionCountPerClientUsername)).append("\n");
    sb.append("    serviceWebMaxPayload: ").append(toIndentedString(serviceWebMaxPayload)).append("\n");
    sb.append("    tcpCongestionWindowSize: ").append(toIndentedString(tcpCongestionWindowSize)).append("\n");
    sb.append("    tcpKeepaliveCount: ").append(toIndentedString(tcpKeepaliveCount)).append("\n");
    sb.append("    tcpKeepaliveIdleTime: ").append(toIndentedString(tcpKeepaliveIdleTime)).append("\n");
    sb.append("    tcpKeepaliveInterval: ").append(toIndentedString(tcpKeepaliveInterval)).append("\n");
    sb.append("    tcpMaxSegmentSize: ").append(toIndentedString(tcpMaxSegmentSize)).append("\n");
    sb.append("    tcpMaxWindowSize: ").append(toIndentedString(tcpMaxWindowSize)).append("\n");
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

