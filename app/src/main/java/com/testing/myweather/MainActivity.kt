package com.testing.myweather

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.testing.myweather.adapter.NextFiveDayAdapter
import com.testing.myweather.model.fivedayweatherinfo.Detail
import com.testing.myweather.model.temperature.Temperature
import com.testing.myweather.model.temperature.Temperaturewithdate
import com.testing.myweather.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var toolbartitle: TextView
    private lateinit var weatherinfo: TextView
    private lateinit var temperature:TextView
    private lateinit var temperaturesymbol:TextView
    private lateinit var showdate:TextView
    private lateinit var showday:TextView
    private lateinit var sunraise:TextView
    private lateinit var sunset:TextView
    private lateinit var morefivedayRecyclerView: RecyclerView
    private lateinit var nextfivedays:Button
    private lateinit var curvebg:RelativeLayout
    private lateinit var mainbg:RelativeLayout
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var progressDialog: ProgressDialog
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
     var description=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout)
        appBarLayout = findViewById(R.id.appBarLayout)
        toolbartitle = findViewById(R.id.toolbar_title)
        weatherinfo = findViewById(R.id.weather_info)
        morefivedayRecyclerView=findViewById(R.id.more_five_day_recyclerview)
        temperature=findViewById(R.id.temperature)
        temperaturesymbol=findViewById(R.id.temperature_symbol)
        nextfivedays=findViewById(R.id.nextfiveday)
        showdate=findViewById(R.id.show_date)
        showday=findViewById(R.id.show_day)
        curvebg=findViewById(R.id.bg)
        mainbg=findViewById(R.id.main_bg)
        sunraise=findViewById(R.id.sun_raise_time)
        sunset=findViewById(R.id.sun_set_time)
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
         fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        setSupportActionBar(toolbar)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading Please  wait...")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setCancelable(false)
        progressDialog.show()
//        loading = AlertDialog.Builder(this)
//            .setTitle("Loading")
//            .setMessage("Please Wait...")
//            .setCancelable(false)
//            .create()
//        loading.show()

