package com.app.nasasearch.di

import android.content.Context
import com.app.nasasearch.NasaApplication
import com.app.nasasearch.api.BaseRepository
import com.app.nasasearch.api.NasaRepository
import com.app.nasasearch.ui.viewmodels.SearchViewModelProviderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideNasaRepository() : BaseRepository = NasaRepository()

    @Singleton
    @Provides
    fun provideSearchViewModelProviderFactory(
        @ApplicationContext context: Context, nasaRepository: BaseRepository) =
        SearchViewModelProviderFactory(context.applicationContext as NasaApplication, nasaRepository)
}