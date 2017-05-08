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


class MsgVpn(object):
    """
    NOTE: This class is auto generated by the swagger code generator program.
    Do not edit the class manually.
    """
    def __init__(self, authentication_basic_enabled=None, authentication_basic_profile_name=None, authentication_basic_radius_domain=None, authentication_basic_type=None, authentication_client_cert_allow_api_provided_username_enabled=None, authentication_client_cert_enabled=None, authentication_client_cert_max_chain_depth=None, authentication_client_cert_validate_date_enabled=None, authentication_kerberos_allow_api_provided_username_enabled=None, authentication_kerberos_enabled=None, authorization_ldap_group_membership_attribute_name=None, authorization_profile_name=None, authorization_type=None, bridging_tls_server_cert_enforce_trusted_common_name_enabled=None, bridging_tls_server_cert_max_chain_depth=None, bridging_tls_server_cert_validate_date_enabled=None, distributed_cache_management_enabled=None, enabled=None, event_connection_count_threshold=None, event_egress_flow_count_threshold=None, event_egress_msg_rate_threshold=None, event_endpoint_count_threshold=None, event_ingress_flow_count_threshold=None, event_ingress_msg_rate_threshold=None, event_large_msg_threshold=None, event_log_tag=None, event_msg_spool_usage_threshold=None, event_publish_client_enabled=None, event_publish_msg_vpn_enabled=None, event_publish_subscription_mode=None, event_publish_topic_format_mqtt_enabled=None, event_publish_topic_format_smf_enabled=None, event_service_mqtt_connection_count_threshold=None, event_service_rest_incoming_connection_count_threshold=None, event_service_smf_connection_count_threshold=None, event_service_web_connection_count_threshold=None, event_subscription_count_threshold=None, event_transacted_session_count_threshold=None, event_transaction_count_threshold=None, export_subscriptions_enabled=None, max_connection_count=None, max_egress_flow_count=None, max_endpoint_count=None, max_ingress_flow_count=None, max_msg_spool_usage=None, max_subscription_count=None, max_transacted_session_count=None, max_transaction_count=None, msg_vpn_name=None, replication_ack_propagation_interval_msg_count=None, replication_bridge_authentication_basic_client_username=None, replication_bridge_authentication_basic_password=None, replication_bridge_authentication_scheme=None, replication_bridge_compressed_data_enabled=None, replication_bridge_egress_flow_window_size=None, replication_bridge_retry_delay=None, replication_bridge_tls_enabled=None, replication_bridge_unidirectional_client_profile_name=None, replication_enabled=None, replication_enabled_queue_behavior=None, replication_queue_max_msg_spool_usage=None, replication_queue_reject_msg_to_sender_on_discard_enabled=None, replication_reject_msg_when_sync_ineligible_enabled=None, replication_role=None, replication_transaction_mode=None, rest_tls_server_cert_enforce_trusted_common_name_enabled=None, rest_tls_server_cert_max_chain_depth=None, rest_tls_server_cert_validate_date_enabled=None, semp_over_msg_bus_admin_client_enabled=None, semp_over_msg_bus_admin_distributed_cache_enabled=None, semp_over_msg_bus_admin_enabled=None, semp_over_msg_bus_enabled=None, semp_over_msg_bus_legacy_show_clear_enabled=None, semp_over_msg_bus_show_enabled=None, service_mqtt_max_connection_count=None, service_mqtt_plain_text_enabled=None, service_mqtt_plain_text_listen_port=None, service_mqtt_tls_enabled=None, service_mqtt_tls_listen_port=None, service_mqtt_tls_web_socket_enabled=None, service_mqtt_tls_web_socket_listen_port=None, service_mqtt_web_socket_enabled=None, service_mqtt_web_socket_listen_port=None, service_rest_incoming_max_connection_count=None, service_rest_incoming_plain_text_enabled=None, service_rest_incoming_plain_text_listen_port=None, service_rest_incoming_tls_enabled=None, service_rest_incoming_tls_listen_port=None, service_rest_outgoing_max_connection_count=None, service_smf_max_connection_count=None, service_smf_plain_text_enabled=None, service_smf_tls_enabled=None, service_web_max_connection_count=None, service_web_plain_text_enabled=None, service_web_tls_enabled=None, tls_allow_downgrade_to_plain_text_enabled=None):
        """
        MsgVpn - a model defined in Swagger

        :param dict swaggerTypes: The key is attribute name
                                  and the value is attribute type.
        :param dict attributeMap: The key is attribute name
                                  and the value is json key in definition.
        """
        self.swagger_types = {
            'authentication_basic_enabled': 'bool',
            'authentication_basic_profile_name': 'str',
            'authentication_basic_radius_domain': 'str',
            'authentication_basic_type': 'str',
            'authentication_client_cert_allow_api_provided_username_enabled': 'bool',
            'authentication_client_cert_enabled': 'bool',
            'authentication_client_cert_max_chain_depth': 'int',
            'authentication_client_cert_validate_date_enabled': 'bool',
            'authentication_kerberos_allow_api_provided_username_enabled': 'bool',
            'authentication_kerberos_enabled': 'bool',
            'authorization_ldap_group_membership_attribute_name': 'str',
            'authorization_profile_name': 'str',
            'authorization_type': 'str',
            'bridging_tls_server_cert_enforce_trusted_common_name_enabled': 'bool',
            'bridging_tls_server_cert_max_chain_depth': 'int',
            'bridging_tls_server_cert_validate_date_enabled': 'bool',
            'distributed_cache_management_enabled': 'bool',
            'enabled': 'bool',
            'event_connection_count_threshold': 'EventThreshold',
            'event_egress_flow_count_threshold': 'EventThreshold',
            'event_egress_msg_rate_threshold': 'EventThresholdByValue',
            'event_endpoint_count_threshold': 'EventThreshold',
            'event_ingress_flow_count_threshold': 'EventThreshold',
            'event_ingress_msg_rate_threshold': 'EventThresholdByValue',
            'event_large_msg_threshold': 'int',
            'event_log_tag': 'str',
            'event_msg_spool_usage_threshold': 'EventThreshold',
            'event_publish_client_enabled': 'bool',
            'event_publish_msg_vpn_enabled': 'bool',
            'event_publish_subscription_mode': 'str',
            'event_publish_topic_format_mqtt_enabled': 'bool',
            'event_publish_topic_format_smf_enabled': 'bool',
            'event_service_mqtt_connection_count_threshold': 'EventThreshold',
            'event_service_rest_incoming_connection_count_threshold': 'EventThreshold',
            'event_service_smf_connection_count_threshold': 'EventThreshold',
            'event_service_web_connection_count_threshold': 'EventThreshold',
            'event_subscription_count_threshold': 'EventThreshold',
            'event_transacted_session_count_threshold': 'EventThreshold',
            'event_transaction_count_threshold': 'EventThreshold',
            'export_subscriptions_enabled': 'bool',
            'max_connection_count': 'int',
            'max_egress_flow_count': 'int',
            'max_endpoint_count': 'int',
            'max_ingress_flow_count': 'int',
            'max_msg_spool_usage': 'int',
            'max_subscription_count': 'int',
            'max_transacted_session_count': 'int',
            'max_transaction_count': 'int',
            'msg_vpn_name': 'str',
            'replication_ack_propagation_interval_msg_count': 'int',
            'replication_bridge_authentication_basic_client_username': 'str',
            'replication_bridge_authentication_basic_password': 'str',
            'replication_bridge_authentication_scheme': 'str',
            'replication_bridge_compressed_data_enabled': 'bool',
            'replication_bridge_egress_flow_window_size': 'int',
            'replication_bridge_retry_delay': 'int',
            'replication_bridge_tls_enabled': 'bool',
            'replication_bridge_unidirectional_client_profile_name': 'str',
            'replication_enabled': 'bool',
            'replication_enabled_queue_behavior': 'str',
            'replication_queue_max_msg_spool_usage': 'int',
            'replication_queue_reject_msg_to_sender_on_discard_enabled': 'bool',
            'replication_reject_msg_when_sync_ineligible_enabled': 'bool',
            'replication_role': 'str',
            'replication_transaction_mode': 'str',
            'rest_tls_server_cert_enforce_trusted_common_name_enabled': 'bool',
            'rest_tls_server_cert_max_chain_depth': 'int',
            'rest_tls_server_cert_validate_date_enabled': 'bool',
            'semp_over_msg_bus_admin_client_enabled': 'bool',
            'semp_over_msg_bus_admin_distributed_cache_enabled': 'bool',
            'semp_over_msg_bus_admin_enabled': 'bool',
            'semp_over_msg_bus_enabled': 'bool',
            'semp_over_msg_bus_legacy_show_clear_enabled': 'bool',
            'semp_over_msg_bus_show_enabled': 'bool',
            'service_mqtt_max_connection_count': 'int',
            'service_mqtt_plain_text_enabled': 'bool',
            'service_mqtt_plain_text_listen_port': 'int',
            'service_mqtt_tls_enabled': 'bool',
            'service_mqtt_tls_listen_port': 'int',
            'service_mqtt_tls_web_socket_enabled': 'bool',
            'service_mqtt_tls_web_socket_listen_port': 'int',
            'service_mqtt_web_socket_enabled': 'bool',
            'service_mqtt_web_socket_listen_port': 'int',
            'service_rest_incoming_max_connection_count': 'int',
            'service_rest_incoming_plain_text_enabled': 'bool',
            'service_rest_incoming_plain_text_listen_port': 'int',
            'service_rest_incoming_tls_enabled': 'bool',
            'service_rest_incoming_tls_listen_port': 'int',
            'service_rest_outgoing_max_connection_count': 'int',
            'service_smf_max_connection_count': 'int',
            'service_smf_plain_text_enabled': 'bool',
            'service_smf_tls_enabled': 'bool',
            'service_web_max_connection_count': 'int',
            'service_web_plain_text_enabled': 'bool',
            'service_web_tls_enabled': 'bool',
            'tls_allow_downgrade_to_plain_text_enabled': 'bool'
        }

        self.attribute_map = {
            'authentication_basic_enabled': 'authenticationBasicEnabled',
            'authentication_basic_profile_name': 'authenticationBasicProfileName',
            'authentication_basic_radius_domain': 'authenticationBasicRadiusDomain',
            'authentication_basic_type': 'authenticationBasicType',
            'authentication_client_cert_allow_api_provided_username_enabled': 'authenticationClientCertAllowApiProvidedUsernameEnabled',
            'authentication_client_cert_enabled': 'authenticationClientCertEnabled',
            'authentication_client_cert_max_chain_depth': 'authenticationClientCertMaxChainDepth',
            'authentication_client_cert_validate_date_enabled': 'authenticationClientCertValidateDateEnabled',
            'authentication_kerberos_allow_api_provided_username_enabled': 'authenticationKerberosAllowApiProvidedUsernameEnabled',
            'authentication_kerberos_enabled': 'authenticationKerberosEnabled',
            'authorization_ldap_group_membership_attribute_name': 'authorizationLdapGroupMembershipAttributeName',
            'authorization_profile_name': 'authorizationProfileName',
            'authorization_type': 'authorizationType',
            'bridging_tls_server_cert_enforce_trusted_common_name_enabled': 'bridgingTlsServerCertEnforceTrustedCommonNameEnabled',
            'bridging_tls_server_cert_max_chain_depth': 'bridgingTlsServerCertMaxChainDepth',
            'bridging_tls_server_cert_validate_date_enabled': 'bridgingTlsServerCertValidateDateEnabled',
            'distributed_cache_management_enabled': 'distributedCacheManagementEnabled',
            'enabled': 'enabled',
            'event_connection_count_threshold': 'eventConnectionCountThreshold',
            'event_egress_flow_count_threshold': 'eventEgressFlowCountThreshold',
            'event_egress_msg_rate_threshold': 'eventEgressMsgRateThreshold',
            'event_endpoint_count_threshold': 'eventEndpointCountThreshold',
            'event_ingress_flow_count_threshold': 'eventIngressFlowCountThreshold',
            'event_ingress_msg_rate_threshold': 'eventIngressMsgRateThreshold',
            'event_large_msg_threshold': 'eventLargeMsgThreshold',
            'event_log_tag': 'eventLogTag',
            'event_msg_spool_usage_threshold': 'eventMsgSpoolUsageThreshold',
            'event_publish_client_enabled': 'eventPublishClientEnabled',
            'event_publish_msg_vpn_enabled': 'eventPublishMsgVpnEnabled',
            'event_publish_subscription_mode': 'eventPublishSubscriptionMode',
            'event_publish_topic_format_mqtt_enabled': 'eventPublishTopicFormatMqttEnabled',
            'event_publish_topic_format_smf_enabled': 'eventPublishTopicFormatSmfEnabled',
            'event_service_mqtt_connection_count_threshold': 'eventServiceMqttConnectionCountThreshold',
            'event_service_rest_incoming_connection_count_threshold': 'eventServiceRestIncomingConnectionCountThreshold',
            'event_service_smf_connection_count_threshold': 'eventServiceSmfConnectionCountThreshold',
            'event_service_web_connection_count_threshold': 'eventServiceWebConnectionCountThreshold',
            'event_subscription_count_threshold': 'eventSubscriptionCountThreshold',
            'event_transacted_session_count_threshold': 'eventTransactedSessionCountThreshold',
            'event_transaction_count_threshold': 'eventTransactionCountThreshold',
            'export_subscriptions_enabled': 'exportSubscriptionsEnabled',
            'max_connection_count': 'maxConnectionCount',
            'max_egress_flow_count': 'maxEgressFlowCount',
            'max_endpoint_count': 'maxEndpointCount',
            'max_ingress_flow_count': 'maxIngressFlowCount',
            'max_msg_spool_usage': 'maxMsgSpoolUsage',
            'max_subscription_count': 'maxSubscriptionCount',
            'max_transacted_session_count': 'maxTransactedSessionCount',
            'max_transaction_count': 'maxTransactionCount',
            'msg_vpn_name': 'msgVpnName',
            'replication_ack_propagation_interval_msg_count': 'replicationAckPropagationIntervalMsgCount',
            'replication_bridge_authentication_basic_client_username': 'replicationBridgeAuthenticationBasicClientUsername',
            'replication_bridge_authentication_basic_password': 'replicationBridgeAuthenticationBasicPassword',
            'replication_bridge_authentication_scheme': 'replicationBridgeAuthenticationScheme',
            'replication_bridge_compressed_data_enabled': 'replicationBridgeCompressedDataEnabled',
            'replication_bridge_egress_flow_window_size': 'replicationBridgeEgressFlowWindowSize',
            'replication_bridge_retry_delay': 'replicationBridgeRetryDelay',
            'replication_bridge_tls_enabled': 'replicationBridgeTlsEnabled',
            'replication_bridge_unidirectional_client_profile_name': 'replicationBridgeUnidirectionalClientProfileName',
            'replication_enabled': 'replicationEnabled',
            'replication_enabled_queue_behavior': 'replicationEnabledQueueBehavior',
            'replication_queue_max_msg_spool_usage': 'replicationQueueMaxMsgSpoolUsage',
            'replication_queue_reject_msg_to_sender_on_discard_enabled': 'replicationQueueRejectMsgToSenderOnDiscardEnabled',
            'replication_reject_msg_when_sync_ineligible_enabled': 'replicationRejectMsgWhenSyncIneligibleEnabled',
            'replication_role': 'replicationRole',
            'replication_transaction_mode': 'replicationTransactionMode',
            'rest_tls_server_cert_enforce_trusted_common_name_enabled': 'restTlsServerCertEnforceTrustedCommonNameEnabled',
            'rest_tls_server_cert_max_chain_depth': 'restTlsServerCertMaxChainDepth',
            'rest_tls_server_cert_validate_date_enabled': 'restTlsServerCertValidateDateEnabled',
            'semp_over_msg_bus_admin_client_enabled': 'sempOverMsgBusAdminClientEnabled',
            'semp_over_msg_bus_admin_distributed_cache_enabled': 'sempOverMsgBusAdminDistributedCacheEnabled',
            'semp_over_msg_bus_admin_enabled': 'sempOverMsgBusAdminEnabled',
            'semp_over_msg_bus_enabled': 'sempOverMsgBusEnabled',
            'semp_over_msg_bus_legacy_show_clear_enabled': 'sempOverMsgBusLegacyShowClearEnabled',
            'semp_over_msg_bus_show_enabled': 'sempOverMsgBusShowEnabled',
            'service_mqtt_max_connection_count': 'serviceMqttMaxConnectionCount',
            'service_mqtt_plain_text_enabled': 'serviceMqttPlainTextEnabled',
            'service_mqtt_plain_text_listen_port': 'serviceMqttPlainTextListenPort',
            'service_mqtt_tls_enabled': 'serviceMqttTlsEnabled',
            'service_mqtt_tls_listen_port': 'serviceMqttTlsListenPort',
            'service_mqtt_tls_web_socket_enabled': 'serviceMqttTlsWebSocketEnabled',
            'service_mqtt_tls_web_socket_listen_port': 'serviceMqttTlsWebSocketListenPort',
            'service_mqtt_web_socket_enabled': 'serviceMqttWebSocketEnabled',
            'service_mqtt_web_socket_listen_port': 'serviceMqttWebSocketListenPort',
            'service_rest_incoming_max_connection_count': 'serviceRestIncomingMaxConnectionCount',
            'service_rest_incoming_plain_text_enabled': 'serviceRestIncomingPlainTextEnabled',
            'service_rest_incoming_plain_text_listen_port': 'serviceRestIncomingPlainTextListenPort',
            'service_rest_incoming_tls_enabled': 'serviceRestIncomingTlsEnabled',
            'service_rest_incoming_tls_listen_port': 'serviceRestIncomingTlsListenPort',
            'service_rest_outgoing_max_connection_count': 'serviceRestOutgoingMaxConnectionCount',
            'service_smf_max_connection_count': 'serviceSmfMaxConnectionCount',
            'service_smf_plain_text_enabled': 'serviceSmfPlainTextEnabled',
            'service_smf_tls_enabled': 'serviceSmfTlsEnabled',
            'service_web_max_connection_count': 'serviceWebMaxConnectionCount',
            'service_web_plain_text_enabled': 'serviceWebPlainTextEnabled',
            'service_web_tls_enabled': 'serviceWebTlsEnabled',
            'tls_allow_downgrade_to_plain_text_enabled': 'tlsAllowDowngradeToPlainTextEnabled'
        }

        self._authentication_basic_enabled = authentication_basic_enabled
        self._authentication_basic_profile_name = authentication_basic_profile_name
        self._authentication_basic_radius_domain = authentication_basic_radius_domain
        self._authentication_basic_type = authentication_basic_type
        self._authentication_client_cert_allow_api_provided_username_enabled = authentication_client_cert_allow_api_provided_username_enabled
        self._authentication_client_cert_enabled = authentication_client_cert_enabled
        self._authentication_client_cert_max_chain_depth = authentication_client_cert_max_chain_depth
        self._authentication_client_cert_validate_date_enabled = authentication_client_cert_validate_date_enabled
        self._authentication_kerberos_allow_api_provided_username_enabled = authentication_kerberos_allow_api_provided_username_enabled
        self._authentication_kerberos_enabled = authentication_kerberos_enabled
        self._authorization_ldap_group_membership_attribute_name = authorization_ldap_group_membership_attribute_name
        self._authorization_profile_name = authorization_profile_name
        self._authorization_type = authorization_type
        self._bridging_tls_server_cert_enforce_trusted_common_name_enabled = bridging_tls_server_cert_enforce_trusted_common_name_enabled
        self._bridging_tls_server_cert_max_chain_depth = bridging_tls_server_cert_max_chain_depth
        self._bridging_tls_server_cert_validate_date_enabled = bridging_tls_server_cert_validate_date_enabled
        self._distributed_cache_management_enabled = distributed_cache_management_enabled
        self._enabled = enabled
        self._event_connection_count_threshold = event_connection_count_threshold
        self._event_egress_flow_count_threshold = event_egress_flow_count_threshold
        self._event_egress_msg_rate_threshold = event_egress_msg_rate_threshold
        self._event_endpoint_count_threshold = event_endpoint_count_threshold
        self._event_ingress_flow_count_threshold = event_ingress_flow_count_threshold
        self._event_ingress_msg_rate_threshold = event_ingress_msg_rate_threshold
        self._event_large_msg_threshold = event_large_msg_threshold
        self._event_log_tag = event_log_tag
        self._event_msg_spool_usage_threshold = event_msg_spool_usage_threshold
        self._event_publish_client_enabled = event_publish_client_enabled
        self._event_publish_msg_vpn_enabled = event_publish_msg_vpn_enabled
        self._event_publish_subscription_mode = event_publish_subscription_mode
        self._event_publish_topic_format_mqtt_enabled = event_publish_topic_format_mqtt_enabled
        self._event_publish_topic_format_smf_enabled = event_publish_topic_format_smf_enabled
        self._event_service_mqtt_connection_count_threshold = event_service_mqtt_connection_count_threshold
        self._event_service_rest_incoming_connection_count_threshold = event_service_rest_incoming_connection_count_threshold
        self._event_service_smf_connection_count_threshold = event_service_smf_connection_count_threshold
        self._event_service_web_connection_count_threshold = event_service_web_connection_count_threshold
        self._event_subscription_count_threshold = event_subscription_count_threshold
        self._event_transacted_session_count_threshold = event_transacted_session_count_threshold
        self._event_transaction_count_threshold = event_transaction_count_threshold
        self._export_subscriptions_enabled = export_subscriptions_enabled
        self._max_connection_count = max_connection_count
        self._max_egress_flow_count = max_egress_flow_count
        self._max_endpoint_count = max_endpoint_count
        self._max_ingress_flow_count = max_ingress_flow_count
        self._max_msg_spool_usage = max_msg_spool_usage
        self._max_subscription_count = max_subscription_count
        self._max_transacted_session_count = max_transacted_session_count
        self._max_transaction_count = max_transaction_count
        self._msg_vpn_name = msg_vpn_name
        self._replication_ack_propagation_interval_msg_count = replication_ack_propagation_interval_msg_count
        self._replication_bridge_authentication_basic_client_username = replication_bridge_authentication_basic_client_username
        self._replication_bridge_authentication_basic_password = replication_bridge_authentication_basic_password
        self._replication_bridge_authentication_scheme = replication_bridge_authentication_scheme
        self._replication_bridge_compressed_data_enabled = replication_bridge_compressed_data_enabled
        self._replication_bridge_egress_flow_window_size = replication_bridge_egress_flow_window_size
        self._replication_bridge_retry_delay = replication_bridge_retry_delay
        self._replication_bridge_tls_enabled = replication_bridge_tls_enabled
        self._replication_bridge_unidirectional_client_profile_name = replication_bridge_unidirectional_client_profile_name
        self._replication_enabled = replication_enabled
        self._replication_enabled_queue_behavior = replication_enabled_queue_behavior
        self._replication_queue_max_msg_spool_usage = replication_queue_max_msg_spool_usage
        self._replication_queue_reject_msg_to_sender_on_discard_enabled = replication_queue_reject_msg_to_sender_on_discard_enabled
        self._replication_reject_msg_when_sync_ineligible_enabled = replication_reject_msg_when_sync_ineligible_enabled
        self._replication_role = replication_role
        self._replication_transaction_mode = replication_transaction_mode
        self._rest_tls_server_cert_enforce_trusted_common_name_enabled = rest_tls_server_cert_enforce_trusted_common_name_enabled
        self._rest_tls_server_cert_max_chain_depth = rest_tls_server_cert_max_chain_depth
        self._rest_tls_server_cert_validate_date_enabled = rest_tls_server_cert_validate_date_enabled
        self._semp_over_msg_bus_admin_client_enabled = semp_over_msg_bus_admin_client_enabled
        self._semp_over_msg_bus_admin_distributed_cache_enabled = semp_over_msg_bus_admin_distributed_cache_enabled
        self._semp_over_msg_bus_admin_enabled = semp_over_msg_bus_admin_enabled
        self._semp_over_msg_bus_enabled = semp_over_msg_bus_enabled
        self._semp_over_msg_bus_legacy_show_clear_enabled = semp_over_msg_bus_legacy_show_clear_enabled
        self._semp_over_msg_bus_show_enabled = semp_over_msg_bus_show_enabled
        self._service_mqtt_max_connection_count = service_mqtt_max_connection_count
        self._service_mqtt_plain_text_enabled = service_mqtt_plain_text_enabled
        self._service_mqtt_plain_text_listen_port = service_mqtt_plain_text_listen_port
        self._service_mqtt_tls_enabled = service_mqtt_tls_enabled
        self._service_mqtt_tls_listen_port = service_mqtt_tls_listen_port
        self._service_mqtt_tls_web_socket_enabled = service_mqtt_tls_web_socket_enabled
        self._service_mqtt_tls_web_socket_listen_port = service_mqtt_tls_web_socket_listen_port
        self._service_mqtt_web_socket_enabled = service_mqtt_web_socket_enabled
        self._service_mqtt_web_socket_listen_port = service_mqtt_web_socket_listen_port
        self._service_rest_incoming_max_connection_count = service_rest_incoming_max_connection_count
        self._service_rest_incoming_plain_text_enabled = service_rest_incoming_plain_text_enabled
        self._service_rest_incoming_plain_text_listen_port = service_rest_incoming_plain_text_listen_port
        self._service_rest_incoming_tls_enabled = service_rest_incoming_tls_enabled
        self._service_rest_incoming_tls_listen_port = service_rest_incoming_tls_listen_port
        self._service_rest_outgoing_max_connection_count = service_rest_outgoing_max_connection_count
        self._service_smf_max_connection_count = service_smf_max_connection_count
        self._service_smf_plain_text_enabled = service_smf_plain_text_enabled
        self._service_smf_tls_enabled = service_smf_tls_enabled
        self._service_web_max_connection_count = service_web_max_connection_count
        self._service_web_plain_text_enabled = service_web_plain_text_enabled
        self._service_web_tls_enabled = service_web_tls_enabled
        self._tls_allow_downgrade_to_plain_text_enabled = tls_allow_downgrade_to_plain_text_enabled

    @property
    def authentication_basic_enabled(self):
        """
        Gets the authentication_basic_enabled of this MsgVpn.
        Enable or disable basic authentication for clients within the Message VPN. Basic authentication is authentication that involves the use of a username and password to prove identity. When enabled, the currently selected authentication type is used for authentication of users that provide basic authentication credentials. If a user provides credentials for a different authentication scheme this setting is not applicable. The default value is `true`.

        :return: The authentication_basic_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._authentication_basic_enabled

    @authentication_basic_enabled.setter
    def authentication_basic_enabled(self, authentication_basic_enabled):
        """
        Sets the authentication_basic_enabled of this MsgVpn.
        Enable or disable basic authentication for clients within the Message VPN. Basic authentication is authentication that involves the use of a username and password to prove identity. When enabled, the currently selected authentication type is used for authentication of users that provide basic authentication credentials. If a user provides credentials for a different authentication scheme this setting is not applicable. The default value is `true`.

        :param authentication_basic_enabled: The authentication_basic_enabled of this MsgVpn.
        :type: bool
        """

        self._authentication_basic_enabled = authentication_basic_enabled

    @property
    def authentication_basic_profile_name(self):
        """
        Gets the authentication_basic_profile_name of this MsgVpn.
        The name of the RADIUS or LDAP profile to use when `authenticationBasicType` is `\"radius\"` or `\"ldap\"` respectively. The default value is `\"default\"`.

        :return: The authentication_basic_profile_name of this MsgVpn.
        :rtype: str
        """
        return self._authentication_basic_profile_name

    @authentication_basic_profile_name.setter
    def authentication_basic_profile_name(self, authentication_basic_profile_name):
        """
        Sets the authentication_basic_profile_name of this MsgVpn.
        The name of the RADIUS or LDAP profile to use when `authenticationBasicType` is `\"radius\"` or `\"ldap\"` respectively. The default value is `\"default\"`.

        :param authentication_basic_profile_name: The authentication_basic_profile_name of this MsgVpn.
        :type: str
        """

        self._authentication_basic_profile_name = authentication_basic_profile_name

    @property
    def authentication_basic_radius_domain(self):
        """
        Gets the authentication_basic_radius_domain of this MsgVpn.
        The RADIUS domain string to use when `authenticationBasicType` is `\"radius\"`. The default value is `\"\"`.

        :return: The authentication_basic_radius_domain of this MsgVpn.
        :rtype: str
        """
        return self._authentication_basic_radius_domain

    @authentication_basic_radius_domain.setter
    def authentication_basic_radius_domain(self, authentication_basic_radius_domain):
        """
        Sets the authentication_basic_radius_domain of this MsgVpn.
        The RADIUS domain string to use when `authenticationBasicType` is `\"radius\"`. The default value is `\"\"`.

        :param authentication_basic_radius_domain: The authentication_basic_radius_domain of this MsgVpn.
        :type: str
        """

        self._authentication_basic_radius_domain = authentication_basic_radius_domain

    @property
    def authentication_basic_type(self):
        """
        Gets the authentication_basic_type of this MsgVpn.
        Authentication mechanism to be used for basic authentication of clients connecting to this Message VPN. The default value is `\"radius\"`. The allowed values and their meaning are:      \"radius\" - Radius authentication. A radius profile must be provided.     \"ldap\" - LDAP authentication. An LDAP profile must be provided.     \"internal\" - Internal database. Authentication is against Client Usernames.     \"none\" - No authentication. Anonymous login allowed. 

        :return: The authentication_basic_type of this MsgVpn.
        :rtype: str
        """
        return self._authentication_basic_type

    @authentication_basic_type.setter
    def authentication_basic_type(self, authentication_basic_type):
        """
        Sets the authentication_basic_type of this MsgVpn.
        Authentication mechanism to be used for basic authentication of clients connecting to this Message VPN. The default value is `\"radius\"`. The allowed values and their meaning are:      \"radius\" - Radius authentication. A radius profile must be provided.     \"ldap\" - LDAP authentication. An LDAP profile must be provided.     \"internal\" - Internal database. Authentication is against Client Usernames.     \"none\" - No authentication. Anonymous login allowed. 

        :param authentication_basic_type: The authentication_basic_type of this MsgVpn.
        :type: str
        """
        allowed_values = ["radius", "ldap", "internal", "none"]
        if authentication_basic_type not in allowed_values:
            raise ValueError(
                "Invalid value for `authentication_basic_type` ({0}), must be one of {1}"
                .format(authentication_basic_type, allowed_values)
            )

        self._authentication_basic_type = authentication_basic_type

    @property
    def authentication_client_cert_allow_api_provided_username_enabled(self):
        """
        Gets the authentication_client_cert_allow_api_provided_username_enabled of this MsgVpn.
        When enabled, if the client specifies a client-username via the API connect method, the client provided username is used instead of the CN (Common Name) field of the certificate's subject. When disabled, the certificate CN is always used as the client-username. The default value is `false`.

        :return: The authentication_client_cert_allow_api_provided_username_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._authentication_client_cert_allow_api_provided_username_enabled

    @authentication_client_cert_allow_api_provided_username_enabled.setter
    def authentication_client_cert_allow_api_provided_username_enabled(self, authentication_client_cert_allow_api_provided_username_enabled):
        """
        Sets the authentication_client_cert_allow_api_provided_username_enabled of this MsgVpn.
        When enabled, if the client specifies a client-username via the API connect method, the client provided username is used instead of the CN (Common Name) field of the certificate's subject. When disabled, the certificate CN is always used as the client-username. The default value is `false`.

        :param authentication_client_cert_allow_api_provided_username_enabled: The authentication_client_cert_allow_api_provided_username_enabled of this MsgVpn.
        :type: bool
        """

        self._authentication_client_cert_allow_api_provided_username_enabled = authentication_client_cert_allow_api_provided_username_enabled

    @property
    def authentication_client_cert_enabled(self):
        """
        Gets the authentication_client_cert_enabled of this MsgVpn.
        Enable or disable client certificate client authentication in the Message VPN. The default value is `false`.

        :return: The authentication_client_cert_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._authentication_client_cert_enabled

    @authentication_client_cert_enabled.setter
    def authentication_client_cert_enabled(self, authentication_client_cert_enabled):
        """
        Sets the authentication_client_cert_enabled of this MsgVpn.
        Enable or disable client certificate client authentication in the Message VPN. The default value is `false`.

        :param authentication_client_cert_enabled: The authentication_client_cert_enabled of this MsgVpn.
        :type: bool
        """

        self._authentication_client_cert_enabled = authentication_client_cert_enabled

    @property
    def authentication_client_cert_max_chain_depth(self):
        """
        Gets the authentication_client_cert_max_chain_depth of this MsgVpn.
        The maximum depth for a client certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.

        :return: The authentication_client_cert_max_chain_depth of this MsgVpn.
        :rtype: int
        """
        return self._authentication_client_cert_max_chain_depth

    @authentication_client_cert_max_chain_depth.setter
    def authentication_client_cert_max_chain_depth(self, authentication_client_cert_max_chain_depth):
        """
        Sets the authentication_client_cert_max_chain_depth of this MsgVpn.
        The maximum depth for a client certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.

        :param authentication_client_cert_max_chain_depth: The authentication_client_cert_max_chain_depth of this MsgVpn.
        :type: int
        """

        self._authentication_client_cert_max_chain_depth = authentication_client_cert_max_chain_depth

    @property
    def authentication_client_cert_validate_date_enabled(self):
        """
        Gets the authentication_client_cert_validate_date_enabled of this MsgVpn.
        Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.

        :return: The authentication_client_cert_validate_date_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._authentication_client_cert_validate_date_enabled

    @authentication_client_cert_validate_date_enabled.setter
    def authentication_client_cert_validate_date_enabled(self, authentication_client_cert_validate_date_enabled):
        """
        Sets the authentication_client_cert_validate_date_enabled of this MsgVpn.
        Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.

        :param authentication_client_cert_validate_date_enabled: The authentication_client_cert_validate_date_enabled of this MsgVpn.
        :type: bool
        """

        self._authentication_client_cert_validate_date_enabled = authentication_client_cert_validate_date_enabled

    @property
    def authentication_kerberos_allow_api_provided_username_enabled(self):
        """
        Gets the authentication_kerberos_allow_api_provided_username_enabled of this MsgVpn.
        When enabled, if the client specifies a client-username via the API connect method, the client provided username is used instead of the Kerberos Principal name in Kerberos token. When disabled, the Kerberos Principal name is always used as the client-username. The default value is `false`.

        :return: The authentication_kerberos_allow_api_provided_username_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._authentication_kerberos_allow_api_provided_username_enabled

    @authentication_kerberos_allow_api_provided_username_enabled.setter
    def authentication_kerberos_allow_api_provided_username_enabled(self, authentication_kerberos_allow_api_provided_username_enabled):
        """
        Sets the authentication_kerberos_allow_api_provided_username_enabled of this MsgVpn.
        When enabled, if the client specifies a client-username via the API connect method, the client provided username is used instead of the Kerberos Principal name in Kerberos token. When disabled, the Kerberos Principal name is always used as the client-username. The default value is `false`.

        :param authentication_kerberos_allow_api_provided_username_enabled: The authentication_kerberos_allow_api_provided_username_enabled of this MsgVpn.
        :type: bool
        """

        self._authentication_kerberos_allow_api_provided_username_enabled = authentication_kerberos_allow_api_provided_username_enabled

    @property
    def authentication_kerberos_enabled(self):
        """
        Gets the authentication_kerberos_enabled of this MsgVpn.
        Enable or disable Kerberos authentication for clients in the Message VPN. If a user provides credentials for a different authentication scheme, this setting is not applicable. The default value is `false`.

        :return: The authentication_kerberos_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._authentication_kerberos_enabled

    @authentication_kerberos_enabled.setter
    def authentication_kerberos_enabled(self, authentication_kerberos_enabled):
        """
        Sets the authentication_kerberos_enabled of this MsgVpn.
        Enable or disable Kerberos authentication for clients in the Message VPN. If a user provides credentials for a different authentication scheme, this setting is not applicable. The default value is `false`.

        :param authentication_kerberos_enabled: The authentication_kerberos_enabled of this MsgVpn.
        :type: bool
        """

        self._authentication_kerberos_enabled = authentication_kerberos_enabled

    @property
    def authorization_ldap_group_membership_attribute_name(self):
        """
        Gets the authorization_ldap_group_membership_attribute_name of this MsgVpn.
        The name of the attribute that should be retrieved from the LDAP server as part of the LDAP search when authorizing a client. It indicates that the client belongs to a particular group (i.e. the value associated with this attribute). The default value is `\"memberOf\"`.

        :return: The authorization_ldap_group_membership_attribute_name of this MsgVpn.
        :rtype: str
        """
        return self._authorization_ldap_group_membership_attribute_name

    @authorization_ldap_group_membership_attribute_name.setter
    def authorization_ldap_group_membership_attribute_name(self, authorization_ldap_group_membership_attribute_name):
        """
        Sets the authorization_ldap_group_membership_attribute_name of this MsgVpn.
        The name of the attribute that should be retrieved from the LDAP server as part of the LDAP search when authorizing a client. It indicates that the client belongs to a particular group (i.e. the value associated with this attribute). The default value is `\"memberOf\"`.

        :param authorization_ldap_group_membership_attribute_name: The authorization_ldap_group_membership_attribute_name of this MsgVpn.
        :type: str
        """

        self._authorization_ldap_group_membership_attribute_name = authorization_ldap_group_membership_attribute_name

    @property
    def authorization_profile_name(self):
        """
        Gets the authorization_profile_name of this MsgVpn.
        The LDAP profile name to be used when `authorizationType` is `\"ldap\"`. The default value is `\"\"`.

        :return: The authorization_profile_name of this MsgVpn.
        :rtype: str
        """
        return self._authorization_profile_name

    @authorization_profile_name.setter
    def authorization_profile_name(self, authorization_profile_name):
        """
        Sets the authorization_profile_name of this MsgVpn.
        The LDAP profile name to be used when `authorizationType` is `\"ldap\"`. The default value is `\"\"`.

        :param authorization_profile_name: The authorization_profile_name of this MsgVpn.
        :type: str
        """

        self._authorization_profile_name = authorization_profile_name

    @property
    def authorization_type(self):
        """
        Gets the authorization_type of this MsgVpn.
        Authorization mechanism to be used for clients connecting to this Message VPN. The default value is `\"internal\"`. The allowed values and their meaning are:      \"ldap\" - LDAP authorization.     \"internal\" - Internal authorization. 

        :return: The authorization_type of this MsgVpn.
        :rtype: str
        """
        return self._authorization_type

    @authorization_type.setter
    def authorization_type(self, authorization_type):
        """
        Sets the authorization_type of this MsgVpn.
        Authorization mechanism to be used for clients connecting to this Message VPN. The default value is `\"internal\"`. The allowed values and their meaning are:      \"ldap\" - LDAP authorization.     \"internal\" - Internal authorization. 

        :param authorization_type: The authorization_type of this MsgVpn.
        :type: str
        """
        allowed_values = ["ldap", "internal"]
        if authorization_type not in allowed_values:
            raise ValueError(
                "Invalid value for `authorization_type` ({0}), must be one of {1}"
                .format(authorization_type, allowed_values)
            )

        self._authorization_type = authorization_type

    @property
    def bridging_tls_server_cert_enforce_trusted_common_name_enabled(self):
        """
        Gets the bridging_tls_server_cert_enforce_trusted_common_name_enabled of this MsgVpn.
        Enable or disable validation of the common name in the server certificate on the remote router. If enabled, the common name is checked against the list of trusted common names configured for the bridge. The default value is `true`.

        :return: The bridging_tls_server_cert_enforce_trusted_common_name_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._bridging_tls_server_cert_enforce_trusted_common_name_enabled

    @bridging_tls_server_cert_enforce_trusted_common_name_enabled.setter
    def bridging_tls_server_cert_enforce_trusted_common_name_enabled(self, bridging_tls_server_cert_enforce_trusted_common_name_enabled):
        """
        Sets the bridging_tls_server_cert_enforce_trusted_common_name_enabled of this MsgVpn.
        Enable or disable validation of the common name in the server certificate on the remote router. If enabled, the common name is checked against the list of trusted common names configured for the bridge. The default value is `true`.

        :param bridging_tls_server_cert_enforce_trusted_common_name_enabled: The bridging_tls_server_cert_enforce_trusted_common_name_enabled of this MsgVpn.
        :type: bool
        """

        self._bridging_tls_server_cert_enforce_trusted_common_name_enabled = bridging_tls_server_cert_enforce_trusted_common_name_enabled

    @property
    def bridging_tls_server_cert_max_chain_depth(self):
        """
        Gets the bridging_tls_server_cert_max_chain_depth of this MsgVpn.
        The maximum depth for a server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.

        :return: The bridging_tls_server_cert_max_chain_depth of this MsgVpn.
        :rtype: int
        """
        return self._bridging_tls_server_cert_max_chain_depth

    @bridging_tls_server_cert_max_chain_depth.setter
    def bridging_tls_server_cert_max_chain_depth(self, bridging_tls_server_cert_max_chain_depth):
        """
        Sets the bridging_tls_server_cert_max_chain_depth of this MsgVpn.
        The maximum depth for a server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.

        :param bridging_tls_server_cert_max_chain_depth: The bridging_tls_server_cert_max_chain_depth of this MsgVpn.
        :type: int
        """

        self._bridging_tls_server_cert_max_chain_depth = bridging_tls_server_cert_max_chain_depth

    @property
    def bridging_tls_server_cert_validate_date_enabled(self):
        """
        Gets the bridging_tls_server_cert_validate_date_enabled of this MsgVpn.
        Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.

        :return: The bridging_tls_server_cert_validate_date_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._bridging_tls_server_cert_validate_date_enabled

    @bridging_tls_server_cert_validate_date_enabled.setter
    def bridging_tls_server_cert_validate_date_enabled(self, bridging_tls_server_cert_validate_date_enabled):
        """
        Sets the bridging_tls_server_cert_validate_date_enabled of this MsgVpn.
        Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.

        :param bridging_tls_server_cert_validate_date_enabled: The bridging_tls_server_cert_validate_date_enabled of this MsgVpn.
        :type: bool
        """

        self._bridging_tls_server_cert_validate_date_enabled = bridging_tls_server_cert_validate_date_enabled

    @property
    def distributed_cache_management_enabled(self):
        """
        Gets the distributed_cache_management_enabled of this MsgVpn.
        Enable or disable managing of cache instances over the message bus. For a given Message VPN only one router in the network should have this attribute enabled. The default value is `true`.

        :return: The distributed_cache_management_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._distributed_cache_management_enabled

    @distributed_cache_management_enabled.setter
    def distributed_cache_management_enabled(self, distributed_cache_management_enabled):
        """
        Sets the distributed_cache_management_enabled of this MsgVpn.
        Enable or disable managing of cache instances over the message bus. For a given Message VPN only one router in the network should have this attribute enabled. The default value is `true`.

        :param distributed_cache_management_enabled: The distributed_cache_management_enabled of this MsgVpn.
        :type: bool
        """

        self._distributed_cache_management_enabled = distributed_cache_management_enabled

    @property
    def enabled(self):
        """
        Gets the enabled of this MsgVpn.
        Enable or disable the Message VPN. The default value is `false`.

        :return: The enabled of this MsgVpn.
        :rtype: bool
        """
        return self._enabled

    @enabled.setter
    def enabled(self, enabled):
        """
        Sets the enabled of this MsgVpn.
        Enable or disable the Message VPN. The default value is `false`.

        :param enabled: The enabled of this MsgVpn.
        :type: bool
        """

        self._enabled = enabled

    @property
    def event_connection_count_threshold(self):
        """
        Gets the event_connection_count_threshold of this MsgVpn.


        :return: The event_connection_count_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_connection_count_threshold

    @event_connection_count_threshold.setter
    def event_connection_count_threshold(self, event_connection_count_threshold):
        """
        Sets the event_connection_count_threshold of this MsgVpn.


        :param event_connection_count_threshold: The event_connection_count_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_connection_count_threshold = event_connection_count_threshold

    @property
    def event_egress_flow_count_threshold(self):
        """
        Gets the event_egress_flow_count_threshold of this MsgVpn.


        :return: The event_egress_flow_count_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_egress_flow_count_threshold

    @event_egress_flow_count_threshold.setter
    def event_egress_flow_count_threshold(self, event_egress_flow_count_threshold):
        """
        Sets the event_egress_flow_count_threshold of this MsgVpn.


        :param event_egress_flow_count_threshold: The event_egress_flow_count_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_egress_flow_count_threshold = event_egress_flow_count_threshold

    @property
    def event_egress_msg_rate_threshold(self):
        """
        Gets the event_egress_msg_rate_threshold of this MsgVpn.


        :return: The event_egress_msg_rate_threshold of this MsgVpn.
        :rtype: EventThresholdByValue
        """
        return self._event_egress_msg_rate_threshold

    @event_egress_msg_rate_threshold.setter
    def event_egress_msg_rate_threshold(self, event_egress_msg_rate_threshold):
        """
        Sets the event_egress_msg_rate_threshold of this MsgVpn.


        :param event_egress_msg_rate_threshold: The event_egress_msg_rate_threshold of this MsgVpn.
        :type: EventThresholdByValue
        """

        self._event_egress_msg_rate_threshold = event_egress_msg_rate_threshold

    @property
    def event_endpoint_count_threshold(self):
        """
        Gets the event_endpoint_count_threshold of this MsgVpn.


        :return: The event_endpoint_count_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_endpoint_count_threshold

    @event_endpoint_count_threshold.setter
    def event_endpoint_count_threshold(self, event_endpoint_count_threshold):
        """
        Sets the event_endpoint_count_threshold of this MsgVpn.


        :param event_endpoint_count_threshold: The event_endpoint_count_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_endpoint_count_threshold = event_endpoint_count_threshold

    @property
    def event_ingress_flow_count_threshold(self):
        """
        Gets the event_ingress_flow_count_threshold of this MsgVpn.


        :return: The event_ingress_flow_count_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_ingress_flow_count_threshold

    @event_ingress_flow_count_threshold.setter
    def event_ingress_flow_count_threshold(self, event_ingress_flow_count_threshold):
        """
        Sets the event_ingress_flow_count_threshold of this MsgVpn.


        :param event_ingress_flow_count_threshold: The event_ingress_flow_count_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_ingress_flow_count_threshold = event_ingress_flow_count_threshold

    @property
    def event_ingress_msg_rate_threshold(self):
        """
        Gets the event_ingress_msg_rate_threshold of this MsgVpn.


        :return: The event_ingress_msg_rate_threshold of this MsgVpn.
        :rtype: EventThresholdByValue
        """
        return self._event_ingress_msg_rate_threshold

    @event_ingress_msg_rate_threshold.setter
    def event_ingress_msg_rate_threshold(self, event_ingress_msg_rate_threshold):
        """
        Sets the event_ingress_msg_rate_threshold of this MsgVpn.


        :param event_ingress_msg_rate_threshold: The event_ingress_msg_rate_threshold of this MsgVpn.
        :type: EventThresholdByValue
        """

        self._event_ingress_msg_rate_threshold = event_ingress_msg_rate_threshold

    @property
    def event_large_msg_threshold(self):
        """
        Gets the event_large_msg_threshold of this MsgVpn.
        Size in KB for what is being considered a large message for the Message VPN. The default value is `1024`.

        :return: The event_large_msg_threshold of this MsgVpn.
        :rtype: int
        """
        return self._event_large_msg_threshold

    @event_large_msg_threshold.setter
    def event_large_msg_threshold(self, event_large_msg_threshold):
        """
        Sets the event_large_msg_threshold of this MsgVpn.
        Size in KB for what is being considered a large message for the Message VPN. The default value is `1024`.

        :param event_large_msg_threshold: The event_large_msg_threshold of this MsgVpn.
        :type: int
        """

        self._event_large_msg_threshold = event_large_msg_threshold

    @property
    def event_log_tag(self):
        """
        Gets the event_log_tag of this MsgVpn.
        A prefix applied to all publish events in this Message VPN. The default is to have no `eventLogTag`.

        :return: The event_log_tag of this MsgVpn.
        :rtype: str
        """
        return self._event_log_tag

    @event_log_tag.setter
    def event_log_tag(self, event_log_tag):
        """
        Sets the event_log_tag of this MsgVpn.
        A prefix applied to all publish events in this Message VPN. The default is to have no `eventLogTag`.

        :param event_log_tag: The event_log_tag of this MsgVpn.
        :type: str
        """

        self._event_log_tag = event_log_tag

    @property
    def event_msg_spool_usage_threshold(self):
        """
        Gets the event_msg_spool_usage_threshold of this MsgVpn.


        :return: The event_msg_spool_usage_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_msg_spool_usage_threshold

    @event_msg_spool_usage_threshold.setter
    def event_msg_spool_usage_threshold(self, event_msg_spool_usage_threshold):
        """
        Sets the event_msg_spool_usage_threshold of this MsgVpn.


        :param event_msg_spool_usage_threshold: The event_msg_spool_usage_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_msg_spool_usage_threshold = event_msg_spool_usage_threshold

    @property
    def event_publish_client_enabled(self):
        """
        Gets the event_publish_client_enabled of this MsgVpn.
        Enable or disable client level event message publishing. The default value is `false`.

        :return: The event_publish_client_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._event_publish_client_enabled

    @event_publish_client_enabled.setter
    def event_publish_client_enabled(self, event_publish_client_enabled):
        """
        Sets the event_publish_client_enabled of this MsgVpn.
        Enable or disable client level event message publishing. The default value is `false`.

        :param event_publish_client_enabled: The event_publish_client_enabled of this MsgVpn.
        :type: bool
        """

        self._event_publish_client_enabled = event_publish_client_enabled

    @property
    def event_publish_msg_vpn_enabled(self):
        """
        Gets the event_publish_msg_vpn_enabled of this MsgVpn.
        Enable or disable Message VPN level event message publishing. The default value is `false`.

        :return: The event_publish_msg_vpn_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._event_publish_msg_vpn_enabled

    @event_publish_msg_vpn_enabled.setter
    def event_publish_msg_vpn_enabled(self, event_publish_msg_vpn_enabled):
        """
        Sets the event_publish_msg_vpn_enabled of this MsgVpn.
        Enable or disable Message VPN level event message publishing. The default value is `false`.

        :param event_publish_msg_vpn_enabled: The event_publish_msg_vpn_enabled of this MsgVpn.
        :type: bool
        """

        self._event_publish_msg_vpn_enabled = event_publish_msg_vpn_enabled

    @property
    def event_publish_subscription_mode(self):
        """
        Gets the event_publish_subscription_mode of this MsgVpn.
         Subscription level event message publishing mode. Format v1 modes use a publish topic of the form:      #LOG/INFO/SUB_ADD/subscribed-topic     #LOG/INFO/SUB_DEL/subscribed-topic  Format v2 modes use a publish topic of the form:      #LOG/INFO/SUB/router-name/ADD/vpn-name/client-name/subscribed-topic     #LOG/INFO/SUB/router-name/DEL/vpn-name/client-name/subscribed-topic  Format v2 is recommended. . The default value is `\"off\"`. The allowed values and their meaning are:      \"off\" - Disable client level event message publishing.     \"on-with-format-v1\" - Enable client level event message publishing with format v1.     \"on-with-no-unsubscribe-events-on-disconnect-format-v1\" - As \"on-with-format-v1\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions.     \"on-with-format-v2\" - Enable client level event message publishing with format v2.     \"on-with-no-unsubscribe-events-on-disconnect-format-v2\" - As \"on-with-format-v2\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. 

        :return: The event_publish_subscription_mode of this MsgVpn.
        :rtype: str
        """
        return self._event_publish_subscription_mode

    @event_publish_subscription_mode.setter
    def event_publish_subscription_mode(self, event_publish_subscription_mode):
        """
        Sets the event_publish_subscription_mode of this MsgVpn.
         Subscription level event message publishing mode. Format v1 modes use a publish topic of the form:      #LOG/INFO/SUB_ADD/subscribed-topic     #LOG/INFO/SUB_DEL/subscribed-topic  Format v2 modes use a publish topic of the form:      #LOG/INFO/SUB/router-name/ADD/vpn-name/client-name/subscribed-topic     #LOG/INFO/SUB/router-name/DEL/vpn-name/client-name/subscribed-topic  Format v2 is recommended. . The default value is `\"off\"`. The allowed values and their meaning are:      \"off\" - Disable client level event message publishing.     \"on-with-format-v1\" - Enable client level event message publishing with format v1.     \"on-with-no-unsubscribe-events-on-disconnect-format-v1\" - As \"on-with-format-v1\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions.     \"on-with-format-v2\" - Enable client level event message publishing with format v2.     \"on-with-no-unsubscribe-events-on-disconnect-format-v2\" - As \"on-with-format-v2\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. 

        :param event_publish_subscription_mode: The event_publish_subscription_mode of this MsgVpn.
        :type: str
        """
        allowed_values = ["off", "on-with-format-v1", "on-with-no-unsubscribe-events-on-disconnect-format-v1", "on-with-format-v2", "on-with-no-unsubscribe-events-on-disconnect-format-v2"]
        if event_publish_subscription_mode not in allowed_values:
            raise ValueError(
                "Invalid value for `event_publish_subscription_mode` ({0}), must be one of {1}"
                .format(event_publish_subscription_mode, allowed_values)
            )

        self._event_publish_subscription_mode = event_publish_subscription_mode

    @property
    def event_publish_topic_format_mqtt_enabled(self):
        """
        Gets the event_publish_topic_format_mqtt_enabled of this MsgVpn.
        Enable or disable event publish topics in MQTT format. The default value is `false`.

        :return: The event_publish_topic_format_mqtt_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._event_publish_topic_format_mqtt_enabled

    @event_publish_topic_format_mqtt_enabled.setter
    def event_publish_topic_format_mqtt_enabled(self, event_publish_topic_format_mqtt_enabled):
        """
        Sets the event_publish_topic_format_mqtt_enabled of this MsgVpn.
        Enable or disable event publish topics in MQTT format. The default value is `false`.

        :param event_publish_topic_format_mqtt_enabled: The event_publish_topic_format_mqtt_enabled of this MsgVpn.
        :type: bool
        """

        self._event_publish_topic_format_mqtt_enabled = event_publish_topic_format_mqtt_enabled

    @property
    def event_publish_topic_format_smf_enabled(self):
        """
        Gets the event_publish_topic_format_smf_enabled of this MsgVpn.
        Enable or disable event publish topics in SMF format. The default value is `true`.

        :return: The event_publish_topic_format_smf_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._event_publish_topic_format_smf_enabled

    @event_publish_topic_format_smf_enabled.setter
    def event_publish_topic_format_smf_enabled(self, event_publish_topic_format_smf_enabled):
        """
        Sets the event_publish_topic_format_smf_enabled of this MsgVpn.
        Enable or disable event publish topics in SMF format. The default value is `true`.

        :param event_publish_topic_format_smf_enabled: The event_publish_topic_format_smf_enabled of this MsgVpn.
        :type: bool
        """

        self._event_publish_topic_format_smf_enabled = event_publish_topic_format_smf_enabled

    @property
    def event_service_mqtt_connection_count_threshold(self):
        """
        Gets the event_service_mqtt_connection_count_threshold of this MsgVpn.


        :return: The event_service_mqtt_connection_count_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_service_mqtt_connection_count_threshold

    @event_service_mqtt_connection_count_threshold.setter
    def event_service_mqtt_connection_count_threshold(self, event_service_mqtt_connection_count_threshold):
        """
        Sets the event_service_mqtt_connection_count_threshold of this MsgVpn.


        :param event_service_mqtt_connection_count_threshold: The event_service_mqtt_connection_count_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_service_mqtt_connection_count_threshold = event_service_mqtt_connection_count_threshold

    @property
    def event_service_rest_incoming_connection_count_threshold(self):
        """
        Gets the event_service_rest_incoming_connection_count_threshold of this MsgVpn.


        :return: The event_service_rest_incoming_connection_count_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_service_rest_incoming_connection_count_threshold

    @event_service_rest_incoming_connection_count_threshold.setter
    def event_service_rest_incoming_connection_count_threshold(self, event_service_rest_incoming_connection_count_threshold):
        """
        Sets the event_service_rest_incoming_connection_count_threshold of this MsgVpn.


        :param event_service_rest_incoming_connection_count_threshold: The event_service_rest_incoming_connection_count_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_service_rest_incoming_connection_count_threshold = event_service_rest_incoming_connection_count_threshold

    @property
    def event_service_smf_connection_count_threshold(self):
        """
        Gets the event_service_smf_connection_count_threshold of this MsgVpn.


        :return: The event_service_smf_connection_count_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_service_smf_connection_count_threshold

    @event_service_smf_connection_count_threshold.setter
    def event_service_smf_connection_count_threshold(self, event_service_smf_connection_count_threshold):
        """
        Sets the event_service_smf_connection_count_threshold of this MsgVpn.


        :param event_service_smf_connection_count_threshold: The event_service_smf_connection_count_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_service_smf_connection_count_threshold = event_service_smf_connection_count_threshold

    @property
    def event_service_web_connection_count_threshold(self):
        """
        Gets the event_service_web_connection_count_threshold of this MsgVpn.


        :return: The event_service_web_connection_count_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_service_web_connection_count_threshold

    @event_service_web_connection_count_threshold.setter
    def event_service_web_connection_count_threshold(self, event_service_web_connection_count_threshold):
        """
        Sets the event_service_web_connection_count_threshold of this MsgVpn.


        :param event_service_web_connection_count_threshold: The event_service_web_connection_count_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_service_web_connection_count_threshold = event_service_web_connection_count_threshold

    @property
    def event_subscription_count_threshold(self):
        """
        Gets the event_subscription_count_threshold of this MsgVpn.


        :return: The event_subscription_count_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_subscription_count_threshold

    @event_subscription_count_threshold.setter
    def event_subscription_count_threshold(self, event_subscription_count_threshold):
        """
        Sets the event_subscription_count_threshold of this MsgVpn.


        :param event_subscription_count_threshold: The event_subscription_count_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_subscription_count_threshold = event_subscription_count_threshold

    @property
    def event_transacted_session_count_threshold(self):
        """
        Gets the event_transacted_session_count_threshold of this MsgVpn.


        :return: The event_transacted_session_count_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_transacted_session_count_threshold

    @event_transacted_session_count_threshold.setter
    def event_transacted_session_count_threshold(self, event_transacted_session_count_threshold):
        """
        Sets the event_transacted_session_count_threshold of this MsgVpn.


        :param event_transacted_session_count_threshold: The event_transacted_session_count_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_transacted_session_count_threshold = event_transacted_session_count_threshold

    @property
    def event_transaction_count_threshold(self):
        """
        Gets the event_transaction_count_threshold of this MsgVpn.


        :return: The event_transaction_count_threshold of this MsgVpn.
        :rtype: EventThreshold
        """
        return self._event_transaction_count_threshold

    @event_transaction_count_threshold.setter
    def event_transaction_count_threshold(self, event_transaction_count_threshold):
        """
        Sets the event_transaction_count_threshold of this MsgVpn.


        :param event_transaction_count_threshold: The event_transaction_count_threshold of this MsgVpn.
        :type: EventThreshold
        """

        self._event_transaction_count_threshold = event_transaction_count_threshold

    @property
    def export_subscriptions_enabled(self):
        """
        Gets the export_subscriptions_enabled of this MsgVpn.
        Enable or disable the export of subscriptions in this Message VPN to other routers in the network over neighbor links. The default value is `false`.

        :return: The export_subscriptions_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._export_subscriptions_enabled

    @export_subscriptions_enabled.setter
    def export_subscriptions_enabled(self, export_subscriptions_enabled):
        """
        Sets the export_subscriptions_enabled of this MsgVpn.
        Enable or disable the export of subscriptions in this Message VPN to other routers in the network over neighbor links. The default value is `false`.

        :param export_subscriptions_enabled: The export_subscriptions_enabled of this MsgVpn.
        :type: bool
        """

        self._export_subscriptions_enabled = export_subscriptions_enabled

    @property
    def max_connection_count(self):
        """
        Gets the max_connection_count of this MsgVpn.
        The maximum number of client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :return: The max_connection_count of this MsgVpn.
        :rtype: int
        """
        return self._max_connection_count

    @max_connection_count.setter
    def max_connection_count(self, max_connection_count):
        """
        Sets the max_connection_count of this MsgVpn.
        The maximum number of client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :param max_connection_count: The max_connection_count of this MsgVpn.
        :type: int
        """

        self._max_connection_count = max_connection_count

    @property
    def max_egress_flow_count(self):
        """
        Gets the max_egress_flow_count of this MsgVpn.
        The maximum number of egress flows that can be created on this Message VPN. The default value is `16000`.

        :return: The max_egress_flow_count of this MsgVpn.
        :rtype: int
        """
        return self._max_egress_flow_count

    @max_egress_flow_count.setter
    def max_egress_flow_count(self, max_egress_flow_count):
        """
        Sets the max_egress_flow_count of this MsgVpn.
        The maximum number of egress flows that can be created on this Message VPN. The default value is `16000`.

        :param max_egress_flow_count: The max_egress_flow_count of this MsgVpn.
        :type: int
        """

        self._max_egress_flow_count = max_egress_flow_count

    @property
    def max_endpoint_count(self):
        """
        Gets the max_endpoint_count of this MsgVpn.
        The maximum number of queues and topic endpoints that can be created on this Message VPN. The default value is `16000`.

        :return: The max_endpoint_count of this MsgVpn.
        :rtype: int
        """
        return self._max_endpoint_count

    @max_endpoint_count.setter
    def max_endpoint_count(self, max_endpoint_count):
        """
        Sets the max_endpoint_count of this MsgVpn.
        The maximum number of queues and topic endpoints that can be created on this Message VPN. The default value is `16000`.

        :param max_endpoint_count: The max_endpoint_count of this MsgVpn.
        :type: int
        """

        self._max_endpoint_count = max_endpoint_count

    @property
    def max_ingress_flow_count(self):
        """
        Gets the max_ingress_flow_count of this MsgVpn.
        The maximum number of ingress flows that can be created on this Message VPN. The default value is `16000`.

        :return: The max_ingress_flow_count of this MsgVpn.
        :rtype: int
        """
        return self._max_ingress_flow_count

    @max_ingress_flow_count.setter
    def max_ingress_flow_count(self, max_ingress_flow_count):
        """
        Sets the max_ingress_flow_count of this MsgVpn.
        The maximum number of ingress flows that can be created on this Message VPN. The default value is `16000`.

        :param max_ingress_flow_count: The max_ingress_flow_count of this MsgVpn.
        :type: int
        """

        self._max_ingress_flow_count = max_ingress_flow_count

    @property
    def max_msg_spool_usage(self):
        """
        Gets the max_msg_spool_usage of this MsgVpn.
        Max spool usage (in MB) allowed for the Message VPN. The default value is `0`.

        :return: The max_msg_spool_usage of this MsgVpn.
        :rtype: int
        """
        return self._max_msg_spool_usage

    @max_msg_spool_usage.setter
    def max_msg_spool_usage(self, max_msg_spool_usage):
        """
        Sets the max_msg_spool_usage of this MsgVpn.
        Max spool usage (in MB) allowed for the Message VPN. The default value is `0`.

        :param max_msg_spool_usage: The max_msg_spool_usage of this MsgVpn.
        :type: int
        """

        self._max_msg_spool_usage = max_msg_spool_usage

    @property
    def max_subscription_count(self):
        """
        Gets the max_subscription_count of this MsgVpn.
        The maximum number of local client subscriptions (both primary and backup) that can be added to this Message VPN. The default varies by platform.

        :return: The max_subscription_count of this MsgVpn.
        :rtype: int
        """
        return self._max_subscription_count

    @max_subscription_count.setter
    def max_subscription_count(self, max_subscription_count):
        """
        Sets the max_subscription_count of this MsgVpn.
        The maximum number of local client subscriptions (both primary and backup) that can be added to this Message VPN. The default varies by platform.

        :param max_subscription_count: The max_subscription_count of this MsgVpn.
        :type: int
        """

        self._max_subscription_count = max_subscription_count

    @property
    def max_transacted_session_count(self):
        """
        Gets the max_transacted_session_count of this MsgVpn.
        The maximum number of transacted sessions for this Message VPN. The default varies by platform.

        :return: The max_transacted_session_count of this MsgVpn.
        :rtype: int
        """
        return self._max_transacted_session_count

    @max_transacted_session_count.setter
    def max_transacted_session_count(self, max_transacted_session_count):
        """
        Sets the max_transacted_session_count of this MsgVpn.
        The maximum number of transacted sessions for this Message VPN. The default varies by platform.

        :param max_transacted_session_count: The max_transacted_session_count of this MsgVpn.
        :type: int
        """

        self._max_transacted_session_count = max_transacted_session_count

    @property
    def max_transaction_count(self):
        """
        Gets the max_transaction_count of this MsgVpn.
        The maximum number of transactions for this Message VPN. The default varies by platform.

        :return: The max_transaction_count of this MsgVpn.
        :rtype: int
        """
        return self._max_transaction_count

    @max_transaction_count.setter
    def max_transaction_count(self, max_transaction_count):
        """
        Sets the max_transaction_count of this MsgVpn.
        The maximum number of transactions for this Message VPN. The default varies by platform.

        :param max_transaction_count: The max_transaction_count of this MsgVpn.
        :type: int
        """

        self._max_transaction_count = max_transaction_count

    @property
    def msg_vpn_name(self):
        """
        Gets the msg_vpn_name of this MsgVpn.
        The name of the Message VPN.

        :return: The msg_vpn_name of this MsgVpn.
        :rtype: str
        """
        return self._msg_vpn_name

    @msg_vpn_name.setter
    def msg_vpn_name(self, msg_vpn_name):
        """
        Sets the msg_vpn_name of this MsgVpn.
        The name of the Message VPN.

        :param msg_vpn_name: The msg_vpn_name of this MsgVpn.
        :type: str
        """

        self._msg_vpn_name = msg_vpn_name

    @property
    def replication_ack_propagation_interval_msg_count(self):
        """
        Gets the replication_ack_propagation_interval_msg_count of this MsgVpn.
        The ack-propagation interval, in number of replicated messages. The default value is `20`.

        :return: The replication_ack_propagation_interval_msg_count of this MsgVpn.
        :rtype: int
        """
        return self._replication_ack_propagation_interval_msg_count

    @replication_ack_propagation_interval_msg_count.setter
    def replication_ack_propagation_interval_msg_count(self, replication_ack_propagation_interval_msg_count):
        """
        Sets the replication_ack_propagation_interval_msg_count of this MsgVpn.
        The ack-propagation interval, in number of replicated messages. The default value is `20`.

        :param replication_ack_propagation_interval_msg_count: The replication_ack_propagation_interval_msg_count of this MsgVpn.
        :type: int
        """

        self._replication_ack_propagation_interval_msg_count = replication_ack_propagation_interval_msg_count

    @property
    def replication_bridge_authentication_basic_client_username(self):
        """
        Gets the replication_bridge_authentication_basic_client_username of this MsgVpn.
        The client username the replication bridge uses to login to the Remote Message VPN on the replication mate. The default is to have no `replicationBridgeAuthenticationBasicClientUsername`.

        :return: The replication_bridge_authentication_basic_client_username of this MsgVpn.
        :rtype: str
        """
        return self._replication_bridge_authentication_basic_client_username

    @replication_bridge_authentication_basic_client_username.setter
    def replication_bridge_authentication_basic_client_username(self, replication_bridge_authentication_basic_client_username):
        """
        Sets the replication_bridge_authentication_basic_client_username of this MsgVpn.
        The client username the replication bridge uses to login to the Remote Message VPN on the replication mate. The default is to have no `replicationBridgeAuthenticationBasicClientUsername`.

        :param replication_bridge_authentication_basic_client_username: The replication_bridge_authentication_basic_client_username of this MsgVpn.
        :type: str
        """

        self._replication_bridge_authentication_basic_client_username = replication_bridge_authentication_basic_client_username

    @property
    def replication_bridge_authentication_basic_password(self):
        """
        Gets the replication_bridge_authentication_basic_password of this MsgVpn.
        The password that the bridge uses to login to the Remote Message VPN. The default is to have no `replicationBridgeAuthenticationBasicPassword`.

        :return: The replication_bridge_authentication_basic_password of this MsgVpn.
        :rtype: str
        """
        return self._replication_bridge_authentication_basic_password

    @replication_bridge_authentication_basic_password.setter
    def replication_bridge_authentication_basic_password(self, replication_bridge_authentication_basic_password):
        """
        Sets the replication_bridge_authentication_basic_password of this MsgVpn.
        The password that the bridge uses to login to the Remote Message VPN. The default is to have no `replicationBridgeAuthenticationBasicPassword`.

        :param replication_bridge_authentication_basic_password: The replication_bridge_authentication_basic_password of this MsgVpn.
        :type: str
        """

        self._replication_bridge_authentication_basic_password = replication_bridge_authentication_basic_password

    @property
    def replication_bridge_authentication_scheme(self):
        """
        Gets the replication_bridge_authentication_scheme of this MsgVpn.
        The authentication scheme for the replication bridge. The default value is `\"basic\"`. The allowed values and their meaning are:      \"basic\" - Basic Authentication Scheme (via username and password).     \"client-certificate\" - Client Certificate Authentication Scheme (via certificate-file). 

        :return: The replication_bridge_authentication_scheme of this MsgVpn.
        :rtype: str
        """
        return self._replication_bridge_authentication_scheme

    @replication_bridge_authentication_scheme.setter
    def replication_bridge_authentication_scheme(self, replication_bridge_authentication_scheme):
        """
        Sets the replication_bridge_authentication_scheme of this MsgVpn.
        The authentication scheme for the replication bridge. The default value is `\"basic\"`. The allowed values and their meaning are:      \"basic\" - Basic Authentication Scheme (via username and password).     \"client-certificate\" - Client Certificate Authentication Scheme (via certificate-file). 

        :param replication_bridge_authentication_scheme: The replication_bridge_authentication_scheme of this MsgVpn.
        :type: str
        """
        allowed_values = ["basic", "client-certificate"]
        if replication_bridge_authentication_scheme not in allowed_values:
            raise ValueError(
                "Invalid value for `replication_bridge_authentication_scheme` ({0}), must be one of {1}"
                .format(replication_bridge_authentication_scheme, allowed_values)
            )

        self._replication_bridge_authentication_scheme = replication_bridge_authentication_scheme

    @property
    def replication_bridge_compressed_data_enabled(self):
        """
        Gets the replication_bridge_compressed_data_enabled of this MsgVpn.
        Whether compression is used for the bridge. The default value is `false`.

        :return: The replication_bridge_compressed_data_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._replication_bridge_compressed_data_enabled

    @replication_bridge_compressed_data_enabled.setter
    def replication_bridge_compressed_data_enabled(self, replication_bridge_compressed_data_enabled):
        """
        Sets the replication_bridge_compressed_data_enabled of this MsgVpn.
        Whether compression is used for the bridge. The default value is `false`.

        :param replication_bridge_compressed_data_enabled: The replication_bridge_compressed_data_enabled of this MsgVpn.
        :type: bool
        """

        self._replication_bridge_compressed_data_enabled = replication_bridge_compressed_data_enabled

    @property
    def replication_bridge_egress_flow_window_size(self):
        """
        Gets the replication_bridge_egress_flow_window_size of this MsgVpn.
        The window size of outstanding guaranteed messages. The default value is `255`.

        :return: The replication_bridge_egress_flow_window_size of this MsgVpn.
        :rtype: int
        """
        return self._replication_bridge_egress_flow_window_size

    @replication_bridge_egress_flow_window_size.setter
    def replication_bridge_egress_flow_window_size(self, replication_bridge_egress_flow_window_size):
        """
        Sets the replication_bridge_egress_flow_window_size of this MsgVpn.
        The window size of outstanding guaranteed messages. The default value is `255`.

        :param replication_bridge_egress_flow_window_size: The replication_bridge_egress_flow_window_size of this MsgVpn.
        :type: int
        """

        self._replication_bridge_egress_flow_window_size = replication_bridge_egress_flow_window_size

    @property
    def replication_bridge_retry_delay(self):
        """
        Gets the replication_bridge_retry_delay of this MsgVpn.
        Number of seconds that must pass before retrying a connection. The default value is `3`.

        :return: The replication_bridge_retry_delay of this MsgVpn.
        :rtype: int
        """
        return self._replication_bridge_retry_delay

    @replication_bridge_retry_delay.setter
    def replication_bridge_retry_delay(self, replication_bridge_retry_delay):
        """
        Sets the replication_bridge_retry_delay of this MsgVpn.
        Number of seconds that must pass before retrying a connection. The default value is `3`.

        :param replication_bridge_retry_delay: The replication_bridge_retry_delay of this MsgVpn.
        :type: int
        """

        self._replication_bridge_retry_delay = replication_bridge_retry_delay

    @property
    def replication_bridge_tls_enabled(self):
        """
        Gets the replication_bridge_tls_enabled of this MsgVpn.
        Enable or disable use of TLS for the bridge connection. The default value is `false`.

        :return: The replication_bridge_tls_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._replication_bridge_tls_enabled

    @replication_bridge_tls_enabled.setter
    def replication_bridge_tls_enabled(self, replication_bridge_tls_enabled):
        """
        Sets the replication_bridge_tls_enabled of this MsgVpn.
        Enable or disable use of TLS for the bridge connection. The default value is `false`.

        :param replication_bridge_tls_enabled: The replication_bridge_tls_enabled of this MsgVpn.
        :type: bool
        """

        self._replication_bridge_tls_enabled = replication_bridge_tls_enabled

    @property
    def replication_bridge_unidirectional_client_profile_name(self):
        """
        Gets the replication_bridge_unidirectional_client_profile_name of this MsgVpn.
        The client-profile for the unidirectional replication bridge for the Message VPN. The client-profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is `\"#client-profile\"`.

        :return: The replication_bridge_unidirectional_client_profile_name of this MsgVpn.
        :rtype: str
        """
        return self._replication_bridge_unidirectional_client_profile_name

    @replication_bridge_unidirectional_client_profile_name.setter
    def replication_bridge_unidirectional_client_profile_name(self, replication_bridge_unidirectional_client_profile_name):
        """
        Sets the replication_bridge_unidirectional_client_profile_name of this MsgVpn.
        The client-profile for the unidirectional replication bridge for the Message VPN. The client-profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is `\"#client-profile\"`.

        :param replication_bridge_unidirectional_client_profile_name: The replication_bridge_unidirectional_client_profile_name of this MsgVpn.
        :type: str
        """

        self._replication_bridge_unidirectional_client_profile_name = replication_bridge_unidirectional_client_profile_name

    @property
    def replication_enabled(self):
        """
        Gets the replication_enabled of this MsgVpn.
        Enable or disable the replication feature for the Message VPN. The default value is `false`.

        :return: The replication_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._replication_enabled

    @replication_enabled.setter
    def replication_enabled(self, replication_enabled):
        """
        Sets the replication_enabled of this MsgVpn.
        Enable or disable the replication feature for the Message VPN. The default value is `false`.

        :param replication_enabled: The replication_enabled of this MsgVpn.
        :type: bool
        """

        self._replication_enabled = replication_enabled

    @property
    def replication_enabled_queue_behavior(self):
        """
        Gets the replication_enabled_queue_behavior of this MsgVpn.
        The behavior to take when enabling replication, depending on the existence of the replication queue. This only has meaning in a request which enables replication. The default value is `\"fail-on-existing-queue\"`. The allowed values and their meaning are:      \"fail-on-existing-queue\" - The data replication queue must not already exist.     \"force-use-existing-queue\" - The data replication queue must already exist. Any data messages on the queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail.     \"force-recreate-queue\" - The data replication queue must already exist. Any data messages on the queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. 

        :return: The replication_enabled_queue_behavior of this MsgVpn.
        :rtype: str
        """
        return self._replication_enabled_queue_behavior

    @replication_enabled_queue_behavior.setter
    def replication_enabled_queue_behavior(self, replication_enabled_queue_behavior):
        """
        Sets the replication_enabled_queue_behavior of this MsgVpn.
        The behavior to take when enabling replication, depending on the existence of the replication queue. This only has meaning in a request which enables replication. The default value is `\"fail-on-existing-queue\"`. The allowed values and their meaning are:      \"fail-on-existing-queue\" - The data replication queue must not already exist.     \"force-use-existing-queue\" - The data replication queue must already exist. Any data messages on the queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail.     \"force-recreate-queue\" - The data replication queue must already exist. Any data messages on the queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. 

        :param replication_enabled_queue_behavior: The replication_enabled_queue_behavior of this MsgVpn.
        :type: str
        """
        allowed_values = ["fail-on-existing-queue", "force-use-existing-queue", "force-recreate-queue"]
        if replication_enabled_queue_behavior not in allowed_values:
            raise ValueError(
                "Invalid value for `replication_enabled_queue_behavior` ({0}), must be one of {1}"
                .format(replication_enabled_queue_behavior, allowed_values)
            )

        self._replication_enabled_queue_behavior = replication_enabled_queue_behavior

    @property
    def replication_queue_max_msg_spool_usage(self):
        """
        Gets the replication_queue_max_msg_spool_usage of this MsgVpn.
        The max spool usage (in MB) of the replication queue. The default value is `60000`.

        :return: The replication_queue_max_msg_spool_usage of this MsgVpn.
        :rtype: int
        """
        return self._replication_queue_max_msg_spool_usage

    @replication_queue_max_msg_spool_usage.setter
    def replication_queue_max_msg_spool_usage(self, replication_queue_max_msg_spool_usage):
        """
        Sets the replication_queue_max_msg_spool_usage of this MsgVpn.
        The max spool usage (in MB) of the replication queue. The default value is `60000`.

        :param replication_queue_max_msg_spool_usage: The replication_queue_max_msg_spool_usage of this MsgVpn.
        :type: int
        """

        self._replication_queue_max_msg_spool_usage = replication_queue_max_msg_spool_usage

    @property
    def replication_queue_reject_msg_to_sender_on_discard_enabled(self):
        """
        Gets the replication_queue_reject_msg_to_sender_on_discard_enabled of this MsgVpn.
        The message discard behavior. The default value is `true`.

        :return: The replication_queue_reject_msg_to_sender_on_discard_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._replication_queue_reject_msg_to_sender_on_discard_enabled

    @replication_queue_reject_msg_to_sender_on_discard_enabled.setter
    def replication_queue_reject_msg_to_sender_on_discard_enabled(self, replication_queue_reject_msg_to_sender_on_discard_enabled):
        """
        Sets the replication_queue_reject_msg_to_sender_on_discard_enabled of this MsgVpn.
        The message discard behavior. The default value is `true`.

        :param replication_queue_reject_msg_to_sender_on_discard_enabled: The replication_queue_reject_msg_to_sender_on_discard_enabled of this MsgVpn.
        :type: bool
        """

        self._replication_queue_reject_msg_to_sender_on_discard_enabled = replication_queue_reject_msg_to_sender_on_discard_enabled

    @property
    def replication_reject_msg_when_sync_ineligible_enabled(self):
        """
        Gets the replication_reject_msg_when_sync_ineligible_enabled of this MsgVpn.
        Enable or disable sync mode ineligible behavior. If enabled and sync replication becomes ineligible, guaranteed messages published to sync replicated topics will be rejected to the sender. If disabled, sync replication will revert to async mode. The default value is `false`.

        :return: The replication_reject_msg_when_sync_ineligible_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._replication_reject_msg_when_sync_ineligible_enabled

    @replication_reject_msg_when_sync_ineligible_enabled.setter
    def replication_reject_msg_when_sync_ineligible_enabled(self, replication_reject_msg_when_sync_ineligible_enabled):
        """
        Sets the replication_reject_msg_when_sync_ineligible_enabled of this MsgVpn.
        Enable or disable sync mode ineligible behavior. If enabled and sync replication becomes ineligible, guaranteed messages published to sync replicated topics will be rejected to the sender. If disabled, sync replication will revert to async mode. The default value is `false`.

        :param replication_reject_msg_when_sync_ineligible_enabled: The replication_reject_msg_when_sync_ineligible_enabled of this MsgVpn.
        :type: bool
        """

        self._replication_reject_msg_when_sync_ineligible_enabled = replication_reject_msg_when_sync_ineligible_enabled

    @property
    def replication_role(self):
        """
        Gets the replication_role of this MsgVpn.
        The replication role for this Message VPN. The default value is `\"standby\"`. The allowed values and their meaning are:      \"active\" - Assume the active role in replication for the Message VPN.     \"standby\" - Assume the standby role in replication for the Message VPN. 

        :return: The replication_role of this MsgVpn.
        :rtype: str
        """
        return self._replication_role

    @replication_role.setter
    def replication_role(self, replication_role):
        """
        Sets the replication_role of this MsgVpn.
        The replication role for this Message VPN. The default value is `\"standby\"`. The allowed values and their meaning are:      \"active\" - Assume the active role in replication for the Message VPN.     \"standby\" - Assume the standby role in replication for the Message VPN. 

        :param replication_role: The replication_role of this MsgVpn.
        :type: str
        """
        allowed_values = ["active", "standby"]
        if replication_role not in allowed_values:
            raise ValueError(
                "Invalid value for `replication_role` ({0}), must be one of {1}"
                .format(replication_role, allowed_values)
            )

        self._replication_role = replication_role

    @property
    def replication_transaction_mode(self):
        """
        Gets the replication_transaction_mode of this MsgVpn.
        The transaction replication mode for all transactions within a Message VPN. When mode is async, all transactions originated by clients are replicated to the standby site using async-replication. When mode is sync, all transactions originated by clients are replicated to the standby site using sync-replication. Changing this value during operation will not affect existing transactions. It is only validated upon starting a transaction. The default value is `\"async\"`. The allowed values and their meaning are:      \"sync\" - Synchronous replication-mode. Published messages are acknowledged when they are spooled on the standby site.     \"async\" - Asynchronous replication-mode. Published messages are acknowledged when they are spooled locally. 

        :return: The replication_transaction_mode of this MsgVpn.
        :rtype: str
        """
        return self._replication_transaction_mode

    @replication_transaction_mode.setter
    def replication_transaction_mode(self, replication_transaction_mode):
        """
        Sets the replication_transaction_mode of this MsgVpn.
        The transaction replication mode for all transactions within a Message VPN. When mode is async, all transactions originated by clients are replicated to the standby site using async-replication. When mode is sync, all transactions originated by clients are replicated to the standby site using sync-replication. Changing this value during operation will not affect existing transactions. It is only validated upon starting a transaction. The default value is `\"async\"`. The allowed values and their meaning are:      \"sync\" - Synchronous replication-mode. Published messages are acknowledged when they are spooled on the standby site.     \"async\" - Asynchronous replication-mode. Published messages are acknowledged when they are spooled locally. 

        :param replication_transaction_mode: The replication_transaction_mode of this MsgVpn.
        :type: str
        """
        allowed_values = ["sync", "async"]
        if replication_transaction_mode not in allowed_values:
            raise ValueError(
                "Invalid value for `replication_transaction_mode` ({0}), must be one of {1}"
                .format(replication_transaction_mode, allowed_values)
            )

        self._replication_transaction_mode = replication_transaction_mode

    @property
    def rest_tls_server_cert_enforce_trusted_common_name_enabled(self):
        """
        Gets the rest_tls_server_cert_enforce_trusted_common_name_enabled of this MsgVpn.
        Enable or disable whether or not the trusted-common-name attribute of a REST Consumer is enforced or not. Each REST Consumer has a list of common-names which it expects to be returned in the server-certificate from the remote REST Consumer. If enforce-trusted-common-name is enabled, but the list of common-names has not been configured, the REST Consumer will not be allowed to be brought into service. An appropriate error message is provided in the REST Consumer operational display. The default value is `true`.

        :return: The rest_tls_server_cert_enforce_trusted_common_name_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._rest_tls_server_cert_enforce_trusted_common_name_enabled

    @rest_tls_server_cert_enforce_trusted_common_name_enabled.setter
    def rest_tls_server_cert_enforce_trusted_common_name_enabled(self, rest_tls_server_cert_enforce_trusted_common_name_enabled):
        """
        Sets the rest_tls_server_cert_enforce_trusted_common_name_enabled of this MsgVpn.
        Enable or disable whether or not the trusted-common-name attribute of a REST Consumer is enforced or not. Each REST Consumer has a list of common-names which it expects to be returned in the server-certificate from the remote REST Consumer. If enforce-trusted-common-name is enabled, but the list of common-names has not been configured, the REST Consumer will not be allowed to be brought into service. An appropriate error message is provided in the REST Consumer operational display. The default value is `true`.

        :param rest_tls_server_cert_enforce_trusted_common_name_enabled: The rest_tls_server_cert_enforce_trusted_common_name_enabled of this MsgVpn.
        :type: bool
        """

        self._rest_tls_server_cert_enforce_trusted_common_name_enabled = rest_tls_server_cert_enforce_trusted_common_name_enabled

    @property
    def rest_tls_server_cert_max_chain_depth(self):
        """
        Gets the rest_tls_server_cert_max_chain_depth of this MsgVpn.
        The maximum depth for the certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.

        :return: The rest_tls_server_cert_max_chain_depth of this MsgVpn.
        :rtype: int
        """
        return self._rest_tls_server_cert_max_chain_depth

    @rest_tls_server_cert_max_chain_depth.setter
    def rest_tls_server_cert_max_chain_depth(self, rest_tls_server_cert_max_chain_depth):
        """
        Sets the rest_tls_server_cert_max_chain_depth of this MsgVpn.
        The maximum depth for the certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.

        :param rest_tls_server_cert_max_chain_depth: The rest_tls_server_cert_max_chain_depth of this MsgVpn.
        :type: int
        """

        self._rest_tls_server_cert_max_chain_depth = rest_tls_server_cert_max_chain_depth

    @property
    def rest_tls_server_cert_validate_date_enabled(self):
        """
        Gets the rest_tls_server_cert_validate_date_enabled of this MsgVpn.
        Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.

        :return: The rest_tls_server_cert_validate_date_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._rest_tls_server_cert_validate_date_enabled

    @rest_tls_server_cert_validate_date_enabled.setter
    def rest_tls_server_cert_validate_date_enabled(self, rest_tls_server_cert_validate_date_enabled):
        """
        Sets the rest_tls_server_cert_validate_date_enabled of this MsgVpn.
        Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.

        :param rest_tls_server_cert_validate_date_enabled: The rest_tls_server_cert_validate_date_enabled of this MsgVpn.
        :type: bool
        """

        self._rest_tls_server_cert_validate_date_enabled = rest_tls_server_cert_validate_date_enabled

    @property
    def semp_over_msg_bus_admin_client_enabled(self):
        """
        Gets the semp_over_msg_bus_admin_client_enabled of this MsgVpn.
        Enable or disable \"admin client\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.

        :return: The semp_over_msg_bus_admin_client_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._semp_over_msg_bus_admin_client_enabled

    @semp_over_msg_bus_admin_client_enabled.setter
    def semp_over_msg_bus_admin_client_enabled(self, semp_over_msg_bus_admin_client_enabled):
        """
        Sets the semp_over_msg_bus_admin_client_enabled of this MsgVpn.
        Enable or disable \"admin client\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.

        :param semp_over_msg_bus_admin_client_enabled: The semp_over_msg_bus_admin_client_enabled of this MsgVpn.
        :type: bool
        """

        self._semp_over_msg_bus_admin_client_enabled = semp_over_msg_bus_admin_client_enabled

    @property
    def semp_over_msg_bus_admin_distributed_cache_enabled(self):
        """
        Gets the semp_over_msg_bus_admin_distributed_cache_enabled of this MsgVpn.
        Enable or disable \"admin distributed-cache\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.

        :return: The semp_over_msg_bus_admin_distributed_cache_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._semp_over_msg_bus_admin_distributed_cache_enabled

    @semp_over_msg_bus_admin_distributed_cache_enabled.setter
    def semp_over_msg_bus_admin_distributed_cache_enabled(self, semp_over_msg_bus_admin_distributed_cache_enabled):
        """
        Sets the semp_over_msg_bus_admin_distributed_cache_enabled of this MsgVpn.
        Enable or disable \"admin distributed-cache\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.

        :param semp_over_msg_bus_admin_distributed_cache_enabled: The semp_over_msg_bus_admin_distributed_cache_enabled of this MsgVpn.
        :type: bool
        """

        self._semp_over_msg_bus_admin_distributed_cache_enabled = semp_over_msg_bus_admin_distributed_cache_enabled

    @property
    def semp_over_msg_bus_admin_enabled(self):
        """
        Gets the semp_over_msg_bus_admin_enabled of this MsgVpn.
        Enable or disable \"admin\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.

        :return: The semp_over_msg_bus_admin_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._semp_over_msg_bus_admin_enabled

    @semp_over_msg_bus_admin_enabled.setter
    def semp_over_msg_bus_admin_enabled(self, semp_over_msg_bus_admin_enabled):
        """
        Sets the semp_over_msg_bus_admin_enabled of this MsgVpn.
        Enable or disable \"admin\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.

        :param semp_over_msg_bus_admin_enabled: The semp_over_msg_bus_admin_enabled of this MsgVpn.
        :type: bool
        """

        self._semp_over_msg_bus_admin_enabled = semp_over_msg_bus_admin_enabled

    @property
    def semp_over_msg_bus_enabled(self):
        """
        Gets the semp_over_msg_bus_enabled of this MsgVpn.
        Enable or disable SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `true`.

        :return: The semp_over_msg_bus_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._semp_over_msg_bus_enabled

    @semp_over_msg_bus_enabled.setter
    def semp_over_msg_bus_enabled(self, semp_over_msg_bus_enabled):
        """
        Sets the semp_over_msg_bus_enabled of this MsgVpn.
        Enable or disable SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `true`.

        :param semp_over_msg_bus_enabled: The semp_over_msg_bus_enabled of this MsgVpn.
        :type: bool
        """

        self._semp_over_msg_bus_enabled = semp_over_msg_bus_enabled

    @property
    def semp_over_msg_bus_legacy_show_clear_enabled(self):
        """
        Gets the semp_over_msg_bus_legacy_show_clear_enabled of this MsgVpn.
        Enable or disable \"legacy-show-clear\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `true`.

        :return: The semp_over_msg_bus_legacy_show_clear_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._semp_over_msg_bus_legacy_show_clear_enabled

    @semp_over_msg_bus_legacy_show_clear_enabled.setter
    def semp_over_msg_bus_legacy_show_clear_enabled(self, semp_over_msg_bus_legacy_show_clear_enabled):
        """
        Sets the semp_over_msg_bus_legacy_show_clear_enabled of this MsgVpn.
        Enable or disable \"legacy-show-clear\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `true`.

        :param semp_over_msg_bus_legacy_show_clear_enabled: The semp_over_msg_bus_legacy_show_clear_enabled of this MsgVpn.
        :type: bool
        """

        self._semp_over_msg_bus_legacy_show_clear_enabled = semp_over_msg_bus_legacy_show_clear_enabled

    @property
    def semp_over_msg_bus_show_enabled(self):
        """
        Gets the semp_over_msg_bus_show_enabled of this MsgVpn.
        Enable or disable \"show\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.

        :return: The semp_over_msg_bus_show_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._semp_over_msg_bus_show_enabled

    @semp_over_msg_bus_show_enabled.setter
    def semp_over_msg_bus_show_enabled(self, semp_over_msg_bus_show_enabled):
        """
        Sets the semp_over_msg_bus_show_enabled of this MsgVpn.
        Enable or disable \"show\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.

        :param semp_over_msg_bus_show_enabled: The semp_over_msg_bus_show_enabled of this MsgVpn.
        :type: bool
        """

        self._semp_over_msg_bus_show_enabled = semp_over_msg_bus_show_enabled

    @property
    def service_mqtt_max_connection_count(self):
        """
        Gets the service_mqtt_max_connection_count of this MsgVpn.
        The maximum number of MQTT client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware. Available since 2.1.0.

        :return: The service_mqtt_max_connection_count of this MsgVpn.
        :rtype: int
        """
        return self._service_mqtt_max_connection_count

    @service_mqtt_max_connection_count.setter
    def service_mqtt_max_connection_count(self, service_mqtt_max_connection_count):
        """
        Sets the service_mqtt_max_connection_count of this MsgVpn.
        The maximum number of MQTT client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware. Available since 2.1.0.

        :param service_mqtt_max_connection_count: The service_mqtt_max_connection_count of this MsgVpn.
        :type: int
        """

        self._service_mqtt_max_connection_count = service_mqtt_max_connection_count

    @property
    def service_mqtt_plain_text_enabled(self):
        """
        Gets the service_mqtt_plain_text_enabled of this MsgVpn.
        Enable or disable TCP plain-text MQTT on this Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. The default value is `false`. Available since 2.1.0.

        :return: The service_mqtt_plain_text_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._service_mqtt_plain_text_enabled

    @service_mqtt_plain_text_enabled.setter
    def service_mqtt_plain_text_enabled(self, service_mqtt_plain_text_enabled):
        """
        Sets the service_mqtt_plain_text_enabled of this MsgVpn.
        Enable or disable TCP plain-text MQTT on this Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. The default value is `false`. Available since 2.1.0.

        :param service_mqtt_plain_text_enabled: The service_mqtt_plain_text_enabled of this MsgVpn.
        :type: bool
        """

        self._service_mqtt_plain_text_enabled = service_mqtt_plain_text_enabled

    @property
    def service_mqtt_plain_text_listen_port(self):
        """
        Gets the service_mqtt_plain_text_listen_port of this MsgVpn.
        TCP port number that MQTT clients can use to connect to the router using raw plain-text TCP for this VPN. The default is to have no `serviceMqttPlainTextListenPort`. Available since 2.1.0.

        :return: The service_mqtt_plain_text_listen_port of this MsgVpn.
        :rtype: int
        """
        return self._service_mqtt_plain_text_listen_port

    @service_mqtt_plain_text_listen_port.setter
    def service_mqtt_plain_text_listen_port(self, service_mqtt_plain_text_listen_port):
        """
        Sets the service_mqtt_plain_text_listen_port of this MsgVpn.
        TCP port number that MQTT clients can use to connect to the router using raw plain-text TCP for this VPN. The default is to have no `serviceMqttPlainTextListenPort`. Available since 2.1.0.

        :param service_mqtt_plain_text_listen_port: The service_mqtt_plain_text_listen_port of this MsgVpn.
        :type: int
        """

        self._service_mqtt_plain_text_listen_port = service_mqtt_plain_text_listen_port

    @property
    def service_mqtt_tls_enabled(self):
        """
        Gets the service_mqtt_tls_enabled of this MsgVpn.
        Enable or disable TCP TLS MQTT on this Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. The default value is `false`. Available since 2.1.0.

        :return: The service_mqtt_tls_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._service_mqtt_tls_enabled

    @service_mqtt_tls_enabled.setter
    def service_mqtt_tls_enabled(self, service_mqtt_tls_enabled):
        """
        Sets the service_mqtt_tls_enabled of this MsgVpn.
        Enable or disable TCP TLS MQTT on this Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. The default value is `false`. Available since 2.1.0.

        :param service_mqtt_tls_enabled: The service_mqtt_tls_enabled of this MsgVpn.
        :type: bool
        """

        self._service_mqtt_tls_enabled = service_mqtt_tls_enabled

    @property
    def service_mqtt_tls_listen_port(self):
        """
        Gets the service_mqtt_tls_listen_port of this MsgVpn.
        TCP port number that MQTT clients can use to connect to the router using raw TCP over TLS for this VPN. The default is to have no `serviceMqttTlsListenPort`. Available since 2.1.0.

        :return: The service_mqtt_tls_listen_port of this MsgVpn.
        :rtype: int
        """
        return self._service_mqtt_tls_listen_port

    @service_mqtt_tls_listen_port.setter
    def service_mqtt_tls_listen_port(self, service_mqtt_tls_listen_port):
        """
        Sets the service_mqtt_tls_listen_port of this MsgVpn.
        TCP port number that MQTT clients can use to connect to the router using raw TCP over TLS for this VPN. The default is to have no `serviceMqttTlsListenPort`. Available since 2.1.0.

        :param service_mqtt_tls_listen_port: The service_mqtt_tls_listen_port of this MsgVpn.
        :type: int
        """

        self._service_mqtt_tls_listen_port = service_mqtt_tls_listen_port

    @property
    def service_mqtt_tls_web_socket_enabled(self):
        """
        Gets the service_mqtt_tls_web_socket_enabled of this MsgVpn.
        Enable or disable TLS WebSocket on this Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. The default value is `false`. Available since 2.1.0.

        :return: The service_mqtt_tls_web_socket_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._service_mqtt_tls_web_socket_enabled

    @service_mqtt_tls_web_socket_enabled.setter
    def service_mqtt_tls_web_socket_enabled(self, service_mqtt_tls_web_socket_enabled):
        """
        Sets the service_mqtt_tls_web_socket_enabled of this MsgVpn.
        Enable or disable TLS WebSocket on this Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. The default value is `false`. Available since 2.1.0.

        :param service_mqtt_tls_web_socket_enabled: The service_mqtt_tls_web_socket_enabled of this MsgVpn.
        :type: bool
        """

        self._service_mqtt_tls_web_socket_enabled = service_mqtt_tls_web_socket_enabled

    @property
    def service_mqtt_tls_web_socket_listen_port(self):
        """
        Gets the service_mqtt_tls_web_socket_listen_port of this MsgVpn.
        TCP port number that MQTT clients can use to connect to the router using TLS WebSocket for this VPN. The default is to have no `serviceMqttTlsWebSocketListenPort`. Available since 2.1.0.

        :return: The service_mqtt_tls_web_socket_listen_port of this MsgVpn.
        :rtype: int
        """
        return self._service_mqtt_tls_web_socket_listen_port

    @service_mqtt_tls_web_socket_listen_port.setter
    def service_mqtt_tls_web_socket_listen_port(self, service_mqtt_tls_web_socket_listen_port):
        """
        Sets the service_mqtt_tls_web_socket_listen_port of this MsgVpn.
        TCP port number that MQTT clients can use to connect to the router using TLS WebSocket for this VPN. The default is to have no `serviceMqttTlsWebSocketListenPort`. Available since 2.1.0.

        :param service_mqtt_tls_web_socket_listen_port: The service_mqtt_tls_web_socket_listen_port of this MsgVpn.
        :type: int
        """

        self._service_mqtt_tls_web_socket_listen_port = service_mqtt_tls_web_socket_listen_port

    @property
    def service_mqtt_web_socket_enabled(self):
        """
        Gets the service_mqtt_web_socket_enabled of this MsgVpn.
        Enable or disable WebSocket MQTT on this Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. The default value is `false`. Available since 2.1.0.

        :return: The service_mqtt_web_socket_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._service_mqtt_web_socket_enabled

    @service_mqtt_web_socket_enabled.setter
    def service_mqtt_web_socket_enabled(self, service_mqtt_web_socket_enabled):
        """
        Sets the service_mqtt_web_socket_enabled of this MsgVpn.
        Enable or disable WebSocket MQTT on this Message VPN. Disabling causes clients connected to the corresponding listen-port to be disconnected. The default value is `false`. Available since 2.1.0.

        :param service_mqtt_web_socket_enabled: The service_mqtt_web_socket_enabled of this MsgVpn.
        :type: bool
        """

        self._service_mqtt_web_socket_enabled = service_mqtt_web_socket_enabled

    @property
    def service_mqtt_web_socket_listen_port(self):
        """
        Gets the service_mqtt_web_socket_listen_port of this MsgVpn.
        TCP port number that MQTT clients can use to connect to the router using WebSocket for this VPN. The default is to have no `serviceMqttWebSocketListenPort`. Available since 2.1.0.

        :return: The service_mqtt_web_socket_listen_port of this MsgVpn.
        :rtype: int
        """
        return self._service_mqtt_web_socket_listen_port

    @service_mqtt_web_socket_listen_port.setter
    def service_mqtt_web_socket_listen_port(self, service_mqtt_web_socket_listen_port):
        """
        Sets the service_mqtt_web_socket_listen_port of this MsgVpn.
        TCP port number that MQTT clients can use to connect to the router using WebSocket for this VPN. The default is to have no `serviceMqttWebSocketListenPort`. Available since 2.1.0.

        :param service_mqtt_web_socket_listen_port: The service_mqtt_web_socket_listen_port of this MsgVpn.
        :type: int
        """

        self._service_mqtt_web_socket_listen_port = service_mqtt_web_socket_listen_port

    @property
    def service_rest_incoming_max_connection_count(self):
        """
        Gets the service_rest_incoming_max_connection_count of this MsgVpn.
        The maximum number of REST incoming client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :return: The service_rest_incoming_max_connection_count of this MsgVpn.
        :rtype: int
        """
        return self._service_rest_incoming_max_connection_count

    @service_rest_incoming_max_connection_count.setter
    def service_rest_incoming_max_connection_count(self, service_rest_incoming_max_connection_count):
        """
        Sets the service_rest_incoming_max_connection_count of this MsgVpn.
        The maximum number of REST incoming client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :param service_rest_incoming_max_connection_count: The service_rest_incoming_max_connection_count of this MsgVpn.
        :type: int
        """

        self._service_rest_incoming_max_connection_count = service_rest_incoming_max_connection_count

    @property
    def service_rest_incoming_plain_text_enabled(self):
        """
        Gets the service_rest_incoming_plain_text_enabled of this MsgVpn.
        Enable or disable plain-text REST for this Message VPN. The default value is `false`.

        :return: The service_rest_incoming_plain_text_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._service_rest_incoming_plain_text_enabled

    @service_rest_incoming_plain_text_enabled.setter
    def service_rest_incoming_plain_text_enabled(self, service_rest_incoming_plain_text_enabled):
        """
        Sets the service_rest_incoming_plain_text_enabled of this MsgVpn.
        Enable or disable plain-text REST for this Message VPN. The default value is `false`.

        :param service_rest_incoming_plain_text_enabled: The service_rest_incoming_plain_text_enabled of this MsgVpn.
        :type: bool
        """

        self._service_rest_incoming_plain_text_enabled = service_rest_incoming_plain_text_enabled

    @property
    def service_rest_incoming_plain_text_listen_port(self):
        """
        Gets the service_rest_incoming_plain_text_listen_port of this MsgVpn.
        The TCP port on the NAB for incoming plain-text REST client connections for the Message VPN. The TCP port must not be in use by another service in any Message VPN in the msg-backbone VRF. Enabling plain-text REST is not allowed without a listen-port. The default is to have no `serviceRestIncomingPlainTextListenPort`.

        :return: The service_rest_incoming_plain_text_listen_port of this MsgVpn.
        :rtype: int
        """
        return self._service_rest_incoming_plain_text_listen_port

    @service_rest_incoming_plain_text_listen_port.setter
    def service_rest_incoming_plain_text_listen_port(self, service_rest_incoming_plain_text_listen_port):
        """
        Sets the service_rest_incoming_plain_text_listen_port of this MsgVpn.
        The TCP port on the NAB for incoming plain-text REST client connections for the Message VPN. The TCP port must not be in use by another service in any Message VPN in the msg-backbone VRF. Enabling plain-text REST is not allowed without a listen-port. The default is to have no `serviceRestIncomingPlainTextListenPort`.

        :param service_rest_incoming_plain_text_listen_port: The service_rest_incoming_plain_text_listen_port of this MsgVpn.
        :type: int
        """

        self._service_rest_incoming_plain_text_listen_port = service_rest_incoming_plain_text_listen_port

    @property
    def service_rest_incoming_tls_enabled(self):
        """
        Gets the service_rest_incoming_tls_enabled of this MsgVpn.
        Enable or disable incoming TLS REST service for this Message VPN. The default value is `false`.

        :return: The service_rest_incoming_tls_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._service_rest_incoming_tls_enabled

    @service_rest_incoming_tls_enabled.setter
    def service_rest_incoming_tls_enabled(self, service_rest_incoming_tls_enabled):
        """
        Sets the service_rest_incoming_tls_enabled of this MsgVpn.
        Enable or disable incoming TLS REST service for this Message VPN. The default value is `false`.

        :param service_rest_incoming_tls_enabled: The service_rest_incoming_tls_enabled of this MsgVpn.
        :type: bool
        """

        self._service_rest_incoming_tls_enabled = service_rest_incoming_tls_enabled

    @property
    def service_rest_incoming_tls_listen_port(self):
        """
        Gets the service_rest_incoming_tls_listen_port of this MsgVpn.
        The TCP port on the NAB for incoming TLS REST client connections for the Message VPN. The TCP port must not be in use by another service in any Message VPN in the msg-backbone VRF. Enabling plain-text REST is not allowed without a listen-port. The default is to have no `serviceRestIncomingTlsListenPort`.

        :return: The service_rest_incoming_tls_listen_port of this MsgVpn.
        :rtype: int
        """
        return self._service_rest_incoming_tls_listen_port

    @service_rest_incoming_tls_listen_port.setter
    def service_rest_incoming_tls_listen_port(self, service_rest_incoming_tls_listen_port):
        """
        Sets the service_rest_incoming_tls_listen_port of this MsgVpn.
        The TCP port on the NAB for incoming TLS REST client connections for the Message VPN. The TCP port must not be in use by another service in any Message VPN in the msg-backbone VRF. Enabling plain-text REST is not allowed without a listen-port. The default is to have no `serviceRestIncomingTlsListenPort`.

        :param service_rest_incoming_tls_listen_port: The service_rest_incoming_tls_listen_port of this MsgVpn.
        :type: int
        """

        self._service_rest_incoming_tls_listen_port = service_rest_incoming_tls_listen_port

    @property
    def service_rest_outgoing_max_connection_count(self):
        """
        Gets the service_rest_outgoing_max_connection_count of this MsgVpn.
        The maximum number of REST consumer connections that can be simultaneously established from the Message VPN. The default varies by platform.

        :return: The service_rest_outgoing_max_connection_count of this MsgVpn.
        :rtype: int
        """
        return self._service_rest_outgoing_max_connection_count

    @service_rest_outgoing_max_connection_count.setter
    def service_rest_outgoing_max_connection_count(self, service_rest_outgoing_max_connection_count):
        """
        Sets the service_rest_outgoing_max_connection_count of this MsgVpn.
        The maximum number of REST consumer connections that can be simultaneously established from the Message VPN. The default varies by platform.

        :param service_rest_outgoing_max_connection_count: The service_rest_outgoing_max_connection_count of this MsgVpn.
        :type: int
        """

        self._service_rest_outgoing_max_connection_count = service_rest_outgoing_max_connection_count

    @property
    def service_smf_max_connection_count(self):
        """
        Gets the service_smf_max_connection_count of this MsgVpn.
        The maximum number of SMF client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :return: The service_smf_max_connection_count of this MsgVpn.
        :rtype: int
        """
        return self._service_smf_max_connection_count

    @service_smf_max_connection_count.setter
    def service_smf_max_connection_count(self, service_smf_max_connection_count):
        """
        Sets the service_smf_max_connection_count of this MsgVpn.
        The maximum number of SMF client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :param service_smf_max_connection_count: The service_smf_max_connection_count of this MsgVpn.
        :type: int
        """

        self._service_smf_max_connection_count = service_smf_max_connection_count

    @property
    def service_smf_plain_text_enabled(self):
        """
        Gets the service_smf_plain_text_enabled of this MsgVpn.
        Enable or disable plain-text SMF service in the Message VPN. The default value is `true`.

        :return: The service_smf_plain_text_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._service_smf_plain_text_enabled

    @service_smf_plain_text_enabled.setter
    def service_smf_plain_text_enabled(self, service_smf_plain_text_enabled):
        """
        Sets the service_smf_plain_text_enabled of this MsgVpn.
        Enable or disable plain-text SMF service in the Message VPN. The default value is `true`.

        :param service_smf_plain_text_enabled: The service_smf_plain_text_enabled of this MsgVpn.
        :type: bool
        """

        self._service_smf_plain_text_enabled = service_smf_plain_text_enabled

    @property
    def service_smf_tls_enabled(self):
        """
        Gets the service_smf_tls_enabled of this MsgVpn.
        Enable or disable TLS SMF service for this Message VPN. The default value is `true`.

        :return: The service_smf_tls_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._service_smf_tls_enabled

    @service_smf_tls_enabled.setter
    def service_smf_tls_enabled(self, service_smf_tls_enabled):
        """
        Sets the service_smf_tls_enabled of this MsgVpn.
        Enable or disable TLS SMF service for this Message VPN. The default value is `true`.

        :param service_smf_tls_enabled: The service_smf_tls_enabled of this MsgVpn.
        :type: bool
        """

        self._service_smf_tls_enabled = service_smf_tls_enabled

    @property
    def service_web_max_connection_count(self):
        """
        Gets the service_web_max_connection_count of this MsgVpn.
        The maximum number of web-transport client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :return: The service_web_max_connection_count of this MsgVpn.
        :rtype: int
        """
        return self._service_web_max_connection_count

    @service_web_max_connection_count.setter
    def service_web_max_connection_count(self, service_web_max_connection_count):
        """
        Sets the service_web_max_connection_count of this MsgVpn.
        The maximum number of web-transport client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.

        :param service_web_max_connection_count: The service_web_max_connection_count of this MsgVpn.
        :type: int
        """

        self._service_web_max_connection_count = service_web_max_connection_count

    @property
    def service_web_plain_text_enabled(self):
        """
        Gets the service_web_plain_text_enabled of this MsgVpn.
        Enable or disable plain-text Web Transport service in the Message VPN. The default value is `true`.

        :return: The service_web_plain_text_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._service_web_plain_text_enabled

    @service_web_plain_text_enabled.setter
    def service_web_plain_text_enabled(self, service_web_plain_text_enabled):
        """
        Sets the service_web_plain_text_enabled of this MsgVpn.
        Enable or disable plain-text Web Transport service in the Message VPN. The default value is `true`.

        :param service_web_plain_text_enabled: The service_web_plain_text_enabled of this MsgVpn.
        :type: bool
        """

        self._service_web_plain_text_enabled = service_web_plain_text_enabled

    @property
    def service_web_tls_enabled(self):
        """
        Gets the service_web_tls_enabled of this MsgVpn.
        Enable or disable TLS Web Transport service in the Message VPN. The default value is `true`.

        :return: The service_web_tls_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._service_web_tls_enabled

    @service_web_tls_enabled.setter
    def service_web_tls_enabled(self, service_web_tls_enabled):
        """
        Sets the service_web_tls_enabled of this MsgVpn.
        Enable or disable TLS Web Transport service in the Message VPN. The default value is `true`.

        :param service_web_tls_enabled: The service_web_tls_enabled of this MsgVpn.
        :type: bool
        """

        self._service_web_tls_enabled = service_web_tls_enabled

    @property
    def tls_allow_downgrade_to_plain_text_enabled(self):
        """
        Gets the tls_allow_downgrade_to_plain_text_enabled of this MsgVpn.
        Enable or disable the allowing of TLS SMF clients to downgrade their connections to plain-text connections. Changing this will not affect existing connections. The default value is `false`.

        :return: The tls_allow_downgrade_to_plain_text_enabled of this MsgVpn.
        :rtype: bool
        """
        return self._tls_allow_downgrade_to_plain_text_enabled

    @tls_allow_downgrade_to_plain_text_enabled.setter
    def tls_allow_downgrade_to_plain_text_enabled(self, tls_allow_downgrade_to_plain_text_enabled):
        """
        Sets the tls_allow_downgrade_to_plain_text_enabled of this MsgVpn.
        Enable or disable the allowing of TLS SMF clients to downgrade their connections to plain-text connections. Changing this will not affect existing connections. The default value is `false`.

        :param tls_allow_downgrade_to_plain_text_enabled: The tls_allow_downgrade_to_plain_text_enabled of this MsgVpn.
        :type: bool
        """

        self._tls_allow_downgrade_to_plain_text_enabled = tls_allow_downgrade_to_plain_text_enabled

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
