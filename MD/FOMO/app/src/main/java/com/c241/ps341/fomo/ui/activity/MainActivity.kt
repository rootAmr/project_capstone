package com.c241.ps341.fomo.ui.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.c241.ps341.fomo.R
import com.c241.ps341.fomo.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Base_Theme_FOMO_ActionBar)
        supportActionBar?.hide()
        checkAndRequestPermissions(this)
        binding = ActivityMainBinding.inflate(layoutInflater)

        with(binding) {
            setContentView(root)
            navController = findNavController(R.id.nav_host_fragment_activity_home)
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_favorite,
                    R.id.navigation_profile
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)

            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (navController.currentDestination?.id == R.id.resultFragment) {
                        navController.navigate(R.id.navigation_home)
                    } else {
                        finish()
                    }
                }
            }

            onBackPressedDispatcher.addCallback(this@MainActivity, callback)
            navView.setupWithNavController(navController)

            navView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_home -> {
                        navController.navigate(R.id.navigation_home)
                    }

                    R.id.navigation_favorite -> {
                        navController.navigate(R.id.navigation_favorite)
                    }

                    R.id.navigation_profile -> {
                        navController.navigate(R.id.navigation_profile)
                    }
                }

                true
            }
        }
    }

    private fun checkAndRequestPermissions(activity: Activity?): Boolean {
        return if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.CAMERA
                )
            } != PackageManager.PERMISSION_GRANTED) {
            if (activity != null) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                    2
                )
            }

            false
        } else {
            true
        }
    }
}