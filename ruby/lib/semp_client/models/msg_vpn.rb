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

  class MsgVpn
    # Enable or disable basic authentication for clients within the Message VPN. Basic authentication is authentication that involves the use of a username and password to prove identity. When enabled, the currently selected authentication type is used for authentication of users that provide basic authentication credentials. If a user provides credentials for a different authentication scheme this setting is not applicable. The default value is `true`.
    attr_accessor :authentication_basic_enabled

    # The name of the RADIUS or LDAP profile to use when `authenticationBasicType` is `\"radius\"` or `\"ldap\"` respectively. The default value is `\"default\"`.
    attr_accessor :authentication_basic_profile_name

    # The RADIUS domain string to use when `authenticationBasicType` is `\"radius\"`. The default value is `\"\"`.
    attr_accessor :authentication_basic_radius_domain

    # Authentication mechanism to be used for basic authentication of clients connecting to this Message VPN. The default value is `\"radius\"`. The allowed values and their meaning are:      \"radius\" - Radius authentication. A radius profile must be provided.     \"ldap\" - LDAP authentication. An LDAP profile must be provided.     \"internal\" - Internal database. Authentication is against Client Usernames.     \"none\" - No authentication. Anonymous login allowed. 
    attr_accessor :authentication_basic_type

    # When enabled, if the client specifies a client-username via the API connect method, the client provided username is used instead of the CN (Common Name) field of the certificate's subject. When disabled, the certificate CN is always used as the client-username. The default value is `false`.
    attr_accessor :authentication_client_cert_allow_api_provided_username_enabled

    # Enable or disable client certificate client authentication in the Message VPN. The default value is `false`.
    attr_accessor :authentication_client_cert_enabled

    # The maximum depth for a client certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.
    attr_accessor :authentication_client_cert_max_chain_depth

    # Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.
    attr_accessor :authentication_client_cert_validate_date_enabled

    # When enabled, if the client specifies a client-username via the API connect method, the client provided username is used instead of the Kerberos Principal name in Kerberos token. When disabled, the Kerberos Principal name is always used as the client-username. The default value is `false`.
    attr_accessor :authentication_kerberos_allow_api_provided_username_enabled

    # Enable or disable Kerberos authentication for clients in the Message VPN. If a user provides credentials for a different authentication scheme, this setting is not applicable. The default value is `false`.
    attr_accessor :authentication_kerberos_enabled

    # The name of the attribute that should be retrieved from the LDAP server as part of the LDAP search when authorizing a client. It indicates that the client belongs to a particular group (i.e. the value associated with this attribute). The default value is `\"memberOf\"`.
    attr_accessor :authorization_ldap_group_membership_attribute_name

    # The LDAP profile name to be used when `authorizationType` is `\"ldap\"`. The default value is `\"\"`.
    attr_accessor :authorization_profile_name

    # Authorization mechanism to be used for clients connecting to this Message VPN. The default value is `\"internal\"`. The allowed values and their meaning are:      \"ldap\" - LDAP authorization.     \"internal\" - Internal authorization. 
    attr_accessor :authorization_type

    # Enable or disable validation of the common name in the server certificate on the remote router. If enabled, the common name is checked against the list of trusted common names configured for the bridge. The default value is `true`.
    attr_accessor :bridging_tls_server_cert_enforce_trusted_common_name_enabled

    # The maximum depth for a server certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.
    attr_accessor :bridging_tls_server_cert_max_chain_depth

    # Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.
    attr_accessor :bridging_tls_server_cert_validate_date_enabled

    # Enable or disable managing of cache instances over the message bus. For a given Message VPN only one router in the network should have this attribute enabled. The default value is `true`.
    attr_accessor :distributed_cache_management_enabled

    # Enable or disable the Message VPN. The default value is `false`.
    attr_accessor :enabled

    attr_accessor :event_connection_count_threshold

    attr_accessor :event_egress_flow_count_threshold

    attr_accessor :event_egress_msg_rate_threshold

    attr_accessor :event_endpoint_count_threshold

    attr_accessor :event_ingress_flow_count_threshold

    attr_accessor :event_ingress_msg_rate_threshold

    # Size in KB for what is being considered a large message for the Message VPN. The default value is `1024`.
    attr_accessor :event_large_msg_threshold

    # A prefix applied to all publish events in this Message VPN. The default is to have no `eventLogTag`.
    attr_accessor :event_log_tag

    attr_accessor :event_msg_spool_usage_threshold

    # Enable or disable client level event message publishing. The default value is `false`.
    attr_accessor :event_publish_client_enabled

    # Enable or disable Message VPN level event message publishing. The default value is `false`.
    attr_accessor :event_publish_msg_vpn_enabled

    #  Subscription level event message publishing mode. Format v1 modes use a publish topic of the form:      #LOG/INFO/SUB_ADD/subscribed-topic     #LOG/INFO/SUB_DEL/subscribed-topic  Format v2 modes use a publish topic of the form:      #LOG/INFO/SUB/router-name/ADD/vpn-name/client-name/subscribed-topic     #LOG/INFO/SUB/router-name/DEL/vpn-name/client-name/subscribed-topic  Format v2 is recommended. . The default value is `\"off\"`. The allowed values and their meaning are:      \"off\" - Disable client level event message publishing.     \"on-with-format-v1\" - Enable client level event message publishing with format v1.     \"on-with-no-unsubscribe-events-on-disconnect-format-v1\" - As \"on-with-format-v1\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions.     \"on-with-format-v2\" - Enable client level event message publishing with format v2.     \"on-with-no-unsubscribe-events-on-disconnect-format-v2\" - As \"on-with-format-v2\", but unsubscribe events are not generated when a client disconnects. Unsubscribe events are still raised when a client explicitly unsubscribes from its subscriptions. 
    attr_accessor :event_publish_subscription_mode

    # Enable or disable event publish topics in MQTT format. The default value is `false`.
    attr_accessor :event_publish_topic_format_mqtt_enabled

    # Enable or disable event publish topics in SMF format. The default value is `true`.
    attr_accessor :event_publish_topic_format_smf_enabled

    attr_accessor :event_service_rest_incoming_connection_count_threshold

    attr_accessor :event_service_smf_connection_count_threshold

    attr_accessor :event_service_web_connection_count_threshold

    attr_accessor :event_subscription_count_threshold

    attr_accessor :event_transacted_session_count_threshold

    attr_accessor :event_transaction_count_threshold

    # Enable or disable the export of subscriptions in this Message VPN to other routers in the network over neighbor links. The default value is `false`.
    attr_accessor :export_subscriptions_enabled

    # The maximum number of client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
    attr_accessor :max_connection_count

    # The maximum number of egress flows that can be created on this Message VPN. The default value is `16000`.
    attr_accessor :max_egress_flow_count

    # The maximum number of queues and topic endpoints that can be created on this Message VPN. The default value is `16000`.
    attr_accessor :max_endpoint_count

    # The maximum number of ingress flows that can be created on this Message VPN. The default value is `16000`.
    attr_accessor :max_ingress_flow_count

    # Max spool usage (in MB) allowed for the Message VPN. The default value is `0`.
    attr_accessor :max_msg_spool_usage

    # The maximum number of local client subscriptions (both primary and backup) that can be added to this Message VPN. The default value is `5000000`.
    attr_accessor :max_subscription_count

    # The maximum number of transacted sessions for this Message VPN. The default value is `16000`.
    attr_accessor :max_transacted_session_count

    # The maximum number of transactions for this Message VPN. The default value is `50000`.
    attr_accessor :max_transaction_count

    # The name of the Message VPN.
    attr_accessor :msg_vpn_name

    # The ack-propagation interval, in number of replicated messages. The default value is `20`.
    attr_accessor :replication_ack_propagation_interval_msg_count

    # The client username the replication bridge uses to login to the Remote Message VPN on the replication mate. The default is to have no `replicationBridgeAuthenticationBasicClientUsername`.
    attr_accessor :replication_bridge_authentication_basic_client_username

    # The password that the bridge uses to login to the Remote Message VPN. The default is to have no `replicationBridgeAuthenticationBasicPassword`.
    attr_accessor :replication_bridge_authentication_basic_password

    # The authentication scheme for the replication bridge. The default value is `\"basic\"`. The allowed values and their meaning are:      \"basic\" - Basic Authentication Scheme (via username and password).     \"client-certificate\" - Client Certificate Authentication Scheme (via certificate-file). 
    attr_accessor :replication_bridge_authentication_scheme

    # Whether compression is used for the bridge. The default value is `false`.
    attr_accessor :replication_bridge_compressed_data_enabled

    # The window size of outstanding guaranteed messages. The default value is `255`.
    attr_accessor :replication_bridge_egress_flow_window_size

    # Number of seconds that must pass before retrying a connection. The default value is `3`.
    attr_accessor :replication_bridge_retry_delay

    # Enable or disable use of TLS for the bridge connection. The default value is `false`.
    attr_accessor :replication_bridge_tls_enabled

    # The client-profile for the unidirectional replication bridge for the Message VPN. The client-profile must exist in the local Message VPN, and it is used only for the TCP parameters. The default value is `\"#client-profile\"`.
    attr_accessor :replication_bridge_unidirectional_client_profile_name

    # Enable or disable the replication feature for the Message VPN. The default value is `false`.
    attr_accessor :replication_enabled

    # The behavior to take when enabling replication, depending on the existence of the replication queue. This only has meaning in a request which enables replication. The default value is `\"fail-on-existing-queue\"`. The allowed values and their meaning are:      \"fail-on-existing-queue\" - The data replication queue must not already exist.     \"force-use-existing-queue\" - The data replication queue must already exist. Any data messages on the queue will be forwarded to interested applications. IMPORTANT: Before using this mode be certain that the messages are not stale or otherwise unsuitable to be forwarded. This mode can only be specified when the existing queue is configured the same as is currently specified under replication configuration otherwise the enabling of replication will fail.     \"force-recreate-queue\" - The data replication queue must already exist. Any data messages on the queue will be discarded. IMPORTANT: Before using this mode be certain that the messages on the existing data replication queue are not needed by interested applications. 
    attr_accessor :replication_enabled_queue_behavior

    # The max spool usage (in MB) of the replication queue. The default value is `60000`.
    attr_accessor :replication_queue_max_msg_spool_usage

    # The message discard behavior. The default value is `true`.
    attr_accessor :replication_queue_reject_msg_to_sender_on_discard_enabled

    # Enable or disable sync mode ineligible behavior. If enabled and sync replication becomes ineligible, guaranteed messages published to sync replicated topics will be rejected to the sender. If disabled, sync replication will revert to async mode. The default value is `false`.
    attr_accessor :replication_reject_msg_when_sync_ineligible_enabled

    # The replication role for this Message VPN. The default value is `\"standby\"`. The allowed values and their meaning are:      \"active\" - Assume the active role in replication for the Message VPN.     \"standby\" - Assume the standby role in replication for the Message VPN. 
    attr_accessor :replication_role

    # The transaction replication mode for all transactions within a Message VPN. When mode is async, all transactions originated by clients are replicated to the standby site using async-replication. When mode is sync, all transactions originated by clients are replicated to the standby site using sync-replication. Changing this value during operation will not affect existing transactions. It is only validated upon starting a transaction. The default value is `\"async\"`. The allowed values and their meaning are:      \"sync\" - Synchronous replication-mode.     \"async\" - Asynchronous replication-mode. 
    attr_accessor :replication_transaction_mode

    # Enable or disable whether or not the trusted-common-name attribute of a REST Consumer is enforced or not. Each REST Consumer has a list of common-names which it expects to be returned in the server-certificate from the remote REST Consumer. If enforce-trusted-common-name is enabled, but the list of common-names has not been configured, the REST Consumer will not be allowed to be brought into service. An appropriate error message is provided in the REST Consumer operational display. The default value is `true`.
    attr_accessor :rest_tls_server_cert_enforce_trusted_common_name_enabled

    # The maximum depth for the certificate chain. The depth of a chain is defined as the number of signing CA certificates that are present in the chain back to a trusted self-signed root CA certificate. The default value is `3`.
    attr_accessor :rest_tls_server_cert_max_chain_depth

    # Enable or disable validation of the \"Not Before\" and \"Not After\" validity dates in the server certificate. When disabled, a certificate will be accepted even if the certificate is not valid according to the \"Not Before\" and \"Not After\" validity dates in the certificate. The default value is `true`.
    attr_accessor :rest_tls_server_cert_validate_date_enabled

    # Enable or disable \"admin client\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.
    attr_accessor :semp_over_msg_bus_admin_client_enabled

    # Enable or disable \"admin distributed-cache\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.
    attr_accessor :semp_over_msg_bus_admin_distributed_cache_enabled

    # Enable or disable \"admin\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.
    attr_accessor :semp_over_msg_bus_admin_enabled

    # Enable or disable SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `true`.
    attr_accessor :semp_over_msg_bus_enabled

    # Enable or disable \"legacy-show-clear\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `true`.
    attr_accessor :semp_over_msg_bus_legacy_show_clear_enabled

    # Enable or disable \"show\" SEMP over Message Bus for the current Message VPN. This applies only to SEMPv1. The default value is `false`.
    attr_accessor :semp_over_msg_bus_show_enabled

    # The maximum number of REST incoming client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
    attr_accessor :service_rest_incoming_max_connection_count

    # Enable or disable plain-text REST for this Message VPN. The default value is `false`.
    attr_accessor :service_rest_incoming_plain_text_enabled

    # The TCP port on the NAB for incoming plain-text REST client connections for the Message VPN. The TCP port must not be in use by another service in any Message VPN in the msg-backbone VRF. Enabling plain-text REST is not allowed without a listen-port. The default is to have no `serviceRestIncomingPlainTextListenPort`.
    attr_accessor :service_rest_incoming_plain_text_listen_port

    # Enable or disable incoming TLS REST service for this Message VPN. The default value is `false`.
    attr_accessor :service_rest_incoming_tls_enabled

    # The TCP port on the NAB for incoming TLS REST client connections for the Message VPN. The TCP port must not be in use by another service in any Message VPN in the msg-backbone VRF. Enabling plain-text REST is not allowed without a listen-port. The default is to have no `serviceRestIncomingTlsListenPort`.
    attr_accessor :service_rest_incoming_tls_listen_port

    # The maximum number of REST consumer connections that can be simultaneously established from the Message VPN. The default value is `6000`.
    attr_accessor :service_rest_outgoing_max_connection_count

    # The maximum number of SMF client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
    attr_accessor :service_smf_max_connection_count

    # Enable or disable plain-text SMF service in the Message VPN. The default value is `true`.
    attr_accessor :service_smf_plain_text_enabled

    # Enable or disable TLS SMF service for this Message VPN. The default value is `true`.
    attr_accessor :service_smf_tls_enabled

    # The maximum number of web-transport client connections that can be simultaneously connected to the Message VPN. This value may be higher than supported by the hardware. The default is the max value supported by the hardware.
    attr_accessor :service_web_max_connection_count

    # Enable or disable plain-text Web Transport service in the Message VPN. The default value is `true`.
    attr_accessor :service_web_plain_text_enabled

    # Enable or disable TLS Web Transport service in the Message VPN. The default value is `true`.
    attr_accessor :service_web_tls_enabled

    # Enable or disable the allowing of TLS SMF clients to downgrade their connections to plain-text connections. Changing this will not affect existing connections. The default value is `false`.
    attr_accessor :tls_allow_downgrade_to_plain_text_enabled

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
        :'authentication_basic_enabled' => :'authenticationBasicEnabled',
        :'authentication_basic_profile_name' => :'authenticationBasicProfileName',
        :'authentication_basic_radius_domain' => :'authenticationBasicRadiusDomain',
        :'authentication_basic_type' => :'authenticationBasicType',
        :'authentication_client_cert_allow_api_provided_username_enabled' => :'authenticationClientCertAllowApiProvidedUsernameEnabled',
        :'authentication_client_cert_enabled' => :'authenticationClientCertEnabled',
        :'authentication_client_cert_max_chain_depth' => :'authenticationClientCertMaxChainDepth',
        :'authentication_client_cert_validate_date_enabled' => :'authenticationClientCertValidateDateEnabled',
        :'authentication_kerberos_allow_api_provided_username_enabled' => :'authenticationKerberosAllowApiProvidedUsernameEnabled',
        :'authentication_kerberos_enabled' => :'authenticationKerberosEnabled',
        :'authorization_ldap_group_membership_attribute_name' => :'authorizationLdapGroupMembershipAttributeName',
        :'authorization_profile_name' => :'authorizationProfileName',
        :'authorization_type' => :'authorizationType',
        :'bridging_tls_server_cert_enforce_trusted_common_name_enabled' => :'bridgingTlsServerCertEnforceTrustedCommonNameEnabled',
        :'bridging_tls_server_cert_max_chain_depth' => :'bridgingTlsServerCertMaxChainDepth',
        :'bridging_tls_server_cert_validate_date_enabled' => :'bridgingTlsServerCertValidateDateEnabled',
        :'distributed_cache_management_enabled' => :'distributedCacheManagementEnabled',
        :'enabled' => :'enabled',
        :'event_connection_count_threshold' => :'eventConnectionCountThreshold',
        :'event_egress_flow_count_threshold' => :'eventEgressFlowCountThreshold',
        :'event_egress_msg_rate_threshold' => :'eventEgressMsgRateThreshold',
        :'event_endpoint_count_threshold' => :'eventEndpointCountThreshold',
        :'event_ingress_flow_count_threshold' => :'eventIngressFlowCountThreshold',
        :'event_ingress_msg_rate_threshold' => :'eventIngressMsgRateThreshold',
        :'event_large_msg_threshold' => :'eventLargeMsgThreshold',
        :'event_log_tag' => :'eventLogTag',
        :'event_msg_spool_usage_threshold' => :'eventMsgSpoolUsageThreshold',
        :'event_publish_client_enabled' => :'eventPublishClientEnabled',
        :'event_publish_msg_vpn_enabled' => :'eventPublishMsgVpnEnabled',
        :'event_publish_subscription_mode' => :'eventPublishSubscriptionMode',
        :'event_publish_topic_format_mqtt_enabled' => :'eventPublishTopicFormatMqttEnabled',
        :'event_publish_topic_format_smf_enabled' => :'eventPublishTopicFormatSmfEnabled',
        :'event_service_rest_incoming_connection_count_threshold' => :'eventServiceRestIncomingConnectionCountThreshold',
        :'event_service_smf_connection_count_threshold' => :'eventServiceSmfConnectionCountThreshold',
        :'event_service_web_connection_count_threshold' => :'eventServiceWebConnectionCountThreshold',
        :'event_subscription_count_threshold' => :'eventSubscriptionCountThreshold',
        :'event_transacted_session_count_threshold' => :'eventTransactedSessionCountThreshold',
        :'event_transaction_count_threshold' => :'eventTransactionCountThreshold',
        :'export_subscriptions_enabled' => :'exportSubscriptionsEnabled',
        :'max_connection_count' => :'maxConnectionCount',
        :'max_egress_flow_count' => :'maxEgressFlowCount',
        :'max_endpoint_count' => :'maxEndpointCount',
        :'max_ingress_flow_count' => :'maxIngressFlowCount',
        :'max_msg_spool_usage' => :'maxMsgSpoolUsage',
        :'max_subscription_count' => :'maxSubscriptionCount',
        :'max_transacted_session_count' => :'maxTransactedSessionCount',
        :'max_transaction_count' => :'maxTransactionCount',
        :'msg_vpn_name' => :'msgVpnName',
        :'replication_ack_propagation_interval_msg_count' => :'replicationAckPropagationIntervalMsgCount',
        :'replication_bridge_authentication_basic_client_username' => :'replicationBridgeAuthenticationBasicClientUsername',
        :'replication_bridge_authentication_basic_password' => :'replicationBridgeAuthenticationBasicPassword',
        :'replication_bridge_authentication_scheme' => :'replicationBridgeAuthenticationScheme',
        :'replication_bridge_compressed_data_enabled' => :'replicationBridgeCompressedDataEnabled',
        :'replication_bridge_egress_flow_window_size' => :'replicationBridgeEgressFlowWindowSize',
        :'replication_bridge_retry_delay' => :'replicationBridgeRetryDelay',
        :'replication_bridge_tls_enabled' => :'replicationBridgeTlsEnabled',
        :'replication_bridge_unidirectional_client_profile_name' => :'replicationBridgeUnidirectionalClientProfileName',
        :'replication_enabled' => :'replicationEnabled',
        :'replication_enabled_queue_behavior' => :'replicationEnabledQueueBehavior',
        :'replication_queue_max_msg_spool_usage' => :'replicationQueueMaxMsgSpoolUsage',
        :'replication_queue_reject_msg_to_sender_on_discard_enabled' => :'replicationQueueRejectMsgToSenderOnDiscardEnabled',
        :'replication_reject_msg_when_sync_ineligible_enabled' => :'replicationRejectMsgWhenSyncIneligibleEnabled',
        :'replication_role' => :'replicationRole',
        :'replication_transaction_mode' => :'replicationTransactionMode',
        :'rest_tls_server_cert_enforce_trusted_common_name_enabled' => :'restTlsServerCertEnforceTrustedCommonNameEnabled',
        :'rest_tls_server_cert_max_chain_depth' => :'restTlsServerCertMaxChainDepth',
        :'rest_tls_server_cert_validate_date_enabled' => :'restTlsServerCertValidateDateEnabled',
        :'semp_over_msg_bus_admin_client_enabled' => :'sempOverMsgBusAdminClientEnabled',
        :'semp_over_msg_bus_admin_distributed_cache_enabled' => :'sempOverMsgBusAdminDistributedCacheEnabled',
        :'semp_over_msg_bus_admin_enabled' => :'sempOverMsgBusAdminEnabled',
        :'semp_over_msg_bus_enabled' => :'sempOverMsgBusEnabled',
        :'semp_over_msg_bus_legacy_show_clear_enabled' => :'sempOverMsgBusLegacyShowClearEnabled',
        :'semp_over_msg_bus_show_enabled' => :'sempOverMsgBusShowEnabled',
        :'service_rest_incoming_max_connection_count' => :'serviceRestIncomingMaxConnectionCount',
        :'service_rest_incoming_plain_text_enabled' => :'serviceRestIncomingPlainTextEnabled',
        :'service_rest_incoming_plain_text_listen_port' => :'serviceRestIncomingPlainTextListenPort',
        :'service_rest_incoming_tls_enabled' => :'serviceRestIncomingTlsEnabled',
        :'service_rest_incoming_tls_listen_port' => :'serviceRestIncomingTlsListenPort',
        :'service_rest_outgoing_max_connection_count' => :'serviceRestOutgoingMaxConnectionCount',
        :'service_smf_max_connection_count' => :'serviceSmfMaxConnectionCount',
        :'service_smf_plain_text_enabled' => :'serviceSmfPlainTextEnabled',
        :'service_smf_tls_enabled' => :'serviceSmfTlsEnabled',
        :'service_web_max_connection_count' => :'serviceWebMaxConnectionCount',
        :'service_web_plain_text_enabled' => :'serviceWebPlainTextEnabled',
        :'service_web_tls_enabled' => :'serviceWebTlsEnabled',
        :'tls_allow_downgrade_to_plain_text_enabled' => :'tlsAllowDowngradeToPlainTextEnabled'
      }
    end

    # Attribute type mapping.
    def self.swagger_types
      {
        :'authentication_basic_enabled' => :'BOOLEAN',
        :'authentication_basic_profile_name' => :'String',
        :'authentication_basic_radius_domain' => :'String',
        :'authentication_basic_type' => :'String',
        :'authentication_client_cert_allow_api_provided_username_enabled' => :'BOOLEAN',
        :'authentication_client_cert_enabled' => :'BOOLEAN',
        :'authentication_client_cert_max_chain_depth' => :'Integer',
        :'authentication_client_cert_validate_date_enabled' => :'BOOLEAN',
        :'authentication_kerberos_allow_api_provided_username_enabled' => :'BOOLEAN',
        :'authentication_kerberos_enabled' => :'BOOLEAN',
        :'authorization_ldap_group_membership_attribute_name' => :'String',
        :'authorization_profile_name' => :'String',
        :'authorization_type' => :'String',
        :'bridging_tls_server_cert_enforce_trusted_common_name_enabled' => :'BOOLEAN',
        :'bridging_tls_server_cert_max_chain_depth' => :'Integer',
        :'bridging_tls_server_cert_validate_date_enabled' => :'BOOLEAN',
        :'distributed_cache_management_enabled' => :'BOOLEAN',
        :'enabled' => :'BOOLEAN',
        :'event_connection_count_threshold' => :'EventThreshold',
        :'event_egress_flow_count_threshold' => :'EventThreshold',
        :'event_egress_msg_rate_threshold' => :'EventThresholdByValue',
        :'event_endpoint_count_threshold' => :'EventThreshold',
        :'event_ingress_flow_count_threshold' => :'EventThreshold',
        :'event_ingress_msg_rate_threshold' => :'EventThresholdByValue',
        :'event_large_msg_threshold' => :'Integer',
        :'event_log_tag' => :'String',
        :'event_msg_spool_usage_threshold' => :'EventThreshold',
        :'event_publish_client_enabled' => :'BOOLEAN',
        :'event_publish_msg_vpn_enabled' => :'BOOLEAN',
        :'event_publish_subscription_mode' => :'String',
        :'event_publish_topic_format_mqtt_enabled' => :'BOOLEAN',
        :'event_publish_topic_format_smf_enabled' => :'BOOLEAN',
        :'event_service_rest_incoming_connection_count_threshold' => :'EventThreshold',
        :'event_service_smf_connection_count_threshold' => :'EventThreshold',
        :'event_service_web_connection_count_threshold' => :'EventThreshold',
        :'event_subscription_count_threshold' => :'EventThreshold',
        :'event_transacted_session_count_threshold' => :'EventThreshold',
        :'event_transaction_count_threshold' => :'EventThreshold',
        :'export_subscriptions_enabled' => :'BOOLEAN',
        :'max_connection_count' => :'Integer',
        :'max_egress_flow_count' => :'Integer',
        :'max_endpoint_count' => :'Integer',
        :'max_ingress_flow_count' => :'Integer',
        :'max_msg_spool_usage' => :'Integer',
        :'max_subscription_count' => :'Integer',
        :'max_transacted_session_count' => :'Integer',
        :'max_transaction_count' => :'Integer',
        :'msg_vpn_name' => :'String',
        :'replication_ack_propagation_interval_msg_count' => :'Integer',
        :'replication_bridge_authentication_basic_client_username' => :'String',
        :'replication_bridge_authentication_basic_password' => :'String',
        :'replication_bridge_authentication_scheme' => :'String',
        :'replication_bridge_compressed_data_enabled' => :'BOOLEAN',
        :'replication_bridge_egress_flow_window_size' => :'Integer',
        :'replication_bridge_retry_delay' => :'Integer',
        :'replication_bridge_tls_enabled' => :'BOOLEAN',
        :'replication_bridge_unidirectional_client_profile_name' => :'String',
        :'replication_enabled' => :'BOOLEAN',
        :'replication_enabled_queue_behavior' => :'String',
        :'replication_queue_max_msg_spool_usage' => :'Integer',
        :'replication_queue_reject_msg_to_sender_on_discard_enabled' => :'BOOLEAN',
        :'replication_reject_msg_when_sync_ineligible_enabled' => :'BOOLEAN',
        :'replication_role' => :'String',
        :'replication_transaction_mode' => :'String',
        :'rest_tls_server_cert_enforce_trusted_common_name_enabled' => :'BOOLEAN',
        :'rest_tls_server_cert_max_chain_depth' => :'Integer',
        :'rest_tls_server_cert_validate_date_enabled' => :'BOOLEAN',
        :'semp_over_msg_bus_admin_client_enabled' => :'BOOLEAN',
        :'semp_over_msg_bus_admin_distributed_cache_enabled' => :'BOOLEAN',
        :'semp_over_msg_bus_admin_enabled' => :'BOOLEAN',
        :'semp_over_msg_bus_enabled' => :'BOOLEAN',
        :'semp_over_msg_bus_legacy_show_clear_enabled' => :'BOOLEAN',
        :'semp_over_msg_bus_show_enabled' => :'BOOLEAN',
        :'service_rest_incoming_max_connection_count' => :'Integer',
        :'service_rest_incoming_plain_text_enabled' => :'BOOLEAN',
        :'service_rest_incoming_plain_text_listen_port' => :'Integer',
        :'service_rest_incoming_tls_enabled' => :'BOOLEAN',
        :'service_rest_incoming_tls_listen_port' => :'Integer',
        :'service_rest_outgoing_max_connection_count' => :'Integer',
        :'service_smf_max_connection_count' => :'Integer',
        :'service_smf_plain_text_enabled' => :'BOOLEAN',
        :'service_smf_tls_enabled' => :'BOOLEAN',
        :'service_web_max_connection_count' => :'Integer',
        :'service_web_plain_text_enabled' => :'BOOLEAN',
        :'service_web_tls_enabled' => :'BOOLEAN',
        :'tls_allow_downgrade_to_plain_text_enabled' => :'BOOLEAN'
      }
    end

    # Initializes the object
    # @param [Hash] attributes Model attributes in the form of hash
    def initialize(attributes = {})
      return unless attributes.is_a?(Hash)

      # convert string to symbol for hash key
      attributes = attributes.each_with_object({}){|(k,v), h| h[k.to_sym] = v}

      if attributes.has_key?(:'authenticationBasicEnabled')
        self.authentication_basic_enabled = attributes[:'authenticationBasicEnabled']
      end

      if attributes.has_key?(:'authenticationBasicProfileName')
        self.authentication_basic_profile_name = attributes[:'authenticationBasicProfileName']
      end

      if attributes.has_key?(:'authenticationBasicRadiusDomain')
        self.authentication_basic_radius_domain = attributes[:'authenticationBasicRadiusDomain']
      end

      if attributes.has_key?(:'authenticationBasicType')
        self.authentication_basic_type = attributes[:'authenticationBasicType']
      end

      if attributes.has_key?(:'authenticationClientCertAllowApiProvidedUsernameEnabled')
        self.authentication_client_cert_allow_api_provided_username_enabled = attributes[:'authenticationClientCertAllowApiProvidedUsernameEnabled']
      end

      if attributes.has_key?(:'authenticationClientCertEnabled')
        self.authentication_client_cert_enabled = attributes[:'authenticationClientCertEnabled']
      end

      if attributes.has_key?(:'authenticationClientCertMaxChainDepth')
        self.authentication_client_cert_max_chain_depth = attributes[:'authenticationClientCertMaxChainDepth']
      end

      if attributes.has_key?(:'authenticationClientCertValidateDateEnabled')
        self.authentication_client_cert_validate_date_enabled = attributes[:'authenticationClientCertValidateDateEnabled']
      end

      if attributes.has_key?(:'authenticationKerberosAllowApiProvidedUsernameEnabled')
        self.authentication_kerberos_allow_api_provided_username_enabled = attributes[:'authenticationKerberosAllowApiProvidedUsernameEnabled']
      end

      if attributes.has_key?(:'authenticationKerberosEnabled')
        self.authentication_kerberos_enabled = attributes[:'authenticationKerberosEnabled']
      end

      if attributes.has_key?(:'authorizationLdapGroupMembershipAttributeName')
        self.authorization_ldap_group_membership_attribute_name = attributes[:'authorizationLdapGroupMembershipAttributeName']
      end

      if attributes.has_key?(:'authorizationProfileName')
        self.authorization_profile_name = attributes[:'authorizationProfileName']
      end

      if attributes.has_key?(:'authorizationType')
        self.authorization_type = attributes[:'authorizationType']
      end

      if attributes.has_key?(:'bridgingTlsServerCertEnforceTrustedCommonNameEnabled')
        self.bridging_tls_server_cert_enforce_trusted_common_name_enabled = attributes[:'bridgingTlsServerCertEnforceTrustedCommonNameEnabled']
      end

      if attributes.has_key?(:'bridgingTlsServerCertMaxChainDepth')
        self.bridging_tls_server_cert_max_chain_depth = attributes[:'bridgingTlsServerCertMaxChainDepth']
      end

      if attributes.has_key?(:'bridgingTlsServerCertValidateDateEnabled')
        self.bridging_tls_server_cert_validate_date_enabled = attributes[:'bridgingTlsServerCertValidateDateEnabled']
      end

      if attributes.has_key?(:'distributedCacheManagementEnabled')
        self.distributed_cache_management_enabled = attributes[:'distributedCacheManagementEnabled']
      end

      if attributes.has_key?(:'enabled')
        self.enabled = attributes[:'enabled']
      end

      if attributes.has_key?(:'eventConnectionCountThreshold')
        self.event_connection_count_threshold = attributes[:'eventConnectionCountThreshold']
      end

      if attributes.has_key?(:'eventEgressFlowCountThreshold')
        self.event_egress_flow_count_threshold = attributes[:'eventEgressFlowCountThreshold']
      end

      if attributes.has_key?(:'eventEgressMsgRateThreshold')
        self.event_egress_msg_rate_threshold = attributes[:'eventEgressMsgRateThreshold']
      end

      if attributes.has_key?(:'eventEndpointCountThreshold')
        self.event_endpoint_count_threshold = attributes[:'eventEndpointCountThreshold']
      end

      if attributes.has_key?(:'eventIngressFlowCountThreshold')
        self.event_ingress_flow_count_threshold = attributes[:'eventIngressFlowCountThreshold']
      end

      if attributes.has_key?(:'eventIngressMsgRateThreshold')
        self.event_ingress_msg_rate_threshold = attributes[:'eventIngressMsgRateThreshold']
      end

      if attributes.has_key?(:'eventLargeMsgThreshold')
        self.event_large_msg_threshold = attributes[:'eventLargeMsgThreshold']
      end

      if attributes.has_key?(:'eventLogTag')
        self.event_log_tag = attributes[:'eventLogTag']
      end

      if attributes.has_key?(:'eventMsgSpoolUsageThreshold')
        self.event_msg_spool_usage_threshold = attributes[:'eventMsgSpoolUsageThreshold']
      end

      if attributes.has_key?(:'eventPublishClientEnabled')
        self.event_publish_client_enabled = attributes[:'eventPublishClientEnabled']
      end

      if attributes.has_key?(:'eventPublishMsgVpnEnabled')
        self.event_publish_msg_vpn_enabled = attributes[:'eventPublishMsgVpnEnabled']
      end

      if attributes.has_key?(:'eventPublishSubscriptionMode')
        self.event_publish_subscription_mode = attributes[:'eventPublishSubscriptionMode']
      end

      if attributes.has_key?(:'eventPublishTopicFormatMqttEnabled')
        self.event_publish_topic_format_mqtt_enabled = attributes[:'eventPublishTopicFormatMqttEnabled']
      end

      if attributes.has_key?(:'eventPublishTopicFormatSmfEnabled')
        self.event_publish_topic_format_smf_enabled = attributes[:'eventPublishTopicFormatSmfEnabled']
      end

      if attributes.has_key?(:'eventServiceRestIncomingConnectionCountThreshold')
        self.event_service_rest_incoming_connection_count_threshold = attributes[:'eventServiceRestIncomingConnectionCountThreshold']
      end

      if attributes.has_key?(:'eventServiceSmfConnectionCountThreshold')
        self.event_service_smf_connection_count_threshold = attributes[:'eventServiceSmfConnectionCountThreshold']
      end

      if attributes.has_key?(:'eventServiceWebConnectionCountThreshold')
        self.event_service_web_connection_count_threshold = attributes[:'eventServiceWebConnectionCountThreshold']
      end

      if attributes.has_key?(:'eventSubscriptionCountThreshold')
        self.event_subscription_count_threshold = attributes[:'eventSubscriptionCountThreshold']
      end

      if attributes.has_key?(:'eventTransactedSessionCountThreshold')
        self.event_transacted_session_count_threshold = attributes[:'eventTransactedSessionCountThreshold']
      end

      if attributes.has_key?(:'eventTransactionCountThreshold')
        self.event_transaction_count_threshold = attributes[:'eventTransactionCountThreshold']
      end

      if attributes.has_key?(:'exportSubscriptionsEnabled')
        self.export_subscriptions_enabled = attributes[:'exportSubscriptionsEnabled']
      end

      if attributes.has_key?(:'maxConnectionCount')
        self.max_connection_count = attributes[:'maxConnectionCount']
      end

      if attributes.has_key?(:'maxEgressFlowCount')
        self.max_egress_flow_count = attributes[:'maxEgressFlowCount']
      end

      if attributes.has_key?(:'maxEndpointCount')
        self.max_endpoint_count = attributes[:'maxEndpointCount']
      end

      if attributes.has_key?(:'maxIngressFlowCount')
        self.max_ingress_flow_count = attributes[:'maxIngressFlowCount']
      end

      if attributes.has_key?(:'maxMsgSpoolUsage')
        self.max_msg_spool_usage = attributes[:'maxMsgSpoolUsage']
      end

      if attributes.has_key?(:'maxSubscriptionCount')
        self.max_subscription_count = attributes[:'maxSubscriptionCount']
      end

      if attributes.has_key?(:'maxTransactedSessionCount')
        self.max_transacted_session_count = attributes[:'maxTransactedSessionCount']
      end

      if attributes.has_key?(:'maxTransactionCount')
        self.max_transaction_count = attributes[:'maxTransactionCount']
      end

      if attributes.has_key?(:'msgVpnName')
        self.msg_vpn_name = attributes[:'msgVpnName']
      end

      if attributes.has_key?(:'replicationAckPropagationIntervalMsgCount')
        self.replication_ack_propagation_interval_msg_count = attributes[:'replicationAckPropagationIntervalMsgCount']
      end

      if attributes.has_key?(:'replicationBridgeAuthenticationBasicClientUsername')
        self.replication_bridge_authentication_basic_client_username = attributes[:'replicationBridgeAuthenticationBasicClientUsername']
      end

      if attributes.has_key?(:'replicationBridgeAuthenticationBasicPassword')
        self.replication_bridge_authentication_basic_password = attributes[:'replicationBridgeAuthenticationBasicPassword']
      end

      if attributes.has_key?(:'replicationBridgeAuthenticationScheme')
        self.replication_bridge_authentication_scheme = attributes[:'replicationBridgeAuthenticationScheme']
      end

      if attributes.has_key?(:'replicationBridgeCompressedDataEnabled')
        self.replication_bridge_compressed_data_enabled = attributes[:'replicationBridgeCompressedDataEnabled']
      end

      if attributes.has_key?(:'replicationBridgeEgressFlowWindowSize')
        self.replication_bridge_egress_flow_window_size = attributes[:'replicationBridgeEgressFlowWindowSize']
      end

      if attributes.has_key?(:'replicationBridgeRetryDelay')
        self.replication_bridge_retry_delay = attributes[:'replicationBridgeRetryDelay']
      end

      if attributes.has_key?(:'replicationBridgeTlsEnabled')
        self.replication_bridge_tls_enabled = attributes[:'replicationBridgeTlsEnabled']
      end

      if attributes.has_key?(:'replicationBridgeUnidirectionalClientProfileName')
        self.replication_bridge_unidirectional_client_profile_name = attributes[:'replicationBridgeUnidirectionalClientProfileName']
      end

      if attributes.has_key?(:'replicationEnabled')
        self.replication_enabled = attributes[:'replicationEnabled']
      end

      if attributes.has_key?(:'replicationEnabledQueueBehavior')
        self.replication_enabled_queue_behavior = attributes[:'replicationEnabledQueueBehavior']
      end

      if attributes.has_key?(:'replicationQueueMaxMsgSpoolUsage')
        self.replication_queue_max_msg_spool_usage = attributes[:'replicationQueueMaxMsgSpoolUsage']
      end

      if attributes.has_key?(:'replicationQueueRejectMsgToSenderOnDiscardEnabled')
        self.replication_queue_reject_msg_to_sender_on_discard_enabled = attributes[:'replicationQueueRejectMsgToSenderOnDiscardEnabled']
      end

      if attributes.has_key?(:'replicationRejectMsgWhenSyncIneligibleEnabled')
        self.replication_reject_msg_when_sync_ineligible_enabled = attributes[:'replicationRejectMsgWhenSyncIneligibleEnabled']
      end

      if attributes.has_key?(:'replicationRole')
        self.replication_role = attributes[:'replicationRole']
      end

      if attributes.has_key?(:'replicationTransactionMode')
        self.replication_transaction_mode = attributes[:'replicationTransactionMode']
      end

      if attributes.has_key?(:'restTlsServerCertEnforceTrustedCommonNameEnabled')
        self.rest_tls_server_cert_enforce_trusted_common_name_enabled = attributes[:'restTlsServerCertEnforceTrustedCommonNameEnabled']
      end

      if attributes.has_key?(:'restTlsServerCertMaxChainDepth')
        self.rest_tls_server_cert_max_chain_depth = attributes[:'restTlsServerCertMaxChainDepth']
      end

      if attributes.has_key?(:'restTlsServerCertValidateDateEnabled')
        self.rest_tls_server_cert_validate_date_enabled = attributes[:'restTlsServerCertValidateDateEnabled']
      end

      if attributes.has_key?(:'sempOverMsgBusAdminClientEnabled')
        self.semp_over_msg_bus_admin_client_enabled = attributes[:'sempOverMsgBusAdminClientEnabled']
      end

      if attributes.has_key?(:'sempOverMsgBusAdminDistributedCacheEnabled')
        self.semp_over_msg_bus_admin_distributed_cache_enabled = attributes[:'sempOverMsgBusAdminDistributedCacheEnabled']
      end

      if attributes.has_key?(:'sempOverMsgBusAdminEnabled')
        self.semp_over_msg_bus_admin_enabled = attributes[:'sempOverMsgBusAdminEnabled']
      end

      if attributes.has_key?(:'sempOverMsgBusEnabled')
        self.semp_over_msg_bus_enabled = attributes[:'sempOverMsgBusEnabled']
      end

      if attributes.has_key?(:'sempOverMsgBusLegacyShowClearEnabled')
        self.semp_over_msg_bus_legacy_show_clear_enabled = attributes[:'sempOverMsgBusLegacyShowClearEnabled']
      end

      if attributes.has_key?(:'sempOverMsgBusShowEnabled')
        self.semp_over_msg_bus_show_enabled = attributes[:'sempOverMsgBusShowEnabled']
      end

      if attributes.has_key?(:'serviceRestIncomingMaxConnectionCount')
        self.service_rest_incoming_max_connection_count = attributes[:'serviceRestIncomingMaxConnectionCount']
      end

      if attributes.has_key?(:'serviceRestIncomingPlainTextEnabled')
        self.service_rest_incoming_plain_text_enabled = attributes[:'serviceRestIncomingPlainTextEnabled']
      end

      if attributes.has_key?(:'serviceRestIncomingPlainTextListenPort')
        self.service_rest_incoming_plain_text_listen_port = attributes[:'serviceRestIncomingPlainTextListenPort']
      end

      if attributes.has_key?(:'serviceRestIncomingTlsEnabled')
        self.service_rest_incoming_tls_enabled = attributes[:'serviceRestIncomingTlsEnabled']
      end

      if attributes.has_key?(:'serviceRestIncomingTlsListenPort')
        self.service_rest_incoming_tls_listen_port = attributes[:'serviceRestIncomingTlsListenPort']
      end

      if attributes.has_key?(:'serviceRestOutgoingMaxConnectionCount')
        self.service_rest_outgoing_max_connection_count = attributes[:'serviceRestOutgoingMaxConnectionCount']
      end

      if attributes.has_key?(:'serviceSmfMaxConnectionCount')
        self.service_smf_max_connection_count = attributes[:'serviceSmfMaxConnectionCount']
      end

      if attributes.has_key?(:'serviceSmfPlainTextEnabled')
        self.service_smf_plain_text_enabled = attributes[:'serviceSmfPlainTextEnabled']
      end

      if attributes.has_key?(:'serviceSmfTlsEnabled')
        self.service_smf_tls_enabled = attributes[:'serviceSmfTlsEnabled']
      end

      if attributes.has_key?(:'serviceWebMaxConnectionCount')
        self.service_web_max_connection_count = attributes[:'serviceWebMaxConnectionCount']
      end

      if attributes.has_key?(:'serviceWebPlainTextEnabled')
        self.service_web_plain_text_enabled = attributes[:'serviceWebPlainTextEnabled']
      end

      if attributes.has_key?(:'serviceWebTlsEnabled')
        self.service_web_tls_enabled = attributes[:'serviceWebTlsEnabled']
      end

      if attributes.has_key?(:'tlsAllowDowngradeToPlainTextEnabled')
        self.tls_allow_downgrade_to_plain_text_enabled = attributes[:'tlsAllowDowngradeToPlainTextEnabled']
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
      authentication_basic_type_validator = EnumAttributeValidator.new('String', ["radius", "ldap", "internal", "none"])
      return false unless authentication_basic_type_validator.valid?(@authentication_basic_type)
      authorization_type_validator = EnumAttributeValidator.new('String', ["ldap", "internal"])
      return false unless authorization_type_validator.valid?(@authorization_type)
      event_publish_subscription_mode_validator = EnumAttributeValidator.new('String', ["off", "on-with-format-v1", "on-with-no-unsubscribe-events-on-disconnect-format-v1", "on-with-format-v2", "on-with-no-unsubscribe-events-on-disconnect-format-v2"])
      return false unless event_publish_subscription_mode_validator.valid?(@event_publish_subscription_mode)
      replication_bridge_authentication_scheme_validator = EnumAttributeValidator.new('String', ["basic", "client-certificate"])
      return false unless replication_bridge_authentication_scheme_validator.valid?(@replication_bridge_authentication_scheme)
      replication_enabled_queue_behavior_validator = EnumAttributeValidator.new('String', ["fail-on-existing-queue", "force-use-existing-queue", "force-recreate-queue"])
      return false unless replication_enabled_queue_behavior_validator.valid?(@replication_enabled_queue_behavior)
      replication_role_validator = EnumAttributeValidator.new('String', ["active", "standby"])
      return false unless replication_role_validator.valid?(@replication_role)
      replication_transaction_mode_validator = EnumAttributeValidator.new('String', ["sync", "async"])
      return false unless replication_transaction_mode_validator.valid?(@replication_transaction_mode)
      return true
    end

    # Custom attribute writer method checking allowed values (enum).
    # @param [Object] authentication_basic_type Object to be assigned
    def authentication_basic_type=(authentication_basic_type)
      validator = EnumAttributeValidator.new('String', ["radius", "ldap", "internal", "none"])
      unless validator.valid?(authentication_basic_type)
        fail ArgumentError, "invalid value for 'authentication_basic_type', must be one of #{validator.allowable_values}."
      end
      @authentication_basic_type = authentication_basic_type
    end

    # Custom attribute writer method checking allowed values (enum).
    # @param [Object] authorization_type Object to be assigned
    def authorization_type=(authorization_type)
      validator = EnumAttributeValidator.new('String', ["ldap", "internal"])
      unless validator.valid?(authorization_type)
        fail ArgumentError, "invalid value for 'authorization_type', must be one of #{validator.allowable_values}."
      end
      @authorization_type = authorization_type
    end

    # Custom attribute writer method checking allowed values (enum).
    # @param [Object] event_publish_subscription_mode Object to be assigned
    def event_publish_subscription_mode=(event_publish_subscription_mode)
      validator = EnumAttributeValidator.new('String', ["off", "on-with-format-v1", "on-with-no-unsubscribe-events-on-disconnect-format-v1", "on-with-format-v2", "on-with-no-unsubscribe-events-on-disconnect-format-v2"])
      unless validator.valid?(event_publish_subscription_mode)
        fail ArgumentError, "invalid value for 'event_publish_subscription_mode', must be one of #{validator.allowable_values}."
      end
      @event_publish_subscription_mode = event_publish_subscription_mode
    end

    # Custom attribute writer method checking allowed values (enum).
    # @param [Object] replication_bridge_authentication_scheme Object to be assigned
    def replication_bridge_authentication_scheme=(replication_bridge_authentication_scheme)
      validator = EnumAttributeValidator.new('String', ["basic", "client-certificate"])
      unless validator.valid?(replication_bridge_authentication_scheme)
        fail ArgumentError, "invalid value for 'replication_bridge_authentication_scheme', must be one of #{validator.allowable_values}."
      end
      @replication_bridge_authentication_scheme = replication_bridge_authentication_scheme
    end

    # Custom attribute writer method checking allowed values (enum).
    # @param [Object] replication_enabled_queue_behavior Object to be assigned
    def replication_enabled_queue_behavior=(replication_enabled_queue_behavior)
      validator = EnumAttributeValidator.new('String', ["fail-on-existing-queue", "force-use-existing-queue", "force-recreate-queue"])
      unless validator.valid?(replication_enabled_queue_behavior)
        fail ArgumentError, "invalid value for 'replication_enabled_queue_behavior', must be one of #{validator.allowable_values}."
      end
      @replication_enabled_queue_behavior = replication_enabled_queue_behavior
    end

    # Custom attribute writer method checking allowed values (enum).
    # @param [Object] replication_role Object to be assigned
    def replication_role=(replication_role)
      validator = EnumAttributeValidator.new('String', ["active", "standby"])
      unless validator.valid?(replication_role)
        fail ArgumentError, "invalid value for 'replication_role', must be one of #{validator.allowable_values}."
      end
      @replication_role = replication_role
    end

    # Custom attribute writer method checking allowed values (enum).
    # @param [Object] replication_transaction_mode Object to be assigned
    def replication_transaction_mode=(replication_transaction_mode)
      validator = EnumAttributeValidator.new('String', ["sync", "async"])
      unless validator.valid?(replication_transaction_mode)
        fail ArgumentError, "invalid value for 'replication_transaction_mode', must be one of #{validator.allowable_values}."
      end
      @replication_transaction_mode = replication_transaction_mode
    end

    # Checks equality by comparing each attribute.
    # @param [Object] Object to be compared
    def ==(o)
      return true if self.equal?(o)
      self.class == o.class &&
          authentication_basic_enabled == o.authentication_basic_enabled &&
          authentication_basic_profile_name == o.authentication_basic_profile_name &&
          authentication_basic_radius_domain == o.authentication_basic_radius_domain &&
          authentication_basic_type == o.authentication_basic_type &&
          authentication_client_cert_allow_api_provided_username_enabled == o.authentication_client_cert_allow_api_provided_username_enabled &&
          authentication_client_cert_enabled == o.authentication_client_cert_enabled &&
          authentication_client_cert_max_chain_depth == o.authentication_client_cert_max_chain_depth &&
          authentication_client_cert_validate_date_enabled == o.authentication_client_cert_validate_date_enabled &&
          authentication_kerberos_allow_api_provided_username_enabled == o.authentication_kerberos_allow_api_provided_username_enabled &&
          authentication_kerberos_enabled == o.authentication_kerberos_enabled &&
          authorization_ldap_group_membership_attribute_name == o.authorization_ldap_group_membership_attribute_name &&
          authorization_profile_name == o.authorization_profile_name &&
          authorization_type == o.authorization_type &&
          bridging_tls_server_cert_enforce_trusted_common_name_enabled == o.bridging_tls_server_cert_enforce_trusted_common_name_enabled &&
          bridging_tls_server_cert_max_chain_depth == o.bridging_tls_server_cert_max_chain_depth &&
          bridging_tls_server_cert_validate_date_enabled == o.bridging_tls_server_cert_validate_date_enabled &&
          distributed_cache_management_enabled == o.distributed_cache_management_enabled &&
          enabled == o.enabled &&
          event_connection_count_threshold == o.event_connection_count_threshold &&
          event_egress_flow_count_threshold == o.event_egress_flow_count_threshold &&
          event_egress_msg_rate_threshold == o.event_egress_msg_rate_threshold &&
          event_endpoint_count_threshold == o.event_endpoint_count_threshold &&
          event_ingress_flow_count_threshold == o.event_ingress_flow_count_threshold &&
          event_ingress_msg_rate_threshold == o.event_ingress_msg_rate_threshold &&
          event_large_msg_threshold == o.event_large_msg_threshold &&
          event_log_tag == o.event_log_tag &&
          event_msg_spool_usage_threshold == o.event_msg_spool_usage_threshold &&
          event_publish_client_enabled == o.event_publish_client_enabled &&
          event_publish_msg_vpn_enabled == o.event_publish_msg_vpn_enabled &&
          event_publish_subscription_mode == o.event_publish_subscription_mode &&
          event_publish_topic_format_mqtt_enabled == o.event_publish_topic_format_mqtt_enabled &&
          event_publish_topic_format_smf_enabled == o.event_publish_topic_format_smf_enabled &&
          event_service_rest_incoming_connection_count_threshold == o.event_service_rest_incoming_connection_count_threshold &&
          event_service_smf_connection_count_threshold == o.event_service_smf_connection_count_threshold &&
          event_service_web_connection_count_threshold == o.event_service_web_connection_count_threshold &&
          event_subscription_count_threshold == o.event_subscription_count_threshold &&
          event_transacted_session_count_threshold == o.event_transacted_session_count_threshold &&
          event_transaction_count_threshold == o.event_transaction_count_threshold &&
          export_subscriptions_enabled == o.export_subscriptions_enabled &&
          max_connection_count == o.max_connection_count &&
          max_egress_flow_count == o.max_egress_flow_count &&
          max_endpoint_count == o.max_endpoint_count &&
          max_ingress_flow_count == o.max_ingress_flow_count &&
          max_msg_spool_usage == o.max_msg_spool_usage &&
          max_subscription_count == o.max_subscription_count &&
          max_transacted_session_count == o.max_transacted_session_count &&
          max_transaction_count == o.max_transaction_count &&
          msg_vpn_name == o.msg_vpn_name &&
          replication_ack_propagation_interval_msg_count == o.replication_ack_propagation_interval_msg_count &&
          replication_bridge_authentication_basic_client_username == o.replication_bridge_authentication_basic_client_username &&
          replication_bridge_authentication_basic_password == o.replication_bridge_authentication_basic_password &&
          replication_bridge_authentication_scheme == o.replication_bridge_authentication_scheme &&
          replication_bridge_compressed_data_enabled == o.replication_bridge_compressed_data_enabled &&
          replication_bridge_egress_flow_window_size == o.replication_bridge_egress_flow_window_size &&
          replication_bridge_retry_delay == o.replication_bridge_retry_delay &&
          replication_bridge_tls_enabled == o.replication_bridge_tls_enabled &&
          replication_bridge_unidirectional_client_profile_name == o.replication_bridge_unidirectional_client_profile_name &&
          replication_enabled == o.replication_enabled &&
          replication_enabled_queue_behavior == o.replication_enabled_queue_behavior &&
          replication_queue_max_msg_spool_usage == o.replication_queue_max_msg_spool_usage &&
          replication_queue_reject_msg_to_sender_on_discard_enabled == o.replication_queue_reject_msg_to_sender_on_discard_enabled &&
          replication_reject_msg_when_sync_ineligible_enabled == o.replication_reject_msg_when_sync_ineligible_enabled &&
          replication_role == o.replication_role &&
          replication_transaction_mode == o.replication_transaction_mode &&
          rest_tls_server_cert_enforce_trusted_common_name_enabled == o.rest_tls_server_cert_enforce_trusted_common_name_enabled &&
          rest_tls_server_cert_max_chain_depth == o.rest_tls_server_cert_max_chain_depth &&
          rest_tls_server_cert_validate_date_enabled == o.rest_tls_server_cert_validate_date_enabled &&
          semp_over_msg_bus_admin_client_enabled == o.semp_over_msg_bus_admin_client_enabled &&
          semp_over_msg_bus_admin_distributed_cache_enabled == o.semp_over_msg_bus_admin_distributed_cache_enabled &&
          semp_over_msg_bus_admin_enabled == o.semp_over_msg_bus_admin_enabled &&
          semp_over_msg_bus_enabled == o.semp_over_msg_bus_enabled &&
          semp_over_msg_bus_legacy_show_clear_enabled == o.semp_over_msg_bus_legacy_show_clear_enabled &&
          semp_over_msg_bus_show_enabled == o.semp_over_msg_bus_show_enabled &&
          service_rest_incoming_max_connection_count == o.service_rest_incoming_max_connection_count &&
          service_rest_incoming_plain_text_enabled == o.service_rest_incoming_plain_text_enabled &&
          service_rest_incoming_plain_text_listen_port == o.service_rest_incoming_plain_text_listen_port &&
          service_rest_incoming_tls_enabled == o.service_rest_incoming_tls_enabled &&
          service_rest_incoming_tls_listen_port == o.service_rest_incoming_tls_listen_port &&
          service_rest_outgoing_max_connection_count == o.service_rest_outgoing_max_connection_count &&
          service_smf_max_connection_count == o.service_smf_max_connection_count &&
          service_smf_plain_text_enabled == o.service_smf_plain_text_enabled &&
          service_smf_tls_enabled == o.service_smf_tls_enabled &&
          service_web_max_connection_count == o.service_web_max_connection_count &&
          service_web_plain_text_enabled == o.service_web_plain_text_enabled &&
          service_web_tls_enabled == o.service_web_tls_enabled &&
          tls_allow_downgrade_to_plain_text_enabled == o.tls_allow_downgrade_to_plain_text_enabled
    end

    # @see the `==` method
    # @param [Object] Object to be compared
    def eql?(o)
      self == o
    end

    # Calculates hash code according to all attributes.
    # @return [Fixnum] Hash code
    def hash
      [authentication_basic_enabled, authentication_basic_profile_name, authentication_basic_radius_domain, authentication_basic_type, authentication_client_cert_allow_api_provided_username_enabled, authentication_client_cert_enabled, authentication_client_cert_max_chain_depth, authentication_client_cert_validate_date_enabled, authentication_kerberos_allow_api_provided_username_enabled, authentication_kerberos_enabled, authorization_ldap_group_membership_attribute_name, authorization_profile_name, authorization_type, bridging_tls_server_cert_enforce_trusted_common_name_enabled, bridging_tls_server_cert_max_chain_depth, bridging_tls_server_cert_validate_date_enabled, distributed_cache_management_enabled, enabled, event_connection_count_threshold, event_egress_flow_count_threshold, event_egress_msg_rate_threshold, event_endpoint_count_threshold, event_ingress_flow_count_threshold, event_ingress_msg_rate_threshold, event_large_msg_threshold, event_log_tag, event_msg_spool_usage_threshold, event_publish_client_enabled, event_publish_msg_vpn_enabled, event_publish_subscription_mode, event_publish_topic_format_mqtt_enabled, event_publish_topic_format_smf_enabled, event_service_rest_incoming_connection_count_threshold, event_service_smf_connection_count_threshold, event_service_web_connection_count_threshold, event_subscription_count_threshold, event_transacted_session_count_threshold, event_transaction_count_threshold, export_subscriptions_enabled, max_connection_count, max_egress_flow_count, max_endpoint_count, max_ingress_flow_count, max_msg_spool_usage, max_subscription_count, max_transacted_session_count, max_transaction_count, msg_vpn_name, replication_ack_propagation_interval_msg_count, replication_bridge_authentication_basic_client_username, replication_bridge_authentication_basic_password, replication_bridge_authentication_scheme, replication_bridge_compressed_data_enabled, replication_bridge_egress_flow_window_size, replication_bridge_retry_delay, replication_bridge_tls_enabled, replication_bridge_unidirectional_client_profile_name, replication_enabled, replication_enabled_queue_behavior, replication_queue_max_msg_spool_usage, replication_queue_reject_msg_to_sender_on_discard_enabled, replication_reject_msg_when_sync_ineligible_enabled, replication_role, replication_transaction_mode, rest_tls_server_cert_enforce_trusted_common_name_enabled, rest_tls_server_cert_max_chain_depth, rest_tls_server_cert_validate_date_enabled, semp_over_msg_bus_admin_client_enabled, semp_over_msg_bus_admin_distributed_cache_enabled, semp_over_msg_bus_admin_enabled, semp_over_msg_bus_enabled, semp_over_msg_bus_legacy_show_clear_enabled, semp_over_msg_bus_show_enabled, service_rest_incoming_max_connection_count, service_rest_incoming_plain_text_enabled, service_rest_incoming_plain_text_listen_port, service_rest_incoming_tls_enabled, service_rest_incoming_tls_listen_port, service_rest_outgoing_max_connection_count, service_smf_max_connection_count, service_smf_plain_text_enabled, service_smf_tls_enabled, service_web_max_connection_count, service_web_plain_text_enabled, service_web_tls_enabled, tls_allow_downgrade_to_plain_text_enabled].hash
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
