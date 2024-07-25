package com.zawadzkia.springtodo.task.category;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping({"/category"})
public class TaskCategoryController {
    private final TaskCategoryService taskCategoryService;

    @GetMapping
    String getCategoryList(Model model) {
        List<TaskCategoryModel> categoryList = taskCategoryService.getUserTaskCategoryList();
        model.addAttribute("categories", categoryList);
        return "task/categories/list";
    }
    
    @GetMapping("/create")
    public ModelAndView createCategory() {
        ModelAndView model = new ModelAndView("/task/categories/createCategory");
        model.addObject("category", new TaskCategoryModel());
        return model;
    }

    @PostMapping("/create")
    public String saveCategory(@ModelAttribute TaskCategoryModel category) {
        taskCategoryService.saveCategory(category);
        return "redirect:/category";
    }

    @GetMapping("/edit/{categoryId}")
    public ModelAndView editCategory(@PathVariable Long categoryId) {
        ModelAndView model = new ModelAndView("/task/categories/editCategory");
        TaskCategoryModel category = taskCategoryService.getCategoryModelById(categoryId);
        model.addObject("category", category);
        return model;
    }

    @PostMapping("/edit/{categoryId}")
    public String saveEditedCategory(
            @PathVariable Long categoryId,
            @ModelAttribute TaskCategoryModel category) {
        taskCategoryService.editCategory(categoryId, category);
        return "redirect:/category";
    }

    @GetMapping("/delete/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId) {
        taskCategoryService.deleteCategoryById(categoryId);
        return "redirect:/category";
    }
}
