package com.mvl.assignment.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mvl.assignment.R
import com.mvl.assignment.base.BaseActivity
import com.mvl.assignment.base.BaseViewModel
import com.mvl.assignment.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()

    override val layoutId: Int = R.layout.activity_main


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSplashScreen()

    }

    private fun initSplashScreen() {
        installSplashScreen().apply {
            setKeepVisibleCondition {
                viewModel.isLoading.value == true
            }
        }
    }
}