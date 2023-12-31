package com.example.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskapp.R
import com.example.taskapp.data.db.AppDataBase
import com.example.taskapp.data.db.repository.TaskRepository
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task
import com.example.taskapp.databinding.FragmentFormTaskBinding
import com.example.taskapp.util.initToolbar
import com.example.taskapp.util.showBottomSheet

class FormTaskFragment : BaseFragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var status: Status = Status.TODO
    private var newTask: Boolean = true

    private val args: FormTaskFragmentArgs by navArgs()

    private val viewModel: TaskViewModel by viewModels {
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        getArgs()
        initListeners()
    }

    private fun getArgs() {
        args.task.let {
            if (it != null) {
                this.task = it

                configTask()
            }
        }
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener {
            observeViewModel()

            validateData()
        }

        binding.rgStatus.setOnCheckedChangeListener { _, id ->
            status = when (id) {
                R.id.rbTodo -> Status.TODO
                R.id.rbDoing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    private fun configTask() {
        newTask = false
        status = task.status
        binding.textToolbar.setText(R.string.text_toolbar_update_form_task_fragment)

        binding.edtTitleTask.setText(task.title)
        binding.edtDescriptionTask.setText(task.description)

        setStatus()
    }

    private fun setStatus() {
        binding.rgStatus.check(
            when (task.status) {
                Status.TODO -> R.id.rbTodo
                Status.DOING -> R.id.rbDoing
                else -> R.id.rbDone
            }
        )
    }

    private fun validateData() {
        val titleTask = binding.edtTitleTask.text.toString().trim()
        val description = binding.edtDescriptionTask.text.toString()

        if (titleTask.isNotEmpty()) {

            if (description.isNotBlank()) {
                hideKeyboard()

                binding.progressBar.isVisible = true

                if (newTask) task = Task()
                task.title = titleTask
                task.description = description
                task.status = status

                if (newTask) {
                    viewModel.insertOrUpdateTask(
                        title = titleTask,
                        description = description,
                        status = status
                    )
                } else {
                    viewModel.insertOrUpdateTask(
                        id = task.id,
                        title = titleTask,
                        description = description,
                        status = status
                    )
                }
            } else {
                showBottomSheet(message = getString(R.string.description_empty_form_task_fragment))

            }

        } else {
            showBottomSheet(message = getString(R.string.title_empty_form_task_fragment))
        }
    }

    private fun observeViewModel() {
        viewModel.taskStateData.observe(viewLifecycleOwner) { statetask ->
            if (statetask == StateTask.Inserted || statetask == StateTask.Update) {
                findNavController().popBackStack()

            }
        }

        viewModel.taskStateMessage.observe(viewLifecycleOwner) { message ->
            binding.progressBar.isVisible = false
            Toast.makeText(
                requireContext(),
                getString(message),
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}