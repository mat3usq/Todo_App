package com.zawadzkia.springtodo.home;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", "/index"})
public class IndexController {

    @GetMapping
    String index() {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return "redirect:/task";
        } else {
            return "index";
        }
    }

}
