package com.tele.teleflow.data

data class Script(
    val title: String,
    val lastEdited: String,
    var isBookmarked: Boolean = false
) 