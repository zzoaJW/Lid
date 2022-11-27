package com.z0o0a.lid

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.z0o0a.lid.databinding.ItemDrinkListBinding

class DrinkListAdapter: RecyclerView.Adapter<DrinkListAdapter.ViewHolder>() {
    private lateinit var itemDrinkListBinding: ItemDrinkListBinding
    var listData = mutableListOf<DrinkListData>()

    //어떤 뷰를 생성할 지
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        itemDrinkListBinding = ItemDrinkListBinding.inflate(inflater,parent,false)

        return ViewHolder(itemDrinkListBinding)
    }
    //생성된 뷰안에 어떤 데이터를 넣을 건지
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(listData[position], position)
    }
    //넣을 데이터는 몇 개인지(몇 개의 list를 만들 건지)
    override fun getItemCount(): Int {
        return listData.size
    }


    //그냥 class사용도 되지만 그냥 class 시용시 static처리가 되어
    //쓸데없이 메모리를 잡아먹게된다.

    //ViewHolder는 현재 화면에 보이는 아이템 레이아웃 개수만큼 생성되고 새롭게 그려 	   //저야 할 아이템 레이아웃이 있다면(스크롤 동작) 가장 위의 ViewHolder를 재사용해	  //서 데이터만 바꿉니다.
    inner class ViewHolder(var itemDrinkListBinding: ItemDrinkListBinding) : RecyclerView.ViewHolder(itemDrinkListBinding.root) {
        private var position : Int? = null

        //setData로 넣을 데이터
        fun setData(content : DrinkListData, position: Int) {
            this.position = position
            itemDrinkListBinding.itemListDrinkId.text = content.drinkId.toString()
            itemDrinkListBinding.itemListDrinkEngName.text = content.drinkEngName
            itemDrinkListBinding.itemListDrinkKrName.text = content.drinkKrName
            itemDrinkListBinding.itemListDrinkType.text = content.drinkType
            itemDrinkListBinding.itemListRating.text = content.drinkRating.toString()
            itemDrinkListBinding.itemListDrinkImg.setImageURI(Uri.parse(content.drinkImg))


            itemView.setOnClickListener {
                val context=itemView.context
                val intent = Intent( context, DrinkTastingNote::class.java)
                intent.putExtra("drinkId", listData[position].drinkId)
                context.startActivity(intent)
            }

            itemView.elevation = 10F
        }
    }

}

