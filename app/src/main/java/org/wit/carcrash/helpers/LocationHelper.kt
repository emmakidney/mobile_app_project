package org.wit.carcrash.helpers

import android.annotation.SuppressLint
import android.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create
import java.util.concurrent.TimeUnit

class LocationHelper {

}

@SuppressLint("RestrictedApi")
fun createDefaultLocationRequest() : LocationRequest {
    val locationRequest = create().apply{
        interval = TimeUnit.SECONDS.toMillis(60)
        fastestInterval = TimeUnit.SECONDS.toMillis(30)
        priority = PRIORITY_HIGH_ACCURACY
    }
    return locationRequest
}