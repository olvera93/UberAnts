package com.olvera.uberants.common.entities

data class Product(
    var name: String,
    var photoUrl: String,
    var isSelected: Boolean = false
)
