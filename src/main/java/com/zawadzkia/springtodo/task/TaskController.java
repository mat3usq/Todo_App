package com.zawadzkia.springtodo.task;

import com.zawadzkia.springtodo.task.category.TaskCategoryService;
import com.zawadzkia.springtodo.task.status.TaskStatusDTO;
import com.zawadzkia.springtodo.task.status.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping({"/task"})
class TaskController {

    private final TaskService taskService;

    private final TaskStatusService taskStatusService;

    private final TaskCategoryService taskCategoryService;

    @GetMapping
    String getTaskList(Model model) {
        List<TaskDTO> taskList = taskService.getTaskList();
        List<TaskStatusDTO> userTaskStatusList = taskStatusService.getUserTaskStatusList();
        model.addAttribute("tasks", taskList);
        model.addAttribute("statusList", userTaskStatusList);
        return "task/list";
    }

    @GetMapping("/create")
    public ModelAndView createTask() {
        ModelAndView model = new ModelAndView("/task/createTask");
        model.addObject("task", new TaskModel());
        model.addObject("categories", taskCategoryService.getUserTaskCategoryList());
        model.addObject("statuses", taskStatusService.getUserTaskStatusList());
        return model;
    }

    @PostMapping("/create")
    public String saveTask(@ModelAttribute TaskModel task) {
        taskService.saveTask(task);
        return "redirect:/task";
    }

    @GetMapping("/edit/{taskId}")
    public ModelAndView editTask(@PathVariable Long taskId) {
        ModelAndView model = new ModelAndView("/task/editTask");
        TaskModel task = taskService.getTaskModelById(taskId);
        model.addObject("task", task);
        model.addObject("categories", taskCategoryService.getUserTaskCategoryList());
        model.addObject("statuses", taskStatusService.getUserTaskStatusList());
        return model;
    }

    @PostMapping("/edit/{taskId}")
    public String saveEditedTask(
            @PathVariable Long taskId,
            @ModelAttribute TaskModel task) {

        taskService.editTask(taskId, task);
        return "redirect:/task";
    }

    @GetMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable Long taskId) {
        taskService.deleteTaskById(taskId);
        return "redirect:/task";
    }
}
