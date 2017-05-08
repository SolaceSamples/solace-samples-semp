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


class MsgVpnBridge(object):
    """
    NOTE: This class is auto generated by the swagger code generator program.
    Do not edit the class manually.
    """
    def __init__(self, bridge_name=None, bridge_virtual_router=None, enabled=None, max_ttl=None, msg_vpn_name=None, remote_authentication_basic_client_username=None, remote_authentication_basic_password=None, remote_authentication_scheme=None, remote_connection_retry_count=None, remote_connection_retry_delay=None, remote_deliver_to_one_priority=None, tls_cipher_suite_list=None):
        """
        MsgVpnBridge - a model defined in Swagger

        :param dict swaggerTypes: The key is attribute name
                                  and the value is attribute type.
        :param dict attributeMap: The key is attribute name
                                  and the value is json key in definition.
        """
        self.swagger_types = {
            'bridge_name': 'str',
            'bridge_virtual_router': 'str',
            'enabled': 'bool',
            'max_ttl': 'int',
            'msg_vpn_name': 'str',
            'remote_authentication_basic_client_username': 'str',
            'remote_authentication_basic_password': 'str',
            'remote_authentication_scheme': 'str',
            'remote_connection_retry_count': 'int',
            'remote_connection_retry_delay': 'int',
            'remote_deliver_to_one_priority': 'str',
            'tls_cipher_suite_list': 'str'
        }

        self.attribute_map = {
            'bridge_name': 'bridgeName',
            'bridge_virtual_router': 'bridgeVirtualRouter',
            'enabled': 'enabled',
            'max_ttl': 'maxTtl',
            'msg_vpn_name': 'msgVpnName',
            'remote_authentication_basic_client_username': 'remoteAuthenticationBasicClientUsername',
            'remote_authentication_basic_password': 'remoteAuthenticationBasicPassword',
            'remote_authentication_scheme': 'remoteAuthenticationScheme',
            'remote_connection_retry_count': 'remoteConnectionRetryCount',
            'remote_connection_retry_delay': 'remoteConnectionRetryDelay',
            'remote_deliver_to_one_priority': 'remoteDeliverToOnePriority',
            'tls_cipher_suite_list': 'tlsCipherSuiteList'
        }

        self._bridge_name = bridge_name
        self._bridge_virtual_router = bridge_virtual_router
        self._enabled = enabled
        self._max_ttl = max_ttl
        self._msg_vpn_name = msg_vpn_name
        self._remote_authentication_basic_client_username = remote_authentication_basic_client_username
        self._remote_authentication_basic_password = remote_authentication_basic_password
        self._remote_authentication_scheme = remote_authentication_scheme
        self._remote_connection_retry_count = remote_connection_retry_count
        self._remote_connection_retry_delay = remote_connection_retry_delay
        self._remote_deliver_to_one_priority = remote_deliver_to_one_priority
        self._tls_cipher_suite_list = tls_cipher_suite_list

    @property
    def bridge_name(self):
        """
        Gets the bridge_name of this MsgVpnBridge.
        The name of the Bridge.

        :return: The bridge_name of this MsgVpnBridge.
        :rtype: str
        """
        return self._bridge_name

    @bridge_name.setter
    def bridge_name(self, bridge_name):
        """
        Sets the bridge_name of this MsgVpnBridge.
        The name of the Bridge.

        :param bridge_name: The bridge_name of this MsgVpnBridge.
        :type: str
        """

        self._bridge_name = bridge_name

    @property
    def bridge_virtual_router(self):
        """
        Gets the bridge_virtual_router of this MsgVpnBridge.
        The virtual-router of the Bridge. The allowed values and their meaning are:      \"primary\" - Bridge belongs to the primary virtual-router.     \"backup\" - Bridge belongs to the backup virtual-router. 

        :return: The bridge_virtual_router of this MsgVpnBridge.
        :rtype: str
        """
        return self._bridge_virtual_router

    @bridge_virtual_router.setter
    def bridge_virtual_router(self, bridge_virtual_router):
        """
        Sets the bridge_virtual_router of this MsgVpnBridge.
        The virtual-router of the Bridge. The allowed values and their meaning are:      \"primary\" - Bridge belongs to the primary virtual-router.     \"backup\" - Bridge belongs to the backup virtual-router. 

        :param bridge_virtual_router: The bridge_virtual_router of this MsgVpnBridge.
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
    def enabled(self):
        """
        Gets the enabled of this MsgVpnBridge.
        Enable or disable the bridge. The default value is `false`.

        :return: The enabled of this MsgVpnBridge.
        :rtype: bool
        """
        return self._enabled

    @enabled.setter
    def enabled(self, enabled):
        """
        Sets the enabled of this MsgVpnBridge.
        Enable or disable the bridge. The default value is `false`.

        :param enabled: The enabled of this MsgVpnBridge.
        :type: bool
        """

        self._enabled = enabled

    @property
    def max_ttl(self):
        """
        Gets the max_ttl of this MsgVpnBridge.
        The max-ttl value for the bridge, in hops. When a bridge is sending a message to the remote router, the TTL value for the message becomes the lower of its current TTL value or this value. The default value is `8`.

        :return: The max_ttl of this MsgVpnBridge.
        :rtype: int
        """
        return self._max_ttl

    @max_ttl.setter
    def max_ttl(self, max_ttl):
        """
        Sets the max_ttl of this MsgVpnBridge.
        The max-ttl value for the bridge, in hops. When a bridge is sending a message to the remote router, the TTL value for the message becomes the lower of its current TTL value or this value. The default value is `8`.

        :param max_ttl: The max_ttl of this MsgVpnBridge.
        :type: int
        """

        self._max_ttl = max_ttl

    @property
    def msg_vpn_name(self):
        """
        Gets the msg_vpn_name of this MsgVpnBridge.
        The name of the Message VPN.

        :return: The msg_vpn_name of this MsgVpnBridge.
        :rtype: str
        """
        return self._msg_vpn_name

    @msg_vpn_name.setter
    def msg_vpn_name(self, msg_vpn_name):
        """
        Sets the msg_vpn_name of this MsgVpnBridge.
        The name of the Message VPN.

        :param msg_vpn_name: The msg_vpn_name of this MsgVpnBridge.
        :type: str
        """

        self._msg_vpn_name = msg_vpn_name

    @property
    def remote_authentication_basic_client_username(self):
        """
        Gets the remote_authentication_basic_client_username of this MsgVpnBridge.
        The client username the bridge uses to login to the Remote Message VPN. The default is to have no `remoteAuthenticationBasicClientUsername`.

        :return: The remote_authentication_basic_client_username of this MsgVpnBridge.
        :rtype: str
        """
        return self._remote_authentication_basic_client_username

    @remote_authentication_basic_client_username.setter
    def remote_authentication_basic_client_username(self, remote_authentication_basic_client_username):
        """
        Sets the remote_authentication_basic_client_username of this MsgVpnBridge.
        The client username the bridge uses to login to the Remote Message VPN. The default is to have no `remoteAuthenticationBasicClientUsername`.

        :param remote_authentication_basic_client_username: The remote_authentication_basic_client_username of this MsgVpnBridge.
        :type: str
        """

        self._remote_authentication_basic_client_username = remote_authentication_basic_client_username

    @property
    def remote_authentication_basic_password(self):
        """
        Gets the remote_authentication_basic_password of this MsgVpnBridge.
        The password for the client username the bridge uses to login to the Remote Message VPN. The default is to have no `remoteAuthenticationBasicPassword`.

        :return: The remote_authentication_basic_password of this MsgVpnBridge.
        :rtype: str
        """
        return self._remote_authentication_basic_password

    @remote_authentication_basic_password.setter
    def remote_authentication_basic_password(self, remote_authentication_basic_password):
        """
        Sets the remote_authentication_basic_password of this MsgVpnBridge.
        The password for the client username the bridge uses to login to the Remote Message VPN. The default is to have no `remoteAuthenticationBasicPassword`.

        :param remote_authentication_basic_password: The remote_authentication_basic_password of this MsgVpnBridge.
        :type: str
        """

        self._remote_authentication_basic_password = remote_authentication_basic_password

    @property
    def remote_authentication_scheme(self):
        """
        Gets the remote_authentication_scheme of this MsgVpnBridge.
        The authentication scheme for the Remote Message VPN. The default value is `\"basic\"`. The allowed values and their meaning are:      \"basic\" - Basic Authentication Scheme (via username and password).     \"client-certificate\" - Client Certificate Authentication Scheme (via certificate-file). 

        :return: The remote_authentication_scheme of this MsgVpnBridge.
        :rtype: str
        """
        return self._remote_authentication_scheme

    @remote_authentication_scheme.setter
    def remote_authentication_scheme(self, remote_authentication_scheme):
        """
        Sets the remote_authentication_scheme of this MsgVpnBridge.
        The authentication scheme for the Remote Message VPN. The default value is `\"basic\"`. The allowed values and their meaning are:      \"basic\" - Basic Authentication Scheme (via username and password).     \"client-certificate\" - Client Certificate Authentication Scheme (via certificate-file). 

        :param remote_authentication_scheme: The remote_authentication_scheme of this MsgVpnBridge.
        :type: str
        """
        allowed_values = ["basic", "client-certificate"]
        if remote_authentication_scheme not in allowed_values:
            raise ValueError(
                "Invalid value for `remote_authentication_scheme` ({0}), must be one of {1}"
                .format(remote_authentication_scheme, allowed_values)
            )

        self._remote_authentication_scheme = remote_authentication_scheme

    @property
    def remote_connection_retry_count(self):
        """
        Gets the remote_connection_retry_count of this MsgVpnBridge.
        The number of retries that are attempted for a router name before the next remote router alternative is attempted. The default value is `0`.

        :return: The remote_connection_retry_count of this MsgVpnBridge.
        :rtype: int
        """
        return self._remote_connection_retry_count

    @remote_connection_retry_count.setter
    def remote_connection_retry_count(self, remote_connection_retry_count):
        """
        Sets the remote_connection_retry_count of this MsgVpnBridge.
        The number of retries that are attempted for a router name before the next remote router alternative is attempted. The default value is `0`.

        :param remote_connection_retry_count: The remote_connection_retry_count of this MsgVpnBridge.
        :type: int
        """

        self._remote_connection_retry_count = remote_connection_retry_count

    @property
    def remote_connection_retry_delay(self):
        """
        Gets the remote_connection_retry_delay of this MsgVpnBridge.
        The number of seconds that must pass before retrying a connection. The default value is `3`.

        :return: The remote_connection_retry_delay of this MsgVpnBridge.
        :rtype: int
        """
        return self._remote_connection_retry_delay

    @remote_connection_retry_delay.setter
    def remote_connection_retry_delay(self, remote_connection_retry_delay):
        """
        Sets the remote_connection_retry_delay of this MsgVpnBridge.
        The number of seconds that must pass before retrying a connection. The default value is `3`.

        :param remote_connection_retry_delay: The remote_connection_retry_delay of this MsgVpnBridge.
        :type: int
        """

        self._remote_connection_retry_delay = remote_connection_retry_delay

    @property
    def remote_deliver_to_one_priority(self):
        """
        Gets the remote_deliver_to_one_priority of this MsgVpnBridge.
        The deliver-to-one priority for the bridge used on the remote router. The default value is `\"p1\"`. The allowed values and their meaning are:      \"p1\" - Priority 1 (highest).     \"p2\" - Priority 2.     \"p3\" - Priority 3.     \"p4\" - Priority 4 (lowest).     \"da\" - Deliver Always. 

        :return: The remote_deliver_to_one_priority of this MsgVpnBridge.
        :rtype: str
        """
        return self._remote_deliver_to_one_priority

    @remote_deliver_to_one_priority.setter
    def remote_deliver_to_one_priority(self, remote_deliver_to_one_priority):
        """
        Sets the remote_deliver_to_one_priority of this MsgVpnBridge.
        The deliver-to-one priority for the bridge used on the remote router. The default value is `\"p1\"`. The allowed values and their meaning are:      \"p1\" - Priority 1 (highest).     \"p2\" - Priority 2.     \"p3\" - Priority 3.     \"p4\" - Priority 4 (lowest).     \"da\" - Deliver Always. 

        :param remote_deliver_to_one_priority: The remote_deliver_to_one_priority of this MsgVpnBridge.
        :type: str
        """
        allowed_values = ["p1", "p2", "p3", "p4", "da"]
        if remote_deliver_to_one_priority not in allowed_values:
            raise ValueError(
                "Invalid value for `remote_deliver_to_one_priority` ({0}), must be one of {1}"
                .format(remote_deliver_to_one_priority, allowed_values)
            )

        self._remote_deliver_to_one_priority = remote_deliver_to_one_priority

    @property
    def tls_cipher_suite_list(self):
        """
        Gets the tls_cipher_suite_list of this MsgVpnBridge.
        The colon-separated list of of cipher suites for the TLS authentication mechanism. The suite selected will be the first suite in the list that is supported by the remote router. The default value is `\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:AES256-GCM-SHA384:AES256-SHA256:AES256-SHA:ECDHE-RSA-DES-CBC3-SHA:DES-CBC3-SHA:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:AES128-GCM-SHA256:AES128-SHA256:AES128-SHA\"`.

        :return: The tls_cipher_suite_list of this MsgVpnBridge.
        :rtype: str
        """
        return self._tls_cipher_suite_list

    @tls_cipher_suite_list.setter
    def tls_cipher_suite_list(self, tls_cipher_suite_list):
        """
        Sets the tls_cipher_suite_list of this MsgVpnBridge.
        The colon-separated list of of cipher suites for the TLS authentication mechanism. The suite selected will be the first suite in the list that is supported by the remote router. The default value is `\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:AES256-GCM-SHA384:AES256-SHA256:AES256-SHA:ECDHE-RSA-DES-CBC3-SHA:DES-CBC3-SHA:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:AES128-GCM-SHA256:AES128-SHA256:AES128-SHA\"`.

        :param tls_cipher_suite_list: The tls_cipher_suite_list of this MsgVpnBridge.
        :type: str
        """

        self._tls_cipher_suite_list = tls_cipher_suite_list

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
