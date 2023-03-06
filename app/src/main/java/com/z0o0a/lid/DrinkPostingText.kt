package com.z0o0a.lid

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.z0o0a.lid.databinding.DrinkPostingTextBinding
import com.z0o0a.lid.model.PostingDrinkSingleton
import com.z0o0a.lid.repository.DrinkDatabase
import com.z0o0a.lid.viewmodel.DrinkPostingVM
import java.text.SimpleDateFormat
import java.util.*

class DrinkPostingText : AppCompatActivity() {
    private lateinit var vm: DrinkPostingVM
    private lateinit var binding: DrinkPostingTextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.drink_posting_text)
        vm = ViewModelProvider(this)[DrinkPostingVM::class.java]
        binding.vm = vm
        binding.lifecycleOwner = this

        // TODO : 이미지 넣기

        binding.btnDrinkBack.setOnClickListener {
            finish()
        }

        binding.btnCancel.setOnClickListener {
            showConfirmDialog()
        }

        binding.btnFinish.setOnClickListener {
            // TODO : drink 저장 (실패하는 경우 예외처리)
            saveDrink()

            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()

            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.switchDrinkKeep.setOnClickListener {
            setDatePickerVisible()
        }

        binding.drinkKeepDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showConfirmDialog(){
        AlertDialog.Builder(this)
            .setTitle("작성을 취소하시겠습니까?")
            .setPositiveButton("네") { _, _ ->
                intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
            .setNegativeButton("아니오") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun setDatePickerVisible(){
        if (binding.txtDrinkKeep.isVisible) {
            binding.txtDrinkKeep.visibility = View.INVISIBLE
            binding.drinkKeepDate.visibility = View.VISIBLE
        } else {
            binding.txtDrinkKeep.visibility = View.VISIBLE
            binding.drinkKeepDate.visibility = View.INVISIBLE
        }
    }

    private fun showDatePickerDialog(){
        val cal = Calendar.getInstance()    //캘린더뷰 만들기
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val dateString = "${year}.${month+1}.${dayOfMonth}"
            binding.drinkKeepDate.text = dateString
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun saveDrink(){

    }

//    fun saveDrink(overallStrRating : String, rating : Float, drinkRegion : String, drinkPrice : String, keepDate : String, place : String, postingDate : String){
//        val postingSingleton = PostingDrinkSingleton.getInstance(applicationContext)
//
//        Thread(Runnable {
//            var newDrink = Drink(0,
//                postingSingleton?.drinkImg,
//                postingSingleton?.drinkEngName.toString(),
//                postingSingleton?.drinkKrName.toString(),
//                postingSingleton?.drinkType.toString(),
//                0,
//                overallStrRating,
//                rating,
//                drinkRegion,
//                drinkPrice,
//                keepDate,
//                place,
//                postingDate)
//
//            val db = DrinkDatabase.getInstance(applicationContext)
//            db!!.drinkDao().insertDrink(newDrink)
//        }).start()
//    }
}