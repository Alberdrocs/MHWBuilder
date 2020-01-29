package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
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
        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).armorPieceDAO()
        val dataSourceSet = AppDatabase.getInstance(application).armorSetDAO()
        val viewModelFactory = EquipmentViewModelFactory(application, dataSource, dataSourceSet)

        val equipmentViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(EquipmentViewModel::class.java)

        binding.equipmentSkillsViewModel = equipmentViewModel

        val inputStreamPiece = context?.assets?.open("armor.json")
        val inputStreamSet = context?.assets?.open("sets.json")

        equipmentViewModel.onStartTracking(inputStreamPiece, inputStreamSet)

        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EquipmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
