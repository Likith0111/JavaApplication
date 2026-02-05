package com.fullstack.jobportal.service;

import com.fullstack.jobportal.dto.JobCreateRequest;
import com.fullstack.jobportal.dto.JobDto;
import com.fullstack.jobportal.dto.PageResponse;
import com.fullstack.jobportal.entity.Job;
import com.fullstack.jobportal.entity.User;
import com.fullstack.jobportal.exception.BadRequestException;
import com.fullstack.jobportal.exception.ResourceNotFoundException;
import com.fullstack.jobportal.mapper.JobMapper;
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
 * Service class managing job posting operations for the job portal platform.
 * 
 * <p>This service provides comprehensive job management functionality including
 * creating, reading, updating, and deleting job postings. It supports public job
 * search, recruiter-specific job management, and job lifecycle management.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Public job search with multiple filter criteria</li>
 *   <li>Recruiter-specific job posting management</li>
 *   <li>Job creation with comprehensive details</li>
 *   <li>Job updates with authorization checks</li>
 *   <li>Soft deletion (deactivation) of job postings</li>
 *   <li>Experience range validation</li>
 *   <li>Pagination support for job listings</li>
 *   <li>Transaction management for data consistency</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Jobs are created as active by default</li>
 *   <li>Only the recruiter who posted a job can update or delete it</li>
 *   <li>Job deletion is soft (sets active=false) to preserve application history</li>
 *   <li>Minimum experience cannot exceed maximum experience</li>
 *   <li>Public search supports filtering by location, role type, and experience range</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class JobService {

    /** Repository for job persistence operations */
    private final JobRepository jobRepository;
    
    /** Repository for user data retrieval */
    private final UserRepository userRepository;
    
    /** Mapper for converting between Job entities and DTOs */
    private final JobMapper jobMapper;

    /**
     * Searches for active job postings with multiple filter criteria.
     * 
     * <p>This method performs a public search across all active job postings,
     * supporting filtering by location, role type, and experience range. Results
     * are paginated for efficient retrieval.</p>
     * 
     * @param location the location to filter by (optional, can be null)
     * @param roleType the role type to filter by (optional, can be null)
     * @param minExp the minimum years of experience (optional, can be null)
     * @param maxExp the maximum years of experience (optional, can be null)
     * @param pageable pagination parameters (page number, size, sorting)
     * @return {@link PageResponse} containing paginated list of matching active jobs
     * 
     * @see PageResponse
     * @see JobDto
     * @see Pageable
     */
    public PageResponse<JobDto> searchPublic(String location, String roleType, Integer minExp, Integer maxExp, Pageable pageable) {
        // Perform search with filters and pagination
        Page<Job> page = jobRepository.search(location, roleType, minExp, maxExp, pageable);
        return toPageResponse(page);
    }

    /**
     * Retrieves paginated active job postings created by the currently authenticated recruiter.
     * 
     * <p>This method fetches all active jobs posted by the logged-in recruiter,
     * enabling recruiters to manage their job postings.</p>
     * 
     * @param pageable pagination parameters (page number, size, sorting)
     * @return {@link PageResponse} containing paginated list of recruiter's active jobs
     * 
     * @see PageResponse
     * @see JobDto
     * @see Pageable
     */
    public PageResponse<JobDto> getMyPostedJobs(Pageable pageable) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Fetch paginated active jobs posted by this recruiter
        Page<Job> page = jobRepository.findByRecruiterIdAndActiveTrue(principal.getId(), pageable);
        return toPageResponse(page);
    }

    /**
     * Retrieves a specific job posting by its unique identifier.
     * 
     * @param id the unique identifier of the job to retrieve
     * @return {@link JobDto} containing the job details
     * @throws ResourceNotFoundException if the job with the given ID does not exist
     * 
     * @see JobDto
     */
    public JobDto getById(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job", id));
        return jobMapper.toDto(job);
    }

    /**
     * Creates a new job posting.
     * 
     * <p>This method creates a new job posting with the provided details. The job
     * is created as active by default and is associated with the currently authenticated
     * recruiter. Experience range is validated to ensure min does not exceed max.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that job creation is atomic and consistent.</p>
     * 
     * @param request the job creation request containing all job details
     * @return {@link JobDto} containing the created job details including generated ID
     * @throws ResourceNotFoundException if the recruiter user does not exist
     * @throws BadRequestException if min experience exceeds max experience
     * 
     * @see JobDto
     * @see JobCreateRequest
     */
    @Transactional
    public JobDto create(JobCreateRequest request) {
        // Get current authenticated user principal
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Retrieve recruiter user entity
        User recruiter = userRepository.findById(principal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", principal.getId()));
        
        // Validate experience range - min cannot exceed max
        if (request.getMinExperience() != null && request.getMaxExperience() != null && request.getMinExperience() > request.getMaxExperience()) {
            throw new BadRequestException("Min experience cannot be greater than max experience");
        }
        
        // Build job entity with provided details, set as active by default
        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .roleType(request.getRoleType())
                .minExperience(request.getMinExperience())
                .maxExperience(request.getMaxExperience())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .active(true) // Jobs are created as active
                .recruiter(recruiter)
                .build();
        
        // Persist job to database
        job = jobRepository.save(job);
        
        // Convert to DTO and return
        return jobMapper.toDto(job);
    }

    /**
     * Updates an existing job posting.
     * 
     * <p>This method allows modification of job details. Only the recruiter who
     * posted the job can update it, ensuring data security and integrity.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that job updates are atomic and consistent.</p>
     * 
     * <p><b>Authorization:</b> Only the recruiter who posted the job can update it.
     * Attempts by other users will result in a BadRequestException.</p>
     * 
     * @param id the unique identifier of the job to update
     * @param request the job update request containing new job details
     * @return {@link JobDto} containing the updated job details
     * @throws ResourceNotFoundException if the job does not exist
     * @throws BadRequestException if the user is not authorized to update this job
     * 
     * @see JobDto
     * @see JobCreateRequest
     */
    @Transactional
    public JobDto update(Long id, JobCreateRequest request) {
        // Retrieve job and validate existence
        Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job", id));
        
        // Get current authenticated user principal
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Authorization check - ensure user is the recruiter who posted this job
        if (!job.getRecruiter().getId().equals(principal.getId())) {
            throw new BadRequestException("Not authorized to update this job");
        }
        
        // Update job properties
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setRoleType(request.getRoleType());
        job.setMinExperience(request.getMinExperience());
        job.setMaxExperience(request.getMaxExperience());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        
        // Persist changes
        job = jobRepository.save(job);
        
        // Convert to DTO and return
        return jobMapper.toDto(job);
    }

    /**
     * Deactivates (soft deletes) a job posting.
     * 
     * <p>This method performs a soft deletion by setting the job's active flag to false.
     * The job record remains in the database to preserve application history, but it
     * will no longer appear in public searches or active job listings.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that job deactivation is atomic and consistent.</p>
     * 
     * <p><b>Authorization:</b> Only the recruiter who posted the job can delete it.
     * Attempts by other users will result in a BadRequestException.</p>
     * 
     * @param id the unique identifier of the job to deactivate
     * @throws ResourceNotFoundException if the job does not exist
     * @throws BadRequestException if the user is not authorized to delete this job
     */
    @Transactional
    public void delete(Long id) {
        // Retrieve job and validate existence
        Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Job", id));
        
        // Get current authenticated user principal
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Authorization check - ensure user is the recruiter who posted this job
        if (!job.getRecruiter().getId().equals(principal.getId())) {
            throw new BadRequestException("Not authorized to delete this job");
        }
        
        // Soft delete: set active flag to false (preserves application history)
        job.setActive(false);
        jobRepository.save(job);
    }

    /**
     * Converts a paginated Page of Job entities to a PageResponse DTO.
     * 
     * <p>This helper method transforms Spring Data's Page object to a custom
     * PageResponse DTO, including pagination metadata and converted content.</p>
     * 
     * @param page the Spring Data Page object containing Job entities
     * @return {@link PageResponse} containing paginated JobDto objects with metadata
     */
    private PageResponse<JobDto> toPageResponse(Page<Job> page) {
        // Convert entities to DTOs
        List<JobDto> content = page.getContent().stream().map(jobMapper::toDto).collect(Collectors.toList());
        
        // Build and return paginated response with metadata
        return PageResponse.<JobDto>builder()
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
