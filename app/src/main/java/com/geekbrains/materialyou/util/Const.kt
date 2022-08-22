package com.geekbrains.materialyou.util

import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment

const val NAME_SHARED_PREFERENCE = "LOGIN"
const val APP_THEME = "APP_THEME"
const val PICTURE_SP_NAME = "picture"
const val PICTURE_SP_KEY = "pic"

fun Fragment.toast(string: String?) {
    Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.BOTTOM, 0, 250)
        show()
    }
}
