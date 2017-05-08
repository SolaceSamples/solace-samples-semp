import sys
import semp_client
from semp_client.rest import ApiException

class ManageVpn:
    
    DEFAULT_CLIENTPROFILE = 'default'
    DEFAULT_CLIENTUSERNAME = 'default'
   
    def __init__(self, host_and_port, user, password):
        semp_client.configuration.host = 'http://' + host_and_port + '/SEMP/v2/config'
        semp_client.configuration.username = user
        semp_client.configuration.password = password
        self.api_instance = semp_client.MsgVpnApi()
 
    def create_message_vpn(self, message_vpn_name):
        print 'Creating Message-VPN: ' + message_vpn_name + '\n'
        msg_vpn = semp_client.MsgVpn()
        msg_vpn.msg_vpn_name = message_vpn_name
        msg_vpn.authentication_basic_type = "internal"
        msg_vpn.max_msg_spool_usage = 1500
        msg_vpn.enabled = True
        self.api_instance.create_msg_vpn(msg_vpn)
 
    def update_default_client_profile_for_persistent_messaging(self, message_vpn_name):
        print 'Modifying Client-Profile for persistent messaging...\n'
        msg_vpn_client_profile = semp_client.MsgVpnClientProfile()
        msg_vpn_client_profile.allow_guaranteed_msg_send_enabled = True
        msg_vpn_client_profile.allow_guaranteed_msg_receive_enabled = True
        msg_vpn_client_profile.allow_guaranteed_endpoint_create_enabled = True
        self.api_instance.update_msg_vpn_client_profile(
          message_vpn_name, self.DEFAULT_CLIENTPROFILE, msg_vpn_client_profile)
  
    def setup_client_username(self, message_vpn_name, client_name, client_password):
        print 'Setting up Client-Username: ' + client_name + ' with password...\n'
        msg_vpn_client_username = semp_client.MsgVpnClientUsername()
        msg_vpn_client_username.password = client_password
        msg_vpn_client_username.enabled = True
        if client_name != self.DEFAULT_CLIENTUSERNAME:
            msg_vpn_client_username.client_username = client_name
            self.api_instance.create_msg_vpn_client_username(
                message_vpn_name, msg_vpn_client_username)
        else:
            self.api_instance.update_msg_vpn_client_username(
                message_vpn_name, self.DEFAULT_CLIENTUSERNAME, msg_vpn_client_username)

    def create_queue(self, message_vpn_name, queue_name):
        print 'Creating persistent Queue: ' + queue_name + '...\n'
        msg_vpn_queue = semp_client.MsgVpnQueue()
        msg_vpn_queue.queue_name = queue_name
        msg_vpn_queue.permission = "delete"
        msg_vpn_queue.ingress_enabled = True
        msg_vpn_queue.egress_enabled = True
        msg_vpn_queue.access_type = "non-exclusive"
        self.api_instance.create_msg_vpn_queue(
            message_vpn_name, msg_vpn_queue)

    def delete_message_vpn(self, message_vpn_name):
        print 'Deleting Message-VPN: ' + message_vpn_name + '...\n'
        # Prerequisite for delete VPN is to remove all queues
        resp = self.api_instance.get_msg_vpn_queues(message_vpn_name)
        if len(resp.data) > 0:
            print 'Message-VPN contains one or more queues, deleting them first:\n'
            for queue in resp.data:
                self.delete_queue(message_vpn_name, queue.queue_name)
        self.api_instance.delete_msg_vpn(message_vpn_name)

    def delete_queue(self, message_vpn_name, queue_name):
        print 'Deleting Queue: ' + queue_name + '...\n'
        self.api_instance.delete_msg_vpn_queue(message_vpn_name, queue_name)

if __name__ == '__main__':
    try: 
        # Modify these params as needed
        vmr_user = 'admin';
        vmr_password = 'admin';
        vpn_user_name = 'default';
        vpn_user_password = 'password';
        test_queue_name = 'testQueue';
        usage = ('\nUsage: manage_vpn [create | delete] <host:port> <vpnname>'
                '\nEx: manage_vpn create <host:port> <vpnname>'
                '\n        Create a message-vpn and add a sample queue: testQueue'
                '\n    manage_vpn delete <host:port> <vpnname>'
                '\n        Delete the message-vpn')
        
        # Check command line arguments
        if len(sys.argv) < 3:
            sys.exit(usage)
        command = sys.argv[1]
        host_and_port = sys.argv[2]
        message_vpn_name = sys.argv[3]  
        
        app = ManageVpn(host_and_port, vmr_user, vmr_password)
        
        if command == "create":
            app.create_message_vpn(message_vpn_name)
            app.update_default_client_profile_for_persistent_messaging(message_vpn_name)
            app.setup_client_username(message_vpn_name, vpn_user_name, vpn_user_password)
            app.create_queue(message_vpn_name, test_queue_name)
        elif command == "delete":
            app.delete_message_vpn(message_vpn_name)
        else:
            sys.exit("Invalid command: " + command + usage)
            
    except ApiException as e:
        print 'Error during operation. Details: %s\n' % e.body
