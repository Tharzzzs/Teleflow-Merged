package com.tele.teleflow.data

import com.google.firebase.Timestamp

data class Bookmark(
    val id: String = "",
    val userId: String = "",
    val scriptId: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
