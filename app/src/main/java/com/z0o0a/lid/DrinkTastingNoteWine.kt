package com.z0o0a.lid

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkTastingNoteWineBinding


class DrinkTastingNoteWine : AppCompatActivity() {
    private lateinit var binding: DrinkTastingNoteWineBinding

    private var drinkId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkTastingNoteWineBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        drinkId = intent.getIntExtra("drinkId",1)

        setDrink()
        setDrinkWine()

        binding.btnWiDel.setOnClickListener {
            delConfirm()
        }

        binding.noteWiBtnBack.setOnClickListener {
            finish()
        }

    }


    private fun setDrink(){
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(applicationContext)

            val drink = db!!.drinkDao().getDrink(drinkId)

            runOnUiThread {
                binding.noteWiImg.visibility = View.VISIBLE
                binding.noteWiImg.setImageBitmap(drink!!.drinkImg)

                binding.noteWiEngName.text = drink!!.drinkEngName
                binding.noteWiKrName.text = drink!!.drinkKrName
                binding.noteWiType.text = drink!!.drinkType
                binding.noteWiRating.text = drink!!.drinkRating.toString()

                binding.noteWiOverallStr.text = drink!!.drinkOverallStr
                binding.noteWiKeepDate.text = drink!!.drinkKeepDate
                binding.noteWiPlace.text = drink!!.drinkPlace
                binding.noteWiPostingDate.text = drink!!.drinkPostingDate
                binding.noteWiRegion.text = drink!!.drinkRegion
                binding.noteWiPrice.text = drink!!.drinkPrice

            }
        }).start()
    }

    private fun setDrinkWine(){
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(applicationContext)

            val wiId = db!!.drinkDao().getDrink(drinkId).typeId
            val wineDrink = db!!.drinkDao().getDrinkWine(wiId)

            val wiNoses : MutableList<String> = wineDrink!!.wiNose

            runOnUiThread {
                binding.noteWiClarity.progress = wineDrink!!.wiClarity
                binding.noteWiBrightness.progress = wineDrink!!.wiBrightness
                binding.noteWiTear.progress = wineDrink!!.wiTears
                if(wineDrink!!.wiRimVariation){
                    binding.noteWiRimArea.progress = wineDrink!!.wiRimArea
                } else{
                    binding.noteWiRimArea.visibility = View.GONE
                    binding.txtNoteWiRimArea.visibility = View.GONE

                    // TODO : 방법 찾기
                    // binding.txtNoteWiNose.TopMargins = 60
                }

                for (nose in wiNoses){
                    var txt = TextView(this)
                    txt.text = nose + "  "
                    txt.setTextColor(Color.parseColor("#323232"))
                    txt.textSize = 14f
                    binding.noteWiNoseLayout.addView(txt)
                }

                binding.noteWiSweet.progress = wineDrink!!.wiSweet
                binding.noteWiAcidity.progress = wineDrink!!.wiAcidity
                binding.noteWiBody.progress = wineDrink!!.wiBody
                binding.noteWiTannin.progress = wineDrink!!.wiTannin

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
                    // 위스키 id 받아오기 (와인 삭제를 위해)
                    val wiId = db!!.drinkDao().getDrink(drinkId).typeId
                    val wineDrink = db!!.drinkDao().getDrinkWine(wiId)

                    // 위스키 삭제
                    db!!.drinkDao().deleteDrinkWine(wineDrink)

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