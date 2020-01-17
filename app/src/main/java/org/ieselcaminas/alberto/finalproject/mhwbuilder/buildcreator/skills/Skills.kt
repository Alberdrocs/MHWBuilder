package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.skills.SkillsDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.SkillsFragmentBinding

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

        val dataSource = SkillsDatabase.getInstance(application).skillsDatabaseDAO

        val viewModelFactory = SkillsViewModelFactory(dataSource, application)

        val skillsViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(SkillsViewModel::class.java)

        binding.skillsViewModel = skillsViewModel

        binding.setLifecycleOwner(this)

        skillsViewModel.onStartTracking()


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SkillsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
