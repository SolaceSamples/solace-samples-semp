=begin
#SEMP (Solace Element Management Protocol)

# SEMP (starting in `v2`, see [note 1](#notes)) is a RESTful API for configuring a Solace router.  SEMP uses URIs to address manageable **resources** of the Solace router. Resources are either individual **objects**, or **collections** of objects. The following APIs are provided:   API|Base Path|Purpose|Comments :---|:---|:---|:--- Configuration|/SEMP/v2/config|Reading and writing config state|See [note 2](#notes)    Resources are always nouns, with individual objects being singular and collections being plural. Objects within a collection are identified by an `obj-id`, which follows the collection name with the form `collection-name/obj-id`. Some examples:  <pre> /SEMP/v2/config/msgVpns                       ; MsgVpn collection /SEMP/v2/config/msgVpns/finance               ; MsgVpn object named \"finance\" /SEMP/v2/config/msgVpns/finance/queues        ; Queue collection within MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ ; Queue object named \"orderQ\" within MsgVpn \"finance\" </pre>  ## Collection Resources  Collections are unordered lists of objects (unless described as otherwise), and are described by JSON arrays. Each item in the array represents an object in the same manner as the individual object would normally be represented. The creation of a new object is done through its collection resource.  ## Object Resources  Objects are composed of attributes and collections, and are described by JSON content as name/value pairs. The collections of an object are not contained directly in the object's JSON content, rather the content includes a URI attribute which points to the collection. This contained collection resource must be managed as a separate resource through this URI.  At a minimum, every object has 1 or more identifying attributes, and its own `uri` attribute which contains the URI to itself. Attributes may have any (non-exclusively) of the following properties:   Property|Meaning|Comments :---|:---|:--- Identifying|Attribute is involved in unique identification of the object, and appears in its URI| Required|Attribute must be provided in the request| Read-Only|Attribute can only be read, not written|See [note 3](#notes) Write-Only|Attribute can only be written, not read| Requires-Disable|Attribute can only be changed when object is disabled| Deprecated|Attribute is deprecated, and will disappear in the next SEMP version|    In some requests, certain attributes may only be provided in certain combinations with other attributes:   Relationship|Meaning :---|:--- Requires|Attribute may only be changed by a request if a particular attribute or combination of attributes is also provided in the request Conflicts|Attribute may only be provided in a request if a particular attribute or combination of attributes is not also provided in the request    ## HTTP Methods  The HTTP methods of POST, PUT, PATCH, DELETE, and GET manipulate resources following these general principles:   Method|Resource|Meaning|Request Body|Response Body|Missing Request Attributes :---|:---|:---|:---|:---|:--- POST|Collection|Create object|Initial attribute values|Object attributes and metadata|Set to default PUT|Object|Replace object|New attribute values|Object attributes and metadata|Set to default (but see [note 4](#notes)) PATCH|Object|Update object|New attribute values|Object attributes and metadata | Left unchanged| DELETE|Object|Delete object|Empty|Object metadata|N/A GET|Object|Get object|Empty|Object attributes and metadata|N/A GET|Collection|Get collection|Empty|Object attributes and collection metadata|N/A    ## Common Query Parameters  The following are some common query parameters that are supported by many method/URI combinations. Individual URIs may document additional parameters. Note that multiple query parameters can be used together in a single URI, separated by the ampersand character. For example:  <pre> ; Request for the MsgVpns collection using two hypothetical query parameters ; \"q1\" and \"q2\" with values \"val1\" and \"val2\" respectively /SEMP/v2/config/msgVpns?q1=val1&q2=val2 </pre>  ### select  Include in the response only selected attributes of the object. Use this query parameter to limit the size of the returned data for each returned object, or return only those fields that are desired.  The value of `select` is a comma-separated list of attribute names. Names may include the `*` wildcard. Nested attribute names are supported using periods (e.g. `parentName.childName`). If the list is empty (i.e. `select=`) no attributes are returned; otherwise the list must match at least one attribute name of the object. Some examples:  <pre> ; List of all MsgVpn names /SEMP/v2/config/msgVpns?select=msgVpnName  ; Authentication attributes of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance?select=authentication*  ; Access related attributes of Queue \"orderQ\" of MsgVpn \"finance\" /SEMP/v2/config/msgVpns/finance/queues/orderQ?select=owner,permission </pre>  ### where  Include in the response only objects where certain conditions are true. Use this query parameter to limit which objects are returned to those whose attribute values meet the given conditions.  The value of `where` is a comma-separated list of expressions. All expressions must be true for the object to be included in the response. Each expression takes the form:  <pre> expression  = attribute-name OP value OP          = '==' | '!=' | '<' | '>' | '<=' | '>=' </pre>  `value` may be a number, string, `true`, or `false`, as appropriate for the type of `attribute-name`. Greater-than and less-than comparisons only work for numbers. A `*` in a string `value` is interpreted as a wildcard. Some examples:  <pre> ; Only enabled MsgVpns /SEMP/v2/config/msgVpns?where=enabled==true  ; Only MsgVpns using basic non-LDAP authentication /SEMP/v2/config/msgVpns?where=authenticationBasicEnabled==true,authenticationBasicType!=ldap  ; Only MsgVpns that allow more than 100 client connections /SEMP/v2/config/msgVpns?where=maxConnectionCount>100 </pre>  ### count  Limit the count of objects in the response. This can be useful to limit the size of the response for large collections. The minimum value for `count` is `1` and the default is `10`. There is a hidden maximum as to prevent overloading the system. For example:  <pre> ; Up to 25 MsgVpns /SEMP/v2/config/msgVpns?count=25 </pre>  ### cursor  The cursor, or position, for the next page of objects. Cursors are opaque data that should not be created or interpreted by SEMP clients, and should only be used as described below.  When a request is made for a collection and there may be additional objects available for retrieval that are not included in the initial response, the response will include a `cursorQuery` field containing a cursor. The value of this field can be specified in the `cursor` query parameter of a subsequent request to retrieve the next page of objects. For convenience, an appropriate URI is constructed automatically by the router and included in the `nextPageUri` field of the response. This URI can be used directly to retrieve the next page of objects.  ## Notes  1. This specification defines SEMP starting in `v2`, and not the original SEMP `v1` interface. Request and response formats between `v1` and `v2` are entirely incompatible, although both protocols share a common port configuration on the Solace router. They are differentiated by the initial portion of the URI path, one of either `/SEMP/` or `/SEMP/v2/`. 2. The config API is partially implemented. Only a subset of all configurable objects are available. 3. Read-only attributes may appear in POST and PUT/PATCH requests. However, if a read-only attribute is not marked as identifying, it will be ignored during a PUT/PATCH. 4. For PUT, if the SEMP user is not authorized to modify the attribute, its value is left unchanged rather than set to default. In addition, the values of write-only attributes are not set to their defaults on a PUT. 5. For DELETE, the body of the request currently serves no purpose and will cause an error if not empty. 

OpenAPI spec version: 2.8.0.0.18
Contact: support_request@solacesystems.com
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

=end

require "uri"

