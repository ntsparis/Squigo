package com.squigo.core.domain.habit

import com.squigo.core.model.Habit
import com.squigo.core.model.HabitWithHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class, ExperimentalTime::class)
class ObserveHabitsWithHistoryUseCase(
  private val repository: HabitRepository,
  private val clock: Clock = Clock.System,
) {

  operator fun invoke(includeArchived: Boolean = false): Flow<List<HabitWithHistory>> {
    val habitsFlow = if (includeArchived) {
      repository.observeAllHabits()
    } else {
      repository.observeActiveHabits()
    }

    return habitsFlow.flatMapLatest { habits ->
      if (habits.isEmpty()) {
        flowOf(emptyList())
      } else {
        val logFlows = habits.map { habit -> repository.observeLogsForHabit(habit.id) }
        combine(logFlows) { logsPerHabit ->
          val today = clock.todayIn(TimeZone.currentSystemDefault())
          habits.mapIndexed { index, habit ->
            val counts = logsPerHabit[index].associate { it.date to it.count }
            habit.toHabitWithHistory(counts, today)
          }
        }
      }
    }
  }

  private fun Habit.toHabitWithHistory(counts: Map<LocalDate, Int>, today: LocalDate): HabitWithHistory {
    val doneDates = counts.filterValues { it >= targetCount }.keys
    return HabitWithHistory(
      habit = this,
      counts = counts,
      currentStreak = StreakCalculator.currentStreak(doneDates, today),
      longestStreak = StreakCalculator.longestStreak(doneDates),
    )
  }
}
