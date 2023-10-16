package foursquare.domain.model.base

sealed class ErrorEntity(var message: String, val code: Int) {

    data class ApiError(val data: String, val errorCode: Int) : ErrorEntity(data, errorCode)

    data object Network : ErrorEntity("No Network Access", -1)

    data class Generic(var data: String) : ErrorEntity(data, -2)
}
