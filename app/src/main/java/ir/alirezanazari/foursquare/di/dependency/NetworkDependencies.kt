package ir.alirezanazari.foursquare.di.dependency

import foursquare.data.config.ServerServices
import foursquare.domain.repository.ErrorHandler

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

interface NetworkDependencies {
    fun serverServices(): ServerServices
    fun errorHandler(): ErrorHandler
}