package org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Skills::class], version = 1, exportSchema = false)
abstract class SkillsDatabase: RoomDatabase() {
    abstract val skillsDatabaseDAO: SkillsDatabaseDAO
    companion object {
        @Volatile
        private var INSTANCE: SkillsDatabase? = null
        fun getInstance(context: Context): SkillsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SkillsDatabase::class.java,
                        "skills_database"
                    ) .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}