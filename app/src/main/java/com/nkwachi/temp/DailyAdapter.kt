package com.nkwachi.temp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nkwachi.temp.databinding.DaysListBinding
import com.nkwachi.temp.weather.DailyWeather

class DailyAdapter(private var dailyWeatherList: ArrayList<DailyWeather>):  RecyclerView.Adapter<DailyWeatherViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherViewHolder {
        val lp = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return DailyWeatherViewHolder(DaysListBinding.inflate(LayoutInflater.from(parent.context)).apply {
            root.layoutParams = lp
        })
    }

    override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
        val dailyWeather = dailyWeatherList[position]
        holder.bind(dailyWeather)
    }

    override fun getItemCount(): Int = dailyWeatherList.size

    fun updateList(newDailyList: ArrayList<DailyWeather>) {
        if(newDailyList.isNotEmpty()) {
            newDailyList.removeAt(0)
            dailyWeatherList = newDailyList
            notifyDataSetChanged()
        }

    }

}


class DailyWeatherViewHolder(private val binding: DaysListBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(dailyWeather: DailyWeather) {
        binding.dailyWeather = dailyWeather
        binding.executePendingBindings()
    }
}