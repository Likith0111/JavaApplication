package com.fullstack.jobportal.dto;

import com.fullstack.jobportal.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDto {

    private Long id;
    private Long jobId;
    private String jobTitle;
    private Long candidateId;
    private String candidateName;
    private String candidateEmail;
    private String coverLetter;
    private ApplicationStatus status;
    private Instant appliedAt;
}
