package com.squigo.core.domain.habit

import com.squigo.core.model.HabitColor
import com.squigo.core.model.HabitType

class CreateHabitUseCase(
  private val repository: HabitRepository,
  private val widgetRefresher: WidgetRefresher,
) {
  suspend operator fun invoke(
    name: String,
    emoji: String?,
    color: HabitColor,
    type: HabitType = HabitType.CHECK,
    targetCount: Int = 1,
    unitLabel: String? = null,
  ): Long {
    require(name.isNotBlank()) { "Habit name cannot be blank" }
    require(targetCount >= 1) { "Target count must be at least 1" }
    val id = repository.createHabit(
      name = name.trim(),
      emoji = emoji,
      color = color,
      type = type,
      targetCount = targetCount,
      unitLabel = unitLabel?.trim()?.takeIf { it.isNotEmpty() },
    )
    widgetRefresher.refreshAll()
    return id
  }
}
