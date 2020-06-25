package com.emmaborkent.waterplants.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.model.Plant
import com.emmaborkent.waterplants.viewmodel.PlantViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val addPlantRequestCode = 1
    private lateinit var plantViewModel: PlantViewModel

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

        plantViewModel = ViewModelProvider(this).get(PlantViewModel::class.java)
        tab_layout.addOnTabSelectedListener(mOnTabSelectedListener)
        replaceFragment(PlantsTodayFragment())

        floating_action_button.setOnClickListener { goToAddEditPlant() }
    }

    private fun goToAddEditPlant() {
        val newPlantIntent = Intent(this, AddEditPlantActivity::class.java)
        startActivityForResult(newPlantIntent, addPlantRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == addPlantRequestCode && resultCode == Activity.RESULT_OK) {
            val name = data?.getStringExtra(AddEditPlantActivity.EXTRA_REPLY_NAME)
            val species = data?.getStringExtra(AddEditPlantActivity.EXTRA_REPLY_SPECIES)
            val image = data?.getStringExtra(AddEditPlantActivity.EXTRA_REPLY_IMAGE)
            val waterEveryDays = data?.getIntExtra(AddEditPlantActivity.EXTRA_REPLY_WATER_EVERY_DAYS, 0)
            val waterDate = data?.getStringExtra(AddEditPlantActivity.EXTRA_REPLY_WATER_DATE)
            val mistEveryDays = data?.getIntExtra(AddEditPlantActivity.EXTRA_REPLY_MIST_EVERY_DAYS, 0)
            val mistDate = data?.getStringExtra(AddEditPlantActivity.EXTRA_REPLY_MIST_DATE)

            val plant = Plant(name!!, species!!, image!!, waterEveryDays!!, waterDate!!, mistEveryDays!!, mistDate!!)
            plantViewModel.insert(plant)
        }
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

}