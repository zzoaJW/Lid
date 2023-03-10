package com.z0o0a.lid

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.chip.Chip
import com.z0o0a.lid.databinding.DrinkPostingDetailWhiskeyBinding
import com.z0o0a.lid.model.PostingDrinkSingleton
import com.z0o0a.lid.repository.DrinkDatabase
import com.z0o0a.lid.viewmodel.DrinkPostingVM
import java.text.SimpleDateFormat
import java.util.*


class DrinkPostingDetailWhiskey : Fragment() {
    private lateinit var binding: DrinkPostingDetailWhiskeyBinding
    private val vm: DrinkPostingVM by activityViewModels()

    val whNoses : MutableList<String> = mutableListOf()
    val whPalates : MutableList<String> = mutableListOf()
    val whFinishs : MutableList<String> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DrinkPostingDetailWhiskeyBinding.inflate(layoutInflater)
        binding.vm = vm
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDrinkImg()

        binding.btnWhiskeyBack.setOnClickListener {
            findNavController().navigate(R.id.drinkPostingMedia)
        }

        binding.btnWhiskeyDetail.setOnClickListener {
            hideWhDetail()
        }

        binding.btnWhiskeyColor.setOnClickListener {
            showColorDialog()
        }


        binding.whiskeyNoseChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            whNoses.clear()
            for (id in checkedIds) {
                var c: Chip = group.findViewById(id)
                var c_t = c.text
                whNoses.add(c_t.toString())
            }
        }

        binding.whiskeyPalateChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            whPalates.clear()
            for (id in checkedIds) {
                var c: Chip = group.findViewById(id)
                var c_t = c.text
                whPalates.add(c_t.toString())
            }
        }

        binding.whiskeyFinishChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            whFinishs.clear()
            for (id in checkedIds) {
                var c: Chip = group.findViewById(id)
                var c_t = c.text
                whFinishs.add(c_t.toString())
            }
        }


        binding.switchWhiskeyKeep.setOnClickListener {
            setDatePickerVisible()
        }

        binding.whiskeyKeepDate.setOnClickListener {
            showDatePickerDialog()
        }


        binding.btnWhiskeyCancel.setOnClickListener {
            cancelConfirm()
        }

        binding.btnWhiskeyPosting.setOnClickListener {
            try{
                saveDrink()
                Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)

                activity?.finish()
            }catch (e:Exception){
                Toast.makeText(requireContext(), "저장에 실패하였습니다. 관리자에 문의해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun hideWhDetail(){
        if (binding.detailLayout.visibility == View.VISIBLE) {
            vm.drinkWhiskey.value!!.whShort = true
            binding.detailLayout.visibility = View.GONE
            binding.btnWhiskeyDetail.setTextColor(Color.parseColor("#E0F14E"))
        } else{
            vm.drinkWhiskey.value!!.whShort = false
            binding.detailLayout.visibility = View.VISIBLE
            binding.btnWhiskeyDetail.setTextColor(Color.parseColor("#CDCDCD"))
        }
    }

    private fun setDrinkImg(){
        val drinkImg = vm.drink.value!!.drinkImg

        if(drinkImg == null){
            binding.postingWhiskeyImg.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.bottle))
        }else{
            binding.postingWhiskeyImg.setImageBitmap(drinkImg)
        }
    }

    private fun showColorDialog(){
        MaterialColorPickerDialog
            .Builder(requireContext()) // Pass Activity Instance
            .setTitle("") // Dialog 제목
            .setColorShape(ColorShape.SQAURE) // 컬러칩 모양
            .setColors(resources.getStringArray(R.array.whiskey_colors)) // 컬러 구성
//                .setDefaultColor("#FCEE97") // Pass Default Color
            .setColorListener { color, colorHex ->
                binding.btnWhiskeyColor.setBackgroundColor(colorHex.toColorInt())
                vm.drinkWhiskey.value!!.whColor = colorHex
            }
            .show()
    }

    // TODO : data binding으로 해결하기
    private fun setWhNosePalateFinish(){
        vm.drinkWhiskey.value!!.whNose = whNoses
        vm.drinkWhiskey.value!!.whPalate = whPalates
        vm.drinkWhiskey.value!!.whFinish = whFinishs
    }

    private fun setDatePickerVisible(){
        if (binding.txtWhiskeyKeep.isVisible) {
            binding.txtWhiskeyKeep.visibility = View.INVISIBLE
            binding.whiskeyKeepDate.visibility = View.VISIBLE
        } else {
            binding.txtWhiskeyKeep.visibility = View.VISIBLE
            binding.whiskeyKeepDate.visibility = View.INVISIBLE
        }
    }

    private fun showDatePickerDialog(){
        val cal = Calendar.getInstance()    //캘린더뷰 만들기
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val dateString = "${year}.${month+1}.${dayOfMonth}"
            binding.whiskeyKeepDate.text = dateString
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
        setWhNosePalateFinish()
        vm.drink.value!!.drinkPostingDate = getCurrentDate()

        vm.insertDrinkWhiskey()
        vm.insertDrink()
    }

    private fun cancelConfirm(){
        AlertDialog.Builder(requireContext())
            .setTitle("작성을 취소하시겠습니까?")
            .setPositiveButton("네") { dialog, which ->
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
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