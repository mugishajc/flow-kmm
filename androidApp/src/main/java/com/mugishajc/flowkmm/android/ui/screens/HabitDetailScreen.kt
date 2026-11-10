package com.mugishajc.flowkmm.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mugishajc.flowkmm.domain.model.Habit
import com.mugishajc.flowkmm.domain.model.HabitEntry
import com.mugishajc.flowkmm.domain.repository.HabitRepository
import com.mugishajc.flowkmm.domain.usecase.RecordEntryUseCase
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habitId: String,
    onBack: () -> Unit,
    onStatsClick: () -> Unit,
    repository: HabitRepository = koinInject(),
    recordEntry: RecordEntryUseCase = koinInject(),
) {
    val entries by repository.getEntriesForHabit(habitId).collectAsState(initial = emptyList())
    var habit by remember { mutableStateOf<Habit?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(habitId) {
        habit = repository.getHabitById(habitId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(habit?.name ?: "Habit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = onStatsClick) {
                        Text("Stats")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { scope.launch { recordEntry(habitId) } },
            ) {
                Text("+", style = MaterialTheme.typography.headlineSmall)
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
        ) {
            habit?.let { h ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(h.name, style = MaterialTheme.typography.headlineSmall)
                        if (h.description.isNotBlank()) {
                            Text(h.description, style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Frequency: ${h.frequency.displayName()} | Target: ${h.targetCount}",
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Recent Entries", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (entries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("No entries yet. Tap + to record one.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(entries) { entry ->
                        EntryRow(entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun EntryRow(entry: HabitEntry) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(entry.date, style = MaterialTheme.typography.bodyMedium)
                entry.note?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(
                "x${entry.count}",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}
