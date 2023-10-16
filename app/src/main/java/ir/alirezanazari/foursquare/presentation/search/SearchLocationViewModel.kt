package ir.alirezanazari.foursquare.presentation.search

import foursquare.domain.usecase.GetSearchLocationUseCase
import ir.alirezanazari.foursquare.presentation.base.BaseViewModel
import javax.inject.Inject

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class SearchLocationViewModel @Inject constructor(
    private val searchLocationUseCase: GetSearchLocationUseCase
) : BaseViewModel() {
}