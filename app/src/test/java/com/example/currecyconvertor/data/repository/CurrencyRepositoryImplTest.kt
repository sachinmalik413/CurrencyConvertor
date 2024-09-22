package com.example.currecyconvertor.data.repository

import com.example.currecyconvertor.data.local.CurrencyRateDao
import com.example.currecyconvertor.data.local.entity.CurrencyRateEntity
import com.example.currecyconvertor.data.local.entity.toCurrencyRate
import com.example.currecyconvertor.data.remote.CurrencyApi
import com.example.currecyconvertor.domain.model.CurrencyRate
import com.example.currecyconvertor.domain.model.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class CurrencyRepositoryImplTest {

    private lateinit var api: CurrencyApi
    private lateinit var dao: CurrencyRateDao
    private lateinit var repository: CurrencyRepositoryImpl

    @Before
    fun setup() {
        api = mock()
        dao = mock()
        repository = CurrencyRepositoryImpl(api, dao)
    }

    @Test
    fun `getCurrencyRatesList should return success with local data first`() = runTest {
        val localCurrencyRates = listOf(
            CurrencyRateEntity("USD", rate = 1.0, name = "USD"),
            CurrencyRateEntity("EUR", rate = 0.85, name = "EUR")
        )

        doReturn(localCurrencyRates).`when`(dao).getAllCurrencyRates()

        val result = repository.getCurrencyRatesList().firstOrNull()

        val expected = Resource.Success(localCurrencyRates.map { it.toCurrencyRate() })

        assertEquals(expected.data, result?.data)
    }

    @Test
    fun `getCurrencyRatesList should return success with remote data after fetching`() = runTest {
        val localCurrencyRates = listOf(
            CurrencyRateEntity("USD", rate = 1.0, name = "USD"),
            CurrencyRateEntity("EUR", rate = 0.85, name = "EUR")
        )
        val remoteCurrencyRates = listOf(
            CurrencyRate("USD", rate = 1.0, name = "USD"),
            CurrencyRate("GBP", rate = 0.72, name = "GBP")
        )

        doReturn(localCurrencyRates).`when`(dao).getAllCurrencyRates()
        doReturn(remoteCurrencyRates).`when`(api).getLatestRates()

        val list = repository.getCurrencyRatesList().toList()

        val result = list.lastOrNull()

        val expected = Resource.Success(remoteCurrencyRates)

        assertEquals(expected.data, result?.data)
    }


    @Test
    fun `getCurrencyRatesList should return error with local data when RuntimeException occurs`() =
        runTest {
            val localCurrencyRates = listOf(
                CurrencyRateEntity("USD", rate = 1.0, name = "USD")
            )
            val expectedCurrencyRates =
                localCurrencyRates.map { it.toCurrencyRate() }

            doReturn(localCurrencyRates).`when`(dao).getAllCurrencyRates()
            doThrow(RuntimeException::class).`when`(api).getLatestRates()

            val result = repository.getCurrencyRatesList().lastOrNull()

            assertEquals(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection",
                    data = expectedCurrencyRates
                ).data,
                result?.data
            )
        }

    @Test
    fun `getCurrencyRatesList should return error with local data when Exception occurs`() = runTest {
        val localCurrencyRates = listOf(
            CurrencyRateEntity("USD", rate = 1.0, name = "USD")
        )
        val errorMessage = "Some error occurred"

        doReturn(localCurrencyRates).`when`(dao).getAllCurrencyRates()
        doThrow(RuntimeException(errorMessage)).`when`(api).getLatestRates()

        val result = repository.getCurrencyRatesList().lastOrNull()

        assertEquals(
            Resource.Error(
                message = "Oops, something went wrong! $errorMessage",
                data = localCurrencyRates
            ).message,
            result?.message
        )
    }
}