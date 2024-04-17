package com.example.happyplaces.room_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {
    @Insert
    suspend fun insertPlace(placeEntity: PlaceEntity)

    @Query("select* from PlacesDataBase")
    fun loadPlace(): Flow<PlaceEntity>
}