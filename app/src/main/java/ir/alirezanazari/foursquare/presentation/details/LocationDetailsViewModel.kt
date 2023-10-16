package ir.alirezanazari.foursquare.presentation.details

import foursquare.domain.usecase.GetLocationDetailsUseCase
import ir.alirezanazari.foursquare.presentation.base.BaseViewModel
import javax.inject.Inject

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class LocationDetailsViewModel @Inject constructor(
    private val getLocationDetailsUseCase: GetLocationDetailsUseCase
) : BaseViewModel() {
}