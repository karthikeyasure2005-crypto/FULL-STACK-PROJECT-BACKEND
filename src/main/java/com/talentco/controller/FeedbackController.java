package com.talentco.controller;

import com.talentco.dto.ApiResponse;
import com.talentco.dto.FeedbackDto;
import com.talentco.entity.Feedback;
import com.talentco.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@Tag(name = "Feedback", description = "Clients leave ratings and reviews for professionals")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    @Operation(summary = "Submit feedback", description = "Client submits a star rating and comment")
    public ResponseEntity<ApiResponse> submitFeedback(@RequestBody FeedbackDto dto,
                                                       Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(feedbackService.submitFeedback(email, dto));
    }

    @GetMapping("/professional/{professionalId}")
    @Operation(summary = "Get feedback for a professional")
    public ResponseEntity<List<Feedback>> getFeedback(@PathVariable Long professionalId) {
        return ResponseEntity.ok(feedbackService.getFeedbackForProfessional(professionalId));
    }

    @GetMapping("/my-feedback")
    @Operation(summary = "Get feedback submitted by the logged-in client")
    public ResponseEntity<List<Feedback>> getMyFeedback(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(feedbackService.getFeedbackByClient(email));
    }
}
