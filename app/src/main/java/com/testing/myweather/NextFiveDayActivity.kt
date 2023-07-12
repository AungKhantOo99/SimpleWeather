package com.testing.myweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.testing.myweather.model.temperature.Temperaturewithdate
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

class NextFiveDayActivity : AppCompatActivity() {
    private lateinit var mCharView: chartview
    private lateinit var back:ImageView
    private val min= ArrayList<Int>()
    private val max= ArrayList<Int>()
    private val day= ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_five_day)
        mCharView= findViewById(R.id.line_char)
        back=findViewById(R.id.back)
        back.setOnClickListener {
            super.onBackPressed()
        }

        val receivedData = intent.getSerializableExtra("TemperatureData") as ArrayList<Temperaturewithdate>
        Log.d("CheckReceiveData",receivedData.toString())
     for( data in receivedData.indices){
         Log.d("CheckReceiveData",receivedData[data].toString())

     }
        receivedData.forEach {
            val temperatureminInt = round(it.min.toDouble() - 273.15).toInt()
            val temperaturemaxInt = round(it.max.toDouble() - 273.15).toInt()
            min.add(temperatureminInt)
            max.add(temperaturemaxInt)
            day.add("${it.date.split("-")[1]}/${it.date.split("-")[2]}")
        }

//        val date = LocalDate.of(2023, 6, 28)  // Example date
//        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

//        Log.d("Day",dayOfWeek.toString())
        findViewById<TextView>(R.id.txt1).text = day[0]
        findViewById<TextView>(R.id.txt2).text = day[1]
        findViewById<TextView>(R.id.txt3).text = day[2]
        findViewById<TextView>(R.id.txt4).text = day[3]
        findViewById<TextView>(R.id.txt5).text = day[4]
        // remove sixth elements for want to show only five
        min.remove(5)
        max.remove(5)
        mCharView.setTempDay(min.toIntArray())
        mCharView.setTempNight(max.toIntArray())
        mCharView.invalidate()
    }
}