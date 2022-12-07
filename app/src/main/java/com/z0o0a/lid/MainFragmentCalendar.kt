package com.z0o0a.lid

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.z0o0a.lid.databinding.MainFragmentCalendarBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainFragmentCalendar: Fragment() {
    private lateinit var binding: MainFragmentCalendarBinding

    private val recyclerviewData : MutableList<DrinkListData> = mutableListOf()
    private var adapter : DrinkListAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MainFragmentCalendarBinding.inflate(inflater, container, false)

        // 초기 세팅
        var today = getCurrentDate()

        val ymd : List<String> = today.split(".")
        binding.calendarFocusDate.text = "${ymd[1]}월 ${ymd[2]}일"

        showDrinks(today)

        adapter = DrinkListAdapter()
        adapter!!.listData = recyclerviewData
        binding.focusDateDrinksRecyclerview.adapter = adapter
        binding.focusDateDrinksRecyclerview.layoutManager = LinearLayoutManager(activity)
//        binding.focusDateDrinksRecyclerview.setHasFixedSize(true)

        // 날짜 누를때마다 바꿔주기
        binding.calendarView.setOnDateChangeListener { calendarView, y, m, d ->
            // 선택 날짜 보여주기
            var focusDate = "${m+1}월 ${d}일"
            binding.calendarFocusDate.text = focusDate

            // 선택 날짜에 쓴 노트 보여주기
            var SearchDate = "${y}.${m+1}.${d}"

            showDrinks(SearchDate)

            adapter!!.notifyDataSetChanged()
        }

        return binding.root
    }

    private fun showDrinks(date : String){
        recyclerviewData.clear()

        val run = Runnable {
            val db = DrinkDatabase.getInstance(requireContext())

            var drinks = db!!.drinkDao().getDateRecyclerviewData(date)

            if (drinks.isNotEmpty()){
                drinks.forEach { drink ->
                    recyclerviewData.add(drink)
                }
            }
        }

        val th = Thread(run)
        th.start()

        try {
            th.join()
        }catch (e : InterruptedException){
            Log.d("Drink 리스트 실패","예외 발생")
        }

    }

    fun getCurrentDate(): String {
        val now = System.currentTimeMillis()
        val y = SimpleDateFormat("yyyy", Locale.KOREAN).format(now).toInt()
        val m = SimpleDateFormat("MM", Locale.KOREAN).format(now).toInt()
        val d = SimpleDateFormat("dd", Locale.KOREAN).format(now).toInt()

        return "${y}.${m}.${d}"
    }
}