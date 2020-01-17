package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDatabaseDAO

class SkillsViewModelFactory(
    private val dataSource: SkillsDatabaseDAO,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SkillsViewModel::class.java)) {
            return SkillsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}