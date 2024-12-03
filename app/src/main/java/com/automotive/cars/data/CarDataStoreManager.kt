package com.automotive.cars.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "car_preferences")

class CarDataStoreManager(private val context: Context) {

    private val CURRENTLY_SELECTED_CAR_ID = intPreferencesKey("currently_selected_car_id")
    private val MOCK_DATA_INJECTED = booleanPreferencesKey("mock_data_injected")


    val currentlySelectedCarId: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[CURRENTLY_SELECTED_CAR_ID]
    }

    val isMockDataInjected: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[MOCK_DATA_INJECTED] ?: false
    }

    suspend fun setCurrentlySelectedCarId(carId: Int) {
        context.dataStore.edit { preferences ->
            preferences[CURRENTLY_SELECTED_CAR_ID] = carId
        }
    }

    suspend fun clearCurrentlySelectedCarId() {
        context.dataStore.edit { preferences ->
            preferences.remove(CURRENTLY_SELECTED_CAR_ID)
        }
    }

    suspend fun setMockDataInjected(isInjected: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MOCK_DATA_INJECTED] = isInjected
        }
    }

    suspend fun clearMockDataInjected() {
        context.dataStore.edit { preferences ->
            preferences.remove(MOCK_DATA_INJECTED)
        }
    }
}
