package ir.alirezanazari.foursquare.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import foursquare.common.GetLocationHelper
import javax.inject.Singleton

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideGetLocationHelper(context: Context) = GetLocationHelper(context)
}