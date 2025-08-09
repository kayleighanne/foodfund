package com.example.foodfund.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val title: String = "",
    var id: String = ""
) : Parcelable