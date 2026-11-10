package com.mugishajc.flowkmm

import com.mugishajc.flowkmm.domain.model.HabitFrequency
import com.mugishajc.flowkmm.domain.usecase.CreateHabitUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateHabitUseCaseTest {

    private val repo = FakeHabitRepository()
    private val useCase = CreateHabitUseCase(repo)

    @Test
    fun createHabit_withValidName_succeeds() = runTest {
        val result = useCase("Morning Run", "Run 5km every morning")
        assertTrue(result.isSuccess)
        assertEquals("Morning Run", result.getOrThrow().name)
    }

    @Test
    fun createHabit_insertsIntoRepository() = runTest {
        useCase("Read", "Read 30 pages")
        val habits = repo.getAllHabits().first()
        assertEquals(1, habits.size)
        assertEquals("Read", habits[0].name)
    }

    @Test
    fun createHabit_withBlankName_fails() = runTest {
        val result = useCase("  ")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun createHabit_withZeroTarget_fails() = runTest {
        val result = useCase("Test", targetCount = 0)
        assertTrue(result.isFailure)
    }

    @Test
    fun createHabit_trimmsWhitespace() = runTest {
        val result = useCase("  Meditate  ", "  Focus  ")
        assertEquals("Meditate", result.getOrThrow().name)
        assertEquals("Focus", result.getOrThrow().description)
    }

    @Test
    fun createHabit_setsFrequencyCorrectly() = runTest {
        val result = useCase("Weekly Review", frequency = HabitFrequency.WEEKLY)
        assertEquals(HabitFrequency.WEEKLY, result.getOrThrow().frequency)
    }
}
