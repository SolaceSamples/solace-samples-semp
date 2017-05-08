=begin
#SEMP (Solace Element Management Protocol)

# SEMP (starting in `v2`, see [note 1](#notes)) is a RESTful API for configuring a Solace router.  SEMP uses URIs to address manageable **resources** of the Solace router. Resources are either individual **objects**, or **collections** of objects. The following APIs are provided:   API|Base Path|Purpose|Comments :---|:---|:---|:--- Configuration|/SEMP/v2/config|Reading and writing config state|See [note 2](#notes)    Resources are always nouns, with individual objects being singular and collections being plural. Objects within a collection are identified by an `obj-id`, which follows the collection name with the form `collection-name/obj-id`. Some examples:  <pre> /SEMP/v2/config/msgVpns                       ; MsgVpn collection /SEMP/v2/config/msgVpns/finance               ; MsgVpn object named \"finance\" /SEMP/v2/config/msgVpns/finance/queues        ; Queue collection within MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ ; Queue object named \"orderQ\" within MsgVpn \"finance\" </pre>  ## Collection Resources  Collections are unordered lists of objects (unless described as otherwise), and are described by JSON arrays. Each item in the array represents an object in the same manner as the individual object would normally be represented. The creation of a new object is done through its collection resource.  ## Object Resources  Objects are composed of attributes and collections, and are described by JSON content as name/value pairs. The collections of an object are not contained directly in the object's JSON content, rather the content includes a URI attribute which points to the collection. This contained collection resource must be managed as a separate resource through this URI.  At a minimum, every object has 1 or more identifying attributes, and its own `uri` attribute which contains the URI to itself. Attributes may have any (non-exclusively) of the following properties:   Property|Meaning|Comments :---|:---|:--- Identifying|Attribute is involved in unique identification of the object, and appears in its URI| Required|Attribute must be provided in the request| Read-Only|Attribute can only be read, not written|See [note 3](#notes) Write-Only|Attribute can only be written, not read| Requires-Disable|Attribute can only be changed when object is disabled| Deprecated|Attribute is deprecated, and will disappear in the next SEMP version|    In some requests, certain attributes may only be provided in certain combinations with other attributes:   Relationship|Meaning :---|:--- Requires|Attribute may only be changed by a request if a particular attribute or combination of attributes is also provided in the request Conflicts|Attribute may only be provided in a request if a particular attribute or combination of attributes is not also provided in the request    ## HTTP Methods  The HTTP methods of POST, PUT, PATCH, DELETE, and GET manipulate resources following these general principles:   Method|Resource|Meaning|Request Body|Response Body|Missing Request Attributes :---|:---|:---|:---|:---|:--- POST|Collection|Create object|Initial attribute values|Object attributes and metadata|Set to default PUT|Object|Replace object|New attribute values|Object attributes and metadata|Set to default (but see [note 4](#notes)) PATCH|Object|Update object|New attribute values|Object attributes and metadata | Left unchanged| DELETE|Object|Delete object|Empty|Object metadata|N/A GET|Object|Get object|Empty|Object attributes and metadata|N/A GET|Collection|Get collection|Empty|Object attributes and collection metadata|N/A    ## Common Query Parameters  The following are some common query parameters that are supported by many method/URI combinations. Individual URIs may document additional parameters. Note that multiple query parameters can be used together in a single URI, separated by the ampersand character. For example:  <pre> ; Request for the MsgVpns collection using two hypothetical query parameters ; \"q1\" and \"q2\" with values \"val1\" and \"val2\" respectively /SEMP/v2/config/msgVpns?q1=val1&q2=val2 </pre>  ### select  Include in the response only selected attributes of the object. Use this query parameter to limit the size of the returned data for each returned object, or return only those fields that are desired.  The value of `select` is a comma-separated list of attribute names. Names may include the `*` wildcard. Nested attribute names are supported using periods (e.g. `parentName.childName`). If the list is empty (i.e. `select=`) no attributes are returned; otherwise the list must match at least one attribute name of the object. Some examples:  <pre> ; List of all MsgVpn names /SEMP/v2/config/msgVpns?select=msgVpnName  ; Authentication attributes of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance?select=authentication*  ; Access related attributes of Queue \"orderQ\" of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ?select=owner,permission </pre>  ### where  Include in the response only objects where certain conditions are true. Use this query parameter to limit which objects are returned to those whose attribute values meet the given conditions.  The value of `where` is a comma-separated list of expressions. All expressions must be true for the object to be included in the response. Each expression takes the form:  <pre> expression  = attribute-name OP value OP          = '==' | '!=' | '<' | '>' | '<=' | '>=' </pre>  `value` may be a number, string, `true`, or `false`, as appropriate for the type of `attribute-name`. Greater-than and less-than comparisons only work for numbers. A `*` in a string `value` is interpreted as a wildcard. Some examples:  <pre> ; Only enabled MsgVpns /SEMP/v2/config/msgVpns?where=enabled==true  ; Only MsgVpns using basic non-LDAP authentication /SEMP/v2/config/msgVpns?where=authenticationBasicEnabled==true,authenticationBasicType!=ldap  ; Only MsgVpns that allow more than 100 client connections /SEMP/v2/config/msgVpns?where=maxConnectionCount>100 </pre>  ### count  Limit the count of objects in the response. This can be useful to limit the size of the response for large collections. The minimum value for `count` is `1` and the default is `10`. There is a hidden maximum as to prevent overloading the system. For example:  <pre> ; Up to 25 MsgVpns /SEMP/v2/config/msgVpns?count=25 </pre>  ### cursor  The cursor, or position, for the next page of objects. Cursors are opaque data that should not be created or interpreted by SEMP clients, and should only be used as described below.  When a request is made for a collection and there may be additional objects available for retrieval that are not included in the initial response, the response will include a `cursorQuery` field containing a cursor. The value of this field can be specified in the `cursor` query parameter of a subsequent request to retrieve the next page of objects. For convenience, an appropriate URI is constructed automatically by the router and included in the `nextPageUri` field of the response. This URI can be used directly to retrieve the next page of objects.  ## Notes  1. This specification defines SEMP starting in `v2`, and not the original SEMP `v1` interface. Request and response formats between `v1` and `v2` are entirely incompatible, although both protocols share a common port configuration on the Solace router. They are differentiated by the initial portion of the URI path, one of either `/SEMP/` or `/SEMP/v2/`. 2. The config API is partially implemented. Only a subset of all configurable objects are available. 3. Read-only attributes may appear in POST and PUT/PATCH requests. However, if a read-only attribute is not marked as identifying, it will be ignored during a PUT/PATCH. 4. For PUT, if the SEMP user is not authorized to modify the attribute, its value is left unchanged rather than set to default. In addition, the values of write-only attributes are not set to their defaults on a PUT. 5. For DELETE, the body of the request currently serves no purpose and will cause an error if not empty. 

