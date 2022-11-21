package com.z0o0a.lid

import androidx.room.*

@Dao
interface DrinkDao {
    @Insert
    fun insert(drink: Drink)

    @Update
    fun update(drink: Drink)

    @Delete
    fun delete(drink: Drink?)

    @Query("DELETE FROM Drink")
    fun deleteTable(): Int

    // 종류별 갯수 : user 페이지의 파이 차트에 넣을 데이터
    @Query("SELECT drinkType, COUNT(drinkType) AS drinkTypeCnt FROM Drink GROUP BY drinkType")
    fun getNumOfEachType(): List<DrinkNumOfType>

    @Query("SELECT * FROM Drink")
    fun getAll(): List<Drink>

    @Query("SELECT * FROM Drink WHERE drinkPostingDate = :date")
    fun getDate(date : String): List<Drink>

    @Query("SELECT * FROM Drink WHERE drinkId = :drinkId")
    fun getDrink(drinkId : Int): Drink

    @Query("SELECT drinkId, drinkEngName, drinkKrName, drinkType, drinkRating, drinkImg  FROM Drink")
    fun getAllRecyclerviewData(): List<DrinkListData>

    @Query("SELECT drinkId, drinkEngName, drinkKrName, drinkType, drinkRating, drinkImg FROM Drink WHERE drinkPostingDate = :date")
    fun getDateRecyclerviewData(date : String): List<DrinkListData>
}