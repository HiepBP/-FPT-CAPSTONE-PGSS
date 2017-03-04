#include <Wire.h> //I2C Arduino Library

#define address 0x1E

void readData(int16_t* x, int16_t* z, int16_t* y)
{
  //Tell the HMC5883L where to begin reading data
  Wire.beginTransmission(address);
  Wire.write(0x03); //select register 3, X MSB register
  Wire.endTransmission();
  //Read data from each axis, 2 registers per axis
  Wire.requestFrom(address, 6);
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

void setup() {
  //Initialize Serial and I2C communications
  Serial.begin(9600);
  Wire.begin();
  Serial.println("Start Sample");
  
  //Put the HMC5883 IC into the correct operating mode
  Wire.beginTransmission(address); //open communication with HMC5883
  Wire.write(0x02); //select mode register
  Wire.write(0x00); //continuous measurement mode
  Wire.endTransmission();

  // get stability values
  int16_t x,y,z;
  int32_t tx, ty, tz;
  tx = 0;
  ty = 0;
  tz = 0;
  for(int i = 0; i < 10; i++) {
    readData(&x,&z,&y);
    tx+=x;
    ty+=y;
    tz+=z;
    delay(500);
  }
  x = tx / 10;
  y = ty / 10;
  z = tz / 10;
  Serial.print(x);
  Serial.print("\t");
  Serial.print(y);
  Serial.print("\t");
  Serial.print(z);
  Serial.print("\t");
  Serial.println();
  Serial.println("End Sapmle");
}

void loop() {
  while(true) {
    int16_t x,y,z;
    readData(&x,&z,&y);
    Serial.print(x);
  Serial.print("\t");
  Serial.print(y);
  Serial.print("\t");
  Serial.print(z);
  Serial.print("\t");
  Serial.println();
    delay(500);
  }
}
