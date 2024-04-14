package com.example.happyplaces

import android.app.Application
import com.google.android.material.color.DynamicColors

class ApplicationHappyPlace: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}