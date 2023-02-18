package com.example.weatherapp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.model.WeatherViewModel
import com.example.weatherapp.network.FiveName
import com.example.weatherapp.network.NextHour
import com.example.weatherapp.network.WeatherData
import com.example.weatherapp.network.WeatherQuery
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org"
private const val CITY_NAME_URL = "https://api.openweathermap.org"


// Retrofit builder hai
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json {
        ignoreUnknownKeys = true
    }.asConverterFactory(MediaType.get("application/json")))
    .baseUrl(BASE_URL)
    .build()
private const val apiKey = "b04650779f1e4c273c1d11109f160bde"

interface WeatherApiService {
    @GET("data/2.5/weather?")
    suspend fun getDetails(
        @Query("q") city: String = "london",
        @Query("appid") apikey: String = apiKey
    ): Response<WeatherData>
}

object WeatherApi {

    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}
//--------------------------------------------------------------
private val retrofitcity = Retrofit.Builder()
    .addConverterFactory(Json {
        ignoreUnknownKeys = true
    }.asConverterFactory(MediaType.get("application/json")))
    .baseUrl(CITY_NAME_URL)
    .build()

interface Fivecity {
    @GET("geo/1.0/direct?")
    suspend fun getCityName(
        @Query("q") city: String,
        @Query("limit") limit: Int,
        @Query("appid") apikey: String = apiKey
    ): Response<List<FiveName>>
}

object FiveCityApi {
    val retrofitService: Fivecity by lazy {
        retrofitcity.create(Fivecity::class.java)
    }
}
//--------------------------------------------------------------
private val nexthours = Retrofit.Builder()
    .addConverterFactory(Json {
        ignoreUnknownKeys = true
    }.asConverterFactory(MediaType.get("application/json")))
    .baseUrl(BASE_URL)
    .build()
interface NextHourApi{
    @GET("data/2.5/forecast?")
    suspend fun getNextHour(
        @Query("q") city : String,
        @Query("appid") apikey: String = apiKey
    ) : Response<NextHour>
}
object NextHourObject{
    val nextHourService : NextHourApi by lazy {
        nexthours.create(NextHourApi::class.java)
    }
}

//--------------------------------------------------------------




