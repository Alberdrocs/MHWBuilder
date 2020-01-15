package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
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
        viewModel = ViewModelProviders.of(this).get(SkillsViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SkillsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
