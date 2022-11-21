package com.z0o0a.lid

import android.app.AlertDialog
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkTastingNoteBinding

class DrinkTastingNote  : AppCompatActivity() {
    private lateinit var binding: DrinkTastingNoteBinding

    var drink : Drink? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkTastingNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val drinkId = intent.getIntExtra("drinkId",1).toInt()

        // 일단 이렇게하구... 나중에 리팩토링할때 메소드로 빼기
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(applicationContext)

            drink = db!!.drinkDao().getDrink(drinkId)

            binding.noteDrinkEngName.setText(drink?.drinkEngName)
            binding.noteDrinkKrName.setText(drink?.drinkKrName)
            binding.noteDrinkType.setText(drink?.drinkType)
            binding.noteDrinkRating.setText(drink?.drinkRating.toString())
            binding.noteDrinkTasting.setText(drink?.drinkTasting)
            binding.noteDrinkKeepDate.setText(drink?.drinkKeepDate)
            binding.noteDrinkPlace.setText(drink?.drinkPlace)
            binding.noteDrinkPostingDate.setText(drink?.drinkPostingDate)

            // 혹시라도 여기서 에러나면 uri 빌더로 기본이미지 uri랑 비교하자 (DrinkPostingImg.kr 참고)
            if (drink?.drinkImg != "android.resource://com.z0o0a.lid/drawable/bottle") {
                binding.noteDrinkImg.visibility = View.VISIBLE
                binding.noteDrinkImg.setImageURI(Uri.parse(drink?.drinkImg))
            }


        }).start()

        binding.btnDrinkDel.setOnClickListener {
            delConfirm()
        }

        binding.noteBtnBack.setOnClickListener {
            finish()
        }

    }

    fun delConfirm(){
        AlertDialog.Builder(this)
            .setTitle("노트를 삭제하시겠습니까?")
            .setMessage("(삭제 후 취소가 불가능합니다.)")
            .setPositiveButton("네", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    // 해당 노트 삭제
                    // 일단 이렇게하구... 나중에 리팩토링할때 메소드로 빼기
                    Thread(Runnable {
                        val db = DrinkDatabase.getInstance(applicationContext)
                        db!!.drinkDao().delete(drink)
                    }).start()

                    // 메인 화면으로 돌아가기
                    finish()
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