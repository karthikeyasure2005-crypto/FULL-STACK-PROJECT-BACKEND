package com.talentco.controller;

import com.talentco.dto.ApiResponse;
import com.talentco.dto.ProfileUpdateDto;
import com.talentco.dto.ServiceListingDto;
import com.talentco.entity.ProfessionalProfile;
import com.talentco.entity.ServiceListing;
import com.talentco.repository.UserRepository;
import com.talentco.service.ProfessionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professionals")
@Tag(name = "Professionals", description = "Professional profiles, search, and service listings")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*")
public class ProfessionalController {

    @Autowired
    private ProfessionalService professionalService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Get all professionals")
    public ResponseEntity<List<ProfessionalProfile>> getAllProfessionals() {
        return ResponseEntity.ok(professionalService.getAllProfessionals());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get professional profile by profile ID")
    public ResponseEntity<?> getProfessionalById(@PathVariable Long id) {
        Optional<ProfessionalProfile> profile = professionalService.getProfessionalById(id);
        if (profile.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(profile.get());
    }

    @GetMapping("/search")
    @Operation(summary = "Search professionals by category, location, or skill")
    public ResponseEntity<List<ProfessionalProfile>> searchProfessionals(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skill) {
        return ResponseEntity.ok(professionalService.searchProfessionals(category, location, skill));
    }

    @GetMapping("/my-profile")
    @Operation(summary = "Get my professional profile")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        com.talentco.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<ProfessionalProfile> profile = professionalService.getProfessionalByUserId(user.getId());
        if (profile.isEmpty()) return ResponseEntity.ok(new ApiResponse(false, "Profile not created yet."));
        return ResponseEntity.ok(profile.get());
    }

    @PutMapping("/profile")
    @Operation(summary = "Create or update my professional profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileUpdateDto dto, Authentication authentication) {
        String email = authentication.getName();
        ProfessionalProfile profile = professionalService.updateProfile(email, dto);
        return ResponseEntity.ok(new ApiResponse(true, "Profile updated!", profile));
    }

    @PostMapping("/services")
    @Operation(summary = "Add a new service listing")
    public ResponseEntity<?> addService(@RequestBody ServiceListingDto dto, Authentication authentication) {
        String email = authentication.getName();
        ServiceListing listing = professionalService.addService(email, dto);
        return ResponseEntity.ok(new ApiResponse(true, "Service added!", listing));
    }

    @GetMapping("/services/my")
    @Operation(summary = "Get my service listings")
    public ResponseEntity<List<ServiceListing>> getMyServices(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(professionalService.getMyServices(email));
    }

    @DeleteMapping("/services/{id}")
    @Operation(summary = "Delete a service listing")
    public ResponseEntity<?> deleteService(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        professionalService.deleteService(id, email);
        return ResponseEntity.ok(new ApiResponse(true, "Service deleted."));
    }

    @GetMapping("/user/{userId}/services")
    @Operation(summary = "Get services by professional user ID")
    public ResponseEntity<List<ServiceListing>> getServicesByProfessional(@PathVariable Long userId) {
        return ResponseEntity.ok(professionalService.getServicesByProfessional(userId));
    }
}
