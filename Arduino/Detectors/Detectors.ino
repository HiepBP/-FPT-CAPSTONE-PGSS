#include <EEPROM.h>
#include <Servo.h>
#include <RGBLED.h>
#include <HMC5883L.h>
#include <RFUtil.h>
#include <RF24_config.h>
#include <RF24.h>
#include <printf.h>
#include <nRF24L01.h>

#define SCHEME_VERSION 0

// Address of device. Used for communication in RF network, each device need to have an unique address
#define DEVICE_ADDRESS 0x000C

// Hardware pins definition
#define PIN_RF_CE 7 // Chip Enable of RF module
#define PIN_RF_CSN 8 // Chip Select Not of RF module

// Indicator LED controller
RGBLED indicator(3, 5, 6, COMMON_ANODE);

// Servo controller
Servo servo;
bool servoStatus = false;

// Metal detector sensor
HMC5883L sensor;
bool sensorStatus = false;

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

// This function will send an ACK payload from this device
void sendAckPayload() {
	uint8_t payloadSize = rfUtil.generateAckPayload(send_payload, DEVICE_ADDRESS);
	if (payloadSize > 0) {
		radio.stopListening();
		radio.write(send_payload, payloadSize);
		//Serial.print(F("Sent ack message: "));
		//rfUtil.printHex8((uint8_t *)send_payload, payloadSize);
	}
}

// This function will send the payload stored in send_payload
// After the payload has been sent, the device will change in to standby mode to wait for ACK payload from target
void sendPayload(uint8_t payloadSize) {
	radio.stopListening();
	radio.write(send_payload, payloadSize);
	radio.startListening();
	//Serial.print(F("Sent message: "));
	//rfUtil.printHex8((uint8_t *)send_payload, payloadSize);
}

// This function will make the device stop working and wait for an ACK payload
// in case no ACK payload after a limited time, or the payload receive from target is not an ACK payload, 
// the device will resend the payload stored in send_payload
bool waitAckPayload(uint8_t payloadSize) {
	unsigned long startWaitingAt = millis();
	bool timeout = false;
	while (!timeout) {
		if (radio.available()) {
			uint8_t len = radio.getDynamicPayloadSize();
			radio.read(receive_payload, len);

			// Spew it
			//Serial.print(F("Got response size="));
			//Serial.println(len);
			//Serial.print(F("Value= "));
			//rfUtil.printHex8((uint8_t *)receive_payload, len);

			if (rfUtil.isTarget(receive_payload, DEVICE_ADDRESS)) {
				if (receive_payload[2] == CMD_ACK ||
					receive_payload[2] == CMD_LOT_STATUS) {
					return true;
				}
			}
		}
		if ((millis() - startWaitingAt) > MAX_WAITING_MILLIS) {
			timeout = true;
		}
	}
	return false;
}

