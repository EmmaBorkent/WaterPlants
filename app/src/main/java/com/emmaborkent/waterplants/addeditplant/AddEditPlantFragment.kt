package com.emmaborkent.waterplants.addeditplant

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.emmaborkent.waterplants.R
import com.emmaborkent.waterplants.databinding.FragmentAddEditPlantBinding
import com.emmaborkent.waterplants.util.PERMISSION_CODE
import com.emmaborkent.waterplants.util.PICK_IMAGE_CODE
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDate
import java.time.Period
import java.util.*

class AddEditPlantFragment : Fragment() {

    // TODO: 4-9-2020 Create string resources for all strings

    private lateinit var binding: FragmentAddEditPlantBinding
    private var plantId = 0
    private lateinit var viewModelFactory: AddEditPlantViewModelFactory
    private lateinit var viewModel: AddEditPlantViewModel
    private var isEditActivity: Boolean = false
    private lateinit var clickedButtonView: View
    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            when (clickedButtonView) {
                binding.buttonDatePlantsNeedsWater -> {
                    Timber.i("Date: $year, $month, $dayOfMonth")
                    viewModel.changeWaterDate(year, month + 1, dayOfMonth)
                }
                binding.buttonDatePlantsNeedsMist -> {
                    Timber.i("Date: $year, $month, $dayOfMonth")
                    viewModel.changeMistDate(year, month + 1, dayOfMonth)
                }
            }
        }
    private var imageIsChanged: Boolean = false
    private var callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
