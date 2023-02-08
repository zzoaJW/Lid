package com.z0o0a.lid

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.z0o0a.lid.databinding.LifeIsDrinkBinding
import com.z0o0a.lid.repository.DrinkDatabase

class LifeIsDrink : AppCompatActivity() {
    private lateinit var binding: LifeIsDrinkBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LifeIsDrinkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        makeChart()

        binding.btnBack3.setOnClickListener {
            finish()
        }

        showDrinkData()
        showDrinkNumOfTypeData()
        showWhiskeyData()

    }

    fun showDrinkData(){
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(applicationContext)

            var arr = db!!.drinkDao().getDrinkAll()

            var result = "데이터가 없어용"

            if (!arr.isEmpty()){
                result = ""
                arr.forEach { drink ->
                    result += drink.toString() + '\n' + '\n'
                }
            }

            runOnUiThread {
                binding.textView4.setText(result)
            }
        }).start()
    }

    fun showDrinkNumOfTypeData(){
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(applicationContext)

            var arr = db!!.drinkDao().getNumOfEachType()

            var result = "데이터가 없어용"

            if (!arr.isEmpty()){
                result = ""
                arr.forEach{ drinkNumOfType ->
                    result += drinkNumOfType.toString() + '\n' +
                            PieEntry(drinkNumOfType.drinkTypeCnt.toFloat(), drinkNumOfType.drinkType).toString() +
                            '\n' + '\n'
                }
            }

            runOnUiThread {
                binding.textView2.setText(result)
            }
        }).start()
    }

    fun showWhiskeyData(){
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(applicationContext)

            var arr = db!!.drinkDao().getDrinkWhiskeyAll()

            var result = "데이터가 없어용"

            if (!arr.isEmpty()){
                result = ""
                arr.forEach{ wh ->
                    result += wh.toString() +
                            '\n' + '\n'
                }
            }

            runOnUiThread {
                binding.textView9.setText(result)
            }
        }).start()
    }


    private fun dataValue(): ArrayList<RadarEntry>? {
        val dataVals = ArrayList<RadarEntry>()
        dataVals.add(RadarEntry(3.0F, ""))
        dataVals.add(RadarEntry(2.5F))
        dataVals.add(RadarEntry(3.0F))
        dataVals.add(RadarEntry(3.0F))
        dataVals.add(RadarEntry(3.0F))
        dataVals.add(RadarEntry(4.0F))
        dataVals.add(RadarEntry(5.0F))
        dataVals.add(RadarEntry(3.5F))
        dataVals.add(RadarEntry(3.5F))
        dataVals.add(RadarEntry(3.5F))
        dataVals.add(RadarEntry(3.5F))
        dataVals.add(RadarEntry(4.0F))
        return dataVals
    }

    private fun makeChart() {
        val dataSet = RadarDataSet(dataValue(), "선")
        dataSet.color = Color.rgb(0,0,255)
        // 데이터 선 안에 채우기 (밑에 두번째줄까지)
        dataSet.setFillColor(Color.rgb(0,0,255))
        dataSet.setDrawFilled(true)
        val data = RadarData()
        data.addDataSet(dataSet)
        // 정확한 데이터 값을 꼭지점에 보여줄건지 설정
        data.setDrawValues(false)
        // 정확한 데이터 값 글자 크기
//        data.setValueTextSize(12F)
        val labels = arrayOf("Fruity", "Alcohol", "Floral", "Sulphury", "Toffee", "Malty", "Hoppy","Astringent", "Bitter", "Sour", "Sweet", "Burnt")
        val xAxis = binding.radarChart.getXAxis()
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        // 데이터 연결
        binding.radarChart.setData(data)
        // 옆에 제목같은거 설정
        binding.radarChart.description.text = ""
        // 휘리릭 안되게 하기
        binding.radarChart.setTouchEnabled(false)
        // 뻗어나가는 선 색상 설정
        binding.radarChart.webColor = Color.BLACK
        // 뻗어나가는 선 굵기 설정
//        binding.radarChart.webLineWidth = 5F
        // 평행선 색상 설정
        binding.radarChart.webColorInner = Color.BLACK
        // 평행선 굵기 설정
//        binding.radarChart.webLineWidthInner = 5F
        // 선 투명도 설정
//        binding.radarChart.webAlpha = 50
        // 밑에 label 숨기기
        binding.radarChart.getLegend().setEnabled(false)
        // 세로 값 보여줄건지 설정
        binding.radarChart.yAxis.setDrawLabels(false)
        // 1-2-3-4-5로 만들기 (는 내가 생각한거랑은 조금 다르게 나왔지만.. 대충 비슷함)
        binding.radarChart.getYAxis().setLabelCount(5)
        binding.radarChart.getYAxis().setAxisMaxValue(5F)
        binding.radarChart.getYAxis().setAxisMinValue(1F)
    }
}