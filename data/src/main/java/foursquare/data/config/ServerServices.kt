package foursquare.data.config

import androidx.annotation.WorkerThread
import foursquare.domain.model.LocationDetails
import foursquare.domain.model.SearchLocation
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

interface ServerServices {

    @WorkerThread
    @GET("places/nearby")
    suspend fun searchPlaces(
        @Query("ll") latlng: String?,
        @Query("query") query: String
    ): SearchLocation.Response

    @WorkerThread
    @GET("places/{id}")
    suspend fun getPlaceDetails(
        @Path("id") id: String,
        @Query("fields") fields: String
    ): LocationDetails.Response

}