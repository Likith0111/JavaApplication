package com.fullstack.jobportal.repository;

import com.fullstack.jobportal.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Page<JobApplication> findByCandidateId(Long candidateId, Pageable pageable);

    Page<JobApplication> findByJobRecruiterId(Long recruiterId, Pageable pageable);

    Optional<JobApplication> findByJobIdAndCandidateId(Long jobId, Long candidateId);

    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
}
