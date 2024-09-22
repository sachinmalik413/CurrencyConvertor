package com.example.currecyconvertor.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.currecyconvertor.data.local.entity.CurrencyRateEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyRateDaoTest {

    private lateinit var database: CurrencyRateDatabase
    private lateinit var dao: CurrencyRateDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CurrencyRateDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.currencyRateDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun upsert_Allinserts_and_updates_currency_rates() = runBlocking {
        val currencyRate1 = CurrencyRateEntity("USD", rate = 1.0, name = "US Dollar")
        val currencyRate2 = CurrencyRateEntity("EUR", rate = 0.85, name = "Euro")
        dao.upsertAll(listOf(currencyRate1, currencyRate2))

        val currencyRate3 = CurrencyRateEntity("USD", rate = 1.1, name = "US Dollar")
        dao.upsertAll(listOf(currencyRate3))

        val allCurrencyRates = dao.getAllCurrencyRates()
        assertEquals(2, allCurrencyRates.size)
        assertEquals(1.1, allCurrencyRates[0].rate, 0.001)
    }

    @Test
    fun getAllCurrencyRates_returns_all_currency_rates() = runBlocking {
        val currencyRate1 = CurrencyRateEntity("USD", rate = 1.0, name = "US Dollar")
        val currencyRate2 = CurrencyRateEntity("EUR", rate = 0.85, name = "Euro")
        dao.upsertAll(listOf(currencyRate1, currencyRate2))

        val allCurrencyRates = dao.getAllCurrencyRates()
        assertEquals(2, allCurrencyRates.size)
        assertEquals(currencyRate1, allCurrencyRates[0])
        assertEquals(currencyRate2, allCurrencyRates[1])
    }
}
