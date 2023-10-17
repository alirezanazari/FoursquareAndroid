package ir.alirezanazari.foursquare.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ir.alirezanazari.foursquare.di.dependency.AppDependencies
import ir.alirezanazari.foursquare.di.dependency.NetworkDependencies
import ir.alirezanazari.foursquare.di.module.AppModule
import ir.alirezanazari.foursquare.di.module.NetworkModule
import javax.inject.Singleton

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class
    ]
)
interface AppComponent :
    AppDependencies,
    NetworkDependencies {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}
