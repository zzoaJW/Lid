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
import com.z0o0a.lid.databinding.DrinkPostingDetailBeerBinding
import com.z0o0a.lid.model.PostingDrinkSingleton
import com.z0o0a.lid.repository.DrinkDatabase
import java.text.SimpleDateFormat
import java.util.*

class DrinkPostingDetailBeer : AppCompatActivity() {
    private lateinit var binding: DrinkPostingDetailBeerBinding

    var bShort = false

    var drinkImg : Bitmap? = null
    var drinkEngName = ""
    var drinkKrName = ""
    var drinkType = ""

    var bId : Long = 0
    var bType = "-" // 소분류 (일단 다 -로 저장)

    var bColor = ""
    var bClarity = 3

    var bHeadColor = ""
    var bHeadRetention = 3
    var bHeadDensity = 3

    var bAromaMalt = 3
    var bAromaHops = 3
    var bAromaFermentation = 3
    var bAromaOther = ""

    var bFlavorMalt = 3
    var bFlavorHops = 3
    var bFlavorFermentation = 3
    var bFlavorFinish = 3

    var bBody = 3
    var bCarbonation = 3
    var bAstringent = 3

    var bRating = 0f
    var bOverallStr = ""
    var bRegion = ""
    var bPrice = ""

    var bPlace = ""
    var bKeepDate = ""
    var bPostingDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingDetailBeerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getSingletonValues()
        setSingletonValuesToUI()

        binding.btnBeerBack.setOnClickListener {
            finish()
        }

        binding.btnBeerCancel.setOnClickListener {
            cancelConfirm()
        }

        binding.btnBeerDetail.setOnClickListener {
            hideDetail()
        }

        binding.btnBeerColor.setOnClickListener {
            showColorDialog("beer")
        }

        binding.btnHeadColor.setOnClickListener {
            showColorDialog("head")
        }

        binding.switchBeerKeep.setOnClickListener {
            if (binding.txtBeerKeep.isVisible) {
                binding.txtBeerKeep.visibility = View.INVISIBLE
                binding.beerKeepDate.visibility = View.VISIBLE
            } else {
                binding.beerKeepDate.text = "개봉일 선택"
                binding.beerKeepDate.visibility = View.INVISIBLE
                binding.txtBeerKeep.visibility = View.VISIBLE
            }
        }

        binding.beerKeepDate.setOnClickListener {
            showKeepDateDialog()
        }

