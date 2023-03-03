package com.z0o0a.lid.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo

class DrinkPostingVM(application: Application) : AndroidViewModel(application) {
    var drinkImg = MutableLiveData<Bitmap>() // 이미지
    var drinkEngName = MutableLiveData<String>() // 영어 이름
    var drinkKrName = MutableLiveData<String>() // 한글 이름
    var drinkType = ""
    // var typeId = MutableLiveData<Long>() // 위스키/와인/맥주테이블에서의 고유번호 (위/와/맥이 아닌 경우 0 저장)

    var drinkOverallStr = MutableLiveData<String>() // 총평 줄글로 쓴거
    var drinkRating = MutableLiveData<Float>() // 별점
    var drinkRegion = MutableLiveData<String>() // 생산 국가/지역
    var drinkPrice = MutableLiveData<String>() // 가격

    var drinkKeepDate = MutableLiveData<String>() // 개봉 일자
    var drinkPlace = MutableLiveData<String>() // 시음 장소
    var drinkPostingDate = MutableLiveData<String>() // 작성 일자

    var wiskey = MutableLiveData<String>()

    init {
        // drinkImg.value =
        drinkEngName.value = ""
        drinkEngName.value = ""
        drinkKrName.value = ""

        drinkOverallStr.value = ""
        drinkRating.value = 0.0f
        drinkRegion.value = ""
        drinkPrice.value = ""

        drinkKeepDate.value = ""
        drinkPlace.value = ""
        drinkPostingDate.value = ""
    }

    fun setDrinkType(dType : Int){
        // 위스키 0 / 와인 1 / 맥주 2 / 기타 3
        when (dType) {
            0 -> drinkType = "위스키"
            1 -> drinkType = "와인"
            2 -> drinkType = "맥주"
            3 -> {
                drinkType = "기타"
            }
        }
    }

    fun checkNameType(){

    }
}