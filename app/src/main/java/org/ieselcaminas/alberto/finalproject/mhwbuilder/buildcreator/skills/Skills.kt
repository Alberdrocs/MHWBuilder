package org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills

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
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.SkillsForDisplay
import org.ieselcaminas.alberto.finalproject.mhwbuilder.database.AppDatabase
import org.ieselcaminas.alberto.finalproject.mhwbuilder.databinding.SkillsFragmentBinding
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


class Skills : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SkillsFragmentBinding =  DataBindingUtil.inflate(inflater, R.layout.skills_fragment,  container, false)
        val application = requireNotNull(this.activity).application

        val dataSource = AppDatabase.getInstance(application).skillsDAO()
        val dataSourceRank = AppDatabase.getInstance(application).skillRankDAO()
        val dataSourceDecoration = AppDatabase.getInstance(application).decorationDAO()
        val dataSourceArmor = AppDatabase.getInstance(application).armorPieceDAO()
        val dataSourceSet = AppDatabase.getInstance(application).armorSetDAO()
        val viewModelFactory = SkillsViewModelFactory(dataSource, dataSourceRank, dataSourceDecoration, application)
        val skillsViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(SkillsViewModel::class.java)


        val equipmentViewModelFactory = EquipmentViewModelFactory(application, dataSourceArmor, dataSourceSet, dataSourceRank, dataSource, viewLifecycleOwner)
        val equipmentViewModel = activity?.run {
            ViewModelProviders.of(
                this, equipmentViewModelFactory).get(EquipmentViewModel::class.java) }

        binding.skillsViewModel = skillsViewModel

        val adapter = SkillsAdapter()
        binding.skillsRecyclerView.adapter = adapter

        if (equipmentViewModel != null) {
            equipmentViewModel.currentSkillsForDisplay.observe(viewLifecycleOwner, Observer {
                it?.let {
                    var skillsForDisplayList = ArrayList<SkillsForDisplay>()
                    for (i in it){
                        skillsForDisplayList.add(i.value)
                    }
                    Collections.sort(skillsForDisplayList, object : Comparator<SkillsForDisplay> {
                        override fun compare(p0: SkillsForDisplay?, p1: SkillsForDisplay?): Int {
                            return if (p0!!.activeLevels > p1!!.activeLevels) -1 else if (p0.activeLevels < p1.activeLevels) 1 else 0
                        }

                    })
                    adapter.data = skillsForDisplayList
                }
            })
        }

        return binding.root
    }



}
