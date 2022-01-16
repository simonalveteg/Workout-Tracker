package com.example.android.january2022.di

import android.app.Application
import androidx.room.Room
import com.example.android.january2022.db.GymDatabase
import com.example.android.january2022.db.GymRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGymDatabase(app: Application): GymDatabase {
        return Room
            .databaseBuilder(
                app,
                GymDatabase::class.java,
                "gymdatabase.db"
            )
            .createFromAsset("gymdatabase.db")
            //.fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGymRepository(db: GymDatabase): GymRepository {
        return GymRepository(db.dao)
    }
}