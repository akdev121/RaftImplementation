import json
import socket
import traceback
import time
import sys

# Wait following seconds below sending the controller request
time.sleep(2)

# Read Message Template
msg = json.load(open("Message.json"))
arg = sys.argv

print(arg)
# Initialize
sender = "Controller"
target = arg[1]
port = 5555
request_i=arg[2]
key=""
value=""

if len(arg)==5:
    key=arg[3]
    value=arg[4]

#print(target,request_i)
# Request
msg['sender_name'] = sender
msg['request'] = request_i
msg['key'] = key
msg['value']=value
#print(f"Request Created : {msg}")

# Socket Creation and Binding
skt = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
skt.bind((sender, port))
skt.settimeout(2)

# Send Message
try:
    # Encoding and sending the message
    skt.sendto(json.dumps(msg).encode('utf-8'), (target, port))
except:
    #  socket.gaierror: [Errno -3] would be thrown if target IP container does not exist or exits, write your listener
    print(f"ERROR WHILE SENDING REQUEST ACROSS : {traceback.format_exc()}")

if request_i in ["LEADER_INFO","STORE","RETRIEVE"]:
    try:
        msg, addr = skt.recvfrom(1024)
        # Decoding the Message received from Node 1
        decoded_msg = json.loads(msg.decode('utf-8'))
        print(f"Message Received : {decoded_msg} From : {addr}")
    except:
        print(f"exit")

print("Exiting Listener Function")
skt.close()
