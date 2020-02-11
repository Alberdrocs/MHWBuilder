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
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.Decoration
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.DecorationDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.Skills
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO

@Database(entities = [Skills::class, SkillRank::class, ArmorPiece::class, ArmorSet::class, Decoration::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun skillsDAO(): SkillsDAO
    abstract fun skillRankDAO(): SkillRankDAO
    abstract fun armorPieceDAO(): ArmorPieceDAO
    abstract fun armorSetDAO(): ArmorSetDAO
    abstract fun decorationDAO(): DecorationDAO
    companion object {
        @JvmField
        val MIGRATION_5_6 : Migration = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE decoration (\n" +
                        "    decoration_id INTEGER PRIMARY KEY\n" +
                        "                          NOT NULL,\n" +
                        "    rarity        INTEGER NOT NULL,\n" +
                        "    slot          INTEGER NOT NULL,\n" +
                        "    name          TEXT    NOT NULL,\n" +
                        "    skill_rank_id TEXT\n" +
                        ");\n")
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

