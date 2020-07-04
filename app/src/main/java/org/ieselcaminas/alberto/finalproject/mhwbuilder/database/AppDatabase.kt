package org.ieselcaminas.alberto.finalproject.mhwbuilder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPieceDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorSet
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorSetDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm.Charms
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm.CharmsDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.Decoration
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.DecorationDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.Skills
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration



@Database(entities = [Skills::class, SkillRank::class, ArmorPiece::class, ArmorSet::class, Decoration::class, Charms::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun skillsDAO(): SkillsDAO
    abstract fun skillRankDAO(): SkillRankDAO
    abstract fun armorPieceDAO(): ArmorPieceDAO
    abstract fun armorSetDAO(): ArmorSetDAO
    abstract fun decorationDAO(): DecorationDAO
    abstract fun charmsDAO(): CharmsDAO
    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance =
                    INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "mhwbuilder_database.db"
                        ).createFromAsset("databases/mhwbuilder_database.db")
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}