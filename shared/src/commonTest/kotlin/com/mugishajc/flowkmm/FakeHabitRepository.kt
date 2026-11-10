package com.mugishajc.flowkmm

import com.mugishajc.flowkmm.domain.model.Habit
import com.mugishajc.flowkmm.domain.model.HabitEntry
import com.mugishajc.flowkmm.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeHabitRepository : HabitRepository {
    private val habits = MutableStateFlow<List<Habit>>(emptyList())
    private val entries = MutableStateFlow<List<HabitEntry>>(emptyList())

    override fun getAllHabits(): Flow<List<Habit>> = habits

    override suspend fun getHabitById(id: String): Habit? =
        habits.value.find { it.id == id }

    override suspend fun insertHabit(habit: Habit) {
        habits.value = habits.value + habit
    }

    override suspend fun updateHabit(habit: Habit) {
        habits.value = habits.value.map { if (it.id == habit.id) habit else it }
    }

    override suspend fun deleteHabit(id: String) {
        habits.value = habits.value.filter { it.id != id }
        entries.value = entries.value.filter { it.habitId != id }
    }

    override fun getEntriesForHabit(habitId: String): Flow<List<HabitEntry>> =
        entries.map { list -> list.filter { it.habitId == habitId } }

    override suspend fun getEntriesForDate(habitId: String, date: String): List<HabitEntry> =
        entries.value.filter { it.habitId == habitId && it.date == date }

    override suspend fun insertEntry(entry: HabitEntry) {
        entries.value = entries.value + entry
    }

    override suspend fun deleteEntry(id: String) {
        entries.value = entries.value.filter { it.id != id }
    }
}
