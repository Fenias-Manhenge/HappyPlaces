package com.example.happyplaces

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MenuRes
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.happyplaces.databinding.AddHappyPlaceBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.Reader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class AddHappyPlace : AppCompatActivity() {

    private val binding by lazy { AddHappyPlaceBinding.inflate(layoutInflater) }

    companion object{
        private var PERMISSION_DENIED_BEFORE = false

        private val CAMERA_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q){
                    addAll(arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ))
                }else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2){
                    add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU){
                    add(Manifest.permission.READ_MEDIA_IMAGES)
                } else
                    addAll(arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                        ))
            }.toTypedArray()

        private fun hasPermissions(context: Context) = CAMERA_PERMISSIONS.all { permissions ->
            ContextCompat.checkSelfPermission(context, permissions) == PackageManager.PERMISSION_GRANTED
        }
    }

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
            TODO("Add callBack method")
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
                    Toast.makeText(this, "Gallery selected", Toast.LENGTH_LONG).show()
                }
                R.id.menuCamera ->{
                    if (!hasPermissions(baseContext))
                        askPermissions.launch(CAMERA_PERMISSIONS)
                    else
                        camera.launch(null)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    private val camera = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){ bitmap ->
            binding.ivAddImage.setImageBitmap(bitmap)
    }
    
    private val askPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions->
        var permissionGranted = true

        permissions.entries.forEach {
            if (it.key in CAMERA_PERMISSIONS && !it.value)
                permissionGranted = false
        }

        if (!permissionGranted){
            if (!PERMISSION_DENIED_BEFORE) {
                rationalDialog()
            } else
                rationalDialogSettings()
        } else
            camera.launch(null)
    }

    private fun rationalDialog(){
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(resources.getString(R.string.warning))
            .setMessage(resources.getString(R.string.warning_message))
            .setPositiveButton("Okay!"){ _, _ ->
                PERMISSION_DENIED_BEFORE = true
                askPermissions.launch(CAMERA_PERMISSIONS)
            }
            .setNegativeButton("Cancel"){ dialog, _ ->
                PERMISSION_DENIED_BEFORE = true
                dialog.dismiss()
            }
            .show()
    }

    private fun rationalDialogSettings(){
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(resources.getString(R.string.warning_disabled))
            .setMessage(resources.getString(R.string.warning_message_disabled))
            .setPositiveButton("Go to settings!"){ _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}