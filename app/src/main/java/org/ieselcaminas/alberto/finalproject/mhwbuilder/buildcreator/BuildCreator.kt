package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.ieselcaminas.alberto.finalproject.mhwbuilder.FragmentAdapterPager
import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills.SkillsViewModel
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills.SkillsViewModelFactory
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.BuildCreatorFragmentBinding



class BuildCreator : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: BuildCreatorFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.build_creator_fragment,  container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = BuildCreatorViewModelFactory(application)

        val buildCreatorViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(BuildCreatorViewModel::class.java)
        binding.buildCreatorViewModel = buildCreatorViewModel
        binding.buildCreatorViewPager.adapter = FragmentAdapterPager(activity!!.supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.buildCreatorViewPager)
        return binding.root
    }



}
