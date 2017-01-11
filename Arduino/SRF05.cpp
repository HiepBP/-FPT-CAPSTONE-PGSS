
#include <Arduino.h>
#include "SRF05.h"

SRF05::SRF05(int trigger_pin, int echo_pin, unsigned int range) {

	_triggerPin = trigger_pin;
	_echoPin = echo_pin;
	_range = range;

	pinMode(echo_pin, INPUT);   
	pinMode(trigger_pin, OUTPUT); 
}


boolean SRF05::InRange(){
	long duration;
	float distanceCm;
   
  digitalWrite(_triggerPin, LOW);
  delayMicroseconds(2);
  digitalWrite(_triggerPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(_triggerPin, LOW);
  
  duration = pulseIn(_echoPin, HIGH, 5000);
 

  distanceCm = duration / 29.1 / 2;
  if (distanceCm <= 0) return false; 
  if (distanceCm > _range) return false;
  else return true;
}

