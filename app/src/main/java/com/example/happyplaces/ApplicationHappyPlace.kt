package com.example.happyplaces

import android.app.Application
import com.example.happyplaces.room_database.HappyPlaceDataBase
import com.google.android.material.color.DynamicColors

class ApplicationHappyPlace: Application() {

    val dataBase by lazy { HappyPlaceDataBase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}