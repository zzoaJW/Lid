package com.z0o0a.lid

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Drink::class], version = 1)
abstract class DrinkDatabase: RoomDatabase() {
    abstract fun drinkDao(): DrinkDao

    companion object {
        private var instance: DrinkDatabase? = null

        @Synchronized
        fun getInstance(context: Context): DrinkDatabase? {
            if (instance == null) {
                synchronized(DrinkDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DrinkDatabase::class.java,
                        "drink-database"
                    ).build()
                }
            }
            return instance
        }
    }
}