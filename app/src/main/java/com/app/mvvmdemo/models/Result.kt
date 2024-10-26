package com.app.mvvmdemo.models

import android.os.Parcel
import android.os.Parcelable

data class Result(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>?,
    val id: Int,
    val name: String?,
    val original_language: String?,
    val profile_path: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val known_for: ArrayList<KnownFor>?,
    val media_type :String?,
    val original_name :String?


) : Parcelable {
    constructor(parcel: Parcel) : this(
        adult = parcel.readByte() != 0.toByte(),
        backdrop_path = parcel.readString(),
        genre_ids = parcel.createIntArray()?.toList() ?: emptyList(),
        id = parcel.readInt(),
        name = parcel.readString(),
        original_language = parcel.readString(),
        profile_path = parcel.readString(),
        original_title = parcel.readString(),
        overview = parcel.readString(),
        popularity = parcel.readDouble(),
        poster_path = parcel.readString(),
        release_date = parcel.readString(),
        title = parcel.readString(),
        video = parcel.readByte() != 0.toByte(),
        vote_average = parcel.readDouble(),
        vote_count = parcel.readInt(),
        known_for = parcel.createTypedArrayList(KnownFor.CREATOR),
        media_type = parcel.readString(),
        original_name = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeString(backdrop_path)
        parcel.writeIntArray(genre_ids?.toIntArray())
        id.let { parcel.writeInt(it) }
        parcel.writeString(name)
        parcel.writeString(original_language)
        parcel.writeString(profile_path)
        parcel.writeString(original_title)
        parcel.writeString(overview)
        popularity.let { parcel.writeDouble(it) }
        parcel.writeString(poster_path)
        parcel.writeString(release_date)
        parcel.writeString(title)
        parcel.writeByte(if (video) 1 else 0)
        vote_average.let { parcel.writeDouble(it) }
        vote_count.let { parcel.writeInt(it) }
        parcel.writeTypedList(known_for)
        parcel.writeString(media_type)
        parcel.writeString(original_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Result> {
        override fun createFromParcel(parcel: Parcel): Result {
            return Result(parcel)
        }

        override fun newArray(size: Int): Array<Result?> {
            return arrayOfNulls(size)
        }
    }
}

