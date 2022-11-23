package com.example.clock.util.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.intuit.sdp.R

@Composable
 fun NumberPicker(
    modifier: Modifier = Modifier,
    number: TextFieldValue,
    labelTimeUnit: String,
    onNumberChange: (TextFieldValue) -> Unit,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.displayLarge,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {


    val numberType = KeyboardOptions(keyboardType = KeyboardType.Number)
    val colors = TextFieldDefaults.textFieldColors(
        containerColor = backgroundColor,
        focusedIndicatorColor = backgroundColor,
        unfocusedIndicatorColor = backgroundColor
        )

    Surface(modifier = modifier) {

        TextField(
            label = {
                Text(text = labelTimeUnit,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen._5sdp))
                )
            },
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
            colors = colors
        )
    }



}