package com.example.weatherapp.screen

import android.util.Log
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.model.WeatherViewModel
import com.example.weatherapp.network.NextHour
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Body(viewmodel: WeatherViewModel) {
    val mystate by viewmodel.weatherState.collectAsState()
    val enabledInstance = viewmodel.enabledInstance.collectAsState()
    val enabledButton = enabledInstance.value
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = if(enabledButton[2]) Arrangement.Center else Arrangement.SpaceBetween) {

        Column(verticalArrangement = if(!enabledButton[2])Arrangement.spacedBy(5.dp) else Arrangement.Center,
        horizontalAlignment = if(enabledButton[2]) Alignment.CenterHorizontally else Alignment.Start) {
            Text(
                //( (fahrenheit  -  32  ) *  5)/9
                text = "${(mystate.main.temp - 273.15).toInt()}\u00B0",
                fontFamily = FontFamily(Font(R.font.oxaniumextrabold)),
                color = Color.White,
                fontSize = 65.sp
            )
            Text(
                text = "${mystate.weather?.get(0)?.main}",
                fontSize = 15.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.oxaniummedium)),
                letterSpacing = 0.5.sp
            )

        }
        //for next10days weather
        if(!enabledButton[2])
        {
            Image(
                painter = painterResource(
                    id = when (mystate.weather?.get(0)?.main) {
                        "Clouds" -> R.drawable.cloudy
                        "Thunderstorm" -> R.drawable.thunderstorm
                        "Rain" -> R.drawable.rainy
                        "Clear" -> R.drawable.sunny
                        "Snow" -> R.drawable.snow
                        else -> R.drawable.whatever
                    }
                ), contentDescription = "Weather State", modifier = Modifier.size(100.dp)
            )
        }

    }
}

@Composable
fun Description(viewmodel: WeatherViewModel) {
    Card(
        modifier = Modifier
            //.padding(start = 10.dp, top = 10.dp, bottom = 10.dp, end = 10.dp)
            .clip(shape = RoundedCornerShape(25.dp))

            .fillMaxHeight(0.2f)

            .fillMaxWidth(),
        elevation = 5.dp,
        backgroundColor = Color(31, 35, 41)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp, end = 10.dp)
        ) {
            val mystate by viewmodel.weatherState.collectAsState()
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.aspectRatio(1f)
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.FillBounds,
                    painter = painterResource(id = R.drawable.wind),
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = "wind"
                )
                Text(
                    text = "${mystate.wind.speed}m/s",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.oxaniumregular))
                )
                Text(
                    text = "Wind", fontFamily = FontFamily(Font(R.font.oxaniummedium)),
                    fontSize = 17.sp, color = Color(98, 98, 100)
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.aspectRatio(1f)
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.FillBounds,
                    painter = painterResource(id = R.drawable.humidity),
                    contentDescription = "humidity"
                )
                Text(
                    text = "${mystate.main.humidity}%",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.oxaniumregular))
                )
                Text(
                    text = "Humidity", fontFamily = FontFamily(Font(R.font.oxaniummedium)),
                    fontSize = 17.sp, color = Color(98, 98, 100)
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.aspectRatio(1f)
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.FillBounds,
                    painter = painterResource(id = R.drawable.rain),
                    contentDescription = "Rain"
                )
                Text(
                    text = "${mystate.weather?.get(0)?.description}",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.oxaniumregular)),
                    overflow = TextOverflow.Clip,
                    maxLines = 1
                )
                Text(
                    text = "Rain", fontFamily = FontFamily(Font(R.font.oxaniummedium)),
                    fontSize = 17.sp, color = Color(98, 98, 100)
                )
            }
        }
    }
}

@Composable
fun Threeday(viewmodel: WeatherViewModel) {
    val NextHourCollector = viewmodel.NextHourInstance.collectAsState()
    val enabledInstance = viewmodel.enabledInstance.collectAsState()
    val enabledButton = enabledInstance.value

    if (NextHourCollector.value.list.size > 0)
        Log.d("Size is", NextHourCollector.value.list.size.toString())
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        flingBehavior = ScrollableDefaults.flingBehavior()
    ) {
        items(NextHourCollector.value.list.size)
        {
            val myTime = NextHourCollector.value.list[it].dt_txt.toString().split(" ", ":", "-")
            val today = NextHourCollector.value.list[0].dt_txt.toString().split(" ", ":", "-")
            Log.d("here", "${myTime[2]}, ${myTime}, ${ NextHourCollector.value.list[it].dt_txt.toString()}")

            if(enabledButton[0] && today[2] == myTime[2])
            {
                CardForNextDay(NextHourCollector, it, Modifier
                    //.graphicsLayer { alpha = 0.99f }
                    .clip(RoundedCornerShape(25.dp))
                    //.size(80.dp)
                    .width(100.dp)
                    .height(140.dp),myTime)
            }
            else if(enabledButton[1] && ((today[2].toInt() + 1 == myTime[2].toInt())) || (today[2] == "31" && myTime[2] == "01"))
            {
                CardForNextDay(NextHourCollector, it, Modifier
                    // .graphicsLayer { alpha = 0.55F }
                    .clip(RoundedCornerShape(25.dp))
                    //.size(80.dp)
                    .width(100.dp)
                    .height(140.dp),myTime)
            }

        }

    }
}

@Composable
fun CardForNextDay(
    NextHourCollector: State<NextHour>,
    i: Int,
    modifier: Modifier,
    myTime: List<String>
) {
    Surface(
        modifier = modifier
            .background(Color(31, 35, 41)),
        elevation = 5.dp
    ) {
        Column(modifier = Modifier.aspectRatio(1f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Log.d("dt_text", NextHourCollector.value.list[3].dt_txt.toString())
//            Log.d(
//                "see",
//                "${myTime.toString()}"
//            )
            val _24HourTime = "${myTime[3]}:${myTime[4]}"
            val _24HourSDF = SimpleDateFormat("HH:mm")
            val _12HourSDF = SimpleDateFormat("hh a")
            val _24HourDt: Date = _24HourSDF.parse(_24HourTime) as Date
            val _12HourOnly = _12HourSDF.format(_24HourDt)
            // Log.d("My time", _12HourOnly+"yay")
            Text(
                text = _12HourOnly,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
          //  Spacer(modifier = Modifier.size(5.dp))
                Image( modifier = Modifier
                    .width(40.dp)
                    .height(40.dp),
                    painter = painterResource(
                        id = when (NextHourCollector.value.list[i].weather?.get(0)?.main) {
                            "Clouds" -> R.drawable.cloudy
                            "Thunderstorm" -> R.drawable.thunderstorm
                            "Rain" -> R.drawable.rainy
                            "Clear" -> R.drawable.sunny
                            "Snow" -> R.drawable.snow
                            else -> R.drawable.whatever
                        }
                    ), contentDescription = "Weather State",
                    contentScale = ContentScale.Fit
                )
            //Spacer(modifier = Modifier.size(5.dp))

            Text(
                //${(mystate.main.temp - 273.15).toInt()}°
                text = "${(NextHourCollector.value.list[i].main.temp - 273.15).toInt()}°",
                color = Color.White
            )
        }

    }
}