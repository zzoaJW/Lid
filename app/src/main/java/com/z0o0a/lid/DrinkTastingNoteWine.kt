package com.z0o0a.lid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkTastingNoteWineBinding

class DrinkTastingNoteWine : AppCompatActivity() {
    private lateinit var binding: DrinkTastingNoteWineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkTastingNoteWineBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}