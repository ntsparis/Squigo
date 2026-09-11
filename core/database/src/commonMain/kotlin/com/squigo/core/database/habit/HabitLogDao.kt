package com.squigo.core.database.habit

import com.squigo.core.model.HabitLog
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface HabitLogDao {
  fun observeLogsForHabit(habitId: Long): Flow<List<HabitLog>>
  suspend fun getLogsForHabits(habitIds: List<Long>): List<HabitLog>
  suspend fun getCount(habitId: Long, date: LocalDate): Int
  suspend fun setCount(habitId: Long, date: LocalDate, count: Int)
}
