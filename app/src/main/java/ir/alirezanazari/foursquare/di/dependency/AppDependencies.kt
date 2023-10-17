package ir.alirezanazari.foursquare.di.dependency

import foursquare.common.GetLocationHelper

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

interface AppDependencies {
    fun getLocationHelper(): GetLocationHelper
}