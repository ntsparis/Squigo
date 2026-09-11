package com.squigo.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A GitHub-contribution-style grid: [weeks] columns of 7 days each, most recent
 * week last. Every cell is independently tappable so the widget can reuse this
 * same layout for its tap-to-toggle interaction.
 */
@OptIn(ExperimentalTime::class)
@Composable
fun HeatmapGrid(
  doneDates: Set<LocalDate>,
  accentColor: Color,
  modifier: Modifier = Modifier,
  weeks: Int = 12,
  cellSize: androidx.compose.ui.unit.Dp = 12.dp,
  cellSpacing: androidx.compose.ui.unit.Dp = 3.dp,
  today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
  todayOnlyClickable: Boolean = false,
  onDayClick: ((LocalDate) -> Unit)? = null,
) {
  val columns = remember(weeks, today) { buildWeekColumns(weeks, today) }

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(cellSpacing),
  ) {
    columns.forEach { week ->
      Column(verticalArrangement = Arrangement.spacedBy(cellSpacing)) {
        week.forEach { date ->
          HeatmapCell(
            filled = date != null && date in doneDates,
            isFuture = date != null && date > today,
            isToday = date == today,
            accentColor = accentColor,
            size = cellSize,
            onClick = if (date != null && onDayClick != null && date <= today &&
              (!todayOnlyClickable || date == today)
            ) {
              { onDayClick(date) }
            } else {
              null
            },
          )
        }
      }
    }
  }
}

@Composable
private fun HeatmapCell(
  filled: Boolean,
  isFuture: Boolean,
  isToday: Boolean,
  accentColor: Color,
  size: androidx.compose.ui.unit.Dp,
  onClick: (() -> Unit)?,
) {
  val emptyColor = MaterialTheme.colorScheme.surfaceVariant
  val color = when {
    isFuture -> Color.Transparent
    filled -> accentColor
    else -> emptyColor.compositeOver(MaterialTheme.colorScheme.background)
  }

  var cellModifier = Modifier
    .size(size)
    .background(color = color, shape = RoundedCornerShape(3.dp))

  if (isToday) {
    cellModifier = cellModifier.background(
      color = if (filled) color else accentColor.copy(alpha = 0.25f),
      shape = RoundedCornerShape(3.dp),
    )
  }

  if (onClick != null) {
    cellModifier = cellModifier.clickable(onClick = onClick)
  }

  androidx.compose.foundation.layout.Box(modifier = cellModifier)
}

/**
 * Builds [weeks] columns of 7 days ending on the Sunday that contains [today],
 * left-padding earlier columns with nulls so every column has exactly 7 rows.
 */
internal fun buildWeekColumns(weeks: Int, today: LocalDate): List<List<LocalDate?>> {
  val todayIsoDayOfWeek = today.dayOfWeek.isoDayNumber % 7 // 0 = Sunday
  val currentWeekStart = today.minus(todayIsoDayOfWeek, DateTimeUnit.DAY)
  val firstWeekStart = currentWeekStart.minus((weeks - 1) * 7, DateTimeUnit.DAY)

  return (0 until weeks).map { weekIndex ->
    val weekStart = firstWeekStart.plusDays(weekIndex * 7)
    (0 until 7).map { dayIndex ->
      val date = weekStart.plusDays(dayIndex)
      date
    }
  }
}

private fun LocalDate.plusDays(days: Int): LocalDate = this.plus(days, DateTimeUnit.DAY)
