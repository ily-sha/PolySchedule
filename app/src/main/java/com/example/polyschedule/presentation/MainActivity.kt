package com.example.polyschedule.presentation


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.polyschedule.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), GroupSettingFragment.ShowBottomNav {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment?
        navHostFragment?.let {
            bottomNavigationView.setupWithNavController(it.navController)
        }

    }

    override fun showBottomNavigationView() {
        bottomNavigationView.visibility = View.VISIBLE
    }




}