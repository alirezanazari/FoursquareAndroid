package ir.alirezanazari.foursquare.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import foursquare.common.gone
import foursquare.common.textChanges
import foursquare.common.visible
import foursquare.domain.model.LocationModel
import foursquare.domain.model.base.UiState
import ir.alirezanazari.foursquare.R
import ir.alirezanazari.foursquare.databinding.FragmentSearchLocationBinding
import ir.alirezanazari.foursquare.di.provideLocationComponent
import ir.alirezanazari.foursquare.presentation.base.BaseFragment
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class SearchLocationFragment :
    BaseFragment<SearchLocationViewModel, FragmentSearchLocationBinding>() {

    private lateinit var adapter: PlaceAdapter

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
        setupPlacesAdapter()
        setupSearch()
        observeSearchResult()
    }

    private fun setupPlacesAdapter() {
        adapter = PlaceAdapter()
        binding.locationsRecyclerView.adapter = adapter
    }

    private fun setupMotionTransition() = with(binding) {
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && rootLayout.currentState != R.id.end) {
                rootLayout.transitionToEnd()
            }
        }
    }

    private fun setupSearch() = with(binding) {
        searchEditText.textChanges()
            .debounce(SEARCH_DEBOUNCE)
            .onEach {
                viewModel.searchNearby(it?.trim().toString())
            }
            .launchIn(lifecycleScope)
    }

    private fun observeSearchResult() {
        viewModel.searchResult.onEach {
            when (it) {
                is UiState.Loading -> {
                    showLoading()
                }

                is UiState.Error -> {
                    hideLoading()
                    showError(it.message)
                }

                is UiState.Result -> {
                    hideLoading()
                    showPlaces(it.response)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showPlaces(places: List<LocationModel>) {
        adapter.updateItems(places)
    }

    private fun showLoading() = with(binding) {
        progressBar.visible()
        locationsRecyclerView.gone()
    }

    private fun hideLoading() = with(binding) {
        progressBar.gone()
        locationsRecyclerView.visible()
    }

    private fun showError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 1000L
    }
}