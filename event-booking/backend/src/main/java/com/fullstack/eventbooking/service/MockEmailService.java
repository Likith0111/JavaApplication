package com.fullstack.eventbooking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Optional;

/**
 * Mock email service for development and testing environments.
 * 
 * <p>This service provides email functionality without actually sending emails.
 * Instead, it logs email content to the console and optionally writes to a log file.
 * This is useful for development, testing, and demonstration purposes where actual
 * email delivery is not required.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Console logging of email content</li>
 *   <li>File-based email logging for persistence</li>
 *   <li>Structured email formatting with timestamps</li>
 *   <li>Graceful error handling for file operations</li>
 *   <li>Booking confirmation email templates</li>
 * </ul>
 * 
 * <p><b>Usage:</b></p>
 * <p>In production environments, this service should be replaced with an actual
 * email service implementation (e.g., SendGrid, AWS SES, or SMTP server).</p>
 * 
 * <p><b>Log File:</b> Emails are logged to "mock-emails.log" in the application's
 * working directory. File write failures are logged as warnings but do not
 * interrupt the application flow.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@Slf4j
public class MockEmailService {

    /** Name of the log file where emails are persisted */
    private static final String LOG_FILE = "mock-emails.log";

    /**
     * Sends an email by logging it to console and optionally to a file.
     * 
     * <p>This method formats the email with a timestamp and logs it to both
     * the console (via SLF4J) and a log file. File write failures are handled
     * gracefully and logged as warnings.</p>
     * 
     * @param to the recipient email address
     * @param subject the email subject line
     * @param body the email body content
     */
    public void sendEmail(String to, String subject, String body) {
        // Format email message with timestamp
        String message = String.format("[%s] To: %s | Subject: %s | Body: %s%n",
                Instant.now(), to, subject, body);
        
        // Log to console via SLF4J
        log.info("MOCK EMAIL: To={}, Subject={}, Body={}", to, subject, body);
        
        // Attempt to write to log file (failures are non-critical)
        try {
            Path path = Path.of(System.getProperty("user.dir"), LOG_FILE);
            Files.writeString(path, message, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            // Log warning but don't throw - file logging is optional
            log.warn("Could not write mock email to file: {}", e.getMessage());
        }
    }

    /**
     * Sends a booking confirmation email to a user.
     * 
     * <p>This method generates a formatted booking confirmation email with
     * booking details including booking ID, event name, and number of seats.
     * The email is sent using the generic sendEmail method.</p>
     * 
     * @param to the recipient email address
     * @param userName the name of the user who made the booking
     * @param bookingId the unique booking identifier (e.g., "BK-2024-001234")
     * @param eventName the name of the event that was booked
     * @param seats the number of seats booked
     */
    public void sendBookingConfirmation(String to, String userName, String bookingId, String eventName, int seats) {
        // Generate email subject with booking ID
        String subject = "Booking Confirmation - " + bookingId;
        
        // Generate formatted email body with booking details
        String body = String.format("Hello %s,%n%nYour booking has been confirmed.%nBooking ID: %s%nEvent: %s%nSeats: %d%n%nThank you.",
                userName, bookingId, eventName, seats);
        
        // Send email via generic sendEmail method
        sendEmail(to, subject, body);
    }
}
