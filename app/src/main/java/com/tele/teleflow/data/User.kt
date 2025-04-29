package com.tele.teleflow.data

data class User(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val pushNotifications: Boolean = true,
    val emailNotifications: Boolean = true,
    val profileVisibility: Boolean = true
)
