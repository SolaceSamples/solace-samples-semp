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

require 'spec_helper'
require 'json'

# Unit tests for SempClient::MsgVpnApi
# Automatically generated by swagger-codegen (github.com/swagger-api/swagger-codegen)
# Please update as you see appropriate
describe 'MsgVpnApi' do
  before do
    # run before each test
    @instance = SempClient::MsgVpnApi.new
  end

  after do
    # run after each test
  end

  describe 'test an instance of MsgVpnApi' do
    it 'should create an instact of MsgVpnApi' do
      expect(@instance).to be_instance_of(SempClient::MsgVpnApi)
    end
  end

  # unit tests for create_msg_vpn
  # Creates a Message VPN object.
  # Creates a Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword||||x| replicationEnabledQueueBehavior||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param body The Message VPN object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnResponse]
  describe 'create_msg_vpn test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_acl_profile
  # Creates an ACL Profile object.
  # Creates an ACL Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param body The ACL Profile object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfileResponse]
  describe 'create_msg_vpn_acl_profile test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_acl_profile_client_connect_exception
  # Creates a Client Connect Exception object.
  # Creates a Client Connect Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| clientConnectExceptionAddress|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param body The Client Connect Exception object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfileClientConnectExceptionResponse]
  describe 'create_msg_vpn_acl_profile_client_connect_exception test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_acl_profile_publish_exception
  # Creates a Publish Topic Exception object.
  # Creates a Publish Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| publishExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param body The Publish Topic Exception object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfilePublishExceptionResponse]
  describe 'create_msg_vpn_acl_profile_publish_exception test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_acl_profile_subscribe_exception
  # Creates a Subscribe Topic Exception object.
  # Creates a Subscribe Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| subscribeExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param body The Subscribe Topic Exception object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfileSubscribeExceptionResponse]
  describe 'create_msg_vpn_acl_profile_subscribe_exception test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_authorization_group
  # Creates a LDAP Authorization Group object.
  # Creates a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authorizationGroupName|x|x||| msgVpnName|x||x|| orderAfterAuthorizationGroupName||||x| orderBeforeAuthorizationGroupName||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param body The LDAP Authorization Group object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAuthorizationGroupResponse]
  describe 'create_msg_vpn_authorization_group test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_bridge
  # Creates a Bridge object.
  # Creates a Bridge object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| msgVpnName|x||x|| remoteAuthenticationBasicPassword||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param body The Bridge object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgeResponse]
  describe 'create_msg_vpn_bridge test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_bridge_remote_msg_vpn
  # Creates a Remote Message VPN object.
  # Creates a Remote Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| password||||x| remoteMsgVpnInterface|x|||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param body The Remote Message VPN object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgeRemoteMsgVpnResponse]
  describe 'create_msg_vpn_bridge_remote_msg_vpn test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_bridge_remote_subscription
  # Creates a Remote Subscription object.
  # Creates a Remote Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| deliverAlwaysEnabled||x||| msgVpnName|x||x|| remoteSubscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param body The Remote Subscription object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgeRemoteSubscriptionResponse]
  describe 'create_msg_vpn_bridge_remote_subscription test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_bridge_tls_trusted_common_name
  # Creates a Trusted Common Name object.
  # Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param body The Trusted Common Name object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgeTlsTrustedCommonNameResponse]
  describe 'create_msg_vpn_bridge_tls_trusted_common_name test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_client_profile
  # Creates a Client Profile object.
  # Creates a Client Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param body The Client Profile object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnClientProfileResponse]
  describe 'create_msg_vpn_client_profile test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_client_username
  # Creates a Client Username object.
  # Creates a Client Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientUsername|x|x||| msgVpnName|x||x|| password||||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param body The Client Username object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnClientUsernameResponse]
  describe 'create_msg_vpn_client_username test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_queue
  # Creates a Queue object.
  # Creates a Queue object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param body The Queue object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnQueueResponse]
  describe 'create_msg_vpn_queue test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_queue_subscription
  # Creates a Queue Subscription object.
  # Creates a Queue Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param queue_name The queueName of the Queue.
  # @param body The Queue Subscription object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnQueueSubscriptionResponse]
  describe 'create_msg_vpn_queue_subscription test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_rest_delivery_point
  # Creates a REST Delivery Point object.
  # Creates a REST Delivery Point object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param body The REST Delivery Point object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointResponse]
  describe 'create_msg_vpn_rest_delivery_point test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_rest_delivery_point_queue_binding
  # Creates a Queue Binding object.
  # Creates a Queue Binding object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueBindingName|x|x||| restDeliveryPointName|x||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param body The Queue Binding object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointQueueBindingResponse]
  describe 'create_msg_vpn_rest_delivery_point_queue_binding test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_rest_delivery_point_rest_consumer
  # Creates a REST Consumer object.
  # Creates a REST Consumer object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword||||x| msgVpnName|x||x|| restConsumerName|x|x||| restDeliveryPointName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param body The REST Consumer object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointRestConsumerResponse]
  describe 'create_msg_vpn_rest_delivery_point_rest_consumer test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name
  # Creates a Trusted Common Name object.
  # Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restConsumerName|x||x|| restDeliveryPointName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param rest_consumer_name The restConsumerName of the REST Consumer.
  # @param body The Trusted Common Name object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse]
  describe 'create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for create_msg_vpn_sequenced_topic
  # Creates a Sequenced Topic object.
  # Creates a Sequenced Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| sequencedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param body The Sequenced Topic object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnSequencedTopicResponse]
  describe 'create_msg_vpn_sequenced_topic test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn
  # Deletes a Message VPN object.
  # Deletes a Message VPN object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_acl_profile
  # Deletes an ACL Profile object.
  # Deletes an ACL Profile object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_acl_profile test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_acl_profile_client_connect_exception
  # Deletes a Client Connect Exception object.
  # Deletes a Client Connect Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param client_connect_exception_address The clientConnectExceptionAddress of the Client Connect Exception.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_acl_profile_client_connect_exception test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_acl_profile_publish_exception
  # Deletes a Publish Topic Exception object.
  # Deletes a Publish Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param topic_syntax The topicSyntax of the Publish Topic Exception.
  # @param publish_exception_topic The publishExceptionTopic of the Publish Topic Exception.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_acl_profile_publish_exception test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_acl_profile_subscribe_exception
  # Deletes a Subscribe Topic Exception object.
  # Deletes a Subscribe Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param topic_syntax The topicSyntax of the Subscribe Topic Exception.
  # @param subscribe_exception_topic The subscribeExceptionTopic of the Subscribe Topic Exception.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_acl_profile_subscribe_exception test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_authorization_group
  # Deletes a LDAP Authorization Group object.
  # Deletes a LDAP Authorization Group object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_authorization_group test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_bridge
  # Deletes a Bridge object.
  # Deletes a Bridge object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_bridge test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_bridge_remote_msg_vpn
  # Deletes a Remote Message VPN object.
  # Deletes a Remote Message VPN object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param remote_msg_vpn_name The remoteMsgVpnName of the Remote Message VPN.
  # @param remote_msg_vpn_location The remoteMsgVpnLocation of the Remote Message VPN.
  # @param remote_msg_vpn_interface The remoteMsgVpnInterface of the Remote Message VPN.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_bridge_remote_msg_vpn test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_bridge_remote_subscription
  # Deletes a Remote Subscription object.
  # Deletes a Remote Subscription object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param remote_subscription_topic The remoteSubscriptionTopic of the Remote Subscription.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_bridge_remote_subscription test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_bridge_tls_trusted_common_name
  # Deletes a Trusted Common Name object.
  # Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_bridge_tls_trusted_common_name test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_client_profile
  # Deletes a Client Profile object.
  # Deletes a Client Profile object.  A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param client_profile_name The clientProfileName of the Client Profile.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_client_profile test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_client_username
  # Deletes a Client Username object.
  # Deletes a Client Username object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param client_username The clientUsername of the Client Username.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_client_username test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_queue
  # Deletes a Queue object.
  # Deletes a Queue object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param queue_name The queueName of the Queue.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_queue test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_queue_subscription
  # Deletes a Queue Subscription object.
  # Deletes a Queue Subscription object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param queue_name The queueName of the Queue.
  # @param subscription_topic The subscriptionTopic of the Queue Subscription.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_queue_subscription test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_rest_delivery_point
  # Deletes a REST Delivery Point object.
  # Deletes a REST Delivery Point object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_rest_delivery_point test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_rest_delivery_point_queue_binding
  # Deletes a Queue Binding object.
  # Deletes a Queue Binding object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param queue_binding_name The queueBindingName of the Queue Binding.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_rest_delivery_point_queue_binding test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_rest_delivery_point_rest_consumer
  # Deletes a REST Consumer object.
  # Deletes a REST Consumer object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param rest_consumer_name The restConsumerName of the REST Consumer.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_rest_delivery_point_rest_consumer test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name
  # Deletes a Trusted Common Name object.
  # Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param rest_consumer_name The restConsumerName of the REST Consumer.
  # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for delete_msg_vpn_sequenced_topic
  # Deletes a Sequenced Topic object.
  # Deletes a Sequenced Topic object.  A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param sequenced_topic The sequencedTopic of the Sequenced Topic.
  # @param [Hash] opts the optional parameters
  # @return [SempMetaOnlyResponse]
  describe 'delete_msg_vpn_sequenced_topic test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn
  # Gets a Message VPN object.
  # Gets a Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnResponse]
  describe 'get_msg_vpn test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_acl_profile
  # Gets an ACL Profile object.
  # Gets an ACL Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfileResponse]
  describe 'get_msg_vpn_acl_profile test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_acl_profile_client_connect_exception
  # Gets a Client Connect Exception object.
  # Gets a Client Connect Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param client_connect_exception_address The clientConnectExceptionAddress of the Client Connect Exception.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfileClientConnectExceptionResponse]
  describe 'get_msg_vpn_acl_profile_client_connect_exception test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_acl_profile_client_connect_exceptions
  # Gets a list of Client Connect Exception objects.
  # Gets a list of Client Connect Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfileClientConnectExceptionsResponse]
  describe 'get_msg_vpn_acl_profile_client_connect_exceptions test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_acl_profile_publish_exception
  # Gets a Publish Topic Exception object.
  # Gets a Publish Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param topic_syntax The topicSyntax of the Publish Topic Exception.
  # @param publish_exception_topic The publishExceptionTopic of the Publish Topic Exception.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfilePublishExceptionResponse]
  describe 'get_msg_vpn_acl_profile_publish_exception test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_acl_profile_publish_exceptions
  # Gets a list of Publish Topic Exception objects.
  # Gets a list of Publish Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfilePublishExceptionsResponse]
  describe 'get_msg_vpn_acl_profile_publish_exceptions test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_acl_profile_subscribe_exception
  # Gets a Subscribe Topic Exception object.
  # Gets a Subscribe Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param topic_syntax The topicSyntax of the Subscribe Topic Exception.
  # @param subscribe_exception_topic The subscribeExceptionTopic of the Subscribe Topic Exception.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfileSubscribeExceptionResponse]
  describe 'get_msg_vpn_acl_profile_subscribe_exception test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_acl_profile_subscribe_exceptions
  # Gets a list of Subscribe Topic Exception objects.
  # Gets a list of Subscribe Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfileSubscribeExceptionsResponse]
  describe 'get_msg_vpn_acl_profile_subscribe_exceptions test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_acl_profiles
  # Gets a list of ACL Profile objects.
  # Gets a list of ACL Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfilesResponse]
  describe 'get_msg_vpn_acl_profiles test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_authorization_group
  # Gets a LDAP Authorization Group object.
  # Gets a LDAP Authorization Group object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAuthorizationGroupResponse]
  describe 'get_msg_vpn_authorization_group test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_authorization_groups
  # Gets a list of LDAP Authorization Group objects.
  # Gets a list of LDAP Authorization Group objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAuthorizationGroupsResponse]
  describe 'get_msg_vpn_authorization_groups test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_bridge
  # Gets a Bridge object.
  # Gets a Bridge object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgeResponse]
  describe 'get_msg_vpn_bridge test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_bridge_remote_msg_vpn
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
  # @return [MsgVpnBridgeRemoteMsgVpnResponse]
  describe 'get_msg_vpn_bridge_remote_msg_vpn test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_bridge_remote_msg_vpns
  # Gets a list of Remote Message VPN objects.
  # Gets a list of Remote Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgeRemoteMsgVpnsResponse]
  describe 'get_msg_vpn_bridge_remote_msg_vpns test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_bridge_remote_subscription
  # Gets a Remote Subscription object.
  # Gets a Remote Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param remote_subscription_topic The remoteSubscriptionTopic of the Remote Subscription.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgeRemoteSubscriptionResponse]
  describe 'get_msg_vpn_bridge_remote_subscription test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_bridge_remote_subscriptions
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
  # @return [MsgVpnBridgeRemoteSubscriptionsResponse]
  describe 'get_msg_vpn_bridge_remote_subscriptions test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_bridge_tls_trusted_common_name
  # Gets a Trusted Common Name object.
  # Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgeTlsTrustedCommonNameResponse]
  describe 'get_msg_vpn_bridge_tls_trusted_common_name test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_bridge_tls_trusted_common_names
  # Gets a list of Trusted Common Name objects.
  # Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgeTlsTrustedCommonNamesResponse]
  describe 'get_msg_vpn_bridge_tls_trusted_common_names test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_bridges
  # Gets a list of Bridge objects.
  # Gets a list of Bridge objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgesResponse]
  describe 'get_msg_vpn_bridges test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_client_profile
  # Gets a Client Profile object.
  # Gets a Client Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param client_profile_name The clientProfileName of the Client Profile.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnClientProfileResponse]
  describe 'get_msg_vpn_client_profile test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_client_profiles
  # Gets a list of Client Profile objects.
  # Gets a list of Client Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnClientProfilesResponse]
  describe 'get_msg_vpn_client_profiles test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_client_username
  # Gets a Client Username object.
  # Gets a Client Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param client_username The clientUsername of the Client Username.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnClientUsernameResponse]
  describe 'get_msg_vpn_client_username test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_client_usernames
  # Gets a list of Client Username objects.
  # Gets a list of Client Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnClientUsernamesResponse]
  describe 'get_msg_vpn_client_usernames test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_queue
  # Gets a Queue object.
  # Gets a Queue object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param queue_name The queueName of the Queue.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnQueueResponse]
  describe 'get_msg_vpn_queue test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_queue_subscription
  # Gets a Queue Subscription object.
  # Gets a Queue Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param queue_name The queueName of the Queue.
  # @param subscription_topic The subscriptionTopic of the Queue Subscription.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnQueueSubscriptionResponse]
  describe 'get_msg_vpn_queue_subscription test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_queue_subscriptions
  # Gets a list of Queue Subscription objects.
  # Gets a list of Queue Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param queue_name The queueName of the Queue.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnQueueSubscriptionsResponse]
  describe 'get_msg_vpn_queue_subscriptions test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_queues
  # Gets a list of Queue objects.
  # Gets a list of Queue objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnQueuesResponse]
  describe 'get_msg_vpn_queues test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_rest_delivery_point
  # Gets a REST Delivery Point object.
  # Gets a REST Delivery Point object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointResponse]
  describe 'get_msg_vpn_rest_delivery_point test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_rest_delivery_point_queue_binding
  # Gets a Queue Binding object.
  # Gets a Queue Binding object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param queue_binding_name The queueBindingName of the Queue Binding.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointQueueBindingResponse]
  describe 'get_msg_vpn_rest_delivery_point_queue_binding test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_rest_delivery_point_queue_bindings
  # Gets a list of Queue Binding objects.
  # Gets a list of Queue Binding objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointQueueBindingsResponse]
  describe 'get_msg_vpn_rest_delivery_point_queue_bindings test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_rest_delivery_point_rest_consumer
  # Gets a REST Consumer object.
  # Gets a REST Consumer object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param rest_consumer_name The restConsumerName of the REST Consumer.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointRestConsumerResponse]
  describe 'get_msg_vpn_rest_delivery_point_rest_consumer test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name
  # Gets a Trusted Common Name object.
  # Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param rest_consumer_name The restConsumerName of the REST Consumer.
  # @param tls_trusted_common_name The tlsTrustedCommonName of the Trusted Common Name.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse]
  describe 'get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names
  # Gets a list of Trusted Common Name objects.
  # Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param rest_consumer_name The restConsumerName of the REST Consumer.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse]
  describe 'get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_rest_delivery_point_rest_consumers
  # Gets a list of REST Consumer objects.
  # Gets a list of REST Consumer objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointRestConsumersResponse]
  describe 'get_msg_vpn_rest_delivery_point_rest_consumers test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_rest_delivery_points
  # Gets a list of REST Delivery Point objects.
  # Gets a list of REST Delivery Point objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointsResponse]
  describe 'get_msg_vpn_rest_delivery_points test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_sequenced_topic
  # Gets a Sequenced Topic object.
  # Gets a Sequenced Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param sequenced_topic The sequencedTopic of the Sequenced Topic.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnSequencedTopicResponse]
  describe 'get_msg_vpn_sequenced_topic test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpn_sequenced_topics
  # Gets a list of Sequenced Topic objects.
  # Gets a list of Sequenced Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnSequencedTopicsResponse]
  describe 'get_msg_vpn_sequenced_topics test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for get_msg_vpns
  # Gets a list of Message VPN objects.
  # Gets a list of Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readonly\&quot; is required to perform this operation.
  # @param [Hash] opts the optional parameters
  # @option opts [Integer] :count Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;).
  # @option opts [String] :cursor The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;).
  # @option opts [Array<String>] :where Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;).
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnsResponse]
  describe 'get_msg_vpns test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for replace_msg_vpn
  # Replaces a Message VPN object.
  # Replaces a Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param body The Message VPN object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnResponse]
  describe 'replace_msg_vpn test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for replace_msg_vpn_acl_profile
  # Replaces an ACL Profile object.
  # Replaces an ACL Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param body The ACL Profile object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfileResponse]
  describe 'replace_msg_vpn_acl_profile test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for replace_msg_vpn_authorization_group
  # Replaces a LDAP Authorization Group object.
  # Replaces a LDAP Authorization Group object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
  # @param body The LDAP Authorization Group object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAuthorizationGroupResponse]
  describe 'replace_msg_vpn_authorization_group test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for replace_msg_vpn_bridge
  # Replaces a Bridge object.
  # Replaces a Bridge object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param body The Bridge object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgeResponse]
  describe 'replace_msg_vpn_bridge test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for replace_msg_vpn_bridge_remote_msg_vpn
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
  # @return [MsgVpnBridgeRemoteMsgVpnResponse]
  describe 'replace_msg_vpn_bridge_remote_msg_vpn test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for replace_msg_vpn_client_profile
  # Replaces a Client Profile object.
  # Replaces a Client Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param client_profile_name The clientProfileName of the Client Profile.
  # @param body The Client Profile object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnClientProfileResponse]
  describe 'replace_msg_vpn_client_profile test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for replace_msg_vpn_client_username
  # Replaces a Client Username object.
  # Replaces a Client Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param client_username The clientUsername of the Client Username.
  # @param body The Client Username object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnClientUsernameResponse]
  describe 'replace_msg_vpn_client_username test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for replace_msg_vpn_queue
  # Replaces a Queue object.
  # Replaces a Queue object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param queue_name The queueName of the Queue.
  # @param body The Queue object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnQueueResponse]
  describe 'replace_msg_vpn_queue test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for replace_msg_vpn_rest_delivery_point
  # Replaces a REST Delivery Point object.
  # Replaces a REST Delivery Point object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param body The REST Delivery Point object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointResponse]
  describe 'replace_msg_vpn_rest_delivery_point test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for replace_msg_vpn_rest_delivery_point_queue_binding
  # Replaces a Queue Binding object.
  # Replaces a Queue Binding object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param queue_binding_name The queueBindingName of the Queue Binding.
  # @param body The Queue Binding object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointQueueBindingResponse]
  describe 'replace_msg_vpn_rest_delivery_point_queue_binding test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for replace_msg_vpn_rest_delivery_point_rest_consumer
  # Replaces a REST Consumer object.
  # Replaces a REST Consumer object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param rest_consumer_name The restConsumerName of the REST Consumer.
  # @param body The REST Consumer object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointRestConsumerResponse]
  describe 'replace_msg_vpn_rest_delivery_point_rest_consumer test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for update_msg_vpn
  # Updates a Message VPN object.
  # Updates a Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite  
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param body The Message VPN object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnResponse]
  describe 'update_msg_vpn test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for update_msg_vpn_acl_profile
  # Updates an ACL Profile object.
  # Updates an ACL Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param acl_profile_name The aclProfileName of the ACL Profile.
  # @param body The ACL Profile object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAclProfileResponse]
  describe 'update_msg_vpn_acl_profile test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for update_msg_vpn_authorization_group
  # Updates a LDAP Authorization Group object.
  # Updates a LDAP Authorization Group object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param authorization_group_name The authorizationGroupName of the LDAP Authorization Group.
  # @param body The LDAP Authorization Group object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnAuthorizationGroupResponse]
  describe 'update_msg_vpn_authorization_group test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for update_msg_vpn_bridge
  # Updates a Bridge object.
  # Updates a Bridge object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite  
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param bridge_name The bridgeName of the Bridge.
  # @param bridge_virtual_router The bridgeVirtualRouter of the Bridge.
  # @param body The Bridge object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnBridgeResponse]
  describe 'update_msg_vpn_bridge test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for update_msg_vpn_bridge_remote_msg_vpn
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
  # @return [MsgVpnBridgeRemoteMsgVpnResponse]
  describe 'update_msg_vpn_bridge_remote_msg_vpn test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for update_msg_vpn_client_profile
  # Updates a Client Profile object.
  # Updates a Client Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \&quot;global/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param client_profile_name The clientProfileName of the Client Profile.
  # @param body The Client Profile object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnClientProfileResponse]
  describe 'update_msg_vpn_client_profile test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for update_msg_vpn_client_username
  # Updates a Client Username object.
  # Updates a Client Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param client_username The clientUsername of the Client Username.
  # @param body The Client Username object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnClientUsernameResponse]
  describe 'update_msg_vpn_client_username test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for update_msg_vpn_queue
  # Updates a Queue object.
  # Updates a Queue object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param queue_name The queueName of the Queue.
  # @param body The Queue object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnQueueResponse]
  describe 'update_msg_vpn_queue test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for update_msg_vpn_rest_delivery_point
  # Updates a REST Delivery Point object.
  # Updates a REST Delivery Point object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param body The REST Delivery Point object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointResponse]
  describe 'update_msg_vpn_rest_delivery_point test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for update_msg_vpn_rest_delivery_point_queue_binding
  # Updates a Queue Binding object.
  # Updates a Queue Binding object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param queue_binding_name The queueBindingName of the Queue Binding.
  # @param body The Queue Binding object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointQueueBindingResponse]
  describe 'update_msg_vpn_rest_delivery_point_queue_binding test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

  # unit tests for update_msg_vpn_rest_delivery_point_rest_consumer
  # Updates a REST Consumer object.
  # Updates a REST Consumer object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \&quot;vpn/readwrite\&quot; is required to perform this operation.
  # @param msg_vpn_name The msgVpnName of the Message VPN.
  # @param rest_delivery_point_name The restDeliveryPointName of the REST Delivery Point.
  # @param rest_consumer_name The restConsumerName of the REST Consumer.
  # @param body The REST Consumer object&#39;s attributes.
  # @param [Hash] opts the optional parameters
  # @option opts [Array<String>] :select Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;).
  # @return [MsgVpnRestDeliveryPointRestConsumerResponse]
  describe 'update_msg_vpn_rest_delivery_point_rest_consumer test' do
    it "should work" do
      # assertion here. ref: https://www.relishapp.com/rspec/rspec-expectations/docs/built-in-matchers
    end
  end

end
