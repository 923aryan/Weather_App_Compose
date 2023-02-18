package com.example.weatherapp.screen

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.R
import com.example.weatherapp.Screens
import com.example.weatherapp.model.WeatherViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Menu(
    viewmodel: WeatherViewModel = viewModel(),
    navController : NavHostController = rememberNavController()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(12, 21, 21))
    ) {
        Text(
            text = "Enter Location:",
            fontFamily = FontFamily(Font(R.font.oxaniumextrabold)),
            fontSize = 24.sp, color = Color.White
        )
        Spacer(modifier = Modifier.size(20.dp))
        var citySearch by remember { mutableStateOf("") }
        val cityNames by viewmodel.cityNameInstance.collectAsState()
        val focusRequester = FocusRequester()
        val keyboardController = LocalSoftwareKeyboardController.current
        //val boxSize by
        Box(
            modifier = Modifier
                .clickable { focusRequester.requestFocus()
                    if (keyboardController != null) {
                        keyboardController.show()
                    }
                }
                .clip(RoundedCornerShape(12.dp))
                .width(IntrinsicSize.Min)
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(12.dp))
                .padding(all = 15.dp)

        )
        {

            BasicTextField(
                modifier = Modifier.focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(  keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {focusRequester.freeFocus();keyboardController?.hide()}),
                decorationBox = { innerTextField ->
                    Box()
                    {
                        if (citySearch.isEmpty()) {
                            Text(
                                text = "City",
                                fontFamily = FontFamily(Font(R.font.oxaniumbold)),
                                fontSize = 25.sp,
                                color = Color.LightGray

                            )
                        }
                        innerTextField()
                    }

                },
                value = citySearch.replaceFirstChar { it.uppercase() },
                onValueChange = { viewmodel.getNames(it);citySearch = it },
                textStyle = TextStyle(
                    fontFamily = FontFamily(Font(R.font.oxaniumbold)),
                    fontSize = 25.sp, color = Color.White
                ), singleLine = true, maxLines = 1,
                cursorBrush = SolidColor(Color.White)
            )
        }

//    for (i in cityNames.indices)
//    {
//        cityNames[i].name?.let { Log.d("here", it) }
//    }
        Spacer(modifier = Modifier.size(10.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            items(cityNames.size)
            {
                Column(modifier = Modifier.clickable {
//                Log.d(
//                    "clickedcity:",
//                    "${cityNames[it].name!!} ${cityNames[it].state} ${cityNames[it].country}"
//                )
                    viewmodel.update(cityNames[it].name!!, cityNames[it].state!!);
                    navController.navigate(Screens.Start.name){popUpTo(Screens.Start.name)}
                    //navController.clea
                }) {
                    cityNames[it].name?.let { it1 ->
                        Text(
                            text = it1,
                            fontFamily = FontFamily(Font(resId = R.font.oxaniumsemibold)),
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.size(2.dp))
                    Text(
                        text = "${cityNames[it].state} ${cityNames[it].country}",
                        fontFamily = FontFamily(Font(R.font.oxaniumregular)),
                        fontSize = 15.sp, color = Color.White
                    )
                }

            }
        }
    }
}