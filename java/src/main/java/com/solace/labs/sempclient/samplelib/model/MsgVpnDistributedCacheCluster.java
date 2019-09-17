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
import com.solace.labs.sempclient.samplelib.model.EventThresholdByPercent;
import com.solace.labs.sempclient.samplelib.model.EventThresholdByValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * MsgVpnDistributedCacheCluster
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-09-11T16:20:54.920-04:00")
public class MsgVpnDistributedCacheCluster {
  @JsonProperty("cacheName")
  private String cacheName = null;

  @JsonProperty("clusterName")
  private String clusterName = null;

  @JsonProperty("deliverToOneOverrideEnabled")
  private Boolean deliverToOneOverrideEnabled = null;

  @JsonProperty("enabled")
  private Boolean enabled = null;

  @JsonProperty("eventDataByteRateThreshold")
  private EventThresholdByValue eventDataByteRateThreshold = null;

  @JsonProperty("eventDataMsgRateThreshold")
  private EventThresholdByValue eventDataMsgRateThreshold = null;

  @JsonProperty("eventMaxMemoryThreshold")
  private EventThresholdByPercent eventMaxMemoryThreshold = null;

  @JsonProperty("eventMaxTopicsThreshold")
  private EventThresholdByPercent eventMaxTopicsThreshold = null;

  @JsonProperty("eventRequestQueueDepthThreshold")
  private EventThresholdByPercent eventRequestQueueDepthThreshold = null;

  @JsonProperty("eventRequestRateThreshold")
  private EventThresholdByValue eventRequestRateThreshold = null;

  @JsonProperty("eventResponseRateThreshold")
  private EventThresholdByValue eventResponseRateThreshold = null;

  @JsonProperty("globalCachingEnabled")
  private Boolean globalCachingEnabled = null;

  @JsonProperty("globalCachingHeartbeat")
  private Long globalCachingHeartbeat = null;

  @JsonProperty("globalCachingTopicLifetime")
  private Long globalCachingTopicLifetime = null;

  @JsonProperty("maxMemory")
  private Long maxMemory = null;

  @JsonProperty("maxMsgsPerTopic")
  private Long maxMsgsPerTopic = null;

  @JsonProperty("maxRequestQueueDepth")
  private Long maxRequestQueueDepth = null;

  @JsonProperty("maxTopicCount")
  private Long maxTopicCount = null;

  @JsonProperty("msgLifetime")
  private Long msgLifetime = null;

  @JsonProperty("msgVpnName")
  private String msgVpnName = null;

  @JsonProperty("newTopicAdvertisementEnabled")
  private Boolean newTopicAdvertisementEnabled = null;

  public MsgVpnDistributedCacheCluster cacheName(String cacheName) {
    this.cacheName = cacheName;
    return this;
  }

   /**
   * The name of the Distributed Cache.
   * @return cacheName
  **/
  @ApiModelProperty(example = "null", value = "The name of the Distributed Cache.")
  public String getCacheName() {
    return cacheName;
  }

  public void setCacheName(String cacheName) {
    this.cacheName = cacheName;
  }

  public MsgVpnDistributedCacheCluster clusterName(String clusterName) {
    this.clusterName = clusterName;
    return this;
  }

   /**
   * The name of the Cache Cluster.
   * @return clusterName
  **/
  @ApiModelProperty(example = "null", value = "The name of the Cache Cluster.")
  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  public MsgVpnDistributedCacheCluster deliverToOneOverrideEnabled(Boolean deliverToOneOverrideEnabled) {
    this.deliverToOneOverrideEnabled = deliverToOneOverrideEnabled;
    return this;
  }

