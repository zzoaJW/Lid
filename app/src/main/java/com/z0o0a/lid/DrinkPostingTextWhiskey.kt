package com.z0o0a.lid

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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

    var whShort = false

    var drinkImg : Bitmap? = null
    var drinkEngName = ""
    var drinkKrName = ""
    var drinkType = ""

    var whId : Long = 0
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

    var whPlace = ""
    var whKeepDate = ""
    var whPostingDate = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingTextWhiskeyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getSingletonValues()
        setSingletonValuesToUI()

        binding.btnWhiskeyBack.setOnClickListener {
            finish()
        }

        binding.btnWhiskeyCancel.setOnClickListener {
            cancelConfirm()
        }

        binding.btnWhiskeyColor.setOnClickListener {
            showColorDialog()
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
            hideWhDetail()
        }

        binding.switchWhiskeyKeep.setOnClickListener {
            if (binding.txtWhiskeyKeep.isVisible) {
                binding.txtWhiskeyKeep.visibility = View.INVISIBLE
                binding.whiskeyKeepDate.visibility = View.VISIBLE
            } else {
                binding.whiskeyKeepDate.text = "개봉일 선택"
                binding.whiskeyKeepDate.visibility = View.INVISIBLE
                binding.txtWhiskeyKeep.visibility = View.VISIBLE
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

        binding.btnWhiskeyPosting.setOnClickListener {
            getCharacters()
            getOverallEtc()

            saveWhiskey()
            saveDrink()


            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()

            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

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

    private fun hideWhDetail(){
        if (binding.detailLayout.visibility == View.VISIBLE) {
            whShort = true
            binding.detailLayout.visibility = View.GONE
            binding.btnWhiskeyDetail.setTextColor(Color.parseColor("#E0F14E"))
        } else{
            whShort = false
            binding.detailLayout.visibility = View.VISIBLE
            binding.btnWhiskeyDetail.setTextColor(Color.parseColor("#CDCDCD"))
        }
    }

    private fun showColorDialog(){
        MaterialColorPickerDialog
            .Builder(this) // Pass Activity Instance
            .setTitle("") // Dialog 제목
            .setColorShape(ColorShape.SQAURE) // 컬러칩 모양
            .setColors(resources.getStringArray(R.array.whiskey_colors)) // 컬러 구성
//                .setDefaultColor("#FCEE97") // Pass Default Color
            .setColorListener { color, colorHex ->
                binding.btnWhiskeyColor.setBackgroundColor(colorHex.toColorInt())
                whColors = colorHex
            }
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

        whPlace = binding.whiskeyPlace.text.toString()
        if (whPlace == "") {
            whPlace = "-"
        }

        whKeepDate = binding.whiskeyKeepDate.text.toString()
        if (whKeepDate == "개봉일 선택") {
            whKeepDate = "보관 안함"
        }

        whPostingDate = getCurrentDate()
    }

    fun getCurrentDate(): String {
        val now = System.currentTimeMillis()
        val y = SimpleDateFormat("yyyy", Locale.KOREAN).format(now).toInt()
        val m = SimpleDateFormat("MM", Locale.KOREAN).format(now).toInt()
        val d = SimpleDateFormat("dd", Locale.KOREAN).format(now).toInt()

        return "${y}.${m}.${d}"
    }

    private fun getSingletonValues(){
        val postingSingleton = PostingDrinkSingleton.getInstance(applicationContext)

        drinkImg = postingSingleton?.drinkImg
        drinkEngName = postingSingleton?.drinkEngName.toString()
        drinkKrName = postingSingleton?.drinkKrName.toString()
        drinkType = postingSingleton?.drinkType.toString()

    }

    private fun setSingletonValuesToUI(){
        binding.postingWhiskeyImg.setImageBitmap(drinkImg)
        binding.postingWhiskeyEngName.text = drinkEngName
        binding.postingWhiskeyKrName.text = drinkKrName
        binding.postingWhiskeyType.text = drinkType
    }

    private fun saveWhiskey(){
        val run : Runnable = Runnable {
            var drinkWhiskey = DrinkWhiskey(0,
                whShort,
                whType,
                whColors,
                whNoses,
                whPalates,
                whCharSweet,
                whCharSpicy,
                whCharBody,
                whFinishs
            )

            val db = DrinkDatabase.getInstance(applicationContext)
            var returnWhId = db!!.drinkDao().insertDrinkWhiskey(drinkWhiskey)

            whId = returnWhId
        }

        val t = Thread(run)
        t.start()

        try {
            t.join()
        }catch (e : InterruptedException){
            Log.d("위스키 저장","실패")
        }
    }

    private fun saveDrink(){
        Thread(Runnable {
            var newDrink = Drink(0,
                drinkImg,
                drinkEngName,
                drinkKrName,
                drinkType,
                whId,
                whOverallStr,
                whRating,
                whRegion,
                whPrice,
                whKeepDate,
                whPlace,
                whPostingDate)

            val db = DrinkDatabase.getInstance(applicationContext)
            db!!.drinkDao().insertDrink(newDrink)
        }).start()
    }
}