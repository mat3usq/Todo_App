package com.zawadzkia.springtodo.task.status;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/status")
@RequiredArgsConstructor
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    @GetMapping
    String getStatusList(Model model) {
        List<TaskStatusDTO> statusList = taskStatusService.getUserTaskStatusList();
        model.addAttribute("statuses", statusList);
        return "task/statuses/list";
    }

    @GetMapping("/create")
    public ModelAndView createStatus() {
        ModelAndView model = new ModelAndView("/task/statuses/createStatus");
        model.addObject("status", new TaskStatusModel());
        return model;
    }

    @PostMapping("/create")
    public String saveStatus(@ModelAttribute TaskStatusModel Status) {
        taskStatusService.saveStatus(Status);
        return "redirect:/status";
    }

    @GetMapping("/edit/{statusId}")
    public ModelAndView editStatus(@PathVariable Long statusId) {
        ModelAndView model = new ModelAndView("/task/statuses/editStatus");
        TaskStatusModel status = taskStatusService.getStatusModelById(statusId);
        model.addObject("status", status);
        return model;
    }

    @PostMapping("/edit/{statusId}")
    public String saveEditedStatus(
            @PathVariable Long statusId,
            @ModelAttribute TaskStatusModel status) {
        taskStatusService.editStatus(statusId, status);
        return "redirect:/status";
    }

    @GetMapping("/delete/{statusId}")
    public String deleteStatus(@PathVariable Long statusId) {
        taskStatusService.deleteStatusById(statusId);
        return "redirect:/status";
    }
}
