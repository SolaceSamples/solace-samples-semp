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

  class MsgVpnQueue
    # The queue access type of either exclusive or non-exclusive. The default value is `\"exclusive\"`. The allowed values and their meaning are:      \"exclusive\" - Exclusive delivery of messages to first bound client.     \"non-exclusive\" - Non-exclusive delivery of messages to all bound clients. 
    attr_accessor :access_type

    # Enable or disable the propagation of consumer acks received on the active replication Message VPN to the standby replication Message VPN. The default value is `true`.
    attr_accessor :consumer_ack_propagation_enabled

    # Identifies the name of the queue which should be used as the queue's dead message queue. The default value is `\"#DEAD_MSG_QUEUE\"`.
    attr_accessor :dead_msg_queue

    # Enable or disable the flow of messages from a queue. The default value is `false`.
    attr_accessor :egress_enabled

    attr_accessor :event_bind_count_threshold

    attr_accessor :event_msg_spool_usage_threshold

    attr_accessor :event_reject_low_priority_msg_limit_threshold

    # Enable or disable the flow of messages to a queue. The default value is `false`.
    attr_accessor :ingress_enabled

    # The maximum number of times a client(s) can bind to a given queue. The default value is `1000`.
    attr_accessor :max_bind_count

    # The max messages delivered but not acknowledged per flow for this queue. The default is the max value supported by the hardware.
    attr_accessor :max_delivered_unacked_msgs_per_flow

    # The max message size (in bytes) allowed in this queue. The default value is `10000000`.
    attr_accessor :max_msg_size

    # The max spool usage (in MB) of this queue. Setting the value to 0 enables the last-value-queue feature and disables quota checking. The default value is `4000`.
    attr_accessor :max_msg_spool_usage

    # The maximum number of times the queue will attempt redelivery of a given message prior to it being discarded or moved to the #DEAD_MSG_QUEUE. A value of 0 means to retry forever. The default value is `0`.
    attr_accessor :max_redelivery_count

    # The maximum number of seconds that a message can stay in a queue or topic-endpoint when respect-ttl is enabled. A message will expire according to the lesser of the TTL in the message (assigned by the publisher) and the max-ttl configured on the endpoint. The max-ttl is a 32 bit integer value from 1 to 4294967295 representing the expiry time in seconds. A max-ttl of 0 disables this feature. The default value is `0`.
    attr_accessor :max_ttl

    # The name of the Message VPN.
    attr_accessor :msg_vpn_name

    # The client-username owner of the queue. The default is to have no `owner`.
    attr_accessor :owner

    # Permission level for users of the queue, excluding the owner. The default value is `\"no-access\"`. The allowed values and their meaning are:      \"no-access\" - Disallows all access.     \"read-only\" - Read-only access to the messages in the queue.     \"consume\" - Consume (read and remove) messages in the queue.     \"modify-topic\" - Consume messages or modify the topic/selector of the queue.     \"delete\" - Consume messages or delete the queue altogether. 
    attr_accessor :permission

    # The name of the Queue.
    attr_accessor :queue_name

    # Enable or disable if low priority messages are subject to `rejectLowPriorityMsgLimit` checking. This may only be enabled if `rejectMsgToSenderOnDiscardBehavior` does not have a value of `\"never\"`. The default value is `false`.
    attr_accessor :reject_low_priority_msg_enabled

    # The number of messages of any priority queued to an endpoint above which low priority messages are not admitted but higher priority messages are allowed into the endpoint. The default value is `0`.
    attr_accessor :reject_low_priority_msg_limit

    # The circumstances under which a nack is sent to the client on discards. Note that nacks cause the message to not be delivered to any destination and transacted-session commits to fail. This attribute may only have a value of `\"never\"` if `rejectLowPriorityMsgEnabled` is disabled. The default value is `\"when-queue-enabled\"`. The allowed values and their meaning are:      \"always\" - Message discards always result in nacks being returned to the sending client, even if the discard reason is that the queue is disabled.     \"when-queue-enabled\" - Message discards result in nacks being returned to the sending client, except if the discard reason is that the queue is disabled.     \"never\" - Message discards never result in nacks being returned to the sending client. 
    attr_accessor :reject_msg_to_sender_on_discard_behavior

    # Enable or disable the respecting of TTL. If enabled, then messages contained in the queue are checked for expiry. If expired, the message is removed from the queue and either discarded or a copy of the message placed in the #DEAD_MSG_QUEUE. The default value is `false`.
    attr_accessor :respect_ttl_enabled

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
        :'access_type' => :'accessType',
        :'consumer_ack_propagation_enabled' => :'consumerAckPropagationEnabled',
        :'dead_msg_queue' => :'deadMsgQueue',
        :'egress_enabled' => :'egressEnabled',
        :'event_bind_count_threshold' => :'eventBindCountThreshold',
        :'event_msg_spool_usage_threshold' => :'eventMsgSpoolUsageThreshold',
        :'event_reject_low_priority_msg_limit_threshold' => :'eventRejectLowPriorityMsgLimitThreshold',
        :'ingress_enabled' => :'ingressEnabled',
        :'max_bind_count' => :'maxBindCount',
        :'max_delivered_unacked_msgs_per_flow' => :'maxDeliveredUnackedMsgsPerFlow',
        :'max_msg_size' => :'maxMsgSize',
        :'max_msg_spool_usage' => :'maxMsgSpoolUsage',
        :'max_redelivery_count' => :'maxRedeliveryCount',
        :'max_ttl' => :'maxTtl',
        :'msg_vpn_name' => :'msgVpnName',
        :'owner' => :'owner',
        :'permission' => :'permission',
        :'queue_name' => :'queueName',
        :'reject_low_priority_msg_enabled' => :'rejectLowPriorityMsgEnabled',
        :'reject_low_priority_msg_limit' => :'rejectLowPriorityMsgLimit',
        :'reject_msg_to_sender_on_discard_behavior' => :'rejectMsgToSenderOnDiscardBehavior',
        :'respect_ttl_enabled' => :'respectTtlEnabled'
      }
    end

    # Attribute type mapping.
    def self.swagger_types
      {
        :'access_type' => :'String',
        :'consumer_ack_propagation_enabled' => :'BOOLEAN',
        :'dead_msg_queue' => :'String',
        :'egress_enabled' => :'BOOLEAN',
        :'event_bind_count_threshold' => :'EventThreshold',
        :'event_msg_spool_usage_threshold' => :'EventThreshold',
        :'event_reject_low_priority_msg_limit_threshold' => :'EventThreshold',
        :'ingress_enabled' => :'BOOLEAN',
        :'max_bind_count' => :'Integer',
        :'max_delivered_unacked_msgs_per_flow' => :'Integer',
        :'max_msg_size' => :'Integer',
        :'max_msg_spool_usage' => :'Integer',
        :'max_redelivery_count' => :'Integer',
        :'max_ttl' => :'Integer',
        :'msg_vpn_name' => :'String',
        :'owner' => :'String',
        :'permission' => :'String',
        :'queue_name' => :'String',
        :'reject_low_priority_msg_enabled' => :'BOOLEAN',
        :'reject_low_priority_msg_limit' => :'Integer',
        :'reject_msg_to_sender_on_discard_behavior' => :'String',
        :'respect_ttl_enabled' => :'BOOLEAN'
      }
    end

    # Initializes the object
    # @param [Hash] attributes Model attributes in the form of hash
    def initialize(attributes = {})
      return unless attributes.is_a?(Hash)

      # convert string to symbol for hash key
      attributes = attributes.each_with_object({}){|(k,v), h| h[k.to_sym] = v}

      if attributes.has_key?(:'accessType')
        self.access_type = attributes[:'accessType']
      end

      if attributes.has_key?(:'consumerAckPropagationEnabled')
        self.consumer_ack_propagation_enabled = attributes[:'consumerAckPropagationEnabled']
      end

      if attributes.has_key?(:'deadMsgQueue')
        self.dead_msg_queue = attributes[:'deadMsgQueue']
      end

      if attributes.has_key?(:'egressEnabled')
        self.egress_enabled = attributes[:'egressEnabled']
      end

      if attributes.has_key?(:'eventBindCountThreshold')
        self.event_bind_count_threshold = attributes[:'eventBindCountThreshold']
      end

      if attributes.has_key?(:'eventMsgSpoolUsageThreshold')
        self.event_msg_spool_usage_threshold = attributes[:'eventMsgSpoolUsageThreshold']
      end

      if attributes.has_key?(:'eventRejectLowPriorityMsgLimitThreshold')
        self.event_reject_low_priority_msg_limit_threshold = attributes[:'eventRejectLowPriorityMsgLimitThreshold']
      end

      if attributes.has_key?(:'ingressEnabled')
        self.ingress_enabled = attributes[:'ingressEnabled']
      end

      if attributes.has_key?(:'maxBindCount')
        self.max_bind_count = attributes[:'maxBindCount']
      end

      if attributes.has_key?(:'maxDeliveredUnackedMsgsPerFlow')
        self.max_delivered_unacked_msgs_per_flow = attributes[:'maxDeliveredUnackedMsgsPerFlow']
      end

      if attributes.has_key?(:'maxMsgSize')
        self.max_msg_size = attributes[:'maxMsgSize']
      end

      if attributes.has_key?(:'maxMsgSpoolUsage')
        self.max_msg_spool_usage = attributes[:'maxMsgSpoolUsage']
      end

      if attributes.has_key?(:'maxRedeliveryCount')
        self.max_redelivery_count = attributes[:'maxRedeliveryCount']
      end

      if attributes.has_key?(:'maxTtl')
        self.max_ttl = attributes[:'maxTtl']
      end

      if attributes.has_key?(:'msgVpnName')
        self.msg_vpn_name = attributes[:'msgVpnName']
      end

      if attributes.has_key?(:'owner')
        self.owner = attributes[:'owner']
      end

      if attributes.has_key?(:'permission')
        self.permission = attributes[:'permission']
      end

      if attributes.has_key?(:'queueName')
        self.queue_name = attributes[:'queueName']
      end

      if attributes.has_key?(:'rejectLowPriorityMsgEnabled')
        self.reject_low_priority_msg_enabled = attributes[:'rejectLowPriorityMsgEnabled']
      end

      if attributes.has_key?(:'rejectLowPriorityMsgLimit')
        self.reject_low_priority_msg_limit = attributes[:'rejectLowPriorityMsgLimit']
      end

      if attributes.has_key?(:'rejectMsgToSenderOnDiscardBehavior')
        self.reject_msg_to_sender_on_discard_behavior = attributes[:'rejectMsgToSenderOnDiscardBehavior']
      end

      if attributes.has_key?(:'respectTtlEnabled')
        self.respect_ttl_enabled = attributes[:'respectTtlEnabled']
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
      access_type_validator = EnumAttributeValidator.new('String', ["exclusive", "non-exclusive"])
      return false unless access_type_validator.valid?(@access_type)
      permission_validator = EnumAttributeValidator.new('String', ["no-access", "read-only", "consume", "modify-topic", "delete"])
      return false unless permission_validator.valid?(@permission)
      reject_msg_to_sender_on_discard_behavior_validator = EnumAttributeValidator.new('String', ["always", "when-queue-enabled", "never"])
      return false unless reject_msg_to_sender_on_discard_behavior_validator.valid?(@reject_msg_to_sender_on_discard_behavior)
      return true
    end

    # Custom attribute writer method checking allowed values (enum).
    # @param [Object] access_type Object to be assigned
    def access_type=(access_type)
      validator = EnumAttributeValidator.new('String', ["exclusive", "non-exclusive"])
      unless validator.valid?(access_type)
        fail ArgumentError, "invalid value for 'access_type', must be one of #{validator.allowable_values}."
      end
      @access_type = access_type
    end

    # Custom attribute writer method checking allowed values (enum).
    # @param [Object] permission Object to be assigned
    def permission=(permission)
      validator = EnumAttributeValidator.new('String', ["no-access", "read-only", "consume", "modify-topic", "delete"])
      unless validator.valid?(permission)
        fail ArgumentError, "invalid value for 'permission', must be one of #{validator.allowable_values}."
      end
      @permission = permission
    end

    # Custom attribute writer method checking allowed values (enum).
    # @param [Object] reject_msg_to_sender_on_discard_behavior Object to be assigned
    def reject_msg_to_sender_on_discard_behavior=(reject_msg_to_sender_on_discard_behavior)
      validator = EnumAttributeValidator.new('String', ["always", "when-queue-enabled", "never"])
      unless validator.valid?(reject_msg_to_sender_on_discard_behavior)
        fail ArgumentError, "invalid value for 'reject_msg_to_sender_on_discard_behavior', must be one of #{validator.allowable_values}."
      end
      @reject_msg_to_sender_on_discard_behavior = reject_msg_to_sender_on_discard_behavior
    end

    # Checks equality by comparing each attribute.
    # @param [Object] Object to be compared
    def ==(o)
      return true if self.equal?(o)
      self.class == o.class &&
          access_type == o.access_type &&
          consumer_ack_propagation_enabled == o.consumer_ack_propagation_enabled &&
          dead_msg_queue == o.dead_msg_queue &&
          egress_enabled == o.egress_enabled &&
          event_bind_count_threshold == o.event_bind_count_threshold &&
          event_msg_spool_usage_threshold == o.event_msg_spool_usage_threshold &&
          event_reject_low_priority_msg_limit_threshold == o.event_reject_low_priority_msg_limit_threshold &&
          ingress_enabled == o.ingress_enabled &&
          max_bind_count == o.max_bind_count &&
          max_delivered_unacked_msgs_per_flow == o.max_delivered_unacked_msgs_per_flow &&
          max_msg_size == o.max_msg_size &&
          max_msg_spool_usage == o.max_msg_spool_usage &&
          max_redelivery_count == o.max_redelivery_count &&
          max_ttl == o.max_ttl &&
          msg_vpn_name == o.msg_vpn_name &&
          owner == o.owner &&
          permission == o.permission &&
          queue_name == o.queue_name &&
          reject_low_priority_msg_enabled == o.reject_low_priority_msg_enabled &&
          reject_low_priority_msg_limit == o.reject_low_priority_msg_limit &&
          reject_msg_to_sender_on_discard_behavior == o.reject_msg_to_sender_on_discard_behavior &&
          respect_ttl_enabled == o.respect_ttl_enabled
    end

    # @see the `==` method
    # @param [Object] Object to be compared
    def eql?(o)
      self == o
    end

    # Calculates hash code according to all attributes.
    # @return [Fixnum] Hash code
    def hash
      [access_type, consumer_ack_propagation_enabled, dead_msg_queue, egress_enabled, event_bind_count_threshold, event_msg_spool_usage_threshold, event_reject_low_priority_msg_limit_threshold, ingress_enabled, max_bind_count, max_delivered_unacked_msgs_per_flow, max_msg_size, max_msg_spool_usage, max_redelivery_count, max_ttl, msg_vpn_name, owner, permission, queue_name, reject_low_priority_msg_enabled, reject_low_priority_msg_limit, reject_msg_to_sender_on_discard_behavior, respect_ttl_enabled].hash
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
