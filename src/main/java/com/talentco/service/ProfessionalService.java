package com.talentco.service;

import com.talentco.dto.ProfileUpdateDto;
import com.talentco.dto.ServiceListingDto;
import com.talentco.entity.ProfessionalProfile;
import com.talentco.entity.ServiceListing;
import com.talentco.entity.User;
import com.talentco.repository.ProfessionalProfileRepository;
import com.talentco.repository.ServiceListingRepository;
import com.talentco.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalService {

    @Autowired
    private ProfessionalProfileRepository profileRepository;

    @Autowired
    private ServiceListingRepository serviceListingRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ProfessionalProfile> getAllProfessionals() {
        return profileRepository.findAll();
    }

    public Optional<ProfessionalProfile> getProfessionalById(Long id) {
        return profileRepository.findById(id);
    }

    public Optional<ProfessionalProfile> getProfessionalByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    public List<ProfessionalProfile> searchProfessionals(String category, String location, String skill) {
        return profileRepository.searchProfessionals(
            (category != null && category.isBlank()) ? null : category,
            (location != null && location.isBlank()) ? null : location,
            (skill != null && skill.isBlank()) ? null : skill
        );
    }

    public ProfessionalProfile updateProfile(String email, ProfileUpdateDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfessionalProfile profile = profileRepository.findByUser(user)
                .orElse(new ProfessionalProfile());

        profile.setUser(user);
        if (dto.getBio() != null) profile.setBio(dto.getBio());
        if (dto.getSkills() != null) profile.setSkills(dto.getSkills());
        if (dto.getHourlyRate() != null) profile.setHourlyRate(dto.getHourlyRate());
        if (dto.getLocation() != null) profile.setLocation(dto.getLocation());
        if (dto.getAvailability() != null) profile.setAvailability(dto.getAvailability());
        if (dto.getCategory() != null) profile.setCategory(dto.getCategory());
        if (dto.getExperienceYears() != null) profile.setExperienceYears(dto.getExperienceYears());

        return profileRepository.save(profile);
    }

    public ServiceListing addService(String email, ServiceListingDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ServiceListing listing = new ServiceListing();
        listing.setProfessional(user);
        listing.setTitle(dto.getTitle());
        listing.setDescription(dto.getDescription());
        listing.setCategory(dto.getCategory());
        listing.setPrice(dto.getPrice());

        return serviceListingRepository.save(listing);
    }

    public List<ServiceListing> getMyServices(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return serviceListingRepository.findByProfessional(user);
    }

    public void deleteService(Long serviceId, String email) {
        ServiceListing listing = serviceListingRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        if (!listing.getProfessional().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized: Cannot delete another professional's service");
        }

        serviceListingRepository.delete(listing);
    }

    public List<ServiceListing> getServicesByProfessional(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return serviceListingRepository.findByProfessionalAndIsActive(user, true);
    }
}
