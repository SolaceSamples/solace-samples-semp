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


class MsgVpnRestDeliveryPointRestConsumer(object):
    """
    NOTE: This class is auto generated by the swagger code generator program.
    Do not edit the class manually.
    """
    def __init__(self, authentication_http_basic_password=None, authentication_http_basic_username=None, authentication_scheme=None, enabled=None, local_interface=None, max_post_wait_time=None, msg_vpn_name=None, outgoing_connection_count=None, remote_host=None, remote_port=None, rest_consumer_name=None, rest_delivery_point_name=None, retry_delay=None, tls_cipher_suite_list=None, tls_enabled=None):
        """
        MsgVpnRestDeliveryPointRestConsumer - a model defined in Swagger

        :param dict swaggerTypes: The key is attribute name
                                  and the value is attribute type.
        :param dict attributeMap: The key is attribute name
                                  and the value is json key in definition.
        """
        self.swagger_types = {
            'authentication_http_basic_password': 'str',
            'authentication_http_basic_username': 'str',
            'authentication_scheme': 'str',
            'enabled': 'bool',
            'local_interface': 'str',
            'max_post_wait_time': 'int',
            'msg_vpn_name': 'str',
            'outgoing_connection_count': 'int',
            'remote_host': 'str',
            'remote_port': 'int',
            'rest_consumer_name': 'str',
            'rest_delivery_point_name': 'str',
            'retry_delay': 'int',
            'tls_cipher_suite_list': 'str',
            'tls_enabled': 'bool'
        }

        self.attribute_map = {
            'authentication_http_basic_password': 'authenticationHttpBasicPassword',
            'authentication_http_basic_username': 'authenticationHttpBasicUsername',
            'authentication_scheme': 'authenticationScheme',
            'enabled': 'enabled',
            'local_interface': 'localInterface',
            'max_post_wait_time': 'maxPostWaitTime',
            'msg_vpn_name': 'msgVpnName',
            'outgoing_connection_count': 'outgoingConnectionCount',
            'remote_host': 'remoteHost',
            'remote_port': 'remotePort',
            'rest_consumer_name': 'restConsumerName',
            'rest_delivery_point_name': 'restDeliveryPointName',
            'retry_delay': 'retryDelay',
            'tls_cipher_suite_list': 'tlsCipherSuiteList',
            'tls_enabled': 'tlsEnabled'
        }

        self._authentication_http_basic_password = authentication_http_basic_password
        self._authentication_http_basic_username = authentication_http_basic_username
        self._authentication_scheme = authentication_scheme
        self._enabled = enabled
        self._local_interface = local_interface
        self._max_post_wait_time = max_post_wait_time
        self._msg_vpn_name = msg_vpn_name
        self._outgoing_connection_count = outgoing_connection_count
        self._remote_host = remote_host
        self._remote_port = remote_port
        self._rest_consumer_name = rest_consumer_name
        self._rest_delivery_point_name = rest_delivery_point_name
        self._retry_delay = retry_delay
        self._tls_cipher_suite_list = tls_cipher_suite_list
        self._tls_enabled = tls_enabled

    @property
    def authentication_http_basic_password(self):
        """
        Gets the authentication_http_basic_password of this MsgVpnRestDeliveryPointRestConsumer.
        The password that the REST Consumer will use to login to the rest-host. The default is to have no `authenticationHttpBasicPassword`.

        :return: The authentication_http_basic_password of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: str
        """
        return self._authentication_http_basic_password

    @authentication_http_basic_password.setter
    def authentication_http_basic_password(self, authentication_http_basic_password):
        """
        Sets the authentication_http_basic_password of this MsgVpnRestDeliveryPointRestConsumer.
        The password that the REST Consumer will use to login to the rest-host. The default is to have no `authenticationHttpBasicPassword`.

        :param authentication_http_basic_password: The authentication_http_basic_password of this MsgVpnRestDeliveryPointRestConsumer.
        :type: str
        """

        self._authentication_http_basic_password = authentication_http_basic_password

    @property
    def authentication_http_basic_username(self):
        """
        Gets the authentication_http_basic_username of this MsgVpnRestDeliveryPointRestConsumer.
        The username that the REST Consumer will use to login to the rest-host. Normally a username is only configured when basic authentication is selected for the REST Consumer. The default is to have no `authenticationHttpBasicUsername`.

        :return: The authentication_http_basic_username of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: str
        """
        return self._authentication_http_basic_username

    @authentication_http_basic_username.setter
    def authentication_http_basic_username(self, authentication_http_basic_username):
        """
        Sets the authentication_http_basic_username of this MsgVpnRestDeliveryPointRestConsumer.
        The username that the REST Consumer will use to login to the rest-host. Normally a username is only configured when basic authentication is selected for the REST Consumer. The default is to have no `authenticationHttpBasicUsername`.

        :param authentication_http_basic_username: The authentication_http_basic_username of this MsgVpnRestDeliveryPointRestConsumer.
        :type: str
        """

        self._authentication_http_basic_username = authentication_http_basic_username

    @property
    def authentication_scheme(self):
        """
        Gets the authentication_scheme of this MsgVpnRestDeliveryPointRestConsumer.
        The authentication scheme used by the REST Consumer to login to the rest-host. The client-certificate auth-scheme is only compatible with connections using TLS. The default value is `\"none\"`. The allowed values and their meaning are:      \"none\" - Login with no authentication (login anonymously). This means no authentication is provided to the REST consumer when connecting to it. This is useful for anonymous connections or when no authentication is required by the REST consumer.     \"http-basic\" - Login with simple username / password credentials according to HTTP Basic authentication as per RFC2616.     \"client-certificate\" - Login with client TLS certificate as per RFC5246. 

        :return: The authentication_scheme of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: str
        """
        return self._authentication_scheme

    @authentication_scheme.setter
    def authentication_scheme(self, authentication_scheme):
        """
        Sets the authentication_scheme of this MsgVpnRestDeliveryPointRestConsumer.
        The authentication scheme used by the REST Consumer to login to the rest-host. The client-certificate auth-scheme is only compatible with connections using TLS. The default value is `\"none\"`. The allowed values and their meaning are:      \"none\" - Login with no authentication (login anonymously). This means no authentication is provided to the REST consumer when connecting to it. This is useful for anonymous connections or when no authentication is required by the REST consumer.     \"http-basic\" - Login with simple username / password credentials according to HTTP Basic authentication as per RFC2616.     \"client-certificate\" - Login with client TLS certificate as per RFC5246. 

        :param authentication_scheme: The authentication_scheme of this MsgVpnRestDeliveryPointRestConsumer.
        :type: str
        """
        allowed_values = ["none", "http-basic", "client-certificate"]
        if authentication_scheme not in allowed_values:
            raise ValueError(
                "Invalid value for `authentication_scheme` ({0}), must be one of {1}"
                .format(authentication_scheme, allowed_values)
            )

        self._authentication_scheme = authentication_scheme

    @property
    def enabled(self):
        """
        Gets the enabled of this MsgVpnRestDeliveryPointRestConsumer.
        Enable or disable this REST Consumer. When disabled, no connections are initiated or messages delivered to this particular REST Consumer. The default value is `false`.

        :return: The enabled of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: bool
        """
        return self._enabled

    @enabled.setter
    def enabled(self, enabled):
        """
        Sets the enabled of this MsgVpnRestDeliveryPointRestConsumer.
        Enable or disable this REST Consumer. When disabled, no connections are initiated or messages delivered to this particular REST Consumer. The default value is `false`.

        :param enabled: The enabled of this MsgVpnRestDeliveryPointRestConsumer.
        :type: bool
        """

        self._enabled = enabled

    @property
    def local_interface(self):
        """
        Gets the local_interface of this MsgVpnRestDeliveryPointRestConsumer.
        The interface that will be used for all outgoing connections associated with the given REST consumer. The source IP address used for these connections will always be the IP address associated with the AD-enabled virtual-router for the specified interface. When unspecified the router will automatically choose an interface through which the REST consumer is reachable. The default is to have no `localInterface`.

        :return: The local_interface of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: str
        """
        return self._local_interface

    @local_interface.setter
    def local_interface(self, local_interface):
        """
        Sets the local_interface of this MsgVpnRestDeliveryPointRestConsumer.
        The interface that will be used for all outgoing connections associated with the given REST consumer. The source IP address used for these connections will always be the IP address associated with the AD-enabled virtual-router for the specified interface. When unspecified the router will automatically choose an interface through which the REST consumer is reachable. The default is to have no `localInterface`.

        :param local_interface: The local_interface of this MsgVpnRestDeliveryPointRestConsumer.
        :type: str
        """

        self._local_interface = local_interface

    @property
    def max_post_wait_time(self):
        """
        Gets the max_post_wait_time of this MsgVpnRestDeliveryPointRestConsumer.
        The maximum amount of time (in seconds) that the router will wait for a POST response from the REST Consumer. Once a POST operation has been outstanding for this period of time, the request is considered hung and the TCP connection is reset. If this POST is for a non-persistent message, the message is discarded. If this POST is for a persistent message, then message delivery is re-attempted via another available outgoing connection on any available outgoing connection for that RDP, up to the Max-Delivery-Count on the queue. If this count is exceeded, and the message is DMQ-eligible, then this message is moved to the DMQ, otherwise it is discarded. The default value is `30`.

        :return: The max_post_wait_time of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: int
        """
        return self._max_post_wait_time

    @max_post_wait_time.setter
    def max_post_wait_time(self, max_post_wait_time):
        """
        Sets the max_post_wait_time of this MsgVpnRestDeliveryPointRestConsumer.
        The maximum amount of time (in seconds) that the router will wait for a POST response from the REST Consumer. Once a POST operation has been outstanding for this period of time, the request is considered hung and the TCP connection is reset. If this POST is for a non-persistent message, the message is discarded. If this POST is for a persistent message, then message delivery is re-attempted via another available outgoing connection on any available outgoing connection for that RDP, up to the Max-Delivery-Count on the queue. If this count is exceeded, and the message is DMQ-eligible, then this message is moved to the DMQ, otherwise it is discarded. The default value is `30`.

        :param max_post_wait_time: The max_post_wait_time of this MsgVpnRestDeliveryPointRestConsumer.
        :type: int
        """

        self._max_post_wait_time = max_post_wait_time

    @property
    def msg_vpn_name(self):
        """
        Gets the msg_vpn_name of this MsgVpnRestDeliveryPointRestConsumer.
        The name of the Message VPN.

        :return: The msg_vpn_name of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: str
        """
        return self._msg_vpn_name

    @msg_vpn_name.setter
    def msg_vpn_name(self, msg_vpn_name):
        """
        Sets the msg_vpn_name of this MsgVpnRestDeliveryPointRestConsumer.
        The name of the Message VPN.

        :param msg_vpn_name: The msg_vpn_name of this MsgVpnRestDeliveryPointRestConsumer.
        :type: str
        """

        self._msg_vpn_name = msg_vpn_name

    @property
    def outgoing_connection_count(self):
        """
        Gets the outgoing_connection_count of this MsgVpnRestDeliveryPointRestConsumer.
        The total number of concurrent TCP connections open to this REST Consumer initiated by the router. Multiple connections to a single REST Consumer are typically desirable to increase throughput via concurrency. The more connections, the higher the potential throughput. The default value is `3`.

        :return: The outgoing_connection_count of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: int
        """
        return self._outgoing_connection_count

    @outgoing_connection_count.setter
    def outgoing_connection_count(self, outgoing_connection_count):
        """
        Sets the outgoing_connection_count of this MsgVpnRestDeliveryPointRestConsumer.
        The total number of concurrent TCP connections open to this REST Consumer initiated by the router. Multiple connections to a single REST Consumer are typically desirable to increase throughput via concurrency. The more connections, the higher the potential throughput. The default value is `3`.

        :param outgoing_connection_count: The outgoing_connection_count of this MsgVpnRestDeliveryPointRestConsumer.
        :type: int
        """

        self._outgoing_connection_count = outgoing_connection_count

    @property
    def remote_host(self):
        """
        Gets the remote_host of this MsgVpnRestDeliveryPointRestConsumer.
        The IPv4 address or DNS name to which the router is to connect to deliver messages for this REST Consumer. If the REST Consumer is enabled while the host value is not configured then the REST Consumer has an operational Down state due to the empty host configuration until a usable host value is configured. The default is to have no `remoteHost`.

        :return: The remote_host of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: str
        """
        return self._remote_host

    @remote_host.setter
    def remote_host(self, remote_host):
        """
        Sets the remote_host of this MsgVpnRestDeliveryPointRestConsumer.
        The IPv4 address or DNS name to which the router is to connect to deliver messages for this REST Consumer. If the REST Consumer is enabled while the host value is not configured then the REST Consumer has an operational Down state due to the empty host configuration until a usable host value is configured. The default is to have no `remoteHost`.

        :param remote_host: The remote_host of this MsgVpnRestDeliveryPointRestConsumer.
        :type: str
        """

        self._remote_host = remote_host

    @property
    def remote_port(self):
        """
        Gets the remote_port of this MsgVpnRestDeliveryPointRestConsumer.
        The port associated with the host of the current REST Consumer. The default value is `8080`.

        :return: The remote_port of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: int
        """
        return self._remote_port

    @remote_port.setter
    def remote_port(self, remote_port):
        """
        Sets the remote_port of this MsgVpnRestDeliveryPointRestConsumer.
        The port associated with the host of the current REST Consumer. The default value is `8080`.

        :param remote_port: The remote_port of this MsgVpnRestDeliveryPointRestConsumer.
        :type: int
        """

        self._remote_port = remote_port

    @property
    def rest_consumer_name(self):
        """
        Gets the rest_consumer_name of this MsgVpnRestDeliveryPointRestConsumer.
        An RDP-wide unique name for the REST consumer.

        :return: The rest_consumer_name of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: str
        """
        return self._rest_consumer_name

    @rest_consumer_name.setter
    def rest_consumer_name(self, rest_consumer_name):
        """
        Sets the rest_consumer_name of this MsgVpnRestDeliveryPointRestConsumer.
        An RDP-wide unique name for the REST consumer.

        :param rest_consumer_name: The rest_consumer_name of this MsgVpnRestDeliveryPointRestConsumer.
        :type: str
        """

        self._rest_consumer_name = rest_consumer_name

    @property
    def rest_delivery_point_name(self):
        """
        Gets the rest_delivery_point_name of this MsgVpnRestDeliveryPointRestConsumer.
        A Message VPN-wide unique name for the REST Delivery Point. This name is used to auto-generate a client-username in this Message VPN, which is used by the client for this RDP.

        :return: The rest_delivery_point_name of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: str
        """
        return self._rest_delivery_point_name

    @rest_delivery_point_name.setter
    def rest_delivery_point_name(self, rest_delivery_point_name):
        """
        Sets the rest_delivery_point_name of this MsgVpnRestDeliveryPointRestConsumer.
        A Message VPN-wide unique name for the REST Delivery Point. This name is used to auto-generate a client-username in this Message VPN, which is used by the client for this RDP.

        :param rest_delivery_point_name: The rest_delivery_point_name of this MsgVpnRestDeliveryPointRestConsumer.
        :type: str
        """

        self._rest_delivery_point_name = rest_delivery_point_name

    @property
    def retry_delay(self):
        """
        Gets the retry_delay of this MsgVpnRestDeliveryPointRestConsumer.
        The number of seconds that must pass before retrying a connection. The default value is `3`.

        :return: The retry_delay of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: int
        """
        return self._retry_delay

    @retry_delay.setter
    def retry_delay(self, retry_delay):
        """
        Sets the retry_delay of this MsgVpnRestDeliveryPointRestConsumer.
        The number of seconds that must pass before retrying a connection. The default value is `3`.

        :param retry_delay: The retry_delay of this MsgVpnRestDeliveryPointRestConsumer.
        :type: int
        """

        self._retry_delay = retry_delay

    @property
    def tls_cipher_suite_list(self):
        """
        Gets the tls_cipher_suite_list of this MsgVpnRestDeliveryPointRestConsumer.
        The colon-separated list of cipher-suites the REST Consumer uses in its encrypted connection. All supported suites are included by default, from most-secure to least-secure. The remote server (REST Consumer) should choose the first suite from this list that it supports. The cipher-suite list can only be changed when the REST Consumer is disabled. The default value is `\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:AES256-GCM-SHA384:AES256-SHA256:AES256-SHA:ECDHE-RSA-DES-CBC3-SHA:DES-CBC3-SHA:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:AES128-GCM-SHA256:AES128-SHA256:AES128-SHA\"`.

        :return: The tls_cipher_suite_list of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: str
        """
        return self._tls_cipher_suite_list

    @tls_cipher_suite_list.setter
    def tls_cipher_suite_list(self, tls_cipher_suite_list):
        """
        Sets the tls_cipher_suite_list of this MsgVpnRestDeliveryPointRestConsumer.
        The colon-separated list of cipher-suites the REST Consumer uses in its encrypted connection. All supported suites are included by default, from most-secure to least-secure. The remote server (REST Consumer) should choose the first suite from this list that it supports. The cipher-suite list can only be changed when the REST Consumer is disabled. The default value is `\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:AES256-GCM-SHA384:AES256-SHA256:AES256-SHA:ECDHE-RSA-DES-CBC3-SHA:DES-CBC3-SHA:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:AES128-GCM-SHA256:AES128-SHA256:AES128-SHA\"`.

        :param tls_cipher_suite_list: The tls_cipher_suite_list of this MsgVpnRestDeliveryPointRestConsumer.
        :type: str
        """

        self._tls_cipher_suite_list = tls_cipher_suite_list

    @property
    def tls_enabled(self):
        """
        Gets the tls_enabled of this MsgVpnRestDeliveryPointRestConsumer.
        Enable or disable TLS for the REST Consumer. This may only be done when the REST Consumer is disabled. The default value is `false`.

        :return: The tls_enabled of this MsgVpnRestDeliveryPointRestConsumer.
        :rtype: bool
        """
        return self._tls_enabled

    @tls_enabled.setter
    def tls_enabled(self, tls_enabled):
        """
        Sets the tls_enabled of this MsgVpnRestDeliveryPointRestConsumer.
        Enable or disable TLS for the REST Consumer. This may only be done when the REST Consumer is disabled. The default value is `false`.

        :param tls_enabled: The tls_enabled of this MsgVpnRestDeliveryPointRestConsumer.
        :type: bool
        """

        self._tls_enabled = tls_enabled

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
