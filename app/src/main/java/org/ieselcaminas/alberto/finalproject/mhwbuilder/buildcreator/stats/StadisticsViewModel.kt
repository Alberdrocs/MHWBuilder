package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

class StadisticsViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
}
