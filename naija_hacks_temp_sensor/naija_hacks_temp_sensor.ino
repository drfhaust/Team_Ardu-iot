

#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>

#include <RF24Network.h> //Library for networking nRF24L01s, using version https://github.com/TMRh20/RF24Network
#include <RF24.h> //Library for nRF24L01, using version https://github.com/TMRh20/RF24
#include <SPI.h> //nRF24L01 uses SPI communication
#include <EEPROM.h> //EEPROM functions

RF24 radio(9,10); //create object to control and communicate with nRF24L01
RF24Network network(radio); //Create object to use nRF24L01 in mesh network
int thisNode; //address of this router or end device
//const uint16_t rXNode = 00; //address of coordinator






#define DHTPIN 4    
#define DHTTYPE    DHT11     // DHT 11

DHT_Unified dht(DHTPIN, DHTTYPE);

uint32_t delayMS;


  
  struct sending{
byte devType;
      int voltage;
      int humidity;
    
  };
 sending sendd;



void checkNodeAddress() {
  pinMode(2, INPUT_PULLUP); 
  int val; 
  byte cAddr = 128; 
    if(EEPROM.get(0,cAddr)!= 128 || digitalRead(2)==LOW) {
    digitalWrite(7,HIGH); //in settings mode so turn on status LED
    Serial.begin(9600); //start serial communication, need to turn off before using sleep and WDT
    //the following code reads the current settings from EEPROM and puts them in local variables
    Serial.println(F("Current settings in EEPROM"));
    Serial.print("Node address: ");
    Serial.println(EEPROM.get(1,val),OCT);
    
    //This following gives you the option to set each setting and if you change a setting it is stored in EEPROM
    Serial.print(F("Would you like to update the "));
    Serial.print("Node Address? ");
    Serial.println(F("Enter 'Y' for yes or any other character for no:"));
    if(getAnswer()) {
      Serial.println(F("Enter Node address to store in EEPROM"));
      delay(50);
      while (!Serial.available()) { }
      val = Serial.parseInt();
      if(val >= 0) {
        EEPROM.put(1, val);
      }
      else { //if zero is entered it is invalid since coordinator is zero
        Serial.print( F("Invalid entry, default to "));
        Serial.println("01");
        val = 01;
        EEPROM.put(1, val);
      }
    }
    
    
    getEEPROMValues(); //gets settings from EEPROM and stor in global variables
    //the following code prints out current settings from global variables
    Serial.print("Node address: ");
    Serial.println(thisNode,OCT);
    cAddr = 128; //write '128' to EEPROM to show that settings have been entered at least once
    EEPROM.put(0, cAddr);
    Serial.end(); //need to end serial communication before using the WDT and sleep functions
  }
  else {
    //not in settings mode so just get settings from EEPROM and store in global variables
    getEEPROMValues();
  }
}

bool getAnswer() {
   while (!Serial.available()) { }
   if(Serial.read() == 'Y') return true;
   else return false;
}
void getEEPROMValues() {
  EEPROM.get(1,thisNode);}


int i;

void setup() {
  Serial.begin(9600);
  Serial.println(EEPROM.get(1,thisNode));
  checkNodeAddress(); //Function used for adding and getting module settings from EEPROM
  SPI.begin(); //Start SPI communication
  radio.begin(); //start nRF24L01 communication and control
 network.begin(90, thisNode);
  sendd.devType=2;//1=BULB 2= TEMP
  dht.begin();
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
   dht.humidity().getSensor(&sensor);
  delayMS = sensor.min_delay / 1000;
}

void loop() {


  
   delay(delayMS);

  sensors_event_t event;
  dht.temperature().getEvent(&event);

    Serial.print(F("Temperature: "));
   sendd.voltage=event.temperature*100;
    Serial.println(F("°C"));
  dht.humidity().getEvent(&event);
 
    Serial.print(F("Humidity: "));
   sendd.humidity=event.relative_humidity;
    Serial.println(F("%"));
 Serial.println(i);
i++;
if (i>=10){
  RF24NetworkHeader headerz(00);


   if (network.write(headerz,&sendd, sizeof(sending))) {
        Serial.println("sent");
      }
      else  { 
        Serial.println("failed");
      }
i=0;}

}
