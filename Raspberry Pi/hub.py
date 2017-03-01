import time
import sys
from pubnub import Pubnub 
import pubnub_meta as PubnubMeta
from RF24 import *
import RPi.GPIO as GPIO
import rf_util as RFUtil
from my_class import *

#################################
###### VARIABLE DEFINITION ######
#################################

# Hub definition
HUB_NAME = "Hub 1"
HUB_ADDRESS = 0xFA00
global CURRENT_AVAILABLE
global TMP_AVAILABLE
CURRENT_AVAILABLE = 45
TMP_AVAILABLE = 45

# Devices dictionary [Device name] : [Device address]
DEVICES_DICTIONARY = BiDict({
    "Detector 1":0xFA01,
    "Indicator 1":0xAA01,
    "Barrier 1":0xBB01,
    "Information 1":0xCC01,
    "Detector 2":0xFA02,
    "Detector 3":0xFA03
    })

# Parking lot object dictionary [Sensor name] : [Parking lot obj]
PARKING_LOT_DICTIONARY = {
    "Detector 1":ParkingLot("Detector 1","Indicator 1","Barrier 1","Information 1"),
    "Detector 2":ParkingLot("Detector 2","Indicator 1","Barrier 1","Information 1")
    }

# Wait for ACK payload
global WAIT_FOR_ACK
WAIT_FOR_ACK = False

# Global statuc checker
global POLLING_STATUS
global REQUEST_STATUS
POLLING_STATUS = False
REQUEST_STATUS = False

###################################
###### LIBRARY CONFIGURATION ######     
###################################

# Initialize Pubnub API
pubnub = Pubnub(publish_key = PubnubMeta.PUBLISH_KEY,
                subscribe_key = PubnubMeta.SUBSCRIBE_KEY)

# Setup nRF24L01 radio with SPIDEV, GPIO 22 and CE0 CSN
# In general, use RF24(<ce_pin>, <a>*10+<b>)
# for proper SPIDEV constructor to address correct
# spi device at /dev/spidev<a>.<b>
radio = RF24(22,0)

###################################
####### FUNCTION DEFINITION #######
###################################

# Basic function to calculate time in milliseconds
millis = lambda: int(round(time.time() * 1000))

####### PubNub API callback #######
def _pubnub_callback(json, channel):
    if channel == PubnubMeta.CHANNEL_CONTROL:
        message = PubnubMeta.get_message(HUB_NAME, json)
        print(message)
        if message != None:
            _execute_message(message)
    
def _pubnub_error(json):
    print(json)

def _pubnub_connect(channel):
    print(PubnubMeta.MESSAGE_CONNECT.format(channel))

def _pubnub_reconnect(channel):
    print(PubnubMeta.MESSAGE_RECONNECT.format(channel))

def _pubnub_disconnect(channel):
    print(PubnubMeta.MESSAGE_DISCONNECT.format(channel))

