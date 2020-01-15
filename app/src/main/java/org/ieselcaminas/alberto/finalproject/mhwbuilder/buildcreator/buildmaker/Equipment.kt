package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.EquipmentFragmentBinding

class Equipment : Fragment() {

    companion object {
        fun newInstance() = Equipment()
    }

    private lateinit var viewModel: EquipmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: EquipmentFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.equipment_fragment,  container, false)
        viewModel = ViewModelProviders.of(this).get(EquipmentViewModel::class.java)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EquipmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
