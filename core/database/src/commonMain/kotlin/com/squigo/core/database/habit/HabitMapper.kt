package com.squigo.core.database.habit

import com.squigo.core.database.HabitEntity
import com.squigo.core.model.Habit
import com.squigo.core.model.HabitColor
import com.squigo.core.model.HabitType
import kotlinx.datetime.LocalDate

internal fun HabitEntity.toDomain(): Habit = Habit(
  id = id,
  name = name,
  emoji = emoji,
  color = HabitColor.valueOf(color),
  createdAt = LocalDate.parse(createdAt),
  archived = archived != 0L,
  type = HabitType.valueOf(type),
  targetCount = targetCount.toInt(),
  unitLabel = unitLabel,
)
