package foursquare.common.lifecycle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

// Written by Alireza Nazari (a.alirezanazari@gmail.com) / (@ali_rezaNazari)

interface AssistedSavedStateViewModelFactory<T : ViewModel> {
    fun create(savedStateHandle: SavedStateHandle): T
}
