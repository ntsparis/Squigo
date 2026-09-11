package com.squigo.feature.habits

import com.squigo.core.model.HabitColor
import com.squigo.core.model.HabitType

sealed interface HabitsAction {
  data class ToggleToday(val habitId: Long) : HabitsAction
  data class AdjustToday(val habitId: Long, val delta: Int) : HabitsAction
  data class OpenHabit(val habitId: Long) : HabitsAction
  data class SetArchived(val habitId: Long, val archived: Boolean) : HabitsAction
  data class Delete(val habitId: Long) : HabitsAction

  data object OpenCreateEditor : HabitsAction
  data class OpenEditEditor(val habitId: Long) : HabitsAction
  data object DismissEditor : HabitsAction
  data class EditorNameChanged(val name: String) : HabitsAction
  data class EditorEmojiChanged(val emoji: String?) : HabitsAction
  data class EditorColorChanged(val color: HabitColor) : HabitsAction
  data class EditorTypeChanged(val type: HabitType) : HabitsAction
  data class EditorTargetCountChanged(val targetCount: String) : HabitsAction
  data class EditorUnitLabelChanged(val unitLabel: String) : HabitsAction
  data object SaveEditor : HabitsAction
}
