package com.dheeraj.news.data.entity

import com.google.gson.annotations.SerializedName

data class CommentsResponse(
    @SerializedName("comments")
    val comments: Int
)
