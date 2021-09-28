package com.nkwachi.temp


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nkwachi.temp.databinding.FragmentHomeBinding
import com.nkwachi.temp.weather.HourlyWeather
import com.nkwachi.temp.weather.WeatherModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


private const val TAG = "HomeFragment"


@AndroidEntryPoint
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

    fun navigateTo7DaysScreen() {
        findNavController().navigate(R.id.action_homeFragment_to_daysFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}