OpenAPI spec version: 2.8.0.0.18
Contact: support_request@solacesystems.com
Generated by: https://github.com/swagger-api/swagger-codegen.git

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

=end

require 'date'

module SempClient

  class MsgVpnClientProfile
    # Enable or disable allowing bridge connections to login. The default value is `false`.
    attr_accessor :allow_bridge_connections_enabled

    # Enable or disable allowing a client to bind to topic endpoints or queues with cut-through forwarding. Changing this value does not affect existing sessions. The default value is `false`.
    attr_accessor :allow_cut_through_forwarding_enabled

    # Enable or disable allowing a client to create topic endponts or queues for the receiving of persistent or non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.
    attr_accessor :allow_guaranteed_endpoint_create_enabled

    # Enable or disable allowing a client to bind to topic endpoints or queues for the receiving of persistent or non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.
    attr_accessor :allow_guaranteed_msg_receive_enabled

    # Enable or disable allowing a client to send persistent and non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.
    attr_accessor :allow_guaranteed_msg_send_enabled

    # Enable or disable allowing a client to use trasacted sessions to bundle persistent or non-persistent message send and receives. Changing this value does not affect existing sessions. The default value is `false`.
    attr_accessor :allow_transacted_sessions_enabled

    # The name of a queue to copy settings from when a new queue is created by an API. The referenced queue must exist. The default is to have no `apiQueueManagementCopyFromOnCreateName`.
    attr_accessor :api_queue_management_copy_from_on_create_name

    # The name of a topic-endpoint to copy settings from when a new topic-endpoint is created by an API. The referenced topic-endpoint must exist. The default is to have no `apiTopicEndpointManagementCopyFromOnCreateName`.
    attr_accessor :api_topic_endpoint_management_copy_from_on_create_name

    # The name of the Client Profile.
    attr_accessor :client_profile_name

    # The eliding delay interval (in milliseconds). 0 means no delay in delivering the message to the client. The default value is `0`.
    attr_accessor :eliding_delay

    # Enables or disables eliding. The default value is `false`.
    attr_accessor :eliding_enabled

    # The maximum number of topics that can be tracked for eliding on a per client basis. The default value is `256`.
    attr_accessor :eliding_max_topic_count

    attr_accessor :event_client_provisioned_endpoint_spool_usage_threshold

    attr_accessor :event_connection_count_per_client_username_threshold

    attr_accessor :event_egress_flow_count_threshold

    attr_accessor :event_endpoint_count_per_client_username_threshold

    attr_accessor :event_ingress_flow_count_threshold

    attr_accessor :event_service_smf_connection_count_per_client_username_threshold

    attr_accessor :event_service_web_connection_count_per_client_username_threshold

    attr_accessor :event_subscription_count_threshold

    attr_accessor :event_transacted_session_count_threshold

    attr_accessor :event_transaction_count_threshold

    # The maximum number of client connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
    attr_accessor :max_connection_count_per_client_username

    # The maximum number of egress flows that can be created by a single client associated with this client-profile. The default value is `16000`.
    attr_accessor :max_egress_flow_count

    # The maximum number of queues and topic endpoints that can be created across clients using the same client-username associated with this client-profile. The default value is `16000`.
    attr_accessor :max_endpoint_count_per_client_username

    # The maximum number of ingress flows that can be created by a single client associated with this client-profile. The default value is `16000`.
    attr_accessor :max_ingress_flow_count

    # The maximum number of subscriptions for a single client associated with this client-profile. The default value is `5000000`.
    attr_accessor :max_subscription_count

    # The maximum number of transacted sessions that can be created by a single client associated with this client-profile. The default value is `10`.
    attr_accessor :max_transacted_session_count

    # The maximum number of transacted sessions that can be created by a single client associated with this client-profile. The default value is `16000`.
    attr_accessor :max_transaction_count

    # The name of the Message VPN.
    attr_accessor :msg_vpn_name

    # The maximum depth of the C-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.
    attr_accessor :queue_control1_max_depth

    # The minimum number of messages that must be on the C-1 queue before its depth is checked against the `queueControl1MaxDepth` setting. The default value is `4`.
    attr_accessor :queue_control1_min_msg_burst

    # The maximum depth of the D-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.
    attr_accessor :queue_direct1_max_depth

    # The minimum number of messages that must be on the D-1 queue before its depth is checked against the `queueDirect1MaxDepth` setting. The default value is `4`.
    attr_accessor :queue_direct1_min_msg_burst

    # The maximum depth of the D-2 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.
    attr_accessor :queue_direct2_max_depth

    # The minimum number of messages that must be on the D-2 queue before its depth is checked against the `queueDirect2MaxDepth` setting. The default value is `4`.
    attr_accessor :queue_direct2_min_msg_burst

    # The maximum depth of the D-3 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.
    attr_accessor :queue_direct3_max_depth

    # The minimum number of messages that must be on the D-3 queue before its depth is checked against the `queueDirect3MaxDepth` setting. The default value is `4`.
    attr_accessor :queue_direct3_min_msg_burst

    # The maximum depth of the G-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.
    attr_accessor :queue_guaranteed1_max_depth

    # The minimum number of messages that must be on the G-1 queue before its depth is checked against the `queueGuaranteed1MaxDepth` setting. The default value is `255`.
    attr_accessor :queue_guaranteed1_min_msg_burst

    # Enable or disable the sending of a negative acknowledgement on the discard of a guaranteed message. When a guaranteed message is published to a topic, and the router does not have any guaranteed subscriptions matching the message topic, the message can either be silently discarded, or a negative acknowledgement can be returned to the sender indicating that no guaranteed subscriptions were found for the message. It should be noted that even if the message is rejected to the publisher, it will still be delivered to any clients who have direct subscriptions to the topic. This configuration option does not affect the behavior of messages published to unknown queue names. That always results in the message being rejected to the sender. The default value is `false`.
    attr_accessor :reject_msg_to_sender_on_no_subscription_match_enabled

    # Enable or disable whether clients using this client profile are allowed to connect to the Message VPN if its replication is in standby state. The default value is `false`.
    attr_accessor :replication_allow_client_connect_when_standby_enabled

    # The maximum number of SMF client connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
    attr_accessor :service_smf_max_connection_count_per_client_username

    # The number of seconds during which the client must send a request or else the session is terminated. The default value is `30`.
    attr_accessor :service_web_inactive_timeout

    # The maximum number of web-transport connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
    attr_accessor :service_web_max_connection_count_per_client_username

    # The maximum number of bytes allowed in a single web transport payload before fragmentation occurs, not including the header. The default value is `1000000`.
    attr_accessor :service_web_max_payload

    # The TCP initial congestion window size for clients belonging to this profile.   The initial congestion window size is used when starting up a TCP connection or recovery from idle (that is, no traffic). It is the number of segments TCP sends before waiting for an acknowledgement from the peer. Larger values of initial window allows a connection to come up to speed quickly. However, care must be taken for if this parameter's value is too high, it may cause congestion in the network. For further details on initial window, refer to RFC 2581. Changing this parameter changes all clients matching this profile, whether already connected or not.   Changing the initial window from its default of 2 results in non-compliance with RFC 2581. Contact Solace Support personnel before changing this parameter. The default value is `2`.
    attr_accessor :tcp_congestion_window_size

    # The number of keepalive probes TCP should send before dropping the connection. The default value is `5`.
    attr_accessor :tcp_keepalive_count

    # The time (in seconds) a connection needs to remain idle before TCP begins sending keepalive probes. The default value is `3`.
    attr_accessor :tcp_keepalive_idle_time

    # The time between individual keepalive probes, when no response is received. The default value is `1`.
    attr_accessor :tcp_keepalive_interval

    # The TCP maximum segment size for the clients belonging to this profile. The default value is `1460`.
    attr_accessor :tcp_max_segment_size

    # The TCP maximum window size (in KB) for clients belonging to this profile. Changes are applied to all existing connections. The maximum window should be at least the bandwidth-delay product of the link between the TCP peers. If the maximum window is less than the bandwidth-delay product, then the TCP connection operates below its maximum potential throughput. If the maximum window is less than about twice the bandwidth-delay product, then occasional packet loss causes TCP connection to operate below its maximum potential throughput as it handles the missing ACKs and retransmissions. There are also problems with a maximum window that's too large. In the presence of a high offered load, TCP gradually increases its congestion window until either (a) the congestion window reaches the maximum window, or (b) packet loss occurs in the network. Initially, when the congestion window is small, the network's physical bandwidth-delay acts as a memory buffer for packets in flight. As the congestion window crosses the bandwidth-delay product, though, the buffering of in-flight packets moves to queues in various switches, routers, etc. in the network. As the congestion window continues to increase, some such queue in some equipment overflows, causing packet loss and TCP back-off. The default value is `256`.
    attr_accessor :tcp_max_window_size


    # Attribute mapping from ruby-style variable name to JSON key.
    def self.attribute_map
      {
        :'allow_bridge_connections_enabled' => :'allowBridgeConnectionsEnabled',
        :'allow_cut_through_forwarding_enabled' => :'allowCutThroughForwardingEnabled',
        :'allow_guaranteed_endpoint_create_enabled' => :'allowGuaranteedEndpointCreateEnabled',
        :'allow_guaranteed_msg_receive_enabled' => :'allowGuaranteedMsgReceiveEnabled',
        :'allow_guaranteed_msg_send_enabled' => :'allowGuaranteedMsgSendEnabled',
        :'allow_transacted_sessions_enabled' => :'allowTransactedSessionsEnabled',
        :'api_queue_management_copy_from_on_create_name' => :'apiQueueManagementCopyFromOnCreateName',
        :'api_topic_endpoint_management_copy_from_on_create_name' => :'apiTopicEndpointManagementCopyFromOnCreateName',
        :'client_profile_name' => :'clientProfileName',
        :'eliding_delay' => :'elidingDelay',
        :'eliding_enabled' => :'elidingEnabled',
        :'eliding_max_topic_count' => :'elidingMaxTopicCount',
        :'event_client_provisioned_endpoint_spool_usage_threshold' => :'eventClientProvisionedEndpointSpoolUsageThreshold',
        :'event_connection_count_per_client_username_threshold' => :'eventConnectionCountPerClientUsernameThreshold',
        :'event_egress_flow_count_threshold' => :'eventEgressFlowCountThreshold',
        :'event_endpoint_count_per_client_username_threshold' => :'eventEndpointCountPerClientUsernameThreshold',
        :'event_ingress_flow_count_threshold' => :'eventIngressFlowCountThreshold',
        :'event_service_smf_connection_count_per_client_username_threshold' => :'eventServiceSmfConnectionCountPerClientUsernameThreshold',
        :'event_service_web_connection_count_per_client_username_threshold' => :'eventServiceWebConnectionCountPerClientUsernameThreshold',
        :'event_subscription_count_threshold' => :'eventSubscriptionCountThreshold',
        :'event_transacted_session_count_threshold' => :'eventTransactedSessionCountThreshold',
        :'event_transaction_count_threshold' => :'eventTransactionCountThreshold',
        :'max_connection_count_per_client_username' => :'maxConnectionCountPerClientUsername',
        :'max_egress_flow_count' => :'maxEgressFlowCount',
        :'max_endpoint_count_per_client_username' => :'maxEndpointCountPerClientUsername',
        :'max_ingress_flow_count' => :'maxIngressFlowCount',
        :'max_subscription_count' => :'maxSubscriptionCount',
        :'max_transacted_session_count' => :'maxTransactedSessionCount',
        :'max_transaction_count' => :'maxTransactionCount',
        :'msg_vpn_name' => :'msgVpnName',
        :'queue_control1_max_depth' => :'queueControl1MaxDepth',
        :'queue_control1_min_msg_burst' => :'queueControl1MinMsgBurst',
        :'queue_direct1_max_depth' => :'queueDirect1MaxDepth',
        :'queue_direct1_min_msg_burst' => :'queueDirect1MinMsgBurst',
        :'queue_direct2_max_depth' => :'queueDirect2MaxDepth',
        :'queue_direct2_min_msg_burst' => :'queueDirect2MinMsgBurst',
        :'queue_direct3_max_depth' => :'queueDirect3MaxDepth',
        :'queue_direct3_min_msg_burst' => :'queueDirect3MinMsgBurst',
        :'queue_guaranteed1_max_depth' => :'queueGuaranteed1MaxDepth',
        :'queue_guaranteed1_min_msg_burst' => :'queueGuaranteed1MinMsgBurst',
        :'reject_msg_to_sender_on_no_subscription_match_enabled' => :'rejectMsgToSenderOnNoSubscriptionMatchEnabled',
        :'replication_allow_client_connect_when_standby_enabled' => :'replicationAllowClientConnectWhenStandbyEnabled',
        :'service_smf_max_connection_count_per_client_username' => :'serviceSmfMaxConnectionCountPerClientUsername',
        :'service_web_inactive_timeout' => :'serviceWebInactiveTimeout',
        :'service_web_max_connection_count_per_client_username' => :'serviceWebMaxConnectionCountPerClientUsername',
        :'service_web_max_payload' => :'serviceWebMaxPayload',
        :'tcp_congestion_window_size' => :'tcpCongestionWindowSize',
        :'tcp_keepalive_count' => :'tcpKeepaliveCount',
        :'tcp_keepalive_idle_time' => :'tcpKeepaliveIdleTime',
        :'tcp_keepalive_interval' => :'tcpKeepaliveInterval',
        :'tcp_max_segment_size' => :'tcpMaxSegmentSize',
        :'tcp_max_window_size' => :'tcpMaxWindowSize'
      }
    end

    # Attribute type mapping.
    def self.swagger_types
      {
        :'allow_bridge_connections_enabled' => :'BOOLEAN',
        :'allow_cut_through_forwarding_enabled' => :'BOOLEAN',
        :'allow_guaranteed_endpoint_create_enabled' => :'BOOLEAN',
        :'allow_guaranteed_msg_receive_enabled' => :'BOOLEAN',
        :'allow_guaranteed_msg_send_enabled' => :'BOOLEAN',
        :'allow_transacted_sessions_enabled' => :'BOOLEAN',
        :'api_queue_management_copy_from_on_create_name' => :'String',
        :'api_topic_endpoint_management_copy_from_on_create_name' => :'String',
        :'client_profile_name' => :'String',
        :'eliding_delay' => :'Integer',
        :'eliding_enabled' => :'BOOLEAN',
        :'eliding_max_topic_count' => :'Integer',
        :'event_client_provisioned_endpoint_spool_usage_threshold' => :'EventThresholdByPercent',
        :'event_connection_count_per_client_username_threshold' => :'EventThreshold',
        :'event_egress_flow_count_threshold' => :'EventThreshold',
        :'event_endpoint_count_per_client_username_threshold' => :'EventThreshold',
        :'event_ingress_flow_count_threshold' => :'EventThreshold',
        :'event_service_smf_connection_count_per_client_username_threshold' => :'EventThreshold',
        :'event_service_web_connection_count_per_client_username_threshold' => :'EventThreshold',
        :'event_subscription_count_threshold' => :'EventThreshold',
        :'event_transacted_session_count_threshold' => :'EventThreshold',
        :'event_transaction_count_threshold' => :'EventThreshold',
        :'max_connection_count_per_client_username' => :'Integer',
        :'max_egress_flow_count' => :'Integer',
        :'max_endpoint_count_per_client_username' => :'Integer',
        :'max_ingress_flow_count' => :'Integer',
        :'max_subscription_count' => :'Integer',
        :'max_transacted_session_count' => :'Integer',
        :'max_transaction_count' => :'Integer',
        :'msg_vpn_name' => :'String',
        :'queue_control1_max_depth' => :'Integer',
        :'queue_control1_min_msg_burst' => :'Integer',
        :'queue_direct1_max_depth' => :'Integer',
        :'queue_direct1_min_msg_burst' => :'Integer',
        :'queue_direct2_max_depth' => :'Integer',
        :'queue_direct2_min_msg_burst' => :'Integer',
        :'queue_direct3_max_depth' => :'Integer',
        :'queue_direct3_min_msg_burst' => :'Integer',
        :'queue_guaranteed1_max_depth' => :'Integer',
        :'queue_guaranteed1_min_msg_burst' => :'Integer',
        :'reject_msg_to_sender_on_no_subscription_match_enabled' => :'BOOLEAN',
        :'replication_allow_client_connect_when_standby_enabled' => :'BOOLEAN',
        :'service_smf_max_connection_count_per_client_username' => :'Integer',
        :'service_web_inactive_timeout' => :'Integer',
        :'service_web_max_connection_count_per_client_username' => :'Integer',
        :'service_web_max_payload' => :'Integer',
        :'tcp_congestion_window_size' => :'Integer',
        :'tcp_keepalive_count' => :'Integer',
        :'tcp_keepalive_idle_time' => :'Integer',
        :'tcp_keepalive_interval' => :'Integer',
        :'tcp_max_segment_size' => :'Integer',
        :'tcp_max_window_size' => :'Integer'
      }
    end

    # Initializes the object
    # @param [Hash] attributes Model attributes in the form of hash
    def initialize(attributes = {})
      return unless attributes.is_a?(Hash)

      # convert string to symbol for hash key
      attributes = attributes.each_with_object({}){|(k,v), h| h[k.to_sym] = v}

      if attributes.has_key?(:'allowBridgeConnectionsEnabled')
        self.allow_bridge_connections_enabled = attributes[:'allowBridgeConnectionsEnabled']
      end

      if attributes.has_key?(:'allowCutThroughForwardingEnabled')
        self.allow_cut_through_forwarding_enabled = attributes[:'allowCutThroughForwardingEnabled']
      end

      if attributes.has_key?(:'allowGuaranteedEndpointCreateEnabled')
        self.allow_guaranteed_endpoint_create_enabled = attributes[:'allowGuaranteedEndpointCreateEnabled']
      end

      if attributes.has_key?(:'allowGuaranteedMsgReceiveEnabled')
        self.allow_guaranteed_msg_receive_enabled = attributes[:'allowGuaranteedMsgReceiveEnabled']
      end

      if attributes.has_key?(:'allowGuaranteedMsgSendEnabled')
        self.allow_guaranteed_msg_send_enabled = attributes[:'allowGuaranteedMsgSendEnabled']
      end

      if attributes.has_key?(:'allowTransactedSessionsEnabled')
        self.allow_transacted_sessions_enabled = attributes[:'allowTransactedSessionsEnabled']
      end

      if attributes.has_key?(:'apiQueueManagementCopyFromOnCreateName')
        self.api_queue_management_copy_from_on_create_name = attributes[:'apiQueueManagementCopyFromOnCreateName']
      end

      if attributes.has_key?(:'apiTopicEndpointManagementCopyFromOnCreateName')
        self.api_topic_endpoint_management_copy_from_on_create_name = attributes[:'apiTopicEndpointManagementCopyFromOnCreateName']
      end

      if attributes.has_key?(:'clientProfileName')
        self.client_profile_name = attributes[:'clientProfileName']
      end

      if attributes.has_key?(:'elidingDelay')
        self.eliding_delay = attributes[:'elidingDelay']
      end

      if attributes.has_key?(:'elidingEnabled')
        self.eliding_enabled = attributes[:'elidingEnabled']
      end

      if attributes.has_key?(:'elidingMaxTopicCount')
        self.eliding_max_topic_count = attributes[:'elidingMaxTopicCount']
      end

      if attributes.has_key?(:'eventClientProvisionedEndpointSpoolUsageThreshold')
        self.event_client_provisioned_endpoint_spool_usage_threshold = attributes[:'eventClientProvisionedEndpointSpoolUsageThreshold']
      end

      if attributes.has_key?(:'eventConnectionCountPerClientUsernameThreshold')
        self.event_connection_count_per_client_username_threshold = attributes[:'eventConnectionCountPerClientUsernameThreshold']
      end

      if attributes.has_key?(:'eventEgressFlowCountThreshold')
        self.event_egress_flow_count_threshold = attributes[:'eventEgressFlowCountThreshold']
      end

      if attributes.has_key?(:'eventEndpointCountPerClientUsernameThreshold')
        self.event_endpoint_count_per_client_username_threshold = attributes[:'eventEndpointCountPerClientUsernameThreshold']
      end

      if attributes.has_key?(:'eventIngressFlowCountThreshold')
        self.event_ingress_flow_count_threshold = attributes[:'eventIngressFlowCountThreshold']
      end

      if attributes.has_key?(:'eventServiceSmfConnectionCountPerClientUsernameThreshold')
        self.event_service_smf_connection_count_per_client_username_threshold = attributes[:'eventServiceSmfConnectionCountPerClientUsernameThreshold']
      end

      if attributes.has_key?(:'eventServiceWebConnectionCountPerClientUsernameThreshold')
        self.event_service_web_connection_count_per_client_username_threshold = attributes[:'eventServiceWebConnectionCountPerClientUsernameThreshold']
      end

      if attributes.has_key?(:'eventSubscriptionCountThreshold')
        self.event_subscription_count_threshold = attributes[:'eventSubscriptionCountThreshold']
      end

      if attributes.has_key?(:'eventTransactedSessionCountThreshold')
        self.event_transacted_session_count_threshold = attributes[:'eventTransactedSessionCountThreshold']
      end

      if attributes.has_key?(:'eventTransactionCountThreshold')
        self.event_transaction_count_threshold = attributes[:'eventTransactionCountThreshold']
      end

      if attributes.has_key?(:'maxConnectionCountPerClientUsername')
        self.max_connection_count_per_client_username = attributes[:'maxConnectionCountPerClientUsername']
      end

      if attributes.has_key?(:'maxEgressFlowCount')
        self.max_egress_flow_count = attributes[:'maxEgressFlowCount']
      end

      if attributes.has_key?(:'maxEndpointCountPerClientUsername')
        self.max_endpoint_count_per_client_username = attributes[:'maxEndpointCountPerClientUsername']
      end

      if attributes.has_key?(:'maxIngressFlowCount')
        self.max_ingress_flow_count = attributes[:'maxIngressFlowCount']
      end

      if attributes.has_key?(:'maxSubscriptionCount')
        self.max_subscription_count = attributes[:'maxSubscriptionCount']
      end

      if attributes.has_key?(:'maxTransactedSessionCount')
        self.max_transacted_session_count = attributes[:'maxTransactedSessionCount']
      end

      if attributes.has_key?(:'maxTransactionCount')
        self.max_transaction_count = attributes[:'maxTransactionCount']
      end

      if attributes.has_key?(:'msgVpnName')
        self.msg_vpn_name = attributes[:'msgVpnName']
      end

      if attributes.has_key?(:'queueControl1MaxDepth')
        self.queue_control1_max_depth = attributes[:'queueControl1MaxDepth']
      end

      if attributes.has_key?(:'queueControl1MinMsgBurst')
        self.queue_control1_min_msg_burst = attributes[:'queueControl1MinMsgBurst']
      end

      if attributes.has_key?(:'queueDirect1MaxDepth')
        self.queue_direct1_max_depth = attributes[:'queueDirect1MaxDepth']
      end

      if attributes.has_key?(:'queueDirect1MinMsgBurst')
        self.queue_direct1_min_msg_burst = attributes[:'queueDirect1MinMsgBurst']
      end

      if attributes.has_key?(:'queueDirect2MaxDepth')
        self.queue_direct2_max_depth = attributes[:'queueDirect2MaxDepth']
      end

      if attributes.has_key?(:'queueDirect2MinMsgBurst')
        self.queue_direct2_min_msg_burst = attributes[:'queueDirect2MinMsgBurst']
      end

      if attributes.has_key?(:'queueDirect3MaxDepth')
        self.queue_direct3_max_depth = attributes[:'queueDirect3MaxDepth']
      end

      if attributes.has_key?(:'queueDirect3MinMsgBurst')
        self.queue_direct3_min_msg_burst = attributes[:'queueDirect3MinMsgBurst']
      end

      if attributes.has_key?(:'queueGuaranteed1MaxDepth')
        self.queue_guaranteed1_max_depth = attributes[:'queueGuaranteed1MaxDepth']
      end

      if attributes.has_key?(:'queueGuaranteed1MinMsgBurst')
        self.queue_guaranteed1_min_msg_burst = attributes[:'queueGuaranteed1MinMsgBurst']
      end

      if attributes.has_key?(:'rejectMsgToSenderOnNoSubscriptionMatchEnabled')
        self.reject_msg_to_sender_on_no_subscription_match_enabled = attributes[:'rejectMsgToSenderOnNoSubscriptionMatchEnabled']
      end

      if attributes.has_key?(:'replicationAllowClientConnectWhenStandbyEnabled')
        self.replication_allow_client_connect_when_standby_enabled = attributes[:'replicationAllowClientConnectWhenStandbyEnabled']
      end

      if attributes.has_key?(:'serviceSmfMaxConnectionCountPerClientUsername')
        self.service_smf_max_connection_count_per_client_username = attributes[:'serviceSmfMaxConnectionCountPerClientUsername']
      end

      if attributes.has_key?(:'serviceWebInactiveTimeout')
        self.service_web_inactive_timeout = attributes[:'serviceWebInactiveTimeout']
      end

      if attributes.has_key?(:'serviceWebMaxConnectionCountPerClientUsername')
        self.service_web_max_connection_count_per_client_username = attributes[:'serviceWebMaxConnectionCountPerClientUsername']
      end

      if attributes.has_key?(:'serviceWebMaxPayload')
        self.service_web_max_payload = attributes[:'serviceWebMaxPayload']
      end

      if attributes.has_key?(:'tcpCongestionWindowSize')
        self.tcp_congestion_window_size = attributes[:'tcpCongestionWindowSize']
      end

      if attributes.has_key?(:'tcpKeepaliveCount')
        self.tcp_keepalive_count = attributes[:'tcpKeepaliveCount']
      end

      if attributes.has_key?(:'tcpKeepaliveIdleTime')
        self.tcp_keepalive_idle_time = attributes[:'tcpKeepaliveIdleTime']
      end

      if attributes.has_key?(:'tcpKeepaliveInterval')
        self.tcp_keepalive_interval = attributes[:'tcpKeepaliveInterval']
      end

      if attributes.has_key?(:'tcpMaxSegmentSize')
        self.tcp_max_segment_size = attributes[:'tcpMaxSegmentSize']
      end

      if attributes.has_key?(:'tcpMaxWindowSize')
        self.tcp_max_window_size = attributes[:'tcpMaxWindowSize']
      end

    end

    # Show invalid properties with the reasons. Usually used together with valid?
    # @return Array for valid properies with the reasons
    def list_invalid_properties
      invalid_properties = Array.new
      return invalid_properties
    end

    # Check to see if the all the properties in the model are valid
    # @return true if the model is valid
    def valid?
      return true
    end

    # Checks equality by comparing each attribute.
    # @param [Object] Object to be compared
    def ==(o)
      return true if self.equal?(o)
      self.class == o.class &&
          allow_bridge_connections_enabled == o.allow_bridge_connections_enabled &&
          allow_cut_through_forwarding_enabled == o.allow_cut_through_forwarding_enabled &&
          allow_guaranteed_endpoint_create_enabled == o.allow_guaranteed_endpoint_create_enabled &&
          allow_guaranteed_msg_receive_enabled == o.allow_guaranteed_msg_receive_enabled &&
          allow_guaranteed_msg_send_enabled == o.allow_guaranteed_msg_send_enabled &&
          allow_transacted_sessions_enabled == o.allow_transacted_sessions_enabled &&
          api_queue_management_copy_from_on_create_name == o.api_queue_management_copy_from_on_create_name &&
          api_topic_endpoint_management_copy_from_on_create_name == o.api_topic_endpoint_management_copy_from_on_create_name &&
          client_profile_name == o.client_profile_name &&
          eliding_delay == o.eliding_delay &&
          eliding_enabled == o.eliding_enabled &&
          eliding_max_topic_count == o.eliding_max_topic_count &&
          event_client_provisioned_endpoint_spool_usage_threshold == o.event_client_provisioned_endpoint_spool_usage_threshold &&
          event_connection_count_per_client_username_threshold == o.event_connection_count_per_client_username_threshold &&
          event_egress_flow_count_threshold == o.event_egress_flow_count_threshold &&
          event_endpoint_count_per_client_username_threshold == o.event_endpoint_count_per_client_username_threshold &&
          event_ingress_flow_count_threshold == o.event_ingress_flow_count_threshold &&
          event_service_smf_connection_count_per_client_username_threshold == o.event_service_smf_connection_count_per_client_username_threshold &&
          event_service_web_connection_count_per_client_username_threshold == o.event_service_web_connection_count_per_client_username_threshold &&
          event_subscription_count_threshold == o.event_subscription_count_threshold &&
          event_transacted_session_count_threshold == o.event_transacted_session_count_threshold &&
          event_transaction_count_threshold == o.event_transaction_count_threshold &&
          max_connection_count_per_client_username == o.max_connection_count_per_client_username &&
          max_egress_flow_count == o.max_egress_flow_count &&
          max_endpoint_count_per_client_username == o.max_endpoint_count_per_client_username &&
          max_ingress_flow_count == o.max_ingress_flow_count &&
          max_subscription_count == o.max_subscription_count &&
          max_transacted_session_count == o.max_transacted_session_count &&
          max_transaction_count == o.max_transaction_count &&
          msg_vpn_name == o.msg_vpn_name &&
          queue_control1_max_depth == o.queue_control1_max_depth &&
          queue_control1_min_msg_burst == o.queue_control1_min_msg_burst &&
          queue_direct1_max_depth == o.queue_direct1_max_depth &&
          queue_direct1_min_msg_burst == o.queue_direct1_min_msg_burst &&
          queue_direct2_max_depth == o.queue_direct2_max_depth &&
          queue_direct2_min_msg_burst == o.queue_direct2_min_msg_burst &&
          queue_direct3_max_depth == o.queue_direct3_max_depth &&
          queue_direct3_min_msg_burst == o.queue_direct3_min_msg_burst &&
          queue_guaranteed1_max_depth == o.queue_guaranteed1_max_depth &&
          queue_guaranteed1_min_msg_burst == o.queue_guaranteed1_min_msg_burst &&
          reject_msg_to_sender_on_no_subscription_match_enabled == o.reject_msg_to_sender_on_no_subscription_match_enabled &&
          replication_allow_client_connect_when_standby_enabled == o.replication_allow_client_connect_when_standby_enabled &&
          service_smf_max_connection_count_per_client_username == o.service_smf_max_connection_count_per_client_username &&
          service_web_inactive_timeout == o.service_web_inactive_timeout &&
          service_web_max_connection_count_per_client_username == o.service_web_max_connection_count_per_client_username &&
          service_web_max_payload == o.service_web_max_payload &&
          tcp_congestion_window_size == o.tcp_congestion_window_size &&
          tcp_keepalive_count == o.tcp_keepalive_count &&
          tcp_keepalive_idle_time == o.tcp_keepalive_idle_time &&
          tcp_keepalive_interval == o.tcp_keepalive_interval &&
          tcp_max_segment_size == o.tcp_max_segment_size &&
          tcp_max_window_size == o.tcp_max_window_size
    end

    # @see the `==` method
    # @param [Object] Object to be compared
    def eql?(o)
      self == o
    end

    # Calculates hash code according to all attributes.
    # @return [Fixnum] Hash code
    def hash
      [allow_bridge_connections_enabled, allow_cut_through_forwarding_enabled, allow_guaranteed_endpoint_create_enabled, allow_guaranteed_msg_receive_enabled, allow_guaranteed_msg_send_enabled, allow_transacted_sessions_enabled, api_queue_management_copy_from_on_create_name, api_topic_endpoint_management_copy_from_on_create_name, client_profile_name, eliding_delay, eliding_enabled, eliding_max_topic_count, event_client_provisioned_endpoint_spool_usage_threshold, event_connection_count_per_client_username_threshold, event_egress_flow_count_threshold, event_endpoint_count_per_client_username_threshold, event_ingress_flow_count_threshold, event_service_smf_connection_count_per_client_username_threshold, event_service_web_connection_count_per_client_username_threshold, event_subscription_count_threshold, event_transacted_session_count_threshold, event_transaction_count_threshold, max_connection_count_per_client_username, max_egress_flow_count, max_endpoint_count_per_client_username, max_ingress_flow_count, max_subscription_count, max_transacted_session_count, max_transaction_count, msg_vpn_name, queue_control1_max_depth, queue_control1_min_msg_burst, queue_direct1_max_depth, queue_direct1_min_msg_burst, queue_direct2_max_depth, queue_direct2_min_msg_burst, queue_direct3_max_depth, queue_direct3_min_msg_burst, queue_guaranteed1_max_depth, queue_guaranteed1_min_msg_burst, reject_msg_to_sender_on_no_subscription_match_enabled, replication_allow_client_connect_when_standby_enabled, service_smf_max_connection_count_per_client_username, service_web_inactive_timeout, service_web_max_connection_count_per_client_username, service_web_max_payload, tcp_congestion_window_size, tcp_keepalive_count, tcp_keepalive_idle_time, tcp_keepalive_interval, tcp_max_segment_size, tcp_max_window_size].hash
    end

    # Builds the object from hash
    # @param [Hash] attributes Model attributes in the form of hash
    # @return [Object] Returns the model itself
    def build_from_hash(attributes)
      return nil unless attributes.is_a?(Hash)
      self.class.swagger_types.each_pair do |key, type|
        if type =~ /^Array<(.*)>/i
          # check to ensure the input is an array given that the the attribute
          # is documented as an array but the input is not
          if attributes[self.class.attribute_map[key]].is_a?(Array)
            self.send("#{key}=", attributes[self.class.attribute_map[key]].map{ |v| _deserialize($1, v) } )
          end
        elsif !attributes[self.class.attribute_map[key]].nil?
          self.send("#{key}=", _deserialize(type, attributes[self.class.attribute_map[key]]))
        end # or else data not found in attributes(hash), not an issue as the data can be optional
      end

      self
    end

    # Deserializes the data based on type
    # @param string type Data type
    # @param string value Value to be deserialized
    # @return [Object] Deserialized data
    def _deserialize(type, value)
      case type.to_sym
      when :DateTime
        DateTime.parse(value)
      when :Date
        Date.parse(value)
      when :String
        value.to_s
      when :Integer
        value.to_i
      when :Float
        value.to_f
      when :BOOLEAN
        if value.to_s =~ /^(true|t|yes|y|1)$/i
          true
        else
          false
        end
      when :Object
        # generic object (usually a Hash), return directly
        value
      when /\AArray<(?<inner_type>.+)>\z/
        inner_type = Regexp.last_match[:inner_type]
        value.map { |v| _deserialize(inner_type, v) }
      when /\AHash<(?<k_type>.+), (?<v_type>.+)>\z/
        k_type = Regexp.last_match[:k_type]
        v_type = Regexp.last_match[:v_type]
        {}.tap do |hash|
          value.each do |k, v|
            hash[_deserialize(k_type, k)] = _deserialize(v_type, v)
          end
        end
      else # model
        temp_model = SempClient.const_get(type).new
        temp_model.build_from_hash(value)
      end
    end

    # Returns the string representation of the object
    # @return [String] String presentation of the object
    def to_s
      to_hash.to_s
    end

    # to_body is an alias to to_hash (backward compatibility)
    # @return [Hash] Returns the object in the form of hash
    def to_body
      to_hash
    end

    # Returns the object in the form of hash
    # @return [Hash] Returns the object in the form of hash
    def to_hash
      hash = {}
      self.class.attribute_map.each_pair do |attr, param|
        value = self.send(attr)
        next if value.nil?
        hash[param] = _to_hash(value)
      end
      hash
    end

    # Outputs non-array value in the form of hash
    # For object, use to_hash. Otherwise, just return the value
    # @param [Object] value Any valid value
    # @return [Hash] Returns the value in the form of hash
    def _to_hash(value)
      if value.is_a?(Array)
        value.compact.map{ |v| _to_hash(v) }
      elsif value.is_a?(Hash)
        {}.tap do |hash|
          value.each { |k, v| hash[k] = _to_hash(v) }
        end
      elsif value.respond_to? :to_hash
        value.to_hash
      else
        value
      end
    end

  end

end
