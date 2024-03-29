package com.z0o0a.lid

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkTastingNoteWhiskeyBinding
import com.z0o0a.lid.repository.DrinkDatabase

class DrinkTastingNoteWhiskey : AppCompatActivity() {
    private lateinit var binding: DrinkTastingNoteWhiskeyBinding

    private var drinkId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkTastingNoteWhiskeyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        drinkId = intent.getIntExtra("drinkId",1)

        setDrink()
        setDrinkWhiskey()



        binding.btnWhDel.setOnClickListener {
            delConfirm()
        }

        binding.noteWhBtnBack.setOnClickListener {
            finish()
        }


    }

    private fun setDrink(){
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(applicationContext)

            val drink = db!!.drinkDao().getDrink(drinkId)

            runOnUiThread {
                // 혹시라도 여기서 에러나면 uri 빌더로 기본이미지 uri랑 비교하자 (DrinkPostingImg.kr 참고)
//                if (drink?.drinkImg != "android.resource://com.z0o0a.lid/drawable/bottle") {
                    binding.noteWhImg.visibility = View.VISIBLE
                    binding.noteWhImg.setImageBitmap(drink!!.drinkImg)
//                }

                binding.noteWhEngName.text = drink!!.drinkEngName
                binding.noteWhKrName.text = drink!!.drinkKrName
                binding.noteWhType.text = drink!!.drinkType
                binding.noteWhRating.text = drink!!.drinkRating.toString()

                binding.noteWhTasting.text = drink!!.drinkOverallStr
                binding.noteWhKeepDate.text = drink!!.drinkKeepDate
                binding.noteWhPlace.text = drink!!.drinkPlace
                binding.noteWhPostingDate.text = drink!!.drinkPostingDate
                binding.noteWhRegion.text = drink!!.drinkRegion
                binding.noteWhPrice.text = drink!!.drinkPrice


            }
        }).start()
    }

    private fun setDrinkWhiskey(){
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(applicationContext)

            val whId = db!!.drinkDao().getDrink(drinkId).typeId
            val whiskeyDrink = db!!.drinkDao().getDrinkWiskey(whId)

            if (whiskeyDrink.whShort) {
                runOnUiThread {
                    binding.whColorCircle.visibility = View.GONE
                    binding.noteWhDetailLayout.visibility = View.GONE
                }
            } else {

                val whNoses: MutableList<String> = whiskeyDrink!!.whNose
                val whPalate: MutableList<String> = whiskeyDrink!!.whPalate
                val whFinish: MutableList<String> = whiskeyDrink!!.whFinish

                runOnUiThread {

                    if (whiskeyDrink!!.whColor != "") {
                        binding.whColorCircle.background.setTint(Color.parseColor(whiskeyDrink!!.whColor))
                    } else {
                        binding.whColorCircle.visibility = View.GONE
                    }


                    for (nose in whNoses) {
                        var txt = TextView(this)
                        txt.text = nose + "  "
                        txt.setTextColor(Color.parseColor("#323232"))
                        txt.textSize = 14f
                        binding.noteWhNoseLayout.addView(txt)
                    }

                    for (palate in whPalate) {
                        var txt = TextView(this)
                        txt.text = palate + "  "
                        txt.setTextColor(Color.parseColor("#323232"))
                        txt.textSize = 14f
                        binding.noteWhPalateLayout.addView(txt)
                    }

                    for (finish in whFinish) {
                        var txt = TextView(this)
                        txt.text = finish + "  "
                        txt.setTextColor(Color.parseColor("#323232"))
                        txt.textSize = 14f
                        binding.noteWhFinishLayout.addView(txt)
                    }

                    binding.noteWhSweet.progress = whiskeyDrink!!.whSweet
                    binding.noteWhSpicy.progress = whiskeyDrink!!.whSpicy
                    binding.noteWhBody.progress = whiskeyDrink!!.whBody

                }
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
                    // 위스키 id 받아오기 (위스키 삭제를 위해)
                    val whId = db!!.drinkDao().getDrink(drinkId).typeId
                    val whiskeyDrink = db!!.drinkDao().getDrinkWiskey(whId)

                    // 위스키 삭제
                    db!!.drinkDao().deleteDrinkWhiskey(whiskeyDrink)

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