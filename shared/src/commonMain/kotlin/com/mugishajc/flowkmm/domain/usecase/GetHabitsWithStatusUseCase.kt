package com.mugishajc.flowkmm.domain.usecase

import com.mugishajc.flowkmm.domain.model.HabitWithEntries
import com.mugishajc.flowkmm.domain.repository.HabitRepository
import com.mugishajc.flowkmm.util.todayIsoString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetHabitsWithStatusUseCase(
    private val repository: HabitRepository,
) {
    operator fun invoke(): Flow<List<HabitWithEntries>> {
        val today = todayIsoString()
        return repository.getAllHabits().map { habits ->
            habits.filter { !it.archived }.map { habit ->
                val todayEntries = repository.getEntriesForDate(habit.id, today)
                val totalToday = todayEntries.sumOf { it.count }
                HabitWithEntries(
                    habit = habit,
                    todayEntries = todayEntries,
                    isCompletedToday = totalToday >= habit.targetCount,
                )
            }
        }
    }
}
