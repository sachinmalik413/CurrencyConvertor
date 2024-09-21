package com.example.currecyconvertor.presentation.main_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.error) {
        if (state.error != null) {
            Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
        }
    }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var shouldBottomSheetShow by remember { mutableStateOf(false) }

    if (shouldBottomSheetShow) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { shouldBottomSheetShow = false },
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(
                        text = "Select Currency",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider()
                }
            },
            content = {
                BottomSheetContent(
                    onItemClicked = { currencyCode ->
                        onEvent(MainScreenEvent.FromCurrencySelect(currencyCode))
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) shouldBottomSheetShow = false
                        }
                    },
                    currenciesList = state.currencyRates.values.toList()
                )
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.End
            ) {

                OutlinedTextField(
                    value = state.fromCurrencyValue,
                    onValueChange = {
                        onEvent(MainScreenEvent.CurrencyEntered(it))
                    },
                    label = { Text("Enter Currency") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                CurrencyRow(
                    modifier = Modifier.fillMaxWidth(),
                    currencyCode = state.fromCurrencyCode,
                    onDropDownIconClicked = {
                        shouldBottomSheetShow = true
                    }
                )


                Spacer(modifier = Modifier.height(12.dp))


                Text(
                    text = state.toCurrencyValue,
                    fontSize = 40.sp,
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.onSurface
                )

            }
        }
        LazyVerticalGrid(
            modifier = Modifier.padding(start = 10.dp, bottom = 30.dp, end = 10.dp),
            columns = GridCells.Fixed(3)
        ) {
            items(state.currencyRates.values.toList()) { key ->
                CurrencyButton(
                    modifier = Modifier.aspectRatio(1f),
                    currency = key.name,
                    code = key.code,
                    backgroundColor = if (key.code == state.toCurrencyCode) {
                        MaterialTheme.colorScheme.primary
                    } else MaterialTheme.colorScheme.surfaceVariant,
                    textColor = if (key.code == state.toCurrencyCode) {
                        Color.White
                    } else Color.Black,
                    onClick = {
                        onEvent(MainScreenEvent.ToCurrencySelect(key.code))
                        onEvent(MainScreenEvent.CurrencyEntered(state.fromCurrencyValue))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun CurrencyRow(
    modifier: Modifier = Modifier,
    currencyCode: String,
    onDropDownIconClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(text = currencyCode, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        IconButton(onClick = onDropDownIconClicked) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Open Bottom Sheet"
            )
        }
    }
}

@Composable
fun CurrencyButton(
    modifier: Modifier = Modifier,
    currency: String,
    code: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .background(color = backgroundColor)
            .border(width = 1.dp, color = Color.Black)
            .clickable { onClick(code) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = currency,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = textColor,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
    }
}