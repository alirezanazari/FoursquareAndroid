package foursquare.data.repository

import foursquare.domain.model.LocationDetails
import foursquare.domain.model.SearchLocation
import foursquare.domain.repository.LocationDataSource
import foursquare.domain.repository.LocationRepository

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class LocationRepositoryImpl(
    private val remote: LocationDataSource
) : LocationRepository {

    override fun search(request: SearchLocation.Request) = remote.search(request)

    override fun getDetails(request: LocationDetails.Request) = remote.getDetails(request)
}