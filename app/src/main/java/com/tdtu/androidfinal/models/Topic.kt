package com.tdtu.androidfinal.models

data class Topic(
    var userId: String,
    var username: String,
    var title: String,
    var description: String = "",
    var cardList: ArrayList<Card> = ArrayList(),
    var isPublic: Boolean = false
)