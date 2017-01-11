

#ifndef SRF05_h
#define SRF05_h

#include <Arduino.h>


class SRF05
{
  public:
	SRF05(int trigger_pin, int echo_pin, unsigned int range);
	boolean InRange();
  private:
	int _triggerPin;
	int _echoPin;
	int _range;
};

#endif