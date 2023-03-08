package com.z0o0a.lid.view

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.z0o0a.lid.R
import com.z0o0a.lid.databinding.DrinkPostingTitleBinding
import com.z0o0a.lid.viewmodel.DrinkPostingVM
import java.util.regex.Pattern

class DrinkPostingTitle : Fragment() {
    private lateinit var binding : DrinkPostingTitleBinding
    private val vm: DrinkPostingVM by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DrinkPostingTitleBinding.inflate(layoutInflater)
        binding.vm = vm
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEngNameFilter()
        setKrNameFilter()

        binding.btnNext.setOnClickListener {
            if(vm.drink.value!!.drinkEngName == "" && vm.drink.value!!.drinkKrName == ""){  // 이름 null
                Toast.makeText(requireContext(), "이름을 작성해주세요.", Toast.LENGTH_SHORT).show()
            }else if (vm.drink.value!!.drinkType == ""){  // 타입 null
                Toast.makeText(requireContext(), "종류를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else{
                findNavController().navigate(R.id.drinkPostingMedia)
            }
        }

        binding.btnBack.setOnClickListener {
            activity?.finish()
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