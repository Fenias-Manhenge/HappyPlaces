package com.example.happyplaces

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.isPhotoPickerAvailable
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.happyplaces.databinding.AddHappyPlaceBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class AddHappyPlace : AppCompatActivity() {

    private val binding by lazy { AddHappyPlaceBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.tbAddHappyPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tbAddHappyPlace.setNavigationOnClickListener {
            this.finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

        onBackPressedDispatcher.addCallback(
            this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                this@AddHappyPlace.finish()
            }
        })

        binding.etDate.setOnClickListener {
            datePicker()
        }

        binding.btnAddImage.setOnClickListener {
            addImageMenu(it, R.menu.add_photo_popup_menu)
        }
    }

    private fun datePicker(){
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val selectedDay = datePicker.selection
            val calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calender.timeInMillis = selectedDay!!
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN)

            binding.etDate.setText(dateFormat.format(calender.time))
        }
        datePicker.show(supportFragmentManager, "tag")
    }

    private fun addImageMenu(view: View, @MenuRes menusRes: Int){
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(menusRes, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId){
                R.id.menuGallery ->{
                    if (!hasStoragePermission(baseContext))
                        askStoragePermission.launch(STORAGE_PERMISSIONS)
                    else
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                R.id.menuCamera ->{
                    if (!hasCameraPermissions(baseContext))
                        askCameraPermissions.launch(CAMERA_PERMISSIONS)
                    else
                        camera.launch(null)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    private val askStoragePermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
        var permissionGranted = true

        permissions.entries.forEach{
            if (it.key in STORAGE_PERMISSIONS && !it.value)
                permissionGranted = false
        }

        if (!permissionGranted){
            if (!PERMISSION_DENIED_BEFORE)
                showStoragePermissionRationalDialog()
            else
                showSettingsRationalDialog()
        }else {
            if (isPhotoPickerAvailable(this))
                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }


    private val photoPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
        if (uri != null)
            binding.ivAddImage.setImageURI(uri)
    }

    private val camera = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){ bitmap ->
        binding.ivAddImage.setImageBitmap(bitmap)
    }
    
    private val askCameraPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions->
        var permissionGranted = true

        permissions.entries.forEach {
            if (it.key in CAMERA_PERMISSIONS && !it.value)
                permissionGranted = false
        }

        if (!permissionGranted){
            if (!PERMISSION_DENIED_BEFORE) {
                showCameraPermissionRationalDialog()
            } else
                showSettingsRationalDialog()
        } else
            camera.launch(null)
    }

    private fun showCameraPermissionRationalDialog(){
        ShowingDialogs.rationalDialogCamera(this, askCameraPermissions)
    }

    private fun showSettingsRationalDialog(){
        ShowingDialogs.rationalDialogSettings(this)
    }

    private fun showStoragePermissionRationalDialog(){
        ShowingDialogs.rationalDialogStorage(this, askStoragePermission)
    }
}