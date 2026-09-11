package com.squigo.android.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.squigo.android.MainActivity
import com.squigo.core.designsystem.theme.toComposeColor
import com.squigo.core.domain.habit.ObserveHabitsWithHistoryUseCase
import com.squigo.core.model.HabitType
import com.squigo.core.model.HabitWithHistory
import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal val HABIT_ID_KEY = androidx.glance.action.ActionParameters.Key<Long>("habitId")
internal val DELTA_KEY = androidx.glance.action.ActionParameters.Key<Int>("delta")

private val WIDGET_BACKGROUND = Color(0x33FAFAFC)
private val CARD_CORNER = 20.dp

class HabitWidget : GlanceAppWidget(), KoinComponent {

  private val observeHabitsWithHistory: ObserveHabitsWithHistoryUseCase by inject()

  override suspend fun provideGlance(context: Context, id: GlanceId) {
    val habitsFlow = observeHabitsWithHistory()
    val initialHabits = habitsFlow.first()
    val openAppAction = actionStartActivity(Intent(context, MainActivity::class.java))

    // Glance re-runs only the composition (not provideGlance) when a live session is
    // updated, so the data must be observed inside provideContent or it goes stale.
    provideContent {
      val habits by habitsFlow.collectAsState(initialHabits)
      SquigoWidgetContent(habits, openAppAction)
    }
  }
}

@Composable
private fun SquigoWidgetContent(
  habits: List<HabitWithHistory>,
  openAppAction: androidx.glance.action.Action
) {
  Box(
    modifier = GlanceModifier
      .fillMaxSize()
      .appWidgetBackground()
      .cornerRadius(CARD_CORNER)
      .background(WIDGET_BACKGROUND)
      .clickable(openAppAction),
  ) {
    if (habits.isEmpty()) {
      EmptyWidgetContent()
    } else {
      // Deliberately a plain Column, not a LazyColumn: Glance's LazyColumn is backed by
      // an Android RemoteViews collection adapter (like a ListView), which needs
      // AppWidgetManager.notifyAppWidgetViewDataChanged() to refresh after the
      // underlying data changes - Glance doesn't reliably trigger that, so the visible
      // list can silently go stale even though the DB write succeeded and updateAll()
      // ran. A plain Column's children are ordinary views that get fully redrawn on
      // every provideGlance() call, so updateAll() always shows the true state. This
      // widget only ever holds a handful of habits, so losing scroll is a fine trade.
      Column(modifier = GlanceModifier.fillMaxSize().padding(12.dp)) {
        habits.forEachIndexed { index, habitWithHistory ->
          HabitWidgetRow(habitWithHistory = habitWithHistory, openAppAction = openAppAction)
          if (index != habits.lastIndex) {
            Spacer(modifier = GlanceModifier.height(14.dp))
          }
        }
      }
    }
  }
}

@Composable
private fun EmptyWidgetContent() {
  Box(
    modifier = GlanceModifier.fillMaxSize().padding(16.dp),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = "Create a habit in Squigo to see it here",
      style = TextStyle(fontSize = 13.sp),
    )
  }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun HabitWidgetRow(
  habitWithHistory: HabitWithHistory,
  openAppAction: androidx.glance.action.Action,
  modifier: GlanceModifier = GlanceModifier,
) {
  val habit = habitWithHistory.habit
  val accentColor = habit.color.toComposeColor()
  val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
  val count = habitWithHistory.countOn(today)
  val done = count >= habit.targetCount

  // The "open app" tap target and the check/stepper's own tap target must never
  // overlap: Glance/RemoteViews doesn't reliably let a nested clickable win over a
  // parent's clickable covering the same pixels, so a single clickable spanning the
  // whole row (as this used to be) would non-deterministically eat taps meant for the
  // check/stepper control instead of running its action. Only the avatar/name/streak
  // portion opens the app; the control at the end keeps an exclusive tap target.
  Row(
    modifier = modifier
      .fillMaxWidth()
      .cornerRadius(16.dp)
      .background(accentColor.copy(alpha = 0.73f))
      .padding(horizontal = 14.dp, vertical = 14.dp),
    verticalAlignment = Alignment.Vertical.CenterVertically,
  ) {
    Row(
      modifier = GlanceModifier.defaultWeight().clickable(openAppAction),
      verticalAlignment = Alignment.Vertical.CenterVertically,
    ) {
      Box(
        modifier = GlanceModifier
          .size(28.dp)
          .cornerRadius(14.dp)
          .background(accentColor.copy(alpha = 0.82f)),
        contentAlignment = Alignment.Center,
      ) {
        Text(text = habit.emoji ?: "•", style = TextStyle(fontSize = 14.sp))
      }

      Spacer(modifier = GlanceModifier.width(10.dp))

      Column(modifier = GlanceModifier.defaultWeight()) {
        Text(
          text = habit.name,
          style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
          maxLines = 1,
        )
        Text(
          text = if (habitWithHistory.currentStreak > 0) "🔥 ${habitWithHistory.currentStreak}d" else "No streak yet",
          style = TextStyle(fontSize = 11.sp),
        )
      }
    }

    Spacer(modifier = GlanceModifier.width(8.dp))

    when (habit.type) {
      HabitType.CHECK -> Box(
        modifier = GlanceModifier
          .size(32.dp)
          .cornerRadius(16.dp)
          .background(if (done) accentColor else Color(0x33000000))
          .clickable(
            actionRunCallback<ToggleHabitAction>(actionParametersOf(HABIT_ID_KEY to habit.id)),
          ),
        contentAlignment = Alignment.Center,
      ) {
        if (done) {
          Text(
            text = "✓",
            style = TextStyle(
              fontSize = 16.sp,
              color = androidx.glance.unit.ColorProvider(Color.White)
            )
          )
        }
      }

      HabitType.COUNTER -> Row(verticalAlignment = Alignment.Vertical.CenterVertically) {
        StepperButton(
          label = "-",
          onClick = actionRunCallback<AdjustHabitAction>(
            actionParametersOf(HABIT_ID_KEY to habit.id, DELTA_KEY to -1),
          ),
        )
        Text(
          text = "$count/${habit.targetCount}",
          style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold),
          modifier = GlanceModifier.padding(horizontal = 6.dp),
        )
        StepperButton(
          label = "+",
          onClick = actionRunCallback<AdjustHabitAction>(
            actionParametersOf(HABIT_ID_KEY to habit.id, DELTA_KEY to 1),
          ),
        )
      }
    }
  }
}

@Composable
private fun StepperButton(label: String, onClick: androidx.glance.action.Action) {
  Box(
    modifier = GlanceModifier
      .size(26.dp)
      .cornerRadius(13.dp)
      .background(Color(0x33000000))
      .clickable(onClick),
    contentAlignment = Alignment.Center,
  ) {
    Text(text = label, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
  }
}

class HabitWidgetReceiver : GlanceAppWidgetReceiver() {
  override val glanceAppWidget: GlanceAppWidget = HabitWidget()
}
