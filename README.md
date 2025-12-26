# Reaction Training

An Android application for measuring and tracking reaction times using Jetpack Compose.

## Overview

The app measures user reaction times through a simple color-based game:

1. Screen starts blue (waiting state)
2. After a random delay (2-6 seconds), screen turns green (ready state)  
3. User taps as quickly as possible when screen turns green
4. Reaction time is measured and stored locally
5. Statistics are calculated and displayed

## Architecture

Clean Architecture implementation with the following layers:

### Domain Layer

- **Models**: `ReactionTime`, `ReactionStats`
- **Repository Interface**: `ReactionRepository`

### Data Layer  

- **Database**: Room with `ReactionTimeEntity` and `ReactionTimeDao`
- **Repository Implementation**: `ReactionRepositoryImpl`

### Presentation Layer

- **UI**: Jetpack Compose screens (`MainScreen`, `StatsCard`)
- **ViewModel**: `MainViewModel` with StateFlow for reactive state management
- **DI**: Hilt for dependency injection

## Technical Specifications

### Target Platform

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 35

### Dependencies

- **UI**: Jetpack Compose BOM 2024.06.00, Material3
- **Architecture**: MVVM with Hilt 2.51.1 for DI
- **Database**: Room 2.6.1 for local data persistence
- **Async**: Kotlin Coroutines 1.8.1
- **Testing**: JUnit 4.13.2, Coroutines Test, Room Testing

### Key Components

- **MainViewModel**: Manages game state (IDLE, WAITING, READY, FINISHED) and statistics
- **ReactionRepository**: Handles data persistence and statistics calculation
- **StatsCard**: Expandable statistics display with reset functionality
- **MainScreen**: Full-screen touch interface with dynamic background colors

## Game Logic

The reaction time measurement follows this sequence:

1. **Initial State**: Blue background, waiting for user tap to start
2. **Waiting State**: Blue background, random delay (2000-6000ms) before transition
3. **Ready State**: Green background, timestamp captured for reaction measurement
4. **Finished State**: Green background, reaction time calculated and displayed
5. **Auto-restart**: Returns to waiting state after 2 seconds

### State Management

- Uses Kotlin `enum class` for finite game states
- `GameUiState` data class for immutable state container
- `StateFlow` for reactive updates to UI components

## Data Persistence

### Database Schema

```sql
CREATE TABLE reaction_times (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    reaction_time_ms INTEGER NOT NULL,
    timestamp INTEGER NOT NULL
);
```

### Statistics Calculation

- **Average Time**: Mean of all recorded reaction times
- **Best Time**: Minimum recorded reaction time  
- **Worst Time**: Maximum recorded reaction time
- **Total Attempts**: Count of all recorded attempts

## UI Components

### MainScreen

- Full-screen touch area with dynamic background color
- State-dependent visual feedback
- Handles all user interaction events

### StatsCard  

- Collapsible statistics display (default collapsed)
- Expand/collapse animation with keyboard arrow indicators
- Reset button (visible only when expanded)
- Real-time statistics updates from database

## Build Commands

```bash
# Build project
./gradlew build

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Build debug APK
./gradlew assembleDebug

# Install to connected device
./gradlew installDebug

# Run lint checks
./gradlew lint
```

## Testing

- **Unit Tests**: ViewModel logic, repository operations, data transformation
- **Instrumented Tests**: Database operations, UI interactions
- **Test Coverage**: JUnit 4, Coroutines Test, Room Testing libraries

## Development Guidelines

Follows established conventions outlined in `AGENTS.md`:

- Clean Architecture layering
- Constructor injection with Hilt
- Coroutines for async operations with proper cancellation handling
- Material Design 3 components
- Comprehensive error handling and input validation

## File Structure

```
app/src/main/java/com/reactiontraining/
├── data/
│   ├── local/
│   │   ├── dao/ReactionTimeDao.kt
│   │   ├── entity/ReactionTimeEntity.kt
│   │   └── AppDatabase.kt
│   └── repository/ReactionRepositoryImpl.kt
├── domain/
│   ├── model/ReactionTime.kt, ReactionStats.kt
│   └── repository/ReactionRepository.kt
├── di/AppModule.kt
├── presentation/
│   ├── ui/MainScreen.kt, StatsCard.kt
│   ├── viewmodel/MainViewModel.kt
│   └── MainActivity.kt
└── ui/theme/Theme.kt, Color.kt, Type.kt
```
