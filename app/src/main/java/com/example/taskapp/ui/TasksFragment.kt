package com.example.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.R
import com.example.taskapp.data.db.AppDataBase
import com.example.taskapp.data.db.repository.TaskRepository
import com.example.taskapp.data.model.Task
import com.example.taskapp.databinding.FragmentTasksBinding
import com.example.taskapp.ui.adapter.TaskAdapter
import com.example.taskapp.util.showBottomSheet

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private val taskListViewModel: TaskListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {

                    val database = AppDataBase.getDatabase(requireContext())
                    val repository = TaskRepository(database.taskDAO())
                    @Suppress("UNCHECKED_CAST")
                    return TaskListViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    private val taskViewModel: TaskViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {

                    val database = AppDataBase.getDatabase(requireContext())
                    val repository = TaskRepository(database.taskDAO())
                    @Suppress("UNCHECKED_CAST")
                    return TaskViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        taskListViewModel.getAllTasks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        initRecyclerView()

        observeViewModel()


    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            val action = TasksFragmentDirections
                .actionTaksFragmentToFormTaskFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {

        taskListViewModel.taskList.observe(viewLifecycleOwner){ taskList ->
            taskAdapter.submitList(taskList)
            listEmpty(taskList)
        }

        taskViewModel.taskStateData.observe(viewLifecycleOwner){ stateTask ->

            if(stateTask == StateTask.Delete){
                taskListViewModel.getAllTasks()
            }

        }
    }

    private fun initRecyclerView() {
        taskAdapter = TaskAdapter() { task, option ->
            optionSelected(task, option)
        }

        with(binding.rvTasks) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }
    }

    private fun optionSelected(task: Task, option: Int) {
        when (option) {
            TaskAdapter.SELECT_REMOVE -> {
                showBottomSheet(
                    titleDialog = R.string.text_title_dialog_delete,
                    message = getString(R.string.text_message_dialog_delete),
                    titleButton = R.string.text_button_dialog_confirm,
                    onClick = {
                        taskViewModel.deleteTask(task.id)
                    }
                )
            }
            TaskAdapter.SELECT_EDIT -> {
                val action = TasksFragmentDirections
                    .actionTaksFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }


        }
    }



    private fun listEmpty(taskList: List<Task>) {
        binding.textInfo.text = if (taskList.isEmpty()) {
            getString(R.string.text_list_task_empty)
        } else {
            ""
        }
        binding.progressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}