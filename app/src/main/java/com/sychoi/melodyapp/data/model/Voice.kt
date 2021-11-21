package com.sychoi.melodyapp.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Voice(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "img")
    val img: Int = 0,
    @Json(name = "title")
    val title: String = "",
    @Json(name = "desc")
    val desc: String = "",
    @Json(name = "playtime")
    val playtime: String = "",
    @Json(name = "avatar")
    val avatar: String = ""
) : Parcelable