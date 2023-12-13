package com.tdtu.androidfinal.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

//@Parcelize
data class Card(var term: String, var define: String, var image: String): Serializable
