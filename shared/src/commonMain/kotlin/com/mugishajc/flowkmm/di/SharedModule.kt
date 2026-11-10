package com.mugishajc.flowkmm.di

import com.mugishajc.flowkmm.data.local.DatabaseDriverFactory
import com.mugishajc.flowkmm.data.local.FlowKmmDatabase
import com.mugishajc.flowkmm.data.remote.HabitApiClient
import com.mugishajc.flowkmm.data.remote.createHttpClient
import com.mugishajc.flowkmm.data.repository.HabitRepositoryImpl
import com.mugishajc.flowkmm.domain.repository.HabitRepository
import com.mugishajc.flowkmm.domain.usecase.CreateHabitUseCase
import com.mugishajc.flowkmm.domain.usecase.GetHabitsWithStatusUseCase
import com.mugishajc.flowkmm.domain.usecase.GetStatsUseCase
import com.mugishajc.flowkmm.domain.usecase.RecordEntryUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule: Module = module {
    single { get<DatabaseDriverFactory>().createDriver() }
    single { FlowKmmDatabase(get()) }
    single { createHttpClient() }
    single { HabitApiClient(get()) }
    single<HabitRepository> { HabitRepositoryImpl(get()) }

    factory { CreateHabitUseCase(get()) }
    factory { RecordEntryUseCase(get()) }
    factory { GetStatsUseCase(get()) }
    factory { GetHabitsWithStatusUseCase(get()) }
}
