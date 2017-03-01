from my_class import *

#################################
###### VARIABLE DEFINITION ######
#################################

####### PubNub Keys #######
PUBLISH_KEY = "pub-c-85b2050b-5425-4964-972f-90910aa358ca"
SUBSCRIBE_KEY = "sub-c-ed7a8b02-ed34-11e6-a504-02ee2ddab7fe"

####### PubNub Channels #######
CHANNEL_DEBUG = "debug"
CHANNEL_REALTIME_MAP = "realtime map"
CHANNEL_LOGGING = "logging"
CHANNEL_CONTROL = "control"

####### PubNub Status Message #######
MESSAGE_CONNECT = "Connected to channel {}"
MESSAGE_RECONNECT = "Reconnected to channel {}"
MESSAGE_DISCONNECT = "Disconnected from channel {}"

####### PubNub Message Variable #######
HUB_NAME = "hub_name"
DEVICE_NAME = "device_name"
COMMAND = "command"
DATA = "data"

###################################
####### FUNCTION DEFINITION #######
###################################

####### Return PubNub Message obj from message #######
# PubNub JSON message definition
# { [HUB_NAME] : name of required hub
#   [DEVICE_NAME] : name of device target to control
#   [COMMAND] : required command for target
#   [DATA] : optional data follow command if need}
def get_message(hub_name, message):
    if HUB_NAME in message and \
       hub_name == message[HUB_NAME]:
        if DEVICE_NAME in message and \
           COMMAND in message:
            if DATA in message:
                return PubnubMessage(message[COMMAND],
                             message[DEVICE_NAME],
                             message[DATA])
            else:
                return PubnubMessage(message[COMMAND],
                             message[DEVICE_NAME],
                             None)
        else:
            return None
    else:
        return None

def realtime_map_message(hub_name, available):
    return {'hub_name' : hub_name, 'available' : available}
