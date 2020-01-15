package org.ieselcaminas.alberto.finalproject.mhwbuilder

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.buildmaker.Equipment
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.skills.Skills
import org.ieselcaminas.alberto.finalproject.mhwbuilder.buildcreator.stats.Stadistics


class FragmentAdapterPager(fm: FragmentManager) : FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    internal val PAGE_COUNT = 3
    private val tabTitles = arrayOf("Skills", "Equipment", "Stats")

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = Skills()
            1 -> fragment = Equipment()
            2 -> fragment = Stadistics()
        }

        return fragment!!
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}