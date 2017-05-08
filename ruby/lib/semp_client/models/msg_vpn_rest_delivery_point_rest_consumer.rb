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

require 'date'

module SempClient

  class MsgVpnRestDeliveryPointRestConsumer
    # The password that the REST Consumer will use to login to the rest-host. The default is to have no `authenticationHttpBasicPassword`.
    attr_accessor :authentication_http_basic_password

    # The username that the REST Consumer will use to login to the rest-host. Normally a username is only configured when basic authentication is selected for the REST Consumer. The default is to have no `authenticationHttpBasicUsername`.
    attr_accessor :authentication_http_basic_username

    # The authentication scheme used by the REST Consumer to login to the rest-host. The client-certificate auth-scheme is only compatible with connections using TLS. The default value is `\"none\"`. The allowed values and their meaning are:      \"none\" - Login with no authentication (login anonymously). This means no authentication is provided to the REST consumer when connecting to it. This is useful for anonymous connections or when no authentication is required by the REST consumer.     \"http-basic\" - Login with simple username / password credentials according to HTTP Basic authentication as per RFC2616.     \"client-certificate\" - Login with client TLS certificate as per RFC5246. 
    attr_accessor :authentication_scheme

    # Enable or disable this REST Consumer. When disabled, no connections are initiated or messages delivered to this particular REST Consumer. The default value is `false`.
    attr_accessor :enabled

    # The interface that will be used for all outgoing connections associated with the given REST consumer. The source IP address used for these connections will always be the IP address associated with the AD-enabled virtual-router for the specified interface. When unspecified the router will automatically choose an interface through which the REST consumer is reachable. The default is to have no `localInterface`.
    attr_accessor :local_interface

    # The maximum amount of time (in seconds) that the router will wait for a POST response from the REST Consumer. Once a POST operation has been outstanding for this period of time, the request is considered hung and the TCP connection is reset. If this POST is for a non-persistent message, the message is discarded. If this POST is for a persistent message, then message delivery is re-attempted via another available outgoing connection on any available outgoing connection for that RDP, up to the Max-Delivery-Count on the queue. If this count is exceeded, and the message is DMQ-eligible, then this message is moved to the DMQ, otherwise it is discarded. The default value is `30`.
    attr_accessor :max_post_wait_time

    # The name of the Message VPN.
    attr_accessor :msg_vpn_name

    # The total number of concurrent TCP connections open to this REST Consumer initiated by the router. Multiple connections to a single REST Consumer are typically desirable to increase throughput via concurrency. The more connections, the higher the potential throughput. The default value is `3`.
    attr_accessor :outgoing_connection_count

    # The IPv4 address or DNS name to which the router is to connect to deliver messages for this REST Consumer. If the REST Consumer is enabled while the host value is not configured then the REST Consumer has an operational Down state due to the empty host configuration until a usable host value is configured. The default is to have no `remoteHost`.
    attr_accessor :remote_host

    # The port associated with the host of the current REST Consumer. The default value is `8080`.
    attr_accessor :remote_port

    # An RDP-wide unique name for the REST consumer.
    attr_accessor :rest_consumer_name

    # A Message VPN-wide unique name for the REST Delivery Point. This name is used to auto-generate a client-username in this Message VPN, which is used by the client for this RDP.
    attr_accessor :rest_delivery_point_name

    # The number of seconds that must pass before retrying a connection. The default value is `3`.
    attr_accessor :retry_delay

    # The colon-separated list of cipher-suites the REST Consumer uses in its encrypted connection. All supported suites are included by default, from most-secure to least-secure. The remote server (REST Consumer) should choose the first suite from this list that it supports. The cipher-suite list can only be changed when the REST Consumer is disabled. The default value is `\"ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:AES256-GCM-SHA384:AES256-SHA256:AES256-SHA:ECDHE-RSA-DES-CBC3-SHA:DES-CBC3-SHA:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:AES128-GCM-SHA256:AES128-SHA256:AES128-SHA\"`.
    attr_accessor :tls_cipher_suite_list

    # Enable or disable TLS for the REST Consumer. This may only be done when the REST Consumer is disabled. The default value is `false`.
    attr_accessor :tls_enabled

    class EnumAttributeValidator
      attr_reader :datatype
      attr_reader :allowable_values

      def initialize(datatype, allowable_values)
        @allowable_values = allowable_values.map do |value|
          case datatype.to_s
          when /Integer/i
            value.to_i
          when /Float/i
            value.to_f
          else
            value
          end
        end
      end

      def valid?(value)
        !value || allowable_values.include?(value)
      end
    end

    # Attribute mapping from ruby-style variable name to JSON key.
    def self.attribute_map
      {
        :'authentication_http_basic_password' => :'authenticationHttpBasicPassword',
        :'authentication_http_basic_username' => :'authenticationHttpBasicUsername',
        :'authentication_scheme' => :'authenticationScheme',
        :'enabled' => :'enabled',
        :'local_interface' => :'localInterface',
        :'max_post_wait_time' => :'maxPostWaitTime',
        :'msg_vpn_name' => :'msgVpnName',
        :'outgoing_connection_count' => :'outgoingConnectionCount',
        :'remote_host' => :'remoteHost',
        :'remote_port' => :'remotePort',
        :'rest_consumer_name' => :'restConsumerName',
        :'rest_delivery_point_name' => :'restDeliveryPointName',
        :'retry_delay' => :'retryDelay',
        :'tls_cipher_suite_list' => :'tlsCipherSuiteList',
        :'tls_enabled' => :'tlsEnabled'
      }
    end

    # Attribute type mapping.
    def self.swagger_types
      {
        :'authentication_http_basic_password' => :'String',
        :'authentication_http_basic_username' => :'String',
        :'authentication_scheme' => :'String',
        :'enabled' => :'BOOLEAN',
        :'local_interface' => :'String',
        :'max_post_wait_time' => :'Integer',
        :'msg_vpn_name' => :'String',
        :'outgoing_connection_count' => :'Integer',
        :'remote_host' => :'String',
        :'remote_port' => :'Integer',
        :'rest_consumer_name' => :'String',
        :'rest_delivery_point_name' => :'String',
        :'retry_delay' => :'Integer',
        :'tls_cipher_suite_list' => :'String',
        :'tls_enabled' => :'BOOLEAN'
      }
    end

    # Initializes the object
    # @param [Hash] attributes Model attributes in the form of hash
    def initialize(attributes = {})
      return unless attributes.is_a?(Hash)

      # convert string to symbol for hash key
      attributes = attributes.each_with_object({}){|(k,v), h| h[k.to_sym] = v}

      if attributes.has_key?(:'authenticationHttpBasicPassword')
        self.authentication_http_basic_password = attributes[:'authenticationHttpBasicPassword']
      end

      if attributes.has_key?(:'authenticationHttpBasicUsername')
        self.authentication_http_basic_username = attributes[:'authenticationHttpBasicUsername']
      end

      if attributes.has_key?(:'authenticationScheme')
        self.authentication_scheme = attributes[:'authenticationScheme']
      end

      if attributes.has_key?(:'enabled')
        self.enabled = attributes[:'enabled']
      end

      if attributes.has_key?(:'localInterface')
        self.local_interface = attributes[:'localInterface']
      end

      if attributes.has_key?(:'maxPostWaitTime')
        self.max_post_wait_time = attributes[:'maxPostWaitTime']
      end

      if attributes.has_key?(:'msgVpnName')
        self.msg_vpn_name = attributes[:'msgVpnName']
      end

      if attributes.has_key?(:'outgoingConnectionCount')
        self.outgoing_connection_count = attributes[:'outgoingConnectionCount']
      end

      if attributes.has_key?(:'remoteHost')
        self.remote_host = attributes[:'remoteHost']
      end

      if attributes.has_key?(:'remotePort')
        self.remote_port = attributes[:'remotePort']
      end

      if attributes.has_key?(:'restConsumerName')
        self.rest_consumer_name = attributes[:'restConsumerName']
      end

      if attributes.has_key?(:'restDeliveryPointName')
        self.rest_delivery_point_name = attributes[:'restDeliveryPointName']
      end

      if attributes.has_key?(:'retryDelay')
        self.retry_delay = attributes[:'retryDelay']
      end

      if attributes.has_key?(:'tlsCipherSuiteList')
        self.tls_cipher_suite_list = attributes[:'tlsCipherSuiteList']
      end

      if attributes.has_key?(:'tlsEnabled')
        self.tls_enabled = attributes[:'tlsEnabled']
      end

    end

    # Show invalid properties with the reasons. Usually used together with valid?
    # @return Array for valid properies with the reasons
    def list_invalid_properties
      invalid_properties = Array.new
      return invalid_properties
    end

    # Check to see if the all the properties in the model are valid
    # @return true if the model is valid
    def valid?
      authentication_scheme_validator = EnumAttributeValidator.new('String', ["none", "http-basic", "client-certificate"])
      return false unless authentication_scheme_validator.valid?(@authentication_scheme)
      return true
    end

    # Custom attribute writer method checking allowed values (enum).
    # @param [Object] authentication_scheme Object to be assigned
    def authentication_scheme=(authentication_scheme)
      validator = EnumAttributeValidator.new('String', ["none", "http-basic", "client-certificate"])
      unless validator.valid?(authentication_scheme)
        fail ArgumentError, "invalid value for 'authentication_scheme', must be one of #{validator.allowable_values}."
      end
      @authentication_scheme = authentication_scheme
    end

    # Checks equality by comparing each attribute.
    # @param [Object] Object to be compared
    def ==(o)
      return true if self.equal?(o)
      self.class == o.class &&
          authentication_http_basic_password == o.authentication_http_basic_password &&
          authentication_http_basic_username == o.authentication_http_basic_username &&
          authentication_scheme == o.authentication_scheme &&
          enabled == o.enabled &&
          local_interface == o.local_interface &&
          max_post_wait_time == o.max_post_wait_time &&
          msg_vpn_name == o.msg_vpn_name &&
          outgoing_connection_count == o.outgoing_connection_count &&
          remote_host == o.remote_host &&
          remote_port == o.remote_port &&
          rest_consumer_name == o.rest_consumer_name &&
          rest_delivery_point_name == o.rest_delivery_point_name &&
          retry_delay == o.retry_delay &&
          tls_cipher_suite_list == o.tls_cipher_suite_list &&
          tls_enabled == o.tls_enabled
    end

    # @see the `==` method
    # @param [Object] Object to be compared
    def eql?(o)
      self == o
    end

    # Calculates hash code according to all attributes.
    # @return [Fixnum] Hash code
    def hash
      [authentication_http_basic_password, authentication_http_basic_username, authentication_scheme, enabled, local_interface, max_post_wait_time, msg_vpn_name, outgoing_connection_count, remote_host, remote_port, rest_consumer_name, rest_delivery_point_name, retry_delay, tls_cipher_suite_list, tls_enabled].hash
    end

    # Builds the object from hash
    # @param [Hash] attributes Model attributes in the form of hash
    # @return [Object] Returns the model itself
    def build_from_hash(attributes)
      return nil unless attributes.is_a?(Hash)
      self.class.swagger_types.each_pair do |key, type|
        if type =~ /^Array<(.*)>/i
          # check to ensure the input is an array given that the the attribute
          # is documented as an array but the input is not
          if attributes[self.class.attribute_map[key]].is_a?(Array)
            self.send("#{key}=", attributes[self.class.attribute_map[key]].map{ |v| _deserialize($1, v) } )
          end
        elsif !attributes[self.class.attribute_map[key]].nil?
          self.send("#{key}=", _deserialize(type, attributes[self.class.attribute_map[key]]))
        end # or else data not found in attributes(hash), not an issue as the data can be optional
      end

      self
    end

    # Deserializes the data based on type
    # @param string type Data type
    # @param string value Value to be deserialized
    # @return [Object] Deserialized data
    def _deserialize(type, value)
      case type.to_sym
      when :DateTime
        DateTime.parse(value)
      when :Date
        Date.parse(value)
      when :String
        value.to_s
      when :Integer
        value.to_i
      when :Float
        value.to_f
      when :BOOLEAN
        if value.to_s =~ /^(true|t|yes|y|1)$/i
          true
        else
          false
        end
      when :Object
        # generic object (usually a Hash), return directly
        value
      when /\AArray<(?<inner_type>.+)>\z/
        inner_type = Regexp.last_match[:inner_type]
        value.map { |v| _deserialize(inner_type, v) }
      when /\AHash<(?<k_type>.+), (?<v_type>.+)>\z/
        k_type = Regexp.last_match[:k_type]
        v_type = Regexp.last_match[:v_type]
        {}.tap do |hash|
          value.each do |k, v|
            hash[_deserialize(k_type, k)] = _deserialize(v_type, v)
          end
        end
      else # model
        temp_model = SempClient.const_get(type).new
        temp_model.build_from_hash(value)
      end
    end

    # Returns the string representation of the object
    # @return [String] String presentation of the object
    def to_s
      to_hash.to_s
    end

    # to_body is an alias to to_hash (backward compatibility)
    # @return [Hash] Returns the object in the form of hash
    def to_body
      to_hash
    end

    # Returns the object in the form of hash
    # @return [Hash] Returns the object in the form of hash
    def to_hash
      hash = {}
      self.class.attribute_map.each_pair do |attr, param|
        value = self.send(attr)
        next if value.nil?
        hash[param] = _to_hash(value)
      end
      hash
    end

    # Outputs non-array value in the form of hash
    # For object, use to_hash. Otherwise, just return the value
    # @param [Object] value Any valid value
    # @return [Hash] Returns the value in the form of hash
    def _to_hash(value)
      if value.is_a?(Array)
        value.compact.map{ |v| _to_hash(v) }
      elsif value.is_a?(Hash)
        {}.tap do |hash|
          value.each { |k, v| hash[k] = _to_hash(v) }
        end
      elsif value.respond_to? :to_hash
        value.to_hash
      else
        value
      end
    end

  end

end
