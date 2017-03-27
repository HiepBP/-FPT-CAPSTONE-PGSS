/*
	RFData.h - An Arduino library contains all data related to RF communication
*/

#ifndef RFUtil_h
#define RFUtil_h

#include "Arduino.h"
#include "CRC24.h"

/*************************************************
* Public Constants
*************************************************/

// Payload
#define MAX_PAYLOAD_SIZE 32
#define CRC_PAYLOAD_INIT 0x00b704ce
#define MAX_WAITING_MILLIS 100
#define MAX_RESEND_PAYLOAD 5
#define PAYLOAD_ADDRESS_BYTE_1 0
#define PAYLOAD_ADDRESS_BYTE_2 1
#define PAYLOAD_COMMAND_BYTE 2
#define PAYLOAD_DATA_BYTE 3

// Commands
#define CMD_ACK 0x06        //0000 0110
#define CMD_NACK 0x15       //0001 0010
#define CMD_DETECTED 0x08   //0000 1000
#define CMD_UNDETECTED 0x18 //0001 1000
#define CMD_LOT_STATUS 0xFA //1111 1010
#define CMD_UPDATE_INFORMATION 0xC0
#define CMD_TEST 0xAA
#define CMD_RESERVE 0xDA
#define CMD_UNRESERVE 0xCD
#define CMD_CHANGE_OFFSET 0xCC

/*************************************************
* Definitions
*************************************************/

class RFUtil
{
	public:
		RFUtil();
		void printHex8(uint8_t *payload, uint8_t payloadSize);
		bool isTarget(char payload[], uint16_t targetAddress);
		uint8_t generateAckPayload(char* payload, uint16_t targetAddress);
		uint8_t generatePayload(char* payload, uint16_t target, uint8_t command);
		uint8_t generatePayload(char* payload, uint16_t target, uint8_t command, uint8_t data);
		uint64_t getPipeAddress(uint8_t pos);
		bool isValidated(char* payload, uint8_t payloadSize);
		uint8_t getCommand(uint8_t *payload);
		uint8_t getData(uint8_t *payload);
	private:
};

#endif