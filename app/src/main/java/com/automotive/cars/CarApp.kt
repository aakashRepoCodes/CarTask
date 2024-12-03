package com.automotive.cars

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@HiltAndroidApp
class CarApp: Application() {
    companion object {
        private val debounceState = MutableStateFlow { }

        init {
            GlobalScope.launch(Dispatchers.Main) {
                debounceState
                    .debounce(300)
                    .collect { onClick ->
                        onClick.invoke()
                    }
            }
        }

        fun debounceClicks(onClick: () -> Unit) {
            debounceState.value = onClick
        }
    }
}