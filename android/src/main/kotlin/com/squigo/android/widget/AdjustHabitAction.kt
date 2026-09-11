package com.squigo.android.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.squigo.core.domain.habit.LogHabitUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * The +/- stepper interaction for COUNTER habits (e.g. glasses of water), from the widget.
 * [LogHabitUseCase] refreshes the widget itself after writing, so the same use case
 * keeps app and widget in sync from either side.
 */
class AdjustHabitAction : ActionCallback, KoinComponent {

  private val logHabit: LogHabitUseCase by inject()

  override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
    val habitId = parameters[HABIT_ID_KEY] ?: return
    val delta = parameters[DELTA_KEY] ?: return
    logHabit.adjust(habitId, delta)
  }
}
