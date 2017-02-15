#include "Arduino.h"
#include "SEVEN_SEGMENT.h"

const byte Number[10] = {
  B00111111,  //0
  B00000110, //1
  B01011011, //2
  B01001111, //3
  B01100110, //4
  B01101101, //5
  B01111101, //6
  B00000111, //7
  B01111111, //8
  B01101111 //9
};

// Constructor
SEVEN_SEGMENT::SEVEN_SEGMENT(int latchPin, int clockPin, int dataPin) {
	LATCH_PIN=latchPin;
	CLOCK_PIN=clockPin;
	DATA_PIN=dataPin;
	pinMode(latchPin, OUTPUT);   
	pinMode(clockPin, OUTPUT); 
	pinMode(dataPin, OUTPUT);
}


void SEVEN_SEGMENT::Display7Segment(long value, int numOfLed) {
  byte *array = new byte[numOfLed];
  for (byte i = 0; i < numOfLed; i++) {
    //Lấy các chữ số từ phải quá trái
    array[i] = (byte)(value % 10UL);
    value = (unsigned long)(value / 10UL);
  }
  digitalWrite(LATCH_PIN, LOW);
  for (int i = numOfLed - 1; i >= 0; i--)
    shiftOut(DATA_PIN, CLOCK_PIN, MSBFIRST, Number[array[i]]);

  digitalWrite(LATCH_PIN, HIGH);
  free(array);
}