package com.tele.teleflow.utils

import android.app.Activity
import android.widget.EditText
import android.widget.Toast

fun Activity.toast(msg:String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}