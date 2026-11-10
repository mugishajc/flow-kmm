package com.mugishajc.flowkmm

import com.mugishajc.flowkmm.domain.model.HabitFrequency
import kotlin.test.Test
import kotlin.test.assertEquals

class HabitModelTest {

    @Test
    fun frequencyDisplayName_daily() {
        assertEquals("Daily", HabitFrequency.DAILY.displayName())
    }

    @Test
    fun frequencyDisplayName_weekly() {
        assertEquals("Weekly", HabitFrequency.WEEKLY.displayName())
    }

    @Test
    fun frequencyDisplayName_monthly() {
        assertEquals("Monthly", HabitFrequency.MONTHLY.displayName())
    }
}
