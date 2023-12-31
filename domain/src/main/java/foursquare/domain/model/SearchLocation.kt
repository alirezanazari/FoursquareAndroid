package foursquare.domain.model

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

sealed class SearchLocation {
    data class Request(
        val lat: Double?,
        val lng: Double?,
        val query: String
    ): SearchLocation() {
        val latlng = if (lat == null || lng == null) null else "$lat,$lng"
    }

    data class Response(
        val results: List<LocationModel>
    ): SearchLocation()
}