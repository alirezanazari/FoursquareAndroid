package ir.alirezanazari.foursquare.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import foursquare.data.config.RequestInterceptor
import foursquare.data.config.RetrofitHelper
import foursquare.data.config.ServerServices
import foursquare.data.datasource.GeneralErrorHandler
import foursquare.domain.repository.ErrorHandler
import javax.inject.Singleton

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRequestInterceptor(): RequestInterceptor {
        return RequestInterceptor()
    }

    @Singleton
    @Provides
    fun provideServerServices(interceptor: RequestInterceptor): ServerServices {
        return RetrofitHelper.invoke(interceptor)
    }

    @Provides
    fun provideErrorHandler(context: Context): ErrorHandler {
        return GeneralErrorHandler(context)
    }
}