package com.squigo.core.database.di

import app.cash.sqldelight.db.SqlDriver
import com.squigo.core.database.SquigoDatabase
import com.squigo.core.database.habit.HabitDao
import com.squigo.core.database.habit.HabitLogDao
import com.squigo.core.database.habit.ProdHabitDao
import com.squigo.core.database.habit.ProdHabitLogDao
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
  includes(sqlDriverModule)

  single { SquigoDatabase(get<SqlDriver>()) }

  single { ProdHabitDao(get()) }.bind<HabitDao>()
  single { ProdHabitLogDao(get()) }.bind<HabitLogDao>()
}