   /**
   * Enable or disable deliver-to-one override for the Cache Cluster. The default value is `true`.
   * @return deliverToOneOverrideEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable deliver-to-one override for the Cache Cluster. The default value is `true`.")
  public Boolean getDeliverToOneOverrideEnabled() {
    return deliverToOneOverrideEnabled;
  }

  public void setDeliverToOneOverrideEnabled(Boolean deliverToOneOverrideEnabled) {
    this.deliverToOneOverrideEnabled = deliverToOneOverrideEnabled;
  }

  public MsgVpnDistributedCacheCluster enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

   /**
   * Enable or disable the Cache Cluster. The default value is `false`.
   * @return enabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the Cache Cluster. The default value is `false`.")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public MsgVpnDistributedCacheCluster eventDataByteRateThreshold(EventThresholdByValue eventDataByteRateThreshold) {
    this.eventDataByteRateThreshold = eventDataByteRateThreshold;
    return this;
  }

   /**
   * Get eventDataByteRateThreshold
   * @return eventDataByteRateThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThresholdByValue getEventDataByteRateThreshold() {
    return eventDataByteRateThreshold;
  }

  public void setEventDataByteRateThreshold(EventThresholdByValue eventDataByteRateThreshold) {
    this.eventDataByteRateThreshold = eventDataByteRateThreshold;
  }

  public MsgVpnDistributedCacheCluster eventDataMsgRateThreshold(EventThresholdByValue eventDataMsgRateThreshold) {
    this.eventDataMsgRateThreshold = eventDataMsgRateThreshold;
    return this;
  }

   /**
   * Get eventDataMsgRateThreshold
   * @return eventDataMsgRateThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThresholdByValue getEventDataMsgRateThreshold() {
    return eventDataMsgRateThreshold;
  }

  public void setEventDataMsgRateThreshold(EventThresholdByValue eventDataMsgRateThreshold) {
    this.eventDataMsgRateThreshold = eventDataMsgRateThreshold;
  }

  public MsgVpnDistributedCacheCluster eventMaxMemoryThreshold(EventThresholdByPercent eventMaxMemoryThreshold) {
    this.eventMaxMemoryThreshold = eventMaxMemoryThreshold;
    return this;
  }

   /**
   * Get eventMaxMemoryThreshold
   * @return eventMaxMemoryThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThresholdByPercent getEventMaxMemoryThreshold() {
    return eventMaxMemoryThreshold;
  }

  public void setEventMaxMemoryThreshold(EventThresholdByPercent eventMaxMemoryThreshold) {
    this.eventMaxMemoryThreshold = eventMaxMemoryThreshold;
  }

  public MsgVpnDistributedCacheCluster eventMaxTopicsThreshold(EventThresholdByPercent eventMaxTopicsThreshold) {
    this.eventMaxTopicsThreshold = eventMaxTopicsThreshold;
    return this;
  }

   /**
   * Get eventMaxTopicsThreshold
   * @return eventMaxTopicsThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThresholdByPercent getEventMaxTopicsThreshold() {
    return eventMaxTopicsThreshold;
  }

  public void setEventMaxTopicsThreshold(EventThresholdByPercent eventMaxTopicsThreshold) {
    this.eventMaxTopicsThreshold = eventMaxTopicsThreshold;
  }

  public MsgVpnDistributedCacheCluster eventRequestQueueDepthThreshold(EventThresholdByPercent eventRequestQueueDepthThreshold) {
    this.eventRequestQueueDepthThreshold = eventRequestQueueDepthThreshold;
    return this;
  }

   /**
   * Get eventRequestQueueDepthThreshold
   * @return eventRequestQueueDepthThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThresholdByPercent getEventRequestQueueDepthThreshold() {
    return eventRequestQueueDepthThreshold;
  }

  public void setEventRequestQueueDepthThreshold(EventThresholdByPercent eventRequestQueueDepthThreshold) {
    this.eventRequestQueueDepthThreshold = eventRequestQueueDepthThreshold;
  }

  public MsgVpnDistributedCacheCluster eventRequestRateThreshold(EventThresholdByValue eventRequestRateThreshold) {
    this.eventRequestRateThreshold = eventRequestRateThreshold;
    return this;
  }

   /**
   * Get eventRequestRateThreshold
   * @return eventRequestRateThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThresholdByValue getEventRequestRateThreshold() {
    return eventRequestRateThreshold;
  }

  public void setEventRequestRateThreshold(EventThresholdByValue eventRequestRateThreshold) {
    this.eventRequestRateThreshold = eventRequestRateThreshold;
  }

  public MsgVpnDistributedCacheCluster eventResponseRateThreshold(EventThresholdByValue eventResponseRateThreshold) {
    this.eventResponseRateThreshold = eventResponseRateThreshold;
    return this;
  }

   /**
   * Get eventResponseRateThreshold
   * @return eventResponseRateThreshold
  **/
  @ApiModelProperty(example = "null", value = "")
  public EventThresholdByValue getEventResponseRateThreshold() {
    return eventResponseRateThreshold;
  }

  public void setEventResponseRateThreshold(EventThresholdByValue eventResponseRateThreshold) {
    this.eventResponseRateThreshold = eventResponseRateThreshold;
  }

