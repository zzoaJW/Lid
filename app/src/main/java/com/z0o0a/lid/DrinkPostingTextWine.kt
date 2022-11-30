package com.z0o0a.lid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkPostingTextWineBinding

class DrinkPostingTextWine : AppCompatActivity() {
    private lateinit var binding: DrinkPostingTextWineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingTextWineBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }
}