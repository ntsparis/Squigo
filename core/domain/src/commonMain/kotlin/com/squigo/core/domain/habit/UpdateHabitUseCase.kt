package com.squigo.core.domain.habit

import com.squigo.core.model.Habit

class UpdateHabitUseCase(
  private val repository: HabitRepository,
  private val widgetRefresher: WidgetRefresher,
) {
  suspend operator fun invoke(habit: Habit) {
    require(habit.name.isNotBlank()) { "Habit name cannot be blank" }
    repository.updateHabit(habit.copy(name = habit.name.trim()))
    widgetRefresher.refreshAll()
  }
}
