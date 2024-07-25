package com.zawadzkia.springtodo.task;

import com.zawadzkia.springtodo.task.status.TaskStatusDTO;
import com.zawadzkia.springtodo.task.status.TaskStatusModel;
import com.zawadzkia.springtodo.task.status.TaskStatusRepository;
import com.zawadzkia.springtodo.user.UserModel;
import com.zawadzkia.springtodo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;

    public List<TaskDTO> getTaskList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findByUsername(username).orElseThrow();

        Set<TaskModel> tasks;
        if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")))
            tasks = new HashSet<>(taskRepository.findAll());
        else
            tasks = user.getTasks();

        List<TaskDTO> result = new ArrayList<>();
        tasks.forEach(taskModel ->
        {
            TaskStatusModel status = taskModel.getStatus();
            TaskStatusDTO taskStatusDTO = new TaskStatusDTO(status.getId(), status.getName(), status.getDisplayName());
            TaskDTO taskDTO = new TaskDTO(taskModel.getId(), taskModel.getSummary(), taskModel.getDescription(),
                    taskModel.getStartDate(), taskModel.getDueDate(), taskModel.getDescription(), taskModel.getCategory().getName(), taskStatusDTO);
            result.add(taskDTO);
        });
        return result;
    }

    public TaskDTO getTaskDTOById(Long id) {
        TaskModel taskModel = taskRepository.getReferenceById(id);
        TaskStatusModel status = taskModel.getStatus();
        TaskStatusDTO taskStatusDTO = new TaskStatusDTO(status.getId(), status.getName(), status.getDisplayName());
        TaskDTO taskDTO = new TaskDTO(taskModel.getId(), taskModel.getSummary(), taskModel.getDescription(),
                taskModel.getStartDate(), taskModel.getDueDate(), taskModel.getDescription(), taskModel.getCategory().getName(), taskStatusDTO);
        return taskDTO;
    }


    public TaskModel getTaskModelById(Long id) {
        TaskModel taskModel = taskRepository.getReferenceById(id);
        return taskModel;
    }

    public void update(TaskDTO taskDTO) {
        TaskModel taskModel = taskRepository.findById(taskDTO.getId()).orElseThrow();
        taskModel.setSummary(taskDTO.getSummary());
        taskModel.setDescription(taskDTO.getDescription());
        taskModel.setStartDate(taskDTO.getStartDate());
        taskModel.setDueDate(taskDTO.getDueDate());
        taskModel.setAttachment(taskDTO.getCategory());
        taskModel.setStatus(taskStatusRepository.getReferenceById(taskDTO.getStatus().getId()));
        taskRepository.save(taskModel);
    }

    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    public void editTask(Long taskId, TaskModel updatedTask) {
        TaskModel task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + taskId));

        task.setSummary(updatedTask.getSummary());
        task.setDescription(updatedTask.getDescription());
        task.setCategory(updatedTask.getCategory());
        task.setStatus(updatedTask.getStatus());
        task.setStartDate(updatedTask.getStartDate());
        task.setDueDate(updatedTask.getDueDate());

        taskRepository.save(task);
    }

    public void saveTask(TaskModel task) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findByUsername(username).orElseThrow();
        task.setOwner(user);
        taskRepository.save(task);
    }
}
