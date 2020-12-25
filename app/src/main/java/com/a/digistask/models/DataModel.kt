package com.a.digistask.models

import com.google.gson.annotations.SerializedName

/**
 *
 *Created by Atef on 24/12/20
 *
 */
data class DataModel(
    @SerializedName("RSRP")
    var rsrp: Double,
    @SerializedName("RSRQ")
    var rsrq: Double,
    @SerializedName("SINR")
    var sinr: Double
)