package com.nkwachi.temp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nkwachi.temp.databinding.FragmentHomeBinding
import com.nkwachi.temp.weather.HourlyWeather
import com.nkwachi.temp.weather.WeatherModel
import java.util.*


private const val TAG = "HomeFragment"


class HomeFragment : Fragment() {

    private val weatherModel: WeatherModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.viewModel = weatherModel
        binding.homeFragment = this@HomeFragment
        val list: ArrayList<HourlyWeather> = ArrayList()
        binding.hourlyWeatherList.adapter = HourlyAdapter(list)
        binding.lifecycleOwner = this
        return binding.root
    }

   fun navigateTo7DaysScreen(){
       findNavController().navigate(R.id.action_homeFragment_to_daysFragment)
   }

    fun showToast() {
        val toast = Toast.makeText(context,"Today is a good day",Toast.LENGTH_SHORT)
        toast.show()
    }



}