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


class MsgVpnApi(object):
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

    def create_msg_vpn(self, body, **kwargs):
        """
        Creates a Message VPN object.
        Creates a Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword||||x| replicationEnabledQueueBehavior||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn(body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param MsgVpn body: The Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_with_http_info(body, **kwargs)
        else:
            (data) = self.create_msg_vpn_with_http_info(body, **kwargs)
            return data

    def create_msg_vpn_with_http_info(self, body, **kwargs):
        """
        Creates a Message VPN object.
        Creates a Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword||||x| replicationEnabledQueueBehavior||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_with_http_info(body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param MsgVpn body: The Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn`")

        resource_path = '/msgVpns'.replace('{format}', 'json')
        path_params = {}

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
                                            response_type='MsgVpnResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

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

    def create_msg_vpn_authorization_group(self, msg_vpn_name, body, **kwargs):
        """
        Creates an LDAP Authorization Group object.
        Creates an LDAP Authorization Group object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authorizationGroupName|x|x||| msgVpnName|x||x|| orderAfterAuthorizationGroupName||||x| orderBeforeAuthorizationGroupName||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_authorization_group(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnAuthorizationGroup body: The LDAP Authorization Group object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAuthorizationGroupResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_authorization_group_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_authorization_group_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def create_msg_vpn_authorization_group_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Creates an LDAP Authorization Group object.
        Creates an LDAP Authorization Group object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authorizationGroupName|x|x||| msgVpnName|x||x|| orderAfterAuthorizationGroupName||||x| orderBeforeAuthorizationGroupName||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_authorization_group_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnAuthorizationGroup body: The LDAP Authorization Group object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAuthorizationGroupResponse
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
                    " to method create_msg_vpn_authorization_group" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_authorization_group`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_authorization_group`")

        resource_path = '/msgVpns/{msgVpnName}/authorizationGroups'.replace('{format}', 'json')
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
                                            response_type='MsgVpnAuthorizationGroupResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_bridge(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Bridge object.
        Creates a Bridge object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| msgVpnName|x||x|| remoteAuthenticationBasicPassword||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_bridge(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnBridge body: The Bridge object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_bridge_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_bridge_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def create_msg_vpn_bridge_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Bridge object.
        Creates a Bridge object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| msgVpnName|x||x|| remoteAuthenticationBasicPassword||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_bridge_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnBridge body: The Bridge object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeResponse
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
                    " to method create_msg_vpn_bridge" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_bridge`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_bridge`")

        resource_path = '/msgVpns/{msgVpnName}/bridges'.replace('{format}', 'json')
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
                                            response_type='MsgVpnBridgeResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_bridge_remote_msg_vpn(self, msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs):
        """
        Creates a Remote Message VPN object.
        Creates a Remote Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| password||||x| remoteMsgVpnInterface|x|||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param MsgVpnBridgeRemoteMsgVpn body: The Remote Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteMsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs)
            return data

    def create_msg_vpn_bridge_remote_msg_vpn_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs):
        """
        Creates a Remote Message VPN object.
        Creates a Remote Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| password||||x| remoteMsgVpnInterface|x|||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param MsgVpnBridgeRemoteMsgVpn body: The Remote Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteMsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_bridge_remote_msg_vpn" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `create_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `create_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_bridge_remote_msg_vpn`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']

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
                                            response_type='MsgVpnBridgeRemoteMsgVpnResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_bridge_remote_subscription(self, msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs):
        """
        Creates a Remote Subscription object.
        Creates a Remote Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| deliverAlwaysEnabled||x||| msgVpnName|x||x|| remoteSubscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param MsgVpnBridgeRemoteSubscription body: The Remote Subscription object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs)
            return data

    def create_msg_vpn_bridge_remote_subscription_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs):
        """
        Creates a Remote Subscription object.
        Creates a Remote Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| deliverAlwaysEnabled||x||| msgVpnName|x||x|| remoteSubscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param MsgVpnBridgeRemoteSubscription body: The Remote Subscription object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_bridge_remote_subscription" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_bridge_remote_subscription`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `create_msg_vpn_bridge_remote_subscription`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `create_msg_vpn_bridge_remote_subscription`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_bridge_remote_subscription`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']

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
                                            response_type='MsgVpnBridgeRemoteSubscriptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_bridge_tls_trusted_common_name(self, msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs):
        """
        Creates a Trusted Common Name object.
        Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param MsgVpnBridgeTlsTrustedCommonName body: The Trusted Common Name object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeTlsTrustedCommonNameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs)
            return data

    def create_msg_vpn_bridge_tls_trusted_common_name_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs):
        """
        Creates a Trusted Common Name object.
        Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param MsgVpnBridgeTlsTrustedCommonName body: The Trusted Common Name object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeTlsTrustedCommonNameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_bridge_tls_trusted_common_name" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_bridge_tls_trusted_common_name`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `create_msg_vpn_bridge_tls_trusted_common_name`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `create_msg_vpn_bridge_tls_trusted_common_name`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_bridge_tls_trusted_common_name`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']

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
                                            response_type='MsgVpnBridgeTlsTrustedCommonNameResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_client_profile(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Client Profile object.
        Creates a Client Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_client_profile(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnClientProfile body: The Client Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_client_profile_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_client_profile_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def create_msg_vpn_client_profile_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Client Profile object.
        Creates a Client Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_client_profile_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnClientProfile body: The Client Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientProfileResponse
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
                    " to method create_msg_vpn_client_profile" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_client_profile`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_client_profile`")

        resource_path = '/msgVpns/{msgVpnName}/clientProfiles'.replace('{format}', 'json')
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
                                            response_type='MsgVpnClientProfileResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_client_username(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Client Username object.
        Creates a Client Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientUsername|x|x||| msgVpnName|x||x|| password||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_client_username(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnClientUsername body: The Client Username object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientUsernameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_client_username_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_client_username_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def create_msg_vpn_client_username_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Client Username object.
        Creates a Client Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientUsername|x|x||| msgVpnName|x||x|| password||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_client_username_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnClientUsername body: The Client Username object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientUsernameResponse
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
                    " to method create_msg_vpn_client_username" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_client_username`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_client_username`")

        resource_path = '/msgVpns/{msgVpnName}/clientUsernames'.replace('{format}', 'json')
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
                                            response_type='MsgVpnClientUsernameResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_mqtt_session(self, msg_vpn_name, body, **kwargs):
        """
        Creates an MQTT Session object.
        Creates an MQTT Session object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_mqtt_session(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnMqttSession body: The MQTT Session object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def create_msg_vpn_mqtt_session_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Creates an MQTT Session object.
        Creates an MQTT Session object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnMqttSession body: The MQTT Session object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionResponse
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
                    " to method create_msg_vpn_mqtt_session" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_mqtt_session`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_mqtt_session`")

        resource_path = '/msgVpns/{msgVpnName}/mqttSessions'.replace('{format}', 'json')
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
                                            response_type='MsgVpnMqttSessionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_mqtt_session_subscription(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs):
        """
        Creates an MQTT Session Subscription object.
        Creates an MQTT Session Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x||x|| mqttSessionVirtualRouter|x||x|| msgVpnName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param MsgVpnMqttSessionSubscription body: The MQTT Session Subscription object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs)
            return data

    def create_msg_vpn_mqtt_session_subscription_with_http_info(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs):
        """
        Creates an MQTT Session Subscription object.
        Creates an MQTT Session Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x||x|| mqttSessionVirtualRouter|x||x|| msgVpnName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param MsgVpnMqttSessionSubscription body: The MQTT Session Subscription object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'mqtt_session_client_id', 'mqtt_session_virtual_router', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_mqtt_session_subscription" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'mqtt_session_client_id' is set
        if ('mqtt_session_client_id' not in params) or (params['mqtt_session_client_id'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_client_id` when calling `create_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'mqtt_session_virtual_router' is set
        if ('mqtt_session_virtual_router' not in params) or (params['mqtt_session_virtual_router'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_virtual_router` when calling `create_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_mqtt_session_subscription`")

        resource_path = '/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'mqtt_session_client_id' in params:
            path_params['mqttSessionClientId'] = params['mqtt_session_client_id']
        if 'mqtt_session_virtual_router' in params:
            path_params['mqttSessionVirtualRouter'] = params['mqtt_session_virtual_router']

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
                                            response_type='MsgVpnMqttSessionSubscriptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_queue(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Queue object.
        Creates a Queue object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_queue(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnQueue body: The Queue object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_queue_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_queue_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def create_msg_vpn_queue_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Queue object.
        Creates a Queue object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_queue_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnQueue body: The Queue object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueResponse
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
                    " to method create_msg_vpn_queue" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_queue`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_queue`")

        resource_path = '/msgVpns/{msgVpnName}/queues'.replace('{format}', 'json')
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
                                            response_type='MsgVpnQueueResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_queue_subscription(self, msg_vpn_name, queue_name, body, **kwargs):
        """
        Creates a Queue Subscription object.
        Creates a Queue Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_queue_subscription(msg_vpn_name, queue_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param MsgVpnQueueSubscription body: The Queue Subscription object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, body, **kwargs)
            return data

    def create_msg_vpn_queue_subscription_with_http_info(self, msg_vpn_name, queue_name, body, **kwargs):
        """
        Creates a Queue Subscription object.
        Creates a Queue Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param MsgVpnQueueSubscription body: The Queue Subscription object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'queue_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method create_msg_vpn_queue_subscription" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_queue_subscription`")
        # verify the required parameter 'queue_name' is set
        if ('queue_name' not in params) or (params['queue_name'] is None):
            raise ValueError("Missing the required parameter `queue_name` when calling `create_msg_vpn_queue_subscription`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_queue_subscription`")

        resource_path = '/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'queue_name' in params:
            path_params['queueName'] = params['queue_name']

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
                                            response_type='MsgVpnQueueSubscriptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_replicated_topic(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Replicated Topic object.
        Creates a Replicated Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_replicated_topic(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnReplicatedTopic body: The Replicated Topic object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnReplicatedTopicResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def create_msg_vpn_replicated_topic_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Replicated Topic object.
        Creates a Replicated Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnReplicatedTopic body: The Replicated Topic object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnReplicatedTopicResponse
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
                    " to method create_msg_vpn_replicated_topic" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_replicated_topic`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_replicated_topic`")

        resource_path = '/msgVpns/{msgVpnName}/replicatedTopics'.replace('{format}', 'json')
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
                                            response_type='MsgVpnReplicatedTopicResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

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

    def create_msg_vpn_sequenced_topic(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Sequenced Topic object.
        Creates a Sequenced Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| sequencedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_sequenced_topic(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnSequencedTopic body: The Sequenced Topic object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnSequencedTopicResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def create_msg_vpn_sequenced_topic_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Sequenced Topic object.
        Creates a Sequenced Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| sequencedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnSequencedTopic body: The Sequenced Topic object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnSequencedTopicResponse
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
                    " to method create_msg_vpn_sequenced_topic" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_sequenced_topic`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_sequenced_topic`")

        resource_path = '/msgVpns/{msgVpnName}/sequencedTopics'.replace('{format}', 'json')
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
                                            response_type='MsgVpnSequencedTopicResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def create_msg_vpn_topic_endpoint(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Topic Endpoint object.
        Creates a Topic Endpoint object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_topic_endpoint(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnTopicEndpoint body: The Topic Endpoint object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnTopicEndpointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.create_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.create_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def create_msg_vpn_topic_endpoint_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Creates a Topic Endpoint object.
        Creates a Topic Endpoint object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.create_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpnTopicEndpoint body: The Topic Endpoint object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnTopicEndpointResponse
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
                    " to method create_msg_vpn_topic_endpoint" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `create_msg_vpn_topic_endpoint`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `create_msg_vpn_topic_endpoint`")

        resource_path = '/msgVpns/{msgVpnName}/topicEndpoints'.replace('{format}', 'json')
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
                                            response_type='MsgVpnTopicEndpointResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def delete_msg_vpn(self, msg_vpn_name, **kwargs):
        """
        Deletes a Message VPN object.
        Deletes a Message VPN object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.delete_msg_vpn_with_http_info(msg_vpn_name, **kwargs)
            return data

    def delete_msg_vpn_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Deletes a Message VPN object.
        Deletes a Message VPN object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn`")

        resource_path = '/msgVpns/{msgVpnName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']

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

    def delete_msg_vpn_authorization_group(self, msg_vpn_name, authorization_group_name, **kwargs):
        """
        Deletes an LDAP Authorization Group object.
        Deletes an LDAP Authorization Group object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str authorization_group_name: The authorizationGroupName of the LDAP Authorization Group. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, **kwargs)
        else:
            (data) = self.delete_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, **kwargs)
            return data

    def delete_msg_vpn_authorization_group_with_http_info(self, msg_vpn_name, authorization_group_name, **kwargs):
        """
        Deletes an LDAP Authorization Group object.
        Deletes an LDAP Authorization Group object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str authorization_group_name: The authorizationGroupName of the LDAP Authorization Group. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'authorization_group_name']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_authorization_group" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_authorization_group`")
        # verify the required parameter 'authorization_group_name' is set
        if ('authorization_group_name' not in params) or (params['authorization_group_name'] is None):
            raise ValueError("Missing the required parameter `authorization_group_name` when calling `delete_msg_vpn_authorization_group`")

        resource_path = '/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'authorization_group_name' in params:
            path_params['authorizationGroupName'] = params['authorization_group_name']

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

    def delete_msg_vpn_bridge(self, msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs):
        """
        Deletes a Bridge object.
        Deletes a Bridge object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs)
        else:
            (data) = self.delete_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs)
            return data

    def delete_msg_vpn_bridge_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs):
        """
        Deletes a Bridge object.
        Deletes a Bridge object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_bridge" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_bridge`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `delete_msg_vpn_bridge`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `delete_msg_vpn_bridge`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']

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

    def delete_msg_vpn_bridge_remote_msg_vpn(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, **kwargs):
        """
        Deletes a Remote Message VPN object.
        Deletes a Remote Message VPN object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_msg_vpn_name: The remoteMsgVpnName of the Remote Message VPN. (required)
        :param str remote_msg_vpn_location: The remoteMsgVpnLocation of the Remote Message VPN. (required)
        :param str remote_msg_vpn_interface: The remoteMsgVpnInterface of the Remote Message VPN. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, **kwargs)
        else:
            (data) = self.delete_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, **kwargs)
            return data

    def delete_msg_vpn_bridge_remote_msg_vpn_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, **kwargs):
        """
        Deletes a Remote Message VPN object.
        Deletes a Remote Message VPN object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_msg_vpn_name: The remoteMsgVpnName of the Remote Message VPN. (required)
        :param str remote_msg_vpn_location: The remoteMsgVpnLocation of the Remote Message VPN. (required)
        :param str remote_msg_vpn_interface: The remoteMsgVpnInterface of the Remote Message VPN. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'remote_msg_vpn_name', 'remote_msg_vpn_location', 'remote_msg_vpn_interface']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_bridge_remote_msg_vpn" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `delete_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `delete_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_name' is set
        if ('remote_msg_vpn_name' not in params) or (params['remote_msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_name` when calling `delete_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_location' is set
        if ('remote_msg_vpn_location' not in params) or (params['remote_msg_vpn_location'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_location` when calling `delete_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_interface' is set
        if ('remote_msg_vpn_interface' not in params) or (params['remote_msg_vpn_interface'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_interface` when calling `delete_msg_vpn_bridge_remote_msg_vpn`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']
        if 'remote_msg_vpn_name' in params:
            path_params['remoteMsgVpnName'] = params['remote_msg_vpn_name']
        if 'remote_msg_vpn_location' in params:
            path_params['remoteMsgVpnLocation'] = params['remote_msg_vpn_location']
        if 'remote_msg_vpn_interface' in params:
            path_params['remoteMsgVpnInterface'] = params['remote_msg_vpn_interface']

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

    def delete_msg_vpn_bridge_remote_subscription(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, **kwargs):
        """
        Deletes a Remote Subscription object.
        Deletes a Remote Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_subscription_topic: The remoteSubscriptionTopic of the Remote Subscription. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, **kwargs)
        else:
            (data) = self.delete_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, **kwargs)
            return data

    def delete_msg_vpn_bridge_remote_subscription_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, **kwargs):
        """
        Deletes a Remote Subscription object.
        Deletes a Remote Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_subscription_topic: The remoteSubscriptionTopic of the Remote Subscription. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'remote_subscription_topic']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_bridge_remote_subscription" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_bridge_remote_subscription`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `delete_msg_vpn_bridge_remote_subscription`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `delete_msg_vpn_bridge_remote_subscription`")
        # verify the required parameter 'remote_subscription_topic' is set
        if ('remote_subscription_topic' not in params) or (params['remote_subscription_topic'] is None):
            raise ValueError("Missing the required parameter `remote_subscription_topic` when calling `delete_msg_vpn_bridge_remote_subscription`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']
        if 'remote_subscription_topic' in params:
            path_params['remoteSubscriptionTopic'] = params['remote_subscription_topic']

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

    def delete_msg_vpn_bridge_tls_trusted_common_name(self, msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, **kwargs):
        """
        Deletes a Trusted Common Name object.
        Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str tls_trusted_common_name: The tlsTrustedCommonName of the Trusted Common Name. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, **kwargs)
        else:
            (data) = self.delete_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, **kwargs)
            return data

    def delete_msg_vpn_bridge_tls_trusted_common_name_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, **kwargs):
        """
        Deletes a Trusted Common Name object.
        Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str tls_trusted_common_name: The tlsTrustedCommonName of the Trusted Common Name. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'tls_trusted_common_name']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_bridge_tls_trusted_common_name" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_bridge_tls_trusted_common_name`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `delete_msg_vpn_bridge_tls_trusted_common_name`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `delete_msg_vpn_bridge_tls_trusted_common_name`")
        # verify the required parameter 'tls_trusted_common_name' is set
        if ('tls_trusted_common_name' not in params) or (params['tls_trusted_common_name'] is None):
            raise ValueError("Missing the required parameter `tls_trusted_common_name` when calling `delete_msg_vpn_bridge_tls_trusted_common_name`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']
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

    def delete_msg_vpn_client_profile(self, msg_vpn_name, client_profile_name, **kwargs):
        """
        Deletes a Client Profile object.
        Deletes a Client Profile object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_client_profile(msg_vpn_name, client_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_profile_name: The clientProfileName of the Client Profile. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, **kwargs)
        else:
            (data) = self.delete_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, **kwargs)
            return data

    def delete_msg_vpn_client_profile_with_http_info(self, msg_vpn_name, client_profile_name, **kwargs):
        """
        Deletes a Client Profile object.
        Deletes a Client Profile object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_profile_name: The clientProfileName of the Client Profile. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'client_profile_name']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_client_profile" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_client_profile`")
        # verify the required parameter 'client_profile_name' is set
        if ('client_profile_name' not in params) or (params['client_profile_name'] is None):
            raise ValueError("Missing the required parameter `client_profile_name` when calling `delete_msg_vpn_client_profile`")

        resource_path = '/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'client_profile_name' in params:
            path_params['clientProfileName'] = params['client_profile_name']

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

    def delete_msg_vpn_client_username(self, msg_vpn_name, client_username, **kwargs):
        """
        Deletes a Client Username object.
        Deletes a Client Username object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_client_username(msg_vpn_name, client_username, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_username: The clientUsername of the Client Username. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, **kwargs)
        else:
            (data) = self.delete_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, **kwargs)
            return data

    def delete_msg_vpn_client_username_with_http_info(self, msg_vpn_name, client_username, **kwargs):
        """
        Deletes a Client Username object.
        Deletes a Client Username object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_username: The clientUsername of the Client Username. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'client_username']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_client_username" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_client_username`")
        # verify the required parameter 'client_username' is set
        if ('client_username' not in params) or (params['client_username'] is None):
            raise ValueError("Missing the required parameter `client_username` when calling `delete_msg_vpn_client_username`")

        resource_path = '/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'client_username' in params:
            path_params['clientUsername'] = params['client_username']

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

    def delete_msg_vpn_mqtt_session(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs):
        """
        Deletes an MQTT Session object.
        Deletes an MQTT Session object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs)
        else:
            (data) = self.delete_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs)
            return data

    def delete_msg_vpn_mqtt_session_with_http_info(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs):
        """
        Deletes an MQTT Session object.
        Deletes an MQTT Session object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'mqtt_session_client_id', 'mqtt_session_virtual_router']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_mqtt_session" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_mqtt_session`")
        # verify the required parameter 'mqtt_session_client_id' is set
        if ('mqtt_session_client_id' not in params) or (params['mqtt_session_client_id'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_client_id` when calling `delete_msg_vpn_mqtt_session`")
        # verify the required parameter 'mqtt_session_virtual_router' is set
        if ('mqtt_session_virtual_router' not in params) or (params['mqtt_session_virtual_router'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_virtual_router` when calling `delete_msg_vpn_mqtt_session`")

        resource_path = '/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'mqtt_session_client_id' in params:
            path_params['mqttSessionClientId'] = params['mqtt_session_client_id']
        if 'mqtt_session_virtual_router' in params:
            path_params['mqttSessionVirtualRouter'] = params['mqtt_session_virtual_router']

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

    def delete_msg_vpn_mqtt_session_subscription(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, **kwargs):
        """
        Deletes an MQTT Session Subscription object.
        Deletes an MQTT Session Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param str subscription_topic: The subscriptionTopic of the MQTT Session Subscription. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, **kwargs)
        else:
            (data) = self.delete_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, **kwargs)
            return data

    def delete_msg_vpn_mqtt_session_subscription_with_http_info(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, **kwargs):
        """
        Deletes an MQTT Session Subscription object.
        Deletes an MQTT Session Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param str subscription_topic: The subscriptionTopic of the MQTT Session Subscription. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'mqtt_session_client_id', 'mqtt_session_virtual_router', 'subscription_topic']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_mqtt_session_subscription" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'mqtt_session_client_id' is set
        if ('mqtt_session_client_id' not in params) or (params['mqtt_session_client_id'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_client_id` when calling `delete_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'mqtt_session_virtual_router' is set
        if ('mqtt_session_virtual_router' not in params) or (params['mqtt_session_virtual_router'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_virtual_router` when calling `delete_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'subscription_topic' is set
        if ('subscription_topic' not in params) or (params['subscription_topic'] is None):
            raise ValueError("Missing the required parameter `subscription_topic` when calling `delete_msg_vpn_mqtt_session_subscription`")

        resource_path = '/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'mqtt_session_client_id' in params:
            path_params['mqttSessionClientId'] = params['mqtt_session_client_id']
        if 'mqtt_session_virtual_router' in params:
            path_params['mqttSessionVirtualRouter'] = params['mqtt_session_virtual_router']
        if 'subscription_topic' in params:
            path_params['subscriptionTopic'] = params['subscription_topic']

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

    def delete_msg_vpn_queue(self, msg_vpn_name, queue_name, **kwargs):
        """
        Deletes a Queue object.
        Deletes a Queue object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_queue(msg_vpn_name, queue_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, **kwargs)
        else:
            (data) = self.delete_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, **kwargs)
            return data

    def delete_msg_vpn_queue_with_http_info(self, msg_vpn_name, queue_name, **kwargs):
        """
        Deletes a Queue object.
        Deletes a Queue object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'queue_name']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_queue" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_queue`")
        # verify the required parameter 'queue_name' is set
        if ('queue_name' not in params) or (params['queue_name'] is None):
            raise ValueError("Missing the required parameter `queue_name` when calling `delete_msg_vpn_queue`")

        resource_path = '/msgVpns/{msgVpnName}/queues/{queueName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'queue_name' in params:
            path_params['queueName'] = params['queue_name']

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

    def delete_msg_vpn_queue_subscription(self, msg_vpn_name, queue_name, subscription_topic, **kwargs):
        """
        Deletes a Queue Subscription object.
        Deletes a Queue Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param str subscription_topic: The subscriptionTopic of the Queue Subscription. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, subscription_topic, **kwargs)
        else:
            (data) = self.delete_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, subscription_topic, **kwargs)
            return data

    def delete_msg_vpn_queue_subscription_with_http_info(self, msg_vpn_name, queue_name, subscription_topic, **kwargs):
        """
        Deletes a Queue Subscription object.
        Deletes a Queue Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param str subscription_topic: The subscriptionTopic of the Queue Subscription. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'queue_name', 'subscription_topic']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_queue_subscription" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_queue_subscription`")
        # verify the required parameter 'queue_name' is set
        if ('queue_name' not in params) or (params['queue_name'] is None):
            raise ValueError("Missing the required parameter `queue_name` when calling `delete_msg_vpn_queue_subscription`")
        # verify the required parameter 'subscription_topic' is set
        if ('subscription_topic' not in params) or (params['subscription_topic'] is None):
            raise ValueError("Missing the required parameter `subscription_topic` when calling `delete_msg_vpn_queue_subscription`")

        resource_path = '/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'queue_name' in params:
            path_params['queueName'] = params['queue_name']
        if 'subscription_topic' in params:
            path_params['subscriptionTopic'] = params['subscription_topic']

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

    def delete_msg_vpn_replicated_topic(self, msg_vpn_name, replicated_topic, **kwargs):
        """
        Deletes a Replicated Topic object.
        Deletes a Replicated Topic object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str replicated_topic: The replicatedTopic of the Replicated Topic. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, **kwargs)
        else:
            (data) = self.delete_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, **kwargs)
            return data

    def delete_msg_vpn_replicated_topic_with_http_info(self, msg_vpn_name, replicated_topic, **kwargs):
        """
        Deletes a Replicated Topic object.
        Deletes a Replicated Topic object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str replicated_topic: The replicatedTopic of the Replicated Topic. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'replicated_topic']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_replicated_topic" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_replicated_topic`")
        # verify the required parameter 'replicated_topic' is set
        if ('replicated_topic' not in params) or (params['replicated_topic'] is None):
            raise ValueError("Missing the required parameter `replicated_topic` when calling `delete_msg_vpn_replicated_topic`")

        resource_path = '/msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'replicated_topic' in params:
            path_params['replicatedTopic'] = params['replicated_topic']

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

    def delete_msg_vpn_sequenced_topic(self, msg_vpn_name, sequenced_topic, **kwargs):
        """
        Deletes a Sequenced Topic object.
        Deletes a Sequenced Topic object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_sequenced_topic(msg_vpn_name, sequenced_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str sequenced_topic: The sequencedTopic of the Sequenced Topic. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, sequenced_topic, **kwargs)
        else:
            (data) = self.delete_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, sequenced_topic, **kwargs)
            return data

    def delete_msg_vpn_sequenced_topic_with_http_info(self, msg_vpn_name, sequenced_topic, **kwargs):
        """
        Deletes a Sequenced Topic object.
        Deletes a Sequenced Topic object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, sequenced_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str sequenced_topic: The sequencedTopic of the Sequenced Topic. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'sequenced_topic']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_sequenced_topic" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_sequenced_topic`")
        # verify the required parameter 'sequenced_topic' is set
        if ('sequenced_topic' not in params) or (params['sequenced_topic'] is None):
            raise ValueError("Missing the required parameter `sequenced_topic` when calling `delete_msg_vpn_sequenced_topic`")

        resource_path = '/msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'sequenced_topic' in params:
            path_params['sequencedTopic'] = params['sequenced_topic']

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

    def delete_msg_vpn_topic_endpoint(self, msg_vpn_name, topic_endpoint_name, **kwargs):
        """
        Deletes a Topic Endpoint object.
        Deletes a Topic Endpoint object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str topic_endpoint_name: The topicEndpointName of the Topic Endpoint. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.delete_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, **kwargs)
        else:
            (data) = self.delete_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, **kwargs)
            return data

    def delete_msg_vpn_topic_endpoint_with_http_info(self, msg_vpn_name, topic_endpoint_name, **kwargs):
        """
        Deletes a Topic Endpoint object.
        Deletes a Topic Endpoint object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.delete_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str topic_endpoint_name: The topicEndpointName of the Topic Endpoint. (required)
        :return: SempMetaOnlyResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'topic_endpoint_name']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method delete_msg_vpn_topic_endpoint" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `delete_msg_vpn_topic_endpoint`")
        # verify the required parameter 'topic_endpoint_name' is set
        if ('topic_endpoint_name' not in params) or (params['topic_endpoint_name'] is None):
            raise ValueError("Missing the required parameter `topic_endpoint_name` when calling `delete_msg_vpn_topic_endpoint`")

        resource_path = '/msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'topic_endpoint_name' in params:
            path_params['topicEndpointName'] = params['topic_endpoint_name']

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

    def get_msg_vpn(self, msg_vpn_name, **kwargs):
        """
        Gets a Message VPN object.
        Gets a Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a Message VPN object.
        Gets a Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn`")

        resource_path = '/msgVpns/{msgVpnName}'.replace('{format}', 'json')
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
                                            response_type='MsgVpnResponse',
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

    def get_msg_vpn_authorization_group(self, msg_vpn_name, authorization_group_name, **kwargs):
        """
        Gets an LDAP Authorization Group object.
        Gets an LDAP Authorization Group object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str authorization_group_name: The authorizationGroupName of the LDAP Authorization Group. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAuthorizationGroupResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, **kwargs)
            return data

    def get_msg_vpn_authorization_group_with_http_info(self, msg_vpn_name, authorization_group_name, **kwargs):
        """
        Gets an LDAP Authorization Group object.
        Gets an LDAP Authorization Group object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str authorization_group_name: The authorizationGroupName of the LDAP Authorization Group. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAuthorizationGroupResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'authorization_group_name', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_authorization_group" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_authorization_group`")
        # verify the required parameter 'authorization_group_name' is set
        if ('authorization_group_name' not in params) or (params['authorization_group_name'] is None):
            raise ValueError("Missing the required parameter `authorization_group_name` when calling `get_msg_vpn_authorization_group`")

        resource_path = '/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'authorization_group_name' in params:
            path_params['authorizationGroupName'] = params['authorization_group_name']

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
                                            response_type='MsgVpnAuthorizationGroupResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_authorization_groups(self, msg_vpn_name, **kwargs):
        """
        Gets a list of LDAP Authorization Group objects.
        Gets a list of LDAP Authorization Group objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_authorization_groups(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAuthorizationGroupsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_authorization_groups_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_authorization_groups_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_authorization_groups_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a list of LDAP Authorization Group objects.
        Gets a list of LDAP Authorization Group objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_authorization_groups_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAuthorizationGroupsResponse
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
                    " to method get_msg_vpn_authorization_groups" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_authorization_groups`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_authorization_groups`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/authorizationGroups'.replace('{format}', 'json')
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
                                            response_type='MsgVpnAuthorizationGroupsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_bridge(self, msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs):
        """
        Gets a Bridge object.
        Gets a Bridge object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs)
        else:
            (data) = self.get_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs)
            return data

    def get_msg_vpn_bridge_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs):
        """
        Gets a Bridge object.
        Gets a Bridge object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_bridge" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_bridge`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `get_msg_vpn_bridge`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `get_msg_vpn_bridge`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']

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
                                            response_type='MsgVpnBridgeResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_bridge_remote_msg_vpn(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, **kwargs):
        """
        Gets a Remote Message VPN object.
        Gets a Remote Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_msg_vpn_name: The remoteMsgVpnName of the Remote Message VPN. (required)
        :param str remote_msg_vpn_location: The remoteMsgVpnLocation of the Remote Message VPN. (required)
        :param str remote_msg_vpn_interface: The remoteMsgVpnInterface of the Remote Message VPN. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteMsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, **kwargs)
        else:
            (data) = self.get_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, **kwargs)
            return data

    def get_msg_vpn_bridge_remote_msg_vpn_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, **kwargs):
        """
        Gets a Remote Message VPN object.
        Gets a Remote Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_msg_vpn_name: The remoteMsgVpnName of the Remote Message VPN. (required)
        :param str remote_msg_vpn_location: The remoteMsgVpnLocation of the Remote Message VPN. (required)
        :param str remote_msg_vpn_interface: The remoteMsgVpnInterface of the Remote Message VPN. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteMsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'remote_msg_vpn_name', 'remote_msg_vpn_location', 'remote_msg_vpn_interface', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_bridge_remote_msg_vpn" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `get_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `get_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_name' is set
        if ('remote_msg_vpn_name' not in params) or (params['remote_msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_name` when calling `get_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_location' is set
        if ('remote_msg_vpn_location' not in params) or (params['remote_msg_vpn_location'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_location` when calling `get_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_interface' is set
        if ('remote_msg_vpn_interface' not in params) or (params['remote_msg_vpn_interface'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_interface` when calling `get_msg_vpn_bridge_remote_msg_vpn`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']
        if 'remote_msg_vpn_name' in params:
            path_params['remoteMsgVpnName'] = params['remote_msg_vpn_name']
        if 'remote_msg_vpn_location' in params:
            path_params['remoteMsgVpnLocation'] = params['remote_msg_vpn_location']
        if 'remote_msg_vpn_interface' in params:
            path_params['remoteMsgVpnInterface'] = params['remote_msg_vpn_interface']

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
                                            response_type='MsgVpnBridgeRemoteMsgVpnResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_bridge_remote_msg_vpns(self, msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs):
        """
        Gets a list of Remote Message VPN objects.
        Gets a list of Remote Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_remote_msg_vpns(msg_vpn_name, bridge_name, bridge_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteMsgVpnsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_bridge_remote_msg_vpns_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs)
        else:
            (data) = self.get_msg_vpn_bridge_remote_msg_vpns_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs)
            return data

    def get_msg_vpn_bridge_remote_msg_vpns_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs):
        """
        Gets a list of Remote Message VPN objects.
        Gets a list of Remote Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_remote_msg_vpns_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteMsgVpnsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_bridge_remote_msg_vpns" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_bridge_remote_msg_vpns`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `get_msg_vpn_bridge_remote_msg_vpns`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `get_msg_vpn_bridge_remote_msg_vpns`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']

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
                                            response_type='MsgVpnBridgeRemoteMsgVpnsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_bridge_remote_subscription(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, **kwargs):
        """
        Gets a Remote Subscription object.
        Gets a Remote Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_subscription_topic: The remoteSubscriptionTopic of the Remote Subscription. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, **kwargs)
        else:
            (data) = self.get_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, **kwargs)
            return data

    def get_msg_vpn_bridge_remote_subscription_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, **kwargs):
        """
        Gets a Remote Subscription object.
        Gets a Remote Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_subscription_topic: The remoteSubscriptionTopic of the Remote Subscription. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'remote_subscription_topic', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_bridge_remote_subscription" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_bridge_remote_subscription`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `get_msg_vpn_bridge_remote_subscription`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `get_msg_vpn_bridge_remote_subscription`")
        # verify the required parameter 'remote_subscription_topic' is set
        if ('remote_subscription_topic' not in params) or (params['remote_subscription_topic'] is None):
            raise ValueError("Missing the required parameter `remote_subscription_topic` when calling `get_msg_vpn_bridge_remote_subscription`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']
        if 'remote_subscription_topic' in params:
            path_params['remoteSubscriptionTopic'] = params['remote_subscription_topic']

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
                                            response_type='MsgVpnBridgeRemoteSubscriptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_bridge_remote_subscriptions(self, msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs):
        """
        Gets a list of Remote Subscription objects.
        Gets a list of Remote Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_remote_subscriptions(msg_vpn_name, bridge_name, bridge_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteSubscriptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_bridge_remote_subscriptions_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs)
        else:
            (data) = self.get_msg_vpn_bridge_remote_subscriptions_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs)
            return data

    def get_msg_vpn_bridge_remote_subscriptions_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs):
        """
        Gets a list of Remote Subscription objects.
        Gets a list of Remote Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_remote_subscriptions_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteSubscriptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'count', 'cursor', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_bridge_remote_subscriptions" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_bridge_remote_subscriptions`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `get_msg_vpn_bridge_remote_subscriptions`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `get_msg_vpn_bridge_remote_subscriptions`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_bridge_remote_subscriptions`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']

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
                                            response_type='MsgVpnBridgeRemoteSubscriptionsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_bridge_tls_trusted_common_name(self, msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, **kwargs):
        """
        Gets a Trusted Common Name object.
        Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str tls_trusted_common_name: The tlsTrustedCommonName of the Trusted Common Name. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeTlsTrustedCommonNameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, **kwargs)
            return data

    def get_msg_vpn_bridge_tls_trusted_common_name_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, **kwargs):
        """
        Gets a Trusted Common Name object.
        Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str tls_trusted_common_name: The tlsTrustedCommonName of the Trusted Common Name. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeTlsTrustedCommonNameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'tls_trusted_common_name', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_bridge_tls_trusted_common_name" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_bridge_tls_trusted_common_name`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `get_msg_vpn_bridge_tls_trusted_common_name`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `get_msg_vpn_bridge_tls_trusted_common_name`")
        # verify the required parameter 'tls_trusted_common_name' is set
        if ('tls_trusted_common_name' not in params) or (params['tls_trusted_common_name'] is None):
            raise ValueError("Missing the required parameter `tls_trusted_common_name` when calling `get_msg_vpn_bridge_tls_trusted_common_name`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']
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
                                            response_type='MsgVpnBridgeTlsTrustedCommonNameResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_bridge_tls_trusted_common_names(self, msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs):
        """
        Gets a list of Trusted Common Name objects.
        Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_tls_trusted_common_names(msg_vpn_name, bridge_name, bridge_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeTlsTrustedCommonNamesResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_bridge_tls_trusted_common_names_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs)
        else:
            (data) = self.get_msg_vpn_bridge_tls_trusted_common_names_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs)
            return data

    def get_msg_vpn_bridge_tls_trusted_common_names_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, **kwargs):
        """
        Gets a list of Trusted Common Name objects.
        Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridge_tls_trusted_common_names_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeTlsTrustedCommonNamesResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_bridge_tls_trusted_common_names" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_bridge_tls_trusted_common_names`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `get_msg_vpn_bridge_tls_trusted_common_names`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `get_msg_vpn_bridge_tls_trusted_common_names`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']

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
                                            response_type='MsgVpnBridgeTlsTrustedCommonNamesResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_bridges(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Bridge objects.
        Gets a list of Bridge objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridges(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgesResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_bridges_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_bridges_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_bridges_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Bridge objects.
        Gets a list of Bridge objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_bridges_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgesResponse
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
                    " to method get_msg_vpn_bridges" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_bridges`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_bridges`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/bridges'.replace('{format}', 'json')
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
                                            response_type='MsgVpnBridgesResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_client_profile(self, msg_vpn_name, client_profile_name, **kwargs):
        """
        Gets a Client Profile object.
        Gets a Client Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_client_profile(msg_vpn_name, client_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_profile_name: The clientProfileName of the Client Profile. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, **kwargs)
            return data

    def get_msg_vpn_client_profile_with_http_info(self, msg_vpn_name, client_profile_name, **kwargs):
        """
        Gets a Client Profile object.
        Gets a Client Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_profile_name: The clientProfileName of the Client Profile. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'client_profile_name', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_client_profile" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_client_profile`")
        # verify the required parameter 'client_profile_name' is set
        if ('client_profile_name' not in params) or (params['client_profile_name'] is None):
            raise ValueError("Missing the required parameter `client_profile_name` when calling `get_msg_vpn_client_profile`")

        resource_path = '/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'client_profile_name' in params:
            path_params['clientProfileName'] = params['client_profile_name']

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
                                            response_type='MsgVpnClientProfileResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_client_profiles(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Client Profile objects.
        Gets a list of Client Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_client_profiles(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientProfilesResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_client_profiles_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_client_profiles_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_client_profiles_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Client Profile objects.
        Gets a list of Client Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_client_profiles_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientProfilesResponse
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
                    " to method get_msg_vpn_client_profiles" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_client_profiles`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_client_profiles`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/clientProfiles'.replace('{format}', 'json')
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
                                            response_type='MsgVpnClientProfilesResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_client_username(self, msg_vpn_name, client_username, **kwargs):
        """
        Gets a Client Username object.
        Gets a Client Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_client_username(msg_vpn_name, client_username, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_username: The clientUsername of the Client Username. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientUsernameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, **kwargs)
        else:
            (data) = self.get_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, **kwargs)
            return data

    def get_msg_vpn_client_username_with_http_info(self, msg_vpn_name, client_username, **kwargs):
        """
        Gets a Client Username object.
        Gets a Client Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_username: The clientUsername of the Client Username. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientUsernameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'client_username', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_client_username" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_client_username`")
        # verify the required parameter 'client_username' is set
        if ('client_username' not in params) or (params['client_username'] is None):
            raise ValueError("Missing the required parameter `client_username` when calling `get_msg_vpn_client_username`")

        resource_path = '/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'client_username' in params:
            path_params['clientUsername'] = params['client_username']

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
                                            response_type='MsgVpnClientUsernameResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_client_usernames(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Client Username objects.
        Gets a list of Client Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_client_usernames(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientUsernamesResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_client_usernames_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_client_usernames_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_client_usernames_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Client Username objects.
        Gets a list of Client Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_client_usernames_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientUsernamesResponse
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
                    " to method get_msg_vpn_client_usernames" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_client_usernames`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_client_usernames`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/clientUsernames'.replace('{format}', 'json')
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
                                            response_type='MsgVpnClientUsernamesResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_mqtt_session(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs):
        """
        Gets an MQTT Session object.
        Gets an MQTT Session object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs)
        else:
            (data) = self.get_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs)
            return data

    def get_msg_vpn_mqtt_session_with_http_info(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs):
        """
        Gets an MQTT Session object.
        Gets an MQTT Session object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'mqtt_session_client_id', 'mqtt_session_virtual_router', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_mqtt_session" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_mqtt_session`")
        # verify the required parameter 'mqtt_session_client_id' is set
        if ('mqtt_session_client_id' not in params) or (params['mqtt_session_client_id'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_client_id` when calling `get_msg_vpn_mqtt_session`")
        # verify the required parameter 'mqtt_session_virtual_router' is set
        if ('mqtt_session_virtual_router' not in params) or (params['mqtt_session_virtual_router'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_virtual_router` when calling `get_msg_vpn_mqtt_session`")

        resource_path = '/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'mqtt_session_client_id' in params:
            path_params['mqttSessionClientId'] = params['mqtt_session_client_id']
        if 'mqtt_session_virtual_router' in params:
            path_params['mqttSessionVirtualRouter'] = params['mqtt_session_virtual_router']

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
                                            response_type='MsgVpnMqttSessionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_mqtt_session_subscription(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, **kwargs):
        """
        Gets an MQTT Session Subscription object.
        Gets an MQTT Session Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param str subscription_topic: The subscriptionTopic of the MQTT Session Subscription. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, **kwargs)
        else:
            (data) = self.get_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, **kwargs)
            return data

    def get_msg_vpn_mqtt_session_subscription_with_http_info(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, **kwargs):
        """
        Gets an MQTT Session Subscription object.
        Gets an MQTT Session Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param str subscription_topic: The subscriptionTopic of the MQTT Session Subscription. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'mqtt_session_client_id', 'mqtt_session_virtual_router', 'subscription_topic', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_mqtt_session_subscription" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'mqtt_session_client_id' is set
        if ('mqtt_session_client_id' not in params) or (params['mqtt_session_client_id'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_client_id` when calling `get_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'mqtt_session_virtual_router' is set
        if ('mqtt_session_virtual_router' not in params) or (params['mqtt_session_virtual_router'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_virtual_router` when calling `get_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'subscription_topic' is set
        if ('subscription_topic' not in params) or (params['subscription_topic'] is None):
            raise ValueError("Missing the required parameter `subscription_topic` when calling `get_msg_vpn_mqtt_session_subscription`")

        resource_path = '/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'mqtt_session_client_id' in params:
            path_params['mqttSessionClientId'] = params['mqtt_session_client_id']
        if 'mqtt_session_virtual_router' in params:
            path_params['mqttSessionVirtualRouter'] = params['mqtt_session_virtual_router']
        if 'subscription_topic' in params:
            path_params['subscriptionTopic'] = params['subscription_topic']

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
                                            response_type='MsgVpnMqttSessionSubscriptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_mqtt_session_subscriptions(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs):
        """
        Gets a list of MQTT Session Subscription objects.
        Gets a list of MQTT Session Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_mqtt_session_subscriptions(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionSubscriptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_mqtt_session_subscriptions_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs)
        else:
            (data) = self.get_msg_vpn_mqtt_session_subscriptions_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs)
            return data

    def get_msg_vpn_mqtt_session_subscriptions_with_http_info(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, **kwargs):
        """
        Gets a list of MQTT Session Subscription objects.
        Gets a list of MQTT Session Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_mqtt_session_subscriptions_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionSubscriptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'mqtt_session_client_id', 'mqtt_session_virtual_router', 'count', 'cursor', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_mqtt_session_subscriptions" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_mqtt_session_subscriptions`")
        # verify the required parameter 'mqtt_session_client_id' is set
        if ('mqtt_session_client_id' not in params) or (params['mqtt_session_client_id'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_client_id` when calling `get_msg_vpn_mqtt_session_subscriptions`")
        # verify the required parameter 'mqtt_session_virtual_router' is set
        if ('mqtt_session_virtual_router' not in params) or (params['mqtt_session_virtual_router'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_virtual_router` when calling `get_msg_vpn_mqtt_session_subscriptions`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_mqtt_session_subscriptions`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'mqtt_session_client_id' in params:
            path_params['mqttSessionClientId'] = params['mqtt_session_client_id']
        if 'mqtt_session_virtual_router' in params:
            path_params['mqttSessionVirtualRouter'] = params['mqtt_session_virtual_router']

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
                                            response_type='MsgVpnMqttSessionSubscriptionsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_mqtt_sessions(self, msg_vpn_name, **kwargs):
        """
        Gets a list of MQTT Session objects.
        Gets a list of MQTT Session objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_mqtt_sessions(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_mqtt_sessions_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_mqtt_sessions_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_mqtt_sessions_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a list of MQTT Session objects.
        Gets a list of MQTT Session objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_mqtt_sessions_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionsResponse
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
                    " to method get_msg_vpn_mqtt_sessions" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_mqtt_sessions`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_mqtt_sessions`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/mqttSessions'.replace('{format}', 'json')
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
                                            response_type='MsgVpnMqttSessionsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_queue(self, msg_vpn_name, queue_name, **kwargs):
        """
        Gets a Queue object.
        Gets a Queue object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_queue(msg_vpn_name, queue_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, **kwargs)
            return data

    def get_msg_vpn_queue_with_http_info(self, msg_vpn_name, queue_name, **kwargs):
        """
        Gets a Queue object.
        Gets a Queue object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'queue_name', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_queue" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_queue`")
        # verify the required parameter 'queue_name' is set
        if ('queue_name' not in params) or (params['queue_name'] is None):
            raise ValueError("Missing the required parameter `queue_name` when calling `get_msg_vpn_queue`")

        resource_path = '/msgVpns/{msgVpnName}/queues/{queueName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'queue_name' in params:
            path_params['queueName'] = params['queue_name']

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
                                            response_type='MsgVpnQueueResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_queue_subscription(self, msg_vpn_name, queue_name, subscription_topic, **kwargs):
        """
        Gets a Queue Subscription object.
        Gets a Queue Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param str subscription_topic: The subscriptionTopic of the Queue Subscription. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, subscription_topic, **kwargs)
        else:
            (data) = self.get_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, subscription_topic, **kwargs)
            return data

    def get_msg_vpn_queue_subscription_with_http_info(self, msg_vpn_name, queue_name, subscription_topic, **kwargs):
        """
        Gets a Queue Subscription object.
        Gets a Queue Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, subscription_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param str subscription_topic: The subscriptionTopic of the Queue Subscription. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'queue_name', 'subscription_topic', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_queue_subscription" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_queue_subscription`")
        # verify the required parameter 'queue_name' is set
        if ('queue_name' not in params) or (params['queue_name'] is None):
            raise ValueError("Missing the required parameter `queue_name` when calling `get_msg_vpn_queue_subscription`")
        # verify the required parameter 'subscription_topic' is set
        if ('subscription_topic' not in params) or (params['subscription_topic'] is None):
            raise ValueError("Missing the required parameter `subscription_topic` when calling `get_msg_vpn_queue_subscription`")

        resource_path = '/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'queue_name' in params:
            path_params['queueName'] = params['queue_name']
        if 'subscription_topic' in params:
            path_params['subscriptionTopic'] = params['subscription_topic']

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
                                            response_type='MsgVpnQueueSubscriptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_queue_subscriptions(self, msg_vpn_name, queue_name, **kwargs):
        """
        Gets a list of Queue Subscription objects.
        Gets a list of Queue Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_queue_subscriptions(msg_vpn_name, queue_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueSubscriptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_queue_subscriptions_with_http_info(msg_vpn_name, queue_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_queue_subscriptions_with_http_info(msg_vpn_name, queue_name, **kwargs)
            return data

    def get_msg_vpn_queue_subscriptions_with_http_info(self, msg_vpn_name, queue_name, **kwargs):
        """
        Gets a list of Queue Subscription objects.
        Gets a list of Queue Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_queue_subscriptions_with_http_info(msg_vpn_name, queue_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueSubscriptionsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'queue_name', 'count', 'cursor', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_queue_subscriptions" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_queue_subscriptions`")
        # verify the required parameter 'queue_name' is set
        if ('queue_name' not in params) or (params['queue_name'] is None):
            raise ValueError("Missing the required parameter `queue_name` when calling `get_msg_vpn_queue_subscriptions`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_queue_subscriptions`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'queue_name' in params:
            path_params['queueName'] = params['queue_name']

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
                                            response_type='MsgVpnQueueSubscriptionsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_queues(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Queue objects.
        Gets a list of Queue objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_queues(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueuesResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_queues_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_queues_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_queues_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Queue objects.
        Gets a list of Queue objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_queues_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueuesResponse
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
                    " to method get_msg_vpn_queues" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_queues`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_queues`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/queues'.replace('{format}', 'json')
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
                                            response_type='MsgVpnQueuesResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_replicated_topic(self, msg_vpn_name, replicated_topic, **kwargs):
        """
        Gets a Replicated Topic object.
        Gets a Replicated Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicatedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str replicated_topic: The replicatedTopic of the Replicated Topic. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnReplicatedTopicResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, **kwargs)
        else:
            (data) = self.get_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, **kwargs)
            return data

    def get_msg_vpn_replicated_topic_with_http_info(self, msg_vpn_name, replicated_topic, **kwargs):
        """
        Gets a Replicated Topic object.
        Gets a Replicated Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicatedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str replicated_topic: The replicatedTopic of the Replicated Topic. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnReplicatedTopicResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'replicated_topic', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_replicated_topic" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_replicated_topic`")
        # verify the required parameter 'replicated_topic' is set
        if ('replicated_topic' not in params) or (params['replicated_topic'] is None):
            raise ValueError("Missing the required parameter `replicated_topic` when calling `get_msg_vpn_replicated_topic`")

        resource_path = '/msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'replicated_topic' in params:
            path_params['replicatedTopic'] = params['replicated_topic']

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
                                            response_type='MsgVpnReplicatedTopicResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_replicated_topics(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Replicated Topic objects.
        Gets a list of Replicated Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicatedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_replicated_topics(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnReplicatedTopicsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_replicated_topics_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_replicated_topics_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_replicated_topics_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Replicated Topic objects.
        Gets a list of Replicated Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicatedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_replicated_topics_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnReplicatedTopicsResponse
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
                    " to method get_msg_vpn_replicated_topics" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_replicated_topics`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_replicated_topics`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/replicatedTopics'.replace('{format}', 'json')
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
                                            response_type='MsgVpnReplicatedTopicsResponse',
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

    def get_msg_vpn_sequenced_topic(self, msg_vpn_name, sequenced_topic, **kwargs):
        """
        Gets a Sequenced Topic object.
        Gets a Sequenced Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_sequenced_topic(msg_vpn_name, sequenced_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str sequenced_topic: The sequencedTopic of the Sequenced Topic. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnSequencedTopicResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, sequenced_topic, **kwargs)
        else:
            (data) = self.get_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, sequenced_topic, **kwargs)
            return data

    def get_msg_vpn_sequenced_topic_with_http_info(self, msg_vpn_name, sequenced_topic, **kwargs):
        """
        Gets a Sequenced Topic object.
        Gets a Sequenced Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, sequenced_topic, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str sequenced_topic: The sequencedTopic of the Sequenced Topic. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnSequencedTopicResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'sequenced_topic', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_sequenced_topic" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_sequenced_topic`")
        # verify the required parameter 'sequenced_topic' is set
        if ('sequenced_topic' not in params) or (params['sequenced_topic'] is None):
            raise ValueError("Missing the required parameter `sequenced_topic` when calling `get_msg_vpn_sequenced_topic`")

        resource_path = '/msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'sequenced_topic' in params:
            path_params['sequencedTopic'] = params['sequenced_topic']

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
                                            response_type='MsgVpnSequencedTopicResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_sequenced_topics(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Sequenced Topic objects.
        Gets a list of Sequenced Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_sequenced_topics(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnSequencedTopicsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_sequenced_topics_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_sequenced_topics_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_sequenced_topics_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Sequenced Topic objects.
        Gets a list of Sequenced Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_sequenced_topics_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnSequencedTopicsResponse
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
                    " to method get_msg_vpn_sequenced_topics" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_sequenced_topics`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_sequenced_topics`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/sequencedTopics'.replace('{format}', 'json')
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
                                            response_type='MsgVpnSequencedTopicsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_topic_endpoint(self, msg_vpn_name, topic_endpoint_name, **kwargs):
        """
        Gets a Topic Endpoint object.
        Gets a Topic Endpoint object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| topicEndpointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str topic_endpoint_name: The topicEndpointName of the Topic Endpoint. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnTopicEndpointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, **kwargs)
            return data

    def get_msg_vpn_topic_endpoint_with_http_info(self, msg_vpn_name, topic_endpoint_name, **kwargs):
        """
        Gets a Topic Endpoint object.
        Gets a Topic Endpoint object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| topicEndpointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str topic_endpoint_name: The topicEndpointName of the Topic Endpoint. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnTopicEndpointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'topic_endpoint_name', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpn_topic_endpoint" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_topic_endpoint`")
        # verify the required parameter 'topic_endpoint_name' is set
        if ('topic_endpoint_name' not in params) or (params['topic_endpoint_name'] is None):
            raise ValueError("Missing the required parameter `topic_endpoint_name` when calling `get_msg_vpn_topic_endpoint`")

        resource_path = '/msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'topic_endpoint_name' in params:
            path_params['topicEndpointName'] = params['topic_endpoint_name']

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
                                            response_type='MsgVpnTopicEndpointResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpn_topic_endpoints(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Topic Endpoint objects.
        Gets a list of Topic Endpoint objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| topicEndpointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_topic_endpoints(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnTopicEndpointsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpn_topic_endpoints_with_http_info(msg_vpn_name, **kwargs)
        else:
            (data) = self.get_msg_vpn_topic_endpoints_with_http_info(msg_vpn_name, **kwargs)
            return data

    def get_msg_vpn_topic_endpoints_with_http_info(self, msg_vpn_name, **kwargs):
        """
        Gets a list of Topic Endpoint objects.
        Gets a list of Topic Endpoint objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| topicEndpointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpn_topic_endpoints_with_http_info(msg_vpn_name, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnTopicEndpointsResponse
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
                    " to method get_msg_vpn_topic_endpoints" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `get_msg_vpn_topic_endpoints`")

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpn_topic_endpoints`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns/{msgVpnName}/topicEndpoints'.replace('{format}', 'json')
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
                                            response_type='MsgVpnTopicEndpointsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def get_msg_vpns(self, **kwargs):
        """
        Gets a list of Message VPN objects.
        Gets a list of Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpns(callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.get_msg_vpns_with_http_info(**kwargs)
        else:
            (data) = self.get_msg_vpns_with_http_info(**kwargs)
            return data

    def get_msg_vpns_with_http_info(self, **kwargs):
        """
        Gets a list of Message VPN objects.
        Gets a list of Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.get_msg_vpns_with_http_info(callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param int count: Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\").
        :param str cursor: The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\").
        :param list[str] where: Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\").
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnsResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['count', 'cursor', 'where', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method get_msg_vpns" % key
                )
            params[key] = val
        del params['kwargs']

        if 'count' in params and params['count'] < 1.0:
            raise ValueError("Invalid value for parameter `count` when calling `get_msg_vpns`, must be a value greater than or equal to `1.0`")
        resource_path = '/msgVpns'.replace('{format}', 'json')
        path_params = {}

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
                                            response_type='MsgVpnsResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn(self, msg_vpn_name, body, **kwargs):
        """
        Replaces a Message VPN object.
        Replaces a Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpn body: The Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def replace_msg_vpn_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Replaces a Message VPN object.
        Replaces a Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpn body: The Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnResponse
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
                    " to method replace_msg_vpn" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn`")

        resource_path = '/msgVpns/{msgVpnName}'.replace('{format}', 'json')
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

        return self.api_client.call_api(resource_path, 'PUT',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnResponse',
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

    def replace_msg_vpn_authorization_group(self, msg_vpn_name, authorization_group_name, body, **kwargs):
        """
        Replaces an LDAP Authorization Group object.
        Replaces an LDAP Authorization Group object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str authorization_group_name: The authorizationGroupName of the LDAP Authorization Group. (required)
        :param MsgVpnAuthorizationGroup body: The LDAP Authorization Group object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAuthorizationGroupResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, body, **kwargs)
            return data

    def replace_msg_vpn_authorization_group_with_http_info(self, msg_vpn_name, authorization_group_name, body, **kwargs):
        """
        Replaces an LDAP Authorization Group object.
        Replaces an LDAP Authorization Group object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str authorization_group_name: The authorizationGroupName of the LDAP Authorization Group. (required)
        :param MsgVpnAuthorizationGroup body: The LDAP Authorization Group object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAuthorizationGroupResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'authorization_group_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_authorization_group" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_authorization_group`")
        # verify the required parameter 'authorization_group_name' is set
        if ('authorization_group_name' not in params) or (params['authorization_group_name'] is None):
            raise ValueError("Missing the required parameter `authorization_group_name` when calling `replace_msg_vpn_authorization_group`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_authorization_group`")

        resource_path = '/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'authorization_group_name' in params:
            path_params['authorizationGroupName'] = params['authorization_group_name']

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
                                            response_type='MsgVpnAuthorizationGroupResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_bridge(self, msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs):
        """
        Replaces a Bridge object.
        Replaces a Bridge object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param MsgVpnBridge body: The Bridge object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs)
            return data

    def replace_msg_vpn_bridge_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs):
        """
        Replaces a Bridge object.
        Replaces a Bridge object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param MsgVpnBridge body: The Bridge object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_bridge" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_bridge`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `replace_msg_vpn_bridge`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `replace_msg_vpn_bridge`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_bridge`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']

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
                                            response_type='MsgVpnBridgeResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_bridge_remote_msg_vpn(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, **kwargs):
        """
        Replaces a Remote Message VPN object.
        Replaces a Remote Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_msg_vpn_name: The remoteMsgVpnName of the Remote Message VPN. (required)
        :param str remote_msg_vpn_location: The remoteMsgVpnLocation of the Remote Message VPN. (required)
        :param str remote_msg_vpn_interface: The remoteMsgVpnInterface of the Remote Message VPN. (required)
        :param MsgVpnBridgeRemoteMsgVpn body: The Remote Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteMsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, **kwargs)
            return data

    def replace_msg_vpn_bridge_remote_msg_vpn_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, **kwargs):
        """
        Replaces a Remote Message VPN object.
        Replaces a Remote Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_msg_vpn_name: The remoteMsgVpnName of the Remote Message VPN. (required)
        :param str remote_msg_vpn_location: The remoteMsgVpnLocation of the Remote Message VPN. (required)
        :param str remote_msg_vpn_interface: The remoteMsgVpnInterface of the Remote Message VPN. (required)
        :param MsgVpnBridgeRemoteMsgVpn body: The Remote Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteMsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'remote_msg_vpn_name', 'remote_msg_vpn_location', 'remote_msg_vpn_interface', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_bridge_remote_msg_vpn" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `replace_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `replace_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_name' is set
        if ('remote_msg_vpn_name' not in params) or (params['remote_msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_name` when calling `replace_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_location' is set
        if ('remote_msg_vpn_location' not in params) or (params['remote_msg_vpn_location'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_location` when calling `replace_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_interface' is set
        if ('remote_msg_vpn_interface' not in params) or (params['remote_msg_vpn_interface'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_interface` when calling `replace_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_bridge_remote_msg_vpn`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']
        if 'remote_msg_vpn_name' in params:
            path_params['remoteMsgVpnName'] = params['remote_msg_vpn_name']
        if 'remote_msg_vpn_location' in params:
            path_params['remoteMsgVpnLocation'] = params['remote_msg_vpn_location']
        if 'remote_msg_vpn_interface' in params:
            path_params['remoteMsgVpnInterface'] = params['remote_msg_vpn_interface']

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
                                            response_type='MsgVpnBridgeRemoteMsgVpnResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_client_profile(self, msg_vpn_name, client_profile_name, body, **kwargs):
        """
        Replaces a Client Profile object.
        Replaces a Client Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_profile_name: The clientProfileName of the Client Profile. (required)
        :param MsgVpnClientProfile body: The Client Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, body, **kwargs)
            return data

    def replace_msg_vpn_client_profile_with_http_info(self, msg_vpn_name, client_profile_name, body, **kwargs):
        """
        Replaces a Client Profile object.
        Replaces a Client Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_profile_name: The clientProfileName of the Client Profile. (required)
        :param MsgVpnClientProfile body: The Client Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'client_profile_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_client_profile" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_client_profile`")
        # verify the required parameter 'client_profile_name' is set
        if ('client_profile_name' not in params) or (params['client_profile_name'] is None):
            raise ValueError("Missing the required parameter `client_profile_name` when calling `replace_msg_vpn_client_profile`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_client_profile`")

        resource_path = '/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'client_profile_name' in params:
            path_params['clientProfileName'] = params['client_profile_name']

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
                                            response_type='MsgVpnClientProfileResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_client_username(self, msg_vpn_name, client_username, body, **kwargs):
        """
        Replaces a Client Username object.
        Replaces a Client Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_client_username(msg_vpn_name, client_username, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_username: The clientUsername of the Client Username. (required)
        :param MsgVpnClientUsername body: The Client Username object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientUsernameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, body, **kwargs)
            return data

    def replace_msg_vpn_client_username_with_http_info(self, msg_vpn_name, client_username, body, **kwargs):
        """
        Replaces a Client Username object.
        Replaces a Client Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_username: The clientUsername of the Client Username. (required)
        :param MsgVpnClientUsername body: The Client Username object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientUsernameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'client_username', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_client_username" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_client_username`")
        # verify the required parameter 'client_username' is set
        if ('client_username' not in params) or (params['client_username'] is None):
            raise ValueError("Missing the required parameter `client_username` when calling `replace_msg_vpn_client_username`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_client_username`")

        resource_path = '/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'client_username' in params:
            path_params['clientUsername'] = params['client_username']

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
                                            response_type='MsgVpnClientUsernameResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_mqtt_session(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs):
        """
        Replaces an MQTT Session object.
        Replaces an MQTT Session object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| owner||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param MsgVpnMqttSession body: The MQTT Session object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs)
            return data

    def replace_msg_vpn_mqtt_session_with_http_info(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs):
        """
        Replaces an MQTT Session object.
        Replaces an MQTT Session object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| owner||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param MsgVpnMqttSession body: The MQTT Session object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'mqtt_session_client_id', 'mqtt_session_virtual_router', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_mqtt_session" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_mqtt_session`")
        # verify the required parameter 'mqtt_session_client_id' is set
        if ('mqtt_session_client_id' not in params) or (params['mqtt_session_client_id'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_client_id` when calling `replace_msg_vpn_mqtt_session`")
        # verify the required parameter 'mqtt_session_virtual_router' is set
        if ('mqtt_session_virtual_router' not in params) or (params['mqtt_session_virtual_router'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_virtual_router` when calling `replace_msg_vpn_mqtt_session`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_mqtt_session`")

        resource_path = '/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'mqtt_session_client_id' in params:
            path_params['mqttSessionClientId'] = params['mqtt_session_client_id']
        if 'mqtt_session_virtual_router' in params:
            path_params['mqttSessionVirtualRouter'] = params['mqtt_session_virtual_router']

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
                                            response_type='MsgVpnMqttSessionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_mqtt_session_subscription(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, **kwargs):
        """
        Replaces an MQTT Session Subscription object.
        Replaces an MQTT Session Subscription object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param str subscription_topic: The subscriptionTopic of the MQTT Session Subscription. (required)
        :param MsgVpnMqttSessionSubscription body: The MQTT Session Subscription object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, **kwargs)
            return data

    def replace_msg_vpn_mqtt_session_subscription_with_http_info(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, **kwargs):
        """
        Replaces an MQTT Session Subscription object.
        Replaces an MQTT Session Subscription object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param str subscription_topic: The subscriptionTopic of the MQTT Session Subscription. (required)
        :param MsgVpnMqttSessionSubscription body: The MQTT Session Subscription object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'mqtt_session_client_id', 'mqtt_session_virtual_router', 'subscription_topic', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_mqtt_session_subscription" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'mqtt_session_client_id' is set
        if ('mqtt_session_client_id' not in params) or (params['mqtt_session_client_id'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_client_id` when calling `replace_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'mqtt_session_virtual_router' is set
        if ('mqtt_session_virtual_router' not in params) or (params['mqtt_session_virtual_router'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_virtual_router` when calling `replace_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'subscription_topic' is set
        if ('subscription_topic' not in params) or (params['subscription_topic'] is None):
            raise ValueError("Missing the required parameter `subscription_topic` when calling `replace_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_mqtt_session_subscription`")

        resource_path = '/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'mqtt_session_client_id' in params:
            path_params['mqttSessionClientId'] = params['mqtt_session_client_id']
        if 'mqtt_session_virtual_router' in params:
            path_params['mqttSessionVirtualRouter'] = params['mqtt_session_virtual_router']
        if 'subscription_topic' in params:
            path_params['subscriptionTopic'] = params['subscription_topic']

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
                                            response_type='MsgVpnMqttSessionSubscriptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_queue(self, msg_vpn_name, queue_name, body, **kwargs):
        """
        Replaces a Queue object.
        Replaces a Queue object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_queue(msg_vpn_name, queue_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param MsgVpnQueue body: The Queue object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, body, **kwargs)
            return data

    def replace_msg_vpn_queue_with_http_info(self, msg_vpn_name, queue_name, body, **kwargs):
        """
        Replaces a Queue object.
        Replaces a Queue object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param MsgVpnQueue body: The Queue object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'queue_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_queue" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_queue`")
        # verify the required parameter 'queue_name' is set
        if ('queue_name' not in params) or (params['queue_name'] is None):
            raise ValueError("Missing the required parameter `queue_name` when calling `replace_msg_vpn_queue`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_queue`")

        resource_path = '/msgVpns/{msgVpnName}/queues/{queueName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'queue_name' in params:
            path_params['queueName'] = params['queue_name']

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
                                            response_type='MsgVpnQueueResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def replace_msg_vpn_replicated_topic(self, msg_vpn_name, replicated_topic, body, **kwargs):
        """
        Replaces a Replicated Topic object.
        Replaces a Replicated Topic object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str replicated_topic: The replicatedTopic of the Replicated Topic. (required)
        :param MsgVpnReplicatedTopic body: The Replicated Topic object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnReplicatedTopicResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, body, **kwargs)
            return data

    def replace_msg_vpn_replicated_topic_with_http_info(self, msg_vpn_name, replicated_topic, body, **kwargs):
        """
        Replaces a Replicated Topic object.
        Replaces a Replicated Topic object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str replicated_topic: The replicatedTopic of the Replicated Topic. (required)
        :param MsgVpnReplicatedTopic body: The Replicated Topic object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnReplicatedTopicResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'replicated_topic', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_replicated_topic" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_replicated_topic`")
        # verify the required parameter 'replicated_topic' is set
        if ('replicated_topic' not in params) or (params['replicated_topic'] is None):
            raise ValueError("Missing the required parameter `replicated_topic` when calling `replace_msg_vpn_replicated_topic`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_replicated_topic`")

        resource_path = '/msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'replicated_topic' in params:
            path_params['replicatedTopic'] = params['replicated_topic']

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
                                            response_type='MsgVpnReplicatedTopicResponse',
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

    def replace_msg_vpn_topic_endpoint(self, msg_vpn_name, topic_endpoint_name, body, **kwargs):
        """
        Replaces a Topic Endpoint object.
        Replaces a Topic Endpoint object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| owner||||x| permission||||x| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str topic_endpoint_name: The topicEndpointName of the Topic Endpoint. (required)
        :param MsgVpnTopicEndpoint body: The Topic Endpoint object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnTopicEndpointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.replace_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, body, **kwargs)
        else:
            (data) = self.replace_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, body, **kwargs)
            return data

    def replace_msg_vpn_topic_endpoint_with_http_info(self, msg_vpn_name, topic_endpoint_name, body, **kwargs):
        """
        Replaces a Topic Endpoint object.
        Replaces a Topic Endpoint object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| owner||||x| permission||||x| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.replace_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str topic_endpoint_name: The topicEndpointName of the Topic Endpoint. (required)
        :param MsgVpnTopicEndpoint body: The Topic Endpoint object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnTopicEndpointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'topic_endpoint_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method replace_msg_vpn_topic_endpoint" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `replace_msg_vpn_topic_endpoint`")
        # verify the required parameter 'topic_endpoint_name' is set
        if ('topic_endpoint_name' not in params) or (params['topic_endpoint_name'] is None):
            raise ValueError("Missing the required parameter `topic_endpoint_name` when calling `replace_msg_vpn_topic_endpoint`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `replace_msg_vpn_topic_endpoint`")

        resource_path = '/msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'topic_endpoint_name' in params:
            path_params['topicEndpointName'] = params['topic_endpoint_name']

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
                                            response_type='MsgVpnTopicEndpointResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn(self, msg_vpn_name, body, **kwargs):
        """
        Updates a Message VPN object.
        Updates a Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpn body: The Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_with_http_info(msg_vpn_name, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_with_http_info(msg_vpn_name, body, **kwargs)
            return data

    def update_msg_vpn_with_http_info(self, msg_vpn_name, body, **kwargs):
        """
        Updates a Message VPN object.
        Updates a Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_with_http_info(msg_vpn_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param MsgVpn body: The Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnResponse
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
                    " to method update_msg_vpn" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn`")

        resource_path = '/msgVpns/{msgVpnName}'.replace('{format}', 'json')
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

        return self.api_client.call_api(resource_path, 'PATCH',
                                            path_params,
                                            query_params,
                                            header_params,
                                            body=body_params,
                                            post_params=form_params,
                                            files=local_var_files,
                                            response_type='MsgVpnResponse',
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

    def update_msg_vpn_authorization_group(self, msg_vpn_name, authorization_group_name, body, **kwargs):
        """
        Updates an LDAP Authorization Group object.
        Updates an LDAP Authorization Group object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str authorization_group_name: The authorizationGroupName of the LDAP Authorization Group. (required)
        :param MsgVpnAuthorizationGroup body: The LDAP Authorization Group object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAuthorizationGroupResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, body, **kwargs)
            return data

    def update_msg_vpn_authorization_group_with_http_info(self, msg_vpn_name, authorization_group_name, body, **kwargs):
        """
        Updates an LDAP Authorization Group object.
        Updates an LDAP Authorization Group object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str authorization_group_name: The authorizationGroupName of the LDAP Authorization Group. (required)
        :param MsgVpnAuthorizationGroup body: The LDAP Authorization Group object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnAuthorizationGroupResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'authorization_group_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_authorization_group" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_authorization_group`")
        # verify the required parameter 'authorization_group_name' is set
        if ('authorization_group_name' not in params) or (params['authorization_group_name'] is None):
            raise ValueError("Missing the required parameter `authorization_group_name` when calling `update_msg_vpn_authorization_group`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_authorization_group`")

        resource_path = '/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'authorization_group_name' in params:
            path_params['authorizationGroupName'] = params['authorization_group_name']

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
                                            response_type='MsgVpnAuthorizationGroupResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_bridge(self, msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs):
        """
        Updates a Bridge object.
        Updates a Bridge object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param MsgVpnBridge body: The Bridge object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs)
            return data

    def update_msg_vpn_bridge_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, body, **kwargs):
        """
        Updates a Bridge object.
        Updates a Bridge object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param MsgVpnBridge body: The Bridge object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_bridge" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_bridge`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `update_msg_vpn_bridge`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `update_msg_vpn_bridge`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_bridge`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']

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
                                            response_type='MsgVpnBridgeResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_bridge_remote_msg_vpn(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, **kwargs):
        """
        Updates a Remote Message VPN object.
        Updates a Remote Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_msg_vpn_name: The remoteMsgVpnName of the Remote Message VPN. (required)
        :param str remote_msg_vpn_location: The remoteMsgVpnLocation of the Remote Message VPN. (required)
        :param str remote_msg_vpn_interface: The remoteMsgVpnInterface of the Remote Message VPN. (required)
        :param MsgVpnBridgeRemoteMsgVpn body: The Remote Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteMsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, **kwargs)
            return data

    def update_msg_vpn_bridge_remote_msg_vpn_with_http_info(self, msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, **kwargs):
        """
        Updates a Remote Message VPN object.
        Updates a Remote Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str bridge_name: The bridgeName of the Bridge. (required)
        :param str bridge_virtual_router: The bridgeVirtualRouter of the Bridge. (required)
        :param str remote_msg_vpn_name: The remoteMsgVpnName of the Remote Message VPN. (required)
        :param str remote_msg_vpn_location: The remoteMsgVpnLocation of the Remote Message VPN. (required)
        :param str remote_msg_vpn_interface: The remoteMsgVpnInterface of the Remote Message VPN. (required)
        :param MsgVpnBridgeRemoteMsgVpn body: The Remote Message VPN object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnBridgeRemoteMsgVpnResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'bridge_name', 'bridge_virtual_router', 'remote_msg_vpn_name', 'remote_msg_vpn_location', 'remote_msg_vpn_interface', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_bridge_remote_msg_vpn" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'bridge_name' is set
        if ('bridge_name' not in params) or (params['bridge_name'] is None):
            raise ValueError("Missing the required parameter `bridge_name` when calling `update_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'bridge_virtual_router' is set
        if ('bridge_virtual_router' not in params) or (params['bridge_virtual_router'] is None):
            raise ValueError("Missing the required parameter `bridge_virtual_router` when calling `update_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_name' is set
        if ('remote_msg_vpn_name' not in params) or (params['remote_msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_name` when calling `update_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_location' is set
        if ('remote_msg_vpn_location' not in params) or (params['remote_msg_vpn_location'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_location` when calling `update_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'remote_msg_vpn_interface' is set
        if ('remote_msg_vpn_interface' not in params) or (params['remote_msg_vpn_interface'] is None):
            raise ValueError("Missing the required parameter `remote_msg_vpn_interface` when calling `update_msg_vpn_bridge_remote_msg_vpn`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_bridge_remote_msg_vpn`")

        resource_path = '/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'bridge_name' in params:
            path_params['bridgeName'] = params['bridge_name']
        if 'bridge_virtual_router' in params:
            path_params['bridgeVirtualRouter'] = params['bridge_virtual_router']
        if 'remote_msg_vpn_name' in params:
            path_params['remoteMsgVpnName'] = params['remote_msg_vpn_name']
        if 'remote_msg_vpn_location' in params:
            path_params['remoteMsgVpnLocation'] = params['remote_msg_vpn_location']
        if 'remote_msg_vpn_interface' in params:
            path_params['remoteMsgVpnInterface'] = params['remote_msg_vpn_interface']

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
                                            response_type='MsgVpnBridgeRemoteMsgVpnResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_client_profile(self, msg_vpn_name, client_profile_name, body, **kwargs):
        """
        Updates a Client Profile object.
        Updates a Client Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_profile_name: The clientProfileName of the Client Profile. (required)
        :param MsgVpnClientProfile body: The Client Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, body, **kwargs)
            return data

    def update_msg_vpn_client_profile_with_http_info(self, msg_vpn_name, client_profile_name, body, **kwargs):
        """
        Updates a Client Profile object.
        Updates a Client Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_profile_name: The clientProfileName of the Client Profile. (required)
        :param MsgVpnClientProfile body: The Client Profile object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientProfileResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'client_profile_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_client_profile" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_client_profile`")
        # verify the required parameter 'client_profile_name' is set
        if ('client_profile_name' not in params) or (params['client_profile_name'] is None):
            raise ValueError("Missing the required parameter `client_profile_name` when calling `update_msg_vpn_client_profile`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_client_profile`")

        resource_path = '/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'client_profile_name' in params:
            path_params['clientProfileName'] = params['client_profile_name']

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
                                            response_type='MsgVpnClientProfileResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_client_username(self, msg_vpn_name, client_username, body, **kwargs):
        """
        Updates a Client Username object.
        Updates a Client Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_client_username(msg_vpn_name, client_username, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_username: The clientUsername of the Client Username. (required)
        :param MsgVpnClientUsername body: The Client Username object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientUsernameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, body, **kwargs)
            return data

    def update_msg_vpn_client_username_with_http_info(self, msg_vpn_name, client_username, body, **kwargs):
        """
        Updates a Client Username object.
        Updates a Client Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str client_username: The clientUsername of the Client Username. (required)
        :param MsgVpnClientUsername body: The Client Username object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnClientUsernameResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'client_username', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_client_username" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_client_username`")
        # verify the required parameter 'client_username' is set
        if ('client_username' not in params) or (params['client_username'] is None):
            raise ValueError("Missing the required parameter `client_username` when calling `update_msg_vpn_client_username`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_client_username`")

        resource_path = '/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'client_username' in params:
            path_params['clientUsername'] = params['client_username']

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
                                            response_type='MsgVpnClientUsernameResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_mqtt_session(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs):
        """
        Updates an MQTT Session object.
        Updates an MQTT Session object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| owner||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param MsgVpnMqttSession body: The MQTT Session object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs)
            return data

    def update_msg_vpn_mqtt_session_with_http_info(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, **kwargs):
        """
        Updates an MQTT Session object.
        Updates an MQTT Session object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| owner||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_mqtt_session_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param MsgVpnMqttSession body: The MQTT Session object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'mqtt_session_client_id', 'mqtt_session_virtual_router', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_mqtt_session" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_mqtt_session`")
        # verify the required parameter 'mqtt_session_client_id' is set
        if ('mqtt_session_client_id' not in params) or (params['mqtt_session_client_id'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_client_id` when calling `update_msg_vpn_mqtt_session`")
        # verify the required parameter 'mqtt_session_virtual_router' is set
        if ('mqtt_session_virtual_router' not in params) or (params['mqtt_session_virtual_router'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_virtual_router` when calling `update_msg_vpn_mqtt_session`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_mqtt_session`")

        resource_path = '/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'mqtt_session_client_id' in params:
            path_params['mqttSessionClientId'] = params['mqtt_session_client_id']
        if 'mqtt_session_virtual_router' in params:
            path_params['mqttSessionVirtualRouter'] = params['mqtt_session_virtual_router']

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
                                            response_type='MsgVpnMqttSessionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_mqtt_session_subscription(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, **kwargs):
        """
        Updates an MQTT Session Subscription object.
        Updates an MQTT Session Subscription object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param str subscription_topic: The subscriptionTopic of the MQTT Session Subscription. (required)
        :param MsgVpnMqttSessionSubscription body: The MQTT Session Subscription object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, **kwargs)
            return data

    def update_msg_vpn_mqtt_session_subscription_with_http_info(self, msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, **kwargs):
        """
        Updates an MQTT Session Subscription object.
        Updates an MQTT Session Subscription object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_mqtt_session_subscription_with_http_info(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str mqtt_session_client_id: The mqttSessionClientId of the MQTT Session. (required)
        :param str mqtt_session_virtual_router: The mqttSessionVirtualRouter of the MQTT Session. (required)
        :param str subscription_topic: The subscriptionTopic of the MQTT Session Subscription. (required)
        :param MsgVpnMqttSessionSubscription body: The MQTT Session Subscription object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnMqttSessionSubscriptionResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'mqtt_session_client_id', 'mqtt_session_virtual_router', 'subscription_topic', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_mqtt_session_subscription" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'mqtt_session_client_id' is set
        if ('mqtt_session_client_id' not in params) or (params['mqtt_session_client_id'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_client_id` when calling `update_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'mqtt_session_virtual_router' is set
        if ('mqtt_session_virtual_router' not in params) or (params['mqtt_session_virtual_router'] is None):
            raise ValueError("Missing the required parameter `mqtt_session_virtual_router` when calling `update_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'subscription_topic' is set
        if ('subscription_topic' not in params) or (params['subscription_topic'] is None):
            raise ValueError("Missing the required parameter `subscription_topic` when calling `update_msg_vpn_mqtt_session_subscription`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_mqtt_session_subscription`")

        resource_path = '/msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'mqtt_session_client_id' in params:
            path_params['mqttSessionClientId'] = params['mqtt_session_client_id']
        if 'mqtt_session_virtual_router' in params:
            path_params['mqttSessionVirtualRouter'] = params['mqtt_session_virtual_router']
        if 'subscription_topic' in params:
            path_params['subscriptionTopic'] = params['subscription_topic']

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
                                            response_type='MsgVpnMqttSessionSubscriptionResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_queue(self, msg_vpn_name, queue_name, body, **kwargs):
        """
        Updates a Queue object.
        Updates a Queue object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_queue(msg_vpn_name, queue_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param MsgVpnQueue body: The Queue object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, body, **kwargs)
            return data

    def update_msg_vpn_queue_with_http_info(self, msg_vpn_name, queue_name, body, **kwargs):
        """
        Updates a Queue object.
        Updates a Queue object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str queue_name: The queueName of the Queue. (required)
        :param MsgVpnQueue body: The Queue object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnQueueResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'queue_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_queue" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_queue`")
        # verify the required parameter 'queue_name' is set
        if ('queue_name' not in params) or (params['queue_name'] is None):
            raise ValueError("Missing the required parameter `queue_name` when calling `update_msg_vpn_queue`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_queue`")

        resource_path = '/msgVpns/{msgVpnName}/queues/{queueName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'queue_name' in params:
            path_params['queueName'] = params['queue_name']

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
                                            response_type='MsgVpnQueueResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))

    def update_msg_vpn_replicated_topic(self, msg_vpn_name, replicated_topic, body, **kwargs):
        """
        Updates a Replicated Topic object.
        Updates a Replicated Topic object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str replicated_topic: The replicatedTopic of the Replicated Topic. (required)
        :param MsgVpnReplicatedTopic body: The Replicated Topic object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnReplicatedTopicResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, body, **kwargs)
            return data

    def update_msg_vpn_replicated_topic_with_http_info(self, msg_vpn_name, replicated_topic, body, **kwargs):
        """
        Updates a Replicated Topic object.
        Updates a Replicated Topic object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_replicated_topic_with_http_info(msg_vpn_name, replicated_topic, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str replicated_topic: The replicatedTopic of the Replicated Topic. (required)
        :param MsgVpnReplicatedTopic body: The Replicated Topic object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnReplicatedTopicResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'replicated_topic', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_replicated_topic" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_replicated_topic`")
        # verify the required parameter 'replicated_topic' is set
        if ('replicated_topic' not in params) or (params['replicated_topic'] is None):
            raise ValueError("Missing the required parameter `replicated_topic` when calling `update_msg_vpn_replicated_topic`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_replicated_topic`")

        resource_path = '/msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'replicated_topic' in params:
            path_params['replicatedTopic'] = params['replicated_topic']

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
                                            response_type='MsgVpnReplicatedTopicResponse',
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

    def update_msg_vpn_topic_endpoint(self, msg_vpn_name, topic_endpoint_name, body, **kwargs):
        """
        Updates a Topic Endpoint object.
        Updates a Topic Endpoint object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| owner||||x| permission||||x| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str topic_endpoint_name: The topicEndpointName of the Topic Endpoint. (required)
        :param MsgVpnTopicEndpoint body: The Topic Endpoint object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnTopicEndpointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """
        kwargs['_return_http_data_only'] = True
        if kwargs.get('callback'):
            return self.update_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, body, **kwargs)
        else:
            (data) = self.update_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, body, **kwargs)
            return data

    def update_msg_vpn_topic_endpoint_with_http_info(self, msg_vpn_name, topic_endpoint_name, body, **kwargs):
        """
        Updates a Topic Endpoint object.
        Updates a Topic Endpoint object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| owner||||x| permission||||x| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

        This method makes a synchronous HTTP request by default. To make an
        asynchronous HTTP request, please define a `callback` function
        to be invoked when receiving the response.
        >>> def callback_function(response):
        >>>     pprint(response)
        >>>
        >>> thread = api.update_msg_vpn_topic_endpoint_with_http_info(msg_vpn_name, topic_endpoint_name, body, callback=callback_function)

        :param callback function: The callback function
            for asynchronous request. (optional)
        :param str msg_vpn_name: The msgVpnName of the Message VPN. (required)
        :param str topic_endpoint_name: The topicEndpointName of the Topic Endpoint. (required)
        :param MsgVpnTopicEndpoint body: The Topic Endpoint object's attributes. (required)
        :param list[str] select: Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\").
        :return: MsgVpnTopicEndpointResponse
                 If the method is called asynchronously,
                 returns the request thread.
        """

        all_params = ['msg_vpn_name', 'topic_endpoint_name', 'body', 'select']
        all_params.append('callback')
        all_params.append('_return_http_data_only')

        params = locals()
        for key, val in iteritems(params['kwargs']):
            if key not in all_params:
                raise TypeError(
                    "Got an unexpected keyword argument '%s'"
                    " to method update_msg_vpn_topic_endpoint" % key
                )
            params[key] = val
        del params['kwargs']
        # verify the required parameter 'msg_vpn_name' is set
        if ('msg_vpn_name' not in params) or (params['msg_vpn_name'] is None):
            raise ValueError("Missing the required parameter `msg_vpn_name` when calling `update_msg_vpn_topic_endpoint`")
        # verify the required parameter 'topic_endpoint_name' is set
        if ('topic_endpoint_name' not in params) or (params['topic_endpoint_name'] is None):
            raise ValueError("Missing the required parameter `topic_endpoint_name` when calling `update_msg_vpn_topic_endpoint`")
        # verify the required parameter 'body' is set
        if ('body' not in params) or (params['body'] is None):
            raise ValueError("Missing the required parameter `body` when calling `update_msg_vpn_topic_endpoint`")

        resource_path = '/msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName}'.replace('{format}', 'json')
        path_params = {}
        if 'msg_vpn_name' in params:
            path_params['msgVpnName'] = params['msg_vpn_name']
        if 'topic_endpoint_name' in params:
            path_params['topicEndpointName'] = params['topic_endpoint_name']

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
                                            response_type='MsgVpnTopicEndpointResponse',
                                            auth_settings=auth_settings,
                                            callback=params.get('callback'),
                                            _return_http_data_only=params.get('_return_http_data_only'))
