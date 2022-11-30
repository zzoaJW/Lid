package com.z0o0a.lid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkPostingTextBeerBinding

class DrinkPostingTextBeer : AppCompatActivity() {
    private lateinit var binding: DrinkPostingTextBeerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingTextBeerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}