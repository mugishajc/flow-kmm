# Flow KMM

A Kotlin Multiplatform Mobile habit tracker that shares domain logic, networking, and persistence between Android and iOS.

## Architecture

```
flow-kmm/
  shared/              Kotlin Multiplatform shared module
    commonMain/        Platform-agnostic business logic
      domain/
        model/         Habit, HabitEntry, HabitStats
        repository/    HabitRepository interface
        usecase/       CreateHabit, RecordEntry, GetStats, GetHabitsWithStatus
      data/
        local/         SQLDelight schema + driver factory (expect/actual)
        remote/        Ktor HTTP client for sync
        repository/    HabitRepositoryImpl (SQLDelight-backed)
      di/              Koin module wiring
    androidMain/       Android SQLite driver
    iosMain/           Native SQLite driver
    commonTest/        Unit tests for domain logic and use cases
  androidApp/          Jetpack Compose UI (list, detail, stats screens)
  iosApp/              SwiftUI placeholder referencing the shared framework
```

## Tech Stack

| Layer          | Library                        |
|----------------|--------------------------------|
| Language       | Kotlin 2.1, Swift (iOS stubs)  |
| UI (Android)   | Jetpack Compose + Material 3   |
| UI (iOS)       | SwiftUI (placeholder)          |
| Networking     | Ktor 3.x                       |
| Persistence    | SQLDelight 2.x                 |
| DI             | Koin 4.x                       |
| Async          | Kotlin Coroutines + Flow       |
| Date/Time      | kotlinx-datetime               |
| Build          | Gradle 8.10 + Version Catalog  |
| CI             | GitHub Actions                 |

## Getting Started

**Prerequisites:** JDK 17+, Android SDK 35, Xcode 15+ (for iOS builds).

```bash
# Clone
git clone https://github.com/mugishajc/flow-kmm.git
cd flow-kmm

# Build shared module
./gradlew :shared:build

# Run shared tests
./gradlew :shared:allTests

# Build Android APK
./gradlew :androidApp:assembleDebug

# iOS: open iosApp/ in Xcode, build with the shared framework
```

## Shared Module

The `shared` module contains all business logic and is consumed by both platforms:

- **Domain models** are plain Kotlin data classes with `@Serializable` for network transport.
- **Use cases** enforce validation (blank names, target bounds, entry limits) and return `Result<T>`.
- **SQLDelight** generates type-safe queries from `.sq` files; platform drivers are wired via `expect/actual`.
- **Ktor client** is configured for JSON content negotiation; swap the base URL to point at a real backend.
- **Koin** wires everything together; platform modules supply the `DatabaseDriverFactory`.

## Tests

16 tests in `shared/commonTest` cover:

- Habit creation (validation, trimming, frequency assignment)
- Entry recording (missing habit, zero count, note preservation)
- Stats computation (streaks, totals, recent entries cap)
- Model display names

## Status

- [x] Shared domain logic with full test coverage
- [x] SQLDelight persistence schema
- [x] Ktor networking layer (mock API target)
- [x] Koin dependency injection
- [x] Android app with Compose screens
- [x] CI pipeline (build + test)
- [ ] iOS SwiftUI screens (placeholder wired)
- [ ] Backend sync API
- [ ] Offline-first conflict resolution

## License

MIT
