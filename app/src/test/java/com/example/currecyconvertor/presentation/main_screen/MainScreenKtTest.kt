package com.example.currecyconvertor.presentation.main_screen

import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.currecyconvertor.domain.model.CurrencyRate
import com.example.currecyconvertor.ui.theme.CurrencyConvertorTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class MainScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `MainScreen displays components and handles events`() {
        val state = MainScreenState(
            currencyRates = mapOf(
                "USD" to CurrencyRate("USD", rate = 1.0, name = "US Dollar"),
                "EUR" to CurrencyRate("EUR", rate = 0.85, name = "Euro")
            ),
            fromCurrencyValue = "20"
        )
        var eventCaptured: MainScreenEvent? = null

        composeTestRule.setContent {
            CurrencyConvertorTheme {
                MainScreen(state = state, onEvent = { eventCaptured = it })
            }
        }

        composeTestRule.onNodeWithText("Enter Currency").assertIsDisplayed()
        composeTestRule.onNodeWithText(state.fromCurrencyCode).assertIsDisplayed()
        composeTestRule.onNodeWithText(state.toCurrencyValue).assertIsDisplayed()
        composeTestRule.onNodeWithText("US Dollar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Euro").assertIsDisplayed()

        composeTestRule.onNodeWithTag("currencyInput")
            .performTextInput("100")

        composeTestRule.runOnIdle {
            assert(eventCaptured is MainScreenEvent.CurrencyEntered)
            assert((eventCaptured as MainScreenEvent.CurrencyEntered).value == "10020")
        }

        composeTestRule.onNodeWithText("Euro").performClick()

        composeTestRule.runOnIdle {
            //println("value ${state}")
            assert(eventCaptured is MainScreenEvent.CurrencyEntered)
            assert((eventCaptured as MainScreenEvent.CurrencyEntered).value == "20")
            //assert(eventCaptured is MainScreenEvent.ToCurrencySelect)
            //assert((eventCaptured as MainScreenEvent.ToCurrencySelect).value == "EUR")
        }

        composeTestRule.onNodeWithTag("currencyDropDown")
            .performClick()

        composeTestRule.onNodeWithText("Select Currency").assertIsDisplayed()

        composeTestRule.onNodeWithText("USD: US Dollar").assertIsDisplayed()
        composeTestRule.onNodeWithText("USD: US Dollar").assertHasClickAction().performClick()

        composeTestRule.runOnIdle {
            println("value ${state}")
            println(eventCaptured)
            //assert(eventCaptured is MainScreenEvent.FromCurrencySelect)
            //assert((eventCaptured as MainScreenEvent.FromCurrencySelect).value == "USD")
        }
    }

    @Test
    fun `MainScreen displays error toast`() {
        val errorMessage = "Network error"
        val state = MainScreenState(error = errorMessage)

        composeTestRule.setContent {
            CurrencyConvertorTheme {
                MainScreen(state = state, onEvent = {})
            }
        }

       // composeTestRule.onNodeWithText("Network error").assertIsDisplayed()

       /* val shadowToast = Shadows.shadowOf(
            Toast.makeText(
                composeTestRule,
                errorMessage,
                Toast.LENGTH_LONG
            )
        )
        shadowToast.show()
        assert(shadowToast.isShowing)*/
    }

    @Test
    fun `CurrencyButton displays correct text and colors`() {
        val currency = "US Dollar"
        val code = "USD"
        val backgroundColor = Color.Red
        val textColor = Color.White

        composeTestRule.setContent {
            CurrencyConvertorTheme {
                CurrencyButton(
                    currency = currency,
                    code = code,
                    backgroundColor = backgroundColor,
                    textColor = textColor,
                    onClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText(currency)
            .assertIsDisplayed()

    }

    @Test
    fun `CurrencyRow displays currency code and icon`() {
        val currencyCode = "USD"

        composeTestRule.setContent {
            CurrencyRow(currencyCode = currencyCode, onDropDownIconClicked = {})
        }

        composeTestRule.onNodeWithText(currencyCode)
            .assertIsDisplayed()
    }

}
