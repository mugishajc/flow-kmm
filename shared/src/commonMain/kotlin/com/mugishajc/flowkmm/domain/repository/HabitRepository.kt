package com.mugishajc.flowkmm.domain.repository

import com.mugishajc.flowkmm.domain.model.Habit
import com.mugishajc.flowkmm.domain.model.HabitEntry
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun getHabitById(id: String): Habit?
    suspend fun insertHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(id: String)

    fun getEntriesForHabit(habitId: String): Flow<List<HabitEntry>>
    suspend fun getEntriesForDate(habitId: String, date: String): List<HabitEntry>
    suspend fun insertEntry(entry: HabitEntry)
    suspend fun deleteEntry(id: String)
}
