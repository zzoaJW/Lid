package com.z0o0a.lid

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Drink (
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "drinkId") var drinkId :Int, // 고유번호
    @ColumnInfo(name = "drinkImg") var drinkImg : String, // 이미지
    @ColumnInfo(name = "drinkEngName") var drinkEngName : String, // 영어 이름
    @ColumnInfo(name = "drinkKrName") var drinkKrName : String, // 한글 이름
    @ColumnInfo(name = "drinkType") var drinkType : String, // 대분류 (위스키/와인/맥주/기타)
    @ColumnInfo(name = "typeId") var typeId : Int, // 위스키/와인/맥주테이블에서의 고유번호 (위/와/맥이 아닌 경우 0 저장)

    @ColumnInfo(name = "drinkOverallStr") var drinkOverallStr : String, // 총평 줄글로 쓴거
    @ColumnInfo(name = "drinkRating") var drinkRating : Float, // 별점
    @ColumnInfo(name = "drinkRegion") var drinkRegion : String, // 생산 국가/지역
    @ColumnInfo(name = "drinkPrice") var drinkPrice : String,

    @ColumnInfo(name = "drinkKeepDate") var drinkKeepDate : String, // 개봉 일자
    @ColumnInfo(name = "drinkPlace") var drinkPlace : String, // 시음 장소
    @ColumnInfo(name = "drinkPostingDate") var drinkPostingDate : String // 작성 일자
)

@Entity
data class DrinkWhiskey(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "whId") var whId : Int, // 고유번호
    @ColumnInfo(name = "whType") var whType : String, // 소분류 (일단 다 -로 저장)

    @ColumnInfo(name = "whColor") var whColor : String, // 색 Appearoance

    @ColumnInfo(name = "whNose") var whNose : MutableList<String>, // 향 목록    // List<String>

    @ColumnInfo(name = "whPalate") var whPalate : MutableList<String>, // 맛 목록    // List<String>

    @ColumnInfo(name = "whSweet") var whSweet : Int, // 드라이-스위트
    @ColumnInfo(name = "whSpicy") var whSpicy : Int, // 순함-스파이시
    @ColumnInfo(name = "whBody") var whBody : Int, // 가벼움-무거움

    @ColumnInfo(name = "whFinish") var whFinish : MutableList<String>, // 피니시 목록    // List<String>

)

@Entity
data class DrinkWine(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "wiId") var wiId : Int, // 고유번호
    @ColumnInfo(name = "wiType") var wiType : String, // 소분류 (일단 다 -로 저장)

    @ColumnInfo(name = "wiClarity") var wiClarity : Int, // 투명도
    @ColumnInfo(name = "wiBrightness") var wiBrightness : Int, // 표면 밝기
    @ColumnInfo(name = "wiRimVariation") var wiRimVariation : Boolean, // 림 변화
    @ColumnInfo(name = "wiRimArea") var wiRimArea : String, // (림 변화 True인 경우만 저장) 림 넓이
    @ColumnInfo(name = "wiRimColor") var wiRimColor : String, // (림 변화 True인 경우만 저장) 림 색
    @ColumnInfo(name = "wiCoreColor") var wiCoreColor : String, // (림 변화 True인 경우만 저장) 코어(림 안쪽) 색
    @ColumnInfo(name = "wiTears") var wiTears : Int, // 눈물 점도

    @ColumnInfo(name = "wiNoseIntensity") var wiNoseIntensity : Int, // 향 강도
    @ColumnInfo(name = "wiNose") var wiNose : MutableList<String>, // 향 목록     // List<String>

    @ColumnInfo(name = "wiPalateIntensity") var wiPalateIntensity : Int, // 맛 강도
    @ColumnInfo(name = "wiSweet") var wiSweet : Int, // 당도
    @ColumnInfo(name = "wiAcidity") var wiAcidity : Int, // 산미
    @ColumnInfo(name = "wiAlcohol") var wiAlcohol : Int, // 알코올
    @ColumnInfo(name = "wiTannin") var wiTannin : Int, // 탄닌
)

@Entity
data class DrinkBeer(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "bId") var bId : Int, // 고유번호
    @ColumnInfo(name = "bType") var bType : String, // 소분류 (일단 다 -로 저장)

    @ColumnInfo(name = "bColor") var bColor : String, // 색 Appearance
    @ColumnInfo(name = "bClarity") var bClarity : Int, // 투명도
    @ColumnInfo(name = "bHeadColor") var bHeadColor : String, // 거품 색
    @ColumnInfo(name = "bHeadRetention") var bHeadRetention : Int, // 거품 유지력
    @ColumnInfo(name = "bDensity") var bDensity : Int, // 거품 조밀도

    @ColumnInfo(name = "bAromaMalt") var bAromaMalt : Int, // 향-맥아
    @ColumnInfo(name = "bAromaHops") var bAromaHops : Int, // 향-홉
    @ColumnInfo(name = "bAromaFermentation") var bAromaFermentation : Int, // 향-발효
    @ColumnInfo(name = "bAromaOther") var bAromaOther : String,// 향-기타

    @ColumnInfo(name = "bFlavorMalt") var bFlavorMalt : Int, // 맛-맥아
    @ColumnInfo(name = "bFlavorHops") var bFlavorHops : Int, // 맛-홉
    @ColumnInfo(name = "bFlavorFermentation") var bFlavorFermentation : Int, // 맛-발효
    @ColumnInfo(name = "bFlavorFinish") var bFlavorFinish : Int, // 맛-피니시

    @ColumnInfo(name = "bBody") var bBody : Int, // 바디감
    @ColumnInfo(name = "bCarbonation") var bCarbonation : Int, // 청량감
    @ColumnInfo(name = "bAstringent") var bAstringent : Int, // 떫은맛
)

data class DrinkListData (
    var drinkId : Int,
    var drinkImg : String,
    var drinkEngName : String,
    var drinkKrName : String,
    var drinkType : String,
    var drinkRating : Float
)

data class DrinkNumOfType (
    var drinkTypeCnt : Int,
    var drinkType : String
)

data class FlavorWheel (
    var flavorRating : Float,
    var flavorName : String
)