  public MsgVpnDistributedCacheCluster globalCachingEnabled(Boolean globalCachingEnabled) {
    this.globalCachingEnabled = globalCachingEnabled;
    return this;
  }

   /**
   * Enable or disable global caching for the Cache Cluster. When enabled, the Cache Instances will fetch topics from remote Home Cache Clusters when requested, and subscribe to those topics to cache them locally. When disabled, the Cache Instances will remove all subscriptions and cached messages for topics from remote Home Cache Clusters. The default value is `false`.
   * @return globalCachingEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable global caching for the Cache Cluster. When enabled, the Cache Instances will fetch topics from remote Home Cache Clusters when requested, and subscribe to those topics to cache them locally. When disabled, the Cache Instances will remove all subscriptions and cached messages for topics from remote Home Cache Clusters. The default value is `false`.")
  public Boolean getGlobalCachingEnabled() {
    return globalCachingEnabled;
  }

  public void setGlobalCachingEnabled(Boolean globalCachingEnabled) {
    this.globalCachingEnabled = globalCachingEnabled;
  }

  public MsgVpnDistributedCacheCluster globalCachingHeartbeat(Long globalCachingHeartbeat) {
    this.globalCachingHeartbeat = globalCachingHeartbeat;
    return this;
  }

   /**
   * The heartbeat interval, in seconds, used by the Cache Instances to monitor connectivity with the remote Home Cache Clusters. The default value is `3`.
   * @return globalCachingHeartbeat
  **/
  @ApiModelProperty(example = "null", value = "The heartbeat interval, in seconds, used by the Cache Instances to monitor connectivity with the remote Home Cache Clusters. The default value is `3`.")
  public Long getGlobalCachingHeartbeat() {
    return globalCachingHeartbeat;
  }

  public void setGlobalCachingHeartbeat(Long globalCachingHeartbeat) {
    this.globalCachingHeartbeat = globalCachingHeartbeat;
  }

  public MsgVpnDistributedCacheCluster globalCachingTopicLifetime(Long globalCachingTopicLifetime) {
    this.globalCachingTopicLifetime = globalCachingTopicLifetime;
    return this;
  }

   /**
   * The topic lifetime, in seconds. If no client requests are received for a given global topic over the duration of the topic lifetime, then the Cache Instance will remove the subscription and cached messages for that topic. A value of 0 disables aging. The default value is `3600`.
   * @return globalCachingTopicLifetime
  **/
  @ApiModelProperty(example = "null", value = "The topic lifetime, in seconds. If no client requests are received for a given global topic over the duration of the topic lifetime, then the Cache Instance will remove the subscription and cached messages for that topic. A value of 0 disables aging. The default value is `3600`.")
  public Long getGlobalCachingTopicLifetime() {
    return globalCachingTopicLifetime;
  }

  public void setGlobalCachingTopicLifetime(Long globalCachingTopicLifetime) {
    this.globalCachingTopicLifetime = globalCachingTopicLifetime;
  }

  public MsgVpnDistributedCacheCluster maxMemory(Long maxMemory) {
    this.maxMemory = maxMemory;
    return this;
  }

   /**
   * The maximum memory usage, in megabytes (MB), for each Cache Instance in the Cache Cluster. The default value is `2048`.
   * @return maxMemory
  **/
  @ApiModelProperty(example = "null", value = "The maximum memory usage, in megabytes (MB), for each Cache Instance in the Cache Cluster. The default value is `2048`.")
  public Long getMaxMemory() {
    return maxMemory;
  }

  public void setMaxMemory(Long maxMemory) {
    this.maxMemory = maxMemory;
  }

  public MsgVpnDistributedCacheCluster maxMsgsPerTopic(Long maxMsgsPerTopic) {
    this.maxMsgsPerTopic = maxMsgsPerTopic;
    return this;
  }

   /**
   * The maximum number of messages per topic for each Cache Instance in the Cache Cluster. When at the maximum, old messages are removed as new messages arrive. The default value is `1`.
   * @return maxMsgsPerTopic
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of messages per topic for each Cache Instance in the Cache Cluster. When at the maximum, old messages are removed as new messages arrive. The default value is `1`.")
  public Long getMaxMsgsPerTopic() {
    return maxMsgsPerTopic;
  }

  public void setMaxMsgsPerTopic(Long maxMsgsPerTopic) {
    this.maxMsgsPerTopic = maxMsgsPerTopic;
  }

  public MsgVpnDistributedCacheCluster maxRequestQueueDepth(Long maxRequestQueueDepth) {
    this.maxRequestQueueDepth = maxRequestQueueDepth;
    return this;
  }

   /**
   * The maximum queue depth for cache requests received by the Cache Cluster. The default value is `100000`.
   * @return maxRequestQueueDepth
  **/
  @ApiModelProperty(example = "null", value = "The maximum queue depth for cache requests received by the Cache Cluster. The default value is `100000`.")
  public Long getMaxRequestQueueDepth() {
    return maxRequestQueueDepth;
  }

