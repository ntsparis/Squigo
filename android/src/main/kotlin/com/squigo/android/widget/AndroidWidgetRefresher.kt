package com.squigo.android.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.squigo.core.domain.habit.WidgetRefresher

/** Pushes an app-side habit mutation out to any placed Squigo widgets. */
class AndroidWidgetRefresher(private val context: Context) : WidgetRefresher {
  override suspend fun refreshAll() {
    HabitWidget().updateAll(context)
  }
}
