import sys
import sempclient_samplelib
from sempclient_samplelib.rest import ApiException

class ManageVpn:
    
    DEFAULT_CLIENTPROFILE = 'default'
    DEFAULT_CLIENTUSERNAME = 'default'
   
    def __init__(self, host_and_port, user, password):
        sempclient_samplelib.configuration.host = 'http://' + host_and_port + '/SEMP/v2/config'
        sempclient_samplelib.configuration.username = user
        sempclient_samplelib.configuration.password = password
        self.api_instance = sempclient_samplelib.MsgVpnApi()
 
    def create_message_vpn(self, message_vpn_name):
        print 'Creating Message-VPN: ' + message_vpn_name
        msg_vpn = sempclient_samplelib.MsgVpn()
        msg_vpn.msg_vpn_name = message_vpn_name
        msg_vpn.authentication_basic_type = "internal"
        msg_vpn.max_msg_spool_usage = 1500
        msg_vpn.enabled = True
        self.api_instance.create_msg_vpn(msg_vpn)
 
    def update_default_client_profile_for_persistent_messaging(self, message_vpn_name):
        print 'Modifying Client-Profile for persistent messaging...'
        msg_vpn_client_profile = sempclient_samplelib.MsgVpnClientProfile()
        msg_vpn_client_profile.allow_guaranteed_msg_send_enabled = True
        msg_vpn_client_profile.allow_guaranteed_msg_receive_enabled = True
        msg_vpn_client_profile.allow_guaranteed_endpoint_create_enabled = True
        self.api_instance.update_msg_vpn_client_profile(
          message_vpn_name, self.DEFAULT_CLIENTPROFILE, msg_vpn_client_profile)
  
    def setup_client_username(self, message_vpn_name, client_name, client_password):
        print 'Setting up Client-Username: ' + client_name + ' with password...'
        msg_vpn_client_username = sempclient_samplelib.MsgVpnClientUsername()
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
        print 'Creating persistent Queue: ' + queue_name + '...'
        msg_vpn_queue = sempclient_samplelib.MsgVpnQueue()
        msg_vpn_queue.queue_name = queue_name
        msg_vpn_queue.permission = "delete"
        msg_vpn_queue.ingress_enabled = True
        msg_vpn_queue.egress_enabled = True
        msg_vpn_queue.access_type = "non-exclusive"
        self.api_instance.create_msg_vpn_queue(
            message_vpn_name, msg_vpn_queue)

    def delete_message_vpn(self, message_vpn_name):
        print 'Deleting Message-VPN: ' + message_vpn_name + '...'
        # Prerequisite for delete VPN is to remove all queues
        resp = self.api_instance.get_msg_vpn_queues(message_vpn_name)
        if len(resp.data) > 0:
            print 'Message-VPN contains one or more queues, deleting them first:'
            for queue in resp.data:
                self.delete_queue(message_vpn_name, queue.queue_name)
        self.api_instance.delete_msg_vpn(message_vpn_name)

    def delete_queue(self, message_vpn_name, queue_name):
        print 'Deleting Queue: ' + queue_name + '...'
        self.api_instance.delete_msg_vpn_queue(message_vpn_name, queue_name)

if __name__ == '__main__':
    try: 
        # Modify these params as needed
        vpn_user_name = 'default';
        vpn_user_password = 'password';
        test_queue_name = 'testQueue';
        usage = ('\nUsage: manage_vpn [create | delete] <host:port> <management_user> <management_password> <vpnname>'
                '\nEx: manage_vpn create <host:port> <management_user> <management_password> <vpnname>'
                '\n        Create a message-vpn and add a sample queue: testQueue'
                '\n    manage_vpn delete <host:port> <management_user> <management_password> <vpnname>'
                '\n        Delete the message-vpn')
        
        # Check command line arguments
        if len(sys.argv) < 6:
            sys.exit(usage)
        command = sys.argv[1]
        host_and_port = sys.argv[2]
        vmr_user = sys.argv[3]
        vmr_password = sys.argv[4]
        message_vpn_name = sys.argv[5]
        
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
        print 'Error during operation. Details: %s' % e.body
