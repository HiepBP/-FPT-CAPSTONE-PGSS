#include <HMC5883L.h>
#include <RFUtil.h>
#include <CRC24.h>
#include <RF24_config.h>
#include <RF24.h>
#include <printf.h>
#include <nRF24L01.h>

#define DEVICE_ADDRESS 0xFA01

#define MAX_WAITING_MILLIS 500

// Hardware pins definition
#define PIN_RF_CE 7
#define PIN_RF_CSN 8

// Metal detector offset
#define X_OFFSET 300
#define Y_OFFSET 300
#define Z_OFFSET 300

// Metal detector sensor
HMC5883L sensor(X_OFFSET, Z_OFFSET, Y_OFFSET);
bool sensorStatus = false;

// Setup nRF24L01 radio with SPI bus, CE and CSN pin
RF24 radio(PIN_RF_CE, PIN_RF_CSN);
RFUtil radioUtil(DEVICE_ADDRESS);

// nRF24L01 pipe addresses
const uint64_t pipes[2] = { PIPE_1, PIPE_2 };

// Payload
char receive_payload[MAX_PAYLOAD_SIZE + 1];
char send_payload[MAX_PAYLOAD_SIZE + 1];

// CRC
CRC24 crc(CRC_PAYLOAD_INIT);

void sendAckPayload(uint8_t payloadSize) {
	radio.stopListening();

	radio.write(send_payload, payloadSize);
	Serial.print(F("Sent message: "));
	radioUtil.printHex8((uint8_t *)send_payload, payloadSize);

	radio.startListening();
}

void sendPayload(uint8_t payloadSize) {
	uint32_t checksum = crc.calculateDebug((uint8_t *)send_payload, payloadSize);
	for (int i = 2; i >= 0; i--) {
		uint8_t bits = i * 8;
		send_payload[payloadSize++] = checksum >> bits & 0xFF;
	}

	radio.stopListening();

	radio.write(send_payload, payloadSize);
	Serial.print(F("Sent message: "));
	radioUtil.printHex8((uint8_t *)send_payload, payloadSize);

	waitAckPayload(payloadSize);
}

void waitAckPayload(uint8_t payloadSize) {
	radio.startListening();
	unsigned long startWaitingAt = millis();
	bool timeout = false;
	bool resend = false;
	while (!timeout) {
		if (radio.available()) {
			uint8_t len = radio.getDynamicPayloadSize();
			radio.read(receive_payload, len);

			// Spew it
			Serial.print(F("Got response size="));
			Serial.println(len);
			Serial.print(F("Value= "));
			radioUtil.printHex8((uint8_t *)receive_payload, len);

			if (radioUtil.isTarget(receive_payload)) {
				if (receive_payload[2] != CMD_ACK) {
					resend = true;
				}
				break;
			}
		}
		if ((millis() - startWaitingAt) > MAX_WAITING_MILLIS) {
			timeout = true;
		}
	}
	if (timeout || resend) {
		sendPayload(payloadSize);
	}
}

void processPayload(char payload[], uint8_t payloadSize) {
	// Check the target of payload
	if (radioUtil.isTarget(payload)) {
		// Payload error detecting
		if (crc.calculateDebug((uint8_t *)payload, payloadSize) == 0) {
			// send ACK message
			sendAckPayload(radioUtil.generateAckPayload(send_payload));

			//TODO: execute command
		}
		else {
			//TODO: request resent payload
		}
	}
	else {
		//TODO: hop payload
	}
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
	radio.openWritingPipe(pipes[1]);
	radio.openReadingPipe(1, pipes[0]);
	radio.startListening();
	radio.printDetails();
	// Setup metal detector sensor
	sensor.setup();
}

void loop()
{
	while (true) {
		if (sensor.isInRange()) {
			if (!sensorStatus) {
				sensorStatus = true;
				sendPayload(radioUtil.generatePayload(send_payload, DEVICE_ADDRESS, CMD_DETECTED));
			}
		}
		else {
			sensorStatus = false;
		}
		if (radio.available()) {
			uint8_t payloadSize = radio.getDynamicPayloadSize();
			if (!payloadSize) {
				continue;
			}
			radio.read(receive_payload, payloadSize);

			// Spew it
			Serial.print(F("Got message size="));
			Serial.println(payloadSize);
			Serial.print(F("Value= "));
			radioUtil.printHex8((uint8_t *)receive_payload, payloadSize);

			processPayload(receive_payload, payloadSize);

			radio.startListening();
		}
	}
}
