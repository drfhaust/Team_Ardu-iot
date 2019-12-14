package ng.arduiot.ardu_iot

/**
 * Created by Akano Kola on 2019-11-25.
 */
data class Device(
    var nodeNumber:String? = null,
    var deviceType:String? = null,

    //Bulb
    var lightState:Boolean? = null,
    var lightBrightness:Int? = null,
    var batteryLevel:String? = null,

    //Switch
    var switch1:Boolean? = null,
    var switch2:Boolean? = null,

    //Battery
    var voltage:String? = null,
    var batteryState:Boolean? = null,

    //Temperature sensor
    var temperature:Float? = null,
    var gasSensor:Int? = null,
    var humidity:Int? = null

)

enum class DeviceTypes{SWITCH, BULB, Battery}