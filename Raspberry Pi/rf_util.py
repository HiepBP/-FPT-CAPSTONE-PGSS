from my_class import *
import crc24 as CRC

#################################
###### VARIABLE DEFINITION ######
#################################

# Radio max waiting time
MAX_WAITING_MILLIS = 500
MAX_RESEND_PAYLOAD = 15

####### Radio Pipes #######
PIPES = [0xF0F0F0F0E1,
         0xF0F0F0F0D2]

####### Radio Command Name #######
CMD_ACK = "ack"
CMD_NACK = "nack"
CMD_TEST = "test"
CMD_DETECTED = "detected"
CMD_UNDETECTED = "undetected"
CMD_LOT_STATUS = "lot status"

####### Radio Command Dictionary #######
CMD_DICTIONARY =  BiDict({
    CMD_TEST : 0xAA,
    CMD_ACK : 0x06,
    CMD_NACK : 0x15,
    CMD_DETECTED : 0x08,
    CMD_UNDETECTED : 0x18,
    CMD_LOT_STATUS : 0xFA
    })

###################################
####### FUNCTION DEFINITION #######
###################################

####### Append new bytes to payload #######
def _payload_append(payload, bytelist):
    for byte in bytelist:
        payload.append(byte)
    return payload

####### Generate new payload with CRC #######
def generate_payload(devices_dictionary, message):
    if message.target in devices_dictionary and \
       message.command in CMD_DICTIONARY:
        target_address = devices_dictionary[message.target]
        cmd_address = CMD_DICTIONARY[message.command]
        
        payload = bytearray()
        payload = _payload_append(payload, target_address.to_bytes(2, "big"))
        payload = _payload_append(payload, cmd_address.to_bytes(1, "big"))

        checksum = CRC.calculate(payload)
        payload = _payload_append(payload, checksum.to_bytes(3, "big"))

        return payload
    else:
        return None

####### Generate new payload with CRC #######
def generate_ack_payload(target_address):

    cmd_address = CMD_DICTIONARY[CMD_ACK]
    
    payload = bytearray()
    payload = _payload_append(payload, target_address.to_bytes(2, "big"))
    payload = _payload_append(payload, cmd_address.to_bytes(1, "big"))

    return payload

####### Print payload in a readable form #######
def print_payload(payload):
    for byte in payload:
        print(format(byte, '#04x'), end=" ")
    print()

####### Get the address in the payload #######
def get_payload_address(payload):
    address = payload[0] << 8 | payload[1]
    return address

####### Check if the payload contains errors using CRC #######
def is_validated(payload):
    checksum = CRC.calculate(payload)
    if checksum == 0:
        return True
    else:
        return False

####### Get the address of command #######
def get_command_address(command):
    return CMD_DICTIONARY[command]

