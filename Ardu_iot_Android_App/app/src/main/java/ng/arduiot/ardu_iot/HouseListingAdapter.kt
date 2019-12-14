package ng.arduiot.ardu_iot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_main.*

class HouseListingAdapter(val houseListings: MutableList<HouseListing>) : RecyclerView.Adapter<HouseListingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseListingViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.holder_house_listing, parent, false).run{
            return HouseListingViewHolder(this)
        }
    }

    override fun getItemCount() = houseListings.size

    override fun onBindViewHolder(holder: HouseListingViewHolder, position: Int) {
            holder.bindData(houseListings[position])
    }

}

class HouseListingViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

    var descriptionTv: TextView = itemView.findViewById(R.id.description_tv)
    var addressTv: TextView = itemView.findViewById(R.id.address_tv)
    var distanceTv: TextView = itemView.findViewById(R.id.distance_tv)
    var sliderView: SliderView = itemView.findViewById(R.id.sliderView)

    fun bindData(houseListing: HouseListing) {
        houseListing.run {
            descriptionTv.text = description
            addressTv.text = address
            distanceTv.text = distance
        }

        val images = mutableListOf(R.drawable.house, R.drawable.house, R.drawable.house,
            R.drawable.house, R.drawable.house)

        sliderView.sliderAdapter = HouseImagesSliderAdapter(images)
        sliderView.startAutoCycle()
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM)
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.scrollTimeInSec = 3
    }

}
