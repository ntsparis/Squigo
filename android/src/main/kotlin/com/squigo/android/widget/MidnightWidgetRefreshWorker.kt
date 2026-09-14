package com.squigo.android.widget

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.squigo.core.domain.habit.WidgetRefresher
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Redraws the widget shortly after local midnight so a CHECK habit's tick and a
 * COUNTER's progress visibly reset to the new day. Nothing else does this: Glance's
 * habits flow only re-emits on a DB write, and the date rolling over isn't one - so
 * without this, a habit stays looking "done" until the next unrelated tap or app open.
 */
class MidnightWidgetRefreshWorker(
  context: Context,
  params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

  private val widgetRefresher: WidgetRefresher by inject()

  override suspend fun doWork(): Result {
    widgetRefresher.refreshAll()
    schedule(applicationContext)
    return Result.success()
  }

  companion object {
    private const val UNIQUE_WORK_NAME = "midnight-widget-refresh"
    private val POST_MIDNIGHT_MARGIN = DateTimeUnit.SECOND

    @OptIn(ExperimentalTime::class)
    fun schedule(context: Context) {
      val zone = TimeZone.currentSystemDefault()
      val now = Clock.System.now()
      val today = now.toLocalDateTime(zone).date
      val nextMidnight = today.plus(1, DateTimeUnit.DAY).atStartOfDayIn(zone)
      val fireAt: Instant = nextMidnight.plus(1, POST_MIDNIGHT_MARGIN)
      val delayMillis = (fireAt - now).inWholeMilliseconds.coerceAtLeast(0)

      val request = OneTimeWorkRequestBuilder<MidnightWidgetRefreshWorker>()
        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
        .build()

      WorkManager.getInstance(context).enqueueUniqueWork(
        UNIQUE_WORK_NAME,
        ExistingWorkPolicy.REPLACE,
        request,
      )
    }
  }
}
