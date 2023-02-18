package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.model.WeatherViewModel
import com.example.weatherapp.screen.MainScreen
import com.example.weatherapp.screen.Menu
import com.example.weatherapp.ui.theme.WeatherappTheme

enum class Screens(title: String) {
    Start(title = "MainScreen"),
    Menu(title = "Menu"),
    Tomorrow(title = "Tomorrow")
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherappTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentScreen =
                    Screens.valueOf(backStackEntry?.destination?.route ?: Screens.Start.name)
                val viewmodel: WeatherViewModel = viewModel()
                Scaffold {
                    NavHost(
                        navController = navController,
                        startDestination = Screens.Start.name,
                        modifier = Modifier.padding(it)
                    )
                    {

                        composable(route = Screens.Start.name)
                        {
                            MainScreen(viewmodel, navController, onNextButtonClicked = {
                                Log.d("Clicked", "Cliced")
                                navController.navigate(Screens.Menu.name)
                            }
                            )
                        }
                        composable(route = Screens.Menu.name) {
                            Menu(viewmodel, navController)
                        }
                    }
//                MainScreen(viewmodel, navController)
//                { navController.navigate(Screens.Start.name) }

                }
                //Menu(viewmodel)
            }
        }
    }
}

