package com.squigo.core.domain.habit

import com.squigo.core.model.Habit
import com.squigo.core.model.HabitColor
import com.squigo.core.model.HabitLog
import com.squigo.core.model.HabitType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface HabitRepository {
  fun observeActiveHabits(): Flow<List<Habit>>
  fun observeAllHabits(): Flow<List<Habit>>
  fun observeLogsForHabit(habitId: Long): Flow<List<HabitLog>>

  suspend fun getHabit(id: Long): Habit?
  suspend fun createHabit(
    name: String,
    emoji: String?,
    color: HabitColor,
    type: HabitType,
    targetCount: Int,
    unitLabel: String?,
  ): Long
  suspend fun updateHabit(habit: Habit)
  suspend fun setArchived(id: Long, archived: Boolean)
  suspend fun deleteHabit(id: Long)

  suspend fun getCount(habitId: Long, date: LocalDate): Int
  suspend fun setCount(habitId: Long, date: LocalDate, count: Int)
  suspend fun getLogsForHabits(habitIds: List<Long>): List<HabitLog>
}
