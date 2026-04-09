package com.talentco.repository;

import com.talentco.entity.Feedback;
import com.talentco.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByProfessional(User professional);
    List<Feedback> findByClient(User client);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.professional.id = :professionalId")
    Double findAverageRatingByProfessionalId(@Param("professionalId") Long professionalId);

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.professional.id = :professionalId")
    Long countByProfessionalId(@Param("professionalId") Long professionalId);
}