// This function will check the payload stored in receive_payload and process based on the content of payload
void processPayload(char payload[], uint8_t payloadSize) {
	// Payload error detecting
	if (rfUtil.isValidated(payload, payloadSize)) {
		// Check the target of payload
		if (rfUtil.isTarget(payload, DEVICE_ADDRESS)) {
			// execute command
			uint8_t command = rfUtil.getCommand((uint8_t *)payload);
			switch (command)
			{
			case CMD_LOT_STATUS: {
				//Serial.println(F("Start sending status"));
				if (sensorStatus == true) {
					payloadSize = rfUtil.generatePayload(send_payload, DEVICE_ADDRESS, CMD_DETECTED);
				}
				else {
					payloadSize = rfUtil.generatePayload(send_payload, DEVICE_ADDRESS, CMD_UNDETECTED);
				}
				sendPayloadProcess(payloadSize);
				break;
			}
			case CMD_RESERVE: {
				sendAckPayload();
				if (servoStatus == false) {
					Serial.println("Reserve");
					servoStatus = true;
					sensorStatus = true;
					servo.write(90);
					indicator.writeRGB(0, 255, 255);
				}
				break;
			}
			case CMD_UNRESERVE: {
				sendAckPayload();
				Serial.println("In CMD_UNRESER");
				if (servoStatus == true) {
					Serial.println("Unreserve");
					servoStatus = false;
					sensorStatus = false;
					servo.write(0);
					indicator.writeRGB(255, 0, 255);
				}
				break;
			}
			case CMD_CHANGE_OFFSET: {
				sendAckPayload();
				Serial.println("In CMD_CHANGE_OFFSET");
				EEPROM.write(0, payload[3]);
				EEPROM.write(1, payload[4]);
				EEPROM.write(2, payload[5]);
				EEPROM.write(3, payload[6]);
				EEPROM.write(4, payload[7]);
				EEPROM.write(5, payload[8]);
				int16_t xOffset, yOffset, zOffset;
				xOffset = (uint16_t)EEPROM.read(0) << 8 | EEPROM.read(1);
				yOffset = (uint16_t)EEPROM.read(2) << 8 | EEPROM.read(3);
				zOffset = (uint16_t)EEPROM.read(4) << 8 | EEPROM.read(5);
				Serial.print("Offset ");
				Serial.print("X= ");
				Serial.print(xOffset);
				Serial.print(" Y= ");
				Serial.print(yOffset);
				Serial.print(" Z= ");
				Serial.println(zOffset);
				sensor.setOffSet(xOffset, yOffset, zOffset);
				break;
			}
			default:
				break;
			}
		}
		else {
			//TODO: request resent payload
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
 Serial.print("Address ");
 Serial.println(DEVICE_ADDRESS);
	printf_begin();
	// Initialize offset from EEPROM
	int16_t xOffset, yOffset, zOffset;
	xOffset = (uint16_t)EEPROM.read(0) << 8 | EEPROM.read(1);
	yOffset = (uint16_t)EEPROM.read(2) << 8 | EEPROM.read(3);
	zOffset = (uint16_t)EEPROM.read(4) << 8 | EEPROM.read(5);
	Serial.print("Offset ");
	Serial.print("X= ");
	Serial.print(xOffset);
	Serial.print(" Y= ");
	Serial.print(yOffset);
	Serial.print(" Z= ");
	Serial.println(zOffset);
	Serial.println(F("Start communication with RF24"));
	// Setup rf radio
	radio.begin();
	radio.setPALevel(RF24_PA_MAX);
	radio.setDataRate(RF24_250KBPS);
	radio.enableDynamicPayloads();
	radio.openWritingPipe(rfUtil.getPipeAddress(1));
	radio.openReadingPipe(1, rfUtil.getPipeAddress(0));
	radio.setAutoAck(false);
	radio.disableCRC();
	radio.startListening();
	radio.printDetails();
	// Setup metal detector sensor
	sensor.setOffSet(xOffset, yOffset, zOffset);
	sensor.setup();
	// Check car status
	if (sensor.isInRange()) {
		sensorStatus = true;
		indicator.writeRGB(0, 255, 255);
	}
	else {
		sensorStatus = false;
		indicator.writeRGB(255, 0, 255);
	}

	// Setup servo
	servo.attach(4);
	servo.write(0);
}

void loop()
{
	while (true) {
		// check if there is any metal (car) in range
		if (sensor.isInRange()) {
			Serial.println("Metal here");
			if (sensorStatus == false && servoStatus == false) {
				// 1st time detected
				Serial.println(F("Metal is near"));
				sensorStatus = true;
				indicator.writeRGB(0, 255, 255);
			}
		}
		else {
			if (sensorStatus == true && servoStatus == false) {
				// 1st time undetected
				Serial.println(F("Metal is gone"));
				sensorStatus = false;
				indicator.writeRGB(255, 0, 255);
			}
		}

		if (radio.available()) {
			Serial.println("##########################");
			uint8_t payloadSize = radio.getDynamicPayloadSize();
			if (!payloadSize) {
				continue;
			}
			radio.read(receive_payload, payloadSize);

			// Spew it
			//Serial.print(F("Got message size="));
			//Serial.println(payloadSize);
			//Serial.print(F("Value= "));
			//rfUtil.printHex8((uint8_t *)receive_payload, payloadSize);

			processPayload(receive_payload, payloadSize);
			radio.startListening();
		}
	}
}

