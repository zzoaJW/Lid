package com.z0o0a.lid

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkTastingNoteBeerBinding

class DrinkTastingNoteBeer : AppCompatActivity() {
    private lateinit var binding: DrinkTastingNoteBeerBinding

    private var drinkId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkTastingNoteBeerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        drinkId = intent.getIntExtra("drinkId",1)

        setDrink()
        setDrinkWhiskey()

        binding.btnBDel.setOnClickListener {
            delConfirm()
        }

        binding.noteBBtnBack.setOnClickListener {
            finish()
        }
    }



    private fun setDrink(){
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(applicationContext)

            val drink = db!!.drinkDao().getDrink(drinkId)

            runOnUiThread {
                binding.noteBImg.visibility = View.VISIBLE
                binding.noteBImg.setImageBitmap(drink!!.drinkImg)

                binding.noteBEngName.text = drink!!.drinkEngName
                binding.noteBKrName.text = drink!!.drinkKrName
                binding.noteBType.text = drink!!.drinkType
                binding.noteBRating.text = drink!!.drinkRating.toString()

                binding.noteBOverallStr.text = drink!!.drinkOverallStr
                binding.noteBKeepDate.text = drink!!.drinkKeepDate
                binding.noteBPlace.text = drink!!.drinkPlace
                binding.noteBPostingDate.text = drink!!.drinkPostingDate
                binding.noteBRegion.text = drink!!.drinkRegion
                binding.noteBPrice.text = drink!!.drinkPrice

            }
        }).start()
    }

    private fun setDrinkWhiskey(){
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(applicationContext)

            val bId = db!!.drinkDao().getDrink(drinkId).typeId
            val bDrink = db!!.drinkDao().getDrinkBeer(bId)

            if (bDrink.bShort) {
                runOnUiThread {
                    binding.noteBColorCircleBeer.visibility = View.GONE
                    binding.noteBColorCircleFoam.visibility = View.GONE
                    binding.noteBDetailLayout.visibility = View.GONE
                }
            } else {
                runOnUiThread {

                    if (bDrink!!.bHeadColor != "") {
                        binding.noteBColorCircleFoam.background.setTint(Color.parseColor(bDrink!!.bHeadColor))
                    } else {
                        binding.noteBColorCircleFoam.visibility = View.INVISIBLE
                    }

                    if (bDrink!!.bColor != "") {
                        binding.noteBColorCircleBeer.background.setTint(Color.parseColor(bDrink!!.bColor))
                    } else {
                        binding.noteBColorCircleBeer.visibility = View.INVISIBLE
                    }

                    binding.noteBClarity.progress = bDrink!!.bClarity

                    binding.noteBHeadRetention.progress = bDrink!!.bHeadRetention
                    binding.noteBHeadDensity.progress = bDrink!!.bHeadDensity

                    binding.noteBAromaMalt.progress = bDrink!!.bAromaMalt
                    binding.noteBAromaHops.progress = bDrink!!.bAromaHops
                    binding.noteBAromaFermentation.progress = bDrink!!.bAromaFermentation
                    binding.noteBAromaOther.text = bDrink!!.bAromaOther

                    binding.noteBFlavorMalt.progress = bDrink!!.bFlavorMalt
                    binding.noteBFlavorHops.progress = bDrink!!.bFlavorHops
                    binding.noteBFlavorFermentation.progress = bDrink!!.bFlavorFermentation
                    binding.noteBFlavorFinish.progress = bDrink!!.bFlavorFinish

                    binding.noteBBody.progress = bDrink!!.bBody
                    binding.noteBCarbonation.progress = bDrink!!.bCarbonation
                    binding.noteBAstringent.progress = bDrink!!.bAstringent

                }
            }
        }).start()
    }







    private fun delConfirm(){
        AlertDialog.Builder(this)
            .setTitle("????????? ?????????????????????????")
            .setMessage("(?????? ??? ????????? ??????????????????.)")
            .setPositiveButton("???") { dialog, which -> // ?????? ?????? ??????

                Thread(Runnable {
                    val db = DrinkDatabase.getInstance(applicationContext)
                    // ????????? id ???????????? (????????? ????????? ??????)
                    val bId = db!!.drinkDao().getDrink(drinkId).typeId
                    val beerDrink = db!!.drinkDao().getDrinkBeer(bId)

                    // ????????? ??????
                    db!!.drinkDao().deleteDrinkBeer(beerDrink)

                    val drink = db!!.drinkDao().getDrink(drinkId)
                    // drink ??????
                    db!!.drinkDao().deleteDrink(drink)
                }).start()

                // ?????? ???????????? ????????????
                finish()
            }
            .setNegativeButton("?????????", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })
            .create()
            .show()
    }
}