package com.fullstack.jobportal.repository;

import com.fullstack.jobportal.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByRecruiterIdAndActiveTrue(Long recruiterId, Pageable pageable);

    Page<Job> findByActiveTrue(Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.active = true " +
            "AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:roleType IS NULL OR LOWER(j.roleType) LIKE LOWER(CONCAT('%', :roleType, '%'))) " +
            "AND (:minExp IS NULL OR j.maxExperience >= :minExp) " +
            "AND (:maxExp IS NULL OR j.minExperience <= :maxExp)")
    Page<Job> search(@Param("location") String location,
                     @Param("roleType") String roleType,
                     @Param("minExp") Integer minExp,
                     @Param("maxExp") Integer maxExp,
                     Pageable pageable);
}
