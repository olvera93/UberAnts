package com.olvera.uberants.common.dataAccess

import com.olvera.uberants.common.entities.DeliveryInfo
import com.olvera.uberants.common.entities.RouteInfo
import com.olvera.uberants.common.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDatabase {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(DeliveryService::class.java)

    suspend fun getMeasures(units: String, origins: String, destinations: String, key: String): DeliveryInfo = withContext(
        Dispatchers.IO) {
        service.getDistanceAndTime(units, origins, destinations, key)
    }

    suspend fun getEstimatedRoute(units: String, origin: String, destination: String, mode: String, key: String): RouteInfo = withContext(Dispatchers.IO) {
        service.getDirections(units, origin, destination, mode, key)
    }
}