# AGENTS.md

This file contains development guidelines and commands for agentic coding agents working on the Reaction Training Android app.

## Build & Test Commands

### Build Commands

```bash
# Full project build
./gradlew build

# Clean build
./gradlew clean build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install debug APK to connected device
./gradlew installDebug
```

### Testing Commands

```bash
# Run all unit tests
./gradlew test

# Run all instrumented tests
./gradlew connectedAndroidTest

# Run specific test class
./gradlew test --tests "com.reactiontraining.presentation.viewmodel.MainViewModelTest"

# Run specific test method
./gradlew test --tests "*.MainViewModelTest.initial state should be WAITING with blue background"

# Run tests with coverage report
./gradlew testDebugUnitTest jacocoTestReport

# Run lint checks
./gradlew lint

# Run specific module tests
./gradlew :app:test
```

## Code Style Guidelines

### File Organization

- Follow Clean Architecture: `data/`, `domain/`, `presentation/`, `di/`
- Package structure: `com.reactiontraining.{layer}.{sublayer}`
- Compose screens in `presentation/ui/`, ViewModels in `presentation/viewmodel/`

### Import Organization

1. Android/AndroidX imports
2. Kotlin standard library imports
3. Project imports (com.reactiontraining.*)
4. Separate groups with blank lines
5. Use explicit imports, avoid `*` imports (except for common Compose groups)

Example:

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import kotlinx.coroutines.flow.StateFlow
import com.reactiontraining.domain.model.ReactionTime
```

### Naming Conventions

- **Classes**: PascalCase (`MainViewModel`, `ReactionTimeEntity`)
- **Functions & Variables**: camelCase (`startGame()`, `uiState`)
- **Constants**: UPPER_SNAKE_CASE (`DEFAULT_DELAY_MS`)
- **Private members**: Prefix with `_` for backing properties (`_uiState`)
- **Enum values**: UPPER_SNAKE_CASE (`IDLE`, `WAITING`, `READY`, `FINISHED`)

### Type Guidelines

- Use `val` over `var` whenever possible
- Prefer nullable types (`String?`) over default values for optional UI state
- Use `Long` for timestamps, `Int` for UI dimensions
- Use sealed classes/enum classes for finite states
- Use data classes for immutable state containers

### Error Handling

- Always handle `CancellationException` in coroutines
- Use try-catch for database operations with meaningful error handling
- Validate input parameters in public methods
- Use `require()` for preconditions, `check()` for invariants
- Log errors appropriately but don't expose internal details to users

Example:

```kotlin
override suspend fun saveReactionTime(reactionTimeMs: Long) {
    try {
        require(reactionTimeMs > 0) { "Reaction time must be positive" }
        val entity = ReactionTimeEntity(reactionTimeMs = reactionTimeMs)
        reactionTimeDao.insertReactionTime(entity)
    } catch (e: CancellationException) {
        throw e // Re-throw cancellation
    } catch (e: Exception) {
        // Log error and handle gracefully
        throw DataOperationException("Failed to save reaction time", e)
    }
}
```

### Compose Guidelines

- Use `@Composable` functions for UI components
- Pass state as parameters, callbacks as lambdas
- Use `remember` for expensive calculations
- Use `derivedStateOf` for derived state calculations
- Add `contentDescription` for accessibility
- Prefer `Modifier.clickable` over `Button` for custom tap areas

Example:

```kotlin
@Composable
fun MainScreen(
    uiState: GameUiState,
    onScreenTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { onScreenTap() }
            .semantics { contentDescription = "Game screen" }
    ) { /* content */ }
}
```

### Testing Guidelines

- Use descriptive test names with backticks
- Follow Given-When-Then structure
- Mock dependencies with Mockito
- Use `runTest` for coroutine testing
- Test all public methods and edge cases
- Verify repository interactions in ViewModel tests

Example:

```kotlin
@Test
fun `startGame should set state to WAITING with blue background`() = runTest {
    // Given
    val initialState = viewModel.uiState.value
    
    // When
    viewModel.startGame()
    
    // Then
    assertEquals(GameState.WAITING, viewModel.uiState.value.gameState)
    assertEquals(Color.Blue, viewModel.uiState.value.backgroundColor)
}
```

### Dependency Injection (Hilt)

- Use constructor injection with `@Inject`
- Mark ViewModels with `@HiltViewModel`
- Use `@Module` and `@InstallIn(SingletonComponent::class)` for providers
- Mark singletons with `@Singleton`
- Use `@AndroidEntryPoint` for Activities/Fragments

### Database (Room)

- Use entities for table definitions with `@Entity`
- Define DAOs with `@Dao` interface
- Use `@Insert`, `@Query`, `@Delete` for operations
- Use `Flow<List<T>>` for observable data
- Handle database errors gracefully

## Development Workflow

1. Always run lint before committing: `./gradlew lint`
2. Run tests after changes: `./gradlew test`
3. Use `./gradlew build` to verify compilation
4. Check imports and formatting with Android Studio
5. Ensure all public APIs are documented
6. Test on emulator/device before marking as complete

## Key Dependencies

- **Jetpack Compose**: Modern UI toolkit
- **Hilt**: Dependency injection
- **Room**: Local database
- **Coroutines**: Asynchronous programming
- **ViewModel**: UI state management
- **Material3**: Design system

## Performance Notes

- Use `collectAsState()` efficiently, collect minimal state
- Avoid recomposition in @Composable functions
- Use `remember` for expensive computations
- Profile with Android Studio's performance tools
- Monitor memory usage in ViewModels

## Security Considerations

- Validate all user inputs
- Use parameterized queries for database operations
- Don't log sensitive information
- Use HTTPS for network operations (if added later)
