package com.example.currecyconvertor.presentation.main_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currecyconvertor.domain.model.Resource
import com.example.currecyconvertor.domain.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    var state by mutableStateOf(MainScreenState())

    init {
        getCurrencyRatesList()
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.ToCurrencySelect -> {
                state = state.copy(toCurrencyCode = event.value)
            }

            is MainScreenEvent.CurrencyEntered -> {
                updateCurrencyValue(value = event.value)
            }

            is MainScreenEvent.FromCurrencySelect -> {
                state = state.copy(fromCurrencyCode = event.value)
                updateCurrencyValue("")
            }
        }
    }

    fun getCurrencyRatesList() {
        viewModelScope.launch {
            repository
                .getCurrencyRatesList()
                .collectLatest { result ->
                    state = when (result) {
                        is Resource.Success -> {
                            state.copy(
                                currencyRates = result.data?.associateBy { it.code }
                                    ?: emptyMap(),
                                error = null
                            )
                        }

                        is Resource.Error -> {
                            state.copy(
                                currencyRates = result.data?.associateBy { it.code }
                                    ?: emptyMap(),
                                error = result.message
                            )
                        }
                    }
                }
        }
    }

    fun updateCurrencyValue(value: String) {

        val fromCurrencyRate = state.currencyRates[state.fromCurrencyCode]?.rate ?: 0.0
        val toCurrencyRate = state.currencyRates[state.toCurrencyCode]?.rate ?: 0.0

        val numberFormat = DecimalFormat("#.00")

        val fromValue = value.toDoubleOrNull() ?: 0.0
        val toValue = fromValue / fromCurrencyRate * toCurrencyRate
        state = state.copy(
            fromCurrencyValue = value,
            toCurrencyValue = numberFormat.format(toValue)
        )
    }
}