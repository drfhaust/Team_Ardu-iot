package ng.arduiot.ardu_iot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.holder_battery.view.*
import kotlinx.android.synthetic.main.holder_bulb.view.*
import kotlinx.android.synthetic.main.holder_device.view.*
import kotlinx.android.synthetic.main.holder_device.view.delete_img
import kotlinx.android.synthetic.main.holder_device.view.description_1_tv
import kotlinx.android.synthetic.main.holder_device.view.description_2_tv
import kotlinx.android.synthetic.main.holder_device.view.device_type_tv
import kotlinx.android.synthetic.main.holder_device.view.node_number_tv
import kotlinx.android.synthetic.main.holder_device.view.value_1_tv
import kotlinx.android.synthetic.main.holder_device.view.value_2_tv
import kotlinx.android.synthetic.main.holder_switch.view.*
import kotlinx.android.synthetic.main.holder_temperature_sensor.view.*

/**
 * Created by Akano Kola on 2019-11-25.
 */

class DevicesAdapter(val deviceTypes:Array<String>, val deviceClickListener: (Device) -> Unit, val deviceDeleteClickListener: (Device) -> Unit) : RecyclerView.Adapter<DevicesAdapterViewHolder>() {

    var deviceList:MutableList<Device> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesAdapterViewHolder {
        val context = parent.context

        val layout = getLayout(viewType)

        LayoutInflater.from(context).inflate(layout, parent, false).run {
            return DevicesAdapterViewHolder(this)
        }
    }

    private fun getLayout(viewType: Int): Int {
        return when(viewType){
            1-> R.layout.holder_switch
            2-> R.layout.holder_bulb
            3-> R.layout.holder_battery
            4-> R.layout.holder_temperature_sensor
            else -> R.layout.holder_device
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(deviceList[position].deviceType){
            deviceTypes[1] -> 1
            deviceTypes[2] -> 2
            deviceTypes[3] -> 3
            deviceTypes[4] -> 4
            else -> 0
        }
    }

    override fun getItemCount() = deviceList.size

    override fun onBindViewHolder(holder: DevicesAdapterViewHolder, position: Int) {

        val itemView = holder.itemView


        deviceList[position].run{
            itemView.device_type_tv.text = "$deviceType:"
            itemView.node_number_tv.text = nodeNumber

            when(deviceType){
                deviceTypes[1] ->{
                    itemView.switch_1_tv.text = if(switch1!!) "ON" else "OFF"
                    itemView.switch_2_tv.text = if(switch2!!) "ON" else "OFF"
                }
                deviceTypes[2] ->{
                    itemView.light_state_tv.text = if(lightState!!) "ON" else "OFF"
                    itemView.brightness_tv.text = "$lightBrightness"
                    itemView.bulb_voltage_tv.text = "$voltage %"
                }
                deviceTypes[3] ->{
                    itemView.voltage_tv.text = "$voltage %"
                    itemView.battery_state_tv.text = if(batteryState!!) "ON" else "OFF"
                }
                deviceTypes[4] ->{
                    itemView.temperature_tv.text = "$temperature"
                    itemView.humidity_tv.text = "$humidity"
                    itemView.status_tv.text = if (gasSensor == null) "Not available" else if (gasSensor!! < 400) "Ok" else "Leaking"
                }
            }
            itemView.setOnClickListener {
                deviceClickListener(this)
            }
            itemView.delete_img.setOnClickListener {
                deviceDeleteClickListener(this)
            }
        }
    }

    fun addDevice(device: Device) {
        deviceList.add(device)
        notifyDataSetChanged()
    }

    fun clearData() {
        deviceList.clear()
        notifyDataSetChanged()
    }
}

class DevicesAdapterViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
