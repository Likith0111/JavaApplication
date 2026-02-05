package com.fullstack.jobportal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyRequest {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    private String coverLetter;
}
