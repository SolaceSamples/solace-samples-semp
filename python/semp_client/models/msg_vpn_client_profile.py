# coding: utf-8

"""
    SEMP (Solace Element Management Protocol)

     SEMP (starting in `v2`, see [note 1](#notes)) is a RESTful API for configuring a Solace router.  SEMP uses URIs to address manageable **resources** of the Solace router. Resources are either individual **objects**, or **collections** of objects. The following APIs are provided:   API|Base Path|Purpose|Comments :---|:---|:---|:--- Configuration|/SEMP/v2/config|Reading and writing config state|See [note 2](#notes)    Resources are always nouns, with individual objects being singular and collections being plural. Objects within a collection are identified by an `obj-id`, which follows the collection name with the form `collection-name/obj-id`. Some examples:  <pre> /SEMP/v2/config/msgVpns                       ; MsgVpn collection /SEMP/v2/config/msgVpns/finance               ; MsgVpn object named \"finance\" /SEMP/v2/config/msgVpns/finance/queues        ; Queue collection within MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ ; Queue object named \"orderQ\" within MsgVpn \"finance\" </pre>  ## Collection Resources  Collections are unordered lists of objects (unless described as otherwise), and are described by JSON arrays. Each item in the array represents an object in the same manner as the individual object would normally be represented. The creation of a new object is done through its collection resource.  ## Object Resources  Objects are composed of attributes and collections, and are described by JSON content as name/value pairs. The collections of an object are not contained directly in the object's JSON content, rather the content includes a URI attribute which points to the collection. This contained collection resource must be managed as a separate resource through this URI.  At a minimum, every object has 1 or more identifying attributes, and its own `uri` attribute which contains the URI to itself. Attributes may have any (non-exclusively) of the following properties:   Property|Meaning|Comments :---|:---|:--- Identifying|Attribute is involved in unique identification of the object, and appears in its URI| Required|Attribute must be provided in the request| Read-Only|Attribute can only be read, not written|See [note 3](#notes) Write-Only|Attribute can only be written, not read| Requires-Disable|Attribute can only be changed when object is disabled| Deprecated|Attribute is deprecated, and will disappear in the next SEMP version|    In some requests, certain attributes may only be provided in certain combinations with other attributes:   Relationship|Meaning :---|:--- Requires|Attribute may only be changed by a request if a particular attribute or combination of attributes is also provided in the request Conflicts|Attribute may only be provided in a request if a particular attribute or combination of attributes is not also provided in the request    ## HTTP Methods  The HTTP methods of POST, PUT, PATCH, DELETE, and GET manipulate resources following these general principles:   Method|Resource|Meaning|Request Body|Response Body|Missing Request Attributes :---|:---|:---|:---|:---|:--- POST|Collection|Create object|Initial attribute values|Object attributes and metadata|Set to default PUT|Object|Replace object|New attribute values|Object attributes and metadata|Set to default (but see [note 4](#notes)) PATCH|Object|Update object|New attribute values|Object attributes and metadata | Left unchanged| DELETE|Object|Delete object|Empty|Object metadata|N/A GET|Object|Get object|Empty|Object attributes and metadata|N/A GET|Collection|Get collection|Empty|Object attributes and collection metadata|N/A    ## Common Query Parameters  The following are some common query parameters that are supported by many method/URI combinations. Individual URIs may document additional parameters. Note that multiple query parameters can be used together in a single URI, separated by the ampersand character. For example:  <pre> ; Request for the MsgVpns collection using two hypothetical query parameters ; \"q1\" and \"q2\" with values \"val1\" and \"val2\" respectively /SEMP/v2/config/msgVpns?q1=val1&q2=val2 </pre>  ### select  Include in the response only selected attributes of the object. Use this query parameter to limit the size of the returned data for each returned object, or return only those fields that are desired.  The value of `select` is a comma-separated list of attribute names. Names may include the `*` wildcard. Nested attribute names are supported using periods (e.g. `parentName.childName`). If the list is empty (i.e. `select=`) no attributes are returned; otherwise the list must match at least one attribute name of the object. Some examples:  <pre> ; List of all MsgVpn names /SEMP/v2/config/msgVpns?select=msgVpnName  ; Authentication attributes of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance?select=authentication*  ; Access related attributes of Queue \"orderQ\" of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ?select=owner,permission </pre>  ### where  Include in the response only objects where certain conditions are true. Use this query parameter to limit which objects are returned to those whose attribute values meet the given conditions.  The value of `where` is a comma-separated list of expressions. All expressions must be true for the object to be included in the response. Each expression takes the form:  <pre> expression  = attribute-name OP value OP          = '==' | '!=' | '<' | '>' | '<=' | '>=' </pre>  `value` may be a number, string, `true`, or `false`, as appropriate for the type of `attribute-name`. Greater-than and less-than comparisons only work for numbers. A `*` in a string `value` is interpreted as a wildcard. Some examples:  <pre> ; Only enabled MsgVpns /SEMP/v2/config/msgVpns?where=enabled==true  ; Only MsgVpns using basic non-LDAP authentication /SEMP/v2/config/msgVpns?where=authenticationBasicEnabled==true,authenticationBasicType!=ldap  ; Only MsgVpns that allow more than 100 client connections /SEMP/v2/config/msgVpns?where=maxConnectionCount>100 </pre>  ### count  Limit the count of objects in the response. This can be useful to limit the size of the response for large collections. The minimum value for `count` is `1` and the default is `10`. There is a hidden maximum as to prevent overloading the system. For example:  <pre> ; Up to 25 MsgVpns /SEMP/v2/config/msgVpns?count=25 </pre>  ### cursor  The cursor, or position, for the next page of objects. Cursors are opaque data that should not be created or interpreted by SEMP clients, and should only be used as described below.  When a request is made for a collection and there may be additional objects available for retrieval that are not included in the initial response, the response will include a `cursorQuery` field containing a cursor. The value of this field can be specified in the `cursor` query parameter of a subsequent request to retrieve the next page of objects. For convenience, an appropriate URI is constructed automatically by the router and included in the `nextPageUri` field of the response. This URI can be used directly to retrieve the next page of objects.  ## Notes  1. This specification defines SEMP starting in `v2`, and not the original SEMP `v1` interface. Request and response formats between `v1` and `v2` are entirely incompatible, although both protocols share a common port configuration on the Solace router. They are differentiated by the initial portion of the URI path, one of either `/SEMP/` or `/SEMP/v2/`. 2. The config API is partially implemented. Only a subset of all configurable objects are available. 3. Read-only attributes may appear in POST and PUT/PATCH requests. However, if a read-only attribute is not marked as identifying, it will be ignored during a PUT/PATCH. 4. For PUT, if the SEMP user is not authorized to modify the attribute, its value is left unchanged rather than set to default. In addition, the values of write-only attributes are not set to their defaults on a PUT. 5. For DELETE, the body of the request currently serves no purpose and will cause an error if not empty. 

    OpenAPI spec version: 2.1.0
    Contact: support@solace.com
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
"""

from pprint import pformat
from six import iteritems
import re


