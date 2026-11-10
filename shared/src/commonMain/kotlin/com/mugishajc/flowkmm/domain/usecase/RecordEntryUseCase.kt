package com.mugishajc.flowkmm.domain.usecase

import com.mugishajc.flowkmm.domain.model.HabitEntry
import com.mugishajc.flowkmm.domain.repository.HabitRepository
import com.mugishajc.flowkmm.util.generateId
import com.mugishajc.flowkmm.util.todayIsoString

class RecordEntryUseCase(
    private val repository: HabitRepository,
) {
    suspend operator fun invoke(
        habitId: String,
        count: Int = 1,
        note: String? = null,
        date: String = todayIsoString(),
    ): Result<HabitEntry> {
        val habit = repository.getHabitById(habitId)
            ?: return Result.failure(IllegalArgumentException("Habit not found: $habitId"))

        if (count < 1) {
            return Result.failure(IllegalArgumentException("Count must be at least 1"))
        }

        val existingEntries = repository.getEntriesForDate(habitId, date)
        val totalToday = existingEntries.sumOf { it.count } + count

        if (totalToday > habit.targetCount * 3) {
            return Result.failure(
                IllegalStateException("Exceeds reasonable limit for today (${habit.targetCount * 3})")
            )
        }

        val entry = HabitEntry(
            id = generateId(),
            habitId = habitId,
            date = date,
            count = count,
            note = note?.trim(),
        )

        repository.insertEntry(entry)
        return Result.success(entry)
    }
}
