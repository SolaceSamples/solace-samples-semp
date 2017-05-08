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


class MsgVpnClientUsername(object):
    """
    NOTE: This class is auto generated by the swagger code generator program.
    Do not edit the class manually.
    """
    def __init__(self, acl_profile_name=None, client_profile_name=None, client_username=None, enabled=None, guaranteed_endpoint_permission_override_enabled=None, msg_vpn_name=None, password=None, subscription_manager_enabled=None):
        """
        MsgVpnClientUsername - a model defined in Swagger

        :param dict swaggerTypes: The key is attribute name
                                  and the value is attribute type.
        :param dict attributeMap: The key is attribute name
                                  and the value is json key in definition.
        """
        self.swagger_types = {
            'acl_profile_name': 'str',
            'client_profile_name': 'str',
            'client_username': 'str',
            'enabled': 'bool',
            'guaranteed_endpoint_permission_override_enabled': 'bool',
            'msg_vpn_name': 'str',
            'password': 'str',
            'subscription_manager_enabled': 'bool'
        }

        self.attribute_map = {
            'acl_profile_name': 'aclProfileName',
            'client_profile_name': 'clientProfileName',
            'client_username': 'clientUsername',
            'enabled': 'enabled',
            'guaranteed_endpoint_permission_override_enabled': 'guaranteedEndpointPermissionOverrideEnabled',
            'msg_vpn_name': 'msgVpnName',
            'password': 'password',
            'subscription_manager_enabled': 'subscriptionManagerEnabled'
        }

        self._acl_profile_name = acl_profile_name
        self._client_profile_name = client_profile_name
        self._client_username = client_username
        self._enabled = enabled
        self._guaranteed_endpoint_permission_override_enabled = guaranteed_endpoint_permission_override_enabled
        self._msg_vpn_name = msg_vpn_name
        self._password = password
        self._subscription_manager_enabled = subscription_manager_enabled

    @property
    def acl_profile_name(self):
        """
        Gets the acl_profile_name of this MsgVpnClientUsername.
        The acl-profile of this client-username. The default value is `\"default\"`.

        :return: The acl_profile_name of this MsgVpnClientUsername.
        :rtype: str
        """
        return self._acl_profile_name

    @acl_profile_name.setter
    def acl_profile_name(self, acl_profile_name):
        """
        Sets the acl_profile_name of this MsgVpnClientUsername.
        The acl-profile of this client-username. The default value is `\"default\"`.

        :param acl_profile_name: The acl_profile_name of this MsgVpnClientUsername.
        :type: str
        """

        self._acl_profile_name = acl_profile_name

    @property
    def client_profile_name(self):
        """
        Gets the client_profile_name of this MsgVpnClientUsername.
        The client-profile of this client-username. The default value is `\"default\"`.

        :return: The client_profile_name of this MsgVpnClientUsername.
        :rtype: str
        """
        return self._client_profile_name

    @client_profile_name.setter
    def client_profile_name(self, client_profile_name):
        """
        Sets the client_profile_name of this MsgVpnClientUsername.
        The client-profile of this client-username. The default value is `\"default\"`.

        :param client_profile_name: The client_profile_name of this MsgVpnClientUsername.
        :type: str
        """

        self._client_profile_name = client_profile_name

    @property
    def client_username(self):
        """
        Gets the client_username of this MsgVpnClientUsername.
        The name of the Client Username.

        :return: The client_username of this MsgVpnClientUsername.
        :rtype: str
        """
        return self._client_username

    @client_username.setter
    def client_username(self, client_username):
        """
        Sets the client_username of this MsgVpnClientUsername.
        The name of the Client Username.

        :param client_username: The client_username of this MsgVpnClientUsername.
        :type: str
        """

        self._client_username = client_username

    @property
    def enabled(self):
        """
        Gets the enabled of this MsgVpnClientUsername.
        Enables or disables a client-username. When disabled all clients currently connected referencing this client username are disconnected. The default value is `false`.

        :return: The enabled of this MsgVpnClientUsername.
        :rtype: bool
        """
        return self._enabled

    @enabled.setter
    def enabled(self, enabled):
        """
        Sets the enabled of this MsgVpnClientUsername.
        Enables or disables a client-username. When disabled all clients currently connected referencing this client username are disconnected. The default value is `false`.

        :param enabled: The enabled of this MsgVpnClientUsername.
        :type: bool
        """

        self._enabled = enabled

    @property
    def guaranteed_endpoint_permission_override_enabled(self):
        """
        Gets the guaranteed_endpoint_permission_override_enabled of this MsgVpnClientUsername.
        Enables or disables guaranteed endpoint permission override for a client-username. When enabled all guaranteed endpoints may be accessed, modified or deleted with the same permission as the owner. The default value is `false`.

        :return: The guaranteed_endpoint_permission_override_enabled of this MsgVpnClientUsername.
        :rtype: bool
        """
        return self._guaranteed_endpoint_permission_override_enabled

    @guaranteed_endpoint_permission_override_enabled.setter
    def guaranteed_endpoint_permission_override_enabled(self, guaranteed_endpoint_permission_override_enabled):
        """
        Sets the guaranteed_endpoint_permission_override_enabled of this MsgVpnClientUsername.
        Enables or disables guaranteed endpoint permission override for a client-username. When enabled all guaranteed endpoints may be accessed, modified or deleted with the same permission as the owner. The default value is `false`.

        :param guaranteed_endpoint_permission_override_enabled: The guaranteed_endpoint_permission_override_enabled of this MsgVpnClientUsername.
        :type: bool
        """

        self._guaranteed_endpoint_permission_override_enabled = guaranteed_endpoint_permission_override_enabled

    @property
    def msg_vpn_name(self):
        """
        Gets the msg_vpn_name of this MsgVpnClientUsername.
        The name of the Message VPN.

        :return: The msg_vpn_name of this MsgVpnClientUsername.
        :rtype: str
        """
        return self._msg_vpn_name

    @msg_vpn_name.setter
    def msg_vpn_name(self, msg_vpn_name):
        """
        Sets the msg_vpn_name of this MsgVpnClientUsername.
        The name of the Message VPN.

        :param msg_vpn_name: The msg_vpn_name of this MsgVpnClientUsername.
        :type: str
        """

        self._msg_vpn_name = msg_vpn_name

    @property
    def password(self):
        """
        Gets the password of this MsgVpnClientUsername.
        The password of this client-username for internal authentication. The default is to have no `password`.

        :return: The password of this MsgVpnClientUsername.
        :rtype: str
        """
        return self._password

    @password.setter
    def password(self, password):
        """
        Sets the password of this MsgVpnClientUsername.
        The password of this client-username for internal authentication. The default is to have no `password`.

        :param password: The password of this MsgVpnClientUsername.
        :type: str
        """

        self._password = password

    @property
    def subscription_manager_enabled(self):
        """
        Gets the subscription_manager_enabled of this MsgVpnClientUsername.
        Enables or disables subscription management capability. This is the ability to manage subscriptions on behalf of other client names. The default value is `false`.

        :return: The subscription_manager_enabled of this MsgVpnClientUsername.
        :rtype: bool
        """
        return self._subscription_manager_enabled

    @subscription_manager_enabled.setter
    def subscription_manager_enabled(self, subscription_manager_enabled):
        """
        Sets the subscription_manager_enabled of this MsgVpnClientUsername.
        Enables or disables subscription management capability. This is the ability to manage subscriptions on behalf of other client names. The default value is `false`.

        :param subscription_manager_enabled: The subscription_manager_enabled of this MsgVpnClientUsername.
        :type: bool
        """

        self._subscription_manager_enabled = subscription_manager_enabled

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
