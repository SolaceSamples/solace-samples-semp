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


class MsgVpnQueue(object):
    """
    NOTE: This class is auto generated by the swagger code generator program.
    Do not edit the class manually.
    """
    def __init__(self, access_type=None, consumer_ack_propagation_enabled=None, egress_enabled=None, event_bind_count_threshold=None, event_msg_spool_usage_threshold=None, event_reject_low_priority_msg_limit_threshold=None, ingress_enabled=None, max_bind_count=None, max_delivered_unacked_msgs_per_flow=None, max_msg_size=None, max_msg_spool_usage=None, max_redelivery_count=None, max_ttl=None, msg_vpn_name=None, owner=None, permission=None, queue_name=None, reject_low_priority_msg_enabled=None, reject_low_priority_msg_limit=None, reject_msg_to_sender_on_discard_behavior=None, respect_ttl_enabled=None):
        """
        MsgVpnQueue - a model defined in Swagger

        :param dict swaggerTypes: The key is attribute name
                                  and the value is attribute type.
        :param dict attributeMap: The key is attribute name
                                  and the value is json key in definition.
        """
        self.swagger_types = {
            'access_type': 'str',
            'consumer_ack_propagation_enabled': 'bool',
            'egress_enabled': 'bool',
            'event_bind_count_threshold': 'EventThreshold',
            'event_msg_spool_usage_threshold': 'EventThreshold',
            'event_reject_low_priority_msg_limit_threshold': 'EventThreshold',
            'ingress_enabled': 'bool',
            'max_bind_count': 'int',
            'max_delivered_unacked_msgs_per_flow': 'int',
            'max_msg_size': 'int',
            'max_msg_spool_usage': 'int',
            'max_redelivery_count': 'int',
            'max_ttl': 'int',
            'msg_vpn_name': 'str',
            'owner': 'str',
            'permission': 'str',
            'queue_name': 'str',
            'reject_low_priority_msg_enabled': 'bool',
            'reject_low_priority_msg_limit': 'int',
            'reject_msg_to_sender_on_discard_behavior': 'str',
            'respect_ttl_enabled': 'bool'
        }

        self.attribute_map = {
            'access_type': 'accessType',
            'consumer_ack_propagation_enabled': 'consumerAckPropagationEnabled',
            'egress_enabled': 'egressEnabled',
            'event_bind_count_threshold': 'eventBindCountThreshold',
            'event_msg_spool_usage_threshold': 'eventMsgSpoolUsageThreshold',
            'event_reject_low_priority_msg_limit_threshold': 'eventRejectLowPriorityMsgLimitThreshold',
            'ingress_enabled': 'ingressEnabled',
            'max_bind_count': 'maxBindCount',
            'max_delivered_unacked_msgs_per_flow': 'maxDeliveredUnackedMsgsPerFlow',
            'max_msg_size': 'maxMsgSize',
            'max_msg_spool_usage': 'maxMsgSpoolUsage',
            'max_redelivery_count': 'maxRedeliveryCount',
            'max_ttl': 'maxTtl',
            'msg_vpn_name': 'msgVpnName',
            'owner': 'owner',
            'permission': 'permission',
            'queue_name': 'queueName',
            'reject_low_priority_msg_enabled': 'rejectLowPriorityMsgEnabled',
            'reject_low_priority_msg_limit': 'rejectLowPriorityMsgLimit',
            'reject_msg_to_sender_on_discard_behavior': 'rejectMsgToSenderOnDiscardBehavior',
            'respect_ttl_enabled': 'respectTtlEnabled'
        }

        self._access_type = access_type
        self._consumer_ack_propagation_enabled = consumer_ack_propagation_enabled
        self._egress_enabled = egress_enabled
        self._event_bind_count_threshold = event_bind_count_threshold
        self._event_msg_spool_usage_threshold = event_msg_spool_usage_threshold
        self._event_reject_low_priority_msg_limit_threshold = event_reject_low_priority_msg_limit_threshold
        self._ingress_enabled = ingress_enabled
        self._max_bind_count = max_bind_count
        self._max_delivered_unacked_msgs_per_flow = max_delivered_unacked_msgs_per_flow
        self._max_msg_size = max_msg_size
        self._max_msg_spool_usage = max_msg_spool_usage
        self._max_redelivery_count = max_redelivery_count
        self._max_ttl = max_ttl
        self._msg_vpn_name = msg_vpn_name
        self._owner = owner
        self._permission = permission
        self._queue_name = queue_name
        self._reject_low_priority_msg_enabled = reject_low_priority_msg_enabled
        self._reject_low_priority_msg_limit = reject_low_priority_msg_limit
        self._reject_msg_to_sender_on_discard_behavior = reject_msg_to_sender_on_discard_behavior
        self._respect_ttl_enabled = respect_ttl_enabled

    @property
    def access_type(self):
        """
        Gets the access_type of this MsgVpnQueue.
        The queue access type of either exclusive or non-exclusive. The default value is `\"exclusive\"`. The allowed values and their meaning are:      \"exclusive\" - Exclusive delivery of messages to first bound client.     \"non-exclusive\" - Non-exclusive delivery of messages to all bound clients. 

        :return: The access_type of this MsgVpnQueue.
        :rtype: str
        """
        return self._access_type

    @access_type.setter
    def access_type(self, access_type):
        """
        Sets the access_type of this MsgVpnQueue.
        The queue access type of either exclusive or non-exclusive. The default value is `\"exclusive\"`. The allowed values and their meaning are:      \"exclusive\" - Exclusive delivery of messages to first bound client.     \"non-exclusive\" - Non-exclusive delivery of messages to all bound clients. 

        :param access_type: The access_type of this MsgVpnQueue.
        :type: str
        """
        allowed_values = ["exclusive", "non-exclusive"]
        if access_type not in allowed_values:
            raise ValueError(
                "Invalid value for `access_type` ({0}), must be one of {1}"
                .format(access_type, allowed_values)
            )

        self._access_type = access_type

    @property
    def consumer_ack_propagation_enabled(self):
        """
        Gets the consumer_ack_propagation_enabled of this MsgVpnQueue.
        Enable or disable the propagation of consumer acks received on the active replication Message VPN to the standby replication Message VPN. The default value is `true`.

        :return: The consumer_ack_propagation_enabled of this MsgVpnQueue.
        :rtype: bool
        """
        return self._consumer_ack_propagation_enabled

    @consumer_ack_propagation_enabled.setter
    def consumer_ack_propagation_enabled(self, consumer_ack_propagation_enabled):
        """
        Sets the consumer_ack_propagation_enabled of this MsgVpnQueue.
        Enable or disable the propagation of consumer acks received on the active replication Message VPN to the standby replication Message VPN. The default value is `true`.

        :param consumer_ack_propagation_enabled: The consumer_ack_propagation_enabled of this MsgVpnQueue.
        :type: bool
        """

        self._consumer_ack_propagation_enabled = consumer_ack_propagation_enabled

    @property
    def egress_enabled(self):
        """
        Gets the egress_enabled of this MsgVpnQueue.
        Enable or disable the flow of messages from a Queue. The default value is `false`.

        :return: The egress_enabled of this MsgVpnQueue.
        :rtype: bool
        """
        return self._egress_enabled

    @egress_enabled.setter
    def egress_enabled(self, egress_enabled):
        """
        Sets the egress_enabled of this MsgVpnQueue.
        Enable or disable the flow of messages from a Queue. The default value is `false`.

        :param egress_enabled: The egress_enabled of this MsgVpnQueue.
        :type: bool
        """

        self._egress_enabled = egress_enabled

    @property
    def event_bind_count_threshold(self):
        """
        Gets the event_bind_count_threshold of this MsgVpnQueue.


        :return: The event_bind_count_threshold of this MsgVpnQueue.
        :rtype: EventThreshold
        """
        return self._event_bind_count_threshold

    @event_bind_count_threshold.setter
    def event_bind_count_threshold(self, event_bind_count_threshold):
        """
        Sets the event_bind_count_threshold of this MsgVpnQueue.


        :param event_bind_count_threshold: The event_bind_count_threshold of this MsgVpnQueue.
        :type: EventThreshold
        """

        self._event_bind_count_threshold = event_bind_count_threshold

    @property
    def event_msg_spool_usage_threshold(self):
        """
        Gets the event_msg_spool_usage_threshold of this MsgVpnQueue.


        :return: The event_msg_spool_usage_threshold of this MsgVpnQueue.
        :rtype: EventThreshold
        """
        return self._event_msg_spool_usage_threshold

    @event_msg_spool_usage_threshold.setter
    def event_msg_spool_usage_threshold(self, event_msg_spool_usage_threshold):
        """
        Sets the event_msg_spool_usage_threshold of this MsgVpnQueue.


        :param event_msg_spool_usage_threshold: The event_msg_spool_usage_threshold of this MsgVpnQueue.
        :type: EventThreshold
        """

        self._event_msg_spool_usage_threshold = event_msg_spool_usage_threshold

    @property
    def event_reject_low_priority_msg_limit_threshold(self):
        """
        Gets the event_reject_low_priority_msg_limit_threshold of this MsgVpnQueue.


        :return: The event_reject_low_priority_msg_limit_threshold of this MsgVpnQueue.
        :rtype: EventThreshold
        """
        return self._event_reject_low_priority_msg_limit_threshold

    @event_reject_low_priority_msg_limit_threshold.setter
    def event_reject_low_priority_msg_limit_threshold(self, event_reject_low_priority_msg_limit_threshold):
        """
        Sets the event_reject_low_priority_msg_limit_threshold of this MsgVpnQueue.


        :param event_reject_low_priority_msg_limit_threshold: The event_reject_low_priority_msg_limit_threshold of this MsgVpnQueue.
        :type: EventThreshold
        """

        self._event_reject_low_priority_msg_limit_threshold = event_reject_low_priority_msg_limit_threshold

    @property
    def ingress_enabled(self):
        """
        Gets the ingress_enabled of this MsgVpnQueue.
        Enable or disable the flow of messages to a Queue. The default value is `false`.

        :return: The ingress_enabled of this MsgVpnQueue.
        :rtype: bool
        """
        return self._ingress_enabled

    @ingress_enabled.setter
    def ingress_enabled(self, ingress_enabled):
        """
        Sets the ingress_enabled of this MsgVpnQueue.
        Enable or disable the flow of messages to a Queue. The default value is `false`.

        :param ingress_enabled: The ingress_enabled of this MsgVpnQueue.
        :type: bool
        """

        self._ingress_enabled = ingress_enabled

    @property
    def max_bind_count(self):
        """
        Gets the max_bind_count of this MsgVpnQueue.
        The maximum number of times clients can bind to a given Queue. The default value is `1000`.

        :return: The max_bind_count of this MsgVpnQueue.
        :rtype: int
        """
        return self._max_bind_count

    @max_bind_count.setter
    def max_bind_count(self, max_bind_count):
        """
        Sets the max_bind_count of this MsgVpnQueue.
        The maximum number of times clients can bind to a given Queue. The default value is `1000`.

        :param max_bind_count: The max_bind_count of this MsgVpnQueue.
        :type: int
        """

        self._max_bind_count = max_bind_count

    @property
    def max_delivered_unacked_msgs_per_flow(self):
        """
        Gets the max_delivered_unacked_msgs_per_flow of this MsgVpnQueue.
        The max messages delivered but not acknowledged per flow for this Queue. The default is the max value supported by the hardware.

        :return: The max_delivered_unacked_msgs_per_flow of this MsgVpnQueue.
        :rtype: int
        """
        return self._max_delivered_unacked_msgs_per_flow

    @max_delivered_unacked_msgs_per_flow.setter
    def max_delivered_unacked_msgs_per_flow(self, max_delivered_unacked_msgs_per_flow):
        """
        Sets the max_delivered_unacked_msgs_per_flow of this MsgVpnQueue.
        The max messages delivered but not acknowledged per flow for this Queue. The default is the max value supported by the hardware.

        :param max_delivered_unacked_msgs_per_flow: The max_delivered_unacked_msgs_per_flow of this MsgVpnQueue.
        :type: int
        """

        self._max_delivered_unacked_msgs_per_flow = max_delivered_unacked_msgs_per_flow

    @property
    def max_msg_size(self):
        """
        Gets the max_msg_size of this MsgVpnQueue.
        The max message size (in bytes) allowed in this Queue. The default value is `10000000`.

        :return: The max_msg_size of this MsgVpnQueue.
        :rtype: int
        """
        return self._max_msg_size

    @max_msg_size.setter
    def max_msg_size(self, max_msg_size):
        """
        Sets the max_msg_size of this MsgVpnQueue.
        The max message size (in bytes) allowed in this Queue. The default value is `10000000`.

        :param max_msg_size: The max_msg_size of this MsgVpnQueue.
        :type: int
        """

        self._max_msg_size = max_msg_size

    @property
    def max_msg_spool_usage(self):
        """
        Gets the max_msg_spool_usage of this MsgVpnQueue.
        The max spool usage (in MB) of this Queue. Setting the value to 0 enables the last-value-queue feature and disables quota checking. The default varies by platform.

        :return: The max_msg_spool_usage of this MsgVpnQueue.
        :rtype: int
        """
        return self._max_msg_spool_usage

    @max_msg_spool_usage.setter
    def max_msg_spool_usage(self, max_msg_spool_usage):
        """
        Sets the max_msg_spool_usage of this MsgVpnQueue.
        The max spool usage (in MB) of this Queue. Setting the value to 0 enables the last-value-queue feature and disables quota checking. The default varies by platform.

        :param max_msg_spool_usage: The max_msg_spool_usage of this MsgVpnQueue.
        :type: int
        """

        self._max_msg_spool_usage = max_msg_spool_usage

    @property
    def max_redelivery_count(self):
        """
        Gets the max_redelivery_count of this MsgVpnQueue.
        The maximum number of times the Queue will attempt redelivery of a given message prior to it being discarded or moved to the #DEAD_MSG_QUEUE. A value of 0 means to retry forever. The default value is `0`.

        :return: The max_redelivery_count of this MsgVpnQueue.
        :rtype: int
        """
        return self._max_redelivery_count

    @max_redelivery_count.setter
    def max_redelivery_count(self, max_redelivery_count):
        """
        Sets the max_redelivery_count of this MsgVpnQueue.
        The maximum number of times the Queue will attempt redelivery of a given message prior to it being discarded or moved to the #DEAD_MSG_QUEUE. A value of 0 means to retry forever. The default value is `0`.

        :param max_redelivery_count: The max_redelivery_count of this MsgVpnQueue.
        :type: int
        """

        self._max_redelivery_count = max_redelivery_count

    @property
    def max_ttl(self):
        """
        Gets the max_ttl of this MsgVpnQueue.
        The maximum number of seconds that a message can stay in the Queue when `respectTtlEnabled` is `true`. A message will expire according to the lesser of the TTL in the message (assigned by the publisher) and the `maxTtl` configured on the Queue. `maxTtl` is a 32-bit integer value from 1 to 4294967295 representing the expiry time in seconds. A `maxTtl` of `0` disables this feature. The default value is `0`.

        :return: The max_ttl of this MsgVpnQueue.
        :rtype: int
        """
        return self._max_ttl

    @max_ttl.setter
    def max_ttl(self, max_ttl):
        """
        Sets the max_ttl of this MsgVpnQueue.
        The maximum number of seconds that a message can stay in the Queue when `respectTtlEnabled` is `true`. A message will expire according to the lesser of the TTL in the message (assigned by the publisher) and the `maxTtl` configured on the Queue. `maxTtl` is a 32-bit integer value from 1 to 4294967295 representing the expiry time in seconds. A `maxTtl` of `0` disables this feature. The default value is `0`.

        :param max_ttl: The max_ttl of this MsgVpnQueue.
        :type: int
        """

        self._max_ttl = max_ttl

    @property
    def msg_vpn_name(self):
        """
        Gets the msg_vpn_name of this MsgVpnQueue.
        The name of the Message VPN.

        :return: The msg_vpn_name of this MsgVpnQueue.
        :rtype: str
        """
        return self._msg_vpn_name

    @msg_vpn_name.setter
    def msg_vpn_name(self, msg_vpn_name):
        """
        Sets the msg_vpn_name of this MsgVpnQueue.
        The name of the Message VPN.

        :param msg_vpn_name: The msg_vpn_name of this MsgVpnQueue.
        :type: str
        """

        self._msg_vpn_name = msg_vpn_name

    @property
    def owner(self):
        """
        Gets the owner of this MsgVpnQueue.
        The Client Username which owns the Queue. The default is to have no `owner`.

        :return: The owner of this MsgVpnQueue.
        :rtype: str
        """
        return self._owner

    @owner.setter
    def owner(self, owner):
        """
        Sets the owner of this MsgVpnQueue.
        The Client Username which owns the Queue. The default is to have no `owner`.

        :param owner: The owner of this MsgVpnQueue.
        :type: str
        """

        self._owner = owner

    @property
    def permission(self):
        """
        Gets the permission of this MsgVpnQueue.
        Permission level for users of the Queue, excluding the owner. The default value is `\"no-access\"`. The allowed values and their meaning are:      \"no-access\" - Disallows all access.     \"read-only\" - Read-only access to the messages in the Queue.     \"consume\" - Consume (read and remove) messages in the Queue.     \"modify-topic\" - Consume messages or modify the topic/selector of the Queue.     \"delete\" - Consume messages, modify the topic/selector or delete the Queue altogether. 

        :return: The permission of this MsgVpnQueue.
        :rtype: str
        """
        return self._permission

    @permission.setter
    def permission(self, permission):
        """
        Sets the permission of this MsgVpnQueue.
        Permission level for users of the Queue, excluding the owner. The default value is `\"no-access\"`. The allowed values and their meaning are:      \"no-access\" - Disallows all access.     \"read-only\" - Read-only access to the messages in the Queue.     \"consume\" - Consume (read and remove) messages in the Queue.     \"modify-topic\" - Consume messages or modify the topic/selector of the Queue.     \"delete\" - Consume messages, modify the topic/selector or delete the Queue altogether. 

        :param permission: The permission of this MsgVpnQueue.
        :type: str
        """
        allowed_values = ["no-access", "read-only", "consume", "modify-topic", "delete"]
        if permission not in allowed_values:
            raise ValueError(
                "Invalid value for `permission` ({0}), must be one of {1}"
                .format(permission, allowed_values)
            )

        self._permission = permission

    @property
    def queue_name(self):
        """
        Gets the queue_name of this MsgVpnQueue.
        The name of the Queue.

        :return: The queue_name of this MsgVpnQueue.
        :rtype: str
        """
        return self._queue_name

    @queue_name.setter
    def queue_name(self, queue_name):
        """
        Sets the queue_name of this MsgVpnQueue.
        The name of the Queue.

        :param queue_name: The queue_name of this MsgVpnQueue.
        :type: str
        """

        self._queue_name = queue_name

    @property
    def reject_low_priority_msg_enabled(self):
        """
        Gets the reject_low_priority_msg_enabled of this MsgVpnQueue.
        Enable or disable if low priority messages are subject to `rejectLowPriorityMsgLimit` checking. This may only be enabled if `rejectMsgToSenderOnDiscardBehavior` does not have a value of `\"never\"`. The default value is `false`.

        :return: The reject_low_priority_msg_enabled of this MsgVpnQueue.
        :rtype: bool
        """
        return self._reject_low_priority_msg_enabled

    @reject_low_priority_msg_enabled.setter
    def reject_low_priority_msg_enabled(self, reject_low_priority_msg_enabled):
        """
        Sets the reject_low_priority_msg_enabled of this MsgVpnQueue.
        Enable or disable if low priority messages are subject to `rejectLowPriorityMsgLimit` checking. This may only be enabled if `rejectMsgToSenderOnDiscardBehavior` does not have a value of `\"never\"`. The default value is `false`.

        :param reject_low_priority_msg_enabled: The reject_low_priority_msg_enabled of this MsgVpnQueue.
        :type: bool
        """

        self._reject_low_priority_msg_enabled = reject_low_priority_msg_enabled

    @property
    def reject_low_priority_msg_limit(self):
        """
        Gets the reject_low_priority_msg_limit of this MsgVpnQueue.
        The number of messages of any priority in the Queue above which low priority messages are not admitted but higher priority messages are allowed. The default value is `0`.

        :return: The reject_low_priority_msg_limit of this MsgVpnQueue.
        :rtype: int
        """
        return self._reject_low_priority_msg_limit

    @reject_low_priority_msg_limit.setter
    def reject_low_priority_msg_limit(self, reject_low_priority_msg_limit):
        """
        Sets the reject_low_priority_msg_limit of this MsgVpnQueue.
        The number of messages of any priority in the Queue above which low priority messages are not admitted but higher priority messages are allowed. The default value is `0`.

        :param reject_low_priority_msg_limit: The reject_low_priority_msg_limit of this MsgVpnQueue.
        :type: int
        """

        self._reject_low_priority_msg_limit = reject_low_priority_msg_limit

    @property
    def reject_msg_to_sender_on_discard_behavior(self):
        """
        Gets the reject_msg_to_sender_on_discard_behavior of this MsgVpnQueue.
        The circumstances under which a nack is sent to the client on discards. Note that nacks cause the message to not be delivered to any destination and transacted-session commits to fail. This attribute may only have a value of `\"never\"` if `rejectLowPriorityMsgEnabled` is disabled. The default value is `\"when-queue-enabled\"`. The allowed values and their meaning are:      \"when-queue-enabled\" - Message discards result in nacks being returned to the sending client, except if the discard reason is that the queue is disabled.     \"never\" - Message discards never result in nacks being returned to the sending client.  Available since 2.1.0.

        :return: The reject_msg_to_sender_on_discard_behavior of this MsgVpnQueue.
        :rtype: str
        """
        return self._reject_msg_to_sender_on_discard_behavior

    @reject_msg_to_sender_on_discard_behavior.setter
    def reject_msg_to_sender_on_discard_behavior(self, reject_msg_to_sender_on_discard_behavior):
        """
        Sets the reject_msg_to_sender_on_discard_behavior of this MsgVpnQueue.
        The circumstances under which a nack is sent to the client on discards. Note that nacks cause the message to not be delivered to any destination and transacted-session commits to fail. This attribute may only have a value of `\"never\"` if `rejectLowPriorityMsgEnabled` is disabled. The default value is `\"when-queue-enabled\"`. The allowed values and their meaning are:      \"when-queue-enabled\" - Message discards result in nacks being returned to the sending client, except if the discard reason is that the queue is disabled.     \"never\" - Message discards never result in nacks being returned to the sending client.  Available since 2.1.0.

        :param reject_msg_to_sender_on_discard_behavior: The reject_msg_to_sender_on_discard_behavior of this MsgVpnQueue.
        :type: str
        """
        allowed_values = ["when-queue-enabled", "never"]
        if reject_msg_to_sender_on_discard_behavior not in allowed_values:
            raise ValueError(
                "Invalid value for `reject_msg_to_sender_on_discard_behavior` ({0}), must be one of {1}"
                .format(reject_msg_to_sender_on_discard_behavior, allowed_values)
            )

        self._reject_msg_to_sender_on_discard_behavior = reject_msg_to_sender_on_discard_behavior

    @property
    def respect_ttl_enabled(self):
        """
        Gets the respect_ttl_enabled of this MsgVpnQueue.
        Enable or disable the respecting of TTL. If enabled, then messages contained in the Queue are checked for expiry. If expired, the message is removed from the Queue and either discarded or a copy of the message placed in the #DEAD_MSG_QUEUE. The default value is `false`.

        :return: The respect_ttl_enabled of this MsgVpnQueue.
        :rtype: bool
        """
        return self._respect_ttl_enabled

    @respect_ttl_enabled.setter
    def respect_ttl_enabled(self, respect_ttl_enabled):
        """
        Sets the respect_ttl_enabled of this MsgVpnQueue.
        Enable or disable the respecting of TTL. If enabled, then messages contained in the Queue are checked for expiry. If expired, the message is removed from the Queue and either discarded or a copy of the message placed in the #DEAD_MSG_QUEUE. The default value is `false`.

        :param respect_ttl_enabled: The respect_ttl_enabled of this MsgVpnQueue.
        :type: bool
        """

        self._respect_ttl_enabled = respect_ttl_enabled

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
