package foursquare.domain.usecase

import foursquare.domain.model.SearchLocation
import foursquare.domain.model.base.ResultEntity
import foursquare.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class GetSearchLocationUseCase constructor(
    private val repository: LocationRepository,
    dispatcher: CoroutineDispatcher
) : BaseUseCase<SearchLocation.Request, ResultEntity<SearchLocation.Response>>(dispatcher) {

    override fun execute(parameters: SearchLocation.Request) =
        repository.search(parameters)

}