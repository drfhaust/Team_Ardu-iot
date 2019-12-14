package ng.arduiot.ardu_iot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_house_listing.*

class HouseListingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_listing)

        setUpAdapter()
    }

    private fun setUpAdapter() {
        val adapter = HouseListingAdapter(getHouseListings())

        house_Listing_rv.layoutManager = LinearLayoutManager(this)
        house_Listing_rv.adapter = adapter
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
