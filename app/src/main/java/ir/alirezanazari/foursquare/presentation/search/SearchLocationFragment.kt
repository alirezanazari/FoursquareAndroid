package ir.alirezanazari.foursquare.presentation.search

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import foursquare.common.GetLocationHelper
import foursquare.common.gone
import foursquare.common.showToast
import foursquare.common.textChanges
import foursquare.common.visible
import foursquare.domain.model.LocationModel
import foursquare.domain.model.base.UiState
import ir.alirezanazari.foursquare.R
import ir.alirezanazari.foursquare.databinding.FragmentSearchLocationBinding
import ir.alirezanazari.foursquare.di.provideLocationComponent
import ir.alirezanazari.foursquare.presentation.base.BaseFragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class SearchLocationFragment :
    BaseFragment<SearchLocationViewModel, FragmentSearchLocationBinding>() {

    @Inject
    lateinit var getLocationHelper: GetLocationHelper

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
        setupLocationPermissions()
    }

    private fun setupPlacesAdapter() {
        adapter = PlaceAdapter()
        binding.locationsRecyclerView.adapter = adapter
        adapter.onItemClicked = { place ->
            findNavController().navigate(
                SearchLocationFragmentDirections.actionToLocationDetailsFragment(
                    distance = place.distance, title = place.name, id = place.id
                ),
                navOptions = navOptions {
                    anim {
                        enter = R.anim.push_up_in
                        exit = R.anim.scale_out
                        popEnter = R.anim.scale_in
                        popExit = R.anim.push_down_out
                    }
                }
            )
        }
    }

    private fun setupMotionTransition() = with(binding) {
        rootLayout.progress = viewModel.motionProgress

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && rootLayout.currentState != R.id.end) {
                if (!viewModel.hasCurrentLocation()) setupLocationPermissions()
                else rootLayout.transitionToEnd()
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
                    showToast(it.message)
                }

                is UiState.Result -> {
                    hideLoading()
                    showPlaces(it.response)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showPlaces(places: List<LocationModel>?) {
        adapter.updateItems(places.orEmpty())
    }

    private fun showLoading() = with(binding) {
        progressBar.visible()
        locationsRecyclerView.gone()
    }

    private fun hideLoading() = with(binding) {
        progressBar.gone()
        locationsRecyclerView.visible()
    }

    private fun setupLocationPermissions() {
        if (hasLocationPermission()) checkLocationSetting()
        else requestPermission()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    private fun requestCurrentLocation() {
        lifecycleScope.launch(exceptionHandler) {
            getLocationHelper.getLocation(
                WeakReference(requireActivity().mainLooper),
                WeakReference { location ->
                    location?.let {
                        viewModel.latitude = it.latitude
                        viewModel.longitude = it.longitude
                    }
                }
            )
        }
    }

    private val openGPSSettingsRequest = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        onGpsSettingResultListener(result.resultCode)
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) &&
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) ->
                checkLocationSetting()

            else -> {
                showToast(getString(R.string.get_location_permission_denied))
            }
        }
    }

    private fun requestPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun checkLocationSetting() {
        val resultListener = { status: Boolean ->
            if (status) requestCurrentLocation()
        }
        getLocationHelper.checkLocationService(
            resultListener,
            openGPSSettingsRequest
        )
    }

    private fun onGpsSettingResultListener(resultCode: Int) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                requestCurrentLocation()
            }

            Activity.RESULT_CANCELED -> {
                showToast(getString(R.string.get_location_permission_denied))
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onPause() {
        viewModel.motionProgress = binding.rootLayout.progress
        super.onPause()
    }

    override fun onDestroy() {
        locationPermissionRequest.unregister()
        openGPSSettingsRequest.unregister()
        getLocationHelper.removeLocationListener()
        super.onDestroy()
    }

    companion object {
        private const val SEARCH_DEBOUNCE = 1000L
    }
}