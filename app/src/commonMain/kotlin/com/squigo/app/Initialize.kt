package com.squigo.app

import com.squigo.app.di.appModule
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication

fun initKoin(config: KoinApplication.() -> Unit = {}): KoinApplication = koinApplication {
  config()
  modules(appModule)
}
