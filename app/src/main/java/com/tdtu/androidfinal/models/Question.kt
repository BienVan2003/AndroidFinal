package com.tdtu.androidfinal.models

data class Question (
    var number: Int,
    var content: String,
    var answers: List<Answer>
)