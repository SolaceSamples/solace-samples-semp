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


/**
 * MsgVpnQueue
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-09-13T14:36:02.848Z")
public class MsgVpnQueue   {
  /**
   * The queue access type of either exclusive or non-exclusive. The default value is `\"exclusive\"`. The allowed values and their meaning are:      \"exclusive\" - Exclusive delivery of messages to first bound client.     \"non-exclusive\" - Non-exclusive delivery of messages to all bound clients. 
   */
  public enum AccessTypeEnum {
    @SerializedName("exclusive")
    EXCLUSIVE("exclusive"),
    
    @SerializedName("non-exclusive")
    NON_EXCLUSIVE("non-exclusive");

    private String value;

    AccessTypeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("accessType")
  private AccessTypeEnum accessType = null;

  @SerializedName("consumerAckPropagationEnabled")
  private Boolean consumerAckPropagationEnabled = null;

  @SerializedName("deadMsgQueue")
  private String deadMsgQueue = null;

  @SerializedName("egressEnabled")
  private Boolean egressEnabled = null;

  @SerializedName("eventBindCountThreshold")
  private EventThreshold eventBindCountThreshold = null;

  @SerializedName("eventMsgSpoolUsageThreshold")
  private EventThreshold eventMsgSpoolUsageThreshold = null;

  @SerializedName("eventRejectLowPriorityMsgLimitThreshold")
  private EventThreshold eventRejectLowPriorityMsgLimitThreshold = null;

  @SerializedName("ingressEnabled")
  private Boolean ingressEnabled = null;

  @SerializedName("maxBindCount")
  private Integer maxBindCount = null;

  @SerializedName("maxDeliveredUnackedMsgsPerFlow")
  private Integer maxDeliveredUnackedMsgsPerFlow = null;

  @SerializedName("maxMsgSize")
  private Integer maxMsgSize = null;

  @SerializedName("maxMsgSpoolUsage")
  private Integer maxMsgSpoolUsage = null;

  @SerializedName("maxRedeliveryCount")
  private Integer maxRedeliveryCount = null;

  @SerializedName("maxTtl")
  private Integer maxTtl = null;

  @SerializedName("msgVpnName")
  private String msgVpnName = null;

  @SerializedName("owner")
  private String owner = null;

  /**
   * Permission level for users of the queue, excluding the owner. The default value is `\"no-access\"`. The allowed values and their meaning are:      \"no-access\" - Disallows all access.     \"read-only\" - Read-only access to the messages in the queue.     \"consume\" - Consume (read and remove) messages in the queue.     \"modify-topic\" - Consume messages or modify the topic/selector of the queue.     \"delete\" - Consume messages or delete the queue altogether. 
   */
  public enum PermissionEnum {
    @SerializedName("no-access")
    NO_ACCESS("no-access"),
    
    @SerializedName("read-only")
    READ_ONLY("read-only"),
    
    @SerializedName("consume")
    CONSUME("consume"),
    
    @SerializedName("modify-topic")
    MODIFY_TOPIC("modify-topic"),
    
    @SerializedName("delete")
    DELETE("delete");

    private String value;

    PermissionEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("permission")
  private PermissionEnum permission = null;

  @SerializedName("queueName")
  private String queueName = null;

  @SerializedName("rejectLowPriorityMsgEnabled")
  private Boolean rejectLowPriorityMsgEnabled = null;

  @SerializedName("rejectLowPriorityMsgLimit")
  private Integer rejectLowPriorityMsgLimit = null;

  /**
   * The circumstances under which a nack is sent to the client on discards. Note that nacks cause the message to not be delivered to any destination and transacted-session commits to fail. This attribute may only have a value of `\"never\"` if `rejectLowPriorityMsgEnabled` is disabled. The default value is `\"when-queue-enabled\"`. The allowed values and their meaning are:      \"always\" - Message discards always result in nacks being returned to the sending client, even if the discard reason is that the queue is disabled.     \"when-queue-enabled\" - Message discards result in nacks being returned to the sending client, except if the discard reason is that the queue is disabled.     \"never\" - Message discards never result in nacks being returned to the sending client. 
   */
  public enum RejectMsgToSenderOnDiscardBehaviorEnum {
    @SerializedName("always")
    ALWAYS("always"),
    
    @SerializedName("when-queue-enabled")
    WHEN_QUEUE_ENABLED("when-queue-enabled"),
    
    @SerializedName("never")
    NEVER("never");

    private String value;

    RejectMsgToSenderOnDiscardBehaviorEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("rejectMsgToSenderOnDiscardBehavior")
  private RejectMsgToSenderOnDiscardBehaviorEnum rejectMsgToSenderOnDiscardBehavior = null;

  @SerializedName("respectTtlEnabled")
  private Boolean respectTtlEnabled = null;

  public MsgVpnQueue accessType(AccessTypeEnum accessType) {
    this.accessType = accessType;
    return this;
  }

   /**
   * The queue access type of either exclusive or non-exclusive. The default value is `\"exclusive\"`. The allowed values and their meaning are:      \"exclusive\" - Exclusive delivery of messages to first bound client.     \"non-exclusive\" - Non-exclusive delivery of messages to all bound clients. 
   * @return accessType
  **/
  @ApiModelProperty(example = "null", value = "The queue access type of either exclusive or non-exclusive. The default value is `\"exclusive\"`. The allowed values and their meaning are:      \"exclusive\" - Exclusive delivery of messages to first bound client.     \"non-exclusive\" - Non-exclusive delivery of messages to all bound clients. ")
  public AccessTypeEnum getAccessType() {
    return accessType;
  }

  public void setAccessType(AccessTypeEnum accessType) {
    this.accessType = accessType;
  }

  public MsgVpnQueue consumerAckPropagationEnabled(Boolean consumerAckPropagationEnabled) {
    this.consumerAckPropagationEnabled = consumerAckPropagationEnabled;
    return this;
  }

   /**
   * Enable or disable the propagation of consumer acks received on the active replication Message VPN to the standby replication Message VPN. The default value is `true`.
   * @return consumerAckPropagationEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the propagation of consumer acks received on the active replication Message VPN to the standby replication Message VPN. The default value is `true`.")
  public Boolean getConsumerAckPropagationEnabled() {
    return consumerAckPropagationEnabled;
  }

  public void setConsumerAckPropagationEnabled(Boolean consumerAckPropagationEnabled) {
    this.consumerAckPropagationEnabled = consumerAckPropagationEnabled;
  }

  public MsgVpnQueue deadMsgQueue(String deadMsgQueue) {
    this.deadMsgQueue = deadMsgQueue;
    return this;
  }

   /**
   * Identifies the name of the queue which should be used as the queue's dead message queue. The default value is `\"#DEAD_MSG_QUEUE\"`.
   * @return deadMsgQueue
  **/
  @ApiModelProperty(example = "null", value = "Identifies the name of the queue which should be used as the queue's dead message queue. The default value is `\"#DEAD_MSG_QUEUE\"`.")
  public String getDeadMsgQueue() {
    return deadMsgQueue;
  }

  public void setDeadMsgQueue(String deadMsgQueue) {
    this.deadMsgQueue = deadMsgQueue;
  }

  public MsgVpnQueue egressEnabled(Boolean egressEnabled) {
    this.egressEnabled = egressEnabled;
    return this;
  }

   /**
   * Enable or disable the flow of messages from a queue. The default value is `false`.
   * @return egressEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the flow of messages from a queue. The default value is `false`.")
  public Boolean getEgressEnabled() {
    return egressEnabled;
  }

  public void setEgressEnabled(Boolean egressEnabled) {
    this.egressEnabled = egressEnabled;
  }

  public MsgVpnQueue eventBindCountThreshold(EventThreshold eventBindCountThreshold) {
    this.eventBindCountThreshold = eventBindCountThreshold;
    return this;
  }

   /**
   * The threshold for the bind count event of the Queue, relative to `maxBindCount`.
   * @return eventBindCountThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the bind count event of the Queue, relative to `maxBindCount`.")
  public EventThreshold getEventBindCountThreshold() {
    return eventBindCountThreshold;
  }

  public void setEventBindCountThreshold(EventThreshold eventBindCountThreshold) {
    this.eventBindCountThreshold = eventBindCountThreshold;
  }

  public MsgVpnQueue eventMsgSpoolUsageThreshold(EventThreshold eventMsgSpoolUsageThreshold) {
    this.eventMsgSpoolUsageThreshold = eventMsgSpoolUsageThreshold;
    return this;
  }

   /**
   * The threshold for the message spool usage event of the Queue, relative to `maxMsgSpoolUsage`.
   * @return eventMsgSpoolUsageThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the message spool usage event of the Queue, relative to `maxMsgSpoolUsage`.")
  public EventThreshold getEventMsgSpoolUsageThreshold() {
    return eventMsgSpoolUsageThreshold;
  }

  public void setEventMsgSpoolUsageThreshold(EventThreshold eventMsgSpoolUsageThreshold) {
    this.eventMsgSpoolUsageThreshold = eventMsgSpoolUsageThreshold;
  }

  public MsgVpnQueue eventRejectLowPriorityMsgLimitThreshold(EventThreshold eventRejectLowPriorityMsgLimitThreshold) {
    this.eventRejectLowPriorityMsgLimitThreshold = eventRejectLowPriorityMsgLimitThreshold;
    return this;
  }

   /**
   * The threshold for the reject-low-priority-msg limit event of the Queue, relative to `rejectLowPriorityMsgLimit`.
   * @return eventRejectLowPriorityMsgLimitThreshold
  **/
  @ApiModelProperty(example = "null", value = "The threshold for the reject-low-priority-msg limit event of the Queue, relative to `rejectLowPriorityMsgLimit`.")
  public EventThreshold getEventRejectLowPriorityMsgLimitThreshold() {
    return eventRejectLowPriorityMsgLimitThreshold;
  }

  public void setEventRejectLowPriorityMsgLimitThreshold(EventThreshold eventRejectLowPriorityMsgLimitThreshold) {
    this.eventRejectLowPriorityMsgLimitThreshold = eventRejectLowPriorityMsgLimitThreshold;
  }

  public MsgVpnQueue ingressEnabled(Boolean ingressEnabled) {
    this.ingressEnabled = ingressEnabled;
    return this;
  }

   /**
   * Enable or disable the flow of messages to a queue. The default value is `false`.
   * @return ingressEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the flow of messages to a queue. The default value is `false`.")
  public Boolean getIngressEnabled() {
    return ingressEnabled;
  }

  public void setIngressEnabled(Boolean ingressEnabled) {
    this.ingressEnabled = ingressEnabled;
  }

  public MsgVpnQueue maxBindCount(Integer maxBindCount) {
    this.maxBindCount = maxBindCount;
    return this;
  }

   /**
   * The maximum number of times a client(s) can bind to a given queue. The default value is `1000`.
   * @return maxBindCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of times a client(s) can bind to a given queue. The default value is `1000`.")
  public Integer getMaxBindCount() {
    return maxBindCount;
  }

  public void setMaxBindCount(Integer maxBindCount) {
    this.maxBindCount = maxBindCount;
  }

  public MsgVpnQueue maxDeliveredUnackedMsgsPerFlow(Integer maxDeliveredUnackedMsgsPerFlow) {
    this.maxDeliveredUnackedMsgsPerFlow = maxDeliveredUnackedMsgsPerFlow;
    return this;
  }

   /**
   * The max messages delivered but not acknowledged per flow for this queue. The default is the max value supported by the hardware.
   * @return maxDeliveredUnackedMsgsPerFlow
  **/
  @ApiModelProperty(example = "null", value = "The max messages delivered but not acknowledged per flow for this queue. The default is the max value supported by the hardware.")
  public Integer getMaxDeliveredUnackedMsgsPerFlow() {
    return maxDeliveredUnackedMsgsPerFlow;
  }

  public void setMaxDeliveredUnackedMsgsPerFlow(Integer maxDeliveredUnackedMsgsPerFlow) {
    this.maxDeliveredUnackedMsgsPerFlow = maxDeliveredUnackedMsgsPerFlow;
  }

  public MsgVpnQueue maxMsgSize(Integer maxMsgSize) {
    this.maxMsgSize = maxMsgSize;
    return this;
  }

   /**
   * The max message size (in bytes) allowed in this queue. The default value is `10000000`.
   * @return maxMsgSize
  **/
  @ApiModelProperty(example = "null", value = "The max message size (in bytes) allowed in this queue. The default value is `10000000`.")
  public Integer getMaxMsgSize() {
    return maxMsgSize;
  }

  public void setMaxMsgSize(Integer maxMsgSize) {
    this.maxMsgSize = maxMsgSize;
  }

  public MsgVpnQueue maxMsgSpoolUsage(Integer maxMsgSpoolUsage) {
    this.maxMsgSpoolUsage = maxMsgSpoolUsage;
    return this;
  }

   /**
   * The max spool usage (in MB) of this queue. Setting the value to 0 enables the last-value-queue feature and disables quota checking. The default value is `4000`.
   * @return maxMsgSpoolUsage
  **/
  @ApiModelProperty(example = "null", value = "The max spool usage (in MB) of this queue. Setting the value to 0 enables the last-value-queue feature and disables quota checking. The default value is `4000`.")
  public Integer getMaxMsgSpoolUsage() {
    return maxMsgSpoolUsage;
  }

  public void setMaxMsgSpoolUsage(Integer maxMsgSpoolUsage) {
    this.maxMsgSpoolUsage = maxMsgSpoolUsage;
  }

  public MsgVpnQueue maxRedeliveryCount(Integer maxRedeliveryCount) {
    this.maxRedeliveryCount = maxRedeliveryCount;
    return this;
  }

   /**
   * The maximum number of times the queue will attempt redelivery of a given message prior to it being discarded or moved to the #DEAD_MSG_QUEUE. A value of 0 means to retry forever. The default value is `0`.
   * @return maxRedeliveryCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of times the queue will attempt redelivery of a given message prior to it being discarded or moved to the #DEAD_MSG_QUEUE. A value of 0 means to retry forever. The default value is `0`.")
  public Integer getMaxRedeliveryCount() {
    return maxRedeliveryCount;
  }

  public void setMaxRedeliveryCount(Integer maxRedeliveryCount) {
    this.maxRedeliveryCount = maxRedeliveryCount;
  }

  public MsgVpnQueue maxTtl(Integer maxTtl) {
    this.maxTtl = maxTtl;
    return this;
  }

   /**
   * The maximum number of seconds that a message can stay in a queue or topic-endpoint when respect-ttl is enabled. A message will expire according to the lesser of the TTL in the message (assigned by the publisher) and the max-ttl configured on the endpoint. The max-ttl is a 32 bit integer value from 1 to 4294967295 representing the expiry time in seconds. A max-ttl of 0 disables this feature. The default value is `0`.
   * @return maxTtl
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of seconds that a message can stay in a queue or topic-endpoint when respect-ttl is enabled. A message will expire according to the lesser of the TTL in the message (assigned by the publisher) and the max-ttl configured on the endpoint. The max-ttl is a 32 bit integer value from 1 to 4294967295 representing the expiry time in seconds. A max-ttl of 0 disables this feature. The default value is `0`.")
  public Integer getMaxTtl() {
    return maxTtl;
  }

  public void setMaxTtl(Integer maxTtl) {
    this.maxTtl = maxTtl;
  }

  public MsgVpnQueue msgVpnName(String msgVpnName) {
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

  public MsgVpnQueue owner(String owner) {
    this.owner = owner;
    return this;
  }

   /**
   * The client-username owner of the queue. The default is to have no `owner`.
   * @return owner
  **/
  @ApiModelProperty(example = "null", value = "The client-username owner of the queue. The default is to have no `owner`.")
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public MsgVpnQueue permission(PermissionEnum permission) {
    this.permission = permission;
    return this;
  }

   /**
   * Permission level for users of the queue, excluding the owner. The default value is `\"no-access\"`. The allowed values and their meaning are:      \"no-access\" - Disallows all access.     \"read-only\" - Read-only access to the messages in the queue.     \"consume\" - Consume (read and remove) messages in the queue.     \"modify-topic\" - Consume messages or modify the topic/selector of the queue.     \"delete\" - Consume messages or delete the queue altogether. 
   * @return permission
  **/
  @ApiModelProperty(example = "null", value = "Permission level for users of the queue, excluding the owner. The default value is `\"no-access\"`. The allowed values and their meaning are:      \"no-access\" - Disallows all access.     \"read-only\" - Read-only access to the messages in the queue.     \"consume\" - Consume (read and remove) messages in the queue.     \"modify-topic\" - Consume messages or modify the topic/selector of the queue.     \"delete\" - Consume messages or delete the queue altogether. ")
  public PermissionEnum getPermission() {
    return permission;
  }

  public void setPermission(PermissionEnum permission) {
    this.permission = permission;
  }

  public MsgVpnQueue queueName(String queueName) {
    this.queueName = queueName;
    return this;
  }

   /**
   * The name of the Queue.
   * @return queueName
  **/
  @ApiModelProperty(example = "null", value = "The name of the Queue.")
  public String getQueueName() {
    return queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  public MsgVpnQueue rejectLowPriorityMsgEnabled(Boolean rejectLowPriorityMsgEnabled) {
    this.rejectLowPriorityMsgEnabled = rejectLowPriorityMsgEnabled;
    return this;
  }

   /**
   * Enable or disable if low priority messages are subject to `rejectLowPriorityMsgLimit` checking. This may only be enabled if `rejectMsgToSenderOnDiscardBehavior` does not have a value of `\"never\"`. The default value is `false`.
   * @return rejectLowPriorityMsgEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable if low priority messages are subject to `rejectLowPriorityMsgLimit` checking. This may only be enabled if `rejectMsgToSenderOnDiscardBehavior` does not have a value of `\"never\"`. The default value is `false`.")
  public Boolean getRejectLowPriorityMsgEnabled() {
    return rejectLowPriorityMsgEnabled;
  }

  public void setRejectLowPriorityMsgEnabled(Boolean rejectLowPriorityMsgEnabled) {
    this.rejectLowPriorityMsgEnabled = rejectLowPriorityMsgEnabled;
  }

  public MsgVpnQueue rejectLowPriorityMsgLimit(Integer rejectLowPriorityMsgLimit) {
    this.rejectLowPriorityMsgLimit = rejectLowPriorityMsgLimit;
    return this;
  }

   /**
   * The number of messages of any priority queued to an endpoint above which low priority messages are not admitted but higher priority messages are allowed into the endpoint. The default value is `0`.
   * @return rejectLowPriorityMsgLimit
  **/
  @ApiModelProperty(example = "null", value = "The number of messages of any priority queued to an endpoint above which low priority messages are not admitted but higher priority messages are allowed into the endpoint. The default value is `0`.")
  public Integer getRejectLowPriorityMsgLimit() {
    return rejectLowPriorityMsgLimit;
  }

  public void setRejectLowPriorityMsgLimit(Integer rejectLowPriorityMsgLimit) {
    this.rejectLowPriorityMsgLimit = rejectLowPriorityMsgLimit;
  }

  public MsgVpnQueue rejectMsgToSenderOnDiscardBehavior(RejectMsgToSenderOnDiscardBehaviorEnum rejectMsgToSenderOnDiscardBehavior) {
    this.rejectMsgToSenderOnDiscardBehavior = rejectMsgToSenderOnDiscardBehavior;
    return this;
  }

   /**
   * The circumstances under which a nack is sent to the client on discards. Note that nacks cause the message to not be delivered to any destination and transacted-session commits to fail. This attribute may only have a value of `\"never\"` if `rejectLowPriorityMsgEnabled` is disabled. The default value is `\"when-queue-enabled\"`. The allowed values and their meaning are:      \"always\" - Message discards always result in nacks being returned to the sending client, even if the discard reason is that the queue is disabled.     \"when-queue-enabled\" - Message discards result in nacks being returned to the sending client, except if the discard reason is that the queue is disabled.     \"never\" - Message discards never result in nacks being returned to the sending client. 
   * @return rejectMsgToSenderOnDiscardBehavior
  **/
  @ApiModelProperty(example = "null", value = "The circumstances under which a nack is sent to the client on discards. Note that nacks cause the message to not be delivered to any destination and transacted-session commits to fail. This attribute may only have a value of `\"never\"` if `rejectLowPriorityMsgEnabled` is disabled. The default value is `\"when-queue-enabled\"`. The allowed values and their meaning are:      \"always\" - Message discards always result in nacks being returned to the sending client, even if the discard reason is that the queue is disabled.     \"when-queue-enabled\" - Message discards result in nacks being returned to the sending client, except if the discard reason is that the queue is disabled.     \"never\" - Message discards never result in nacks being returned to the sending client. ")
  public RejectMsgToSenderOnDiscardBehaviorEnum getRejectMsgToSenderOnDiscardBehavior() {
    return rejectMsgToSenderOnDiscardBehavior;
  }

  public void setRejectMsgToSenderOnDiscardBehavior(RejectMsgToSenderOnDiscardBehaviorEnum rejectMsgToSenderOnDiscardBehavior) {
    this.rejectMsgToSenderOnDiscardBehavior = rejectMsgToSenderOnDiscardBehavior;
  }

  public MsgVpnQueue respectTtlEnabled(Boolean respectTtlEnabled) {
    this.respectTtlEnabled = respectTtlEnabled;
    return this;
  }

   /**
   * Enable or disable the respecting of TTL. If enabled, then messages contained in the queue are checked for expiry. If expired, the message is removed from the queue and either discarded or a copy of the message placed in the #DEAD_MSG_QUEUE. The default value is `false`.
   * @return respectTtlEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the respecting of TTL. If enabled, then messages contained in the queue are checked for expiry. If expired, the message is removed from the queue and either discarded or a copy of the message placed in the #DEAD_MSG_QUEUE. The default value is `false`.")
  public Boolean getRespectTtlEnabled() {
    return respectTtlEnabled;
  }

  public void setRespectTtlEnabled(Boolean respectTtlEnabled) {
    this.respectTtlEnabled = respectTtlEnabled;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnQueue msgVpnQueue = (MsgVpnQueue) o;
    return Objects.equals(this.accessType, msgVpnQueue.accessType) &&
        Objects.equals(this.consumerAckPropagationEnabled, msgVpnQueue.consumerAckPropagationEnabled) &&
        Objects.equals(this.deadMsgQueue, msgVpnQueue.deadMsgQueue) &&
        Objects.equals(this.egressEnabled, msgVpnQueue.egressEnabled) &&
        Objects.equals(this.eventBindCountThreshold, msgVpnQueue.eventBindCountThreshold) &&
        Objects.equals(this.eventMsgSpoolUsageThreshold, msgVpnQueue.eventMsgSpoolUsageThreshold) &&
        Objects.equals(this.eventRejectLowPriorityMsgLimitThreshold, msgVpnQueue.eventRejectLowPriorityMsgLimitThreshold) &&
        Objects.equals(this.ingressEnabled, msgVpnQueue.ingressEnabled) &&
        Objects.equals(this.maxBindCount, msgVpnQueue.maxBindCount) &&
        Objects.equals(this.maxDeliveredUnackedMsgsPerFlow, msgVpnQueue.maxDeliveredUnackedMsgsPerFlow) &&
        Objects.equals(this.maxMsgSize, msgVpnQueue.maxMsgSize) &&
        Objects.equals(this.maxMsgSpoolUsage, msgVpnQueue.maxMsgSpoolUsage) &&
        Objects.equals(this.maxRedeliveryCount, msgVpnQueue.maxRedeliveryCount) &&
        Objects.equals(this.maxTtl, msgVpnQueue.maxTtl) &&
        Objects.equals(this.msgVpnName, msgVpnQueue.msgVpnName) &&
        Objects.equals(this.owner, msgVpnQueue.owner) &&
        Objects.equals(this.permission, msgVpnQueue.permission) &&
        Objects.equals(this.queueName, msgVpnQueue.queueName) &&
        Objects.equals(this.rejectLowPriorityMsgEnabled, msgVpnQueue.rejectLowPriorityMsgEnabled) &&
        Objects.equals(this.rejectLowPriorityMsgLimit, msgVpnQueue.rejectLowPriorityMsgLimit) &&
        Objects.equals(this.rejectMsgToSenderOnDiscardBehavior, msgVpnQueue.rejectMsgToSenderOnDiscardBehavior) &&
        Objects.equals(this.respectTtlEnabled, msgVpnQueue.respectTtlEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessType, consumerAckPropagationEnabled, deadMsgQueue, egressEnabled, eventBindCountThreshold, eventMsgSpoolUsageThreshold, eventRejectLowPriorityMsgLimitThreshold, ingressEnabled, maxBindCount, maxDeliveredUnackedMsgsPerFlow, maxMsgSize, maxMsgSpoolUsage, maxRedeliveryCount, maxTtl, msgVpnName, owner, permission, queueName, rejectLowPriorityMsgEnabled, rejectLowPriorityMsgLimit, rejectMsgToSenderOnDiscardBehavior, respectTtlEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnQueue {\n");
    
    sb.append("    accessType: ").append(toIndentedString(accessType)).append("\n");
    sb.append("    consumerAckPropagationEnabled: ").append(toIndentedString(consumerAckPropagationEnabled)).append("\n");
    sb.append("    deadMsgQueue: ").append(toIndentedString(deadMsgQueue)).append("\n");
    sb.append("    egressEnabled: ").append(toIndentedString(egressEnabled)).append("\n");
    sb.append("    eventBindCountThreshold: ").append(toIndentedString(eventBindCountThreshold)).append("\n");
    sb.append("    eventMsgSpoolUsageThreshold: ").append(toIndentedString(eventMsgSpoolUsageThreshold)).append("\n");
    sb.append("    eventRejectLowPriorityMsgLimitThreshold: ").append(toIndentedString(eventRejectLowPriorityMsgLimitThreshold)).append("\n");
    sb.append("    ingressEnabled: ").append(toIndentedString(ingressEnabled)).append("\n");
    sb.append("    maxBindCount: ").append(toIndentedString(maxBindCount)).append("\n");
    sb.append("    maxDeliveredUnackedMsgsPerFlow: ").append(toIndentedString(maxDeliveredUnackedMsgsPerFlow)).append("\n");
    sb.append("    maxMsgSize: ").append(toIndentedString(maxMsgSize)).append("\n");
    sb.append("    maxMsgSpoolUsage: ").append(toIndentedString(maxMsgSpoolUsage)).append("\n");
    sb.append("    maxRedeliveryCount: ").append(toIndentedString(maxRedeliveryCount)).append("\n");
    sb.append("    maxTtl: ").append(toIndentedString(maxTtl)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    permission: ").append(toIndentedString(permission)).append("\n");
    sb.append("    queueName: ").append(toIndentedString(queueName)).append("\n");
    sb.append("    rejectLowPriorityMsgEnabled: ").append(toIndentedString(rejectLowPriorityMsgEnabled)).append("\n");
    sb.append("    rejectLowPriorityMsgLimit: ").append(toIndentedString(rejectLowPriorityMsgLimit)).append("\n");
    sb.append("    rejectMsgToSenderOnDiscardBehavior: ").append(toIndentedString(rejectMsgToSenderOnDiscardBehavior)).append("\n");
    sb.append("    respectTtlEnabled: ").append(toIndentedString(respectTtlEnabled)).append("\n");
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

