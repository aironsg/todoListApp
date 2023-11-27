package com.example.taskapp.data.db.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskapp.data.db.dao.TaskDAO
import com.example.taskapp.data.db.entity.TaskEntity
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task

class TaskRepository(private val taskDAO: TaskDAO) {
    suspend fun getAllTasks(): List<Task>{
        return taskDAO.getAllTasks()
    }

    suspend fun getTaskById(id:Long){
        return taskDAO.getTaskById(id)
    }

    suspend fun insertTask(taskEntity: TaskEntity): Long {
        return taskDAO.insertTask(taskEntity)
    }

    suspend fun deleteTask(id:Long) = taskDAO.deleteTask(id)

    suspend fun updateTask(taskEntity:TaskEntity){
        taskDAO.updateTask(
            id = taskEntity.id,
            description = taskEntity.description,
            status = taskEntity.status
        )
    }
}