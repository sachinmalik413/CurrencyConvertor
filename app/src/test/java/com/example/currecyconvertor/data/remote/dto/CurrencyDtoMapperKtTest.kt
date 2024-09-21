package com.example.currecyconvertor.data.remote.dto

import com.example.currecyconvertor.domain.model.CurrencyRate
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyDtoTest {

    @Test
    fun `toCurrencyRates converts CurrencyDto to List of CurrencyRate`() {
        val currencyDto = CurrencyDto(
            CurrencyDataDto(
                INR = 88.0, EUR = 1.0, USD = 1.08, JPY = 143.0, BGN = 1.95, CZK = 24.0,
                DKK = 7.44, GBP = 0.87, HUF = 370.0, PLN = 4.45, RON = 4.93, SEK = 11.28,
                CHF = 0.98, ISK = 149.0, NOK = 10.85, HRK = 7.53, RUB = 83.0, TRY = 19.15,
                AUD = 1.61, BRL = 5.24, CAD = 1.45, CNY = 7.26, HKD = 8.38, IDR = 15700.0,
                ILS = 3.75, KRW = 1330.0, MXN = 18.0, MYR = 4.78, NZD = 1.73, PHP = 55.0,
                SGD = 1.43, THB = 36.0, ZAR = 18.8
            )
        )

        val expectedCurrencyRates = listOf(
            CurrencyRate("INR", "Indian Rupee", 88.0),
            CurrencyRate("EUR", "Euro", 1.0),
            CurrencyRate("USD", "US Dollar", 1.08),
            CurrencyRate("JPY", "Japanese Yen", 143.0),
            CurrencyRate("BGN", "Bulgarian Lev", 1.95),
            CurrencyRate("CZK", "Czech Republic Koruna", 24.0),
            CurrencyRate("DKK", "Danish Krone", 7.44),
            CurrencyRate("GBP", "British Pound Sterling", 0.87),
            CurrencyRate("HUF", "Hungarian Forint", 370.0),
            CurrencyRate("PLN", "Polish Zloty", 4.45),
            CurrencyRate("RON", "Romanian Leu", 4.93),
            CurrencyRate("SEK", "Swedish Krona", 11.28),
            CurrencyRate("CHF", "Swiss Franc", 0.98),
            CurrencyRate("ISK", "Icelandic Króna", 149.0),
            CurrencyRate("NOK", "Norwegian Krone", 10.85),
            CurrencyRate("HRK", "Croatian Kuna", 7.53),
            CurrencyRate("RUB", "Russian Ruble", 83.0),
            CurrencyRate("TRY", "Turkish Lira", 19.15),
            CurrencyRate("AUD", "Australian Dollar", 1.61),
            CurrencyRate("BRL", "Brazilian Real", 5.24),
            CurrencyRate("CAD", "Canadian Dollar", 1.45),
            CurrencyRate("CNY", "Chinese Yuan", 7.26),
            CurrencyRate("HKD", "Hong Kong Dollar", 8.38),
            CurrencyRate("IDR", "Indonesian Rupiah", 15700.0),
            CurrencyRate("ILS", "Israeli New Sheqel", 3.75),
            CurrencyRate("KRW", "South Korean Won", 1330.0),
            CurrencyRate("MXN", "Mexican Peso", 18.0),
            CurrencyRate("MYR", "Malaysian Ringgit", 4.78),
            CurrencyRate("NZD", "New Zealand Dollar", 1.73),
            CurrencyRate("PHP", "Philippine Peso", 55.0),
            CurrencyRate("SGD", "Singapore Dollar", 1.43),
            CurrencyRate("THB", "Thai Baht", 36.0),
            CurrencyRate("ZAR", "South African Rand", 18.8)
        )

        val actualCurrencyRates = currencyDto.toCurrencyRates()

        assertEquals(expectedCurrencyRates, actualCurrencyRates)
    }
}