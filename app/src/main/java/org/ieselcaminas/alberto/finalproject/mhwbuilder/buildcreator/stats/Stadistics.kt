package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.stats

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.StadisticsFragmentBinding

class Stadistics : Fragment() {

    companion object {
        fun newInstance() = Stadistics()
    }

    private lateinit var viewModel: StadisticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: StadisticsFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.stadistics_fragment,  container, false)
        viewModel = ViewModelProviders.of(this).get(StadisticsViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StadisticsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
