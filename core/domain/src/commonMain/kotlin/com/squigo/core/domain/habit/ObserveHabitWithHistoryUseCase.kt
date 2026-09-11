package com.squigo.core.domain.habit

import com.squigo.core.model.HabitWithHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

/**
 * Used by the habit detail screen (and the widget) to observe a single habit's
 * full history, independent of the list-level filters in [ObserveHabitsWithHistoryUseCase].
 */
class ObserveHabitWithHistoryUseCase(
  private val observeHabitsWithHistory: ObserveHabitsWithHistoryUseCase,
) {
  operator fun invoke(habitId: Long): Flow<HabitWithHistory> = observeHabitsWithHistory(includeArchived = true)
    .map { it.firstOrNull { withHistory -> withHistory.habit.id == habitId } }
    .filterNotNull()
}
