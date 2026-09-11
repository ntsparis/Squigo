package com.squigo.core.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.squigo.core.database.SquigoDatabase
import org.koin.dsl.module

actual val sqlDriverModule: org.koin.core.module.Module = module {
  single<SqlDriver> {
    NativeSqliteDriver(
      schema = SquigoDatabase.Schema,
      name = "squigo.db",
    )
  }
}
