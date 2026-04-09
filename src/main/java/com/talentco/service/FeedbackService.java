package com.talentco.service;

import com.talentco.dto.ApiResponse;
import com.talentco.dto.FeedbackDto;
import com.talentco.entity.Feedback;
import com.talentco.entity.ProfessionalProfile;
import com.talentco.entity.User;
import com.talentco.repository.FeedbackRepository;
import com.talentco.repository.ProfessionalProfileRepository;
import com.talentco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessionalProfileRepository profileRepository;

    public ApiResponse submitFeedback(String clientEmail, FeedbackDto dto) {
        User client = userRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        User professional = userRepository.findById(dto.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Professional not found"));

        if (dto.getRating() < 1 || dto.getRating() > 5) {
            return new ApiResponse(false, "Rating must be between 1 and 5.");
        }

        Feedback feedback = new Feedback();
        feedback.setClient(client);
        feedback.setProfessional(professional);
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        feedbackRepository.save(feedback);

        // Update professional average rating
        Double avgRating = feedbackRepository.findAverageRatingByProfessionalId(professional.getId());
        Long totalReviews = feedbackRepository.countByProfessionalId(professional.getId());

        Optional<ProfessionalProfile> profileOpt = profileRepository.findByUser(professional);
        if (profileOpt.isPresent()) {
            ProfessionalProfile profile = profileOpt.get();
            profile.setAverageRating(avgRating != null ? avgRating : 0.0);
            profile.setTotalReviews(totalReviews != null ? totalReviews.intValue() : 0);
            profileRepository.save(profile);
        }

        return new ApiResponse(true, "Feedback submitted successfully!");
    }

    public List<Feedback> getFeedbackForProfessional(Long professionalId) {
        User professional = userRepository.findById(professionalId)
                .orElseThrow(() -> new RuntimeException("Professional not found"));
        return feedbackRepository.findByProfessional(professional);
    }

    public List<Feedback> getFeedbackByClient(String clientEmail) {
        User client = userRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return feedbackRepository.findByClient(client);
    }
}
