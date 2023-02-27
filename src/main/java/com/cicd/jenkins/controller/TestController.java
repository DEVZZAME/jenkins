package com.cicd.jenkins.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    public String test() {
        return "Hey Jenkins! i have pushed! do your job!";
    }
}
