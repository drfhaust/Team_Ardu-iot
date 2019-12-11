#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>
#include <WiFiManager.h>  
#include <RF24Network.h>
#include <SoftwareSerial.h>
#include <RF24.h>
#include <SPI.h>




int node;
String nod;

 
WiFiServer server(80);

String headerx;

RF24 radio(4,15);

// start RF24 network layer
RF24Network network(radio);

// Coordinator address
const uint16_t thisNode = 00;

String firebaseUserId="FH8dJILtpnfvjOybVGNyzpCXERc2";
// Structure of our payload coming from router and end devices
 struct bulbData {
      byte lightState;
      byte bright;
      byte sendz;
       };
  bulbData data;

 struct sending{
 byte devType;
int voltage;
 //int humidity;
  };
 sending sendd;
       
 struct switchData{
      byte sw1;
      byte sw2;
      };
      
 switchData Switch;
 
bool clients=false;


#define FIREBASE_HOST "https://arduiot.firebaseio.com/"
#define FIREBASE_AUTH "6GQMMU5rCCocBJzfIaXRGpVpn4ICxhMhyMhkUWhH"

String path = "/FH8dJILtpnfvjOybVGNyzpCXERc2";
FirebaseData firebaseData;
FirebaseData firebaseData2;

FirebaseData firebaseData3;
FirebaseJson json;

// Current time
unsigned long currentTime = millis();

// Previous time
unsigned long previousTime = 0; 

// Define timeout time in milliseconds (example: 2000ms = 2s)
const long timeoutTime = 2000;
int sendz;
String sendi;
void setup(void){

 Serial.begin(115200);
 
   WiFiManager wifiManager;
  
  wifiManager.autoConnect("Ardu-iot");  
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  server.begin();
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
 //x Firebase.reconnectWiFi(true);
  

  if (!Firebase.beginStream(firebaseData3, path))
  {
    Serial.println("------------------------------------");
    Serial.println("Can't begin stream connection...");
    Serial.println("REASON: " + firebaseData3.errorReason());
    Serial.println("------------------------------------");
    Serial.println();
  }
  Serial.println("Coordinator is online.....");

  SPI.begin();
  radio.begin();
  network.begin(90, thisNode);
}

