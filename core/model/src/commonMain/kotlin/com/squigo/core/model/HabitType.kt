package com.squigo.core.model

/**
 * CHECK habits toggle done/not-done for the day (target is always 1).
 * COUNTER habits track a running count against [Habit.targetCount] (e.g. 8 glasses of water),
 * incremented/decremented one step at a time from the app or the widget.
 */
enum class HabitType {
  CHECK,
  COUNTER,
}
