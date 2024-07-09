package com.techmania.weatherapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.techmania.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// a0db9a967932621841d7aa3927ab32df
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Kurukshetra")
        SearchCity()
    }

    private fun SearchCity() {
       val searchView = binding.searchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchWeatherData(cityName:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(Apiinterface::class.java)

        val response = retrofit.getWeatherData(cityName, "a0db9a967932621841d7aa3927ab32df", "metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
               val responseBody = response.body()
               if(response.isSuccessful && responseBody != null){
                  // Toast.makeText(this@MainActivity, "Weather Data Fetched", Toast.LENGTH_LONG).show()
                   val temperature = responseBody.main.temp.toString()
                   //Log.d("TAG", "onResponse: $temperature")
                   val humidity = responseBody.main.humidity.toString()
                   val windSpeed = responseBody.wind.speed.toString()
                   val sunrize = responseBody.sys.sunrise.toLong()
                   val sunset = responseBody.sys.sunset.toLong()
                   val sealevel = responseBody.main.pressure.toString()
                   val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                   val maxtemp = responseBody.main.temp_max.toString()
                   val mintemp = responseBody.main.temp_min.toString()
                   binding.temp.text="$temperature °C"
                   binding.weather.text=condition
                   binding.maxtemp.text="Max : $maxtemp °C"
                   binding.mintemp.text="Min : $mintemp °C"
                   binding.humid.text="$humidity %"
                   binding.windspeed.text="$windSpeed m/s"
                   binding.sunrise.text="${time(sunrize)}"
                   binding.sunset.text="${time(sunset)}"
                   binding.sea.text="$sealevel hPa"
                   binding.conditions.text=condition
                   binding.day.text=dayName(System.currentTimeMillis())
                   binding.date.text=date()
                   binding.cityname.text=cityName


                   changeImageAccordingToWeatherCondition(condition)
               }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun changeImageAccordingToWeatherCondition(condition: String){
        when(condition){
            "Haze" ->{
                binding.root.setBackgroundResource(R.drawable.hazebg)
                binding.lottieAnimationView10.setAnimation(R.raw.haze)
            }
            "Clear Sky" , "Sunny" , "Clear"->{
                binding.root.setBackgroundResource(R.drawable.clearbg)
                binding.lottieAnimationView10.setAnimation(R.raw.sunny)
            }
            "Light Rain" , "Drizzel" , "Moderate Rain" , "Heavy Rain"->{
                binding.root.setBackgroundResource(R.drawable.rainbg)
                binding.lottieAnimationView10.setAnimation(R.raw.rainy)
            }
            "Partly Clouds" , "Overcast Clouds" , "Foggy","Clouds"->{
                binding.root.setBackgroundResource(R.drawable.cloudybg)
                binding.lottieAnimationView10.setAnimation(R.raw.cloudy)
            }
            "Mist" , "Smoke" , "Dust" ->{
                binding.root.setBackgroundResource(R.drawable.mistbg)
                binding.lottieAnimationView10.setAnimation(R.raw.mist)
            }
            "Thunderstrom","Lightening"->{
                binding.root.setBackgroundResource(R.drawable.thunderstrombg)
                binding.lottieAnimationView10.setAnimation(R.raw.thunderstrom)
            }
            else->{
                binding.root.setBackgroundResource(R.drawable.clearbg)
                binding.lottieAnimationView10.setAnimation(R.raw.sunny)
            }
        }
        binding.lottieAnimationView10.playAnimation()
    }

    private fun time(timestamp: Long):String{
        val sdf = SimpleDateFormat("HH:MM", Locale.getDefault())
        return sdf.format(Date(timestamp*1000))
    }

    private fun date():String{
        val sdf = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())
        return sdf.format(Date())
    }

    fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }
}