package com.app.mvvmdemo.models

import android.os.Parcel
import android.os.Parcelable


data class KnownFor(
    val backdrop_path: String?,
    val id: Int = 0,
    val title: String?,
    val original_title: String?,
    val overview: String?,
    val poster_path: String?,
    val media_type: String?,
    val adult: Boolean = false,
    val original_language: String?,
    val genre_ids: List<Int>?,
    val popularity: Double = 0.0,
    val release_date: String?,
    val video: Boolean = false,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0,
    val name: String?,
    val original_name: String?,
    val first_air_date: String?,
    val origin_country: List<String>?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        backdrop_path = parcel.readString() ,
        id = parcel.readInt(),
        title = parcel.readString() ,
        original_title = parcel.readString() ,
        overview = parcel.readString() ,
        poster_path = parcel.readString(),
        media_type = parcel.readString() ,
        adult = parcel.readByte() != 0.toByte(),
        original_language = parcel.readString() ,
        genre_ids = parcel.createIntArray()?.toList() ?: emptyList(),
        popularity = parcel.readDouble(),
        release_date = parcel.readString() ,
        video = parcel.readByte() != 0.toByte(),
        vote_average = parcel.readDouble(),
        vote_count = parcel.readInt(),
        name = parcel.readString() ,
        original_name = parcel.readString() ,
        first_air_date = parcel.readString() ,
        origin_country = parcel.createStringArrayList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(backdrop_path)
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(original_title)
        parcel.writeString(overview)
        parcel.writeString(poster_path)
        parcel.writeString(media_type)
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeString(original_language)
        parcel.writeIntArray(genre_ids?.toIntArray())
        parcel.writeDouble(popularity)
        parcel.writeString(release_date)
        parcel.writeByte(if (video) 1 else 0)
        parcel.writeDouble(vote_average)
        parcel.writeInt(vote_count)
        parcel.writeString(name)
        parcel.writeString(original_name)
        parcel.writeString(first_air_date)
        parcel.writeStringList(origin_country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KnownFor> {
        override fun createFromParcel(parcel: Parcel): KnownFor {
            return KnownFor(parcel)
        }

        override fun newArray(size: Int): Array<KnownFor?> {
            return arrayOfNulls(size)
        }
    }
}