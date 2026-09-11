package com.squigo.core.domain.habit

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/**
 * Streak math lives here so both the habit list, the detail screen and the
 * widget agree on what counts as a "current streak".
 */
object StreakCalculator {

  /**
   * The current streak counts backwards from [today]. A gap on [today] itself
   * doesn't break the streak yet (the user still has until end of day to log it),
   * but a gap on any earlier day does.
   */
  fun currentStreak(doneDates: Set<LocalDate>, today: LocalDate): Int {
    if (doneDates.isEmpty()) return 0

    var streak = 0
    var cursor = today

    if (cursor !in doneDates) {
      cursor = cursor.minus(1, DateTimeUnit.DAY)
    }

    while (cursor in doneDates) {
      streak++
      cursor = cursor.minus(1, DateTimeUnit.DAY)
    }

    return streak
  }

  fun longestStreak(doneDates: Set<LocalDate>): Int {
    if (doneDates.isEmpty()) return 0

    val sorted = doneDates.sorted()
    var longest = 1
    var current = 1

    for (i in 1 until sorted.size) {
      val expectedNext = sorted[i - 1].plus(1, DateTimeUnit.DAY)
      current = if (sorted[i] == expectedNext) current + 1 else 1
      if (current > longest) longest = current
    }

    return longest
  }
}
