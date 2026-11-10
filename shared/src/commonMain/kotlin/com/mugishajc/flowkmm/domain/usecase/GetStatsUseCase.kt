package com.mugishajc.flowkmm.domain.usecase

import com.mugishajc.flowkmm.domain.model.HabitEntry
import com.mugishajc.flowkmm.domain.model.HabitFrequency
import com.mugishajc.flowkmm.domain.model.HabitStats
import com.mugishajc.flowkmm.domain.repository.HabitRepository
import kotlinx.coroutines.flow.first

class GetStatsUseCase(
    private val repository: HabitRepository,
) {
    suspend operator fun invoke(habitId: String): Result<HabitStats> {
        val habit = repository.getHabitById(habitId)
            ?: return Result.failure(IllegalArgumentException("Habit not found: $habitId"))

        val entries = repository.getEntriesForHabit(habitId).first()
        val sortedEntries = entries.sortedByDescending { it.date }

        val entriesByDate = entries.groupBy { it.date }
        val completedDates = entriesByDate
            .filter { (_, dayEntries) -> dayEntries.sumOf { it.count } >= habit.targetCount }
            .keys
            .sorted()

        val streaks = calculateStreaks(completedDates.toList(), habit.frequency)

        val daysSinceCreation = maxOf(1, entriesByDate.size)
        val completionRate = if (daysSinceCreation > 0) {
            completedDates.size.toDouble() / daysSinceCreation
        } else {
            0.0
        }

        return Result.success(
            HabitStats(
                habitId = habitId,
                habitName = habit.name,
                totalEntries = entries.size,
                currentStreak = streaks.current,
                longestStreak = streaks.longest,
                completionRate = completionRate.coerceIn(0.0, 1.0),
                recentEntries = sortedEntries.take(10),
            )
        )
    }

    private fun calculateStreaks(
        sortedDates: List<String>,
        frequency: HabitFrequency,
    ): StreakResult {
        if (sortedDates.isEmpty()) return StreakResult(0, 0)

        var currentStreak = 1
        var longestStreak = 1
        var tempStreak = 1

        for (i in 1 until sortedDates.size) {
            if (areDatesConsecutive(sortedDates[i - 1], sortedDates[i], frequency)) {
                tempStreak++
                longestStreak = maxOf(longestStreak, tempStreak)
            } else {
                tempStreak = 1
            }
        }

        // Current streak counts backwards from the last entry
        currentStreak = 1
        for (i in sortedDates.size - 1 downTo 1) {
            if (areDatesConsecutive(sortedDates[i - 1], sortedDates[i], frequency)) {
                currentStreak++
            } else {
                break
            }
        }

        return StreakResult(currentStreak, maxOf(longestStreak, currentStreak))
    }

    private fun areDatesConsecutive(
        earlier: String,
        later: String,
        frequency: HabitFrequency,
    ): Boolean {
        // Simple date comparison: dates are ISO strings (YYYY-MM-DD)
        // For daily, consecutive means exactly 1 day apart
        // For weekly/monthly we relax the constraint
        val daysBetween = approximateDaysBetween(earlier, later)
        return when (frequency) {
            HabitFrequency.DAILY -> daysBetween == 1
            HabitFrequency.WEEKLY -> daysBetween in 1..7
            HabitFrequency.MONTHLY -> daysBetween in 1..31
        }
    }

    private fun approximateDaysBetween(earlier: String, later: String): Int {
        // Parse YYYY-MM-DD and compute approximate difference
        return try {
            val (y1, m1, d1) = earlier.split("-").map { it.toInt() }
            val (y2, m2, d2) = later.split("-").map { it.toInt() }
            (y2 - y1) * 365 + (m2 - m1) * 30 + (d2 - d1)
        } catch (_: Exception) {
            Int.MAX_VALUE
        }
    }

    private data class StreakResult(val current: Int, val longest: Int)
}
