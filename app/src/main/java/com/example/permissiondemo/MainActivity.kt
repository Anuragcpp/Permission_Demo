package com.example.permissiondemo

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private val cameraResultLoncher : ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            isGranted ->
            if (isGranted){
                Toast.makeText(this,"Permission granted ", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Permission Denied ", Toast.LENGTH_SHORT).show()
            }
        }

    private val cameraAndLocationResultLoncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value

                if (isGranted){
                    //if permission is granted then we have to check which permisson has been granted manually
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this,
                            "Permission granted for location",
                            Toast.LENGTH_SHORT)
                            .show()
                    }else if (permissionName == Manifest.permission.CAMERA){

                        Toast.makeText(this,
                            "Permission granted for Camera",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                }else{
                    // if the permission is denied then we have to check again which permission has been denied
                    if (permissionName == Manifest.permission.CAMERA){
                        Toast.makeText(this,
                            "Permission Denied! for camera",
                            Toast.LENGTH_SHORT)
                            .show()
                    }else if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this,
                            "Permission Denied for location",
                            Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cameraPermission : Button = findViewById(R.id.button)

        cameraPermission.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showRationalDialogue("Permission Demo requires camera access" , "camera cannot be used as Camera access is denied")
            }else{
                cameraAndLocationResultLoncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }
    }


    // Dialog to popup in the main screen
    private fun showRationalDialogue(
        title : String,
        message : String
    ){
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("cancel"){ dialog, _->
                dialog.dismiss()
            }

        builder.create().show()
    }
}