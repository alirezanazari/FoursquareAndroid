package ir.alirezanazari.foursquare.presentation.details

import android.view.LayoutInflater
import android.view.ViewGroup
import ir.alirezanazari.foursquare.databinding.FragmentLocationDetailsBinding
import ir.alirezanazari.foursquare.presentation.base.BaseFragment

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class LocationDetailsFragment :
    BaseFragment<LocationDetailsViewModel, FragmentLocationDetailsBinding>() {

    override fun getViewModelClass() = LocationDetailsViewModel::class.java

    override fun initInjection() {
        TODO("Not yet implemented")
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ) = FragmentLocationDetailsBinding.inflate(inflater, parent, false)
}