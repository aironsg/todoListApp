package com.example.taskapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskapp.data.db.repository.TaskRepository
import com.example.taskapp.data.model.Task
import com.example.taskapp.util.StateView

class TaskViewModel(private  val repository: TaskRepository) : ViewModel() {

    private val _taskList = MutableLiveData<StateView<List<Task>>>()
    val taskList: LiveData<StateView<List<Task>>> = _taskList

    private val _taskInsert = MutableLiveData<StateView<Task>>()
    val taskInsert: LiveData<StateView<Task>> = _taskInsert

    private val _taskUpdate = MutableLiveData<StateView<Task>>()
    val taskUpdate: LiveData<StateView<Task>> = _taskUpdate

    private val _taskDelete = MutableLiveData<StateView<Task>>()
    val taskDelete: LiveData<StateView<Task>> = _taskDelete


    fun getTasks() {

    }

    fun insertTask(task: Task) {

    }

    fun updateTask(task: Task) {

    }

    fun deleteTask(task: Task) {

    }

}

class WordViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}