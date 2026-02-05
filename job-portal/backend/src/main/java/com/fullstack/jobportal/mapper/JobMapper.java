package com.fullstack.jobportal.mapper;

import com.fullstack.jobportal.dto.JobDto;
import com.fullstack.jobportal.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public JobDto toDto(Job job) {
        if (job == null) return null;
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .roleType(job.getRoleType())
                .minExperience(job.getMinExperience())
                .maxExperience(job.getMaxExperience())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .active(job.getActive())
                .recruiterId(job.getRecruiter() != null ? job.getRecruiter().getId() : null)
                .recruiterName(job.getRecruiter() != null ? job.getRecruiter().getName() : null)
                .createdAt(job.getCreatedAt())
                .build();
    }
}
