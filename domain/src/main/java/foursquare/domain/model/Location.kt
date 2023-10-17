package foursquare.domain.model

import com.google.gson.annotations.SerializedName

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

data class LocationModel(
    @SerializedName("fsq_id")
    val id: String,
    val name: String,
    val distance: Long,
    val categories: List<LocationCategory>,
    val location: LocationAddressModel
)

data class LocationCategory(
    val id: String,
    val name: String,
    val icon: IconModel?
)

data class LocationAddressModel(
    val address: String,
    val dma: String
) {
    val formatted = "$address $dma"
}

data class IconModel(
    val prefix: String,
    val suffix: String,
) {
    val url = "$prefix$suffix"
}

data class LocationPhotoModel(
    val id: String,
    val prefix: String,
    val suffix: String
)

enum class LocationField(val field: String) {
    PHOTOS("photos")
}