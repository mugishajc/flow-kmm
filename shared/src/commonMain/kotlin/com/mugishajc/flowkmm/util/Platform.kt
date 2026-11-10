package com.mugishajc.flowkmm.util

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun generateId(): String = Uuid.random().toString()

fun todayIsoString(): String =
    Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

fun nowIsoString(): String =
    Clock.System.now().toString()
