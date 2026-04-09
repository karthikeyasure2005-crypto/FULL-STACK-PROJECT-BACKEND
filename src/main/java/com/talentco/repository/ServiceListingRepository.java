package com.talentco.repository;

import com.talentco.entity.ServiceListing;
import com.talentco.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceListingRepository extends JpaRepository<ServiceListing, Long> {
    List<ServiceListing> findByProfessional(User professional);
    List<ServiceListing> findByProfessionalAndIsActive(User professional, boolean isActive);
    List<ServiceListing> findByCategory(String category);
    List<ServiceListing> findByIsActive(boolean isActive);
}