class MsgVpnClientProfile(object):
    """
    NOTE: This class is auto generated by the swagger code generator program.
    Do not edit the class manually.
    """
    def __init__(self, allow_bridge_connections_enabled=None, allow_cut_through_forwarding_enabled=None, allow_guaranteed_endpoint_create_enabled=None, allow_guaranteed_msg_receive_enabled=None, allow_guaranteed_msg_send_enabled=None, allow_transacted_sessions_enabled=None, api_queue_management_copy_from_on_create_name=None, api_topic_endpoint_management_copy_from_on_create_name=None, client_profile_name=None, eliding_delay=None, eliding_enabled=None, eliding_max_topic_count=None, event_client_provisioned_endpoint_spool_usage_threshold=None, event_connection_count_per_client_username_threshold=None, event_egress_flow_count_threshold=None, event_endpoint_count_per_client_username_threshold=None, event_ingress_flow_count_threshold=None, event_service_smf_connection_count_per_client_username_threshold=None, event_service_web_connection_count_per_client_username_threshold=None, event_subscription_count_threshold=None, event_transacted_session_count_threshold=None, event_transaction_count_threshold=None, max_connection_count_per_client_username=None, max_egress_flow_count=None, max_endpoint_count_per_client_username=None, max_ingress_flow_count=None, max_subscription_count=None, max_transacted_session_count=None, max_transaction_count=None, msg_vpn_name=None, queue_control1_max_depth=None, queue_control1_min_msg_burst=None, queue_direct1_max_depth=None, queue_direct1_min_msg_burst=None, queue_direct2_max_depth=None, queue_direct2_min_msg_burst=None, queue_direct3_max_depth=None, queue_direct3_min_msg_burst=None, queue_guaranteed1_max_depth=None, queue_guaranteed1_min_msg_burst=None, replication_allow_client_connect_when_standby_enabled=None, service_smf_max_connection_count_per_client_username=None, service_web_inactive_timeout=None, service_web_max_connection_count_per_client_username=None, service_web_max_payload=None, tcp_congestion_window_size=None, tcp_keepalive_count=None, tcp_keepalive_idle_time=None, tcp_keepalive_interval=None, tcp_max_segment_size=None, tcp_max_window_size=None):
        """
        MsgVpnClientProfile - a model defined in Swagger

        :param dict swaggerTypes: The key is attribute name
                                  and the value is attribute type.
        :param dict attributeMap: The key is attribute name
                                  and the value is json key in definition.
        """
        self.swagger_types = {
            'allow_bridge_connections_enabled': 'bool',
            'allow_cut_through_forwarding_enabled': 'bool',
            'allow_guaranteed_endpoint_create_enabled': 'bool',
            'allow_guaranteed_msg_receive_enabled': 'bool',
            'allow_guaranteed_msg_send_enabled': 'bool',
            'allow_transacted_sessions_enabled': 'bool',
            'api_queue_management_copy_from_on_create_name': 'str',
            'api_topic_endpoint_management_copy_from_on_create_name': 'str',
            'client_profile_name': 'str',
            'eliding_delay': 'int',
            'eliding_enabled': 'bool',
            'eliding_max_topic_count': 'int',
            'event_client_provisioned_endpoint_spool_usage_threshold': 'EventThresholdByPercent',
            'event_connection_count_per_client_username_threshold': 'EventThreshold',
            'event_egress_flow_count_threshold': 'EventThreshold',
            'event_endpoint_count_per_client_username_threshold': 'EventThreshold',
            'event_ingress_flow_count_threshold': 'EventThreshold',
            'event_service_smf_connection_count_per_client_username_threshold': 'EventThreshold',
            'event_service_web_connection_count_per_client_username_threshold': 'EventThreshold',
            'event_subscription_count_threshold': 'EventThreshold',
            'event_transacted_session_count_threshold': 'EventThreshold',
            'event_transaction_count_threshold': 'EventThreshold',
            'max_connection_count_per_client_username': 'int',
            'max_egress_flow_count': 'int',
            'max_endpoint_count_per_client_username': 'int',
            'max_ingress_flow_count': 'int',
            'max_subscription_count': 'int',
            'max_transacted_session_count': 'int',
            'max_transaction_count': 'int',
            'msg_vpn_name': 'str',
            'queue_control1_max_depth': 'int',
            'queue_control1_min_msg_burst': 'int',
            'queue_direct1_max_depth': 'int',
            'queue_direct1_min_msg_burst': 'int',
            'queue_direct2_max_depth': 'int',
            'queue_direct2_min_msg_burst': 'int',
            'queue_direct3_max_depth': 'int',
            'queue_direct3_min_msg_burst': 'int',
            'queue_guaranteed1_max_depth': 'int',
            'queue_guaranteed1_min_msg_burst': 'int',
            'replication_allow_client_connect_when_standby_enabled': 'bool',
            'service_smf_max_connection_count_per_client_username': 'int',
            'service_web_inactive_timeout': 'int',
            'service_web_max_connection_count_per_client_username': 'int',
            'service_web_max_payload': 'int',
            'tcp_congestion_window_size': 'int',
            'tcp_keepalive_count': 'int',
            'tcp_keepalive_idle_time': 'int',
            'tcp_keepalive_interval': 'int',
            'tcp_max_segment_size': 'int',
            'tcp_max_window_size': 'int'
        }

        self.attribute_map = {
            'allow_bridge_connections_enabled': 'allowBridgeConnectionsEnabled',
            'allow_cut_through_forwarding_enabled': 'allowCutThroughForwardingEnabled',
            'allow_guaranteed_endpoint_create_enabled': 'allowGuaranteedEndpointCreateEnabled',
            'allow_guaranteed_msg_receive_enabled': 'allowGuaranteedMsgReceiveEnabled',
            'allow_guaranteed_msg_send_enabled': 'allowGuaranteedMsgSendEnabled',
            'allow_transacted_sessions_enabled': 'allowTransactedSessionsEnabled',
            'api_queue_management_copy_from_on_create_name': 'apiQueueManagementCopyFromOnCreateName',
            'api_topic_endpoint_management_copy_from_on_create_name': 'apiTopicEndpointManagementCopyFromOnCreateName',
            'client_profile_name': 'clientProfileName',
            'eliding_delay': 'elidingDelay',
            'eliding_enabled': 'elidingEnabled',
            'eliding_max_topic_count': 'elidingMaxTopicCount',
            'event_client_provisioned_endpoint_spool_usage_threshold': 'eventClientProvisionedEndpointSpoolUsageThreshold',
            'event_connection_count_per_client_username_threshold': 'eventConnectionCountPerClientUsernameThreshold',
            'event_egress_flow_count_threshold': 'eventEgressFlowCountThreshold',
            'event_endpoint_count_per_client_username_threshold': 'eventEndpointCountPerClientUsernameThreshold',
            'event_ingress_flow_count_threshold': 'eventIngressFlowCountThreshold',
            'event_service_smf_connection_count_per_client_username_threshold': 'eventServiceSmfConnectionCountPerClientUsernameThreshold',
            'event_service_web_connection_count_per_client_username_threshold': 'eventServiceWebConnectionCountPerClientUsernameThreshold',
            'event_subscription_count_threshold': 'eventSubscriptionCountThreshold',
            'event_transacted_session_count_threshold': 'eventTransactedSessionCountThreshold',
            'event_transaction_count_threshold': 'eventTransactionCountThreshold',
            'max_connection_count_per_client_username': 'maxConnectionCountPerClientUsername',
            'max_egress_flow_count': 'maxEgressFlowCount',
            'max_endpoint_count_per_client_username': 'maxEndpointCountPerClientUsername',
            'max_ingress_flow_count': 'maxIngressFlowCount',
            'max_subscription_count': 'maxSubscriptionCount',
            'max_transacted_session_count': 'maxTransactedSessionCount',
            'max_transaction_count': 'maxTransactionCount',
            'msg_vpn_name': 'msgVpnName',
            'queue_control1_max_depth': 'queueControl1MaxDepth',
            'queue_control1_min_msg_burst': 'queueControl1MinMsgBurst',
            'queue_direct1_max_depth': 'queueDirect1MaxDepth',
            'queue_direct1_min_msg_burst': 'queueDirect1MinMsgBurst',
            'queue_direct2_max_depth': 'queueDirect2MaxDepth',
            'queue_direct2_min_msg_burst': 'queueDirect2MinMsgBurst',
            'queue_direct3_max_depth': 'queueDirect3MaxDepth',
            'queue_direct3_min_msg_burst': 'queueDirect3MinMsgBurst',
            'queue_guaranteed1_max_depth': 'queueGuaranteed1MaxDepth',
            'queue_guaranteed1_min_msg_burst': 'queueGuaranteed1MinMsgBurst',
            'replication_allow_client_connect_when_standby_enabled': 'replicationAllowClientConnectWhenStandbyEnabled',
            'service_smf_max_connection_count_per_client_username': 'serviceSmfMaxConnectionCountPerClientUsername',
            'service_web_inactive_timeout': 'serviceWebInactiveTimeout',
            'service_web_max_connection_count_per_client_username': 'serviceWebMaxConnectionCountPerClientUsername',
            'service_web_max_payload': 'serviceWebMaxPayload',
            'tcp_congestion_window_size': 'tcpCongestionWindowSize',
            'tcp_keepalive_count': 'tcpKeepaliveCount',
            'tcp_keepalive_idle_time': 'tcpKeepaliveIdleTime',
            'tcp_keepalive_interval': 'tcpKeepaliveInterval',
            'tcp_max_segment_size': 'tcpMaxSegmentSize',
            'tcp_max_window_size': 'tcpMaxWindowSize'
        }

        self._allow_bridge_connections_enabled = allow_bridge_connections_enabled
        self._allow_cut_through_forwarding_enabled = allow_cut_through_forwarding_enabled
        self._allow_guaranteed_endpoint_create_enabled = allow_guaranteed_endpoint_create_enabled
        self._allow_guaranteed_msg_receive_enabled = allow_guaranteed_msg_receive_enabled
        self._allow_guaranteed_msg_send_enabled = allow_guaranteed_msg_send_enabled
        self._allow_transacted_sessions_enabled = allow_transacted_sessions_enabled
        self._api_queue_management_copy_from_on_create_name = api_queue_management_copy_from_on_create_name
        self._api_topic_endpoint_management_copy_from_on_create_name = api_topic_endpoint_management_copy_from_on_create_name
        self._client_profile_name = client_profile_name
        self._eliding_delay = eliding_delay
        self._eliding_enabled = eliding_enabled
        self._eliding_max_topic_count = eliding_max_topic_count
        self._event_client_provisioned_endpoint_spool_usage_threshold = event_client_provisioned_endpoint_spool_usage_threshold
        self._event_connection_count_per_client_username_threshold = event_connection_count_per_client_username_threshold
        self._event_egress_flow_count_threshold = event_egress_flow_count_threshold
        self._event_endpoint_count_per_client_username_threshold = event_endpoint_count_per_client_username_threshold
        self._event_ingress_flow_count_threshold = event_ingress_flow_count_threshold
        self._event_service_smf_connection_count_per_client_username_threshold = event_service_smf_connection_count_per_client_username_threshold
        self._event_service_web_connection_count_per_client_username_threshold = event_service_web_connection_count_per_client_username_threshold
        self._event_subscription_count_threshold = event_subscription_count_threshold
        self._event_transacted_session_count_threshold = event_transacted_session_count_threshold
        self._event_transaction_count_threshold = event_transaction_count_threshold
        self._max_connection_count_per_client_username = max_connection_count_per_client_username
        self._max_egress_flow_count = max_egress_flow_count
        self._max_endpoint_count_per_client_username = max_endpoint_count_per_client_username
        self._max_ingress_flow_count = max_ingress_flow_count
        self._max_subscription_count = max_subscription_count
        self._max_transacted_session_count = max_transacted_session_count
        self._max_transaction_count = max_transaction_count
        self._msg_vpn_name = msg_vpn_name
        self._queue_control1_max_depth = queue_control1_max_depth
        self._queue_control1_min_msg_burst = queue_control1_min_msg_burst
        self._queue_direct1_max_depth = queue_direct1_max_depth
        self._queue_direct1_min_msg_burst = queue_direct1_min_msg_burst
        self._queue_direct2_max_depth = queue_direct2_max_depth
        self._queue_direct2_min_msg_burst = queue_direct2_min_msg_burst
        self._queue_direct3_max_depth = queue_direct3_max_depth
        self._queue_direct3_min_msg_burst = queue_direct3_min_msg_burst
        self._queue_guaranteed1_max_depth = queue_guaranteed1_max_depth
        self._queue_guaranteed1_min_msg_burst = queue_guaranteed1_min_msg_burst
        self._replication_allow_client_connect_when_standby_enabled = replication_allow_client_connect_when_standby_enabled
        self._service_smf_max_connection_count_per_client_username = service_smf_max_connection_count_per_client_username
        self._service_web_inactive_timeout = service_web_inactive_timeout
        self._service_web_max_connection_count_per_client_username = service_web_max_connection_count_per_client_username
        self._service_web_max_payload = service_web_max_payload
        self._tcp_congestion_window_size = tcp_congestion_window_size
        self._tcp_keepalive_count = tcp_keepalive_count
        self._tcp_keepalive_idle_time = tcp_keepalive_idle_time
        self._tcp_keepalive_interval = tcp_keepalive_interval
        self._tcp_max_segment_size = tcp_max_segment_size
        self._tcp_max_window_size = tcp_max_window_size

    @property
    def allow_bridge_connections_enabled(self):
        """
        Gets the allow_bridge_connections_enabled of this MsgVpnClientProfile.
        Enable or disable allowing bridge connections to login. The default value is `false`.

        :return: The allow_bridge_connections_enabled of this MsgVpnClientProfile.
        :rtype: bool
        """
        return self._allow_bridge_connections_enabled

    @allow_bridge_connections_enabled.setter
    def allow_bridge_connections_enabled(self, allow_bridge_connections_enabled):
        """
        Sets the allow_bridge_connections_enabled of this MsgVpnClientProfile.
        Enable or disable allowing bridge connections to login. The default value is `false`.

        :param allow_bridge_connections_enabled: The allow_bridge_connections_enabled of this MsgVpnClientProfile.
        :type: bool
        """

        self._allow_bridge_connections_enabled = allow_bridge_connections_enabled

    @property
    def allow_cut_through_forwarding_enabled(self):
        """
        Gets the allow_cut_through_forwarding_enabled of this MsgVpnClientProfile.
        Enable or disable allowing a client to bind to topic endpoints or queues with cut-through forwarding. Changing this value does not affect existing sessions. The default value is `false`.

        :return: The allow_cut_through_forwarding_enabled of this MsgVpnClientProfile.
        :rtype: bool
        """
        return self._allow_cut_through_forwarding_enabled

    @allow_cut_through_forwarding_enabled.setter
    def allow_cut_through_forwarding_enabled(self, allow_cut_through_forwarding_enabled):
        """
        Sets the allow_cut_through_forwarding_enabled of this MsgVpnClientProfile.
        Enable or disable allowing a client to bind to topic endpoints or queues with cut-through forwarding. Changing this value does not affect existing sessions. The default value is `false`.

        :param allow_cut_through_forwarding_enabled: The allow_cut_through_forwarding_enabled of this MsgVpnClientProfile.
        :type: bool
        """

        self._allow_cut_through_forwarding_enabled = allow_cut_through_forwarding_enabled

    @property
    def allow_guaranteed_endpoint_create_enabled(self):
        """
        Gets the allow_guaranteed_endpoint_create_enabled of this MsgVpnClientProfile.
        Enable or disable allowing a client to create topic endponts or queues for the receiving of persistent or non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.

        :return: The allow_guaranteed_endpoint_create_enabled of this MsgVpnClientProfile.
        :rtype: bool
        """
        return self._allow_guaranteed_endpoint_create_enabled

    @allow_guaranteed_endpoint_create_enabled.setter
    def allow_guaranteed_endpoint_create_enabled(self, allow_guaranteed_endpoint_create_enabled):
        """
        Sets the allow_guaranteed_endpoint_create_enabled of this MsgVpnClientProfile.
        Enable or disable allowing a client to create topic endponts or queues for the receiving of persistent or non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.

        :param allow_guaranteed_endpoint_create_enabled: The allow_guaranteed_endpoint_create_enabled of this MsgVpnClientProfile.
        :type: bool
        """

        self._allow_guaranteed_endpoint_create_enabled = allow_guaranteed_endpoint_create_enabled

    @property
    def allow_guaranteed_msg_receive_enabled(self):
        """
        Gets the allow_guaranteed_msg_receive_enabled of this MsgVpnClientProfile.
        Enable or disable allowing a client to bind to topic endpoints or queues for the receiving of persistent or non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.

        :return: The allow_guaranteed_msg_receive_enabled of this MsgVpnClientProfile.
        :rtype: bool
        """
        return self._allow_guaranteed_msg_receive_enabled

    @allow_guaranteed_msg_receive_enabled.setter
    def allow_guaranteed_msg_receive_enabled(self, allow_guaranteed_msg_receive_enabled):
        """
        Sets the allow_guaranteed_msg_receive_enabled of this MsgVpnClientProfile.
        Enable or disable allowing a client to bind to topic endpoints or queues for the receiving of persistent or non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.

        :param allow_guaranteed_msg_receive_enabled: The allow_guaranteed_msg_receive_enabled of this MsgVpnClientProfile.
        :type: bool
        """

        self._allow_guaranteed_msg_receive_enabled = allow_guaranteed_msg_receive_enabled

    @property
    def allow_guaranteed_msg_send_enabled(self):
        """
        Gets the allow_guaranteed_msg_send_enabled of this MsgVpnClientProfile.
        Enable or disable allowing a client to send persistent and non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.

        :return: The allow_guaranteed_msg_send_enabled of this MsgVpnClientProfile.
        :rtype: bool
        """
        return self._allow_guaranteed_msg_send_enabled

    @allow_guaranteed_msg_send_enabled.setter
    def allow_guaranteed_msg_send_enabled(self, allow_guaranteed_msg_send_enabled):
        """
        Sets the allow_guaranteed_msg_send_enabled of this MsgVpnClientProfile.
        Enable or disable allowing a client to send persistent and non-persistent messages. Changing this value does not affect existing sessions. The default value is `false`.

        :param allow_guaranteed_msg_send_enabled: The allow_guaranteed_msg_send_enabled of this MsgVpnClientProfile.
        :type: bool
        """

        self._allow_guaranteed_msg_send_enabled = allow_guaranteed_msg_send_enabled

    @property
    def allow_transacted_sessions_enabled(self):
        """
        Gets the allow_transacted_sessions_enabled of this MsgVpnClientProfile.
        Enable or disable allowing a client to use trasacted sessions to bundle persistent or non-persistent message send and receives. Changing this value does not affect existing sessions. The default value is `false`.

        :return: The allow_transacted_sessions_enabled of this MsgVpnClientProfile.
        :rtype: bool
        """
        return self._allow_transacted_sessions_enabled

    @allow_transacted_sessions_enabled.setter
    def allow_transacted_sessions_enabled(self, allow_transacted_sessions_enabled):
        """
        Sets the allow_transacted_sessions_enabled of this MsgVpnClientProfile.
        Enable or disable allowing a client to use trasacted sessions to bundle persistent or non-persistent message send and receives. Changing this value does not affect existing sessions. The default value is `false`.

        :param allow_transacted_sessions_enabled: The allow_transacted_sessions_enabled of this MsgVpnClientProfile.
        :type: bool
        """

        self._allow_transacted_sessions_enabled = allow_transacted_sessions_enabled

    @property
    def api_queue_management_copy_from_on_create_name(self):
        """
        Gets the api_queue_management_copy_from_on_create_name of this MsgVpnClientProfile.
        The name of a queue to copy settings from when a new queue is created by an API. The referenced queue must exist. The default is to have no `apiQueueManagementCopyFromOnCreateName`.

        :return: The api_queue_management_copy_from_on_create_name of this MsgVpnClientProfile.
        :rtype: str
        """
        return self._api_queue_management_copy_from_on_create_name

    @api_queue_management_copy_from_on_create_name.setter
    def api_queue_management_copy_from_on_create_name(self, api_queue_management_copy_from_on_create_name):
        """
        Sets the api_queue_management_copy_from_on_create_name of this MsgVpnClientProfile.
        The name of a queue to copy settings from when a new queue is created by an API. The referenced queue must exist. The default is to have no `apiQueueManagementCopyFromOnCreateName`.

        :param api_queue_management_copy_from_on_create_name: The api_queue_management_copy_from_on_create_name of this MsgVpnClientProfile.
        :type: str
        """

        self._api_queue_management_copy_from_on_create_name = api_queue_management_copy_from_on_create_name

    @property
    def api_topic_endpoint_management_copy_from_on_create_name(self):
        """
        Gets the api_topic_endpoint_management_copy_from_on_create_name of this MsgVpnClientProfile.
        The name of a topic-endpoint to copy settings from when a new topic-endpoint is created by an API. The referenced topic-endpoint must exist. The default is to have no `apiTopicEndpointManagementCopyFromOnCreateName`.

        :return: The api_topic_endpoint_management_copy_from_on_create_name of this MsgVpnClientProfile.
        :rtype: str
        """
        return self._api_topic_endpoint_management_copy_from_on_create_name

    @api_topic_endpoint_management_copy_from_on_create_name.setter
    def api_topic_endpoint_management_copy_from_on_create_name(self, api_topic_endpoint_management_copy_from_on_create_name):
        """
        Sets the api_topic_endpoint_management_copy_from_on_create_name of this MsgVpnClientProfile.
        The name of a topic-endpoint to copy settings from when a new topic-endpoint is created by an API. The referenced topic-endpoint must exist. The default is to have no `apiTopicEndpointManagementCopyFromOnCreateName`.

        :param api_topic_endpoint_management_copy_from_on_create_name: The api_topic_endpoint_management_copy_from_on_create_name of this MsgVpnClientProfile.
        :type: str
        """

        self._api_topic_endpoint_management_copy_from_on_create_name = api_topic_endpoint_management_copy_from_on_create_name

    @property
    def client_profile_name(self):
        """
        Gets the client_profile_name of this MsgVpnClientProfile.
        The name of the Client Profile.

        :return: The client_profile_name of this MsgVpnClientProfile.
        :rtype: str
        """
        return self._client_profile_name

    @client_profile_name.setter
    def client_profile_name(self, client_profile_name):
        """
        Sets the client_profile_name of this MsgVpnClientProfile.
        The name of the Client Profile.

        :param client_profile_name: The client_profile_name of this MsgVpnClientProfile.
        :type: str
        """

        self._client_profile_name = client_profile_name

    @property
    def eliding_delay(self):
        """
        Gets the eliding_delay of this MsgVpnClientProfile.
        The eliding delay interval (in milliseconds). 0 means no delay in delivering the message to the client. The default value is `0`.

        :return: The eliding_delay of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._eliding_delay

    @eliding_delay.setter
    def eliding_delay(self, eliding_delay):
        """
        Sets the eliding_delay of this MsgVpnClientProfile.
        The eliding delay interval (in milliseconds). 0 means no delay in delivering the message to the client. The default value is `0`.

        :param eliding_delay: The eliding_delay of this MsgVpnClientProfile.
        :type: int
        """

        self._eliding_delay = eliding_delay

    @property
    def eliding_enabled(self):
        """
        Gets the eliding_enabled of this MsgVpnClientProfile.
        Enables or disables eliding. The default value is `false`.

        :return: The eliding_enabled of this MsgVpnClientProfile.
        :rtype: bool
        """
        return self._eliding_enabled

    @eliding_enabled.setter
    def eliding_enabled(self, eliding_enabled):
        """
        Sets the eliding_enabled of this MsgVpnClientProfile.
        Enables or disables eliding. The default value is `false`.

        :param eliding_enabled: The eliding_enabled of this MsgVpnClientProfile.
        :type: bool
        """

        self._eliding_enabled = eliding_enabled

    @property
    def eliding_max_topic_count(self):
        """
        Gets the eliding_max_topic_count of this MsgVpnClientProfile.
        The maximum number of topics that can be tracked for eliding on a per client basis. The default value is `256`.

        :return: The eliding_max_topic_count of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._eliding_max_topic_count

    @eliding_max_topic_count.setter
    def eliding_max_topic_count(self, eliding_max_topic_count):
        """
        Sets the eliding_max_topic_count of this MsgVpnClientProfile.
        The maximum number of topics that can be tracked for eliding on a per client basis. The default value is `256`.

        :param eliding_max_topic_count: The eliding_max_topic_count of this MsgVpnClientProfile.
        :type: int
        """

        self._eliding_max_topic_count = eliding_max_topic_count

    @property
    def event_client_provisioned_endpoint_spool_usage_threshold(self):
        """
        Gets the event_client_provisioned_endpoint_spool_usage_threshold of this MsgVpnClientProfile.


        :return: The event_client_provisioned_endpoint_spool_usage_threshold of this MsgVpnClientProfile.
        :rtype: EventThresholdByPercent
        """
        return self._event_client_provisioned_endpoint_spool_usage_threshold

    @event_client_provisioned_endpoint_spool_usage_threshold.setter
    def event_client_provisioned_endpoint_spool_usage_threshold(self, event_client_provisioned_endpoint_spool_usage_threshold):
        """
        Sets the event_client_provisioned_endpoint_spool_usage_threshold of this MsgVpnClientProfile.


        :param event_client_provisioned_endpoint_spool_usage_threshold: The event_client_provisioned_endpoint_spool_usage_threshold of this MsgVpnClientProfile.
        :type: EventThresholdByPercent
        """

        self._event_client_provisioned_endpoint_spool_usage_threshold = event_client_provisioned_endpoint_spool_usage_threshold

    @property
    def event_connection_count_per_client_username_threshold(self):
        """
        Gets the event_connection_count_per_client_username_threshold of this MsgVpnClientProfile.


        :return: The event_connection_count_per_client_username_threshold of this MsgVpnClientProfile.
        :rtype: EventThreshold
        """
        return self._event_connection_count_per_client_username_threshold

    @event_connection_count_per_client_username_threshold.setter
    def event_connection_count_per_client_username_threshold(self, event_connection_count_per_client_username_threshold):
        """
        Sets the event_connection_count_per_client_username_threshold of this MsgVpnClientProfile.


        :param event_connection_count_per_client_username_threshold: The event_connection_count_per_client_username_threshold of this MsgVpnClientProfile.
        :type: EventThreshold
        """

        self._event_connection_count_per_client_username_threshold = event_connection_count_per_client_username_threshold

    @property
    def event_egress_flow_count_threshold(self):
        """
        Gets the event_egress_flow_count_threshold of this MsgVpnClientProfile.


        :return: The event_egress_flow_count_threshold of this MsgVpnClientProfile.
        :rtype: EventThreshold
        """
        return self._event_egress_flow_count_threshold

    @event_egress_flow_count_threshold.setter
    def event_egress_flow_count_threshold(self, event_egress_flow_count_threshold):
        """
        Sets the event_egress_flow_count_threshold of this MsgVpnClientProfile.


        :param event_egress_flow_count_threshold: The event_egress_flow_count_threshold of this MsgVpnClientProfile.
        :type: EventThreshold
        """

        self._event_egress_flow_count_threshold = event_egress_flow_count_threshold

    @property
    def event_endpoint_count_per_client_username_threshold(self):
        """
        Gets the event_endpoint_count_per_client_username_threshold of this MsgVpnClientProfile.


        :return: The event_endpoint_count_per_client_username_threshold of this MsgVpnClientProfile.
        :rtype: EventThreshold
        """
        return self._event_endpoint_count_per_client_username_threshold

    @event_endpoint_count_per_client_username_threshold.setter
    def event_endpoint_count_per_client_username_threshold(self, event_endpoint_count_per_client_username_threshold):
        """
        Sets the event_endpoint_count_per_client_username_threshold of this MsgVpnClientProfile.


        :param event_endpoint_count_per_client_username_threshold: The event_endpoint_count_per_client_username_threshold of this MsgVpnClientProfile.
        :type: EventThreshold
        """

        self._event_endpoint_count_per_client_username_threshold = event_endpoint_count_per_client_username_threshold

    @property
    def event_ingress_flow_count_threshold(self):
        """
        Gets the event_ingress_flow_count_threshold of this MsgVpnClientProfile.


        :return: The event_ingress_flow_count_threshold of this MsgVpnClientProfile.
        :rtype: EventThreshold
        """
        return self._event_ingress_flow_count_threshold

    @event_ingress_flow_count_threshold.setter
    def event_ingress_flow_count_threshold(self, event_ingress_flow_count_threshold):
        """
        Sets the event_ingress_flow_count_threshold of this MsgVpnClientProfile.


        :param event_ingress_flow_count_threshold: The event_ingress_flow_count_threshold of this MsgVpnClientProfile.
        :type: EventThreshold
        """

        self._event_ingress_flow_count_threshold = event_ingress_flow_count_threshold

    @property
    def event_service_smf_connection_count_per_client_username_threshold(self):
        """
        Gets the event_service_smf_connection_count_per_client_username_threshold of this MsgVpnClientProfile.


        :return: The event_service_smf_connection_count_per_client_username_threshold of this MsgVpnClientProfile.
        :rtype: EventThreshold
        """
        return self._event_service_smf_connection_count_per_client_username_threshold

    @event_service_smf_connection_count_per_client_username_threshold.setter
    def event_service_smf_connection_count_per_client_username_threshold(self, event_service_smf_connection_count_per_client_username_threshold):
        """
        Sets the event_service_smf_connection_count_per_client_username_threshold of this MsgVpnClientProfile.


        :param event_service_smf_connection_count_per_client_username_threshold: The event_service_smf_connection_count_per_client_username_threshold of this MsgVpnClientProfile.
        :type: EventThreshold
        """

        self._event_service_smf_connection_count_per_client_username_threshold = event_service_smf_connection_count_per_client_username_threshold

    @property
    def event_service_web_connection_count_per_client_username_threshold(self):
        """
        Gets the event_service_web_connection_count_per_client_username_threshold of this MsgVpnClientProfile.


        :return: The event_service_web_connection_count_per_client_username_threshold of this MsgVpnClientProfile.
        :rtype: EventThreshold
        """
        return self._event_service_web_connection_count_per_client_username_threshold

    @event_service_web_connection_count_per_client_username_threshold.setter
    def event_service_web_connection_count_per_client_username_threshold(self, event_service_web_connection_count_per_client_username_threshold):
        """
        Sets the event_service_web_connection_count_per_client_username_threshold of this MsgVpnClientProfile.


        :param event_service_web_connection_count_per_client_username_threshold: The event_service_web_connection_count_per_client_username_threshold of this MsgVpnClientProfile.
        :type: EventThreshold
        """

        self._event_service_web_connection_count_per_client_username_threshold = event_service_web_connection_count_per_client_username_threshold

    @property
    def event_subscription_count_threshold(self):
        """
        Gets the event_subscription_count_threshold of this MsgVpnClientProfile.


        :return: The event_subscription_count_threshold of this MsgVpnClientProfile.
        :rtype: EventThreshold
        """
        return self._event_subscription_count_threshold

    @event_subscription_count_threshold.setter
    def event_subscription_count_threshold(self, event_subscription_count_threshold):
        """
        Sets the event_subscription_count_threshold of this MsgVpnClientProfile.


        :param event_subscription_count_threshold: The event_subscription_count_threshold of this MsgVpnClientProfile.
        :type: EventThreshold
        """

        self._event_subscription_count_threshold = event_subscription_count_threshold

    @property
    def event_transacted_session_count_threshold(self):
        """
        Gets the event_transacted_session_count_threshold of this MsgVpnClientProfile.


        :return: The event_transacted_session_count_threshold of this MsgVpnClientProfile.
        :rtype: EventThreshold
        """
        return self._event_transacted_session_count_threshold

    @event_transacted_session_count_threshold.setter
    def event_transacted_session_count_threshold(self, event_transacted_session_count_threshold):
        """
        Sets the event_transacted_session_count_threshold of this MsgVpnClientProfile.


        :param event_transacted_session_count_threshold: The event_transacted_session_count_threshold of this MsgVpnClientProfile.
        :type: EventThreshold
        """

        self._event_transacted_session_count_threshold = event_transacted_session_count_threshold

    @property
    def event_transaction_count_threshold(self):
        """
        Gets the event_transaction_count_threshold of this MsgVpnClientProfile.


        :return: The event_transaction_count_threshold of this MsgVpnClientProfile.
        :rtype: EventThreshold
        """
        return self._event_transaction_count_threshold

    @event_transaction_count_threshold.setter
    def event_transaction_count_threshold(self, event_transaction_count_threshold):
        """
        Sets the event_transaction_count_threshold of this MsgVpnClientProfile.


        :param event_transaction_count_threshold: The event_transaction_count_threshold of this MsgVpnClientProfile.
        :type: EventThreshold
        """

        self._event_transaction_count_threshold = event_transaction_count_threshold

    @property
    def max_connection_count_per_client_username(self):
        """
        Gets the max_connection_count_per_client_username of this MsgVpnClientProfile.
        The maximum number of client connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :return: The max_connection_count_per_client_username of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._max_connection_count_per_client_username

    @max_connection_count_per_client_username.setter
    def max_connection_count_per_client_username(self, max_connection_count_per_client_username):
        """
        Sets the max_connection_count_per_client_username of this MsgVpnClientProfile.
        The maximum number of client connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :param max_connection_count_per_client_username: The max_connection_count_per_client_username of this MsgVpnClientProfile.
        :type: int
        """

        self._max_connection_count_per_client_username = max_connection_count_per_client_username

    @property
    def max_egress_flow_count(self):
        """
        Gets the max_egress_flow_count of this MsgVpnClientProfile.
        The maximum number of egress flows that can be created by a single client associated with this client-profile. The default is the max value supported by the hardware.

        :return: The max_egress_flow_count of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._max_egress_flow_count

    @max_egress_flow_count.setter
    def max_egress_flow_count(self, max_egress_flow_count):
        """
        Sets the max_egress_flow_count of this MsgVpnClientProfile.
        The maximum number of egress flows that can be created by a single client associated with this client-profile. The default is the max value supported by the hardware.

        :param max_egress_flow_count: The max_egress_flow_count of this MsgVpnClientProfile.
        :type: int
        """

        self._max_egress_flow_count = max_egress_flow_count

    @property
    def max_endpoint_count_per_client_username(self):
        """
        Gets the max_endpoint_count_per_client_username of this MsgVpnClientProfile.
        The maximum number of queues and topic endpoints that can be created across clients using the same client-username associated with this client-profile. The default is the max value supported by the hardware.

        :return: The max_endpoint_count_per_client_username of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._max_endpoint_count_per_client_username

    @max_endpoint_count_per_client_username.setter
    def max_endpoint_count_per_client_username(self, max_endpoint_count_per_client_username):
        """
        Sets the max_endpoint_count_per_client_username of this MsgVpnClientProfile.
        The maximum number of queues and topic endpoints that can be created across clients using the same client-username associated with this client-profile. The default is the max value supported by the hardware.

        :param max_endpoint_count_per_client_username: The max_endpoint_count_per_client_username of this MsgVpnClientProfile.
        :type: int
        """

        self._max_endpoint_count_per_client_username = max_endpoint_count_per_client_username

    @property
    def max_ingress_flow_count(self):
        """
        Gets the max_ingress_flow_count of this MsgVpnClientProfile.
        The maximum number of ingress flows that can be created by a single client associated with this client-profile. The default is the max value supported by the hardware.

        :return: The max_ingress_flow_count of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._max_ingress_flow_count

    @max_ingress_flow_count.setter
    def max_ingress_flow_count(self, max_ingress_flow_count):
        """
        Sets the max_ingress_flow_count of this MsgVpnClientProfile.
        The maximum number of ingress flows that can be created by a single client associated with this client-profile. The default is the max value supported by the hardware.

        :param max_ingress_flow_count: The max_ingress_flow_count of this MsgVpnClientProfile.
        :type: int
        """

        self._max_ingress_flow_count = max_ingress_flow_count

    @property
    def max_subscription_count(self):
        """
        Gets the max_subscription_count of this MsgVpnClientProfile.
        The maximum number of subscriptions for a single client associated with this client-profile. The default varies by platform.

        :return: The max_subscription_count of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._max_subscription_count

    @max_subscription_count.setter
    def max_subscription_count(self, max_subscription_count):
        """
        Sets the max_subscription_count of this MsgVpnClientProfile.
        The maximum number of subscriptions for a single client associated with this client-profile. The default varies by platform.

        :param max_subscription_count: The max_subscription_count of this MsgVpnClientProfile.
        :type: int
        """

        self._max_subscription_count = max_subscription_count

    @property
    def max_transacted_session_count(self):
        """
        Gets the max_transacted_session_count of this MsgVpnClientProfile.
        The maximum number of transacted sessions that can be created by a single client associated with this client-profile. The default value is `10`.

        :return: The max_transacted_session_count of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._max_transacted_session_count

    @max_transacted_session_count.setter
    def max_transacted_session_count(self, max_transacted_session_count):
        """
        Sets the max_transacted_session_count of this MsgVpnClientProfile.
        The maximum number of transacted sessions that can be created by a single client associated with this client-profile. The default value is `10`.

        :param max_transacted_session_count: The max_transacted_session_count of this MsgVpnClientProfile.
        :type: int
        """

        self._max_transacted_session_count = max_transacted_session_count

    @property
    def max_transaction_count(self):
        """
        Gets the max_transaction_count of this MsgVpnClientProfile.
        The maximum number of transacted sessions that can be created by a single client associated with this client-profile. The default varies by platform.

        :return: The max_transaction_count of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._max_transaction_count

    @max_transaction_count.setter
    def max_transaction_count(self, max_transaction_count):
        """
        Sets the max_transaction_count of this MsgVpnClientProfile.
        The maximum number of transacted sessions that can be created by a single client associated with this client-profile. The default varies by platform.

        :param max_transaction_count: The max_transaction_count of this MsgVpnClientProfile.
        :type: int
        """

        self._max_transaction_count = max_transaction_count

    @property
    def msg_vpn_name(self):
        """
        Gets the msg_vpn_name of this MsgVpnClientProfile.
        The name of the Message VPN.

        :return: The msg_vpn_name of this MsgVpnClientProfile.
        :rtype: str
        """
        return self._msg_vpn_name

    @msg_vpn_name.setter
    def msg_vpn_name(self, msg_vpn_name):
        """
        Sets the msg_vpn_name of this MsgVpnClientProfile.
        The name of the Message VPN.

        :param msg_vpn_name: The msg_vpn_name of this MsgVpnClientProfile.
        :type: str
        """

        self._msg_vpn_name = msg_vpn_name

    @property
    def queue_control1_max_depth(self):
        """
        Gets the queue_control1_max_depth of this MsgVpnClientProfile.
        The maximum depth of the C-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.

        :return: The queue_control1_max_depth of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._queue_control1_max_depth

    @queue_control1_max_depth.setter
    def queue_control1_max_depth(self, queue_control1_max_depth):
        """
        Sets the queue_control1_max_depth of this MsgVpnClientProfile.
        The maximum depth of the C-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.

        :param queue_control1_max_depth: The queue_control1_max_depth of this MsgVpnClientProfile.
        :type: int
        """

        self._queue_control1_max_depth = queue_control1_max_depth

    @property
    def queue_control1_min_msg_burst(self):
        """
        Gets the queue_control1_min_msg_burst of this MsgVpnClientProfile.
        The minimum number of messages that must be on the C-1 queue before its depth is checked against the `queueControl1MaxDepth` setting. The default value is `4`.

        :return: The queue_control1_min_msg_burst of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._queue_control1_min_msg_burst

    @queue_control1_min_msg_burst.setter
    def queue_control1_min_msg_burst(self, queue_control1_min_msg_burst):
        """
        Sets the queue_control1_min_msg_burst of this MsgVpnClientProfile.
        The minimum number of messages that must be on the C-1 queue before its depth is checked against the `queueControl1MaxDepth` setting. The default value is `4`.

        :param queue_control1_min_msg_burst: The queue_control1_min_msg_burst of this MsgVpnClientProfile.
        :type: int
        """

        self._queue_control1_min_msg_burst = queue_control1_min_msg_burst

    @property
    def queue_direct1_max_depth(self):
        """
        Gets the queue_direct1_max_depth of this MsgVpnClientProfile.
        The maximum depth of the D-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.

        :return: The queue_direct1_max_depth of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._queue_direct1_max_depth

    @queue_direct1_max_depth.setter
    def queue_direct1_max_depth(self, queue_direct1_max_depth):
        """
        Sets the queue_direct1_max_depth of this MsgVpnClientProfile.
        The maximum depth of the D-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.

        :param queue_direct1_max_depth: The queue_direct1_max_depth of this MsgVpnClientProfile.
        :type: int
        """

        self._queue_direct1_max_depth = queue_direct1_max_depth

    @property
    def queue_direct1_min_msg_burst(self):
        """
        Gets the queue_direct1_min_msg_burst of this MsgVpnClientProfile.
        The minimum number of messages that must be on the D-1 queue before its depth is checked against the `queueDirect1MaxDepth` setting. The default value is `4`.

        :return: The queue_direct1_min_msg_burst of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._queue_direct1_min_msg_burst

    @queue_direct1_min_msg_burst.setter
    def queue_direct1_min_msg_burst(self, queue_direct1_min_msg_burst):
        """
        Sets the queue_direct1_min_msg_burst of this MsgVpnClientProfile.
        The minimum number of messages that must be on the D-1 queue before its depth is checked against the `queueDirect1MaxDepth` setting. The default value is `4`.

        :param queue_direct1_min_msg_burst: The queue_direct1_min_msg_burst of this MsgVpnClientProfile.
        :type: int
        """

        self._queue_direct1_min_msg_burst = queue_direct1_min_msg_burst

    @property
    def queue_direct2_max_depth(self):
        """
        Gets the queue_direct2_max_depth of this MsgVpnClientProfile.
        The maximum depth of the D-2 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.

        :return: The queue_direct2_max_depth of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._queue_direct2_max_depth

    @queue_direct2_max_depth.setter
    def queue_direct2_max_depth(self, queue_direct2_max_depth):
        """
        Sets the queue_direct2_max_depth of this MsgVpnClientProfile.
        The maximum depth of the D-2 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.

        :param queue_direct2_max_depth: The queue_direct2_max_depth of this MsgVpnClientProfile.
        :type: int
        """

        self._queue_direct2_max_depth = queue_direct2_max_depth

    @property
    def queue_direct2_min_msg_burst(self):
        """
        Gets the queue_direct2_min_msg_burst of this MsgVpnClientProfile.
        The minimum number of messages that must be on the D-2 queue before its depth is checked against the `queueDirect2MaxDepth` setting. The default value is `4`.

        :return: The queue_direct2_min_msg_burst of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._queue_direct2_min_msg_burst

    @queue_direct2_min_msg_burst.setter
    def queue_direct2_min_msg_burst(self, queue_direct2_min_msg_burst):
        """
        Sets the queue_direct2_min_msg_burst of this MsgVpnClientProfile.
        The minimum number of messages that must be on the D-2 queue before its depth is checked against the `queueDirect2MaxDepth` setting. The default value is `4`.

        :param queue_direct2_min_msg_burst: The queue_direct2_min_msg_burst of this MsgVpnClientProfile.
        :type: int
        """

        self._queue_direct2_min_msg_burst = queue_direct2_min_msg_burst

    @property
    def queue_direct3_max_depth(self):
        """
        Gets the queue_direct3_max_depth of this MsgVpnClientProfile.
        The maximum depth of the D-3 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.

        :return: The queue_direct3_max_depth of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._queue_direct3_max_depth

    @queue_direct3_max_depth.setter
    def queue_direct3_max_depth(self, queue_direct3_max_depth):
        """
        Sets the queue_direct3_max_depth of this MsgVpnClientProfile.
        The maximum depth of the D-3 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.

        :param queue_direct3_max_depth: The queue_direct3_max_depth of this MsgVpnClientProfile.
        :type: int
        """

        self._queue_direct3_max_depth = queue_direct3_max_depth

    @property
    def queue_direct3_min_msg_burst(self):
        """
        Gets the queue_direct3_min_msg_burst of this MsgVpnClientProfile.
        The minimum number of messages that must be on the D-3 queue before its depth is checked against the `queueDirect3MaxDepth` setting. The default value is `4`.

        :return: The queue_direct3_min_msg_burst of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._queue_direct3_min_msg_burst

    @queue_direct3_min_msg_burst.setter
    def queue_direct3_min_msg_burst(self, queue_direct3_min_msg_burst):
        """
        Sets the queue_direct3_min_msg_burst of this MsgVpnClientProfile.
        The minimum number of messages that must be on the D-3 queue before its depth is checked against the `queueDirect3MaxDepth` setting. The default value is `4`.

        :param queue_direct3_min_msg_burst: The queue_direct3_min_msg_burst of this MsgVpnClientProfile.
        :type: int
        """

        self._queue_direct3_min_msg_burst = queue_direct3_min_msg_burst

    @property
    def queue_guaranteed1_max_depth(self):
        """
        Gets the queue_guaranteed1_max_depth of this MsgVpnClientProfile.
        The maximum depth of the G-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.

        :return: The queue_guaranteed1_max_depth of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._queue_guaranteed1_max_depth

    @queue_guaranteed1_max_depth.setter
    def queue_guaranteed1_max_depth(self, queue_guaranteed1_max_depth):
        """
        Sets the queue_guaranteed1_max_depth of this MsgVpnClientProfile.
        The maximum depth of the G-1 queue measured in work units. Each work unit is 2048 bytes of data. The default value is `20000`.

        :param queue_guaranteed1_max_depth: The queue_guaranteed1_max_depth of this MsgVpnClientProfile.
        :type: int
        """

        self._queue_guaranteed1_max_depth = queue_guaranteed1_max_depth

    @property
    def queue_guaranteed1_min_msg_burst(self):
        """
        Gets the queue_guaranteed1_min_msg_burst of this MsgVpnClientProfile.
        The minimum number of messages that must be on the G-1 queue before its depth is checked against the `queueGuaranteed1MaxDepth` setting. The default value is `255`.

        :return: The queue_guaranteed1_min_msg_burst of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._queue_guaranteed1_min_msg_burst

    @queue_guaranteed1_min_msg_burst.setter
    def queue_guaranteed1_min_msg_burst(self, queue_guaranteed1_min_msg_burst):
        """
        Sets the queue_guaranteed1_min_msg_burst of this MsgVpnClientProfile.
        The minimum number of messages that must be on the G-1 queue before its depth is checked against the `queueGuaranteed1MaxDepth` setting. The default value is `255`.

        :param queue_guaranteed1_min_msg_burst: The queue_guaranteed1_min_msg_burst of this MsgVpnClientProfile.
        :type: int
        """

        self._queue_guaranteed1_min_msg_burst = queue_guaranteed1_min_msg_burst

    @property
    def replication_allow_client_connect_when_standby_enabled(self):
        """
        Gets the replication_allow_client_connect_when_standby_enabled of this MsgVpnClientProfile.
        Enable or disable whether clients using this client profile are allowed to connect to the Message VPN if its replication is in standby state. The default value is `false`.

        :return: The replication_allow_client_connect_when_standby_enabled of this MsgVpnClientProfile.
        :rtype: bool
        """
        return self._replication_allow_client_connect_when_standby_enabled

    @replication_allow_client_connect_when_standby_enabled.setter
    def replication_allow_client_connect_when_standby_enabled(self, replication_allow_client_connect_when_standby_enabled):
        """
        Sets the replication_allow_client_connect_when_standby_enabled of this MsgVpnClientProfile.
        Enable or disable whether clients using this client profile are allowed to connect to the Message VPN if its replication is in standby state. The default value is `false`.

        :param replication_allow_client_connect_when_standby_enabled: The replication_allow_client_connect_when_standby_enabled of this MsgVpnClientProfile.
        :type: bool
        """

        self._replication_allow_client_connect_when_standby_enabled = replication_allow_client_connect_when_standby_enabled

    @property
    def service_smf_max_connection_count_per_client_username(self):
        """
        Gets the service_smf_max_connection_count_per_client_username of this MsgVpnClientProfile.
        The maximum number of SMF client connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :return: The service_smf_max_connection_count_per_client_username of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._service_smf_max_connection_count_per_client_username

    @service_smf_max_connection_count_per_client_username.setter
    def service_smf_max_connection_count_per_client_username(self, service_smf_max_connection_count_per_client_username):
        """
        Sets the service_smf_max_connection_count_per_client_username of this MsgVpnClientProfile.
        The maximum number of SMF client connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :param service_smf_max_connection_count_per_client_username: The service_smf_max_connection_count_per_client_username of this MsgVpnClientProfile.
        :type: int
        """

        self._service_smf_max_connection_count_per_client_username = service_smf_max_connection_count_per_client_username

    @property
    def service_web_inactive_timeout(self):
        """
        Gets the service_web_inactive_timeout of this MsgVpnClientProfile.
        The number of seconds during which the client must send a request or else the session is terminated. The default value is `30`.

        :return: The service_web_inactive_timeout of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._service_web_inactive_timeout

    @service_web_inactive_timeout.setter
    def service_web_inactive_timeout(self, service_web_inactive_timeout):
        """
        Sets the service_web_inactive_timeout of this MsgVpnClientProfile.
        The number of seconds during which the client must send a request or else the session is terminated. The default value is `30`.

        :param service_web_inactive_timeout: The service_web_inactive_timeout of this MsgVpnClientProfile.
        :type: int
        """

        self._service_web_inactive_timeout = service_web_inactive_timeout

    @property
    def service_web_max_connection_count_per_client_username(self):
        """
        Gets the service_web_max_connection_count_per_client_username of this MsgVpnClientProfile.
        The maximum number of web-transport connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :return: The service_web_max_connection_count_per_client_username of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._service_web_max_connection_count_per_client_username

    @service_web_max_connection_count_per_client_username.setter
    def service_web_max_connection_count_per_client_username(self, service_web_max_connection_count_per_client_username):
        """
        Sets the service_web_max_connection_count_per_client_username of this MsgVpnClientProfile.
        The maximum number of web-transport connections that can be simultaneously connected with the same client-username. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :param service_web_max_connection_count_per_client_username: The service_web_max_connection_count_per_client_username of this MsgVpnClientProfile.
        :type: int
        """

        self._service_web_max_connection_count_per_client_username = service_web_max_connection_count_per_client_username

    @property
    def service_web_max_payload(self):
        """
        Gets the service_web_max_payload of this MsgVpnClientProfile.
        The maximum number of bytes allowed in a single web transport payload before fragmentation occurs, not including the header. The default value is `1000000`.

        :return: The service_web_max_payload of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._service_web_max_payload

    @service_web_max_payload.setter
    def service_web_max_payload(self, service_web_max_payload):
        """
        Sets the service_web_max_payload of this MsgVpnClientProfile.
        The maximum number of bytes allowed in a single web transport payload before fragmentation occurs, not including the header. The default value is `1000000`.

        :param service_web_max_payload: The service_web_max_payload of this MsgVpnClientProfile.
        :type: int
        """

        self._service_web_max_payload = service_web_max_payload

    @property
    def tcp_congestion_window_size(self):
        """
        Gets the tcp_congestion_window_size of this MsgVpnClientProfile.
        The TCP initial congestion window size for clients belonging to this profile.   The initial congestion window size is used when starting up a TCP connection or recovery from idle (that is, no traffic). It is the number of segments TCP sends before waiting for an acknowledgement from the peer. Larger values of initial window allows a connection to come up to speed quickly. However, care must be taken for if this parameter's value is too high, it may cause congestion in the network. For further details on initial window, refer to RFC 2581. Changing this parameter changes all clients matching this profile, whether already connected or not.   Changing the initial window from its default of 2 results in non-compliance with RFC 2581. Contact Solace Support personnel before changing this parameter. The default value is `2`.

        :return: The tcp_congestion_window_size of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._tcp_congestion_window_size

    @tcp_congestion_window_size.setter
    def tcp_congestion_window_size(self, tcp_congestion_window_size):
        """
        Sets the tcp_congestion_window_size of this MsgVpnClientProfile.
        The TCP initial congestion window size for clients belonging to this profile.   The initial congestion window size is used when starting up a TCP connection or recovery from idle (that is, no traffic). It is the number of segments TCP sends before waiting for an acknowledgement from the peer. Larger values of initial window allows a connection to come up to speed quickly. However, care must be taken for if this parameter's value is too high, it may cause congestion in the network. For further details on initial window, refer to RFC 2581. Changing this parameter changes all clients matching this profile, whether already connected or not.   Changing the initial window from its default of 2 results in non-compliance with RFC 2581. Contact Solace Support personnel before changing this parameter. The default value is `2`.

        :param tcp_congestion_window_size: The tcp_congestion_window_size of this MsgVpnClientProfile.
        :type: int
        """

        self._tcp_congestion_window_size = tcp_congestion_window_size

    @property
    def tcp_keepalive_count(self):
        """
        Gets the tcp_keepalive_count of this MsgVpnClientProfile.
        The number of keepalive probes TCP should send before dropping the connection. The default value is `5`.

        :return: The tcp_keepalive_count of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._tcp_keepalive_count

    @tcp_keepalive_count.setter
    def tcp_keepalive_count(self, tcp_keepalive_count):
        """
        Sets the tcp_keepalive_count of this MsgVpnClientProfile.
        The number of keepalive probes TCP should send before dropping the connection. The default value is `5`.

        :param tcp_keepalive_count: The tcp_keepalive_count of this MsgVpnClientProfile.
        :type: int
        """

        self._tcp_keepalive_count = tcp_keepalive_count

    @property
    def tcp_keepalive_idle_time(self):
        """
        Gets the tcp_keepalive_idle_time of this MsgVpnClientProfile.
        The time (in seconds) a connection needs to remain idle before TCP begins sending keepalive probes. The default value is `3`.

        :return: The tcp_keepalive_idle_time of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._tcp_keepalive_idle_time

    @tcp_keepalive_idle_time.setter
    def tcp_keepalive_idle_time(self, tcp_keepalive_idle_time):
        """
        Sets the tcp_keepalive_idle_time of this MsgVpnClientProfile.
        The time (in seconds) a connection needs to remain idle before TCP begins sending keepalive probes. The default value is `3`.

        :param tcp_keepalive_idle_time: The tcp_keepalive_idle_time of this MsgVpnClientProfile.
        :type: int
        """

        self._tcp_keepalive_idle_time = tcp_keepalive_idle_time

    @property
    def tcp_keepalive_interval(self):
        """
        Gets the tcp_keepalive_interval of this MsgVpnClientProfile.
        The time between individual keepalive probes, when no response is received. The default value is `1`.

        :return: The tcp_keepalive_interval of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._tcp_keepalive_interval

    @tcp_keepalive_interval.setter
    def tcp_keepalive_interval(self, tcp_keepalive_interval):
        """
        Sets the tcp_keepalive_interval of this MsgVpnClientProfile.
        The time between individual keepalive probes, when no response is received. The default value is `1`.

        :param tcp_keepalive_interval: The tcp_keepalive_interval of this MsgVpnClientProfile.
        :type: int
        """

        self._tcp_keepalive_interval = tcp_keepalive_interval

    @property
    def tcp_max_segment_size(self):
        """
        Gets the tcp_max_segment_size of this MsgVpnClientProfile.
        The TCP maximum segment size for the clients belonging to this profile. The default value is `1460`.

        :return: The tcp_max_segment_size of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._tcp_max_segment_size

    @tcp_max_segment_size.setter
    def tcp_max_segment_size(self, tcp_max_segment_size):
        """
        Sets the tcp_max_segment_size of this MsgVpnClientProfile.
        The TCP maximum segment size for the clients belonging to this profile. The default value is `1460`.

        :param tcp_max_segment_size: The tcp_max_segment_size of this MsgVpnClientProfile.
        :type: int
        """

        self._tcp_max_segment_size = tcp_max_segment_size

    @property
    def tcp_max_window_size(self):
        """
        Gets the tcp_max_window_size of this MsgVpnClientProfile.
        The TCP maximum window size (in KB) for clients belonging to this profile. Changes are applied to all existing connections. The maximum window should be at least the bandwidth-delay product of the link between the TCP peers. If the maximum window is less than the bandwidth-delay product, then the TCP connection operates below its maximum potential throughput. If the maximum window is less than about twice the bandwidth-delay product, then occasional packet loss causes TCP connection to operate below its maximum potential throughput as it handles the missing ACKs and retransmissions. There are also problems with a maximum window that's too large. In the presence of a high offered load, TCP gradually increases its congestion window until either (a) the congestion window reaches the maximum window, or (b) packet loss occurs in the network. Initially, when the congestion window is small, the network's physical bandwidth-delay acts as a memory buffer for packets in flight. As the congestion window crosses the bandwidth-delay product, though, the buffering of in-flight packets moves to queues in various switches, routers, etc. in the network. As the congestion window continues to increase, some such queue in some equipment overflows, causing packet loss and TCP back-off. The default value is `256`.

        :return: The tcp_max_window_size of this MsgVpnClientProfile.
        :rtype: int
        """
        return self._tcp_max_window_size

    @tcp_max_window_size.setter
    def tcp_max_window_size(self, tcp_max_window_size):
        """
        Sets the tcp_max_window_size of this MsgVpnClientProfile.
        The TCP maximum window size (in KB) for clients belonging to this profile. Changes are applied to all existing connections. The maximum window should be at least the bandwidth-delay product of the link between the TCP peers. If the maximum window is less than the bandwidth-delay product, then the TCP connection operates below its maximum potential throughput. If the maximum window is less than about twice the bandwidth-delay product, then occasional packet loss causes TCP connection to operate below its maximum potential throughput as it handles the missing ACKs and retransmissions. There are also problems with a maximum window that's too large. In the presence of a high offered load, TCP gradually increases its congestion window until either (a) the congestion window reaches the maximum window, or (b) packet loss occurs in the network. Initially, when the congestion window is small, the network's physical bandwidth-delay acts as a memory buffer for packets in flight. As the congestion window crosses the bandwidth-delay product, though, the buffering of in-flight packets moves to queues in various switches, routers, etc. in the network. As the congestion window continues to increase, some such queue in some equipment overflows, causing packet loss and TCP back-off. The default value is `256`.

        :param tcp_max_window_size: The tcp_max_window_size of this MsgVpnClientProfile.
        :type: int
        """

        self._tcp_max_window_size = tcp_max_window_size

    def to_dict(self):
        """
        Returns the model properties as a dict
        """
        result = {}

        for attr, _ in iteritems(self.swagger_types):
            value = getattr(self, attr)
            if isinstance(value, list):
                result[attr] = list(map(
                    lambda x: x.to_dict() if hasattr(x, "to_dict") else x,
                    value
                ))
            elif hasattr(value, "to_dict"):
                result[attr] = value.to_dict()
            elif isinstance(value, dict):
                result[attr] = dict(map(
                    lambda item: (item[0], item[1].to_dict())
                    if hasattr(item[1], "to_dict") else item,
                    value.items()
                ))
            else:
                result[attr] = value

        return result

    def to_str(self):
        """
        Returns the string representation of the model
        """
        return pformat(self.to_dict())

    def __repr__(self):
        """
        For `print` and `pprint`
        """
        return self.to_str()

    def __eq__(self, other):
        """
        Returns true if both objects are equal
        """
        return self.__dict__ == other.__dict__

    def __ne__(self, other):
        """
        Returns true if both objects are not equal
        """
        return not self == other
