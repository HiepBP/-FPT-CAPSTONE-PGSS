from my_class import *
import crc24 as CRC

#################################
###### VARIABLE DEFINITION ######
#################################

####### Radio Pipes #######
PIPES = [0xF0F0F0F0E1,
         0xF0F0F0F0D2]

####### Radio Command Name #######
CMD_ACK = 0x06
CMD_NACK = 0x15
CMD_TEST = "test"

####### Radio Command Dictionary #######
CMD_DICTIONARY =  {
    CMD_TEST : 0x01
    }

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

####### Print payload in a readable form #######
def print_payload(payload):
    for byte in payload:
        print(format(byte, '#04x'), end=" ")
    print()
