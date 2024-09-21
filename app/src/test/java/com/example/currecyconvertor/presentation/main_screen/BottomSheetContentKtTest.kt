package com.example.currecyconvertor.presentation.main_screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.currecyconvertor.domain.model.CurrencyRate
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BottomSheetContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `BottomSheetContent displays currency list and triggers callback on click`() {
        val currencies = listOf(
            CurrencyRate("USD", rate = 1.0, name = "US Dollar"),
            CurrencyRate("EUR", rate = 0.85, name = "Euro")
        )
        var clickedCurrencyCode = ""

        composeTestRule.setContent {
            BottomSheetContent(
                onItemClicked = { clickedCurrencyCode = it },
                currenciesList = currencies
            )
        }

        composeTestRule.onNodeWithText("USD: US Dollar").assertIsDisplayed()
        composeTestRule.onNodeWithText("EUR: Euro").assertIsDisplayed()

        composeTestRule.onNodeWithText("EUR: Euro").performClick()

        composeTestRule.runOnIdle {
            assert(clickedCurrencyCode == "EUR")
        }
    }

    @Test
    fun `BottomSheetContent handles empty currency list`() {
        composeTestRule.setContent {
            BottomSheetContent(
                onItemClicked = {},
                currenciesList = emptyList()
            )
        }

        composeTestRule.onNodeWithText("USD: US Dollar").assertDoesNotExist()
    }
}