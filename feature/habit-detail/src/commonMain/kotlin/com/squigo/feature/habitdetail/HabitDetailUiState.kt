package com.squigo.feature.habitdetail

import com.squigo.core.model.HabitWithHistory

sealed interface HabitDetailUiState {
  data object Loading : HabitDetailUiState
  data class Content(val habitWithHistory: HabitWithHistory) : HabitDetailUiState
}
