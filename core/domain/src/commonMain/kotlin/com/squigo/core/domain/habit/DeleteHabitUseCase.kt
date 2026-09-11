package com.squigo.core.domain.habit

class DeleteHabitUseCase(
  private val repository: HabitRepository,
  private val widgetRefresher: WidgetRefresher,
) {
  suspend operator fun invoke(habitId: Long) {
    repository.deleteHabit(habitId)
    widgetRefresher.refreshAll()
  }
}
