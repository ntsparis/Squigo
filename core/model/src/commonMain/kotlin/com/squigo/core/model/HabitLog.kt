package com.squigo.core.model

import kotlinx.datetime.LocalDate

data class HabitLog(
  val habitId: Long,
  val date: LocalDate,
  val count: Int,
)
