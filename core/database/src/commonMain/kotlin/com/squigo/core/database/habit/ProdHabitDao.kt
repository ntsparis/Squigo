package com.squigo.core.database.habit

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.squigo.core.database.HabitEntity
import com.squigo.core.database.SquigoDatabase
import com.squigo.core.model.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProdHabitDao(private val database: SquigoDatabase) : HabitDao {

  private val queries = database.habitQueries

  override fun observeActiveHabits(): Flow<List<Habit>> = queries
    .selectActiveHabits()
    .asFlow()
    .mapToList(Dispatchers.Default)
    .map { it.map(HabitEntity::toDomain) }

  override fun observeAllHabits(): Flow<List<Habit>> = queries
    .selectAllHabits()
    .asFlow()
    .mapToList(Dispatchers.Default)
    .map { it.map(HabitEntity::toDomain) }

  override suspend fun getHabit(id: Long): Habit? = withContext(Dispatchers.Default) {
    queries.selectHabitById(id).executeAsOneOrNull()?.toDomain()
  }

  override suspend fun insertHabit(habit: Habit): Long = withContext(Dispatchers.Default) {
    queries.transactionWithResult {
      queries.insertHabit(
        name = habit.name,
        emoji = habit.emoji,
        color = habit.color.name,
        createdAt = habit.createdAt.toString(),
        archived = if (habit.archived) 1L else 0L,
        sortOrder = 0,
        type = habit.type.name,
        targetCount = habit.targetCount.toLong(),
        unitLabel = habit.unitLabel,
      )
      queries.lastInsertRowId().executeAsOne()
    }
  }

  override suspend fun updateHabit(habit: Habit): Unit = withContext(Dispatchers.Default) {
    queries.updateHabit(
      name = habit.name,
      emoji = habit.emoji,
      color = habit.color.name,
      type = habit.type.name,
      targetCount = habit.targetCount.toLong(),
      unitLabel = habit.unitLabel,
      id = habit.id,
    )
  }

  override suspend fun setArchived(id: Long, archived: Boolean): Unit = withContext(Dispatchers.Default) {
    queries.setArchived(archived = if (archived) 1L else 0L, id = id)
  }

  override suspend fun deleteHabit(id: Long): Unit = withContext(Dispatchers.Default) {
    queries.deleteHabit(id)
  }
}
