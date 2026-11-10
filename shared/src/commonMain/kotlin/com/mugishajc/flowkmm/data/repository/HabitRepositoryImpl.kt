package com.mugishajc.flowkmm.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mugishajc.flowkmm.data.local.FlowKmmDatabase
import com.mugishajc.flowkmm.domain.model.Habit
import com.mugishajc.flowkmm.domain.model.HabitEntry
import com.mugishajc.flowkmm.domain.model.HabitFrequency
import com.mugishajc.flowkmm.domain.repository.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class HabitRepositoryImpl(
    private val database: FlowKmmDatabase,
) : HabitRepository {

    private val queries get() = database.habitQueries

    override fun getAllHabits(): Flow<List<Habit>> =
        queries.selectAllHabits()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toHabit() } }

    override suspend fun getHabitById(id: String): Habit? = withContext(Dispatchers.Default) {
        queries.selectHabitById(id).executeAsOneOrNull()?.toHabit()
    }

    override suspend fun insertHabit(habit: Habit) = withContext(Dispatchers.Default) {
        queries.insertHabit(
            id = habit.id,
            name = habit.name,
            description = habit.description,
            frequency = habit.frequency.name,
            target_count = habit.targetCount.toLong(),
            color = habit.color,
            created_at = habit.createdAt,
            archived = if (habit.archived) 1L else 0L,
        )
    }

    override suspend fun updateHabit(habit: Habit) = withContext(Dispatchers.Default) {
        queries.updateHabit(
            name = habit.name,
            description = habit.description,
            frequency = habit.frequency.name,
            target_count = habit.targetCount.toLong(),
            color = habit.color,
            archived = if (habit.archived) 1L else 0L,
            id = habit.id,
        )
    }

    override suspend fun deleteHabit(id: String) = withContext(Dispatchers.Default) {
        queries.transaction {
            queries.deleteEntriesForHabit(id)
            queries.deleteHabit(id)
        }
    }

    override fun getEntriesForHabit(habitId: String): Flow<List<HabitEntry>> =
        queries.selectEntriesForHabit(habitId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toEntry() } }

    override suspend fun getEntriesForDate(
        habitId: String,
        date: String,
    ): List<HabitEntry> = withContext(Dispatchers.Default) {
        queries.selectEntriesForDate(habitId, date).executeAsList().map { it.toEntry() }
    }

    override suspend fun insertEntry(entry: HabitEntry) = withContext(Dispatchers.Default) {
        queries.insertEntry(
            id = entry.id,
            habit_id = entry.habitId,
            date = entry.date,
            count = entry.count.toLong(),
            note = entry.note,
        )
    }

    override suspend fun deleteEntry(id: String) = withContext(Dispatchers.Default) {
        queries.deleteEntry(id)
    }
}

private fun com.mugishajc.flowkmm.data.local.HabitEntity.toHabit() = Habit(
    id = id,
    name = name,
    description = description,
    frequency = HabitFrequency.valueOf(frequency),
    targetCount = target_count.toInt(),
    color = color,
    createdAt = created_at,
    archived = archived != 0L,
)

private fun com.mugishajc.flowkmm.data.local.HabitEntryEntity.toEntry() = HabitEntry(
    id = id,
    habitId = habit_id,
    date = date,
    count = count.toInt(),
    note = note,
)
