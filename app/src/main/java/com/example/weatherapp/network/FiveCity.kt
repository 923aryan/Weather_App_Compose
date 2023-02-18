package com.example.weatherapp.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class FiveName(
    val name : String?  = "",
    val country : String? = "",
    val state : String? = ""
)