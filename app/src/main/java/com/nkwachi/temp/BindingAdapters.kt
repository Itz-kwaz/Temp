package com.nkwachi.temp

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nkwachi.temp.weather.DailyWeather
import com.nkwachi.temp.weather.HourlyWeather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<HourlyWeather>?) {
    val adapter = recyclerView.adapter as HourlyAdapter
    if (data != null) {
        adapter.updateList(data as ArrayList<HourlyWeather>)
    }
}

@BindingAdapter("listDataDaily")
fun bindRecyclerViewDailyWeather(recyclerView: RecyclerView, data: List<DailyWeather>?) {
    val adapter = recyclerView.adapter as DailyAdapter
    if (data != null) {
        adapter.updateList(data as ArrayList<DailyWeather>)
    }
}



@BindingAdapter("date")
fun bindDate(textView: TextView, date: Long?) {
    if (date != null) {
        textView.text = getDateString(date)
    }
}

@BindingAdapter("dayOfTheWeek")
fun bindDayOfTheWeek(textView: TextView, date: Long?) {
    if (date != null) {
        textView.text = getDayOfTheWeek(date)
    }
}

private fun getDateString(time: Long) : String {
    val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

    return simpleDateFormat.format(time * 1000L)
}

private fun getDayOfTheWeek(time: Long) : String {
    val simpleDateFormat = SimpleDateFormat("E", Locale.ENGLISH)

    return simpleDateFormat.format(time * 1000L)
}


