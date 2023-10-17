package ir.alirezanazari.foursquare.presentation.search

import androidx.lifecycle.viewModelScope
import foursquare.domain.model.LocationModel
import foursquare.domain.model.SearchLocation
import foursquare.domain.model.base.ResultEntity
import foursquare.domain.model.base.UiState
import foursquare.domain.usecase.GetSearchLocationUseCase
import ir.alirezanazari.foursquare.presentation.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class SearchLocationViewModel @Inject constructor(
    private val searchLocationUseCase: GetSearchLocationUseCase
) : BaseViewModel() {

    var motionProgress = 0F

    var latitude = 39.044893
    var longitude = -77.488266

    private var searchJob: Job? = null
    private var searchedKey = ""

    private val _searchResult =
        MutableStateFlow<UiState<List<LocationModel>>>(UiState.Result(listOf()))
    val searchResult: StateFlow<UiState<List<LocationModel>>>
        get() = _searchResult

    fun searchNearby(query: String) {
        searchJob?.cancelChildren()

        when {
            query.isEmpty() -> {
                _searchResult.value = UiState.Result(listOf())
            }

            query == searchedKey && searchResult.value !is UiState.Error -> {
                _searchResult.value = searchResult.value
            }

            else -> {
                searchJob = searchLocationUseCase.invoke(
                    SearchLocation.Request(latitude, longitude, query)
                ).onStart {
                    _searchResult.value = UiState.Loading
                }.onEach {
                    when (it) {
                        is ResultEntity.Error -> {
                            _searchResult.value = UiState.Error(it.error.message)
                        }

                        is ResultEntity.Success -> {
                            _searchResult.value = UiState.Result(it.data.results)
                        }

                        else -> Unit
                    }
                }.launchIn(viewModelScope)
            }
        }
        searchedKey = query
    }
}