package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.charmPicker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import org.ieselcaminas.alberto.finalproject.mhwbuilder.R
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.EquipmentViewModel
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.EquipmentViewModelFactory
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.charm.Charms
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.CharmPickerFragmentBinding

/**
 * A simple [Fragment] subclass.
 */
class CharmPickerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: CharmPickerFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.charm_picker_fragment, container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).charmsDAO()

        val dataSourceSet = AppDatabase.getInstance(application).armorSetDAO()
        val dataSourceSkill = AppDatabase.getInstance(application).skillsDAO()
        val dataSourceSkillRank = AppDatabase.getInstance(application).skillRankDAO()
        val dataSourceArmor = AppDatabase.getInstance(application).armorPieceDAO()
        val dataSourceCharm = AppDatabase.getInstance(application).charmsDAO()

        val equipmentViewModelFactory = EquipmentViewModelFactory(application, dataSourceArmor, dataSourceSet, dataSourceSkillRank, dataSourceSkill,dataSourceCharm)
        val equipmentViewModel = activity?.run { ViewModelProviders.of(this, equipmentViewModelFactory).get(EquipmentViewModel::class.java) }

        val viewModelFactory = CharmPickerViewModelFactory(application, dataSource, binding)
        val charmPickerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(CharmPickerViewModel::class.java)

        val adapter = equipmentViewModel?.let {
            CharmPickerAdapter(viewLifecycleOwner, dataSourceSkill, dataSourceSkillRank, it)
        }

        binding.charmsRecyclerView.adapter = adapter

        equipmentViewModel?.getAllCharms()?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter?.data = it
            }
        })

        charmPickerViewModel.charmSearchQuery.observe(viewLifecycleOwner, Observer { query ->
            if (query == ""){
                charmPickerViewModel.getAllCharms().observe(viewLifecycleOwner, Observer {
                    it?.let {
                        if (adapter != null) {
                            adapter.data = it
                        }
                    }
                })
            } else {
                val charmListQueried = ArrayList<Charms>()
                charmPickerViewModel.getAllCharms().observe(viewLifecycleOwner, Observer {
                    it?.let {
                        for (i in it)
                            if (i.name.toLowerCase().contains(query.toLowerCase()))
                                charmListQueried.add(i)
                        adapter?.data = charmListQueried
                    }
                })
            }
        })

        return binding.root
    }


}
