package com.z0o0a.lid

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.z0o0a.lid.databinding.MainFragmentDrinksBinding

class MainFragmentDrinks: Fragment() {
    private lateinit var binding: MainFragmentDrinksBinding

    private val recyclerviewData : MutableList<DrinkListData> = mutableListOf()
    private var adapter : DrinkListAdapter? = null
    private var allNum = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MainFragmentDrinksBinding.inflate(inflater, container, false)

        // 일단 이렇게하구... 나중에 리팩토링할때 메소드로 빼기
        Thread(Runnable {
            val db = DrinkDatabase.getInstance(requireContext())

            var drinks = db!!.drinkDao().getAllRecyclerviewData()
            allNum = db!!.drinkDao().getAllNum()

            // 엥 이게 되네??
            binding.totalDrink.text = "${allNum}잔의 기록"

            if (!drinks.isEmpty()){
                drinks.forEach { drink ->
                    recyclerviewData.add(drink)
                }
            }
        }).start()

        // 왜 안되지.. 일단 쓰레드에서 저렇게 하기 ^^.. 나중에 리팩토링 ㄱㄱ
//        binding.totalDrink.text = "${allNum} 잔의 기록"
//        binding.totalDrink.setText("Life is Drink")

        binding.btnPosting.setOnClickListener {
            val intent = Intent(context, DrinkPostingName::class.java)
            startActivity(intent)
        }


        adapter = DrinkListAdapter()
        adapter!!.listData = recyclerviewData
        binding.drinksRecyclerview.adapter = adapter
        binding.drinksRecyclerview.layoutManager = LinearLayoutManager(activity)
//        binding.drinksRecyclerview.setHasFixedSize(true)


        return binding.root
    }
}