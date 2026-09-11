package com.squigo.feature.habitdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squigo.core.domain.habit.LogHabitUseCase
import com.squigo.core.domain.habit.ObserveHabitWithHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class HabitDetailViewModel(
  private val habitId: Long,
  private val observeHabitWithHistory: ObserveHabitWithHistoryUseCase,
  private val logHabit: LogHabitUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow<HabitDetailUiState>(HabitDetailUiState.Loading)
  val uiState: StateFlow<HabitDetailUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      observeHabitWithHistory(habitId).collect { habitWithHistory ->
        _uiState.update { HabitDetailUiState.Content(habitWithHistory) }
      }
    }
  }

  fun onToggleToday() {
    viewModelScope.launch { logHabit.toggle(habitId) }
  }

  fun onAdjustToday(delta: Int) {
    viewModelScope.launch { logHabit.adjust(habitId, delta) }
  }

  fun onDayToggled(date: LocalDate) {
    viewModelScope.launch { logHabit.toggle(habitId, date) }
  }

  fun onDayAdjusted(date: LocalDate, delta: Int) {
    viewModelScope.launch { logHabit.adjust(habitId, delta, date) }
  }
}
