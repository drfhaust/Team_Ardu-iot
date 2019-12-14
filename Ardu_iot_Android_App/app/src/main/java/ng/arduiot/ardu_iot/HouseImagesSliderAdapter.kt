package ng.arduiot.ardu_iot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smarteist.autoimageslider.SliderViewAdapter

class HouseImagesSliderAdapter(val images: MutableList<Int>) : SliderViewAdapter<HouseImagesSliderAdapter.HouseImagesSliderHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup): HouseImagesSliderHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.holder_house_image_slider, null).run{
            return HouseImagesSliderHolder(this)
        }
    }

    override fun onBindViewHolder(viewHolder: HouseImagesSliderHolder, position: Int) {
        val image = images[position]
    }


    override fun getCount() = images.size

    class HouseImagesSliderHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {

    }
}



