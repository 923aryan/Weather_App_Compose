package com.example.weatherapp.screen

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.R
import com.example.weatherapp.model.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun MainScreen(viewmodel: WeatherViewModel = viewModel(),
               navController: NavHostController = rememberNavController(),  onNextButtonClicked: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(12, 21, 21))
            .padding(top = 10.dp, start = 15.dp, end = 15.dp, bottom = 10.dp)
    ) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp
        val mystate by viewmodel.weatherState.collectAsState()

        val seechanges by remember {
            mutableStateOf(mystate.main.temp)
        }
        //Log.d("My json: ", "${mystate.weather?.get(0)}")

        //Text(text = "${mystate.main.humidity}")
        Header(modifier = Modifier.height(50.dp), viewmodel, navController){Log.d("hell", "hee"); onNextButtonClicked(); Log.d("after called", "after called")}
        //{onNextButtonClicked}
        Spacer(modifier = Modifier.size(35.dp))
        Body(viewmodel)
        Spacer(modifier = Modifier.size(34.dp))
        Description(viewmodel)
        // after description card
        Spacer(modifier = Modifier.size(35.dp))
        ThreeDaySelector(viewmodel)
        Spacer(modifier = Modifier.size(35.dp))
        Threeday(viewmodel)
        TenDay(viewmodel)

        //baad me implement
        //Menu()
    }
}

@Composable
fun Header(
    modifier: Modifier,
    viewmodel: WeatherViewModel = viewModel(),
    navController: NavHostController,
    onNextButtonClicked: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        val cityname by viewmodel.cityinstance.collectAsState()
        val enabledInstance = viewmodel.enabledInstance.collectAsState()
        val enabledButton = enabledInstance.value
        val calendar: Calendar = Calendar.getInstance()
        val date: Date = calendar.time
        val day = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
        val month = SimpleDateFormat("MMMM", Locale.ENGLISH).format(date.time)
        val dateint = SimpleDateFormat("DD", Locale.ENGLISH).format(date.time)
        //this if for tomorrow section
        if(enabledButton[2])
        {
            Box(modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(color = Color(31, 35, 49))
                .clickable(onClick = { onNextButtonClicked() }))
            {
                Image(
                    modifier = Modifier
                        .size(25.dp)
                        .align(Alignment.Center),
                    painter = painterResource(id = R.drawable.squares_four_fill_svgrepo_com),
                    contentDescription = "icon"
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = cityname.city.replaceFirstChar { it.uppercase() },
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.oxaniumbold)),
                fontSize = 24.sp
            )
            // hatana hai
            //TextField(value = updatedname, onValueChange = {viewmodel.update(it); updatedname = it; Log.d("new is", it)})

            if(!enabledButton[2]) {
                Text(
                    text = "$dateint $month, $day", color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.oxaniummedium))
                )
            }
        }
        Box(modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color(31, 35, 49))
            .clickable(onClick = { onNextButtonClicked() }))
        {
            Image(
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.squares_four_fill_svgrepo_com),
                contentDescription = "icon"
            )
        }

    }
}

@Composable
fun ThreeDaySelector(viewmodel: WeatherViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        val enabled = viewmodel.enabledInstance.collectAsState()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ClickableText(
                text = AnnotatedString("Today"), style = TextStyle(
                    color = if (enabled.value[0]) {
                        Color.White
                    } else {
                        Color.Gray
                    },
                    fontFamily = FontFamily(Font(R.font.oxaniummedium))
                ),
                onClick = {
                    enabled.value.replaceAll { false };
                    if (!enabled.value[0]) {
                        enabled.value[0] = true
                    }
                }

            )
            if (enabled.value[0]) Dot()
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClickableText(
                text = AnnotatedString("Tomorrow"),
                style = TextStyle(
                    color = if (enabled.value[1]) Color.White else Color.Gray,
                    fontFamily = FontFamily(Font(R.font.oxaniummedium))
                ),
                onClick = {
                    enabled.value.replaceAll { false }; if(!enabled.value[1]) {
                    enabled.value[1] = true
                };

                }
            )
            if (enabled.value[1])
                Dot()
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClickableText(
                text = AnnotatedString("Next 10 days"),
                style = TextStyle(
                    color = if (enabled.value[2]) Color.White else Color.Gray,
                    fontFamily = FontFamily(Font(R.font.oxaniummedium))
                ),
                onClick = {
                    enabled.value.replaceAll { false };if (!enabled.value[2]) {
                    enabled.value[2] = true
                }
                }
            )
            if (enabled.value[2]) Dot()
        }

    }
}

@Composable
fun Dot() {

    Spacer(modifier = Modifier.size(8.dp))
    Canvas(modifier = Modifier) {
        drawCircle(
            Color.White,
            radius = 3.dp.toPx()
        )
    }
}
//
//@Composable
//fun WeatherApp(modifier: Modifier = Modifier,
//               viewmodel: WeatherViewModel = viewModel()) {
//    val navController = rememberNavController()
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen = Screens.valueOf(backStackEntry?.destination?.route ?: Screens.Start.name)
//    Scaffold { innerPadding ->
//        NavHost(
//            navController = navController,
//            startDestination = Screens.Start.name,
//            modifier = Modifier.padding(innerPadding)
//        )
//        {
//
//            composable(route = Screens.Start.name)
//            {
//                Menu(viewmodel,
//                onNextButtonClicked = {navController.navigate(Screens.Menu.name)})
//            }
//        }
//
//    }
//}