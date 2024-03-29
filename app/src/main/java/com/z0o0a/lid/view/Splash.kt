package com.z0o0a.lid.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.z0o0a.lid.MainHost
import com.z0o0a.lid.R
import com.z0o0a.lid.databinding.SplashBinding

class Splash : AppCompatActivity() {
    private lateinit var binding: SplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.splash)
        binding.lifecycleOwner = this

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainHost::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}