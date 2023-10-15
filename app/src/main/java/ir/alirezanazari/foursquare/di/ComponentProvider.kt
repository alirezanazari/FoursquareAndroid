package ir.alirezanazari.foursquare.di

import android.content.Context
import androidx.fragment.app.Fragment
import ir.alirezanazari.foursquare.FoursquareApplication
import ir.alirezanazari.foursquare.di.component.AppComponent
import ir.alirezanazari.foursquare.di.component.DaggerLocationComponent
import ir.alirezanazari.foursquare.di.component.LifecycleComponent
import ir.alirezanazari.foursquare.di.component.LocationComponent

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

fun Context.provideAppComponent(): AppComponent =
    (applicationContext as FoursquareApplication).appComponent

fun Context.provideLifecycleComponent(): LifecycleComponent =
    (applicationContext as FoursquareApplication).lifecycleComponent

fun Fragment.provideLocationComponent(): LocationComponent =
    DaggerLocationComponent.factory().create(
        requireContext().provideAppComponent(),
        requireContext().provideLifecycleComponent(),
        requireActivity().applicationContext
    )