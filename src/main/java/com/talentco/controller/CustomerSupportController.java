package com.talentco.controller;

import com.talentco.entity.HiringRequest;
import com.talentco.entity.User;
import com.talentco.repository.HiringRequestRepository;
import com.talentco.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support")
@Tag(name = "Customer Support", description = "Support team can view user inquiries and platform activity")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
@CrossOrigin(origins = "*")
public class CustomerSupportController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HiringRequestRepository hiringRequestRepository;

    @GetMapping("/overview")
    @Operation(summary = "Support overview", description = "View platform activity summary")
    public ResponseEntity<Map<String, Object>> getOverview() {
        Map<String, Object> overview = new HashMap<>();
        overview.put("totalUsers", userRepository.count());
        overview.put("activeUsers", userRepository.findByIsActive(true).size());
        overview.put("inactiveUsers", userRepository.findByIsActive(false).size());
        overview.put("totalRequests", hiringRequestRepository.count());
        return ResponseEntity.ok(overview);
    }

    @GetMapping("/users")
    @Operation(summary = "View all users for support")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/requests")
    @Operation(summary = "View all requests for support monitoring")
    public ResponseEntity<List<HiringRequest>> getAllRequests() {
        return ResponseEntity.ok(hiringRequestRepository.findAll());
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get user details by ID")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
