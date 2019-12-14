package ng.arduiot.ardu_iot


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_switch_pop_up.view.*


/**
 * A simple [Fragment] subclass.
 */
class SwitchPopUp(val device: Device, val deviceListener: (Device, String) -> Unit) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_switch_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        device.run {

            val oldNodeNumber = nodeNumber!!

            view.node_number_et.setText(nodeNumber)

            view.switch1.isChecked = switch1!!
            view.switch2.isChecked = switch2!!

            view.switch1.setOnCheckedChangeListener{ buttonView, isChecked ->
                switch1 = isChecked
            }

            view.switch2.setOnCheckedChangeListener{ buttonView, isChecked ->
                switch2 = isChecked
            }

            view.update_device_btn.setOnClickListener {
                this.nodeNumber = view.node_number_et.trimmedText()
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
