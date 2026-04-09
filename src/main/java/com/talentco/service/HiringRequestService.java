package com.talentco.service;

import com.talentco.dto.ApiResponse;
import com.talentco.dto.HiringRequestDto;
import com.talentco.dto.RejectRequestDto;
import com.talentco.entity.HiringRequest;
import com.talentco.entity.User;
import com.talentco.repository.HiringRequestRepository;
import com.talentco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HiringRequestService {

    @Autowired
    private HiringRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    public ApiResponse sendRequest(String clientEmail, HiringRequestDto dto) {
        User client = userRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        User professional = userRepository.findById(dto.getProfessionalId())
                .orElseThrow(() -> new RuntimeException("Professional not found"));

        if (professional.getRole() != User.Role.PROFESSIONAL) {
            return new ApiResponse(false, "Target user is not a professional.");
        }

        boolean alreadyPending = requestRepository.existsByClientAndProfessionalAndStatus(
                client, professional, HiringRequest.Status.PENDING);

        if (alreadyPending) {
            return new ApiResponse(false, "You already have a pending request to this professional.");
        }

        HiringRequest request = new HiringRequest();
        request.setClient(client);
        request.setProfessional(professional);
        request.setProjectTitle(dto.getProjectTitle());
        request.setMessage(dto.getMessage());
        request.setBudget(dto.getBudget());
        request.setStatus(HiringRequest.Status.PENDING);

        HiringRequest saved = requestRepository.save(request);
        return new ApiResponse(true, "Hiring request sent successfully!", saved.getId());
    }

    public List<HiringRequest> getRequestsForProfessional(String email) {
        User professional = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Professional not found"));
        return requestRepository.findByProfessionalOrderByCreatedAtDesc(professional);
    }

    public List<HiringRequest> getRequestsForClient(String email) {
        User client = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return requestRepository.findByClientOrderByCreatedAtDesc(client);
    }

    public ApiResponse acceptRequest(Long requestId, String professionalEmail) {
        HiringRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getProfessional().getEmail().equals(professionalEmail)) {
            return new ApiResponse(false, "Unauthorized: This is not your request.");
        }

        if (request.getStatus() != HiringRequest.Status.PENDING) {
            return new ApiResponse(false, "Request is no longer pending.");
        }

        request.setStatus(HiringRequest.Status.ACCEPTED);
        requestRepository.save(request);

        return new ApiResponse(true, "Hiring request accepted!");
    }

    public ApiResponse rejectRequest(Long requestId, String professionalEmail, RejectRequestDto dto) {
        HiringRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getProfessional().getEmail().equals(professionalEmail)) {
            return new ApiResponse(false, "Unauthorized: This is not your request.");
        }

        if (request.getStatus() != HiringRequest.Status.PENDING) {
            return new ApiResponse(false, "Request is no longer pending.");
        }

        request.setStatus(HiringRequest.Status.REJECTED);
        if (dto != null && dto.getReason() != null) {
            request.setRejectionReason(dto.getReason());
        }
        requestRepository.save(request);

        return new ApiResponse(true, "Hiring request rejected.");
    }

    public List<HiringRequest> getAllRequests() {
        return requestRepository.findAll();
    }
}
