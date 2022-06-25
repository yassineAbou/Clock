package com.example.clock.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.example.clock.util.clearFocusOnKeyboardDismiss
import java.time.format.TextStyle

@Composable
 fun NumberPicker(
    modifier: Modifier = Modifier,
    number: TextFieldValue,
    onNumberChange: (TextFieldValue) -> Unit,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.displayLarge
) {

    val background = MaterialTheme.colorScheme.surface
    val colors = TextFieldDefaults.textFieldColors(
        backgroundColor = background,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )
    val numberType = KeyboardOptions(keyboardType = KeyboardType.Number)

    Surface(modifier = modifier) {

        TextField(
            modifier = Modifier
                .clearFocusOnKeyboardDismiss()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && number.text.isEmpty()) {
                        onNumberChange(TextFieldValue("00"))
                    }
                    if (!focusState.isFocused && number.text.length == 1) {
                        onNumberChange(TextFieldValue(number.text.padStart(2, '0')))
                    }
                },
            value = number,
            onValueChange = onNumberChange,
            textStyle = textStyle,
            keyboardOptions = numberType,
            colors = colors,
        )
    }



}