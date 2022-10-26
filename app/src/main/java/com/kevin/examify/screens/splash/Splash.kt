package com.kevin.examify.screens.splash

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BuildCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.kevin.examify.BuildConfig
import com.kevin.examify.R
import com.kevin.examify.databinding.SplashBinding


class Splash : Fragment() {

    private lateinit var binding: SplashBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.splash, container, false)


        val versionNum = BuildConfig.VERSION_NAME

        binding.version.text = "v$versionNum"

        setDuration()


        return binding.root
    }


    private fun setDuration() {
        val timer = object: CountDownTimer(3000L,1000L) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                findNavController().navigate(R.id.action_splash_to_onBoard)
            }
        }
        timer.start()
    }


}