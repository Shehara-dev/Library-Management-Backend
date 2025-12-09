package com.library.librarymanagement.controller;

import com.library.librarymanagement.entity.User;
import com.library.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @PatchMapping("/{id}/blacklist")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> blacklistUser(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(userService.blacklistUser(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/unblacklist")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> unblacklistUser(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(userService.unblacklistUser(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}