package com.android.domain.model
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MovieItem(
    @field:SerializedName("overview")
    val overview: String,
    @field:SerializedName("id")
    val movieId: String,
    @field:SerializedName("original_language")
    val originalLanguage: String,
    @field:SerializedName("original_title")
    val originalTitle: String,
    @field:SerializedName("video")
    val video: Boolean,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("poster_path")
    val posterPath: String,
    @field:SerializedName("backdrop_path")
    val backdropPath: String,
    @field:SerializedName("release_date")
    val releaseDate: String,
    @field:SerializedName("popularity")
    val popularity: Double,
    @field:SerializedName("vote_average")
    val voteAverage: Double,
    @field:SerializedName("adult")
    val adult: Boolean,
    @field:SerializedName("vote_count")
    val voteCount: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean == true,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readValue(Double::class.java.classLoader) as? Double ?: 0.0,
        parcel.readValue(Double::class.java.classLoader) as? Double ?: 0.0,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean == true,
        parcel.readValue(Int::class.java.classLoader) as? Int ?: 0,
    ) {
    }

    override fun writeToParcel(
        parcel: Parcel,
        flags: Int,
    ) {
        parcel.writeString(overview)
        parcel.writeString(movieId)
        parcel.writeString(originalLanguage)
        parcel.writeString(originalTitle)
        parcel.writeValue(video)
        parcel.writeString(title)
        parcel.writeString(posterPath)
        parcel.writeString(backdropPath)
        parcel.writeString(releaseDate)
        parcel.writeValue(popularity)
        parcel.writeValue(voteAverage)
        parcel.writeValue(adult)
        parcel.writeValue(voteCount)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<MovieItem> {
        override fun createFromParcel(parcel: Parcel): MovieItem = MovieItem(parcel)

        override fun newArray(size: Int): Array<MovieItem?> = arrayOfNulls(size)
    }
}
