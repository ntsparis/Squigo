package com.squigo.feature.habitdetail.di

import com.squigo.feature.habitdetail.HabitDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val habitDetailModule = module {
  viewModel { params -> HabitDetailViewModel(habitId = params.get(), observeHabitWithHistory = get(), logHabit = get()) }
}
