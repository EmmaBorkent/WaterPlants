package com.emmaborkent.waterplants.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentTabbedBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_tabbed.*

class TabbedFragment : Fragment() {

    private lateinit var binding: FragmentTabbedBinding

    //    private val addPlantRequestCode = 1
    private val tabListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
            // Called when a tab that is already selected is chosen again by the user. Some
            // applications may use this action to return to the top level of a category.
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            // Called when a tab exits the selected state.
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            setFragmentForTabPosition(tab!!.position)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tabbed, container, false)
        binding.tabLayout.addOnTabSelectedListener(tabListener)
        replaceFragment(PlantsTodayFragment())
//        nav_host_container_tabbed?.findNavController()?.navigate(TabbedFragmentDirections.actionTabbedFragmentToPlantsTodayFragment())
        binding.floatingActionButton.setOnClickListener {
            view?.findNavController()?.navigate(TabbedFragmentDirections.actionTabbedFragmentToAddEditPlantFragment())
        }
        return binding.root
    }

    private fun setFragmentForTabPosition(position: Int) {
        when (position) {
            0 -> {
                replaceFragment(PlantsTodayFragment())
//                view?.findNavController()?.navigate(TabbedFragmentDirections.actionTabbedFragmentToPlantsTodayFragment())
            }
            1 -> {
                replaceFragment(PlantsAllFragment())
//                view?.findNavController()?.navigate(TabbedFragmentDirections.actionTabbedFragmentToPlantsAllFragment())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_container_tabbed, fragment)
        fragmentTransaction.commit()
    }
}