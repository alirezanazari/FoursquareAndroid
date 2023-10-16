package foursquare.common

import android.widget.ImageView
import coil.load

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

fun ImageView.loadImage(url: String) {
    load(url) {
        crossfade(true)
    }
}