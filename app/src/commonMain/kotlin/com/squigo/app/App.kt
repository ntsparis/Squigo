package com.squigo.app

import androidx.compose.runtime.Composable
import com.squigo.app.navigation.SquigoNavHost
import com.squigo.core.designsystem.theme.SquigoTheme

@Composable
fun App() {
  SquigoTheme {
    SquigoNavHost()
  }
}
