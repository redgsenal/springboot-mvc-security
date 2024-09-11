package com.springboot.tutorial.mvc.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {

    @GetMapping("/")
    public String showHome() {
        return "home";
    }

    @GetMapping("/leaders")
    public String showLeaderPage() {
        return "leaders";
    }

    @GetMapping("/systems")
    public String showSystemPage() {
        return "systems";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }

}
