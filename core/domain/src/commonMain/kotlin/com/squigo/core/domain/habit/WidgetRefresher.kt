package com.squigo.core.domain.habit

/**
 * Tells the home-screen widget(s) to redraw after an app-side mutation, so a habit
 * logged from the app shows up on the widget without needing another widget-side tap.
 *
 * The reverse direction (widget -> app) needs no wiring: the widget mutates the same
 * on-device database the app's reactive queries observe, in the same process, so the
 * app's Flows pick up the change automatically.
 */
fun interface WidgetRefresher {
  suspend fun refreshAll()
}
