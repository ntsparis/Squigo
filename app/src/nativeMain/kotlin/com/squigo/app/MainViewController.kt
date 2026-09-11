package com.squigo.app

import androidx.compose.ui.window.ComposeUIViewController
import com.squigo.core.domain.habit.WidgetRefresher
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

// No home-screen widget on iOS yet, so app-side mutations have nothing to refresh.
private val noWidgetModule = module {
  single<WidgetRefresher> { WidgetRefresher {} }
}

fun MainViewController() = ComposeUIViewController(
  configure = {
    if (KoinPlatform.getKoinOrNull() == null) {
      startKoin { modules(com.squigo.app.di.appModule, noWidgetModule) }
    }
  },
) {
  App()
}
