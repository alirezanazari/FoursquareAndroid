package ir.alirezanazari.foursquare.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import foursquare.common.gone
import foursquare.common.loadImage
import foursquare.common.showToast
import foursquare.common.visible
import foursquare.domain.model.base.UiState
import ir.alirezanazari.foursquare.R
import ir.alirezanazari.foursquare.databinding.FragmentLocationDetailsBinding
import ir.alirezanazari.foursquare.di.provideLocationComponent
import ir.alirezanazari.foursquare.presentation.base.BaseFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class LocationDetailsFragment :
    BaseFragment<LocationDetailsViewModel, FragmentLocationDetailsBinding>() {

    private val args by navArgs<LocationDetailsFragmentArgs>()

    override fun getViewModelClass() = LocationDetailsViewModel::class.java

    override fun initInjection() {
        provideLocationComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getLocationPhoto(args.id)
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ) = FragmentLocationDetailsBinding.inflate(inflater, parent, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        super.onViewCreated(view, savedInstanceState)
        showPrefetchedLocationDetails()
        observeLocationPhotos()
    }

    private fun setupToolbar() = with(binding) {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.run {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun showPrefetchedLocationDetails() = with(binding) {
        toolbar.title = args.title
        distanceTextView.text = getString(R.string.distance, args.distance.toString())
    }

    private fun observeLocationPhotos() {
        viewModel.photoResult.onEach {
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
                    binding.bannerImageView.loadImage(
                        url = it.response?.url.orEmpty(), placeholder = R.drawable.placeholder
                    )
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showLoading() = with(binding) {
        progressBar.visible()
        contentsGroup.gone()
    }

    private fun hideLoading() = with(binding) {
        progressBar.gone()
        contentsGroup.visible()
    }
}