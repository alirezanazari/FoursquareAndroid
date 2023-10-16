package foursquare.domain.usecase

import foursquare.domain.model.LocationDetails
import foursquare.domain.model.SearchLocation
import foursquare.domain.model.base.ResultEntity
import foursquare.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class GetLocationDetailsUseCase constructor(
    private val repository: LocationRepository,
    dispatcher: CoroutineDispatcher
) : BaseUseCase<LocationDetails.Request, ResultEntity<LocationDetails.Response>>(dispatcher) {

    override fun execute(parameters: LocationDetails.Request) =
        repository.getDetails(parameters)

}