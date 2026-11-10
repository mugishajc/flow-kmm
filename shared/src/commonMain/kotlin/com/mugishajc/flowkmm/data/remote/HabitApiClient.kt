package com.mugishajc.flowkmm.data.remote

import com.mugishajc.flowkmm.domain.model.Habit
import com.mugishajc.flowkmm.domain.model.HabitEntry
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Remote sync client for habit data. Targets a REST API for cross-device sync.
 * Currently backed by a mock/stub -- swap the base URL to point at a real backend.
 */
class HabitApiClient(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://api.flowkmm.example.com/v1",
) {
    suspend fun fetchHabits(): List<Habit> =
        httpClient.get("$baseUrl/habits").body()

    suspend fun pushHabit(habit: Habit): Habit =
        httpClient.post("$baseUrl/habits") {
            contentType(ContentType.Application.Json)
            setBody(habit)
        }.body()

    suspend fun pushEntry(entry: HabitEntry): HabitEntry =
        httpClient.post("$baseUrl/entries") {
            contentType(ContentType.Application.Json)
            setBody(entry)
        }.body()
}
