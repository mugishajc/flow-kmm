package com.mugishajc.flowkmm

import com.mugishajc.flowkmm.domain.model.Habit
import com.mugishajc.flowkmm.domain.model.HabitEntry
import com.mugishajc.flowkmm.domain.model.HabitFrequency
import com.mugishajc.flowkmm.domain.usecase.GetStatsUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetStatsUseCaseTest {

    private val repo = FakeHabitRepository()
    private val useCase = GetStatsUseCase(repo)

    private val testHabit = Habit(
        id = "h1",
        name = "Journaling",
        description = "",
        frequency = HabitFrequency.DAILY,
        targetCount = 1,
        createdAt = "2024-01-01T00:00:00Z",
    )

    @Test
    fun getStats_forMissingHabit_fails() = runTest {
        val result = useCase("nonexistent")
        assertTrue(result.isFailure)
    }

    @Test
    fun getStats_withNoEntries_returnsZeros() = runTest {
        repo.insertHabit(testHabit)
        val stats = useCase("h1").getOrThrow()
        assertEquals(0, stats.totalEntries)
        assertEquals(0, stats.currentStreak)
        assertEquals(0, stats.longestStreak)
    }

    @Test
    fun getStats_withEntries_computesTotalCorrectly() = runTest {
        repo.insertHabit(testHabit)
        repo.insertEntry(HabitEntry("e1", "h1", "2024-06-01", 1))
        repo.insertEntry(HabitEntry("e2", "h1", "2024-06-02", 1))
        repo.insertEntry(HabitEntry("e3", "h1", "2024-06-03", 1))

        val stats = useCase("h1").getOrThrow()
        assertEquals(3, stats.totalEntries)
        assertEquals("Journaling", stats.habitName)
    }

    @Test
    fun getStats_computesStreakForConsecutiveDays() = runTest {
        repo.insertHabit(testHabit)
        repo.insertEntry(HabitEntry("e1", "h1", "2024-06-01", 1))
        repo.insertEntry(HabitEntry("e2", "h1", "2024-06-02", 1))
        repo.insertEntry(HabitEntry("e3", "h1", "2024-06-03", 1))

        val stats = useCase("h1").getOrThrow()
        assertEquals(3, stats.currentStreak)
        assertEquals(3, stats.longestStreak)
    }

    @Test
    fun getStats_capsRecentEntriesAtTen() = runTest {
        repo.insertHabit(testHabit)
        repeat(15) { i ->
            val day = (i + 1).toString().padStart(2, '0')
            repo.insertEntry(HabitEntry("e$i", "h1", "2024-06-$day", 1))
        }

        val stats = useCase("h1").getOrThrow()
        assertEquals(10, stats.recentEntries.size)
    }
}