module SempClient
  class MsgVpnApi
    attr_accessor :api_client

    def initialize(api_client = ApiClient.default)
      @api_client = api_client
    end

    # Creates a Message VPN object.
    # Creates a Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword||||x| replicationEnabledQueueBehavior||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param body The Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnResponse]
    def create_msg_vpn(body, opts = {})
      data, _status_code, _headers = create_msg_vpn_with_http_info(body, opts)
      return data
    end

    # Creates a Message VPN object.
    # Creates a Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword||||x| replicationEnabledQueueBehavior||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param body The Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnResponse, Fixnum, Hash)>] MsgVpnResponse data, response status code and response headers
    def create_msg_vpn_with_http_info(body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn ..."
      end
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn" if body.nil?
      # resource path
      local_var_path = "/msgVpns".sub('{format}','json')

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates an ACL Profile object.
    # Creates an ACL Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The ACL Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfileResponse]
    def create_msg_vpn_acl_profile(msg_vpn_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_acl_profile_with_http_info(msg_vpn_name, body, opts)
      return data
    end

    # Creates an ACL Profile object.
    # Creates an ACL Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The ACL Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfileResponse, Fixnum, Hash)>] MsgVpnAclProfileResponse data, response status code and response headers
    def create_msg_vpn_acl_profile_with_http_info(msg_vpn_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_acl_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_acl_profile" if msg_vpn_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_acl_profile" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfileResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_acl_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Client Connect Exception object.
    # Creates a Client Connect Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| clientConnectExceptionAddress|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param body The Client Connect Exception object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfileClientConnectExceptionResponse]
    def create_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, body, opts)
      return data
    end

    # Creates a Client Connect Exception object.
    # Creates a Client Connect Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| clientConnectExceptionAddress|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param body The Client Connect Exception object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfileClientConnectExceptionResponse, Fixnum, Hash)>] MsgVpnAclProfileClientConnectExceptionResponse data, response status code and response headers
    def create_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_acl_profile_client_connect_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_acl_profile_client_connect_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.create_msg_vpn_acl_profile_client_connect_exception" if acl_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_acl_profile_client_connect_exception" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfileClientConnectExceptionResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_acl_profile_client_connect_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Publish Topic Exception object.
    # Creates a Publish Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| publishExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param body The Publish Topic Exception object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfilePublishExceptionResponse]
    def create_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, body, opts)
      return data
    end

    # Creates a Publish Topic Exception object.
    # Creates a Publish Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| publishExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param body The Publish Topic Exception object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfilePublishExceptionResponse, Fixnum, Hash)>] MsgVpnAclProfilePublishExceptionResponse data, response status code and response headers
    def create_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_acl_profile_publish_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_acl_profile_publish_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.create_msg_vpn_acl_profile_publish_exception" if acl_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_acl_profile_publish_exception" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfilePublishExceptionResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_acl_profile_publish_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Subscribe Topic Exception object.
    # Creates a Subscribe Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| subscribeExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param body The Subscribe Topic Exception object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfileSubscribeExceptionResponse]
    def create_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, body, opts)
      return data
    end

    # Creates a Subscribe Topic Exception object.
    # Creates a Subscribe Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| subscribeExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param body The Subscribe Topic Exception object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfileSubscribeExceptionResponse, Fixnum, Hash)>] MsgVpnAclProfileSubscribeExceptionResponse data, response status code and response headers
    def create_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_acl_profile_subscribe_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_acl_profile_subscribe_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.create_msg_vpn_acl_profile_subscribe_exception" if acl_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_acl_profile_subscribe_exception" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfileSubscribeExceptionResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_acl_profile_subscribe_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a LDAP Authorization Group object.
    # Creates a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authorizationGroupName|x|x||| msgVpnName|x||x|| orderAfterAuthorizationGroupName||||x| orderBeforeAuthorizationGroupName||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The LDAP Authorization Group object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAuthorizationGroupResponse]
    def create_msg_vpn_authorization_group(msg_vpn_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_authorization_group_with_http_info(msg_vpn_name, body, opts)
      return data
    end

    # Creates a LDAP Authorization Group object.
    # Creates a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authorizationGroupName|x|x||| msgVpnName|x||x|| orderAfterAuthorizationGroupName||||x| orderBeforeAuthorizationGroupName||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The LDAP Authorization Group object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAuthorizationGroupResponse, Fixnum, Hash)>] MsgVpnAuthorizationGroupResponse data, response status code and response headers
    def create_msg_vpn_authorization_group_with_http_info(msg_vpn_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_authorization_group ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_authorization_group" if msg_vpn_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_authorization_group" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/authorizationGroups".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAuthorizationGroupResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_authorization_group\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Bridge object.
    # Creates a Bridge object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| msgVpnName|x||x|| remoteAuthenticationBasicPassword||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Bridge object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeResponse]
    def create_msg_vpn_bridge(msg_vpn_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_bridge_with_http_info(msg_vpn_name, body, opts)
      return data
    end

    # Creates a Bridge object.
    # Creates a Bridge object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| msgVpnName|x||x|| remoteAuthenticationBasicPassword||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Bridge object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeResponse, Fixnum, Hash)>] MsgVpnBridgeResponse data, response status code and response headers
    def create_msg_vpn_bridge_with_http_info(msg_vpn_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_bridge ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_bridge" if msg_vpn_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_bridge" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_bridge\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Remote Message VPN object.
    # Creates a Remote Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| password||||x| remoteMsgVpnInterface|x|||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param body The Remote Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeRemoteMsgVpnResponse]
    def create_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts)
      return data
    end

    # Creates a Remote Message VPN object.
    # Creates a Remote Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| password||||x| remoteMsgVpnInterface|x|||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param body The Remote Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeRemoteMsgVpnResponse, Fixnum, Hash)>] MsgVpnBridgeRemoteMsgVpnResponse data, response status code and response headers
    def create_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_bridge_remote_msg_vpn ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_bridge_remote_msg_vpn" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.create_msg_vpn_bridge_remote_msg_vpn" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.create_msg_vpn_bridge_remote_msg_vpn" if bridge_virtual_router.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_bridge_remote_msg_vpn" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeRemoteMsgVpnResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_bridge_remote_msg_vpn\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Remote Subscription object.
    # Creates a Remote Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| deliverAlwaysEnabled||x||| msgVpnName|x||x|| remoteSubscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param body The Remote Subscription object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeRemoteSubscriptionResponse]
    def create_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts)
      return data
    end

    # Creates a Remote Subscription object.
    # Creates a Remote Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| deliverAlwaysEnabled||x||| msgVpnName|x||x|| remoteSubscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param body The Remote Subscription object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeRemoteSubscriptionResponse, Fixnum, Hash)>] MsgVpnBridgeRemoteSubscriptionResponse data, response status code and response headers
    def create_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_bridge_remote_subscription ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_bridge_remote_subscription" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.create_msg_vpn_bridge_remote_subscription" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.create_msg_vpn_bridge_remote_subscription" if bridge_virtual_router.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_bridge_remote_subscription" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeRemoteSubscriptionResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_bridge_remote_subscription\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Trusted Common Name object.
    # Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param body The Trusted Common Name object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeTlsTrustedCommonNameResponse]
    def create_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts)
      return data
    end

    # Creates a Trusted Common Name object.
    # Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param body The Trusted Common Name object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeTlsTrustedCommonNameResponse, Fixnum, Hash)>] MsgVpnBridgeTlsTrustedCommonNameResponse data, response status code and response headers
    def create_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_bridge_tls_trusted_common_name ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_bridge_tls_trusted_common_name" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.create_msg_vpn_bridge_tls_trusted_common_name" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.create_msg_vpn_bridge_tls_trusted_common_name" if bridge_virtual_router.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_bridge_tls_trusted_common_name" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeTlsTrustedCommonNameResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_bridge_tls_trusted_common_name\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Client Profile object.
    # Creates a Client Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Client Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnClientProfileResponse]
    def create_msg_vpn_client_profile(msg_vpn_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_client_profile_with_http_info(msg_vpn_name, body, opts)
      return data
    end

    # Creates a Client Profile object.
    # Creates a Client Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Client Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnClientProfileResponse, Fixnum, Hash)>] MsgVpnClientProfileResponse data, response status code and response headers
    def create_msg_vpn_client_profile_with_http_info(msg_vpn_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_client_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_client_profile" if msg_vpn_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_client_profile" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientProfiles".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnClientProfileResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_client_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Client Username object.
    # Creates a Client Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientUsername|x|x||| msgVpnName|x||x|| password||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Client Username object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnClientUsernameResponse]
    def create_msg_vpn_client_username(msg_vpn_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_client_username_with_http_info(msg_vpn_name, body, opts)
      return data
    end

    # Creates a Client Username object.
    # Creates a Client Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientUsername|x|x||| msgVpnName|x||x|| password||||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Client Username object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnClientUsernameResponse, Fixnum, Hash)>] MsgVpnClientUsernameResponse data, response status code and response headers
    def create_msg_vpn_client_username_with_http_info(msg_vpn_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_client_username ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_client_username" if msg_vpn_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_client_username" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientUsernames".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnClientUsernameResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_client_username\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Queue object.
    # Creates a Queue object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Queue object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnQueueResponse]
    def create_msg_vpn_queue(msg_vpn_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_queue_with_http_info(msg_vpn_name, body, opts)
      return data
    end

    # Creates a Queue object.
    # Creates a Queue object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Queue object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnQueueResponse, Fixnum, Hash)>] MsgVpnQueueResponse data, response status code and response headers
    def create_msg_vpn_queue_with_http_info(msg_vpn_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_queue ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_queue" if msg_vpn_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_queue" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/queues".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnQueueResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_queue\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Queue Subscription object.
    # Creates a Queue Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param body The Queue Subscription object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnQueueSubscriptionResponse]
    def create_msg_vpn_queue_subscription(msg_vpn_name, queue_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, body, opts)
      return data
    end

    # Creates a Queue Subscription object.
    # Creates a Queue Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param body The Queue Subscription object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnQueueSubscriptionResponse, Fixnum, Hash)>] MsgVpnQueueSubscriptionResponse data, response status code and response headers
    def create_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_queue_subscription ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_queue_subscription" if msg_vpn_name.nil?
      # verify the required parameter 'queue_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_name' when calling MsgVpnApi.create_msg_vpn_queue_subscription" if queue_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_queue_subscription" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'queueName' + '}', queue_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnQueueSubscriptionResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_queue_subscription\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a REST Delivery Point object.
    # Creates a REST Delivery Point object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The REST Delivery Point object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointResponse]
    def create_msg_vpn_rest_delivery_point(msg_vpn_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, body, opts)
      return data
    end

    # Creates a REST Delivery Point object.
    # Creates a REST Delivery Point object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The REST Delivery Point object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointResponse data, response status code and response headers
    def create_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_rest_delivery_point ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point" if msg_vpn_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_rest_delivery_point\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Queue Binding object.
    # Creates a Queue Binding object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueBindingName|x|x||| restDeliveryPointName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param body The Queue Binding object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointQueueBindingResponse]
    def create_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, body, opts)
      return data
    end

    # Creates a Queue Binding object.
    # Creates a Queue Binding object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueBindingName|x|x||| restDeliveryPointName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param body The Queue Binding object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointQueueBindingResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointQueueBindingResponse data, response status code and response headers
    def create_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_rest_delivery_point_queue_binding ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point_queue_binding" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point_queue_binding" if rest_delivery_point_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point_queue_binding" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointQueueBindingResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_rest_delivery_point_queue_binding\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a REST Consumer object.
    # Creates a REST Consumer object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword||||x| msgVpnName|x||x|| restConsumerName|x|x||| restDeliveryPointName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param body The REST Consumer object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointRestConsumerResponse]
    def create_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, body, opts)
      return data
    end

    # Creates a REST Consumer object.
    # Creates a REST Consumer object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword||||x| msgVpnName|x||x|| restConsumerName|x|x||| restDeliveryPointName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param body The REST Consumer object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointRestConsumerResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointRestConsumerResponse data, response status code and response headers
    def create_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_rest_delivery_point_rest_consumer ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point_rest_consumer" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point_rest_consumer" if rest_delivery_point_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point_rest_consumer" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointRestConsumerResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_rest_delivery_point_rest_consumer\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Trusted Common Name object.
    # Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restConsumerName|x||x|| restDeliveryPointName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param body The Trusted Common Name object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse]
    def create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts)
      return data
    end

    # Creates a Trusted Common Name object.
    # Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restConsumerName|x||x|| restDeliveryPointName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param body The Trusted Common Name object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse data, response status code and response headers
    def create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if rest_delivery_point_name.nil?
      # verify the required parameter 'rest_consumer_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_consumer_name' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if rest_consumer_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'restConsumerName' + '}', rest_consumer_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Creates a Sequenced Topic object.
    # Creates a Sequenced Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| sequencedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Sequenced Topic object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnSequencedTopicResponse]
    def create_msg_vpn_sequenced_topic(msg_vpn_name, body, opts = {})
      data, _status_code, _headers = create_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, body, opts)
      return data
    end

    # Creates a Sequenced Topic object.
    # Creates a Sequenced Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| sequencedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Sequenced Topic object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnSequencedTopicResponse, Fixnum, Hash)>] MsgVpnSequencedTopicResponse data, response status code and response headers
    def create_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.create_msg_vpn_sequenced_topic ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.create_msg_vpn_sequenced_topic" if msg_vpn_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.create_msg_vpn_sequenced_topic" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/sequencedTopics".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:POST, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnSequencedTopicResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#create_msg_vpn_sequenced_topic\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Message VPN object.
    # Deletes a Message VPN object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn(msg_vpn_name, opts = {})
      data, _status_code, _headers = delete_msg_vpn_with_http_info(msg_vpn_name, opts)
      return data
    end

    # Deletes a Message VPN object.
    # Deletes a Message VPN object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_with_http_info(msg_vpn_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn" if msg_vpn_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes an ACL Profile object.
    # Deletes an ACL Profile object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, opts = {})
      data, _status_code, _headers = delete_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, opts)
      return data
    end

    # Deletes an ACL Profile object.
    # Deletes an ACL Profile object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_acl_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_acl_profile" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.delete_msg_vpn_acl_profile" if acl_profile_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_acl_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Client Connect Exception object.
    # Deletes a Client Connect Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param client_connect_exception_address The clientConnectExceptionAddress of the Client Connect Exception.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address, opts = {})
      data, _status_code, _headers = delete_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, client_connect_exception_address, opts)
      return data
    end

    # Deletes a Client Connect Exception object.
    # Deletes a Client Connect Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param client_connect_exception_address The clientConnectExceptionAddress of the Client Connect Exception.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, client_connect_exception_address, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_acl_profile_client_connect_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_acl_profile_client_connect_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.delete_msg_vpn_acl_profile_client_connect_exception" if acl_profile_name.nil?
      # verify the required parameter 'client_connect_exception_address' is set
      fail ArgumentError, "Missing the required parameter 'client_connect_exception_address' when calling MsgVpnApi.delete_msg_vpn_acl_profile_client_connect_exception" if client_connect_exception_address.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s).sub('{' + 'clientConnectExceptionAddress' + '}', client_connect_exception_address.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_acl_profile_client_connect_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Publish Topic Exception object.
    # Deletes a Publish Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param topic_syntax The topicSyntax of the Publish Topic Exception.
    # @param publish_exception_topic The publishExceptionTopic of the Publish Topic Exception.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, opts = {})
      data, _status_code, _headers = delete_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, opts)
      return data
    end

    # Deletes a Publish Topic Exception object.
    # Deletes a Publish Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param topic_syntax The topicSyntax of the Publish Topic Exception.
    # @param publish_exception_topic The publishExceptionTopic of the Publish Topic Exception.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_acl_profile_publish_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_acl_profile_publish_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.delete_msg_vpn_acl_profile_publish_exception" if acl_profile_name.nil?
      # verify the required parameter 'topic_syntax' is set
      fail ArgumentError, "Missing the required parameter 'topic_syntax' when calling MsgVpnApi.delete_msg_vpn_acl_profile_publish_exception" if topic_syntax.nil?
      # verify the required parameter 'publish_exception_topic' is set
      fail ArgumentError, "Missing the required parameter 'publish_exception_topic' when calling MsgVpnApi.delete_msg_vpn_acl_profile_publish_exception" if publish_exception_topic.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s).sub('{' + 'topicSyntax' + '}', topic_syntax.to_s).sub('{' + 'publishExceptionTopic' + '}', publish_exception_topic.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_acl_profile_publish_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Subscribe Topic Exception object.
    # Deletes a Subscribe Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param topic_syntax The topicSyntax of the Subscribe Topic Exception.
    # @param subscribe_exception_topic The subscribeExceptionTopic of the Subscribe Topic Exception.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, opts = {})
      data, _status_code, _headers = delete_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, opts)
      return data
    end

    # Deletes a Subscribe Topic Exception object.
    # Deletes a Subscribe Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param topic_syntax The topicSyntax of the Subscribe Topic Exception.
    # @param subscribe_exception_topic The subscribeExceptionTopic of the Subscribe Topic Exception.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_acl_profile_subscribe_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_acl_profile_subscribe_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.delete_msg_vpn_acl_profile_subscribe_exception" if acl_profile_name.nil?
      # verify the required parameter 'topic_syntax' is set
      fail ArgumentError, "Missing the required parameter 'topic_syntax' when calling MsgVpnApi.delete_msg_vpn_acl_profile_subscribe_exception" if topic_syntax.nil?
      # verify the required parameter 'subscribe_exception_topic' is set
      fail ArgumentError, "Missing the required parameter 'subscribe_exception_topic' when calling MsgVpnApi.delete_msg_vpn_acl_profile_subscribe_exception" if subscribe_exception_topic.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s).sub('{' + 'topicSyntax' + '}', topic_syntax.to_s).sub('{' + 'subscribeExceptionTopic' + '}', subscribe_exception_topic.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_acl_profile_subscribe_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a LDAP Authorization Group object.
    # Deletes a LDAP Authorization Group object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, opts = {})
      data, _status_code, _headers = delete_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, opts)
      return data
    end

    # Deletes a LDAP Authorization Group object.
    # Deletes a LDAP Authorization Group object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_authorization_group ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_authorization_group" if msg_vpn_name.nil?
      # verify the required parameter 'authorization_group_name' is set
      fail ArgumentError, "Missing the required parameter 'authorization_group_name' when calling MsgVpnApi.delete_msg_vpn_authorization_group" if authorization_group_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'authorizationGroupName' + '}', authorization_group_name.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_authorization_group\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Bridge object.
    # Deletes a Bridge object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, opts = {})
      data, _status_code, _headers = delete_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, opts)
      return data
    end

    # Deletes a Bridge object.
    # Deletes a Bridge object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_bridge ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_bridge" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.delete_msg_vpn_bridge" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.delete_msg_vpn_bridge" if bridge_virtual_router.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_bridge\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Remote Message VPN object.
    # Deletes a Remote Message VPN object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_msg_vpn_name The remoteMsgVpnName of the Remote Message VPN.
    # @param remote_msg_vpn_location The remoteMsgVpnLocation of the Remote Message VPN.
    # @param remote_msg_vpn_interface The remoteMsgVpnInterface of the Remote Message VPN.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, opts = {})
      data, _status_code, _headers = delete_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, opts)
      return data
    end

    # Deletes a Remote Message VPN object.
    # Deletes a Remote Message VPN object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_msg_vpn_name The remoteMsgVpnName of the Remote Message VPN.
    # @param remote_msg_vpn_location The remoteMsgVpnLocation of the Remote Message VPN.
    # @param remote_msg_vpn_interface The remoteMsgVpnInterface of the Remote Message VPN.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_bridge_remote_msg_vpn ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_bridge_remote_msg_vpn" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.delete_msg_vpn_bridge_remote_msg_vpn" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.delete_msg_vpn_bridge_remote_msg_vpn" if bridge_virtual_router.nil?
      # verify the required parameter 'remote_msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_name.nil?
      # verify the required parameter 'remote_msg_vpn_location' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_location' when calling MsgVpnApi.delete_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_location.nil?
      # verify the required parameter 'remote_msg_vpn_interface' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_interface' when calling MsgVpnApi.delete_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_interface.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s).sub('{' + 'remoteMsgVpnName' + '}', remote_msg_vpn_name.to_s).sub('{' + 'remoteMsgVpnLocation' + '}', remote_msg_vpn_location.to_s).sub('{' + 'remoteMsgVpnInterface' + '}', remote_msg_vpn_interface.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_bridge_remote_msg_vpn\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Remote Subscription object.
    # Deletes a Remote Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_subscription_topic The remoteSubscriptionTopic of the Remote Subscription.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, opts = {})
      data, _status_code, _headers = delete_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, opts)
      return data
    end

    # Deletes a Remote Subscription object.
    # Deletes a Remote Subscription object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_subscription_topic The remoteSubscriptionTopic of the Remote Subscription.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_bridge_remote_subscription ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_bridge_remote_subscription" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.delete_msg_vpn_bridge_remote_subscription" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.delete_msg_vpn_bridge_remote_subscription" if bridge_virtual_router.nil?
      # verify the required parameter 'remote_subscription_topic' is set
      fail ArgumentError, "Missing the required parameter 'remote_subscription_topic' when calling MsgVpnApi.delete_msg_vpn_bridge_remote_subscription" if remote_subscription_topic.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s).sub('{' + 'remoteSubscriptionTopic' + '}', remote_subscription_topic.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_bridge_remote_subscription\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Trusted Common Name object.
    # Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, opts = {})
      data, _status_code, _headers = delete_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, opts)
      return data
    end

    # Deletes a Trusted Common Name object.
    # Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_bridge_tls_trusted_common_name ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_bridge_tls_trusted_common_name" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.delete_msg_vpn_bridge_tls_trusted_common_name" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.delete_msg_vpn_bridge_tls_trusted_common_name" if bridge_virtual_router.nil?
      # verify the required parameter 'tls_trusted_common_name' is set
      fail ArgumentError, "Missing the required parameter 'tls_trusted_common_name' when calling MsgVpnApi.delete_msg_vpn_bridge_tls_trusted_common_name" if tls_trusted_common_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s).sub('{' + 'tlsTrustedCommonName' + '}', tls_trusted_common_name.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_bridge_tls_trusted_common_name\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Client Profile object.
    # Deletes a Client Profile object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_profile_name The clientProfileName of the Client Profile.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_client_profile(msg_vpn_name, client_profile_name, opts = {})
      data, _status_code, _headers = delete_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, opts)
      return data
    end

    # Deletes a Client Profile object.
    # Deletes a Client Profile object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_profile_name The clientProfileName of the Client Profile.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_client_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_client_profile" if msg_vpn_name.nil?
      # verify the required parameter 'client_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'client_profile_name' when calling MsgVpnApi.delete_msg_vpn_client_profile" if client_profile_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'clientProfileName' + '}', client_profile_name.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_client_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Client Username object.
    # Deletes a Client Username object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_username The clientUsername of the Client Username.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_client_username(msg_vpn_name, client_username, opts = {})
      data, _status_code, _headers = delete_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, opts)
      return data
    end

    # Deletes a Client Username object.
    # Deletes a Client Username object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_username The clientUsername of the Client Username.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_client_username ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_client_username" if msg_vpn_name.nil?
      # verify the required parameter 'client_username' is set
      fail ArgumentError, "Missing the required parameter 'client_username' when calling MsgVpnApi.delete_msg_vpn_client_username" if client_username.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'clientUsername' + '}', client_username.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_client_username\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Queue object.
    # Deletes a Queue object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_queue(msg_vpn_name, queue_name, opts = {})
      data, _status_code, _headers = delete_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, opts)
      return data
    end

    # Deletes a Queue object.
    # Deletes a Queue object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_queue ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_queue" if msg_vpn_name.nil?
      # verify the required parameter 'queue_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_name' when calling MsgVpnApi.delete_msg_vpn_queue" if queue_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/queues/{queueName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'queueName' + '}', queue_name.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_queue\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Queue Subscription object.
    # Deletes a Queue Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param subscription_topic The subscriptionTopic of the Queue Subscription.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic, opts = {})
      data, _status_code, _headers = delete_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, subscription_topic, opts)
      return data
    end

    # Deletes a Queue Subscription object.
    # Deletes a Queue Subscription object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param subscription_topic The subscriptionTopic of the Queue Subscription.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, subscription_topic, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_queue_subscription ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_queue_subscription" if msg_vpn_name.nil?
      # verify the required parameter 'queue_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_name' when calling MsgVpnApi.delete_msg_vpn_queue_subscription" if queue_name.nil?
      # verify the required parameter 'subscription_topic' is set
      fail ArgumentError, "Missing the required parameter 'subscription_topic' when calling MsgVpnApi.delete_msg_vpn_queue_subscription" if subscription_topic.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'queueName' + '}', queue_name.to_s).sub('{' + 'subscriptionTopic' + '}', subscription_topic.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_queue_subscription\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a REST Delivery Point object.
    # Deletes a REST Delivery Point object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, opts = {})
      data, _status_code, _headers = delete_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, opts)
      return data
    end

    # Deletes a REST Delivery Point object.
    # Deletes a REST Delivery Point object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_rest_delivery_point ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point" if rest_delivery_point_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_rest_delivery_point\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Queue Binding object.
    # Deletes a Queue Binding object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param queue_binding_name The queueBindingName of the Queue Binding.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, opts = {})
      data, _status_code, _headers = delete_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, opts)
      return data
    end

    # Deletes a Queue Binding object.
    # Deletes a Queue Binding object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param queue_binding_name The queueBindingName of the Queue Binding.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_rest_delivery_point_queue_binding ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point_queue_binding" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point_queue_binding" if rest_delivery_point_name.nil?
      # verify the required parameter 'queue_binding_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_binding_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point_queue_binding" if queue_binding_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'queueBindingName' + '}', queue_binding_name.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_rest_delivery_point_queue_binding\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a REST Consumer object.
    # Deletes a REST Consumer object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts = {})
      data, _status_code, _headers = delete_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts)
      return data
    end

    # Deletes a REST Consumer object.
    # Deletes a REST Consumer object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_rest_delivery_point_rest_consumer ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point_rest_consumer" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point_rest_consumer" if rest_delivery_point_name.nil?
      # verify the required parameter 'rest_consumer_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_consumer_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point_rest_consumer" if rest_consumer_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'restConsumerName' + '}', rest_consumer_name.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_rest_delivery_point_rest_consumer\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Trusted Common Name object.
    # Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, opts = {})
      data, _status_code, _headers = delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, opts)
      return data
    end

    # Deletes a Trusted Common Name object.
    # Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if rest_delivery_point_name.nil?
      # verify the required parameter 'rest_consumer_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_consumer_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if rest_consumer_name.nil?
      # verify the required parameter 'tls_trusted_common_name' is set
      fail ArgumentError, "Missing the required parameter 'tls_trusted_common_name' when calling MsgVpnApi.delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if tls_trusted_common_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'restConsumerName' + '}', rest_consumer_name.to_s).sub('{' + 'tlsTrustedCommonName' + '}', tls_trusted_common_name.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Deletes a Sequenced Topic object.
    # Deletes a Sequenced Topic object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param sequenced_topic The sequencedTopic of the Sequenced Topic.
    # @param [Hash] opts the optional parameters
    # @return [SempMetaOnlyResponse]
    def delete_msg_vpn_sequenced_topic(msg_vpn_name, sequenced_topic, opts = {})
      data, _status_code, _headers = delete_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, sequenced_topic, opts)
      return data
    end

    # Deletes a Sequenced Topic object.
    # Deletes a Sequenced Topic object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param sequenced_topic The sequencedTopic of the Sequenced Topic.
    # @param [Hash] opts the optional parameters
    # @return [Array<(SempMetaOnlyResponse, Fixnum, Hash)>] SempMetaOnlyResponse data, response status code and response headers
    def delete_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, sequenced_topic, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.delete_msg_vpn_sequenced_topic ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.delete_msg_vpn_sequenced_topic" if msg_vpn_name.nil?
      # verify the required parameter 'sequenced_topic' is set
      fail ArgumentError, "Missing the required parameter 'sequenced_topic' when calling MsgVpnApi.delete_msg_vpn_sequenced_topic" if sequenced_topic.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'sequencedTopic' + '}', sequenced_topic.to_s)

      # query parameters
      query_params = {}

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'SempMetaOnlyResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#delete_msg_vpn_sequenced_topic\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Message VPN object.
    # Gets a Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnResponse]
    def get_msg_vpn(msg_vpn_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_with_http_info(msg_vpn_name, opts)
      return data
    end

    # Gets a Message VPN object.
    # Gets a Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnResponse, Fixnum, Hash)>] MsgVpnResponse data, response status code and response headers
    def get_msg_vpn_with_http_info(msg_vpn_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn" if msg_vpn_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets an ACL Profile object.
    # Gets an ACL Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfileResponse]
    def get_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, opts)
      return data
    end

    # Gets an ACL Profile object.
    # Gets an ACL Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfileResponse, Fixnum, Hash)>] MsgVpnAclProfileResponse data, response status code and response headers
    def get_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_acl_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_acl_profile" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.get_msg_vpn_acl_profile" if acl_profile_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfileResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_acl_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Client Connect Exception object.
    # Gets a Client Connect Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param client_connect_exception_address The clientConnectExceptionAddress of the Client Connect Exception.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfileClientConnectExceptionResponse]
    def get_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address, opts = {})
      data, _status_code, _headers = get_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, client_connect_exception_address, opts)
      return data
    end

    # Gets a Client Connect Exception object.
    # Gets a Client Connect Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param client_connect_exception_address The clientConnectExceptionAddress of the Client Connect Exception.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfileClientConnectExceptionResponse, Fixnum, Hash)>] MsgVpnAclProfileClientConnectExceptionResponse data, response status code and response headers
    def get_msg_vpn_acl_profile_client_connect_exception_with_http_info(msg_vpn_name, acl_profile_name, client_connect_exception_address, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_acl_profile_client_connect_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_client_connect_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_client_connect_exception" if acl_profile_name.nil?
      # verify the required parameter 'client_connect_exception_address' is set
      fail ArgumentError, "Missing the required parameter 'client_connect_exception_address' when calling MsgVpnApi.get_msg_vpn_acl_profile_client_connect_exception" if client_connect_exception_address.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s).sub('{' + 'clientConnectExceptionAddress' + '}', client_connect_exception_address.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfileClientConnectExceptionResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_acl_profile_client_connect_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Client Connect Exception objects.
    # Gets a list of Client Connect Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfileClientConnectExceptionsResponse]
    def get_msg_vpn_acl_profile_client_connect_exceptions(msg_vpn_name, acl_profile_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_acl_profile_client_connect_exceptions_with_http_info(msg_vpn_name, acl_profile_name, opts)
      return data
    end

    # Gets a list of Client Connect Exception objects.
    # Gets a list of Client Connect Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfileClientConnectExceptionsResponse, Fixnum, Hash)>] MsgVpnAclProfileClientConnectExceptionsResponse data, response status code and response headers
    def get_msg_vpn_acl_profile_client_connect_exceptions_with_http_info(msg_vpn_name, acl_profile_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_acl_profile_client_connect_exceptions ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_client_connect_exceptions" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_client_connect_exceptions" if acl_profile_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_acl_profile_client_connect_exceptions, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfileClientConnectExceptionsResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_acl_profile_client_connect_exceptions\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Publish Topic Exception object.
    # Gets a Publish Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param topic_syntax The topicSyntax of the Publish Topic Exception.
    # @param publish_exception_topic The publishExceptionTopic of the Publish Topic Exception.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfilePublishExceptionResponse]
    def get_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, opts = {})
      data, _status_code, _headers = get_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, opts)
      return data
    end

    # Gets a Publish Topic Exception object.
    # Gets a Publish Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param topic_syntax The topicSyntax of the Publish Topic Exception.
    # @param publish_exception_topic The publishExceptionTopic of the Publish Topic Exception.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfilePublishExceptionResponse, Fixnum, Hash)>] MsgVpnAclProfilePublishExceptionResponse data, response status code and response headers
    def get_msg_vpn_acl_profile_publish_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_acl_profile_publish_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_publish_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_publish_exception" if acl_profile_name.nil?
      # verify the required parameter 'topic_syntax' is set
      fail ArgumentError, "Missing the required parameter 'topic_syntax' when calling MsgVpnApi.get_msg_vpn_acl_profile_publish_exception" if topic_syntax.nil?
      # verify the required parameter 'publish_exception_topic' is set
      fail ArgumentError, "Missing the required parameter 'publish_exception_topic' when calling MsgVpnApi.get_msg_vpn_acl_profile_publish_exception" if publish_exception_topic.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s).sub('{' + 'topicSyntax' + '}', topic_syntax.to_s).sub('{' + 'publishExceptionTopic' + '}', publish_exception_topic.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfilePublishExceptionResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_acl_profile_publish_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Publish Topic Exception objects.
    # Gets a list of Publish Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfilePublishExceptionsResponse]
    def get_msg_vpn_acl_profile_publish_exceptions(msg_vpn_name, acl_profile_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_acl_profile_publish_exceptions_with_http_info(msg_vpn_name, acl_profile_name, opts)
      return data
    end

    # Gets a list of Publish Topic Exception objects.
    # Gets a list of Publish Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfilePublishExceptionsResponse, Fixnum, Hash)>] MsgVpnAclProfilePublishExceptionsResponse data, response status code and response headers
    def get_msg_vpn_acl_profile_publish_exceptions_with_http_info(msg_vpn_name, acl_profile_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_acl_profile_publish_exceptions ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_publish_exceptions" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_publish_exceptions" if acl_profile_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_acl_profile_publish_exceptions, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfilePublishExceptionsResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_acl_profile_publish_exceptions\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Subscribe Topic Exception object.
    # Gets a Subscribe Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param topic_syntax The topicSyntax of the Subscribe Topic Exception.
    # @param subscribe_exception_topic The subscribeExceptionTopic of the Subscribe Topic Exception.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfileSubscribeExceptionResponse]
    def get_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, opts = {})
      data, _status_code, _headers = get_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, opts)
      return data
    end

    # Gets a Subscribe Topic Exception object.
    # Gets a Subscribe Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param topic_syntax The topicSyntax of the Subscribe Topic Exception.
    # @param subscribe_exception_topic The subscribeExceptionTopic of the Subscribe Topic Exception.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfileSubscribeExceptionResponse, Fixnum, Hash)>] MsgVpnAclProfileSubscribeExceptionResponse data, response status code and response headers
    def get_msg_vpn_acl_profile_subscribe_exception_with_http_info(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_acl_profile_subscribe_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_subscribe_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_subscribe_exception" if acl_profile_name.nil?
      # verify the required parameter 'topic_syntax' is set
      fail ArgumentError, "Missing the required parameter 'topic_syntax' when calling MsgVpnApi.get_msg_vpn_acl_profile_subscribe_exception" if topic_syntax.nil?
      # verify the required parameter 'subscribe_exception_topic' is set
      fail ArgumentError, "Missing the required parameter 'subscribe_exception_topic' when calling MsgVpnApi.get_msg_vpn_acl_profile_subscribe_exception" if subscribe_exception_topic.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s).sub('{' + 'topicSyntax' + '}', topic_syntax.to_s).sub('{' + 'subscribeExceptionTopic' + '}', subscribe_exception_topic.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfileSubscribeExceptionResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_acl_profile_subscribe_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Subscribe Topic Exception objects.
    # Gets a list of Subscribe Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfileSubscribeExceptionsResponse]
    def get_msg_vpn_acl_profile_subscribe_exceptions(msg_vpn_name, acl_profile_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_acl_profile_subscribe_exceptions_with_http_info(msg_vpn_name, acl_profile_name, opts)
      return data
    end

    # Gets a list of Subscribe Topic Exception objects.
    # Gets a list of Subscribe Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfileSubscribeExceptionsResponse, Fixnum, Hash)>] MsgVpnAclProfileSubscribeExceptionsResponse data, response status code and response headers
    def get_msg_vpn_acl_profile_subscribe_exceptions_with_http_info(msg_vpn_name, acl_profile_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_acl_profile_subscribe_exceptions ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_subscribe_exceptions" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.get_msg_vpn_acl_profile_subscribe_exceptions" if acl_profile_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_acl_profile_subscribe_exceptions, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfileSubscribeExceptionsResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_acl_profile_subscribe_exceptions\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of ACL Profile objects.
    # Gets a list of ACL Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfilesResponse]
    def get_msg_vpn_acl_profiles(msg_vpn_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_acl_profiles_with_http_info(msg_vpn_name, opts)
      return data
    end

    # Gets a list of ACL Profile objects.
    # Gets a list of ACL Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfilesResponse, Fixnum, Hash)>] MsgVpnAclProfilesResponse data, response status code and response headers
    def get_msg_vpn_acl_profiles_with_http_info(msg_vpn_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_acl_profiles ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_acl_profiles" if msg_vpn_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_acl_profiles, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfilesResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_acl_profiles\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a LDAP Authorization Group object.
    # Gets a LDAP Authorization Group object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAuthorizationGroupResponse]
    def get_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, opts)
      return data
    end

    # Gets a LDAP Authorization Group object.
    # Gets a LDAP Authorization Group object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAuthorizationGroupResponse, Fixnum, Hash)>] MsgVpnAuthorizationGroupResponse data, response status code and response headers
    def get_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_authorization_group ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_authorization_group" if msg_vpn_name.nil?
      # verify the required parameter 'authorization_group_name' is set
      fail ArgumentError, "Missing the required parameter 'authorization_group_name' when calling MsgVpnApi.get_msg_vpn_authorization_group" if authorization_group_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'authorizationGroupName' + '}', authorization_group_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAuthorizationGroupResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_authorization_group\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of LDAP Authorization Group objects.
    # Gets a list of LDAP Authorization Group objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAuthorizationGroupsResponse]
    def get_msg_vpn_authorization_groups(msg_vpn_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_authorization_groups_with_http_info(msg_vpn_name, opts)
      return data
    end

    # Gets a list of LDAP Authorization Group objects.
    # Gets a list of LDAP Authorization Group objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAuthorizationGroupsResponse, Fixnum, Hash)>] MsgVpnAuthorizationGroupsResponse data, response status code and response headers
    def get_msg_vpn_authorization_groups_with_http_info(msg_vpn_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_authorization_groups ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_authorization_groups" if msg_vpn_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_authorization_groups, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/authorizationGroups".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAuthorizationGroupsResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_authorization_groups\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Bridge object.
    # Gets a Bridge object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeResponse]
    def get_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, opts = {})
      data, _status_code, _headers = get_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, opts)
      return data
    end

    # Gets a Bridge object.
    # Gets a Bridge object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeResponse, Fixnum, Hash)>] MsgVpnBridgeResponse data, response status code and response headers
    def get_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_bridge ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_bridge" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.get_msg_vpn_bridge" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.get_msg_vpn_bridge" if bridge_virtual_router.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_bridge\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Remote Message VPN object.
    # Gets a Remote Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_msg_vpn_name The remoteMsgVpnName of the Remote Message VPN.
    # @param remote_msg_vpn_location The remoteMsgVpnLocation of the Remote Message VPN.
    # @param remote_msg_vpn_interface The remoteMsgVpnInterface of the Remote Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeRemoteMsgVpnResponse]
    def get_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, opts = {})
      data, _status_code, _headers = get_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, opts)
      return data
    end

    # Gets a Remote Message VPN object.
    # Gets a Remote Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_msg_vpn_name The remoteMsgVpnName of the Remote Message VPN.
    # @param remote_msg_vpn_location The remoteMsgVpnLocation of the Remote Message VPN.
    # @param remote_msg_vpn_interface The remoteMsgVpnInterface of the Remote Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeRemoteMsgVpnResponse, Fixnum, Hash)>] MsgVpnBridgeRemoteMsgVpnResponse data, response status code and response headers
    def get_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_bridge_remote_msg_vpn ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_bridge_remote_msg_vpn" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.get_msg_vpn_bridge_remote_msg_vpn" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.get_msg_vpn_bridge_remote_msg_vpn" if bridge_virtual_router.nil?
      # verify the required parameter 'remote_msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_name.nil?
      # verify the required parameter 'remote_msg_vpn_location' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_location' when calling MsgVpnApi.get_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_location.nil?
      # verify the required parameter 'remote_msg_vpn_interface' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_interface' when calling MsgVpnApi.get_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_interface.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s).sub('{' + 'remoteMsgVpnName' + '}', remote_msg_vpn_name.to_s).sub('{' + 'remoteMsgVpnLocation' + '}', remote_msg_vpn_location.to_s).sub('{' + 'remoteMsgVpnInterface' + '}', remote_msg_vpn_interface.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeRemoteMsgVpnResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_bridge_remote_msg_vpn\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Remote Message VPN objects.
    # Gets a list of Remote Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeRemoteMsgVpnsResponse]
    def get_msg_vpn_bridge_remote_msg_vpns(msg_vpn_name, bridge_name, bridge_virtual_router, opts = {})
      data, _status_code, _headers = get_msg_vpn_bridge_remote_msg_vpns_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, opts)
      return data
    end

    # Gets a list of Remote Message VPN objects.
    # Gets a list of Remote Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeRemoteMsgVpnsResponse, Fixnum, Hash)>] MsgVpnBridgeRemoteMsgVpnsResponse data, response status code and response headers
    def get_msg_vpn_bridge_remote_msg_vpns_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_bridge_remote_msg_vpns ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_bridge_remote_msg_vpns" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.get_msg_vpn_bridge_remote_msg_vpns" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.get_msg_vpn_bridge_remote_msg_vpns" if bridge_virtual_router.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s)

      # query parameters
      query_params = {}
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeRemoteMsgVpnsResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_bridge_remote_msg_vpns\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Remote Subscription object.
    # Gets a Remote Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_subscription_topic The remoteSubscriptionTopic of the Remote Subscription.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeRemoteSubscriptionResponse]
    def get_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, opts = {})
      data, _status_code, _headers = get_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, opts)
      return data
    end

    # Gets a Remote Subscription object.
    # Gets a Remote Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_subscription_topic The remoteSubscriptionTopic of the Remote Subscription.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeRemoteSubscriptionResponse, Fixnum, Hash)>] MsgVpnBridgeRemoteSubscriptionResponse data, response status code and response headers
    def get_msg_vpn_bridge_remote_subscription_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_bridge_remote_subscription ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_bridge_remote_subscription" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.get_msg_vpn_bridge_remote_subscription" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.get_msg_vpn_bridge_remote_subscription" if bridge_virtual_router.nil?
      # verify the required parameter 'remote_subscription_topic' is set
      fail ArgumentError, "Missing the required parameter 'remote_subscription_topic' when calling MsgVpnApi.get_msg_vpn_bridge_remote_subscription" if remote_subscription_topic.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s).sub('{' + 'remoteSubscriptionTopic' + '}', remote_subscription_topic.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeRemoteSubscriptionResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_bridge_remote_subscription\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Remote Subscription objects.
    # Gets a list of Remote Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeRemoteSubscriptionsResponse]
    def get_msg_vpn_bridge_remote_subscriptions(msg_vpn_name, bridge_name, bridge_virtual_router, opts = {})
      data, _status_code, _headers = get_msg_vpn_bridge_remote_subscriptions_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, opts)
      return data
    end

    # Gets a list of Remote Subscription objects.
    # Gets a list of Remote Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeRemoteSubscriptionsResponse, Fixnum, Hash)>] MsgVpnBridgeRemoteSubscriptionsResponse data, response status code and response headers
    def get_msg_vpn_bridge_remote_subscriptions_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_bridge_remote_subscriptions ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_bridge_remote_subscriptions" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.get_msg_vpn_bridge_remote_subscriptions" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.get_msg_vpn_bridge_remote_subscriptions" if bridge_virtual_router.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_bridge_remote_subscriptions, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeRemoteSubscriptionsResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_bridge_remote_subscriptions\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Trusted Common Name object.
    # Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeTlsTrustedCommonNameResponse]
    def get_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, opts)
      return data
    end

    # Gets a Trusted Common Name object.
    # Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeTlsTrustedCommonNameResponse, Fixnum, Hash)>] MsgVpnBridgeTlsTrustedCommonNameResponse data, response status code and response headers
    def get_msg_vpn_bridge_tls_trusted_common_name_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_bridge_tls_trusted_common_name ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_bridge_tls_trusted_common_name" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.get_msg_vpn_bridge_tls_trusted_common_name" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.get_msg_vpn_bridge_tls_trusted_common_name" if bridge_virtual_router.nil?
      # verify the required parameter 'tls_trusted_common_name' is set
      fail ArgumentError, "Missing the required parameter 'tls_trusted_common_name' when calling MsgVpnApi.get_msg_vpn_bridge_tls_trusted_common_name" if tls_trusted_common_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s).sub('{' + 'tlsTrustedCommonName' + '}', tls_trusted_common_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeTlsTrustedCommonNameResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_bridge_tls_trusted_common_name\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Trusted Common Name objects.
    # Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeTlsTrustedCommonNamesResponse]
    def get_msg_vpn_bridge_tls_trusted_common_names(msg_vpn_name, bridge_name, bridge_virtual_router, opts = {})
      data, _status_code, _headers = get_msg_vpn_bridge_tls_trusted_common_names_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, opts)
      return data
    end

    # Gets a list of Trusted Common Name objects.
    # Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeTlsTrustedCommonNamesResponse, Fixnum, Hash)>] MsgVpnBridgeTlsTrustedCommonNamesResponse data, response status code and response headers
    def get_msg_vpn_bridge_tls_trusted_common_names_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_bridge_tls_trusted_common_names ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_bridge_tls_trusted_common_names" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.get_msg_vpn_bridge_tls_trusted_common_names" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.get_msg_vpn_bridge_tls_trusted_common_names" if bridge_virtual_router.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s)

      # query parameters
      query_params = {}
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeTlsTrustedCommonNamesResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_bridge_tls_trusted_common_names\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Bridge objects.
    # Gets a list of Bridge objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgesResponse]
    def get_msg_vpn_bridges(msg_vpn_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_bridges_with_http_info(msg_vpn_name, opts)
      return data
    end

    # Gets a list of Bridge objects.
    # Gets a list of Bridge objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgesResponse, Fixnum, Hash)>] MsgVpnBridgesResponse data, response status code and response headers
    def get_msg_vpn_bridges_with_http_info(msg_vpn_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_bridges ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_bridges" if msg_vpn_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_bridges, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgesResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_bridges\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Client Profile object.
    # Gets a Client Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_profile_name The clientProfileName of the Client Profile.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnClientProfileResponse]
    def get_msg_vpn_client_profile(msg_vpn_name, client_profile_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, opts)
      return data
    end

    # Gets a Client Profile object.
    # Gets a Client Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_profile_name The clientProfileName of the Client Profile.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnClientProfileResponse, Fixnum, Hash)>] MsgVpnClientProfileResponse data, response status code and response headers
    def get_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_client_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_client_profile" if msg_vpn_name.nil?
      # verify the required parameter 'client_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'client_profile_name' when calling MsgVpnApi.get_msg_vpn_client_profile" if client_profile_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'clientProfileName' + '}', client_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnClientProfileResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_client_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Client Profile objects.
    # Gets a list of Client Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnClientProfilesResponse]
    def get_msg_vpn_client_profiles(msg_vpn_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_client_profiles_with_http_info(msg_vpn_name, opts)
      return data
    end

    # Gets a list of Client Profile objects.
    # Gets a list of Client Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnClientProfilesResponse, Fixnum, Hash)>] MsgVpnClientProfilesResponse data, response status code and response headers
    def get_msg_vpn_client_profiles_with_http_info(msg_vpn_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_client_profiles ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_client_profiles" if msg_vpn_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_client_profiles, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientProfiles".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnClientProfilesResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_client_profiles\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Client Username object.
    # Gets a Client Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_username The clientUsername of the Client Username.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnClientUsernameResponse]
    def get_msg_vpn_client_username(msg_vpn_name, client_username, opts = {})
      data, _status_code, _headers = get_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, opts)
      return data
    end

    # Gets a Client Username object.
    # Gets a Client Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_username The clientUsername of the Client Username.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnClientUsernameResponse, Fixnum, Hash)>] MsgVpnClientUsernameResponse data, response status code and response headers
    def get_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_client_username ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_client_username" if msg_vpn_name.nil?
      # verify the required parameter 'client_username' is set
      fail ArgumentError, "Missing the required parameter 'client_username' when calling MsgVpnApi.get_msg_vpn_client_username" if client_username.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'clientUsername' + '}', client_username.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnClientUsernameResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_client_username\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Client Username objects.
    # Gets a list of Client Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnClientUsernamesResponse]
    def get_msg_vpn_client_usernames(msg_vpn_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_client_usernames_with_http_info(msg_vpn_name, opts)
      return data
    end

    # Gets a list of Client Username objects.
    # Gets a list of Client Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnClientUsernamesResponse, Fixnum, Hash)>] MsgVpnClientUsernamesResponse data, response status code and response headers
    def get_msg_vpn_client_usernames_with_http_info(msg_vpn_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_client_usernames ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_client_usernames" if msg_vpn_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_client_usernames, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientUsernames".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnClientUsernamesResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_client_usernames\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Queue object.
    # Gets a Queue object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnQueueResponse]
    def get_msg_vpn_queue(msg_vpn_name, queue_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, opts)
      return data
    end

    # Gets a Queue object.
    # Gets a Queue object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnQueueResponse, Fixnum, Hash)>] MsgVpnQueueResponse data, response status code and response headers
    def get_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_queue ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_queue" if msg_vpn_name.nil?
      # verify the required parameter 'queue_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_name' when calling MsgVpnApi.get_msg_vpn_queue" if queue_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/queues/{queueName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'queueName' + '}', queue_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnQueueResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_queue\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Queue Subscription object.
    # Gets a Queue Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param subscription_topic The subscriptionTopic of the Queue Subscription.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnQueueSubscriptionResponse]
    def get_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic, opts = {})
      data, _status_code, _headers = get_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, subscription_topic, opts)
      return data
    end

    # Gets a Queue Subscription object.
    # Gets a Queue Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param subscription_topic The subscriptionTopic of the Queue Subscription.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnQueueSubscriptionResponse, Fixnum, Hash)>] MsgVpnQueueSubscriptionResponse data, response status code and response headers
    def get_msg_vpn_queue_subscription_with_http_info(msg_vpn_name, queue_name, subscription_topic, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_queue_subscription ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_queue_subscription" if msg_vpn_name.nil?
      # verify the required parameter 'queue_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_name' when calling MsgVpnApi.get_msg_vpn_queue_subscription" if queue_name.nil?
      # verify the required parameter 'subscription_topic' is set
      fail ArgumentError, "Missing the required parameter 'subscription_topic' when calling MsgVpnApi.get_msg_vpn_queue_subscription" if subscription_topic.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'queueName' + '}', queue_name.to_s).sub('{' + 'subscriptionTopic' + '}', subscription_topic.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnQueueSubscriptionResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_queue_subscription\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Queue Subscription objects.
    # Gets a list of Queue Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnQueueSubscriptionsResponse]
    def get_msg_vpn_queue_subscriptions(msg_vpn_name, queue_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_queue_subscriptions_with_http_info(msg_vpn_name, queue_name, opts)
      return data
    end

    # Gets a list of Queue Subscription objects.
    # Gets a list of Queue Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnQueueSubscriptionsResponse, Fixnum, Hash)>] MsgVpnQueueSubscriptionsResponse data, response status code and response headers
    def get_msg_vpn_queue_subscriptions_with_http_info(msg_vpn_name, queue_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_queue_subscriptions ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_queue_subscriptions" if msg_vpn_name.nil?
      # verify the required parameter 'queue_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_name' when calling MsgVpnApi.get_msg_vpn_queue_subscriptions" if queue_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_queue_subscriptions, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/queues/{queueName}/subscriptions".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'queueName' + '}', queue_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnQueueSubscriptionsResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_queue_subscriptions\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Queue objects.
    # Gets a list of Queue objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnQueuesResponse]
    def get_msg_vpn_queues(msg_vpn_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_queues_with_http_info(msg_vpn_name, opts)
      return data
    end

    # Gets a list of Queue objects.
    # Gets a list of Queue objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnQueuesResponse, Fixnum, Hash)>] MsgVpnQueuesResponse data, response status code and response headers
    def get_msg_vpn_queues_with_http_info(msg_vpn_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_queues ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_queues" if msg_vpn_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_queues, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/queues".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnQueuesResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_queues\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a REST Delivery Point object.
    # Gets a REST Delivery Point object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointResponse]
    def get_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, opts)
      return data
    end

    # Gets a REST Delivery Point object.
    # Gets a REST Delivery Point object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointResponse data, response status code and response headers
    def get_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_rest_delivery_point ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point" if rest_delivery_point_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_rest_delivery_point\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Queue Binding object.
    # Gets a Queue Binding object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param queue_binding_name The queueBindingName of the Queue Binding.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointQueueBindingResponse]
    def get_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, opts)
      return data
    end

    # Gets a Queue Binding object.
    # Gets a Queue Binding object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param queue_binding_name The queueBindingName of the Queue Binding.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointQueueBindingResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointQueueBindingResponse data, response status code and response headers
    def get_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_rest_delivery_point_queue_binding ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_queue_binding" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_queue_binding" if rest_delivery_point_name.nil?
      # verify the required parameter 'queue_binding_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_binding_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_queue_binding" if queue_binding_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'queueBindingName' + '}', queue_binding_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointQueueBindingResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_rest_delivery_point_queue_binding\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Queue Binding objects.
    # Gets a list of Queue Binding objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointQueueBindingsResponse]
    def get_msg_vpn_rest_delivery_point_queue_bindings(msg_vpn_name, rest_delivery_point_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_rest_delivery_point_queue_bindings_with_http_info(msg_vpn_name, rest_delivery_point_name, opts)
      return data
    end

    # Gets a list of Queue Binding objects.
    # Gets a list of Queue Binding objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointQueueBindingsResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointQueueBindingsResponse data, response status code and response headers
    def get_msg_vpn_rest_delivery_point_queue_bindings_with_http_info(msg_vpn_name, rest_delivery_point_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_rest_delivery_point_queue_bindings ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_queue_bindings" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_queue_bindings" if rest_delivery_point_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_queue_bindings, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointQueueBindingsResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_rest_delivery_point_queue_bindings\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a REST Consumer object.
    # Gets a REST Consumer object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointRestConsumerResponse]
    def get_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts)
      return data
    end

    # Gets a REST Consumer object.
    # Gets a REST Consumer object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointRestConsumerResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointRestConsumerResponse data, response status code and response headers
    def get_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer" if rest_delivery_point_name.nil?
      # verify the required parameter 'rest_consumer_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_consumer_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer" if rest_consumer_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'restConsumerName' + '}', rest_consumer_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointRestConsumerResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_rest_delivery_point_rest_consumer\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Trusted Common Name object.
    # Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse]
    def get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, opts)
      return data
    end

    # Gets a Trusted Common Name object.
    # Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse data, response status code and response headers
    def get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if rest_delivery_point_name.nil?
      # verify the required parameter 'rest_consumer_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_consumer_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if rest_consumer_name.nil?
      # verify the required parameter 'tls_trusted_common_name' is set
      fail ArgumentError, "Missing the required parameter 'tls_trusted_common_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name" if tls_trusted_common_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'restConsumerName' + '}', rest_consumer_name.to_s).sub('{' + 'tlsTrustedCommonName' + '}', tls_trusted_common_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Trusted Common Name objects.
    # Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse]
    def get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts)
      return data
    end

    # Gets a list of Trusted Common Name objects.
    # Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse data, response status code and response headers
    def get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names" if rest_delivery_point_name.nil?
      # verify the required parameter 'rest_consumer_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_consumer_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names" if rest_consumer_name.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'restConsumerName' + '}', rest_consumer_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of REST Consumer objects.
    # Gets a list of REST Consumer objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointRestConsumersResponse]
    def get_msg_vpn_rest_delivery_point_rest_consumers(msg_vpn_name, rest_delivery_point_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_rest_delivery_point_rest_consumers_with_http_info(msg_vpn_name, rest_delivery_point_name, opts)
      return data
    end

    # Gets a list of REST Consumer objects.
    # Gets a list of REST Consumer objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointRestConsumersResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointRestConsumersResponse data, response status code and response headers
    def get_msg_vpn_rest_delivery_point_rest_consumers_with_http_info(msg_vpn_name, rest_delivery_point_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumers ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumers" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumers" if rest_delivery_point_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_rest_delivery_point_rest_consumers, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointRestConsumersResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_rest_delivery_point_rest_consumers\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of REST Delivery Point objects.
    # Gets a list of REST Delivery Point objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointsResponse]
    def get_msg_vpn_rest_delivery_points(msg_vpn_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_rest_delivery_points_with_http_info(msg_vpn_name, opts)
      return data
    end

    # Gets a list of REST Delivery Point objects.
    # Gets a list of REST Delivery Point objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointsResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointsResponse data, response status code and response headers
    def get_msg_vpn_rest_delivery_points_with_http_info(msg_vpn_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_rest_delivery_points ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_rest_delivery_points" if msg_vpn_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_rest_delivery_points, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointsResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_rest_delivery_points\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a Sequenced Topic object.
    # Gets a Sequenced Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param sequenced_topic The sequencedTopic of the Sequenced Topic.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnSequencedTopicResponse]
    def get_msg_vpn_sequenced_topic(msg_vpn_name, sequenced_topic, opts = {})
      data, _status_code, _headers = get_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, sequenced_topic, opts)
      return data
    end

    # Gets a Sequenced Topic object.
    # Gets a Sequenced Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param sequenced_topic The sequencedTopic of the Sequenced Topic.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnSequencedTopicResponse, Fixnum, Hash)>] MsgVpnSequencedTopicResponse data, response status code and response headers
    def get_msg_vpn_sequenced_topic_with_http_info(msg_vpn_name, sequenced_topic, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_sequenced_topic ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_sequenced_topic" if msg_vpn_name.nil?
      # verify the required parameter 'sequenced_topic' is set
      fail ArgumentError, "Missing the required parameter 'sequenced_topic' when calling MsgVpnApi.get_msg_vpn_sequenced_topic" if sequenced_topic.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'sequencedTopic' + '}', sequenced_topic.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnSequencedTopicResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_sequenced_topic\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Sequenced Topic objects.
    # Gets a list of Sequenced Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnSequencedTopicsResponse]
    def get_msg_vpn_sequenced_topics(msg_vpn_name, opts = {})
      data, _status_code, _headers = get_msg_vpn_sequenced_topics_with_http_info(msg_vpn_name, opts)
      return data
    end

    # Gets a list of Sequenced Topic objects.
    # Gets a list of Sequenced Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnSequencedTopicsResponse, Fixnum, Hash)>] MsgVpnSequencedTopicsResponse data, response status code and response headers
    def get_msg_vpn_sequenced_topics_with_http_info(msg_vpn_name, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpn_sequenced_topics ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.get_msg_vpn_sequenced_topics" if msg_vpn_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpn_sequenced_topics, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/sequencedTopics".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnSequencedTopicsResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpn_sequenced_topics\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Gets a list of Message VPN objects.
    # Gets a list of Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). (default to 10)
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnsResponse]
    def get_msg_vpns(opts = {})
      data, _status_code, _headers = get_msg_vpns_with_http_info(opts)
      return data
    end

    # Gets a list of Message VPN objects.
    # Gets a list of Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
    # @param [Hash] opts the optional parameters
    # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
    # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
    # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnsResponse, Fixnum, Hash)>] MsgVpnsResponse data, response status code and response headers
    def get_msg_vpns_with_http_info(opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.get_msg_vpns ..."
      end
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling MsgVpnApi.get_msg_vpns, must be greater than or equal to 1.0.'
      end

      # resource path
      local_var_path = "/msgVpns".sub('{format}','json')

      # query parameters
      query_params = {}
      query_params[:'count'] = opts[:'count'] if !opts[:'count'].nil?
      query_params[:'cursor'] = opts[:'cursor'] if !opts[:'cursor'].nil?
      query_params[:'where'] = @api_client.build_collection_param(opts[:'where'], :multi) if !opts[:'where'].nil?
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = nil
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:GET, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnsResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#get_msg_vpns\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Replaces a Message VPN object.
    # Replaces a Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnResponse]
    def replace_msg_vpn(msg_vpn_name, body, opts = {})
      data, _status_code, _headers = replace_msg_vpn_with_http_info(msg_vpn_name, body, opts)
      return data
    end

    # Replaces a Message VPN object.
    # Replaces a Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnResponse, Fixnum, Hash)>] MsgVpnResponse data, response status code and response headers
    def replace_msg_vpn_with_http_info(msg_vpn_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.replace_msg_vpn ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn" if msg_vpn_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.replace_msg_vpn" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PUT, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#replace_msg_vpn\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Replaces an ACL Profile object.
    # Replaces an ACL Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param body The ACL Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfileResponse]
    def replace_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, opts = {})
      data, _status_code, _headers = replace_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, body, opts)
      return data
    end

    # Replaces an ACL Profile object.
    # Replaces an ACL Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param body The ACL Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfileResponse, Fixnum, Hash)>] MsgVpnAclProfileResponse data, response status code and response headers
    def replace_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.replace_msg_vpn_acl_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn_acl_profile" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.replace_msg_vpn_acl_profile" if acl_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.replace_msg_vpn_acl_profile" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PUT, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfileResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#replace_msg_vpn_acl_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Replaces a LDAP Authorization Group object.
    # Replaces a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
    # @param body The LDAP Authorization Group object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAuthorizationGroupResponse]
    def replace_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, opts = {})
      data, _status_code, _headers = replace_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, body, opts)
      return data
    end

    # Replaces a LDAP Authorization Group object.
    # Replaces a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
    # @param body The LDAP Authorization Group object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAuthorizationGroupResponse, Fixnum, Hash)>] MsgVpnAuthorizationGroupResponse data, response status code and response headers
    def replace_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.replace_msg_vpn_authorization_group ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn_authorization_group" if msg_vpn_name.nil?
      # verify the required parameter 'authorization_group_name' is set
      fail ArgumentError, "Missing the required parameter 'authorization_group_name' when calling MsgVpnApi.replace_msg_vpn_authorization_group" if authorization_group_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.replace_msg_vpn_authorization_group" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'authorizationGroupName' + '}', authorization_group_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PUT, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAuthorizationGroupResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#replace_msg_vpn_authorization_group\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Replaces a Bridge object.
    # Replaces a Bridge object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param body The Bridge object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeResponse]
    def replace_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts = {})
      data, _status_code, _headers = replace_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts)
      return data
    end

    # Replaces a Bridge object.
    # Replaces a Bridge object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param body The Bridge object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeResponse, Fixnum, Hash)>] MsgVpnBridgeResponse data, response status code and response headers
    def replace_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.replace_msg_vpn_bridge ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn_bridge" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.replace_msg_vpn_bridge" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.replace_msg_vpn_bridge" if bridge_virtual_router.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.replace_msg_vpn_bridge" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PUT, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#replace_msg_vpn_bridge\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Replaces a Remote Message VPN object.
    # Replaces a Remote Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_msg_vpn_name The remoteMsgVpnName of the Remote Message VPN.
    # @param remote_msg_vpn_location The remoteMsgVpnLocation of the Remote Message VPN.
    # @param remote_msg_vpn_interface The remoteMsgVpnInterface of the Remote Message VPN.
    # @param body The Remote Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeRemoteMsgVpnResponse]
    def replace_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, opts = {})
      data, _status_code, _headers = replace_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, opts)
      return data
    end

    # Replaces a Remote Message VPN object.
    # Replaces a Remote Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_msg_vpn_name The remoteMsgVpnName of the Remote Message VPN.
    # @param remote_msg_vpn_location The remoteMsgVpnLocation of the Remote Message VPN.
    # @param remote_msg_vpn_interface The remoteMsgVpnInterface of the Remote Message VPN.
    # @param body The Remote Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeRemoteMsgVpnResponse, Fixnum, Hash)>] MsgVpnBridgeRemoteMsgVpnResponse data, response status code and response headers
    def replace_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.replace_msg_vpn_bridge_remote_msg_vpn ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn_bridge_remote_msg_vpn" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.replace_msg_vpn_bridge_remote_msg_vpn" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.replace_msg_vpn_bridge_remote_msg_vpn" if bridge_virtual_router.nil?
      # verify the required parameter 'remote_msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_name.nil?
      # verify the required parameter 'remote_msg_vpn_location' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_location' when calling MsgVpnApi.replace_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_location.nil?
      # verify the required parameter 'remote_msg_vpn_interface' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_interface' when calling MsgVpnApi.replace_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_interface.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.replace_msg_vpn_bridge_remote_msg_vpn" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s).sub('{' + 'remoteMsgVpnName' + '}', remote_msg_vpn_name.to_s).sub('{' + 'remoteMsgVpnLocation' + '}', remote_msg_vpn_location.to_s).sub('{' + 'remoteMsgVpnInterface' + '}', remote_msg_vpn_interface.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PUT, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeRemoteMsgVpnResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#replace_msg_vpn_bridge_remote_msg_vpn\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Replaces a Client Profile object.
    # Replaces a Client Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_profile_name The clientProfileName of the Client Profile.
    # @param body The Client Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnClientProfileResponse]
    def replace_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, opts = {})
      data, _status_code, _headers = replace_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, body, opts)
      return data
    end

    # Replaces a Client Profile object.
    # Replaces a Client Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_profile_name The clientProfileName of the Client Profile.
    # @param body The Client Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnClientProfileResponse, Fixnum, Hash)>] MsgVpnClientProfileResponse data, response status code and response headers
    def replace_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.replace_msg_vpn_client_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn_client_profile" if msg_vpn_name.nil?
      # verify the required parameter 'client_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'client_profile_name' when calling MsgVpnApi.replace_msg_vpn_client_profile" if client_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.replace_msg_vpn_client_profile" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'clientProfileName' + '}', client_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PUT, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnClientProfileResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#replace_msg_vpn_client_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Replaces a Client Username object.
    # Replaces a Client Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_username The clientUsername of the Client Username.
    # @param body The Client Username object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnClientUsernameResponse]
    def replace_msg_vpn_client_username(msg_vpn_name, client_username, body, opts = {})
      data, _status_code, _headers = replace_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, body, opts)
      return data
    end

    # Replaces a Client Username object.
    # Replaces a Client Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_username The clientUsername of the Client Username.
    # @param body The Client Username object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnClientUsernameResponse, Fixnum, Hash)>] MsgVpnClientUsernameResponse data, response status code and response headers
    def replace_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.replace_msg_vpn_client_username ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn_client_username" if msg_vpn_name.nil?
      # verify the required parameter 'client_username' is set
      fail ArgumentError, "Missing the required parameter 'client_username' when calling MsgVpnApi.replace_msg_vpn_client_username" if client_username.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.replace_msg_vpn_client_username" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'clientUsername' + '}', client_username.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PUT, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnClientUsernameResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#replace_msg_vpn_client_username\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Replaces a Queue object.
    # Replaces a Queue object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param body The Queue object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnQueueResponse]
    def replace_msg_vpn_queue(msg_vpn_name, queue_name, body, opts = {})
      data, _status_code, _headers = replace_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, body, opts)
      return data
    end

    # Replaces a Queue object.
    # Replaces a Queue object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param body The Queue object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnQueueResponse, Fixnum, Hash)>] MsgVpnQueueResponse data, response status code and response headers
    def replace_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.replace_msg_vpn_queue ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn_queue" if msg_vpn_name.nil?
      # verify the required parameter 'queue_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_name' when calling MsgVpnApi.replace_msg_vpn_queue" if queue_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.replace_msg_vpn_queue" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/queues/{queueName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'queueName' + '}', queue_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PUT, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnQueueResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#replace_msg_vpn_queue\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Replaces a REST Delivery Point object.
    # Replaces a REST Delivery Point object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param body The REST Delivery Point object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointResponse]
    def replace_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, opts = {})
      data, _status_code, _headers = replace_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, body, opts)
      return data
    end

    # Replaces a REST Delivery Point object.
    # Replaces a REST Delivery Point object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param body The REST Delivery Point object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointResponse data, response status code and response headers
    def replace_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.replace_msg_vpn_rest_delivery_point ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn_rest_delivery_point" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.replace_msg_vpn_rest_delivery_point" if rest_delivery_point_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.replace_msg_vpn_rest_delivery_point" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PUT, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#replace_msg_vpn_rest_delivery_point\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Replaces a Queue Binding object.
    # Replaces a Queue Binding object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param queue_binding_name The queueBindingName of the Queue Binding.
    # @param body The Queue Binding object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointQueueBindingResponse]
    def replace_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, opts = {})
      data, _status_code, _headers = replace_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, opts)
      return data
    end

    # Replaces a Queue Binding object.
    # Replaces a Queue Binding object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param queue_binding_name The queueBindingName of the Queue Binding.
    # @param body The Queue Binding object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointQueueBindingResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointQueueBindingResponse data, response status code and response headers
    def replace_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.replace_msg_vpn_rest_delivery_point_queue_binding ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn_rest_delivery_point_queue_binding" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.replace_msg_vpn_rest_delivery_point_queue_binding" if rest_delivery_point_name.nil?
      # verify the required parameter 'queue_binding_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_binding_name' when calling MsgVpnApi.replace_msg_vpn_rest_delivery_point_queue_binding" if queue_binding_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.replace_msg_vpn_rest_delivery_point_queue_binding" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'queueBindingName' + '}', queue_binding_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PUT, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointQueueBindingResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#replace_msg_vpn_rest_delivery_point_queue_binding\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Replaces a REST Consumer object.
    # Replaces a REST Consumer object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param body The REST Consumer object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointRestConsumerResponse]
    def replace_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts = {})
      data, _status_code, _headers = replace_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts)
      return data
    end

    # Replaces a REST Consumer object.
    # Replaces a REST Consumer object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param body The REST Consumer object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointRestConsumerResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointRestConsumerResponse data, response status code and response headers
    def replace_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.replace_msg_vpn_rest_delivery_point_rest_consumer ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.replace_msg_vpn_rest_delivery_point_rest_consumer" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.replace_msg_vpn_rest_delivery_point_rest_consumer" if rest_delivery_point_name.nil?
      # verify the required parameter 'rest_consumer_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_consumer_name' when calling MsgVpnApi.replace_msg_vpn_rest_delivery_point_rest_consumer" if rest_consumer_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.replace_msg_vpn_rest_delivery_point_rest_consumer" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'restConsumerName' + '}', rest_consumer_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PUT, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointRestConsumerResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#replace_msg_vpn_rest_delivery_point_rest_consumer\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Updates a Message VPN object.
    # Updates a Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnResponse]
    def update_msg_vpn(msg_vpn_name, body, opts = {})
      data, _status_code, _headers = update_msg_vpn_with_http_info(msg_vpn_name, body, opts)
      return data
    end

    # Updates a Message VPN object.
    # Updates a Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param body The Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnResponse, Fixnum, Hash)>] MsgVpnResponse data, response status code and response headers
    def update_msg_vpn_with_http_info(msg_vpn_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.update_msg_vpn ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.update_msg_vpn" if msg_vpn_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.update_msg_vpn" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PATCH, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#update_msg_vpn\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Updates an ACL Profile object.
    # Updates an ACL Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param body The ACL Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAclProfileResponse]
    def update_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, opts = {})
      data, _status_code, _headers = update_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, body, opts)
      return data
    end

    # Updates an ACL Profile object.
    # Updates an ACL Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param acl_profile_name The aclProfileName of the ACL Profile.
    # @param body The ACL Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAclProfileResponse, Fixnum, Hash)>] MsgVpnAclProfileResponse data, response status code and response headers
    def update_msg_vpn_acl_profile_with_http_info(msg_vpn_name, acl_profile_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.update_msg_vpn_acl_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.update_msg_vpn_acl_profile" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling MsgVpnApi.update_msg_vpn_acl_profile" if acl_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.update_msg_vpn_acl_profile" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'aclProfileName' + '}', acl_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PATCH, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAclProfileResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#update_msg_vpn_acl_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Updates a LDAP Authorization Group object.
    # Updates a LDAP Authorization Group object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
    # @param body The LDAP Authorization Group object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnAuthorizationGroupResponse]
    def update_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, opts = {})
      data, _status_code, _headers = update_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, body, opts)
      return data
    end

    # Updates a LDAP Authorization Group object.
    # Updates a LDAP Authorization Group object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
    # @param body The LDAP Authorization Group object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnAuthorizationGroupResponse, Fixnum, Hash)>] MsgVpnAuthorizationGroupResponse data, response status code and response headers
    def update_msg_vpn_authorization_group_with_http_info(msg_vpn_name, authorization_group_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.update_msg_vpn_authorization_group ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.update_msg_vpn_authorization_group" if msg_vpn_name.nil?
      # verify the required parameter 'authorization_group_name' is set
      fail ArgumentError, "Missing the required parameter 'authorization_group_name' when calling MsgVpnApi.update_msg_vpn_authorization_group" if authorization_group_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.update_msg_vpn_authorization_group" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'authorizationGroupName' + '}', authorization_group_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PATCH, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnAuthorizationGroupResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#update_msg_vpn_authorization_group\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Updates a Bridge object.
    # Updates a Bridge object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param body The Bridge object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeResponse]
    def update_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts = {})
      data, _status_code, _headers = update_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts)
      return data
    end

    # Updates a Bridge object.
    # Updates a Bridge object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param body The Bridge object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeResponse, Fixnum, Hash)>] MsgVpnBridgeResponse data, response status code and response headers
    def update_msg_vpn_bridge_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.update_msg_vpn_bridge ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.update_msg_vpn_bridge" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.update_msg_vpn_bridge" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.update_msg_vpn_bridge" if bridge_virtual_router.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.update_msg_vpn_bridge" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PATCH, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#update_msg_vpn_bridge\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Updates a Remote Message VPN object.
    # Updates a Remote Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_msg_vpn_name The remoteMsgVpnName of the Remote Message VPN.
    # @param remote_msg_vpn_location The remoteMsgVpnLocation of the Remote Message VPN.
    # @param remote_msg_vpn_interface The remoteMsgVpnInterface of the Remote Message VPN.
    # @param body The Remote Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnBridgeRemoteMsgVpnResponse]
    def update_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, opts = {})
      data, _status_code, _headers = update_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, opts)
      return data
    end

    # Updates a Remote Message VPN object.
    # Updates a Remote Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param bridge_name The bridgeName of the Bridge.
    # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
    # @param remote_msg_vpn_name The remoteMsgVpnName of the Remote Message VPN.
    # @param remote_msg_vpn_location The remoteMsgVpnLocation of the Remote Message VPN.
    # @param remote_msg_vpn_interface The remoteMsgVpnInterface of the Remote Message VPN.
    # @param body The Remote Message VPN object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnBridgeRemoteMsgVpnResponse, Fixnum, Hash)>] MsgVpnBridgeRemoteMsgVpnResponse data, response status code and response headers
    def update_msg_vpn_bridge_remote_msg_vpn_with_http_info(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.update_msg_vpn_bridge_remote_msg_vpn ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.update_msg_vpn_bridge_remote_msg_vpn" if msg_vpn_name.nil?
      # verify the required parameter 'bridge_name' is set
      fail ArgumentError, "Missing the required parameter 'bridge_name' when calling MsgVpnApi.update_msg_vpn_bridge_remote_msg_vpn" if bridge_name.nil?
      # verify the required parameter 'bridge_virtual_router' is set
      fail ArgumentError, "Missing the required parameter 'bridge_virtual_router' when calling MsgVpnApi.update_msg_vpn_bridge_remote_msg_vpn" if bridge_virtual_router.nil?
      # verify the required parameter 'remote_msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_name' when calling MsgVpnApi.update_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_name.nil?
      # verify the required parameter 'remote_msg_vpn_location' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_location' when calling MsgVpnApi.update_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_location.nil?
      # verify the required parameter 'remote_msg_vpn_interface' is set
      fail ArgumentError, "Missing the required parameter 'remote_msg_vpn_interface' when calling MsgVpnApi.update_msg_vpn_bridge_remote_msg_vpn" if remote_msg_vpn_interface.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.update_msg_vpn_bridge_remote_msg_vpn" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'bridgeName' + '}', bridge_name.to_s).sub('{' + 'bridgeVirtualRouter' + '}', bridge_virtual_router.to_s).sub('{' + 'remoteMsgVpnName' + '}', remote_msg_vpn_name.to_s).sub('{' + 'remoteMsgVpnLocation' + '}', remote_msg_vpn_location.to_s).sub('{' + 'remoteMsgVpnInterface' + '}', remote_msg_vpn_interface.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PATCH, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnBridgeRemoteMsgVpnResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#update_msg_vpn_bridge_remote_msg_vpn\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Updates a Client Profile object.
    # Updates a Client Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_profile_name The clientProfileName of the Client Profile.
    # @param body The Client Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnClientProfileResponse]
    def update_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, opts = {})
      data, _status_code, _headers = update_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, body, opts)
      return data
    end

    # Updates a Client Profile object.
    # Updates a Client Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_profile_name The clientProfileName of the Client Profile.
    # @param body The Client Profile object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnClientProfileResponse, Fixnum, Hash)>] MsgVpnClientProfileResponse data, response status code and response headers
    def update_msg_vpn_client_profile_with_http_info(msg_vpn_name, client_profile_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.update_msg_vpn_client_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.update_msg_vpn_client_profile" if msg_vpn_name.nil?
      # verify the required parameter 'client_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'client_profile_name' when calling MsgVpnApi.update_msg_vpn_client_profile" if client_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.update_msg_vpn_client_profile" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientProfiles/{clientProfileName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'clientProfileName' + '}', client_profile_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PATCH, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnClientProfileResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#update_msg_vpn_client_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Updates a Client Username object.
    # Updates a Client Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_username The clientUsername of the Client Username.
    # @param body The Client Username object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnClientUsernameResponse]
    def update_msg_vpn_client_username(msg_vpn_name, client_username, body, opts = {})
      data, _status_code, _headers = update_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, body, opts)
      return data
    end

    # Updates a Client Username object.
    # Updates a Client Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param client_username The clientUsername of the Client Username.
    # @param body The Client Username object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnClientUsernameResponse, Fixnum, Hash)>] MsgVpnClientUsernameResponse data, response status code and response headers
    def update_msg_vpn_client_username_with_http_info(msg_vpn_name, client_username, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.update_msg_vpn_client_username ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.update_msg_vpn_client_username" if msg_vpn_name.nil?
      # verify the required parameter 'client_username' is set
      fail ArgumentError, "Missing the required parameter 'client_username' when calling MsgVpnApi.update_msg_vpn_client_username" if client_username.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.update_msg_vpn_client_username" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/clientUsernames/{clientUsername}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'clientUsername' + '}', client_username.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PATCH, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnClientUsernameResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#update_msg_vpn_client_username\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Updates a Queue object.
    # Updates a Queue object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param body The Queue object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnQueueResponse]
    def update_msg_vpn_queue(msg_vpn_name, queue_name, body, opts = {})
      data, _status_code, _headers = update_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, body, opts)
      return data
    end

    # Updates a Queue object.
    # Updates a Queue object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param queue_name The queueName of the Queue.
    # @param body The Queue object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnQueueResponse, Fixnum, Hash)>] MsgVpnQueueResponse data, response status code and response headers
    def update_msg_vpn_queue_with_http_info(msg_vpn_name, queue_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.update_msg_vpn_queue ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.update_msg_vpn_queue" if msg_vpn_name.nil?
      # verify the required parameter 'queue_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_name' when calling MsgVpnApi.update_msg_vpn_queue" if queue_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.update_msg_vpn_queue" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/queues/{queueName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'queueName' + '}', queue_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PATCH, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnQueueResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#update_msg_vpn_queue\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Updates a REST Delivery Point object.
    # Updates a REST Delivery Point object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param body The REST Delivery Point object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointResponse]
    def update_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, opts = {})
      data, _status_code, _headers = update_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, body, opts)
      return data
    end

    # Updates a REST Delivery Point object.
    # Updates a REST Delivery Point object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param body The REST Delivery Point object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointResponse data, response status code and response headers
    def update_msg_vpn_rest_delivery_point_with_http_info(msg_vpn_name, rest_delivery_point_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.update_msg_vpn_rest_delivery_point ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.update_msg_vpn_rest_delivery_point" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.update_msg_vpn_rest_delivery_point" if rest_delivery_point_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.update_msg_vpn_rest_delivery_point" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PATCH, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#update_msg_vpn_rest_delivery_point\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Updates a Queue Binding object.
    # Updates a Queue Binding object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param queue_binding_name The queueBindingName of the Queue Binding.
    # @param body The Queue Binding object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointQueueBindingResponse]
    def update_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, opts = {})
      data, _status_code, _headers = update_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, opts)
      return data
    end

    # Updates a Queue Binding object.
    # Updates a Queue Binding object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param queue_binding_name The queueBindingName of the Queue Binding.
    # @param body The Queue Binding object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointQueueBindingResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointQueueBindingResponse data, response status code and response headers
    def update_msg_vpn_rest_delivery_point_queue_binding_with_http_info(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.update_msg_vpn_rest_delivery_point_queue_binding ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.update_msg_vpn_rest_delivery_point_queue_binding" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.update_msg_vpn_rest_delivery_point_queue_binding" if rest_delivery_point_name.nil?
      # verify the required parameter 'queue_binding_name' is set
      fail ArgumentError, "Missing the required parameter 'queue_binding_name' when calling MsgVpnApi.update_msg_vpn_rest_delivery_point_queue_binding" if queue_binding_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.update_msg_vpn_rest_delivery_point_queue_binding" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'queueBindingName' + '}', queue_binding_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PATCH, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointQueueBindingResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#update_msg_vpn_rest_delivery_point_queue_binding\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # Updates a REST Consumer object.
    # Updates a REST Consumer object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param body The REST Consumer object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [MsgVpnRestDeliveryPointRestConsumerResponse]
    def update_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts = {})
      data, _status_code, _headers = update_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts)
      return data
    end

    # Updates a REST Consumer object.
    # Updates a REST Consumer object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
    # @param msg_vpn_name The msgVpnName of the Message VPN.
    # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
    # @param rest_consumer_name The restConsumerName of the REST Consumer.
    # @param body The REST Consumer object&#39;s attributes.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
    # @return [Array<(MsgVpnRestDeliveryPointRestConsumerResponse, Fixnum, Hash)>] MsgVpnRestDeliveryPointRestConsumerResponse data, response status code and response headers
    def update_msg_vpn_rest_delivery_point_rest_consumer_with_http_info(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug "Calling API: MsgVpnApi.update_msg_vpn_rest_delivery_point_rest_consumer ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling MsgVpnApi.update_msg_vpn_rest_delivery_point_rest_consumer" if msg_vpn_name.nil?
      # verify the required parameter 'rest_delivery_point_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_delivery_point_name' when calling MsgVpnApi.update_msg_vpn_rest_delivery_point_rest_consumer" if rest_delivery_point_name.nil?
      # verify the required parameter 'rest_consumer_name' is set
      fail ArgumentError, "Missing the required parameter 'rest_consumer_name' when calling MsgVpnApi.update_msg_vpn_rest_delivery_point_rest_consumer" if rest_consumer_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling MsgVpnApi.update_msg_vpn_rest_delivery_point_rest_consumer" if body.nil?
      # resource path
      local_var_path = "/msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}".sub('{format}','json').sub('{' + 'msgVpnName' + '}', msg_vpn_name.to_s).sub('{' + 'restDeliveryPointName' + '}', rest_delivery_point_name.to_s).sub('{' + 'restConsumerName' + '}', rest_consumer_name.to_s)

      # query parameters
      query_params = {}
      query_params[:'select'] = @api_client.build_collection_param(opts[:'select'], :multi) if !opts[:'select'].nil?

      # header parameters
      header_params = {}

      # HTTP header 'Accept' (if needed)
      local_header_accept = ['application/json']
      local_header_accept_result = @api_client.select_header_accept(local_header_accept) and header_params['Accept'] = local_header_accept_result

      # HTTP header 'Content-Type'
      local_header_content_type = ['application/json']
      header_params['Content-Type'] = @api_client.select_header_content_type(local_header_content_type)

      # form parameters
      form_params = {}

      # http body (model)
      post_body = @api_client.object_to_http_body(body)
      auth_names = ['basicAuth']
      data, status_code, headers = @api_client.call_api(:PATCH, local_var_path,
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => 'MsgVpnRestDeliveryPointRestConsumerResponse')
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: MsgVpnApi#update_msg_vpn_rest_delivery_point_rest_consumer\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end
  end
end
