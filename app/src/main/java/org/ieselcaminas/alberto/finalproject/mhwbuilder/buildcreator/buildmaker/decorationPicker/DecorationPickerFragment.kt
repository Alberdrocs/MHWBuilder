package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.decorationPicker

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.EquipmentViewModel
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.EquipmentViewModelFactory
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.DecorationPickerFragmentBinding

class DecorationPickerFragment : Fragment() {

    companion object {
        fun newInstance() = DecorationPickerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DecorationPickerFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.decoration_picker_fragment,  container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(application).decorationDAO()
        val dataSourceSkill = AppDatabase.getInstance(application).skillsDAO()
        val dataSourceSkillRank = AppDatabase.getInstance(application).skillRankDAO()
        val dataSourceArmor = AppDatabase.getInstance(application).armorPieceDAO()
        val dataSourceSet = AppDatabase.getInstance(application).armorSetDAO()
        val viewModelFactory = DecorationPickerViewModelFactory(application, dataSource)
        val decorationPickerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(DecorationPickerViewModel::class.java)

        val equipmentViewModelFactory = EquipmentViewModelFactory(application, dataSourceArmor, dataSourceSet, viewLifecycleOwner)
        val equipmentViewModel = activity?.run {
            ViewModelProviders.of(
                this, equipmentViewModelFactory).get(EquipmentViewModel::class.java) }

        binding.decorationPickerViewModel = decorationPickerViewModel
        val args = DecorationPickerFragmentArgs.fromBundle(arguments!!)

        val adapter = equipmentViewModel?.let {
            DecorationPickerAdapter(viewLifecycleOwner, dataSourceSkill, dataSourceSkillRank,
                it, args.slotPosition, args.armorType
            )
        }

        binding.decorationRecyclerView.adapter = adapter


        decorationPickerViewModel.getDecorationsOfSlot(args.slot1).observe(viewLifecycleOwner, Observer {
            it?.let {
                if (adapter != null) {
                    adapter.data = it
                }
            }
        })

        return binding.root
    }


}
