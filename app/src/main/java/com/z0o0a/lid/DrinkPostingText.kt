package com.z0o0a.lid

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.z0o0a.lid.databinding.DrinkPostingTextBinding
import java.text.SimpleDateFormat
import java.util.*

class DrinkPostingText : AppCompatActivity() {
    private lateinit var binding: DrinkPostingTextBinding

    var drinkImg : String = ""
    var drinkRating : Float = 0F
    var drinkRegion : String = ""
    var drinkPrice : String = ""
    var drinkOverallStrRating : String = ""
    var drinkKeepDate : String = ""
    var drinkPlace : String = ""
    var drinkPostingDate : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingTextBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setDrinkImgNameType()
        binding.postingDrinkImg.setImageURI(Uri.parse(drinkImg))

        binding.btnCancel.setOnClickListener {
            cancelConfirm()
        }

        binding.btnFinish.setOnClickListener {
            drinkRating = binding.drinkRatingBar.rating
            drinkOverallStrRating = binding.drinkTastingNote.text.toString()
            if (drinkOverallStrRating == "") {
                drinkOverallStrRating = "-"
            }
            drinkKeepDate = binding.drinkKeepDate.text.toString()
            if (drinkKeepDate == "개봉일 선택") {
                drinkKeepDate = "-"
            }
            drinkPlace = binding.drinkPlace.text.toString()
            if (drinkPlace == "") {
                drinkPlace = "-"
            }
            drinkRegion = binding.drinkRegion.text.toString()
            if (drinkRegion == "") {
                drinkRegion = "-"
            }
            drinkPrice = binding.drinkPrice.text.toString()
            if (drinkPrice == "") {
                drinkPrice = "-"
            }
            drinkPostingDate = binding.btnDrinkDate.text.toString()
            saveDrink(drinkOverallStrRating, drinkRating, drinkRegion, drinkPrice, drinkKeepDate, drinkPlace, drinkPostingDate)

            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()

            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.switchDrinkKeep.setOnClickListener {
            if (binding.txtDrinkKeep.isVisible) {
                binding.txtDrinkKeep.visibility = View.INVISIBLE
                binding.drinkKeepDate.visibility = View.VISIBLE
            } else {
                binding.txtDrinkKeep.visibility = View.VISIBLE
                binding.drinkKeepDate.visibility = View.INVISIBLE
            }
        }

        binding.drinkKeepDate.setOnClickListener {
            var dateString = ""

            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}.${month+1}.${dayOfMonth}"
                binding.drinkKeepDate.setText(dateString)
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnDrinkDate.text = getCurrentDate()
        binding.btnDrinkDate.setOnClickListener {
            var dateString = ""

            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}.${month+1}.${dayOfMonth}"
                binding.btnDrinkDate.setText(dateString)
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
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

    fun getCurrentDate():String{
        val now =  System.currentTimeMillis()
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN).format(now)

        return simpleDateFormat
    }

    fun setDrinkImgNameType(){
        val postingSingleton = PostingDrinkSingleton.getInstance(applicationContext)

        drinkImg = postingSingleton?.drinkImg.toString()
        binding.postingDrinkEngName.text = postingSingleton?.drinkEngName.toString()
        binding.postingDrinkKrName.text = postingSingleton?.drinkKrName.toString()
        binding.postingDrinkType.text = postingSingleton?.drinkType.toString()
    }

    fun saveDrink(overallStrRating : String, rating : Float, drinkRegion : String, drinkPrice : String, keepDate : String, place : String, postingDate : String){
        val postingSingleton = PostingDrinkSingleton.getInstance(applicationContext)

        Thread(Runnable {
            var newDrink = Drink(0,
                postingSingleton?.drinkImg.toString(),
                postingSingleton?.drinkEngName.toString(),
                postingSingleton?.drinkKrName.toString(),
                postingSingleton?.drinkType.toString(),
                0,
                overallStrRating,
                rating,
                drinkRegion,
                drinkPrice,
                keepDate,
                place,
                postingDate)

            val db = DrinkDatabase.getInstance(applicationContext)
            db!!.drinkDao().insertDrink(newDrink)
        }).start()
    }
}