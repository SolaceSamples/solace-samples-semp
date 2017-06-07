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
import io.swagger.client.model.EventThresholdByPercent;


/**
 * MsgVpnClientProfile
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-09-13T14:36:02.848Z")
public class MsgVpnClientProfile   {
  @SerializedName("allowBridgeConnectionsEnabled")
  private Boolean allowBridgeConnectionsEnabled = null;

  @SerializedName("allowCutThroughForwardingEnabled")
  private Boolean allowCutThroughForwardingEnabled = null;

  @SerializedName("allowGuaranteedEndpointCreateEnabled")
  private Boolean allowGuaranteedEndpointCreateEnabled = null;

  @SerializedName("allowGuaranteedMsgReceiveEnabled")
  private Boolean allowGuaranteedMsgReceiveEnabled = null;

  @SerializedName("allowGuaranteedMsgSendEnabled")
  private Boolean allowGuaranteedMsgSendEnabled = null;

  @SerializedName("allowTransactedSessionsEnabled")
  private Boolean allowTransactedSessionsEnabled = null;

  @SerializedName("apiQueueManagementCopyFromOnCreateName")
  private String apiQueueManagementCopyFromOnCreateName = null;

  @SerializedName("apiTopicEndpointManagementCopyFromOnCreateName")
  private String apiTopicEndpointManagementCopyFromOnCreateName = null;

  @SerializedName("clientProfileName")
  private String clientProfileName = null;

  @SerializedName("elidingDelay")
  private Integer elidingDelay = null;

  @SerializedName("elidingEnabled")
  private Boolean elidingEnabled = null;

  @SerializedName("elidingMaxTopicCount")
  private Integer elidingMaxTopicCount = null;

  @SerializedName("eventClientProvisionedEndpointSpoolUsageThreshold")
  private EventThresholdByPercent eventClientProvisionedEndpointSpoolUsageThreshold = null;

  @SerializedName("eventConnectionCountPerClientUsernameThreshold")
  private EventThreshold eventConnectionCountPerClientUsernameThreshold = null;

  @SerializedName("eventEgressFlowCountThreshold")
  private EventThreshold eventEgressFlowCountThreshold = null;

  @SerializedName("eventEndpointCountPerClientUsernameThreshold")
  private EventThreshold eventEndpointCountPerClientUsernameThreshold = null;

  @SerializedName("eventIngressFlowCountThreshold")
  private EventThreshold eventIngressFlowCountThreshold = null;

  @SerializedName("eventServiceSmfConnectionCountPerClientUsernameThreshold")
  private EventThreshold eventServiceSmfConnectionCountPerClientUsernameThreshold = null;

  @SerializedName("eventServiceWebConnectionCountPerClientUsernameThreshold")
  private EventThreshold eventServiceWebConnectionCountPerClientUsernameThreshold = null;

  @SerializedName("eventSubscriptionCountThreshold")
  private EventThreshold eventSubscriptionCountThreshold = null;

  @SerializedName("eventTransactedSessionCountThreshold")
  private EventThreshold eventTransactedSessionCountThreshold = null;

  @SerializedName("eventTransactionCountThreshold")
  private EventThreshold eventTransactionCountThreshold = null;

  @SerializedName("maxConnectionCountPerClientUsername")
  private Integer maxConnectionCountPerClientUsername = null;

  @SerializedName("maxEgressFlowCount")
  private Integer maxEgressFlowCount = null;

  @SerializedName("maxEndpointCountPerClientUsername")
  private Integer maxEndpointCountPerClientUsername = null;

  @SerializedName("maxIngressFlowCount")
  private Integer maxIngressFlowCount = null;

  @SerializedName("maxSubscriptionCount")
  private Integer maxSubscriptionCount = null;

  @SerializedName("maxTransactedSessionCount")
  private Integer maxTransactedSessionCount = null;

  @SerializedName("maxTransactionCount")
  private Integer maxTransactionCount = null;

  @SerializedName("msgVpnName")
  private String msgVpnName = null;

  @SerializedName("queueControl1MaxDepth")
  private Integer queueControl1MaxDepth = null;

  @SerializedName("queueControl1MinMsgBurst")
  private Integer queueControl1MinMsgBurst = null;

  @SerializedName("queueDirect1MaxDepth")
  private Integer queueDirect1MaxDepth = null;

  @SerializedName("queueDirect1MinMsgBurst")
  private Integer queueDirect1MinMsgBurst = null;

  @SerializedName("queueDirect2MaxDepth")
  private Integer queueDirect2MaxDepth = null;

  @SerializedName("queueDirect2MinMsgBurst")
  private Integer queueDirect2MinMsgBurst = null;

  @SerializedName("queueDirect3MaxDepth")
  private Integer queueDirect3MaxDepth = null;

  @SerializedName("queueDirect3MinMsgBurst")
  private Integer queueDirect3MinMsgBurst = null;

  @SerializedName("queueGuaranteed1MaxDepth")
  private Integer queueGuaranteed1MaxDepth = null;

  @SerializedName("queueGuaranteed1MinMsgBurst")
  private Integer queueGuaranteed1MinMsgBurst = null;

  @SerializedName("rejectMsgToSenderOnNoSubscriptionMatchEnabled")
  private Boolean rejectMsgToSenderOnNoSubscriptionMatchEnabled = null;

  @SerializedName("replicationAllowClientConnectWhenStandbyEnabled")
  private Boolean replicationAllowClientConnectWhenStandbyEnabled = null;

  @SerializedName("serviceSmfMaxConnectionCountPerClientUsername")
  private Integer serviceSmfMaxConnectionCountPerClientUsername = null;

  @SerializedName("serviceWebInactiveTimeout")
  private Integer serviceWebInactiveTimeout = null;

  @SerializedName("serviceWebMaxConnectionCountPerClientUsername")
  private Integer serviceWebMaxConnectionCountPerClientUsername = null;

  @SerializedName("serviceWebMaxPayload")
  private Integer serviceWebMaxPayload = null;

  @SerializedName("tcpCongestionWindowSize")
  private Integer tcpCongestionWindowSize = null;

  @SerializedName("tcpKeepaliveCount")
  private Integer tcpKeepaliveCount = null;

  @SerializedName("tcpKeepaliveIdleTime")
  private Integer tcpKeepaliveIdleTime = null;

  @SerializedName("tcpKeepaliveInterval")
  private Integer tcpKeepaliveInterval = null;

  @SerializedName("tcpMaxSegmentSize")
  private Integer tcpMaxSegmentSize = null;

  @SerializedName("tcpMaxWindowSize")
  private Integer tcpMaxWindowSize = null;

  public MsgVpnClientProfile allowBridgeConnectionsEnabled(Boolean allowBridgeConnectionsEnabled) {
    this.allowBridgeConnectionsEnabled = allowBridgeConnectionsEnabled;
    return this;
  }

   /**
   * Enable or disable allowing bridge connections to login. The default value is `false`.
   * @return allowBridgeConnectionsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing bridge connections to login. The default value is `false`.")
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
   * Enable or disable allowing a client to bind to topic endpoints or queues with cut-through forwarding. Changing this value does not affect existing sessions. The default value is `false`.
   * @return allowCutThroughForwardingEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing a client to bind to topic endpoints or queues with cut-through forwarding. Changing this value does not affect existing sessions. The default value is `false`.")
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
   * Enable or disable allowing a client to create topic endponts or queues for the receiving of persistent or non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.
   * @return allowGuaranteedEndpointCreateEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing a client to create topic endponts or queues for the receiving of persistent or non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.")
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
   * Enable or disable allowing a client to bind to topic endpoints or queues for the receiving of persistent or non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.
   * @return allowGuaranteedMsgReceiveEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing a client to bind to topic endpoints or queues for the receiving of persistent or non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.")
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
   * Enable or disable allowing a client to send persistent and non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.
   * @return allowGuaranteedMsgSendEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing a client to send persistent and non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.")
  public Boolean getAllowGuaranteedMsgSendEnabled() {
    return allowGuaranteedMsgSendEnabled;
  }

  public void setAllowGuaranteedMsgSendEnabled(Boolean allowGuaranteedMsgSendEnabled) {
    this.allowGuaranteedMsgSendEnabled = allowGuaranteedMsgSendEnabled;
  }

  public MsgVpnClientProfile allowTransactedSessionsEnabled(Boolean allowTransactedSessionsEnabled) {
    this.allowTransactedSessionsEnabled = allowTransactedSessionsEnabled;
    return this;
  }

   /**
   * Enable or disable allowing a client to use trasacted sessions to bundle persistent or non-persistent message send and receives. Changing this value does not affect existing sessions. The default value is `false`.
   * @return allowTransactedSessionsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable allowing a client to use trasacted sessions to bundle persistent or non-persistent message send and receives. Changing this value does not affect existing sessions. The default value is `false`.")
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
   * The name of a queue to copy settings from when a new queue is created by an API. The referenced queue must exist. The default is to have no `apiQueueManagementCopyFromOnCreateName`.
   * @return apiQueueManagementCopyFromOnCreateName
  **/
  @ApiModelProperty(example = "null", value = "The name of a queue to copy settings from when a new queue is created by an API. The referenced queue must exist. The default is to have no `apiQueueManagementCopyFromOnCreateName`.")
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
   * The name of a topic-endpoint to copy settings from when a new topic-endpoint is created by an API. The referenced topic-endpoint must exist. The default is to have no `apiTopicEndpointManagementCopyFromOnCreateName`.
   * @return apiTopicEndpointManagementCopyFromOnCreateName
  **/
  @ApiModelProperty(example = "null", value = "The name of a topic-endpoint to copy settings from when a new topic-endpoint is created by an API. The referenced topic-endpoint must exist. The default is to have no `apiTopicEndpointManagementCopyFromOnCreateName`.")
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

  public MsgVpnClientProfile elidingDelay(Integer elidingDelay) {
    this.elidingDelay = elidingDelay;
    return this;
  }

   /**
   * The eliding delay interval (in milliseconds). 0 means no delay in delivering the message to the client. The default value is `0`.
   * @return elidingDelay
  **/
  @ApiModelProperty(example = "null", value = "The eliding delay interval (in milliseconds). 0 means no delay in delivering the message to the client. The default value is `0`.")
  public Integer getElidingDelay() {
    return elidingDelay;
  }

  public void setElidingDelay(Integer elidingDelay) {
    this.elidingDelay = elidingDelay;
  }

  public MsgVpnClientProfile elidingEnabled(Boolean elidingEnabled) {
    this.elidingEnabled = elidingEnabled;
    return this;
  }

   /**
   * Enables or disables eliding. The default value is `false`.
   * @return elidingEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enables or disables eliding. The default value is `false`.")
  public Boolean getElidingEnabled() {
    return elidingEnabled;
  }

  public void setElidingEnabled(Boolean elidingEnabled) {
    this.elidingEnabled = elidingEnabled;
  }

  public MsgVpnClientProfile elidingMaxTopicCount(Integer elidingMaxTopicCount) {
    this.elidingMaxTopicCount = elidingMaxTopicCount;
    return this;
  }

   /**
   * The maximum number of topics that can be tracked for eliding on a per client basis. The default value is `256`.
   * @return elidingMaxTopicCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of topics that can be tracked for eliding on a per client basis. The default value is `256`.")
  public Integer getElidingMaxTopicCount() {
    return elidingMaxTopicCount;
  }

  public void setElidingMaxTopicCount(Integer elidingMaxTopicCount) {
    this.elidingMaxTopicCount = elidingMaxTopicCount;
  }

  public MsgVpnClientProfile eventClientProvisionedEndpointSpoolUsageThreshold(EventThresholdByPercent eventClientProvisionedEndpointSpoolUsageThreshold) {
    this.eventClientProvisionedEndpointSpoolUsageThreshold = eventClientProvisionedEndpointSpoolUsageThreshold;
    return this;
  }

   /**
   * The threshold for the provisioned endpoint spool usage event as a percentage of the signalled `maxMsgSpoolUsage`. Changing these values during operation does not affect existing sessions. For provisioned durable endpoints, these settings apply when initially provisioned, but can then be changed at any time afterwards via the associated queue/topic-endpoint message spool usage setting.
   * @return eventClientProvisionedEndpointSpoolUsageThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the provisioned endpoint spool usage event as a percentage of the signalled `maxMsgSpoolUsage`. Changing these values during operation does not affect existing sessions. For provisioned durable endpoints, these settings apply when initially provisioned, but can then be changed at any time afterwards via the associated queue/topic-endpoint message spool usage setting.")
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
   * The threshold for the client-username connection count event of the Client Profile, relative to `maxConnectionCountPerClientUsername`.
   * @return eventConnectionCountPerClientUsernameThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the client-username connection count event of the Client Profile, relative to `maxConnectionCountPerClientUsername`.")
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
   * The threshold for the egress flow count event of the Client Profile, relative to `maxEgressFlowCount`.
   * @return eventEgressFlowCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the egress flow count event of the Client Profile, relative to `maxEgressFlowCount`.")
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
   * The threshold for the client-username endpoint count event of the Client Profile, relative to `maxEndpointCountPerClientUsername`.
   * @return eventEndpointCountPerClientUsernameThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the client-username endpoint count event of the Client Profile, relative to `maxEndpointCountPerClientUsername`.")
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
   * The threshold for the ingress flow count event of the Client Profile, relative to `maxIngressFlowCount`.
   * @return eventIngressFlowCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the ingress flow count event of the Client Profile, relative to `maxIngressFlowCount`.")
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
   * The threshold for the client-username SMF connection count event of the Client Profile, relative to `serviceSmfMaxConnectionCountPerClientUsername`.
   * @return eventServiceSmfConnectionCountPerClientUsernameThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the client-username SMF connection count event of the Client Profile, relative to `serviceSmfMaxConnectionCountPerClientUsername`.")
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
   * The threshold for the client-username web-transport connection count event of the Client Profile, relative to `serviceWebMaxConnectionCountPerClientUsername`.
   * @return eventServiceWebConnectionCountPerClientUsernameThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the client-username web-transport connection count event of the Client Profile, relative to `serviceWebMaxConnectionCountPerClientUsername`.")
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
   * The threshold for the subscription count event of the Client Profile, relative to `maxSubscriptionCount`.
   * @return eventSubscriptionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the subscription count event of the Client Profile, relative to `maxSubscriptionCount`.")
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
   * The threshold for the transacted session count event of the Client Profile, relative to `maxTransactedSessionCount`.
   * @return eventTransactedSessionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the transacted session count event of the Client Profile, relative to `maxTransactedSessionCount`.")
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
   * The threshold for the transaction count event of the Client Profile, relative to `maxTransactionCount`.
   * @return eventTransactionCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the transaction count event of the Client Profile, relative to `maxTransactionCount`.")
  public EventThreshold getEventTransactionCountThreshold() {
    return eventTransactionCountThreshold;
  }

  public void setEventTransactionCountThreshold(EventThreshold eventTransactionCountThreshold) {
    this.eventTransactionCountThreshold = eventTransactionCountThreshold;
  }

  public MsgVpnClientProfile maxConnectionCountPerClientUsername(Integer maxConnectionCountPerClientUsername) {
    this.maxConnectionCountPerClientUsername = maxConnectionCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of client connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
   * @return maxConnectionCountPerClientUsername
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of client connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.")
  public Integer getMaxConnectionCountPerClientUsername() {
    return maxConnectionCountPerClientUsername;
  }

  public void setMaxConnectionCountPerClientUsername(Integer maxConnectionCountPerClientUsername) {
    this.maxConnectionCountPerClientUsername = maxConnectionCountPerClientUsername;
  }

  public MsgVpnClientProfile maxEgressFlowCount(Integer maxEgressFlowCount) {
    this.maxEgressFlowCount = maxEgressFlowCount;
    return this;
  }

   /**
   * The maximum number of egress flows that can be created by a single client associated with this client-profile. The default value is `16000`.
   * @return maxEgressFlowCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of egress flows that can be created by a single client associated with this client-profile. The default value is `16000`.")
  public Integer getMaxEgressFlowCount() {
    return maxEgressFlowCount;
  }

  public void setMaxEgressFlowCount(Integer maxEgressFlowCount) {
    this.maxEgressFlowCount = maxEgressFlowCount;
  }

  public MsgVpnClientProfile maxEndpointCountPerClientUsername(Integer maxEndpointCountPerClientUsername) {
    this.maxEndpointCountPerClientUsername = maxEndpointCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of queues and topic endpoints that can be created across clients using the same client-username associated with this client-profile. The default value is `16000`.
   * @return maxEndpointCountPerClientUsername
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of queues and topic endpoints that can be created across clients using the same client-username associated with this client-profile. The default value is `16000`.")
  public Integer getMaxEndpointCountPerClientUsername() {
    return maxEndpointCountPerClientUsername;
  }

  public void setMaxEndpointCountPerClientUsername(Integer maxEndpointCountPerClientUsername) {
    this.maxEndpointCountPerClientUsername = maxEndpointCountPerClientUsername;
  }

  public MsgVpnClientProfile maxIngressFlowCount(Integer maxIngressFlowCount) {
    this.maxIngressFlowCount = maxIngressFlowCount;
    return this;
  }

   /**
   * The maximum number of ingress flows that can be created by a single client associated with this client-profile. The default value is `16000`.
   * @return maxIngressFlowCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of ingress flows that can be created by a single client associated with this client-profile. The default value is `16000`.")
  public Integer getMaxIngressFlowCount() {
    return maxIngressFlowCount;
  }

  public void setMaxIngressFlowCount(Integer maxIngressFlowCount) {
    this.maxIngressFlowCount = maxIngressFlowCount;
  }

  public MsgVpnClientProfile maxSubscriptionCount(Integer maxSubscriptionCount) {
    this.maxSubscriptionCount = maxSubscriptionCount;
    return this;
  }

   /**
   * The maximum number of subscriptions for a single client associated with this client-profile. The default value is `5000000`.
   * @return maxSubscriptionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of subscriptions for a single client associated with this client-profile. The default value is `5000000`.")
  public Integer getMaxSubscriptionCount() {
    return maxSubscriptionCount;
  }

  public void setMaxSubscriptionCount(Integer maxSubscriptionCount) {
    this.maxSubscriptionCount = maxSubscriptionCount;
  }

  public MsgVpnClientProfile maxTransactedSessionCount(Integer maxTransactedSessionCount) {
    this.maxTransactedSessionCount = maxTransactedSessionCount;
    return this;
  }

   /**
   * The maximum number of transacted sessions that can be created by a single client associated with this client-profile. The default value is `10`.
   * @return maxTransactedSessionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of transacted sessions that can be created by a single client associated with this client-profile. The default value is `10`.")
  public Integer getMaxTransactedSessionCount() {
    return maxTransactedSessionCount;
  }

  public void setMaxTransactedSessionCount(Integer maxTransactedSessionCount) {
    this.maxTransactedSessionCount = maxTransactedSessionCount;
  }

  public MsgVpnClientProfile maxTransactionCount(Integer maxTransactionCount) {
    this.maxTransactionCount = maxTransactionCount;
    return this;
  }

   /**
   * The maximum number of transacted sessions that can be created by a single client associated with this client-profile. The default value is `16000`.
   * @return maxTransactionCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of transacted sessions that can be created by a single client associated with this client-profile. The default value is `16000`.")
  public Integer getMaxTransactionCount() {
    return maxTransactionCount;
  }

  public void setMaxTransactionCount(Integer maxTransactionCount) {
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
   * The maximum depth of the C-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.
   * @return queueControl1MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the C-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.")
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
   * The minimum number of messages that must be on the C-1 queue before its depth is checked against the `queueControl1MaxDepth` setting. The default value is `4`.
   * @return queueControl1MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The minimum number of messages that must be on the C-1 queue before its depth is checked against the `queueControl1MaxDepth` setting. The default value is `4`.")
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
   * The maximum depth of the D-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.
   * @return queueDirect1MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the D-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.")
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
   * The minimum number of messages that must be on the D-1 queue before its depth is checked against the `queueDirect1MaxDepth` setting. The default value is `4`.
   * @return queueDirect1MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The minimum number of messages that must be on the D-1 queue before its depth is checked against the `queueDirect1MaxDepth` setting. The default value is `4`.")
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
   * The maximum depth of the D-2 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.
   * @return queueDirect2MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the D-2 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.")
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
   * The minimum number of messages that must be on the D-2 queue before its depth is checked against the `queueDirect2MaxDepth` setting. The default value is `4`.
   * @return queueDirect2MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The minimum number of messages that must be on the D-2 queue before its depth is checked against the `queueDirect2MaxDepth` setting. The default value is `4`.")
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
   * The maximum depth of the D-3 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.
   * @return queueDirect3MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the D-3 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.")
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
   * The minimum number of messages that must be on the D-3 queue before its depth is checked against the `queueDirect3MaxDepth` setting. The default value is `4`.
   * @return queueDirect3MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The minimum number of messages that must be on the D-3 queue before its depth is checked against the `queueDirect3MaxDepth` setting. The default value is `4`.")
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
   * The maximum depth of the G-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.
   * @return queueGuaranteed1MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the G-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.")
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
   * The minimum number of messages that must be on the G-1 queue before its depth is checked against the `queueGuaranteed1MaxDepth` setting. The default value is `255`.
   * @return queueGuaranteed1MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The minimum number of messages that must be on the G-1 queue before its depth is checked against the `queueGuaranteed1MaxDepth` setting. The default value is `255`.")
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
   * Enable or disable the sending of a negative acknowledgement on the discard of a guaranteed message. When a guaranteed message is published to a topic, and the router does not have any guaranteed subscriptions matching the message topic, the message can either be silently discarded, or a negative acknowledgement can be returned to the sender indicating that no guaranteed subscriptions were found for the message. It should be noted that even if the message is rejected to the publisher, it will still be delivered to any clients who have direct subscriptions to the topic. This configuration option does not affect the behavior of messages published to unknown queue names. That always results in the message being rejected to the sender. The default value is `false`.
   * @return rejectMsgToSenderOnNoSubscriptionMatchEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the sending of a negative acknowledgement on the discard of a guaranteed message. When a guaranteed message is published to a topic, and the router does not have any guaranteed subscriptions matching the message topic, the message can either be silently discarded, or a negative acknowledgement can be returned to the sender indicating that no guaranteed subscriptions were found for the message. It should be noted that even if the message is rejected to the publisher, it will still be delivered to any clients who have direct subscriptions to the topic. This configuration option does not affect the behavior of messages published to unknown queue names. That always results in the message being rejected to the sender. The default value is `false`.")
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
   * Enable or disable whether clients using this client profile are allowed to connect to the Message VPN if its replication is in standby state. The default value is `false`.
   * @return replicationAllowClientConnectWhenStandbyEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable whether clients using this client profile are allowed to connect to the Message VPN if its replication is in standby state. The default value is `false`.")
  public Boolean getReplicationAllowClientConnectWhenStandbyEnabled() {
    return replicationAllowClientConnectWhenStandbyEnabled;
  }

  public void setReplicationAllowClientConnectWhenStandbyEnabled(Boolean replicationAllowClientConnectWhenStandbyEnabled) {
    this.replicationAllowClientConnectWhenStandbyEnabled = replicationAllowClientConnectWhenStandbyEnabled;
  }

  public MsgVpnClientProfile serviceSmfMaxConnectionCountPerClientUsername(Integer serviceSmfMaxConnectionCountPerClientUsername) {
    this.serviceSmfMaxConnectionCountPerClientUsername = serviceSmfMaxConnectionCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of SMF client connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
   * @return serviceSmfMaxConnectionCountPerClientUsername
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of SMF client connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.")
  public Integer getServiceSmfMaxConnectionCountPerClientUsername() {
    return serviceSmfMaxConnectionCountPerClientUsername;
  }

  public void setServiceSmfMaxConnectionCountPerClientUsername(Integer serviceSmfMaxConnectionCountPerClientUsername) {
    this.serviceSmfMaxConnectionCountPerClientUsername = serviceSmfMaxConnectionCountPerClientUsername;
  }

  public MsgVpnClientProfile serviceWebInactiveTimeout(Integer serviceWebInactiveTimeout) {
    this.serviceWebInactiveTimeout = serviceWebInactiveTimeout;
    return this;
  }

   /**
   * The number of seconds during which the client must send a request or else the session is terminated. The default value is `30`.
   * @return serviceWebInactiveTimeout
  **/
  @ApiModelProperty(example = "null", value = "The number of seconds during which the client must send a request or else the session is terminated. The default value is `30`.")
  public Integer getServiceWebInactiveTimeout() {
    return serviceWebInactiveTimeout;
  }

  public void setServiceWebInactiveTimeout(Integer serviceWebInactiveTimeout) {
    this.serviceWebInactiveTimeout = serviceWebInactiveTimeout;
  }

  public MsgVpnClientProfile serviceWebMaxConnectionCountPerClientUsername(Integer serviceWebMaxConnectionCountPerClientUsername) {
    this.serviceWebMaxConnectionCountPerClientUsername = serviceWebMaxConnectionCountPerClientUsername;
    return this;
  }

   /**
   * The maximum number of web-transport connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
   * @return serviceWebMaxConnectionCountPerClientUsername
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of web-transport connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.")
  public Integer getServiceWebMaxConnectionCountPerClientUsername() {
    return serviceWebMaxConnectionCountPerClientUsername;
  }

  public void setServiceWebMaxConnectionCountPerClientUsername(Integer serviceWebMaxConnectionCountPerClientUsername) {
    this.serviceWebMaxConnectionCountPerClientUsername = serviceWebMaxConnectionCountPerClientUsername;
  }

  public MsgVpnClientProfile serviceWebMaxPayload(Integer serviceWebMaxPayload) {
    this.serviceWebMaxPayload = serviceWebMaxPayload;
    return this;
  }

   /**
   * The maximum number of bytes allowed in a single web transport payload before fragmentation occurs, not including the header. The default value is `1000000`.
   * @return serviceWebMaxPayload
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of bytes allowed in a single web transport payload before fragmentation occurs, not including the header. The default value is `1000000`.")
  public Integer getServiceWebMaxPayload() {
    return serviceWebMaxPayload;
  }

  public void setServiceWebMaxPayload(Integer serviceWebMaxPayload) {
    this.serviceWebMaxPayload = serviceWebMaxPayload;
  }

  public MsgVpnClientProfile tcpCongestionWindowSize(Integer tcpCongestionWindowSize) {
    this.tcpCongestionWindowSize = tcpCongestionWindowSize;
    return this;
  }

   /**
   * The TCP initial congestion window size for clients belonging to this profile.   The initial congestion window size is used when starting up a TCP connection or recovery from idle (that is, no traffic). It is the number of segments TCP sends before waiting for an acknowledgement from the peer. Larger values of initial window allows a connection to come up to speed quickly. However, care must be taken for if this parameter's value is too high, it may cause congestion in the network. For further details on initial window, refer to RFC 2581. Changing this parameter changes all clients matching this profile, whether already connected or not.   Changing the initial window from its default of 2 results in non-compliance with RFC 2581. Contact Solace Support personnel before changing this parameter. The default value is `2`.
   * @return tcpCongestionWindowSize
  **/
  @ApiModelProperty(example = "null", value = "The TCP initial congestion window size for clients belonging to this profile.   The initial congestion window size is used when starting up a TCP connection or recovery from idle (that is, no traffic). It is the number of segments TCP sends before waiting for an acknowledgement from the peer. Larger values of initial window allows a connection to come up to speed quickly. However, care must be taken for if this parameter's value is too high, it may cause congestion in the network. For further details on initial window, refer to RFC 2581. Changing this parameter changes all clients matching this profile, whether already connected or not.   Changing the initial window from its default of 2 results in non-compliance with RFC 2581. Contact Solace Support personnel before changing this parameter. The default value is `2`.")
  public Integer getTcpCongestionWindowSize() {
    return tcpCongestionWindowSize;
  }

  public void setTcpCongestionWindowSize(Integer tcpCongestionWindowSize) {
    this.tcpCongestionWindowSize = tcpCongestionWindowSize;
  }

  public MsgVpnClientProfile tcpKeepaliveCount(Integer tcpKeepaliveCount) {
    this.tcpKeepaliveCount = tcpKeepaliveCount;
    return this;
  }

   /**
   * The number of keepalive probes TCP should send before dropping the connection. The default value is `5`.
   * @return tcpKeepaliveCount
  **/
  @ApiModelProperty(example = "null", value = "The number of keepalive probes TCP should send before dropping the connection. The default value is `5`.")
  public Integer getTcpKeepaliveCount() {
    return tcpKeepaliveCount;
  }

  public void setTcpKeepaliveCount(Integer tcpKeepaliveCount) {
    this.tcpKeepaliveCount = tcpKeepaliveCount;
  }

  public MsgVpnClientProfile tcpKeepaliveIdleTime(Integer tcpKeepaliveIdleTime) {
    this.tcpKeepaliveIdleTime = tcpKeepaliveIdleTime;
    return this;
  }

   /**
   * The time (in seconds) a connection needs to remain idle before TCP begins sending keepalive probes. The default value is `3`.
   * @return tcpKeepaliveIdleTime
  **/
  @ApiModelProperty(example = "null", value = "The time (in seconds) a connection needs to remain idle before TCP begins sending keepalive probes. The default value is `3`.")
  public Integer getTcpKeepaliveIdleTime() {
    return tcpKeepaliveIdleTime;
  }

  public void setTcpKeepaliveIdleTime(Integer tcpKeepaliveIdleTime) {
    this.tcpKeepaliveIdleTime = tcpKeepaliveIdleTime;
  }

  public MsgVpnClientProfile tcpKeepaliveInterval(Integer tcpKeepaliveInterval) {
    this.tcpKeepaliveInterval = tcpKeepaliveInterval;
    return this;
  }

   /**
   * The time between individual keepalive probes, when no response is received. The default value is `1`.
   * @return tcpKeepaliveInterval
  **/
  @ApiModelProperty(example = "null", value = "The time between individual keepalive probes, when no response is received. The default value is `1`.")
  public Integer getTcpKeepaliveInterval() {
    return tcpKeepaliveInterval;
  }

  public void setTcpKeepaliveInterval(Integer tcpKeepaliveInterval) {
    this.tcpKeepaliveInterval = tcpKeepaliveInterval;
  }

  public MsgVpnClientProfile tcpMaxSegmentSize(Integer tcpMaxSegmentSize) {
    this.tcpMaxSegmentSize = tcpMaxSegmentSize;
    return this;
  }

   /**
   * The TCP maximum segment size for the clients belonging to this profile. The default value is `1460`.
   * @return tcpMaxSegmentSize
  **/
  @ApiModelProperty(example = "null", value = "The TCP maximum segment size for the clients belonging to this profile. The default value is `1460`.")
  public Integer getTcpMaxSegmentSize() {
    return tcpMaxSegmentSize;
  }

  public void setTcpMaxSegmentSize(Integer tcpMaxSegmentSize) {
    this.tcpMaxSegmentSize = tcpMaxSegmentSize;
  }

  public MsgVpnClientProfile tcpMaxWindowSize(Integer tcpMaxWindowSize) {
    this.tcpMaxWindowSize = tcpMaxWindowSize;
    return this;
  }

   /**
   * The TCP maximum window size (in KB) for clients belonging to this profile. Changes are applied to all existing connections. The maximum window should be at least the bandwidth-delay product of the link between the TCP peers. If the maximum window is less than the bandwidth-delay product, then the TCP connection operates below its maximum potential throughput. If the maximum window is less than about twice the bandwidth-delay product, then occasional packet loss causes TCP connection to operate below its maximum potential throughput as it handles the missing ACKs and retransmissions. There are also problems with a maximum window that's too large. In the presence of a high offered load, TCP gradually increases its congestion window until either (a) the congestion window reaches the maximum window, or (b) packet loss occurs in the network. Initially, when the congestion window is small, the network's physical bandwidth-delay acts as a memory buffer for packets in flight. As the congestion window crosses the bandwidth-delay product, though, the buffering of in-flight packets moves to queues in various switches, routers, etc. in the network. As the congestion window continues to increase, some such queue in some equipment overflows, causing packet loss and TCP back-off. The default value is `256`.
   * @return tcpMaxWindowSize
  **/
  @ApiModelProperty(example = "null", value = "The TCP maximum window size (in KB) for clients belonging to this profile. Changes are applied to all existing connections. The maximum window should be at least the bandwidth-delay product of the link between the TCP peers. If the maximum window is less than the bandwidth-delay product, then the TCP connection operates below its maximum potential throughput. If the maximum window is less than about twice the bandwidth-delay product, then occasional packet loss causes TCP connection to operate below its maximum potential throughput as it handles the missing ACKs and retransmissions. There are also problems with a maximum window that's too large. In the presence of a high offered load, TCP gradually increases its congestion window until either (a) the congestion window reaches the maximum window, or (b) packet loss occurs in the network. Initially, when the congestion window is small, the network's physical bandwidth-delay acts as a memory buffer for packets in flight. As the congestion window crosses the bandwidth-delay product, though, the buffering of in-flight packets moves to queues in various switches, routers, etc. in the network. As the congestion window continues to increase, some such queue in some equipment overflows, causing packet loss and TCP back-off. The default value is `256`.")
  public Integer getTcpMaxWindowSize() {
    return tcpMaxWindowSize;
  }

  public void setTcpMaxWindowSize(Integer tcpMaxWindowSize) {
    this.tcpMaxWindowSize = tcpMaxWindowSize;
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
        Objects.equals(this.allowTransactedSessionsEnabled, msgVpnClientProfile.allowTransactedSessionsEnabled) &&
        Objects.equals(this.apiQueueManagementCopyFromOnCreateName, msgVpnClientProfile.apiQueueManagementCopyFromOnCreateName) &&
        Objects.equals(this.apiTopicEndpointManagementCopyFromOnCreateName, msgVpnClientProfile.apiTopicEndpointManagementCopyFromOnCreateName) &&
        Objects.equals(this.clientProfileName, msgVpnClientProfile.clientProfileName) &&
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
        Objects.equals(this.tcpMaxWindowSize, msgVpnClientProfile.tcpMaxWindowSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allowBridgeConnectionsEnabled, allowCutThroughForwardingEnabled, allowGuaranteedEndpointCreateEnabled, allowGuaranteedMsgReceiveEnabled, allowGuaranteedMsgSendEnabled, allowTransactedSessionsEnabled, apiQueueManagementCopyFromOnCreateName, apiTopicEndpointManagementCopyFromOnCreateName, clientProfileName, elidingDelay, elidingEnabled, elidingMaxTopicCount, eventClientProvisionedEndpointSpoolUsageThreshold, eventConnectionCountPerClientUsernameThreshold, eventEgressFlowCountThreshold, eventEndpointCountPerClientUsernameThreshold, eventIngressFlowCountThreshold, eventServiceSmfConnectionCountPerClientUsernameThreshold, eventServiceWebConnectionCountPerClientUsernameThreshold, eventSubscriptionCountThreshold, eventTransactedSessionCountThreshold, eventTransactionCountThreshold, maxConnectionCountPerClientUsername, maxEgressFlowCount, maxEndpointCountPerClientUsername, maxIngressFlowCount, maxSubscriptionCount, maxTransactedSessionCount, maxTransactionCount, msgVpnName, queueControl1MaxDepth, queueControl1MinMsgBurst, queueDirect1MaxDepth, queueDirect1MinMsgBurst, queueDirect2MaxDepth, queueDirect2MinMsgBurst, queueDirect3MaxDepth, queueDirect3MinMsgBurst, queueGuaranteed1MaxDepth, queueGuaranteed1MinMsgBurst, rejectMsgToSenderOnNoSubscriptionMatchEnabled, replicationAllowClientConnectWhenStandbyEnabled, serviceSmfMaxConnectionCountPerClientUsername, serviceWebInactiveTimeout, serviceWebMaxConnectionCountPerClientUsername, serviceWebMaxPayload, tcpCongestionWindowSize, tcpKeepaliveCount, tcpKeepaliveIdleTime, tcpKeepaliveInterval, tcpMaxSegmentSize, tcpMaxWindowSize);
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
    sb.append("    allowTransactedSessionsEnabled: ").append(toIndentedString(allowTransactedSessionsEnabled)).append("\n");
    sb.append("    apiQueueManagementCopyFromOnCreateName: ").append(toIndentedString(apiQueueManagementCopyFromOnCreateName)).append("\n");
    sb.append("    apiTopicEndpointManagementCopyFromOnCreateName: ").append(toIndentedString(apiTopicEndpointManagementCopyFromOnCreateName)).append("\n");
    sb.append("    clientProfileName: ").append(toIndentedString(clientProfileName)).append("\n");
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

