package com.squigo.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StreakBadge(streak: Int, modifier: Modifier = Modifier) {
  Row(modifier = modifier.padding(vertical = 2.dp)) {
    val label = when {
      streak <= 0 -> "No streak yet"
      streak == 1 -> "1 day streak"
      else -> "$streak day streak"
    }
    Text(
      text = if (streak > 0) "🔥 $label" else label,
      style = MaterialTheme.typography.labelMedium,
    )
  }
}
