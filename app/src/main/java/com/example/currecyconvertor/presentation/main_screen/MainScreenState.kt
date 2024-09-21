package com.example.currecyconvertor.presentation.main_screen

import com.example.currecyconvertor.domain.model.CurrencyRate

data class MainScreenState(
    val fromCurrencyCode: String = "INR",
    val toCurrencyCode: String = "USD",
    val fromCurrencyValue: String = "",
    val toCurrencyValue: String = "0.00",
    val currencyRates: Map<String, CurrencyRate> = emptyMap(),
    val error: String? = null
)