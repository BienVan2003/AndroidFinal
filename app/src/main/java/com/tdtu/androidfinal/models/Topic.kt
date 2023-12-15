package com.tdtu.androidfinal.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


data class Topic(
    var id: String = "",
    var userId: String,
    var username: String,
    var title: String,
    var description: String = "",
    var cardList: ArrayList<Card> = ArrayList(),
    var isPublic: Boolean = false
):Serializable