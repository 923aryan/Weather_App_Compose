package com.example.weatherapp.model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.FiveCityApi
import com.example.weatherapp.NextHourObject
import com.example.weatherapp.WeatherApi
import com.example.weatherapp.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.Response

class WeatherViewModel : ViewModel() {
    private val apikey = "b04650779f1e4c273c1d11109f160bde"
    private val weathercity = MutableStateFlow(WeatherQuery())
    var cityinstance: StateFlow<WeatherQuery> = weathercity.asStateFlow()
    private val _weatherState = MutableStateFlow(
        WeatherData(
            "",
            0,
            Coord(0.0, 0.0),
            listOf(Weather("", "", 0, "")),
            Main(0.0, 0, 0, 0.0, 0.0, 0.0),
            0,
            Wind(0, 0.0),
            Clouds(0),
            0,
            Sys("", 0, 0, 0, 0),
            0,
            0,
            ""
        )
    )
    var weatherState: StateFlow<WeatherData> = _weatherState.asStateFlow()
    private val cityName = MutableStateFlow(listOf( FiveName("", "")))
    var cityNameInstance: StateFlow<List<FiveName>> = cityName.asStateFlow()
    private var NextHourState  = MutableStateFlow(NextHour(emptyList()))
    val NextHourInstance : StateFlow<NextHour> = NextHourState.asStateFlow()
    private val enabled = MutableStateFlow(mutableStateListOf(true, false, false))
    var enabledInstance : StateFlow<SnapshotStateList<Boolean>> = enabled.asStateFlow()
    init {
        getWeatherData()
    }

    fun update(cityUpdated: String, state: String) {
        weathercity.update { c -> c.copy(city = cityUpdated, stateCode = state) }
        getWeatherData()
    }

    private fun getWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {

           // Log.d("city is", cityinstance.value.city)
            try {
                val weatherData =
                    WeatherApi.retrofitService.getDetails(cityinstance.value.city+","+cityinstance.value.stateCode, apikey = apikey)
                val serializedWeatherData = Json.encodeToString(weatherData.body())
                val d = Json.decodeFromString<WeatherData>(serializedWeatherData)
                _weatherState.value = d
                Log.d("mynew", _weatherState.value.toString())
                getNextHour()
            } catch (e: Exception) {
                Log.d("city Not found", cityinstance.value.city)
            }

        }
    }

    fun getNames(updatedName: String) {

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val cityData: Response<List<FiveName>> =
                        FiveCityApi.retrofitService.getCityName(updatedName, 10, apikey = apikey)
                    Log.d("cityJson", cityData.body().toString())
                    val serializedWeatherData = Json.encodeToString(cityData.body())
                    val d = Json.decodeFromString<List<FiveName>>(serializedWeatherData)
//                for(i in d)
//                    Log.d("here", "${i.name} ${i.state}")
                    cityName.value = d
                } catch (e: Exception) {
                    Log.d("Not initialized", e.toString())
                }
            }
    }
    fun getNextHour()
    {
        try {
            viewModelScope.launch {
                val NextHourData : Response<NextHour> = NextHourObject.nextHourService.getNextHour(cityinstance.value.city+","+cityinstance.value.stateCode, apikey)
                val serializedWeatherData = Json.encodeToString(NextHourData.body())
                val d = Json.decodeFromString<NextHour>(serializedWeatherData)
                NextHourState.value = d
                Log.d("NextHour", NextHourInstance.value.list.toString())
            }
        }
        catch (e: Exception)
        {
            Log.d("NextHourFailed", e.toString())
        }
    }


}