void loop(void){
 

  WiFiClient client = server.available();   // Listen for incoming clients
if (client) {                             // If a new client connects,
    Serial.println("New Client.");          // print a message out in the serial port
    String currentLine = "";                // make a String to hold incoming data from the client
    currentTime = millis();
    previousTime = currentTime;
    while (client.connected() && currentTime - previousTime <= timeoutTime) { // loop while the client's connected
      currentTime = millis();         
      if (client.available()) {             // if there's bytes to read from the client,
        char c = client.read();             // read a byte, then
        Serial.write(c);                    // print it out the serial monitor
        headerx += c;
        if (c == '\n') {                    // if the byte is a newline character
          // if the current line is blank, you got two newline characters in a row.
          // that's the end of the client HTTP request, so send a response:
          if (currentLine.length() == 0) {
             client.println("HTTP/1.1 200 OK");
            client.println("Content-type:text/html");
            client.println("Connection: close");
            client.println();
           
          } else { // if you got a newline, then clear currentLine
            currentLine = "";
          }
        } else if (c != '\r') {  // if you got anything else but a carriage return character,
          currentLine += c;      // add it to the end of the currentLine
        }
      }
    }
    clients=true;
    // Close the connection
    client.stop();
    Serial.println("Client disconnected.");
    Serial.println("");
  }
            
network.update();
radio.startListening();
 RF24NetworkHeader headerz; //create header variable
 while ( network.available() )
  {
    // If so, grab it and print it out
    network.read(headerz,&sendd,sizeof(sending));
    Serial.print("The node this is from: ");
       Serial.println(headerz.from_node);
     Serial.println(sendd.devType);
     
      if (sendd.devType==1){
    sendi=String(sendd.voltage);
  Serial.println(sendi);
     if (Firebase.setString(firebaseData, firebaseUserId+"/"+headerz.from_node+"/voltage", sendi))
    {
      Serial.println("P");
    }
    else
    {
      Serial.println("FAILED");
      Serial.println("REASON: " + firebaseData.errorReason());
      }
      }
      if (sendd.devType==2){
        sendd.voltage=sendd.voltage/100;
sendi=String(sendd.voltage);
     if (Firebase.setString(firebaseData, firebaseUserId+"/"+headerz.from_node+"/temperature", sendi))
    {
      Serial.println("PASSEDt");
    }
    else
    {
      Serial.println("FAILEDt");
      Serial.println("REASON: " + firebaseData.errorReason());
      }

        
      }

}
      





  if (!Firebase.readStream(firebaseData3))
  {
    Serial.println("------------------------------------");
    Serial.println("Can't read stream data...");
    Serial.println("REASON: " + firebaseData3.errorReason());
    Serial.println("------------------------------------");
    Serial.println();
  }

  if (firebaseData3.streamTimeout())
  {
    Serial.println("Stream timeout, resume streaming...");
    Serial.println();
  }

  if (firebaseData3.streamAvailable())
  {
   String i= firebaseData3.streamPath();
   String j=firebaseData3.dataPath();
   
    if(Firebase.get(firebaseData2,i+j+"/deviceType" )){
      if (firebaseData2.stringData()=="Bulb"){
        if(Firebase.get(firebaseData2,i+j+"/nodeNumber" )){
           nod=firebaseData2.stringData();
           node=nod.toInt();
          
        }
        if(Firebase.get(firebaseData2,i+j+"/lightBrightness" )){
          data.bright=firebaseData2.intData();
          
          }

         if(Firebase.get(firebaseData2,i+j+"/lightState" )){
          if(firebaseData2.boolData()==true){
           data.lightState=1;
          }
          else{
           data.lightState=0;
          }
          }
          RF24NetworkHeader BULB(node); 

data.sendz=1;
      if (network.write(BULB,&data, sizeof(bulbData))) {
        Serial.println("sent");
      }
      else  { 
        Serial.println("failed");
      }

      }
      if (firebaseData2.stringData()=="Switch"){
        if(Firebase.get(firebaseData2,i+j+"/nodeNumber" )){
           nod=firebaseData2.stringData();
           node=nod.toInt();
          
        }
         if(Firebase.get(firebaseData2,i+j+"/switch1" )){
          if(firebaseData2.boolData()==true){
            Switch.sw1=1;
          }
          else{
            Switch.sw1=0;
          }
          }
           if(Firebase.get(firebaseData2,i+j+"/switch2" )){
          if(firebaseData2.boolData()==true){
            Switch.sw2=1;
          }
          else{
            Switch.sw2=0;
          }
        }
          RF24NetworkHeader SW(node); 
Serial.println(node);

      if (network.write(SW,&Switch, sizeof(switchData))) {
        Serial.println("sent");
      }
      else  { 
        Serial.println("failed");
      }

    
    }
   // String send=
   
  }
}


data.sendz=0;





  

 if (clients==true){
    
if (headerx.indexOf("send=") >= 0) {
      int x = headerx.indexOf('=') ;
      int y = headerx.indexOf(",");
      String ReadString = headerx.substring(x+1, y);
      if(ReadString== "1"){//switch
        int ind2 = headerx.indexOf(',', y+1  );
       String nooode= headerx.substring(y+1, ind2 );
        int ind3 = headerx.indexOf(',', ind2 + 1 );
       String sw1= headerx.substring(ind2 + 1, ind3 );
       int ind4 = headerx.indexOf('#', ind3 + 1 );
       String sw2= headerx.substring(ind3 + 1, ind4 );
         Serial.println(nooode);
          Serial.println(sw1);
           Serial.println(sw2);
       
      }
        
      

       if(ReadString=="2"){//bulb

         int index2 = headerx.indexOf(',', y+1  );
       String bulbNode= headerx.substring(y+1, index2 );
        int index3 = headerx.indexOf(',', index2 + 1 );
       String state= headerx.substring(index2 + 1, index3 );
       int index4 = headerx.indexOf('#', index3 + 1 );
       String bright= headerx.substring(index3 + 1, index4 );
         Serial.println(bulbNode);
          Serial.println(state);
           Serial.println(bright);
        
      }
    }
  
 /*  if (
    headerx.indexOf("r1on") != -1) {
     data.r1=5;
      data.d1 = 1;
      }
       if (headerx.indexOf("r1off") != -1) {
     data.r1=5;
      data.d1 = 0;
      }

     if (headerx.indexOf("r2on") != -1) {
     data.r2=4;
      data.d2 = 1;
      }
      if (headerx.indexOf("r2off") != -1) {
     data.r2=4;
      data.d2 = 0;
      }*/
      headerx="";
    clients=false;


  }

  
 
}
