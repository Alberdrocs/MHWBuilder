package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.SkillsFragmentBinding

class Skills : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SkillsFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.skills_fragment,  container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).skillsDAO()
        val dataSourceRank = AppDatabase.getInstance(application).skillRankDAO()
        val dataSourceDecoration = AppDatabase.getInstance(application).decorationDAO()

        val viewModelFactory = SkillsViewModelFactory(dataSource, dataSourceRank, dataSourceDecoration, application)

        val skillsViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(SkillsViewModel::class.java)

        binding.skillsViewModel = skillsViewModel


        val inputStream = context?.assets?.open("decorations.json")
        skillsViewModel.onStartTracking(inputStream)


        return binding.root
    }



}
