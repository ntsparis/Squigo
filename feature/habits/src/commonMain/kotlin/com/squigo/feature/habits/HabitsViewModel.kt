package com.squigo.feature.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squigo.core.domain.habit.ArchiveHabitUseCase
import com.squigo.core.domain.habit.CreateHabitUseCase
import com.squigo.core.domain.habit.DeleteHabitUseCase
import com.squigo.core.domain.habit.LogHabitUseCase
import com.squigo.core.domain.habit.ObserveHabitsWithHistoryUseCase
import com.squigo.core.domain.habit.UpdateHabitUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HabitsViewModel(
  private val observeHabitsWithHistory: ObserveHabitsWithHistoryUseCase,
  private val createHabit: CreateHabitUseCase,
  private val updateHabit: UpdateHabitUseCase,
  private val archiveHabit: ArchiveHabitUseCase,
  private val deleteHabit: DeleteHabitUseCase,
  private val logHabit: LogHabitUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(HabitsUiState())
  val uiState: StateFlow<HabitsUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      observeHabitsWithHistory().collect { habits ->
        _uiState.update { it.copy(habits = habits, loading = false) }
      }
    }
  }

  fun onAction(action: HabitsAction) {
    when (action) {
      is HabitsAction.ToggleToday -> viewModelScope.launch { logHabit.toggle(action.habitId) }
      is HabitsAction.AdjustToday -> viewModelScope.launch { logHabit.adjust(action.habitId, action.delta) }
      is HabitsAction.SetArchived -> setArchived(action.habitId, action.archived)
      is HabitsAction.Delete -> delete(action.habitId)
      is HabitsAction.OpenHabit -> Unit // handled by navigation in the Screen layer

      HabitsAction.OpenCreateEditor -> _uiState.update { it.copy(editorState = HabitEditorState()) }
      is HabitsAction.OpenEditEditor -> openEditEditor(action.habitId)
      HabitsAction.DismissEditor -> _uiState.update { it.copy(editorState = null) }
      is HabitsAction.EditorNameChanged -> updateEditor { it.copy(name = action.name) }
      is HabitsAction.EditorEmojiChanged -> updateEditor { it.copy(emoji = action.emoji) }
      is HabitsAction.EditorColorChanged -> updateEditor { it.copy(color = action.color) }
      is HabitsAction.EditorTypeChanged -> updateEditor { it.copy(type = action.type) }
      is HabitsAction.EditorTargetCountChanged -> updateEditor { it.copy(targetCount = action.targetCount) }
      is HabitsAction.EditorUnitLabelChanged -> updateEditor { it.copy(unitLabel = action.unitLabel) }
      HabitsAction.SaveEditor -> saveEditor()
    }
  }

  private fun setArchived(habitId: Long, archived: Boolean) {
    viewModelScope.launch { archiveHabit(habitId, archived) }
  }

  private fun delete(habitId: Long) {
    viewModelScope.launch { deleteHabit(habitId) }
  }

  private fun openEditEditor(habitId: Long) {
    val habit = _uiState.value.habits.firstOrNull { it.habit.id == habitId }?.habit ?: return
    _uiState.update {
      it.copy(
        editorState = HabitEditorState(
          habitId = habit.id,
          name = habit.name,
          emoji = habit.emoji,
          color = habit.color,
          type = habit.type,
          targetCount = habit.targetCount.toString(),
          unitLabel = habit.unitLabel.orEmpty(),
        ),
      )
    }
  }

  private fun updateEditor(transform: (HabitEditorState) -> HabitEditorState) {
    _uiState.update { state ->
      state.editorState?.let { state.copy(editorState = transform(it)) } ?: state
    }
  }

  private fun saveEditor() {
    val editor = _uiState.value.editorState ?: return
    val targetCount = if (editor.type == com.squigo.core.model.HabitType.CHECK) 1 else editor.targetCountValue
    if (!editor.canSave || targetCount == null) return

    viewModelScope.launch {
      val existingId = editor.habitId
      val unitLabel = editor.unitLabel.trim().takeIf { it.isNotEmpty() }
      if (existingId == null) {
        createHabit(
          name = editor.name,
          emoji = editor.emoji,
          color = editor.color,
          type = editor.type,
          targetCount = targetCount,
          unitLabel = unitLabel,
        )
      } else {
        val existing = _uiState.value.habits.first { it.habit.id == existingId }.habit
        updateHabit(
          existing.copy(
            name = editor.name,
            emoji = editor.emoji,
            color = editor.color,
            type = editor.type,
            targetCount = targetCount,
            unitLabel = unitLabel,
          ),
        )
      }
      _uiState.update { it.copy(editorState = null) }
    }
  }
}
