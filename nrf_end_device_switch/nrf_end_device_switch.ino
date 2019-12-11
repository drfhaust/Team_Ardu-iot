
#include <RF24Network.h> //Library for networking nRF24L01s, using version https://github.com/TMRh20/RF24Network
#include <RF24.h> //Library for nRF24L01, using version https://github.com/TMRh20/RF24
#include <SPI.h> //nRF24L01 uses SPI communication
#include <EEPROM.h> //EEPROM functions


RF24 radio(9,10); //create object to control and communicate with nRF24L01
RF24Network network(radio); //Create object to use nRF24L01 in mesh network
int thisNode; //address of this router or end device
const uint16_t rXNode = 00; //address of coordinator



void checkNodeAddress() {
  pinMode(2, INPUT_PULLUP); 
  int val; 
  byte cAddr = 128; 
    if(EEPROM.get(0,cAddr)!= 128 || digitalRead(2)==HIGH) {
    digitalWrite(7,HIGH); //in settings mode so turn on status LED
    Serial.begin(115200); //start serial communication, need to turn off before using sleep and WDT
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


 struct Data_Package {
      byte r1;
      byte r2;
      byte d2;
      byte d1;
      };
      Data_Package data;


 struct switchData{
      byte sw1;
      byte sw2;
      };
      
 switchData Switch;
 
int i;
void setup(void){
  Serial.begin(115200);
  Serial.println(EEPROM.get(1,thisNode));
  pinMode(4, OUTPUT); //set digital pin 7 as output to control status LED
  pinMode(5,OUTPUT); //set to output, connected to interrupt pin of STTS751 temp sensor
  digitalWrite(5,LOW); //Need this to pull interrupt pin of STTS751 high (it cannot float)
  digitalWrite(4,LOW);
  delay(200);
  checkNodeAddress(); //Function used for adding and getting module settings from EEPROM
  SPI.begin(); //Start SPI communication
  radio.begin(); //start nRF24L01 communication and control
 // radio.setRetries(0,0); 
  //setup network communication, first argument is channel which determines frequency band module communicates on. Second argument is address of this module
  network.begin(90, thisNode); 
}


void loop(void){ 
 
 network.update(); //check to see if there is any network traffic that needs to be passed on, technically an end device does not need this 
 RF24NetworkHeader SW; //create header variable
  while ( network.available() )
  {
    // If so, grab it and print it out
    network.read(SW,&Switch,sizeof(switchData));
    Serial.print("The node this is from: ");
    Serial.println(SW.from_node);
     digitalWrite(5,Switch.sw1);
     
  delay(50);
   digitalWrite(4,Switch.sw2);

  delay(50);
  }
    }
  
