package ir.alirezanazari.foursquare

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import foursquare.domain.model.LocationDetails
import foursquare.domain.model.LocationField
import foursquare.domain.model.LocationPhotoModel
import foursquare.domain.model.base.ErrorEntity
import foursquare.domain.model.base.ResultEntity
import foursquare.domain.model.base.UiState
import foursquare.domain.usecase.GetLocationDetailsUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import ir.alirezanazari.foursquare.presentation.details.LocationDetailsViewModel
import ir.alirezanazari.foursquare.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class LocationDetailsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    lateinit var getLocationDetailsUseCase: GetLocationDetailsUseCase

    private lateinit var viewModel: LocationDetailsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = LocationDetailsViewModel(getLocationDetailsUseCase)
    }

    @Test
    fun `Given place id, When request place photos, Then respond successfully`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        Assert.assertTrue(viewModel.photoResult.value is UiState.Loading)

        val request = LocationDetails.Request(
            id = "7382kdk0920", fields = listOf(LocationField.PHOTOS)
        )
        val response = LocationDetails.Response(
            photos = listOf(
                LocationPhotoModel("3j91", "https://google.com/logo", ".png")
            )
        )

        every {
            getLocationDetailsUseCase.invoke(request)
        } returns flowOf(ResultEntity.Success(response))

        viewModel.getLocationPhoto(request.id)

        assert(viewModel.photoResult.value == UiState.Result(response.photos!!.first()))
    }

    @Test
    fun `Given place id, When request place photos, Then respond error`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val request = LocationDetails.Request(
            id = "7382kdk0920", fields = listOf(LocationField.PHOTOS)
        )
        val error = ErrorEntity.ApiError("Error", 500)

        every {
            getLocationDetailsUseCase.invoke(request)
        } returns flowOf(ResultEntity.Error(ErrorEntity.ApiError(error.message, error.code)))

        viewModel.getLocationPhoto(request.id)

        assert(viewModel.photoResult.value == UiState.Error(error.message))
    }
}