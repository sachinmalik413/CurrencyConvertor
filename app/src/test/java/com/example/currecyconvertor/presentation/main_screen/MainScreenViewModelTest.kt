package com.example.currecyconvertor.presentation.main_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currecyconvertor.domain.model.CurrencyRate
import com.example.currecyconvertor.domain.model.Resource
import com.example.currecyconvertor.domain.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class MainScreenViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MainScreenViewModel
    private lateinit var repository: CurrencyRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = MainScreenViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCurrencyRatesList should update state with success result`() = runTest {
        val currencyRates = listOf(
            CurrencyRate("USD", rate = 1.0, name = "USD"),
            CurrencyRate("EUR", rate = 0.85, name = "EUR")
        )
        val successResult = Resource.Success(currencyRates)

        whenever(repository.getCurrencyRatesList()).thenReturn(flowOf(successResult))

             viewModel.getCurrencyRatesList()

        advanceUntilIdle()

        val expectedState = MainScreenState(
            currencyRates = currencyRates.associateBy { it.code },
            error = null
        )
        println("before assert")
        assertEquals(expectedState, viewModel.state)
    }

    @Test
    fun `getCurrencyRatesList should update state with error result`() = runTest {
        val errorMessage = "Network error"
        val errorResult = Resource.Error<List<CurrencyRate>>(message = errorMessage)

        whenever(repository.getCurrencyRatesList()).thenReturn(flowOf(errorResult))

        viewModel.getCurrencyRatesList()

        advanceUntilIdle()

        val expectedState = MainScreenState(
            currencyRates = emptyMap(),
            error = errorMessage
        )
        println("TEST ${viewModel.state} && ${expectedState.error}")
        assertEquals(expectedState, viewModel.state)
    }

    @Test
    fun `onEvent ToCurrencySelect should update toCurrencyCode`() {
        val newToCurrencyCode = "EUR"
        val event = MainScreenEvent.ToCurrencySelect(newToCurrencyCode)

        viewModel.onEvent(event)

        assertEquals(newToCurrencyCode, viewModel.state.toCurrencyCode)
    }

    @Test
    fun `onEvent FromCurrencySelect should update fromCurrencyCode and reset toCurrencyValue`() {
        val newFromCurrencyCode = "EUR"
        val event = MainScreenEvent.FromCurrencySelect(newFromCurrencyCode)

        viewModel.onEvent(event)

        assertEquals(newFromCurrencyCode, viewModel.state.fromCurrencyCode)
        assertTrue(viewModel.state.toCurrencyValue.toDouble().isNaN())
    }

    @Test
    fun `onEvent CurrencyEntered should update currency values`() {
        val fromCurrencyCode = "USD"
        val toCurrencyCode = "EUR"
        val fromValue = "100"

        val currencyRates = listOf(
            CurrencyRate(fromCurrencyCode, rate = 1.0, name = fromCurrencyCode),
            CurrencyRate(toCurrencyCode, rate = 0.85, name = toCurrencyCode)
        )
        viewModel.state = viewModel.state.copy(
            currencyRates = currencyRates.associateBy { it.code },
            fromCurrencyCode = fromCurrencyCode,
            toCurrencyCode = toCurrencyCode
        )

        val event = MainScreenEvent.CurrencyEntered(fromValue)

        viewModel.onEvent(event)

        assertEquals(fromValue, viewModel.state.fromCurrencyValue)
        assertEquals("85.00", viewModel.state.toCurrencyValue)
    }

    @Test
    fun `updateCurrencyValue should handle empty input`() {
        val fromCurrencyCode = "USD"
        val toCurrencyCode = "EUR"

        val currencyRates = listOf(
            CurrencyRate(fromCurrencyCode, rate = 1.0, name = fromCurrencyCode),
            CurrencyRate(toCurrencyCode, rate = 0.85, name = toCurrencyCode)
        )
        viewModel.state = viewModel.state.copy(
            currencyRates = currencyRates.associateBy { it.code },
            fromCurrencyCode = fromCurrencyCode,
            toCurrencyCode = toCurrencyCode
        )

        viewModel.updateCurrencyValue("")

        assertEquals("", viewModel.state.fromCurrencyValue)
        assertEquals(".00", viewModel.state.toCurrencyValue)
    }

    @Test
    fun `updateCurrencyValue should handle missing currency rates`() {
        viewModel.state = viewModel.state.copy(
            currencyRates = emptyMap(),
            fromCurrencyCode = "USD",
            toCurrencyCode = "EUR"
        )

        viewModel.updateCurrencyValue("100")

        assertEquals("100", viewModel.state.fromCurrencyValue)
        assertTrue(viewModel.state.toCurrencyValue.toDouble().isNaN())
    }
}