//        val dialogBuilder = AlertDialog.Builder(this)
//        val inflater = layoutInflater
//        val dialogView = inflater.inflate(R.layout.custom_dialog, null)
//        dialogBuilder.setView(dialogView)
//        val dialog = dialogBuilder.create()
//        dialog.show()

        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val isCollapsed = Math.abs(verticalOffset) == appBarLayout.totalScrollRange
            setToolbarBackground(isCollapsed)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {

        super.onStart()
        if (checkLocationPermission()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        Log.d("Check", "Startv0")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("Check", "Start")
                LocationServices.getFusedLocationProviderClient(this@MainActivity).lastLocation
                    .addOnSuccessListener { location: Location? ->
                        Log.d("Check", location.toString())
                        location?.let {
                            PerformApiCall(
                                location.latitude.toString(),
                                location.longitude.toString()
                            )
                        }
                    }
                    .addOnFailureListener {
                        Log.d("CheckErr", it.toString())
                    }

//                val locationRequest = LocationRequest.create().apply {
//                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//                    interval = 10000 // 10 seconds
//                    fastestInterval = 5000 // 5 seconds
//                }
//
//                val locationCallback = object : LocationCallback() {
//                    @RequiresApi(Build.VERSION_CODES.O)
//                    override fun onLocationResult(p0: LocationResult) {
//                        p0?.lastLocation?.let { location ->
//                            Log.d("Checkapi30", location.toString())
//                            PerformApiCall(location.latitude.toString(), location.longitude.toString())
//                            // You can stop location updates if needed after you get the location
//                            fusedLocationClient.removeLocationUpdates(this)
//                        }
//                    }
//                }
//
//                fusedLocationClient.requestLocationUpdates(
//                    locationRequest,
//                    locationCallback,
//                    null
//                )
            } catch (e: Exception) {
                Log.d("CheckErrorapi30", e.message.toString())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        Log.d("Checkapi30", "Start")

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000 // 10 seconds
            fastestInterval = 5000 // 5 seconds
        }

        val locationCallback = object : LocationCallback() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onLocationResult(p0: LocationResult) {
                p0?.lastLocation?.let { location ->
                    Log.d("Checkapi30", location.toString())
                    PerformApiCall(location.latitude.toString(), location.longitude.toString())
                    // You can stop location updates if needed after you get the location
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun PerformApiCall(lat: String, lon: String) {
        val TemperatureMap = mutableMapOf<String, MutableList<Temperature>>()
        val FinalTemperature = ArrayList<Temperaturewithdate>()
        val Todaydetail = ArrayList<Detail>()
        weatherViewModel.getWeatherData(lat,lon).observe(this) { it ->
            val todaydate = it.list[0].dt_txt.split(" ")[0]
            it.list.forEach {
                val date = it.dt_txt.split(" ")[0]
                if (date == todaydate) {
                    Todaydetail.add(it)
                    Log.d("today", Todaydetail.toString())
                }
                val temperature =
                    Temperature(it.main.temp_min.toString(), it.main.temp_max.toString())
                if (TemperatureMap.containsKey(date)) {
                    // Add the Tem to the existing Date
                    TemperatureMap[date]?.add(temperature)
                } else {
                    // Create a new Date and add the tem to it
                    val List = mutableListOf<Temperature>()
                    List.add(temperature)
                    TemperatureMap[date] = List
                }
            }
            for ((date, temperatureList) in TemperatureMap) {
                //get min and max temperature of one key or date
                val smallestMin = temperatureList.minByOrNull { it.min }
                val biggestMax = temperatureList.maxByOrNull { it.max }
                if (smallestMin != null && biggestMax != null) {
                    val data = Temperaturewithdate(date, smallestMin.min, biggestMax.max)
                    FinalTemperature.add(data)
                }
            }
            runOnUiThread {
                progressDialog.dismiss()
                sunraise.text = getsunraisesettime(it.city.sunrise.toLong()) + " AM"
                sunset.text = getsunraisesettime(it.city.sunset.toLong()) + " PM"
                description = it.list[0].weather[0].description
                toolbartitle.text = it.city.name
                // use round to get nearest integer values from the double
                val temperatureInt = round(it.list[0].main.temp - 273.15).toInt()
                val temperatureMin = round(FinalTemperature[0].min.toDouble() - 273.15).toInt()
                val temperatureMax = round(FinalTemperature[0].max.toDouble() - 273.15).toInt()
                val year = FinalTemperature[0].date.split("-")[0].toInt()
                val month = FinalTemperature[0].date.split("-")[1].toInt()
                val day = FinalTemperature[0].date.split("-")[2].toInt()
                val currentdescription = it.list[0].weather[0].description
                temperature.text = temperatureInt.toString()
                showdate.text = FinalTemperature[0].date
                showday.text = getday(year, month, day)
                //degrees circus symbol
                temperaturesymbol.text = "\u00B0C"
                weatherinfo.text = "$currentdescription  $temperatureMin°C / $temperatureMax°C"
                morefivedayRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                morefivedayRecyclerView.adapter = NextFiveDayAdapter(this, it!!, FinalTemperature)

                nextfivedays.setOnClickListener {
                    val intent = Intent(this, NextFiveDayActivity::class.java)
                    intent.putExtra("TemperatureData", FinalTemperature as java.io.Serializable)
                    startActivity(intent)
                }
            }

        }

//        callApi(lat, lon) { weatherData ->
//            runOnUiThread {
//             morefivedayRecyclerView.layoutManager=LinearLayoutManager(this@MainActivity)
//                morefivedayRecyclerView.adapter=NextFiveDayAdapter(this,weatherData!!)
//            }
//        }
    }

    @SuppressLint("ResourceType")
    private fun setToolbarBackground(isCollapsed: Boolean = false) {
        if (isCollapsed) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor=Color.GRAY
            toolbar.visibility=View.VISIBLE
            toolbar.setBackgroundColor(Color.GRAY)
            nextfivedays.visibility=View.GONE
            curvebg.setBackgroundColor(Color.GRAY)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
            toolbar.setBackgroundColor(Color.TRANSPARENT)
            toolbar.visibility=View.GONE
            nextfivedays.visibility=View.VISIBLE
            Log.d("description",description)
            if(description.contains("clouds")){
                mainbg.setBackgroundResource(R.drawable.couldly)
                curvebg.setBackgroundResource(R.drawable.couldly)
            }else
            if(description.contains("rain")){
                mainbg.setBackgroundResource(R.drawable.raining)
                curvebg.setBackgroundResource(R.drawable.raining)
            }else
            if(description.contains("sun")){
                mainbg.setBackgroundResource(R.drawable.sunshine)
                curvebg.setBackgroundResource(R.drawable.sunshine)
            }else
            if(description.contains("than")){
                mainbg.setBackgroundResource(R.drawable.thanderstone)
                curvebg.setBackgroundResource(R.drawable.thanderstone)
            }else{
                mainbg.setBackgroundResource(R.color.toolbar_color)
                curvebg.setBackgroundResource(R.color.toolbar_color)
//                curvebg.setBackgroundColor(R.color.bg)
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val granted = PackageManager.PERMISSION_GRANTED

        return ContextCompat.checkSelfPermission(this, permission) == granted
    }

    private fun requestLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        ActivityCompat.requestPermissions(this, arrayOf(permission), LOCATION_PERMISSION_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this@MainActivity, "Cannot show your weather info because you don't allow location", Toast.LENGTH_SHORT).show()
            }
        }
    }

}