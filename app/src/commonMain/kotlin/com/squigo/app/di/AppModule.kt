package com.squigo.app.di

import com.squigo.core.data.di.dataModule
import com.squigo.feature.habitdetail.di.habitDetailModule
import com.squigo.feature.habits.di.habitsModule
import org.koin.dsl.module

val appModule = module {
  includes(dataModule, habitsModule, habitDetailModule)
}