//            Toast.makeText(requireContext(), "TEST", Toast.LENGTH_SHORT).show()
            deletePlantReturnHome()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView called")
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_add_edit_plant, container, false)
        plantId = AddEditPlantFragmentArgs.fromBundle(requireArguments()).plantId
        val application = requireNotNull(this.activity).application
        viewModelFactory = AddEditPlantViewModelFactory(plantId, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddEditPlantViewModel::class.java)
        binding.lifecycleOwner = this
        binding.addEditPlantViewModel = viewModel

        isEditActivity = isEditActivity(plantId)

        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        if (isEditActivity) {
            setActivityTitle(R.string.edit_plant_toolbar_title)
            viewModel.setupPageToEditPlant()
        } else {
            setActivityTitle(R.string.add_plant_toolbar_title)
            viewModel.setupPageToAddPlant()
        }

        binding.buttonPickImage.setOnClickListener {
            pickImage()
        }
        binding.buttonDatePlantsNeedsWater.setOnClickListener { v ->
            clickedButtonView = v!!
            if (viewModel.plant.value != null) {
                Timber.i("Date: ${viewModel.waterYear.value}, ${viewModel.waterMonth.value}, ${viewModel.waterDay.value}")
                DatePickerDialog(
                    requireContext(), dateSetListener,
                    viewModel.waterYear.value!!,
                    viewModel.waterMonth.value!! - 1,
                    viewModel.waterDay.value!!
                )
                    .show()
            } else {
                throw IllegalArgumentException("No value for date")
            }
        }
        binding.buttonDatePlantsNeedsMist.setOnClickListener { v ->
            clickedButtonView = v!!
            Timber.i("Date: ${viewModel.mistYear.value}, ${viewModel.mistMonth.value}, ${viewModel.mistDay.value}")
            if (viewModel.plant.value != null) {
                DatePickerDialog(
                    requireContext(), dateSetListener,
                    viewModel.mistYear.value!!,
                    viewModel.mistMonth.value!! - 1,
                    viewModel.mistDay.value!!
                )
                    .show()
            } else {
                throw IllegalArgumentException("No value for date")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.plant.observe(viewLifecycleOwner, androidx.lifecycle.Observer { plant ->
            val plantImageUri = Uri.parse(plant.image)
            binding.imagePlant.setImageURI(plantImageUri)
        })
    }

    private fun isEditActivity(plantId: Int): Boolean {
        return plantId != 0
    }

    private fun Fragment.setActivityTitle(id: Int) {
        (activity as AppCompatActivity?)!!.supportActionBar?.title = getString(id)
    }

    private fun pickImage() {
        if (checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, PERMISSION_CODE)
        } else {
            openImageGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager
                    .PERMISSION_DENIED
            ) {
                // Permission from popup is denied
                Toast.makeText(
                    requireContext(),
                    "Please grant permission to add an image",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Permission from popup is granted
                openImageGallery()
            }
        }
    }

    private fun openImageGallery() {
        val openImageGallery = Intent(Intent.ACTION_GET_CONTENT)
        openImageGallery.type = "image/*"
        startActivityForResult(
            openImageGallery,
            PICK_IMAGE_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_CODE) {
            imageIsChanged = true
            binding.imagePlant.setImageURI(data?.data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu_add_edit_plant, menu)
        if (isEditActivity) {
            val saveNewPlantMenuItem = menu.findItem(R.id.action_save_new)
            saveNewPlantMenuItem.isVisible = false
            val updatePlantMenuItem = menu.findItem(R.id.action_save_update)
            updatePlantMenuItem.isVisible = true
            val deleteMenuItem: MenuItem = menu.findItem(R.id.action_delete)
            deleteMenuItem.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save_new -> {
                if (checkViewsForValidInput()) {
                    updatePlant()
                    view?.findNavController()
                        ?.navigate(AddEditPlantFragmentDirections.actionAddEditPlantFragmentToTabbedFragment())
                }
            }
            R.id.action_save_update -> {
                if (checkViewsForValidInput()) {
                    updatePlant()
                    view?.findNavController()?.navigate(
                        AddEditPlantFragmentDirections.actionAddEditPlantFragmentToDetailsFragment(
                            viewModel.plant.value!!.id
                        )
                    )
                }
            }
            R.id.action_delete -> {
                warnBeforeDeleteDialog()
            }
            android.R.id.home -> {
                viewModel.deletePlant(viewModel.plant.value!!)
                Timber.i("Plant Deleted")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkViewsForValidInput(): Boolean {
        return if (binding.editPlantName.text.isNullOrEmpty()) {
            Toast.makeText(context, "No Plant Name is Entered", Toast.LENGTH_SHORT).show()
            false
        } else if (!isEditActivity && !imageIsChanged) {
            Toast.makeText(context, "There is no image selected", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    private fun updatePlant() {
        val plantImageUri = saveImageToInternalStorage()

        viewModel.plant.value?.apply {
            name = binding.editPlantName.text.toString()
            species = binding.editPlantSpecies.text.toString()
            image = plantImageUri.toString()
            Timber.d("Plant Image String: $image")
            waterEveryDays = binding.editWaterEveryDays.text.toString().toInt()
            waterDate = viewModel.plant.value?.waterDate!!
            mistEveryDays = binding.editMistEveryDays.text.toString().toInt()
            mistDate = viewModel.plant.value?.mistDate!!

            val today = LocalDate.now()
            waterInDays = Period.between(today, waterDate).days
            mistInDays = Period.between(today, mistDate).days
        }

        viewModel.updatePlant(viewModel.plant.value)
    }

    private fun saveImageToInternalStorage(): Uri {
        val drawable = binding.imagePlant.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val file = File(context?.filesDir, "${UUID.randomUUID()}.jpg")
        val stream: OutputStream = FileOutputStream(file)
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream)
            stream.flush()
        } finally {
            try {
                stream.close()
            } catch (e: IOException) {
                Timber.e("Closing OutputStream Failed")
            }
        }

        Timber.d("New Image Successfully Saved to Internal Storage")
        return Uri.parse(file.absolutePath)
    }

    private fun warnBeforeDeleteDialog() {
        val builder = AlertDialog.Builder(context)
        builder
            .setTitle("Delete Plant?")
            .setIcon(R.drawable.ic_delete_black_24dp)
            .setMessage("All data will be lost")
            .setPositiveButton("Yes") { _, _ ->
                deletePlantReturnHome()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun deletePlantReturnHome() {
        // Delete from database
        viewModel.deletePlant(viewModel.plant.value!!)

        // Delete image from internal files
        if (viewModel.plant.value!!.image != "") {
            val file = File(viewModel.plant.value!!.image)
            file.setExecutable(true)
            file.delete()
        }

        Timber.i("Plant Deleted")

        // Return to home
        view?.findNavController()
            ?.navigate(AddEditPlantFragmentDirections.actionAddEditPlantFragmentToTabbedFragment())
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("onAttach called")
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