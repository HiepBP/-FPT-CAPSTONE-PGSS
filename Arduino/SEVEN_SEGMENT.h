#ifndef SEVEN_SEGMENT_h
#define SEVEN_SEGMENT_h

#include "Arduino.h"

class SEVEN_SEGMENT
{
  public:
	SEVEN_SEGMENT(int latchPin , int clockPin, int dataPin);
	void Display7Segment(long value, int numOfLed);
  private:
	int LATCH_PIN;
	int CLOCK_PIN;
	int DATA_PIN;
  
};

#endif