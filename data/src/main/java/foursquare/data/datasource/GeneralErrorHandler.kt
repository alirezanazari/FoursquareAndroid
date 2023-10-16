package foursquare.data.datasource

import android.content.Context
import com.google.gson.Gson
import foursquare.data.BuildConfig
import foursquare.data.R
import foursquare.domain.model.base.ErrorEntity
import foursquare.domain.model.base.ErrorResponse
import foursquare.domain.repository.ErrorHandler
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.UnknownHostException

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class GeneralErrorHandler constructor(
    private val context: Context
) : ErrorHandler {

    override fun getError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is UnknownHostException -> ErrorEntity.Network.apply {
                message = context.getString(R.string.no_network_access)
            }

            is MalformedURLException -> ErrorEntity.Network.apply {
                message = context.getString(R.string.server_error)
            }

            is IOException -> ErrorEntity.Network.apply {
                message = context.getString(R.string.server_error)
            }

            is HttpException -> convertHttpErrorBody(
                throwable = throwable
            )

            else -> ErrorEntity.Generic(
                data = throwable.message ?: throwable.toString()
            )
        }
    }

    private fun convertHttpErrorBody(throwable: HttpException): ErrorEntity {
        val code = throwable.response()?.code() ?: DEFAULT_ERROR_CODE
        return ErrorEntity.ApiError(
            data = getErrorMessage(throwable),
            errorCode = code
        )
    }

    private fun getErrorMessage(throwable: HttpException) =
        getErrorResponseHttpException(throwable)
            ?.let(::getErrorMessageFromByteArr) ?: context.getString(R.string.server_error)

    private fun getErrorMessageFromByteArr(data: ByteArray): String {
        return try {
            Gson().fromJson(data.toString(Charsets.UTF_8), ErrorResponse::class.java).message
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.printStackTrace()
            null
        } ?: context.getString(R.string.unknown_error)
    }

    private fun getErrorResponseHttpException(throwable: HttpException): ByteArray? = try {
        val bufferedSource = throwable.response()?.errorBody()?.source()
        bufferedSource?.run {
            val result = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var length: Int
            while (bufferedSource.read(buffer).also { length = it } != -1) {
                result.write(buffer, 0, length)
            }
            result.toByteArray()
        }
    } catch (e: IllegalStateException) {
        if (BuildConfig.DEBUG) e.printStackTrace()
        null
    }

    private companion object {
        private const val DEFAULT_ERROR_CODE = 500
    }
}