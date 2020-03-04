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
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.Equipment
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.EquipmentViewModel
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.decorations.Decoration
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.DecorationPickerFragmentBinding

class DecorationPickerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DecorationPickerFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.decoration_picker_fragment,  container, false)
        val application = requireNotNull(this.activity).application
        val databaseInstance = AppDatabase.getInstance(application)
        val dataSource = databaseInstance.decorationDAO()
        val dataSourceSkill = databaseInstance.skillsDAO()
        val dataSourceSkillRank = databaseInstance.skillRankDAO()

        val decorationViewModelFactory = DecorationPickerViewModelFactory(application, dataSource, binding)
        val decorationPickerViewModel = ViewModelProviders.of(this, decorationViewModelFactory).get(DecorationPickerViewModel::class.java)

        val equipmentViewModel = activity?.run {ViewModelProviders.of(this, Equipment.viewModelFactory).get(EquipmentViewModel::class.java)}

        val args = DecorationPickerFragmentArgs.fromBundle(arguments!!)

        val adapter = equipmentViewModel?.let {
            DecorationPickerAdapter(viewLifecycleOwner, dataSourceSkill, dataSourceSkillRank,
                it, args.slotPosition, args.armorType
            )
        }

        binding.decorationRecyclerView.adapter = adapter

        decorationPickerViewModel.getDecorationsOfSlot(args.slot1).observe(viewLifecycleOwner, Observer {
            it?.let { if (adapter != null) adapter.data = it }
        })

        decorationPickerViewModel.decorationSearchQuery.observe(viewLifecycleOwner, Observer { query ->
            if (query == ""){
                decorationPickerViewModel.getDecorationsOfSlot(args.slot1).observe(viewLifecycleOwner, Observer {
                    it?.let {
                        if (adapter != null) {
                            adapter.data = it
                        }
                    }
                })
            } else {
                val decorationListQueried = ArrayList<Decoration>()
                decorationPickerViewModel.getDecorationsOfSlot(args.slot1).observe(viewLifecycleOwner, Observer {
                    it?.let {
                        for (i in it)
                            if (i.name.toLowerCase().contains(query.toLowerCase()))
                                decorationListQueried.add(i)
                        adapter?.data = decorationListQueried
                    }
                })
            }
        })

        return binding.root
    }


}
