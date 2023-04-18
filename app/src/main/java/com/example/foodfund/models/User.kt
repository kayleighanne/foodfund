package com.example.foodfund.models


// a data model class for a user with the required fields
data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val mobile: Long = 0,
    val profileCompleted: Int = 0)


