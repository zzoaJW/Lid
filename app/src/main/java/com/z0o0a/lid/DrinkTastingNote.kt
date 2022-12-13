package com.z0o0a.lid

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkTastingNoteBinding

class DrinkTastingNote  : AppCompatActivity() {
    private lateinit var binding: DrinkTastingNoteBinding

    var drinkId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkTastingNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        drinkId = intent.getIntExtra("drinkId",1)

        setDrink()

        binding.btnDrinkDel.setOnClickListener {
            delConfirm()
        }

        binding.noteBtnBack.setOnClickListener {
            finish()
        }

    }

    private fun setDrink(){
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(applicationContext)

            val drink = db!!.drinkDao().getDrink(drinkId)

            runOnUiThread {
                binding.noteDrinkEngName.text = drink.drinkEngName
                binding.noteDrinkKrName.text = drink.drinkKrName
                binding.noteDrinkType.text = drink.drinkType
                binding.noteDrinkRating.text = drink.drinkRating.toString()
                binding.noteDrinkTasting.text = drink.drinkOverallStr
                binding.noteDrinkKeepDate.text = drink.drinkKeepDate
                binding.noteDrinkPlace.text = drink.drinkPlace
                binding.noteDrinkPostingDate.text = drink.drinkPostingDate
                binding.noteDrinkRegion.text = drink.drinkRegion
                binding.noteDrinkPrice.text = drink.drinkPrice

                // 조건 왜 안되지 ㅠ
//                if (drink?.drinkImg != BitmapFactory.decodeResource(resources, R.drawable.bottle)) {
                    binding.noteDrinkImg.visibility = View.VISIBLE
                    binding.noteDrinkImg.setImageBitmap(drink.drinkImg)
//                }
            }
        }).start()
    }

    private fun delConfirm(){
        AlertDialog.Builder(this)
            .setTitle("노트를 삭제하시겠습니까?")
            .setMessage("(삭제 후 취소가 불가능합니다.)")
            .setPositiveButton("네") { dialog, which -> // 해당 노트 삭제

                Thread(Runnable {
                    val db = DrinkDatabase.getInstance(applicationContext)

                    val drink = db!!.drinkDao().getDrink(drinkId)
                    // drink 삭제
                    db!!.drinkDao().deleteDrink(drink)
                }).start()

                // 메인 화면으로 돌아가기
                finish()
            }
            .setNegativeButton("아니오", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            .create()
            .show()
    }
}