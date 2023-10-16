package foursquare.domain.model.base

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

sealed class UiState<out T> {

    data object Loading : UiState<Nothing>()

    data class Error(
        val message: String = ""
    ) : UiState<Nothing>()

    data class Result<out T>(
        val response: T
    ) : UiState<T>()
}
