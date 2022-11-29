package com.z0o0a.lid

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Drink::class, DrinkWhiskey::class, DrinkWine::class, DrinkBeer::class], version = 2)
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
                        "drink-database")
                        .fallbackToDestructiveMigration() // 이전 데이터베이스 삭제 후 새로운 데이터베이스 생성
                        .build()
                }
            }
            return instance
        }
    }
}