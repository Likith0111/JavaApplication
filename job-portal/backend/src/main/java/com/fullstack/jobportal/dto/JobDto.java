package com.fullstack.jobportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String roleType;
    private Integer minExperience;
    private Integer maxExperience;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private Boolean active;
    private Long recruiterId;
    private String recruiterName;
    private Instant createdAt;
}
