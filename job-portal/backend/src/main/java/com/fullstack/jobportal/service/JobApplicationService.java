package com.fullstack.jobportal.service;

import com.fullstack.jobportal.dto.ApplyRequest;
import com.fullstack.jobportal.dto.JobApplicationDto;
import com.fullstack.jobportal.dto.PageResponse;
import com.fullstack.jobportal.dto.UpdateApplicationStatusRequest;
import com.fullstack.jobportal.entity.ApplicationStatus;
import com.fullstack.jobportal.entity.Job;
import com.fullstack.jobportal.entity.JobApplication;
import com.fullstack.jobportal.entity.User;
import com.fullstack.jobportal.exception.BadRequestException;
import com.fullstack.jobportal.exception.ResourceNotFoundException;
import com.fullstack.jobportal.mapper.JobApplicationMapper;
import com.fullstack.jobportal.repository.JobApplicationRepository;
import com.fullstack.jobportal.repository.JobRepository;
import com.fullstack.jobportal.repository.UserRepository;
import com.fullstack.jobportal.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class managing job application operations for the job portal platform.
 * 
 * <p>This service provides comprehensive application management functionality including
 * submitting applications, retrieving application history, and managing application
 * status updates. It handles the complete application lifecycle from submission to
 * recruiter review.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Job application submission with validation</li>
 *   <li>Duplicate application prevention</li>
 *   <li>Active job validation</li>
 *   <li>Candidate-specific application retrieval</li>
 *   <li>Recruiter-specific application retrieval</li>
 *   <li>Application status management (PENDING, REVIEWED, ACCEPTED, REJECTED)</li>
 *   <li>Pagination support for application listings</li>
 *   <li>Transaction management ensuring atomic operations</li>
 *   <li>Authorization checks for status updates</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Users can only apply once per job posting</li>
 *   <li>Applications can only be submitted for active job postings</li>
 *   <li>Only the job's recruiter can update application status</li>
 *   <li>Applications are initially created with PENDING status</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class JobApplicationService {

    /** Repository for job application persistence operations */
    private final JobApplicationRepository applicationRepository;
    
    /** Repository for job data retrieval and validation */
    private final JobRepository jobRepository;
    
    /** Repository for user data retrieval */
    private final UserRepository userRepository;
    
    /** Mapper for converting between JobApplication entities and DTOs */
    private final JobApplicationMapper mapper;

    /**
     * Submits a new job application for the currently authenticated user.
     * 
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Validates job exists and is active</li>
     *   <li>Checks for duplicate applications (user cannot apply twice)</li>
     *   <li>Retrieves candidate user details</li>
     *   <li>Creates application with PENDING status</li>
     *   <li>Persists application to database</li>
     *   <li>Returns application details</li>
     * </ol>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that application creation is atomic and consistent.</p>
     * 
     * <p><b>Validation:</b> The method prevents duplicate applications and ensures
     * only active jobs can receive applications.</p>
     * 
     * @param request the application request containing job ID and cover letter
     * @return {@link JobApplicationDto} containing the created application details
     * @throws ResourceNotFoundException if the job or user does not exist
     * @throws BadRequestException if the job is inactive or user has already applied
     * 
     * @see JobApplicationDto
     * @see ApplyRequest
     */
    @Transactional
    public JobApplicationDto apply(ApplyRequest request) {
        // Get current authenticated user principal
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Retrieve job and validate existence
        Job job = jobRepository.findById(request.getJobId()).orElseThrow(() -> new ResourceNotFoundException("Job", request.getJobId()));
        
        // Validate job is still active
        if (!job.getActive()) {
            throw new BadRequestException("Job is no longer active");
        }
        
        // Check for duplicate application - prevent multiple applications for same job
        if (applicationRepository.existsByJobIdAndCandidateId(job.getId(), principal.getId())) {
            throw new BadRequestException("You have already applied for this job");
        }
        
        // Retrieve candidate user entity
        User candidate = userRepository.findById(principal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", principal.getId()));
        
        // Create application with PENDING status
        JobApplication app = JobApplication.builder()
                .job(job)
                .candidate(candidate)
                .coverLetter(request.getCoverLetter())
                .status(ApplicationStatus.PENDING)
                .build();
        
        // Persist application to database
        app = applicationRepository.save(app);
        
        // Convert to DTO and return
        return mapper.toDto(app);
    }

    /**
     * Retrieves paginated applications submitted by the currently authenticated candidate.
     * 
     * <p>This method fetches all applications submitted by the logged-in user,
     * supporting pagination for efficient retrieval of large application histories.</p>
     * 
     * @param pageable pagination parameters (page number, size, sorting)
     * @return {@link PageResponse} containing paginated list of user's applications
     * 
     * @see PageResponse
     * @see JobApplicationDto
     * @see Pageable
     */
    public PageResponse<JobApplicationDto> getMyApplications(Pageable pageable) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Fetch paginated applications for the candidate
        Page<JobApplication> page = applicationRepository.findByCandidateId(principal.getId(), pageable);
        
        return toPageResponse(page);
    }

    /**
     * Retrieves paginated applications for jobs posted by the currently authenticated recruiter.
     * 
     * <p>This method fetches all applications for jobs posted by the logged-in recruiter,
     * enabling recruiters to review and manage applications for their job postings.</p>
     * 
     * @param pageable pagination parameters (page number, size, sorting)
     * @return {@link PageResponse} containing paginated list of applications for recruiter's jobs
     * 
     * @see PageResponse
     * @see JobApplicationDto
     * @see Pageable
     */
    public PageResponse<JobApplicationDto> getApplicationsForRecruiter(Pageable pageable) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Fetch paginated applications for jobs posted by this recruiter
        Page<JobApplication> page = applicationRepository.findByJobRecruiterId(principal.getId(), pageable);
        
        return toPageResponse(page);
    }

    /**
     * Updates the status of a job application.
     * 
     * <p>This method allows recruiters to update application status (e.g., from
     * PENDING to REVIEWED, ACCEPTED, or REJECTED). Only the recruiter who posted
     * the job can update its applications.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that status updates are atomic and consistent.</p>
     * 
     * <p><b>Authorization:</b> Only the recruiter who posted the job can update
     * applications for that job. Attempts by other users will result in a BadRequestException.</p>
     * 
     * @param applicationId the unique identifier of the application to update
     * @param request the status update request containing the new status
     * @return {@link JobApplicationDto} containing the updated application details
     * @throws ResourceNotFoundException if the application does not exist
     * @throws BadRequestException if the user is not authorized to update this application
     * 
     * @see JobApplicationDto
     * @see UpdateApplicationStatusRequest
     * @see ApplicationStatus
     */
    @Transactional
    public JobApplicationDto updateStatus(Long applicationId, UpdateApplicationStatusRequest request) {
        // Retrieve application and validate existence
        JobApplication app = applicationRepository.findById(applicationId).orElseThrow(() -> new ResourceNotFoundException("Application", applicationId));
        
        // Get current authenticated user principal
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Authorization check - ensure user is the recruiter who posted this job
        if (!app.getJob().getRecruiter().getId().equals(principal.getId())) {
            throw new BadRequestException("Not authorized to update this application");
        }
        
        // Update application status
        app.setStatus(request.getStatus());
        app = applicationRepository.save(app);
        
        // Convert to DTO and return
        return mapper.toDto(app);
    }

    /**
     * Converts a paginated Page of JobApplication entities to a PageResponse DTO.
     * 
     * <p>This helper method transforms Spring Data's Page object to a custom
     * PageResponse DTO, including pagination metadata and converted content.</p>
     * 
     * @param page the Spring Data Page object containing JobApplication entities
     * @return {@link PageResponse} containing paginated JobApplicationDto objects with metadata
     */
    private PageResponse<JobApplicationDto> toPageResponse(Page<JobApplication> page) {
        // Convert entities to DTOs
        List<JobApplicationDto> content = page.getContent().stream().map(mapper::toDto).collect(Collectors.toList());
        
        // Build and return paginated response with metadata
        return PageResponse.<JobApplicationDto>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
