package com.squigo.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import com.squigo.core.model.HabitColor

val SquigoPrimary = Color(0xFF4C6FFF)
val SquigoBackground = Color(0xFFFAFAFC)
val SquigoBackgroundDark = Color(0xFF121218)
val SquigoSurfaceDark = Color(0xFF1C1C24)

fun HabitColor.toComposeColor(): Color = when (this) {
  HabitColor.RED -> Color(0xFFEF5350)
  HabitColor.ORANGE -> Color(0xFFFF9800)
  HabitColor.YELLOW -> Color(0xFFFDD835)
  HabitColor.GREEN -> Color(0xFF43A047)
  HabitColor.TEAL -> Color(0xFF00897B)
  HabitColor.BLUE -> Color(0xFF1E88E5)
  HabitColor.PURPLE -> Color(0xFF8E24AA)
  HabitColor.PINK -> Color(0xFFD81B60)
}
