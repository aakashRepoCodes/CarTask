package com.automotive.cars.di

import android.content.Context
import androidx.room.Room
import com.automotive.cars.data.CarMapper
import com.automotive.cars.data.CarDataStoreManager
import com.automotive.cars.data.local.dao.CarDao
import com.automotive.cars.data.local.CarDatabase
import com.automotive.cars.data.local.dao.MockDataDao
import com.automotive.cars.data.repository.CarRepositoryImpl
import com.automotive.cars.domain.repository.CarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): CarDatabase =
        Room.databaseBuilder(
            context,
            CarDatabase::class.java,
            "car_database"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideUserCarDao(database: CarDatabase): CarDao {
        return database.myCarDao()
    }

    @Provides
    fun provideMockDataDao(database: CarDatabase): MockDataDao {
        return database.mockDataDao()
    }

    @Provides
    fun provideCarMapper(): CarMapper {
        return CarMapper()
    }

    @Provides
    @Singleton
    fun provideCarRepository(
        carDao: CarDao,
        mockDataDao: MockDataDao,
        carMapper: CarMapper
    ): CarRepository {
        return CarRepositoryImpl(carDao, mockDataDao, carMapper)
    }

    @Provides
    @Singleton
    fun provideCurrentlySelectedCarManager(
        @ApplicationContext context: Context
    ): CarDataStoreManager {
        return CarDataStoreManager(context)
    }

}

