package com.amit.airqualitymonitor.ui
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amit.airqualitymonitor.R
import com.amit.airqualitymonitor.database.model.Aqi
import com.amit.airqualitymonitor.databinding.AqiItemBinding
import org.ocpsoft.prettytime.PrettyTime
import java.util.*


class AqiAdapter(
    private var aqiList: MutableList<Aqi>,
    private val aqiViewModel: AqiViewModel
) : RecyclerView.Adapter<AqiAdapter.ViewHolder>() {


    companion object {
        private const val TAG = "AqiAdapter"
    }

    private lateinit var context : Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val itemBinding = AqiItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = aqiList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = aqiList[position]
        holder?.run {
            bind(item)
            binding.root.setOnClickListener {
                aqiViewModel.openCityAqiDetails.value = item
            }
        }
    }

    fun addToList(aqiList: List<Aqi>) {
        val prevCount = itemCount
        this.aqiList.clear()
        this.aqiList.addAll(aqiList)
        Log.d(TAG, "addToList: ${aqiList.size}")
        if (prevCount > aqiList.size) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeInserted(prevCount, aqiList.size)
        }
    }

    inner class ViewHolder(val binding: AqiItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Aqi) {
            item.aqi = String.format("%.2f", item.aqi).toDouble()
            val date = Date(item.timestamp)
            val prettyTime  = PrettyTime()
            item.prettyTime = prettyTime.format(date)
            item.color = getColor(item.aqi.toInt())
            with(binding) {
                aqiEntity = item
                Log.d(TAG, "bind: $aqiEntity")
                executePendingBindings()
            }
        }
    }

    fun getColor(aqi : Int) : Int {
       return when(aqi) {
            in 0..50 -> ContextCompat.getColor(context, R.color.good)
            in 51..100 -> ContextCompat.getColor(context, R.color.satisfactory)
            in 101..200 -> ContextCompat.getColor(context, R.color.moderate)
            in 201..300 -> ContextCompat.getColor(context, R.color.poor)
            in 301..400 -> ContextCompat.getColor(context, R.color.very_poor)
            else -> ContextCompat.getColor(context, R.color.severe)
        }
    }
}
