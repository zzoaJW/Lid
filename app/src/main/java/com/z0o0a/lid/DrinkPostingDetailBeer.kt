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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.z0o0a.lid.databinding.DrinkPostingDetailBeerBinding
import com.z0o0a.lid.databinding.DrinkPostingDetailWhiskeyBinding
import com.z0o0a.lid.model.PostingDrinkSingleton
import com.z0o0a.lid.repository.DrinkDatabase
import com.z0o0a.lid.viewmodel.DrinkPostingVM
import java.text.SimpleDateFormat
import java.util.*

class DrinkPostingDetailBeer : Fragment() {
    private lateinit var binding: DrinkPostingDetailBeerBinding
    private val vm: DrinkPostingVM by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DrinkPostingDetailBeerBinding.inflate(layoutInflater)
        binding.vm = vm
        binding.lifecycleOwner = requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDrinkImg()

        binding.btnBeerBack.setOnClickListener {
            findNavController().navigate(R.id.drinkPostingMedia)
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
            setDatePickerVisible()
        }

        binding.beerKeepDate.setOnClickListener {
            showKeepDateDialog()
        }

        binding.btnBeerPosting.setOnClickListener {
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


        binding.btnBeerCancel.setOnClickListener {
            cancelConfirm()
        }

    }

    private fun setDrinkImg(){
        val drinkImg = vm.drink.value!!.drinkImg

        if(drinkImg == null){
            binding.postingBeerImg.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.bottle))
        }else{
            binding.postingBeerImg.setImageBitmap(drinkImg)
        }
    }

    private fun hideDetail(){
        if (binding.beerDetailLayout.visibility == View.VISIBLE) {
            vm.drinkBeer.value!!.bShort = true
            binding.beerDetailLayout.visibility = View.GONE
            binding.btnBeerDetail.setTextColor(Color.parseColor("#E0F14E"))
        } else{
            vm.drinkBeer.value!!.bShort = false
            binding.beerDetailLayout.visibility = View.VISIBLE
            binding.btnBeerDetail.setTextColor(Color.parseColor("#CDCDCD"))
        }
    }

    private fun showColorDialog(whatsColor : String){
        MaterialColorPickerDialog
            .Builder(requireContext()) // Pass Activity Instance
            .setTitle("") // Dialog 제목
            .setColorShape(ColorShape.SQAURE) // 컬러칩 모양
            .setColors(resources.getStringArray(R.array.beer_colors)) // 컬러 구성
//                .setDefaultColor("#FCEE97") // Pass Default Color
            .setColorListener { color, colorHex ->
                if(whatsColor == "head"){
                    binding.btnHeadColor.setBackgroundColor(colorHex.toColorInt())
                    vm.drinkBeer.value!!.bHeadColor = colorHex
                }else{
                    binding.btnBeerColor.setBackgroundColor(colorHex.toColorInt())
                    vm.drinkBeer.value!!.bColor = colorHex
                }
            }
            .show()
    }

    private fun setDatePickerVisible(){
        if (binding.txtBeerKeep.isVisible) {
            binding.txtBeerKeep.visibility = View.INVISIBLE
            binding.beerKeepDate.visibility = View.VISIBLE
        } else {
            binding.txtBeerKeep.visibility = View.VISIBLE
            binding.beerKeepDate.visibility = View.INVISIBLE
        }
    }

    private fun showKeepDateDialog(){
        val cal = Calendar.getInstance()    //캘린더뷰 만들기
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val dateString = "${year}.${month+1}.${dayOfMonth}"
            binding.beerKeepDate.text = dateString
        }
        DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(
            Calendar.DAY_OF_MONTH)).show()
    }


    fun getCurrentDate(): String {
        val now = System.currentTimeMillis()
        val y = SimpleDateFormat("yyyy", Locale.KOREAN).format(now).toInt()
        val m = SimpleDateFormat("MM", Locale.KOREAN).format(now).toInt()
        val d = SimpleDateFormat("dd", Locale.KOREAN).format(now).toInt()

        return "${y}.${m}.${d}"
    }


    private fun saveDrink() {
        vm.drink.value!!.drinkPostingDate = getCurrentDate()

        vm.insertDrinkBeer()
        vm.insertDrink()
    }

    private fun cancelConfirm(){
        AlertDialog.Builder(requireContext())
            .setTitle("작성을 취소하시겠습니까?")
            .setPositiveButton("네", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    val intent = Intent(requireContext(), MainActivity::class.java)
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