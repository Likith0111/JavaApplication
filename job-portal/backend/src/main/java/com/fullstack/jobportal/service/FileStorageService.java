package com.fullstack.jobportal.service;

import com.fullstack.jobportal.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

/**
 * Service class managing file storage operations for the job portal platform.
 * 
 * <p>This service provides comprehensive file storage functionality specifically
 * designed for resume uploads. It handles file validation, secure storage,
 * and file name management with UUID-based naming to prevent conflicts.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Resume file upload with validation</li>
 *   <li>File type validation (PDF, DOC, DOCX only)</li>
 *   <li>Content type validation for security</li>
 *   <li>UUID-based file naming to prevent conflicts</li>
 *   <li>Automatic directory creation</li>
 *   <li>Secure file storage with user-specific naming</li>
 *   <li>File name extraction utilities</li>
 * </ul>
 * 
 * <p><b>Security Considerations:</b></p>
 * <ul>
 *   <li>Only PDF and Microsoft Word documents are accepted</li>
 *   <li>Both file extension and MIME type are validated</li>
 *   <li>Files are stored with UUID-based names to prevent path traversal attacks</li>
 *   <li>User ID is included in filename for traceability</li>
 * </ul>
 * 
 * <p><b>Configuration:</b></p>
 * <p>The upload directory can be configured via the "app.upload.dir" property.
 * Defaults to "${user.dir}/uploads/resumes" if not specified.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    /** Set of allowed MIME content types for resume uploads */
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    
    /** Set of allowed file extensions for resume uploads */
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "doc", "docx");

    /** Upload directory path, configurable via application properties */
    @Value("${app.upload.dir:${user.dir}/uploads/resumes}")
    private String uploadDir;

    /**
     * Stores a resume file with validation and secure naming.
     * 
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Validates file is not null or empty</li>
     *   <li>Validates file name is present</li>
     *   <li>Validates file extension against allowed types</li>
     *   <li>Validates MIME content type against allowed types</li>
     *   <li>Creates upload directory if it doesn't exist</li>
     *   <li>Generates secure filename with user ID and UUID</li>
     *   <li>Stores file to disk</li>
     *   <li>Returns the full path to the stored file</li>
     * </ol>
     * 
     * <p><b>File Naming:</b> Files are stored with the format "{userId}_{UUID}.{extension}"
     * to ensure uniqueness and prevent conflicts.</p>
     * 
     * @param file the multipart file to store (must be PDF, DOC, or DOCX)
     * @param userId the unique identifier of the user uploading the resume
     * @return the full path to the stored file
     * @throws BadRequestException if file is null, empty, has invalid extension,
     *                             or has invalid content type
     * @throws BadRequestException if file storage fails due to I/O errors
     */
    public String storeResume(MultipartFile file, Long userId) {
        // Validate file is present and not empty
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }
        
        // Validate file name exists
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new BadRequestException("Invalid file name");
        }
        
        // Extract and validate file extension
        String ext = getExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new BadRequestException("Only PDF and DOC/DOCX files are allowed");
        }
        
        // Validate MIME content type for additional security
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BadRequestException("Invalid file type. Only PDF and DOC/DOCX are allowed");
        }
        
        try {
            // Ensure upload directory exists
            Path root = Paths.get(uploadDir);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            
            // Generate secure filename: userId_UUID.extension
            String filename = userId + "_" + UUID.randomUUID() + "." + ext;
            Path target = root.resolve(filename);
            
            // Copy file to storage location
            Files.copy(file.getInputStream(), target);
            
            return target.toString();
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new BadRequestException("Failed to store file");
        }
    }

    /**
     * Extracts the filename from a full file path.
     * 
     * <p>This utility method extracts just the filename portion from a full
     * file path, handling both Windows and Unix path separators.</p>
     * 
     * @param fullPath the full path to the file (can be null)
     * @return the filename portion of the path, or null if input is null
     */
    public String getStoredFileName(String fullPath) {
        if (fullPath == null) return null;
        
        // Normalize path separators and find last separator
        int last = fullPath.replace('\\', '/').lastIndexOf('/');
        
        // Return filename (everything after last separator) or full path if no separator
        return last >= 0 ? fullPath.substring(last + 1) : fullPath;
    }

    /**
     * Extracts the file extension from a filename.
     * 
     * <p>This helper method extracts the extension (everything after the last dot)
     * from a filename. Returns empty string if no extension is found.</p>
     * 
     * @param filename the filename to extract extension from
     * @return the file extension (without the dot), or empty string if no extension
     */
    private String getExtension(String filename) {
        // Find last dot in filename
        int i = filename.lastIndexOf('.');
        
        // Return extension (everything after dot) or empty string if no dot found
        return i > 0 ? filename.substring(i + 1) : "";
    }
}