  public void setMaxRequestQueueDepth(Long maxRequestQueueDepth) {
    this.maxRequestQueueDepth = maxRequestQueueDepth;
  }

  public MsgVpnDistributedCacheCluster maxTopicCount(Long maxTopicCount) {
    this.maxTopicCount = maxTopicCount;
    return this;
  }

   /**
   * The maximum number of topics for each Cache Instance in the Cache Cluster. The default value is `2000000`.
   * @return maxTopicCount
  **/
  @ApiModelProperty(example = "null", value = "The maximum number of topics for each Cache Instance in the Cache Cluster. The default value is `2000000`.")
  public Long getMaxTopicCount() {
    return maxTopicCount;
  }

  public void setMaxTopicCount(Long maxTopicCount) {
    this.maxTopicCount = maxTopicCount;
  }

  public MsgVpnDistributedCacheCluster msgLifetime(Long msgLifetime) {
    this.msgLifetime = msgLifetime;
    return this;
  }

   /**
   * The message lifetime, in seconds. If a message remains cached for the duration of its lifetime, the Cache Instance will remove the message. A lifetime of 0 results in the message being retained indefinitely. The default is to have no `msgLifetime`.
   * @return msgLifetime
  **/
  @ApiModelProperty(example = "null", value = "The message lifetime, in seconds. If a message remains cached for the duration of its lifetime, the Cache Instance will remove the message. A lifetime of 0 results in the message being retained indefinitely. The default is to have no `msgLifetime`.")
  public Long getMsgLifetime() {
    return msgLifetime;
  }

  public void setMsgLifetime(Long msgLifetime) {
    this.msgLifetime = msgLifetime;
  }

