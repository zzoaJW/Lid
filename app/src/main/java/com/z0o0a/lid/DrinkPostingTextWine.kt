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
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.z0o0a.lid.databinding.DrinkPostingTextWineBinding
import java.text.SimpleDateFormat
import java.util.*

class DrinkPostingTextWine : AppCompatActivity() {
    private lateinit var binding: DrinkPostingTextWineBinding

    var drinkImg : Bitmap? = null
    var drinkEngName = ""
    var drinkKrName = ""
    var drinkType = ""

    var wiId : Long = 0
    var wiType = "-" // 소분류 (일단 다 -로 저장)

    var wiClarity = 3
    var wiBrightness = 3
    var wiTears = 3

    var wiRimVariation = false
    var wiRimArea = 3
    var wiRimColor = "" // 일단 디폴드 값으로 저장
    var wiCoreColor = "" // 일단 디폴드 값으로 저장

    val wiNoses : MutableList<String> = mutableListOf()
    var wiSweet = 3
    var wiAcidity = 3
    var wiBody = 3
    var wiTannin = 3

    var wiRating = 0f
    var wiOverallStr = ""
    var wiRegion = ""
    var wiPrice = ""

    var wiPlace = ""
    var wiKeepDate = ""
    var wiPostingDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingTextWineBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getSingletonValues()
        setSingletonValuesToUI()

        binding.btnWineBack.setOnClickListener {
            finish()
        }

        binding.btnWineCancel.setOnClickListener {
            cancelConfirm()
        }

        binding.btnWineDetail.setOnClickListener {
            hideDetail()
        }

        binding.switchPostWiRimVariation.setOnClickListener{
            if (binding.postWiRimLayout.isVisible){
                wiRimVariation = false
                binding.postWiRimLayout.visibility = View.GONE
            }else{
                wiRimVariation = true
                binding.postWiRimLayout.visibility = View.VISIBLE
            }
        }

        binding.postWiNoseChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            wiNoses.clear()
            for (id in checkedIds) {
                var c: Chip = group.findViewById(id)
                var c_t = c.text
                wiNoses.add(c_t.toString())
            }
        }

        binding.switchWineKeep.setOnClickListener {
            if (binding.txtWineKeep.isVisible) {
                binding.txtWineKeep.visibility = View.INVISIBLE
                binding.wineKeepDate.visibility = View.VISIBLE
            } else {
                binding.wineKeepDate.text = "개봉일 선택"
                binding.wineKeepDate.visibility = View.INVISIBLE
                binding.txtWineKeep.visibility = View.VISIBLE
            }
        }

        binding.wineKeepDate.setOnClickListener {
            showKeepDateDialog()
        }

        binding.btnWinePosting.setOnClickListener {
            getValues()

            saveWine()
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

    private fun hideDetail(){
        if (binding.wineDetailLayout.visibility == View.VISIBLE) {
            binding.wineDetailLayout.visibility = View.GONE

            binding.btnWineDetail.setTextColor(Color.parseColor("#E0F14E"))
        } else{
            binding.wineDetailLayout.visibility = View.VISIBLE

            binding.btnWineDetail.setTextColor(Color.parseColor("#CDCDCD"))
        }
    }

    private fun showKeepDateDialog(){
        var dateString = ""

        val cal = Calendar.getInstance()    //캘린더뷰 만들기
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}.${month+1}.${dayOfMonth}"
            binding.wineKeepDate.text = dateString
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
            Calendar.DAY_OF_MONTH)).show()
    }

    private fun getSingletonValues(){
        val postingSingleton = PostingDrinkSingleton.getInstance(applicationContext)

        drinkImg = postingSingleton?.drinkImg
        drinkEngName = postingSingleton?.drinkEngName.toString()
        drinkKrName = postingSingleton?.drinkKrName.toString()
        drinkType = postingSingleton?.drinkType.toString()

    }

    private fun setSingletonValuesToUI(){
        binding.postingWineImg.setImageBitmap(drinkImg)
        binding.postingWineEngName.text = drinkEngName
        binding.postingWineKrName.text = drinkKrName
        binding.postingWineType.text = drinkType
    }

    private fun getValues(){
        getAppearance()
        getPalate()

        // getRating
        wiRating = binding.postWRatingBar.rating
        // getOverallStr
        wiOverallStr = binding.wiOverallStr.text.toString()

        getInfo()
    }

    private fun getAppearance(){
        wiClarity = binding.postWiClarity.progress
        wiBrightness = binding.postWiBrightness.progress
        wiTears  = binding.postWiTear.progress
        wiRimArea = binding.postWiRimArea.progress
    }

    private fun getPalate(){
        wiSweet = binding.postWiSweet.progress
        wiAcidity = binding.postWiAcidity.progress
        wiBody = binding.postWiBody.progress
        wiTannin = binding.postWiTannin.progress
    }

    private fun getInfo(){
        wiRegion = binding.wineRegion.text.toString()
        wiPrice = binding.winePrice.text.toString()
        wiPlace = binding.winePlace.text.toString()

        wiKeepDate = binding.wineKeepDate.text.toString()
        if (wiKeepDate == "개봉일 선택") {
            wiKeepDate = "보관 안함"
        }
        wiPostingDate = getCurrentDate()
    }

    fun getCurrentDate(): String {
        val now = System.currentTimeMillis()

        return SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN).format(now)
    }

    private fun saveWine() {
        val run : Runnable = Runnable {
            var drinkWine = DrinkWine(0,
                wiType,
                wiClarity,
                wiBrightness,
                wiTears,
                wiRimVariation,
                wiRimArea,
                wiRimColor,
                wiCoreColor,
                wiNoses,
                wiSweet,
                wiAcidity,
                wiBody,
                wiTannin
            )

            val db = DrinkDatabase.getInstance(applicationContext)
            var returnWiId = db!!.drinkDao().insertDrinkWine(drinkWine)

            wiId = returnWiId
        }

        val t = Thread(run)
        t.start()

        try {
            t.join()
        }catch (e : InterruptedException){
            Log.d("와인 저장 실패","실패, 예외 발생")
        }
    }

    private fun saveDrink() {
        Thread(Runnable {
            var newDrink = Drink(0,
                drinkImg,
                drinkEngName,
                drinkKrName,
                drinkType,
                wiId,
                wiOverallStr,
                wiRating,
                wiRegion,
                wiPrice,
                wiKeepDate,
                wiPlace,
                wiPostingDate)

            val db = DrinkDatabase.getInstance(applicationContext)
            db!!.drinkDao().insertDrink(newDrink)
        }).start()
    }
}