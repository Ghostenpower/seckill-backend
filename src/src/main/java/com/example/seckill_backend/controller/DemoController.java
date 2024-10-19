package com.example.seckill_backend.controller;

import com.example.seckill_backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DemoController {
    @GetMapping("/")
    public String myDemo() {
        return "Hello World";
    }
}
