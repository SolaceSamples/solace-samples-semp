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

import sys
import os
import re

# python 2 and python 3 compatibility library
from six import iteritems

from ..configuration import Configuration
from ..api_client import ApiClient


class RestDeliveryPointApi(object):
    """
    NOTE: This class is auto generated by the swagger code generator program.
    Do not edit the class manually.
    Ref: https://github.com/swagger-api/swagger-codegen
    """

    def __init__(self, api_client=None):
        config = Configuration()
        if api_client:
            self.api_client = api_client
        else:
            if not config.api_client:
                config.api_client = ApiClient()
            self.api_client = config.api_client

    def create_msg_vpn_rest_delivery_point(self, msg_vpn_name, body, **kwargs):
        """
        Creates a REST Delivery Point object.
        Creates a REST Delivery Point object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_rest_delivery_point(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnRestDeliveryPoint body: The REST Delivery Point object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def create_msg_vpn_rest_delivery_point_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Creates a REST Delivery Point object.
        Creates a REST Delivery Point object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnRestDeliveryPoint body: The REST Delivery Point object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_rest_delivery_point" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_rest_delivery_point`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_rest_delivery_point`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None
        if 'body' in params:
            body_params = params['body']

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'POST',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_rest_delivery_point_queue_binding(self, msg_vpn_name, rest_delivery_point_name, body, **kwargs):
        """
        Creates a Queue Binding object.
        Creates a Queue Binding object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueBindingName|x|x||| restDeliveryPointName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param MsgVpnRestDeliveryPointQueueBinding body: The Queue Binding object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointQueueBindingResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, body, **kwargs)
            return data

    def create_msg_vpn_rest_delivery_point_queue_binding_with_http_info(self, msg_vpn_name, rest_delivery_point_name, body, **kwargs):
        """
        Creates a Queue Binding object.
        Creates a Queue Binding object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueBindingName|x|x||| restDeliveryPointName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param MsgVpnRestDeliveryPointQueueBinding body: The Queue Binding object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointQueueBindingResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_rest_delivery_point_queue_binding" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `create_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_rest_delivery_point_queue_binding`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None
        if 'body' in params:
            body_params = params['body']

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'POST',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointQueueBindingResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_rest_delivery_point_rest_consumer(self, msg_vpn_name, rest_delivery_point_name, body, **kwargs):
        """
        Creates a REST Consumer object.
        Creates a REST Consumer object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword||||x| msgVpnName|x||x|| restConsumerName|x|x||| restDeliveryPointName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param MsgVpnRestDeliveryPointRestConsumer body: The REST Consumer object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, body, **kwargs)
            return data

    def create_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(self, msg_vpn_name, rest_delivery_point_name, body, **kwargs):
        """
        Creates a REST Consumer object.
        Creates a REST Consumer object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword||||x| msgVpnName|x||x|| restConsumerName|x|x||| restDeliveryPointName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param MsgVpnRestDeliveryPointRestConsumer body: The REST Consumer object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_rest_delivery_point_rest_consumer" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `create_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_rest_delivery_point_rest_consumer`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None
        if 'body' in params:
            body_params = params['body']

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'POST',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointRestConsumerResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs):
        """
        Creates a Trusted Common Name object.
        Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restConsumerName|x||x|| restDeliveryPointName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName body: The Trusted Common Name object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs)
            return data

    def create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs):
        """
        Creates a Trusted Common Name object.
        Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restConsumerName|x||x|| restDeliveryPointName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName body: The Trusted Common Name object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'rest_consumer_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")
        # verify the required parameter 'rest_consumer_name' is set
        if ('rest_consumer_name' not in params) or (params['rest_consumer_name'] is None):
            raise ValueError("Missing the required parameter `rest_consumer_name` when calling `create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'rest_consumer_name' in params:
            path_params['restConsumerName'] = params['rest_consumer_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None
        if 'body' in params:
            body_params = params['body']

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'POST',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def delete_msg_vpn_rest_delivery_point(self, msg_vpn_name, rest_delivery_point_name, **kwargs):
        """
        Deletes a REST Delivery Point object.
        Deletes a REST Delivery Point object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, **kwargs)
        else:
            (data) = self.delete_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, **kwargs)
            return data

    def delete_msg_vpn_rest_delivery_point_with_http_info(self, msg_vpn_name, rest_delivery_point_name, **kwargs):
        """
        Deletes a REST Delivery Point object.
        Deletes a REST Delivery Point object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_rest_delivery_point" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_rest_delivery_point`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `delete_msg_vpn_rest_delivery_point`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']

        query_params = {}

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'DELETE',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='SempMetaOnlyResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def delete_msg_vpn_rest_delivery_point_queue_binding(self, msg_vpn_name, rest_delivery_point_name, queue_binding_name, **kwargs):
        """
        Deletes a Queue Binding object.
        Deletes a Queue Binding object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str queue_binding_name: The queueBindingName of the Queue Binding. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, **kwargs)
        else:
            (data) = self.delete_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, **kwargs)
            return data

    def delete_msg_vpn_rest_delivery_point_queue_binding_with_http_info(self, msg_vpn_name, rest_delivery_point_name, queue_binding_name, **kwargs):
        """
        Deletes a Queue Binding object.
        Deletes a Queue Binding object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str queue_binding_name: The queueBindingName of the Queue Binding. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'queue_binding_name']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_rest_delivery_point_queue_binding" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `delete_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'queue_binding_name' is set
        if ('queue_binding_name' not in params) or (params['queue_binding_name'] is None):
            raise ValueError("Missing the required parameter `queue_binding_name` when calling `delete_msg_vpn_rest_delivery_point_queue_binding`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'queue_binding_name' in params:
            path_params['queueBindingName'] = params['queue_binding_name']

        query_params = {}

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'DELETE',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='SempMetaOnlyResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def delete_msg_vpn_rest_delivery_point_rest_consumer(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs):
        """
        Deletes a REST Consumer object.
        Deletes a REST Consumer object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs)
        else:
            (data) = self.delete_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs)
            return data

    def delete_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs):
        """
        Deletes a REST Consumer object.
        Deletes a REST Consumer object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'rest_consumer_name']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_rest_delivery_point_rest_consumer" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `delete_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'rest_consumer_name' is set
        if ('rest_consumer_name' not in params) or (params['rest_consumer_name'] is None):
            raise ValueError("Missing the required parameter `rest_consumer_name` when calling `delete_msg_vpn_rest_delivery_point_rest_consumer`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'rest_consumer_name' in params:
            path_params['restConsumerName'] = params['rest_consumer_name']

        query_params = {}

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'DELETE',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='SempMetaOnlyResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, **kwargs):
        """
        Deletes a Trusted Common Name object.
        Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param str tls_trusted_common_name: The tlsTrustedCommonName of the Trusted Common Name. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, **kwargs)
        else:
            (data) = self.delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, **kwargs)
            return data

    def delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, **kwargs):
        """
        Deletes a Trusted Common Name object.
        Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param str tls_trusted_common_name: The tlsTrustedCommonName of the Trusted Common Name. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'rest_consumer_name', 'tls_trusted_common_name']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")
        # verify the required parameter 'rest_consumer_name' is set
        if ('rest_consumer_name' not in params) or (params['rest_consumer_name'] is None):
            raise ValueError("Missing the required parameter `rest_consumer_name` when calling `delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")
        # verify the required parameter 'tls_trusted_common_name' is set
        if ('tls_trusted_common_name' not in params) or (params['tls_trusted_common_name'] is None):
            raise ValueError("Missing the required parameter `tls_trusted_common_name` when calling `delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'rest_consumer_name' in params:
            path_params['restConsumerName'] = params['rest_consumer_name']
        if 'tls_trusted_common_name' in params:
            path_params['tlsTrustedCommonName'] = params['tls_trusted_common_name']

        query_params = {}

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'DELETE',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='SempMetaOnlyResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_rest_delivery_point(self, msg_vpn_name, rest_delivery_point_name, **kwargs):
        """
        Gets a REST Delivery Point object.
        Gets a REST Delivery Point object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, **kwargs)
            return data

    def get_msg_vpn_rest_delivery_point_with_http_info(self, msg_vpn_name, rest_delivery_point_name, **kwargs):
        """
        Gets a REST Delivery Point object.
        Gets a REST Delivery Point object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_rest_delivery_point" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_rest_delivery_point`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `get_msg_vpn_rest_delivery_point`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'GET',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_rest_delivery_point_queue_binding(self, msg_vpn_name, rest_delivery_point_name, queue_binding_name, **kwargs):
        """
        Gets a Queue Binding object.
        Gets a Queue Binding object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str queue_binding_name: The queueBindingName of the Queue Binding. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointQueueBindingResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, **kwargs)
            return data

    def get_msg_vpn_rest_delivery_point_queue_binding_with_http_info(self, msg_vpn_name, rest_delivery_point_name, queue_binding_name, **kwargs):
        """
        Gets a Queue Binding object.
        Gets a Queue Binding object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str queue_binding_name: The queueBindingName of the Queue Binding. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointQueueBindingResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'queue_binding_name', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_rest_delivery_point_queue_binding" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `get_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'queue_binding_name' is set
        if ('queue_binding_name' not in params) or (params['queue_binding_name'] is None):
            raise ValueError("Missing the required parameter `queue_binding_name` when calling `get_msg_vpn_rest_delivery_point_queue_binding`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'queue_binding_name' in params:
            path_params['queueBindingName'] = params['queue_binding_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'GET',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointQueueBindingResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_rest_delivery_point_queue_bindings(self, msg_vpn_name, rest_delivery_point_name, **kwargs):
        """
        Gets a list of Queue Binding objects.
        Gets a list of Queue Binding objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_queue_bindings(msg_vpn_name, rest_delivery_point_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointQueueBindingsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_rest_delivery_point_queue_bindings_with_http_info(msg_vpn_name, rest_delivery_point_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_rest_delivery_point_queue_bindings_with_http_info(msg_vpn_name, rest_delivery_point_name, **kwargs)
            return data

    def get_msg_vpn_rest_delivery_point_queue_bindings_with_http_info(self, msg_vpn_name, rest_delivery_point_name, **kwargs):
        """
        Gets a list of Queue Binding objects.
        Gets a list of Queue Binding objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_queue_bindings_with_http_info(msg_vpn_name, rest_delivery_point_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointQueueBindingsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'count', 'cursor', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_rest_delivery_point_queue_bindings" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_rest_delivery_point_queue_bindings`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `get_msg_vpn_rest_delivery_point_queue_bindings`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_rest_delivery_point_queue_bindings`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']

        query_params = {}
        if 'count' in params:
            query_params['count'] = params['count']
        if 'cursor' in params:
            query_params['cursor'] = params['cursor']
        if 'where' in params:
            query_params['where'] = params['where']
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'GET',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointQueueBindingsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_rest_delivery_point_rest_consumer(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs):
        """
        Gets a REST Consumer object.
        Gets a REST Consumer object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs)
            return data

    def get_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs):
        """
        Gets a REST Consumer object.
        Gets a REST Consumer object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'rest_consumer_name', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_rest_delivery_point_rest_consumer" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'rest_consumer_name' is set
        if ('rest_consumer_name' not in params) or (params['rest_consumer_name'] is None):
            raise ValueError("Missing the required parameter `rest_consumer_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumer`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'rest_consumer_name' in params:
            path_params['restConsumerName'] = params['rest_consumer_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'GET',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointRestConsumerResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, **kwargs):
        """
        Gets a Trusted Common Name object.
        Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param str tls_trusted_common_name: The tlsTrustedCommonName of the Trusted Common Name. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, **kwargs)
            return data

    def get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, **kwargs):
        """
        Gets a Trusted Common Name object.
        Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param str tls_trusted_common_name: The tlsTrustedCommonName of the Trusted Common Name. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'rest_consumer_name', 'tls_trusted_common_name', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")
        # verify the required parameter 'rest_consumer_name' is set
        if ('rest_consumer_name' not in params) or (params['rest_consumer_name'] is None):
            raise ValueError("Missing the required parameter `rest_consumer_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")
        # verify the required parameter 'tls_trusted_common_name' is set
        if ('tls_trusted_common_name' not in params) or (params['tls_trusted_common_name'] is None):
            raise ValueError("Missing the required parameter `tls_trusted_common_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'rest_consumer_name' in params:
            path_params['restConsumerName'] = params['rest_consumer_name']
        if 'tls_trusted_common_name' in params:
            path_params['tlsTrustedCommonName'] = params['tls_trusted_common_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'GET',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs):
        """
        Gets a list of Trusted Common Name objects.
        Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs)
            return data

    def get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names_with_http_info(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, **kwargs):
        """
        Gets a list of Trusted Common Name objects.
        Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'rest_consumer_name', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names`")
        # verify the required parameter 'rest_consumer_name' is set
        if ('rest_consumer_name' not in params) or (params['rest_consumer_name'] is None):
            raise ValueError("Missing the required parameter `rest_consumer_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'rest_consumer_name' in params:
            path_params['restConsumerName'] = params['rest_consumer_name']

        query_params = {}
        if 'where' in params:
            query_params['where'] = params['where']
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'GET',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_rest_delivery_point_rest_consumers(self, msg_vpn_name, rest_delivery_point_name, **kwargs):
        """
        Gets a list of REST Consumer objects.
        Gets a list of REST Consumer objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_rest_consumers(msg_vpn_name, rest_delivery_point_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumersResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_rest_delivery_point_rest_consumers_with_http_info(msg_vpn_name, rest_delivery_point_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_rest_delivery_point_rest_consumers_with_http_info(msg_vpn_name, rest_delivery_point_name, **kwargs)
            return data

    def get_msg_vpn_rest_delivery_point_rest_consumers_with_http_info(self, msg_vpn_name, rest_delivery_point_name, **kwargs):
        """
        Gets a list of REST Consumer objects.
        Gets a list of REST Consumer objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_point_rest_consumers_with_http_info(msg_vpn_name, rest_delivery_point_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumersResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'count', 'cursor', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_rest_delivery_point_rest_consumers" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumers`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `get_msg_vpn_rest_delivery_point_rest_consumers`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_rest_delivery_point_rest_consumers`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']

        query_params = {}
        if 'count' in params:
            query_params['count'] = params['count']
        if 'cursor' in params:
            query_params['cursor'] = params['cursor']
        if 'where' in params:
            query_params['where'] = params['where']
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'GET',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointRestConsumersResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_rest_delivery_points(self, msg_vpn_name, **kwargs):
        """
        Gets a list of REST Delivery Point objects.
        Gets a list of REST Delivery Point objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_points(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_rest_delivery_points_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_rest_delivery_points_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_rest_delivery_points_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a list of REST Delivery Point objects.
        Gets a list of REST Delivery Point objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_rest_delivery_points_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'count', 'cursor', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_rest_delivery_points" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_rest_delivery_points`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_rest_delivery_points`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']

        query_params = {}
        if 'count' in params:
            query_params['count'] = params['count']
        if 'cursor' in params:
            query_params['cursor'] = params['cursor']
        if 'where' in params:
            query_params['where'] = params['where']
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'GET',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_rest_delivery_point(self, msg_vpn_name, rest_delivery_point_name, body, **kwargs):
        """
        Replaces a REST Delivery Point object.
        Replaces a REST Delivery Point object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param MsgVpnRestDeliveryPoint body: The REST Delivery Point object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, body, **kwargs)
            return data

    def replace_msg_vpn_rest_delivery_point_with_http_info(self, msg_vpn_name, rest_delivery_point_name, body, **kwargs):
        """
        Replaces a REST Delivery Point object.
        Replaces a REST Delivery Point object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param MsgVpnRestDeliveryPoint body: The REST Delivery Point object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_rest_delivery_point" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_rest_delivery_point`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `replace_msg_vpn_rest_delivery_point`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_rest_delivery_point`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None
        if 'body' in params:
            body_params = params['body']

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'PUT',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_rest_delivery_point_queue_binding(self, msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, **kwargs):
        """
        Replaces a Queue Binding object.
        Replaces a Queue Binding object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str queue_binding_name: The queueBindingName of the Queue Binding. (required)
        :param MsgVpnRestDeliveryPointQueueBinding body: The Queue Binding object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointQueueBindingResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, **kwargs)
            return data

    def replace_msg_vpn_rest_delivery_point_queue_binding_with_http_info(self, msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, **kwargs):
        """
        Replaces a Queue Binding object.
        Replaces a Queue Binding object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str queue_binding_name: The queueBindingName of the Queue Binding. (required)
        :param MsgVpnRestDeliveryPointQueueBinding body: The Queue Binding object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointQueueBindingResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'queue_binding_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_rest_delivery_point_queue_binding" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `replace_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'queue_binding_name' is set
        if ('queue_binding_name' not in params) or (params['queue_binding_name'] is None):
            raise ValueError("Missing the required parameter `queue_binding_name` when calling `replace_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_rest_delivery_point_queue_binding`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'queue_binding_name' in params:
            path_params['queueBindingName'] = params['queue_binding_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None
        if 'body' in params:
            body_params = params['body']

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'PUT',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointQueueBindingResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_rest_delivery_point_rest_consumer(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs):
        """
        Replaces a REST Consumer object.
        Replaces a REST Consumer object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param MsgVpnRestDeliveryPointRestConsumer body: The REST Consumer object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs)
            return data

    def replace_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs):
        """
        Replaces a REST Consumer object.
        Replaces a REST Consumer object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param MsgVpnRestDeliveryPointRestConsumer body: The REST Consumer object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'rest_consumer_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_rest_delivery_point_rest_consumer" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `replace_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'rest_consumer_name' is set
        if ('rest_consumer_name' not in params) or (params['rest_consumer_name'] is None):
            raise ValueError("Missing the required parameter `rest_consumer_name` when calling `replace_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_rest_delivery_point_rest_consumer`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'rest_consumer_name' in params:
            path_params['restConsumerName'] = params['rest_consumer_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None
        if 'body' in params:
            body_params = params['body']

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'PUT',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointRestConsumerResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_rest_delivery_point(self, msg_vpn_name, rest_delivery_point_name, body, **kwargs):
        """
        Updates a REST Delivery Point object.
        Updates a REST Delivery Point object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param MsgVpnRestDeliveryPoint body: The REST Delivery Point object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, body, **kwargs)
            return data

    def update_msg_vpn_rest_delivery_point_with_http_info(self, msg_vpn_name, rest_delivery_point_name, body, **kwargs):
        """
        Updates a REST Delivery Point object.
        Updates a REST Delivery Point object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param MsgVpnRestDeliveryPoint body: The REST Delivery Point object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_rest_delivery_point" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_rest_delivery_point`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `update_msg_vpn_rest_delivery_point`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_rest_delivery_point`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None
        if 'body' in params:
            body_params = params['body']

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'PATCH',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_rest_delivery_point_queue_binding(self, msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, **kwargs):
        """
        Updates a Queue Binding object.
        Updates a Queue Binding object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str queue_binding_name: The queueBindingName of the Queue Binding. (required)
        :param MsgVpnRestDeliveryPointQueueBinding body: The Queue Binding object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointQueueBindingResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, **kwargs)
            return data

    def update_msg_vpn_rest_delivery_point_queue_binding_with_http_info(self, msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, **kwargs):
        """
        Updates a Queue Binding object.
        Updates a Queue Binding object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str queue_binding_name: The queueBindingName of the Queue Binding. (required)
        :param MsgVpnRestDeliveryPointQueueBinding body: The Queue Binding object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointQueueBindingResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'queue_binding_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_rest_delivery_point_queue_binding" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `update_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'queue_binding_name' is set
        if ('queue_binding_name' not in params) or (params['queue_binding_name'] is None):
            raise ValueError("Missing the required parameter `queue_binding_name` when calling `update_msg_vpn_rest_delivery_point_queue_binding`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_rest_delivery_point_queue_binding`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'queue_binding_name' in params:
            path_params['queueBindingName'] = params['queue_binding_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None
        if 'body' in params:
            body_params = params['body']

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'PATCH',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointQueueBindingResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_rest_delivery_point_rest_consumer(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs):
        """
        Updates a REST Consumer object.
        Updates a REST Consumer object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param MsgVpnRestDeliveryPointRestConsumer body: The REST Consumer object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs)
            return data

    def update_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(self, msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, **kwargs):
        """
        Updates a REST Consumer object.
        Updates a REST Consumer object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str rest_delivery_point_name: The restDeliveryPointName of the REST Delivery Point. (required)
        :param str rest_consumer_name: The restConsumerName of the REST Consumer. (required)
        :param MsgVpnRestDeliveryPointRestConsumer body: The REST Consumer object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnRestDeliveryPointRestConsumerResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'rest_delivery_point_name', 'rest_consumer_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_rest_delivery_point_rest_consumer" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'rest_delivery_point_name' is set
        if ('rest_delivery_point_name' not in params) or (params['rest_delivery_point_name'] is None):
            raise ValueError("Missing the required parameter `rest_delivery_point_name` when calling `update_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'rest_consumer_name' is set
        if ('rest_consumer_name' not in params) or (params['rest_consumer_name'] is None):
            raise ValueError("Missing the required parameter `rest_consumer_name` when calling `update_msg_vpn_rest_delivery_point_rest_consumer`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_rest_delivery_point_rest_consumer`")

        resource_path = '/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'rest_delivery_point_name' in params:
            path_params['restDeliveryPointName'] = params['rest_delivery_point_name']
        if 'rest_consumer_name' in params:
            path_params['restConsumerName'] = params['rest_consumer_name']

        query_params = {}
        if 'select' in params:
            query_params['select'] = params['select']

        header_params = {}

        form_params = []
        local_var_files = {}

        body_params = None
        if 'body' in params:
            body_params = params['body']

        # HTTP header `Accept`
        header_params['Accept'] = self.api_client.\
            select_header_accept(['application/json'])
        if not header_params['Accept']:
            del header_params['Accept']

        # HTTP header `Content-Type`
        header_params['Content-Type'] = self.api_client.\
            select_header_content_type(['application/json'])

        # Authentication setting
        auth_settings = ['basicAuth']

        return self.api_client.call_api(resource_path, 'PATCH',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnRestDeliveryPointRestConsumerResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))
