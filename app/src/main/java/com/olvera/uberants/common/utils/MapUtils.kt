package com.olvera.uberants.common.utils

import android.content.Context
import android.graphics.Bitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.olvera.uberants.R

object MapUtils {

    private var iconDeliveryMarker: Bitmap? = null
    private var iconDestinationMarker: Bitmap? = null

    private var deliveryMarker: Marker? = null
    private var totalProducts: Int = 0

    fun getDestinationDelivery(): LatLng = LatLng(19.424394544795007, -99.1689509849917)

    fun getOriginDelivery(): LatLng = LatLng(19.424794932049313, -99.16522944262901)

    fun setupMap(context: Context, map: GoogleMap) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(getDestinationDelivery(), 15f))
        map.uiSettings.apply {
            isMyLocationButtonEnabled = false
            isZoomGesturesEnabled = false
            isRotateGesturesEnabled = false
            isTiltGesturesEnabled = false
            isMapToolbarEnabled = false
        }

        setupMapStyle(context, map)

        addDeliveryMarker(context, map, getOriginDelivery())
        addDestinationMarker(map, getDestinationDelivery())
    }

    private fun setupMapStyle(context: Context, map: GoogleMap) {

        try {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


     fun setupMarkersData(context: Context, total: Int) {
        Utils.getBitmapFromVector(context, R.drawable.ic_delivery_motorbike_32)?.let {
            iconDeliveryMarker = it
        }

        Utils.getBitmapFromVector(context, R.drawable.ic_goal_32)?.let {
            iconDestinationMarker = it
        }

        totalProducts = total

    }

    private fun addDeliveryMarker(context: Context, map: GoogleMap, location: LatLng) {
        iconDeliveryMarker?.let {
            deliveryMarker = map.addMarker(MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(it))
                .anchor(0.5f, 0.5f)
                .title(formatTitle(context))
            )
            deliveryMarker?.showInfoWindow()
        }
    }

    private fun formatTitle(context: Context): String {
        var total = ""
        var label = ""

        if (totalProducts == 1) {
            total = context.getString(R.string.tracking_total_product)
            label = context.getString(R.string.tracking_label_product)

        } else {
            total = totalProducts.toString()
            label = context.getString(R.string.tracking_label_products)
        }

        return String.format("%s %s", total, label)

    }

    private fun addDestinationMarker(map: GoogleMap, location: LatLng) {
        iconDestinationMarker?.let {
            map.addMarker(MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(it))
                .anchor(0.3f, 1f)
            )
        }
    }

    fun formatDistance(rawDistance: Double): String {
        var distance = rawDistance.toInt()
        val unit = if (distance > 1_000) {
            distance /= 1_000
            "km"
        } else {
            "m"
        }

        return String.format("%d%s", distance, unit)
    }
}