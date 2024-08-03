package com.example.weatherapp






//import androidx.databinding.DataBindingUtil
//import com.example.testapp.databinding.ActivityMainBinding
import android.app.Activity
import android.content.ContentValues.TAG
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import retrofit2.Call
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.ViewDataBinding
import androidx.test.espresso.remote.Converter
import androidx.test.espresso.remote.internal.deps.protobuf.Timestamp
//import com.example.weatherapp.data.ActivityMainBinding
import com.example.weatherapp.databinding.ActivityMainBinding
import org.hamcrest.Condition

import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// for a `var` variable also add



//7c9b2fce0e080cd5c343c8d90e2f19b2
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
          fetchWeatherData("Delhi")
         Searchcity()
        // Your code here to interact with views
    }

    private fun Searchcity() {
        val searchView=binding.searchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName:String)
    {
        val  retrofit=Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response=retrofit.getgetweatherdata(cityName,"7c9b2fce0e080cd5c343c8d90e2f19b2","metric")
        response.enqueue(object :Callback<Weather>
        {

            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                val  responseBody=response.body()
                if(response.isSuccessful&&responseBody!=null)
                {
                    val temperature=responseBody.main.temp.toString()
                    val humidity=responseBody.main.humidity
                    val windspeed=responseBody.wind.speed
                    val sunrise=responseBody.sys.sunrise
                    val sunset=responseBody.sys.sunset
                    val sealevel=responseBody.main.pressure
                    val condition=responseBody.weather.firstOrNull()?.main?: "unknown"
                    val maxtemp=responseBody.main.temp_max
                    val mintemp=responseBody.main.temp_min;


                    binding.temp.text="$temperature °C"
                    binding.weather.text=condition
                    binding.MaxTemp.text="Max Temp:$maxtemp °C"
                    binding.MinTemp.text="Min Temp:$mintemp °C"
                    binding.humidity.text="$humidity %"
                    binding.wind.text="$windspeed m/s"
                    binding.sunrise.text="$sunrise"
                    binding.sunset.text="$sunset"
                    binding.sea.text="$sealevel hPa"
                    binding.condition.text=condition
                    binding.day.text=dayName(System.currentTimeMillis())
                        binding.Date.text=date()
                        binding.cityName.text="$cityName"


                    changeImageAccordingToweatherCondition(condition)

                   // Log.d("TAG","response $temperature")
                }
            }




            override fun onFailure(call: Call<Weather>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun changeImageAccordingToweatherCondition(conditions: String) {

          when(conditions){

              "Clear Sky","Sunny","Clear" ->{
                  binding.root.setBackgroundResource(R.drawable.sunny_background)

              }

              "Partly Clouds","Clouds","Overcast","Mist","Foggy" ->{
                  binding.root.setBackgroundResource(R.drawable.colud_background)
              }

              "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain" ->{
                  binding.root.setBackgroundResource(R.drawable.rain_background)
              }


              "Light snow","Moderate snow","Heavy snow","Blizzard"->
              {
                  binding.root.setBackgroundResource(R.drawable.snow_background)
              }
              else->{

                  binding.root.setBackgroundResource(R.drawable.sunny_background)
              }

          }




    }

    private fun date(): CharSequence? {

        val Sdf=SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return Sdf.format(Date())
    }


    fun dayName(timestamp:Long):String
    {
        val Sdf=SimpleDateFormat("EEEE", Locale.getDefault())
        return Sdf.format(Date())
    }
}

private fun android.widget.SearchView.setOnQueryTextListener(onQueryTextListener: SearchView.OnQueryTextListener) {
    TODO("Not yet implemented")
}


