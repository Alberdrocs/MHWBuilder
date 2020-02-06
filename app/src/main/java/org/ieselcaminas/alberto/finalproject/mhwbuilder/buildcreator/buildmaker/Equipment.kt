package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.armor.ArmorPiece
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.EquipmentFragmentBinding

class Equipment : Fragment() {

    companion object {
        fun newInstance() = Equipment()
    }

    val equipmentArmors: ArrayList<SelectedArmor> = ArrayList()

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

        val adapter = EquipmentAdapter()
        binding.armorPieceRecyclerView.adapter = adapter


        equipmentViewModel.getPieceOfEachType().observe(viewLifecycleOwner, Observer {
            it?.let {
                equipmentArmors.add(0,SelectedArmor(it[0],"skill1","skill2"))
                equipmentArmors.add(1,SelectedArmor(it[1],"skill1","skill2"))
                equipmentArmors.add(2,SelectedArmor(it[2],"skill1","skill2"))
                equipmentArmors.add(3,SelectedArmor(it[3],"skill1","skill2"))
                equipmentArmors.add(4,SelectedArmor(it[4],"skill1","skill2"))
                adapter.data = equipmentArmors
            }
        })




        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EquipmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
