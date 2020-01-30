package org.ieselcaminas.alberto.finalproject.mhwbuilder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.DatabaseCopier
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPieceDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorSet
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorSetDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.Skills
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO

@Database(entities = [Skills::class, SkillRank::class, ArmorPiece::class, ArmorSet::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun skillsDAO(): SkillsDAO
    abstract fun skillRankDAO(): SkillRankDAO
    abstract fun armorPieceDAO(): ArmorPieceDAO
    abstract fun armorSetDAO(): ArmorSetDAO
    companion object {
        @JvmField
        val MIGRATION_3_5 : Migration = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance =
                    INSTANCE
                if (instance == null) {
                    instance = DatabaseCopier.getInstance(context).roomDatabase
                    INSTANCE = instance
                }
                return instance!!
            }
        }
    }
}

