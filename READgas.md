#include <FirebaseJson.h>
#include <ESP8266WiFi.h>

#define FIREBASE_HOST "https://arduiot.firebaseio.com/
#define FIREBASE_AUTH "6GQMMU5rCCocBJzfIaXRGpVpn4ICxhMhyMhkUWhH"

#define WIFI_SSID "Timmy"
#define WIFI_PASSWORD "timmydee"

int airPin = A0;

void setup() {
  //Serial Begin at 9600 Baud
  pinMode(airPin, INPUT);
  
  Serial.begin(9600);
     WiFi.begin (WIFI_SSID, WIFI_PASSWORD);
        while (WiFi.status() != WL_CONNECTED) {
           delay(500);
            Serial.print(".");
  
     }
          Serial.println ("");
          Serial.println ("WiFi Connected!");
          Serial.println(WiFi.localIP());
     Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);


  
}

void loop() {

int airData = analogRead(airPin);
Firebase.setInt("airData", airData);



delay(2000);




}
