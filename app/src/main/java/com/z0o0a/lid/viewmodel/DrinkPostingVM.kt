package com.z0o0a.lid.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import com.z0o0a.lid.Drink
import com.z0o0a.lid.DrinkBeer
import com.z0o0a.lid.DrinkWhiskey
import com.z0o0a.lid.DrinkWine

class DrinkPostingVM(application: Application) : AndroidViewModel(application) {
    // [ 이름 페이지 ]
    // (1) 영어 이름 (2) 원하는 언어 이름 (3) 술 종류
    // [ 촬영or이미지 페이지 ]
    // (1) 촬영 화질 높이기 (2) 이미지 선택
    // (3) 이미지 선택 안했을때 null로 저장하되 나중에 NullPoint예외 안나오게하는 방법 찾기 (예외 발생 안하고 기본 이미지 불러오는 방법)
    // [ 나머지 정보 페이지 ]
    // 술 종류마다 다른 정보 입력 받기

    val drink = MutableLiveData<Drink>()
    val drinkWhiskey = MutableLiveData<DrinkWhiskey>()
    val drinkWine = MutableLiveData<DrinkWine>()
    val drinkBeer = MutableLiveData<DrinkBeer>()

    val keepDateChecked = MutableLiveData<Boolean>()


    init {
        drink.value = Drink(0,null, "", "", "", 0L, "",
                             5f, "", "", "개봉일 선택", "", "")
        drinkWhiskey.value = DrinkWhiskey(0, false, "-", "", arrayListOf(), arrayListOf(),
                                   3, 3, 3, arrayListOf())
        drinkWine.value = DrinkWine(0, false, "-", 0, 0, 0, false,
                                     3, "", "", arrayListOf(), 3, 3, 3, 3)
        drinkBeer.value = DrinkBeer(0, false, "-", "", 3, "", 3, 3,
                                      3, 3, 3, "", 3, 3, 3,
                                      3, 3, 3, 3)

        keepDateChecked.value = false
    }

    fun setDrinkType(drinkType : Int){
        // 위스키 0 / 와인 1 / 맥주 2 / 기타 3
        when (drinkType) {
            0 -> drink.value!!.drinkType = "위스키"
            1 -> drink.value!!.drinkType = "와인"
            2 -> drink.value!!.drinkType = "맥주"
            3 -> drink.value!!.drinkType = "기타"
        }
    }

    fun checkNameType(){

    }

    // 개봉일 선택 날짜 visible
    // view에서 visible 처리
    fun setKeepDateVisible(checked : Boolean){
        keepDateChecked.value = checked
    }
}