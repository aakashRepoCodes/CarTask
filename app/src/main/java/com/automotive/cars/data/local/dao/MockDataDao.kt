package com.automotive.cars.data.local.dao

import androidx.room.*
import com.automotive.cars.data.local.model.BrandEntity
import com.automotive.cars.data.local.model.SeriesEntity
import com.automotive.cars.data.local.model.SupportedFeaturesEntity

@Dao
interface MockDataDao {

    // --------- Brand Queries -------- //
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrand(brand: BrandEntity): Long

    @Query("SELECT * FROM brands WHERE name = :brandName")
    suspend fun getBrandByName(brandName: String): BrandEntity?

    @Query("SELECT * FROM brands")
    suspend fun getAllBrands(): List<BrandEntity>

    // --------- Series Queries -------- //
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: SeriesEntity): Long

    @Query("SELECT * FROM series WHERE brandId = :brandId")
    suspend fun getSeriesByBrandId(brandId: Int): List<SeriesEntity>

    @Query("SELECT * FROM series WHERE seriesName = :seriesName AND brandId = :brandId")
    suspend fun getSeriesByNameAndBrand(seriesName: String, brandId: Int): SeriesEntity?

    //--------- Supported Features Queries -------- //
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeature(feature: SupportedFeaturesEntity): Long

    @Query("SELECT featureName FROM features WHERE seriesId = :seriesId")
    suspend fun getFeatureNamesBySeriesId(seriesId: Int): List<String>
}