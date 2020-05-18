package com.example.restopass.main.ui.home

import android.os.Parcel
import android.os.Parcelable

data class PlanData(val title: String?, val price: Double) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeDouble(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlanData> {
        override fun createFromParcel(parcel: Parcel): PlanData {
            return PlanData(parcel)
        }

        override fun newArray(size: Int): Array<PlanData?> {
            return arrayOfNulls(size)
        }
    }

}