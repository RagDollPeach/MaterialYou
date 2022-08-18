package com.geekbrains.materialyou

import android.app.Application

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        myApplication = this
    }

    companion object {
        private var myApplication: MyApplication? = null
        fun getMyApp() = myApplication!!
    }
}