package com.automotive.cars.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.automotive.cars.data.local.model.CarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

   @Query("SELECT * FROM vehicles")
    fun getCars(): Flow<List<CarEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCar(carEntity: CarEntity): Long

    @Delete
    fun deleteCar(carEntity: CarEntity): Int
}