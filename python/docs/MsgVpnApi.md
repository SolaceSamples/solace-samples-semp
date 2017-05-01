# semp_client.MsgVpnApi

All URIs are relative to *http://localhost/SEMP/v2/config*

Method | HTTP request | Description
------------- | ------------- | -------------
[**create_msg_vpn**](MsgVpnApi.md#create_msg_vpn) | **POST** /msgVpns | Creates a Message VPN object.
[**create_msg_vpn_acl_profile**](MsgVpnApi.md#create_msg_vpn_acl_profile) | **POST** /msgVpns/{msgVpnName}/aclProfiles | Creates an ACL Profile object.
[**create_msg_vpn_acl_profile_client_connect_exception**](MsgVpnApi.md#create_msg_vpn_acl_profile_client_connect_exception) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions | Creates a Client Connect Exception object.
[**create_msg_vpn_acl_profile_publish_exception**](MsgVpnApi.md#create_msg_vpn_acl_profile_publish_exception) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions | Creates a Publish Topic Exception object.
[**create_msg_vpn_acl_profile_subscribe_exception**](MsgVpnApi.md#create_msg_vpn_acl_profile_subscribe_exception) | **POST** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions | Creates a Subscribe Topic Exception object.
[**create_msg_vpn_authorization_group**](MsgVpnApi.md#create_msg_vpn_authorization_group) | **POST** /msgVpns/{msgVpnName}/authorizationGroups | Creates an LDAP Authorization Group object.
[**create_msg_vpn_bridge**](MsgVpnApi.md#create_msg_vpn_bridge) | **POST** /msgVpns/{msgVpnName}/bridges | Creates a Bridge object.
[**create_msg_vpn_bridge_remote_msg_vpn**](MsgVpnApi.md#create_msg_vpn_bridge_remote_msg_vpn) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Creates a Remote Message VPN object.
[**create_msg_vpn_bridge_remote_subscription**](MsgVpnApi.md#create_msg_vpn_bridge_remote_subscription) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Creates a Remote Subscription object.
[**create_msg_vpn_bridge_tls_trusted_common_name**](MsgVpnApi.md#create_msg_vpn_bridge_tls_trusted_common_name) | **POST** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Creates a Trusted Common Name object.
[**create_msg_vpn_client_profile**](MsgVpnApi.md#create_msg_vpn_client_profile) | **POST** /msgVpns/{msgVpnName}/clientProfiles | Creates a Client Profile object.
[**create_msg_vpn_client_username**](MsgVpnApi.md#create_msg_vpn_client_username) | **POST** /msgVpns/{msgVpnName}/clientUsernames | Creates a Client Username object.
[**create_msg_vpn_mqtt_session**](MsgVpnApi.md#create_msg_vpn_mqtt_session) | **POST** /msgVpns/{msgVpnName}/mqttSessions | Creates an MQTT Session object.
[**create_msg_vpn_mqtt_session_subscription**](MsgVpnApi.md#create_msg_vpn_mqtt_session_subscription) | **POST** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions | Creates an MQTT Session Subscription object.
[**create_msg_vpn_queue**](MsgVpnApi.md#create_msg_vpn_queue) | **POST** /msgVpns/{msgVpnName}/queues | Creates a Queue object.
[**create_msg_vpn_queue_subscription**](MsgVpnApi.md#create_msg_vpn_queue_subscription) | **POST** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions | Creates a Queue Subscription object.
[**create_msg_vpn_replicated_topic**](MsgVpnApi.md#create_msg_vpn_replicated_topic) | **POST** /msgVpns/{msgVpnName}/replicatedTopics | Creates a Replicated Topic object.
[**create_msg_vpn_rest_delivery_point**](MsgVpnApi.md#create_msg_vpn_rest_delivery_point) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints | Creates a REST Delivery Point object.
[**create_msg_vpn_rest_delivery_point_queue_binding**](MsgVpnApi.md#create_msg_vpn_rest_delivery_point_queue_binding) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings | Creates a Queue Binding object.
[**create_msg_vpn_rest_delivery_point_rest_consumer**](MsgVpnApi.md#create_msg_vpn_rest_delivery_point_rest_consumer) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers | Creates a REST Consumer object.
[**create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**](MsgVpnApi.md#create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name) | **POST** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames | Creates a Trusted Common Name object.
[**create_msg_vpn_sequenced_topic**](MsgVpnApi.md#create_msg_vpn_sequenced_topic) | **POST** /msgVpns/{msgVpnName}/sequencedTopics | Creates a Sequenced Topic object.
[**create_msg_vpn_topic_endpoint**](MsgVpnApi.md#create_msg_vpn_topic_endpoint) | **POST** /msgVpns/{msgVpnName}/topicEndpoints | Creates a Topic Endpoint object.
[**delete_msg_vpn**](MsgVpnApi.md#delete_msg_vpn) | **DELETE** /msgVpns/{msgVpnName} | Deletes a Message VPN object.
[**delete_msg_vpn_acl_profile**](MsgVpnApi.md#delete_msg_vpn_acl_profile) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Deletes an ACL Profile object.
[**delete_msg_vpn_acl_profile_client_connect_exception**](MsgVpnApi.md#delete_msg_vpn_acl_profile_client_connect_exception) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress} | Deletes a Client Connect Exception object.
[**delete_msg_vpn_acl_profile_publish_exception**](MsgVpnApi.md#delete_msg_vpn_acl_profile_publish_exception) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic} | Deletes a Publish Topic Exception object.
[**delete_msg_vpn_acl_profile_subscribe_exception**](MsgVpnApi.md#delete_msg_vpn_acl_profile_subscribe_exception) | **DELETE** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic} | Deletes a Subscribe Topic Exception object.
[**delete_msg_vpn_authorization_group**](MsgVpnApi.md#delete_msg_vpn_authorization_group) | **DELETE** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Deletes an LDAP Authorization Group object.
[**delete_msg_vpn_bridge**](MsgVpnApi.md#delete_msg_vpn_bridge) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Deletes a Bridge object.
[**delete_msg_vpn_bridge_remote_msg_vpn**](MsgVpnApi.md#delete_msg_vpn_bridge_remote_msg_vpn) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Deletes a Remote Message VPN object.
[**delete_msg_vpn_bridge_remote_subscription**](MsgVpnApi.md#delete_msg_vpn_bridge_remote_subscription) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Deletes a Remote Subscription object.
[**delete_msg_vpn_bridge_tls_trusted_common_name**](MsgVpnApi.md#delete_msg_vpn_bridge_tls_trusted_common_name) | **DELETE** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Deletes a Trusted Common Name object.
[**delete_msg_vpn_client_profile**](MsgVpnApi.md#delete_msg_vpn_client_profile) | **DELETE** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Deletes a Client Profile object.
[**delete_msg_vpn_client_username**](MsgVpnApi.md#delete_msg_vpn_client_username) | **DELETE** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Deletes a Client Username object.
[**delete_msg_vpn_mqtt_session**](MsgVpnApi.md#delete_msg_vpn_mqtt_session) | **DELETE** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Deletes an MQTT Session object.
[**delete_msg_vpn_mqtt_session_subscription**](MsgVpnApi.md#delete_msg_vpn_mqtt_session_subscription) | **DELETE** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Deletes an MQTT Session Subscription object.
[**delete_msg_vpn_queue**](MsgVpnApi.md#delete_msg_vpn_queue) | **DELETE** /msgVpns/{msgVpnName}/queues/{queueName} | Deletes a Queue object.
[**delete_msg_vpn_queue_subscription**](MsgVpnApi.md#delete_msg_vpn_queue_subscription) | **DELETE** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic} | Deletes a Queue Subscription object.
[**delete_msg_vpn_replicated_topic**](MsgVpnApi.md#delete_msg_vpn_replicated_topic) | **DELETE** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Deletes a Replicated Topic object.
[**delete_msg_vpn_rest_delivery_point**](MsgVpnApi.md#delete_msg_vpn_rest_delivery_point) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Deletes a REST Delivery Point object.
[**delete_msg_vpn_rest_delivery_point_queue_binding**](MsgVpnApi.md#delete_msg_vpn_rest_delivery_point_queue_binding) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Deletes a Queue Binding object.
[**delete_msg_vpn_rest_delivery_point_rest_consumer**](MsgVpnApi.md#delete_msg_vpn_rest_delivery_point_rest_consumer) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Deletes a REST Consumer object.
[**delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**](MsgVpnApi.md#delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name) | **DELETE** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Deletes a Trusted Common Name object.
[**delete_msg_vpn_sequenced_topic**](MsgVpnApi.md#delete_msg_vpn_sequenced_topic) | **DELETE** /msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic} | Deletes a Sequenced Topic object.
[**delete_msg_vpn_topic_endpoint**](MsgVpnApi.md#delete_msg_vpn_topic_endpoint) | **DELETE** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Deletes a Topic Endpoint object.
[**get_msg_vpn**](MsgVpnApi.md#get_msg_vpn) | **GET** /msgVpns/{msgVpnName} | Gets a Message VPN object.
[**get_msg_vpn_acl_profile**](MsgVpnApi.md#get_msg_vpn_acl_profile) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Gets an ACL Profile object.
[**get_msg_vpn_acl_profile_client_connect_exception**](MsgVpnApi.md#get_msg_vpn_acl_profile_client_connect_exception) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions/{clientConnectExceptionAddress} | Gets a Client Connect Exception object.
[**get_msg_vpn_acl_profile_client_connect_exceptions**](MsgVpnApi.md#get_msg_vpn_acl_profile_client_connect_exceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/clientConnectExceptions | Gets a list of Client Connect Exception objects.
[**get_msg_vpn_acl_profile_publish_exception**](MsgVpnApi.md#get_msg_vpn_acl_profile_publish_exception) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions/{topicSyntax},{publishExceptionTopic} | Gets a Publish Topic Exception object.
[**get_msg_vpn_acl_profile_publish_exceptions**](MsgVpnApi.md#get_msg_vpn_acl_profile_publish_exceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/publishExceptions | Gets a list of Publish Topic Exception objects.
[**get_msg_vpn_acl_profile_subscribe_exception**](MsgVpnApi.md#get_msg_vpn_acl_profile_subscribe_exception) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions/{topicSyntax},{subscribeExceptionTopic} | Gets a Subscribe Topic Exception object.
[**get_msg_vpn_acl_profile_subscribe_exceptions**](MsgVpnApi.md#get_msg_vpn_acl_profile_subscribe_exceptions) | **GET** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName}/subscribeExceptions | Gets a list of Subscribe Topic Exception objects.
[**get_msg_vpn_acl_profiles**](MsgVpnApi.md#get_msg_vpn_acl_profiles) | **GET** /msgVpns/{msgVpnName}/aclProfiles | Gets a list of ACL Profile objects.
[**get_msg_vpn_authorization_group**](MsgVpnApi.md#get_msg_vpn_authorization_group) | **GET** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Gets an LDAP Authorization Group object.
[**get_msg_vpn_authorization_groups**](MsgVpnApi.md#get_msg_vpn_authorization_groups) | **GET** /msgVpns/{msgVpnName}/authorizationGroups | Gets a list of LDAP Authorization Group objects.
[**get_msg_vpn_bridge**](MsgVpnApi.md#get_msg_vpn_bridge) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Gets a Bridge object.
[**get_msg_vpn_bridge_remote_msg_vpn**](MsgVpnApi.md#get_msg_vpn_bridge_remote_msg_vpn) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Gets a Remote Message VPN object.
[**get_msg_vpn_bridge_remote_msg_vpns**](MsgVpnApi.md#get_msg_vpn_bridge_remote_msg_vpns) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns | Gets a list of Remote Message VPN objects.
[**get_msg_vpn_bridge_remote_subscription**](MsgVpnApi.md#get_msg_vpn_bridge_remote_subscription) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions/{remoteSubscriptionTopic} | Gets a Remote Subscription object.
[**get_msg_vpn_bridge_remote_subscriptions**](MsgVpnApi.md#get_msg_vpn_bridge_remote_subscriptions) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteSubscriptions | Gets a list of Remote Subscription objects.
[**get_msg_vpn_bridge_tls_trusted_common_name**](MsgVpnApi.md#get_msg_vpn_bridge_tls_trusted_common_name) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Gets a Trusted Common Name object.
[**get_msg_vpn_bridge_tls_trusted_common_names**](MsgVpnApi.md#get_msg_vpn_bridge_tls_trusted_common_names) | **GET** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/tlsTrustedCommonNames | Gets a list of Trusted Common Name objects.
[**get_msg_vpn_bridges**](MsgVpnApi.md#get_msg_vpn_bridges) | **GET** /msgVpns/{msgVpnName}/bridges | Gets a list of Bridge objects.
[**get_msg_vpn_client_profile**](MsgVpnApi.md#get_msg_vpn_client_profile) | **GET** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Gets a Client Profile object.
[**get_msg_vpn_client_profiles**](MsgVpnApi.md#get_msg_vpn_client_profiles) | **GET** /msgVpns/{msgVpnName}/clientProfiles | Gets a list of Client Profile objects.
[**get_msg_vpn_client_username**](MsgVpnApi.md#get_msg_vpn_client_username) | **GET** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Gets a Client Username object.
[**get_msg_vpn_client_usernames**](MsgVpnApi.md#get_msg_vpn_client_usernames) | **GET** /msgVpns/{msgVpnName}/clientUsernames | Gets a list of Client Username objects.
[**get_msg_vpn_mqtt_session**](MsgVpnApi.md#get_msg_vpn_mqtt_session) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Gets an MQTT Session object.
[**get_msg_vpn_mqtt_session_subscription**](MsgVpnApi.md#get_msg_vpn_mqtt_session_subscription) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Gets an MQTT Session Subscription object.
[**get_msg_vpn_mqtt_session_subscriptions**](MsgVpnApi.md#get_msg_vpn_mqtt_session_subscriptions) | **GET** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions | Gets a list of MQTT Session Subscription objects.
[**get_msg_vpn_mqtt_sessions**](MsgVpnApi.md#get_msg_vpn_mqtt_sessions) | **GET** /msgVpns/{msgVpnName}/mqttSessions | Gets a list of MQTT Session objects.
[**get_msg_vpn_queue**](MsgVpnApi.md#get_msg_vpn_queue) | **GET** /msgVpns/{msgVpnName}/queues/{queueName} | Gets a Queue object.
[**get_msg_vpn_queue_subscription**](MsgVpnApi.md#get_msg_vpn_queue_subscription) | **GET** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions/{subscriptionTopic} | Gets a Queue Subscription object.
[**get_msg_vpn_queue_subscriptions**](MsgVpnApi.md#get_msg_vpn_queue_subscriptions) | **GET** /msgVpns/{msgVpnName}/queues/{queueName}/subscriptions | Gets a list of Queue Subscription objects.
[**get_msg_vpn_queues**](MsgVpnApi.md#get_msg_vpn_queues) | **GET** /msgVpns/{msgVpnName}/queues | Gets a list of Queue objects.
[**get_msg_vpn_replicated_topic**](MsgVpnApi.md#get_msg_vpn_replicated_topic) | **GET** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Gets a Replicated Topic object.
[**get_msg_vpn_replicated_topics**](MsgVpnApi.md#get_msg_vpn_replicated_topics) | **GET** /msgVpns/{msgVpnName}/replicatedTopics | Gets a list of Replicated Topic objects.
[**get_msg_vpn_rest_delivery_point**](MsgVpnApi.md#get_msg_vpn_rest_delivery_point) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Gets a REST Delivery Point object.
[**get_msg_vpn_rest_delivery_point_queue_binding**](MsgVpnApi.md#get_msg_vpn_rest_delivery_point_queue_binding) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Gets a Queue Binding object.
[**get_msg_vpn_rest_delivery_point_queue_bindings**](MsgVpnApi.md#get_msg_vpn_rest_delivery_point_queue_bindings) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings | Gets a list of Queue Binding objects.
[**get_msg_vpn_rest_delivery_point_rest_consumer**](MsgVpnApi.md#get_msg_vpn_rest_delivery_point_rest_consumer) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Gets a REST Consumer object.
[**get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**](MsgVpnApi.md#get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames/{tlsTrustedCommonName} | Gets a Trusted Common Name object.
[**get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names**](MsgVpnApi.md#get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName}/tlsTrustedCommonNames | Gets a list of Trusted Common Name objects.
[**get_msg_vpn_rest_delivery_point_rest_consumers**](MsgVpnApi.md#get_msg_vpn_rest_delivery_point_rest_consumers) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers | Gets a list of REST Consumer objects.
[**get_msg_vpn_rest_delivery_points**](MsgVpnApi.md#get_msg_vpn_rest_delivery_points) | **GET** /msgVpns/{msgVpnName}/restDeliveryPoints | Gets a list of REST Delivery Point objects.
[**get_msg_vpn_sequenced_topic**](MsgVpnApi.md#get_msg_vpn_sequenced_topic) | **GET** /msgVpns/{msgVpnName}/sequencedTopics/{sequencedTopic} | Gets a Sequenced Topic object.
[**get_msg_vpn_sequenced_topics**](MsgVpnApi.md#get_msg_vpn_sequenced_topics) | **GET** /msgVpns/{msgVpnName}/sequencedTopics | Gets a list of Sequenced Topic objects.
[**get_msg_vpn_topic_endpoint**](MsgVpnApi.md#get_msg_vpn_topic_endpoint) | **GET** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Gets a Topic Endpoint object.
[**get_msg_vpn_topic_endpoints**](MsgVpnApi.md#get_msg_vpn_topic_endpoints) | **GET** /msgVpns/{msgVpnName}/topicEndpoints | Gets a list of Topic Endpoint objects.
[**get_msg_vpns**](MsgVpnApi.md#get_msg_vpns) | **GET** /msgVpns | Gets a list of Message VPN objects.
[**replace_msg_vpn**](MsgVpnApi.md#replace_msg_vpn) | **PUT** /msgVpns/{msgVpnName} | Replaces a Message VPN object.
[**replace_msg_vpn_acl_profile**](MsgVpnApi.md#replace_msg_vpn_acl_profile) | **PUT** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Replaces an ACL Profile object.
[**replace_msg_vpn_authorization_group**](MsgVpnApi.md#replace_msg_vpn_authorization_group) | **PUT** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Replaces an LDAP Authorization Group object.
[**replace_msg_vpn_bridge**](MsgVpnApi.md#replace_msg_vpn_bridge) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Replaces a Bridge object.
[**replace_msg_vpn_bridge_remote_msg_vpn**](MsgVpnApi.md#replace_msg_vpn_bridge_remote_msg_vpn) | **PUT** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Replaces a Remote Message VPN object.
[**replace_msg_vpn_client_profile**](MsgVpnApi.md#replace_msg_vpn_client_profile) | **PUT** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Replaces a Client Profile object.
[**replace_msg_vpn_client_username**](MsgVpnApi.md#replace_msg_vpn_client_username) | **PUT** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Replaces a Client Username object.
[**replace_msg_vpn_mqtt_session**](MsgVpnApi.md#replace_msg_vpn_mqtt_session) | **PUT** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Replaces an MQTT Session object.
[**replace_msg_vpn_mqtt_session_subscription**](MsgVpnApi.md#replace_msg_vpn_mqtt_session_subscription) | **PUT** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Replaces an MQTT Session Subscription object.
[**replace_msg_vpn_queue**](MsgVpnApi.md#replace_msg_vpn_queue) | **PUT** /msgVpns/{msgVpnName}/queues/{queueName} | Replaces a Queue object.
[**replace_msg_vpn_replicated_topic**](MsgVpnApi.md#replace_msg_vpn_replicated_topic) | **PUT** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Replaces a Replicated Topic object.
[**replace_msg_vpn_rest_delivery_point**](MsgVpnApi.md#replace_msg_vpn_rest_delivery_point) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Replaces a REST Delivery Point object.
[**replace_msg_vpn_rest_delivery_point_queue_binding**](MsgVpnApi.md#replace_msg_vpn_rest_delivery_point_queue_binding) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Replaces a Queue Binding object.
[**replace_msg_vpn_rest_delivery_point_rest_consumer**](MsgVpnApi.md#replace_msg_vpn_rest_delivery_point_rest_consumer) | **PUT** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Replaces a REST Consumer object.
[**replace_msg_vpn_topic_endpoint**](MsgVpnApi.md#replace_msg_vpn_topic_endpoint) | **PUT** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Replaces a Topic Endpoint object.
[**update_msg_vpn**](MsgVpnApi.md#update_msg_vpn) | **PATCH** /msgVpns/{msgVpnName} | Updates a Message VPN object.
[**update_msg_vpn_acl_profile**](MsgVpnApi.md#update_msg_vpn_acl_profile) | **PATCH** /msgVpns/{msgVpnName}/aclProfiles/{aclProfileName} | Updates an ACL Profile object.
[**update_msg_vpn_authorization_group**](MsgVpnApi.md#update_msg_vpn_authorization_group) | **PATCH** /msgVpns/{msgVpnName}/authorizationGroups/{authorizationGroupName} | Updates an LDAP Authorization Group object.
[**update_msg_vpn_bridge**](MsgVpnApi.md#update_msg_vpn_bridge) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter} | Updates a Bridge object.
[**update_msg_vpn_bridge_remote_msg_vpn**](MsgVpnApi.md#update_msg_vpn_bridge_remote_msg_vpn) | **PATCH** /msgVpns/{msgVpnName}/bridges/{bridgeName},{bridgeVirtualRouter}/remoteMsgVpns/{remoteMsgVpnName},{remoteMsgVpnLocation},{remoteMsgVpnInterface} | Updates a Remote Message VPN object.
[**update_msg_vpn_client_profile**](MsgVpnApi.md#update_msg_vpn_client_profile) | **PATCH** /msgVpns/{msgVpnName}/clientProfiles/{clientProfileName} | Updates a Client Profile object.
[**update_msg_vpn_client_username**](MsgVpnApi.md#update_msg_vpn_client_username) | **PATCH** /msgVpns/{msgVpnName}/clientUsernames/{clientUsername} | Updates a Client Username object.
[**update_msg_vpn_mqtt_session**](MsgVpnApi.md#update_msg_vpn_mqtt_session) | **PATCH** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter} | Updates an MQTT Session object.
[**update_msg_vpn_mqtt_session_subscription**](MsgVpnApi.md#update_msg_vpn_mqtt_session_subscription) | **PATCH** /msgVpns/{msgVpnName}/mqttSessions/{mqttSessionClientId},{mqttSessionVirtualRouter}/subscriptions/{subscriptionTopic} | Updates an MQTT Session Subscription object.
[**update_msg_vpn_queue**](MsgVpnApi.md#update_msg_vpn_queue) | **PATCH** /msgVpns/{msgVpnName}/queues/{queueName} | Updates a Queue object.
[**update_msg_vpn_replicated_topic**](MsgVpnApi.md#update_msg_vpn_replicated_topic) | **PATCH** /msgVpns/{msgVpnName}/replicatedTopics/{replicatedTopic} | Updates a Replicated Topic object.
[**update_msg_vpn_rest_delivery_point**](MsgVpnApi.md#update_msg_vpn_rest_delivery_point) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName} | Updates a REST Delivery Point object.
[**update_msg_vpn_rest_delivery_point_queue_binding**](MsgVpnApi.md#update_msg_vpn_rest_delivery_point_queue_binding) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/queueBindings/{queueBindingName} | Updates a Queue Binding object.
[**update_msg_vpn_rest_delivery_point_rest_consumer**](MsgVpnApi.md#update_msg_vpn_rest_delivery_point_rest_consumer) | **PATCH** /msgVpns/{msgVpnName}/restDeliveryPoints/{restDeliveryPointName}/restConsumers/{restConsumerName} | Updates a REST Consumer object.
[**update_msg_vpn_topic_endpoint**](MsgVpnApi.md#update_msg_vpn_topic_endpoint) | **PATCH** /msgVpns/{msgVpnName}/topicEndpoints/{topicEndpointName} | Updates a Topic Endpoint object.


# **create_msg_vpn**
> MsgVpnResponse create_msg_vpn(body, select=select)

Creates a Message VPN object.

Creates a Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword||||x| replicationEnabledQueueBehavior||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
body = semp_client.MsgVpn() # MsgVpn | The Message VPN object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Message VPN object.
    api_response = api_instance.create_msg_vpn(body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**MsgVpn**](MsgVpn.md)| The Message VPN object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnResponse**](MsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_acl_profile**
> MsgVpnAclProfileResponse create_msg_vpn_acl_profile(msg_vpn_name, body, select=select)

Creates an ACL Profile object.

Creates an ACL Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnAclProfile() # MsgVpnAclProfile | The ACL Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates an ACL Profile object.
    api_response = api_instance.create_msg_vpn_acl_profile(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_acl_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnAclProfile**](MsgVpnAclProfile.md)| The ACL Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileResponse**](MsgVpnAclProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_acl_profile_client_connect_exception**
> MsgVpnAclProfileClientConnectExceptionResponse create_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, body, select=select)

Creates a Client Connect Exception object.

Creates a Client Connect Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| clientConnectExceptionAddress|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
body = semp_client.MsgVpnAclProfileClientConnectException() # MsgVpnAclProfileClientConnectException | The Client Connect Exception object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Client Connect Exception object.
    api_response = api_instance.create_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_acl_profile_client_connect_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **body** | [**MsgVpnAclProfileClientConnectException**](MsgVpnAclProfileClientConnectException.md)| The Client Connect Exception object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileClientConnectExceptionResponse**](MsgVpnAclProfileClientConnectExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_acl_profile_publish_exception**
> MsgVpnAclProfilePublishExceptionResponse create_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, body, select=select)

Creates a Publish Topic Exception object.

Creates a Publish Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| publishExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
body = semp_client.MsgVpnAclProfilePublishException() # MsgVpnAclProfilePublishException | The Publish Topic Exception object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Publish Topic Exception object.
    api_response = api_instance.create_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_acl_profile_publish_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **body** | [**MsgVpnAclProfilePublishException**](MsgVpnAclProfilePublishException.md)| The Publish Topic Exception object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfilePublishExceptionResponse**](MsgVpnAclProfilePublishExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_acl_profile_subscribe_exception**
> MsgVpnAclProfileSubscribeExceptionResponse create_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, body, select=select)

Creates a Subscribe Topic Exception object.

Creates a Subscribe Topic Exception object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x||x|| msgVpnName|x||x|| subscribeExceptionTopic|x|x||| topicSyntax|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
body = semp_client.MsgVpnAclProfileSubscribeException() # MsgVpnAclProfileSubscribeException | The Subscribe Topic Exception object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Subscribe Topic Exception object.
    api_response = api_instance.create_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_acl_profile_subscribe_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **body** | [**MsgVpnAclProfileSubscribeException**](MsgVpnAclProfileSubscribeException.md)| The Subscribe Topic Exception object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileSubscribeExceptionResponse**](MsgVpnAclProfileSubscribeExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_authorization_group**
> MsgVpnAuthorizationGroupResponse create_msg_vpn_authorization_group(msg_vpn_name, body, select=select)

Creates an LDAP Authorization Group object.

Creates an LDAP Authorization Group object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authorizationGroupName|x|x||| msgVpnName|x||x|| orderAfterAuthorizationGroupName||||x| orderBeforeAuthorizationGroupName||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnAuthorizationGroup() # MsgVpnAuthorizationGroup | The LDAP Authorization Group object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates an LDAP Authorization Group object.
    api_response = api_instance.create_msg_vpn_authorization_group(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_authorization_group: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnAuthorizationGroup**](MsgVpnAuthorizationGroup.md)| The LDAP Authorization Group object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAuthorizationGroupResponse**](MsgVpnAuthorizationGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_bridge**
> MsgVpnBridgeResponse create_msg_vpn_bridge(msg_vpn_name, body, select=select)

Creates a Bridge object.

Creates a Bridge object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| msgVpnName|x||x|| remoteAuthenticationBasicPassword||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnBridge() # MsgVpnBridge | The Bridge object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Bridge object.
    api_response = api_instance.create_msg_vpn_bridge(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_bridge: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnBridge**](MsgVpnBridge.md)| The Bridge object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_bridge_remote_msg_vpn**
> MsgVpnBridgeRemoteMsgVpnResponse create_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)

Creates a Remote Message VPN object.

Creates a Remote Message VPN object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| password||||x| remoteMsgVpnInterface|x|||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
body = semp_client.MsgVpnBridgeRemoteMsgVpn() # MsgVpnBridgeRemoteMsgVpn | The Remote Message VPN object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Remote Message VPN object.
    api_response = api_instance.create_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_bridge_remote_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **body** | [**MsgVpnBridgeRemoteMsgVpn**](MsgVpnBridgeRemoteMsgVpn.md)| The Remote Message VPN object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_bridge_remote_subscription**
> MsgVpnBridgeRemoteSubscriptionResponse create_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)

Creates a Remote Subscription object.

Creates a Remote Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| deliverAlwaysEnabled||x||| msgVpnName|x||x|| remoteSubscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
body = semp_client.MsgVpnBridgeRemoteSubscription() # MsgVpnBridgeRemoteSubscription | The Remote Subscription object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Remote Subscription object.
    api_response = api_instance.create_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_bridge_remote_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **body** | [**MsgVpnBridgeRemoteSubscription**](MsgVpnBridgeRemoteSubscription.md)| The Remote Subscription object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteSubscriptionResponse**](MsgVpnBridgeRemoteSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_bridge_tls_trusted_common_name**
> MsgVpnBridgeTlsTrustedCommonNameResponse create_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)

Creates a Trusted Common Name object.

Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x||x|| bridgeVirtualRouter|x||x|| msgVpnName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
body = semp_client.MsgVpnBridgeTlsTrustedCommonName() # MsgVpnBridgeTlsTrustedCommonName | The Trusted Common Name object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Trusted Common Name object.
    api_response = api_instance.create_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_bridge_tls_trusted_common_name: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **body** | [**MsgVpnBridgeTlsTrustedCommonName**](MsgVpnBridgeTlsTrustedCommonName.md)| The Trusted Common Name object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeTlsTrustedCommonNameResponse**](MsgVpnBridgeTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_client_profile**
> MsgVpnClientProfileResponse create_msg_vpn_client_profile(msg_vpn_name, body, select=select)

Creates a Client Profile object.

Creates a Client Profile object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnClientProfile() # MsgVpnClientProfile | The Client Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Client Profile object.
    api_response = api_instance.create_msg_vpn_client_profile(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_client_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnClientProfile**](MsgVpnClientProfile.md)| The Client Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_client_username**
> MsgVpnClientUsernameResponse create_msg_vpn_client_username(msg_vpn_name, body, select=select)

Creates a Client Username object.

Creates a Client Username object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientUsername|x|x||| msgVpnName|x||x|| password||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnClientUsername() # MsgVpnClientUsername | The Client Username object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Client Username object.
    api_response = api_instance.create_msg_vpn_client_username(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_client_username: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_mqtt_session**
> MsgVpnMqttSessionResponse create_msg_vpn_mqtt_session(msg_vpn_name, body, select=select)

Creates an MQTT Session object.

Creates an MQTT Session object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnMqttSession() # MsgVpnMqttSession | The MQTT Session object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates an MQTT Session object.
    api_response = api_instance.create_msg_vpn_mqtt_session(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_mqtt_session: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnMqttSession**](MsgVpnMqttSession.md)| The MQTT Session object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionResponse**](MsgVpnMqttSessionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_mqtt_session_subscription**
> MsgVpnMqttSessionSubscriptionResponse create_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)

Creates an MQTT Session Subscription object.

Creates an MQTT Session Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x||x|| mqttSessionVirtualRouter|x||x|| msgVpnName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
body = semp_client.MsgVpnMqttSessionSubscription() # MsgVpnMqttSessionSubscription | The MQTT Session Subscription object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates an MQTT Session Subscription object.
    api_response = api_instance.create_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_mqtt_session_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **body** | [**MsgVpnMqttSessionSubscription**](MsgVpnMqttSessionSubscription.md)| The MQTT Session Subscription object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionSubscriptionResponse**](MsgVpnMqttSessionSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_queue**
> MsgVpnQueueResponse create_msg_vpn_queue(msg_vpn_name, body, select=select)

Creates a Queue object.

Creates a Queue object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnQueue() # MsgVpnQueue | The Queue object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Queue object.
    api_response = api_instance.create_msg_vpn_queue(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_queue: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnQueue**](MsgVpnQueue.md)| The Queue object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_queue_subscription**
> MsgVpnQueueSubscriptionResponse create_msg_vpn_queue_subscription(msg_vpn_name, queue_name, body, select=select)

Creates a Queue Subscription object.

Creates a Queue Subscription object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueName|x||x|| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
body = semp_client.MsgVpnQueueSubscription() # MsgVpnQueueSubscription | The Queue Subscription object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Queue Subscription object.
    api_response = api_instance.create_msg_vpn_queue_subscription(msg_vpn_name, queue_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_queue_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **body** | [**MsgVpnQueueSubscription**](MsgVpnQueueSubscription.md)| The Queue Subscription object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueSubscriptionResponse**](MsgVpnQueueSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_replicated_topic**
> MsgVpnReplicatedTopicResponse create_msg_vpn_replicated_topic(msg_vpn_name, body, select=select)

Creates a Replicated Topic object.

Creates a Replicated Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnReplicatedTopic() # MsgVpnReplicatedTopic | The Replicated Topic object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Replicated Topic object.
    api_response = api_instance.create_msg_vpn_replicated_topic(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_replicated_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnReplicatedTopic**](MsgVpnReplicatedTopic.md)| The Replicated Topic object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_rest_delivery_point**
> MsgVpnRestDeliveryPointResponse create_msg_vpn_rest_delivery_point(msg_vpn_name, body, select=select)

Creates a REST Delivery Point object.

Creates a REST Delivery Point object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnRestDeliveryPoint() # MsgVpnRestDeliveryPoint | The REST Delivery Point object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a REST Delivery Point object.
    api_response = api_instance.create_msg_vpn_rest_delivery_point(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_rest_delivery_point: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnRestDeliveryPoint**](MsgVpnRestDeliveryPoint.md)| The REST Delivery Point object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointResponse**](MsgVpnRestDeliveryPointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_rest_delivery_point_queue_binding**
> MsgVpnRestDeliveryPointQueueBindingResponse create_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, body, select=select)

Creates a Queue Binding object.

Creates a Queue Binding object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| queueBindingName|x|x||| restDeliveryPointName|x||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
body = semp_client.MsgVpnRestDeliveryPointQueueBinding() # MsgVpnRestDeliveryPointQueueBinding | The Queue Binding object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Queue Binding object.
    api_response = api_instance.create_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_rest_delivery_point_queue_binding: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **body** | [**MsgVpnRestDeliveryPointQueueBinding**](MsgVpnRestDeliveryPointQueueBinding.md)| The Queue Binding object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointQueueBindingResponse**](MsgVpnRestDeliveryPointQueueBindingResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_rest_delivery_point_rest_consumer**
> MsgVpnRestDeliveryPointRestConsumerResponse create_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, body, select=select)

Creates a REST Consumer object.

Creates a REST Consumer object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword||||x| msgVpnName|x||x|| restConsumerName|x|x||| restDeliveryPointName|x||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
body = semp_client.MsgVpnRestDeliveryPointRestConsumer() # MsgVpnRestDeliveryPointRestConsumer | The REST Consumer object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a REST Consumer object.
    api_response = api_instance.create_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_rest_delivery_point_rest_consumer: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **body** | [**MsgVpnRestDeliveryPointRestConsumer**](MsgVpnRestDeliveryPointRestConsumer.md)| The REST Consumer object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerResponse**](MsgVpnRestDeliveryPointRestConsumerResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**
> MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, select=select)

Creates a Trusted Common Name object.

Creates a Trusted Common Name object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| restConsumerName|x||x|| restDeliveryPointName|x||x|| tlsTrustedCommonName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
rest_consumer_name = 'rest_consumer_name_example' # str | The restConsumerName of the REST Consumer.
body = semp_client.MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName() # MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName | The Trusted Common Name object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Trusted Common Name object.
    api_response = api_instance.create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **str**| The restConsumerName of the REST Consumer. | 
 **body** | [**MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName**](MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonName.md)| The Trusted Common Name object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse**](MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_sequenced_topic**
> MsgVpnSequencedTopicResponse create_msg_vpn_sequenced_topic(msg_vpn_name, body, select=select)

Creates a Sequenced Topic object.

Creates a Sequenced Topic object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| sequencedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnSequencedTopic() # MsgVpnSequencedTopic | The Sequenced Topic object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Sequenced Topic object.
    api_response = api_instance.create_msg_vpn_sequenced_topic(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_sequenced_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnSequencedTopic**](MsgVpnSequencedTopic.md)| The Sequenced Topic object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnSequencedTopicResponse**](MsgVpnSequencedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **create_msg_vpn_topic_endpoint**
> MsgVpnTopicEndpointResponse create_msg_vpn_topic_endpoint(msg_vpn_name, body, select=select)

Creates a Topic Endpoint object.

Creates a Topic Endpoint object. Any attribute missing from the request will be set to its default value.   Attribute|Identifying|Required|Read-Only|Write-Only|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x||x|| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpnTopicEndpoint() # MsgVpnTopicEndpoint | The Topic Endpoint object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Creates a Topic Endpoint object.
    api_response = api_instance.create_msg_vpn_topic_endpoint(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->create_msg_vpn_topic_endpoint: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpnTopicEndpoint**](MsgVpnTopicEndpoint.md)| The Topic Endpoint object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn**
> SempMetaOnlyResponse delete_msg_vpn(msg_vpn_name)

Deletes a Message VPN object.

Deletes a Message VPN object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.

try: 
    # Deletes a Message VPN object.
    api_response = api_instance.delete_msg_vpn(msg_vpn_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_acl_profile**
> SempMetaOnlyResponse delete_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name)

Deletes an ACL Profile object.

Deletes an ACL Profile object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.

try: 
    # Deletes an ACL Profile object.
    api_response = api_instance.delete_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_acl_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_acl_profile_client_connect_exception**
> SempMetaOnlyResponse delete_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address)

Deletes a Client Connect Exception object.

Deletes a Client Connect Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
client_connect_exception_address = 'client_connect_exception_address_example' # str | The clientConnectExceptionAddress of the Client Connect Exception.

try: 
    # Deletes a Client Connect Exception object.
    api_response = api_instance.delete_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_acl_profile_client_connect_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **client_connect_exception_address** | **str**| The clientConnectExceptionAddress of the Client Connect Exception. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_acl_profile_publish_exception**
> SempMetaOnlyResponse delete_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic)

Deletes a Publish Topic Exception object.

Deletes a Publish Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
topic_syntax = 'topic_syntax_example' # str | The topicSyntax of the Publish Topic Exception.
publish_exception_topic = 'publish_exception_topic_example' # str | The publishExceptionTopic of the Publish Topic Exception.

try: 
    # Deletes a Publish Topic Exception object.
    api_response = api_instance.delete_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_acl_profile_publish_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **topic_syntax** | **str**| The topicSyntax of the Publish Topic Exception. | 
 **publish_exception_topic** | **str**| The publishExceptionTopic of the Publish Topic Exception. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_acl_profile_subscribe_exception**
> SempMetaOnlyResponse delete_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic)

Deletes a Subscribe Topic Exception object.

Deletes a Subscribe Topic Exception object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
topic_syntax = 'topic_syntax_example' # str | The topicSyntax of the Subscribe Topic Exception.
subscribe_exception_topic = 'subscribe_exception_topic_example' # str | The subscribeExceptionTopic of the Subscribe Topic Exception.

try: 
    # Deletes a Subscribe Topic Exception object.
    api_response = api_instance.delete_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_acl_profile_subscribe_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **topic_syntax** | **str**| The topicSyntax of the Subscribe Topic Exception. | 
 **subscribe_exception_topic** | **str**| The subscribeExceptionTopic of the Subscribe Topic Exception. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_authorization_group**
> SempMetaOnlyResponse delete_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name)

Deletes an LDAP Authorization Group object.

Deletes an LDAP Authorization Group object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
authorization_group_name = 'authorization_group_name_example' # str | The authorizationGroupName of the LDAP Authorization Group.

try: 
    # Deletes an LDAP Authorization Group object.
    api_response = api_instance.delete_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_authorization_group: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **authorization_group_name** | **str**| The authorizationGroupName of the LDAP Authorization Group. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_bridge**
> SempMetaOnlyResponse delete_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router)

Deletes a Bridge object.

Deletes a Bridge object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.

try: 
    # Deletes a Bridge object.
    api_response = api_instance.delete_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_bridge: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_bridge_remote_msg_vpn**
> SempMetaOnlyResponse delete_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface)

Deletes a Remote Message VPN object.

Deletes a Remote Message VPN object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_msg_vpn_name = 'remote_msg_vpn_name_example' # str | The remoteMsgVpnName of the Remote Message VPN.
remote_msg_vpn_location = 'remote_msg_vpn_location_example' # str | The remoteMsgVpnLocation of the Remote Message VPN.
remote_msg_vpn_interface = 'remote_msg_vpn_interface_example' # str | The remoteMsgVpnInterface of the Remote Message VPN.

try: 
    # Deletes a Remote Message VPN object.
    api_response = api_instance.delete_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_bridge_remote_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_msg_vpn_name** | **str**| The remoteMsgVpnName of the Remote Message VPN. | 
 **remote_msg_vpn_location** | **str**| The remoteMsgVpnLocation of the Remote Message VPN. | 
 **remote_msg_vpn_interface** | **str**| The remoteMsgVpnInterface of the Remote Message VPN. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_bridge_remote_subscription**
> SempMetaOnlyResponse delete_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic)

Deletes a Remote Subscription object.

Deletes a Remote Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_subscription_topic = 'remote_subscription_topic_example' # str | The remoteSubscriptionTopic of the Remote Subscription.

try: 
    # Deletes a Remote Subscription object.
    api_response = api_instance.delete_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_bridge_remote_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_subscription_topic** | **str**| The remoteSubscriptionTopic of the Remote Subscription. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_bridge_tls_trusted_common_name**
> SempMetaOnlyResponse delete_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name)

Deletes a Trusted Common Name object.

Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
tls_trusted_common_name = 'tls_trusted_common_name_example' # str | The tlsTrustedCommonName of the Trusted Common Name.

try: 
    # Deletes a Trusted Common Name object.
    api_response = api_instance.delete_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_bridge_tls_trusted_common_name: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **tls_trusted_common_name** | **str**| The tlsTrustedCommonName of the Trusted Common Name. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_client_profile**
> SempMetaOnlyResponse delete_msg_vpn_client_profile(msg_vpn_name, client_profile_name)

Deletes a Client Profile object.

Deletes a Client Profile object.  A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_profile_name = 'client_profile_name_example' # str | The clientProfileName of the Client Profile.

try: 
    # Deletes a Client Profile object.
    api_response = api_instance.delete_msg_vpn_client_profile(msg_vpn_name, client_profile_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_client_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **str**| The clientProfileName of the Client Profile. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_client_username**
> SempMetaOnlyResponse delete_msg_vpn_client_username(msg_vpn_name, client_username)

Deletes a Client Username object.

Deletes a Client Username object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_username = 'client_username_example' # str | The clientUsername of the Client Username.

try: 
    # Deletes a Client Username object.
    api_response = api_instance.delete_msg_vpn_client_username(msg_vpn_name, client_username)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_client_username: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_username** | **str**| The clientUsername of the Client Username. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_mqtt_session**
> SempMetaOnlyResponse delete_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router)

Deletes an MQTT Session object.

Deletes an MQTT Session object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.

try: 
    # Deletes an MQTT Session object.
    api_response = api_instance.delete_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_mqtt_session: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_mqtt_session_subscription**
> SempMetaOnlyResponse delete_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic)

Deletes an MQTT Session Subscription object.

Deletes an MQTT Session Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the MQTT Session Subscription.

try: 
    # Deletes an MQTT Session Subscription object.
    api_response = api_instance.delete_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_mqtt_session_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **subscription_topic** | **str**| The subscriptionTopic of the MQTT Session Subscription. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_queue**
> SempMetaOnlyResponse delete_msg_vpn_queue(msg_vpn_name, queue_name)

Deletes a Queue object.

Deletes a Queue object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.

try: 
    # Deletes a Queue object.
    api_response = api_instance.delete_msg_vpn_queue(msg_vpn_name, queue_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_queue: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_queue_subscription**
> SempMetaOnlyResponse delete_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic)

Deletes a Queue Subscription object.

Deletes a Queue Subscription object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the Queue Subscription.

try: 
    # Deletes a Queue Subscription object.
    api_response = api_instance.delete_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_queue_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **subscription_topic** | **str**| The subscriptionTopic of the Queue Subscription. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_replicated_topic**
> SempMetaOnlyResponse delete_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic)

Deletes a Replicated Topic object.

Deletes a Replicated Topic object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
replicated_topic = 'replicated_topic_example' # str | The replicatedTopic of the Replicated Topic.

try: 
    # Deletes a Replicated Topic object.
    api_response = api_instance.delete_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_replicated_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **replicated_topic** | **str**| The replicatedTopic of the Replicated Topic. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_rest_delivery_point**
> SempMetaOnlyResponse delete_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name)

Deletes a REST Delivery Point object.

Deletes a REST Delivery Point object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.

try: 
    # Deletes a REST Delivery Point object.
    api_response = api_instance.delete_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_rest_delivery_point: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_rest_delivery_point_queue_binding**
> SempMetaOnlyResponse delete_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name)

Deletes a Queue Binding object.

Deletes a Queue Binding object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
queue_binding_name = 'queue_binding_name_example' # str | The queueBindingName of the Queue Binding.

try: 
    # Deletes a Queue Binding object.
    api_response = api_instance.delete_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_rest_delivery_point_queue_binding: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **queue_binding_name** | **str**| The queueBindingName of the Queue Binding. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_rest_delivery_point_rest_consumer**
> SempMetaOnlyResponse delete_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name)

Deletes a REST Consumer object.

Deletes a REST Consumer object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
rest_consumer_name = 'rest_consumer_name_example' # str | The restConsumerName of the REST Consumer.

try: 
    # Deletes a REST Consumer object.
    api_response = api_instance.delete_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_rest_delivery_point_rest_consumer: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **str**| The restConsumerName of the REST Consumer. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**
> SempMetaOnlyResponse delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name)

Deletes a Trusted Common Name object.

Deletes a Trusted Common Name object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
rest_consumer_name = 'rest_consumer_name_example' # str | The restConsumerName of the REST Consumer.
tls_trusted_common_name = 'tls_trusted_common_name_example' # str | The tlsTrustedCommonName of the Trusted Common Name.

try: 
    # Deletes a Trusted Common Name object.
    api_response = api_instance.delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **str**| The restConsumerName of the REST Consumer. | 
 **tls_trusted_common_name** | **str**| The tlsTrustedCommonName of the Trusted Common Name. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_sequenced_topic**
> SempMetaOnlyResponse delete_msg_vpn_sequenced_topic(msg_vpn_name, sequenced_topic)

Deletes a Sequenced Topic object.

Deletes a Sequenced Topic object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
sequenced_topic = 'sequenced_topic_example' # str | The sequencedTopic of the Sequenced Topic.

try: 
    # Deletes a Sequenced Topic object.
    api_response = api_instance.delete_msg_vpn_sequenced_topic(msg_vpn_name, sequenced_topic)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_sequenced_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **sequenced_topic** | **str**| The sequencedTopic of the Sequenced Topic. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete_msg_vpn_topic_endpoint**
> SempMetaOnlyResponse delete_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name)

Deletes a Topic Endpoint object.

Deletes a Topic Endpoint object.  A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
topic_endpoint_name = 'topic_endpoint_name_example' # str | The topicEndpointName of the Topic Endpoint.

try: 
    # Deletes a Topic Endpoint object.
    api_response = api_instance.delete_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->delete_msg_vpn_topic_endpoint: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **topic_endpoint_name** | **str**| The topicEndpointName of the Topic Endpoint. | 

### Return type

[**SempMetaOnlyResponse**](SempMetaOnlyResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn**
> MsgVpnResponse get_msg_vpn(msg_vpn_name, select=select)

Gets a Message VPN object.

Gets a Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Message VPN object.
    api_response = api_instance.get_msg_vpn(msg_vpn_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnResponse**](MsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile**
> MsgVpnAclProfileResponse get_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, select=select)

Gets an ACL Profile object.

Gets an ACL Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets an ACL Profile object.
    api_response = api_instance.get_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_acl_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileResponse**](MsgVpnAclProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_client_connect_exception**
> MsgVpnAclProfileClientConnectExceptionResponse get_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address, select=select)

Gets a Client Connect Exception object.

Gets a Client Connect Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
client_connect_exception_address = 'client_connect_exception_address_example' # str | The clientConnectExceptionAddress of the Client Connect Exception.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Client Connect Exception object.
    api_response = api_instance.get_msg_vpn_acl_profile_client_connect_exception(msg_vpn_name, acl_profile_name, client_connect_exception_address, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_acl_profile_client_connect_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **client_connect_exception_address** | **str**| The clientConnectExceptionAddress of the Client Connect Exception. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileClientConnectExceptionResponse**](MsgVpnAclProfileClientConnectExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_client_connect_exceptions**
> MsgVpnAclProfileClientConnectExceptionsResponse get_msg_vpn_acl_profile_client_connect_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Client Connect Exception objects.

Gets a list of Client Connect Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| clientConnectExceptionAddress|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Client Connect Exception objects.
    api_response = api_instance.get_msg_vpn_acl_profile_client_connect_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_acl_profile_client_connect_exceptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileClientConnectExceptionsResponse**](MsgVpnAclProfileClientConnectExceptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_publish_exception**
> MsgVpnAclProfilePublishExceptionResponse get_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, select=select)

Gets a Publish Topic Exception object.

Gets a Publish Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
topic_syntax = 'topic_syntax_example' # str | The topicSyntax of the Publish Topic Exception.
publish_exception_topic = 'publish_exception_topic_example' # str | The publishExceptionTopic of the Publish Topic Exception.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Publish Topic Exception object.
    api_response = api_instance.get_msg_vpn_acl_profile_publish_exception(msg_vpn_name, acl_profile_name, topic_syntax, publish_exception_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_acl_profile_publish_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **topic_syntax** | **str**| The topicSyntax of the Publish Topic Exception. | 
 **publish_exception_topic** | **str**| The publishExceptionTopic of the Publish Topic Exception. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfilePublishExceptionResponse**](MsgVpnAclProfilePublishExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_publish_exceptions**
> MsgVpnAclProfilePublishExceptionsResponse get_msg_vpn_acl_profile_publish_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Publish Topic Exception objects.

Gets a list of Publish Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| publishExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Publish Topic Exception objects.
    api_response = api_instance.get_msg_vpn_acl_profile_publish_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_acl_profile_publish_exceptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfilePublishExceptionsResponse**](MsgVpnAclProfilePublishExceptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_subscribe_exception**
> MsgVpnAclProfileSubscribeExceptionResponse get_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, select=select)

Gets a Subscribe Topic Exception object.

Gets a Subscribe Topic Exception object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
topic_syntax = 'topic_syntax_example' # str | The topicSyntax of the Subscribe Topic Exception.
subscribe_exception_topic = 'subscribe_exception_topic_example' # str | The subscribeExceptionTopic of the Subscribe Topic Exception.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Subscribe Topic Exception object.
    api_response = api_instance.get_msg_vpn_acl_profile_subscribe_exception(msg_vpn_name, acl_profile_name, topic_syntax, subscribe_exception_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_acl_profile_subscribe_exception: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **topic_syntax** | **str**| The topicSyntax of the Subscribe Topic Exception. | 
 **subscribe_exception_topic** | **str**| The subscribeExceptionTopic of the Subscribe Topic Exception. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileSubscribeExceptionResponse**](MsgVpnAclProfileSubscribeExceptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profile_subscribe_exceptions**
> MsgVpnAclProfileSubscribeExceptionsResponse get_msg_vpn_acl_profile_subscribe_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Subscribe Topic Exception objects.

Gets a list of Subscribe Topic Exception objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x|| subscribeExceptionTopic|x|| topicSyntax|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Subscribe Topic Exception objects.
    api_response = api_instance.get_msg_vpn_acl_profile_subscribe_exceptions(msg_vpn_name, acl_profile_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_acl_profile_subscribe_exceptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileSubscribeExceptionsResponse**](MsgVpnAclProfileSubscribeExceptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_acl_profiles**
> MsgVpnAclProfilesResponse get_msg_vpn_acl_profiles(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of ACL Profile objects.

Gets a list of ACL Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: aclProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of ACL Profile objects.
    api_response = api_instance.get_msg_vpn_acl_profiles(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_acl_profiles: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfilesResponse**](MsgVpnAclProfilesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_authorization_group**
> MsgVpnAuthorizationGroupResponse get_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, select=select)

Gets an LDAP Authorization Group object.

Gets an LDAP Authorization Group object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
authorization_group_name = 'authorization_group_name_example' # str | The authorizationGroupName of the LDAP Authorization Group.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets an LDAP Authorization Group object.
    api_response = api_instance.get_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_authorization_group: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **authorization_group_name** | **str**| The authorizationGroupName of the LDAP Authorization Group. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAuthorizationGroupResponse**](MsgVpnAuthorizationGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_authorization_groups**
> MsgVpnAuthorizationGroupsResponse get_msg_vpn_authorization_groups(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of LDAP Authorization Group objects.

Gets a list of LDAP Authorization Group objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authorizationGroupName|x|| msgVpnName|x|| orderAfterAuthorizationGroupName||x| orderBeforeAuthorizationGroupName||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of LDAP Authorization Group objects.
    api_response = api_instance.get_msg_vpn_authorization_groups(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_authorization_groups: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAuthorizationGroupsResponse**](MsgVpnAuthorizationGroupsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge**
> MsgVpnBridgeResponse get_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, select=select)

Gets a Bridge object.

Gets a Bridge object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Bridge object.
    api_response = api_instance.get_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_bridge: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_remote_msg_vpn**
> MsgVpnBridgeRemoteMsgVpnResponse get_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, select=select)

Gets a Remote Message VPN object.

Gets a Remote Message VPN object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_msg_vpn_name = 'remote_msg_vpn_name_example' # str | The remoteMsgVpnName of the Remote Message VPN.
remote_msg_vpn_location = 'remote_msg_vpn_location_example' # str | The remoteMsgVpnLocation of the Remote Message VPN.
remote_msg_vpn_interface = 'remote_msg_vpn_interface_example' # str | The remoteMsgVpnInterface of the Remote Message VPN.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Remote Message VPN object.
    api_response = api_instance.get_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_bridge_remote_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_msg_vpn_name** | **str**| The remoteMsgVpnName of the Remote Message VPN. | 
 **remote_msg_vpn_location** | **str**| The remoteMsgVpnLocation of the Remote Message VPN. | 
 **remote_msg_vpn_interface** | **str**| The remoteMsgVpnInterface of the Remote Message VPN. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_remote_msg_vpns**
> MsgVpnBridgeRemoteMsgVpnsResponse get_msg_vpn_bridge_remote_msg_vpns(msg_vpn_name, bridge_name, bridge_virtual_router, where=where, select=select)

Gets a list of Remote Message VPN objects.

Gets a list of Remote Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| password||x| remoteMsgVpnInterface|x|| remoteMsgVpnLocation|x|| remoteMsgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Remote Message VPN objects.
    api_response = api_instance.get_msg_vpn_bridge_remote_msg_vpns(msg_vpn_name, bridge_name, bridge_virtual_router, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_bridge_remote_msg_vpns: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteMsgVpnsResponse**](MsgVpnBridgeRemoteMsgVpnsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_remote_subscription**
> MsgVpnBridgeRemoteSubscriptionResponse get_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, select=select)

Gets a Remote Subscription object.

Gets a Remote Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_subscription_topic = 'remote_subscription_topic_example' # str | The remoteSubscriptionTopic of the Remote Subscription.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Remote Subscription object.
    api_response = api_instance.get_msg_vpn_bridge_remote_subscription(msg_vpn_name, bridge_name, bridge_virtual_router, remote_subscription_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_bridge_remote_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_subscription_topic** | **str**| The remoteSubscriptionTopic of the Remote Subscription. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteSubscriptionResponse**](MsgVpnBridgeRemoteSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_remote_subscriptions**
> MsgVpnBridgeRemoteSubscriptionsResponse get_msg_vpn_bridge_remote_subscriptions(msg_vpn_name, bridge_name, bridge_virtual_router, count=count, cursor=cursor, where=where, select=select)

Gets a list of Remote Subscription objects.

Gets a list of Remote Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteSubscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Remote Subscription objects.
    api_response = api_instance.get_msg_vpn_bridge_remote_subscriptions(msg_vpn_name, bridge_name, bridge_virtual_router, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_bridge_remote_subscriptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteSubscriptionsResponse**](MsgVpnBridgeRemoteSubscriptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_tls_trusted_common_name**
> MsgVpnBridgeTlsTrustedCommonNameResponse get_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, select=select)

Gets a Trusted Common Name object.

Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
tls_trusted_common_name = 'tls_trusted_common_name_example' # str | The tlsTrustedCommonName of the Trusted Common Name.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Trusted Common Name object.
    api_response = api_instance.get_msg_vpn_bridge_tls_trusted_common_name(msg_vpn_name, bridge_name, bridge_virtual_router, tls_trusted_common_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_bridge_tls_trusted_common_name: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **tls_trusted_common_name** | **str**| The tlsTrustedCommonName of the Trusted Common Name. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeTlsTrustedCommonNameResponse**](MsgVpnBridgeTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridge_tls_trusted_common_names**
> MsgVpnBridgeTlsTrustedCommonNamesResponse get_msg_vpn_bridge_tls_trusted_common_names(msg_vpn_name, bridge_name, bridge_virtual_router, where=where, select=select)

Gets a list of Trusted Common Name objects.

Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Trusted Common Name objects.
    api_response = api_instance.get_msg_vpn_bridge_tls_trusted_common_names(msg_vpn_name, bridge_name, bridge_virtual_router, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_bridge_tls_trusted_common_names: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeTlsTrustedCommonNamesResponse**](MsgVpnBridgeTlsTrustedCommonNamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_bridges**
> MsgVpnBridgesResponse get_msg_vpn_bridges(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Bridge objects.

Gets a list of Bridge objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: bridgeName|x|| bridgeVirtualRouter|x|| msgVpnName|x|| remoteAuthenticationBasicPassword||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Bridge objects.
    api_response = api_instance.get_msg_vpn_bridges(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_bridges: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgesResponse**](MsgVpnBridgesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_client_profile**
> MsgVpnClientProfileResponse get_msg_vpn_client_profile(msg_vpn_name, client_profile_name, select=select)

Gets a Client Profile object.

Gets a Client Profile object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_profile_name = 'client_profile_name_example' # str | The clientProfileName of the Client Profile.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Client Profile object.
    api_response = api_instance.get_msg_vpn_client_profile(msg_vpn_name, client_profile_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_client_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **str**| The clientProfileName of the Client Profile. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_client_profiles**
> MsgVpnClientProfilesResponse get_msg_vpn_client_profiles(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Client Profile objects.

Gets a list of Client Profile objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientProfileName|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Client Profile objects.
    api_response = api_instance.get_msg_vpn_client_profiles(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_client_profiles: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfilesResponse**](MsgVpnClientProfilesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_client_username**
> MsgVpnClientUsernameResponse get_msg_vpn_client_username(msg_vpn_name, client_username, select=select)

Gets a Client Username object.

Gets a Client Username object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_username = 'client_username_example' # str | The clientUsername of the Client Username.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Client Username object.
    api_response = api_instance.get_msg_vpn_client_username(msg_vpn_name, client_username, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_client_username: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_username** | **str**| The clientUsername of the Client Username. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_client_usernames**
> MsgVpnClientUsernamesResponse get_msg_vpn_client_usernames(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Client Username objects.

Gets a list of Client Username objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: clientUsername|x|| msgVpnName|x|| password||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Client Username objects.
    api_response = api_instance.get_msg_vpn_client_usernames(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_client_usernames: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientUsernamesResponse**](MsgVpnClientUsernamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_mqtt_session**
> MsgVpnMqttSessionResponse get_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, select=select)

Gets an MQTT Session object.

Gets an MQTT Session object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets an MQTT Session object.
    api_response = api_instance.get_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_mqtt_session: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionResponse**](MsgVpnMqttSessionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_mqtt_session_subscription**
> MsgVpnMqttSessionSubscriptionResponse get_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, select=select)

Gets an MQTT Session Subscription object.

Gets an MQTT Session Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the MQTT Session Subscription.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets an MQTT Session Subscription object.
    api_response = api_instance.get_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_mqtt_session_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **subscription_topic** | **str**| The subscriptionTopic of the MQTT Session Subscription. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionSubscriptionResponse**](MsgVpnMqttSessionSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_mqtt_session_subscriptions**
> MsgVpnMqttSessionSubscriptionsResponse get_msg_vpn_mqtt_session_subscriptions(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, count=count, cursor=cursor, where=where, select=select)

Gets a list of MQTT Session Subscription objects.

Gets a list of MQTT Session Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of MQTT Session Subscription objects.
    api_response = api_instance.get_msg_vpn_mqtt_session_subscriptions(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_mqtt_session_subscriptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionSubscriptionsResponse**](MsgVpnMqttSessionSubscriptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_mqtt_sessions**
> MsgVpnMqttSessionsResponse get_msg_vpn_mqtt_sessions(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of MQTT Session objects.

Gets a list of MQTT Session objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: mqttSessionClientId|x|| mqttSessionVirtualRouter|x|| msgVpnName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of MQTT Session objects.
    api_response = api_instance.get_msg_vpn_mqtt_sessions(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_mqtt_sessions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionsResponse**](MsgVpnMqttSessionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_queue**
> MsgVpnQueueResponse get_msg_vpn_queue(msg_vpn_name, queue_name, select=select)

Gets a Queue object.

Gets a Queue object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Queue object.
    api_response = api_instance.get_msg_vpn_queue(msg_vpn_name, queue_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_queue: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_queue_subscription**
> MsgVpnQueueSubscriptionResponse get_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic, select=select)

Gets a Queue Subscription object.

Gets a Queue Subscription object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the Queue Subscription.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Queue Subscription object.
    api_response = api_instance.get_msg_vpn_queue_subscription(msg_vpn_name, queue_name, subscription_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_queue_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **subscription_topic** | **str**| The subscriptionTopic of the Queue Subscription. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueSubscriptionResponse**](MsgVpnQueueSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_queue_subscriptions**
> MsgVpnQueueSubscriptionsResponse get_msg_vpn_queue_subscriptions(msg_vpn_name, queue_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Queue Subscription objects.

Gets a list of Queue Subscription objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x|| subscriptionTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Queue Subscription objects.
    api_response = api_instance.get_msg_vpn_queue_subscriptions(msg_vpn_name, queue_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_queue_subscriptions: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueSubscriptionsResponse**](MsgVpnQueueSubscriptionsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_queues**
> MsgVpnQueuesResponse get_msg_vpn_queues(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Queue objects.

Gets a list of Queue objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Queue objects.
    api_response = api_instance.get_msg_vpn_queues(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_queues: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueuesResponse**](MsgVpnQueuesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_replicated_topic**
> MsgVpnReplicatedTopicResponse get_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, select=select)

Gets a Replicated Topic object.

Gets a Replicated Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicatedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
replicated_topic = 'replicated_topic_example' # str | The replicatedTopic of the Replicated Topic.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Replicated Topic object.
    api_response = api_instance.get_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_replicated_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **replicated_topic** | **str**| The replicatedTopic of the Replicated Topic. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_replicated_topics**
> MsgVpnReplicatedTopicsResponse get_msg_vpn_replicated_topics(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Replicated Topic objects.

Gets a list of Replicated Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicatedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Replicated Topic objects.
    api_response = api_instance.get_msg_vpn_replicated_topics(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_replicated_topics: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnReplicatedTopicsResponse**](MsgVpnReplicatedTopicsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_rest_delivery_point**
> MsgVpnRestDeliveryPointResponse get_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, select=select)

Gets a REST Delivery Point object.

Gets a REST Delivery Point object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a REST Delivery Point object.
    api_response = api_instance.get_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_rest_delivery_point: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointResponse**](MsgVpnRestDeliveryPointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_rest_delivery_point_queue_binding**
> MsgVpnRestDeliveryPointQueueBindingResponse get_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, select=select)

Gets a Queue Binding object.

Gets a Queue Binding object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
queue_binding_name = 'queue_binding_name_example' # str | The queueBindingName of the Queue Binding.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Queue Binding object.
    api_response = api_instance.get_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_rest_delivery_point_queue_binding: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **queue_binding_name** | **str**| The queueBindingName of the Queue Binding. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointQueueBindingResponse**](MsgVpnRestDeliveryPointQueueBindingResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_rest_delivery_point_queue_bindings**
> MsgVpnRestDeliveryPointQueueBindingsResponse get_msg_vpn_rest_delivery_point_queue_bindings(msg_vpn_name, rest_delivery_point_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Queue Binding objects.

Gets a list of Queue Binding objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| queueBindingName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Queue Binding objects.
    api_response = api_instance.get_msg_vpn_rest_delivery_point_queue_bindings(msg_vpn_name, rest_delivery_point_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_rest_delivery_point_queue_bindings: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointQueueBindingsResponse**](MsgVpnRestDeliveryPointQueueBindingsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_rest_delivery_point_rest_consumer**
> MsgVpnRestDeliveryPointRestConsumerResponse get_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, select=select)

Gets a REST Consumer object.

Gets a REST Consumer object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
rest_consumer_name = 'rest_consumer_name_example' # str | The restConsumerName of the REST Consumer.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a REST Consumer object.
    api_response = api_instance.get_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_rest_delivery_point_rest_consumer: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **str**| The restConsumerName of the REST Consumer. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerResponse**](MsgVpnRestDeliveryPointRestConsumerResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name**
> MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, select=select)

Gets a Trusted Common Name object.

Gets a Trusted Common Name object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
rest_consumer_name = 'rest_consumer_name_example' # str | The restConsumerName of the REST Consumer.
tls_trusted_common_name = 'tls_trusted_common_name_example' # str | The tlsTrustedCommonName of the Trusted Common Name.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Trusted Common Name object.
    api_response = api_instance.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, tls_trusted_common_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_name: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **str**| The restConsumerName of the REST Consumer. | 
 **tls_trusted_common_name** | **str**| The tlsTrustedCommonName of the Trusted Common Name. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse**](MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names**
> MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, where=where, select=select)

Gets a list of Trusted Common Name objects.

Gets a list of Trusted Common Name objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x|| tlsTrustedCommonName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
rest_consumer_name = 'rest_consumer_name_example' # str | The restConsumerName of the REST Consumer.
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Trusted Common Name objects.
    api_response = api_instance.get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_rest_delivery_point_rest_consumer_tls_trusted_common_names: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **str**| The restConsumerName of the REST Consumer. | 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse**](MsgVpnRestDeliveryPointRestConsumerTlsTrustedCommonNamesResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_rest_delivery_point_rest_consumers**
> MsgVpnRestDeliveryPointRestConsumersResponse get_msg_vpn_rest_delivery_point_rest_consumers(msg_vpn_name, rest_delivery_point_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of REST Consumer objects.

Gets a list of REST Consumer objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: authenticationHttpBasicPassword||x| msgVpnName|x|| restConsumerName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of REST Consumer objects.
    api_response = api_instance.get_msg_vpn_rest_delivery_point_rest_consumers(msg_vpn_name, rest_delivery_point_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_rest_delivery_point_rest_consumers: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumersResponse**](MsgVpnRestDeliveryPointRestConsumersResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_rest_delivery_points**
> MsgVpnRestDeliveryPointsResponse get_msg_vpn_rest_delivery_points(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of REST Delivery Point objects.

Gets a list of REST Delivery Point objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| restDeliveryPointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of REST Delivery Point objects.
    api_response = api_instance.get_msg_vpn_rest_delivery_points(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_rest_delivery_points: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointsResponse**](MsgVpnRestDeliveryPointsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_sequenced_topic**
> MsgVpnSequencedTopicResponse get_msg_vpn_sequenced_topic(msg_vpn_name, sequenced_topic, select=select)

Gets a Sequenced Topic object.

Gets a Sequenced Topic object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
sequenced_topic = 'sequenced_topic_example' # str | The sequencedTopic of the Sequenced Topic.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Sequenced Topic object.
    api_response = api_instance.get_msg_vpn_sequenced_topic(msg_vpn_name, sequenced_topic, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_sequenced_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **sequenced_topic** | **str**| The sequencedTopic of the Sequenced Topic. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnSequencedTopicResponse**](MsgVpnSequencedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_sequenced_topics**
> MsgVpnSequencedTopicsResponse get_msg_vpn_sequenced_topics(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Sequenced Topic objects.

Gets a list of Sequenced Topic objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| sequencedTopic|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Sequenced Topic objects.
    api_response = api_instance.get_msg_vpn_sequenced_topics(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_sequenced_topics: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnSequencedTopicsResponse**](MsgVpnSequencedTopicsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_topic_endpoint**
> MsgVpnTopicEndpointResponse get_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, select=select)

Gets a Topic Endpoint object.

Gets a Topic Endpoint object.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| topicEndpointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
topic_endpoint_name = 'topic_endpoint_name_example' # str | The topicEndpointName of the Topic Endpoint.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a Topic Endpoint object.
    api_response = api_instance.get_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_topic_endpoint: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **topic_endpoint_name** | **str**| The topicEndpointName of the Topic Endpoint. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpn_topic_endpoints**
> MsgVpnTopicEndpointsResponse get_msg_vpn_topic_endpoints(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)

Gets a list of Topic Endpoint objects.

Gets a list of Topic Endpoint objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| topicEndpointName|x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Topic Endpoint objects.
    api_response = api_instance.get_msg_vpn_topic_endpoints(msg_vpn_name, count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpn_topic_endpoints: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnTopicEndpointsResponse**](MsgVpnTopicEndpointsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_msg_vpns**
> MsgVpnsResponse get_msg_vpns(count=count, cursor=cursor, where=where, select=select)

Gets a list of Message VPN objects.

Gets a list of Message VPN objects.   Attribute|Identifying|Write-Only|Deprecated :---|:---:|:---:|:---: msgVpnName|x|| replicationBridgeAuthenticationBasicPassword||x| replicationEnabledQueueBehavior||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readonly\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
count = 10 # int | Limit the count of objects in the response. See [Count](#count \"Description of the syntax of the `count` parameter\"). (optional) (default to 10)
cursor = 'cursor_example' # str | The cursor, or position, for the next page of objects. See [Cursor](#cursor \"Description of the syntax of the `cursor` parameter\"). (optional)
where = ['where_example'] # list[str] | Include in the response only objects where certain conditions are true. See [Where](#where \"Description of the syntax of the `where` parameter\"). (optional)
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Gets a list of Message VPN objects.
    api_response = api_instance.get_msg_vpns(count=count, cursor=cursor, where=where, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->get_msg_vpns: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **count** | **int**| Limit the count of objects in the response. See [Count](#count \&quot;Description of the syntax of the &#x60;count&#x60; parameter\&quot;). | [optional] [default to 10]
 **cursor** | **str**| The cursor, or position, for the next page of objects. See [Cursor](#cursor \&quot;Description of the syntax of the &#x60;cursor&#x60; parameter\&quot;). | [optional] 
 **where** | [**list[str]**](str.md)| Include in the response only objects where certain conditions are true. See [Where](#where \&quot;Description of the syntax of the &#x60;where&#x60; parameter\&quot;). | [optional] 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnsResponse**](MsgVpnsResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn**
> MsgVpnResponse replace_msg_vpn(msg_vpn_name, body, select=select)

Replaces a Message VPN object.

Replaces a Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite    This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpn() # MsgVpn | The Message VPN object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Message VPN object.
    api_response = api_instance.replace_msg_vpn(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpn**](MsgVpn.md)| The Message VPN object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnResponse**](MsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_acl_profile**
> MsgVpnAclProfileResponse replace_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, select=select)

Replaces an ACL Profile object.

Replaces an ACL Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
body = semp_client.MsgVpnAclProfile() # MsgVpnAclProfile | The ACL Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces an ACL Profile object.
    api_response = api_instance.replace_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_acl_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **body** | [**MsgVpnAclProfile**](MsgVpnAclProfile.md)| The ACL Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileResponse**](MsgVpnAclProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_authorization_group**
> MsgVpnAuthorizationGroupResponse replace_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, select=select)

Replaces an LDAP Authorization Group object.

Replaces an LDAP Authorization Group object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
authorization_group_name = 'authorization_group_name_example' # str | The authorizationGroupName of the LDAP Authorization Group.
body = semp_client.MsgVpnAuthorizationGroup() # MsgVpnAuthorizationGroup | The LDAP Authorization Group object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces an LDAP Authorization Group object.
    api_response = api_instance.replace_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_authorization_group: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **authorization_group_name** | **str**| The authorizationGroupName of the LDAP Authorization Group. | 
 **body** | [**MsgVpnAuthorizationGroup**](MsgVpnAuthorizationGroup.md)| The LDAP Authorization Group object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAuthorizationGroupResponse**](MsgVpnAuthorizationGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_bridge**
> MsgVpnBridgeResponse replace_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)

Replaces a Bridge object.

Replaces a Bridge object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
body = semp_client.MsgVpnBridge() # MsgVpnBridge | The Bridge object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Bridge object.
    api_response = api_instance.replace_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_bridge: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **body** | [**MsgVpnBridge**](MsgVpnBridge.md)| The Bridge object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_bridge_remote_msg_vpn**
> MsgVpnBridgeRemoteMsgVpnResponse replace_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, select=select)

Replaces a Remote Message VPN object.

Replaces a Remote Message VPN object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_msg_vpn_name = 'remote_msg_vpn_name_example' # str | The remoteMsgVpnName of the Remote Message VPN.
remote_msg_vpn_location = 'remote_msg_vpn_location_example' # str | The remoteMsgVpnLocation of the Remote Message VPN.
remote_msg_vpn_interface = 'remote_msg_vpn_interface_example' # str | The remoteMsgVpnInterface of the Remote Message VPN.
body = semp_client.MsgVpnBridgeRemoteMsgVpn() # MsgVpnBridgeRemoteMsgVpn | The Remote Message VPN object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Remote Message VPN object.
    api_response = api_instance.replace_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_bridge_remote_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_msg_vpn_name** | **str**| The remoteMsgVpnName of the Remote Message VPN. | 
 **remote_msg_vpn_location** | **str**| The remoteMsgVpnLocation of the Remote Message VPN. | 
 **remote_msg_vpn_interface** | **str**| The remoteMsgVpnInterface of the Remote Message VPN. | 
 **body** | [**MsgVpnBridgeRemoteMsgVpn**](MsgVpnBridgeRemoteMsgVpn.md)| The Remote Message VPN object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_client_profile**
> MsgVpnClientProfileResponse replace_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, select=select)

Replaces a Client Profile object.

Replaces a Client Profile object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_profile_name = 'client_profile_name_example' # str | The clientProfileName of the Client Profile.
body = semp_client.MsgVpnClientProfile() # MsgVpnClientProfile | The Client Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Client Profile object.
    api_response = api_instance.replace_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_client_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **str**| The clientProfileName of the Client Profile. | 
 **body** | [**MsgVpnClientProfile**](MsgVpnClientProfile.md)| The Client Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_client_username**
> MsgVpnClientUsernameResponse replace_msg_vpn_client_username(msg_vpn_name, client_username, body, select=select)

Replaces a Client Username object.

Replaces a Client Username object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_username = 'client_username_example' # str | The clientUsername of the Client Username.
body = semp_client.MsgVpnClientUsername() # MsgVpnClientUsername | The Client Username object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Client Username object.
    api_response = api_instance.replace_msg_vpn_client_username(msg_vpn_name, client_username, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_client_username: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_username** | **str**| The clientUsername of the Client Username. | 
 **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_mqtt_session**
> MsgVpnMqttSessionResponse replace_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)

Replaces an MQTT Session object.

Replaces an MQTT Session object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| owner||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
body = semp_client.MsgVpnMqttSession() # MsgVpnMqttSession | The MQTT Session object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces an MQTT Session object.
    api_response = api_instance.replace_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_mqtt_session: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **body** | [**MsgVpnMqttSession**](MsgVpnMqttSession.md)| The MQTT Session object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionResponse**](MsgVpnMqttSessionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_mqtt_session_subscription**
> MsgVpnMqttSessionSubscriptionResponse replace_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, select=select)

Replaces an MQTT Session Subscription object.

Replaces an MQTT Session Subscription object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the MQTT Session Subscription.
body = semp_client.MsgVpnMqttSessionSubscription() # MsgVpnMqttSessionSubscription | The MQTT Session Subscription object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces an MQTT Session Subscription object.
    api_response = api_instance.replace_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_mqtt_session_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **subscription_topic** | **str**| The subscriptionTopic of the MQTT Session Subscription. | 
 **body** | [**MsgVpnMqttSessionSubscription**](MsgVpnMqttSessionSubscription.md)| The MQTT Session Subscription object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionSubscriptionResponse**](MsgVpnMqttSessionSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_queue**
> MsgVpnQueueResponse replace_msg_vpn_queue(msg_vpn_name, queue_name, body, select=select)

Replaces a Queue object.

Replaces a Queue object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
body = semp_client.MsgVpnQueue() # MsgVpnQueue | The Queue object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Queue object.
    api_response = api_instance.replace_msg_vpn_queue(msg_vpn_name, queue_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_queue: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **body** | [**MsgVpnQueue**](MsgVpnQueue.md)| The Queue object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_replicated_topic**
> MsgVpnReplicatedTopicResponse replace_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, body, select=select)

Replaces a Replicated Topic object.

Replaces a Replicated Topic object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
replicated_topic = 'replicated_topic_example' # str | The replicatedTopic of the Replicated Topic.
body = semp_client.MsgVpnReplicatedTopic() # MsgVpnReplicatedTopic | The Replicated Topic object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Replicated Topic object.
    api_response = api_instance.replace_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_replicated_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **replicated_topic** | **str**| The replicatedTopic of the Replicated Topic. | 
 **body** | [**MsgVpnReplicatedTopic**](MsgVpnReplicatedTopic.md)| The Replicated Topic object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_rest_delivery_point**
> MsgVpnRestDeliveryPointResponse replace_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, select=select)

Replaces a REST Delivery Point object.

Replaces a REST Delivery Point object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
body = semp_client.MsgVpnRestDeliveryPoint() # MsgVpnRestDeliveryPoint | The REST Delivery Point object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a REST Delivery Point object.
    api_response = api_instance.replace_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_rest_delivery_point: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **body** | [**MsgVpnRestDeliveryPoint**](MsgVpnRestDeliveryPoint.md)| The REST Delivery Point object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointResponse**](MsgVpnRestDeliveryPointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_rest_delivery_point_queue_binding**
> MsgVpnRestDeliveryPointQueueBindingResponse replace_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, select=select)

Replaces a Queue Binding object.

Replaces a Queue Binding object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
queue_binding_name = 'queue_binding_name_example' # str | The queueBindingName of the Queue Binding.
body = semp_client.MsgVpnRestDeliveryPointQueueBinding() # MsgVpnRestDeliveryPointQueueBinding | The Queue Binding object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Queue Binding object.
    api_response = api_instance.replace_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_rest_delivery_point_queue_binding: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **queue_binding_name** | **str**| The queueBindingName of the Queue Binding. | 
 **body** | [**MsgVpnRestDeliveryPointQueueBinding**](MsgVpnRestDeliveryPointQueueBinding.md)| The Queue Binding object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointQueueBindingResponse**](MsgVpnRestDeliveryPointQueueBindingResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_rest_delivery_point_rest_consumer**
> MsgVpnRestDeliveryPointRestConsumerResponse replace_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, select=select)

Replaces a REST Consumer object.

Replaces a REST Consumer object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
rest_consumer_name = 'rest_consumer_name_example' # str | The restConsumerName of the REST Consumer.
body = semp_client.MsgVpnRestDeliveryPointRestConsumer() # MsgVpnRestDeliveryPointRestConsumer | The REST Consumer object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a REST Consumer object.
    api_response = api_instance.replace_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_rest_delivery_point_rest_consumer: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **str**| The restConsumerName of the REST Consumer. | 
 **body** | [**MsgVpnRestDeliveryPointRestConsumer**](MsgVpnRestDeliveryPointRestConsumer.md)| The REST Consumer object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerResponse**](MsgVpnRestDeliveryPointRestConsumerResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **replace_msg_vpn_topic_endpoint**
> MsgVpnTopicEndpointResponse replace_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, body, select=select)

Replaces a Topic Endpoint object.

Replaces a Topic Endpoint object. Any attribute missing from the request will be set to its default value, unless the user is not authorized to change its value in which case the missing attribute will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| owner||||x| permission||||x| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
topic_endpoint_name = 'topic_endpoint_name_example' # str | The topicEndpointName of the Topic Endpoint.
body = semp_client.MsgVpnTopicEndpoint() # MsgVpnTopicEndpoint | The Topic Endpoint object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Replaces a Topic Endpoint object.
    api_response = api_instance.replace_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->replace_msg_vpn_topic_endpoint: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **topic_endpoint_name** | **str**| The topicEndpointName of the Topic Endpoint. | 
 **body** | [**MsgVpnTopicEndpoint**](MsgVpnTopicEndpoint.md)| The Topic Endpoint object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn**
> MsgVpnResponse update_msg_vpn(msg_vpn_name, body, select=select)

Updates a Message VPN object.

Updates a Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicationBridgeAuthenticationBasicPassword|||x|| replicationEnabledQueueBehavior|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByValue|clearValue|setValue| EventThresholdByValue|setValue|clearValue| MsgVpn|authenticationBasicProfileName|authenticationBasicType| MsgVpn|authorizationProfileName|authorizationType| MsgVpn|eventPublishTopicFormatMqttEnabled|eventPublishTopicFormatSmfEnabled| MsgVpn|eventPublishTopicFormatSmfEnabled|eventPublishTopicFormatMqttEnabled| MsgVpn|replicationBridgeAuthenticationBasicClientUsername|replicationBridgeAuthenticationBasicPassword| MsgVpn|replicationBridgeAuthenticationBasicPassword|replicationBridgeAuthenticationBasicClientUsername| MsgVpn|replicationEnabledQueueBehavior|replicationEnabled|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: authenticationBasicEnabled|global/readwrite authenticationBasicProfileName|global/readwrite authenticationBasicRadiusDomain|global/readwrite authenticationBasicType|global/readwrite authenticationClientCertAllowApiProvidedUsernameEnabled|global/readwrite authenticationClientCertEnabled|global/readwrite authenticationClientCertMaxChainDepth|global/readwrite authenticationClientCertValidateDateEnabled|global/readwrite authenticationKerberosAllowApiProvidedUsernameEnabled|global/readwrite authenticationKerberosEnabled|global/readwrite authorizationLdapGroupMembershipAttributeName|global/readwrite authorizationProfileName|global/readwrite authorizationType|global/readwrite bridgingTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite bridgingTlsServerCertMaxChainDepth|global/readwrite bridgingTlsServerCertValidateDateEnabled|global/readwrite exportSubscriptionsEnabled|global/readwrite maxConnectionCount|global/readwrite maxEgressFlowCount|global/readwrite maxEndpointCount|global/readwrite maxIngressFlowCount|global/readwrite maxMsgSpoolUsage|global/readwrite maxSubscriptionCount|global/readwrite maxTransactedSessionCount|global/readwrite maxTransactionCount|global/readwrite replicationBridgeAuthenticationBasicClientUsername|global/readwrite replicationBridgeAuthenticationBasicPassword|global/readwrite replicationBridgeAuthenticationScheme|global/readwrite replicationBridgeCompressedDataEnabled|global/readwrite replicationBridgeEgressFlowWindowSize|global/readwrite replicationBridgeRetryDelay|global/readwrite replicationBridgeTlsEnabled|global/readwrite replicationBridgeUnidirectionalClientProfileName|global/readwrite replicationEnabled|global/readwrite replicationEnabledQueueBehavior|global/readwrite replicationQueueMaxMsgSpoolUsage|global/readwrite replicationRole|global/readwrite restTlsServerCertEnforceTrustedCommonNameEnabled|global/readwrite restTlsServerCertMaxChainDepth|global/readwrite restTlsServerCertValidateDateEnabled|global/readwrite sempOverMsgBusAdminClientEnabled|global/readwrite sempOverMsgBusAdminDistributedCacheEnabled|global/readwrite sempOverMsgBusAdminEnabled|global/readwrite sempOverMsgBusEnabled|global/readwrite sempOverMsgBusLegacyShowClearEnabled|global/readwrite sempOverMsgBusShowEnabled|global/readwrite serviceRestIncomingMaxConnectionCount|global/readwrite serviceRestIncomingPlainTextListenPort|global/readwrite serviceRestIncomingTlsListenPort|global/readwrite serviceRestOutgoingMaxConnectionCount|global/readwrite serviceSmfMaxConnectionCount|global/readwrite serviceWebMaxConnectionCount|global/readwrite    This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
body = semp_client.MsgVpn() # MsgVpn | The Message VPN object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Message VPN object.
    api_response = api_instance.update_msg_vpn(msg_vpn_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **body** | [**MsgVpn**](MsgVpn.md)| The Message VPN object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnResponse**](MsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_acl_profile**
> MsgVpnAclProfileResponse update_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, select=select)

Updates an ACL Profile object.

Updates an ACL Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName|x|x||| msgVpnName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
acl_profile_name = 'acl_profile_name_example' # str | The aclProfileName of the ACL Profile.
body = semp_client.MsgVpnAclProfile() # MsgVpnAclProfile | The ACL Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates an ACL Profile object.
    api_response = api_instance.update_msg_vpn_acl_profile(msg_vpn_name, acl_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_acl_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **acl_profile_name** | **str**| The aclProfileName of the ACL Profile. | 
 **body** | [**MsgVpnAclProfile**](MsgVpnAclProfile.md)| The ACL Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAclProfileResponse**](MsgVpnAclProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_authorization_group**
> MsgVpnAuthorizationGroupResponse update_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, select=select)

Updates an LDAP Authorization Group object.

Updates an LDAP Authorization Group object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| authorizationGroupName|x|x||| clientProfileName||||x| msgVpnName|x|x||| orderAfterAuthorizationGroupName|||x|| orderBeforeAuthorizationGroupName|||x||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnAuthorizationGroup|orderAfterAuthorizationGroupName||orderBeforeAuthorizationGroupName MsgVpnAuthorizationGroup|orderBeforeAuthorizationGroupName||orderAfterAuthorizationGroupName    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
authorization_group_name = 'authorization_group_name_example' # str | The authorizationGroupName of the LDAP Authorization Group.
body = semp_client.MsgVpnAuthorizationGroup() # MsgVpnAuthorizationGroup | The LDAP Authorization Group object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates an LDAP Authorization Group object.
    api_response = api_instance.update_msg_vpn_authorization_group(msg_vpn_name, authorization_group_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_authorization_group: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **authorization_group_name** | **str**| The authorizationGroupName of the LDAP Authorization Group. | 
 **body** | [**MsgVpnAuthorizationGroup**](MsgVpnAuthorizationGroup.md)| The LDAP Authorization Group object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnAuthorizationGroupResponse**](MsgVpnAuthorizationGroupResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_bridge**
> MsgVpnBridgeResponse update_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)

Updates a Bridge object.

Updates a Bridge object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| maxTtl||||x| msgVpnName|x|x||| remoteAuthenticationBasicClientUsername||||x| remoteAuthenticationBasicPassword|||x|x| remoteAuthenticationScheme||||x| remoteDeliverToOnePriority||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridge|remoteAuthenticationBasicClientUsername|remoteAuthenticationBasicPassword| MsgVpnBridge|remoteAuthenticationBasicPassword|remoteAuthenticationBasicClientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation. Requests which include the following attributes require greater access scope/level:   Attribute|Access Scope/Level :---|:---: tlsCipherSuiteList|global/readwrite    This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
body = semp_client.MsgVpnBridge() # MsgVpnBridge | The Bridge object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Bridge object.
    api_response = api_instance.update_msg_vpn_bridge(msg_vpn_name, bridge_name, bridge_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_bridge: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **body** | [**MsgVpnBridge**](MsgVpnBridge.md)| The Bridge object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeResponse**](MsgVpnBridgeResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_bridge_remote_msg_vpn**
> MsgVpnBridgeRemoteMsgVpnResponse update_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, select=select)

Updates a Remote Message VPN object.

Updates a Remote Message VPN object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: bridgeName|x|x||| bridgeVirtualRouter|x|x||| clientUsername||||x| compressedDataEnabled||||x| egressFlowWindowSize||||x| msgVpnName|x|x||| password|||x|x| remoteMsgVpnInterface|x|x||| remoteMsgVpnLocation|x|x||| remoteMsgVpnName|x|x||| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnBridgeRemoteMsgVpn|clientUsername|password| MsgVpnBridgeRemoteMsgVpn|password|clientUsername|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
bridge_name = 'bridge_name_example' # str | The bridgeName of the Bridge.
bridge_virtual_router = 'bridge_virtual_router_example' # str | The bridgeVirtualRouter of the Bridge.
remote_msg_vpn_name = 'remote_msg_vpn_name_example' # str | The remoteMsgVpnName of the Remote Message VPN.
remote_msg_vpn_location = 'remote_msg_vpn_location_example' # str | The remoteMsgVpnLocation of the Remote Message VPN.
remote_msg_vpn_interface = 'remote_msg_vpn_interface_example' # str | The remoteMsgVpnInterface of the Remote Message VPN.
body = semp_client.MsgVpnBridgeRemoteMsgVpn() # MsgVpnBridgeRemoteMsgVpn | The Remote Message VPN object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Remote Message VPN object.
    api_response = api_instance.update_msg_vpn_bridge_remote_msg_vpn(msg_vpn_name, bridge_name, bridge_virtual_router, remote_msg_vpn_name, remote_msg_vpn_location, remote_msg_vpn_interface, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_bridge_remote_msg_vpn: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **bridge_name** | **str**| The bridgeName of the Bridge. | 
 **bridge_virtual_router** | **str**| The bridgeVirtualRouter of the Bridge. | 
 **remote_msg_vpn_name** | **str**| The remoteMsgVpnName of the Remote Message VPN. | 
 **remote_msg_vpn_location** | **str**| The remoteMsgVpnLocation of the Remote Message VPN. | 
 **remote_msg_vpn_interface** | **str**| The remoteMsgVpnInterface of the Remote Message VPN. | 
 **body** | [**MsgVpnBridgeRemoteMsgVpn**](MsgVpnBridgeRemoteMsgVpn.md)| The Remote Message VPN object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnBridgeRemoteMsgVpnResponse**](MsgVpnBridgeRemoteMsgVpnResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_client_profile**
> MsgVpnClientProfileResponse update_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, select=select)

Updates a Client Profile object.

Updates a Client Profile object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName|x|x||| msgVpnName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent EventThresholdByPercent|clearPercent|setPercent| EventThresholdByPercent|setPercent|clearPercent|    A SEMP client authorized with a minimum access scope/level of \"global/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_profile_name = 'client_profile_name_example' # str | The clientProfileName of the Client Profile.
body = semp_client.MsgVpnClientProfile() # MsgVpnClientProfile | The Client Profile object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Client Profile object.
    api_response = api_instance.update_msg_vpn_client_profile(msg_vpn_name, client_profile_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_client_profile: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_profile_name** | **str**| The clientProfileName of the Client Profile. | 
 **body** | [**MsgVpnClientProfile**](MsgVpnClientProfile.md)| The Client Profile object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientProfileResponse**](MsgVpnClientProfileResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_client_username**
> MsgVpnClientUsernameResponse update_msg_vpn_client_username(msg_vpn_name, client_username, body, select=select)

Updates a Client Username object.

Updates a Client Username object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: aclProfileName||||x| clientProfileName||||x| clientUsername|x|x||| msgVpnName|x|x||| password|||x||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
client_username = 'client_username_example' # str | The clientUsername of the Client Username.
body = semp_client.MsgVpnClientUsername() # MsgVpnClientUsername | The Client Username object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Client Username object.
    api_response = api_instance.update_msg_vpn_client_username(msg_vpn_name, client_username, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_client_username: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **client_username** | **str**| The clientUsername of the Client Username. | 
 **body** | [**MsgVpnClientUsername**](MsgVpnClientUsername.md)| The Client Username object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnClientUsernameResponse**](MsgVpnClientUsernameResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_mqtt_session**
> MsgVpnMqttSessionResponse update_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)

Updates an MQTT Session object.

Updates an MQTT Session object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| owner||||x|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
body = semp_client.MsgVpnMqttSession() # MsgVpnMqttSession | The MQTT Session object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates an MQTT Session object.
    api_response = api_instance.update_msg_vpn_mqtt_session(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_mqtt_session: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **body** | [**MsgVpnMqttSession**](MsgVpnMqttSession.md)| The MQTT Session object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionResponse**](MsgVpnMqttSessionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_mqtt_session_subscription**
> MsgVpnMqttSessionSubscriptionResponse update_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, select=select)

Updates an MQTT Session Subscription object.

Updates an MQTT Session Subscription object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: mqttSessionClientId|x|x||| mqttSessionVirtualRouter|x|x||| msgVpnName|x|x||| subscriptionTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
mqtt_session_client_id = 'mqtt_session_client_id_example' # str | The mqttSessionClientId of the MQTT Session.
mqtt_session_virtual_router = 'mqtt_session_virtual_router_example' # str | The mqttSessionVirtualRouter of the MQTT Session.
subscription_topic = 'subscription_topic_example' # str | The subscriptionTopic of the MQTT Session Subscription.
body = semp_client.MsgVpnMqttSessionSubscription() # MsgVpnMqttSessionSubscription | The MQTT Session Subscription object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates an MQTT Session Subscription object.
    api_response = api_instance.update_msg_vpn_mqtt_session_subscription(msg_vpn_name, mqtt_session_client_id, mqtt_session_virtual_router, subscription_topic, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_mqtt_session_subscription: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **mqtt_session_client_id** | **str**| The mqttSessionClientId of the MQTT Session. | 
 **mqtt_session_virtual_router** | **str**| The mqttSessionVirtualRouter of the MQTT Session. | 
 **subscription_topic** | **str**| The subscriptionTopic of the MQTT Session Subscription. | 
 **body** | [**MsgVpnMqttSessionSubscription**](MsgVpnMqttSessionSubscription.md)| The MQTT Session Subscription object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnMqttSessionSubscriptionResponse**](MsgVpnMqttSessionSubscriptionResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_queue**
> MsgVpnQueueResponse update_msg_vpn_queue(msg_vpn_name, queue_name, body, select=select)

Updates a Queue object.

Updates a Queue object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: accessType||||x| msgVpnName|x|x||| owner||||x| permission||||x| queueName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
queue_name = 'queue_name_example' # str | The queueName of the Queue.
body = semp_client.MsgVpnQueue() # MsgVpnQueue | The Queue object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Queue object.
    api_response = api_instance.update_msg_vpn_queue(msg_vpn_name, queue_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_queue: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **queue_name** | **str**| The queueName of the Queue. | 
 **body** | [**MsgVpnQueue**](MsgVpnQueue.md)| The Queue object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnQueueResponse**](MsgVpnQueueResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_replicated_topic**
> MsgVpnReplicatedTopicResponse update_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, body, select=select)

Updates a Replicated Topic object.

Updates a Replicated Topic object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| replicatedTopic|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
replicated_topic = 'replicated_topic_example' # str | The replicatedTopic of the Replicated Topic.
body = semp_client.MsgVpnReplicatedTopic() # MsgVpnReplicatedTopic | The Replicated Topic object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Replicated Topic object.
    api_response = api_instance.update_msg_vpn_replicated_topic(msg_vpn_name, replicated_topic, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_replicated_topic: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **replicated_topic** | **str**| The replicatedTopic of the Replicated Topic. | 
 **body** | [**MsgVpnReplicatedTopic**](MsgVpnReplicatedTopic.md)| The Replicated Topic object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnReplicatedTopicResponse**](MsgVpnReplicatedTopicResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_rest_delivery_point**
> MsgVpnRestDeliveryPointResponse update_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, select=select)

Updates a REST Delivery Point object.

Updates a REST Delivery Point object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: clientProfileName||||x| msgVpnName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
body = semp_client.MsgVpnRestDeliveryPoint() # MsgVpnRestDeliveryPoint | The REST Delivery Point object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a REST Delivery Point object.
    api_response = api_instance.update_msg_vpn_rest_delivery_point(msg_vpn_name, rest_delivery_point_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_rest_delivery_point: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **body** | [**MsgVpnRestDeliveryPoint**](MsgVpnRestDeliveryPoint.md)| The REST Delivery Point object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointResponse**](MsgVpnRestDeliveryPointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_rest_delivery_point_queue_binding**
> MsgVpnRestDeliveryPointQueueBindingResponse update_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, select=select)

Updates a Queue Binding object.

Updates a Queue Binding object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| queueBindingName|x|x||| restDeliveryPointName|x|x|||    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
queue_binding_name = 'queue_binding_name_example' # str | The queueBindingName of the Queue Binding.
body = semp_client.MsgVpnRestDeliveryPointQueueBinding() # MsgVpnRestDeliveryPointQueueBinding | The Queue Binding object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Queue Binding object.
    api_response = api_instance.update_msg_vpn_rest_delivery_point_queue_binding(msg_vpn_name, rest_delivery_point_name, queue_binding_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_rest_delivery_point_queue_binding: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **queue_binding_name** | **str**| The queueBindingName of the Queue Binding. | 
 **body** | [**MsgVpnRestDeliveryPointQueueBinding**](MsgVpnRestDeliveryPointQueueBinding.md)| The Queue Binding object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointQueueBindingResponse**](MsgVpnRestDeliveryPointQueueBindingResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_rest_delivery_point_rest_consumer**
> MsgVpnRestDeliveryPointRestConsumerResponse update_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, select=select)

Updates a REST Consumer object.

Updates a REST Consumer object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: authenticationHttpBasicPassword|||x|x| authenticationHttpBasicUsername||||x| authenticationScheme||||x| msgVpnName|x|x||| outgoingConnectionCount||||x| remoteHost||||x| remotePort||||x| restConsumerName|x|x||| restDeliveryPointName|x|x||| tlsCipherSuiteList||||x| tlsEnabled||||x|    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicPassword|authenticationHttpBasicUsername| MsgVpnRestDeliveryPointRestConsumer|authenticationHttpBasicUsername|authenticationHttpBasicPassword| MsgVpnRestDeliveryPointRestConsumer|remotePort|tlsEnabled| MsgVpnRestDeliveryPointRestConsumer|tlsEnabled|remotePort|    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.0.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
rest_delivery_point_name = 'rest_delivery_point_name_example' # str | The restDeliveryPointName of the REST Delivery Point.
rest_consumer_name = 'rest_consumer_name_example' # str | The restConsumerName of the REST Consumer.
body = semp_client.MsgVpnRestDeliveryPointRestConsumer() # MsgVpnRestDeliveryPointRestConsumer | The REST Consumer object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a REST Consumer object.
    api_response = api_instance.update_msg_vpn_rest_delivery_point_rest_consumer(msg_vpn_name, rest_delivery_point_name, rest_consumer_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_rest_delivery_point_rest_consumer: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **rest_delivery_point_name** | **str**| The restDeliveryPointName of the REST Delivery Point. | 
 **rest_consumer_name** | **str**| The restConsumerName of the REST Consumer. | 
 **body** | [**MsgVpnRestDeliveryPointRestConsumer**](MsgVpnRestDeliveryPointRestConsumer.md)| The REST Consumer object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnRestDeliveryPointRestConsumerResponse**](MsgVpnRestDeliveryPointRestConsumerResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update_msg_vpn_topic_endpoint**
> MsgVpnTopicEndpointResponse update_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, body, select=select)

Updates a Topic Endpoint object.

Updates a Topic Endpoint object. Any attribute missing from the request will be left unchanged.   Attribute|Identifying|Read-Only|Write-Only|Requires-Disable|Deprecated :---|:---:|:---:|:---:|:---:|:---: msgVpnName|x|x||| owner||||x| permission||||x| topicEndpointName|x|x|||    The following attributes in the request may only be provided in certain combinations with other attributes:   Class|Attribute|Requires|Conflicts :---|:---|:---|:--- EventThreshold|clearPercent|setPercent|clearValue, setValue EventThreshold|clearValue|setValue|clearPercent, setPercent EventThreshold|setPercent|clearPercent|clearValue, setValue EventThreshold|setValue|clearValue|clearPercent, setPercent    A SEMP client authorized with a minimum access scope/level of \"vpn/readwrite\" is required to perform this operation.  This has been available since 2.1.0.

### Example 
```python
import time
import semp_client
from semp_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basicAuth
semp_client.configuration.username = 'YOUR_USERNAME'
semp_client.configuration.password = 'YOUR_PASSWORD'

# create an instance of the API class
api_instance = semp_client.MsgVpnApi()
msg_vpn_name = 'msg_vpn_name_example' # str | The msgVpnName of the Message VPN.
topic_endpoint_name = 'topic_endpoint_name_example' # str | The topicEndpointName of the Topic Endpoint.
body = semp_client.MsgVpnTopicEndpoint() # MsgVpnTopicEndpoint | The Topic Endpoint object's attributes.
select = ['select_example'] # list[str] | Include in the response only selected attributes of the object. See [Select](#select \"Description of the syntax of the `select` parameter\"). (optional)

try: 
    # Updates a Topic Endpoint object.
    api_response = api_instance.update_msg_vpn_topic_endpoint(msg_vpn_name, topic_endpoint_name, body, select=select)
    pprint(api_response)
except ApiException as e:
    print "Exception when calling MsgVpnApi->update_msg_vpn_topic_endpoint: %s\n" % e
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **msg_vpn_name** | **str**| The msgVpnName of the Message VPN. | 
 **topic_endpoint_name** | **str**| The topicEndpointName of the Topic Endpoint. | 
 **body** | [**MsgVpnTopicEndpoint**](MsgVpnTopicEndpoint.md)| The Topic Endpoint object&#39;s attributes. | 
 **select** | [**list[str]**](str.md)| Include in the response only selected attributes of the object. See [Select](#select \&quot;Description of the syntax of the &#x60;select&#x60; parameter\&quot;). | [optional] 

### Return type

[**MsgVpnTopicEndpointResponse**](MsgVpnTopicEndpointResponse.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

