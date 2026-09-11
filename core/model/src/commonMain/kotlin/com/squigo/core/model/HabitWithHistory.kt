package com.squigo.core.model

import kotlinx.datetime.LocalDate

/**
 * A habit combined with its per-day counts and derived streak stats.
 * A day counts as "done" once its count reaches [Habit.targetCount].
 */
data class HabitWithHistory(
  val habit: Habit,
  val counts: Map<LocalDate, Int>,
  val currentStreak: Int,
  val longestStreak: Int,
) {
  val doneDates: Set<LocalDate> get() = counts.filterValues { it >= habit.targetCount }.keys

  fun countOn(date: LocalDate): Int = counts[date] ?: 0

  fun isDoneOn(date: LocalDate): Boolean = countOn(date) >= habit.targetCount
}
