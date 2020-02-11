package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.Decoration
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.DecorationDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRank
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillRankDAO
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDAO

class SkillsViewModelFactory(
    private val dataSource: SkillsDAO,
    private val dataSourceRank: SkillRankDAO,
    private val dataSourceDecoration: DecorationDAO,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SkillsViewModel::class.java)) {
            return SkillsViewModel(dataSource, dataSourceRank, dataSourceDecoration, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}