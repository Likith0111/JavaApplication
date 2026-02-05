package com.fullstack.jobportal.controller;

import com.fullstack.jobportal.dto.ApplyRequest;
import com.fullstack.jobportal.dto.JobApplicationDto;
import com.fullstack.jobportal.dto.PageResponse;
import com.fullstack.jobportal.dto.UpdateApplicationStatusRequest;
import com.fullstack.jobportal.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling job application-related HTTP requests.
 * 
 * <p>This controller exposes endpoints for job application management in the
 * job portal platform. It allows candidates to apply for jobs, view their
 * applications, and enables recruiters to manage application statuses.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/applications}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code POST /api/applications} - Submit new job application</li>
 *   <li>{@code GET /api/applications/my-applications} - Get candidate's applications</li>
 *   <li>{@code GET /api/applications/recruiter} - Get applications for recruiter's jobs</li>
 *   <li>{@code PATCH /api/applications/{id}/status} - Update application status</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Authenticated users. Candidates can view
 * their own applications, recruiters can manage applications for their posted jobs.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see JobApplicationService
 * @see JobApplicationDto
 * @see PageResponse
 */
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    /** Service layer dependency for job application operations */
    private final JobApplicationService applicationService;

    /**
     * Submits a new job application for a job posting.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/applications}</p>
     * <p><b>Access:</b> Authenticated candidates only</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "jobId": 1,
     *   "coverLetter": "I am interested in this position..."
     * }
     * </pre>
     * 
     * <p><b>Success Response (201 Created):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "jobId": 1,
     *   "jobTitle": "Software Engineer",
     *   "candidateId": 5,
     *   "candidateName": "John Doe",
     *   "coverLetter": "I am interested...",
     *   "status": "PENDING",
     *   "appliedAt": "2024-01-15T10:30:00Z"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/applications \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"jobId":1,"coverLetter":"I am interested..."}'
     * </pre>
     * 
     * @param request validated application request with job ID and cover letter
     * @return ResponseEntity containing created JobApplicationDto with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<JobApplicationDto> apply(@Valid @RequestBody ApplyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.apply(request));
    }

    /**
     * Retrieves a paginated list of applications submitted by the current candidate.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/applications/my-applications}</p>
     * <p><b>Access:</b> Authenticated candidates only</p>
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
     *       "jobId": 1,
     *       "jobTitle": "Software Engineer",
     *       "status": "PENDING",
     *       "appliedAt": "2024-01-15T10:30:00Z"
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
     * curl -X GET "http://localhost:8080/api/applications/my-applications?page=0&size=10" \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return ResponseEntity containing PageResponse with JobApplicationDto objects
     */
    @GetMapping("/my-applications")
    public ResponseEntity<PageResponse<JobApplicationDto>> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return ResponseEntity.ok(applicationService.getMyApplications(pageable));
    }

    /**
     * Retrieves a paginated list of applications for jobs posted by the current recruiter.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/applications/recruiter}</p>
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
     *       "jobId": 1,
     *       "jobTitle": "Software Engineer",
     *       "candidateId": 5,
     *       "candidateName": "John Doe",
     *       "status": "PENDING",
     *       "appliedAt": "2024-01-15T10:30:00Z"
     *     }
     *   ],
     *   "page": 0,
     *   "size": 10,
     *   "totalElements": 20,
     *   "totalPages": 2
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET "http://localhost:8080/api/applications/recruiter?page=0&size=10" \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return ResponseEntity containing PageResponse with JobApplicationDto objects
     */
    @GetMapping("/recruiter")
    public ResponseEntity<PageResponse<JobApplicationDto>> getApplicationsForRecruiter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return ResponseEntity.ok(applicationService.getApplicationsForRecruiter(pageable));
    }

    /**
     * Updates the status of a job application.
     * 
     * <p><b>HTTP Method:</b> PATCH</p>
     * <p><b>Endpoint:</b> {@code /api/applications/{id}/status}</p>
     * <p><b>Access:</b> Authenticated recruiters (for their posted jobs)</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Application ID</li>
     * </ul>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "status": "ACCEPTED"
     * }
     * </pre>
     * 
     * <p><b>Valid Status Values:</b> PENDING, ACCEPTED, REJECTED</p>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "jobId": 1,
     *   "jobTitle": "Software Engineer",
     *   "candidateId": 5,
     *   "status": "ACCEPTED",
     *   "appliedAt": "2024-01-15T10:30:00Z"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X PATCH http://localhost:8080/api/applications/1/status \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"status":"ACCEPTED"}'
     * </pre>
     * 
     * @param id application ID to update
     * @param request validated request containing new status
     * @return ResponseEntity containing updated JobApplicationDto
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplicationDto> updateStatus(@PathVariable Long id,
                                                          @Valid @RequestBody UpdateApplicationStatusRequest request) {
        return ResponseEntity.ok(applicationService.updateStatus(id, request));
    }
}
