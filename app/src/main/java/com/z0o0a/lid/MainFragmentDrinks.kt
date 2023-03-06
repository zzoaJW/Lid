package com.z0o0a.lid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.z0o0a.lid.adapter.DrinkListAdapter
import com.z0o0a.lid.databinding.MainFragmentDrinksBinding
import com.z0o0a.lid.repository.DrinkDatabase
import com.z0o0a.lid.view.DrinkPostingName

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

        // 노트 불러오는 과정에서 예외 발생해서 recyclerviewData가 없는 경우 처리
        if(!recyclerviewData.isNullOrEmpty()){
            adapter = DrinkListAdapter()
            adapter!!.listData = recyclerviewData
            binding.drinksRecyclerview.adapter = adapter
            binding.drinksRecyclerview.layoutManager = LinearLayoutManager(activity)
        }


        return binding.root
    }

    private fun getDrinks(){
        val run = Runnable {
            val db = DrinkDatabase.getInstance(requireContext())

            try {
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
            }catch (e:NullPointerException){
                Log.d("(Null 예외)노트 개수", db!!.drinkDao().getDrinkCount().toString())

                activity?.runOnUiThread {
                    Toast.makeText(activity?.applicationContext, "이미지 오류 : 노트 불러오기를 실패했습니다. 관리자에 문의해주세요.", Toast.LENGTH_LONG).show()
                }
            }catch (e:Exception){
                Log.d("(예외)노트 개수", db!!.drinkDao().getDrinkCount().toString())

                activity?.runOnUiThread {
                    Toast.makeText(activity?.applicationContext, "노트 불러오기를 실패했습니다. 관리자에 문의해주세요.", Toast.LENGTH_LONG).show()
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