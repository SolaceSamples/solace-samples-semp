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


class MsgVpnBridgeRemoteMsgVpn(object):
    """
    NOTE: This class is auto generated by the swagger code generator program.
    Do not edit the class manually.
    """
    def __init__(self, bridge_name=None, bridge_virtual_router=None, client_username=None, compressed_data_enabled=None, connect_order=None, egress_flow_window_size=None, enabled=None, msg_vpn_name=None, password=None, queue_binding=None, remote_msg_vpn_interface=None, remote_msg_vpn_location=None, remote_msg_vpn_name=None, tls_enabled=None, unidirectional_client_profile=None):
        """
        MsgVpnBridgeRemoteMsgVpn - a model defined in Swagger

        :param dict swaggerTypes: The key is attribute name
                                  and the value is attribute type.
        :param dict attributeMap: The key is attribute name
                                  and the value is json key in definition.
        """
        self.swagger_types = {
            'bridge_name': 'str',
            'bridge_virtual_router': 'str',
            'client_username': 'str',
            'compressed_data_enabled': 'bool',
            'connect_order': 'int',
            'egress_flow_window_size': 'int',
            'enabled': 'bool',
            'msg_vpn_name': 'str',
            'password': 'str',
            'queue_binding': 'str',
            'remote_msg_vpn_interface': 'str',
            'remote_msg_vpn_location': 'str',
            'remote_msg_vpn_name': 'str',
            'tls_enabled': 'bool',
            'unidirectional_client_profile': 'str'
        }

        self.attribute_map = {
            'bridge_name': 'bridgeName',
            'bridge_virtual_router': 'bridgeVirtualRouter',
            'client_username': 'clientUsername',
            'compressed_data_enabled': 'compressedDataEnabled',
            'connect_order': 'connectOrder',
            'egress_flow_window_size': 'egressFlowWindowSize',
            'enabled': 'enabled',
            'msg_vpn_name': 'msgVpnName',
            'password': 'password',
            'queue_binding': 'queueBinding',
            'remote_msg_vpn_interface': 'remoteMsgVpnInterface',
            'remote_msg_vpn_location': 'remoteMsgVpnLocation',
            'remote_msg_vpn_name': 'remoteMsgVpnName',
            'tls_enabled': 'tlsEnabled',
            'unidirectional_client_profile': 'unidirectionalClientProfile'
        }

        self._bridge_name = bridge_name
        self._bridge_virtual_router = bridge_virtual_router
        self._client_username = client_username
        self._compressed_data_enabled = compressed_data_enabled
        self._connect_order = connect_order
        self._egress_flow_window_size = egress_flow_window_size
        self._enabled = enabled
        self._msg_vpn_name = msg_vpn_name
        self._password = password
        self._queue_binding = queue_binding
        self._remote_msg_vpn_interface = remote_msg_vpn_interface
        self._remote_msg_vpn_location = remote_msg_vpn_location
        self._remote_msg_vpn_name = remote_msg_vpn_name
        self._tls_enabled = tls_enabled
        self._unidirectional_client_profile = unidirectional_client_profile

    @property
    def bridge_name(self):
        """
        Gets the bridge_name of this MsgVpnBridgeRemoteMsgVpn.
        The name of the Bridge.

        :return: The bridge_name of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: str
        """
        return self._bridge_name

    @bridge_name.setter
    def bridge_name(self, bridge_name):
        """
        Sets the bridge_name of this MsgVpnBridgeRemoteMsgVpn.
        The name of the Bridge.

        :param bridge_name: The bridge_name of this MsgVpnBridgeRemoteMsgVpn.
        :type: str
        """

        self._bridge_name = bridge_name

    @property
    def bridge_virtual_router(self):
        """
        Gets the bridge_virtual_router of this MsgVpnBridgeRemoteMsgVpn.
        The virtual-router of the Bridge. The allowed values and their meaning are:      \"primary\" - Bridge belongs to the primary virtual-router.     \"backup\" - Bridge belongs to the backup virtual-router. 

        :return: The bridge_virtual_router of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: str
        """
        return self._bridge_virtual_router

    @bridge_virtual_router.setter
    def bridge_virtual_router(self, bridge_virtual_router):
        """
        Sets the bridge_virtual_router of this MsgVpnBridgeRemoteMsgVpn.
        The virtual-router of the Bridge. The allowed values and their meaning are:      \"primary\" - Bridge belongs to the primary virtual-router.     \"backup\" - Bridge belongs to the backup virtual-router. 

        :param bridge_virtual_router: The bridge_virtual_router of this MsgVpnBridgeRemoteMsgVpn.
        :type: str
        """
        allowed_values = ["primary", "backup"]
        if bridge_virtual_router not in allowed_values:
            raise ValueError(
                "Invalid value for `bridge_virtual_router` ({0}), must be one of {1}"
                .format(bridge_virtual_router, allowed_values)
            )

        self._bridge_virtual_router = bridge_virtual_router

    @property
    def client_username(self):
        """
        Gets the client_username of this MsgVpnBridgeRemoteMsgVpn.
        The client username the bridge uses to login to the Remote Message VPN. This per Remote Message VPN value overrides the value provided for the bridge overall. The default is to have no `clientUsername`.

        :return: The client_username of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: str
        """
        return self._client_username

    @client_username.setter
    def client_username(self, client_username):
        """
        Sets the client_username of this MsgVpnBridgeRemoteMsgVpn.
        The client username the bridge uses to login to the Remote Message VPN. This per Remote Message VPN value overrides the value provided for the bridge overall. The default is to have no `clientUsername`.

        :param client_username: The client_username of this MsgVpnBridgeRemoteMsgVpn.
        :type: str
        """

        self._client_username = client_username

    @property
    def compressed_data_enabled(self):
        """
        Gets the compressed_data_enabled of this MsgVpnBridgeRemoteMsgVpn.
        Enable or disable data compression for the Remote Message VPN. The default value is `false`.

        :return: The compressed_data_enabled of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: bool
        """
        return self._compressed_data_enabled

    @compressed_data_enabled.setter
    def compressed_data_enabled(self, compressed_data_enabled):
        """
        Sets the compressed_data_enabled of this MsgVpnBridgeRemoteMsgVpn.
        Enable or disable data compression for the Remote Message VPN. The default value is `false`.

        :param compressed_data_enabled: The compressed_data_enabled of this MsgVpnBridgeRemoteMsgVpn.
        :type: bool
        """

        self._compressed_data_enabled = compressed_data_enabled

    @property
    def connect_order(self):
        """
        Gets the connect_order of this MsgVpnBridgeRemoteMsgVpn.
        The order in which attempts to connect to different Message VPN hosts are attempted, or the preference given to incoming connections from remote routers, from `1` (highest priority) to `4` (lowest priority). The default value is `4`.

        :return: The connect_order of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: int
        """
        return self._connect_order

    @connect_order.setter
    def connect_order(self, connect_order):
        """
        Sets the connect_order of this MsgVpnBridgeRemoteMsgVpn.
        The order in which attempts to connect to different Message VPN hosts are attempted, or the preference given to incoming connections from remote routers, from `1` (highest priority) to `4` (lowest priority). The default value is `4`.

        :param connect_order: The connect_order of this MsgVpnBridgeRemoteMsgVpn.
        :type: int
        """

        self._connect_order = connect_order

    @property
    def egress_flow_window_size(self):
        """
        Gets the egress_flow_window_size of this MsgVpnBridgeRemoteMsgVpn.
        The window size indicates how many outstanding guaranteed messages can be sent over the Remote Message VPN connection before acknowledgement is received by the sender. The default value is `255`.

        :return: The egress_flow_window_size of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: int
        """
        return self._egress_flow_window_size

    @egress_flow_window_size.setter
    def egress_flow_window_size(self, egress_flow_window_size):
        """
        Sets the egress_flow_window_size of this MsgVpnBridgeRemoteMsgVpn.
        The window size indicates how many outstanding guaranteed messages can be sent over the Remote Message VPN connection before acknowledgement is received by the sender. The default value is `255`.

        :param egress_flow_window_size: The egress_flow_window_size of this MsgVpnBridgeRemoteMsgVpn.
        :type: int
        """

        self._egress_flow_window_size = egress_flow_window_size

    @property
    def enabled(self):
        """
        Gets the enabled of this MsgVpnBridgeRemoteMsgVpn.
        Enable or disable the Remote Message VPN. The default value is `false`.

        :return: The enabled of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: bool
        """
        return self._enabled

    @enabled.setter
    def enabled(self, enabled):
        """
        Sets the enabled of this MsgVpnBridgeRemoteMsgVpn.
        Enable or disable the Remote Message VPN. The default value is `false`.

        :param enabled: The enabled of this MsgVpnBridgeRemoteMsgVpn.
        :type: bool
        """

        self._enabled = enabled

    @property
    def msg_vpn_name(self):
        """
        Gets the msg_vpn_name of this MsgVpnBridgeRemoteMsgVpn.
        The name of the Message VPN.

        :return: The msg_vpn_name of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: str
        """
        return self._msg_vpn_name

    @msg_vpn_name.setter
    def msg_vpn_name(self, msg_vpn_name):
        """
        Sets the msg_vpn_name of this MsgVpnBridgeRemoteMsgVpn.
        The name of the Message VPN.

        :param msg_vpn_name: The msg_vpn_name of this MsgVpnBridgeRemoteMsgVpn.
        :type: str
        """

        self._msg_vpn_name = msg_vpn_name

    @property
    def password(self):
        """
        Gets the password of this MsgVpnBridgeRemoteMsgVpn.
        The password for the client username the bridge uses to login to the Remote Message VPN. The default is to have no `password`.

        :return: The password of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: str
        """
        return self._password

    @password.setter
    def password(self, password):
        """
        Sets the password of this MsgVpnBridgeRemoteMsgVpn.
        The password for the client username the bridge uses to login to the Remote Message VPN. The default is to have no `password`.

        :param password: The password of this MsgVpnBridgeRemoteMsgVpn.
        :type: str
        """

        self._password = password

    @property
    def queue_binding(self):
        """
        Gets the queue_binding of this MsgVpnBridgeRemoteMsgVpn.
        The queue binding of the bridge for this Remote Message VPN. The bridge attempts to bind to that queue over the bridge link once the link has been established, or immediately if it already is established. The queue must be configured on the remote router when the bridge connection is established. If the bind fails an event log is generated which includes the reason for the failure. The default is to have no `queueBinding`.

        :return: The queue_binding of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: str
        """
        return self._queue_binding

    @queue_binding.setter
    def queue_binding(self, queue_binding):
        """
        Sets the queue_binding of this MsgVpnBridgeRemoteMsgVpn.
        The queue binding of the bridge for this Remote Message VPN. The bridge attempts to bind to that queue over the bridge link once the link has been established, or immediately if it already is established. The queue must be configured on the remote router when the bridge connection is established. If the bind fails an event log is generated which includes the reason for the failure. The default is to have no `queueBinding`.

        :param queue_binding: The queue_binding of this MsgVpnBridgeRemoteMsgVpn.
        :type: str
        """

        self._queue_binding = queue_binding

    @property
    def remote_msg_vpn_interface(self):
        """
        Gets the remote_msg_vpn_interface of this MsgVpnBridgeRemoteMsgVpn.
        The interface on the local router through which to access the Remote Message VPN. If not provided (recommended) then an interface will be chosen automatically based on routing tables. If an interface is provided, `remoteMsgVpnLocation` must be either a hostname or IP Address, not a virtual router-name.

        :return: The remote_msg_vpn_interface of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: str
        """
        return self._remote_msg_vpn_interface

    @remote_msg_vpn_interface.setter
    def remote_msg_vpn_interface(self, remote_msg_vpn_interface):
        """
        Sets the remote_msg_vpn_interface of this MsgVpnBridgeRemoteMsgVpn.
        The interface on the local router through which to access the Remote Message VPN. If not provided (recommended) then an interface will be chosen automatically based on routing tables. If an interface is provided, `remoteMsgVpnLocation` must be either a hostname or IP Address, not a virtual router-name.

        :param remote_msg_vpn_interface: The remote_msg_vpn_interface of this MsgVpnBridgeRemoteMsgVpn.
        :type: str
        """

        self._remote_msg_vpn_interface = remote_msg_vpn_interface

    @property
    def remote_msg_vpn_location(self):
        """
        Gets the remote_msg_vpn_location of this MsgVpnBridgeRemoteMsgVpn.
        The location of the Remote Message VPN. This may be given as either a hostname (resolvable via DNS), IP Address, or virtual router-name (starts with 'v:'). If specified as a hostname or IP Address, a port must be specified as well.

        :return: The remote_msg_vpn_location of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: str
        """
        return self._remote_msg_vpn_location

    @remote_msg_vpn_location.setter
    def remote_msg_vpn_location(self, remote_msg_vpn_location):
        """
        Sets the remote_msg_vpn_location of this MsgVpnBridgeRemoteMsgVpn.
        The location of the Remote Message VPN. This may be given as either a hostname (resolvable via DNS), IP Address, or virtual router-name (starts with 'v:'). If specified as a hostname or IP Address, a port must be specified as well.

        :param remote_msg_vpn_location: The remote_msg_vpn_location of this MsgVpnBridgeRemoteMsgVpn.
        :type: str
        """

        self._remote_msg_vpn_location = remote_msg_vpn_location

    @property
    def remote_msg_vpn_name(self):
        """
        Gets the remote_msg_vpn_name of this MsgVpnBridgeRemoteMsgVpn.
        The name of the Remote Message VPN.

        :return: The remote_msg_vpn_name of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: str
        """
        return self._remote_msg_vpn_name

    @remote_msg_vpn_name.setter
    def remote_msg_vpn_name(self, remote_msg_vpn_name):
        """
        Sets the remote_msg_vpn_name of this MsgVpnBridgeRemoteMsgVpn.
        The name of the Remote Message VPN.

        :param remote_msg_vpn_name: The remote_msg_vpn_name of this MsgVpnBridgeRemoteMsgVpn.
        :type: str
        """

        self._remote_msg_vpn_name = remote_msg_vpn_name

    @property
    def tls_enabled(self):
        """
        Gets the tls_enabled of this MsgVpnBridgeRemoteMsgVpn.
        Enable or disable TLS for the Remote Message VPN. The default value is `false`.

        :return: The tls_enabled of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: bool
        """
        return self._tls_enabled

    @tls_enabled.setter
    def tls_enabled(self, tls_enabled):
        """
        Sets the tls_enabled of this MsgVpnBridgeRemoteMsgVpn.
        Enable or disable TLS for the Remote Message VPN. The default value is `false`.

        :param tls_enabled: The tls_enabled of this MsgVpnBridgeRemoteMsgVpn.
        :type: bool
        """

        self._tls_enabled = tls_enabled

    @property
    def unidirectional_client_profile(self):
        """
        Gets the unidirectional_client_profile of this MsgVpnBridgeRemoteMsgVpn.
        The client-profile for the unidirectional bridge for the Remote Message VPN. The client-profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is `\"#client-profile\"`.

        :return: The unidirectional_client_profile of this MsgVpnBridgeRemoteMsgVpn.
        :rtype: str
        """
        return self._unidirectional_client_profile

    @unidirectional_client_profile.setter
    def unidirectional_client_profile(self, unidirectional_client_profile):
        """
        Sets the unidirectional_client_profile of this MsgVpnBridgeRemoteMsgVpn.
        The client-profile for the unidirectional bridge for the Remote Message VPN. The client-profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is `\"#client-profile\"`.

        :param unidirectional_client_profile: The unidirectional_client_profile of this MsgVpnBridgeRemoteMsgVpn.
        :type: str
        """

        self._unidirectional_client_profile = unidirectional_client_profile

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
