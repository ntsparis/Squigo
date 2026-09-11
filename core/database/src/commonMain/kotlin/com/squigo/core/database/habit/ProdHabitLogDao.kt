package com.squigo.core.database.habit

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.squigo.core.database.HabitLogEntity
import com.squigo.core.database.SquigoDatabase
import com.squigo.core.model.HabitLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class ProdHabitLogDao(private val database: SquigoDatabase) : HabitLogDao {

  private val queries = database.habitLogQueries

  override fun observeLogsForHabit(habitId: Long): Flow<List<HabitLog>> = queries
    .selectLogsForHabit(habitId)
    .asFlow()
    .mapToList(Dispatchers.Default)
    .map { it.map(HabitLogEntity::toDomain) }

  override suspend fun getLogsForHabits(habitIds: List<Long>): List<HabitLog> = withContext(Dispatchers.Default) {
    queries.selectLogsForHabits(habitIds).executeAsList().map { it.toDomain() }
  }

  override suspend fun getCount(habitId: Long, date: LocalDate): Int = withContext(Dispatchers.Default) {
    queries.selectLogForDate(habitId, date.toString()).executeAsOneOrNull()?.count?.toInt() ?: 0
  }

  override suspend fun setCount(habitId: Long, date: LocalDate, count: Int): Unit = withContext(Dispatchers.Default) {
    queries.upsertLog(habitId = habitId, date = date.toString(), count = count.coerceAtLeast(0).toLong())
  }
}

private fun HabitLogEntity.toDomain(): HabitLog = HabitLog(
  habitId = habitId,
  date = LocalDate.parse(date),
  count = count.toInt(),
)
