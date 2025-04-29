package com.tele.teleflow.utils

import android.content.Context
import android.widget.Toast

// Extension function for Context (works for both Activity and Fragment via requireContext())
fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
