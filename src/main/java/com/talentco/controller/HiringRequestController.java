package com.talentco.controller;

import com.talentco.dto.ApiResponse;
import com.talentco.dto.HiringRequestDto;
import com.talentco.dto.RejectRequestDto;
import com.talentco.entity.HiringRequest;
import com.talentco.service.HiringRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@Tag(name = "Hiring Requests", description = "Clients send requests; Professionals accept or reject")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*")
public class HiringRequestController {

    @Autowired
    private HiringRequestService hiringRequestService;

    @PostMapping
    @Operation(summary = "Send a hiring request", description = "Client sends a request to a professional")
    public ResponseEntity<ApiResponse> sendRequest(@RequestBody HiringRequestDto dto,
                                                    Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(hiringRequestService.sendRequest(email, dto));
    }

    @GetMapping("/client")
    @Operation(summary = "Get all requests sent by client", description = "View your requests with status")
    public ResponseEntity<List<HiringRequest>> getClientRequests(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(hiringRequestService.getRequestsForClient(email));
    }

    @GetMapping("/professional")
    @Operation(summary = "Get incoming requests for professional", description = "View all requests received")
    public ResponseEntity<List<HiringRequest>> getProfessionalRequests(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(hiringRequestService.getRequestsForProfessional(email));
    }

    @PutMapping("/{id}/accept")
    @Operation(summary = "Accept a hiring request")
    public ResponseEntity<ApiResponse> acceptRequest(@PathVariable Long id,
                                                      Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(hiringRequestService.acceptRequest(id, email));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject a hiring request", description = "Professional can reject with optional reason")
    public ResponseEntity<ApiResponse> rejectRequest(@PathVariable Long id,
                                                      @RequestBody(required = false) RejectRequestDto dto,
                                                      Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(hiringRequestService.rejectRequest(id, email, dto));
    }
}
