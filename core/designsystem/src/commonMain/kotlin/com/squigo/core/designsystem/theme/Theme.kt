package com.squigo.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
  primary = SquigoPrimary,
  background = SquigoBackground,
  surface = SquigoBackground,
)

private val DarkColors = darkColorScheme(
  primary = SquigoPrimary,
  background = SquigoBackgroundDark,
  surface = SquigoSurfaceDark,
)

@Composable
fun SquigoTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColors else LightColors

  MaterialTheme(
    colorScheme = colorScheme,
    content = content,
  )
}
