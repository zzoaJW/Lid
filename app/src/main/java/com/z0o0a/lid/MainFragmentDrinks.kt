package com.z0o0a.lid

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private var allDrinkNum = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MainFragmentDrinksBinding.inflate(inflater, container, false)

        getDrinks()

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

    private fun getDrinks(){
        val run = Runnable {
            val db = DrinkDatabase.getInstance(requireContext())

            var drinks = db!!.drinkDao().getAllRecyclerviewData()
            allDrinkNum = db!!.drinkDao().getDrinkAllNum()

            activity?.runOnUiThread{
                if (!drinks.isEmpty()) {
                    binding.totalDrink.text = "${allDrinkNum}잔의 기록"
                } else{
                    binding.totalDrink.text = "기록이 없습니다"
                }
            }

            if (!drinks.isEmpty()){
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
            Log.d("Drink reyclerview 출력 실패","실패")
        }
    }
}