        binding.btnBeerPosting.setOnClickListener {
            getValues()

            saveBeer()
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

    private fun getSingletonValues(){
        val postingSingleton = PostingDrinkSingleton.getInstance(applicationContext)

        drinkImg = postingSingleton?.drinkImg
        drinkEngName = postingSingleton?.drinkEngName.toString()
        drinkKrName = postingSingleton?.drinkKrName.toString()
        drinkType = postingSingleton?.drinkType.toString()

    }

    private fun setSingletonValuesToUI(){
        binding.postingBeerImg.setImageBitmap(drinkImg)
        binding.postingBeerEngName.text = drinkEngName
        binding.postingBeerKrName.text = drinkKrName
        binding.postingBeerType.text = drinkType
    }

    private fun hideDetail(){
        if (binding.beerDetailLayout.visibility == View.VISIBLE) {
            bShort = true
            binding.beerDetailLayout.visibility = View.GONE
            binding.btnBeerDetail.setTextColor(Color.parseColor("#E0F14E"))
        } else{
            bShort = false
            binding.beerDetailLayout.visibility = View.VISIBLE
            binding.btnBeerDetail.setTextColor(Color.parseColor("#CDCDCD"))
        }
    }

    private fun showColorDialog(whatsColor : String){
        MaterialColorPickerDialog
            .Builder(this) // Pass Activity Instance
            .setTitle("") // Dialog 제목
            .setColorShape(ColorShape.SQAURE) // 컬러칩 모양
            .setColors(resources.getStringArray(R.array.beer_colors)) // 컬러 구성
//                .setDefaultColor("#FCEE97") // Pass Default Color
            .setColorListener { color, colorHex ->
                if(whatsColor == "head"){
                    binding.btnHeadColor.setBackgroundColor(colorHex.toColorInt())
                    bHeadColor = colorHex
                }else{
                    binding.btnBeerColor.setBackgroundColor(colorHex.toColorInt())
                    bColor = colorHex
                }
            }
            .show()
    }

    private fun showKeepDateDialog(){
        var dateString = ""

        val cal = Calendar.getInstance()    //캘린더뷰 만들기
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}.${month+1}.${dayOfMonth}"
            binding.beerKeepDate.text = dateString
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
            Calendar.DAY_OF_MONTH)).show()
    }

    private fun getValues(){
        getAppearanceHead()
        getAroma()
        getFlavor()
        getMouthFeel()

        // getRating
        bRating = binding.postBRatingBar.rating
        // getOverallStr
        bOverallStr = binding.postBOverallStr.text.toString()

        getInfo()
    }

    private fun getAppearanceHead(){
        bClarity = binding.postBClarity.progress
        bHeadRetention = binding.postBHeadRetention.progress
        bHeadDensity = binding.postBHeadDensity.progress
    }

    private fun getAroma(){
        bAromaMalt = binding.postBAromaMalt.progress
        bAromaHops = binding.postBAromaHop.progress
        bAromaFermentation = binding.postBAromaFermentation.progress
        bAromaOther = binding.postBAromaOther.text.toString()
    }

    private fun getFlavor(){
        bFlavorMalt = binding.postBFlavorMalt.progress
        bFlavorHops = binding.postBFlavorHop.progress
        bFlavorFermentation = binding.postBFlavorFermentation.progress
        bFlavorFinish = binding.postBFlavorFinish.progress
    }

    private fun getMouthFeel(){
        bBody = binding.postBBody.progress
        bCarbonation = binding.postBCarbonation.progress
        bAstringent = binding.postBAstringent.progress
    }

    private fun getInfo(){
        bRegion = binding.beerRegion.text.toString()
        bPrice = binding.beerPrice.text.toString()
        bPlace = binding.beerPlace.text.toString()

        bKeepDate = binding.beerKeepDate.text.toString()
        if (bKeepDate == "개봉일 선택") {
            bKeepDate = "보관 안함"
        }
        bPostingDate = getCurrentDate()
    }

    fun getCurrentDate(): String {
        val now = System.currentTimeMillis()
        val y = SimpleDateFormat("yyyy", Locale.KOREAN).format(now).toInt()
        val m = SimpleDateFormat("MM", Locale.KOREAN).format(now).toInt()
        val d = SimpleDateFormat("dd", Locale.KOREAN).format(now).toInt()

        return "${y}.${m}.${d}"
    }


    private fun saveBeer() {
        val run : Runnable = Runnable {
            var drinkBeer = DrinkBeer(0,
                bShort,
                bType,
                bColor,
                bClarity,
                bHeadColor,
                bHeadRetention,
                bHeadDensity,
                bAromaMalt,
                bAromaHops,
                bAromaFermentation,
                bAromaOther,
                bFlavorMalt,
                bFlavorHops,
                bFlavorFermentation,
                bFlavorFinish,
                bBody,
                bCarbonation,
                bAstringent
            )

            val db = DrinkDatabase.getInstance(applicationContext)
            var returnBId = db!!.drinkDao().insertDrinkBeer(drinkBeer)

            bId = returnBId
        }

        val t = Thread(run)
        t.start()

        try {
            t.join()
        }catch (e : InterruptedException){
            Log.d("맥주 저장 실패","예외 발생")
        }
    }

    private fun saveDrink() {
        Thread(Runnable {
            var newDrink = Drink(0,
                drinkImg,
                drinkEngName,
                drinkKrName,
                drinkType,
                bId,
                bOverallStr,
                bRating,
                bRegion,
                bPrice,
                bKeepDate,
                bPlace,
                bPostingDate)

            val db = DrinkDatabase.getInstance(applicationContext)
            db!!.drinkDao().insertDrink(newDrink)
        }).start()
    }
}