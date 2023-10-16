package foursquare.domain.repository

import foursquare.domain.model.base.ErrorEntity

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

interface ErrorHandler {
    fun getError(throwable: Throwable): ErrorEntity
}