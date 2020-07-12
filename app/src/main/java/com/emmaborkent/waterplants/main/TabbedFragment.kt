package com.emmaborkent.waterplants.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.addeditplant.AddEditPlantActivity
import com.emmaborkent.waterplants.databinding.FragmentTabbedBinding
import com.google.android.material.tabs.TabLayout

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
//        binding.floatingActionButton.setOnClickListener { goToAddEditPlant() }
        return binding.root
    }

    private fun setFragmentForTabPosition(position: Int) {
        when (position) {
            0 -> {
                replaceFragment(PlantsTodayFragment())
            }
            1 -> {
                replaceFragment(PlantsAllFragment())
            }
        }
    }

//    private fun setFragmentForTabPosition(position: Int) {
//        return when (position) {
//            0 -> { view?.findNavController()!!.navigate(R.id.action_tabbedFragment_to_plantsTodayFragment) }
//            1 -> { view?.findNavController()!!.navigate(R.id.action_tabbedFragment_to_plantsAllFragment) }
//            else -> throw IllegalArgumentException("No fragment position available")
//        }
//    }

//    private fun goToAddEditPlant() {
//        val newPlantIntent = Intent(this, AddEditPlantActivity::class.java)
//        startActivityForResult(newPlantIntent, addPlantRequestCode)
//    }

//    fun getItem(position: Int): Fragment? {
//        return when (position) {
//            0 -> FragmentTab()
//            1 -> FragmentTab()
//            2 -> FragmentTab()
//            else -> null
//        }
//    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}