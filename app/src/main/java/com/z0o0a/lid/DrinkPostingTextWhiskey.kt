package com.z0o0a.lid

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.chip.Chip
import com.z0o0a.lid.databinding.DrinkPostingTextWhiskeyBinding
import java.text.SimpleDateFormat
import java.util.*


class DrinkPostingTextWhiskey : AppCompatActivity() {
    private lateinit var binding: DrinkPostingTextWhiskeyBinding

    var whType = "-" // 소분류 (일단 다 -로 저장)

    var whColors = ""
    val whNoses : MutableList<String> = mutableListOf()
    val whPalates : MutableList<String> = mutableListOf()
    var whCharSweet = 3
    var whCharSpicy = 3
    var whCharBody = 3
    val whFinishs : MutableList<String> = mutableListOf()

    var whRating = 0f
    var whOverallStr = ""
    var whRegion = ""
    var whPrice = ""

    var drinkPlace = ""
    var drinkKeepDate = ""
    var drinkPostingDate = ""


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

        binding.btnWhiskeyColor.setOnClickListener {
            MaterialColorPickerDialog
                .Builder(this) // Pass Activity Instance
                .setTitle("") // Dialog 제목
                .setColorShape(ColorShape.SQAURE) // 컬러칩 모양
                .setColors(resources.getStringArray(R.array.whiskey_colors)) // 컬러 구성
//                .setDefaultColor("#FCEE97") // Pass Default Color
                .setColorListener { color, colorHex ->
                    binding.btnWhiskeyColor.setBackgroundColor(colorHex.toColorInt())
                    whColors = colorHex.toColorInt().toString()
                }
                .show()
        }

        binding.whiskeyNoseChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            whNoses.clear()
            for (id in checkedIds) {
                var c: Chip = group.findViewById(id)
                var c_t = c.text
                whNoses.add(c_t.toString())
            }
        }

        binding.whiskeyPalateChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            whPalates.clear()
            for (id in checkedIds) {
                var c: Chip = group.findViewById(id)
                var c_t = c.text
                whPalates.add(c_t.toString())
            }
        }

        binding.whiskeyFinishChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            whFinishs.clear()
            for (id in checkedIds) {
                var c: Chip = group.findViewById(id)
                var c_t = c.text
                whFinishs.add(c_t.toString())
            }
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

        binding.whiskeyKeepDate.setOnClickListener {
            var dateString = ""

            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}.${month+1}.${dayOfMonth}"
                binding.whiskeyKeepDate.text = dateString
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
                Calendar.DAY_OF_MONTH)).show()
        }

        binding.

    }

    private fun cancelConfirm(){
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

    private fun getCharacters(){
        whCharSweet = binding.whiskeySweet.progress
        whCharSpicy = binding.whiskeySpicy.progress
        whCharBody = binding.whiskeyBody.progress
    }

    private fun getOverallEtc(){
        whRating = binding.whiskeyRatingBar.rating

        whOverallStr = binding.whiskeyOverallStr.text.toString()
        if (whOverallStr == "") {
            whOverallStr = "-"
        }

        whRegion = binding.whiskeyRegion.text.toString()
        if (whRegion == "") {
            whRegion = "-"
        }

        whPrice = binding.whiskeyPrice.text.toString()
        if (whPrice == "") {
            whPrice = "-"
        }

        drinkPlace = binding.whiskeyPlace.text.toString()
        if (drinkPlace == "") {
            drinkPlace = "-"
        }

        drinkKeepDate = binding.whiskeyKeepDate.text.toString()

        drinkPostingDate = getCurrentDate()
    }

    fun getCurrentDate():String{
        val now =  System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN).format(now)

        return simpleDateFormat
    }
}