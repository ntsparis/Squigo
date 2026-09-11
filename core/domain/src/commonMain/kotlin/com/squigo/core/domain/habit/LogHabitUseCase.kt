package com.squigo.core.domain.habit

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * The action behind logging progress on a habit, shared by the in-app screens
 * and the home-screen widget. For a CHECK habit, [delta] is ignored and the day
 * is simply toggled between 0 and [Habit.targetCount]. For a COUNTER habit,
 * [delta] adjusts today's count by one step (e.g. +1/-1 glass of water).
 *
 * Each call runs under [mutex] so a fast second tap (from the app or the widget,
 * while the first tap's DB write + widget redraw is still in flight) waits for the
 * first to fully finish instead of racing it - two overlapping read-modify-write
 * cycles would otherwise let the slower one silently clobber the faster one's result.
 */
@OptIn(ExperimentalTime::class)
class LogHabitUseCase(
  private val repository: HabitRepository,
  private val widgetRefresher: WidgetRefresher,
  private val clock: Clock = Clock.System,
) {
  private val mutex = Mutex()

  suspend fun toggle(habitId: Long, date: LocalDate = today()) {
    mutex.withLock {
      val habit = repository.getHabit(habitId) ?: return
      val current = repository.getCount(habitId, date)
      val newCount = if (current >= habit.targetCount) 0 else habit.targetCount
      repository.setCount(habitId, date, newCount)
    }
    widgetRefresher.refreshAll()
  }

  suspend fun adjust(habitId: Long, delta: Int, date: LocalDate = today()) {
    mutex.withLock {
      val current = repository.getCount(habitId, date)
      repository.setCount(habitId, date, (current + delta).coerceAtLeast(0))
    }
    widgetRefresher.refreshAll()
  }

  private fun today(): LocalDate = clock.todayIn(TimeZone.currentSystemDefault())
}
