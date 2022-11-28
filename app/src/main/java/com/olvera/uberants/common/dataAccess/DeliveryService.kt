package com.olvera.uberants.common.dataAccess

import com.olvera.uberants.common.entities.DeliveryInfo
import com.olvera.uberants.common.entities.RouteInfo
import com.olvera.uberants.common.utils.Constants
import retrofit2.http.POST
import retrofit2.http.Query

interface DeliveryService {

    @POST(Constants.DISTANCE_MATRIX_PATH)
    suspend fun getDistanceAndTime(
        @Query(Constants.UNITS_PARAM) units: String,
        @Query(Constants.ORIGINS_PARAM) origins: String,
        @Query(Constants.DESTINATIONS_PARAM) destinations: String,
        @Query(Constants.KEY_PARAM) key: String
    ): DeliveryInfo

    @POST(Constants.DIRECTIONS_PATH)
    suspend fun getDirections(
        @Query(Constants.UNITS_PARAM) units: String,
        @Query(Constants.ORIGIN_PARAM) origin: String,
        @Query(Constants.DESTINATION_PARAM) destination: String,
        @Query(Constants.MODE_PARAM) mode: String,
        @Query(Constants.KEY_PARAM) key: String
    ): RouteInfo

}