@file:Suppress("ConflictingOnColor")
/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package dev.msfjarvis.aps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.msfjarvis.aps.R

private val lightColors =
  lightColors(
    primary = Color(0xFF001831),
    primaryVariant = Color(0xFF00000A),
    secondary = Color(0xFFFFFFFF),
    secondaryVariant = Color(0xFFCCCCCC),
    background = Color(0xFFCCCCCC),
    surface = Color(0xFFFFFFFF),
    error = Color.Red.copy(alpha = 0.6f),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onSurface = Color(0xFF000000),
    onError = Color(0xFFFFFFFF),
  )

private val darkColors =
  darkColors(
    primary = Color(0xFF3B6888),
    primaryVariant = Color(0xFF003E5B),
    secondary = Color(0xFF111111),
    secondaryVariant = Color(0xFF000000),
    background = Color(0xFF111111),
    surface = Color(0xFF111111),
    error = Color.Red.copy(alpha = 0.6f),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    onError = Color(0xFFFFFFFF),
  )

private val typography =
  Typography(
    defaultFontFamily =
      FontFamily(
        Font(R.font.manrope_bold, FontWeight.Bold),
        Font(R.font.manrope_extrabold, FontWeight.ExtraBold),
        Font(R.font.manrope_extralight, FontWeight.ExtraLight),
        Font(R.font.manrope_light, FontWeight.Light),
        Font(R.font.manrope_medium, FontWeight.Medium),
        Font(R.font.manrope_regular, FontWeight.Normal),
        Font(R.font.manrope_semibold, FontWeight.SemiBold),
      )
  )

@Composable
fun APSTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colors = if (isSystemInDarkTheme()) darkColors else lightColors,
    typography = typography,
  ) { content() }
}
