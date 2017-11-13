# Load the gem
require 'sempclient_samplelib'

class ManageVpn
  
  DEFAULT_CLIENTPROFILE = "default"
  DEFAULT_CLIENTUSERNAME = "default"

  def initialize(host_and_port, user, password)
    SempClientSampleLib.configure do |config|
      config.scheme = 'http'
      config.host = host_and_port
      config.base_path = 'SEMP/v2/config'
      config.username = user
      config.password = password
    end
    @api_instance = SempClientSampleLib::MsgVpnApi.new
  end

  def create_message_vpn(message_vpn_name)
    STDOUT.puts "Creating Message-VPN: #{message_vpn_name}...\n"
    msg_vpn = SempClientSampleLib::MsgVpn.new
    msg_vpn.msg_vpn_name = message_vpn_name
    msg_vpn.authentication_basic_type = "internal"
    msg_vpn.max_msg_spool_usage = 1500
    msg_vpn.enabled = true
    @api_instance.create_msg_vpn(msg_vpn)
  end

  def update_default_client_profile_for_persistent_messaging(message_vpn_name)
    STDOUT.puts "Modifying Client-Profile for persistent messaging...\n"
    msg_vpn_client_profile = SempClientSampleLib::MsgVpnClientProfile.new
    msg_vpn_client_profile.allow_guaranteed_msg_send_enabled = true
    msg_vpn_client_profile.allow_guaranteed_msg_receive_enabled = true
    msg_vpn_client_profile.allow_guaranteed_endpoint_create_enabled = true
    @api_instance.update_msg_vpn_client_profile(
      message_vpn_name, DEFAULT_CLIENTPROFILE, msg_vpn_client_profile)
  end
  
  def setup_client_username(message_vpn_name, client_name, client_password)
    STDOUT.puts "Setting up Client-Username: #{client_name} with password...\n"
    msg_vpn_client_username = SempClientSampleLib::MsgVpnClientUsername.new
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
  end

  def create_queue(message_vpn_name, queue_name)
    STDOUT.puts "Creating persistent Queue: #{queue_name}...\n"
    msg_vpn_queue = SempClientSampleLib::MsgVpnQueue.new
    msg_vpn_queue.queue_name = queue_name
    msg_vpn_queue.permission = "delete"
    msg_vpn_queue.ingress_enabled = true
    msg_vpn_queue.egress_enabled = true
    msg_vpn_queue.access_type = "non-exclusive"
    @api_instance.create_msg_vpn_queue(message_vpn_name, msg_vpn_queue)
  end

  def delete_message_vpn(message_vpn_name)
    STDOUT.puts "Deleting Message-VPN: #{message_vpn_name}...\n"
    # Prerequisite for delete VPN is to remove all queues
    resp = @api_instance.get_msg_vpn_queues(message_vpn_name)
    if resp.data.length > 0
      STDOUT.puts "Message-VPN contains one or more queues, deleting them first:\n"
      resp.data.each do |queue|
        delete_queue(message_vpn_name, queue.queue_name)
      end  
    end
    @api_instance.delete_msg_vpn(message_vpn_name)
  end

  def delete_queue(message_vpn_name, queue_name)
    STDOUT.puts "Deleting Queue: #{queue_name}...\n"
    @api_instance.delete_msg_vpn_queue(message_vpn_name, queue_name)
  end  
end

if __FILE__ == $0
  begin
    # Modify these params as needed
    vpn_user_name = "default";
    vpn_user_password = "password";
    test_queue_name = "testQueue";
    usage = "\nUsage: manage_vpn [create | delete] <host:port> <management_user> <management_password> <vpnname>" +
            "\nEx: manage_vpn create <host:port> <management_user> <management_password> <vpnname>" +
            "\n        Create a message-vpn and add a sample queue: testQueue" +
            "\n    manage_vpn delete <host:port> <management_user> <management_password> <vpnname>" +
            "\n        Delete the message-vpn"
  
    # Check command line arguments
    if ARGV.length < 5
      abort(usage)
    end
    command = ARGV[0]
    host_and_port = ARGV[1]
    vmr_user = ARGV[2]
    vmr_password = ARGV[3]
    message_vpn_name = ARGV[4]
    
    app = ManageVpn.new(host_and_port, vmr_user, vmr_password)
    
    case command
    when "create"
      app.create_message_vpn(message_vpn_name)
      app.update_default_client_profile_for_persistent_messaging(message_vpn_name)
      app.setup_client_username(message_vpn_name, vpn_user_name, vpn_user_password)
      app.create_queue(message_vpn_name, test_queue_name)
    when "delete"
      app.delete_message_vpn(message_vpn_name)
    else
      abort("Invalid command: #{command}" + usage)
    end
        
  rescue SempClientSampleLib::ApiError  => e
    puts "Error during operation. Details: #{e.response_body}"
  end
end