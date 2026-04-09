package com.talentco.service;

import com.talentco.dto.ApiResponse;
import com.talentco.entity.User;
import com.talentco.repository.HiringRequestRepository;
import com.talentco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HiringRequestRepository hiringRequestRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(String role) {
        try {
            User.Role userRole = User.Role.valueOf(role.toUpperCase());
            return userRepository.findByRole(userRole);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }

    public ApiResponse updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            user.setRole(User.Role.valueOf(newRole.toUpperCase()));
            userRepository.save(user);
            return new ApiResponse(true, "User role updated to " + newRole);
        } catch (IllegalArgumentException e) {
            return new ApiResponse(false, "Invalid role: " + newRole);
        }
    }

    public ApiResponse toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(!user.isActive());
        userRepository.save(user);

        String status = user.isActive() ? "activated" : "deactivated";
        return new ApiResponse(true, "User account has been " + status);
    }

    public ApiResponse deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
        return new ApiResponse(true, "User deleted successfully.");
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalProfessionals", userRepository.findByRole(User.Role.PROFESSIONAL).size());
        stats.put("totalClients", userRepository.findByRole(User.Role.CLIENT).size());
        stats.put("totalRequests", hiringRequestRepository.count());
        stats.put("pendingRequests", hiringRequestRepository.findByStatus(
            com.talentco.entity.HiringRequest.Status.PENDING).size());
        stats.put("acceptedRequests", hiringRequestRepository.findByStatus(
            com.talentco.entity.HiringRequest.Status.ACCEPTED).size());
        stats.put("rejectedRequests", hiringRequestRepository.findByStatus(
            com.talentco.entity.HiringRequest.Status.REJECTED).size());
        return stats;
    }
}
