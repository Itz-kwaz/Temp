package com.nkwachi.temp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.nkwachi.temp.databinding.FragmentDaysBinding
import com.nkwachi.temp.weather.DailyWeather
import com.nkwachi.temp.weather.WeatherModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList


@AndroidEntryPoint
class DaysFragment : Fragment() {

    private val weatherModel: WeatherModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDaysBinding.inflate(inflater)
        binding.viewModel = weatherModel
        val list: ArrayList<DailyWeather> = ArrayList()
        binding.dailyWeatherList.adapter =  DailyAdapter(list)
        binding.lifecycleOwner = this

        return binding.root
    }


}