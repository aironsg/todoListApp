package com.example.taskapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskapp.data.db.dao.TaskDAO
import com.example.taskapp.data.db.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDataBase: RoomDatabase() {

    abstract fun taskDAO(): TaskDAO

    @Volatile
    private var INSTANCE: AppDataBase? = null

    fun getDatabase(context: Context): AppDataBase {
        return INSTANCE ?: synchronized(this){
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "task_database"
            ).build()
            INSTANCE = instance
            return instance
        }
    }
}