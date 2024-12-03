package com.automotive.cars.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotive.cars.data.local.MockDataForRoom
import com.automotive.cars.common.Resource
import com.automotive.cars.data.CarDataStoreManager
import com.automotive.cars.data.local.dao.MockDataDao
import com.automotive.cars.domain.usecase.GetAllBrandsUseCase
import com.automotive.cars.domain.usecase.GetAllSeriesUseCase
import com.automotive.cars.domain.usecase.GetSupportedYearsByBrandUseCase
import com.automotive.cars.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataProviderViewModel @Inject constructor(
    private val mockDataDao: MockDataDao,
    private val carDataStoreManager: CarDataStoreManager,
    private val getAllBrandsUseCase: GetAllBrandsUseCase,
    private val getAllSeriesUseCase: GetAllSeriesUseCase,
    private val getSupportedYearsByBrandUseCase: GetSupportedYearsByBrandUseCase
): ViewModel() {

    private val _brandsState = MutableStateFlow<UiState<List<String>>>(UiState.Idle())
    val brandsState: StateFlow<UiState<List<String>>> = _brandsState

    private val _seriesState = MutableStateFlow<UiState<List<String>>>(UiState.Idle())
    val seriesState: StateFlow<UiState<List<String>>> = _seriesState

    private val _yearsState = MutableStateFlow<UiState<List<String>>>(UiState.Idle())
    val yearsState: StateFlow<UiState<List<String>>> = _yearsState

    private val searchQuery = MutableStateFlow("")
    private var brandList: List<String> = emptyList()
    private var seriesList: List<String> = emptyList()
    private var yearList: List<String> = emptyList()

    init {
        initDB()
    }

    private fun initDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isDataInjected = carDataStoreManager.isMockDataInjected.firstOrNull()
                if (isDataInjected == false) {
                    val mockDataForRoom = MockDataForRoom()
                    mockDataForRoom.insertMockData(mockDataDao)
                    carDataStoreManager.setMockDataInjected(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery(
        originalList: List<String>,
        stateFlow: MutableStateFlow<UiState<List<String>>>,
        filterCondition: (String, String) -> Boolean
    ) {
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    performSearch(query, originalList, stateFlow, filterCondition)
                }
        }
    }

    fun getAllBrands() {
        viewModelScope.launch {
            getAllBrandsUseCase()
                .onStart { _brandsState.value = UiState.Loading() }
                .catch { e ->
                    _brandsState.value = UiState.Error(e.message ?: "An unexpected error occurred")
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _brandsState.value = UiState.Loading()
                        is Resource.Success ->{
                            _brandsState.value = UiState.Success(resource.data.orEmpty())
                            brandList = resource.data.orEmpty()
                            observeBrandSearch(brandList)
                        }
                        is Resource.Error -> _brandsState.value = UiState.Error(resource.message.orEmpty())
                    }
                }
        }
    }
    fun getAllSeries(brandName: String) {
        viewModelScope.launch {
            getAllSeriesUseCase(brandName)
                .onStart { _seriesState.value = UiState.Loading() }
                .catch { e ->
                    _seriesState.value = UiState.Error(e.message ?: "An unexpected error occurred")
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _seriesState.value = UiState.Loading()
                        is Resource.Success -> {
                            _seriesState.value = UiState.Success(resource.data.orEmpty())
                             seriesList = resource.data.orEmpty()
                            observeSeriesSearch(seriesList)
                        }
                        is Resource.Error -> _seriesState.value = UiState.Error(resource.message.orEmpty())
                    }
                }
        }
    }

    fun fetchSupportedYears(brandName: String, seriesName: String) {
        viewModelScope.launch {
            getSupportedYearsByBrandUseCase(brandName, seriesName)
                .onStart { _yearsState.value = UiState.Loading() }
                .catch { e ->
                    _yearsState.value = UiState.Error(e.message ?: "Failed to load years")
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _yearsState.value = UiState.Loading()
                        is Resource.Success ->{
                            _yearsState.value = UiState.Success(resource.data.orEmpty())
                            yearList = resource.data.orEmpty()
                            observeYearSearch(yearList)
                        }
                        is Resource.Error -> _yearsState.value = UiState.Error(resource.message.orEmpty())
                    }
                }
        }
    }

    private fun <T> performSearch(
        query: String,
        originalList: List<T>,
        currentState: MutableStateFlow<UiState<List<T>>>,
        filterCondition: (T, String) -> Boolean
    ) {
        if (query.isBlank()) {
            currentState.value = UiState.Success(originalList)
        } else {
            val filteredList = originalList.filter { item -> filterCondition(item, query) }
            currentState.value = UiState.Success(filteredList)
        }
    }

    private fun observeSeriesSearch(seriesList: List<String>) {
        observeSearchQuery(
            originalList = seriesList,
            stateFlow = _seriesState,
            filterCondition = { series, query -> series.contains(query, ignoreCase = true) }
        )
    }

    private fun observeBrandSearch(brandList: List<String>) {
        observeSearchQuery(
            originalList = brandList,
            stateFlow = _brandsState,
            filterCondition = { brand, query -> brand.contains(query, ignoreCase = true) }
        )
    }

    private fun observeYearSearch(yearList: List<String>) {
        observeSearchQuery(
            originalList = yearList,
            stateFlow = _yearsState,
            filterCondition = { year, query -> year.contains(query) }
        )
    }

}