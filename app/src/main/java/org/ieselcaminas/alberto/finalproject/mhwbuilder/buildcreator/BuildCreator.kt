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

    companion object {
        fun newInstance() =
            BuildCreator()
    }

    private lateinit var viewModel: BuildCreatorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: BuildCreatorFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.build_creator_fragment,  container, false)
        viewModel = ViewModelProviders.of(this).get(BuildCreatorViewModel::class.java)
        binding.buildCreatorViewPager.adapter = FragmentAdapterPager(activity!!.supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.buildCreatorViewPager)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BuildCreatorViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
