package com.zawadzkia.springtodo.auth;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zawadzkia.springtodo.user.UserModel;
import com.zawadzkia.springtodo.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping({"/auth"})
class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/task";
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public ModelAndView createUser() {
        ModelAndView model = new ModelAndView("/auth/register");
        model.addObject("user", new UserModel());
        return model;
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute UserModel user) {
        UserModel userModel = userService.existingUser(user.getUsername());
        if (userModel == null) {
            userService.saveUser(user);
            return "redirect:/task";
        } else {
            return "redirect:/auth/register";
        }
    }
    
    @GetMapping("/error")
    String error() {
        throw new NotImplementedException("Not Implemented Yet!");
    }
}
