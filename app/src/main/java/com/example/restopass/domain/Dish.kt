package com.example.restopass.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Dish(
    val name: String,
    val description: String,
    val topMembership: MembershipType,
    val stars: Number
) : Parcelable