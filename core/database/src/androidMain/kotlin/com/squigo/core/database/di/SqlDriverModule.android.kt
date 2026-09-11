package com.squigo.core.database.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.squigo.core.database.SquigoDatabase
import org.koin.dsl.module

actual val sqlDriverModule: org.koin.core.module.Module = module {
  single<SqlDriver> {
    AndroidSqliteDriver(
      schema = SquigoDatabase.Schema,
      context = get<Context>(),
      name = "squigo.db",
    )
  }
}
