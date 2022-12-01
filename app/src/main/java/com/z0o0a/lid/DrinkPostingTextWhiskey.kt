package com.z0o0a.lid

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.z0o0a.lid.databinding.DrinkPostingTextWhiskeyBinding

class DrinkPostingTextWhiskey : AppCompatActivity() {
    private lateinit var binding: DrinkPostingTextWhiskeyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingTextWhiskeyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnWhiskeyBack.setOnClickListener {
            finish()
        }

        binding.btnWhiskeyCancel.setOnClickListener {
            cancelConfirm()
        }



        binding.btnWhiskeyDetail.setOnClickListener {
            if (binding.detailLayout.visibility == View.VISIBLE) {
                binding.detailLayout.visibility = View.GONE

                binding.btnWhiskeyDetail.setTextColor(Color.parseColor("#E0F14E"))
            } else{
                binding.detailLayout.visibility = View.VISIBLE

                binding.btnWhiskeyDetail.setTextColor(Color.parseColor("#CDCDCD"))
            }
        }

        binding.switchWhiskeyKeep.setOnClickListener {
            if (binding.txtWhiskeyKeep.isVisible) {
                binding.txtWhiskeyKeep.visibility = View.INVISIBLE
                binding.whiskeyKeepDate.visibility = View.VISIBLE
            } else {
                binding.txtWhiskeyKeep.visibility = View.VISIBLE
                binding.whiskeyKeepDate.visibility = View.INVISIBLE
            }
        }

    }

    fun cancelConfirm(){
        AlertDialog.Builder(this)
            .setTitle("작성을 취소하시겠습니까?")
            .setPositiveButton("네", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
            })
            .setNegativeButton("아니오", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            .create()
            .show()
    }
}