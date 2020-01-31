package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.ieselcaminas.alberto.finalproject.mhwbuilder.DatabaseCopier

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.SkillsFragmentBinding
import java.io.File
import java.io.InputStream

class Skills : Fragment() {

    companion object {
        fun newInstance() = Skills()
    }

    private lateinit var viewModel: SkillsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SkillsFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.skills_fragment,  container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).skillsDAO()
        val dataSourceRank = AppDatabase.getInstance(application).skillRankDAO()

        val viewModelFactory = SkillsViewModelFactory(dataSource, dataSourceRank, application)

        val skillsViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(SkillsViewModel::class.java)

        binding.skillsViewModel = skillsViewModel


        val inputStream = context?.assets?.open("skills.json")
        skillsViewModel.onStartTracking(inputStream)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SkillsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
