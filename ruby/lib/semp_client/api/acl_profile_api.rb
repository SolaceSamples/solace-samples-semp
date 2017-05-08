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
  class AclProfileApi
    attr_accessor :api_client

    def initialize(api_client = ApiClient.default)
      @api_client = api_client
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.create_msg_vpn_acl_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.create_msg_vpn_acl_profile" if msg_vpn_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling AclProfileApi.create_msg_vpn_acl_profile" if body.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#create_msg_vpn_acl_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.create_msg_vpn_acl_profile_client_connect_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.create_msg_vpn_acl_profile_client_connect_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.create_msg_vpn_acl_profile_client_connect_exception" if acl_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling AclProfileApi.create_msg_vpn_acl_profile_client_connect_exception" if body.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#create_msg_vpn_acl_profile_client_connect_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.create_msg_vpn_acl_profile_publish_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.create_msg_vpn_acl_profile_publish_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.create_msg_vpn_acl_profile_publish_exception" if acl_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling AclProfileApi.create_msg_vpn_acl_profile_publish_exception" if body.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#create_msg_vpn_acl_profile_publish_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.create_msg_vpn_acl_profile_subscribe_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.create_msg_vpn_acl_profile_subscribe_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.create_msg_vpn_acl_profile_subscribe_exception" if acl_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling AclProfileApi.create_msg_vpn_acl_profile_subscribe_exception" if body.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#create_msg_vpn_acl_profile_subscribe_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.delete_msg_vpn_acl_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.delete_msg_vpn_acl_profile" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.delete_msg_vpn_acl_profile" if acl_profile_name.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#delete_msg_vpn_acl_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.delete_msg_vpn_acl_profile_client_connect_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.delete_msg_vpn_acl_profile_client_connect_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.delete_msg_vpn_acl_profile_client_connect_exception" if acl_profile_name.nil?
      # verify the required parameter 'client_connect_exception_address' is set
      fail ArgumentError, "Missing the required parameter 'client_connect_exception_address' when calling AclProfileApi.delete_msg_vpn_acl_profile_client_connect_exception" if client_connect_exception_address.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#delete_msg_vpn_acl_profile_client_connect_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.delete_msg_vpn_acl_profile_publish_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.delete_msg_vpn_acl_profile_publish_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.delete_msg_vpn_acl_profile_publish_exception" if acl_profile_name.nil?
      # verify the required parameter 'topic_syntax' is set
      fail ArgumentError, "Missing the required parameter 'topic_syntax' when calling AclProfileApi.delete_msg_vpn_acl_profile_publish_exception" if topic_syntax.nil?
      # verify the required parameter 'publish_exception_topic' is set
      fail ArgumentError, "Missing the required parameter 'publish_exception_topic' when calling AclProfileApi.delete_msg_vpn_acl_profile_publish_exception" if publish_exception_topic.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#delete_msg_vpn_acl_profile_publish_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.delete_msg_vpn_acl_profile_subscribe_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.delete_msg_vpn_acl_profile_subscribe_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.delete_msg_vpn_acl_profile_subscribe_exception" if acl_profile_name.nil?
      # verify the required parameter 'topic_syntax' is set
      fail ArgumentError, "Missing the required parameter 'topic_syntax' when calling AclProfileApi.delete_msg_vpn_acl_profile_subscribe_exception" if topic_syntax.nil?
      # verify the required parameter 'subscribe_exception_topic' is set
      fail ArgumentError, "Missing the required parameter 'subscribe_exception_topic' when calling AclProfileApi.delete_msg_vpn_acl_profile_subscribe_exception" if subscribe_exception_topic.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#delete_msg_vpn_acl_profile_subscribe_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.get_msg_vpn_acl_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.get_msg_vpn_acl_profile" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.get_msg_vpn_acl_profile" if acl_profile_name.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#get_msg_vpn_acl_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.get_msg_vpn_acl_profile_client_connect_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.get_msg_vpn_acl_profile_client_connect_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.get_msg_vpn_acl_profile_client_connect_exception" if acl_profile_name.nil?
      # verify the required parameter 'client_connect_exception_address' is set
      fail ArgumentError, "Missing the required parameter 'client_connect_exception_address' when calling AclProfileApi.get_msg_vpn_acl_profile_client_connect_exception" if client_connect_exception_address.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#get_msg_vpn_acl_profile_client_connect_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.get_msg_vpn_acl_profile_client_connect_exceptions ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.get_msg_vpn_acl_profile_client_connect_exceptions" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.get_msg_vpn_acl_profile_client_connect_exceptions" if acl_profile_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling AclProfileApi.get_msg_vpn_acl_profile_client_connect_exceptions, must be greater than or equal to 1.0.'
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
        @api_client.config.logger.debug "API called: AclProfileApi#get_msg_vpn_acl_profile_client_connect_exceptions\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.get_msg_vpn_acl_profile_publish_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.get_msg_vpn_acl_profile_publish_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.get_msg_vpn_acl_profile_publish_exception" if acl_profile_name.nil?
      # verify the required parameter 'topic_syntax' is set
      fail ArgumentError, "Missing the required parameter 'topic_syntax' when calling AclProfileApi.get_msg_vpn_acl_profile_publish_exception" if topic_syntax.nil?
      # verify the required parameter 'publish_exception_topic' is set
      fail ArgumentError, "Missing the required parameter 'publish_exception_topic' when calling AclProfileApi.get_msg_vpn_acl_profile_publish_exception" if publish_exception_topic.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#get_msg_vpn_acl_profile_publish_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.get_msg_vpn_acl_profile_publish_exceptions ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.get_msg_vpn_acl_profile_publish_exceptions" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.get_msg_vpn_acl_profile_publish_exceptions" if acl_profile_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling AclProfileApi.get_msg_vpn_acl_profile_publish_exceptions, must be greater than or equal to 1.0.'
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
        @api_client.config.logger.debug "API called: AclProfileApi#get_msg_vpn_acl_profile_publish_exceptions\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.get_msg_vpn_acl_profile_subscribe_exception ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.get_msg_vpn_acl_profile_subscribe_exception" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.get_msg_vpn_acl_profile_subscribe_exception" if acl_profile_name.nil?
      # verify the required parameter 'topic_syntax' is set
      fail ArgumentError, "Missing the required parameter 'topic_syntax' when calling AclProfileApi.get_msg_vpn_acl_profile_subscribe_exception" if topic_syntax.nil?
      # verify the required parameter 'subscribe_exception_topic' is set
      fail ArgumentError, "Missing the required parameter 'subscribe_exception_topic' when calling AclProfileApi.get_msg_vpn_acl_profile_subscribe_exception" if subscribe_exception_topic.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#get_msg_vpn_acl_profile_subscribe_exception\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.get_msg_vpn_acl_profile_subscribe_exceptions ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.get_msg_vpn_acl_profile_subscribe_exceptions" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.get_msg_vpn_acl_profile_subscribe_exceptions" if acl_profile_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling AclProfileApi.get_msg_vpn_acl_profile_subscribe_exceptions, must be greater than or equal to 1.0.'
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
        @api_client.config.logger.debug "API called: AclProfileApi#get_msg_vpn_acl_profile_subscribe_exceptions\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.get_msg_vpn_acl_profiles ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.get_msg_vpn_acl_profiles" if msg_vpn_name.nil?
      if !opts[:'count'].nil? && opts[:'count'] < 1.0
        fail ArgumentError, 'invalid value for "opts[:"count"]" when calling AclProfileApi.get_msg_vpn_acl_profiles, must be greater than or equal to 1.0.'
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
        @api_client.config.logger.debug "API called: AclProfileApi#get_msg_vpn_acl_profiles\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.replace_msg_vpn_acl_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.replace_msg_vpn_acl_profile" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.replace_msg_vpn_acl_profile" if acl_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling AclProfileApi.replace_msg_vpn_acl_profile" if body.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#replace_msg_vpn_acl_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
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
        @api_client.config.logger.debug "Calling API: AclProfileApi.update_msg_vpn_acl_profile ..."
      end
      # verify the required parameter 'msg_vpn_name' is set
      fail ArgumentError, "Missing the required parameter 'msg_vpn_name' when calling AclProfileApi.update_msg_vpn_acl_profile" if msg_vpn_name.nil?
      # verify the required parameter 'acl_profile_name' is set
      fail ArgumentError, "Missing the required parameter 'acl_profile_name' when calling AclProfileApi.update_msg_vpn_acl_profile" if acl_profile_name.nil?
      # verify the required parameter 'body' is set
      fail ArgumentError, "Missing the required parameter 'body' when calling AclProfileApi.update_msg_vpn_acl_profile" if body.nil?
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
        @api_client.config.logger.debug "API called: AclProfileApi#update_msg_vpn_acl_profile\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end
  end
end
