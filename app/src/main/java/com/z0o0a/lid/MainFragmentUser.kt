package com.z0o0a.lid

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.z0o0a.lid.databinding.MainFragmentUserBinding


class MainFragmentUser: Fragment() {
    private lateinit var binding: MainFragmentUserBinding

    private val drinkNumOfTypeList : MutableList<DrinkNumOfType> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MainFragmentUserBinding.inflate(inflater, container, false)

        // 일단 이렇게하구... 나중에 리팩토링할때 메소드로 빼기
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(requireContext())

            var drinkNumOfTypes = db!!.drinkDao().getNumOfEachType()

            if (!drinkNumOfTypes.isEmpty()){
                drinkNumOfTypes.forEach { drinkNumOfType ->
                    drinkNumOfTypeList.add(drinkNumOfType)
                }
            }
        }).start()

        pieChart()

        binding.btnLid.setOnClickListener {
            var intent = Intent(context, LifeIsDrink::class.java)
            startActivity(intent)
        }

        binding.btnDeveloperLetter.setOnClickListener {
            var intent = Intent(context, UserLetter::class.java)
            startActivity(intent)
        }

        return binding.root
    }


    private fun pieChartData(): ArrayList<PieEntry>?{
        val yValues = ArrayList<PieEntry>()

//        yValues.add(PieEntry(10f, "temp"))
//        yValues.add(PieEntry(20f, "temp2"))
//        yValues.add(PieEntry(30f, "temp3"))

        drinkNumOfTypeList.forEach { drinkNumOfType ->
            yValues.add(PieEntry(drinkNumOfType.drinkTypeCnt.toFloat()))
        }
        return yValues
    }


    private fun pieChart(){
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.getDescription().setEnabled(false)

        val dataSet = PieDataSet(pieChartData(), "술 종류")
        // 슬라이스 사이 간격
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.setColors(Color.rgb(129, 224, 119), Color.rgb(82, 97, 80),Color.rgb(194, 230, 190), Color.rgb(100, 173, 92), Color.rgb(56, 97, 51))

        val data = PieData(dataSet)
        // 각각 데이터 이름 글자 크기
        data.setValueTextSize(10f)
        // 각각 데이터 글자 색상
        data.setValueTextColor(Color.WHITE)

        // 상호작용 막기
        binding.pieChart.setTouchEnabled(false)

        binding.pieChart.setData(data)
        // 옆에 차트 이름? 지우기
        binding.pieChart.getLegend().setEnabled(false)
        binding.pieChart.description.text = ""
        // 데이터가 없을때 나올 문장
//        binding.pieChart.setNoDataText("")
    }
}