package com.fullstack.jobportal.controller;

import com.fullstack.jobportal.dto.JobCreateRequest;
import com.fullstack.jobportal.dto.JobDto;
import com.fullstack.jobportal.dto.PageResponse;
import com.fullstack.jobportal.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling job-related HTTP requests.
 * 
 * <p>This controller exposes endpoints for job posting management in the job
 * portal platform. It provides functionality for searching jobs, creating job
 * postings, and managing job listings.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/jobs}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/jobs/public/search} - Public job search with filters</li>
 *   <li>{@code GET /api/jobs/public/{id}} - Get public job details</li>
 *   <li>{@code GET /api/jobs/my-jobs} - Get recruiter's posted jobs</li>
 *   <li>{@code GET /api/jobs/{id}} - Get job by ID</li>
 *   <li>{@code POST /api/jobs} - Create new job posting</li>
 *   <li>{@code PUT /api/jobs/{id}} - Update existing job posting</li>
 *   <li>{@code DELETE /api/jobs/{id}} - Delete job posting</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Public read access for searching and viewing jobs.
 * Create/Update/Delete operations require recruiter authentication.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see JobService
 * @see JobDto
 * @see PageResponse
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    /** Service layer dependency for job operations */
    private final JobService jobService;

    /**
     * Searches for jobs with optional filters (public access).
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/jobs/public/search}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Query Parameters:</b></p>
     * <ul>
     *   <li>{@code location} - Filter by location (optional)</li>
     *   <li>{@code roleType} - Filter by role type (optional)</li>
     *   <li>{@code minExp} - Minimum years of experience (optional)</li>
     *   <li>{@code maxExp} - Maximum years of experience (optional)</li>
     *   <li>{@code page} - Page number (default: 0)</li>
     *   <li>{@code size} - Page size (default: 10)</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "content": [
     *     {
     *       "id": 1,
     *       "title": "Software Engineer",
     *       "description": "Full-stack developer needed",
     *       "location": "New York",
     *       "roleType": "FULL_TIME",
     *       "experience": 3
     *     }
     *   ],
     *   "page": 0,
     *   "size": 10,
     *   "totalElements": 25,
     *   "totalPages": 3
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET "http://localhost:8080/api/jobs/public/search?location=New%20York&minExp=2&maxExp=5&page=0&size=10"
     * </pre>
     * 
     * @param location optional location filter
     * @param roleType optional role type filter
     * @param minExp optional minimum experience filter
     * @param maxExp optional maximum experience filter
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return ResponseEntity containing PageResponse with JobDto objects
     */
    @GetMapping("/public/search")
    public ResponseEntity<PageResponse<JobDto>> searchPublic(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String roleType,
            @RequestParam(required = false) Integer minExp,
            @RequestParam(required = false) Integer maxExp,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(jobService.searchPublic(location, roleType, minExp, maxExp, pageable));
    }

    /**
     * Retrieves a specific job by its ID (public access).
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/jobs/public/{id}}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Job ID</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "title": "Software Engineer",
     *   "description": "Full-stack developer needed",
     *   "location": "New York",
     *   "roleType": "FULL_TIME",
     *   "experience": 3,
     *   "salary": 80000
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/jobs/public/1
     * </pre>
     * 
     * @param id job ID
     * @return ResponseEntity containing JobDto
     */
    @GetMapping("/public/{id}")
    public ResponseEntity<JobDto> getByIdPublic(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getById(id));
    }

    /**
     * Retrieves a paginated list of jobs posted by the current recruiter.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/jobs/my-jobs}</p>
     * <p><b>Access:</b> Authenticated recruiters only</p>
     * 
     * <p><b>Query Parameters:</b></p>
     * <ul>
     *   <li>{@code page} - Page number (default: 0)</li>
     *   <li>{@code size} - Page size (default: 10)</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "content": [
     *     {
     *       "id": 1,
     *       "title": "Software Engineer",
     *       "location": "New York",
     *       "createdAt": "2024-01-15T10:30:00Z"
     *     }
     *   ],
     *   "page": 0,
     *   "size": 10,
     *   "totalElements": 5,
     *   "totalPages": 1
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET "http://localhost:8080/api/jobs/my-jobs?page=0&size=10" \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return ResponseEntity containing PageResponse with JobDto objects
     */
    @GetMapping("/my-jobs")
    public ResponseEntity<PageResponse<JobDto>> getMyPostedJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(jobService.getMyPostedJobs(pageable));
    }

    /**
     * Retrieves a specific job by its ID.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/jobs/{id}}</p>
     * <p><b>Access:</b> Authenticated users</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Job ID</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "title": "Software Engineer",
     *   "description": "Full-stack developer needed",
     *   "location": "New York",
     *   "roleType": "FULL_TIME",
     *   "experience": 3,
     *   "salary": 80000
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/jobs/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id job ID
     * @return ResponseEntity containing JobDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getById(id));
    }

    /**
     * Creates a new job posting.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/jobs}</p>
     * <p><b>Access:</b> Authenticated recruiters only</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "title": "Software Engineer",
     *   "description": "Full-stack developer needed",
     *   "location": "New York",
     *   "roleType": "FULL_TIME",
     *   "experience": 3,
     *   "salary": 80000
     * }
     * </pre>
     * 
     * <p><b>Success Response (201 Created):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "title": "Software Engineer",
     *   "description": "Full-stack developer needed",
     *   "location": "New York",
     *   "roleType": "FULL_TIME",
     *   "experience": 3,
     *   "salary": 80000,
     *   "createdAt": "2024-01-15T10:30:00Z"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/jobs \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"title":"Software Engineer","location":"New York","roleType":"FULL_TIME","experience":3}'
     * </pre>
     * 
     * @param request validated job creation request
     * @return ResponseEntity containing created JobDto with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<JobDto> create(@Valid @RequestBody JobCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.create(request));
    }

    /**
     * Updates an existing job posting.
     * 
     * <p><b>HTTP Method:</b> PUT</p>
     * <p><b>Endpoint:</b> {@code /api/jobs/{id}}</p>
     * <p><b>Access:</b> Authenticated recruiters (own jobs only)</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Job ID to update</li>
     * </ul>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "title": "Senior Software Engineer",
     *   "description": "Updated description",
     *   "location": "San Francisco",
     *   "roleType": "FULL_TIME",
     *   "experience": 5,
     *   "salary": 120000
     * }
     * </pre>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "title": "Senior Software Engineer",
     *   "description": "Updated description",
     *   "location": "San Francisco",
     *   "roleType": "FULL_TIME",
     *   "experience": 5,
     *   "salary": 120000
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X PUT http://localhost:8080/api/jobs/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"title":"Senior Software Engineer","location":"San Francisco","experience":5}'
     * </pre>
     * 
     * @param id job ID to update
     * @param request validated job update request
     * @return ResponseEntity containing updated JobDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobDto> update(@PathVariable Long id, @Valid @RequestBody JobCreateRequest request) {
        return ResponseEntity.ok(jobService.update(id, request));
    }

    /**
     * Deletes a job posting.
     * 
     * <p><b>HTTP Method:</b> DELETE</p>
     * <p><b>Endpoint:</b> {@code /api/jobs/{id}}</p>
     * <p><b>Access:</b> Authenticated recruiters (own jobs only)</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Job ID to delete</li>
     * </ul>
     * 
     * <p><b>Success Response (204 No Content):</b></p>
     * Empty response body
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X DELETE http://localhost:8080/api/jobs/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id job ID to delete
     * @return ResponseEntity with no content (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
