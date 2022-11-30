package com.z0o0a.lid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkPostingGuideBinding

class DrinkPostingGuide : AppCompatActivity() {
    private lateinit var binding: DrinkPostingGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingGuideBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }

}