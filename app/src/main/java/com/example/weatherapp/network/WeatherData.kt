package com.example.weatherapp.network

import kotlinx.serialization.Serializable

@Serializable
data class NextHour(
    val list : List<WeatherData>
)
@Serializable
data class WeatherData(
    val base:String? = null,
    val cod: Int? = null,
    val coord: Coord? = null,
    val weather: List<Weather>? = null,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Int,
    val sys: Sys? =null,
    val timezone: Int? = null,
    val id: Int? = null,
    val name: String? = null,
    val dt_txt: String? = null
)
data class WeatherQuery(
    val city : String = "patna",
    val stateCode : String = ""
)
@Serializable
data class Wind(
    val deg: Int,
    val speed: Double
)
@Serializable
data class Clouds(
    val all: Int
)
@Serializable
data class Coord(
    val lat: Double,
    val lon: Double
)
@Serializable
data class Main(
    val feels_like: Double?,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val temp_max: Double,
    val temp_min: Double,
)
@Serializable
data class Weather(
    val description: String? = null,
    val icon: String? = null,
    val id: Int? = null,
    val main: String? = null
)
@Serializable
data class Sys(
    val country: String? = null,
    val id: Int? = null,
    val sunrise: Int? = null,
    val sunset: Int? = null,
    val type: Int? = null,
    val pod: String? = null
)


//@Serializable
//data class Data(
//    val temp : Float?,
//    val feels_like: Float?,
//    val temp_min :Float?,
//    val temp_max:Float?,
//    val pressure:Int?,
//    val humidity:Int?,
//    val sea_level:Int?,
//    val grnd_level:Int?
//)