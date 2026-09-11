package com.squigo.android.di

import android.content.Context
import com.squigo.android.widget.AndroidWidgetRefresher
import com.squigo.core.domain.habit.WidgetRefresher
import org.koin.dsl.module

val androidWidgetModule = module {
  single<WidgetRefresher> { AndroidWidgetRefresher(get<Context>()) }
}
