package com.squigo.core.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.squigo.core.model.HabitType

/**
 * The single tap-to-toggle (CHECK) or +/- stepper (COUNTER) control shared by the
 * habit list, the detail screen and (conceptually) the widget's own interaction.
 */
@Composable
fun HabitProgressControl(
  type: HabitType,
  count: Int,
  targetCount: Int,
  accentColor: Color,
  unitLabel: String?,
  onToggle: () -> Unit,
  onIncrement: () -> Unit,
  onDecrement: () -> Unit,
  modifier: Modifier = Modifier,
) {
  when (type) {
    HabitType.CHECK -> CheckControl(count >= targetCount, accentColor, onToggle, modifier)
    HabitType.COUNTER -> CounterControl(count, targetCount, unitLabel, accentColor, onIncrement, onDecrement, modifier)
  }
}

@Composable
private fun CheckControl(done: Boolean, accentColor: Color, onToggle: () -> Unit, modifier: Modifier = Modifier) {
  val background by animateColorAsState(
    targetValue = if (done) accentColor else MaterialTheme.colorScheme.surfaceVariant,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "habitCheckBackground",
  )
  androidx.compose.foundation.layout.Box(
    modifier = modifier
      .size(36.dp)
      .background(background, CircleShape)
      .clickable(onClick = onToggle),
    contentAlignment = Alignment.Center,
  ) {
    androidx.compose.animation.AnimatedVisibility(
      visible = done,
      enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
      exit = scaleOut(),
    ) {
      Icon(imageVector = Icons.Filled.Check, contentDescription = "Done today", tint = Color.White)
    }
  }
}

@Composable
private fun CounterControl(
  count: Int,
  targetCount: Int,
  unitLabel: String?,
  accentColor: Color,
  onIncrement: () -> Unit,
  onDecrement: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    IconButton(onClick = onDecrement, modifier = Modifier.size(32.dp)) {
      Icon(imageVector = Icons.Filled.Remove, contentDescription = "Decrease")
    }
    AnimatedContent(targetState = count, label = "habitCount") { animatedCount ->
      Text(
        text = if (unitLabel != null) "$animatedCount/$targetCount $unitLabel" else "$animatedCount/$targetCount",
        color = if (animatedCount >= targetCount) accentColor else MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.labelLarge,
      )
    }
    IconButton(onClick = onIncrement, modifier = Modifier.size(32.dp)) {
      Icon(imageVector = Icons.Filled.Add, contentDescription = "Increase")
    }
  }
}
