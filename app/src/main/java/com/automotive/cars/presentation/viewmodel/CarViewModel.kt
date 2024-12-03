package com.automotive.cars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.cars.common.Resource
import com.automotive.cars.data.CarDataStoreManager
import com.automotive.cars.domain.model.Car
import com.automotive.cars.domain.usecase.GetAllCarsUseCase
import com.automotive.cars.domain.usecase.GetCreateCarUseCase
import com.automotive.cars.domain.usecase.GetDeleteCarUseCase
import com.automotive.cars.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val getDeleteCarUseCase: GetDeleteCarUseCase,
    private val getCreateCarUseCase: GetCreateCarUseCase,
    private val getAllCarsUseCase: GetAllCarsUseCase,
    private val carDataStoreManager: CarDataStoreManager
) : ViewModel() {

    private val _deleteCarState = MutableStateFlow<UiState<Unit>>(UiState.Idle())
    val deleteCarState: StateFlow<UiState<Unit>> = _deleteCarState

    private val _createCarState = MutableSharedFlow<UiState<Unit>>(replay = 0)
    val createCarState = _createCarState

    private val _getMyCarsState = MutableStateFlow<UiState<List<Car>>>(UiState.Idle())
    val getMyCarsState: StateFlow<UiState<List<Car>>> = _getMyCarsState

    private val _getMySelectedCarsState = MutableStateFlow<UiState<Car>>(UiState.Idle())
    val getMySelectedCarsState: StateFlow<UiState<Car>> = _getMySelectedCarsState

    private val _hasCarsState = MutableStateFlow(false)
    val hasCarsState: StateFlow<Boolean> = _hasCarsState

    init {
        hasCars()
    }

    fun updateSelectedCar(carId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            carDataStoreManager.setCurrentlySelectedCarId(carId)
        }
    }

    private fun hasCars() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getAllCarsUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            val cars = result.data.orEmpty()
                            _hasCarsState.value = cars.isNotEmpty()
                        }
                        is Resource.Error -> _hasCarsState.value = false
                        else -> Unit
                    }
                }
            } catch (e: Exception) {
                _hasCarsState.value = false
            }
        }
    }


    fun getMyCars() {
        viewModelScope.launch(Dispatchers.IO) {
            _getMyCarsState.value = UiState.Loading()
            try {
                val currentlySelectedCarId = carDataStoreManager.currentlySelectedCarId.firstOrNull()
                getAllCarsUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            val updatedCars = result.data.orEmpty().map { car ->
                                car.copy(isCurrentlySelected = car.id == currentlySelectedCarId)
                            }
                            _getMyCarsState.value = UiState.Success(updatedCars)
                        }
                        is Resource.Error -> _getMyCarsState.value = UiState.Error("Failed to fetch cars: ${result.message}")
                        is Resource.Loading -> _getMyCarsState.value = UiState.Loading()
                    }
                }
            } catch (e: Exception) {
                _getMyCarsState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getMySelectedCar() {
        viewModelScope.launch(Dispatchers.IO) {
            _getMySelectedCarsState.value = UiState.Loading()
            try {
                val currentlySelectedCarId = carDataStoreManager.currentlySelectedCarId.firstOrNull()
                getAllCarsUseCase().collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            val updatedCars = result.data.orEmpty().map { car ->
                                car.copy(isCurrentlySelected = car.id == currentlySelectedCarId)
                            }
                            val currentlySelectedCar = updatedCars.firstOrNull { car -> car.isCurrentlySelected }
                            currentlySelectedCar?.let {
                                _getMySelectedCarsState.value = UiState.Success(it)
                            }
                        }
                        is Resource.Error -> _getMySelectedCarsState.value = UiState.Error(result.message ?: "An error occurred")
                        is Resource.Loading -> _getMySelectedCarsState.value = UiState.Loading()
                    }
                }
            } catch (e: Exception) {
                _getMySelectedCarsState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch (Dispatchers.IO){
            getDeleteCarUseCase(car)
                .onStart { _deleteCarState.value = UiState.Loading() }
                .catch { e -> _deleteCarState.value = UiState.Error(e.message ?: "An error occurred") }
                .collect { result ->
                    when (result) {
                        is Resource.Success -> _deleteCarState.value = UiState.Success(Unit)
                        is Resource.Error -> _deleteCarState.value = UiState.Error(result.message ?: "An error occurred")
                        is Resource.Loading -> _deleteCarState.value = UiState.Loading()
                    }
                }
        }
    }

    fun createCar(car: Car) {
        viewModelScope.launch(Dispatchers.IO) {
            getCreateCarUseCase(car)
                .onStart { _createCarState.emit(UiState.Loading()) }
                .catch { e -> _createCarState.emit(UiState.Error(e.message ?: "An error occurred")) }
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { updateSelectedCar(it.toInt()) }
                            _createCarState.emit(UiState.Success(Unit))
                        }
                        is Resource.Error -> _createCarState.emit(UiState.Error(result.message ?: "An error occurred"))
                        is Resource.Loading -> _createCarState.emit(UiState.Loading())
                    }
                }
        }
    }
}
