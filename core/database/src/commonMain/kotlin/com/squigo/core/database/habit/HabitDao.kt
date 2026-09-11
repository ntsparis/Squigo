package com.squigo.core.database.habit

import com.squigo.core.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitDao {
  fun observeActiveHabits(): Flow<List<Habit>>
  fun observeAllHabits(): Flow<List<Habit>>
  suspend fun getHabit(id: Long): Habit?
  suspend fun insertHabit(habit: Habit): Long
  suspend fun updateHabit(habit: Habit)
  suspend fun setArchived(id: Long, archived: Boolean)
  suspend fun deleteHabit(id: Long)
}
