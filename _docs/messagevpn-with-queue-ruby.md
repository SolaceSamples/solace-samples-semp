---
layout: tutorials
title: Message VPN with Queue – Ruby
summary: Sample integration into a Ruby-based management tool using the SEMP Ruby Client Library
icon: management-integration-ruby.svg
icon-height: 90px
icon-width: 104px
---

This tutorial will guide you through the steps of creating a sample Ruby-based tool to administer basic message-VPNs and queues.

## Goals

The goals of this tutorial are to:

* Introduce the Solace Element Management Protocol (SEMP) object model for Solace message-VPNs and its resources
* Demonstrate how to integrate the management of Solace Messaging into Ruby-based DevOps tools using the SEMP Ruby Client Library

Let's review the admin objectives:

* **Creating message-VPNs** is a convenient way to slice a Solace Messaging system to separated independent virtual routers, multiplying its use. There are many use cases including security, controlled share of functionality and capacity. For developers, one way to make use of a message-VPN is to share a VMR with everyone having a dedicated message-VPN for development use. Once a message-VPN is created, the client can connect to it with the assigned username and password using basic internal authentication.
* Another common objective is to **create a persistent queue** as a message endpoint in the particular message-VPN. This resource is only visible to those connected to this message-VPN and it can store incoming guaranteed messages until consumed.
* The administrator may also want the ability to **delete a queue** or
* **delete a message-VPN** when no longer needed.

To accomplish this, we will create a set of tasks as building blocks that match the SEMP object model of the Solace Messaging and use a simple framework to demonstrate the integration and orchestration of these tasks to achieve above objectives. We will utilize the SEMP Ruby Client Library to implement these tasks which is a Ruby wrapper for the SEMP REST API introduced in the [Basic Operations - curl]({{ site.baseurl }}/curl) tutorial.

### Assumptions

This tutorial assumes that you have access to a running Solace VMR with the following configuration:

* A management user authorized with a minimum access scope level of *global/read-write*.

One simple way to get access to a Solace Messaging system is to start a Solace VMR as outlined [here]({{ site.docs-vmr-setup }}){:target="_top"}.

We also assume that Ruby is installed.

### Trying it yourself

This tutorial is available in [GitHub]({{ site.repository }}){:target="_blank"} along with the other [Solace SEMP Getting Started Examples]({{ site.tutorials-home }}){:target="_blank"}.

At the end, this tutorial walks through downloading and running the sample from source.

{% include_relative assets/solaceMessaging.md %}

## The SEMP object model of Solace message-VPNs and VPN resources

Here we introduce some important basic concepts that are required for the implementation of Solace Messaging management tasks using SEMP.

As described in the introduction, message-VPNs provide isolated messaging domains for exclusive use. You can read more about message-VPNs in the [Solace Messaging Concepts]({{ site.docs-router-concepts }}){:target="_top"} document.

Solace Messaging management is divided into Router-global level and individual Message-VPN level management. A management user with *global/read-write* access scope is authorized for all router and all message-VPN level configurations, such as global configuration or administering a message-VPN. Conversely, *VPN/read-write* access scope only allows for management of objects that have an effect within the assigned VPN, such as configuring a queue. In this tutorial, we will use a management user with *global/read-write* access scope for all configurations. For more details, refer to the [Management User Authentication/Authorization]({{ site.docs-mgmt-user-aa }}){:target="_top"} documentation.

![]({{ site.baseurl }}/assets/images/message-vpn-semp-objects.png)

Clients can connect to a message-VPN and use its resources after proper authentication and authorization, controlled by the properties of the following SEMP objects on the Solace Messaging:

* The [Message-VPN]({{ site.docs-msg-vpn }}){:target="_top"} object defines the type and details of client authentication applied and restrictions to the combined resource usage of all VPN clients.
* A [Client Profile]({{ site.docs-client-profile }}){:target="_top"} object within a message-VPN defines resource usage restrictions applied to individual clients.
* An [ACL (Access Control List) Profile]({{ site.docs-acl-profile }}){:target="_top"} object within a message-VPN defines access restrictions by listing allowance or denial of which clients can connect and to which topics and queues.
* A [Client Username]({{ site.docs-client-username }}){:target="_top"} object within a message-VPN has an associated Client Profile and an ACL Profile. The username provided by the connecting client will be the Client Username applied to that connection or if it is not found then the default Client Username will be applied. The password property will be used if internal client authentication type has been specified for the message-VPN.

Once connected, clients can send messages to message-VPN endpoints, represented as [Queue]({{ site.docs-queues }}){:target="_top"} or [Topic Endpoint]({{ site.docs-dtes }}){:target="_top"} SEMP objects within a message-VPN.

