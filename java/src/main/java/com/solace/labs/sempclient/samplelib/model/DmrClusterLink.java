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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * DmrClusterLink
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-11T16:20:54.920-04:00")
public class DmrClusterLink {
  @JsonProperty("authenticationBasicPassword")
  private String authenticationBasicPassword = null;

  /**
   * The authentication scheme to be used by the Link which initiates connections to the remote node. The default value is `\"basic\"`. The allowed values and their meaning are:  <pre> \"basic\" - Basic Authentication Scheme (via username and password). \"client-certificate\" - Client Certificate Authentication Scheme (via certificate file or content). </pre> 
   */
  public enum AuthenticationSchemeEnum {
    BASIC("basic"),
    
    CLIENT_CERTIFICATE("client-certificate");

    private String value;

    AuthenticationSchemeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AuthenticationSchemeEnum fromValue(String text) {
      for (AuthenticationSchemeEnum b : AuthenticationSchemeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("authenticationScheme")
  private AuthenticationSchemeEnum authenticationScheme = null;

  @JsonProperty("clientProfileQueueControl1MaxDepth")
  private Integer clientProfileQueueControl1MaxDepth = null;

  @JsonProperty("clientProfileQueueControl1MinMsgBurst")
  private Integer clientProfileQueueControl1MinMsgBurst = null;

  @JsonProperty("clientProfileQueueDirect1MaxDepth")
  private Integer clientProfileQueueDirect1MaxDepth = null;

  @JsonProperty("clientProfileQueueDirect1MinMsgBurst")
  private Integer clientProfileQueueDirect1MinMsgBurst = null;

  @JsonProperty("clientProfileQueueDirect2MaxDepth")
  private Integer clientProfileQueueDirect2MaxDepth = null;

  @JsonProperty("clientProfileQueueDirect2MinMsgBurst")
  private Integer clientProfileQueueDirect2MinMsgBurst = null;

  @JsonProperty("clientProfileQueueDirect3MaxDepth")
  private Integer clientProfileQueueDirect3MaxDepth = null;

  @JsonProperty("clientProfileQueueDirect3MinMsgBurst")
  private Integer clientProfileQueueDirect3MinMsgBurst = null;

  @JsonProperty("clientProfileQueueGuaranteed1MaxDepth")
  private Integer clientProfileQueueGuaranteed1MaxDepth = null;

  @JsonProperty("clientProfileQueueGuaranteed1MinMsgBurst")
  private Integer clientProfileQueueGuaranteed1MinMsgBurst = null;

  @JsonProperty("clientProfileTcpCongestionWindowSize")
  private Long clientProfileTcpCongestionWindowSize = null;

  @JsonProperty("clientProfileTcpKeepaliveCount")
  private Long clientProfileTcpKeepaliveCount = null;

  @JsonProperty("clientProfileTcpKeepaliveIdleTime")
  private Long clientProfileTcpKeepaliveIdleTime = null;

  @JsonProperty("clientProfileTcpKeepaliveInterval")
  private Long clientProfileTcpKeepaliveInterval = null;

  @JsonProperty("clientProfileTcpMaxSegmentSize")
  private Long clientProfileTcpMaxSegmentSize = null;

  @JsonProperty("clientProfileTcpMaxWindowSize")
  private Long clientProfileTcpMaxWindowSize = null;

  @JsonProperty("dmrClusterName")
  private String dmrClusterName = null;

  @JsonProperty("egressFlowWindowSize")
  private Long egressFlowWindowSize = null;

  @JsonProperty("enabled")
  private Boolean enabled = null;

  /**
   * The initiator of the Link's TCP connections. The default value is `\"lexical\"`. The allowed values and their meaning are:  <pre> \"lexical\" - The \"higher\" node-name initiates. \"local\" - The local node initiates. \"remote\" - The remote node initiates. </pre> 
   */
  public enum InitiatorEnum {
    LEXICAL("lexical"),
    
    LOCAL("local"),
    
    REMOTE("remote");

    private String value;

    InitiatorEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static InitiatorEnum fromValue(String text) {
      for (InitiatorEnum b : InitiatorEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("initiator")
  private InitiatorEnum initiator = null;

  @JsonProperty("queueDeadMsgQueue")
  private String queueDeadMsgQueue = null;

  @JsonProperty("queueEventSpoolUsageThreshold")
  private EventThreshold queueEventSpoolUsageThreshold = null;

  @JsonProperty("queueMaxDeliveredUnackedMsgsPerFlow")
  private Long queueMaxDeliveredUnackedMsgsPerFlow = null;

  @JsonProperty("queueMaxMsgSpoolUsage")
  private Long queueMaxMsgSpoolUsage = null;

  @JsonProperty("queueMaxRedeliveryCount")
  private Long queueMaxRedeliveryCount = null;

  @JsonProperty("queueMaxTtl")
  private Long queueMaxTtl = null;

  /**
   * Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. The default value is `\"always\"`. The allowed values and their meaning are:  <pre> \"always\" - Always return a negative acknowledgment (NACK) to the sending client on message discard. \"when-queue-enabled\" - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Queue is enabled. \"never\" - Never return a negative acknowledgment (NACK) to the sending client on message discard. </pre> 
   */
  public enum QueueRejectMsgToSenderOnDiscardBehaviorEnum {
    ALWAYS("always"),
    
    WHEN_QUEUE_ENABLED("when-queue-enabled"),
    
    NEVER("never");

    private String value;

    QueueRejectMsgToSenderOnDiscardBehaviorEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static QueueRejectMsgToSenderOnDiscardBehaviorEnum fromValue(String text) {
      for (QueueRejectMsgToSenderOnDiscardBehaviorEnum b : QueueRejectMsgToSenderOnDiscardBehaviorEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("queueRejectMsgToSenderOnDiscardBehavior")
  private QueueRejectMsgToSenderOnDiscardBehaviorEnum queueRejectMsgToSenderOnDiscardBehavior = null;

  @JsonProperty("queueRespectTtlEnabled")
  private Boolean queueRespectTtlEnabled = null;

  @JsonProperty("remoteNodeName")
  private String remoteNodeName = null;

  /**
   * The span of the Link, either internal or external. Internal Links connect nodes within the same Cluster. External Links connect nodes within different Clusters. The default value is `\"external\"`. The allowed values and their meaning are:  <pre> \"internal\" - Link to same cluster. \"external\" - Link to other cluster. </pre> 
   */
  public enum SpanEnum {
    INTERNAL("internal"),
    
    EXTERNAL("external");

    private String value;

    SpanEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static SpanEnum fromValue(String text) {
      for (SpanEnum b : SpanEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("span")
  private SpanEnum span = null;

  @JsonProperty("transportCompressedEnabled")
  private Boolean transportCompressedEnabled = null;

  @JsonProperty("transportTlsEnabled")
  private Boolean transportTlsEnabled = null;

  public DmrClusterLink authenticationBasicPassword(String authenticationBasicPassword) {
    this.authenticationBasicPassword = authenticationBasicPassword;
    return this;
  }

   /**
   * The password used to authenticate with the remote node when using basic internal authentication. If this per-Link password is not configured, the Cluster's password is used instead. The default is to have no `authenticationBasicPassword`.
   * @return authenticationBasicPassword
  **/
  @ApiModelProperty(example = "null", value = "The password used to authenticate with the remote node when using basic internal authentication. If this per-Link password is not configured, the Cluster's password is used instead. The default is to have no `authenticationBasicPassword`.")
  public String getAuthenticationBasicPassword() {
    return authenticationBasicPassword;
  }

  public void setAuthenticationBasicPassword(String authenticationBasicPassword) {
    this.authenticationBasicPassword = authenticationBasicPassword;
  }

  public DmrClusterLink authenticationScheme(AuthenticationSchemeEnum authenticationScheme) {
    this.authenticationScheme = authenticationScheme;
    return this;
  }

   /**
   * The authentication scheme to be used by the Link which initiates connections to the remote node. The default value is `\"basic\"`. The allowed values and their meaning are:  <pre> \"basic\" - Basic Authentication Scheme (via username and password). \"client-certificate\" - Client Certificate Authentication Scheme (via certificate file or content). </pre> 
   * @return authenticationScheme
  **/
  @ApiModelProperty(example = "null", value = "The authentication scheme to be used by the Link which initiates connections to the remote node. The default value is `\"basic\"`. The allowed values and their meaning are:  <pre> \"basic\" - Basic Authentication Scheme (via username and password). \"client-certificate\" - Client Certificate Authentication Scheme (via certificate file or content). </pre> ")
  public AuthenticationSchemeEnum getAuthenticationScheme() {
    return authenticationScheme;
  }

  public void setAuthenticationScheme(AuthenticationSchemeEnum authenticationScheme) {
    this.authenticationScheme = authenticationScheme;
  }

  public DmrClusterLink clientProfileQueueControl1MaxDepth(Integer clientProfileQueueControl1MaxDepth) {
    this.clientProfileQueueControl1MaxDepth = clientProfileQueueControl1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \"Control 1\" (C-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.
   * @return clientProfileQueueControl1MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the \"Control 1\" (C-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.")
  public Integer getClientProfileQueueControl1MaxDepth() {
    return clientProfileQueueControl1MaxDepth;
  }

  public void setClientProfileQueueControl1MaxDepth(Integer clientProfileQueueControl1MaxDepth) {
    this.clientProfileQueueControl1MaxDepth = clientProfileQueueControl1MaxDepth;
  }

  public DmrClusterLink clientProfileQueueControl1MinMsgBurst(Integer clientProfileQueueControl1MinMsgBurst) {
    this.clientProfileQueueControl1MinMsgBurst = clientProfileQueueControl1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \"Control 1\" (C-1) priority queue, regardless of the `clientProfileQueueControl1MaxDepth` value. The default value is `4`.
   * @return clientProfileQueueControl1MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The number of messages that are always allowed entry into the \"Control 1\" (C-1) priority queue, regardless of the `clientProfileQueueControl1MaxDepth` value. The default value is `4`.")
  public Integer getClientProfileQueueControl1MinMsgBurst() {
    return clientProfileQueueControl1MinMsgBurst;
  }

  public void setClientProfileQueueControl1MinMsgBurst(Integer clientProfileQueueControl1MinMsgBurst) {
    this.clientProfileQueueControl1MinMsgBurst = clientProfileQueueControl1MinMsgBurst;
  }

  public DmrClusterLink clientProfileQueueDirect1MaxDepth(Integer clientProfileQueueDirect1MaxDepth) {
    this.clientProfileQueueDirect1MaxDepth = clientProfileQueueDirect1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \"Direct 1\" (D-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.
   * @return clientProfileQueueDirect1MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the \"Direct 1\" (D-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.")
  public Integer getClientProfileQueueDirect1MaxDepth() {
    return clientProfileQueueDirect1MaxDepth;
  }

  public void setClientProfileQueueDirect1MaxDepth(Integer clientProfileQueueDirect1MaxDepth) {
    this.clientProfileQueueDirect1MaxDepth = clientProfileQueueDirect1MaxDepth;
  }

  public DmrClusterLink clientProfileQueueDirect1MinMsgBurst(Integer clientProfileQueueDirect1MinMsgBurst) {
    this.clientProfileQueueDirect1MinMsgBurst = clientProfileQueueDirect1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \"Direct 1\" (D-1) priority queue, regardless of the `clientProfileQueueDirect1MaxDepth` value. The default value is `4`.
   * @return clientProfileQueueDirect1MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The number of messages that are always allowed entry into the \"Direct 1\" (D-1) priority queue, regardless of the `clientProfileQueueDirect1MaxDepth` value. The default value is `4`.")
  public Integer getClientProfileQueueDirect1MinMsgBurst() {
    return clientProfileQueueDirect1MinMsgBurst;
  }

  public void setClientProfileQueueDirect1MinMsgBurst(Integer clientProfileQueueDirect1MinMsgBurst) {
    this.clientProfileQueueDirect1MinMsgBurst = clientProfileQueueDirect1MinMsgBurst;
  }

  public DmrClusterLink clientProfileQueueDirect2MaxDepth(Integer clientProfileQueueDirect2MaxDepth) {
    this.clientProfileQueueDirect2MaxDepth = clientProfileQueueDirect2MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \"Direct 2\" (D-2) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.
   * @return clientProfileQueueDirect2MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the \"Direct 2\" (D-2) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.")
  public Integer getClientProfileQueueDirect2MaxDepth() {
    return clientProfileQueueDirect2MaxDepth;
  }

  public void setClientProfileQueueDirect2MaxDepth(Integer clientProfileQueueDirect2MaxDepth) {
    this.clientProfileQueueDirect2MaxDepth = clientProfileQueueDirect2MaxDepth;
  }

  public DmrClusterLink clientProfileQueueDirect2MinMsgBurst(Integer clientProfileQueueDirect2MinMsgBurst) {
    this.clientProfileQueueDirect2MinMsgBurst = clientProfileQueueDirect2MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \"Direct 2\" (D-2) priority queue, regardless of the `clientProfileQueueDirect2MaxDepth` value. The default value is `4`.
   * @return clientProfileQueueDirect2MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The number of messages that are always allowed entry into the \"Direct 2\" (D-2) priority queue, regardless of the `clientProfileQueueDirect2MaxDepth` value. The default value is `4`.")
  public Integer getClientProfileQueueDirect2MinMsgBurst() {
    return clientProfileQueueDirect2MinMsgBurst;
  }

  public void setClientProfileQueueDirect2MinMsgBurst(Integer clientProfileQueueDirect2MinMsgBurst) {
    this.clientProfileQueueDirect2MinMsgBurst = clientProfileQueueDirect2MinMsgBurst;
  }

  public DmrClusterLink clientProfileQueueDirect3MaxDepth(Integer clientProfileQueueDirect3MaxDepth) {
    this.clientProfileQueueDirect3MaxDepth = clientProfileQueueDirect3MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \"Direct 3\" (D-3) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.
   * @return clientProfileQueueDirect3MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the \"Direct 3\" (D-3) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.")
  public Integer getClientProfileQueueDirect3MaxDepth() {
    return clientProfileQueueDirect3MaxDepth;
  }

  public void setClientProfileQueueDirect3MaxDepth(Integer clientProfileQueueDirect3MaxDepth) {
    this.clientProfileQueueDirect3MaxDepth = clientProfileQueueDirect3MaxDepth;
  }

  public DmrClusterLink clientProfileQueueDirect3MinMsgBurst(Integer clientProfileQueueDirect3MinMsgBurst) {
    this.clientProfileQueueDirect3MinMsgBurst = clientProfileQueueDirect3MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \"Direct 3\" (D-3) priority queue, regardless of the `clientProfileQueueDirect3MaxDepth` value. The default value is `4`.
   * @return clientProfileQueueDirect3MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The number of messages that are always allowed entry into the \"Direct 3\" (D-3) priority queue, regardless of the `clientProfileQueueDirect3MaxDepth` value. The default value is `4`.")
  public Integer getClientProfileQueueDirect3MinMsgBurst() {
    return clientProfileQueueDirect3MinMsgBurst;
  }

  public void setClientProfileQueueDirect3MinMsgBurst(Integer clientProfileQueueDirect3MinMsgBurst) {
    this.clientProfileQueueDirect3MinMsgBurst = clientProfileQueueDirect3MinMsgBurst;
  }

  public DmrClusterLink clientProfileQueueGuaranteed1MaxDepth(Integer clientProfileQueueGuaranteed1MaxDepth) {
    this.clientProfileQueueGuaranteed1MaxDepth = clientProfileQueueGuaranteed1MaxDepth;
    return this;
  }

   /**
   * The maximum depth of the \"Guaranteed 1\" (G-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.
   * @return clientProfileQueueGuaranteed1MaxDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum depth of the \"Guaranteed 1\" (G-1) priority queue, in work units. Each work unit is 2048 bytes of message data. The default value is `20000`.")
  public Integer getClientProfileQueueGuaranteed1MaxDepth() {
    return clientProfileQueueGuaranteed1MaxDepth;
  }

  public void setClientProfileQueueGuaranteed1MaxDepth(Integer clientProfileQueueGuaranteed1MaxDepth) {
    this.clientProfileQueueGuaranteed1MaxDepth = clientProfileQueueGuaranteed1MaxDepth;
  }

  public DmrClusterLink clientProfileQueueGuaranteed1MinMsgBurst(Integer clientProfileQueueGuaranteed1MinMsgBurst) {
    this.clientProfileQueueGuaranteed1MinMsgBurst = clientProfileQueueGuaranteed1MinMsgBurst;
    return this;
  }

   /**
   * The number of messages that are always allowed entry into the \"Guaranteed 1\" (G-3) priority queue, regardless of the `clientProfileQueueGuaranteed1MaxDepth` value. The default value is `255`.
   * @return clientProfileQueueGuaranteed1MinMsgBurst
  **/
  @ApiModelProperty(example = "null", value = "The number of messages that are always allowed entry into the \"Guaranteed 1\" (G-3) priority queue, regardless of the `clientProfileQueueGuaranteed1MaxDepth` value. The default value is `255`.")
  public Integer getClientProfileQueueGuaranteed1MinMsgBurst() {
    return clientProfileQueueGuaranteed1MinMsgBurst;
  }

  public void setClientProfileQueueGuaranteed1MinMsgBurst(Integer clientProfileQueueGuaranteed1MinMsgBurst) {
    this.clientProfileQueueGuaranteed1MinMsgBurst = clientProfileQueueGuaranteed1MinMsgBurst;
  }

  public DmrClusterLink clientProfileTcpCongestionWindowSize(Long clientProfileTcpCongestionWindowSize) {
    this.clientProfileTcpCongestionWindowSize = clientProfileTcpCongestionWindowSize;
    return this;
  }

   /**
   * The TCP initial congestion window size, in multiples of the TCP Maximum Segment Size (MSS). Changing the value from its default of 2 results in non-compliance with RFC 2581. Contact Solace Support before changing this value. The default value is `2`.
   * @return clientProfileTcpCongestionWindowSize
  **/
  @ApiModelProperty(example = "null", value = "The TCP initial congestion window size, in multiples of the TCP Maximum Segment Size (MSS). Changing the value from its default of 2 results in non-compliance with RFC 2581. Contact Solace Support before changing this value. The default value is `2`.")
  public Long getClientProfileTcpCongestionWindowSize() {
    return clientProfileTcpCongestionWindowSize;
  }

  public void setClientProfileTcpCongestionWindowSize(Long clientProfileTcpCongestionWindowSize) {
    this.clientProfileTcpCongestionWindowSize = clientProfileTcpCongestionWindowSize;
  }

  public DmrClusterLink clientProfileTcpKeepaliveCount(Long clientProfileTcpKeepaliveCount) {
    this.clientProfileTcpKeepaliveCount = clientProfileTcpKeepaliveCount;
    return this;
  }

   /**
   * The number of TCP keepalive retransmissions to be carried out before declaring that the remote end is not available. The default value is `5`.
   * @return clientProfileTcpKeepaliveCount
  **/
  @ApiModelProperty(example = "null", value = "The number of TCP keepalive retransmissions to be carried out before declaring that the remote end is not available. The default value is `5`.")
  public Long getClientProfileTcpKeepaliveCount() {
    return clientProfileTcpKeepaliveCount;
  }

  public void setClientProfileTcpKeepaliveCount(Long clientProfileTcpKeepaliveCount) {
    this.clientProfileTcpKeepaliveCount = clientProfileTcpKeepaliveCount;
  }

  public DmrClusterLink clientProfileTcpKeepaliveIdleTime(Long clientProfileTcpKeepaliveIdleTime) {
    this.clientProfileTcpKeepaliveIdleTime = clientProfileTcpKeepaliveIdleTime;
    return this;
  }

   /**
   * The amount of time a connection must remain idle before TCP begins sending keepalive probes, in seconds. The default value is `3`.
   * @return clientProfileTcpKeepaliveIdleTime
  **/
  @ApiModelProperty(example = "null", value = "The amount of time a connection must remain idle before TCP begins sending keepalive probes, in seconds. The default value is `3`.")
  public Long getClientProfileTcpKeepaliveIdleTime() {
    return clientProfileTcpKeepaliveIdleTime;
  }

  public void setClientProfileTcpKeepaliveIdleTime(Long clientProfileTcpKeepaliveIdleTime) {
    this.clientProfileTcpKeepaliveIdleTime = clientProfileTcpKeepaliveIdleTime;
  }

  public DmrClusterLink clientProfileTcpKeepaliveInterval(Long clientProfileTcpKeepaliveInterval) {
    this.clientProfileTcpKeepaliveInterval = clientProfileTcpKeepaliveInterval;
    return this;
  }

   /**
   * The amount of time between TCP keepalive retransmissions when no acknowledgement is received, in seconds. The default value is `1`.
   * @return clientProfileTcpKeepaliveInterval
  **/
  @ApiModelProperty(example = "null", value = "The amount of time between TCP keepalive retransmissions when no acknowledgement is received, in seconds. The default value is `1`.")
  public Long getClientProfileTcpKeepaliveInterval() {
    return clientProfileTcpKeepaliveInterval;
  }

  public void setClientProfileTcpKeepaliveInterval(Long clientProfileTcpKeepaliveInterval) {
    this.clientProfileTcpKeepaliveInterval = clientProfileTcpKeepaliveInterval;
  }

  public DmrClusterLink clientProfileTcpMaxSegmentSize(Long clientProfileTcpMaxSegmentSize) {
    this.clientProfileTcpMaxSegmentSize = clientProfileTcpMaxSegmentSize;
    return this;
  }

   /**
   * The TCP maximum segment size, in kilobytes. Changes are applied to all existing connections. The default value is `1460`.
   * @return clientProfileTcpMaxSegmentSize
  **/
  @ApiModelProperty(example = "null", value = "The TCP maximum segment size, in kilobytes. Changes are applied to all existing connections. The default value is `1460`.")
  public Long getClientProfileTcpMaxSegmentSize() {
    return clientProfileTcpMaxSegmentSize;
  }

  public void setClientProfileTcpMaxSegmentSize(Long clientProfileTcpMaxSegmentSize) {
    this.clientProfileTcpMaxSegmentSize = clientProfileTcpMaxSegmentSize;
  }

  public DmrClusterLink clientProfileTcpMaxWindowSize(Long clientProfileTcpMaxWindowSize) {
    this.clientProfileTcpMaxWindowSize = clientProfileTcpMaxWindowSize;
    return this;
  }

   /**
   * The TCP maximum window size, in kilobytes. Changes are applied to all existing connections. The default value is `256`.
   * @return clientProfileTcpMaxWindowSize
  **/
  @ApiModelProperty(example = "null", value = "The TCP maximum window size, in kilobytes. Changes are applied to all existing connections. The default value is `256`.")
  public Long getClientProfileTcpMaxWindowSize() {
    return clientProfileTcpMaxWindowSize;
  }

  public void setClientProfileTcpMaxWindowSize(Long clientProfileTcpMaxWindowSize) {
    this.clientProfileTcpMaxWindowSize = clientProfileTcpMaxWindowSize;
  }

  public DmrClusterLink dmrClusterName(String dmrClusterName) {
    this.dmrClusterName = dmrClusterName;
    return this;
  }

   /**
   * The name of the Cluster.
   * @return dmrClusterName
  **/
  @ApiModelProperty(example = "null", value = "The name of the Cluster.")
  public String getDmrClusterName() {
    return dmrClusterName;
  }

  public void setDmrClusterName(String dmrClusterName) {
    this.dmrClusterName = dmrClusterName;
  }

  public DmrClusterLink egressFlowWindowSize(Long egressFlowWindowSize) {
    this.egressFlowWindowSize = egressFlowWindowSize;
    return this;
  }

   /**
   * The number of outstanding guaranteed messages that can be sent over the Link before acknowledgement is received by the sender. The default value is `255`.
   * @return egressFlowWindowSize
  **/
  @ApiModelProperty(example = "null", value = "The number of outstanding guaranteed messages that can be sent over the Link before acknowledgement is received by the sender. The default value is `255`.")
  public Long getEgressFlowWindowSize() {
    return egressFlowWindowSize;
  }

  public void setEgressFlowWindowSize(Long egressFlowWindowSize) {
    this.egressFlowWindowSize = egressFlowWindowSize;
  }

  public DmrClusterLink enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the Link. When disabled, subscription sets of this and the remote node are not kept up-to-date, and messages are not exchanged with the remote node. Published guaranteed messages will be queued up for future delivery based on current subscription sets. The default value is `false`.
   * @return enabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the Link. When disabled, subscription sets of this and the remote node are not kept up-to-date, and messages are not exchanged with the remote node. Published guaranteed messages will be queued up for future delivery based on current subscription sets. The default value is `false`.")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public DmrClusterLink initiator(InitiatorEnum initiator) {
    this.initiator = initiator;
    return this;
  }

   /**
   * The initiator of the Link's TCP connections. The default value is `\"lexical\"`. The allowed values and their meaning are:  <pre> \"lexical\" - The \"higher\" node-name initiates. \"local\" - The local node initiates. \"remote\" - The remote node initiates. </pre> 
   * @return initiator
  **/
  @ApiModelProperty(example = "null", value = "The initiator of the Link's TCP connections. The default value is `\"lexical\"`. The allowed values and their meaning are:  <pre> \"lexical\" - The \"higher\" node-name initiates. \"local\" - The local node initiates. \"remote\" - The remote node initiates. </pre> ")
  public InitiatorEnum getInitiator() {
    return initiator;
  }

  public void setInitiator(InitiatorEnum initiator) {
    this.initiator = initiator;
  }

  public DmrClusterLink queueDeadMsgQueue(String queueDeadMsgQueue) {
    this.queueDeadMsgQueue = queueDeadMsgQueue;
    return this;
  }

   /**
   * The name of the Dead Message Queue (DMQ) used by the Queue for discarded messages. The default value is `\"#DEAD_MSG_QUEUE\"`.
   * @return queueDeadMsgQueue
  **/
  @ApiModelProperty(example = "null", value = "The name of the Dead Message Queue (DMQ) used by the Queue for discarded messages. The default value is `\"#DEAD_MSG_QUEUE\"`.")
  public String getQueueDeadMsgQueue() {
    return queueDeadMsgQueue;
  }

  public void setQueueDeadMsgQueue(String queueDeadMsgQueue) {
    this.queueDeadMsgQueue = queueDeadMsgQueue;
  }

  public DmrClusterLink queueEventSpoolUsageThreshold(EventThreshold queueEventSpoolUsageThreshold) {
    this.queueEventSpoolUsageThreshold = queueEventSpoolUsageThreshold;
    return this;
  }

   /**
   * Get queueEventSpoolUsageThreshold
   * @return queueEventSpoolUsageThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThreshold getQueueEventSpoolUsageThreshold() {
    return queueEventSpoolUsageThreshold;
  }

  public void setQueueEventSpoolUsageThreshold(EventThreshold queueEventSpoolUsageThreshold) {
    this.queueEventSpoolUsageThreshold = queueEventSpoolUsageThreshold;
  }

  public DmrClusterLink queueMaxDeliveredUnackedMsgsPerFlow(Long queueMaxDeliveredUnackedMsgsPerFlow) {
    this.queueMaxDeliveredUnackedMsgsPerFlow = queueMaxDeliveredUnackedMsgsPerFlow;
    return this;
  }

   /**
   * The maximum number of messages delivered but not acknowledged per flow for the Queue. The default is the max value supported by the platform.
   * @return queueMaxDeliveredUnackedMsgsPerFlow
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of messages delivered but not acknowledged per flow for the Queue. The default is the max value supported by the platform.")
  public Long getQueueMaxDeliveredUnackedMsgsPerFlow() {
    return queueMaxDeliveredUnackedMsgsPerFlow;
  }

  public void setQueueMaxDeliveredUnackedMsgsPerFlow(Long queueMaxDeliveredUnackedMsgsPerFlow) {
    this.queueMaxDeliveredUnackedMsgsPerFlow = queueMaxDeliveredUnackedMsgsPerFlow;
  }

  public DmrClusterLink queueMaxMsgSpoolUsage(Long queueMaxMsgSpoolUsage) {
    this.queueMaxMsgSpoolUsage = queueMaxMsgSpoolUsage;
    return this;
  }

   /**
   * The maximum message spool usage by the Queue (quota), in megabytes (MB). The default varies by platform.
   * @return queueMaxMsgSpoolUsage
  **/
  @ApiModelProperty(example = "null", value = "The maximum message spool usage by the Queue (quota), in megabytes (MB). The default varies by platform.")
  public Long getQueueMaxMsgSpoolUsage() {
    return queueMaxMsgSpoolUsage;
  }

  public void setQueueMaxMsgSpoolUsage(Long queueMaxMsgSpoolUsage) {
    this.queueMaxMsgSpoolUsage = queueMaxMsgSpoolUsage;
  }

  public DmrClusterLink queueMaxRedeliveryCount(Long queueMaxRedeliveryCount) {
    this.queueMaxRedeliveryCount = queueMaxRedeliveryCount;
    return this;
  }

   /**
   * The maximum number of times the Queue will attempt redelivery of a message prior to it being discarded or moved to the DMQ. A value of 0 means to retry forever. The default value is `0`.
   * @return queueMaxRedeliveryCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of times the Queue will attempt redelivery of a message prior to it being discarded or moved to the DMQ. A value of 0 means to retry forever. The default value is `0`.")
  public Long getQueueMaxRedeliveryCount() {
    return queueMaxRedeliveryCount;
  }

  public void setQueueMaxRedeliveryCount(Long queueMaxRedeliveryCount) {
    this.queueMaxRedeliveryCount = queueMaxRedeliveryCount;
  }

  public DmrClusterLink queueMaxTtl(Long queueMaxTtl) {
    this.queueMaxTtl = queueMaxTtl;
    return this;
  }

   /**
   * The maximum time in seconds a message can stay in the Queue when `queueRespectTtlEnabled` is `true`. A message expires when the lesser of the sender assigned time-to-live (TTL) in the message and the `queueMaxTtl` configured for the Queue, is exceeded. A value of 0 disables expiry. The default value is `0`.
   * @return queueMaxTtl
  **/
  @ApiModelProperty(example = "null", value = "The maximum time in seconds a message can stay in the Queue when `queueRespectTtlEnabled` is `true`. A message expires when the lesser of the sender assigned time-to-live (TTL) in the message and the `queueMaxTtl` configured for the Queue, is exceeded. A value of 0 disables expiry. The default value is `0`.")
  public Long getQueueMaxTtl() {
    return queueMaxTtl;
  }

  public void setQueueMaxTtl(Long queueMaxTtl) {
    this.queueMaxTtl = queueMaxTtl;
  }

  public DmrClusterLink queueRejectMsgToSenderOnDiscardBehavior(QueueRejectMsgToSenderOnDiscardBehaviorEnum queueRejectMsgToSenderOnDiscardBehavior) {
    this.queueRejectMsgToSenderOnDiscardBehavior = queueRejectMsgToSenderOnDiscardBehavior;
    return this;
  }

   /**
   * Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. The default value is `\"always\"`. The allowed values and their meaning are:  <pre> \"always\" - Always return a negative acknowledgment (NACK) to the sending client on message discard. \"when-queue-enabled\" - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Queue is enabled. \"never\" - Never return a negative acknowledgment (NACK) to the sending client on message discard. </pre> 
   * @return queueRejectMsgToSenderOnDiscardBehavior
  **/
  @ApiModelProperty(example = "null", value = "Determines when to return negative acknowledgements (NACKs) to sending clients on message discards. Note that NACKs cause the message to not be delivered to any destination and Transacted Session commits to fail. The default value is `\"always\"`. The allowed values and their meaning are:  <pre> \"always\" - Always return a negative acknowledgment (NACK) to the sending client on message discard. \"when-queue-enabled\" - Only return a negative acknowledgment (NACK) to the sending client on message discard when the Queue is enabled. \"never\" - Never return a negative acknowledgment (NACK) to the sending client on message discard. </pre> ")
  public QueueRejectMsgToSenderOnDiscardBehaviorEnum getQueueRejectMsgToSenderOnDiscardBehavior() {
    return queueRejectMsgToSenderOnDiscardBehavior;
  }

  public void setQueueRejectMsgToSenderOnDiscardBehavior(QueueRejectMsgToSenderOnDiscardBehaviorEnum queueRejectMsgToSenderOnDiscardBehavior) {
    this.queueRejectMsgToSenderOnDiscardBehavior = queueRejectMsgToSenderOnDiscardBehavior;
  }

  public DmrClusterLink queueRespectTtlEnabled(Boolean queueRespectTtlEnabled) {
    this.queueRespectTtlEnabled = queueRespectTtlEnabled;
    return this;
  }

   /**
   * Enable or disable the respecting of the time-to-live (TTL) for messages in the Queue. When enabled, expired messages are discarded or moved to the DMQ. The default value is `false`.
   * @return queueRespectTtlEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the respecting of the time-to-live (TTL) for messages in the Queue. When enabled, expired messages are discarded or moved to the DMQ. The default value is `false`.")
  public Boolean getQueueRespectTtlEnabled() {
    return queueRespectTtlEnabled;
  }

  public void setQueueRespectTtlEnabled(Boolean queueRespectTtlEnabled) {
    this.queueRespectTtlEnabled = queueRespectTtlEnabled;
  }

  public DmrClusterLink remoteNodeName(String remoteNodeName) {
    this.remoteNodeName = remoteNodeName;
    return this;
  }

   /**
   * The name of the node at the remote end of the Link.
   * @return remoteNodeName
  **/
  @ApiModelProperty(example = "null", value = "The name of the node at the remote end of the Link.")
  public String getRemoteNodeName() {
    return remoteNodeName;
  }

  public void setRemoteNodeName(String remoteNodeName) {
    this.remoteNodeName = remoteNodeName;
  }

  public DmrClusterLink span(SpanEnum span) {
    this.span = span;
    return this;
  }

   /**
   * The span of the Link, either internal or external. Internal Links connect nodes within the same Cluster. External Links connect nodes within different Clusters. The default value is `\"external\"`. The allowed values and their meaning are:  <pre> \"internal\" - Link to same cluster. \"external\" - Link to other cluster. </pre> 
   * @return span
  **/
  @ApiModelProperty(example = "null", value = "The span of the Link, either internal or external. Internal Links connect nodes within the same Cluster. External Links connect nodes within different Clusters. The default value is `\"external\"`. The allowed values and their meaning are:  <pre> \"internal\" - Link to same cluster. \"external\" - Link to other cluster. </pre> ")
  public SpanEnum getSpan() {
    return span;
  }

  public void setSpan(SpanEnum span) {
    this.span = span;
  }

  public DmrClusterLink transportCompressedEnabled(Boolean transportCompressedEnabled) {
    this.transportCompressedEnabled = transportCompressedEnabled;
    return this;
  }

   /**
   * Enable or disable compression on the Link. The default value is `false`.
   * @return transportCompressedEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable compression on the Link. The default value is `false`.")
  public Boolean getTransportCompressedEnabled() {
    return transportCompressedEnabled;
  }

  public void setTransportCompressedEnabled(Boolean transportCompressedEnabled) {
    this.transportCompressedEnabled = transportCompressedEnabled;
  }

  public DmrClusterLink transportTlsEnabled(Boolean transportTlsEnabled) {
    this.transportTlsEnabled = transportTlsEnabled;
    return this;
  }

   /**
   * Enable or disable encryption on the Link. The default value is `false`.
   * @return transportTlsEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable encryption on the Link. The default value is `false`.")
  public Boolean getTransportTlsEnabled() {
    return transportTlsEnabled;
  }

  public void setTransportTlsEnabled(Boolean transportTlsEnabled) {
    this.transportTlsEnabled = transportTlsEnabled;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DmrClusterLink dmrClusterLink = (DmrClusterLink) o;
    return Objects.equals(this.authenticationBasicPassword, dmrClusterLink.authenticationBasicPassword) &&
        Objects.equals(this.authenticationScheme, dmrClusterLink.authenticationScheme) &&
        Objects.equals(this.clientProfileQueueControl1MaxDepth, dmrClusterLink.clientProfileQueueControl1MaxDepth) &&
        Objects.equals(this.clientProfileQueueControl1MinMsgBurst, dmrClusterLink.clientProfileQueueControl1MinMsgBurst) &&
        Objects.equals(this.clientProfileQueueDirect1MaxDepth, dmrClusterLink.clientProfileQueueDirect1MaxDepth) &&
        Objects.equals(this.clientProfileQueueDirect1MinMsgBurst, dmrClusterLink.clientProfileQueueDirect1MinMsgBurst) &&
        Objects.equals(this.clientProfileQueueDirect2MaxDepth, dmrClusterLink.clientProfileQueueDirect2MaxDepth) &&
        Objects.equals(this.clientProfileQueueDirect2MinMsgBurst, dmrClusterLink.clientProfileQueueDirect2MinMsgBurst) &&
        Objects.equals(this.clientProfileQueueDirect3MaxDepth, dmrClusterLink.clientProfileQueueDirect3MaxDepth) &&
        Objects.equals(this.clientProfileQueueDirect3MinMsgBurst, dmrClusterLink.clientProfileQueueDirect3MinMsgBurst) &&
        Objects.equals(this.clientProfileQueueGuaranteed1MaxDepth, dmrClusterLink.clientProfileQueueGuaranteed1MaxDepth) &&
        Objects.equals(this.clientProfileQueueGuaranteed1MinMsgBurst, dmrClusterLink.clientProfileQueueGuaranteed1MinMsgBurst) &&
        Objects.equals(this.clientProfileTcpCongestionWindowSize, dmrClusterLink.clientProfileTcpCongestionWindowSize) &&
        Objects.equals(this.clientProfileTcpKeepaliveCount, dmrClusterLink.clientProfileTcpKeepaliveCount) &&
        Objects.equals(this.clientProfileTcpKeepaliveIdleTime, dmrClusterLink.clientProfileTcpKeepaliveIdleTime) &&
        Objects.equals(this.clientProfileTcpKeepaliveInterval, dmrClusterLink.clientProfileTcpKeepaliveInterval) &&
        Objects.equals(this.clientProfileTcpMaxSegmentSize, dmrClusterLink.clientProfileTcpMaxSegmentSize) &&
        Objects.equals(this.clientProfileTcpMaxWindowSize, dmrClusterLink.clientProfileTcpMaxWindowSize) &&
        Objects.equals(this.dmrClusterName, dmrClusterLink.dmrClusterName) &&
        Objects.equals(this.egressFlowWindowSize, dmrClusterLink.egressFlowWindowSize) &&
        Objects.equals(this.enabled, dmrClusterLink.enabled) &&
        Objects.equals(this.initiator, dmrClusterLink.initiator) &&
        Objects.equals(this.queueDeadMsgQueue, dmrClusterLink.queueDeadMsgQueue) &&
        Objects.equals(this.queueEventSpoolUsageThreshold, dmrClusterLink.queueEventSpoolUsageThreshold) &&
        Objects.equals(this.queueMaxDeliveredUnackedMsgsPerFlow, dmrClusterLink.queueMaxDeliveredUnackedMsgsPerFlow) &&
        Objects.equals(this.queueMaxMsgSpoolUsage, dmrClusterLink.queueMaxMsgSpoolUsage) &&
        Objects.equals(this.queueMaxRedeliveryCount, dmrClusterLink.queueMaxRedeliveryCount) &&
        Objects.equals(this.queueMaxTtl, dmrClusterLink.queueMaxTtl) &&
        Objects.equals(this.queueRejectMsgToSenderOnDiscardBehavior, dmrClusterLink.queueRejectMsgToSenderOnDiscardBehavior) &&
        Objects.equals(this.queueRespectTtlEnabled, dmrClusterLink.queueRespectTtlEnabled) &&
        Objects.equals(this.remoteNodeName, dmrClusterLink.remoteNodeName) &&
        Objects.equals(this.span, dmrClusterLink.span) &&
        Objects.equals(this.transportCompressedEnabled, dmrClusterLink.transportCompressedEnabled) &&
        Objects.equals(this.transportTlsEnabled, dmrClusterLink.transportTlsEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticationBasicPassword, authenticationScheme, clientProfileQueueControl1MaxDepth, clientProfileQueueControl1MinMsgBurst, clientProfileQueueDirect1MaxDepth, clientProfileQueueDirect1MinMsgBurst, clientProfileQueueDirect2MaxDepth, clientProfileQueueDirect2MinMsgBurst, clientProfileQueueDirect3MaxDepth, clientProfileQueueDirect3MinMsgBurst, clientProfileQueueGuaranteed1MaxDepth, clientProfileQueueGuaranteed1MinMsgBurst, clientProfileTcpCongestionWindowSize, clientProfileTcpKeepaliveCount, clientProfileTcpKeepaliveIdleTime, clientProfileTcpKeepaliveInterval, clientProfileTcpMaxSegmentSize, clientProfileTcpMaxWindowSize, dmrClusterName, egressFlowWindowSize, enabled, initiator, queueDeadMsgQueue, queueEventSpoolUsageThreshold, queueMaxDeliveredUnackedMsgsPerFlow, queueMaxMsgSpoolUsage, queueMaxRedeliveryCount, queueMaxTtl, queueRejectMsgToSenderOnDiscardBehavior, queueRespectTtlEnabled, remoteNodeName, span, transportCompressedEnabled, transportTlsEnabled);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DmrClusterLink {\n");
    
    sb.append("    authenticationBasicPassword: ").append(toIndentedString(authenticationBasicPassword)).append("\n");
    sb.append("    authenticationScheme: ").append(toIndentedString(authenticationScheme)).append("\n");
    sb.append("    clientProfileQueueControl1MaxDepth: ").append(toIndentedString(clientProfileQueueControl1MaxDepth)).append("\n");
    sb.append("    clientProfileQueueControl1MinMsgBurst: ").append(toIndentedString(clientProfileQueueControl1MinMsgBurst)).append("\n");
    sb.append("    clientProfileQueueDirect1MaxDepth: ").append(toIndentedString(clientProfileQueueDirect1MaxDepth)).append("\n");
    sb.append("    clientProfileQueueDirect1MinMsgBurst: ").append(toIndentedString(clientProfileQueueDirect1MinMsgBurst)).append("\n");
    sb.append("    clientProfileQueueDirect2MaxDepth: ").append(toIndentedString(clientProfileQueueDirect2MaxDepth)).append("\n");
    sb.append("    clientProfileQueueDirect2MinMsgBurst: ").append(toIndentedString(clientProfileQueueDirect2MinMsgBurst)).append("\n");
    sb.append("    clientProfileQueueDirect3MaxDepth: ").append(toIndentedString(clientProfileQueueDirect3MaxDepth)).append("\n");
    sb.append("    clientProfileQueueDirect3MinMsgBurst: ").append(toIndentedString(clientProfileQueueDirect3MinMsgBurst)).append("\n");
    sb.append("    clientProfileQueueGuaranteed1MaxDepth: ").append(toIndentedString(clientProfileQueueGuaranteed1MaxDepth)).append("\n");
    sb.append("    clientProfileQueueGuaranteed1MinMsgBurst: ").append(toIndentedString(clientProfileQueueGuaranteed1MinMsgBurst)).append("\n");
    sb.append("    clientProfileTcpCongestionWindowSize: ").append(toIndentedString(clientProfileTcpCongestionWindowSize)).append("\n");
    sb.append("    clientProfileTcpKeepaliveCount: ").append(toIndentedString(clientProfileTcpKeepaliveCount)).append("\n");
    sb.append("    clientProfileTcpKeepaliveIdleTime: ").append(toIndentedString(clientProfileTcpKeepaliveIdleTime)).append("\n");
    sb.append("    clientProfileTcpKeepaliveInterval: ").append(toIndentedString(clientProfileTcpKeepaliveInterval)).append("\n");
    sb.append("    clientProfileTcpMaxSegmentSize: ").append(toIndentedString(clientProfileTcpMaxSegmentSize)).append("\n");
    sb.append("    clientProfileTcpMaxWindowSize: ").append(toIndentedString(clientProfileTcpMaxWindowSize)).append("\n");
    sb.append("    dmrClusterName: ").append(toIndentedString(dmrClusterName)).append("\n");
    sb.append("    egressFlowWindowSize: ").append(toIndentedString(egressFlowWindowSize)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    initiator: ").append(toIndentedString(initiator)).append("\n");
    sb.append("    queueDeadMsgQueue: ").append(toIndentedString(queueDeadMsgQueue)).append("\n");
    sb.append("    queueEventSpoolUsageThreshold: ").append(toIndentedString(queueEventSpoolUsageThreshold)).append("\n");
    sb.append("    queueMaxDeliveredUnackedMsgsPerFlow: ").append(toIndentedString(queueMaxDeliveredUnackedMsgsPerFlow)).append("\n");
    sb.append("    queueMaxMsgSpoolUsage: ").append(toIndentedString(queueMaxMsgSpoolUsage)).append("\n");
    sb.append("    queueMaxRedeliveryCount: ").append(toIndentedString(queueMaxRedeliveryCount)).append("\n");
    sb.append("    queueMaxTtl: ").append(toIndentedString(queueMaxTtl)).append("\n");
    sb.append("    queueRejectMsgToSenderOnDiscardBehavior: ").append(toIndentedString(queueRejectMsgToSenderOnDiscardBehavior)).append("\n");
    sb.append("    queueRespectTtlEnabled: ").append(toIndentedString(queueRespectTtlEnabled)).append("\n");
    sb.append("    remoteNodeName: ").append(toIndentedString(remoteNodeName)).append("\n");
    sb.append("    span: ").append(toIndentedString(span)).append("\n");
    sb.append("    transportCompressedEnabled: ").append(toIndentedString(transportCompressedEnabled)).append("\n");
    sb.append("    transportTlsEnabled: ").append(toIndentedString(transportTlsEnabled)).append("\n");
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

