package com.kai.enote.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/")
@CrossOrigin("*")
public class UserController {

    @GetMapping("hello")
    public String returnHello(){
        return "Hello User !";
    }

}
