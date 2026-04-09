package com.talentco.controller;

import com.talentco.dto.ApiResponse;
import com.talentco.entity.HiringRequest;
import com.talentco.entity.User;
import com.talentco.service.AdminService;
import com.talentco.service.HiringRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin only: manage users, roles, requests, and platform statistics")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private HiringRequestService hiringRequestService;

    @GetMapping("/dashboard")
    @Operation(summary = "Get dashboard statistics", description = "Total users, requests, and status breakdown")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/users/role/{role}")
    @Operation(summary = "Get users by role", description = "Filter users by ADMIN, PROFESSIONAL, CLIENT, SUPPORT")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(adminService.getUsersByRole(role));
    }

    @PutMapping("/users/{id}/role")
    @Operation(summary = "Update user role")
    public ResponseEntity<ApiResponse> updateUserRole(@PathVariable Long id,
                                                       @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(adminService.updateUserRole(id, body.get("role")));
    }

    @PutMapping("/users/{id}/toggle-status")
    @Operation(summary = "Activate or deactivate a user account")
    public ResponseEntity<ApiResponse> toggleUserStatus(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.toggleUserStatus(id));
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteUser(id));
    }

    @GetMapping("/requests")
    @Operation(summary = "View all hiring requests on the platform")
    public ResponseEntity<List<HiringRequest>> getAllRequests() {
        return ResponseEntity.ok(hiringRequestService.getAllRequests());
    }
}
