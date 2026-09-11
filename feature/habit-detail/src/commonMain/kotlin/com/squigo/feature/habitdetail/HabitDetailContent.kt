package com.squigo.feature.habitdetail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.squigo.core.designsystem.theme.toComposeColor
import com.squigo.core.model.HabitWithHistory
import com.squigo.core.ui.HabitProgressControl
import com.squigo.core.ui.HeatmapGrid
import com.squigo.core.ui.StreakBadge
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val HISTORY_DAYS = 30

@OptIn(ExperimentalTime::class)
@Composable
fun HabitDetailContent(
  habitWithHistory: HabitWithHistory,
  onToggleToday: () -> Unit,
  onAdjustToday: (Int) -> Unit,
  onDayClick: (LocalDate) -> Unit,
  onDayAdjust: (LocalDate, Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val habit = habitWithHistory.habit
  val color = habit.color.toComposeColor()
  val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
  val historyDates = remember(today) { (0 until HISTORY_DAYS).map { offset -> today.minus(offset, DateTimeUnit.DAY) } }

  LazyColumn(modifier = modifier) {
    item {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Column(modifier = Modifier.weight(1f)) {
            StreakBadge(streak = habitWithHistory.currentStreak)
            Text(
              text = "Longest streak: ${habitWithHistory.longestStreak} days",
              style = MaterialTheme.typography.bodyMedium,
            )
          }
          HabitProgressControl(
            type = habit.type,
            count = habitWithHistory.countOn(today),
            targetCount = habit.targetCount,
            accentColor = color,
            unitLabel = habit.unitLabel,
            onToggle = onToggleToday,
            onIncrement = { onAdjustToday(1) },
            onDecrement = { onAdjustToday(-1) },
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
          HeatmapGrid(
            doneDates = habitWithHistory.doneDates,
            accentColor = color,
            weeks = 20,
            onDayClick = { date -> onDayClick(date) },
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "History", style = MaterialTheme.typography.titleMedium)
      }
    }

    items(historyDates, key = { it.toString() }) { date ->
      HabitHistoryRow(
        date = date,
        isToday = date == today,
        habitWithHistory = habitWithHistory,
        accentColor = color,
        onToggle = { onDayClick(date) },
        onAdjust = { delta -> onDayAdjust(date, delta) },
        modifier = Modifier.animateItem(),
      )
      HorizontalDivider()
    }

    item { Spacer(modifier = Modifier.height(24.dp)) }
  }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun HabitHistoryRow(
  date: LocalDate,
  isToday: Boolean,
  habitWithHistory: HabitWithHistory,
  accentColor: androidx.compose.ui.graphics.Color,
  onToggle: () -> Unit,
  onAdjust: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val habit = habitWithHistory.habit

  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = date.formatFriendly(isToday),
      style = MaterialTheme.typography.bodyLarge,
      modifier = Modifier.weight(1f),
    )
    HabitProgressControl(
      type = habit.type,
      count = habitWithHistory.countOn(date),
      targetCount = habit.targetCount,
      accentColor = accentColor,
      unitLabel = habit.unitLabel,
      onToggle = onToggle,
      onIncrement = { onAdjust(1) },
      onDecrement = { onAdjust(-1) },
    )
  }
}

private fun LocalDate.formatFriendly(isToday: Boolean): String {
  if (isToday) return "Today"
  val weekday = dayOfWeek.friendlyName()
  return "$weekday, ${monthNumber.toString().padStart(2, '0')}/${dayOfMonth.toString().padStart(2, '0')}"
}

private fun DayOfWeek.friendlyName(): String = when (this) {
  DayOfWeek.MONDAY -> "Mon"
  DayOfWeek.TUESDAY -> "Tue"
  DayOfWeek.WEDNESDAY -> "Wed"
  DayOfWeek.THURSDAY -> "Thu"
  DayOfWeek.FRIDAY -> "Fri"
  DayOfWeek.SATURDAY -> "Sat"
  DayOfWeek.SUNDAY -> "Sun"
  else -> ""
}
