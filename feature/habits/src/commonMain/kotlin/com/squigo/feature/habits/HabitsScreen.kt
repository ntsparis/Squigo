package com.squigo.feature.habits

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.squigo.feature.habits.ui.HabitEditorSheet
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
  onOpenHabit: (Long) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: HabitsViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsState()

  fun handleAction(action: HabitsAction) {
    if (action is HabitsAction.OpenHabit) {
      onOpenHabit(action.habitId)
    } else {
      viewModel.onAction(action)
    }
  }

  Scaffold(
    modifier = modifier,
    floatingActionButton = {
      FloatingActionButton(onClick = { viewModel.onAction(HabitsAction.OpenCreateEditor) }) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add habit")
      }
    },
  ) { padding ->
    HabitsContent(
      uiState = uiState,
      onAction = ::handleAction,
      contentPadding = padding,
    )
  }

  uiState.editorState?.let { editorState ->
    HabitEditorSheet(
      state = editorState,
      onDismiss = { viewModel.onAction(HabitsAction.DismissEditor) },
      onNameChanged = { viewModel.onAction(HabitsAction.EditorNameChanged(it)) },
      onEmojiChanged = { viewModel.onAction(HabitsAction.EditorEmojiChanged(it)) },
      onColorChanged = { viewModel.onAction(HabitsAction.EditorColorChanged(it)) },
      onTypeChanged = { viewModel.onAction(HabitsAction.EditorTypeChanged(it)) },
      onTargetCountChanged = { viewModel.onAction(HabitsAction.EditorTargetCountChanged(it)) },
      onUnitLabelChanged = { viewModel.onAction(HabitsAction.EditorUnitLabelChanged(it)) },
      onSave = { viewModel.onAction(HabitsAction.SaveEditor) },
    )
  }
}
