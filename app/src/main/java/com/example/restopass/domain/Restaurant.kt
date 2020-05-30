package com.example.restopass.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Restaurant(
    val name: String,
    val img: String,
    val address: String,
    val tags: List<String>,
    val dishes: List<Dish>,
    val stars: Int
) : Parcelable