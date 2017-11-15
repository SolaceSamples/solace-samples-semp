
## Get Solace Messaging

This tutorial requires access Solace Messaging and requires that you know several connectivity properties about your Solace Messaging. Specifically you need to know the following:

<table>
  <tr>
    <th>Resource</th>
    <th>Value</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Host</td>
    <td>String</td>
    <td>This is the address clients use when connecting to the Solace Messaging to send and receive messages. (Format: <code>DNS_NAME:Port</code> or <code>IP:Port</code>)</td>
  </tr>
  <tr>
    <td>Message VPN</td>
    <td>String</td>
    <td>The Solace Messaging Message VPN that this client should connect to. </td>
  </tr>
  <tr>
    <td>Management Username</td>
    <td>String</td>
    <td>The management username.</td>
  </tr>
  <tr>
    <td>Management Password</td>
    <td>String</td>
    <td>The management password.</td>
  </tr>
</table>

### Option 1: Start a Solace VMR

* Follow [these instructions]({{ site.docs-vmr-setup }}){:target="_top"} to start the Solace VMR in leading Clouds, Container Platforms or Hypervisors. The tutorials outline where to download and and how to install the Solace VMR.
* The messaging connectivity information are the following:
    * Host: \<public_ip> (IP address assigned to the VMR in tutorial instructions)
    * Message VPN: default
    * A management username and password to access the router

### Option 2: Get access to a Solace appliance

* Contact your Solace appliance administrators and obtain the following:
    * A Solace Message-VPN where you can produce and consume direct and persistent messages
    * The host name or IP address of the Solace appliance hosting your Message-VPN
    * Management Username: sampleUser (for example: admin)
    * Management Password: samplePassword (for example: admin)