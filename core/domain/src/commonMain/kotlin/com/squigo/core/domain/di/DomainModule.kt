package com.squigo.core.domain.di

import com.squigo.core.domain.habit.ArchiveHabitUseCase
import com.squigo.core.domain.habit.CreateHabitUseCase
import com.squigo.core.domain.habit.DeleteHabitUseCase
import com.squigo.core.domain.habit.ObserveHabitWithHistoryUseCase
import com.squigo.core.domain.habit.ObserveHabitsWithHistoryUseCase
import com.squigo.core.domain.habit.LogHabitUseCase
import com.squigo.core.domain.habit.UpdateHabitUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val domainModule = module {
  single<Clock> { Clock.System }

  factoryOf(::ObserveHabitsWithHistoryUseCase)
  factoryOf(::ObserveHabitWithHistoryUseCase)
  factoryOf(::CreateHabitUseCase)
  factoryOf(::UpdateHabitUseCase)
  factoryOf(::ArchiveHabitUseCase)
  factoryOf(::DeleteHabitUseCase)
  factoryOf(::LogHabitUseCase)
}
