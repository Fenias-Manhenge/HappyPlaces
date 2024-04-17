package com.example.happyplaces.room_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlin.concurrent.Volatile

@Database(entities = [PlaceEntity::class], version = 1, exportSchema = true)
abstract class HappyPlaceDataBase: RoomDatabase() {

    abstract fun placeDao(): PlacesDao

    companion object{

        @Volatile
        private var INSTANCE: HappyPlaceDataBase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): HappyPlaceDataBase{
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HappyPlaceDataBase::class.java,
                        "PlacesDataBase"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}