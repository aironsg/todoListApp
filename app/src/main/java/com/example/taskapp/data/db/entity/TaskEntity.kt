package com.example.taskapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskapp.data.model.Status

@Entity(tableName = "task_table")
class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "description")
    val description:String,
    @ColumnInfo(name = "status")
    val status: Status


)
