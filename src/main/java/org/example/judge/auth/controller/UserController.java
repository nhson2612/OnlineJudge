package org.example.judge.auth.controller;

import org.example.judge.auth.domain.SearchUserProjection;
import org.example.judge.auth.domain.User;
import org.example.judge.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/search")
    public ResponseEntity<List<SearchUserProjection>> searchUser(@RequestParam(name = "name", required = true) String name) {
        return ResponseEntity.ok(userService.searchUsers(name));
    }
}
