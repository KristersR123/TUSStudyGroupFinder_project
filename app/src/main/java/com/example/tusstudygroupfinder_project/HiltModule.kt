package com.example.tusstudygroupfinder_project


import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * TUS Study Group Finder Project
 * File: HiltModule.kt
 * Description: Provides dependency injection using Dagger Hilt for Firebase services (Authentication, Firestore)
 *              and the Room database. Configures dependencies for both Singleton and ViewModel scopes.
 * Author: Kristers Rakstins - K00273773
 * Copied From Last Year's Project (TUS Campus Connect)
 */

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {

    @Provides
    fun provideAuthentication(): FirebaseAuth = Firebase.auth


    @Provides
    @ViewModelScoped
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "study_group_db"
        ).build()
    }

    @Provides
    fun provideGroupDao(database: AppDatabase): GroupDao = database.groupDao()

    @Provides
    fun provideSessionDao(database: AppDatabase): SessionDao = database.sessionDao()
}