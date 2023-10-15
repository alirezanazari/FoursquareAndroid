package ir.alirezanazari.foursquare

import android.app.Application
import ir.alirezanazari.foursquare.di.component.DaggerAppComponent
import ir.alirezanazari.foursquare.di.component.DaggerLifecycleComponent

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class FoursquareApplication: Application() {

    val lifecycleComponent by lazy {
        DaggerLifecycleComponent.factory().create()
    }

    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}