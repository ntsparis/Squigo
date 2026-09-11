package com.squigo.feature.habitdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.squigo.core.scaffold.SquigoScaffold
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun HabitDetailScreen(
  habitId: Long,
  onNavigateUp: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: HabitDetailViewModel = koinViewModel(parameters = { parametersOf(habitId) }),
) {
  val uiState by viewModel.uiState.collectAsState()

  SquigoScaffold(
    title = (uiState as? HabitDetailUiState.Content)?.habitWithHistory?.habit?.name ?: "Habit",
    onNavigateUp = onNavigateUp,
    modifier = modifier,
  ) { padding ->
    when (val state = uiState) {
      HabitDetailUiState.Loading -> Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center,
      ) {
        CircularProgressIndicator()
      }

      is HabitDetailUiState.Content -> HabitDetailContent(
        habitWithHistory = state.habitWithHistory,
        onToggleToday = viewModel::onToggleToday,
        onAdjustToday = viewModel::onAdjustToday,
        onDayClick = viewModel::onDayToggled,
        onDayAdjust = viewModel::onDayAdjusted,
        modifier = Modifier.fillMaxSize().padding(padding),
      )
    }
  }
}
