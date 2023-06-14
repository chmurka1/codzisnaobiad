package com.chmurka.codzisnaobiad.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Recipe::class, Product::class, FridgeProduct::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): RecipeDao

    companion object {
        @Volatile
        private var instance : AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase {
            val tmp = instance
            if(tmp == null) {
                synchronized(this) {
                    val tmp2 = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "recipes.db"
                    ).build()
                    instance = tmp2
                    return tmp2
                }
            } else {
                return tmp
            }
        }
    }
}