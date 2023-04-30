package com.example.clock.util.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.intuit.sdp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    number: TextFieldValue,
    timeUnit: String,
    onNumberChange: (TextFieldValue) -> Unit,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.displayLarge,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    val numericKeyboard = KeyboardOptions(keyboardType = KeyboardType.Number)
    val colors = TextFieldDefaults.textFieldColors(
        containerColor = backgroundColor,
        focusedIndicatorColor = backgroundColor,
        unfocusedIndicatorColor = backgroundColor,
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        onNumberChange(
            number.copy(
                selection = if (isFocused) {
                    TextRange(
                        start = 0,
                        end = number.text.length,
                    )
                } else {
                    number.selection
                },
            ),
        )
    }

    Surface(modifier = modifier) {
        TextField(
            label = {
                Text(
                    text = timeUnit,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen._5sdp)),
                )
            },
            modifier = Modifier
                .clearFocusOnKeyboardDismiss()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && number.text.isEmpty()) {
                        onNumberChange(TextFieldValue("00"))
                    } else if (!focusState.isFocused && number.text.length == 1) {
                        onNumberChange(TextFieldValue(number.text.padStart(2, '0')))
                    }
                },
            value = number,
            onValueChange = { onNumberChange(it) },
            textStyle = textStyle,
            keyboardOptions = numericKeyboard,
            colors = colors,
            interactionSource = interactionSource,
        )
    }
}
