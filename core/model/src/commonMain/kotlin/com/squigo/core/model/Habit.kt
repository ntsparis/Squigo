package com.squigo.core.model

import kotlinx.datetime.LocalDate

data class Habit(
  val id: Long,
  val name: String,
  val emoji: String?,
  val color: HabitColor,
  val createdAt: LocalDate,
  val archived: Boolean,
  val type: HabitType = HabitType.CHECK,
  val targetCount: Int = 1,
  val unitLabel: String? = null,
)
