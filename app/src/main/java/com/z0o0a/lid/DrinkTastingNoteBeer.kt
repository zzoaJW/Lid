package com.z0o0a.lid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkTastingNoteBeerBinding

class DrinkTastingNoteBeer : AppCompatActivity() {
    private lateinit var binding: DrinkTastingNoteBeerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkTastingNoteBeerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}