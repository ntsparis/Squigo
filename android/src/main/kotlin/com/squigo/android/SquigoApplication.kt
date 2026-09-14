package com.squigo.android

import android.app.Application
import com.squigo.android.di.androidWidgetModule
import com.squigo.android.widget.MidnightWidgetRefreshWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SquigoApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidContext(this@SquigoApplication)
      modules(com.squigo.app.di.appModule, androidWidgetModule)
    }
    MidnightWidgetRefreshWorker.schedule(this)
  }
}
