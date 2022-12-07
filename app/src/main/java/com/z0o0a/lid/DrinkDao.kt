package com.z0o0a.lid

import androidx.room.*

@Dao
interface DrinkDao {
    @Insert
    fun insertDrink(drink: Drink)

    @Update
    fun updateDrink(drink: Drink)

    @Delete
    fun deleteDrink(drink: Drink?)

    // 전체 테이블 삭제 ***추가로 위스키/와인/맥주 테이블 따로 삭제 해줘야함
    @Query("DELETE FROM Drink")
    fun deleteDrinkTable(): Int

    // 전체 기록 갯수 반환
    @Query("SELECT COUNT(*) FROM Drink")
    fun getDrinkAllNum(): Int

    // 종류별 갯수 : user 페이지의 파이 차트에 넣을 데이터
    @Query("SELECT drinkType, COUNT(drinkType) AS drinkTypeCnt FROM Drink GROUP BY drinkType")
    fun getNumOfEachType(): List<DrinkNumOfType>

    @Query("SELECT * FROM Drink")
    fun getDrinkAll(): List<Drink>

    // ??? 어디서 쓰는거지
    @Query("SELECT * FROM Drink WHERE drinkPostingDate = :date")
    fun getDate(date : String): List<Drink>

    // recyclerview item 눌러서 테이스팅 노트 화면으로 넘어가서 해당 drinkId의 테이스팅 노트 보여주기
    @Query("SELECT * FROM Drink WHERE drinkId = :drinkId")
    fun getDrink(drinkId : Int): Drink

    // reyclerview에 뿌릴때 필요한 정보들 가져오기
    @Query("SELECT drinkId, drinkImg, drinkEngName, drinkKrName, drinkType, drinkRating  FROM Drink")
    fun getAllRecyclerviewData(): List<DrinkListData>

    // 캘린더에서 날짜별로 recyclerview에 뿌릴때 필요한 정보들 가져오기
    @Query("SELECT drinkId, drinkImg, drinkEngName, drinkKrName, drinkType, drinkRating FROM Drink WHERE drinkPostingDate = :date")
    fun getDateRecyclerviewData(date : String): List<DrinkListData>

    @Query("SELECT EXISTS(SELECT * FROM Drink WHERE drinkId = :drinkId)")
    fun getDrinkExist(drinkId : Int) : Boolean



    // Whiskey Dao
    @Insert
    fun insertDrinkWhiskey(drinkWhiskey : DrinkWhiskey) : Long

    @Update
    fun updateDrinkWhiskey(drinkWhiskey : DrinkWhiskey)

    @Delete
    fun deleteDrinkWhiskey(drinkWhiskey : DrinkWhiskey)

    // recyclerview item 눌러서 테이스팅 노트 화면으로 넘어가서 해당 drinkId의 테이스팅 노트 보여주기
    @Query("SELECT * FROM DrinkWhiskey WHERE whId = :whId")
    fun getDrinkWiskey(whId : Long): DrinkWhiskey



    @Query("DELETE FROM DrinkWhiskey")
    fun deleteDrinkWhiskeyTable(): Int

    @Query("SELECT COUNT(*) FROM DrinkWhiskey")
    fun getDrinkWhiskeyAllNum(): Int

    @Query("SELECT * FROM DrinkWhiskey")
    fun getDrinkWhiskeyAll(): List<DrinkWhiskey>




    // Wine Dao
    @Insert
    fun insertDrinkWine(drinkWine : DrinkWine) : Long

    @Update
    fun updateDrinkWine(drinkWine : DrinkWine)

    @Delete
    fun deleteDrinkWine(drinkWine : DrinkWine)

    @Query("DELETE FROM DrinkWine")
    fun deleteDrinkWineTable(): Int

    @Query("SELECT COUNT(*) FROM DrinkWine")
    fun getDrinkWineAllNum(): Int

    @Query("SELECT * FROM DrinkWine")
    fun getDrinkWineAll(): List<DrinkWine>

    @Query("SELECT * FROM DrinkWine WHERE wiId = :wiId")
    fun getDrinkWine(wiId : Long): DrinkWine




    // Beer Dao
    @Insert
    fun insertDrinkBeer(drinkBeer : DrinkBeer) : Long

    @Update
    fun updateDrinkBeer(drinkBeer : DrinkBeer)

    @Delete
    fun deleteDrinkBeer(drinkBeer : DrinkBeer)


    @Query("DELETE FROM DrinkBeer")
    fun deleteDrinkBeerTable(): Int

    @Query("SELECT COUNT(*) FROM DrinkBeer")
    fun getDrinkBeerAllNum(): Int

    @Query("SELECT * FROM DrinkBeer")
    fun getDrinkBeerAll(): List<DrinkBeer>

    @Query("SELECT * FROM DrinkBeer WHERE bId = :bId")
    fun getDrinkBeer(bId : Long): DrinkBeer
}