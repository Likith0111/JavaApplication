package com.fullstack.jobportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCreateRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 2000)
    private String description;

    @NotBlank(message = "Location is required")
    @Size(max = 100)
    private String location;

    @NotBlank(message = "Role type is required")
    @Size(max = 100)
    private String roleType;

    @NotNull(message = "Min experience is required")
    private Integer minExperience;

    @NotNull(message = "Max experience is required")
    private Integer maxExperience;

    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
}
