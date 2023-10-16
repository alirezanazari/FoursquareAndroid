package foursquare.data.datasource

import android.util.Log
import foursquare.data.BuildConfig
import foursquare.domain.model.base.ResultEntity
import foursquare.domain.repository.ErrorHandler

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

abstract class BaseDataSource(private val errorHandler: ErrorHandler) {

    suspend fun <T> safeRequest(apiCall: suspend () -> T): ResultEntity<T> {
        return try {
            ResultEntity.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            errorHandler.getError(throwable).let {
                if (BuildConfig.DEBUG) Log.e(TAG, it.toString())
                ResultEntity.Error(it)
            }
        }
    }

    private companion object {
        private const val TAG = "REQUEST FAILED"
    }
}