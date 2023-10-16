package foursquare.data.datasource

import foursquare.data.config.ServerServices
import foursquare.domain.model.LocationDetails
import foursquare.domain.model.SearchLocation
import foursquare.domain.model.base.ResultEntity
import foursquare.domain.repository.ErrorHandler
import foursquare.domain.repository.LocationDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class RemoteLocationDataSource constructor(
    private val api: ServerServices,
    errorHandler: ErrorHandler,
) : LocationDataSource, BaseDataSource(errorHandler) {

    override fun search(request: SearchLocation.Request): Flow<ResultEntity<SearchLocation.Response>> {
        return flow {
            val result = safeRequest { api.searchPlaces(request.latlng, request.query) }
            emit(result)
        }
    }

    override fun getDetails(request: LocationDetails.Request): Flow<ResultEntity<LocationDetails.Response>> {
        return flow {
            val result = safeRequest {
                api.getPlaceDetails(request.id, request.fields.joinToString { it.field })
            }
            emit(result)
        }
    }
}