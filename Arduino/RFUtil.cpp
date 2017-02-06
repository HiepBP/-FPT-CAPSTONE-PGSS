/*
	RFData.h - An Arduino library contains all data related to RF communication
*/

#include "Arduino.h"
#include "RFUtil.h"
#include "CRC24.h"

const uint64_t PIPES[] = {
	0xF0F0F0F0E1,
	0xF0F0F0F0D2
};

CRC24 crc;

RFUtil::RFUtil() {
}

void RFUtil::printHex8(uint8_t *payload, uint8_t payloadSize) 
{
	for (int i = 0; i < payloadSize; i++) {
		Serial.print("0x");
		if (payload[i] < 0x10) {
			Serial.print("0");
		}
		Serial.print(payload[i], HEX);
		Serial.print(" ");
	}
	Serial.println();
}

bool RFUtil::isTarget(char payload[], uint16_t targetAddress) 
{
	uint16_t address = (uint16_t)payload[0] << 8 | payload[1];
	if (address == targetAddress) 
		return true; 
	return false;
}

uint8_t RFUtil::generateAckPayload(char* payload, uint16_t targetAddress)
{
	payload[0] = targetAddress >> 8 & 0xFF;
	payload[1] = targetAddress >> 0 & 0xFF;
	payload[2] = CMD_ACK;
	
	return 3;
}

uint8_t RFUtil::generatePayload(char* payload, uint16_t target, uint8_t command)
{
	payload[0] = target >> 8 & 0xFF;
	payload[1] = target >> 0 & 0xFF;
	payload[2] = command;
	uint8_t payloadSize = 3;
	
	uint32_t checksum = crc.calculate((uint8_t *)payload, payloadSize);
	for (int i = 2; i >= 0; i--) {
		uint8_t bits = i * 8;
		payload[payloadSize++] = checksum >> bits & 0xFF;
	}
	
	return payloadSize;
}

uint8_t RFUtil::generatePayload(char* payload, uint16_t target, uint8_t command, uint8_t data)
{
	payload[0] = target >> 8 & 0xFF;
	payload[1] = target >> 0 & 0xFF;
	payload[2] = command;
	payload[3] = data;
	uint8_t payloadSize = 4;
	
	uint32_t checksum = crc.calculate((uint8_t *)payload, payloadSize);
	for (int i = 2; i >= 0; i--) {
		uint8_t bits = i * 8;
		payload[payloadSize++] = checksum >> bits & 0xFF;
	}
	
	return payloadSize;
}

uint64_t RFUtil::getPipeAddress(uint8_t pos) 
{
	return PIPES[pos];
}

bool RFUtil::isValidated(char* payload, uint8_t payloadSize)
{
	uint32_t checksum = crc.calculate((uint8_t *)payload, payloadSize);
	if (checksum == 0) {
		return true;
	} else {
		return false;
	}
}