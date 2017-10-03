#include "Wire.h"

#define ADDRESS 0x1E

const uint8_t sampleTimes = 10;
const uint16_t sampleDelay = 250;

int16_t xStab, zStab, yStab;

void setupStabilityValue() {
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

void setup() {
  Serial.begin(9600);
  // Initialize Serial and I2C communications
  Wire.begin();
  // Put the HMC5883L into correct operating mode
  Wire.beginTransmission(ADDRESS);
  Wire.write(0x02); // select mode register
  Wire.write(0x00); // continous measurement mode
  Wire.endTransmission();
  setupStabilityValue();
  Serial.println("End setup");
}

void readData(int16_t* x, int16_t* z, int16_t* y)
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

void loop() {
  int16_t x,z,y;
  while(true) {
    readData(&x,&z,&y);
    Serial.print(x - xStab);
    Serial.print("\t");
    Serial.print(y - yStab);
    Serial.print("\t");
    Serial.println(z - zStab);
  }
}
