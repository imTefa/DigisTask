package com.a.digistask.network

import com.a.digistask.models.DataModel
import retrofit2.http.GET

/**
 *
 *Created by Atef on 24/12/20
 *
 */
interface Api {

    @GET("random")
    suspend fun getData(): DataModel
}