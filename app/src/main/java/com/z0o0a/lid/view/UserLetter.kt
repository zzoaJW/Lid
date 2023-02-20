package com.z0o0a.lid.view

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.UserLetterBinding

class UserLetter : AppCompatActivity() {
    private lateinit var binding: UserLetterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserLetterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnCopyMail.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("LiD_service_email", "z0o0a.service@gmail.com")

            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "이메일이 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.letterBtnBack.setOnClickListener {
            finish()
        }


    }

}