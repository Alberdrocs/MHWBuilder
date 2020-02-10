package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.EquipmentFragmentBinding


class Equipment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).armorPieceDAO()
        val dataSourceSet = AppDatabase.getInstance(application).armorSetDAO()
        val viewModelFactory = EquipmentViewModelFactory(application, dataSource, dataSourceSet, viewLifecycleOwner)
        val equipmentViewModel = activity?.run {
            ViewModelProviders.of(
                this, viewModelFactory).get(EquipmentViewModel::class.java)}

        val binding: EquipmentFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.equipment_fragment,  container, false)
        binding.equipmentSkillsViewModel = equipmentViewModel
        val adapter = EquipmentAdapter()
        binding.armorPieceRecyclerView.adapter = adapter

        //equipmentViewModel.getArmorPiece()

        if (equipmentViewModel != null) {
            equipmentViewModel.currentArmorPieces.observe(viewLifecycleOwner, Observer {
                it?.let {
                    adapter.data = it
                }
            })
        }
        if (equipmentViewModel != null) {
            Log.i("TAG", equipmentViewModel.currentArmorPieces.value.toString())
        }

        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}
