package com.dheeraj.news.data.model

import com.google.gson.annotations.SerializedName

data class LikesResponse(
    @SerializedName("likes")
    val likes: Int
)