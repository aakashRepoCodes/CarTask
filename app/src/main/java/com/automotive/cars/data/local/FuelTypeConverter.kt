package com.automotive.cars.data.local

import androidx.room.TypeConverter
import com.automotive.cars.data.local.model.FuelType

class FuelTypeConverter {

@TypeConverter
fun fromFuelType(fuelType: FuelType): String {
    return when (fuelType) {
        is FuelType.Electric -> "Electric"
        is FuelType.Gasoline -> "Gasoline"
        is FuelType.Hybrid -> "Hybrid"
        is FuelType.Diesel -> "Diesel"
        else -> "Others"
    }
}

    @TypeConverter
    fun toFuelType(fuelTypeString: String): FuelType {
        return when (fuelTypeString) {
            "Diesel" -> FuelType.Diesel
            "Electric" -> FuelType.Electric
            "Gasoline" -> FuelType.Gasoline
            "Hybrid" -> FuelType.Hybrid
            else -> FuelType.Others
        }
    }

}