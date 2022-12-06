package com.z0o0a.lid

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.z0o0a.lid.databinding.DrinkPostingNameBinding
import java.util.regex.Pattern

class DrinkPostingName : AppCompatActivity() {
    private lateinit var binding: DrinkPostingNameBinding

    var drinkEngName = ""
    var drinkKrName = ""
    var drinkType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrinkPostingNameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var filterEngNumSpace = InputFilter { source, start, end, dest, dstart, dend ->
            /*
                [요약 설명]
                1. 정규식 패턴 ^[a-z] : 영어 소문자 허용
                2. 정규식 패턴 ^[A-Z] : 영어 대문자 허용
                3. 정규식 패턴 ^[ㄱ-ㅣ가-힣] : 한글 허용
                4. 정규식 패턴 ^[0-9] : 숫자 허용
                5. 정규식 패턴 ^[ ] or ^[\\s] : 공백 허용
            */
            val ps = Pattern.compile("^[a-zA-Z0-9'\\s]+$")
            if (!ps.matcher(source).matches()) {
                ""
            } else source
        }

        var filterKrNumSpace = InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[ㄱ-ㅣ가-힣0-9'\\s]+$")
            if (!ps.matcher(source).matches()) {
                ""
            } else source
        }

        binding.inputDrinkEngName.filters = arrayOf(filterEngNumSpace)
        binding.inputDrinkKrName.filters = arrayOf(filterKrNumSpace)


        binding.radioGroupDrinkType.setOnCheckedChangeListener { radioGroup, radioButtonid ->
            when(radioButtonid){
                R.id.radio_btn_wiskey -> {
                    binding.editTxtEtcType.visibility = View.INVISIBLE
                    drinkType = "위스키"
                }
                R.id.radio_btn_wine -> {
                    binding.editTxtEtcType.visibility = View.INVISIBLE
                    drinkType = "와인"
                }
                R.id.radio_btn_beer -> {
                    binding.editTxtEtcType.visibility = View.INVISIBLE
                    drinkType = "맥주"
                }
                R.id.radio_btn_etc -> {
                    binding.editTxtEtcType.visibility = View.VISIBLE
                    drinkType = ""
                }
            }
        }

        binding.btnNext.setOnClickListener {
            drinkEngName = binding.inputDrinkEngName.text.toString()
            drinkKrName = binding.inputDrinkKrName.text.toString()
            if (binding.radioBtnEtc.isChecked && drinkType=="") {
                var temp = binding.editTxtEtcType.text.toString()
                if (temp == "") {
                    drinkType = "기타"
                }else{
                    drinkType = temp
                }
            }

            setPostingSingleton(drinkEngName, drinkKrName, drinkType)

            if (binding.radioBtnEtc.isChecked){
                drinkType = binding.radioBtnEtc.text.toString()
            }

            if(drinkEngName == "" && drinkKrName == ""){
                Toast.makeText(this, "이름을 작성해주세요.", Toast.LENGTH_SHORT).show()
            }else if (drinkType == ""){
                Toast.makeText(this, "종류를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else{
                intent = Intent(this, DrinkPostingImg::class.java)
                startActivity(intent)
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    fun setPostingSingleton(engName:String, krName:String, type:String){
        val postingSingleton = PostingDrinkSingleton.getInstance(applicationContext)

        postingSingleton?.drinkEngName = engName
        postingSingleton?.drinkKrName = krName
        postingSingleton?.drinkType = type
    }
}