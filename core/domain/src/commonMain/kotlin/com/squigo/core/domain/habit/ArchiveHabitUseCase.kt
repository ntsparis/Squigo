package com.squigo.core.domain.habit

class ArchiveHabitUseCase(
  private val repository: HabitRepository,
  private val widgetRefresher: WidgetRefresher,
) {
  suspend operator fun invoke(habitId: Long, archived: Boolean) {
    repository.setArchived(habitId, archived)
    widgetRefresher.refreshAll()
  }
}
