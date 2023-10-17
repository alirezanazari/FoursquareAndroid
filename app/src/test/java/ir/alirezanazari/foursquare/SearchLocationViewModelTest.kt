package ir.alirezanazari.foursquare

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import foursquare.domain.model.LocationModel
import foursquare.domain.model.SearchLocation
import foursquare.domain.model.base.ErrorEntity
import foursquare.domain.model.base.ResultEntity
import foursquare.domain.model.base.UiState
import foursquare.domain.usecase.GetSearchLocationUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkClass
import ir.alirezanazari.foursquare.presentation.search.SearchLocationViewModel
import ir.alirezanazari.foursquare.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class SearchLocationViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    lateinit var searchLocationUseCase: GetSearchLocationUseCase

    private lateinit var viewModel: SearchLocationViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SearchLocationViewModel(searchLocationUseCase)
    }

    @Test
    fun `Given latlng, When request search pizza, Then respond successfully`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val response = SearchLocation.Response(
            results = listOf(
                mockkClass(LocationModel::class)
            )
        )

        viewModel.latitude = LATITUDE
        viewModel.longitude = LONGITUDE

        every {
            searchLocationUseCase.invoke(any())
        } returns flowOf(ResultEntity.Success(response))

        viewModel.searchNearby("pizza")

        assert(viewModel.searchResult.value == UiState.Result(response.results))
    }

    @Test
    fun `Given latlng, When request search pizza, Then respond error`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val error = ErrorEntity.ApiError("Error", 500)

        viewModel.latitude = LATITUDE
        viewModel.longitude = LONGITUDE

        every {
            searchLocationUseCase.invoke(any())
        } returns flowOf(ResultEntity.Error(ErrorEntity.ApiError(error.message, error.code)))

        viewModel.searchNearby("pizza")

        println("value " + viewModel.searchResult.value)
        assert(viewModel.searchResult.value == UiState.Error(error.message))
    }

    @Test
    fun `Given empty query, When request near locations, Then respond empty list`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        viewModel.searchNearby("")

        assert(viewModel.searchResult.value == UiState.Result(listOf<LocationModel>()))
    }

    @Test
    fun `Given latlng, When check has current location, Then return true`() = runTest {
        viewModel.latitude = LATITUDE
        viewModel.longitude = LONGITUDE

        assert(viewModel.hasCurrentLocation())
    }

    companion object {
        private const val LATITUDE = 39.044893
        private const val LONGITUDE = -77.488266
    }
}