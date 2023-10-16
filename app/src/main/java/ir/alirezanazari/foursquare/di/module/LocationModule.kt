package ir.alirezanazari.foursquare.di.module

import dagger.Module
import dagger.Provides
import foursquare.data.config.ServerServices
import foursquare.data.datasource.RemoteLocationDataSource
import foursquare.data.repository.LocationRepositoryImpl
import foursquare.domain.repository.ErrorHandler
import foursquare.domain.repository.LocationDataSource
import foursquare.domain.repository.LocationRepository
import foursquare.domain.usecase.GetLocationDetailsUseCase
import foursquare.domain.usecase.GetSearchLocationUseCase
import kotlinx.coroutines.Dispatchers

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

@Module
class LocationModule {

    @Provides
    fun provideLocationDataSource(
        api: ServerServices,
        errorHandler: ErrorHandler
    ): LocationDataSource {
        return RemoteLocationDataSource(api, errorHandler)
    }

    @Provides
    fun provideLocationRepository(remote: LocationDataSource): LocationRepository {
        return LocationRepositoryImpl(remote)
    }

    @Provides
    fun provideGetSearchLocationUseCase(repository: LocationRepository) =
        GetSearchLocationUseCase(repository, Dispatchers.IO)

    @Provides
    fun provideGetLocationDetailsUseCase(repository: LocationRepository) =
        GetLocationDetailsUseCase(repository, Dispatchers.IO)
}