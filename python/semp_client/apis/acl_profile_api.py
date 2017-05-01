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


class AclProfileApi(object):
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

    def create_msg_vpn_acl_profile(self, msg_vpn_name, body, **kwargs):
        """
        Creates an ACL Profile object.
        Creates an ACL Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_acl_profile(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnAclProfile body: The ACL Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_acl_profile_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_acl_profile_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def create_msg_vpn_acl_profile_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Creates an ACL Profile object.
        Creates an ACL Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_acl_profile_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnAclProfile body: The ACL Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileResponse
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
                    " to method create_msg_vpn_acl_profile" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_acl_profile`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_acl_profile`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles'.replace('{format}', 'json')
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
                                            response_type='MsgVpnAclProfileResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_acl_profile_client_connect_exception(self, msg_vpn_name, acl_profile_name, body, **kwargs):
        """
        Creates a Client Connect Exception object.
        Creates a Client Connect Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| clientConnectExceptionAddress|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param MsgVpnAclProfileClientConnectException body: The Client Connect Exception object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileClientConnectExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, body, **kwargs)
            return data

    def create_msg_vpn_acl_profile_client_connect_exception_with_http_info(self, msg_vpn_name, acl_profile_name, body, **kwargs):
        """
        Creates a Client Connect Exception object.
        Creates a Client Connect Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| clientConnectExceptionAddress|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param MsgVpnAclProfileClientConnectException body: The Client Connect Exception object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileClientConnectExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_acl_profile_client_connect_exception" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_acl_profile_client_connect_exception`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `create_msg_vpn_acl_profile_client_connect_exception`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_acl_profile_client_connect_exception`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']

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
                                            response_type='MsgVpnAclProfileClientConnectExceptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_acl_profile_publish_exception(self, msg_vpn_name, acl_profile_name, body, **kwargs):
        """
        Creates a Publish Topic Exception object.
        Creates a Publish Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| publishExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param MsgVpnAclProfilePublishException body: The Publish Topic Exception object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfilePublishExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, body, **kwargs)
            return data

    def create_msg_vpn_acl_profile_publish_exception_with_http_info(self, msg_vpn_name, acl_profile_name, body, **kwargs):
        """
        Creates a Publish Topic Exception object.
        Creates a Publish Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| publishExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param MsgVpnAclProfilePublishException body: The Publish Topic Exception object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfilePublishExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_acl_profile_publish_exception" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_acl_profile_publish_exception`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `create_msg_vpn_acl_profile_publish_exception`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_acl_profile_publish_exception`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']

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
                                            response_type='MsgVpnAclProfilePublishExceptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_acl_profile_subscribe_exception(self, msg_vpn_name, acl_profile_name, body, **kwargs):
        """
        Creates a Subscribe Topic Exception object.
        Creates a Subscribe Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| subscribeExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param MsgVpnAclProfileSubscribeException body: The Subscribe Topic Exception object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileSubscribeExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, body, **kwargs)
            return data

    def create_msg_vpn_acl_profile_subscribe_exception_with_http_info(self, msg_vpn_name, acl_profile_name, body, **kwargs):
        """
        Creates a Subscribe Topic Exception object.
        Creates a Subscribe Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| subscribeExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param MsgVpnAclProfileSubscribeException body: The Subscribe Topic Exception object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileSubscribeExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_acl_profile_subscribe_exception" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_acl_profile_subscribe_exception`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `create_msg_vpn_acl_profile_subscribe_exception`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_acl_profile_subscribe_exception`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']

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
                                            response_type='MsgVpnAclProfileSubscribeExceptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def delete_msg_vpn_acl_profile(self, msg_vpn_name, acl_profile_name, **kwargs):
        """
        Deletes an ACL Profile object.
        Deletes an ACL Profile object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, **kwargs)
        else:
            (data) = self.delete_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, **kwargs)
            return data

    def delete_msg_vpn_acl_profile_with_http_info(self, msg_vpn_name, acl_profile_name, **kwargs):
        """
        Deletes an ACL Profile object.
        Deletes an ACL Profile object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_acl_profile" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_acl_profile`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `delete_msg_vpn_acl_profile`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']

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

    def delete_msg_vpn_acl_profile_client_connect_exception(self, msg_vpn_name, acl_profile_name, client_connect_exception_address, **kwargs):
        """
        Deletes a Client Connect Exception object.
        Deletes a Client Connect Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str client_connect_exception_address: The clientConnectExceptionAddress of the Client Connect Exception. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, client_connect_exception_address, **kwargs)
        else:
            (data) = self.delete_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, client_connect_exception_address, **kwargs)
            return data

    def delete_msg_vpn_acl_profile_client_connect_exception_with_http_info(self, msg_vpn_name, acl_profile_name, client_connect_exception_address, **kwargs):
        """
        Deletes a Client Connect Exception object.
        Deletes a Client Connect Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, client_connect_exception_address, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str client_connect_exception_address: The clientConnectExceptionAddress of the Client Connect Exception. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'client_connect_exception_address']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_acl_profile_client_connect_exception" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_acl_profile_client_connect_exception`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `delete_msg_vpn_acl_profile_client_connect_exception`")
        # verify the required parameter 'client_connect_exception_address' is set
        if ('client_connect_exception_address' not in params) or (params['client_connect_exception_address'] is None):
            raise ValueError("Missing the required parameter `client_connect_exception_address` when calling `delete_msg_vpn_acl_profile_client_connect_exception`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']
        if 'client_connect_exception_address' in params:
            path_params['clientConnectExceptionAddress'] = params['client_connect_exception_address']

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

    def delete_msg_vpn_acl_profile_publish_exception(self, msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, **kwargs):
        """
        Deletes a Publish Topic Exception object.
        Deletes a Publish Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str topic_syntax: The topicSyntax of the Publish Topic Exception. (required)
        :param str publish_exception_topic: The publishExceptionTopic of the Publish Topic Exception. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, **kwargs)
        else:
            (data) = self.delete_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, **kwargs)
            return data

    def delete_msg_vpn_acl_profile_publish_exception_with_http_info(self, msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, **kwargs):
        """
        Deletes a Publish Topic Exception object.
        Deletes a Publish Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str topic_syntax: The topicSyntax of the Publish Topic Exception. (required)
        :param str publish_exception_topic: The publishExceptionTopic of the Publish Topic Exception. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'topic_syntax', 'publish_exception_topic']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_acl_profile_publish_exception" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_acl_profile_publish_exception`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `delete_msg_vpn_acl_profile_publish_exception`")
        # verify the required parameter 'topic_syntax' is set
        if ('topic_syntax' not in params) or (params['topic_syntax'] is None):
            raise ValueError("Missing the required parameter `topic_syntax` when calling `delete_msg_vpn_acl_profile_publish_exception`")
        # verify the required parameter 'publish_exception_topic' is set
        if ('publish_exception_topic' not in params) or (params['publish_exception_topic'] is None):
            raise ValueError("Missing the required parameter `publish_exception_topic` when calling `delete_msg_vpn_acl_profile_publish_exception`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']
        if 'topic_syntax' in params:
            path_params['topicSyntax'] = params['topic_syntax']
        if 'publish_exception_topic' in params:
            path_params['publishExceptionTopic'] = params['publish_exception_topic']

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

    def delete_msg_vpn_acl_profile_subscribe_exception(self, msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, **kwargs):
        """
        Deletes a Subscribe Topic Exception object.
        Deletes a Subscribe Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str topic_syntax: The topicSyntax of the Subscribe Topic Exception. (required)
        :param str subscribe_exception_topic: The subscribeExceptionTopic of the Subscribe Topic Exception. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, **kwargs)
        else:
            (data) = self.delete_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, **kwargs)
            return data

    def delete_msg_vpn_acl_profile_subscribe_exception_with_http_info(self, msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, **kwargs):
        """
        Deletes a Subscribe Topic Exception object.
        Deletes a Subscribe Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str topic_syntax: The topicSyntax of the Subscribe Topic Exception. (required)
        :param str subscribe_exception_topic: The subscribeExceptionTopic of the Subscribe Topic Exception. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'topic_syntax', 'subscribe_exception_topic']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_acl_profile_subscribe_exception" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_acl_profile_subscribe_exception`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `delete_msg_vpn_acl_profile_subscribe_exception`")
        # verify the required parameter 'topic_syntax' is set
        if ('topic_syntax' not in params) or (params['topic_syntax'] is None):
            raise ValueError("Missing the required parameter `topic_syntax` when calling `delete_msg_vpn_acl_profile_subscribe_exception`")
        # verify the required parameter 'subscribe_exception_topic' is set
        if ('subscribe_exception_topic' not in params) or (params['subscribe_exception_topic'] is None):
            raise ValueError("Missing the required parameter `subscribe_exception_topic` when calling `delete_msg_vpn_acl_profile_subscribe_exception`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']
        if 'topic_syntax' in params:
            path_params['topicSyntax'] = params['topic_syntax']
        if 'subscribe_exception_topic' in params:
            path_params['subscribeExceptionTopic'] = params['subscribe_exception_topic']

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

    def get_msg_vpn_acl_profile(self, msg_vpn_name, acl_profile_name, **kwargs):
        """
        Gets an ACL Profile object.
        Gets an ACL Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, **kwargs)
            return data

    def get_msg_vpn_acl_profile_with_http_info(self, msg_vpn_name, acl_profile_name, **kwargs):
        """
        Gets an ACL Profile object.
        Gets an ACL Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_acl_profile" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_acl_profile`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `get_msg_vpn_acl_profile`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']

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
                                            response_type='MsgVpnAclProfileResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_acl_profile_client_connect_exception(self, msg_vpn_name, acl_profile_name, client_connect_exception_address, **kwargs):
        """
        Gets a Client Connect Exception object.
        Gets a Client Connect Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str client_connect_exception_address: The clientConnectExceptionAddress of the Client Connect Exception. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileClientConnectExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, client_connect_exception_address, **kwargs)
        else:
            (data) = self.get_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, client_connect_exception_address, **kwargs)
            return data

    def get_msg_vpn_acl_profile_client_connect_exception_with_http_info(self, msg_vpn_name, acl_profile_name, client_connect_exception_address, **kwargs):
        """
        Gets a Client Connect Exception object.
        Gets a Client Connect Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, client_connect_exception_address, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str client_connect_exception_address: The clientConnectExceptionAddress of the Client Connect Exception. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileClientConnectExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'client_connect_exception_address', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_acl_profile_client_connect_exception" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_acl_profile_client_connect_exception`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `get_msg_vpn_acl_profile_client_connect_exception`")
        # verify the required parameter 'client_connect_exception_address' is set
        if ('client_connect_exception_address' not in params) or (params['client_connect_exception_address'] is None):
            raise ValueError("Missing the required parameter `client_connect_exception_address` when calling `get_msg_vpn_acl_profile_client_connect_exception`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']
        if 'client_connect_exception_address' in params:
            path_params['clientConnectExceptionAddress'] = params['client_connect_exception_address']

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
                                            response_type='MsgVpnAclProfileClientConnectExceptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_acl_profile_client_connect_exceptions(self, msg_vpn_name, acl_profile_name, **kwargs):
        """
        Gets a list of Client Connect Exception objects.
        Gets a list of Client Connect Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_client_connect_exceptions(msg_vpn_name, acl_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileClientConnectExceptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_acl_profile_client_connect_exceptions_with_http_info(msg_vpn_name, acl_profile_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_acl_profile_client_connect_exceptions_with_http_info(msg_vpn_name, acl_profile_name, **kwargs)
            return data

    def get_msg_vpn_acl_profile_client_connect_exceptions_with_http_info(self, msg_vpn_name, acl_profile_name, **kwargs):
        """
        Gets a list of Client Connect Exception objects.
        Gets a list of Client Connect Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_client_connect_exceptions_with_http_info(msg_vpn_name, acl_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileClientConnectExceptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'count', 'cursor', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_acl_profile_client_connect_exceptions" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_acl_profile_client_connect_exceptions`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `get_msg_vpn_acl_profile_client_connect_exceptions`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_acl_profile_client_connect_exceptions`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']

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
                                            response_type='MsgVpnAclProfileClientConnectExceptionsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_acl_profile_publish_exception(self, msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, **kwargs):
        """
        Gets a Publish Topic Exception object.
        Gets a Publish Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str topic_syntax: The topicSyntax of the Publish Topic Exception. (required)
        :param str publish_exception_topic: The publishExceptionTopic of the Publish Topic Exception. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfilePublishExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, **kwargs)
        else:
            (data) = self.get_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, **kwargs)
            return data

    def get_msg_vpn_acl_profile_publish_exception_with_http_info(self, msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, **kwargs):
        """
        Gets a Publish Topic Exception object.
        Gets a Publish Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str topic_syntax: The topicSyntax of the Publish Topic Exception. (required)
        :param str publish_exception_topic: The publishExceptionTopic of the Publish Topic Exception. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfilePublishExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'topic_syntax', 'publish_exception_topic', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_acl_profile_publish_exception" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_acl_profile_publish_exception`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `get_msg_vpn_acl_profile_publish_exception`")
        # verify the required parameter 'topic_syntax' is set
        if ('topic_syntax' not in params) or (params['topic_syntax'] is None):
            raise ValueError("Missing the required parameter `topic_syntax` when calling `get_msg_vpn_acl_profile_publish_exception`")
        # verify the required parameter 'publish_exception_topic' is set
        if ('publish_exception_topic' not in params) or (params['publish_exception_topic'] is None):
            raise ValueError("Missing the required parameter `publish_exception_topic` when calling `get_msg_vpn_acl_profile_publish_exception`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']
        if 'topic_syntax' in params:
            path_params['topicSyntax'] = params['topic_syntax']
        if 'publish_exception_topic' in params:
            path_params['publishExceptionTopic'] = params['publish_exception_topic']

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
                                            response_type='MsgVpnAclProfilePublishExceptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_acl_profile_publish_exceptions(self, msg_vpn_name, acl_profile_name, **kwargs):
        """
        Gets a list of Publish Topic Exception objects.
        Gets a list of Publish Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_publish_exceptions(msg_vpn_name, acl_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfilePublishExceptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_acl_profile_publish_exceptions_with_http_info(msg_vpn_name, acl_profile_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_acl_profile_publish_exceptions_with_http_info(msg_vpn_name, acl_profile_name, **kwargs)
            return data

    def get_msg_vpn_acl_profile_publish_exceptions_with_http_info(self, msg_vpn_name, acl_profile_name, **kwargs):
        """
        Gets a list of Publish Topic Exception objects.
        Gets a list of Publish Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_publish_exceptions_with_http_info(msg_vpn_name, acl_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfilePublishExceptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'count', 'cursor', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_acl_profile_publish_exceptions" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_acl_profile_publish_exceptions`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `get_msg_vpn_acl_profile_publish_exceptions`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_acl_profile_publish_exceptions`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']

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
                                            response_type='MsgVpnAclProfilePublishExceptionsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_acl_profile_subscribe_exception(self, msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, **kwargs):
        """
        Gets a Subscribe Topic Exception object.
        Gets a Subscribe Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str topic_syntax: The topicSyntax of the Subscribe Topic Exception. (required)
        :param str subscribe_exception_topic: The subscribeExceptionTopic of the Subscribe Topic Exception. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileSubscribeExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, **kwargs)
        else:
            (data) = self.get_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, **kwargs)
            return data

    def get_msg_vpn_acl_profile_subscribe_exception_with_http_info(self, msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, **kwargs):
        """
        Gets a Subscribe Topic Exception object.
        Gets a Subscribe Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param str topic_syntax: The topicSyntax of the Subscribe Topic Exception. (required)
        :param str subscribe_exception_topic: The subscribeExceptionTopic of the Subscribe Topic Exception. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileSubscribeExceptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'topic_syntax', 'subscribe_exception_topic', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_acl_profile_subscribe_exception" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_acl_profile_subscribe_exception`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `get_msg_vpn_acl_profile_subscribe_exception`")
        # verify the required parameter 'topic_syntax' is set
        if ('topic_syntax' not in params) or (params['topic_syntax'] is None):
            raise ValueError("Missing the required parameter `topic_syntax` when calling `get_msg_vpn_acl_profile_subscribe_exception`")
        # verify the required parameter 'subscribe_exception_topic' is set
        if ('subscribe_exception_topic' not in params) or (params['subscribe_exception_topic'] is None):
            raise ValueError("Missing the required parameter `subscribe_exception_topic` when calling `get_msg_vpn_acl_profile_subscribe_exception`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']
        if 'topic_syntax' in params:
            path_params['topicSyntax'] = params['topic_syntax']
        if 'subscribe_exception_topic' in params:
            path_params['subscribeExceptionTopic'] = params['subscribe_exception_topic']

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
                                            response_type='MsgVpnAclProfileSubscribeExceptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_acl_profile_subscribe_exceptions(self, msg_vpn_name, acl_profile_name, **kwargs):
        """
        Gets a list of Subscribe Topic Exception objects.
        Gets a list of Subscribe Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_subscribe_exceptions(msg_vpn_name, acl_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileSubscribeExceptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_acl_profile_subscribe_exceptions_with_http_info(msg_vpn_name, acl_profile_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_acl_profile_subscribe_exceptions_with_http_info(msg_vpn_name, acl_profile_name, **kwargs)
            return data

    def get_msg_vpn_acl_profile_subscribe_exceptions_with_http_info(self, msg_vpn_name, acl_profile_name, **kwargs):
        """
        Gets a list of Subscribe Topic Exception objects.
        Gets a list of Subscribe Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profile_subscribe_exceptions_with_http_info(msg_vpn_name, acl_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileSubscribeExceptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'count', 'cursor', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_acl_profile_subscribe_exceptions" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_acl_profile_subscribe_exceptions`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `get_msg_vpn_acl_profile_subscribe_exceptions`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_acl_profile_subscribe_exceptions`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']

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
                                            response_type='MsgVpnAclProfileSubscribeExceptionsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_acl_profiles(self, msg_vpn_name, **kwargs):
        """
        Gets a list of ACL Profile objects.
        Gets a list of ACL Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profiles(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfilesResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_acl_profiles_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_acl_profiles_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_acl_profiles_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a list of ACL Profile objects.
        Gets a list of ACL Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_acl_profiles_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfilesResponse
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
                    " to method get_msg_vpn_acl_profiles" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_acl_profiles`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_acl_profiles`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/aclProfiles'.replace('{format}', 'json')
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
                                            response_type='MsgVpnAclProfilesResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_acl_profile(self, msg_vpn_name, acl_profile_name, body, **kwargs):
        """
        Replaces an ACL Profile object.
        Replaces an ACL Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param MsgVpnAclProfile body: The ACL Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, body, **kwargs)
            return data

    def replace_msg_vpn_acl_profile_with_http_info(self, msg_vpn_name, acl_profile_name, body, **kwargs):
        """
        Replaces an ACL Profile object.
        Replaces an ACL Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param MsgVpnAclProfile body: The ACL Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_acl_profile" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_acl_profile`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `replace_msg_vpn_acl_profile`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_acl_profile`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']

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
                                            response_type='MsgVpnAclProfileResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_acl_profile(self, msg_vpn_name, acl_profile_name, body, **kwargs):
        """
        Updates an ACL Profile object.
        Updates an ACL Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param MsgVpnAclProfile body: The ACL Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, body, **kwargs)
            return data

    def update_msg_vpn_acl_profile_with_http_info(self, msg_vpn_name, acl_profile_name, body, **kwargs):
        """
        Updates an ACL Profile object.
        Updates an ACL Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str acl_profile_name: The aclProfileName of the ACL Profile. (required)
        :param MsgVpnAclProfile body: The ACL Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAclProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'acl_profile_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_acl_profile" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_acl_profile`")
        # verify the required parameter 'acl_profile_name' is set
        if ('acl_profile_name' not in params) or (params['acl_profile_name'] is None):
            raise ValueError("Missing the required parameter `acl_profile_name` when calling `update_msg_vpn_acl_profile`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_acl_profile`")

        resource_path = '/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'acl_profile_name' in params:
            path_params['aclProfileName'] = params['acl_profile_name']

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
                                            response_type='MsgVpnAclProfileResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))
