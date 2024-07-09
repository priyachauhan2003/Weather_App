package com.techmania.weatherapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
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

// a0db9a967932621841d7aa3927ab32df
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData()
    }

    private fun fetchWeatherData() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(Apiinterface::class.java)

        val response = retrofit.getWeatherData("Kurukshetra", "a0db9a967932621841d7aa3927ab32df", "metric")
        response.enqueue(object : Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
               val responseBody = response.body()
               if(response.isSuccessful && responseBody != null){
                   Toast.makeText(this@MainActivity, "Weather Data Fetched", Toast.LENGTH_LONG).show()
                   val temperature = responseBody.main.temp.toString()
                   //Log.d("TAG", "onResponse: $temperature")
                   val humidity = responseBody.main.humidity.toString()
                   val windSpeed = responseBody.wind.speed.toString()
                   val sunrize = responseBody.sys.sunrise.toString()
                   val sunset = responseBody.sys.sunset.toString()
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
                   binding.sunrise.text="$sunrize"
                   binding.sunset.text="$sunset"
                   binding.sea.text="$sealevel hPa"
                   binding.conditions.text=condition

               }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}