package foursquare.common

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.


fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                this@callbackFlow.trySend(s).isSuccess
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}