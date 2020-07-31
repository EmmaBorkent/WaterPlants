package com.emmaborkent.waterplants.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.emmaborkent.waterplants.PlantViewModel
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.allplants.PlantsAllFragment
import com.emmaborkent.waterplants.databinding.FragmentTabbedBinding
import com.emmaborkent.waterplants.plantstoday.PlantsTodayFragment
import com.google.android.material.tabs.TabLayout
import timber.log.Timber


class TabbedFragment : Fragment() {

    private lateinit var binding: FragmentTabbedBinding
    private val viewModel by activityViewModels<PlantViewModel>()
    private var selectedTab: Int = 0

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
            selectedTab = tab!!.position
            Timber.i("SelectedTab = $selectedTab")
            setFragmentForTabPosition(tab.position)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(PlantsTodayFragment())
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_tabbed, container, false)
        viewModel.updateActionBarTitle(activity?.title.toString())

        binding.tabLayout.addOnTabSelectedListener(tabListener)
//        nav_host_container_tabbed?.findNavController()?.navigate(TabbedFragmentDirections.actionTabbedFragmentToPlantsTodayFragment())

        Timber.i("Creating View; selectedTab = $selectedTab")
//        replaceFragment(PlantsTodayFragment())
//        setFragmentForTabPosition(selectedTab)
        binding.tabLayout.getTabAt(selectedTab)?.select()
//        binding.tabLayout.setScrollPosition(selectedTab, 0f, true)


        binding.floatingActionButton.setOnClickListener {
            view?.findNavController()?.navigate(TabbedFragmentDirections.actionTabbedFragmentToAddEditPlantFragment())
        }
        Timber.i("onCreateView called")
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_container_tabbed, fragment)
        fragmentTransaction.commit()
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("onAttach called")
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.i("onSaveInstanceState called")
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.i("onActivityCreated called")
    }
    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
    }
    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
    }
    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }
    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("onDestroyView called")
    }
    override fun onDetach() {
        super.onDetach()
        Timber.i("onDetach called")
    }
}