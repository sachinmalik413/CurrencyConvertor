package com.example.currecyconvertor.data.remote

import com.example.currecyconvertor.data.remote.dto.CurrencyDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("api/latest.json")
    suspend fun getLatestRates(
        @Query("app_id") apiKey: String = APP_ID
    ): CurrencyDto

    companion object {
        const val APP_ID = "34513e74403a4663878ed449c0ad1bda"
        const val BASE_URL = "https://openexchangerates.org/"
    }

}