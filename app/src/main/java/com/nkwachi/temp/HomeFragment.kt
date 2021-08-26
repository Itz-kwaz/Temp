package com.nkwachi.temp


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    fun navigateTo7DaysScreen() {
        findNavController().navigate(R.id.action_homeFragment_to_daysFragment)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
        val textView = view.findViewById<TextView>(R.id.textView10)
        textView.setOnClickListener {
            val v: Vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            val pattern = longArrayOf(0, 100, 1000)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26 and above
                v.vibrate(VibrationEffect.createWaveform(pattern, 0));
            } else {
                // Below API 26
                v.vibrate(pattern, 0);
            }
        }

        val today = view.findViewById<TextView>(R.id.today_text_view)
        today.setOnClickListener {
            val v: Vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.cancel()
        }
    }
}