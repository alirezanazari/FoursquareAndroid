package foursquare.domain.repository

import foursquare.domain.model.LocationDetails
import foursquare.domain.model.SearchLocation
import foursquare.domain.model.base.ResultEntity
import kotlinx.coroutines.flow.Flow

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

interface LocationDataSource {

    fun search(request: SearchLocation.Request): Flow<ResultEntity<SearchLocation.Response>>

    fun getDetails(request: LocationDetails.Request): Flow<ResultEntity<LocationDetails.Response>>
}