#include "DHTesp.h"

#ifdef ESP32
#pragma message(THIS EXAMPLE IS FOR ESP8266 ONLY!)
#error Select ESP8266 board.
#endif

DHTesp dht;

#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>
#include <WiFiManager.h>






String path = "/FH8dJILtpnfvjOybVGNyzpCXERc2";
FirebaseData firebaseData;
#define FIREBASE_HOST "https://arduiot.firebaseio.com/"
#define FIREBASE_AUTH "6GQMMU5rCCocBJzfIaXRGpVpn4ICxhMhyMhkUWhH"


int airPin = A0;//gas sensor pin

void setup() {
   Serial.begin(115200);
 
   WiFiManager wifiManager;
  
  wifiManager.autoConnect("Ardu-iot");  
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
    dht.setup(17, DHTesp::DHT11); // Connect DHT sensor to GPIO 17
  pinMode(airPin, INPUT);
  
 

          Serial.println ("");
          Serial.println ("WiFi Connected!");
          Serial.println(WiFi.localIP());
     Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);


  
}

void loop() {

int airData = analogRead(airPin);

  if (Firebase.setInt(firebaseData, path+"/timmy/gasSensor", airData))
    {
      Serial.println("P");
    }
    else
    {
      Serial.println("FAILED");
      Serial.println("REASON: " + firebaseData.errorReason());
      }


   delay(dht.getMinimumSamplingPeriod());

  float Humidity = dht.getHumidity();
  float Temperature = dht.getTemperature();
Serial.println(Temperature);

if (Firebase.setFloat(firebaseData, path+"/timmy/Temp", Temperature))
    {
      Serial.println("P");
    }
    else
    {
      Serial.println("FAILED");
      Serial.println("REASON: " + firebaseData.errorReason());
      }

if (Firebase.setFloat(firebaseData, path+"/timmy/Humidity", Humidity))
    {
      Serial.println("P");
    }
    else
    {
      Serial.println("FAILED");
      Serial.println("REASON: " + firebaseData.errorReason());
      }
    
      

delay(2000);




}
