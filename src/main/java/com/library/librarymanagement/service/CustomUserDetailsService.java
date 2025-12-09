package com.library.librarymanagement.service;

import com.library.librarymanagement.entity.User; // Import your User entity
import com.library.librarymanagement.repository.UserRepository; // Import your Repo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Fetch user from DB
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. IMPORTANT: Create the Authority/Role
        // We assume your database stores roles as "USER", "LIBRARIAN", etc.
        // If you use hasRole() in controller, add "ROLE_" prefix here.
        // If you use hasAuthority() in controller, keep it as is.
        
        // Let's stick to standard practice: Add ROLE_ prefix
        String roleName = "ROLE_" + user.getRole().name(); // Assuming role is an Enum
        // OR if role is a String: String roleName = "ROLE_" + user.getRole();
        
        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        // 3. Return the UserDetails with the authority
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority) // <--- THIS WAS MISSING
        );
    }
}