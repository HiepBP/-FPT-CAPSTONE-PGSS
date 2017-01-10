/*
	RFData.h - An Arduino library contains all data related to RF communication
*/

#include "Arduino.h"
#include "RFUtil.h"

RFUtil::RFUtil(uint16_t address) {
	_deviceAddress = address;
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

bool RFUtil::isTarget(char payload[]) 
{
	uint16_t address = (uint16_t)payload[0] << 8 | payload[1];
	if (address == _deviceAddress) 
		return true; 
	return false;
}

uint8_t RFUtil::generateAckPayload(char* payload)
{
	payload[0] = _deviceAddress >> 8 & 0xFF;
	payload[1] = _deviceAddress >> 0 & 0xFF;
	payload[2] = CMD_ACK;
	
	return 3;
}

uint8_t RFUtil::generatePayload(char* payload)generatePayload(char* payload, uint16_t target, uint8_t command)
{
	payload[0] = target >> 8 & 0xFF;
	payload[1] = target >> 0 & 0xFF;
	payload[2] = command;
	
	return 3;
}

uint8_t RFUtil::generatePayload(char* payload)generatePayload(char* payload, uint16_t target, uint8_t command, uint8_t data)
{
	payload[0] = target >> 8 & 0xFF;
	payload[1] = target >> 0 & 0xFF;
	payload[2] = command;
	payload[3] = data;
	
	return 4;
}