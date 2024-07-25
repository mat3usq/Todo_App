package com.zawadzkia.springtodo.task.status;

import com.zawadzkia.springtodo.auth.AppUserDetails;
import com.zawadzkia.springtodo.user.UserModel;
import com.zawadzkia.springtodo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;

    public List<TaskStatusDTO> getUserTaskStatusList() {
        List<TaskStatusDTO> result = new ArrayList<>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof AppUserDetails userDetails) {
            UserModel user = userDetails.getUser();
            List<TaskStatusModel> allByOwner;
    
            if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                allByOwner = taskStatusRepository.findAll();
            } else {
                allByOwner = taskStatusRepository.findAllByOwner(user);
            }

            allByOwner.forEach(status -> result.add(new TaskStatusDTO(status.getId(), status.getName(),
                    status.getDisplayName())));
        }
    
        return result;
    }
    public TaskStatusModel getStatusModelById(Long id) {
        TaskStatusModel StatusModel = taskStatusRepository.getReferenceById(id);
        return StatusModel;
    }

    public void saveStatus(TaskStatusModel Status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findByUsername(username).orElseThrow();
        Status.setOwner(user);
        taskStatusRepository.save(Status);
    }

    public void editStatus(Long StatusId, TaskStatusModel updatedTaskStatus) {
        TaskStatusModel Status = taskStatusRepository.findById(StatusId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + StatusId));
        Status.setName(updatedTaskStatus.getName());
        taskStatusRepository.save(Status);
    }

    public void deleteStatusById(Long StatusId) {
        try {
            taskStatusRepository.deleteById(StatusId);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
