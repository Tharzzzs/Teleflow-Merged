package com.tele.teleflow.data

import java.util.UUID

class Script(
    var title: String,
    private val lastEdited: String,
    var content: String = "",
    var isBookmarked: Boolean = false
) {
    var id: String = UUID.randomUUID().toString()

    fun getLastEditedString(): String {
        return lastEdited
    }
}