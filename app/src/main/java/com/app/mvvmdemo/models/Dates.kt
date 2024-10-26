package com.app.mvvmdemo.models

import android.os.Parcel
import android.os.Parcelable

data class Dates(
    val maximum: String,
    val minimum: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(maximum)
        parcel.writeString(minimum)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dates> {
        override fun createFromParcel(parcel: Parcel): Dates {
            return Dates(parcel)
        }

        override fun newArray(size: Int): Array<Dates?> {
            return arrayOfNulls(size)
        }
    }
}
