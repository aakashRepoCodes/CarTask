package com.automotive.cars.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "features",
    foreignKeys = [ForeignKey(
        entity = SeriesEntity::class,
        parentColumns = ["id"],
        childColumns = ["seriesId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SupportedFeaturesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val seriesId: Int,
    val featureName: String
)
