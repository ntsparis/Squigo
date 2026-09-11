package com.squigo.feature.habits.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.squigo.core.designsystem.theme.toComposeColor
import com.squigo.core.model.HabitColor
import com.squigo.core.model.HabitType
import com.squigo.core.ui.HabitAvatar
import com.squigo.feature.habits.HabitEditorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitEditorSheet(
  state: HabitEditorState,
  onDismiss: () -> Unit,
  onNameChanged: (String) -> Unit,
  onEmojiChanged: (String?) -> Unit,
  onColorChanged: (HabitColor) -> Unit,
  onTypeChanged: (HabitType) -> Unit,
  onTargetCountChanged: (String) -> Unit,
  onUnitLabelChanged: (String) -> Unit,
  onSave: () -> Unit,
) {
  ModalBottomSheet(onDismissRequest = onDismiss) {
    Column(modifier = Modifier.padding(24.dp)) {
      Text(text = if (state.isEditing) "Edit habit" else "New habit")

      Spacer(modifier = Modifier.height(16.dp))

      Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        HabitAvatar(emoji = state.emoji, color = state.color.toComposeColor())

        Spacer(modifier = Modifier.width(12.dp))

        OutlinedTextField(
          value = state.emoji.orEmpty(),
          onValueChange = { text ->
            // An emoji (incl. skin-tone/ZWJ sequences) can be several UTF-16 chars,
            // so keep the whole grapheme rather than clamping to 1 char - just cap
            // total length as a sanity bound against pasting long strings here.
            onEmojiChanged(text.take(8).ifBlank { null })
          },
          label = { Text("Emoji") },
          placeholder = { Text("🙂") },
          singleLine = true,
          modifier = Modifier.width(96.dp),
        )

        Spacer(modifier = Modifier.width(12.dp))

        OutlinedTextField(
          value = state.name,
          onValueChange = onNameChanged,
          label = { Text("Name") },
          modifier = Modifier.weight(1f),
          singleLine = true,
        )
      }

      // Color, type, and target/unit are one-time decisions made at creation - changing
      // type or target after the fact would make existing history hard to interpret
      // (e.g. a CHECK habit's history re-read as a COUNTER's). Editing only ever
      // touches name and emoji.
      if (!state.isEditing) {
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow {
          items(HabitColor.entries) { color ->
            ColorSwatch(
              color = color.toComposeColor(),
              selected = color == state.color,
              onClick = { onColorChanged(color) },
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
          FilterChip(
            selected = state.type == HabitType.CHECK,
            onClick = { onTypeChanged(HabitType.CHECK) },
            label = { Text("Check off") },
          )
          Spacer(modifier = Modifier.width(8.dp))
          FilterChip(
            selected = state.type == HabitType.COUNTER,
            onClick = { onTypeChanged(HabitType.COUNTER) },
            label = { Text("Counter") },
          )
        }

        AnimatedVisibility(visible = state.type == HabitType.COUNTER) {
          Column {
            Spacer(modifier = Modifier.height(16.dp))
            Row {
              OutlinedTextField(
                value = state.targetCount,
                onValueChange = onTargetCountChanged,
                label = { Text("Daily target") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true,
              )
              Spacer(modifier = Modifier.width(8.dp))
              OutlinedTextField(
                value = state.unitLabel,
                onValueChange = onUnitLabelChanged,
                label = { Text("Unit (e.g. glasses)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = onSave,
        enabled = state.canSave,
        modifier = Modifier.fillMaxWidth(),
      ) {
        Text(if (state.isEditing) "Save" else "Create habit")
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun ColorSwatch(color: Color, selected: Boolean, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .padding(4.dp)
      .size(36.dp)
      .clip(CircleShape)
      .background(color)
      .clickable(onClick = onClick),
    contentAlignment = androidx.compose.ui.Alignment.Center,
  ) {
    if (selected) {
      Text(text = "✓", color = Color.White)
    }
  }
}
