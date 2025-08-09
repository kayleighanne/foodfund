package com.example.foodfund.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val user_id: String = "",
    val user_name: String = "",
    val title: String = "",
    val pickup_point: String = "",
    val description: String = "",
    val quantity: String = "",
    var product_id: String = "",
) : Parcelable