package com.automotive.cars.data.repository

import com.automotive.cars.data.CarMapper
import com.automotive.cars.data.local.dao.CarDao
import com.automotive.cars.data.local.dao.MockDataDao
import com.automotive.cars.domain.model.Car
import com.automotive.cars.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CarRepositoryImpl(
    private val carDao: CarDao,
    private val mockDataDao: MockDataDao,
    private val carMapper: CarMapper
) : CarRepository {

    override suspend fun getAllCars(): Flow<List<Car>> {
        return carDao.getCars().map { carEntities ->
            carEntities.map { carEntity ->
                val features = getSupportedFeatures(carEntity.brand, carEntity.model)
                carMapper.toDomain(carEntity).copy(supportedFeatures = features)
            }
        }
    }

    override suspend fun getSupportedFeatures(brandName: String, seriesName: String): List<String> {
        val brand = mockDataDao.getBrandByName(brandName) ?: return emptyList()
        val series = mockDataDao.getSeriesByNameAndBrand(seriesName, brand.id) ?: return emptyList()
        return mockDataDao.getFeatureNamesBySeriesId(series.id)
    }

    override suspend fun insertCar(car: Car): Long {
        return carDao.insertCar(
            carMapper.toInsertionEntity(car)
        )
    }

    override suspend fun deleteCar(car: Car): Int {
        return carDao.deleteCar(
            carMapper.toEntity(car)
        )
    }

    override suspend fun getAllBrands(): List<String> {
        return mockDataDao.getAllBrands().map { brandEntity ->
            brandEntity.name
        }
    }

    override suspend fun getAllSeries(brandName: String): List<String> {
        return mockDataDao.getBrandByName(brandName)?.let { brandEntity ->
            mockDataDao.getSeriesByBrandId(brandEntity.id).map { it.seriesName }
        } ?: emptyList()
    }

    override suspend fun getAllSupportedYears(brandName: String, seriesName: String): List<String> {
        return mockDataDao.getBrandByName(brandName)?.let { brandEntity ->
            mockDataDao.getSeriesByNameAndBrand(seriesName, brandEntity.id)?.let { seriesEntity ->
                (seriesEntity.minYear..seriesEntity.maxYear).map { it.toString() }
            }
        } ?: emptyList()
    }

}