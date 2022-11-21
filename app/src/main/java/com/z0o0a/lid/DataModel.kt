package com.z0o0a.lid

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Drink (
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "drinkId") var drinkId :Int,
    @ColumnInfo(name = "drinkEngName") var drinkEngName : String,
    @ColumnInfo(name = "drinkKrName") var drinkKrName : String,
    @ColumnInfo(name = "drinkType") var drinkType : String,
    @ColumnInfo(name = "drinkRating") var drinkRating : Float,
    @ColumnInfo(name = "drinkImg") var drinkImg : String,
    @ColumnInfo(name = "drinkTasting") var drinkTasting : String,
    @ColumnInfo(name = "drinkKeepDate") var drinkKeepDate : String,
    @ColumnInfo(name = "drinkPlace") var drinkPlace : String,
    @ColumnInfo(name = "drinkPostingDate") var drinkPostingDate : String
)

data class DrinkListData (
    var drinkId : Int,
    var drinkEngName : String,
    var drinkKrName : String,
    var drinkType : String,
    var drinkRating : Float,
    var drinkImg : String,
)

data class DrinkNumOfType (
    var drinkTypeCnt : Int,
    var drinkType : String
)

data class FlavorWheel (
    var flavorRating : Float,
    var flavorName : String
)