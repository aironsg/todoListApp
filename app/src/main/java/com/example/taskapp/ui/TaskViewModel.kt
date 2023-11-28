package com.example.taskapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.R
import com.example.taskapp.data.db.entity.toTaskEntity
import com.example.taskapp.data.db.repository.TaskRepository
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _taskStateData = MutableLiveData<StateTask>()
    val taskStateData: LiveData<StateTask> = _taskStateData

    private val _taskStateMessage = MutableLiveData<Int>()
    val taskStateMessage: LiveData<Int> = _taskStateMessage

    fun getTasks() {

    }

    fun insertOrUpdateTask(id: Long = 0, description: String = "", status: Status = Status.TODO) {
        if (id == 0L) {
            insertTask(Task(description = description, status = status))
        } else {
            updateTask(Task(id = id, description = description, status = status))
        }

    }

    private fun insertTask(task: Task) = viewModelScope.launch {
        try {
            val id = repository.insertTask(task.toTaskEntity())
            if (id > 0L) {
                _taskStateData.postValue(StateTask.inserted)
                _taskStateMessage.postValue(R.string.text_save_success_form_task_fragment)
            }
        } catch (e: Exception) {
            _taskStateMessage.postValue(R.string.text_save_error_form_task_fragment)
        }

    }


    private fun updateTask(task: Task) {}
//        try {
//            repository.updateTask(task.toTaskEntity())
//
//        } catch (e: Exception) {
//            _taskStateMessage.postValue(R.string.text_update_error_form_task_fragment)
//        }



    fun deleteTask(task: Task) {

    }

}


sealed class StateTask {
    object inserted : StateTask()
    object update : StateTask()
    object delete : StateTask()
    object List : StateTask()
}