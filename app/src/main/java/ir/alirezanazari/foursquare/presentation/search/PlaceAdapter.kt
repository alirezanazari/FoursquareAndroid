package ir.alirezanazari.foursquare.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import foursquare.common.loadImage
import foursquare.domain.model.LocationModel
import ir.alirezanazari.foursquare.R
import ir.alirezanazari.foursquare.databinding.RowPlaceBinding

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class PlaceAdapter : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    var onItemClicked: ((LocationModel) -> Unit)? = null
    private var items = listOf<LocationModel>()

    fun updateItems(newItems: List<LocationModel>) {
        val diffResult = DiffUtil.calculateDiff(PlacesDiffCallback(items, newItems))
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowPlaceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[holder.adapterPosition])
    }

    inner class ViewHolder(
        private val binding: RowPlaceBinding
    ) : RecyclerView.ViewHolder(binding.rootLayout) {

        fun bind(place: LocationModel) = with(binding) {
            titleTextView.text = place.name
            place.categories.firstOrNull()?.run {
                categoryTextView.text = name
                iconImageView.loadImage(icon?.url.orEmpty(), placeholder = R.drawable.placeholder)
            }
            rootLayout.setOnClickListener {
                onItemClicked?.invoke(place)
            }
        }
    }

}