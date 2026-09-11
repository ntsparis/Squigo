package com.squigo.feature.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.squigo.feature.habits.ui.HabitListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsContent(
  uiState: HabitsUiState,
  onAction: (HabitsAction) -> Unit,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(),
) {
  when {
    uiState.isEmpty -> EmptyHabitsPlaceholder(modifier.fillMaxSize().padding(contentPadding))
    else -> LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = contentPadding) {
      items(uiState.habits, key = { it.habit.id }) { habitWithHistory ->
        val habitId = habitWithHistory.habit.id
        val dismissState = rememberSwipeToDismissBoxState()

        SwipeToDismissBox(
          state = dismissState,
          modifier = Modifier.animateItem(),
          backgroundContent = { DeleteSwipeBackground(dismissState.targetValue) },
          onDismiss = { onAction(HabitsAction.Delete(habitId)) },
        ) {
          HabitListItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            habitWithHistory = habitWithHistory,
            onClick = { onAction(HabitsAction.OpenHabit(habitId)) },
            onLongClick = { onAction(HabitsAction.OpenEditEditor(habitId)) },
            onToggleToday = { onAction(HabitsAction.ToggleToday(habitId)) },
            onAdjustToday = { delta -> onAction(HabitsAction.AdjustToday(habitId, delta)) },
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteSwipeBackground(targetValue: SwipeToDismissBoxValue) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.errorContainer)
      .padding(horizontal = 24.dp),
  ) {
    Icon(
      imageVector = Icons.Filled.Delete,
      contentDescription = "Delete habit",
      tint = MaterialTheme.colorScheme.onErrorContainer,
      modifier = Modifier.align(Alignment.CenterEnd),
    )

    Icon(
      imageVector = Icons.Filled.Delete,
      contentDescription = "Delete habit",
      tint = MaterialTheme.colorScheme.onErrorContainer,
      modifier = Modifier.align(Alignment.CenterStart),
    )
  }
}

@Composable
private fun EmptyHabitsPlaceholder(modifier: Modifier = Modifier) {
  Box(modifier = modifier, contentAlignment = Alignment.Center) {
    Text(
      text = "No habits yet.\nTap + to create your first one.",
      style = MaterialTheme.typography.bodyLarge,
    )
  }
}
