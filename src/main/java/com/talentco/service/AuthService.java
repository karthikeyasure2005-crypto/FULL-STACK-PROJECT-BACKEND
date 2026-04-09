package com.talentco.service;

import com.talentco.dto.*;
import com.talentco.entity.User;
import com.talentco.repository.UserRepository;
import com.talentco.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public ApiResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse(false, "Email already registered. Please login.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        try {
            if (request.getRole() != null && !request.getRole().isEmpty()) {
                user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
            } else {
                user.setRole(User.Role.CLIENT);
            }
        } catch (IllegalArgumentException e) {
            user.setRole(User.Role.CLIENT);
        }

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        user.setVerified(false);

        userRepository.save(user);
        emailService.sendRegistrationOtp(request.getEmail(), otp);

        return new ApiResponse(true, "Registration successful! Please check your email for OTP verification.");
    }

    public ApiResponse verifyOtp(OtpVerifyRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isVerified()) {
            return new ApiResponse(false, "Account already verified.");
        }

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            return new ApiResponse(false, "Invalid OTP. Please try again.");
        }

        if (user.getOtpExpiry() == null || LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            return new ApiResponse(false, "OTP has expired. Please request a new one.");
        }

        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        emailService.sendWelcomeEmail(user.getEmail(), user.getName());

        return new ApiResponse(true, "Email verified successfully! You can now login.");
    }

    public ApiResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return new ApiResponse(false, "Invalid email or password.");
        }

        if (!user.isVerified()) {
            return new ApiResponse(false, "Please verify your email before logging in.");
        }

        if (!user.isActive()) {
            return new ApiResponse(false, "Your account has been deactivated. Contact support.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ApiResponse(false, "Invalid email or password.");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("id", user.getId());
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        data.put("role", user.getRole().name());

        return new ApiResponse(true, "Login successful!", data);
    }

    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return new ApiResponse(false, "No account found with this email.");
        }

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        emailService.sendPasswordResetOtp(request.getEmail(), otp);

        return new ApiResponse(true, "OTP sent to your email. Check your inbox.");
    }

    public ApiResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            return new ApiResponse(false, "Invalid OTP. Please try again.");
        }

        if (user.getOtpExpiry() == null || LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            return new ApiResponse(false, "OTP expired. Please request a new one.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return new ApiResponse(true, "Password reset successfully! You can now login.");
    }

    public ApiResponse resendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isVerified()) {
            return new ApiResponse(false, "Account already verified.");
        }

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        emailService.sendRegistrationOtp(email, otp);

        return new ApiResponse(true, "New OTP sent to your email.");
    }
}
