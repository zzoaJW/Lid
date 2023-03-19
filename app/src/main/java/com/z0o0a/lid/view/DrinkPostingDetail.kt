package com.z0o0a.lid.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.z0o0a.lid.MainHost
import com.z0o0a.lid.R
import com.z0o0a.lid.databinding.DrinkPostingDetailBinding
import com.z0o0a.lid.viewmodel.DrinkPostingVM
import java.text.SimpleDateFormat
import java.util.*

class DrinkPostingDetail : Fragment() {
    private lateinit var binding: DrinkPostingDetailBinding
    private val vm: DrinkPostingVM by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DrinkPostingDetailBinding.inflate(layoutInflater)
        binding.vm = vm
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDrinkImg()

        binding.btnDrinkBack.setOnClickListener {
            findNavController().navigate(R.id.drinkPostingMedia)
        }

        binding.btnCancel.setOnClickListener {
            showConfirmDialog()
        }

        binding.btnFinish.setOnClickListener {
            try{
                saveDrink()
                Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), MainHost::class.java)
                startActivity(intent)

                activity?.finish()
            }catch (e:Exception){
                Toast.makeText(requireContext(), "저장에 실패하였습니다. 관리자에 문의해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.switchDrinkKeep.setOnClickListener {
            setDatePickerVisible()
        }

        binding.drinkKeepDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setDrinkImg(){
        val drinkImg = vm.drink.value!!.drinkImg

        Glide.with(this)
            .load(drinkImg)
            .error(R.drawable.bottle)
            .fallback(R.drawable.bottle)
            .into(binding.postingDrinkImg)
    }

    private fun showConfirmDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle("작성을 취소하시겠습니까?")
            .setPositiveButton("네") { _, _ ->
                val intent = Intent(requireContext(), MainHost::class.java)
                startActivity(intent)
            }
            .setNegativeButton("아니오") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun setDatePickerVisible(){
        if (binding.txtDrinkKeep.isVisible) {
            binding.txtDrinkKeep.visibility = View.INVISIBLE
            binding.drinkKeepDate.visibility = View.VISIBLE
        } else {
            binding.txtDrinkKeep.visibility = View.VISIBLE
            binding.drinkKeepDate.visibility = View.INVISIBLE
        }
    }

    private fun showDatePickerDialog(){
        val cal = Calendar.getInstance()    //캘린더뷰 만들기
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val dateString = "${year}.${month+1}.${dayOfMonth}"
            binding.drinkKeepDate.text = dateString
        }
        DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun getCurrentDate(): String {
        val now = System.currentTimeMillis()
        val y = SimpleDateFormat("yyyy", Locale.KOREAN).format(now).toInt()
        val m = SimpleDateFormat("MM", Locale.KOREAN).format(now).toInt()
        val d = SimpleDateFormat("dd", Locale.KOREAN).format(now).toInt()

        return "${y}.${m}.${d}"
    }

    private fun saveDrink(){
        vm.drink.value!!.drinkPostingDate = getCurrentDate()

        vm.insertDrink()
    }
}