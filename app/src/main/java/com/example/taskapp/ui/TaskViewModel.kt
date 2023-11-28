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
                _taskStateData.postValue(StateTask.Inserted)
                _taskStateMessage.postValue(R.string.text_save_success_form_task_fragment)
            }
        } catch (e: Exception) {
            _taskStateMessage.postValue(R.string.text_save_error_form_task_fragment)
        }

    }


    private fun updateTask(task: Task) = viewModelScope.launch {
        try {
            repository.updateTask(task.toTaskEntity())
                _taskStateData.postValue(StateTask.Update)
                _taskStateMessage.postValue(R.string.text_update_success_form_task_fragment)

        } catch (e: Exception) {
            _taskStateMessage.postValue(R.string.text_update_error_form_task_fragment)
        }

    }



    fun deleteTask(id :Long) = viewModelScope.launch {
        try {
            repository.deleteTask(id)
            _taskStateData.postValue(StateTask.Delete)
            _taskStateMessage.postValue(R.string.text_delete_success_form_task_fragment)

        } catch (e: Exception) {
            _taskStateMessage.postValue(R.string.text_delete_error_form_task_fragment)
        }

    }

}


sealed class StateTask {
    object Inserted : StateTask()
    object Update : StateTask()
    object Delete : StateTask()
    object List : StateTask()
}