package com.z0o0a.lid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkPostingTextWhiskeyBinding

class DrinkPostingTextWhiskey : AppCompatActivity() {
    private lateinit var binding: DrinkPostingTextWhiskeyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingTextWhiskeyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}