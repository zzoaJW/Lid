package com.z0o0a.lid.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
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
import com.google.android.material.chip.Chip
import com.z0o0a.lid.MainHost
import com.z0o0a.lid.R
import com.z0o0a.lid.databinding.DrinkPostingDetailWineBinding
import com.z0o0a.lid.viewmodel.DrinkPostingVM
import java.text.SimpleDateFormat
import java.util.*

class DrinkPostingDetailWine : Fragment()  {
    private lateinit var binding: DrinkPostingDetailWineBinding
    private val vm: DrinkPostingVM by activityViewModels()

    val wiNoses : MutableList<String> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DrinkPostingDetailWineBinding.inflate(layoutInflater)
        binding.vm = vm
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDrinkImg()

        binding.btnWineBack.setOnClickListener {
            findNavController().navigate(R.id.drinkPostingMedia)
        }

        binding.btnWineCancel.setOnClickListener {
            cancelConfirm()
        }

        binding.btnWineDetail.setOnClickListener {
            hideWiDetail()
        }

        binding.switchPostWiRimVariation.setOnClickListener{
            setRimVariationVisible()
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
            setDatePickerVisible()
        }

        binding.wineKeepDate.setOnClickListener {
            showKeepDateDialog()
        }

        binding.btnWinePosting.setOnClickListener {
            try {
                saveDrink()
                Toast.makeText(requireContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), MainHost::class.java)
                startActivity(intent)

                activity?.finish()
            }catch (e:Exception){
                Toast.makeText(requireContext(), "저장에 실패하였습니다. 관리자에 문의해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setDrinkImg(){
        val drinkImg = vm.drink.value!!.drinkImg

        Glide.with(this)
            .load(drinkImg)
            .error(R.drawable.bottle)
            .fallback(R.drawable.bottle)
            .into(binding.postingWineImg)
    }

    private fun hideWiDetail(){
        if (binding.wineDetailLayout.visibility == View.VISIBLE) {
            vm.drinkWine.value!!.wiShort = true
            binding.wineDetailLayout.visibility = View.GONE
            binding.btnWineDetail.setTextColor(Color.parseColor("#E0F14E"))
        } else{
            vm.drinkWine.value!!.wiShort = false
            binding.wineDetailLayout.visibility = View.VISIBLE
            binding.btnWineDetail.setTextColor(Color.parseColor("#CDCDCD"))
        }
    }

    private fun setRimVariationVisible() {
        if (binding.postWiRimLayout.isVisible){
            vm.drinkWine.value!!.wiRimVariation = false
            binding.postWiRimLayout.visibility = View.GONE
        }else{
            vm.drinkWine.value!!.wiRimVariation = true
            binding.postWiRimLayout.visibility = View.VISIBLE
        }
    }

    // TODO : data binding으로 해결하기
    private fun setWiNose(){
        vm.drinkWine.value!!.wiNose = wiNoses
    }

    private fun setDatePickerVisible(){
        if (binding.txtWineKeep.isVisible) {
            binding.txtWineKeep.visibility = View.INVISIBLE
            binding.wineKeepDate.visibility = View.VISIBLE
        } else {
            binding.txtWineKeep.visibility = View.VISIBLE
            binding.wineKeepDate.visibility = View.INVISIBLE
        }
    }

    private fun showKeepDateDialog(){
        val cal = Calendar.getInstance()    //캘린더뷰 만들기
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val dateString = "${year}.${month+1}.${dayOfMonth}"
            binding.wineKeepDate.text = dateString
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

    private fun saveDrink() {
        setWiNose()
        vm.drink.value!!.drinkPostingDate = getCurrentDate()

        vm.insertDrinkWine()
        vm.insertDrink()
    }

    private fun cancelConfirm(){
        AlertDialog.Builder(requireContext())
            .setTitle("작성을 취소하시겠습니까?")
            .setPositiveButton("네", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val intent = Intent(requireContext(), MainHost::class.java)
                    startActivity(intent)

                    activity?.finish()
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
}