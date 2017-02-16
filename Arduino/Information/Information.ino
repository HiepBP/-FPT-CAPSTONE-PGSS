#include <SEVEN_SEGMENT.h>
#include <RFUtil.h>
#include <RF24_config.h>
#include <RF24.h>
#include <printf.h>
#include <nRF24L01.h>

// Address of device. Used for communication in RF network, each device need to have an unique address
#define DEVICE_ADDRESS 0xCC01

// Hardware pins definition
#define PIN_RF_CE 7 // Chip Enable of RF module
#define PIN_RF_CSN 8 // Chip Select Not of RF module

// Information LED controller
SEVEN_SEGMENT information(4, 5, 6);

// Setup nRF24L01 radio with SPI bus, CE and CSN pin
RF24 radio(PIN_RF_CE, PIN_RF_CSN);
// RFUtil contains all neccessary meta data and function to use with RF module
// like PIPES_ADDRESS, MAX_PAYLOAD_SIZE, MAX_WAITING_MILLIS...
RFUtil rfUtil;

// Payload variables
// the receive_payload will contains the payload get from RF module in RXMODE
// the send_payload will contains the payload to be send by RF module in TXMODE
char receive_payload[MAX_PAYLOAD_SIZE + 1];
char send_payload[MAX_PAYLOAD_SIZE + 1];
char ack_payload[3];

// This function will send an ACK payload from this device
void sendAckPayload() {
	Serial.println(F("Start sendAckPayload"));
	radio.stopListening();
	radio.write(ack_payload, 3);
	Serial.print(F("Sent message: "));
	rfUtil.printHex8((uint8_t *)ack_payload, 3);
}

// This function will send the payload stored in send_payload
// After the payload has been sent, the device will change in to standby mode to wait for ACK payload from target
void sendPayload(uint8_t payloadSize) {
	radio.stopListening();
	radio.write(send_payload, payloadSize);
	radio.startListening();
	Serial.print(F("Sent message: "));
	rfUtil.printHex8((uint8_t *)send_payload, payloadSize);
}

// This function will make the device stop working and wait for an ACK payload
// in case no ACK payload after a limited time, or the payload receive from target is not an ACK payload, 
// the device will resend the payload stored in send_payload
bool waitAckPayload(uint8_t payloadSize) {
	unsigned long startWaitingAt = millis();
	bool timeout = false;
	bool resend = false;
	while (!timeout && !resend) {
		if (radio.available()) {
			uint8_t len = radio.getDynamicPayloadSize();
			radio.read(receive_payload, len);

			// Spew it
			Serial.print(F("Got response size="));
			Serial.println(len);
			Serial.print(F("Value= "));
			rfUtil.printHex8((uint8_t *)receive_payload, len);

			if (rfUtil.isTarget(receive_payload, DEVICE_ADDRESS)) {
				if (receive_payload[2] != CMD_ACK) {
					resend = true;
				}
				else {
					return true;
				}
			}
		}
		if ((millis() - startWaitingAt) > MAX_WAITING_MILLIS) {
			timeout = true;
		}
	}
	if (timeout || resend) {
		return false;
	}
}

// This function will check the payload stored in receive_payload and process based on the content of payload
void processPayload(char payload[], uint8_t payloadSize) {
	// Check the target of payload
	if (rfUtil.isTarget(payload, DEVICE_ADDRESS)) {
		// Payload error detecting
		if (rfUtil.isValidated(payload, payloadSize)) {
			Serial.println(F("Validate OK"));
			// send ACK message
			sendAckPayload();
			// execute command
			uint8_t command = rfUtil.getCommand((uint8_t *)payload);
			switch (command)
			{
				//      case CMD_LOT_STATUS: {
				//        Serial.println(F("Start sending status"));
				//        if (sensorStatus == true) {
				//          payloadSize = rfUtil.generatePayload(send_payload, DEVICE_ADDRESS, CMD_DETECTED);
				//        }
				//        else {
				//          payloadSize = rfUtil.generatePayload(send_payload, DEVICE_ADDRESS, CMD_UNDETECTED);
				//        }
				//        sendPayloadProcess(payloadSize);
				//        break;
				//      }
			case CMD_UPDATE_INFORMATION: {
				uint8_t data = rfUtil.getData((uint8_t *)payload);
				Serial.print(F("Start update information "));
				Serial.println(data);
				information.Display7Segment(data, 2);
			}
			default:
				break;
			}
		}
		else {
			Serial.println(F("WRONG CHECKSUM"));
		}
	}
	else {
		//TODO: hop payload
	}
}

// This function will processes the payload by resend the payload multiple times until receive the ACK payload
// The process will break and back to main process of device after a set number of resend
bool sendPayloadProcess(uint8_t payloadSize)
{
	if (payloadSize > 0) {
		bool ack = false;
		uint8_t resendTime = 0;
		while ((!ack) && (resendTime < MAX_RESEND_PAYLOAD)) {
			sendPayload(payloadSize);
			ack = waitAckPayload(payloadSize);
			resendTime++;
		}
		return ack;
	}
	return false;
}

void setup()
{
	// Print preamble
	Serial.begin(115200);
	printf_begin();
	Serial.println(F("Start communication with RF24"));
	// Setup rf radio
	radio.begin();
	radio.enableDynamicPayloads();
	radio.setRetries(5, 15);
	radio.openWritingPipe(rfUtil.getPipeAddress(1));
	radio.openReadingPipe(1, rfUtil.getPipeAddress(0));
	radio.startListening();
	radio.printDetails();
	// Create ack payload
	rfUtil.generateAckPayload(ack_payload, DEVICE_ADDRESS);
}

void loop()
{
	while (true) {
		if (radio.available()) {
			Serial.println("##########################");
			uint8_t payloadSize = radio.getDynamicPayloadSize();
			if (!payloadSize) {
				continue;
			}
			radio.read(receive_payload, payloadSize);

			// Spew it
			Serial.print(F("Got message size="));
			Serial.println(payloadSize);
			Serial.print(F("Value= "));
			rfUtil.printHex8((uint8_t *)receive_payload, payloadSize);

			processPayload(receive_payload, payloadSize);
			radio.startListening();
		}
	}
}
