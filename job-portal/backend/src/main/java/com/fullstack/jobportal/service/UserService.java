package com.fullstack.jobportal.service;

import com.fullstack.jobportal.dto.UserDto;
import com.fullstack.jobportal.entity.User;
import com.fullstack.jobportal.exception.ResourceNotFoundException;
import com.fullstack.jobportal.mapper.UserMapper;
import com.fullstack.jobportal.repository.UserRepository;
import com.fullstack.jobportal.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class managing user operations for the job portal platform.
 * 
 * <p>This service provides comprehensive user management functionality including
 * user profile retrieval, resume upload, user listing, and user deletion. It handles
 * both candidate and recruiter user operations.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Current user profile retrieval</li>
 *   <li>User profile retrieval by ID</li>
 *   <li>Resume upload and storage</li>
 *   <li>User listing with pagination</li>
 *   <li>User count statistics</li>
 *   <li>User deletion with validation</li>
 *   <li>Transaction management for data consistency</li>
 * </ul>
 * 
 * <p><b>Business Logic:</b></p>
 * <ul>
 *   <li>Users can upload resumes which are stored securely</li>
 *   <li>Resume file paths are stored in user profile</li>
 *   <li>User deletion permanently removes user records</li>
 *   <li>All user operations respect authentication and authorization</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class UserService {

    /** Repository for user persistence operations */
    private final UserRepository userRepository;
    
    /** Mapper for converting between User entities and DTOs */
    private final UserMapper userMapper;
    
    /** Service for handling resume file storage operations */
    private final FileStorageService fileStorageService;

    /**
     * Retrieves the profile of the currently authenticated user.
     * 
     * <p>This method fetches the complete user profile for the logged-in user,
     * including resume information if uploaded.</p>
     * 
     * @return {@link UserDto} containing the current user's profile details
     * @throws ResourceNotFoundException if the user does not exist (should not occur in normal flow)
     * 
     * @see UserDto
     */
    public UserDto getCurrentUser() {
        // Get current authenticated user principal
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Retrieve user entity
        User user = userRepository.findById(principal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", principal.getId()));
        
        // Convert to DTO and return
        return userMapper.toDto(user);
    }

    /**
     * Retrieves a specific user's profile by their unique identifier.
     * 
     * @param id the unique identifier of the user to retrieve
     * @return {@link UserDto} containing the user's profile details
     * @throws ResourceNotFoundException if the user with the given ID does not exist
     * 
     * @see UserDto
     */
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toDto(user);
    }

    /**
     * Uploads a resume file for the currently authenticated user.
     * 
     * <p>This method handles resume file upload, validation, storage, and updates
     * the user's profile with the resume file path and filename. The file is
     * stored securely using the FileStorageService.</p>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that resume upload and profile update are atomic and consistent.</p>
     * 
     * @param file the resume file to upload (must be PDF, DOC, or DOCX)
     * @return {@link UserDto} containing the updated user profile with resume information
     * @throws ResourceNotFoundException if the user does not exist
     * @throws BadRequestException if file validation fails (handled by FileStorageService)
     * 
     * @see UserDto
     * @see FileStorageService
     */
    @Transactional
    public UserDto uploadResume(MultipartFile file) {
        // Get current authenticated user principal
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Retrieve user entity
        User user = userRepository.findById(principal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", principal.getId()));
        
        // Store resume file and get full path
        String path = fileStorageService.storeResume(file, user.getId());
        
        // Update user profile with resume information
        user.setResumePath(path);
        user.setResumeFileName(fileStorageService.getStoredFileName(path));
        user = userRepository.save(user);
        
        // Convert to DTO and return
        return userMapper.toDto(user);
    }

    /**
     * Retrieves paginated list of all users in the system.
     * 
     * <p>This method fetches all users with pagination support, useful for
     * administrative operations and user management interfaces.</p>
     * 
     * @param pageable pagination parameters (page number, size, sorting)
     * @return list of {@link UserDto} objects representing users in the requested page
     * 
     * @see UserDto
     * @see Pageable
     */
    public List<UserDto> getAllUsers(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return page.getContent().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves the total count of users in the system.
     * 
     * <p>This method provides a quick way to get user statistics without
     * fetching all user records.</p>
     * 
     * @return the total number of users in the system
     */
    public long countAllUsers() {
        return userRepository.count();
    }

    /**
     * Permanently deletes a user from the system.
     * 
     * <p>This method removes a user after validating their existence. The operation
     * is transactional to ensure data consistency.</p>
     * 
     * <p><b>Note:</b> Consider the impact on associated job applications and job
     * postings before deleting a user. Users with active applications or job postings
     * should typically be deactivated rather than deleted.</p>
     * 
     * @param id the unique identifier of the user to delete
     * @throws ResourceNotFoundException if the user with the given ID does not exist
     */
    @Transactional
    public void deleteUser(Long id) {
        // Validate user existence before deletion
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        
        // Permanently delete the user
        userRepository.deleteById(id);
    }
}
