package com.tele.teleflow.utils

import android.app.Activity
import android.widget.EditText
import android.widget.Toast

fun Activity.toast(msg:String) {
    Toast.makeText(this, "Username and password is empty", Toast.LENGTH_LONG).show()
}