You can learn more about above SEMP objects by following the provided links to the Solace documentation.

## Building blocks using the SEMP Ruby Client Library

Now we can start to implement the tasks as building blocks for the sample management tool.

### Initialize the SEMP Ruby Client Library

We need to create an instance of the SEMP Ruby Client Library, initialized with the target host, management port and management credentials.

```ruby
require 'semp_client'

SempClient.configure do |config|
  config.scheme = 'http'
  config.host = host_and_port          # formatted as host:port
  config.base_path = 'SEMP/v2/config'
  config.username = user
  config.password = password
end
@api_instance = SempClient::MsgVpnApi.new
```


### Create a new message-VPN

The first task is to create a new message-VPN with a name. Consulting the Solace documentation by following above [Message-VPN]({{ site.docs-msg-vpn }}){:target="_top"} link, we determine that it shall be configured for basic internal authentication, have the storage size increased for persistent message queues from default 0 and have it enabled. Tip: the [SolAdmin]({{ site.docs-soladmin-home }}){:target="_top"} management GUI tool can be used to show the default values for new created objects. If in doubt, try the intended management operation using SolAdmin, which can be [downloaded here]({{ site.docs-solace-downloads }}){:target="_top"}.

To understand how to implement this, let’s consult the [SEMP online API documentation]({{ site.docs-api }}){:target="_top"}:
- scrolling down to msgVPN and opening the `List Operations` link will show the color-coded options for all available operations. Clicking on `POST /msgVpns - Creates a Message VPN object` will open up the details. Scrolling down to `MsgVpn {`, here we can find the names of the attributes and we can derive the Ruby instance variable names using the rule: lowercase with words separated by underscores. For example, we look up the attribute `maxMsgSpoolUsage (integer)` and determine the instance variable name to be `max_msg_spool_usage`. Additionally, you can explore the SEMP Ruby Client Library specific documentation included in the `solace-sample-semp/ruby/docs` folder once you build the ruby project.

```ruby
# Create message-vpn
msg_vpn = SempClient::MsgVpn.new
msg_vpn.msg_vpn_name = message_vpn_name
msg_vpn.authentication_basic_type = "internal"
msg_vpn.max_msg_spool_usage = 1500
msg_vpn.enabled = true
@api_instance.create_msg_vpn(msg_vpn)
```

When a new message-VPN management object is created on the Solace Messaging system, a new `default` Client Username object will also be created automatically, associated with a new `default` Client Profile and a new `default` ACL Profile object. We will use these created default objects as a starting point for further configuration as they require fewer properties to modify. Alternatively, it is possible to create new non-default objects if a more complex structure is required.

The `default` ACL Profile doesn’t define any access restrictions so there is no need to modify that.

### Modify the default Client Profile

However, the `default` Client Profile needs to be modified to enable support for guaranteed messaging:

```ruby
# Modify existing default client-profile
DEFAULT_CLIENTUSERNAME = "default"
msg_vpn_client_profile = SempClient::MsgVpnClientProfile.new
msg_vpn_client_profile.allow_guaranteed_msg_send_enabled = true
msg_vpn_client_profile.allow_guaranteed_msg_receive_enabled = true
msg_vpn_client_profile.allow_guaranteed_endpoint_create_enabled = true
@api_instance.update_msg_vpn_client_profile(
  message_vpn_name, DEFAULT_CLIENTPROFILE, msg_vpn_client_profile)
```

### Create new or modify the default Client Username

The Client Username needs to get a password property assigned and enabled.  If we opt for using a specific username, we need to create a new Client Username. Note that unless a different Client Profile and ACL profile is assigned, even a newly created Client Username will be associated with the default profiles.

```ruby
# Create or modify client-username
msg_vpn_client_username = SempClient::MsgVpnClientUsername.new
msg_vpn_client_username.password = client_password
msg_vpn_client_username.enabled = true
if client_name != DEFAULT_CLIENTUSERNAME
  msg_vpn_client_username.client_username = client_name
  @api_instance.create_msg_vpn_client_username(
        message_vpn_name, msg_vpn_client_username)
else
  @api_instance.update_msg_vpn_client_username(
        message_vpn_name, DEFAULT_CLIENTUSERNAME, msg_vpn_client_username)
end
```

### Create a Queue

Creating a Queue requires a unique name, permissions set, ingress enabled for receiving messages and egress enabled to send messages. Additional properties can be set to non-default values, as for the other objects. For example, in this tutorial we will change the default Exclusive access type of the created queue to Non-exclusive, which means that multiple clients can connect to this queue and messages will be delivered in a round-robin scheme, which can be useful for load-balancing between slow consumers. For more information of what is available, see the documentation of Configuring Queues and Configuring Topic Endpoints.

