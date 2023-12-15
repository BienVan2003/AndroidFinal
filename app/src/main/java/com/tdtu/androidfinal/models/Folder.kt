package com.tdtu.androidfinal.models

import java.io.Serializable

data class Folder(
    var id: String = "",
    var userId: String,
    var username: String,
    var title: String,
    var description: String = "",
    var topics: ArrayList<Topic> = ArrayList()
): Serializable
