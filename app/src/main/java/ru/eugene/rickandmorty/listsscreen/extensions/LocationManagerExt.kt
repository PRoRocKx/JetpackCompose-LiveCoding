package ru.eugene.rickandmorty.listsscreen.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat

fun LocationManager.getLastPosition(context: Context): Location? =
    (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)).takeIf {
        it == PackageManager.PERMISSION_GRANTED
    }?.let {
        this.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }