package com.omasyo.snac.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.omasyo.snac.core.ui.R


private val DisplayFamily = FontFamily(Font(R.font.anton))

private val TextFamily = FontFamily(Font(R.font.permanent_marker))

@Immutable
data class Typography(
    val hero: TextStyle,
    val display: TextStyle,
    val headline: TextStyle,
    val title: TextStyle,
    val subtitle: TextStyle,
    val tag: TextStyle,
    val body: TextStyle,
    val label: TextStyle,
    val caption: TextStyle,
    val overline: TextStyle,
    val hand: TextStyle,
)



val AppTypography = Typography(
    hero = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 88.sp,
        lineHeight = 0.9.em,
        letterSpacing = 2.sp,
    ),
    display = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 52.sp,
        lineHeight = 0.9.em,
        letterSpacing = 1.sp,
    ),
    headline = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 0.95.em,
        letterSpacing = 1.sp,
    ),
    title = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 1.0.em,
        letterSpacing = 0.5.sp,
    ),
    subtitle = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 1.05.em,
        letterSpacing = 0.sp,
    ),
    tag = TextStyle(
        fontFamily = DisplayFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 1.1.em,
        letterSpacing = 0.5.sp,
    ),
    body = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 1.5.em,
        letterSpacing = 0.sp,
    ),
    label = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 1.3.em,
        letterSpacing = 0.5.sp,
    ),
    caption = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 1.4.em,
        letterSpacing = 0.sp,
    ),
    overline = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 1.2.em,
        letterSpacing = 1.5.sp,
    ),
    hand = TextStyle(
        fontFamily = TextFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 1.2.em,
        letterSpacing = 0.sp,
    ),
)