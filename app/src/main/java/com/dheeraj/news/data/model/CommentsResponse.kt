package com.dheeraj.news.data.model

import com.google.gson.annotations.SerializedName

data class CommentsResponse(
    @SerializedName("comments")
    val comments: Int
)
