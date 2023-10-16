package ir.alirezanazari.foursquare.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.alirezanazari.foursquare.R
import ir.alirezanazari.foursquare.databinding.FragmentSearchLocationBinding
import ir.alirezanazari.foursquare.di.provideLocationComponent
import ir.alirezanazari.foursquare.presentation.base.BaseFragment

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class SearchLocationFragment :
    BaseFragment<SearchLocationViewModel, FragmentSearchLocationBinding>() {

    override fun getViewModelClass() = SearchLocationViewModel::class.java

    override fun initInjection() {
        provideLocationComponent().inject(this)
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ) = FragmentSearchLocationBinding.inflate(inflater, parent, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMotionTransition()
    }

    private fun setupMotionTransition() = with(binding) {
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && rootLayout.currentState != R.id.end) {
                rootLayout.transitionToEnd()
            }
        }
    }
}