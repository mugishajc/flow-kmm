package com.mugishajc.flowkmm.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mugishajc.flowkmm.domain.model.HabitStats
import com.mugishajc.flowkmm.domain.usecase.GetStatsUseCase
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitStatsScreen(
    habitId: String,
    onBack: () -> Unit,
    getStats: GetStatsUseCase = koinInject(),
) {
    var stats by remember { mutableStateOf<HabitStats?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(habitId) {
        getStats(habitId).fold(
            onSuccess = { stats = it },
            onFailure = { error = it.message },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stats?.habitName ?: "Stats") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            stats?.let { s ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    StatCard("Current Streak", "${s.currentStreak} days")
                    StatCard("Longest Streak", "${s.longestStreak} days")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    StatCard("Total Entries", "${s.totalEntries}")
                    StatCard("Completion", "${(s.completionRate * 100).toInt()}%")
                }

                Spacer(Modifier.height(8.dp))
                Text("Recent Activity", style = MaterialTheme.typography.titleMedium)

                s.recentEntries.forEach { entry ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(entry.date)
                            Text("x${entry.count}", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}
