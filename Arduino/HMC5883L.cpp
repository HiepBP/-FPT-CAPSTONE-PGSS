/*
	HMC5883L.h - An Arduino library for Triple Axis Magnetometer HMC5883L
*/

#include "Arduino.h"
#include "HMC5883L.h"
#include "Wire.h" //I2C Arduino Library

#define ADDRESS 0x1E //0011110b, I2C 7bit address of HMC5883L

const uint8_t sampleTimes = 10;
const uint16_t sampleDelay = 250;

int16_t xStab, zStab, yStab;

HMC5883L::HMC5883L(int16_t xOffset, int16_t zOffset, int16_t yOffset)
{
	X_OFFSET = xOffset;
	Z_OFFSET = zOffset;
	Y_OFFSET = yOffset;
}

void HMC5883L::setup()
{
	// Initialize Serial and I2C communications
	Wire.begin();
	// Put the HMC5883L into correct operating mode
	Wire.beginTransmission(ADDRESS);
	Wire.write(0x02); // select mode register
	Wire.write(0x00); // continous measurement mode
	Wire.endTransmission();
	Serial.println(F("Setup HMC5883L with continous measurement mode"));
	// Sample the surrounding magnetic for stability values
	setupStabilityValue();
	Serial.println(F("Stability axis data:"));
	Serial.print(F("x: "));
	Serial.print(xStab);
	Serial.print(F(" y: "));
	Serial.print(yStab);
	Serial.print(F(" z: "));
	Serial.print(zStab);
	Serial.println();
}

boolean HMC5883L::isInRange()
{
	int16_t x,z,y,xDiff,zDiff,yDiff;
	readData(&x, &z, &y);
	xDiff = x - xStab;
	yDiff = y - yStab;
	zDiff = z - zStab;
	if (abs(xDiff) >= X_OFFSET || abs(yDiff) >= Y_OFFSET || abs(zDiff) >= Z_OFFSET)
	{
		return true;
	}
	return false;
}

boolean HMC5883L::isInRangeDebug()
{
	int16_t x,z,y,xDiff,zDiff,yDiff;
	readData(&x, &z, &y);
	xDiff = x - xStab;
	yDiff = y - yStab;
	zDiff = z - zStab;
	if (abs(xDiff) >= X_OFFSET || abs(yDiff) >= Y_OFFSET || abs(zDiff) >= Z_OFFSET)
	{
		Serial.print(F("Current axis data: "));
		Serial.print(F("x: "));
		Serial.print(x);
		Serial.print(F(" y: "));
		Serial.print(y);
		Serial.print(F(" z: "));
		Serial.print(z);
		Serial.println();
		return true;
	}
	return false;
}

void HMC5883L::setupStabilityValue()
{
	int16_t x,z,y;
	int32_t totalX = 0, totalZ = 0, totalY = 0;
	for (uint8_t i = 0; i < sampleTimes; i++)
	{
		readData(&x, &z, &y);
		totalX += x;
		totalZ += z;
		totalY += y;
		delay(sampleDelay);
	}
	
	xStab = totalX / sampleTimes;
	zStab = totalZ / sampleTimes;
	yStab = totalY / sampleTimes;
}

void HMC5883L::readData(int16_t* x, int16_t* z, int16_t* y)
{
	//Tell the HMC5883L where to begin reading data
	Wire.beginTransmission(ADDRESS);
	Wire.write(0x03); //select register 3, X MSB register
	Wire.endTransmission();
	//Read data from each axis, 2 registers per axis
	Wire.requestFrom(ADDRESS, 6);
	if(6<=Wire.available())
	{
		*x = Wire.read()<<8; //X msb
		*x |= Wire.read(); //X lsb
		*z = Wire.read()<<8; //Z msb
		*z |= Wire.read(); //Z lsb
		*y = Wire.read()<<8; //Y msb
		*y |= Wire.read(); //Y lsb
	}
}