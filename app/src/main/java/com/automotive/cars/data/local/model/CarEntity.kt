package com.automotive.cars.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class CarEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val brand: String,
    val model: String,
    val year: String,
    val fuelType: FuelType,
    val isCurrentlySelected: Boolean = false
){
    //Room forces for constructor
}