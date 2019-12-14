package ng.arduiot.ardu_iot

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nexmo.client.NexmoClient
import com.nexmo.client.sms.SmsSubmissionResponse
import com.nexmo.client.sms.messages.TextMessage
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import kotlinx.android.synthetic.main.activity_main.*


class DashBoardActivity : BaseActivity() {

    lateinit var devicesAdapter:DevicesAdapter
    lateinit var deviceRef: DatabaseReference
    lateinit var deviceTypes: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        deviceTypes = resources.getStringArray(R.array.device_types)

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        deviceRef = FirebaseDatabase.getInstance().getReference(userId)

        add_new_devices_img.setOnClickListener {
//            showAddDeviceDialog()
            val client = NexmoClient.Builder()
                .apiKey("1dbfd8a2")
                .apiSecret("xwXiOW8jAqlnd8LG")
                .build()

            val messageText = "Hello from Nexmo"
            val message = TextMessage("Nexmo", "2348099411303", messageText)

            val response: SmsSubmissionResponse = client.smsClient.submitMessage(message)

            response.messages.forEach {
                log("$it")
            }
        }

        add_new_devices_tv.setOnClickListener {
            showAddDeviceDialog()
        }

        house_Listing_tv.setOnClickListener {
            launchClass(HouseListingActivity::class.java)
        }

        sliderView.setOnClickListener {
            launchClass(HouseListingActivity::class.java)
        }

        setUpAdapter()
        fetchDevices()

        device_srl.isRefreshing = true

        device_srl.setOnRefreshListener{fetchDevices()}

        startSlideShowAdapter()
    }

    private fun fetchDevices() {
        deviceRef.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                device_srl.isRefreshing = false

                log("dataSnapshot.children.count: ${dataSnapshot.children.count()}")
                devicesAdapter.clearData()

                dataSnapshot.children.forEach {
                    it.getValue(Device::class.java)?.let {device->
                        devicesAdapter.addDevice(device)
                        log("${device.nodeNumber}")
                    }
                }
            }
        })
    }

    private fun setUpAdapter() {
        devicesAdapter = DevicesAdapter(deviceTypes, {
            showDevicePopUp(it)
        }, {
            showDeleteConfirmationDialog(it)

        })
        devices_rv.layoutManager = LinearLayoutManager(this)
        devices_rv.adapter = devicesAdapter
    }

    private fun showDeleteConfirmationDialog(device: Device) {
        val deleteConfirmationDialog = DeleteConfirmationDialog(device){
            deleteDevice(device.nodeNumber!!)
        }
        deleteConfirmationDialog.show(supportFragmentManager, null)
    }

    private fun deleteDevice(childPath: String) {
        deviceRef.child(childPath).removeValue()
    }

    private fun showDevicePopUp(device: Device) {
        when(device.deviceType){
            deviceTypes[1]-> {
                // Switch
                val switchPopUp = SwitchPopUp(device){updatedDevice, oldNodeNumber->
                    saveDevice(updatedDevice, oldNodeNumber)
                }
                switchPopUp.show(supportFragmentManager, null)
            }
            deviceTypes[2]-> {
                val bulbPopUp = BulbPopUp(device){updatedDevice, oldNodeNumber->
                    saveDevice(updatedDevice, oldNodeNumber)
                }
                bulbPopUp.show(supportFragmentManager, null)
            }
            deviceTypes[3]-> {
                val batteryPopUp = BatteryPopUp(device){updatedDevice, oldNodeNumber->
                    saveDevice(updatedDevice, oldNodeNumber)
                }
                batteryPopUp.show(supportFragmentManager, null)
            }
            deviceTypes[4]-> {

            }
        }
    }

    private fun showAddDeviceDialog() {
        val addDeviceDialog = AddDeviceDialog{
            setDeviceDefaultDetails(it)
        }
        addDeviceDialog.show(supportFragmentManager, null)
    }

    private fun setDeviceDefaultDetails(device: Device) {
        device.apply {
            when(deviceType){
                deviceTypes[1]-> {
                    switch1 = false
                    switch2 = false
                }
                deviceTypes[2]-> {
                    lightBrightness = 0
                    lightState = false
                    batteryLevel = "20"
                    voltage = "0"
                }
                deviceTypes[3]-> {
                    voltage = "0"
                    batteryState = false
                }
                deviceTypes[4]-> {
                    temperature = 0f
                    humidity = 0
                    gasSensor = 0
                }
            }
        }.run {
            saveDevice(this)
        }
    }

    // TODO Edit for updating devices
    private fun saveDevice(device: Device, oldNodeNumber:String? = null) {

        log("old number: $oldNodeNumber")

        if (oldNodeNumber != null && device.nodeNumber != oldNodeNumber){
            deleteDevice(oldNodeNumber)
        }

        deviceRef.child(device.nodeNumber!!).setValue(device, object:DatabaseReference.CompletionListener{
            override fun onComplete(p0: DatabaseError?, p1: DatabaseReference) {

            }
        })
    }

    private fun startSlideShowAdapter() {
        val houseListings = getHouseListings()
        sliderView.sliderAdapter = SliderAdapterExample(houseListings)
        sliderView.startAutoCycle()
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM)
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.scrollTimeInSec = 3
    }

    private fun getHouseListings(): MutableList<HouseListing> {
        val houseListings: MutableList<HouseListing> = mutableListOf()


        for(x in 0..20){
            HouseListing().apply {
                description = "2 bedroom apartment for rent"
                address = "No 102, biggie man street, Akoka"
                distance = "Within 10km from you"
                houseListings.add(this)
            }
        }

        return houseListings
    }
}

