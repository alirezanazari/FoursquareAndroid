package ir.alirezanazari.foursquare.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import ir.alirezanazari.foursquare.databinding.FragmentSearchLocationBinding
import ir.alirezanazari.foursquare.presentation.base.BaseFragment

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class SearchLocationFragment :
    BaseFragment<SearchLocationViewModel, FragmentSearchLocationBinding>() {

    override fun getViewModelClass() = SearchLocationViewModel::class.java

    override fun initInjection() {
        TODO("Not yet implemented")
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ) = FragmentSearchLocationBinding.inflate(inflater, parent, false)
}