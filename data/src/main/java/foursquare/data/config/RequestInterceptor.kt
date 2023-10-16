package foursquare.data.config

import foursquare.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class RequestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader(AUTHORIZATION_KEY, BuildConfig.FOURSQUARE_API_KEY)
                .build()
        )
    }

    private companion object {
        private const val AUTHORIZATION_KEY = "Authorization"
    }
}