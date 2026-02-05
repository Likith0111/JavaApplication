package com.fullstack.jobportal.mapper;

import com.fullstack.jobportal.dto.JobApplicationDto;
import com.fullstack.jobportal.entity.JobApplication;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper {

    public JobApplicationDto toDto(JobApplication app) {
        if (app == null) return null;
        return JobApplicationDto.builder()
                .id(app.getId())
                .jobId(app.getJob() != null ? app.getJob().getId() : null)
                .jobTitle(app.getJob() != null ? app.getJob().getTitle() : null)
                .candidateId(app.getCandidate() != null ? app.getCandidate().getId() : null)
                .candidateName(app.getCandidate() != null ? app.getCandidate().getName() : null)
                .candidateEmail(app.getCandidate() != null ? app.getCandidate().getEmail() : null)
                .coverLetter(app.getCoverLetter())
                .status(app.getStatus())
                .appliedAt(app.getAppliedAt())
                .build();
    }
}
