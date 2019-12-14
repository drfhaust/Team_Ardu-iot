package ng.arduiot.ardu_iot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_delete_confirmation_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class DeleteConfirmationDialog(val device:Device, val deleteDeviceListener:(Device)->Unit) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_confirmation_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmation_tv.text = "Are you sure you want to delete this ${device.deviceType} (${device.nodeNumber})"

        delete_device_btn.setOnClickListener {
            deleteDeviceListener(device)
            dismiss()
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
