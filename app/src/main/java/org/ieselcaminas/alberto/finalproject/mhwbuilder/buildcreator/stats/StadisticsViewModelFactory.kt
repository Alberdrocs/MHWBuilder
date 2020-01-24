package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.stats

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO

class StadisticsViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StadisticsViewModel::class.java)) {
            return StadisticsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}