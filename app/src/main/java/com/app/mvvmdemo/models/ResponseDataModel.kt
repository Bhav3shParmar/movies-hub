package com.app.mvvmdemo.models

import android.os.Parcel
import android.os.Parcelable


data class ResponseDataModel(
    val dates: Dates?,
    val page: Int,
    val results: List<Result>?,
    val total_pages: Int,
    val total_results: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Dates::class.java.classLoader),
        parcel.readInt(),
        parcel.createTypedArrayList(Result.CREATOR)?: emptyList(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(dates,flags)
        page.let { parcel.writeInt(it) }
        parcel.writeTypedList(results)
        total_pages.let { parcel.writeInt(it) }
        total_results.let { parcel.writeInt(it) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseDataModel> {
        override fun createFromParcel(parcel: Parcel): ResponseDataModel {
            return ResponseDataModel(parcel)
        }

        override fun newArray(size: Int): Array<ResponseDataModel?> {
            return arrayOfNulls(size)
        }
    }

}