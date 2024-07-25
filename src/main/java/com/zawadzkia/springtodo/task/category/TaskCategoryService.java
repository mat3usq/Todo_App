package com.zawadzkia.springtodo.task.category;

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
public class TaskCategoryService {

    private final TaskCategoryRepository taskCategoryRepository;
    private final UserRepository userRepository;

    public List<TaskCategoryModel> getUserTaskCategoryList() {
        List<TaskCategoryModel> result = new ArrayList<>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    
        if (principal instanceof AppUserDetails userDetails) {
            UserModel user = userDetails.getUser();
            List<TaskCategoryModel> categories;
    
            if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                categories = taskCategoryRepository.findAll();
            } else {
                categories = taskCategoryRepository.findAllByOwner(user);
            }
    
            categories.forEach(category -> 
                result.add(new TaskCategoryModel(category.getId(), category.getName(), 
                        category.getDescription(), category.getOwner()))
            );
        }
        return result;
    }

    public TaskCategoryModel getCategoryModelById(Long id) {
        TaskCategoryModel categoryModel = taskCategoryRepository.getReferenceById(id);
        return categoryModel;
    }

    public void saveCategory(TaskCategoryModel category) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findByUsername(username).orElseThrow();
        category.setOwner(user);
        taskCategoryRepository.save(category);
    }

    public void editCategory(Long categoryId, TaskCategoryModel updatedTaskCategory) {
        TaskCategoryModel category = taskCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + categoryId));
        category.setName(updatedTaskCategory.getName());
        category.setDescription(updatedTaskCategory.getDescription());
        taskCategoryRepository.save(category);
    }

    public void deleteCategoryById(Long categoryId) {
        try {
            taskCategoryRepository.deleteById(categoryId);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
