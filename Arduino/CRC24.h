/*
	CRC24.h - An Arduino library for calculating a CRC24 checksum
*/

#ifndef CRC24_h
#define CRC24_h

#include "Arduino.h"

class CRC24
{
	public:
		CRC24(uint32_t init);
		uint32_t calculate(const uint8_t* data, uint8_t size);
		uint32_t calculateDebug(const uint8_t* data, uint8_t size);
	private:
		uint32_t CRC_24_INITIALIZATION;
};

#endif