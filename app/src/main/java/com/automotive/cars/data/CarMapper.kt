package com.automotive.cars.data

import com.automotive.cars.data.local.model.CarEntity
import com.automotive.cars.domain.model.Car

class CarMapper {

    fun toDomain(carEntity: CarEntity): Car {
        return Car(
            id = carEntity.id,
            brand = carEntity.brand,
            model = carEntity.model,
            year = carEntity.year,
            fuelType = carEntity.fuelType,
            isCurrentlySelected = carEntity.isCurrentlySelected
        )
    }

    fun toEntity(car: Car): CarEntity {
        return CarEntity(
            id = car.id!!, //fail the deletion operation if id does not exists
            brand = car.brand,
            model = car.model,
            year = car.year,
            fuelType = car.fuelType,
            isCurrentlySelected = car.isCurrentlySelected
        )
    }

    fun toInsertionEntity(car: Car): CarEntity {
        return CarEntity(
            brand = car.brand,
            model = car.model,
            year = car.year,
            fuelType = car.fuelType,
            isCurrentlySelected = car.isCurrentlySelected
        )
    }
}