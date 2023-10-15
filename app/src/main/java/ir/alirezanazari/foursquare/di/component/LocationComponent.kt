package ir.alirezanazari.foursquare.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ir.alirezanazari.foursquare.di.module.LocationViewModelModule
import ir.alirezanazari.foursquare.di.scope.LocationScope
import ir.alirezanazari.foursquare.presentation.details.LocationDetailsFragment
import ir.alirezanazari.foursquare.presentation.search.SearchLocationFragment

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

@LocationScope
@Component(
    dependencies = [AppComponent::class, LifecycleComponent::class],
    modules = [LocationViewModelModule::class]
)
interface LocationComponent {

    @Component.Factory
    interface Factory {
        fun create(
            appComponent: AppComponent,
            lifecycleComponent: LifecycleComponent,
            @BindsInstance context: Context
        ): LocationComponent
    }

    fun inject(fragment: SearchLocationFragment)
    fun inject(fragment: LocationDetailsFragment)
}