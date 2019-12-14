package ng.arduiot.ardu_iot


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_add_device_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class AddDeviceDialog(val getDevice: (Device) -> Unit) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_device_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_device_btn.setOnClickListener {
            if (node_number_et.isValid() && isDeviceTypeSelected()){
                createDevice()
            }
        }
    }

    private fun createDevice() {
        val device = Device()
        device.nodeNumber = node_number_et.trimmedText()
        device.deviceType = device_type_sp.selectedItem.toString()

        getDevice(device)
        dismiss()
    }

    private fun isDeviceTypeSelected(): Boolean {
        val valid = device_type_sp.selectedItem.toString() != "- Select a device type -"
        if (!valid) Toast.makeText(activity!!, "Select a device type", Toast.LENGTH_SHORT).show()
        return valid
    }

}
