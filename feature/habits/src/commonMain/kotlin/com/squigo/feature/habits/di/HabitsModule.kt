package com.squigo.feature.habits.di

import com.squigo.feature.habits.HabitsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val habitsModule = module {
  viewModelOf(::HabitsViewModel)
}
