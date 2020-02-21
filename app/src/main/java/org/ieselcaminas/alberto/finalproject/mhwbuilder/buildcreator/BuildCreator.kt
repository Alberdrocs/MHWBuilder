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
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.BuildCreatorFragmentBinding



class BuildCreator : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: BuildCreatorFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.build_creator_fragment,  container, false)

        binding.buildCreatorViewPager.adapter = FragmentAdapterPager(childFragmentManager)
        binding.buildCreatorViewPager.currentItem = 1
        binding.tabLayout.setupWithViewPager(binding.buildCreatorViewPager)
        return binding.root
    }

}
