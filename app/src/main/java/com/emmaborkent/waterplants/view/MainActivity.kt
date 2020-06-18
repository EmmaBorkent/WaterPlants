package com.emmaborkent.waterplants.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.emmaborkent.waterplants.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {}

        override fun onTabUnselected(tab: TabLayout.Tab?) {}

        override fun onTabSelected(tab: TabLayout.Tab?) {
            setFragmentForTabPosition(tab!!.position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tab_layout.addOnTabSelectedListener(mOnTabSelectedListener)
        replaceFragment(PlantsTodayFragment())
        floating_action_button.setOnClickListener { goToAddEditPlant() }
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
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun goToAddEditPlant() {
        val newPlantIntent = Intent(this, AddEditPlantActivity::class.java)
        startActivity(newPlantIntent)
    }

}