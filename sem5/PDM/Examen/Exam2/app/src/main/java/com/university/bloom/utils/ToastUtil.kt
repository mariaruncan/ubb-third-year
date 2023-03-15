package com.university.bloom.utils

import android.content.Context
import android.widget.Toast

fun displayToast(context: Context, message: String?) {
    if (message == null) {
        Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}