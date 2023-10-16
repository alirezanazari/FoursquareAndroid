package ir.alirezanazari.foursquare.presentation.search

import androidx.recyclerview.widget.DiffUtil
import foursquare.domain.model.LocationModel

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class PlacesDiffCallback(
    private val oldPlaces: List<LocationModel>,
    private val newPlaces: List<LocationModel>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldPlaces.size

    override fun getNewListSize() = newPlaces.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldPlaces[oldItemPosition].id == newPlaces[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldPlaces[oldItemPosition] == newPlaces[newItemPosition]
}