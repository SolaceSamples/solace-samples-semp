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

from __future__ import absolute_import

# import models into model package
from .event_threshold import EventThreshold
from .event_threshold_by_percent import EventThresholdByPercent
from .event_threshold_by_value import EventThresholdByValue
from .msg_vpn import MsgVpn
from .msg_vpn_acl_profile import MsgVpnAclProfile
from .msg_vpn_acl_profile_client_connect_exception import MsgVpnAclProfileClientConnectException
from .msg_vpn_acl_profile_client_connect_exception_links import MsgVpnAclProfileClientConnectExceptionLinks
from .msg_vpn_acl_profile_client_connect_exception_response import MsgVpnAclProfileClientConnectExceptionResponse
from .msg_vpn_acl_profile_client_connect_exceptions_response import MsgVpnAclProfileClientConnectExceptionsResponse
from .msg_vpn_acl_profile_links import MsgVpnAclProfileLinks
from .msg_vpn_acl_profile_publish_exception import MsgVpnAclProfilePublishException
from .msg_vpn_acl_profile_publish_exception_links import MsgVpnAclProfilePublishExceptionLinks
from .msg_vpn_acl_profile_publish_exception_response import MsgVpnAclProfilePublishExceptionResponse
from .msg_vpn_acl_profile_publish_exceptions_response import MsgVpnAclProfilePublishExceptionsResponse
from .msg_vpn_acl_profile_response import MsgVpnAclProfileResponse
from .msg_vpn_acl_profile_subscribe_exception import MsgVpnAclProfileSubscribeException
from .msg_vpn_acl_profile_subscribe_exception_links import MsgVpnAclProfileSubscribeExceptionLinks
from .msg_vpn_acl_profile_subscribe_exception_response import MsgVpnAclProfileSubscribeExceptionResponse
from .msg_vpn_acl_profile_subscribe_exceptions_response import MsgVpnAclProfileSubscribeExceptionsResponse
from .msg_vpn_acl_profiles_response import MsgVpnAclProfilesResponse
from .msg_vpn_authorization_group import MsgVpnAuthorizationGroup
from .msg_vpn_authorization_group_links import MsgVpnAuthorizationGroupLinks
from .msg_vpn_authorization_group_response import MsgVpnAuthorizationGroupResponse
from .msg_vpn_authorization_groups_response import MsgVpnAuthorizationGroupsResponse
from .msg_vpn_bridge import MsgVpnBridge
from .msg_vpn_bridge_links import MsgVpnBridgeLinks
from .msg_vpn_bridge_remote_msg_vpn import MsgVpnBridgeRemoteMsgVpn
from .msg_vpn_bridge_remote_msg_vpn_links import MsgVpnBridgeRemoteMsgVpnLinks
from .msg_vpn_bridge_remote_msg_vpn_response import MsgVpnBridgeRemoteMsgVpnResponse
from .msg_vpn_bridge_remote_msg_vpns_response import MsgVpnBridgeRemoteMsgVpnsResponse
from .msg_vpn_bridge_remote_subscription import MsgVpnBridgeRemoteSubscription
from .msg_vpn_bridge_remote_subscription_links import MsgVpnBridgeRemoteSubscriptionLinks
from .msg_vpn_bridge_remote_subscription_response import MsgVpnBridgeRemoteSubscriptionResponse
from .msg_vpn_bridge_remote_subscriptions_response import MsgVpnBridgeRemoteSubscriptionsResponse
from .msg_vpn_bridge_response import MsgVpnBridgeResponse
from .msg_vpn_bridge_tls_trusted_common_name import MsgVpnBridgeTlsTrustedCommonName
from .msg_vpn_bridge_tls_trusted_common_name_links import MsgVpnBridgeTlsTrustedCommonNameLinks
from .msg_vpn_bridge_tls_trusted_common_name_response import MsgVpnBridgeTlsTrustedCommonNameResponse
from .msg_vpn_bridge_tls_trusted_common_names_response import MsgVpnBridgeTlsTrustedCommonNamesResponse
from .msg_vpn_bridges_response import MsgVpnBridgesResponse
from .msg_vpn_client_profile import MsgVpnClientProfile
from .msg_vpn_client_profile_links import MsgVpnClientProfileLinks
from .msg_vpn_client_profile_response import MsgVpnClientProfileResponse
from .msg_vpn_client_profiles_response import MsgVpnClientProfilesResponse
from .msg_vpn_client_username import MsgVpnClientUsername
from .msg_vpn_client_username_links import MsgVpnClientUsernameLinks
from .msg_vpn_client_username_response import MsgVpnClientUsernameResponse
from .msg_vpn_client_usernames_response import MsgVpnClientUsernamesResponse
from .msg_vpn_links import MsgVpnLinks
from .msg_vpn_mqtt_session import MsgVpnMqttSession
from .msg_vpn_mqtt_session_links import MsgVpnMqttSessionLinks
from .msg_vpn_mqtt_session_response import MsgVpnMqttSessionResponse
from .msg_vpn_mqtt_session_subscription import MsgVpnMqttSessionSubscription
from .msg_vpn_mqtt_session_subscription_links import MsgVpnMqttSessionSubscriptionLinks
from .msg_vpn_mqtt_session_subscription_response import MsgVpnMqttSessionSubscriptionResponse
from .msg_vpn_mqtt_session_subscriptions_response import MsgVpnMqttSessionSubscriptionsResponse
from .msg_vpn_mqtt_sessions_response import MsgVpnMqttSessionsResponse
from .msg_vpn_queue import MsgVpnQueue
from .msg_vpn_queue_links import MsgVpnQueueLinks
from .msg_vpn_queue_response import MsgVpnQueueResponse
from .msg_vpn_queue_subscription import MsgVpnQueueSubscription
from .msg_vpn_queue_subscription_links import MsgVpnQueueSubscriptionLinks
from .msg_vpn_queue_subscription_response import MsgVpnQueueSubscriptionResponse
from .msg_vpn_queue_subscriptions_response import MsgVpnQueueSubscriptionsResponse
from .msg_vpn_queues_response import MsgVpnQueuesResponse
from .msg_vpn_replicated_topic import MsgVpnReplicatedTopic
from .msg_vpn_replicated_topic_links import MsgVpnReplicatedTopicLinks
from .msg_vpn_replicated_topic_response import MsgVpnReplicatedTopicResponse
from .msg_vpn_replicated_topics_response import MsgVpnReplicatedTopicsResponse
from .msg_vpn_response import MsgVpnResponse
from .msg_vpn_rest_delivery_point import MsgVpnRestDeliveryPoint
from .msg_vpn_rest_delivery_point_links import MsgVpnRestDeliveryPointLinks
from .msg_vpn_rest_delivery_point_queue_binding import MsgVpnRestDeliveryPointQueueBinding
from .msg_vpn_rest_delivery_point_queue_binding_links import MsgVpnRestDeliveryPointQueueBindingLinks
from .msg_vpn_rest_delivery_point_queue_binding_response import MsgVpnRestDeliveryPointQueueBindingResponse
from .msg_vpn_rest_delivery_point_queue_bindings_response import MsgVpnRestDeliveryPointQueueBindingsResponse
from .msg_vpn_rest_delivery_point_response import MsgVpnRestDeliveryPointResponse
from .msg_vpn_rest_delivery_point_rest_consumer import MsgVpnRestDeliveryPointRestConsumer
from .msg_vpn_rest_delivery_point_rest_consumer_links import MsgVpnRestDeliveryPointRestConsumerLinks
from .msg_vpn_rest_delivery_point_rest_consumer_response import MsgVpnRestDeliveryPointRestConsumerResponse
from .msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name import MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName
from .msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_links import MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameLinks
from .msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_response import MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse
from .msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names_response import MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse
from .msg_vpn_rest_delivery_point_rest_consumers_response import MsgVpnRestDeliveryPointRestConsumersResponse
from .msg_vpn_rest_delivery_points_response import MsgVpnRestDeliveryPointsResponse
from .msg_vpn_sequenced_topic import MsgVpnSequencedTopic
from .msg_vpn_sequenced_topic_links import MsgVpnSequencedTopicLinks
from .msg_vpn_sequenced_topic_response import MsgVpnSequencedTopicResponse
from .msg_vpn_sequenced_topics_response import MsgVpnSequencedTopicsResponse
from .msg_vpn_topic_endpoint import MsgVpnTopicEndpoint
from .msg_vpn_topic_endpoint_links import MsgVpnTopicEndpointLinks
from .msg_vpn_topic_endpoint_response import MsgVpnTopicEndpointResponse
from .msg_vpn_topic_endpoints_response import MsgVpnTopicEndpointsResponse
from .msg_vpns_response import MsgVpnsResponse
from .semp_error import SempError
from .semp_meta import SempMeta
from .semp_meta_only_response import SempMetaOnlyResponse
from .semp_paging import SempPaging
from .semp_request import SempRequest
from .system_information import SystemInformation
from .system_information_links import SystemInformationLinks
from .system_information_response import SystemInformationResponse
