package com.z0o0a.lid.repository

import android.app.Application
import com.z0o0a.lid.Drink
import com.z0o0a.lid.DrinkBeer
import com.z0o0a.lid.DrinkWhiskey
import com.z0o0a.lid.DrinkWine

class DrinkPostingRepo(application: Application) {
    val drinkDatabase = DrinkDatabase.getInstance(application)!!
    private val drinkDao : DrinkDao = drinkDatabase.drinkDao()

    fun insert(drink: Drink){
        try {
            val thread = Thread(Runnable {
                drinkDao.insertDrink(drink)
            })
            thread.start()
        } catch (e : Exception){e.printStackTrace()}
    }

    fun insertDrinkWhiskey(drinkWhiskey : DrinkWhiskey): Long{
        var whiskeyId = 0L
        try {
            val thread = Thread(Runnable {
                whiskeyId = drinkDao.insertDrinkWhiskey(drinkWhiskey)
            })
            thread.start()
        } catch (e : Exception){e.printStackTrace()}

        return whiskeyId
    }

    fun insertDrinkWine(drinkWine : DrinkWine) : Long{
        var wineId = 0L
        try {
            val thread = Thread(Runnable {
                wineId = drinkDao.insertDrinkWine(drinkWine)
            })
            thread.start()
        } catch (e : Exception){e.printStackTrace()}

        return wineId
    }

    fun insertDrinkBeer(drinkBeer : DrinkBeer) : Long{
        var beerId = 0L
        try {
            val thread = Thread(Runnable {
                beerId = drinkDao.insertDrinkBeer(drinkBeer)
            })
            thread.start()
        } catch (e : Exception){e.printStackTrace()}

        return beerId
    }
}