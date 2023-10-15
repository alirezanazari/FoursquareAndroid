package ir.alirezanazari.foursquare.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ir.alirezanazari.foursquare.di.ViewModelKey
import ir.alirezanazari.foursquare.presentation.details.LocationDetailsViewModel
import ir.alirezanazari.foursquare.presentation.search.SearchLocationViewModel

@Module
abstract class LocationViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LocationDetailsViewModel::class)
    abstract fun bindLocationDetailsViewModel(
        viewModel: LocationDetailsViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchLocationViewModel::class)
    abstract fun bindSearchLocationViewModel(
        viewModel: SearchLocationViewModel
    ): ViewModel
}
