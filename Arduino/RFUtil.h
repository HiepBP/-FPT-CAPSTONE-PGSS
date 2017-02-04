/*
	RFData.h - An Arduino library contains all data related to RF communication
*/

#ifndef RFUtil_h
#define RFUtil_h

#include "Arduino.h"

/*************************************************
* Public Constants
*************************************************/

// Payload
#define MAX_PAYLOAD_SIZE 32
#define CRC_PAYLOAD_INIT 0x00b704ce

// Commands
#define CMD_ACK 0x06
#define CMD_NACK 0x15
#define CMD_DETECTED 0xAA

// Pipes
#define PIPE_1 0xF0F0F0F0E1LL
#define PIPE_2 0xF0F0F0F0D2LL

/*************************************************
* Definitions
*************************************************/

class RFUtil
{
	public:
		RFUtil(uint16_t address);
		void printHex8(uint8_t *payload, uint8_t payloadSize);
		bool isTarget(char payload[]);
		uint8_t generateAckPayload(char* payload);
		uint8_t generatePayload(char* payload, uint16_t target, uint8_t command);
		uint8_t generatePayload(char* payload, uint16_t target, uint8_t command, uint8_t data);
	private:
		uint16_t _deviceAddress;
};

#endif