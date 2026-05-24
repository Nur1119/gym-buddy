package app.gymbuddy.di

/**
 * Repositories use constructor injection with `@Inject constructor(...)` and
 * `@Singleton`, so Hilt auto-binds them without explicit @Provides functions.
 *
 * This module exists as the documented home for any future binding overrides
 * (e.g. fakes/mocks for testing, switching repository implementations).
 */
@dagger.Module
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
object RepositoryModule