  public MsgVpnDistributedCacheCluster msgVpnName(String msgVpnName) {
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

  public MsgVpnDistributedCacheCluster newTopicAdvertisementEnabled(Boolean newTopicAdvertisementEnabled) {
    this.newTopicAdvertisementEnabled = newTopicAdvertisementEnabled;
    return this;
  }

   /**
   * Enable or disable the advertising, onto the message bus, of new topics learned by each Cache Instance in the Cache Cluster. The default value is `false`.
   * @return newTopicAdvertisementEnabled
  **/
  @ApiModelProperty(example = "null", value = "Enable or disable the advertising, onto the message bus, of new topics learned by each Cache Instance in the Cache Cluster. The default value is `false`.")
  public Boolean getNewTopicAdvertisementEnabled() {
    return newTopicAdvertisementEnabled;
  }

  public void setNewTopicAdvertisementEnabled(Boolean newTopicAdvertisementEnabled) {
    this.newTopicAdvertisementEnabled = newTopicAdvertisementEnabled;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MsgVpnDistributedCacheCluster msgVpnDistributedCacheCluster = (MsgVpnDistributedCacheCluster) o;
    return Objects.equals(this.cacheName, msgVpnDistributedCacheCluster.cacheName) &&
        Objects.equals(this.clusterName, msgVpnDistributedCacheCluster.clusterName) &&
        Objects.equals(this.deliverToOneOverrideEnabled, msgVpnDistributedCacheCluster.deliverToOneOverrideEnabled) &&
        Objects.equals(this.enabled, msgVpnDistributedCacheCluster.enabled) &&
        Objects.equals(this.eventDataByteRateThreshold, msgVpnDistributedCacheCluster.eventDataByteRateThreshold) &&
        Objects.equals(this.eventDataMsgRateThreshold, msgVpnDistributedCacheCluster.eventDataMsgRateThreshold) &&
        Objects.equals(this.eventMaxMemoryThreshold, msgVpnDistributedCacheCluster.eventMaxMemoryThreshold) &&
        Objects.equals(this.eventMaxTopicsThreshold, msgVpnDistributedCacheCluster.eventMaxTopicsThreshold) &&
        Objects.equals(this.eventRequestQueueDepthThreshold, msgVpnDistributedCacheCluster.eventRequestQueueDepthThreshold) &&
        Objects.equals(this.eventRequestRateThreshold, msgVpnDistributedCacheCluster.eventRequestRateThreshold) &&
        Objects.equals(this.eventResponseRateThreshold, msgVpnDistributedCacheCluster.eventResponseRateThreshold) &&
        Objects.equals(this.globalCachingEnabled, msgVpnDistributedCacheCluster.globalCachingEnabled) &&
        Objects.equals(this.globalCachingHeartbeat, msgVpnDistributedCacheCluster.globalCachingHeartbeat) &&
        Objects.equals(this.globalCachingTopicLifetime, msgVpnDistributedCacheCluster.globalCachingTopicLifetime) &&
        Objects.equals(this.maxMemory, msgVpnDistributedCacheCluster.maxMemory) &&
        Objects.equals(this.maxMsgsPerTopic, msgVpnDistributedCacheCluster.maxMsgsPerTopic) &&
        Objects.equals(this.maxRequestQueueDepth, msgVpnDistributedCacheCluster.maxRequestQueueDepth) &&
        Objects.equals(this.maxTopicCount, msgVpnDistributedCacheCluster.maxTopicCount) &&
        Objects.equals(this.msgLifetime, msgVpnDistributedCacheCluster.msgLifetime) &&
        Objects.equals(this.msgVpnName, msgVpnDistributedCacheCluster.msgVpnName) &&
        Objects.equals(this.newTopicAdvertisementEnabled, msgVpnDistributedCacheCluster.newTopicAdvertisementEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cacheName, clusterName, deliverToOneOverrideEnabled, enabled, eventDataByteRateThreshold, eventDataMsgRateThreshold, eventMaxMemoryThreshold, eventMaxTopicsThreshold, eventRequestQueueDepthThreshold, eventRequestRateThreshold, eventResponseRateThreshold, globalCachingEnabled, globalCachingHeartbeat, globalCachingTopicLifetime, maxMemory, maxMsgsPerTopic, maxRequestQueueDepth, maxTopicCount, msgLifetime, msgVpnName, newTopicAdvertisementEnabled);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MsgVpnDistributedCacheCluster {\n");
    
    sb.append("    cacheName: ").append(toIndentedString(cacheName)).append("\n");
    sb.append("    clusterName: ").append(toIndentedString(clusterName)).append("\n");
    sb.append("    deliverToOneOverrideEnabled: ").append(toIndentedString(deliverToOneOverrideEnabled)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    eventDataByteRateThreshold: ").append(toIndentedString(eventDataByteRateThreshold)).append("\n");
    sb.append("    eventDataMsgRateThreshold: ").append(toIndentedString(eventDataMsgRateThreshold)).append("\n");
    sb.append("    eventMaxMemoryThreshold: ").append(toIndentedString(eventMaxMemoryThreshold)).append("\n");
    sb.append("    eventMaxTopicsThreshold: ").append(toIndentedString(eventMaxTopicsThreshold)).append("\n");
    sb.append("    eventRequestQueueDepthThreshold: ").append(toIndentedString(eventRequestQueueDepthThreshold)).append("\n");
    sb.append("    eventRequestRateThreshold: ").append(toIndentedString(eventRequestRateThreshold)).append("\n");
    sb.append("    eventResponseRateThreshold: ").append(toIndentedString(eventResponseRateThreshold)).append("\n");
    sb.append("    globalCachingEnabled: ").append(toIndentedString(globalCachingEnabled)).append("\n");
    sb.append("    globalCachingHeartbeat: ").append(toIndentedString(globalCachingHeartbeat)).append("\n");
    sb.append("    globalCachingTopicLifetime: ").append(toIndentedString(globalCachingTopicLifetime)).append("\n");
    sb.append("    maxMemory: ").append(toIndentedString(maxMemory)).append("\n");
    sb.append("    maxMsgsPerTopic: ").append(toIndentedString(maxMsgsPerTopic)).append("\n");
    sb.append("    maxRequestQueueDepth: ").append(toIndentedString(maxRequestQueueDepth)).append("\n");
    sb.append("    maxTopicCount: ").append(toIndentedString(maxTopicCount)).append("\n");
    sb.append("    msgLifetime: ").append(toIndentedString(msgLifetime)).append("\n");
    sb.append("    msgVpnName: ").append(toIndentedString(msgVpnName)).append("\n");
    sb.append("    newTopicAdvertisementEnabled: ").append(toIndentedString(newTopicAdvertisementEnabled)).append("\n");
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

