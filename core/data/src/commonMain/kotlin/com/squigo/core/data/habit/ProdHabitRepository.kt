package com.squigo.core.data.habit

import com.squigo.core.database.habit.HabitDao
import com.squigo.core.database.habit.HabitLogDao
import com.squigo.core.domain.habit.HabitRepository
import com.squigo.core.model.Habit
import com.squigo.core.model.HabitColor
import com.squigo.core.model.HabitLog
import com.squigo.core.model.HabitType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ProdHabitRepository(
  private val habitDao: HabitDao,
  private val habitLogDao: HabitLogDao,
  private val clock: Clock = Clock.System,
) : HabitRepository {

  override fun observeActiveHabits(): Flow<List<Habit>> = habitDao.observeActiveHabits()

  override fun observeAllHabits(): Flow<List<Habit>> = habitDao.observeAllHabits()

  override fun observeLogsForHabit(habitId: Long): Flow<List<HabitLog>> = habitLogDao.observeLogsForHabit(habitId)

  override suspend fun getHabit(id: Long): Habit? = habitDao.getHabit(id)

  override suspend fun createHabit(
    name: String,
    emoji: String?,
    color: HabitColor,
    type: HabitType,
    targetCount: Int,
    unitLabel: String?,
  ): Long = habitDao.insertHabit(
    Habit(
      id = 0,
      name = name,
      emoji = emoji,
      color = color,
      createdAt = clock.todayIn(TimeZone.currentSystemDefault()),
      archived = false,
      type = type,
      targetCount = targetCount,
      unitLabel = unitLabel,
    ),
  )

  override suspend fun updateHabit(habit: Habit) = habitDao.updateHabit(habit)

  override suspend fun setArchived(id: Long, archived: Boolean) = habitDao.setArchived(id, archived)

  override suspend fun deleteHabit(id: Long) = habitDao.deleteHabit(id)

  override suspend fun getCount(habitId: Long, date: LocalDate): Int = habitLogDao.getCount(habitId, date)

  override suspend fun setCount(habitId: Long, date: LocalDate, count: Int) =
    habitLogDao.setCount(habitId, date, count)

  override suspend fun getLogsForHabits(habitIds: List<Long>): List<HabitLog> =
    habitLogDao.getLogsForHabits(habitIds)
}
