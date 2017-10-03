/*
	HMC5883L.h - An Arduino library for Triple Axis Magnetometer HMC5883L
*/

#ifndef HMC5883L_h
#define HMC5883L_h

#include "Arduino.h"

class HMC5883L
{
	public:
		HMC5883L();
		void setup();
		void setOffSet(int16_t xOffset, int16_t zOffset, int16_t yOffset);
		boolean isInRange();
		boolean isInRangeDebug();
	private:
		void readData(int16_t* x, int16_t* z, int16_t* y);
		void setupStabilityValue();
		int16_t X_OFFSET;
		int16_t Z_OFFSET;
		int16_t Y_OFFSET;
};

#endif