package com.publicis.sapient.rapid.lookup.RapidLookUp.controller;


import com.publicis.sapient.rapid.lookup.RapidLookUp.entity.User;
import com.publicis.sapient.rapid.lookup.RapidLookUp.service.MassIndexerConfig;
import com.publicis.sapient.rapid.lookup.RapidLookUp.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "User API", description = "REST API for managing users")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MassIndexerConfig massIndexerConfig;

    @PostMapping("/load")
    @Operation(summary = "Load users from external API into H2 database")
    public ResponseEntity<String> loadUsers() {
        userService.loadDataFromApi();
        return ResponseEntity.ok("Users loaded successfully!");
    }

    @GetMapping("/find")
    @Operation(summary = "Find a user by ID or email")
    public ResponseEntity<User> findUserByEmailOrId(@RequestParam(required = false) Long id,
                                                    @RequestParam(required = false) String email) {
        User user = userService.findUserByEmailOrId(id, email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by firstName, lastName, or ssn")
    public ResponseEntity<List<User>> search(@RequestParam String searchText) {
        if (searchText.length() < 3) {
            return ResponseEntity.badRequest().build();
        }
        List<User> users = userService.searchUsers(searchText);
        return ResponseEntity.ok(users);
    }
}