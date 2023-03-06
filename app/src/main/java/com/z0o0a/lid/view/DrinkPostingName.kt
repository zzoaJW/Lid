package com.z0o0a.lid.view

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.z0o0a.lid.DrinkPostingImg
import com.z0o0a.lid.R
import com.z0o0a.lid.databinding.DrinkPostingNameBinding
import com.z0o0a.lid.viewmodel.DrinkPostingVM
import java.util.regex.Pattern

class DrinkPostingName : AppCompatActivity() {
    private lateinit var vm: DrinkPostingVM
    private lateinit var binding : DrinkPostingNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.drink_posting_name)
        vm = ViewModelProvider(this)[DrinkPostingVM::class.java]
        binding.vm = vm
        binding.lifecycleOwner = this

        setEngNameFilter()
        setKrNameFilter()

        binding.btnNext.setOnClickListener {
            if(vm.drink.value!!.drinkEngName == "" && vm.drink.value!!.drinkKrName == ""){  // 이름 null
                Toast.makeText(this, "이름을 작성해주세요.", Toast.LENGTH_SHORT).show()
            }else if (vm.drink.value!!.drinkType == ""){  // 타입 null
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

    private fun setEngNameFilter(){
        /*
            [정규식 패턴 요약]
            1. ^[a-z] : 영어 소문자 허용
            2. ^[A-Z] : 영어 대문자 허용
            3. ^[ㄱ-ㅣ가-힣] : 한글 허용
            4. ^[0-9] : 숫자 허용
            5. ^[ ] or ^[\\s] : 공백 허용
        */

        val engNameFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[a-zA-Z0-9'_\\-\\s]+$")

            if (!ps.matcher(source).matches()) { "" }
            else source
        }

        binding.inputDrinkEngName.filters = arrayOf(engNameFilter)
    }

    private fun setKrNameFilter(){
        val krNameFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val ps = Pattern.compile("^[ㄱ-ㅣ가-힣0-9'_\\-\\s]+$")

            if (!ps.matcher(source).matches()) { "" }
            else source
        }

        binding.inputDrinkKrName.filters = arrayOf(krNameFilter)
    }
}