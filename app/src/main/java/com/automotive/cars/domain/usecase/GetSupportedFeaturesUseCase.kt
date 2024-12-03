package com.automotive.cars.domain.usecase

import com.automotive.cars.domain.repository.CarRepository
import javax.inject.Inject

class GetSupportedFeaturesUseCase @Inject constructor(
    private val repository: CarRepository
) {
    suspend operator fun invoke(brandName: String, seriesName: String): List<String> {
        return repository.getSupportedFeatures(brandName, seriesName)
    }
}