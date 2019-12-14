package ng.arduiot.ardu_iot


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.fragment_bulb_pop_up.*

/**
 * A simple [Fragment] subclass.
 */
class BulbPopUp(val device: Device, val deviceListener: (Device, String) -> Unit) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bulb_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        device.run {

            val oldNodeNumber = nodeNumber!!

            node_number_et.setText(oldNodeNumber)

            light_state_switch.isChecked = lightState!!

            lightBrightness!!.run {
                bulb_slider.value = this.toFloat()
                bulb_brightness_tv.text = "Bulb Brightness: ${this}"
            }


            light_state_switch.setOnCheckedChangeListener{ _, isChecked ->
                lightState = isChecked
            }

            bulb_slider.setOnChangeListener { _, value ->
                value.toInt().run{
                    lightBrightness = this
                    bulb_brightness_tv.text = "Bulb Brightness: ${this}"
                }
            }

            update_device_btn.setOnClickListener {
                this.nodeNumber = node_number_et.trimmedText()
                deviceListener(this, oldNodeNumber)
                dismiss()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }
}
