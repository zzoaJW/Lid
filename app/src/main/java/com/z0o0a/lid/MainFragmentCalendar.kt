package com.z0o0a.lid

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.z0o0a.lid.databinding.MainFragmentCalendarBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainFragmentCalendar: Fragment() {
    private lateinit var binding: MainFragmentCalendarBinding

    private val recyclerviewData : MutableList<DrinkListData> = mutableListOf()
    private var adapter : DrinkListAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MainFragmentCalendarBinding.inflate(inflater, container, false)

        // 초기 세팅
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM월 dd일")
        val formatted = current.format(formatter)
        binding.calendarFocusDate.setText(formatted)
        val formatter2 = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val formatted2 = current.format(formatter2)

        Thread(Runnable {
            val db = DrinkDatabase.getInstance(requireContext())

            var drinks = db!!.drinkDao().getDateRecyclerviewData(formatted2)

            if (!drinks.isEmpty()){
                drinks.forEach { drink ->
                    // 이미지 전달하는 방법 찾으면 수정하기
                    recyclerviewData.add(drink)
                }
            }
        }).start()

        adapter = DrinkListAdapter()
        adapter!!.listData = recyclerviewData
        binding.focusDateDrinksRecyclerview.adapter = adapter
        binding.focusDateDrinksRecyclerview.layoutManager = LinearLayoutManager(activity)
        binding.focusDateDrinksRecyclerview.setHasFixedSize(true)

        // 날짜 누를때마다 바꿔주기
        binding.calendarView.setOnDateChangeListener { calendarView, y, m, d ->
            // 선택 날짜 보여주기
            var focusDate = "${m+1}월 ${d}일"
            binding.calendarFocusDate.setText(focusDate)

            // 선택 날짜에 쓴 노트 보여주기
            var SearchDate = "${y}.${m+1}.${d}"
            recyclerviewData.clear()
            // 일단 이렇게하구... 나중에 리팩토링할때 메소드로 빼기
            Thread(Runnable {
                val db = DrinkDatabase.getInstance(requireContext())

                var drinks = db!!.drinkDao().getDateRecyclerviewData(SearchDate)

                if (!drinks.isEmpty()){
                    drinks.forEach { drink ->
                        // 이미지 전달하는 방법 찾으면 수정하기
                        recyclerviewData.add(drink)
                    }
                }
            }).start()

            adapter!!.notifyDataSetChanged()
        }




        return binding.root
    }
}