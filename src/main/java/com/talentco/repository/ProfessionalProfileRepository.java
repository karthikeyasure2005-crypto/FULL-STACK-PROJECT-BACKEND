package com.talentco.repository;

import com.talentco.entity.ProfessionalProfile;
import com.talentco.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionalProfileRepository extends JpaRepository<ProfessionalProfile, Long> {
    Optional<ProfessionalProfile> findByUser(User user);
    Optional<ProfessionalProfile> findByUserId(Long userId);

    @Query("SELECT p FROM ProfessionalProfile p WHERE " +
           "(:category IS NULL OR p.category = :category) AND " +
           "(:location IS NULL OR p.location LIKE %:location%) AND " +
           "(:skill IS NULL OR p.skills LIKE %:skill%)")
    List<ProfessionalProfile> searchProfessionals(
        @Param("category") String category,
        @Param("location") String location,
        @Param("skill") String skill
    );

    List<ProfessionalProfile> findByCategory(String category);
}
