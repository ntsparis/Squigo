package com.squigo.feature.habits.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.squigo.core.designsystem.theme.toComposeColor
import com.squigo.core.model.HabitWithHistory
import com.squigo.core.ui.HabitAvatar
import com.squigo.core.ui.HabitProgressControl
import com.squigo.core.ui.HeatmapGrid
import com.squigo.core.ui.StreakBadge
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalComposeUiApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class, ExperimentalTime::class)
@Composable
fun HabitListItem(
  habitWithHistory: HabitWithHistory,
  onClick: () -> Unit,
  onLongClick: () -> Unit,
  onToggleToday: () -> Unit,
  onAdjustToday: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val habit = habitWithHistory.habit
  val color = habit.color.toComposeColor()
  val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

  Row(
    modifier = modifier
      .fillMaxWidth()
      .combinedClickable(onClick = onClick, onLongClick = onLongClick)
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    HabitAvatar(emoji = habit.emoji, color = color)
    Spacer(modifier = Modifier.width(12.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(text = habit.name, style = MaterialTheme.typography.titleMedium)
      StreakBadge(streak = habitWithHistory.currentStreak)
    }

    Spacer(modifier = Modifier.width(8.dp))

    HeatmapGrid(
      doneDates = habitWithHistory.doneDates,
      accentColor = color,
      weeks = 6,
      cellSize = 10.dp,
      onDayClick = null,
    )

    Spacer(modifier = Modifier.width(12.dp))

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
}
