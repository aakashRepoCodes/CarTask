package com.automotive.cars.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.automotive.cars.data.local.dao.CarDao
import com.automotive.cars.data.local.dao.MockDataDao
import com.automotive.cars.data.local.model.BrandEntity
import com.automotive.cars.data.local.model.CarEntity
import com.automotive.cars.data.local.model.SeriesEntity
import com.automotive.cars.data.local.model.SupportedFeaturesEntity

@TypeConverters(FuelTypeConverter::class)
@Database(
    entities = [BrandEntity::class, SeriesEntity::class, SupportedFeaturesEntity::class, CarEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CarDatabase : RoomDatabase() {
    abstract fun myCarDao(): CarDao
    abstract fun mockDataDao(): MockDataDao
}