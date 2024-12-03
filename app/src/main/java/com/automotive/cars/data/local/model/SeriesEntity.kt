package com.automotive.cars.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "series",
    foreignKeys = [
        ForeignKey(
            entity = BrandEntity::class,
            parentColumns = ["id"],
            childColumns = ["brandId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["brandId"])]
)
data class SeriesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val brandId: Int,
    val seriesName: String,
    val minYear: Int,
    val maxYear: Int
)
