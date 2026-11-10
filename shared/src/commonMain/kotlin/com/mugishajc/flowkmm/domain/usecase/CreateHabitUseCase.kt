package com.mugishajc.flowkmm.domain.usecase

import com.mugishajc.flowkmm.domain.model.Habit
import com.mugishajc.flowkmm.domain.model.HabitFrequency
import com.mugishajc.flowkmm.domain.repository.HabitRepository
import com.mugishajc.flowkmm.util.generateId
import com.mugishajc.flowkmm.util.nowIsoString

class CreateHabitUseCase(
    private val repository: HabitRepository,
) {
    suspend operator fun invoke(
        name: String,
        description: String = "",
        frequency: HabitFrequency = HabitFrequency.DAILY,
        targetCount: Int = 1,
        color: String = "#4A90D9",
    ): Result<Habit> {
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Habit name must not be blank"))
        }
        if (targetCount < 1) {
            return Result.failure(IllegalArgumentException("Target count must be at least 1"))
        }

        val habit = Habit(
            id = generateId(),
            name = name.trim(),
            description = description.trim(),
            frequency = frequency,
            targetCount = targetCount,
            color = color,
            createdAt = nowIsoString(),
        )

        repository.insertHabit(habit)
        return Result.success(habit)
    }
}
