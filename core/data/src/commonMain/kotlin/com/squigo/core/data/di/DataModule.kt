package com.squigo.core.data.di

import com.squigo.core.data.habit.ProdHabitRepository
import com.squigo.core.database.di.databaseModule
import com.squigo.core.domain.di.domainModule
import com.squigo.core.domain.habit.HabitRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
  includes(databaseModule, domainModule)

  singleOf(::ProdHabitRepository).bind<HabitRepository>()
}
