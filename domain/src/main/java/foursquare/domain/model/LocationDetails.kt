package foursquare.domain.model

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

sealed class LocationDetails {

    data class Request(
        val id: String,
        val fields: List<LocationField> = listOf()
    ): LocationDetails()

    data class Response(
        val photos: List<LocationPhotoModel>? = null
    )
}