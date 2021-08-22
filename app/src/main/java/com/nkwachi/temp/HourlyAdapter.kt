package com.nkwachi.temp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nkwachi.temp.databinding.HomeListBinding
import com.nkwachi.temp.weather.HourlyWeather

class HourlyAdapter(private var hourlyWeatherList: ArrayList<HourlyWeather>):  RecyclerView.Adapter<HourlyWeatherViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        return HourlyWeatherViewHolder(HomeListBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val hourlyWeather = hourlyWeatherList[position]
        holder.bind(hourlyWeather)
    }

    override fun getItemCount(): Int = hourlyWeatherList.size

    fun updateList(newHourlyList: ArrayList<HourlyWeather>) {
        hourlyWeatherList = newHourlyList
        notifyDataSetChanged()
    }

}


class HourlyWeatherViewHolder(private var binding: HomeListBinding ): RecyclerView.ViewHolder(binding.root) {

    fun bind(hourlyWeather: HourlyWeather) {
        binding.hourlyWeather = hourlyWeather
        binding.executePendingBindings()
    }
}