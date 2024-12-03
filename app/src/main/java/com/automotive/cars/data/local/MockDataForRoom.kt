package com.automotive.cars.data.local

import com.automotive.cars.data.local.dao.MockDataDao
import com.automotive.cars.data.local.model.BrandEntity
import com.automotive.cars.data.local.model.SeriesEntity
import com.automotive.cars.data.local.model.SupportedFeaturesEntity

class MockDataForRoom {

    suspend fun insertMockData(mockDataDao: MockDataDao) {
        val audiFeature = listOf("Diagnostics", "Live Data", "Battery Check")
        val BMWFeatures = listOf("Diagnostics", "Live Data", "Battery Check", "Car Check")
        val MercedesFeatures = listOf("Diagnostics", "Battery Check", "Car Check")
        val VolksWageFeatures  =listOf("Diagnostics", "Battery Check")
        val carData = listOf(
            "Audi" to listOf(
                Triple("A1", 2010 to 2023, audiFeature),
                Triple("A3", 1996 to 2023, audiFeature),
                Triple("A4", 1994 to 2023, audiFeature),
                Triple("A5", 2007 to 2023, audiFeature),
                Triple("A6", 1994 to 2023, audiFeature),
                Triple("A7", 2010 to 2023, audiFeature),
                Triple("A8", 1994 to 2023, audiFeature),
                Triple("Q2", 2016 to 2023, audiFeature),
                Triple("Q3", 2011 to 2023, audiFeature),
                Triple("Q4", 2019 to 2023, audiFeature),
                Triple("Q5", 2008 to 2023, audiFeature),
                Triple("Q7", 2005 to 2023, audiFeature),
                Triple("Q8", 2018 to 2023, audiFeature),
                Triple("TT", 1998 to 2023, audiFeature),
                Triple("R8", 2006 to 2023, audiFeature),
                Triple("e-Tron", 2019 to 2023, audiFeature)
            ),
            "BMW" to listOf(
                Triple("1 Series", 2010 to 2023, BMWFeatures),
                Triple("2 Series", 2004 to 2023, BMWFeatures),
                Triple("3 Series", 2005 to 2023, BMWFeatures),
                Triple("4 Series", 2014 to 2023, BMWFeatures),
                Triple("5 Series", 2010 to 2023, BMWFeatures),
                Triple("6 Series", 2011 to 2023, BMWFeatures),
                Triple("7 Series", 2009 to 2023, BMWFeatures),
                Triple("8 Series", 2019 to 2023, BMWFeatures),
                Triple("X1 Series", 2010 to 2023, BMWFeatures),
                Triple("X2 Series", 2018 to 2023, BMWFeatures),
                Triple("X3 Series", 2004 to 2023, BMWFeatures),
                Triple("X4 Series", 2014 to 2023, BMWFeatures),
                Triple("X5 Series", 2000 to 2023, BMWFeatures),
                Triple("X6 Series", 2008 to 2023, BMWFeatures),
                Triple("X7 Series", 2019 to 2023, BMWFeatures),
                Triple("Z4", 2002 to 2023, BMWFeatures)
            ),
            "Mercedes" to listOf(
                Triple("A Class", 1997 to 2023, MercedesFeatures),
                Triple("B Class", 2005 to 2023, MercedesFeatures),
                Triple("C Class", 1993 to 2023, MercedesFeatures),
                Triple("CLA Class", 2013 to 2023, MercedesFeatures),
                Triple("CLS Class", 2004 to 2023, MercedesFeatures),
                Triple("E Class", 1993 to 2023, MercedesFeatures),
                Triple("G-Class", 1979 to 2023, MercedesFeatures),
                Triple("GLA Class", 2014 to 2023, MercedesFeatures),
                Triple("GLB Class", 2019 to 2023, MercedesFeatures),
                Triple("GLC Class", 2016 to 2023, MercedesFeatures),
                Triple("GLE Class", 1997 to 2023, MercedesFeatures),
                Triple("GLS Class", 2006 to 2023, MercedesFeatures),
                Triple("S Class", 1972 to 2023, MercedesFeatures),
                Triple("SL Class", 1954 to 2023, MercedesFeatures),
                Triple("SLC Class", 1996 to 2023, MercedesFeatures),
                Triple("GLE Coupe", 2015 to 2023, MercedesFeatures),
                Triple("GLC Coupe", 2016 to 2023, MercedesFeatures),
                Triple("GLS Coupe", 2015 to 2023, MercedesFeatures),
                Triple("AMG GT", 2015 to 2023, MercedesFeatures),
                Triple("EQC", 2019 to 2023, MercedesFeatures)
            ),
            "Volkswagen" to listOf(
                Triple("Polo", 1975 to 2023, VolksWageFeatures),
                Triple("Golf", 1974 to 2023, VolksWageFeatures),
                Triple("Passat", 1973 to 2023, VolksWageFeatures),
                Triple("Arteon", 2017 to 2023, VolksWageFeatures),
                Triple("Tiguan", 2007 to 2023, VolksWageFeatures),
                Triple("T-Roc", 2017 to 2023, VolksWageFeatures),
                Triple("Touareg", 2002 to 2023, VolksWageFeatures),
                Triple("ID 3", 2020 to 2023, VolksWageFeatures),
                Triple("ID 4", 2020 to 2023, VolksWageFeatures),
                Triple("ID Buzz", 2022 to 2023, VolksWageFeatures),
                Triple("Amarok", 2010 to 2023, VolksWageFeatures),
                Triple("Caddy", 1980 to 2023, VolksWageFeatures),
                Triple("Transporter", 1950 to 2023, VolksWageFeatures),
                Triple("California", 1988 to 2023, VolksWageFeatures),
                Triple("Caravelle", 1990 to 2023, VolksWageFeatures)
            )
        )

        carData.forEach { (brandName, seriesList) ->
            val brandId = mockDataDao.insertBrand(BrandEntity(name = brandName)).toInt()

            seriesList.forEach { (seriesName, yearRange, features) ->
                val seriesId = mockDataDao.insertSeries(
                    SeriesEntity(
                        seriesName = seriesName,
                        brandId = brandId,
                        minYear = yearRange.first,
                        maxYear = yearRange.second
                    )
                ).toInt()

                features.forEach { feature ->
                    mockDataDao.insertFeature(SupportedFeaturesEntity(seriesId = seriesId, featureName = feature))
                }
            }
        }
    }
}