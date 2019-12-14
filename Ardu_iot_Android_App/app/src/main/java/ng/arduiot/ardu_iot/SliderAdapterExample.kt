package ng.arduiot.ardu_iot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.smarteist.autoimageslider.SliderViewAdapter

/**
 * Created by Akano Kola on 2019-10-08.
 */

class SliderAdapterExample(val houseListing: MutableList<HouseListing>) : SliderViewAdapter<SliderAdapterExample.SliderAdapterVH>() {


    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null).run{
            return SliderAdapterVH(this)
        }
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        houseListing[position].run {
            viewHolder.descriptionTv.text = description
            viewHolder.addressTv.text = address
            viewHolder.distanceTv.text = distance
        }
    }


    override fun getCount() = houseListing.size

    class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        var descriptionTv: TextView = itemView.findViewById(R.id.description_tv)
        var addressTv: TextView = itemView.findViewById(R.id.address_tv)
        var distanceTv: TextView = itemView.findViewById(R.id.distance_tv)
    }
}





