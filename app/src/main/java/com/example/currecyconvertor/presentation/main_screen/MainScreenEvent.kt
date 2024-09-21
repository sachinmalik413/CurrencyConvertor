package com.example.currecyconvertor.presentation.main_screen

sealed class MainScreenEvent {
    data class ToCurrencySelect(val value: String): MainScreenEvent()
    data class FromCurrencySelect(val value: String): MainScreenEvent()
    data class CurrencyEntered(val value: String): MainScreenEvent()
}
