package com.example.currecyconvertor.domain.repository

import com.example.currecyconvertor.domain.model.CurrencyRate
import com.example.currecyconvertor.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getCurrencyRatesList(): Flow<Resource<List<CurrencyRate>>>
}