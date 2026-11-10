package com.mugishajc.flowkmm

import com.mugishajc.flowkmm.domain.model.Habit
import com.mugishajc.flowkmm.domain.model.HabitFrequency
import com.mugishajc.flowkmm.domain.usecase.RecordEntryUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RecordEntryUseCaseTest {

    private val repo = FakeHabitRepository()
    private val useCase = RecordEntryUseCase(repo)

    private val testHabit = Habit(
        id = "h1",
        name = "Push-ups",
        description = "50 push-ups",
        frequency = HabitFrequency.DAILY,
        targetCount = 1,
        createdAt = "2024-01-01T00:00:00Z",
    )

    @Test
    fun recordEntry_forExistingHabit_succeeds() = runTest {
        repo.insertHabit(testHabit)
        val result = useCase("h1", date = "2024-06-15")
        assertTrue(result.isSuccess)
        assertEquals("h1", result.getOrThrow().habitId)
    }

    @Test
    fun recordEntry_forMissingHabit_fails() = runTest {
        val result = useCase("nonexistent")
        assertTrue(result.isFailure)
    }

    @Test
    fun recordEntry_withZeroCount_fails() = runTest {
        repo.insertHabit(testHabit)
        val result = useCase("h1", count = 0)
        assertTrue(result.isFailure)
    }

    @Test
    fun recordEntry_preservesNote() = runTest {
        repo.insertHabit(testHabit)
        val result = useCase("h1", note = "Felt great today", date = "2024-06-15")
        assertEquals("Felt great today", result.getOrThrow().note)
    }
}
