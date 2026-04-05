package com.finance.controller;

import com.finance.model.User;
import com.finance.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /api/users
    // Body: { "username": "john", "password": "pass123", "role": "VIEWER" }
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String roleStr = body.get("role");

        if (username == null || password == null || roleStr == null) {
            throw new RuntimeException("username, password and role are required");
        }

        User.Role role = User.Role.valueOf(roleStr.toUpperCase());
        User user = userService.createUser(username, password, role);
        return ResponseEntity.ok(user);
    }

    // GET /api/users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // PUT /api/users/{id}/role?role=ANALYST
    @PutMapping("/{id}/role")
    public ResponseEntity<User> updateRole(@PathVariable Long id,
                                            @RequestParam String role) {
        User.Role newRole = User.Role.valueOf(role.toUpperCase());
        return ResponseEntity.ok(userService.updateRole(id, newRole));
    }

    // PUT /api/users/{id}/toggle-status
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<User> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleStatus(id));
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}
