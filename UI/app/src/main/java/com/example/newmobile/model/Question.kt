package com.example.newmobile.model

data class Question(
    val id: String,
    val authorName: String,
    val authorProfession: String,
    val content: String,
    val answersCount: Int
)