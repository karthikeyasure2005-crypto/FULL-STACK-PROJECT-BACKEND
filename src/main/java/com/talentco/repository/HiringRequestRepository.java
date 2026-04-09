package com.talentco.repository;

import com.talentco.entity.HiringRequest;
import com.talentco.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HiringRequestRepository extends JpaRepository<HiringRequest, Long> {
    List<HiringRequest> findByClient(User client);
    List<HiringRequest> findByProfessional(User professional);
    List<HiringRequest> findByClientOrderByCreatedAtDesc(User client);
    List<HiringRequest> findByProfessionalOrderByCreatedAtDesc(User professional);
    List<HiringRequest> findByStatus(HiringRequest.Status status);
    boolean existsByClientAndProfessionalAndStatus(User client, User professional, HiringRequest.Status status);
}
