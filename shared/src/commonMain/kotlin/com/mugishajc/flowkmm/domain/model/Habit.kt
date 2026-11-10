package com.mugishajc.flowkmm.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val id: String,
    val name: String,
    val description: String,
    val frequency: HabitFrequency,
    val targetCount: Int = 1,
    val color: String = "#4A90D9",
    val createdAt: String,
    val archived: Boolean = false,
)

@Serializable
enum class HabitFrequency {
    DAILY,
    WEEKLY,
    MONTHLY;

    fun displayName(): String = when (this) {
        DAILY -> "Daily"
        WEEKLY -> "Weekly"
        MONTHLY -> "Monthly"
    }
}

@Serializable
data class HabitEntry(
    val id: String,
    val habitId: String,
    val date: String,
    val count: Int = 1,
    val note: String? = null,
)

data class HabitStats(
    val habitId: String,
    val habitName: String,
    val totalEntries: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val completionRate: Double,
    val recentEntries: List<HabitEntry>,
)

data class HabitWithEntries(
    val habit: Habit,
    val todayEntries: List<HabitEntry>,
    val isCompletedToday: Boolean,
)
