package foursquare.common

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import java.lang.ref.WeakReference

// Written by Alireza Nazari, <@ali_rezaNazari> <a.alirezaNazari@gmail.com>.

class GetLocationHelper constructor(private val context: Context) {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var currentLocation: Location? = null

    fun checkLocationService(
        onResult: (Boolean) -> Unit,
        openOsGpsSettingResultListener: ActivityResultLauncher<IntentSenderRequest>? = null
    ) {
        fun openGpsSettingDialog(exception: ResolvableApiException) {
            try {
                openOsGpsSettingResultListener?.launch(
                    IntentSenderRequest.Builder(exception.resolution).build()
                )
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
        checkLocationSettings(
            onResultReady = {
                onResult.invoke(it)
            },
            onFailed = {
                openGpsSettingDialog(it)
            }
        )
    }

    private fun checkLocationSettings(
        onResultReady: (Boolean) -> Unit,
        onFailed: (ResolvableApiException) -> Unit,
    ) {
        val locationRequest = buildLocationRequest()

        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> =
            client.checkLocationSettings(locationSettingsRequest)

        task.addOnSuccessListener { locationSettingsResponse ->
            onResultReady.invoke(
                locationSettingsResponse?.locationSettingsStates?.isGpsUsable ?: false
            )
        }
        task.addOnFailureListener { exception ->
            onResultReady.invoke(false)
            if (exception is ResolvableApiException) {
                onFailed.invoke(exception)
            }
        }
    }

    fun getLocation(
        looper: WeakReference<Looper?>? = null,
        onResult: WeakReference<(Location?) -> Unit>
    ) {
        currentLocation?.let {
            onResult.get()?.invoke(it)
            return
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onResult.get()?.invoke(null)
            return
        }

        fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
            location?.let { lastLocation ->
                if (lastLocation.time + TIME_THRESHOLD > System.currentTimeMillis() &&
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    lastLocation.onResultAction(onResult)
                } else if (lastLocation.time + TIME_THRESHOLD > System.currentTimeMillis() &&
                    lastLocation.accuracy < ACCURACY_THRESHOLD
                ) {
                    lastLocation.onResultAction(onResult)
                } else {
                    getCurrentLocation(looper, onResult)
                }
            } ?: getCurrentLocation(looper, onResult)
        }
    }

    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ]
    )
    private fun getCurrentLocation(
        looper: WeakReference<Looper?>? = null,
        onResult: WeakReference<(Location?) -> Unit>
    ) {

        val mLocationRequest = buildLocationRequest()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.firstOrNull()?.let { location ->
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        location.onResultAction(onResult)
                    } else if (location.accuracy < ACCURACY_THRESHOLD) {
                        location.onResultAction(onResult)
                    }
                }
            }
        }

        looper?.get()?.let {
            locationCallback?.let { callback ->
                fusedLocationClient?.requestLocationUpdates(
                    mLocationRequest,
                    callback,
                    it
                )
            }
        }
    }

    private fun Location.onResultAction(resultListener: WeakReference<(Location?) -> Unit>) {
        resultListener.get()?.invoke(this)
        currentLocation = this
        removeLocationListener()
    }

    fun removeLocationListener() {
        locationCallback?.let {
            fusedLocationClient?.removeLocationUpdates(it)
            fusedLocationClient = null
        }
    }

    private fun buildLocationRequest() =
        LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(FAST_INTERVAL)
            .setFastestInterval(FAST_INTERVAL)


    companion object {
        private const val TIME_THRESHOLD = 15 * 60 * 1000 // 15m
        private const val ACCURACY_THRESHOLD = 1000
        private const val FAST_INTERVAL = 500L
    }

}
