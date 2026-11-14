package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun TextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    inputTransformation: InputTransformation? = null,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    interactionSource: MutableInteractionSource? = null,
) {
    val textStyle = LocalTextStyle.current.merge(textStyle)
    BasicTextField(
        state = state,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        inputTransformation = inputTransformation,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        lineLimits = lineLimits,
        onKeyboardAction = onKeyboardAction,
        cursorBrush = SolidColor(textStyle.color),
        interactionSource = interactionSource,
        decorator = { innerTextField ->
            if (state.text.isEmpty() && placeholder != null) {
                BasicText(
                    placeholder,
                    style = textStyle.copy(color = SnacColors.faint)
                )
            }
            innerTextField()
        }
    )
}