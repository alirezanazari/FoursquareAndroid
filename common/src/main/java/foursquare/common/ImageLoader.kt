package foursquare.common

import android.widget.ImageView
import coil.load

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

fun ImageView.loadImage(url: String, placeholder: Int = 0) {
    load(url) {
        placeholder(placeholder)
        error(placeholder)
        crossfade(true)
    }
}