#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>
#include <WiFiManager.h>

#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>


#define DHTPIN 4    //change to any pin
#define DHTTYPE    DHT11     // DHT 11
DHT_Unified dht(DHTPIN, DHTTYPE);

uint32_t delayMS;


String path = "/FH8dJILtpnfvjOybVGNyzpCXERc2";
FirebaseData firebaseData;
#define FIREBASE_HOST "https://arduiot.firebaseio.com/"
#define FIREBASE_AUTH "6GQMMU5rCCocBJzfIaXRGpVpn4ICxhMhyMhkUWhH"


int airPin = A0;

void setup() {
   Serial.begin(115200);
 
   WiFiManager wifiManager;
  
  wifiManager.autoConnect("Ardu-iot");  
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  dht.begin();
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
   dht.humidity().getSensor(&sensor);
  delayMS = sensor.min_delay / 1000;
  pinMode(airPin, INPUT);
  
 

          Serial.println ("");
          Serial.println ("WiFi Connected!");
          Serial.println(WiFi.localIP());
     Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);


  
}

void loop() {

int airData = analogRead(airPin);

  if (Firebase.setInt(firebaseData, path+"/gasSensor", airData))
    {
      Serial.println("P");
    }
    else
    {
      Serial.println("FAILED");
      Serial.println("REASON: " + firebaseData.errorReason());
      }


      delay(delayMS);

  sensors_event_t event;
  dht.temperature().getEvent(&event);

    Serial.print(F("Temperature: "));
   float Temperature =event.temperature*100;
    Serial.println(F("°C"));
  dht.humidity().getEvent(&event);
 
    Serial.print(F("Humidity: "));
   int Humidity =event.relative_humidity;
    Serial.println(F("%"));

if (Firebase.setFloat(firebaseData, path+"/Temp", Temperature))
    {
      Serial.println("P");
    }
    else
    {
      Serial.println("FAILED");
      Serial.println("REASON: " + firebaseData.errorReason());
      }

if (Firebase.setInt(firebaseData, path+"/Humidity", Humidity))
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
