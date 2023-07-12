package com.testing.myweather.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.testing.myweather.R
import com.testing.myweather.getday
import com.testing.myweather.model.fivedayweatherinfo.WeatherResponse
import com.testing.myweather.model.temperature.Temperaturewithdate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

class NextFiveDayAdapter(private val context: Context,private val item:WeatherResponse,val data:ArrayList<Temperaturewithdate>):RecyclerView.Adapter<NextFiveDayAdapter.ViewHolder>() {
    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.dailyweatheritem,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return data.size
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val year=data[position].date.split("-")[0].toInt()
        val month=data[position].date.split("-")[1].toInt()
        val day=data[position].date.split("-")[2].toInt()
        val temperaturemin = round(data[position].min.toDouble()-273.15).toInt()
        val temperaturemax = round(data[position].max.toDouble()-273.15).toInt()
        if(position==0){
            holder.itemView.findViewById<TextView>(R.id.day).text = "Today"
        }else if(position==1){
            holder.itemView.findViewById<TextView>(R.id.day).text = "Tomorrow"
        }else{
            holder.itemView.findViewById<TextView>(R.id.day).text = getday(year,month,day)
        }
        holder.itemView.findViewById<TextView>(R.id.temperature).text = "$temperaturemin°C / $temperaturemax°C"
        holder.itemView.findViewById<TextView>(R.id.date).text = data[position].date
    }

}