package ir.alirezanazari.foursquare.presentation.details

import androidx.lifecycle.viewModelScope
import foursquare.domain.model.LocationDetails
import foursquare.domain.model.LocationField
import foursquare.domain.model.LocationPhotoModel
import foursquare.domain.model.base.ResultEntity
import foursquare.domain.model.base.UiState
import foursquare.domain.usecase.GetLocationDetailsUseCase
import ir.alirezanazari.foursquare.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class LocationDetailsViewModel @Inject constructor(
    private val getLocationDetailsUseCase: GetLocationDetailsUseCase
) : BaseViewModel() {

    private val _photoResult = MutableStateFlow<UiState<LocationPhotoModel?>>(UiState.Loading)
    val photoResult: StateFlow<UiState<LocationPhotoModel?>>
        get() = _photoResult

    fun getLocationPhoto(id: String) {
        getLocationDetailsUseCase.invoke(
            LocationDetails.Request(id, listOf(LocationField.PHOTOS))
        ).onStart {
            _photoResult.value = UiState.Loading
        }.onEach {
            when (it) {
                is ResultEntity.Error -> {
                    _photoResult.value = UiState.Error(it.error.message)
                }

                is ResultEntity.Success -> {
                    _photoResult.value = UiState.Result(it.data.photos?.firstOrNull())
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }
}