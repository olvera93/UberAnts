package com.olvera.uberants.common.entities

data class DeliveryInfo(
    val destination_addresses: Array<String>,
    val origin_addresses: Array<String>,
    val rows: Array<Row>
)
