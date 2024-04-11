package com.example.springoauth2clientjwt.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @GetMapping("/user")
    @ResponseBody
    public String userAPI() {

        return "user route";
    }
}