####### RF functions #######
def _execute_message(message):
    global REQUEST_STATUS
    global POLLING_STATUS
    REQUEST_STATUS = True
    while POLLING_STATUS:
        time.sleep(0.1)
    print("Start request")
    if message.command == RFUtil.CMD_RESERVE:
        global CURRENT_AVAILABLE
        global TMP_AVAILABLE
        lot = PARKING_LOT_DICTIONARY.get(message.target)
        lot.reserved = True
        print("Start send request payload")
        _send_payload_process(message)
        new_available = TMP_AVAILABLE - 1
        pubnub.publish(PubnubMeta.CHANNEL_REALTIME_MAP, PubnubMeta.realtime_map_message(HUB_NAME, new_available))
        # Current setting for demo is Detector, Indicator and Barrier will be in the same node
        print("Send information payload {} {}".format(TMP_AVAILABLE, new_available))
        message = PubnubMessage(RFUtil.CMD_UPDATE_INFORMATION, lot.information_name, new_available)
        radio.openWritingPipe(RFUtil.PIPES[2])
        if _send_payload_process(message):
            lot.set_available(False)
            CURRENT_AVAILABLE = new_available
            TMP_AVAILABLE = new_available
        radio.openWritingPipe(RFUtil.PIPES[0])
    elif message.command == RFUtil.CMD_UNRESERVE:
        global TMP_AVAILABLE
        global CURRENT_AVAILABLE
        lot = PARKING_LOT_DICTIONARY.get(message.target)
        lot.reserved = False
        print("Start send request payload")
        _send_payload_process(message)
        new_available = TMP_AVAILABLE + 1
        pubnub.publish(PubnubMeta.CHANNEL_REALTIME_MAP, PubnubMeta.realtime_map_message(HUB_NAME, new_available))
        # Current setting for demo is Detector, Indicator and Barrier will be in the same node
        print("Send information payload {} {}".format(TMP_AVAILABLE, new_available))
        message = PubnubMessage(RFUtil.CMD_UPDATE_INFORMATION, lot.information_name, new_available)
        radio.openWritingPipe(RFUtil.PIPES[2])
        if _send_payload_process(message):
            lot.set_available(True)
            CURRENT_AVAILABLE = new_available
            TMP_AVAILABLE = new_available
        radio.openWritingPipe(RFUtil.PIPES[0])

    REQUEST_STATUS = False
    print("End request")

def _send_payload_process(message):
    payload = RFUtil.generate_payload(DEVICES_DICTIONARY, message)
    if payload != None:
        ack = False
        resendTime = 0
        while not ack and resendTime < RFUtil.MAX_RESEND_PAYLOAD:
            _send_payload(payload)
            ack = _wait_ack_payload(message)
            resendTime = resendTime + 1
        return ack

def _send_payload(payload):
    if payload != None:
        print("Now sending ... ", end="")
        RFUtil.print_payload(payload)
        radio.stopListening()
        radio.write(payload)
        radio.startListening()

def _send_ack_payload(target_address):
    payload = RFUtil.generate_ack_payload(target_address)
    if payload != None:
        print("Now sending ... ", end="")
        RFUtil.print_payload(payload)
        radio.stopListening()
        radio.write(payload)

def _wait_ack_payload(message):
    started_waiting_at = millis()
    timeout = False
    resend = False
    while not timeout and not resend:
        if radio.available():
            len = radio.getDynamicPayloadSize()
            receive_payload = radio.read(len)
            print("Get response ... ", end="")
            RFUtil.print_payload(receive_payload)
            device_address = RFUtil.get_payload_address(receive_payload)
            if device_address == DEVICES_DICTIONARY[message.target]:
                if receive_payload[2] != RFUtil.get_command_address(RFUtil.CMD_ACK):
                    if receive_payload[2] == RFUtil.get_command_address(RFUtil.CMD_DETECTED) or \
                       receive_payload[2] == RFUtil.get_command_address(RFUtil.CMD_UNDETECTED):
                        return True
                    # stress test: exit if the ack is wrong package
                    sys.exit()
                    resend = True
                else:
                    return True
        wait_time = millis() - started_waiting_at
        if wait_time > RFUtil.MAX_WAITING_MILLIS:
            print("Start: {} Wait: {}".format(started_waiting_at, wait_time))
            timeout = True
    if timeout or resend:
        return False
    

def _process_payload(payload, lot):
    print("Start process payload")
    # Payload error detecting
    if RFUtil.is_validated(payload):
        print("Checksum OK")
        # Check the device address of payload
        device_address = RFUtil.get_payload_address(payload)
        if device_address in DEVICES_DICTIONARY:
            print("Target OK")
            # First, send ACK payload
            _send_ack_payload(device_address)
            # Check command
            command = RFUtil.get_command(payload)
            available = None
            global TMP_AVAILABLE
            if command == RFUtil.get_command_address(RFUtil.CMD_DETECTED):
                available = False
                new_available = TMP_AVAILABLE - 1
            elif command == RFUtil.get_command_address(RFUtil.CMD_UNDETECTED):
                available = True
                new_available = TMP_AVAILABLE + 1
            if lot.reserved == False:
                if available != lot.available:
                    TMP_AVAILABLE = new_available
                    lot.set_available(available)
