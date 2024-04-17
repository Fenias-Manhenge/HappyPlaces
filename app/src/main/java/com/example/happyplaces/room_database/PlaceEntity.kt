package com.example.happyplaces.room_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("PlacesDataBase")
data class PlaceEntity(
    @PrimaryKey
    @ColumnInfo("Title")
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val location: String = "",
    val latitude: String = "",
    val longitude: String = ""
)
