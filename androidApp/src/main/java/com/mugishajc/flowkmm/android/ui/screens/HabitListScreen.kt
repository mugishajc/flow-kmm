package com.mugishajc.flowkmm.android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mugishajc.flowkmm.domain.model.HabitFrequency
import com.mugishajc.flowkmm.domain.model.HabitWithEntries
import com.mugishajc.flowkmm.domain.usecase.CreateHabitUseCase
import com.mugishajc.flowkmm.domain.usecase.GetHabitsWithStatusUseCase
import com.mugishajc.flowkmm.domain.usecase.RecordEntryUseCase
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    onHabitClick: (String) -> Unit,
    onStatsClick: (String) -> Unit,
    getHabitsWithStatus: GetHabitsWithStatusUseCase = koinInject(),
    createHabit: CreateHabitUseCase = koinInject(),
    recordEntry: RecordEntryUseCase = koinInject(),
) {
    val habits by getHabitsWithStatus().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flow KMM") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Text("+", style = MaterialTheme.typography.headlineSmall)
            }
        },
    ) { padding ->
        if (habits.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text("No habits yet. Tap + to create one.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(habits) { item ->
                    HabitCard(
                        item = item,
                        onClick = { onHabitClick(item.habit.id) },
                        onComplete = {
                            scope.launch { recordEntry(item.habit.id) }
                        },
                        onStatsClick = { onStatsClick(item.habit.id) },
                    )
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateHabitDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name, description, frequency ->
                scope.launch {
                    createHabit(name, description, frequency)
                    showCreateDialog = false
                }
            },
        )
    }
}

@Composable
private fun HabitCard(
    item: HabitWithEntries,
    onClick: () -> Unit,
    onComplete: () -> Unit,
    onStatsClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCompletedToday) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.habit.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                if (item.habit.description.isNotBlank()) {
                    Text(
                        text = item.habit.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = "${item.todayEntries.sumOf { it.count }}/${item.habit.targetCount} ${item.habit.frequency.displayName()}",
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            TextButton(onClick = onStatsClick) {
                Text("Stats")
            }

            if (!item.isCompletedToday) {
                Button(onClick = onComplete) {
                    Text("Done")
                }
            } else {
                Text(
                    "Completed",
                    color = Color(0xFF2E7D32),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Composable
private fun CreateHabitDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, HabitFrequency) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf(HabitFrequency.DAILY) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Habit") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HabitFrequency.entries.forEach { freq ->
                        FilterChip(
                            selected = frequency == freq,
                            onClick = { frequency = freq },
                            label = { Text(freq.displayName()) },
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onCreate(name, description, frequency) },
            ) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
