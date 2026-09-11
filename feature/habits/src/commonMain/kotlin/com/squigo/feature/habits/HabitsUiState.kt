package com.squigo.feature.habits

import com.squigo.core.model.HabitColor
import com.squigo.core.model.HabitType
import com.squigo.core.model.HabitWithHistory

data class HabitsUiState(
  val habits: List<HabitWithHistory> = emptyList(),
  val loading: Boolean = true,
  val editorState: HabitEditorState? = null,
) {
  val isEmpty: Boolean get() = !loading && habits.isEmpty()
}

data class HabitEditorState(
  val habitId: Long? = null,
  val name: String = "",
  val emoji: String? = null,
  val color: HabitColor = HabitColor.BLUE,
  val type: HabitType = HabitType.CHECK,
  val targetCount: String = "1",
  val unitLabel: String = "",
) {
  val isEditing: Boolean get() = habitId != null
  val targetCountValue: Int? get() = targetCount.toIntOrNull()?.takeIf { it >= 1 }
  val canSave: Boolean get() = name.isNotBlank() && (type == HabitType.CHECK || targetCountValue != null)
}
