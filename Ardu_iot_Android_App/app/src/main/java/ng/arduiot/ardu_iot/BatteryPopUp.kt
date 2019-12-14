package ng.arduiot.ardu_iot


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_battery_pop_up.*

/**
 * A simple [Fragment] subclass.
 */
class BatteryPopUp(val device: Device, val deviceListener: (Device, String) -> Unit) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_battery_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        device.run {

            val oldNodeNumber = nodeNumber!!

            node_number_et.setText(oldNodeNumber)

            battery_state_switch.isChecked = batteryState!!

            battery_state_switch.setOnCheckedChangeListener{ _, isChecked ->
                batteryState = isChecked
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
