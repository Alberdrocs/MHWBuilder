package org.ieselcaminas.alberto.finalproject.mhwbuilder

import android.os.Debug
import android.util.Log
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.Skills
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDatabaseDAO
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SkillDatabaseTest {

    private lateinit var skillDao: SkillsDatabaseDAO
    private lateinit var db: SkillsDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, SkillsDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        skillDao = db.skillsDatabaseDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
//        val rankList: ArrayList<SkillRank> = ArrayList()
//        rankList.add(SkillRank(1,"Reduces the number of times you take poison damage.",1,null))
//        rankList.add(SkillRank(2,"Greatly reduces the number of times you take poison damage.",2,null))
//        rankList.add(SkillRank(3,"Prevents poison.",3,null))
//        val skill = Skills(1,"Poison Resistance","Reduces damage while poisoned.",rankList)
//        skillDao.insert(skill)
        Log.i("TAG",skillDao.get(1).name)

    }
}