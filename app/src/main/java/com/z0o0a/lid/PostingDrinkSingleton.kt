package com.z0o0a.lid

import android.content.Context

class PostingDrinkSingleton {
    var drinkEngName : String = ""
    var drinkKrName : String = ""
    var drinkType : String = ""
    var drinkImg : String = ""
    // 굳이 싱글톤에 저장하고나서 다시 꺼내서 db에 저장하지는 않아도될듯? 이것들은 바로 db로 넣어주자
//    var drinkRating : Float = 0F
//    var drinkTasting : String = ""
//    var drinkKeepDate : String = ""
//    var drinkPlace : String = ""
//    var drinkPostingDate : String = ""

    companion object {
        private var instance: PostingDrinkSingleton? = null

        @Synchronized
        fun getInstance(context: Context): PostingDrinkSingleton? {
            if (instance == null) {
                synchronized(DrinkDatabase::class){
                    instance = PostingDrinkSingleton()
                }
            }
            return instance
        }
    }
}