##                    pubnub.publish(PubnubMeta.CHANNEL_REALTIME_MAP, PubnubMeta.realtime_map_message(HUB_NAME, new_available))
##                    # Current setting for demo is Detector, Indicator and Barrier will be in the same node
##                    # Send message to Indicator
##                    print("Send indicator payload")
##                    message = PubnubMessage(RFUtil.CMD_TEST, lot.indicator_name, None)
##                    print("Send information payload {} {}".format(CURRENT_AVAILABLE, new_available))
##                    message = PubnubMessage(RFUtil.CMD_UPDATE_INFORMATION, lot.information_name, new_available)
##                    radio.openWritingPipe(RFUtil.PIPES[2])
##                    if _send_payload_process(message):
##                        lot.set_available(available)
##                        CURRENT_AVAILABLE = new_available
##                    radio.openWritingPipe(RFUtil.PIPES[0])
            return True
    return False

#############################
####### MAIN PROGRAM ########
#############################

# Start radio
radio.begin()
radio.enableDynamicPayloads()
radio.setRetries(5,15)
radio.printDetails()
radio.openWritingPipe(RFUtil.PIPES[0])
radio.openReadingPipe(1,RFUtil.PIPES[1])

# Start Pubnub
try:
    pubnub.publish(PubnubMeta.CHANNEL_LOGGING, "Rise and Shine baby")
    pubnub.subscribe(channels = PubnubMeta.CHANNEL_CONTROL,
                     callback = _pubnub_callback,
                     error = _pubnub_error,
                     connect = _pubnub_connect,
                     reconnect = _pubnub_reconnect,
                     disconnect = _pubnub_disconnect)
    while True:
        while REQUEST_STATUS:
            POLLING_STATUS = False
            time.sleep(0.1)
        POLLING_STATUS = True
        print("####################")
        for sensor_name, lot in PARKING_LOT_DICTIONARY.items():
            message = PubnubMessage(RFUtil.CMD_LOT_STATUS, sensor_name, None)
            _send_payload_process(message)
            radio.startListening()
            
            started_waiting_at = millis()
            total_waiting_time = RFUtil.MAX_WAITING_MILLIS * RFUtil.MAX_RESEND_PAYLOAD
            while (millis() - started_waiting_at) < total_waiting_time:
                if radio.available():
                    len = radio.getDynamicPayloadSize()
                    receive_payload = radio.read(len)

                    print("Get payload ... ", end="")
                    RFUtil.print_payload(receive_payload)
                    check_payload = _process_payload(receive_payload, lot)
                    if check_payload:
                        break
        if CURRENT_AVAILABLE != TMP_AVAILABLE:
            pubnub.publish(PubnubMeta.CHANNEL_REALTIME_MAP, PubnubMeta.realtime_map_message(HUB_NAME, TMP_AVAILABLE))
            # Current setting for demo is Detector, Indicator and Barrier will be in the same node
            print("Send information payload {} {}".format(CURRENT_AVAILABLE, TMP_AVAILABLE))
            message = PubnubMessage(RFUtil.CMD_UPDATE_INFORMATION, "Information 1", TMP_AVAILABLE)
            radio.openWritingPipe(RFUtil.PIPES[2])
            if _send_payload_process(message):
                CURRENT_AVAILABLE = TMP_AVAILABLE
            radio.openWritingPipe(RFUtil.PIPES[0])
        time.sleep(0.1)
finally:
    print("Pubnub stop")
    pubnub.stop()
    print("GPIO cleanup")
    GPIO.cleanup()