```ruby
# Create queue
msg_vpn_queue = SempClient::MsgVpnQueue.new
msg_vpn_queue.queue_name = queue_name
msg_vpn_queue.permission = "delete"
msg_vpn_queue.ingress_enabled = true
msg_vpn_queue.egress_enabled = true
msg_vpn_queue.access_type = "non-exclusive"
@api_instance.create_msg_vpn_queue(message_vpn_name, msg_vpn_queue)
```

### Delete a Queue

Deleting a Queue is straightforward:

```ruby
# Delete queue
@api_instance.delete_msg_vpn_queue(message_vpn_name, queue_name)
```

### Delete a message-VPN

Deleting a message-VPN requires first all queue resources assigned to the message-VPN to be deleted. We do that by querying the collection of queues within the message-VPN to be deleted and then iterating through the resulting list.

```ruby
# Prerequisite for delete VPN is to remove all queues
resp = @api_instance.get_msg_vpn_queues(message_vpn_name)
if resp.data.length > 0
  STDOUT.puts "Message-VPN contains one or more queues, deleting them first:\n"
  resp.data.each do |queue|
    delete_queue(message_vpn_name, queue.queue_name)
  end  
end
@api_instance.delete_msg_vpn(message_vpn_name)
```

### Pulling it all together

We can now implement the admin objectives in the sample Ruby-based command-line manageVPN tool. In this example, the tool will simply parse command-line parameters in its main method and perform the admin action passed using one or more of the building blocks. Real-world applications would likely use similar building blocks as a Ruby-library.

```ruby
    case command
    when "create"
      app.create_message_vpn(message_vpn_name)
      app.update_default_client_profile_for_persistent_messaging(message_vpn_name)
      app.setup_client_username(message_vpn_name, vpn_user_name, vpn_user_password)
      app.create_queue(message_vpn_name, test_queue_name)
    when "delete"
      app.delete_message_vpn(message_vpn_name)
    end
```

## Summary

The full source code for this example is available in [GitHub]({{ site.repository-ruby }}){:target="_blank"}. If you combine the example source code shown above, it results in the following source:

* [manage_vpn.rb]({{ site.repository }}/blob/master/ruby/samples/manage_vpn.rb){:target="_blank"}

### Getting the Source

Clone the GitHub repository containing the Solace samples.

```
git clone {{ site.repository }}
cd {{ site.repository | split: '/' | last}}/ruby
```

### Builing and Installing the SEMP Ruby Client Library

Build the project using Gradle.

```
../gradlew build
```

Building and installing the library as a gem is simple, just run:

```
gem build semp_client_lib.gemspec
gem install --dev ./semp_client_lib-1.0.0.gem
```
(or `sudo gem install --dev ./semp_client_lib-1.0.0.gem` to install the package for all users)

Note: you may need to install 'respec' if you see the error 'rspec requires rspec-expectations (~> 3.7.0)', the command `gem install respec` will do that.

The instructions in this tutorial assume that you are using a Linux shell. If your environment differs, adapt the instructions.

### Running the Sample

From the `solace-samples-semp/ruby` directory, replace `<semp_base_path>` by the SEMP base path, for example: `http://solacevmr:8080/SEMP/v2/config`, replace the `<management_user>` and `<management_password>` with your credentials and execute:

```
ruby -Ilib samples/manage_vpn.rb create <semp_base_path> <management_user> <management_password> myNewVPN
```
Note: you may need to run with `sudo` for `LoadError` exception.

This will create a new message-VPN called `myNewVPN` and also create a sample queue called `testQueue`. The credentials to connect to the new message-VPN are *default:password*, which can also be changed in the code if required.

A message-VPN can also be deleted when no longer needed:

```
ruby -Ilib samples/manage_vpn.rb delete <semp_base_path> <management_user> <management_password> myNewVPN
```

This will detect that the queue `testQueue` and possibly other queues are still there so it will delete these first and then it will delete `myNewVPN`.

Now it is possible to easily create as many message-VPNs as required and share an installed VMR with other developers. To try the new message-VPN, use the [Persistence with Queues]({{ site.solace-samples-jms-queues }}){:target="_top"} Java sample (which requires Java installed). Follow the instructions to get the source, build and then run from the appropriate directory with the additional optional parameter of the message-VPN name:

```
./build/staged/bin/queueProducer <HOST> myNewVPN
```

If you have any issues using SEMP, check the [Solace community.]({{ site.solace-community }}){:target="_top"} for answers to common issues.
