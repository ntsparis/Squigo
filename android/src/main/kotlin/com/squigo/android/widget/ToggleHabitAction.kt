package com.squigo.android.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.squigo.core.domain.habit.LogHabitUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * The core Squigo interaction: toggling a habit's "done today" straight from the
 * widget, with no app launch required. [LogHabitUseCase] refreshes the widget itself
 * after writing, so the same use case keeps app and widget in sync from either side.
 */
class ToggleHabitAction : ActionCallback, KoinComponent {

  private val logHabit: LogHabitUseCase by inject()

  override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
    val habitId = parameters[HABIT_ID_KEY] ?: return
    logHabit.toggle(habitId)
  }
}
