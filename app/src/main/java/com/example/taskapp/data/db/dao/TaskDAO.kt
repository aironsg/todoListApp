package com.example.taskapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskapp.data.db.entity.TaskEntity
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task

@Dao
interface TaskDAO{

    @Query("SELECT * FROM task_table ORDER BY id DESC")
    suspend fun getAllTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(taskEntity:TaskEntity): Long

    @Query("DELETE FROM task_table WHERE id = :id")
    suspend fun deleteTask(id:Long)

    @Query("UPDATE task_table SET description = :description, status = :status WHERE id = :id")
    suspend fun updateTask(id:Long, description: String, status: Status)

}